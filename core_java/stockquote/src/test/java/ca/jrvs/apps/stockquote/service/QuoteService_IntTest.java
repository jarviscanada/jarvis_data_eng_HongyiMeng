package ca.jrvs.apps.stockquote.service;

import ca.jrvs.apps.stockquote.dao.*;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.SQLException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
public class QuoteService_IntTest {

    @Container
    private PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:17.2-alpine"
    ).withInitScript("init_test_data.sql");

    QuoteService quoteService;

    @BeforeEach
    void setup() throws SQLException {
        DatabaseConnectionManager dcm = new DatabaseConnectionManager(
                postgres.getJdbcUrl(),
                postgres.getUsername(),
                postgres.getPassword()
        );
        QuoteDao quoteDao = new QuoteDao(dcm.getConnection());
        OkHttpClient client = new OkHttpClient();
        QuoteHttpHelper httpHelper = new QuoteHttpHelper(client);
        quoteService = new QuoteService(quoteDao, httpHelper);
    }

    @Test
    public void whenFetchQuoteDataFromAPI_thenQuoteIsSaved() {
        assertTrue(postgres.isRunning());
        String ticker = "TSLA";
        Optional<Quote> optionalQuote = quoteService.fetchQuoteDataFromAPI(ticker);

        assertTrue(optionalQuote.isPresent());
        assertEquals(ticker, optionalQuote.get().getTicker());
    }
}
