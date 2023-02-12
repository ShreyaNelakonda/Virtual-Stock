package view;

import java.io.PrintStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import dto.StockDatePriceDTO;
import dto.TimeStampBinsDTO;
import model.PortfolioModel;
import viewmodel.StockViewModel;
import viewmodel.StockViewModelFlex;

/**
 * Handles output for the app.
 */
public class TextViewConsoleImpl implements TextView {

  private void displayPortfolioInfo(PrintStream ps, HashMap<String, PortfolioModel> portfolios) {
    if (portfolios.isEmpty()) {
      ps.println("No portfolios found.");
      return;
    }
    //ps.println("# \t Name");
    int i = 1;
    for (PortfolioModel portfolio : portfolios.values()) {
      ps.println(i++ + " \t " + portfolio.getPortfolioName());
    }
  }

  @Override
  public void displayInitProfile(PrintStream ps) {
    //ps.println("Hey there!");
    ps.println("How to load your profile?");
    ps.println(" Enter 1 : to load a previous profile.");
    ps.println(" Enter 2 : to load an external profile.");
    ps.println(" Enter 3 : to start with a new profile.");
  }

  @Override
  public void displayInputError(PrintStream ps) {
    ps.println("Incorrect input! Please try again.");
  }

  @Override
  public void displayExit(PrintStream ps) {
    ps.println("Good Bye!");
  }

  @Override
  public void displayMainMenuCommonOption(PrintStream ps) {
    ps.println("Main Menu");
    ps.println(" Enter 1 \t : to create portfolio.");
    ps.println(" Enter 2 \t : to view all portfolios.");
    ps.println(" Enter 3 \t : to check portfolio composition.");
    ps.println(" Enter 4 \t : to view the price of a portfolio on a certain date.");
    ps.println(" Enter 5 \t : to purchase/sell shares in existing portfolio.");
    ps.println(" Enter 6 \t : to compute cost basis.");
    ps.println(" Enter 7 \t : to check portfolio performance.");
    ps.println(" Enter any \t : to exit App.");
  }

  @Override
  public void displayPortfolios(PrintStream ps, HashMap<String, PortfolioModel> flexPortfolios,
                                HashMap<String, PortfolioModel> inflexPortfolios) {
    ps.println("******* Flexible Portfolios *******");
    displayPortfolioInfo(ps, flexPortfolios);
    ps.println();
    ps.println("******* Inflexible Portfolios *******");
    displayPortfolioInfo(ps, inflexPortfolios);
  }

  @Override
  public void getPortfolioValueDate(String operation, PrintStream ps) {
    ps.println("Enter the Date (YYYY-MM-DD) you want to check the Portfolio "
            + operation + " on: ");
  }

  @Override
  public void displayPortfolioValue(PrintStream ps, float portfolioValue, LocalDate date,
                                    String portfolioType) {
    ps.println("******* " + portfolioType + " Portfolio *******");
    ps.println("The value of Portfolio on " + date + " is "
            + String.format("%.2f", portfolioValue) + " USD.");
  }

  @Override
  public void portfolioDoesNotExist(String portfolioNameValue, PrintStream ps) {
    ps.println("There is no portfolio present with the name: " + portfolioNameValue);
  }

  @Override
  public void portfolioListEmpty(PrintStream ps) {
    ps.println("No portfolios found.");
  }

  @Override
  public void displayGetStockTicker(PrintStream ps) {
    ps.println("Enter ticker symbol : ");
  }

  @Override
  public void displayGetPortfolioName(int err, PrintStream out) {
    if (err == 2) {
      out.println("Portfolio name already exists. Please try again.");
    } else if (err == 1) {
      out.println("Portfolio name must contain alphanumeric characters only. Please try again.");
    }
    out.println("Enter portfolio name : ");
  }

  @Override
  public void displayCurrStockPrice(StockDatePriceDTO result, PrintStream out) {
    out.println("The price [" + result.getDate() + "] of " + result.getTicker() + " is "
            + result.getPrice() + " USD.");
  }

  @Override
  public void displayGetQuantity(boolean err, PrintStream out) {
    if (err) {
      out.println("Invalid value. Must be Integer > 0.");
    }
    out.println("Enter Quantity: ");
  }

  @Override
  public void displayAddMoreStock(PrintStream out) {
    out.println("Do you want to add more stock to your portfolio?");
    out.println("Enter 1 : To add more stock.");
    out.println("Any key : To view portfolio & save.");
  }

  @Override
  public void displayBuyStock(HashSet<StockViewModel> stockList, PrintStream out) {
    for (Iterator<StockViewModel> it = stockList.iterator(); it.hasNext(); ) {
      StockViewModel stock = it.next();
      out.println(stock.getTickerSymbol()
              + " | " + stock.getInvestedPrice()
              + " | " + (int) stock.getStockQuantity());
    }
    out.println("Do you want to save your portfolio?");
    out.println("Enter 1 : to save.");
    out.println("Enter any : not to save.");
  }

  @Override
  public void displayPortfolioNotSaved(PrintStream out) {
    out.println("Portfolio not saved!");
  }

  @Override
  public void getFilepath(PrintStream ps) {
    ps.println("Enter the filepath in .csv for the list of portfolios to be loaded: ");
  }

  @Override
  public void displayPortfolioComposition(PrintStream ps,
                                          HashMap<String, Float> stockComposition,
                                          String portfolioType) {
    if (stockComposition.isEmpty()) {
      ps.println("No stocks found.");
      return;
    }
    ps.println("******* " + portfolioType + " Portfolio *******");
    ps.println("#  Stock | Qty | Composition");
    int i = 1;
    for (String stock : stockComposition.keySet()) {
      String[] stockTicker = stock.split(" ");
      ps.println(i++ + "  " + stockTicker[0]
              + " | " + stockTicker[1] + " | "
              + String.format("%.2f", stockComposition.get(stock)) + "%");
    }
  }

  @Override
  public void fileLoadSuccess(String load, PrintStream ps) {
    if (load.contains("external")) {
      ps.println("File uploaded successfully");
    } else {
      ps.println("Profile loaded successfully.");
    }
  }

  @Override
  public void displayExceptionMessage(PrintStream out, String msg) {
    out.println(msg);
  }

  @Override
  public void displayUserCreated(PrintStream out) {
    out.println("Your profile is created.");
  }

  @Override
  public void futureDate(PrintStream ps) {
    ps.println("The date should not be a future date. Please try again.");
  }

  @Override
  public void invalidDateFormat(PrintStream ps) {
    ps.println("Format of the date is invalid. Please try again.");
  }

  @Override
  public void displayDataNotFound(String tickerSymbol, LocalDate date, PrintStream ps) {
    ps.println("Value for ticker " + tickerSymbol + " not found for the given date " + date + ".");
    ps.println("Please try with a future date.");
  }

  @Override
  public void errorFractionalShares(PrintStream ps) {
    ps.println("Some of the data in the file is invalid. \n"
            + "User can only buy whole shares for inflexible portfolio. "
            + "Please update the quantity and try again.");
  }

  @Override
  public void errorInvalidFile(PrintStream ps) {
    ps.println("File format is invalid or it does not exist. Please load data from a valid "
            + "external file or create portfolio manually.");
  }

  @Override
  public void displayQuantityLessZero(PrintStream out) {
    out.println("Some of the data in the file is invalid. \n"
            + "Quantity must always be greater than 0. Please update the quantity and try again.");
  }

  @Override
  public void displayPortfolioSaved(PrintStream out) {
    out.println("Portfolio is saved successfully.");
  }

  @Override
  public void displayChoosePortfolioType(PrintStream out) {
    out.println("Select type of portfolio : ");
    out.println(" Enter 1 \t : Flexible Portfolio.");
    out.println(" Enter Any \t : Inflexible Portfolio.");
  }

  @Override
  public void displayDateOfTransaction(PrintStream out) {
    out.println("Enter transaction date (YYYY-MM-DD) :");
  }

  @Override
  public void displayTxnType(PrintStream out) {
    out.println("Select type of transaction : ");
    out.println(" Enter 1 \t : Buy ");
    out.println(" Enter Any \t : Sell ");
  }

  @Override
  public void displayFileUploadInvalidData(String errorType, PrintStream out) {
    out.println("Some of the data in the file is invalid.");
    if (errorType.equals("Commission Fee")) {
      out.println("Commission Fee must always be greater than 0.");
    } else if (errorType.equals("Date Format")) {
      out.println("Date Format (YYYY-MM-DD) is invalid.");
    } else {
      out.println("Data for certain stocks on the given transaction date is not available. "
              + "Date passed might be a holiday.");
    }
    out.println("Please update the " + errorType + " and try again.");
  }

  @Override
  public void displayCommissionFee(PrintStream out) {
    out.println("Enter commission fee for this transaction : ");
  }

  @Override
  public void displayFlexBuyStock(StockViewModelFlex stock, PrintStream out) {
    out.println("Stock | Date | Price | Fee | Qty | Type ");
    out.println(stock.getTickerSymbol()
            + " | " + stock.getTransactionDate()
            + " | " + stock.getInvestedPrice()
            + " | " + stock.getCommissionFee()
            + " | " + stock.getStockQuantity()
            + " | " + formatBuySellBoolean(stock.getBuy()));
    out.println("Do you want to add this transaction to your portfolio?");
    out.println("Enter 1 : to save.");
    out.println("Enter any : to go back to main menu");
  }

  private String formatBuySellBoolean(boolean val) {
    if (val) {
      return "Buy";
    } else {
      return "Sell";
    }
  }

  @Override
  public void displayPortfolioError(PrintStream out, String msg) {
    out.println(msg + " Try again.");
  }

  @Override
  public void costBasisInflexibleError(PrintStream out) {
    out.println("Cost Basis can only be calculated for flexible portfolios. Please try again.");
  }

  @Override
  public void displayCostBasis(float costBasis, String portfolioName, LocalDate portfolioCostDate,
                               PrintStream out) {
    out.println("Cost Basis for portfolio " + portfolioName + " on " + portfolioCostDate
            + " is " + costBasis + " USD.");
  }

  @Override
  public void displayTimeStampRangeType(PrintStream out) {
    out.println("Enter 1 : year");
    out.println("Enter 2 : month");
    out.println("Enter any : days");
  }

  @Override
  public void displayYear(PrintStream out, String type) {
    out.println("Enter " + type + " year : (YYYY)");
  }

  @Override
  public void displayMonth(PrintStream out, String type) {
    out.println("Enter " + type + " month : (MM)");
  }

  @Override
  public void displayDay(PrintStream out, String type) {
    out.println("Enter " + type + " day : (DD)");
  }

  @Override
  public void displayAddMoreFlexStock(PrintStream out, String operation) {
    out.println("Stock " + operation + "!");
    out.println("Do you want to do more transactions on your portfolio?");
    out.println("Enter 1 : To add more transaction.");
    out.println("Any key : To go back to main menu.");
  }

  @Override
  public void displayPerformanceTile(String pName, String startDate, String endDate,
                                     PrintStream out) {
    out.println("Performance of portfolio "
            + pName
            + " from "
            + startDate
            + " to "
            + endDate);
  }

  @Override
  public void displayPerformanceChart(String pName, PrintStream out, List<Float> performanceVal,
                                      TimeStampBinsDTO timeStampBinsDTO, int stepSize,
                                      float minPrice) {
    displayPerformanceTile(pName, timestampBinFormatter(timeStampBinsDTO, 0),
            timestampBinFormatter(timeStampBinsDTO,
                    timeStampBinsDTO.getTimeStampBins().size() - 1), out);

    for (int j = 0; j < performanceVal.size(); j++) {
      out.print(timestampBinFormatter(timeStampBinsDTO, j));
      out.print(" : ");
      float actualVal = performanceVal.get(j);
      double x = actualVal - minPrice;
      if (actualVal > 0) {
        do {
          out.print("*");
          x = x - stepSize;
        }
        while (x > 0);
      }
      out.println();
    }
    out.println("Scale: * = $" + stepSize);
    out.println("Base Amount = $" + String.format("%.2f", minPrice));
  }

  private String timestampBinFormatter(TimeStampBinsDTO timeStampBinsDTO, int index) {
    if (timeStampBinsDTO.getType().equals(TimeStampBinsDTO.TimestampType.YEAR)) {
      return String.valueOf(timeStampBinsDTO.getTimeStampBins().get(index).getYear());
    } else if (timeStampBinsDTO.getType().equals(TimeStampBinsDTO.TimestampType.MONTH)) {
      DateTimeFormatter monthFormat = DateTimeFormatter.ofPattern("MMM yyyy",
              Locale.ENGLISH);
      String monthName = monthFormat.format(timeStampBinsDTO.getTimeStampBins().get(index));
      return monthName;
    } else {
      return timeStampBinsDTO.getTimeStampBins().get(index).toString();
    }
  }
}
