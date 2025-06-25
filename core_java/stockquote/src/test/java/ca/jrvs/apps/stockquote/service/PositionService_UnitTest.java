package ca.jrvs.apps.stockquote.service;

import ca.jrvs.apps.stockquote.dao.Position;
import ca.jrvs.apps.stockquote.dao.PositionDao;
import ca.jrvs.apps.stockquote.dao.Quote;
import ca.jrvs.apps.stockquote.dao.QuoteDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PositionService_UnitTest {

    @Mock
    PositionDao positionDao;
    @Mock
    QuoteDao quoteDao;
    static PositionService positionService;
    static Quote quote;
    static Position position;
    static final String VALID_TICKER = "NVDA";
    static final String INVALID_TICKER = "NVDAAAA";
    static final int VOLUME = 100000;

    @BeforeEach
    void setup() {
        quote = new Quote();
        quote.setVolume(VOLUME);
        position = new Position();

        positionService = new PositionService(positionDao, quoteDao);
    }

    @Test
    public void givenTickerNumberOfSharesAndPrice_whenBuy_thenInvokeSave() {
        when(quoteDao.findById(VALID_TICKER)).thenReturn(Optional.of(quote));

        positionService.buy(VALID_TICKER, 1, 194.00);

        verify(positionDao, times(1)).save(any());
    }

    @Test
    public void givenTickerNumberOfSharesAndPrice_whenBuy_thenReturnedSavedPosition() {
        when(quoteDao.findById(VALID_TICKER)).thenReturn(Optional.of(quote));
        when(positionDao.save(any())).thenReturn(position);

        Position returnedPosition = positionService.buy(VALID_TICKER, 1, 194.00);

        assertEquals(position, returnedPosition);
    }

    @Test
    public void givenTickerDoesNotExist_whenBuy_thenThrowIllegalArgumentException() {
        when(quoteDao.findById(INVALID_TICKER)).thenReturn(Optional.empty());
        String expectedMessage = "Ticker " + INVALID_TICKER + " is not present in the quote table.";

        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> positionService.buy(INVALID_TICKER, 1, 200)
        );
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test()
    public void givenVolumeLessThanNumberOfShares_whenBuy_thenThrowIllegalArgumentException() {
        when(quoteDao.findById(VALID_TICKER)).thenReturn(Optional.of(quote));
        int numShares = VOLUME + 1;
        String expectedMessage = "Cannot purchase " + numShares + " shares. " + "Volume available: " + VOLUME ;

        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> positionService.buy(VALID_TICKER, numShares, 200)
        );
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test()
    public void givenNegativeNumberOfShares_whenBuy_thenThrowIllegalArgumentException() {
        when(quoteDao.findById(VALID_TICKER)).thenReturn(Optional.of(quote));
        String expectedMessage = "Number of shares must be non-negative.";

        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> positionService.buy(VALID_TICKER, -1, 200)
        );
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void givenTicker_whenSell_thenInvokeDeleteById() {
        positionService.sell(VALID_TICKER);

        verify(positionDao, times(1)).deleteById(VALID_TICKER);
    }

}
