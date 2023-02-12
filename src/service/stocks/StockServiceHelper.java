package service.stocks;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Abstract class that provides common stock service methods.
 */
public abstract class StockServiceHelper implements StocksDataService {

  private static final String API_KEY = "XKQWZTAHCRNG4LG4";
  private static final String STOCK_DIR = "database/StockDB/";
  private static final String ERROR_MSG = "Error Message";

  /**
   * Checks for error in the result returned by the API.
   * @param ticker tickerSymbol
   * @param buffer buffer size
   * @param b byte
   * @throws IllegalArgumentException when error is found in the result returned.
   */
  protected void checkForErrorsInResult(String ticker, byte[] buffer, int b)
          throws IllegalArgumentException {
    String s = new String(buffer, 0, b, StandardCharsets.UTF_8);
    if (s.contains(ERROR_MSG)) {
      throw new IllegalArgumentException(ticker + " does not exist.");
    }
  }

  /**
   * Generates URL for the Alpha vantage API.
   * @param ticker tickerSymbol
   * @param fileType type of file (json or csv)
   * @return api as URL
   * @throws RuntimeException when API implementation has changed.
   */
  protected URL generateURL(String ticker, String fileType) throws RuntimeException {
    try {
      URL url = new URL("https://www.alphavantage"
              + ".co/query?function=TIME_SERIES_DAILY"
              + "&outputsize=full"
              + "&symbol"
              + "=" + ticker
              + "&apikey=" + API_KEY
              + "&datatype=" + fileType);
      return url;
    } catch (MalformedURLException e) {
      throw new RuntimeException("the alphavantage API has either changed or "
              + "no longer works");
    }
  }

  @Override
  public void clearStockDatastore() {
    File dir = new File(STOCK_DIR);
    if (dir != null && dir.listFiles() != null) {
      for (File file : dir.listFiles()) {
        if (file != null && !file.isDirectory()) {
          file.delete();
        }
      }
    }
  }
}
