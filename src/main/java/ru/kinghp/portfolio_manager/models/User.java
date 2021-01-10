package ru.kinghp.portfolio_manager.models;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.util.List;

@Data
//@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    String name;
    String email;

//    @Autowired
//    @OneToMany()
    List <Portfolio> portfolios;

    public User() {

    }

    public User(List<Portfolio> portfolios) {
        this.portfolios = portfolios;
    }
}
