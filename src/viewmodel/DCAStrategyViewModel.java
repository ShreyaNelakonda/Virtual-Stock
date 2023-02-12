package viewmodel;

import java.time.LocalDate;
import java.util.List;

import model.DCAStockRatio;

/**
 * View Model Interface to compute the dollar cost averaging strategy for given portfolio.
 * This is a read-only version of the DCAStrategyModel.
 */
public interface DCAStrategyViewModel {

  /**
   * Method to get the name of the portfolio for which dollar cost average strategy will be added.
   *
   * @return the name of the portfolio
   */
  String getPortfolioName();

  /**
   * Method to get a map of stocks and the percentage of amount to be invested in that stock.
   *
   * @return list of DCAStockRatio which stores the stock ticker and the percentage
   */
  List<DCAStockRatio> getStockMap();

  /**
   * Method to get the start date of the dollar cost averaging strategy.
   * @return start date of the dollar cost average strategy
   */
  LocalDate getStartDate();

  /**
   * Method to get the end date of the dollar cost averaging strategy. If end date is null,
   * then the strategy is ongoing.
   * @return end date of the dollar cost average strategy
   */
  LocalDate getEndDate();

  /**
   * Method to get the amount invested in the strategy.
   * @return invested amount in a strategy
   */
  double getInvestedAmount();

  /**
   * Method to get the frequency in days at which stocks should be invested the portfolio.
   * @return the frequency in days between each investment
   */
  int getFrequencyDays();

  /**
   * Method to get the commission fee for the investment transaction. Commission fee would be the
   * same for each transaction in the strategy.
   * @return commission fee for each investment transaction
   */
  float getCommissionFee();
}
