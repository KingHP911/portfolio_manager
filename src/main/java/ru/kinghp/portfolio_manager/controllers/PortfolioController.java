package ru.kinghp.portfolio_manager.controllers;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.kinghp.portfolio_manager.models.*;
import ru.kinghp.portfolio_manager.service.impl.DBUserServiceImpl;
import ru.kinghp.portfolio_manager.service.impl.PaperServiceImpl;
import ru.kinghp.portfolio_manager.service.impl.PortfolioServiceImpl;
import ru.kinghp.portfolio_manager.service.impl.PortfoliosPaperServiceImpl;

import javax.validation.Valid;
import java.util.*;

@Data
@Controller
public class PortfolioController {

    private final PaperServiceImpl paperService;
    private final PortfolioServiceImpl portfolioService;
    private final PortfoliosPaperServiceImpl portfoliosPaperService;
    private final DBUserServiceImpl userService;


    @GetMapping("/login")
    public String login(@ModelAttribute("user") DBUser user, Model model) {
        return "login";
    }

    @GetMapping("/registration")
    public String registration(@ModelAttribute("user") DBUser user, Model model) {
        return "registration";
    }

    @PostMapping("/registration")
    public String registrationPost (@ModelAttribute("user") @Valid DBUser user,
                                BindingResult bindingResult, Model model){

        if (bindingResult.hasErrors()){
            return "registration";
        }
        //todo реализовать корректный фронт
        if (!user.getPassword().equals(user.getPasswordConfirm())){
            model.addAttribute("passwordConfirmError", true);
            return "registration";
        }

        userService.add(user);
        return "redirect:/login";
    }

    @GetMapping("/papers")
    public String papers (Model model){
        model.addAttribute("papers", paperService.findAll());
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
        if (bindingResult.hasErrors()){
            model.addAttribute("types", PaperTypes.values());
            model.addAttribute("currency", PaperCurrency.values());
            return "paper/paper-add";
        }

        paperService.add(paper);
        return "redirect:/papers";
    }

    @GetMapping("/paper/{id}")
    public String paper(@PathVariable("id") Long id, Model model){

        if (!paperService.existsById(id)){
            return "redirect:/papers";
        }
        model.addAttribute("paper", paperService.findById(id).get());
        return "paper/paper-details";
    }

    @GetMapping("/paper/{id}/edit")
    public String paperEdit(@ModelAttribute("paper") Paper paper, Model model){

        Long id = paper.getId();

        if (!paperService.existsById(id)){
            return "redirect:/papers";
        }

        model.addAttribute("paper", paperService.findById(id).get());

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

        paperService.update(paper);
        return "redirect:/papers";
    }

    @PostMapping("/paper/{id}/remove")
    public String paperPostDelete (@PathVariable("id") Long id, Model model){
        paperService.deleteById(id);
        return "redirect:/papers";
    }


    @GetMapping("/portfolios")
    public String portfolios (Model model){
        model.addAttribute("portfolios", portfolioService.findAll());
        return "portfolio/portfolios";
    }

    @GetMapping("/portfolio/add")
    public String portfolioAdd (@ModelAttribute("portfolio") Portfolio portfolio, Model model){
        return "portfolio/portfolio-add";
    }

    @PostMapping("/portfolio/add")
    public String portfolioPostAdd (@ModelAttribute("portfolio") @Valid Portfolio portfolio,
                                    BindingResult bindingResult, Model model){

        if (bindingResult.hasErrors()){
           return "portfolio/portfolio-add";
        }

        portfolioService.add(portfolio);
        return "redirect:/portfolios";
    }

    @GetMapping("/portfolio/{id}")
    public String portfolio(@PathVariable("id") Long id, @ModelAttribute("portfoliosPaper") PortfoliosPaper portfoliosPaper, Model model){

        //todo валидация форм
        if (!portfolioService.existsById(id)){
            return "redirect:/portfolios";
        }

        Portfolio portfolio = portfolioService.findById(id).get();
        model.addAttribute("portfolio", portfolio);

        List<PortfoliosPaper> portfoliosPapers = portfolio.getPapers();
        model.addAttribute("papers", portfoliosPapers);

        Iterable<Paper> allPapers = paperService.findAll();
        model.addAttribute("allPapers", allPapers);

        return "portfolio/portfolio-details";
    }

    @PostMapping("/portfolio/{id}/addPaper")
    public String portfolioPostAddPaper (@PathVariable("id") Long portfolioId, @Validated Long addingPaperId, @RequestParam Integer vol,
                                         @ModelAttribute("portfoliosPaper") PortfoliosPaper portfoliosPaper, Model model){

        if (!paperService.existsById(addingPaperId)){
            return "redirect:/portfolio/{id}";
        }
        if (!portfolioService.existsById(portfolioId)){
            return "redirect:/portfolios";
        }

        portfolioService.addPaper(portfolioId, addingPaperId, portfoliosPaper, vol);
        return "redirect:/portfolio/{id}";
    }

    @PostMapping("/portfolio/{id}/removePaper")
    public String portfolioPostRemovePaper (@PathVariable("id") Long portfolioId, @Validated Long removingPaperId, Model model){

        if (!portfoliosPaperService.existsById(removingPaperId)){
            return "redirect:/portfolio/{id}";
        }
        if (!portfolioService.existsById(portfolioId)){
            return "redirect:/portfolios";
        }

        portfolioService.removePaper(portfolioId, removingPaperId);
        return "redirect:/portfolio/{id}";
    }

}
