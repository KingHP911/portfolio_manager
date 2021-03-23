package ru.kinghp.portfolio_manager.service;

import java.math.BigDecimal;

public interface FinnHubService {

    BigDecimal getCurrentPrice(String ticker);

}
