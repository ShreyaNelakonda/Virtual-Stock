package viewmodel;

import java.util.HashMap;

import model.StockModel;

/**
 * Provides read only methods to access Portfolio Model.
 */
public interface PortfolioViewModel {

  /**
   * Returns portfolioName as string of the given portfolio.
   *
   * @return portfolioName of the portfolio
   */
  String getPortfolioName();

  /**
   * Returns stockMap of the given portfolio.
   *
   * @return hashmap of stock in the portfolio
   */
  HashMap<String, StockModel> getStockMap();
}
