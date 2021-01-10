package ru.kinghp.portfolio_manager.models;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Data
public class Portfolio {

//    @Autowired
    List <Paper> papers;

    public Portfolio() {

    }

    public Portfolio(List<Paper> papers) {
        this.papers = papers;
    }
}
