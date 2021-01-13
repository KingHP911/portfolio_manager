package ru.kinghp.portfolio_manager.models;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
public class Portfolio {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    Long id;
    String name;

    @ManyToMany(mappedBy = "portfolios")
    Set<Paper> papers;

    public Portfolio() {
    }

    public Portfolio(String name) {
        this.name = name;
    }
}
