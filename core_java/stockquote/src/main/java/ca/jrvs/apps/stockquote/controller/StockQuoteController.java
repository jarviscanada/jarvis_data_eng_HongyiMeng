package ca.jrvs.apps.stockquote.controller;

import ca.jrvs.apps.stockquote.dao.Position;
import ca.jrvs.apps.stockquote.dao.Quote;
import ca.jrvs.apps.stockquote.service.PositionService;
import ca.jrvs.apps.stockquote.service.QuoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.InputMismatchException;
import java.util.Optional;
import java.util.Scanner;

public class StockQuoteController {

    private static final Logger LOGGER = LoggerFactory.getLogger(StockQuoteController.class);
    private final QuoteService quoteService;
    private final PositionService positionService;

    public StockQuoteController(QuoteService quoteService, PositionService positionService) {
        this.quoteService = quoteService;
        this.positionService = positionService;
    }

    /**
     * User interface for our application
     */
    public void initClient() {
        System.out.println("Welcome to Stock App");
        Scanner scanner = new Scanner(System.in);
        while(true) {
            System.out.println("To display portfolio, press 1");
            System.out.println("To display stock data, press 2");
            System.out.println("To display purchase stock, press 3");
            System.out.println("To display sell stock, press 4");

            try {
                int input = scanner.nextInt();
                System.out.println(input);
                String ticker;
                switch (input) {
                    case 1:
                        displayPortfolio();
                        break;
                    case 2:
                        System.out.println("Enter a ticker");
                        ticker = scanner.next();
                        displayStockData(ticker);
                        break;
                    case 3:
                        System.out.println("Enter a ticker");
                        ticker = scanner.next();
                        System.out.println("Enter a quantity");
                        int quantity = scanner.nextInt();
                        buyStock(ticker, quantity);
                        break;
                    case 4:
                        System.out.println("Enter a ticker");
                        ticker = scanner.next();
                        sellStock(ticker);
                        break;
                    default:
                        System.out.println("Invalid input, please try again");
                        scanner.nextLine();
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input, please try again");
                scanner.nextLine();
            }
        }
    }

    private void displayPortfolio() {
        System.out.println("Your Portfolio");
        System.out.println("==============");
        System.out.println("Symbol | Shares | Book Value | Market Value");
        Iterable<Position> positions = positionService.getAllPositions();
        if (!positions.iterator().hasNext()) {
            System.out.println("No current holdings");
        }
        for (Position position : positions) {
            Optional<Quote> optionalQuote = quoteService.fetchQuoteDataFromAPI(position.getTicker());
            if (optionalQuote.isEmpty()) {
                LOGGER.error("Could not fetch quote data for " + position.getTicker());
            }
            Quote currentQuote = optionalQuote.get();
            System.out.println(position.getTicker() + " | " + position.getNumOfShares() + " | "
                    + position.getValuePaid() + " | " + currentQuote.getPrice() * position.getNumOfShares());
        }
        System.out.println("==============");
    }

    private void displayStockData(String symbol) {
        Optional<Quote> optionalQuote = quoteService.fetchQuoteDataFromAPI(symbol);
        if(optionalQuote.isEmpty()) {
            System.out.println("No data for " + symbol);
            System.out.println("==================");
        } else {
            Quote quote = optionalQuote.get();
            System.out.println("==================");
            System.out.println("Ticker: " + quote.getTicker());
            System.out.println("Open: " + quote.getOpen());
            System.out.println("High: " + quote.getHigh());
            System.out.println("Low: " + quote.getLow());
            System.out.println("Price: " + quote.getPrice());
            System.out.println("Volume: " + quote.getVolume());
            System.out.println("Latest trading day: " + quote.getLatestTradingDay());
            System.out.println("Previous close: " + quote.getPreviousClose());
            System.out.println("Change: " + quote.getChange());
            System.out.println("Change percent: " + quote.getChangePercent());
            System.out.println("==================");
        }
    }

    private void buyStock(String ticker, int numberOfShares) {
        Iterable<Position> positions = positionService.getAllPositions();
        for(Position position : positions) {
            if(position.getTicker().equals(ticker)) {
                System.out.println("You already own " + ticker);
                return;
            }
        }
        Optional<Quote> optionalQuote = quoteService.fetchQuoteDataFromAPI(ticker);
        if (optionalQuote.isEmpty()) {
            LOGGER.error("Could not fetch quote data for " + ticker);
        } else {
            positionService.buy(ticker, numberOfShares, numberOfShares * optionalQuote.get().getPrice());
            System.out.println("Successfully purchased " + numberOfShares + " shares of " + ticker + " for "
                    + numberOfShares * optionalQuote.get().getPrice());
        }
    }

    private void sellStock(String symbol) {
        Iterable<Position> positions = positionService.getAllPositions();
        for(Position position : positions) {
            if(position.getTicker().equals(symbol)) {
                positionService.sell(symbol);
                System.out.println("Sold all " + position.getNumOfShares() + " shares of " + position.getTicker());
                return;
            }
        }
    }
}
