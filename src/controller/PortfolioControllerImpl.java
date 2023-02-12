package controller;

import java.io.IOException;
import java.io.PrintStream;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Month;
import java.time.MonthDay;
import java.time.Year;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

import dto.StockDatePriceDTO;
import dto.TimeStampBinsDTO;
import model.StockModelFlexImpl;
import model.UserModel;
import service.stocks.StockCSVServiceImpl;
import service.stocks.StocksDataService;
import service.validations.ValidationServiceImpl;
import view.TextView;
import view.TextViewConsoleImpl;
import viewmodel.StockViewModel;
import viewmodel.StockViewModelFlex;

import static java.time.temporal.TemporalAdjusters.firstDayOfMonth;
import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;
import static java.time.temporal.TemporalAdjusters.lastDayOfYear;

/**
 * PortfolioControllerImpl for PortfolioController that describes its implementation.
 */
public class PortfolioControllerImpl implements PortfolioController {

  private final StocksDataService stockService = StockCSVServiceImpl.getInstance();

  private final ValidationServiceImpl validationService = new ValidationServiceImpl();

  /**
   * Method to get the value of all the stocks in a portfolio on a certain date.
   *
   * @param userModel model object from which the portfolio values for a certain date is needed
   * @param sc        Input Stream
   * @param out       Output Stream
   */
  @Override
  public void getPortfolioValue(UserModel userModel, Scanner sc, PrintStream out) {
    TextView view = new TextViewConsoleImpl();
    if (!userModel.portfoliosExist()) {
      view.portfolioListEmpty(out);
    } else {
      view.displayGetPortfolioName(-1, out);
      String portfolioName = sc.next().toUpperCase();
      if (userModel.checkPortfolioType(portfolioName) != -1) {
        String portfolioType = getPortfolioType(userModel.checkPortfolioType(portfolioName));
        boolean validDateInput;
        do {
          validDateInput = validDateInput(userModel, sc, out, view, portfolioName, portfolioType);
        }
        while (!validDateInput);
      } else {
        view.portfolioDoesNotExist(portfolioName, out);
      }
    }
  }

  /**
   * Method to get the composition of a portfolio.
   * Composition displays the stock ticker, quantity and the percentage the stock holds in the
   * given portfolio.
   *
   * @param userModel model object from which the portfolio composition is needed
   * @param sc        input stream
   * @param out       output stream
   */
  @Override
  public void checkPortfolioComposition(UserModel userModel, Scanner sc,
                                        PrintStream out) {
    TextView view = new TextViewConsoleImpl();

    if (!userModel.portfoliosExist()) {
      view.portfolioListEmpty(out);
    } else {
      view.displayGetPortfolioName(-1, out);
      String portfolioName = sc.next().toUpperCase();
      HashMap<String, Float> stockComposition;
      if (userModel.checkPortfolioType(portfolioName) != -1) {
        LocalDate portfolioCompDate = null;
        String portfolioType = getPortfolioType(userModel.checkPortfolioType(portfolioName));
        if (userModel.checkPortfolioType(portfolioName) == 1) {
          boolean validDateInput;
          String dateString;
          do {
            view.getPortfolioValueDate("Composition", out);
            dateString = sc.next();
            try {
              portfolioCompDate = userModel.validateDate(dateString);
              validDateInput = true;
            } catch (DateTimeException e) {
              view.invalidDateFormat(out);
              validDateInput = false;
            } catch (IllegalArgumentException e) {
              if (e.getMessage().contains("The date should not be a future date")) {
                view.futureDate(out);
              }
              validDateInput = false;
            }
          }
          while (!validDateInput);
        }
        try {
          stockComposition = userModel.calculateComposition(portfolioName, portfolioCompDate);
          view.displayPortfolioComposition(out, stockComposition, portfolioType);
        } catch (Exception e) {
          view.displayExceptionMessage(out, "Something went wrong. Please try again");
        }
      } else {
        view.portfolioDoesNotExist(portfolioName, out);
      }
    }
  }

  @Override
  public void updateFlexPortfolio(UserModel userModel, Scanner sc, PrintStream out,
                                  TextView view) {
    final String pName = getFlexPortfolioName(userModel, sc, out, view);

    updateFlexPortfolioByName(pName, userModel, sc, out, view);
  }

  /**
   * Method to determine the cost basis (i.e. the total amount of money invested in a portfolio).
   *
   * @param userModel model object from which the portfolio values for a certain date is needed
   * @param sc        Input Stream
   * @param out       Output Stream
   */
  @Override
  public void getCostBasis(UserModel userModel, Scanner sc, PrintStream out) {
    TextView view = new TextViewConsoleImpl();
    float costBasis;
    if (!userModel.portfoliosExist()) {
      view.portfolioListEmpty(out);
    } else {
      view.displayGetPortfolioName(-1, out);
      String portfolioName = sc.next().toUpperCase();
      if (userModel.checkPortfolioType(portfolioName) != -1) {
        LocalDate portfolioCostDate = null;
        String portfolioType = getPortfolioType(userModel.checkPortfolioType(portfolioName));
        if (userModel.checkPortfolioType(portfolioName) == 1) {
          boolean validDateInput;
          String dateString;
          do {
            view.getPortfolioValueDate("Cost Basis", out);
            dateString = sc.next();
            try {
              portfolioCostDate = userModel.validateDate(dateString);
              validDateInput = true;
            } catch (DateTimeException e) {
              view.invalidDateFormat(out);
              validDateInput = false;
            } catch (IllegalArgumentException e) {
              if (e.getMessage().contains("The date should not be a future date")) {
                view.futureDate(out);
              }
              validDateInput = false;
            }
          }
          while (!validDateInput);
        }
        try {
          costBasis = userModel.getCostBasis(portfolioName, portfolioCostDate);
          view.displayCostBasis(costBasis, portfolioName, portfolioCostDate, out);
        } catch (IllegalArgumentException e) {
          view.costBasisInflexibleError(out);
        } catch (Exception e) {
          throw new RuntimeException(e);
        }

      } else {
        view.portfolioDoesNotExist(portfolioName, out);
      }
    }
  }


  @Override
  public void createInflexPortfolio(UserModel userModel, Scanner sc, PrintStream out) {
    TextView view = new TextViewConsoleImpl();
    int errorType = -1;

    //portfolioName
    final String pName = getPortfolioNameInput(userModel, sc, out, view, errorType);

    HashSet<StockViewModel> stockSet = getInflexStockListInput(sc, out, view);

    view.displayBuyStock(stockSet, out);
    int buy = getInputOption(sc);
    if (buy == 1) {
      try {
        userModel.savePortfolio(pName, stockSet, false);
        view.displayPortfolioSaved(out);
      } catch (IOException e) {
        view.displayExceptionMessage(out, e.getMessage());
      }
    } else {
      view.displayPortfolioNotSaved(out);
    }
  }

  @Override
  public void createFlexPortfolio(UserModel userModel, Scanner sc, PrintStream out) {
    TextView view = new TextViewConsoleImpl();
    int errorType = -1;

    //portfolioName
    final String pName = getPortfolioNameInput(userModel, sc, out, view, errorType);

    StockDatePriceDTO res;
    while (true) {
      final String ticker = getValidTickerInput(sc, out, view);

      //date of transaction
      final LocalDate date = getTransactionDateInput(sc, out, view);

      //display price of stock as per ticker and date
      try {
        final float stockPrice = stockService.getStockActualPriceByDate(ticker, date);
        res = new StockDatePriceDTO(ticker, stockPrice, date.toString());
        view.displayCurrStockPrice(res, out);
      } catch (Exception e) {
        view.displayExceptionMessage(out, e.getMessage() + " Try again.");
        continue;
      }
      //buy or sell transaction
      final boolean buy = getBuySellInput(sc, out, view);
      if (!buy) {
        view.displayExceptionMessage(out, "Portfolio is empty. Cannot sell from empty.");
        continue;
      }

      //Quantity
      final int qty = getQuantityInput(sc, out, view);

      //Commission fee
      final float commFee = getCommissionFeeInput(sc, out, view);
      StockViewModelFlex currStock =
              new StockModelFlexImpl(res.getTicker(), res.getPrice(), qty, res.getDate(), buy,
                      commFee);
      view.displayFlexBuyStock(currStock, out);
      int inputSave = getInputOption(sc);
      if (inputSave == 1) {
        try {
          List<StockViewModelFlex> stockList = new ArrayList<>();
          stockList.add(currStock);
          userModel.saveFlexPortfolio(pName, stockList, true);
          //view.displayPortfolioSaved(out);
          view.displayAddMoreFlexStock(out, buy ? "bought" : "sold");
          int addMore = getInputOption(sc);
          if (addMore == 1) {
            updateFlexPortfolioByName(pName, userModel, sc, out, view);
          }
          break;
        } catch (IOException e) {
          view.displayExceptionMessage(out, e.getMessage());
        }
      } else {
        view.displayPortfolioNotSaved(out);
        break;
      }
    }
  }

  private float getCommissionFeeInput(Scanner sc, PrintStream out, TextView view) {
    while (true) {
      view.displayCommissionFee(out);
      try {
        float fee = Float.parseFloat(sc.next());
        if (fee <= 0) {
          throw new IllegalArgumentException("Commission fee cannot be less than/equal to 0.");
        }
        return fee;
      } catch (IllegalArgumentException e) {
        view.displayExceptionMessage(out, e.getMessage());
      } catch (Exception e) {
        view.displayInputError(out);
      }
    }
  }

  private boolean getBuySellInput(Scanner sc, PrintStream out, TextView view) {
    view.displayTxnType(out);
    return (getInputOption(sc) == 1);
  }

  private String getValidTickerInput(Scanner sc, PrintStream out, TextView view) {
    while (true) {
      try {
        String ticker = getStockTickerInput(sc, out, view);
        stockService.checkDownloadStockData(ticker);
        return ticker;
      } catch (IllegalArgumentException | IOException e) {
        view.displayExceptionMessage(out, e.getMessage());
      }
    }
  }

  private int getQuantityInput(Scanner sc, PrintStream out, TextView view) {
    int qty;
    boolean error = false;
    do {
      view.displayGetQuantity(error, out);
      try {
        qty = Integer.parseInt(sc.next());
      } catch (Exception e) {
        qty = -1;
      }
    }
    while (error = qty <= 0);
    return qty;
  }

  private HashSet<StockViewModel> getInflexStockListInput(Scanner sc, PrintStream out,
                                                          TextView view) {
    float stockPrice;
    HashSet<StockViewModel> stockSet = new HashSet<>();

    while (true) {
      //ticker
      final String ticker = getStockTickerInput(sc, out, view);

      //final float stockPrice =
      try {
        StockDatePriceDTO res = stockService.getStockPriceByTicker(ticker);
        stockPrice = res.getPrice();
        view.displayCurrStockPrice(res, out);
      } catch (Exception e) {
        view.displayExceptionMessage(out, e.getMessage() + " Try again.");
        continue;
      }

      //Quantity
      int qty = getQuantityInput(sc, out, view);

      //All good, save
      validationService.composeInflexStocks(stockSet, stockPrice, ticker, qty);
      view.displayAddMoreStock(out);
      int optAdd = getInputOption(sc);
      if (optAdd != 1) {
        break;
      }
    }
    return stockSet;
  }

  private LocalDate getTransactionDateInput(Scanner sc, PrintStream out, TextView view) {
    while (true) {
      view.displayDateOfTransaction(out);
      String dateString = sc.next();
      try {
        LocalDate date = validationService.validateDateFormat(dateString);
        validationService.validateTwoDatesAfter(date, LocalDate.now());
        return date;
      } catch (IllegalArgumentException | DateTimeException e) {
        view.displayExceptionMessage(out, e.getMessage());
      }
    }
  }

  private String getPortfolioNameInput(UserModel userModel, Scanner sc, PrintStream out,
                                       TextView view, int errorType) {
    String pName;
    do {
      view.displayGetPortfolioName(errorType, out);
      pName = sc.next().toUpperCase();
    }
    while ((errorType = validationService.validatePortfolioName(pName, userModel)) > 0);
    return pName;
  }

  /**
   * Method to take user input for adding stock and saving the portfolio.
   *
   * @param sc input stream
   * @return option entered by the user
   */
  private int getInputOption(Scanner sc) {
    int opt;
    try {
      opt = Integer.parseInt(sc.next());
    } catch (Exception i) {
      opt = 0;
    }
    return opt;
  }


  private void updateFlexPortfolioByName(String pName, UserModel userModel,
                                         Scanner sc, PrintStream out, TextView view) {
    StockDatePriceDTO res;
    while (true) {
      final String ticker = getValidTickerInput(sc, out, view);

      //date of transaction
      final LocalDate date = getTransactionDateInput(sc, out, view);

      //display price of stock as per ticker and date
      try {
        final float stockPrice = stockService.getStockActualPriceByDate(ticker, date);
        res = new StockDatePriceDTO(ticker, stockPrice, date.toString());
        view.displayCurrStockPrice(res, out);
      } catch (Exception e) {
        view.displayExceptionMessage(out, e.getMessage() + " Try again.");
        continue;
      }
      //buy or sell transaction
      final boolean buy = getBuySellInput(sc, out, view);

      //Quantity
      final int qty = getQuantityInput(sc, out, view);

      //Commission fee
      final float commFee = getCommissionFeeInput(sc, out, view);
      final StockViewModelFlex currStock =
              new StockModelFlexImpl(res.getTicker(), res.getPrice(), qty, res.getDate(), buy,
                      commFee);

      if (!buy) {
        try {
          validationService.transactionSequence(userModel, currStock, pName);
        } catch (IllegalArgumentException e) {
          view.displayExceptionMessage(out, e.getMessage());
          continue;
        }
      }
      view.displayFlexBuyStock(currStock, out);
      int inputSave = getInputOption(sc);
      if (inputSave == 1) {
        try {
          userModel.saveFlexStock(pName, currStock);
          //view.displayPortfolioSaved(out);
          view.displayAddMoreFlexStock(out, buy ? "bought" : "sold");
          int addMore = getInputOption(sc);
          if (addMore == 1) {
            continue;
          }
          break;
        } catch (IOException e) {
          view.displayExceptionMessage(out, e.getMessage());
        }
      } else {
        view.displayPortfolioNotSaved(out);
        break;
      }
    }
  }

  @Override
  public void portfolioPerformance(UserModel userModel, Scanner sc, PrintStream out,
                                   TextView view) {
    final String pName = getFlexPortfolioName(userModel, sc, out, view);
    TimeStampBinsDTO timeStampBinsDTO = getTimeStampRangeInput(sc, out, view);

    List<Float> performanceVal = new ArrayList<>();
    for (LocalDate date : timeStampBinsDTO.getTimeStampBins()) {
      try {
        float price = userModel.calculatePortfolioValue(date, pName);
        performanceVal.add(price);
      } catch (Exception e) {
        view.displayExceptionMessage(out, e.getMessage() + " Something went wrong.");
        return;
      }
    }

    int stepSize = getStepSize(performanceVal);
    view.displayPerformanceChart(pName, out, performanceVal, timeStampBinsDTO, stepSize,
            Collections.min(performanceVal));
  }

  private int getStepSize(List<Float> performanceVal) {
    float minPrice = Collections.min(performanceVal);
    float maxPrice = Collections.max(performanceVal);

    float priceRange = maxPrice - minPrice;

    double scale1 = priceRange / 50;
    int stepSize = (int) Math.ceil(priceRange / 50);
    return stepSize;
  }

  private TimeStampBinsDTO getTimeStampRangeInput(Scanner sc, PrintStream out, TextView view) {
    view.displayTimeStampRangeType(out);
    int opt = getInputOption(sc);
    LocalDate startDate;
    LocalDate endDate;
    TimeStampBinsDTO timeStampBinsDTO;
    switch (opt) {
      case 1:
        while (true) {
          try {
            startDate = getStartYear(view, sc, out);
            endDate = getEndYear(view, sc, out);
            validationService.validateStartEndDate(startDate, endDate);
            break;
          } catch (NumberFormatException e) {
            view.displayExceptionMessage(out, "Incorrect format.");
          } catch (IllegalArgumentException e) {
            view.displayExceptionMessage(out, e.getMessage());
          }
        }
        timeStampBinsDTO = calculateMonthBins(startDate, endDate);
        break;
      case 2:
        while (true) {
          try {
            startDate = getMonthYear(view, sc, out, "start");
            endDate = getMonthYear(view, sc, out, "end");
            validationService.validateStartEndDate(startDate, endDate);
            break;
          } catch (IllegalArgumentException e) {
            view.displayExceptionMessage(out, e.getMessage());
          }
        }
        timeStampBinsDTO = calculateDayBins(startDate, endDate);
        break;
      default:
        while (true) {
          try {
            startDate = getDayMonthYear(view, sc, out, "start");
            endDate = getDayMonthYear(view, sc, out, "end");
            validationService.validateStartEndDate(startDate, endDate);
            break;
          } catch (IllegalArgumentException e) {
            view.displayExceptionMessage(out, e.getMessage());
          }
        }
        timeStampBinsDTO = calculateDayBins(startDate, endDate);
    }
    return timeStampBinsDTO;
  }

  private TimeStampBinsDTO calculateDayBins(LocalDate startDate, LocalDate endDate) {
    startDate = startDate.with(firstDayOfMonth());
    long range = ChronoUnit.DAYS.between(startDate, endDate) + 1;
    //calculate range from end and start date
    //Period diff = Period.between(startDate, endDate);
    int quotientDay = (int) range / 30;
    int remainderDay = (int) (range % 30);
    if (quotientDay < 30) {
      int divisionFactor;
      if (remainderDay > 1) {
        divisionFactor = quotientDay + 1;
      } else {
        divisionFactor = quotientDay;
      }
      int bins;
      int quotientbins = (int) range / divisionFactor;
      int remainderbins = (int) (range % divisionFactor);
      if (remainderbins > 1) {
        bins = quotientbins + 1;
      } else {
        bins = quotientbins;
      }
      if (bins < 5) {
        endDate = startDate.plusDays(4);
      }

      List<LocalDate> dayBins = new ArrayList<>();
      dayBins.add(startDate);

      for (int i = 0; i < bins - 2; i++) {
        startDate = startDate.plusDays(divisionFactor);
        dayBins.add(startDate);
      }
      dayBins.add(endDate);
      return new TimeStampBinsDTO(dayBins, TimeStampBinsDTO.TimestampType.DAY);
    } else {
      return calculateMonthBins(startDate, endDate);
    }
  }

  private TimeStampBinsDTO calculateMonthBins(LocalDate startDate, LocalDate endDate) {
    long range = ChronoUnit.MONTHS.between(startDate, endDate);
    int quotientMonth = (int) range / 30;
    int remainderMonth = (int) (range % 30);

    if (quotientMonth < 12) {
      int divisionFactor;
      if (remainderMonth > 1) {
        divisionFactor = quotientMonth + 1;
      } else {
        divisionFactor = quotientMonth;
      }
      int bins;
      int quotientbins = (int) range / divisionFactor;
      int remainderbins = (int) (range % divisionFactor);
      if (remainderbins > 1) {
        bins = quotientbins + 1;
      } else {
        bins = quotientbins;
      }

      List<LocalDate> monthBins = new ArrayList<>();
      monthBins.add(startDate);

      while (startDate.compareTo(endDate) < 0) {
        startDate = startDate.plusMonths(divisionFactor);
        startDate = startDate.with(lastDayOfMonth());
        monthBins.add(startDate);
      }
      // to set last value of bin to end date
      monthBins.remove(startDate);
      monthBins.add(endDate);
      return new TimeStampBinsDTO(monthBins, TimeStampBinsDTO.TimestampType.MONTH);
    } else {
      return calculateYearBins(startDate, endDate);
    }
  }

  private TimeStampBinsDTO calculateYearBins(LocalDate startDate, LocalDate endDate) {
    startDate = startDate.with(lastDayOfYear());
    long range = ChronoUnit.YEARS.between(startDate, endDate);
    int quotientYear = (int) range / 30;
    int remainderYear = (int) (range % 30);

    int timePeriod;
    if (remainderYear > 1) {
      timePeriod = quotientYear + 1;
    } else {
      timePeriod = quotientYear;
    }

    List<LocalDate> yearBins = new ArrayList<>();
    yearBins.add(startDate);

    while (startDate.compareTo(endDate) < 0) {
      startDate = startDate.plusYears(timePeriod);
      yearBins.add(startDate);
    }
    // to set last value of bin to end date
    yearBins.remove(startDate);
    yearBins.add(endDate);
    return new TimeStampBinsDTO(yearBins, TimeStampBinsDTO.TimestampType.YEAR);
  }


  private String getFlexPortfolioName(UserModel userModel, Scanner sc, PrintStream out,
                                      TextView view) {
    while (true) {
      view.displayGetPortfolioName(-1, out);
      final String pName = sc.next().toUpperCase();
      switch (userModel.checkPortfolioType(pName)) {
        case 0:
          view.displayPortfolioError(out, "Cannot update inflexible portfolio.");
          continue;
        case -1:
          view.displayPortfolioError(out, "Portfolio does not exist.");
          continue;
        case 1:
          return pName;
        default:
          //no action needed
      }
    }
  }

  private LocalDate getDayMonthYear(TextView view, Scanner sc, PrintStream out, String type) {
    while (true) {
      view.displayYear(out, type);
      int year = Integer.parseInt(sc.next());
      if (year < 1) {
        view.displayInputError(out);
        continue;
      }
      view.displayMonth(out, type);
      try {
        int month = Integer.parseInt(sc.next());
        view.displayDay(out, type);
        int day = Integer.parseInt(sc.next());
        LocalDate date;
        date = LocalDate.of(year, month, day);
        return date;
      } catch (DateTimeException e) {
        view.displayExceptionMessage(out, e.getMessage());
      }
    }
  }

  private String getStockTickerInput(Scanner sc, PrintStream out, TextView view) {
    view.displayGetStockTicker(out);
    String ticker = sc.next().toUpperCase();
    return ticker;
  }

  private LocalDate getMonthYear(TextView view, Scanner sc, PrintStream out, String type) {
    while (true) {
      view.displayYear(out, type);
      int inputYear = Integer.parseInt(sc.next());
      if (inputYear < 1) {
        view.displayInputError(out);
        continue;
      }
      view.displayMonth(out, type);
      int inputMonth = Integer.parseInt(sc.next());
      try {
        LocalDate date;
        if (inputYear == (LocalDate.now().getYear())
                && Month.of(inputMonth).equals(LocalDate.now().getMonth())) {
          date = LocalDate.now();
        } else {
          date = YearMonth.of(inputYear, inputMonth).atEndOfMonth();
        }
        return date;
      } catch (DateTimeException e) {
        view.displayExceptionMessage(out, e.getMessage());
      }
    }
  }

  private LocalDate getEndYear(TextView view, Scanner sc, PrintStream out) {
    while (true) {
      view.displayYear(out, "end");
      int input = Integer.parseInt(sc.next());
      if (input < 1) {
        view.displayInputError(out);
        continue;
      }
      LocalDate endDate = formatYear(input);
      return endDate;
    }
  }

  private LocalDate formatYear(int input) {
    LocalDate endDate;
    if (input == (LocalDate.now().getYear())) {
      endDate = LocalDate.now();
    } else {
      endDate = Year.of(input).atMonthDay(MonthDay.of(12, 31));
    }
    return endDate;
  }

  private LocalDate getStartYear(TextView view, Scanner sc, PrintStream out) {
    while (true) {
      view.displayYear(out, "start");
      int input = Integer.parseInt(sc.next());
      if (input < 1) {
        view.displayInputError(out);
        continue;
      }
      LocalDate startDate = Year.of(input).atMonthDay(MonthDay.of(1, 31));
      return startDate;
    }
  }


  /**
   * Method to validate the date is not in the future and calculate the value for the given date.
   *
   * @param userModel     model object from which the portfolio values for a certain date is needed
   * @param sc            Input Stream
   * @param out           Output Stream
   * @param view          View class
   * @param portfolioName name of the portfolio
   * @param portfolioType portfolio type (flex/ inflex)
   */
  private boolean validDateInput(UserModel userModel, Scanner sc, PrintStream out, TextView view,
                                 String portfolioName, String portfolioType) {
    String dateString;
    float value = 0;
    boolean validDateInput;
    view.getPortfolioValueDate("Value", out);
    dateString = sc.next();
    LocalDate portfolioValueDate = null;
    try {
      portfolioValueDate = userModel.validateDate(dateString);
      validDateInput = true;
    } catch (DateTimeException e) {
      view.invalidDateFormat(out);
      validDateInput = false;
    } catch (IllegalArgumentException e) {
      if (e.getMessage().contains("The date should not be a future date")) {
        view.futureDate(out);
      }
      validDateInput = false;
    }
    if (validDateInput) {
      try {
        value = userModel.calculatePortfolioValue(portfolioValueDate, portfolioName);
        view.displayPortfolioValue(out, value, portfolioValueDate, portfolioType);
      } catch (IOException e) {
        validDateInput = false;
        String exceptionMessage = String.valueOf(e);
        String ticker = exceptionMessage.substring(exceptionMessage
                .lastIndexOf(" ") + 1);
        view.displayDataNotFound(ticker, portfolioValueDate, out);
      } catch (Exception e) {
        validDateInput = false;
        view.displayExceptionMessage(out, "Something went wrong. Please try again");
      }
    }
    return validDateInput;
  }

  /**
   * Method to return the type of portfolio (flexible/ inflexible).
   *
   * @param pType type of portfolio as integer output received from the model
   * @return type of portfolio as a String
   */
  private String getPortfolioType(int pType) {
    String portfolioType = "";
    if (pType == 0) {
      portfolioType = "Inflexible";
    } else {
      portfolioType = "Flexible";
    }
    return portfolioType;
  }
}
