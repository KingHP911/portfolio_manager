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
import java.util.ArrayList;
import java.util.Optional;

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


        LocalDateTime purchaseDate = LocalDateTime.now();
        try {
            purchaseDate = LocalDateTime.parse(purchaseDateStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        }catch (Exception e){

        }
        Paper paper = new Paper(name, ticker, type, currency, purchasePrice, purchaseDate);
        paperRepository.save(paper);
        return "redirect:/papers";
    }

    @GetMapping("/paper/{id}")
    public String paper(@PathVariable("id") Long id, Model model){

        if (!paperRepository.existsById(id)){
            return "redirect:/papers";
        }

        Optional<Paper> paper = paperRepository.findById(id);
        ArrayList<Paper> res = new ArrayList<>();
        paper.ifPresent(res::add);
        model.addAttribute("paper", res);
        return "paper-details";
    }

    @GetMapping("/paper/{id}/edit")
    public String paperEdit(@PathVariable("id") Long id, Model model){

        if (!paperRepository.existsById(id)){
            return "redirect:/papers";
        }

        Optional<Paper> paper = paperRepository.findById(id);
        ArrayList<Paper> res = new ArrayList<>();
        paper.ifPresent(res::add);
        model.addAttribute("paper", res);
        model.addAttribute("types", TypesOfPaper.values());
        model.addAttribute("currency", CurrencyOfPaper.values());
        return "paper-edit";
    }

    @PostMapping("/paper/{id}/edit")
    public String paperPostUpdate (@RequestParam String name, @RequestParam String ticker,
                                @RequestParam TypesOfPaper type, @RequestParam CurrencyOfPaper currency,
                                @RequestParam BigDecimal purchasePrice, @RequestParam String purchaseDateStr,
                                   @PathVariable("id") Long id, Model model){


        LocalDateTime purchaseDate = LocalDateTime.now();
        try {
            purchaseDate = LocalDateTime.parse(purchaseDateStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        }catch (Exception e){

        }

        Paper paper = paperRepository.findById(id).orElseThrow();
        paper.setName(name);
        paper.setTicker(ticker);
        paper.setType(type);
        paper.setCurrency(currency);
        paper.setPurchasePrice(purchasePrice);
        paper.setPurchaseDate(purchaseDate);

        paperRepository.save(paper);
        return "redirect:/papers";
    }

    @PostMapping("/paper/{id}/remove")
    public String paperPostDelete (@PathVariable("id") Long id, Model model){

        Paper paper = paperRepository.findById(id).orElseThrow();
        paperRepository.delete(paper);

        return "redirect:/papers";
    }

}
