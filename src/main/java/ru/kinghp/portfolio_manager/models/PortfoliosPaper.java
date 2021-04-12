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
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    private Paper paper;

    private BigDecimal currentPrice;
    private BigDecimal purchasePrice;
    private LocalDateTime purchaseDate;
    private Integer vol;

    @ManyToOne(fetch = FetchType.LAZY)
    private Portfolio portfolio;

}
