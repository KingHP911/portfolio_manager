package ru.kinghp.portfolio_manager.models;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import javax.validation.constraints.NotBlank;

@Data
@Entity
public class Paper {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @NotBlank
    private String name;
    @NotBlank
    private String ticker;
    private PaperTypes type;
    private PaperCurrency currency;
    private BigDecimal currentPrice;

}
