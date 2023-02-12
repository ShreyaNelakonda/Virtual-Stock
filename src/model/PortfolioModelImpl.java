package model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;


/**
 * This class represents a single Portfolio added by the user.
 */
public abstract class PortfolioModelImpl implements PortfolioModel {
  private final String portfolioName;

  private final HashMap<String, StockModel> stockMap;

  private final Date creationDate;


  /**
   * Constructor for Portfolio that takes portfolioName and list of stocks as parameters.
   *
   * @param portfolioName name of the portfolio
   * @param stockMap      list of stocks in portfolio
   * @throws IllegalArgumentException when validation of parameters fails
   */
  public PortfolioModelImpl(String portfolioName, HashMap<String, StockModel> stockMap)
          throws IllegalArgumentException {
    validatePortfolioName(portfolioName);
    validateStockList(stockMap);

    this.portfolioName = portfolioName.toUpperCase();
    this.stockMap = stockMap;
    this.creationDate = new Date();

  }

  /**
   * Checks if portfolioName is empty or null.
   *
   * @param portfolioName name of the portfolio
   * @throws IllegalArgumentException when portfolioName is empty or null
   */
  private void validatePortfolioName(String portfolioName) throws IllegalArgumentException {
    if (null == portfolioName) {
      throw new IllegalArgumentException("Portfolio name is required.");
    }
    if (portfolioName.isEmpty()) {
      throw new IllegalArgumentException("Portfolio name cannot be empty.");
    }
  }

  /**
   * Checks if stockList is empty or null.
   *
   * @param stockMap list of stocks
   * @throws IllegalArgumentException when stockList is empty or null
   */
  private void validateStockList(HashMap<String, StockModel> stockMap)
          throws IllegalArgumentException {
    if (null == stockMap) {
      throw new IllegalArgumentException("Stock List is required.");
    }
    if (stockMap.isEmpty()) {
      throw new IllegalArgumentException("Stock List cannot be empty.");
    }
  }

  /**
   * Returns portfolioName of the given portfolio.
   *
   * @return portfolioName of the portfolio
   */
  @Override
  public String getPortfolioName() {
    return this.portfolioName;
  }

  /**
   * Returns stockList of the given portfolio.
   *
   * @return list of stock in the portfolio
   */
  @Override
  public HashMap<String, StockModel> getStockMap() {
    return this.stockMap;
  }

  /**
   * Returns date when the portfolio was created.
   *
   * @return date as string in "yyyy-MM-dd" format
   */
  public String getCreationDate() {
    return new SimpleDateFormat("yyyy-MM-dd").format(this.creationDate);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PortfolioModelImpl that = (PortfolioModelImpl) o;
    return portfolioName.equalsIgnoreCase(that.portfolioName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(portfolioName);
  }
}
