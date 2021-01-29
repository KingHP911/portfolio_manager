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
    Long id;

    @NotBlank
    String name, ticker;
    TypesOfPaper type;
    CurrencyOfPaper currency;
    BigDecimal currentPrice;

}
