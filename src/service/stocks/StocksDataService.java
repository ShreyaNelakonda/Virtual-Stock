package service.stocks;

import java.io.IOException;
import java.time.LocalDate;

import dto.StockDatePriceDTO;

/**
 * Interface that provides services to access different data stores or API.
 */
public interface StocksDataService {

  /**
   * Service to fetch the latest data for a given ticker from API.
   *
   * @param ticker ticker symbol of the stock
   * @throws IOException when stock data is unavailable
   */
  void getStockDataFromAPI(String ticker) throws IOException;

  /**
   * Service to fetch the latest closing price of the stock from given data source.
   * If info is not available in the data source then it updates it from API.
   *
   * @param ticker ticker symbol of the stock
   * @return latest price of the given stock
   * @throws Exception when it fails to fetch required stock price from the data source.
   */
  StockDatePriceDTO getStockPriceByTicker(String ticker) throws Exception;

  /**
   * Service to check if ticker mentioned has data store available or not.
   *
   * @param ticker stock ticker symbol
   * @throws IllegalArgumentException when stock ticker is invalid, not available in datastore
   */
  void checkDataExistsForTicker(String ticker) throws IllegalArgumentException;

  /**
   * Method to get the Stock Price for the given ticker on a given date from the database.
   *
   * @param stockSymbol ticker symbol of the stock
   * @param date        (YYYY-MM-DD) date to look for
   * @return stock price of the given stock on the particular date
   * @throws IOException when file operations fail
   */
  float getStockValueOnCertainDate(String stockSymbol, LocalDate date)
          throws IOException;

  /**
   * Clears stock datastore from the system, before/after the session.
   */
  void clearStockDatastore();

  /**
   * Fetches the price of the stock for the given date.
   *
   * @param ticker stock symbol
   * @param date   date to be queried
   * @return stock price if it exists for the date
   * @throws IllegalArgumentException when price is not found for a date
   */
  float getStockActualPriceByDate(String ticker, LocalDate date) throws IllegalArgumentException;

  /**
   * Checks if data exists in the datastore for given symbol, otherwise it downloads.
   *
   * @param ticker stock symbol
   * @throws IOException when stock is not available
   */
  void checkDownloadStockData(String ticker) throws IOException;
}
