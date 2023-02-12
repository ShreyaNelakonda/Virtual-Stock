package model;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;

import service.stocks.StockCSVServiceImpl;

/**
 * PortfolioModelInflexImpl implements methods related to Inflexible Portfolios.
 */
public class PortfolioModelInflexImpl extends PortfolioModelImpl {

  /**
   * Constructor for Portfolio that takes portfolioName and list of stocks as parameters.
   *
   * @param portfolioName name of the portfolio
   * @param stockMap      list of stocks in portfolio
   * @throws IllegalArgumentException when validation of parameters fails
   */
  public PortfolioModelInflexImpl(String portfolioName, HashMap<String, StockModel> stockMap)
          throws IllegalArgumentException {
    super(portfolioName, stockMap);
  }

  /**
   * Method to calculate and return value of the portfolio on a certain date by summing the
   * value of all the stocks in the portfolio on that date.
   *
   * @param date date on which to get the portfolio value
   * @return value of the portfolio on the given date
   */
  @Override
  public float calculateStockValue(LocalDate date) throws IOException {

    float portfolioValueCertainDate = 0;
    float valueOfEachStock = 0;
    HashMap<String, StockModel> stocks = this.getStockMap();
    for (StockModel stock : stocks.values()) {
      String tickerSymbol = stock.getTickerSymbol();
      try {
        valueOfEachStock = StockCSVServiceImpl.getInstance()
                .getStockValueOnCertainDate(tickerSymbol, date);
        portfolioValueCertainDate = portfolioValueCertainDate
                + (valueOfEachStock * stock.getStockQuantity());
      } catch (IllegalArgumentException e) {
        throw new IOException(e);
      } catch (IOException e) {
        throw new IOException(e);
      }
    }

    return portfolioValueCertainDate;
  }

  /**
   * Method to calculate and return the cost basis (i.e. the total amount of money invested
   * in a portfolio) for a portfolio. Cost basis will only be calculated for flexible portfolios.
   *
   * @return cost basis for a flexible portfolio
   */
  @Override
  public float calculateCostBasis(LocalDate portfolioCostDate) {
    throw new IllegalArgumentException("Cost Basis can only be calculated for flexible "
            + "portfolios.");
  }

  /**
   * Method to calculate and return the composition of a stock (percentage of the stock)
   * in a portfolio.
   *
   * @return hashmap storing the stock data and the percentage of that stock in a given portfolio
   */
  @Override
  public HashMap<String, Float> getStockComposition(LocalDate portfolioCompDate) {
    float totalStockQuantity = this.getTotalQuantity(portfolioCompDate, null,
            null);
    HashMap<String, Float> stockComposition = new HashMap<>();
    for (StockModel stock : this.getStockMap().values()) {
      float comp = (stock.getStockQuantity() / totalStockQuantity) * 100;
      String t = stock.getTickerSymbol() + " " + stock.getStockQuantity();
      stockComposition.put(t, comp);
    }
    return stockComposition;
  }

  /**
   * Method to calculate the total quantity of all the stocks in a portfolio. Value will be fixed
   * for inflexible portfolio and will change depending on the date for a flexible portfolio.
   *
   * @return quantity of stocks present in a portfolio
   */
  @Override
  public float getTotalQuantity(LocalDate portfolioCompDate, HashMap<String, String> stockBuyMap,
                              HashMap<String, String> stockSellMap) {
    float totalStockQuantity = 0;
    for (StockModel stock : this.getStockMap().values()) {
      totalStockQuantity = (int) (totalStockQuantity + stock.getStockQuantity());
    }
    return totalStockQuantity;
  }
}
