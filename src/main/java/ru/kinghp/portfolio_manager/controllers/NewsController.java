package ru.kinghp.portfolio_manager.controllers;

import lombok.Data;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.kinghp.portfolio_manager.models.*;
import ru.kinghp.portfolio_manager.service.NewsService;

import javax.validation.Valid;

@Data
@Controller
public class NewsController {

    private final NewsService newsService;

    //todo фронт для news везде заглушки return index
    @GetMapping("/news")
    public String allNews(Model model) {
        model.addAttribute("news", newsService.findAllNotViewed());
        return "index";
    }

    @GetMapping("/news/add")
    public String newsAdd (@ModelAttribute("news") News news, Model model){
        return "index";
    }

    @PostMapping("/news/add")
    public String newsPostAdd (@ModelAttribute("news") @Valid News news,
                                BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors()){
            return "index";
        }
        newsService.add(news);
        return "redirect:";
    }

    @GetMapping("/news/{id}")
    public String newsDetails(@PathVariable("id") Long id, Model model){

        if (!newsService.existsById(id)){
            return "redirect:";
        }
        model.addAttribute("news", newsService.openById(id));
        return "index";
    }

    @PostMapping("/news/{id}")
    public String newsMarkAsNotViewed(@PathVariable("id") Long id, Model model){

        if (!newsService.existsById(id)){
            return "redirect:";
        }
        model.addAttribute("news", newsService.markAsNotViewedById(id));
        return "index";
    }

    @GetMapping("/news/{id}/action")
    public String newsActionButton(@PathVariable("id") Long id, Model model){

        if (!newsService.existsById(id)){
            return "redirect:";
        }
        return newsService.processActonButtonById(id);
    }

}
