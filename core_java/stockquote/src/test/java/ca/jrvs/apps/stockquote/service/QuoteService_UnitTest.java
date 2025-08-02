package ca.jrvs.apps.stockquote.service;

import ca.jrvs.apps.stockquote.dao.Quote;
import ca.jrvs.apps.stockquote.dao.QuoteDao;
import ca.jrvs.apps.stockquote.dao.QuoteHttpHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class QuoteService_UnitTest {

    @Mock
    QuoteHttpHelper httpHelper;
    @Mock
    QuoteDao quoteDao;
    Quote quote;
    Quote savedQuote;
    static final String TICKER = "AAPL";
    QuoteService service;

    @BeforeEach
    void setup() {
        savedQuote = new Quote();
        quote = new Quote();
        Mockito.when(httpHelper.fetchQuoteInfo(TICKER)).thenReturn(quote);
        Mockito.when(quoteDao.save(quote)).thenReturn(savedQuote);

        service = new QuoteService(quoteDao, httpHelper);
    }

    @Test
    public void givenTicker_whenFetchQuoteDataFromAPI_thenInvokeFetchQuoteInfo() {
        service.fetchQuoteDataFromAPI(TICKER);

        verify(httpHelper, times(1)).fetchQuoteInfo(TICKER);
    }

    @Test
    public void givenTicker_whenFetchQuoteDataFromAPI_thenInvokeSave() {
        service.fetchQuoteDataFromAPI(TICKER);

        verify(quoteDao, times(1)).save(quote);

    }

    @Test
    public void givenTicker_whenFetchQuoteDataFromAPI_thenReturnSavedQuote() {
        Quote returnedQuote = service.fetchQuoteDataFromAPI(TICKER).get();

        assertEquals(savedQuote, returnedQuote);
    }
}
