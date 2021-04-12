package ru.kinghp.portfolio_manager.service.impl;

import lombok.Data;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.kinghp.portfolio_manager.dto.DBUserDto;
import ru.kinghp.portfolio_manager.dto.NewsDto;
import ru.kinghp.portfolio_manager.models.DBUser;
import ru.kinghp.portfolio_manager.models.News;
import ru.kinghp.portfolio_manager.models.Paper;
import ru.kinghp.portfolio_manager.repository.DBUserRepository;
import ru.kinghp.portfolio_manager.repository.NewsRepository;
import ru.kinghp.portfolio_manager.service.NewsService;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.constraints.NotNull;
import org.springframework.data.domain.Pageable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
@Service
@Transactional
public class NewsServiceImpl implements NewsService {

    private final NewsRepository newsRepository;
    private final DBUserRepository userRepository;
    private final ModelMapper modelMapper;


    @Transactional(readOnly = true)
    @Override
    public ResponseEntity<List<NewsDto>> allNotViewedNewses() {
        List<News> newses = findAllNotViewed();
        List<NewsDto> newsDtos = newses.stream().map(this::convertNewsToDto).collect(Collectors.toList());
        return ResponseEntity.ok().body(newsDtos);
    }

    @Transactional
    @Override
    public ResponseEntity<NewsDto> createNews (NewsDto newsDto){

        if (!userRepository.existsById(newsDto.getDbUserDto().getId())){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        if (!getAuthUser().getIsAdmin()){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "User can not creat newses");
        }

        News news = modelMapper.map(newsDto, News.class);
        DBUser user = userRepository.findByLogin(newsDto.getDbUserDto().getLogin());
        news.setDbUser(user);
        News createdNews = newsRepository.save(news);

        return ResponseEntity.ok().body(convertNewsToDto(createdNews));
    }

    @Transactional
    @Override
    public ResponseEntity<NewsDto> openAuthUserNews(Long id){

        DBUser user = getAuthUser();
        if (!newsRepository.existsByIdAndDbUser(id, user)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "News not found");
        }

        News news = openById(id);
        return ResponseEntity.ok().body(convertNewsToDto(news));
    }

    @Transactional
    @Override
    public ResponseEntity<NewsDto> markNewsAsNotViewed(Long id){

        if (!newsRepository.existsById(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "News not found");
        }

        if (!getAuthUser().getIsAdmin()){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "User can not change newses");
        }

        News processingNews = newsRepository.findById(id).get();
        processingNews.setIsViewed(false);
        //todo смещаю границу актуальности на день
        processingNews.setValidUntil(LocalDateTime.now().plusDays(1));
        update(processingNews);

        return ResponseEntity.ok().body(convertNewsToDto(processingNews));
    }


    private NewsDto convertNewsToDto(News news){
        NewsDto newsDto = modelMapper.map(news, NewsDto.class);
        newsDto.setDbUserDto(modelMapper.map(news.getDbUser(), DBUserDto.class));
        return newsDto;
    }

    private DBUser getAuthUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        DBUser user = userRepository.findByLogin(auth.getName());
        return user;
    }


    @Transactional
    public List<News> findAllNotViewed(){
        processNotViewedNews();
        int countNews = 2; //todo в константы базы
        return findAllPagingIsViewed(false, PageRequest.of(0,countNews));
    }

    @Transactional
    @Scheduled(fixedDelayString = "3600000") //повторяем раз в 5 мин
    public Iterable<News> processNotViewedNews(){

        List<News> newsForProcessing = newsRepository.findAllByIsViewedIsFalse();
        for (News i : newsForProcessing){
            if (i.getValidUntil().isBefore(LocalDateTime.now())){
                i.setIsViewed(true);
            }
        }
        return newsRepository.saveAll(newsForProcessing);
    }

    @Transactional(readOnly = true)
    public List<News> findAllPagingIsViewed(Boolean isViewed, Pageable pageable){

        DBUser user = getAuthUser();

        Page page = newsRepository.findAll(new Specification<News>() {
            @Override
            public Predicate toPredicate(Root<News> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if (isViewed){
                    predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("isViewed"), true)));
                } else if (!isViewed){
                    predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("isViewed"), false)));
                }

                predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("dbUser"), user.getId())));

                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        }, pageable);
        return page.getContent();
    }

    @Transactional
    public News update(News news) {

        News editingNews = newsRepository.findById(news.getId()).orElseThrow();

        editingNews.setTitle(news.getTitle());
        editingNews.setBriefDis(news.getBriefDis());
        editingNews.setFullDis(news.getFullDis());
        editingNews.setImgRef(news.getImgRef());
        editingNews.setIsViewed(news.getIsViewed());
        editingNews.setValidUntil(news.getValidUntil());
        editingNews.setFButtonAction(news.getFButtonAction());
        editingNews.setFButtonName(news.getFButtonName());
        editingNews.setDbUser(news.getDbUser());

        return newsRepository.save(editingNews);
    }

    @Transactional
    public News openById(Long id){
        News openingNews = newsRepository.findById(id).orElseThrow();
        openingNews.setIsViewed(true);
        return update(openingNews);
    }

    @Transactional(readOnly = true)
    @Override
    public ResponseEntity<String> processActonButtonById(Long id){

        News processingNews = newsRepository.findById(id).orElseThrow();
        String result;

        try {
            Method method = this.getClass().getMethod(processingNews.getFButtonAction(), new Class[]{});
            result = (String) method.invoke(this, new Object[]{});
        } catch (NoSuchMethodException e) {
            result = "NoSuchMethodException";
        } catch (IllegalAccessException e) {
            result = "IllegalAccessException";
        } catch (InvocationTargetException e) {
            result = "InvocationTargetException";
        }
        return ResponseEntity.ok().body(result);

    }

    public String defaultAction(){
        return "Method completed";
    }

}
