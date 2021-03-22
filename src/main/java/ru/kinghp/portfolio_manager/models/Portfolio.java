package ru.kinghp.portfolio_manager.models;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Portfolio {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    @NotBlank
    private String name;

    @OneToMany(mappedBy = "portfolio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PortfoliosPaper> papers = new ArrayList<>();

    private String ownerName;

}
