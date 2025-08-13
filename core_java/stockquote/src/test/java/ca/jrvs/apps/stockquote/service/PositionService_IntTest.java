package ca.jrvs.apps.stockquote.service;

import ca.jrvs.apps.stockquote.dao.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@Testcontainers
public class PositionService_IntTest {

    @Container
    private PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:17.2-alpine"
    ).withInitScript("init_test_data.sql");
    PositionService positionService;

    @BeforeEach
    void setup() throws SQLException {
        DatabaseConnectionManager dcm = new DatabaseConnectionManager(
                postgres.getJdbcUrl(),
                postgres.getUsername(),
                postgres.getPassword()
        );
        QuoteDao quoteDao = new QuoteDao(dcm.getConnection());
        PositionDao positionDao = new PositionDao(dcm.getConnection());
        positionService = new PositionService(positionDao, quoteDao);
    }

    @Test
    public void whenBuy_thenNewPositionIsCreated() {
        String ticker = "GOOG";

        Position position = positionService.buy(ticker, 1, 400.32);

        assertEquals(ticker, position.getTicker());
    }

    @Test
    public void whenSell_thenPositionIsRemoved() {
        String ticker = "AAPL";

        positionService.sell(ticker);

        Iterable<Position> positions = positionService.getAllPositions();
        Position appl = null;
        for(Position position : positions) {
            if(position.getTicker().equals(ticker)) {
                appl = position;
            }
        }
        assertNull(appl);
    }

}
