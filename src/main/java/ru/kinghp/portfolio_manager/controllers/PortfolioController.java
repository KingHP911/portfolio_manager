package ru.kinghp.portfolio_manager.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kinghp.portfolio_manager.models.CurrencyOfPaper;
import ru.kinghp.portfolio_manager.models.Paper;
import ru.kinghp.portfolio_manager.models.TypesOfPaper;
import ru.kinghp.portfolio_manager.repos.PaperRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
public class PortfolioController {

    @Autowired
    private PaperRepository paperRepository;

    @GetMapping("/")
    public String users (Model model){

        return "index";
    }

    @GetMapping("/papers")
    public String papers (Model model){
        Iterable<Paper> papers = paperRepository.findAll();
        model.addAttribute("papers", papers);
        return "papers";
    }

    @GetMapping("/paper/add")
    public String paperAdd (Model model){
        model.addAttribute("types", TypesOfPaper.values());
        model.addAttribute("currency", CurrencyOfPaper.values());
        return "paper-add";
    }

    @PostMapping("/paper/add")
    public String paperPostAdd (@RequestParam String name, @RequestParam String ticker,
                                @RequestParam TypesOfPaper type, @RequestParam CurrencyOfPaper currency,
                                @RequestParam BigDecimal purchasePrice, @RequestParam String purchaseDateStr,
                                Model model){


        LocalDateTime purchaseDate = LocalDateTime.parse(purchaseDateStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        Paper paper = new Paper(name, ticker, type, currency, purchasePrice, purchaseDate);
        paperRepository.save(paper);
        return "redirect:/papers";
    }

    @GetMapping("/paper/{id}")
    public String paper(@PathVariable("id") Long id, Model model){
        //todo
        return "index";
    }

}
