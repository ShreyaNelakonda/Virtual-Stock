import java.util.HashMap;

import model.PortfolioModel;
import model.PortfolioModelFlexImpl;
import model.PortfolioModelInflexImpl;
import model.StockModel;
import model.StockModelFlexImpl;
import model.StockModelImpl;

/**
 * A JUnit helper test class for creating a collection of stock objects and storing in a portfolio.
 */
public class TestHelper {

  /**
   * Returns a dummy list of flexible portfolio.
   *
   * @return dummy list of flexible portfolios
   */
  static HashMap<String, PortfolioModel> getFlexPortfolio() {
    HashMap<String, PortfolioModel> plist = new HashMap<>();
    HashMap<String, StockModel> slist = TestHelper.getStockListMock();
    plist.put("P1", new PortfolioModelFlexImpl("P1", slist));
    plist.put("P2", new PortfolioModelFlexImpl("P2", slist));
    plist.put("P3", new PortfolioModelFlexImpl("P3", slist));
    return plist;
  }

  /**
   * Returns a dummy list of inflexible portfolio.
   *
   * @return dummy list of inflexible portfolios
   */
  static HashMap<String, PortfolioModel> getInflexPortfolio() {
    HashMap<String, PortfolioModel> plist = new HashMap<>();
    HashMap<String, StockModel> slist = TestHelper.getStockListMock();
    plist.put("P1", new PortfolioModelInflexImpl("P1", slist));
    plist.put("P2", new PortfolioModelInflexImpl("P2", slist));
    plist.put("P3", new PortfolioModelInflexImpl("P3", slist));
    return plist;
  }

  /**
   * Returns a stockModel object containing list of stocks.
   *
   * @return dummy list of stock
   */
  protected static HashMap<String, StockModel> getStockListMock() {
    HashMap<String, StockModel> stockList = new HashMap<>();
    stockList.put("APPL", new StockModelImpl("AAPL", 112, 7));
    stockList.put("GOOG", new StockModelImpl("GOOG", 121, 2));
    return stockList;
  }

  /**
   * Returns list of valid tickers for stock.
   *
   * @return list of valid stock tickers
   */
  public static String[] validTickersFromFile() {
    String[] list = {"AAPL", "GOOG", "UBER", "C", "AMZN"};
    return list;
  }

  protected static HashMap<String, StockModel> getStockListMock1() {
    HashMap<String, StockModel> stockList = new HashMap<>();
    stockList.put("META", new StockModelImpl("META", 153, 9));
    stockList.put("GOOG", new StockModelImpl("GOOG", 98, 35));
    stockList.put("AAPL", new StockModelImpl("AAPL", 98, 6));
    stockList.put("NKLA", new StockModelImpl("NKLA", (float) 4.8, 25));
    return stockList;
  }

  protected static HashMap<String, StockModel> getStockListMock2() {
    HashMap<String, StockModel> stockList = new HashMap<>();
    stockList.put("GOOG", new StockModelImpl("GOOG", 113, 14));
    stockList.put("AAPL", new StockModelImpl("AAPL", (float) 99.23, 6));
    return stockList;
  }

  protected static HashMap<String, PortfolioModel> getPortfolioListMock() {
    HashMap<String, PortfolioModel> plist = new HashMap<>();
    plist.put("PORTFOLIO1", new PortfolioModelInflexImpl("PORTFOLIO1",
            TestHelper.getStockListMock1()));
    plist.put("PORTFOLIO2", new PortfolioModelInflexImpl("PORTFOLIO2",
            TestHelper.getStockListMock2()));
    return plist;
  }

  protected static HashMap<String, StockModel> getStockListMockFlex1() {
    HashMap<String, StockModel> stockList = new HashMap<>();
    stockList.put("META", new StockModelFlexImpl("META", 153, 9,
            "2019-07-10", true, (float) 4.7));
    stockList.put("GOOG", new StockModelFlexImpl("GOOG", 98, 12,
            "2018-09-10", true, (float) 3.9));
    stockList.put("AAPL", new StockModelFlexImpl("AAPL", 98, 14,
            "2009-12-26", true, (float) 2.2));
    stockList.put("AAPL", new StockModelFlexImpl("AAPL", (float) 4.8, 8,
            "2020-02-09", false, (float) 5.2));
    return stockList;
  }

  protected static HashMap<String, StockModel> getStockListMockFlex2() {
    HashMap<String, StockModel> stockList = new HashMap<>();
    stockList.put("GOOG", new StockModelFlexImpl("AAPL", 113, 14,
            "2020-07-10", true, (float) 3.8));
    stockList.put("GOOG", new StockModelFlexImpl("GOOG", (float) 99.23, 6,
            "2021-04-27", false, (float) 2.4));
    stockList.put("AAPL", new StockModelFlexImpl("AAPL", (float) 99.23, 6,
            "2021-11-16", true, (float) 6.2));
    return stockList;
  }

  protected static HashMap<String, PortfolioModel> getPortfolioListMockFlex() {
    HashMap<String, PortfolioModel> plist = new HashMap<>();
    plist.put("PORTFOLIOFLEX1", new PortfolioModelFlexImpl("PORTFOLIOFLEX1",
            TestHelper.getStockListMockFlex1()));
    plist.put("PORTFOLIOFLEX2", new PortfolioModelFlexImpl("PORTFOLIOFLEX2",
            TestHelper.getStockListMockFlex2()));
    return plist;
  }
}
