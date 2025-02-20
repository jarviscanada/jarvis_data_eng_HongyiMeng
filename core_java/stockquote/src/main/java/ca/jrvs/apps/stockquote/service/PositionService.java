package ca.jrvs.apps.stockquote.service;

import ca.jrvs.apps.stockquote.dao.Position;
import ca.jrvs.apps.stockquote.dao.PositionDao;
import ca.jrvs.apps.stockquote.dao.Quote;
import ca.jrvs.apps.stockquote.dao.QuoteDao;

import java.util.Optional;

public class PositionService {
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
        Optional<Quote> optionalQuote = quoteDao.findById(ticker);
        if(optionalQuote.isEmpty()) {
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
        position.setValuePaid(position.getValuePaid());
        return dao.save(position);
    }

    /**
     * Sells all shares of the given ticker symbol
     * @param ticker to sell
     */
    public void sell(String ticker) {
        dao.deleteById(ticker);
    }

    /**
     * Retrieve all current positions
     * @return
     */
    public Iterable<Position> getAllPositions() {
        return dao.findAll();
    }
}
