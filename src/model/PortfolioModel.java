package model;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;

import viewmodel.PortfolioViewModel;

/**
 * Interface that describes methods of Portfolio model.
 */
public interface PortfolioModel extends PortfolioViewModel {

  /**
   * Method to calculate the total quantity of all the stocks in a portfolio. Value will be fixed
   * for inflexible portfolio and will change depending on the date for a flexible portfolio.
   *
   * @param portfolioCompDate date on which composition is needed
   * @param stockBuyMap stock (bought) available in the portfolio as per the date
   * @param stockSellMap stock (sold) available in the portfolio as per the date
   * @return quantity of stocks present in a portfolio
   */
  float getTotalQuantity(LocalDate portfolioCompDate, HashMap<String, String> stockBuyMap,
                       HashMap<String, String> stockSellMap);

  /**
   * Method to calculate and return the composition of a stock (percentage of the stock)
   * in a portfolio.
   *
   * @param portfolioCompDate date on which composition is required
   * @return hashmap storing the stock data and the percentage of that stock in a given portfolio
   */
  HashMap<String, Float> getStockComposition(LocalDate portfolioCompDate);

  /**
   * Method to calculate and return value of the portfolio on a certain date by summing the
   * value of all the stocks in the portfolio on that date.
   *
   * @param date date on which to get the portfolio value
   * @return value of the portfolio on the given date
   * @throws Exception when unable to retrieve the value
   */
  float calculateStockValue(LocalDate date) throws Exception;

  /**
   * Method to calculate and return the cost basis (i.e. the total amount of money invested
   * in a portfolio) for a portfolio. Cost basis will only be calculated for flexible portfolios.
   *
   * @param portfolioCostDate date to be queried on
   * @return cost basis for a flexible portfolio
   * @throws IOException when unable to read data from the source
   */
  float calculateCostBasis(LocalDate portfolioCostDate) throws IOException;
}
