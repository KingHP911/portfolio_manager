package ru.kinghp.portfolio_manager.news;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.transaction.annotation.Transactional;
import ru.kinghp.portfolio_manager.dto.DBUserDto;
import ru.kinghp.portfolio_manager.dto.NewsDto;
import ru.kinghp.portfolio_manager.models.DBUser;
import ru.kinghp.portfolio_manager.models.News;
import ru.kinghp.portfolio_manager.repository.DBUserRepository;
import ru.kinghp.portfolio_manager.repository.NewsRepository;
import ru.kinghp.portfolio_manager.security.AuthRequest;
import ru.kinghp.portfolio_manager.security.AuthResponse;
import ru.kinghp.portfolio_manager.security.JWTService;
import ru.kinghp.portfolio_manager.service.NewsService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class NewsControllerTest {

//	@Autowired
//	private NewsRepository newsRepository;
//	@Autowired
//	private DBUserRepository userRepository;
	@Autowired
    private TestRestTemplate testRestTemplate;
	@Autowired
	private JWTService jwtService;

    @Test
	void allNewsesForAdmin() {

		AuthResponse authResponse = getAuthHeaderForUser("Admin", "1");
		assertTrue(jwtService.extractUsername(authResponse.getJwtToken()).equals("Admin"));

		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + authResponse.getJwtToken());

		ResponseEntity<List<NewsDto>> response =
				testRestTemplate.exchange("/news", HttpMethod.GET, new HttpEntity<>(headers), new ParameterizedTypeReference<List<NewsDto>>(){});

		assertTrue(response.getBody().size() == 1);

	}

	@Test
	void allNewsesForUser() {

		AuthResponse authResponse = getAuthHeaderForUser("User", "1");
		assertTrue(jwtService.extractUsername(authResponse.getJwtToken()).equals("User"));

		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + authResponse.getJwtToken());

		ResponseEntity<List<NewsDto>> response =
				testRestTemplate.exchange("/news", HttpMethod.GET, new HttpEntity<>(headers), new ParameterizedTypeReference<List<NewsDto>>(){});

		assertTrue(response.getBody().size() == 2);

	}

	@Test
	void createNewsByAdmin(){

		DBUserDto userDto = new DBUserDto();
		userDto.setId(Long.valueOf(8146));
		userDto.setLogin("Admin");

		NewsDto newsDto = new NewsDto();
		newsDto.setTitle("Title1");
		newsDto.setBriefDis("BriefDis1");
		newsDto.setFullDis("FullDis1");
		newsDto.setImgRef("ImgRef1");
		newsDto.setIsViewed(false);
		newsDto.setValidUntil(LocalDateTime.now().plusDays(1));
		newsDto.setFButtonAction("ButtonAction1");
		newsDto.setFButtonName("ButtonName1");
		newsDto.setDbUserDto(userDto);

		AuthResponse authResponse = getAuthHeaderForUser("Admin", "1");
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + authResponse.getJwtToken());

		ResponseEntity<NewsDto> response =
				testRestTemplate.exchange("/news/add", HttpMethod.POST, new HttpEntity<>(newsDto, headers), new ParameterizedTypeReference<NewsDto>(){});

		ResponseEntity<List<NewsDto>> responseAllNews =
				testRestTemplate.exchange("/news", HttpMethod.GET, new HttpEntity<>(headers), new ParameterizedTypeReference<List<NewsDto>>(){});

		assertTrue(responseAllNews.getBody().size() == 2);


	}


	private AuthResponse getAuthHeaderForUser(String name, String password) {
		AuthRequest authRequest = new AuthRequest();
		authRequest.setName(name);
		authRequest.setPassword(password);
		AuthResponse authResponse = testRestTemplate.postForObject("/authenticate", authRequest, AuthResponse.class);
		return authResponse;
	}


//	@BeforeEach
//	void deleteAllNews(){
//
//		newsRepository.deleteAll();
//
//		DBUser admin = userRepository.findByLogin("Admin");
//		DBUser user = userRepository.findByLogin("User");
//
//		List<News> newsList = new ArrayList<>();
//
//		News news1 = new News();
//		news1.setTitle("Title1");
//		news1.setBriefDis("BriefDis1");
//		news1.setFullDis("FullDis1");
//		news1.setImgRef("ImgRef1");
//		news1.setIsViewed(false);
//		news1.setValidUntil(LocalDateTime.now().minusDays(1));
//		news1.setFButtonAction("ButtonAction1");
//		news1.setFButtonName("ButtonName1");
//		news1.setDbUser(admin);
//		newsList.add(news1);
//
//		News news2 = new News();
//		news2.setTitle("Title2");
//		news2.setBriefDis("BriefDis2");
//		news2.setFullDis("FullDis2");
//		news2.setImgRef("ImgRef2");
//		news2.setIsViewed(false);
//		news2.setValidUntil(LocalDateTime.now().minusDays(1));
//		news2.setFButtonAction("ButtonAction2");
//		news2.setFButtonName("ButtonName2");
//		news2.setDbUser(admin);
//		newsList.add(news2);
//
//		News news3 = new News();
//		news3.setTitle("Title3");
//		news3.setBriefDis("BriefDis3");
//		news3.setFullDis("FullDis3");
//		news3.setImgRef("ImgRef3");
//		news3.setIsViewed(false);
//		news3.setValidUntil(LocalDateTime.now().plusDays(1));
//		news3.setFButtonAction("ButtonAction3");
//		news3.setFButtonName("ButtonName3");
//		news3.setDbUser(admin);
//		newsList.add(news3);
//
//		News news4 = new News();
//		news4.setTitle("Title4");
//		news4.setBriefDis("BriefDis4");
//		news4.setFullDis("FullDis4");
//		news4.setImgRef("ImgRef4");
//		news4.setIsViewed(false);
//		news4.setValidUntil(LocalDateTime.now().plusDays(1));
//		news4.setFButtonAction("ButtonAction4");
//		news4.setFButtonName("ButtonName4");
//		news4.setDbUser(admin);
//		newsList.add(news4);
//
//		News news5 = new News();
//		news5.setTitle("Title5");
//		news5.setBriefDis("BriefDis5");
//		news5.setFullDis("FullDis5");
//		news5.setImgRef("ImgRef5");
//		news5.setIsViewed(false);
//		news5.setValidUntil(LocalDateTime.now().plusDays(1));
//		news5.setFButtonAction("ButtonAction5");
//		news5.setFButtonName("ButtonName5");
//		news5.setDbUser(user);
//		newsList.add(news5);
//
//		newsRepository.saveAll(newsList);
//	}

}
