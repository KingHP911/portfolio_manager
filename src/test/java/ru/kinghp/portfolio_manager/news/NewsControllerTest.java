package ru.kinghp.portfolio_manager.news;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import ru.kinghp.portfolio_manager.service.NewsService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class NewsControllerTest {

    @Autowired
    private NewsService newsService;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
	void checkAuth() {

		String cookie = getCookieForUser("Admin", "1", "/login");

        String securedUrl = "/portfolios";
		HttpHeaders headers = new HttpHeaders();
		headers.add("Cookie", cookie);

		ResponseEntity<String> responseFromSecuredEndPoint = testRestTemplate.exchange(
		        securedUrl,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                String.class);
		assertEquals(responseFromSecuredEndPoint.getStatusCode(), HttpStatus.OK);
		assertTrue(responseFromSecuredEndPoint.getBody().equals("Admin"));


	}

    private String getCookieForUser(String username, String password, String loginUrl)   {

		MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
		form.set("username", username);
		form.set("password", password);
		ResponseEntity<String> loginResponse = testRestTemplate.postForEntity(
				loginUrl,
				new HttpEntity<>(form, new HttpHeaders()),
				String.class);
		String cookie = loginResponse.getHeaders().get("Set-Cookie").get(0);
		return cookie;

	}

}
