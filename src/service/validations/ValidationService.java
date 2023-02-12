package service.validations;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;

import model.StockModel;
import model.UserModel;
import viewmodel.StockViewModel;
import viewmodel.StockViewModelFlex;

/**
 * Interface to validate the date input and stock sets.
 */
public interface ValidationService {
  /**
   * Method to check if the date given as input by the user is not in the future and
   * is of the correct format (YYYY-MM-DD).
   *
   * @param dateString date given as input by the user in String format
   * @return date LocalDate instance of correct format date in YYYY-mm-dd
   * @throws IllegalArgumentException when format is incorrect
   */
  LocalDate validateDateFormat(String dateString) throws IllegalArgumentException;

  /**
   * It checks if current stock already exists or not.
   * If it does, then it combines and update the quantity.
   *
   * @param stockSet hashmap collection to store stocks added
   * @param price    current Price of the stock
   * @param ticker   ticker of the stock
   * @param qty      quantity of the stock
   */
  void composeInflexStocks(HashSet<StockViewModel> stockSet, float price,
                           String ticker, int qty);

  /**
   * It checks if current stock already exists or not.
   * If it does, then it combines and update the quantity and commission fee.
   *
   * @param stockMap  hashmap collection to store stocks added
   * @param stockData stock to be added
   */
  void composeFlexStocks(HashMap<String, StockModel> stockMap, StockViewModelFlex stockData);

  /**
   * Checks if the startDate and endDate are not future dates
   * and start date is before end date.
   *
   * @param startDate start date of time range
   * @param endDate   end date of time range
   */
  void validateStartEndDate(LocalDate startDate, LocalDate endDate);

  /**
   * Validates if transaction sequence is correct
   * i.e. there are enough stock to bought before selling.
   *
   * @param userModel userModel instance
   * @param stockData stock to be updated
   * @param pname     portfolio name
   */
  void transactionSequence(UserModel userModel, StockViewModelFlex stockData, String pname);

  /**
   * Checks if actual date is after a certain date.
   *
   * @param actualDate  date to be compared
   * @param compareDate date to be compared with
   */
  void validateTwoDatesAfter(LocalDate actualDate, LocalDate compareDate);

  /**
   * Method to validate portfolio name while creation. It checks whether the portfolio name
   * contains only alphanumeric values, and it is not a duplicate name.
   *
   * @param pName     name of the portfolio to be created
   * @param userModel model in which portfolio will be created
   * @return 1 if portfolio name is not alphanumeric, 2 if name is a duplicate
   *         and -1 if everything is fine
   */
  int validatePortfolioName(String pName, UserModel userModel);
}
