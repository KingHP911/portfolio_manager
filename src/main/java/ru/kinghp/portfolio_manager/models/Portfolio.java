package ru.kinghp.portfolio_manager.models;

import lombok.Data;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Portfolio {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    Long id;
    String name;

    @OneToMany(mappedBy = "portfolio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PortfoliosPaper> papers = new ArrayList<>();

    public Portfolio() {
    }

    public Portfolio(String name) {
        this.name = name;
    }


    public void addPaper(PortfoliosPaper paper){
        this.papers.add(paper);
        paper.setPortfolio(this);
    }
    public void removePaper(PortfoliosPaper paper){
        this.papers.remove(paper);
        paper.setPortfolio(null);
    }

}
