package service.stocks;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import dto.StockDatePriceDTO;

import static java.time.LocalDate.now;

/**
 * CSV implementation to perform operations on Stock Service.
 */
public class StockCSVServiceImpl extends StockServiceHelper {
  private static final String STOCK_DIR = "database/StockDB/";
  private static final String CSV_SEP = ",";
  private static final int CLOSE_DAY = 4;
  private static final int TXN_DATE = 0;
  private static final String FILE_TYPE = "csv";

  private static StockCSVServiceImpl instance;

  private StockCSVServiceImpl() {
    //nothing
  }

  /**
   * Method to get the instance of StockCSVServiceImpl.
   *
   * @return instance of the StockCSVServiceImpl
   */
  public static synchronized StockCSVServiceImpl getInstance() {
    if (instance == null) {
      instance = new StockCSVServiceImpl();
    }
    return instance;
  }

  @Override
  public void getStockDataFromAPI(String ticker) throws IOException {

    URL url = generateURL(ticker, FILE_TYPE);

    InputStream in;
    try {
      in = url.openStream();
      byte[] buffer = new byte[4096];
      int b = -1;
      if ((b = in.read(buffer)) != -1) {
        checkForErrorsInResult(ticker, buffer, b); //throws IllegalArgumentException
      }
      //if no errors then write to datastore
      FileOutputStream outputStream = new FileOutputStream(STOCK_DIR + ticker + "."
              + FILE_TYPE);
      do {
        outputStream.write(buffer, 0, b);
      }
      while ((b = in.read(buffer)) != -1);
    } catch (IllegalArgumentException ie) {
      throw new IllegalArgumentException(ie);
    } catch (IOException e) {
      throw new IOException("Unable to update data for " + ticker);
    }
  }

  @Override
  public StockDatePriceDTO getStockPriceByTicker(String stockSymbol)
          throws IOException, IllegalArgumentException {
    this.checkDownloadStockData(stockSymbol);

    try {
      String filePath = STOCK_DIR + "" + stockSymbol + ".csv";
      BufferedReader br = new BufferedReader(new FileReader(filePath));
      String row = "";

      br.readLine(); //skip header row

      //read only first row - latest data
      if ((row = br.readLine()) != null) {
        String[] stock = row.split(CSV_SEP);
        String dateStr = stock[TXN_DATE];
        StockDatePriceDTO res = new StockDatePriceDTO(stockSymbol,
                Float.parseFloat(stock[CLOSE_DAY]), dateStr);
        return res;
      }
    } catch (FileNotFoundException e) {
      throw new FileNotFoundException("Ticker data does not exist.");
    } catch (IOException e) {
      throw new IOException("Error occurred while reading file.");
    }
    throw new IOException("Something went wrong.");
  }

  @Override
  public void checkDownloadStockData(String stockSymbol) throws IOException {
    try {
      this.checkDataExistsForTicker(stockSymbol);
    } catch (IllegalArgumentException e) {
      this.getStockDataFromAPI(stockSymbol); //throw IllegalArgumentException
    }
  }

  @Override
  public void checkDataExistsForTicker(String ticker) throws IllegalArgumentException {
    File dir = new File(STOCK_DIR);
    if (dir != null && dir.list() != null) {
      List<String> p = List.of(dir.list());
      ticker = ticker.toUpperCase();
      if (!p.contains(ticker + ".csv")) {
        throw new IllegalArgumentException("Data not found for given stock : " + ticker);
      }
    } else {
      throw new IllegalArgumentException("User Database path is not configured correctly.");
    }
  }

  /**
   * Method to get the Stock Price for the given ticker on a given date from the database.
   * If date entered is when the market is close, method returns the value from the latest
   * available date.
   *
   * @param stockSymbol ticker symbol of the stock
   * @param date        (YYYY-MM-DD) date to look for
   * @return stock price of the given stock on the particular date
   * @throws IOException when file operations fail
   */
  @Override
  public float getStockValueOnCertainDate(String stockSymbol, LocalDate date)
          throws IOException {
    String filePath = STOCK_DIR + "" + stockSymbol + ".csv";

    DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    try {
      BufferedReader br = new BufferedReader(new FileReader(filePath));
      String row = "";
      br.readLine(); //skip header row

      LocalDate prevDate = now();
      float prevDateValue = 0.0F;
      while ((row = br.readLine()) != null) {
        String[] stock = row.split(CSV_SEP);
        LocalDate eachDate = LocalDate.parse(stock[0], format);

        if (date.isEqual(LocalDate.now())) {
          return (Float.parseFloat(stock[CLOSE_DAY]));
        }
        if (date.isEqual(eachDate)) {
          return (Float.parseFloat(stock[CLOSE_DAY]));
        }
        if (date.isBefore(prevDate) && date.isAfter(eachDate)) {
          return prevDateValue;
        }
        prevDate = LocalDate.parse(stock[0], format);
        prevDateValue = Float.parseFloat(stock[CLOSE_DAY]);
      }
    } catch (IOException e) {
      throw new IOException("Invalid file format. Please enter correct file.");
    }
    throw new IllegalArgumentException("Stock value not found for the given date for Ticker: "
            + stockSymbol);
  }

  @Override
  public float getStockActualPriceByDate(String stockSymbol, LocalDate date)
          throws IllegalArgumentException {
    try {
      String filePath = STOCK_DIR + "" + stockSymbol + ".csv";
      DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
      validateTwoDatesAfter(date, LocalDate.now());
      BufferedReader br = new BufferedReader(new FileReader(filePath));
      String row = "";
      br.readLine(); //skip header row

      //reading latest txn date
      if ((row = br.readLine()) != null) {
        String[] stock = row.split(CSV_SEP);
        LocalDate txnDate = LocalDate.parse(stock[TXN_DATE], format);
        validateTwoDatesAfter(date, txnDate);
        if (date.isEqual(txnDate)) {
          return (Float.parseFloat(stock[CLOSE_DAY]));
        }
      }

      //check older dates
      while ((row = br.readLine()) != null) {
        String[] stock = row.split(CSV_SEP);
        LocalDate txnDate = LocalDate.parse(stock[TXN_DATE], format);
        if (date.isEqual(txnDate)) {
          return (Float.parseFloat(stock[CLOSE_DAY]));
        }
      }
      throw new IllegalArgumentException("Stock data not available on " + date + ".");
    } catch (IOException e) {
      throw new IllegalArgumentException("Ticker details not available.");
    }
  }

  private void validateTwoDatesAfter(LocalDate actualDate, LocalDate compareDate) {
    if (actualDate.isAfter(compareDate)) {
      throw new IllegalArgumentException("Date details are not available.");
    }
  }
}
