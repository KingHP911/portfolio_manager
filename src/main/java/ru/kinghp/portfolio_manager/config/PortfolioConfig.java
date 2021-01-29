package ru.kinghp.portfolio_manager.config;

import com.github.oscerd.finnhub.client.FinnhubClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ru.kinghp.portfolio_manager.models.FinnHubData;

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
    public FinnHubData finnHubData(FinnhubClient finnhubClient){
        FinnHubData hubData = new FinnHubData(finnhubClient);
        return hubData;
    }

}
