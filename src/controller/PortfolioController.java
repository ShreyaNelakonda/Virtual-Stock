package controller;

import java.io.PrintStream;
import java.util.Scanner;
import model.UserModel;
import view.TextView;

/**
 * PortfolioController Interface that provides methods that support Portfolio operations.
 */
public interface PortfolioController {

  /**
   * Creates a flexible portfolio for the given user.
   * @param userModel user where portfolio will be added
   * @param sc        input stream
   * @param out       output stream
   */
  void createFlexPortfolio(UserModel userModel, Scanner sc, PrintStream out);

  /**
   * Creates a new portfolio for the given user.
   *
   * @param userModel user where portfolio will be added
   * @param sc        input stream
   * @param out       output stream
   */
  void createInflexPortfolio(UserModel userModel, Scanner sc, PrintStream out);

  /**
   * Method to get the composition of a portfolio.
   * Composition displays the stock ticker, quantity and the percentage the stock holds in the
   * given portfolio.
   *
   * @param userModel model object from which the portfolio composition is needed
   * @param sc        input stream
   * @param out       output stream
   */
  void checkPortfolioComposition(UserModel userModel, Scanner sc,
                                 PrintStream out);

  /**
   * Method to get the value of all the stocks in a portfolio on a certain date.
   *
   * @param userModel model object from which the portfolio values for a certain date is needed
   * @param sc        Input Stream
   * @param out       Output Stream
   * @throws Exception when value does not exist
   */
  void getPortfolioValue(UserModel userModel, Scanner sc, PrintStream out) throws Exception;

  /**
   * Method to update (buy/sell) stocks in a flexible portfolio.
   *
   * @param userModel model object from which the flexible portfolio must be updated
   * @param sc        Input Stream
   * @param out       Output Stream
   * @param view      view class
   */
  void updateFlexPortfolio(UserModel userModel, Scanner sc, PrintStream out, TextView view);

  /**
   * Method to get the performance of a portfolio for a given period of time.
   *
   * @param userModel model object for the portfolio for which performance needs to plotted
   * @param sc        Input Stream
   * @param out       Output Stream
   * @param view      view class
   */
  void portfolioPerformance(UserModel userModel, Scanner sc, PrintStream out, TextView view);

  /**
   * Method to determine the cost basis (i.e. the total amount of money invested in a portfolio).
   *
   * @param userModel model object from which the portfolio values for a certain date is needed
   * @param sc        Input Stream
   * @param out       Output Stream
   */
  void getCostBasis(UserModel userModel, Scanner sc, PrintStream out);
}
