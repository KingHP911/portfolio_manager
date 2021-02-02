package ru.kinghp.portfolio_manager.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.kinghp.portfolio_manager.dao.PortfolioRepository;
import ru.kinghp.portfolio_manager.dao.PortfoliosPaperRepository;
import ru.kinghp.portfolio_manager.models.*;
import ru.kinghp.portfolio_manager.dao.PaperRepository;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Controller
public class PortfolioController {

    @Autowired
    private PaperRepository paperRepository;
    @Autowired
    private PortfolioRepository portfolioRepository;
    @Autowired
    private PortfoliosPaperRepository portfoliosPaperRepository;
    @Autowired
    private FinnHubData hubData;

    @GetMapping("/")
    public String users (Model model){
        return "index";
    }

    @GetMapping("/papers")
    public String papers (Model model){
        Iterable<Paper> papers = paperRepository.findAll();
        //todo переписать hubData.getCurrentPrice(paper.getTicker()) на пакетное получение, тек реализация очень медлено получает тек цены
        model.addAttribute("papers", papers);
        return "paper/papers";
    }

    @GetMapping("/paper/add")
    public String paperAdd (@ModelAttribute("paper") Paper paper, Model model){
        //model.addAttribute("paper", new Paper()); эквиваленитно @ModelAttribute("paper") Paper paper с model в аргументе newPaper()
        model.addAttribute("types", PaperTypes.values());
        model.addAttribute("currency", PaperCurrency.values());
        return "paper/paper-add";
    }

    @PostMapping("/paper/add")
    public String paperPostAdd (@ModelAttribute("paper") @Valid Paper paper,
                                BindingResult bindingResult, Model model){

        //@Valid проверяет корректность значений
        //BindingResult bindingResult ошибка валидации. Должен быть после валидир-го объекта

        //todo получать тек цену?
        if (bindingResult.hasErrors()){
            model.addAttribute("types", PaperTypes.values());
            model.addAttribute("currency", PaperCurrency.values());
            return "paper/paper-add";
        }

        //todo не добавлять бумаги с уже имеющимися тикерами

        paper.setCurrentPrice(new BigDecimal(0));
        paperRepository.save(paper);
        return "redirect:/papers";
    }

    @GetMapping("/paper/{id}")
    public String paper(@PathVariable("id") Long id, Model model){

        if (!paperRepository.existsById(id)){
            return "redirect:/papers";
        }

        Optional<Paper> paper = paperRepository.findById(id);
        //todo получать тек цены
        model.addAttribute("paper", paper.get());
        return "paper/paper-details";
    }

    @GetMapping("/paper/{id}/edit")
    public String paperEdit(@ModelAttribute("paper") Paper paper, Model model){

        Long id = paper.getId();

        if (!paperRepository.existsById(id)){
            return "redirect:/papers";
        }

        Optional<Paper> paperDB = paperRepository.findById(id);
        model.addAttribute("paper", paperDB.get());
        model.addAttribute("types", PaperTypes.values());
        model.addAttribute("currency", PaperCurrency.values());
        return "paper/paper-edit";
    }

    @PostMapping("/paper/{id}/edit")
    public String paperPostUpdate (@ModelAttribute("paper") @Valid Paper paper,
                                   BindingResult bindingResult, Model model){

        if (bindingResult.hasErrors()){
            model.addAttribute("paper", paper);
            model.addAttribute("types", PaperTypes.values());
            model.addAttribute("currency", PaperCurrency.values());
            return "paper/paper-edit";
        }

        Paper editingPaper = paperRepository.findById(paper.getId()).orElseThrow();
        editingPaper.setName(paper.getName());
        editingPaper.setTicker(paper.getTicker());
        editingPaper.setType(paper.getType());
        editingPaper.setCurrency(paper.getCurrency());

        paperRepository.save(editingPaper);
        return "redirect:/papers";
    }

    @PostMapping("/paper/{id}/remove")
    public String paperPostDelete (@PathVariable("id") Long id, Model model){

        Paper paper = paperRepository.findById(id).orElseThrow();
        try {
            paperRepository.delete(paper);
        }catch (Exception e){
            //todo отправить пользователю сообщение о не возможности удаления
        }

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

        //todo валидация форм
        if (!portfolioRepository.existsById(id)){
            return "redirect:/portfolios";
        }

        Portfolio portfolio = portfolioRepository.findById(id).get();
        model.addAttribute("portfolio", portfolio);

        List<PortfoliosPaper> papersInPort = portfolio.getPapers();
        model.addAttribute("papers", papersInPort);

        Iterable<Paper> allPapers = paperRepository.findAll();
        model.addAttribute("allPapers", allPapers);

        return "portfolio/portfolio-details";
    }

    @PostMapping("/portfolio/{id}/addPaper")
    public String portfolioPostAddPaper (@PathVariable("id") Long id, @Validated Long addingPaperId,
                                         @RequestParam Integer vol, Model model){

        if (!paperRepository.existsById(addingPaperId)){
            return "redirect:/portfolio/{id}";
        }

        if (!portfolioRepository.existsById(id)){
            return "redirect:/portfolios";
        }

        Paper paper = paperRepository.findById(addingPaperId).get();
        Portfolio portfolio = portfolioRepository.findById(id).get();
        PortfoliosPaper portfoliosPaper = new PortfoliosPaper(paper, new BigDecimal(0), LocalDateTime.now(), vol);
        portfoliosPaper.setPurchasePrice(hubData.getCurrentPrice(paper.getTicker()));
        portfolio.addPaper(portfoliosPaper);

        portfolioRepository.save(portfolio);

        return "redirect:/portfolio/{id}";
    }

    @PostMapping("/portfolio/{id}/removePaper")
    public String portfolioPostRemovePaper (@PathVariable("id") Long id, @Validated Long removingPaperId, Model model){

        if (!portfoliosPaperRepository.existsById(removingPaperId)){
            return "redirect:/portfolio/{id}";
        }

        if (!portfolioRepository.existsById(id)){
            return "redirect:/portfolios";
        }

        PortfoliosPaper paper = portfoliosPaperRepository.findById(removingPaperId).get();
        Portfolio portfolio = portfolioRepository.findById(id).orElseThrow();

        portfolio.removePaper(paper);

        portfolioRepository.save(portfolio);
        return "redirect:/portfolio/{id}";
    }

}
