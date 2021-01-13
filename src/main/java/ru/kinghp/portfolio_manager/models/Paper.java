package ru.kinghp.portfolio_manager.models;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


@Data
@Entity
public class Paper {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    Long id;

    String name, ticker;
    TypesOfPaper type;
    CurrencyOfPaper currency;
    BigDecimal purchasePrice, currentPrice;
    LocalDateTime purchaseDate;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "paper_portfolio",
            joinColumns = @JoinColumn(name = "paper_id"),
            inverseJoinColumns = @JoinColumn(name = "portfolio_id"))
    List<Portfolio> portfolios;

    public Paper(String name, String ticker, TypesOfPaper type, CurrencyOfPaper currency, BigDecimal purchasePrice, LocalDateTime purchaseDate) {
        this.name = name;
        this.ticker = ticker;
        this.type = type;
        this.currency = currency;
        this.purchasePrice = purchasePrice;
        this.purchaseDate = purchaseDate;
    }

    public Paper() {
    }

    public void addPortfolio(Portfolio portfolio){
        this.portfolios.add(portfolio);
        portfolio.getPapers().add(this);
    }

    public void removePortfolio(Portfolio portfolio){
        this.portfolios.remove(portfolio);
        portfolio.getPapers().remove(this);
    }
}
