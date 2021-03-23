package ru.kinghp.portfolio_manager.service.impl;

import com.github.oscerd.finnhub.client.FinnhubClient;
import com.github.oscerd.finnhub.model.Quote;
import lombok.Data;
import org.apache.http.client.ClientProtocolException;
import org.springframework.stereotype.Service;
import ru.kinghp.portfolio_manager.service.FinnHubService;

import java.io.IOException;
import java.math.BigDecimal;

@Data
@Service
public class FinnHubServiceImpl implements FinnHubService {

    private final FinnhubClient client;

    public BigDecimal getCurrentPrice(String ticker){

        BigDecimal currentPrice = new BigDecimal(0);
        try {
            Quote quote = client.getQuote(ticker);
            currentPrice = new BigDecimal(quote.getCurrentPrice());
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return currentPrice;
    }

}
