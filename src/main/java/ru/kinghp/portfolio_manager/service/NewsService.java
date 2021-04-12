package ru.kinghp.portfolio_manager.service;

import org.springframework.http.ResponseEntity;
import ru.kinghp.portfolio_manager.dto.NewsDto;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface NewsService {

    ResponseEntity<List<NewsDto>> allNotViewedNewses();

    ResponseEntity<NewsDto> createNews(NewsDto newsDto);

    ResponseEntity<NewsDto> openAuthUserNews(@NotNull Long id);

    ResponseEntity<NewsDto> markNewsAsNotViewed(@NotNull Long id);

    ResponseEntity<String> processActonButtonById(@NotNull Long id);


}
