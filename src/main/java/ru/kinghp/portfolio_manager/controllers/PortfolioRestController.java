package ru.kinghp.portfolio_manager.controllers;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.kinghp.portfolio_manager.models.DBUser;
import ru.kinghp.portfolio_manager.models.Paper;
import ru.kinghp.portfolio_manager.models.Portfolio;
import ru.kinghp.portfolio_manager.models.PortfoliosPaper;
import ru.kinghp.portfolio_manager.service.DBUserService;
import ru.kinghp.portfolio_manager.service.PaperService;
import ru.kinghp.portfolio_manager.service.PortfolioService;
import ru.kinghp.portfolio_manager.service.PortfoliosPaperService;

import javax.validation.Valid;
import java.util.List;

@Data
@RestController
public class PortfolioRestController {

    private final PaperService paperService;
    private final PortfolioService portfolioService;
    private final PortfoliosPaperService portfoliosPaperService;
    private final DBUserService userService;

    @PostMapping("/registration")
    public DBUser registrationPost (@RequestBody DBUser user){
        if (!user.getPassword().equals(user.getPasswordConfirm())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad request");
        }
        return userService.add(user);
    }

    @GetMapping("/papers")
    public Iterable<Paper> papers (){
        return paperService.findAll();
    }

    @PostMapping("/paper/add")
    public Paper paperPostAdd (@RequestBody Paper paper){
        return paperService.add(paper);
    }

    @GetMapping("/paper/{id}")
    public Paper paper(@PathVariable("id") Long id){
        if (!paperService.existsById(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Paper not found");
        }
        return paperService.findById(id).get();
    }

    @PostMapping("/paper/{id}/edit")
    public Paper paperPostUpdate (@RequestBody Paper paper){
        return paperService.update(paper);
    }

    @PostMapping("/paper/{id}/remove")
    public ResponseEntity<String> paperPostDelete (@PathVariable("id") Long id){
        paperService.deleteById(id);
        return ResponseEntity.ok().body("Paper deleted");
    }



    @GetMapping("/portfolios")
    public Iterable<Portfolio> portfolios (Model model){
        return portfolioService.findAll();
    }

    @PostMapping("/portfolio/add")
    public Portfolio portfolioPostAdd (@RequestBody Portfolio portfolio){
        return portfolioService.add(portfolio);
    }

    @GetMapping("/portfolio/{id}")
    public Portfolio portfolio(@PathVariable("id") Long id){
        if (!portfolioService.existsById(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Portfolio not found");
        }
        return portfolioService.findById(id).get();
    }

    @PostMapping("/portfolio/{id}/addPaper")
    public Portfolio portfolioPostAddPaper (@PathVariable("id") Long portfolioId, @RequestParam Long addingPaperId, @RequestParam Integer vol,
                                            @RequestBody PortfoliosPaper portfoliosPaper){

        if (!paperService.existsById(addingPaperId)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Paper not found");
        }
        if (!portfolioService.existsById(portfolioId)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Portfolio not found");
        }
        return portfolioService.addPaper(portfolioId, addingPaperId, portfoliosPaper, vol);
    }

    @PostMapping("/portfolio/{id}/removePaper")
    public Portfolio portfolioPostRemovePaper (@PathVariable("id") Long portfolioId, @RequestParam Long removingPaperId){
        if (!portfoliosPaperService.existsById(removingPaperId)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Paper not found");
        }
        if (!portfolioService.existsById(portfolioId)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Portfolio not found");
        }
        return portfolioService.removePaper(portfolioId, removingPaperId);
    }

}
