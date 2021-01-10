package ru.kinghp.portfolio_manager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import ru.kinghp.portfolio_manager.models.Paper;
import ru.kinghp.portfolio_manager.models.Portfolio;
import ru.kinghp.portfolio_manager.models.User;

import java.util.List;

@Configuration
@ComponentScan("ru.kinghp.portfolio_manager")
public class PortfolioConfig {

//    @Bean
//    public Paper paper(){
//        return new Paper();
//    }

//    @Bean
//    public Portfolio portfolio (List<Paper> papers){
//        return new Portfolio(papers);
//    }
//
//    @Bean
//    public User user(List<Portfolio> portfolios){
//        return new User(portfolios);
//    }

}
