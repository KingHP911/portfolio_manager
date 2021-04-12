package ru.kinghp.portfolio_manager;

import lombok.Data;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.kinghp.portfolio_manager.models.PortfoliosPaper;
import ru.kinghp.portfolio_manager.ws.PortfoliosPaperDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PortfolioTest {

    @Autowired
    private ModelMapper modelMapper;

    @Test
    public void convertObjDtoTest(){

    }

}
