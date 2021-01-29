package ru.kinghp.portfolio_manager.models;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
public class PortfoliosPaper{

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    Long id;

    @NotBlank
    String name;

    @ManyToOne(fetch = FetchType.LAZY)
    Paper paper;

    BigDecimal purchasePrice, currentPrice;
    LocalDateTime purchaseDate;
    Integer vol;

    @ManyToOne(fetch = FetchType.LAZY)
    Portfolio portfolio;


    public PortfoliosPaper(Paper paper, BigDecimal purchasePrice, LocalDateTime purchaseDate,
                           Integer vol) {
        this.paper = paper;
        this.name = paper.getName();

        this.purchasePrice = purchasePrice;
        this.purchaseDate = purchaseDate;
        this.vol = vol;
    }

    public PortfoliosPaper() {
       
    }


}
