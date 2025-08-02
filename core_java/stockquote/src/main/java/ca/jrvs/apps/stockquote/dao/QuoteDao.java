package ca.jrvs.apps.stockquote.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class QuoteDao implements CrudDao<Quote, String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(QuoteDao.class);
    private static final String INSERT = "INSERT INTO quote (symbol, open, high, low, price, volume, " +
            "latest_trading_day, previous_close, change, change_percent) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
    private static final String UPDATE = "UPDATE quote SET open = ?, high = ?, low = ?, price = ?, " +
            "volume = ?, latest_trading_day = ?, previous_close = ?, change = ?, change_percent = ? WHERE symbol = ?;";
    private static final String SELECT_BY_ID = "SELECT symbol, open, high, low, price, volume, " +
            "latest_trading_day, previous_close, change, change_percent, timestamp FROM quote WHERE symbol = ?;";
    private static final String SELECT_ALL = "SELECT symbol, open, high, low, price, volume, " +
            "latest_trading_day, previous_close, change, change_percent, timestamp from quote;";
    private static final String DELETE = "DELETE FROM quote WHERE symbol = ?;";
    private static final String DELETE_ALL = "TRUNCATE TABLE quote CASCADE;";

    private Connection c;

    public QuoteDao(Connection c) {
        this.c = c;
    }

    @Override
    public Quote save(Quote entity) throws IllegalArgumentException {
        LOGGER.info("Saving quote: " + entity);
        Quote quote;
        Optional<Quote> optionalQuote = findById(entity.getTicker());
        if(optionalQuote.isPresent()) {
            quote = update(entity);
        } else {
            quote = insert(entity);
        }
        return quote;
    }

    private Quote insert(Quote entity) throws IllegalArgumentException {
        LOGGER.info("Inserting new quote");
        Quote quote;
        try(PreparedStatement statement = c.prepareStatement(INSERT)) {
            statement.setString(1, entity.getTicker());
            statement.setDouble(2, entity.getOpen());
            statement.setDouble(3, entity.getHigh());
            statement.setDouble(4, entity.getLow());
            statement.setDouble(5, entity.getPrice());
            statement.setInt(6, entity.getVolume());
            statement.setDate(7, Date.valueOf(entity.getLatestTradingDay()));
            statement.setDouble(8, entity.getPreviousClose());
            statement.setDouble(9, entity.getChange());
            statement.setString(10, entity.getChangePercent());
            statement.execute();
            quote = findById(entity.getTicker()).orElse(null);
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
            throw new IllegalArgumentException(e);
        }
        return quote;
    }

    private Quote update(Quote entity) throws IllegalArgumentException {
        LOGGER.info("Updating existing quote");
        Quote quote;
        try(PreparedStatement statement = c.prepareStatement(UPDATE)) {
            statement.setDouble(1, entity.getOpen());
            statement.setDouble(2, entity.getHigh());
            statement.setDouble(3, entity.getLow());
            statement.setDouble(4, entity.getPrice());
            statement.setInt(5, entity.getVolume());
            statement.setDate(6, Date.valueOf(entity.getLatestTradingDay()));
            statement.setDouble(7, entity.getPreviousClose());
            statement.setDouble(8, entity.getChange());
            statement.setString(9, entity.getChangePercent());
            statement.setString(10, entity.getTicker());
            statement.executeUpdate();
            quote = findById(entity.getTicker()).orElse(null);
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
            throw new IllegalArgumentException(e);
        }
        return quote;
    }

    @Override
    public Optional<Quote> findById(String s) throws IllegalArgumentException {
        LOGGER.info("Fetching quote with id " + s);
        Quote quote = null;
        try(PreparedStatement statement = c.prepareStatement(SELECT_BY_ID)) {
            statement.setString(1, s);
            ResultSet rs = statement.executeQuery();
            while(rs.next()) {
                quote = new Quote();
                quote.setTicker(rs.getString("symbol"));
                quote.setOpen(rs.getDouble("open"));
                quote.setHigh(rs.getDouble("high"));
                quote.setLow(rs.getDouble("low"));
                quote.setPrice(rs.getDouble("price"));
                quote.setVolume(rs.getInt("volume"));
                quote.setLatestTradingDay(rs.getDate("latest_trading_day").toLocalDate());
                quote.setPreviousClose(rs.getDouble("previous_close"));
                quote.setChange(rs.getDouble("change"));
                quote.setChangePercent(rs.getString("change_percent"));
                quote.setTimestamp(rs.getTimestamp("timestamp"));
            }
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
            throw new IllegalArgumentException(e);
        }
        return Optional.ofNullable(quote);
    }

    @Override
    public Iterable<Quote> findAll() {
        LOGGER.info("Fetching all quotes");
        List<Quote> results = new ArrayList<>();
        try(PreparedStatement statement = c.prepareStatement(SELECT_ALL)) {
            ResultSet rs = statement.executeQuery();
            while(rs.next()) {
                Quote quote = new Quote();
                quote.setTicker(rs.getString("symbol"));
                quote.setOpen(rs.getDouble("open"));
                quote.setHigh(rs.getDouble("high"));
                quote.setLow(rs.getDouble("low"));
                quote.setPrice(rs.getDouble("price"));
                quote.setVolume(rs.getInt("volume"));
                quote.setLatestTradingDay(rs.getDate("latest_trading_day").toLocalDate());
                quote.setPreviousClose(rs.getDouble("previous_close"));
                quote.setChange(rs.getDouble("change"));
                quote.setChangePercent(rs.getString("change_percent"));
                quote.setTimestamp(rs.getTimestamp("timestamp"));
                results.add(quote);
            }
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
            throw new IllegalArgumentException(e);
        }
        return results;
    }

    @Override
    public void deleteById(String s) throws IllegalArgumentException {
        LOGGER.info("Deleting quote with id " + s);
        try(PreparedStatement statement = c.prepareStatement(DELETE)) {
            statement.setString(1, s);
            statement.execute();
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public void deleteAll() throws IllegalArgumentException {
        LOGGER.info("Deleting all quotes");
        try(PreparedStatement statement = c.prepareStatement(DELETE_ALL)) {
            statement.execute();
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
            throw new IllegalArgumentException(e);
        }
    }
}
