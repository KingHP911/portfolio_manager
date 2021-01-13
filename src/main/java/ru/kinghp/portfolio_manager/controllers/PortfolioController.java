package ru.kinghp.portfolio_manager.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kinghp.portfolio_manager.dao.PortfolioRepository;
import ru.kinghp.portfolio_manager.models.CurrencyOfPaper;
import ru.kinghp.portfolio_manager.models.Paper;
import ru.kinghp.portfolio_manager.models.Portfolio;
import ru.kinghp.portfolio_manager.models.TypesOfPaper;
import ru.kinghp.portfolio_manager.dao.PaperRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;

@Controller
public class PortfolioController {

    @Autowired
    private PaperRepository paperRepository;

    @Autowired
    private PortfolioRepository portfolioRepository;

    @GetMapping("/")
    public String users (Model model){

        return "index";
    }

    @GetMapping("/papers")
    public String papers (Model model){
        Iterable<Paper> papers = paperRepository.findAll();
        model.addAttribute("papers", papers);
        return "paper/papers";
    }

    @GetMapping("/paper/add")
    public String paperAdd (Model model){
        model.addAttribute("types", TypesOfPaper.values());
        model.addAttribute("currency", CurrencyOfPaper.values());
        return "paper/paper-add";
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
        return "paper/paper-details";
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
        return "paper/paper-edit";
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


    @GetMapping("/portfolios")
    public String portfolios (Model model){
        Iterable<Portfolio> portfolios = portfolioRepository.findAll();
        model.addAttribute("portfolios", portfolios);
        return "portfolio/portfolios";
    }

    @GetMapping("/portfolio/add")
    public String portfolioAdd (Model model){
        return "portfolio/portfolio-add";
    }

    @PostMapping("/portfolio/add")
    public String portfolioPostAdd (@RequestParam String name, Model model){

        Portfolio portfolio = new Portfolio(name);
        portfolioRepository.save(portfolio);
        return "redirect:/portfolios";
    }

    @GetMapping("/portfolio/{id}")
    public String portfolio(@PathVariable("id") Long id, Model model){

        if (!portfolioRepository.existsById(id)){
            return "redirect:/portfolios";
        }

        Optional<Portfolio> portfolio = portfolioRepository.findById(id);
        ArrayList<Portfolio> res = new ArrayList<>();
        portfolio.ifPresent(res::add);
        if (res.isEmpty()){
            return "redirect:/portfolios";
        }
        model.addAttribute("portfolio", res.get(0));
        model.addAttribute("papers", res.get(0).getPapers());

        Iterable<Paper> allPapers = paperRepository.findAll();
        model.addAttribute("allPapers", allPapers);

        return "portfolio/portfolio-details";
    }

    @PostMapping("/portfolio/{id}/addpapaer")
    public String portfolioPostAddPaper (@PathVariable("id") Long id, @Validated Long addingPaperId, Model model){

        if (!paperRepository.existsById(addingPaperId)){
            return portfolio(id, model);
        }

        if (!portfolioRepository.existsById(id)){
            return "redirect:/portfolios";
        }

        Paper paper = paperRepository.findById(addingPaperId).orElseThrow();
        Portfolio portfolio = portfolioRepository.findById(id).orElseThrow();
        paper.addPortfolio(portfolio);

        portfolioRepository.save(portfolio);
        paperRepository.save(paper);

        return portfolio(id, model);
    }

}
