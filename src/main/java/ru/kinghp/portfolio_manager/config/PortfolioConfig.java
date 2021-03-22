package ru.kinghp.portfolio_manager.config;

import com.github.oscerd.finnhub.client.FinnhubClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.kinghp.portfolio_manager.service.impl.FinnHubServiceImpl;

@Configuration
@ComponentScan("ru.kinghp.portfolio_manager")
public class PortfolioConfig {

    @Value("${fhtoken}")
    private String fhtoken;

    @Bean
    public FinnhubClient finnhubClient(){
        return new FinnhubClient(fhtoken);
    }

    @Bean
    public FinnHubServiceImpl finnHubService(FinnhubClient finnhubClient){
        FinnHubServiceImpl hubData = new FinnHubServiceImpl(finnhubClient);
        return hubData;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new Argon2PasswordEncoder();
    }

}
