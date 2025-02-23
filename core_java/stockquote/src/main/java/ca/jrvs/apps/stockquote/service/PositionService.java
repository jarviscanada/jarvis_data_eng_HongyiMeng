package ca.jrvs.apps.stockquote.service;

import ca.jrvs.apps.stockquote.dao.Position;
import ca.jrvs.apps.stockquote.dao.PositionDao;
import ca.jrvs.apps.stockquote.dao.Quote;
import ca.jrvs.apps.stockquote.dao.QuoteDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class PositionService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PositionService.class);
    private PositionDao dao;
    private QuoteDao quoteDao;

    public PositionService(PositionDao dao, QuoteDao quoteDao) {
        this.dao = dao;
        this.quoteDao = quoteDao;
    }

    /**
     * Processes a buy order and updates the database accordingly
     * @param ticker symbol
     * @param numberOfShares to purchase
     * @param price total price paid
     * @return The position in our database after processing the buy
     */
    public Position buy(String ticker, int numberOfShares, double price) throws IllegalArgumentException {
        LOGGER.info("Buying {} shares of {} for {}", numberOfShares, ticker, price);
        Optional<Quote> optionalQuote = quoteDao.findById(ticker);
        if(optionalQuote.isEmpty()) {
            LOGGER.error("Error purchasing shares: could not find quote for {}", ticker);
            throw new IllegalArgumentException("Ticker " + ticker + " is not present in the quote table.");
        }
        Quote quote = optionalQuote.get();
        if (quote.getVolume() < numberOfShares) {
            throw new IllegalArgumentException("Cannot purchase " + numberOfShares + " shares. " +
                    "Volume available: " + quote.getVolume());
        }
        if (numberOfShares < 0) {
            throw new IllegalArgumentException("Number of shares must be non-negative.");
        }
        Position position = new Position();
        position.setTicker(ticker);
        position.setNumOfShares(numberOfShares);
        position.setValuePaid(price);
        LOGGER.info("Bought {} shares of {} for {}", numberOfShares, ticker, price);
        return dao.save(position);
    }

    /**
     * Sells all shares of the given ticker symbol
     * @param ticker to sell
     */
    public void sell(String ticker) {
        LOGGER.info("Selling shares of {}", ticker);
        dao.deleteById(ticker);
    }

    /**
     * Retrieve all current positions
     * @return
     */
    public Iterable<Position> getAllPositions() {
        LOGGER.info("Getting all positions");
        return dao.findAll();
    }
}
