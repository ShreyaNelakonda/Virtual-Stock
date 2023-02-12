package service.user;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import model.PortfolioModel;
import model.PortfolioModelFlexImpl;
import model.PortfolioModelInflexImpl;
import model.StockModel;
import model.StockModelFlexImpl;
import model.StockModelImpl;
import service.stocks.StockCSVServiceImpl;
import service.validations.ValidationServiceImpl;
import viewmodel.PortfolioViewModel;
import viewmodel.StockViewModel;

/**
 * CSV implementation to access UserDB.
 */
public class UserCSVServiceImpl implements UserDataService {

  private static final String USER_DIR = "database/UserDB/UserProfile.csv";
  private static final int P_NAME = 0;
  private static final int TICKER = 1;
  private static final int QTY = 2;
  private static final int FLEX = 3;
  private static final int TRANSACTION_DATE = 4;
  private static final int BUY = 6;
  private static final int COMMISSION_FEE = 5;
  private static final String CSV_SEP = ",";

  @Override
  public void createOverrideEmptyFile() throws IOException {
    try {
      String path = USER_DIR;
      File file = new File(path);
      if (file.exists()) {
        file.delete();
      }
      file.createNewFile();
    } catch (IOException e) {
      throw new IOException("Unable to create a new profile.");
    }
  }

  @Override
  public void loadExistingPortfolio() {
    try {
      BufferedReader br = new BufferedReader(new FileReader(USER_DIR));
      String row;
      HashMap<String, PortfolioViewModel> portfolioListLoaded = new HashMap();
      HashSet<StockViewModel> stockListLoaded = new HashSet<>();
      while ((row = br.readLine()) != null) {
        String[] stock = row.split(CSV_SEP);
        String portfolioName = stock[P_NAME].toUpperCase();
        String tickerSymbol = stock[TICKER].toUpperCase();
        try {
          StockCSVServiceImpl.getInstance().checkDataExistsForTicker(tickerSymbol);
        } catch (IllegalArgumentException e) {
          //if invalid then skip : do not add stock to the list
          continue;
        }
        int quantity = Integer.parseInt(stock[QTY]);
        float currentPrice = StockCSVServiceImpl.getInstance()
                .getStockPriceByTicker(tickerSymbol).getPrice();

        new ValidationServiceImpl().composeInflexStocks(stockListLoaded, currentPrice,
                tickerSymbol, quantity);

        if (portfolioListLoaded.containsKey(portfolioName)) {
          //TODO
        }

      }
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Persists portfolio created into the User profile in the file database.
   *
   * @param portfolio portfolio object to be saved
   * @param update    files needs to be updated or overwritten
   * @throws IOException when IO operation fails while accessing / reading the file
   */
  @Override
  public void savePortfolioToFile(PortfolioViewModel portfolio, boolean flexType, boolean update)
          throws IOException {
    try {
      FileWriter fw = new FileWriter(USER_DIR, update);
      BufferedWriter bw = new BufferedWriter(fw);

      String pName = portfolio.getPortfolioName();
      for (StockModel stock : portfolio.getStockMap().values()) {
        String row = pName
                + CSV_SEP + stock.getTickerSymbol();
        if (flexType) {
          row = row + CSV_SEP + (float) Math.round(stock.getStockQuantity() * 100.0) / 100.0;
        } else {
          row = row + CSV_SEP + (int) stock.getStockQuantity();
        }
        // + CSV_SEP + stock.getInvestedPrice()
        row = row + CSV_SEP + flexType;
        if (flexType) {
          row = row + CSV_SEP + stock.getTransactionDate()
                  + CSV_SEP + stock.getCommissionFee()
                  + CSV_SEP + stock.getBuy();
        }
        bw.write(row);
        bw.newLine();
      }
      bw.flush();
      bw.close();
      fw.close();
    } catch (IOException e) {
      throw new IOException("Something went wrong. Changes not saved.");
    }
  }

  /**
   * Method loads list of portfolios and its collection of stocks
   * from an external csv file or from the file database
   * and associates it to the given User.
   *
   * @param filePath external file location, if null then load from internal data store
   * @throws IOException when file operations fail
   */
  @Override
  public HashMap<String, PortfolioModel> loadUserProfile(String filePath) throws IOException {
    if (filePath == null) {
      filePath = USER_DIR;
    }

    HashMap<String, PortfolioModel> portfolioMapFromFile = new HashMap<>();

    try {
      String temp = null;
      BufferedReader br = new BufferedReader(new FileReader(filePath));

      String line = "";
      String portfolioName = null;
      while ((line = br.readLine()) != null) {
        String[] stock = line.split(CSV_SEP);
        portfolioName = stock[P_NAME].toUpperCase();
        String tickerSymbol = stock[TICKER].toUpperCase();

        boolean validTicker;
        try {
          try {
            StockCSVServiceImpl.getInstance().checkDataExistsForTicker(tickerSymbol);
          } catch (Exception e) {
            StockCSVServiceImpl.getInstance().getStockDataFromAPI(tickerSymbol);
          }
          validTicker = true;
        } catch (IllegalArgumentException e) {
          validTicker = false;
        }

        if (validTicker) {
          //float quantity = Integer.parseInt(stock[2]);
          float currentPrice;

          HashMap<String, StockModel> stockMap = new HashMap<>();
          List<StockModel> stockListFlex = new ArrayList<>();
          if (portfolioMapFromFile.containsKey(portfolioName)) {
            PortfolioModel port = portfolioMapFromFile.get(portfolioName);
            stockMap = port.getStockMap();
          }
          final boolean flex = Boolean.parseBoolean(stock[FLEX]);
          if (flex) {
            String transactionDate = String.valueOf(stock[TRANSACTION_DATE]);
            boolean buy = Boolean.parseBoolean(stock[BUY]);
            float commissionFee = Float.parseFloat(stock[COMMISSION_FEE]);
            float quantity = Float.parseFloat(stock[QTY]);

            DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate txnDate = LocalDate.parse(transactionDate, format);
            currentPrice = StockCSVServiceImpl.getInstance()
                    .getStockValueOnCertainDate(tickerSymbol, txnDate);

            stockListFlex.add(new StockModelFlexImpl(tickerSymbol, currentPrice, quantity,
                    transactionDate, buy, commissionFee));
            stockMap = composeStocks(stockMap, currentPrice, tickerSymbol, quantity, flex,
                    transactionDate, buy, commissionFee);
            portfolioMapFromFile.put(portfolioName, new PortfolioModelFlexImpl(portfolioName,
                    stockMap));
          } else {
            float quantity = Integer.parseInt(stock[QTY]);
            if (portfolioMapFromFile.containsKey(portfolioName)) {
              PortfolioModel port = portfolioMapFromFile.get(portfolioName);
              stockMap = port.getStockMap();
            }
            currentPrice = StockCSVServiceImpl.getInstance()
                    .getStockPriceByTicker(tickerSymbol).getPrice();
            stockMap = composeStocks(stockMap, currentPrice, tickerSymbol, (int) quantity, flex,
                    null, false, 0);
            portfolioMapFromFile.put(portfolioName,
                    new PortfolioModelInflexImpl(portfolioName, stockMap));
          }
        }
      }

    } catch (IOException e) {
      throw new IOException("Invalid file format. Please enter correct file");
    } catch (NumberFormatException e) {
      throw new NumberFormatException("Cannot buy fractional shares. Please buy whole shares.");
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException(e);
    } catch (DateTimeException e) {
      throw new DateTimeException("Invalid date format.");
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return portfolioMapFromFile;
  }

  /**
   * It checks if current stock already exists in the given portfolio list.
   * If it does, then it combines and update the quantity for that stock.
   *
   * @param stockMap        hashmap collection to store stocks added
   * @param currPrice       current Price of the stock
   * @param ticker          ticker of the stock
   * @param qty             quantity of the stock
   * @param flex            portfolio type is flexible or inflexible
   * @param transactionDate date of transcation for the stock
   * @param buy             stocks were bought/sold for that portfolio
   * @param commissionFee   commission fee for that stock transaction
   */
  private HashMap<String, StockModel> composeStocks(HashMap<String, StockModel> stockMap,
                                                    float currPrice, String ticker, float qty,
                                                    boolean flex, String transactionDate,
                                                    boolean buy, float commissionFee) {
    if (flex) {
      stockMap.put(ticker + "" + transactionDate + "" + buy, new StockModelFlexImpl(ticker,
              currPrice, qty,
              transactionDate, buy, commissionFee));
    } else {
      if (!stockMap.containsKey(ticker)) {
        stockMap.put(ticker, new StockModelImpl(ticker, currPrice, qty));
      } else {
        //already exists
        StockModel existingStock = stockMap.get(ticker);
        StockModel newStock;
        newStock = new StockModelImpl(ticker, currPrice,
                qty + existingStock.getStockQuantity());
        stockMap.put(ticker, newStock);
      }
    }
    return stockMap;
  }
}
