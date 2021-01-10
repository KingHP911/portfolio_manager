package ru.kinghp.portfolio_manager.models;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.time.LocalDateTime;


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
}
