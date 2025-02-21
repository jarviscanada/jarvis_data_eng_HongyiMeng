package ca.jrvs.apps.stockquote;

import ca.jrvs.apps.stockquote.controller.StockQuoteController;
import ca.jrvs.apps.stockquote.dao.PositionDao;
import ca.jrvs.apps.stockquote.dao.QuoteDao;
import ca.jrvs.apps.stockquote.dao.QuoteHttpHelper;
import ca.jrvs.apps.stockquote.service.PositionService;
import ca.jrvs.apps.stockquote.service.QuoteService;
import okhttp3.OkHttpClient;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class StockQuote {
    public static void main(String[] args) {
        Map<String, String> properties = new HashMap<>();
        try(BufferedReader br = new BufferedReader(new FileReader("src/main/resources/properties.txt"))) {
            String line;
            while((line = br.readLine()) != null) {
                String[] tokens = line.split(":");
                properties.put(tokens[0], tokens[1]);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        try {
            Class.forName(properties.get("db-class"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        OkHttpClient client = new OkHttpClient();
        String url = "jdbc:postgresql://" + properties.get("server") + ":" + properties.get("port") + "/"
                + properties.get("database");
        try(Connection c = DriverManager.getConnection(url, properties.get("username"), properties.get("password"))) {
            QuoteDao quoteDao = new QuoteDao(c);
            PositionDao positionDao = new PositionDao(c);
            QuoteHttpHelper httpHelper = new QuoteHttpHelper(client, properties.get("api-key"));
            QuoteService quoteService = new QuoteService(quoteDao, httpHelper);
            PositionService positionService = new PositionService(positionDao, quoteDao);
            StockQuoteController stockQuoteController = new StockQuoteController(quoteService, positionService);
            stockQuoteController.initClient();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
