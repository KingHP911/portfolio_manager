package ru.kinghp.portfolio_manager.controllers;

import lombok.Data;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kinghp.portfolio_manager.dto.DBUserDto;
import ru.kinghp.portfolio_manager.models.DBUser;
import ru.kinghp.portfolio_manager.models.News;
import ru.kinghp.portfolio_manager.dto.NewsDto;
import ru.kinghp.portfolio_manager.service.NewsService;


import java.util.List;
import java.util.stream.Collectors;

@Data
@RestController
public class NewsRestController {

    private final NewsService newsService;

    @GetMapping("/news")
    public ResponseEntity<List<NewsDto>> allNewses() {
       return newsService.allNotViewedNewses();
    }

    @PostMapping("/news/add")
    public ResponseEntity<NewsDto> createNews(@RequestBody NewsDto newsDto){
      return newsService.createNews(newsDto);
    }

    @GetMapping("/News/{id}")
    public ResponseEntity<NewsDto> openAuthUserNews(@PathVariable("id") Long id){
        return newsService.openAuthUserNews(id);
    }

    @PostMapping("/news/{id}")
    public ResponseEntity<NewsDto> markNewsAsNotViewed(@PathVariable("id") Long id){
        return newsService.markNewsAsNotViewed(id);
    }

    @GetMapping("/news/{id}/action")
    public ResponseEntity<String> newsActionButton(@PathVariable("id") Long id){
        return newsService.processActonButtonById(id);
    }
}
