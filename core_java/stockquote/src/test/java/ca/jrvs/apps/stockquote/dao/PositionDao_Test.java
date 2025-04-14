package ca.jrvs.apps.stockquote.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.SQLException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
public class PositionDao_Test {

    private PositionDao dao;

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
        dao = new PositionDao(dcm.getConnection());
    }

    @Test
    public void givenEntity_whenSaving_thenReturnSavedEntity() {
        assertTrue(postgres.isRunning());
        String ticker = "GOOG";
        int numberOfShares = 500;
        double totalPaid = 100000.00;

        Position newPosition = new Position();
        newPosition.setTicker(ticker);
        newPosition.setNumOfShares(numberOfShares);
        newPosition.setValuePaid(totalPaid);

        Position savedPosition = dao.save(newPosition);
        
        assertNotEquals(newPosition, savedPosition);
        assertEquals(ticker, savedPosition.getTicker());
        assertEquals(numberOfShares, savedPosition.getNumOfShares());
        assertEquals(totalPaid, savedPosition.getValuePaid());
    }

    @Test
    public void givenEntityAlreadyExists_whenSaving_thenReturnUpdatedEntity() {
        assertTrue(postgres.isRunning());
        String ticker = "AAPL";
        int numberOfShares = 500;
        double totalPaid = 100000.00;

        Position newPosition = new Position();
        newPosition.setTicker(ticker);
        newPosition.setNumOfShares(numberOfShares);
        newPosition.setValuePaid(totalPaid);

        Position savedPosition = dao.save(newPosition);

        assertNotEquals(newPosition, savedPosition);
        assertEquals(ticker, savedPosition.getTicker());
        assertEquals(numberOfShares, savedPosition.getNumOfShares());
        assertEquals(totalPaid, savedPosition.getValuePaid());
    }

    @Test
    public void givenTickerAndEntityExists_whenFindById_thenReturnEntity() {
        assertTrue(postgres.isRunning());
        String ticker = "AAPL";

        Optional<Position> optionalPosition = dao.findById(ticker);

        assertTrue(optionalPosition.isPresent());
        assertEquals(ticker, optionalPosition.get().getTicker());
    }

    @Test
    public void givenTickerAndNoEntityExists_whenFindById_thenReturnEmptyOptional() {
        assertTrue(postgres.isRunning());
        String ticker = "AAPLR";

        Optional<Position> optionalPosition = dao.findById(ticker);

        assertTrue(optionalPosition.isEmpty());
    }

    @Test
    public void whenFindAll_thenReturnAllEntities() {
        assertTrue(postgres.isRunning());
        Iterable<Position> positionIterable = dao.findAll();
        int count = 0;
        for(Position p : positionIterable) {
            count++;
        }

        assertEquals(1, count);
    }

    @Test
    public void givenTickerAndEntityExists_whenDeleteById_thenEntityIsDeleted() {
        assertTrue(postgres.isRunning());
        String ticker = "AAPL";
        Optional<Position> optionalPosition = dao.findById(ticker);
        assertTrue(optionalPosition.isPresent());

        dao.deleteById(ticker);

        optionalPosition = dao.findById(ticker);
        assertFalse(optionalPosition.isPresent());
    }

    @Test
    public void whenDeleteAll_thenDeleteAllEntities() {
        assertTrue(postgres.isRunning());
        Iterable<Position> positionIterable = dao.findAll();
        int count = 0;
        for(Position p : positionIterable) {
            count++;
        }
        assertEquals(1, count);

        dao.deleteAll();

        positionIterable = dao.findAll();
        count = 0;
        for(Position p : positionIterable) {
            count++;
        }
        assertEquals(0, count);
    }
}
