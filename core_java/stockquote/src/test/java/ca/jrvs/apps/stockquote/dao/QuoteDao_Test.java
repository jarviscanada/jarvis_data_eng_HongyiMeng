package ca.jrvs.apps.stockquote.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
public class QuoteDao_Test {

    private QuoteDao dao;

    @Container
    private PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:17.2-alpine"
    )
            .withInitScript("init_test_data.sql");

    @BeforeEach
    public void setupData() throws SQLException {
        DatabaseConnectionManager dcm = new DatabaseConnectionManager(
                postgres.getJdbcUrl(),
                postgres.getUsername(),
                postgres.getPassword()
        );
        dao = new QuoteDao(dcm.getConnection());
    }

    @Test
    public void givenEntity_whenSaving_thenReturnSavedEntity() {
        assertTrue(postgres.isRunning());
        String ticker = "AMZN";
        double open = 228.80;
        double high = 228.42;
        double low = 224.34;
        double price = 226.65;
        int volume = 3245645;
        LocalDate date = LocalDate.parse("2025-02-18");
        double previousClose = 228.43;
        double change = -2.03;
        String changePercent = "-0.89%";

        Quote newQuote = new Quote();
        newQuote.setTicker(ticker);
        newQuote.setOpen(open);
        newQuote.setHigh(high);
        newQuote.setLow(low);
        newQuote.setPrice(price);
        newQuote.setVolume(volume);
        newQuote.setLatestTradingDay(date);
        newQuote.setPreviousClose(previousClose);
        newQuote.setChange(change);
        newQuote.setChangePercent(changePercent);

        Quote savedQuote = dao.save(newQuote);

        assertNotEquals(newQuote, savedQuote);
        assertEquals(ticker, savedQuote.getTicker());
        assertEquals(open, savedQuote.getOpen());
        assertEquals(high, savedQuote.getHigh());
        assertEquals(low, savedQuote.getLow());
        assertEquals(price, savedQuote.getPrice());
        assertEquals(volume, savedQuote.getVolume());
        assertEquals(date, savedQuote.getLatestTradingDay());
        assertEquals(previousClose, savedQuote.getPreviousClose());
        assertEquals(change, savedQuote.getChange());
        assertEquals(changePercent, savedQuote.getChangePercent());
    }

    @Test
    public void givenEntityAlreadyExists_whenSaving_thenReturnUpdatedEntity() {
        assertTrue(postgres.isRunning());
        String ticker = "GOOG";
        double open = 228.80;
        double high = 228.42;
        double low = 224.34;
        double price = 226.65;
        int volume = 3245645;
        LocalDate date = LocalDate.parse("2025-02-18");
        double previousClose = 228.43;
        double change = -2.03;
        String changePercent = "-0.89%";

        Quote newQuote = new Quote();
        newQuote.setTicker(ticker);
        newQuote.setOpen(open);
        newQuote.setHigh(high);
        newQuote.setLow(low);
        newQuote.setPrice(price);
        newQuote.setVolume(volume);
        newQuote.setLatestTradingDay(date);
        newQuote.setPreviousClose(previousClose);
        newQuote.setChange(change);
        newQuote.setChangePercent(changePercent);

        Quote savedQuote = dao.save(newQuote);

        assertNotEquals(newQuote, savedQuote);
        assertEquals(ticker, savedQuote.getTicker());
        assertEquals(open, savedQuote.getOpen());
        assertEquals(high, savedQuote.getHigh());
        assertEquals(low, savedQuote.getLow());
        assertEquals(price, savedQuote.getPrice());
        assertEquals(volume, savedQuote.getVolume());
        assertEquals(date, savedQuote.getLatestTradingDay());
        assertEquals(previousClose, savedQuote.getPreviousClose());
        assertEquals(change, savedQuote.getChange());
        assertEquals(changePercent, savedQuote.getChangePercent());
    }

    @Test
    public void givenTickerAndEntityExists_whenFindById_thenReturnEntity() {
        assertTrue(postgres.isRunning());
        String ticker = "AAPL";

        Optional<Quote> optionalQuote = dao.findById(ticker);

        assertTrue(optionalQuote.isPresent());
        assertEquals(ticker, optionalQuote.get().getTicker());
    }

    @Test
    public void givenTickerAndNoEntityExists_whenFindById_thenReturnEmptyOptional() {
        assertTrue(postgres.isRunning());
        String ticker = "AAPLR";

        Optional<Quote> optionalQuote = dao.findById(ticker);

        assertTrue(optionalQuote.isEmpty());
    }

    @Test
    public void whenFindAll_thenReturnAllEntities() {
        assertTrue(postgres.isRunning());
        Iterable<Quote> quoteIterable = dao.findAll();
        int count = 0;
        for(Quote q : quoteIterable) {
            count++;
        }

        assertEquals(2, count);
    }

    @Test
    public void givenTickerAndEntityExists_whenDeleteById_thenEntityIsDeleted() {
        assertTrue(postgres.isRunning());
        String ticker = "AAPL";
        Optional<Quote> optionalQuote = dao.findById(ticker);
        assertTrue(optionalQuote.isPresent());

        dao.deleteById(ticker);

        optionalQuote = dao.findById(ticker);
        assertFalse(optionalQuote.isPresent());
    }

    @Test
    public void whenDeleteAll_thenDeleteAllEntities() {
        assertTrue(postgres.isRunning());
        Iterable<Quote> quoteIterable = dao.findAll();
        int count = 0;
        for(Quote q : quoteIterable) {
            count++;
        }
        assertEquals(2, count);

        dao.deleteAll();

        quoteIterable = dao.findAll();
        count = 0;
        for(Quote q : quoteIterable) {
            count++;
        }
        assertEquals(0, count);
    }

}
