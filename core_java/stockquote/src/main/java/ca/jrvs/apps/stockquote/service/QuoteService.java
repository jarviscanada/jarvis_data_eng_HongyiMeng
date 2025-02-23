package ca.jrvs.apps.stockquote.service;

import ca.jrvs.apps.stockquote.dao.Quote;
import ca.jrvs.apps.stockquote.dao.QuoteDao;
import ca.jrvs.apps.stockquote.dao.QuoteHttpHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class QuoteService {

    private static final Logger LOGGER = LoggerFactory.getLogger(QuoteService.class);
    private QuoteDao dao;
    private QuoteHttpHelper httpHelper;

    public QuoteService(QuoteDao dao, QuoteHttpHelper httpHelper) {
        this.dao = dao;
        this.httpHelper = httpHelper;
    }

    /**
     * Fetches latest quote data from endpoint
     * @param ticker
     * @return Latest quote information or empty optional if ticker symbol not found
     */
    public Optional<Quote> fetchQuoteDataFromAPI(String ticker) {
        LOGGER.info("Fetching quote for {}", ticker);
        try {
            Quote quote = httpHelper.fetchQuoteInfo(ticker);
            Quote savedQuote = dao.save(quote);
            return Optional.of(savedQuote);
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }


}
