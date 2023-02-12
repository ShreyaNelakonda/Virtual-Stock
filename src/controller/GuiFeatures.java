package controller;

import javax.swing.JToggleButton;
import javax.swing.table.TableModel;

/**
 * Interface to pass the inputs given by the user on the graphical user interface to the
 * controller.
 */
public interface GuiFeatures {

  /**
   * Method to get the init option selected on the graphical user interface by the user.
   *
   * @param option the option selected by the user on the init menu
   */
  void getInitInput(String option);

  /**
   * Method to get the filepath chosen on the graphical user interface for external file upload.
   *
   * @param filePath path chosen from the gui
   */
  void getFilePath(String filePath);

  /**
   * Method to get the main menu option selected on the graphical user interface by the user.
   *
   * @param option the option selected by the user on the main menu
   */
  void getMainMenuInput(String option);

  /**
   * Method to get the ticker symbol given as input from the GUI.
   *
   * @param tickerSymbol ticker symbol of the stock
   */
  void getTickerInput(String tickerSymbol);

  /**
   * Method to get the input details for computing cost basis of  a portfolio on the given date
   * from the GUI.
   *
   * @param portfolioName name of the portfolio
   * @param year          year to check the cost basis
   * @param month         month to check the cost basis
   * @param day           day to check the cost basis
   */
  void getCostBasis(String portfolioName, String year, String month, String day);

  /**
   * Method to get the input details for calculating the value of a portfolio on the given date
   * from GUI.
   *
   * @param portfolioName name of the portfolio
   * @param year          year to check the cost basis
   * @param month         month to check the cost basis
   * @param day           day to check the cost basis
   */
  void getPortfolioValue(String portfolioName, String year, String month, String day);

  /**
   * Method to get input details to compute portfolio composition.
   *
   * @param flexType      type of portfolio - flexible or inflexible
   * @param portfolioName name of portfolio
   * @param year          year (YYYY) for which composition is required
   * @param month         month (MM) for which composition is required
   * @param day           day (DD) for which composition is required
   */
  void getComposition(JToggleButton flexType, String portfolioName,
                      String year, String month, String day);

  /**
   * Method to get the input details for creating a dollar cost averaging strategy in a new or
   * an existing portfolio from the gui.
   *
   * @param strategyStockData stock ticker and the percentage to be invested
   * @param portfolioName     name of the portfolio
   * @param investedAmount    amount to be invested in the strategy
   * @param commissionFee     commission fee for each transaction
   * @param frequencyLabel    frequency in which the amount should be invested
   * @param startDate         start date of strategy
   * @param endDate           end date of the strategy
   */
  void addDCAStrategy(TableModel strategyStockData, String portfolioName, String investedAmount,
                      String commissionFee, String frequencyLabel, String startDate,
                      String endDate);

  /**
   * Method to buy or sell more stocks to existing flexible portfolios.
   *
   * @param portfolioName name of the portfolio
   * @param tickerSymbol  ticker symbol associated with the stock
   * @param year          YYYY representation of year
   * @param month         MM representation of month
   * @param day           DD representation of the day
   * @param quantity      amount of stock to be bought/sold
   * @param commissionFee commission fee associated with the stock
   * @param buySell       true if stock is sold, otherwise false if bought
   */
  void buySellStock(String portfolioName, String tickerSymbol, String year, String month,
                    String day, String quantity, String commissionFee, String buySell);

  /**
   * Fetches the price data for the given ticker symbol on the given date.
   *
   * @param tickerSymbol ticker symbol associated with the stock
   * @param year         YYYY representation of year
   * @param month        MM representation of month
   * @param day          DD representation of the day
   */
  void getTickerPriceDate(String tickerSymbol, String year, String month, String day);

  /**
   * Method that retrieves stocks associated with the given portfolios.
   *
   * @param portfolioName name of the portfolio
   */
  void getPortfolioData(String portfolioName);

  /**
   * Method to invest amount in existing portfolio.
   *
   * @param portfolioName     name of the portfolio
   * @param investedAmount    amount to be invested in the stock
   * @param commissionFee     commission fee associated with the stock
   * @param year              YYYY representation of year
   * @param month             MM representation of month
   * @param day               DD representation of the day
   * @param stockWeightsTable tableModel data containing weights associated with each stock
   */
  void investInPortfolio(String portfolioName, String investedAmount, String commissionFee,
                         String year, String month, String day, TableModel stockWeightsTable);

  /**
   * Method to create a new flexible portfolio.
   *
   * @param portfolioName name of the portfolio
   * @param tickerSymbol  ticker symbol associated with the stock
   * @param year          YYYY representation of year
   * @param month         MM representation of month
   * @param day           DD representation of the day
   * @param quantity      amount of stock to be bought/sold
   * @param commissionFee commission fee associated with the stock
   * @param buySell       true if stock is sold, otherwise false if bought
   */
  void createFlexPortfolio(String portfolioName, String tickerSymbol, String year, String month,
                           String day, String quantity, String commissionFee, String buySell);

  /**
   * Method to get performance inputs from UI.
   *
   * @param portfolioName name of the portfolio
   * @param startDate     start date of the performance
   * @param endDate       end date of the performance
   */
  void getPerformance(String portfolioName, String startDate, String endDate);
}
