package model;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import viewmodel.DCAStrategyViewModel;
import viewmodel.StockViewModel;
import viewmodel.StockViewModelFlex;
import viewmodel.UserViewModel;

/**
 * Interface that describes methods of User model.
 * Class performs operations on a list of Portfolios associated to that user.
 */
public interface UserModel extends UserViewModel {

  /**
   * Method to dollar cost averaging strategy for any portfolio to the user model.
   *
   * @param dcaPortfolio dca strategy view model with all the details needed for calculating to
   *                     the dollar cost averaging
   * @return DCAStrategyModel where the dollar cost averaging calculation will be done
   */
  DCAStrategyModel addDCAStrategyToList(DCAStrategyViewModel dcaPortfolio);

  /**
   * Adds new portfolio to the user.
   *
   * @param p portfolio to be added
   */
  void addPortfolio(PortfolioModel p);

  /**
   * Method to check if the user has existing portfolios. Returns true if the profile loaded
   * has portfolios, else false.
   *
   * @return true if the profile loaded has portfolios, else false
   */
  boolean portfoliosExist();

  /**
   * Method to check if the portfolio name given as input by the user exists for the profile.
   * Returns true if portfolio is present, else false.
   *
   * @param portfolioName name of the portfolio
   * @return true if a portfolio exists with the given name input, else false
   */
  boolean validatePortfolioNameDuplicate(String portfolioName);

  /**
   * Method to validate if the date input from the user on which they want to check the
   * portfolio value is correct or not.
   *
   * @param dateString date value given as input for getting the portfolio value
   * @return portfolioValueDate the date on which user wants to get the portfolio value
   */
  LocalDate validateDate(String dateString);

  /**
   * Method to calculate and return value of the portfolio on a certain date.
   *
   * @param date          date on which to get the portfolio value
   * @param portfolioName portfolio for which value needs to be calculated
   * @return value of the portfolio on the given date
   * @throws Exception when stock data is unavailable
   */
  float calculatePortfolioValue(LocalDate date, String portfolioName) throws Exception;

  /**
   * Method to calculate and return the composition of a stock (percentage of the stock)
   * in a portfolio.
   *
   * @param portfolioCompDate date to find composition on
   * @param portfolioName portfolio for which value needs to be calculated
   * @return hashmap storing the stock data and the percentage of that stock in a given portfolio
   * @throws Exception when stock data is unavailable
   */
  HashMap<String, Float> calculateComposition(String portfolioName,
                                              LocalDate portfolioCompDate) throws Exception;

  /**
   * Method loads list of portfolios and its collection of stocks from an external csv file or
   * from the file database and associates it to the given User.
   *
   * @param filePath path of the file to be loaded
   * @throws IOException when file operations fail
   */
  void loadUser(String filePath) throws IOException;

  /**
   * creates a new user profile and persists in the data store.
   *
   * @throws Exception when creation of new user fails
   */
  void createUser() throws Exception;

  /**
   * Checks if portfolio name format is valid or not.
   * Valid format is alphanumeric characters.
   *
   * @param pName name of the portfolio
   * @return if portfolio name is of valid format or not
   */
  boolean validatePortfolioNameFormat(String pName);

  /**
   * Method to save portfolio with all valid details given as input by the user.
   *
   * @param pName Name of the portfolio that will be saved
   * @param stockMap Map of stocks associated with that portfolio
   * @param flexType check whether portfolio is flexible or inflexible
   *
   * @throws IOException when stock data does not exist
   */
  void savePortfolio(String pName, HashSet<StockViewModel> stockMap, boolean flexType)
          throws IOException;

  /**
   * Adds new flexible portfolio to the user.
   *
   * @param p portfolio to be added
   */
  void addFlexPortfolio(PortfolioModel p);

  /**
   * Method to check if the portfolio name given as input by the user exists for the profile.
   * Returns 0,1,-1 depending on the condition.
   * 0 if portfolioName belongs to inflexible portfolios,
   * 1 if portfolioName belongs to flexible portfolio,
   * -1 if it does not exist in either.
   *
   * @param portfolioName portfolio name inputted by the user
   * @return 0 if portfolioName belongs to inflexible portfolios,
   *         1 if portfolioName belongs to flexible portfolio,
   *        -1 if it does not exist in either
   */
  int checkPortfolioType(String portfolioName);

  /**
   * Method to save a flexible portfolio.
   *
   * @param pName name of portfolio created or updated
   * @param stockSet list of stocks in the portfolio
   * @param b true if stock is bought, else false
   * @throws IOException when stock data is unavailable
   */
  void saveFlexPortfolio(String pName, List<StockViewModelFlex> stockSet, boolean b)
          throws IOException;

  /**
   * Method to calculate and return the cost basis (i.e. the total amount of money invested
   * in a portfolio) for a portfolio. Cost basis will only be calculated for flexible portfolios.
   *
   * @param portfolioName name of the portfolio
   * @param portfolioCostDate date of required to query
   * @return cost basis for a flexible portfolio
   */
  float getCostBasis(String portfolioName, LocalDate portfolioCostDate);

  /**
   * Adds a new flexible stock to the flexible portfolio.
   *
   * @param pName     portfolio Name
   * @param currStock stock object
   * @throws IOException when save operation fails
   */
  void saveFlexStock(String pName, StockViewModelFlex currStock) throws IOException;
}
