package ru.kinghp.portfolio_manager.config;

import com.github.oscerd.finnhub.client.FinnhubClient;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;
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

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
