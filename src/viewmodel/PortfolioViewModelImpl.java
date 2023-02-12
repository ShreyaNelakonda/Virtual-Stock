package viewmodel;

import java.util.HashMap;

import model.StockModel;

/**
 * Read Only implementation for Portfolio Model methods.
 */
public class PortfolioViewModelImpl implements PortfolioViewModel {

  private final PortfolioViewModel portfolio;

  public PortfolioViewModelImpl(PortfolioViewModel portfolio) {
    this.portfolio = portfolio;
  }

  /**
   * Returns portfolioName of the given portfolio.
   *
   * @return portfolioName of the portfolio
   */
  @Override
  public String getPortfolioName() {
    return portfolio.getPortfolioName();
  }

  /**
   * Returns stockList of the given portfolio.
   *
   * @return list of stock in the portfolio
   */
  @Override
  public HashMap<String, StockModel> getStockMap() {
    return portfolio.getStockMap();
  }

}
