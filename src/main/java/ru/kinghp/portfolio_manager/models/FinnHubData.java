package ru.kinghp.portfolio_manager.models;

import com.github.oscerd.finnhub.client.FinnhubClient;
import com.github.oscerd.finnhub.model.CompanyProfile;
import com.github.oscerd.finnhub.model.Quote;
import lombok.Data;
import org.apache.http.client.ClientProtocolException;

import java.io.IOException;
import java.math.BigDecimal;

@Data
public class FinnHubData {

    private FinnhubClient client;

    public FinnHubData() {
    }

    public FinnHubData(FinnhubClient client) {
        this.client = client;
    }


    public Quote getQuote(String ticker){

        Quote quote = null;
        try {
            quote = client.getQuote(ticker);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return quote;
    }

    public CompanyProfile getCompanyProfile(String ticker){

        CompanyProfile companyProfile = null;
        try {
            companyProfile = client.getCompanyProfile(ticker);
            if(companyProfile.getName() == null){
                return null;
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return companyProfile;
    }

    public BigDecimal getCurrentPrice(String ticker){
        BigDecimal currentPrice = new BigDecimal(0);
        Quote quote = getQuote(ticker);
        if (quote!=null){
            currentPrice = new BigDecimal(quote.getCurrentPrice());
        }
        return currentPrice;
    }

}
