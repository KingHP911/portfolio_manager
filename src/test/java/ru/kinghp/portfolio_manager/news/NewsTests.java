package ru.kinghp.portfolio_manager.news;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import ru.kinghp.portfolio_manager.models.News;
import ru.kinghp.portfolio_manager.repository.NewsRepository;
import ru.kinghp.portfolio_manager.service.NewsService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;

@Transactional
@SpringBootTest
public class NewsTests {

    @Autowired
    private NewsService newsService;

    @Autowired
    private NewsRepository newsRepository;

//    @Test
//    void allNews(){
//        List<News> all = new ArrayList<>();
//        newsService.findAll().forEach(o -> all.add(o));
//        assertEquals(all.size(), 5);
//    }
//
//    @Test
//    void addNews(){
//        News news = new News();
//        news.setId(Long.valueOf(6));
//        news.setTitle("Title");
//        news.setBriefDis("BriefDis");
//        news.setFullDis("FullDis");
//        news.setImgRef("ImgRef");
//        news.setIsViewed(false);
//        news.setValidUntil(LocalDateTime.now().minusDays(1));
//        news.setFButtonAction("ButtonAction");
//        news.setFButtonName("ButtonName");
//
//        newsService.add(news);
//
//        List<News> all = new ArrayList<>();
//        newsService.findAll().forEach(o -> all.add(news));
//        assertEquals(all.size(), 6);
//    }
//
//    @Test
//    void newsRepositorySearch(){
//
////        List<News> newsList = newsService.findAllNotViewed();
////        assertEquals(newsList.size(), 2);
////
////        newsService.openById(newsList.get(0).getId());
////        newsService.openById(newsList.get(1).getId());
////
////        newsList = newsService.findAllNotViewed();
////        assertEquals(newsList.size(), 1);
//    }
//
//    @Test
//    void openById_markAsNotViewedById(){
////        News news = newsService.openById(newsService.findAllPagingIsViewed(false, PageRequest.of(0,1)).get(0).getId());
////        assertEquals(news.getIsViewed(), true);
////
////        news = newsService.markAsNotViewedById(newsService.findAllPagingIsViewed(true, PageRequest.of(0,1)).get(0).getId());
////        assertEquals(news.getIsViewed(), false);
////        assertEquals(news.getValidUntil().getDayOfYear(), LocalDateTime.now().plusDays(1).getDayOfYear());
//    }
//
//    @Test
//    void processActonButtonById(){
//
//        News news = new News();
//        news.setTitle("ActonButton");
//        news.setBriefDis("ActonButton");
//        news.setFullDis("ActonButton");
//        news.setImgRef("ActonButton");
//        news.setIsViewed(false);
//        news.setValidUntil(LocalDateTime.now().plusDays(1));
//        news.setFButtonAction("defaultAction");
//        news.setFButtonName("ButtonName");
////        News newsInDB = newsService.add(news);
//
//        String goodResult = newsService.processActonButtonById(newsInDB.getId());
//        assertEquals(goodResult, "portfolio/portfolios");
//
//        newsInDB.setFButtonAction("unknownAction");
//        newsInDB = newsService.update(newsInDB);
//
//        String badResult = newsService.processActonButtonById(newsInDB.getId());
//        assertEquals(badResult, "index");
//
//    }
//
//    @BeforeEach
//    void deleteAllNews(){
//
//        newsRepository.deleteAll();
//
//        List<News> newsList = new ArrayList<>();
//
//        News news1 = new News();
//        news1.setId(Long.valueOf(1));
//        news1.setTitle("Title1");
//        news1.setBriefDis("BriefDis1");
//        news1.setFullDis("FullDis1");
//        news1.setImgRef("ImgRef1");
//        news1.setIsViewed(false);
//        news1.setValidUntil(LocalDateTime.now().minusDays(1));
//        news1.setFButtonAction("ButtonAction1");
//        news1.setFButtonName("ButtonName1");
//        newsList.add(news1);
//
//        News news2 = new News();
//        news2.setId(Long.valueOf(2));
//        news2.setTitle("Title2");
//        news2.setBriefDis("BriefDis2");
//        news2.setFullDis("FullDis2");
//        news2.setImgRef("ImgRef2");
//        news2.setIsViewed(false);
//        news2.setValidUntil(LocalDateTime.now().minusDays(1));
//        news2.setFButtonAction("ButtonAction2");
//        news2.setFButtonName("ButtonName2");
//        newsList.add(news2);
//
//        News news3 = new News();
//        news3.setId(Long.valueOf(3));
//        news3.setTitle("Title3");
//        news3.setBriefDis("BriefDis3");
//        news3.setFullDis("FullDis3");
//        news3.setImgRef("ImgRef3");
//        news3.setIsViewed(false);
//        news3.setValidUntil(LocalDateTime.now().plusDays(1));
//        news3.setFButtonAction("ButtonAction3");
//        news3.setFButtonName("ButtonName3");
//        newsList.add(news3);
//
//        News news4 = new News();
//        news4.setId(Long.valueOf(4));
//        news4.setTitle("Title4");
//        news4.setBriefDis("BriefDis4");
//        news4.setFullDis("FullDis4");
//        news4.setImgRef("ImgRef4");
//        news4.setIsViewed(false);
//        news4.setValidUntil(LocalDateTime.now().plusDays(1));
//        news4.setFButtonAction("ButtonAction4");
//        news4.setFButtonName("ButtonName4");
//        newsList.add(news4);
//
//        News news5 = new News();
//        news5.setId(Long.valueOf(5));
//        news5.setTitle("Title5");
//        news5.setBriefDis("BriefDis5");
//        news5.setFullDis("FullDis5");
//        news5.setImgRef("ImgRef5");
//        news5.setIsViewed(false);
//        news5.setValidUntil(LocalDateTime.now().plusDays(1));
//        news5.setFButtonAction("ButtonAction5");
//        news5.setFButtonName("ButtonName5");
//        newsList.add(news5);
//
//        newsRepository.saveAll(newsList);
//    }
}
