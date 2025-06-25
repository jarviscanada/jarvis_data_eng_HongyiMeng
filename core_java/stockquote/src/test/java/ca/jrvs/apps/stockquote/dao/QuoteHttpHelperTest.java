package ca.jrvs.apps.stockquote.dao;

import okhttp3.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class QuoteHttpHelperTest {

    private static final String SAMPLE_JSON = "{\"Global Quote\":{\"01. symbol\":\"MSFT\",\"02. open\":\"407.2100\",\"03. high\":\"410.7500\",\"04. low\":\"404.3673\",\"05. price\":\"409.0400\",\"06. volume\":\"19121734\",\"07. latest trading day\":\"2025-02-12\",\"08. previous close\":\"411.4400\",\"09. change\":\"-2.4000\",\"10. change percent\":\"-0.5833%\"}}";

    @Mock
     OkHttpClient client;

    @Mock
    Call call;

    QuoteHttpHelper quoteHttpHelper;

    Response response;

    @BeforeEach
    private void setup() throws IOException {
        Request request = new Request.Builder()
                .url("https://some-url.com")
                .build();
        response = new Response.Builder()
                .request(request)
                .protocol(Protocol.HTTP_2)
                .code(200)
                .message("")
                .body(ResponseBody.create(SAMPLE_JSON, MediaType.get("application/json; charset=utf-8")))
                .build();
        Mockito.when(client.newCall(any(Request.class)))
                .thenReturn(call);
        Mockito.when(call.execute())
                .thenReturn(response);
        quoteHttpHelper = new QuoteHttpHelper(client);
    }


    @Test
    public void givenSymbolMsft_whenFetchQuoteInfo_thenReturnedQuoteContainsTicker() throws IOException {
        String expected = "MSFT";

        Quote quote = quoteHttpHelper.fetchQuoteInfo("MSFT");

        assertEquals(expected, quote.getTicker());
    }

    @Test
    public void givenSymbolMsft_whenFetchQuoteInfo_thenReturnedQuoteContainsOpen() throws IOException {
        double expected = 407.2100;

        Quote quote = quoteHttpHelper.fetchQuoteInfo("MSFT");

        assertEquals(expected, quote.getOpen());
    }

    @Test
    public void givenSymbolMsft_whenFetchQuoteInfo_thenReturnedQuoteContainsHigh() throws IOException {
        double expected = 410.7500;

        Quote quote = quoteHttpHelper.fetchQuoteInfo("MSFT");

        assertEquals(expected, quote.getHigh());
    }

    @Test
    public void givenSymbolMsft_whenFetchQuoteInfo_thenReturnedQuoteContainsLow() throws IOException {
        double expected = 404.3673;

        Quote quote = quoteHttpHelper.fetchQuoteInfo("MSFT");

        assertEquals(expected, quote.getLow());
    }

    @Test
    public void givenSymbolMsft_whenFetchQuoteInfo_thenReturnedQuoteContainsPrice() throws IOException {
        double expected = 409.0400;

        Quote quote = quoteHttpHelper.fetchQuoteInfo("MSFT");

        assertEquals(expected, quote.getPrice());
    }

    @Test
    public void givenSymbolMsft_whenFetchQuoteInfo_thenReturnedQuoteContainsVolume() throws IOException {
        int expected = 19121734;

        Quote quote = quoteHttpHelper.fetchQuoteInfo("MSFT");

        assertEquals(expected, quote.getVolume());
    }

    @Test
    public void givenSymbolMsft_whenFetchQuoteInfo_thenReturnedQuoteContainsLatestTradingDay() throws IOException {
        LocalDate expected = LocalDate.parse("2025-02-12");

        Quote quote = quoteHttpHelper.fetchQuoteInfo("MSFT");

        assertEquals(expected, quote.getLatestTradingDay());
    }

    @Test
    public void givenSymbolMsft_whenFetchQuoteInfo_thenReturnedQuoteContainsPreviousClose() throws IOException {
        double expected = 411.4400;

        Quote quote = quoteHttpHelper.fetchQuoteInfo("MSFT");

        assertEquals(expected, quote.getPreviousClose());
    }

    @Test
    public void givenSymbolMsft_whenFetchQuoteInfo_thenReturnedQuoteContainsChange() throws IOException {
        double expected = -2.4000;

        Quote quote = quoteHttpHelper.fetchQuoteInfo("MSFT");

        assertEquals(expected, quote.getChange());
    }

    @Test
    public void givenSymbolMsft_whenFetchQuoteInfo_thenReturnedQuoteContainsChangePercent() throws IOException {
        String expected = "-0.5833%";

        Quote quote = quoteHttpHelper.fetchQuoteInfo("MSFT");

        assertEquals(expected, quote.getChangePercent());
    }


}
