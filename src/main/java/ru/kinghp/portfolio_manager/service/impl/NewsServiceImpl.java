package ru.kinghp.portfolio_manager.service.impl;

import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kinghp.portfolio_manager.models.News;
import ru.kinghp.portfolio_manager.models.Paper;
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

@Data
@Service
@Transactional
public class NewsServiceImpl implements NewsService {

    private final NewsRepository newsRepository;

    @Transactional(readOnly = true)
    @Override
    public List<News> findAllPagingIsViewed(Boolean isViewed, Pageable pageable){
        Page page = newsRepository.findAll(new Specification<News>() {
            @Override
            public Predicate toPredicate(Root<News> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if (isViewed){
                    predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("isViewed"), true)));
                } else if (!isViewed){
                    predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("isViewed"), false)));
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        }, pageable);
        return page.getContent();
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
    @Override
    public Iterable<News> findAll() {
        return newsRepository.findAll();
    }

    @Transactional
    @Override
    public News add(News news) {
        return newsRepository.save(news);
    }

    @Transactional(readOnly = true)
    @Override
    public boolean existsById(@NotNull Long id) {
        return newsRepository.existsById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<News> findById(@NotNull Long id) {
        return newsRepository.findById(id);
    }

    @Transactional
    @Override
    public void deleteById(@NotNull Long id) {
        newsRepository.deleteById(id);
    }

    @Transactional
    @Override
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

        return newsRepository.save(editingNews);
    }

    @Transactional
    @Override
    public News openById(Long id){
        News openingNews = newsRepository.findById(id).orElseThrow();
        openingNews.setIsViewed(true);
        return update(openingNews);
    }

    @Transactional
    @Override
    public News markAsNotViewedById(Long id){
        News processingNews = newsRepository.findById(id).orElseThrow();
        processingNews.setIsViewed(false);
        //todo смещаю границу актуальности на день
        processingNews.setValidUntil(LocalDateTime.now().plusDays(1));
        return update(processingNews);
    }

    @Transactional(readOnly = true)
    @Override
    public String processActonButtonById(Long id){

        News processingNews = newsRepository.findById(id).orElseThrow();
        try {
            Method method = this.getClass().getMethod(processingNews.getFButtonAction(), new Class[]{});
            String result = (String) method.invoke(this, new Object[]{});
            return result;
        } catch (NoSuchMethodException e) {
            return "index"; //todo переписать для фронта, возвращать ошибку нахождения метода
        } catch (IllegalAccessException e) {
            return "index"; //todo переписать для фронта, возвращать ошибку нахождения метода
        } catch (InvocationTargetException e) {
            return "index"; //todo переписать для фронта, возвращать ошибку нахождения метода
        }

    }

    public String defaultAction(){
        return "portfolio/portfolios";
    }

}
