package view;

import java.io.PrintStream;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import dto.StockDatePriceDTO;
import dto.TimeStampBinsDTO;
import model.PortfolioModel;
import viewmodel.StockViewModel;
import viewmodel.StockViewModelFlex;

/**
 * View Interface to display various output messages to the user based on operations.
 */
public interface TextView {
  /**
   * displays initial profile loading options.
   *
   * @param ps output stream
   */
  void displayInitProfile(PrintStream ps);

  /**
   * displays input error.
   *
   * @param ps output stream
   */
  void displayInputError(PrintStream ps);

  /**
   * displays exit message.
   *
   * @param ps output stream
   */
  void displayExit(PrintStream ps);


  /**
   * Displays common Menu option to the user.
   *
   * @param ps output stream
   */
  void displayMainMenuCommonOption(PrintStream ps);

  /**
   * displays list of portfolios.
   *
   * @param ps               output stream
   * @param flexPortfolios   list of flexible portfolios
   * @param inflexPortfolios list of inflexible portfolios
   */
  void displayPortfolios(PrintStream ps, HashMap<String, PortfolioModel> flexPortfolios,
                         HashMap<String, PortfolioModel> inflexPortfolios);

  /**
   * Method takes the Date on which portfolio value needs to be checked as input from the user.
   *
   * @param operation type of operation
   * @param ps OutputStream for date
   */
  void getPortfolioValueDate(String operation, PrintStream ps);

  /**
   * Method displays the value of all the stocks in that portfolio to the user.
   *
   * @param ps             OutputStream for value
   * @param date           date on which value is being checked
   * @param portfolioValue value of all the stocks in that portfolio
   * @param portfolioType  type of portfolio
   */
  void displayPortfolioValue(PrintStream ps, float portfolioValue, LocalDate date,
                             String portfolioType);

  /**
   * Method displays a message to the user if the portfolio name entered does not exist.
   *
   * @param portfolioNameValue portfolio name taken as an input from the user
   * @param ps                 OutputStream for value
   */
  void portfolioDoesNotExist(String portfolioNameValue, PrintStream ps);

  /**
   * Method displays a message to the user if the portfolio name entered does not exist.
   *
   * @param ps OutputStream for value
   */
  void portfolioListEmpty(PrintStream ps);

  /**
   * displays input for stock ticker symbol.
   *
   * @param ps output stream
   */
  void displayGetStockTicker(PrintStream ps);

  /**
   * displays portfolioName input and error.
   *
   * @param err error type (-1: no error)
   * @param out output stream
   */
  void displayGetPortfolioName(int err, PrintStream out);

  /**
   * displays latest stock price.
   *
   * @param result object that contains ticker, date and price
   * @param out    output stream
   */
  void displayCurrStockPrice(StockDatePriceDTO result, PrintStream out);

  /**
   * displays quantity input.
   *
   * @param err error
   * @param out output stream
   */
  void displayGetQuantity(boolean err, PrintStream out);

  /**
   * displays add more stock options.
   *
   * @param out output stream
   */
  void displayAddMoreStock(PrintStream out);

  /**
   * displays saving portfolio options.
   *
   * @param stockList list of stocks
   * @param out       output stream
   */
  void displayBuyStock(HashSet<StockViewModel> stockList, PrintStream out);

  /**
   * displays portfolio not saved error.
   *
   * @param out output stream
   */
  void displayPortfolioNotSaved(PrintStream out);

  /**
   * Method takes the path of the file that needs to be loaded as an input from the user.
   *
   * @param ps OutputStream
   */
  void getFilepath(PrintStream ps);

  /**
   * Method to display the list of stocks present in the given portfolio.
   *
   * @param ps               OutputStream
   * @param stockComposition List of Stocks present in a portfolio
   * @param portfolioType type of the portfolio
   */
  void displayPortfolioComposition(PrintStream ps,
                                   HashMap<String, Float> stockComposition,
                                   String portfolioType);

  /**
   * Method to display that file upload was successful.
   *
   * @param load type
   * @param ps   OutputStream
   */
  void fileLoadSuccess(String load, PrintStream ps);

  /**
   * displays exception message.
   *
   * @param msg message
   * @param out output stream
   */
  void displayExceptionMessage(PrintStream out, String msg);

  /**
   * displays user created.
   *
   * @param out output stream
   */
  void displayUserCreated(PrintStream out);

  /**
   * Method to display that date entered is in the future.
   *
   * @param ps OutputStream
   */
  void futureDate(PrintStream ps);

  /**
   * Method to display that date entered has incorrect format.
   *
   * @param ps OutputStream
   */
  void invalidDateFormat(PrintStream ps);

  /**
   * Method to display that stock data is not available for given date.
   *
   * @param ps           OutputStream
   * @param date         date for getting the portfolio value
   * @param tickerSymbol stock symbol which doesn't the value available for the given date
   */
  void displayDataNotFound(String tickerSymbol, LocalDate date, PrintStream ps);

  /**
   * Method to display that user cannot buy fractional shares.
   *
   * @param ps OutputStream
   */
  void errorFractionalShares(PrintStream ps);

  /**
   * Method to display that the existing profile from which data needs to be loaded is empty.
   *
   * @param ps OutputStream
   */
  void errorInvalidFile(PrintStream ps);

  /**
   * displays quantity error.
   *
   * @param out output stream
   */
  void displayQuantityLessZero(PrintStream out);

  /**
   * displays portfolio saved.
   *
   * @param out output stream
   */
  void displayPortfolioSaved(PrintStream out);

  /**
   * displays portfolio type options.
   *
   * @param out output stream
   */
  void displayChoosePortfolioType(PrintStream out);

  /**
   * displays date of transaction.
   *
   * @param out output stream
   */
  void displayDateOfTransaction(PrintStream out);


  /**
   * displays type of transaction.
   *
   * @param out output stream
   */
  void displayTxnType(PrintStream out);

  /**
   * Display errors in commission fee, date format and stock value not available on given date
   * while file upload.
   *
   * @param errorType type of error to display message
   * @param out       output stream
   */
  void displayFileUploadInvalidData(String errorType, PrintStream out);

  /**
   * displays commission fee input.
   *
   * @param out output stream
   */
  void displayCommissionFee(PrintStream out);

  /**
   * displays flex stock buy query.
   *
   * @param stock stock to be displayed
   * @param out output stream
   */
  void displayFlexBuyStock(StockViewModelFlex stock, PrintStream out);

  /**
   * displays portfolio error.
   *
   * @param msg message string to be displayed
   * @param out output stream
   */
  void displayPortfolioError(PrintStream out, String msg);

  /**
   * Display error for cost basis not available for inflexible portfolios.
   *
   * @param out output stream
   */
  void costBasisInflexibleError(PrintStream out);

  /**
   * Display cost basis (i.e. the total amount of money invested in a portfolio) for
   * flexible portfolios.
   *
   * @param costBasis         cost basis (i.e. the total amount of money invested in a portfolio)
   * @param portfolioName     name of the portfolio for which cost basis is being calculated
   * @param portfolioCostDate date on which cost basis needs to be computed
   * @param out               output stream
   */
  void displayCostBasis(float costBasis, String portfolioName, LocalDate portfolioCostDate,
                        PrintStream out);

  /**
   * displays timestamps range input.
   *
   * @param out output stream
   */
  void displayTimeStampRangeType(PrintStream out);

  /**
   * displays year input.
   *
   * @param type string : start or end
   * @param out output stream
   */
  void displayYear(PrintStream out, String type);

  /**
   * displays month input.
   * @param type start or end
   * @param out output stream
   */
  void displayMonth(PrintStream out, String type);

  /**
   * displays day input.
   *
   * @param type start or end
   * @param out output stream
   */
  void displayDay(PrintStream out, String type);

  /**
   * displays add more flex stock input.
   *
   * @param out output stream
   */
  void displayAddMoreFlexStock(PrintStream out, String operation);

  /**
   * displays performance chart title.
   *
   * @param pName     portfolio name
   * @param startDate start of time range
   * @param endDate   end of time range
   * @param out       output stream
   */
  void displayPerformanceTile(String pName, String startDate, String endDate,
                              PrintStream out);

  /**
   * Generates performance graph for given list of stocks.
   *
   * @param out            output stream
   * @param performanceVal list of value of stocks
   * @param timeStampBinsDTO  DTO of timestamp range and type
   * @param stepSize       scaling factor
   * @param minPrice       minimum price
   */
  void displayPerformanceChart(String pName, PrintStream out, List<Float> performanceVal,
                               TimeStampBinsDTO timeStampBinsDTO, int stepSize, float minPrice);
}
