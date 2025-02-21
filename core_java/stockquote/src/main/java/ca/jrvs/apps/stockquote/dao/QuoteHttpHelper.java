package ca.jrvs.apps.stockquote.dao;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class QuoteHttpHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(QuoteHttpHelper.class);
    private final String apiKey;
    private final OkHttpClient client;

    public QuoteHttpHelper(OkHttpClient client) {
        apiKey = System.getenv("RAPID_API_KEY");
        if (apiKey == null) {
            throw new RuntimeException("Error: no rapid api key found. Please set env. var. RAPID_API_KEY");
        }
        this.client = client;
    }

    public QuoteHttpHelper(OkHttpClient client, String apiKey) {
        this.client = client;
        this.apiKey = apiKey;
    }

    /**
     * Fetch latest quote data from Alpha Vantage endpoint
     * @param symbol ticker symbol
     * @return Quote with latest data
     * @throws IllegalArgumentException - if no data was found for the given symbol
     */
    public Quote fetchQuoteInfo(String symbol) throws IllegalArgumentException {
        QuoteWrapper wrapper;
        Request request = new Request.Builder()
                .url("https://alpha-vantage.p.rapidapi.com/query?function=GLOBAL_QUOTE&symbol="+symbol+"&datatype=json")
                .header("X-RapidAPI-Key", apiKey)
                .header("X-RapidAPI-Host", "alpha-vantage.p.rapidapi.com")
                .build();

        try (Response response = client.newCall(request).execute()) {
            wrapper = JsonParser.toObjectFromJson(response.body().string(), QuoteWrapper.class);
            if(wrapper.getQuote() == null || wrapper.getQuote().getTicker() == null) {
                LOGGER.error("Symbol {} does not exist", symbol);
                throw new IllegalArgumentException("Error fetching data for symbol :" + symbol);
            }
        } catch (IOException e) {
            System.out.println(e);
            LOGGER.error("Symbol {} does not exist", symbol);
            throw new IllegalArgumentException("Error fetching data for symbol :" + symbol, e);
        }
        return wrapper.getQuote();
    }

}
