package controller;

import java.io.IOException;
import java.text.DecimalFormat;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import javax.swing.JToggleButton;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import dto.StockDatePriceDTO;
import dto.TimeStampBinsDTO;
import model.DCAStockRatio;
import model.DCAStockRatioImpl;
import model.DCAStrategyModel;
import model.DCAStrategyModelImpl;
import model.PortfolioModel;
import model.StockModel;
import model.StockModelFlexImpl;
import model.UserModel;
import service.stocks.StockCSVServiceImpl;
import service.stocks.StocksDataService;
import service.validations.ValidationServiceImpl;
import view.GuiView;
import viewmodel.DCAStrategyViewModel;
import viewmodel.DCAStrategyViewModelImpl;
import viewmodel.StockViewModelFlex;
import viewmodel.UserViewModel;
import viewmodel.UserViewModelImpl;

import static java.time.temporal.TemporalAdjusters.firstDayOfMonth;
import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;
import static java.time.temporal.TemporalAdjusters.lastDayOfYear;

/**
 * Class implements MainController and GuiFeatures. Class gets user inputs from graphical user
 * interface from GuiFeatures interface.
 */
public class MainControllerGuiImpl implements MainController, GuiFeatures {
  private final UserModel userModel;
  private final UserViewModel userViewModel;
  private final GuiView view;

  private final StocksDataService stockService = StockCSVServiceImpl.getInstance();

  private final ValidationServiceImpl validationService = new ValidationServiceImpl();


  /**
   * Constructs MainControllerGuiImpl with the user model and the GUI View.
   * Calls the add features method which passes the inputs from the gui view to the controller.
   *
   * @param userModel user model object
   * @param view      graphical user interface view object
   */
  public MainControllerGuiImpl(UserModel userModel, GuiView view) {
    this.userModel = userModel;
    this.userViewModel = new UserViewModelImpl(this.userModel);
    this.view = view;
    this.view.addFeatures(this);
  }

  @Override
  public void init() {
    stockService.clearStockDatastore();
    view.displayInitProfile();
  }

  @Override
  public void main() {
    Arrays.stream(view.getMainPanel().getComponents()).forEach(c -> c.setVisible(false));
    view.displayMainMenu();
  }

  @Override
  public void getInitInput(String option) {
    switch (option) {
      case "1":
        try {
          this.userModel.loadUser(null);
          view.displayDialog("User Profile loaded successfully");
          this.main();
        } catch (DateTimeException | IllegalArgumentException | IOException e) {
          view.displayDialog(e.getMessage());
        } catch (Exception e) {
          view.displayDialog("File format is invalid or it does not exist. Please try again.");
        }
        break;
      case "2":
        view.getMainPanel().getComponent(0).setVisible(false);
        view.displayExternalFileInput();
        break;
      case "3":
        try {
          this.userModel.createUser();
          view.displayDialog("New profile created.");
          this.main();
        } catch (Exception e) {
          view.displayDialog(e.getMessage());
        }
        break;
      default:
        view.displayDialog("Invalid option selected. Try again!");
    }
  }

  @Override
  public void getFilePath(String filePath) {
    if (filePath == null) {
      view.displayDialog("File path not selected.");
    } else {
      try {
        this.userModel.loadUser(filePath);
        view.displayDialog("External user profile loaded successfully.");
        this.main();
      } catch (DateTimeException | IllegalArgumentException | IOException e) {
        view.displayDialog(e.getMessage());
      } catch (Exception e) {
        view.displayDialog("File format is invalid or it does not exist. Please try again.");
      }
    }
  }

  @Override
  public void getMainMenuInput(String option) {
    switch (option) {
      case "1":
        Arrays.stream(view.getStatusPanel().getComponents()).forEach(c -> c.setVisible(false));
        view.displayPortfolioCreation();
        break;
      case "2":
        Arrays.stream(view.getStatusPanel().getComponents()).forEach(c -> c.setVisible(false));
        view.displayPortfolioList(formatPortfolioData(this.userViewModel.getInflexPortfolios()),
                formatPortfolioData(this.userViewModel.getFlexPortfolios()));
        break;
      case "3":
        Arrays.stream(view.getStatusPanel().getComponents()).forEach(c -> c.setVisible(false));
        if (!userModel.portfoliosExist()) {
          view.displayDialog("No portfolios found.");
        } else {
          view.displayPortfolioComposition();
        }
        break;
      case "4":
        Arrays.stream(view.getStatusPanel().getComponents()).forEach(c -> c.setVisible(false));
        if (!userModel.portfoliosExist()) {
          view.displayDialog("No portfolios found.");
        } else {
          view.displayPortfolioValue();
        }
        break;
      case "5":
        Arrays.stream(view.getStatusPanel().getComponents()).forEach(c -> c.setVisible(false));
        if (!userModel.portfoliosExist()) {
          view.displayDialog("No portfolios found.");
        } else {
          view.displayBuySellStocks();
        }
        break;
      case "6":
        Arrays.stream(view.getStatusPanel().getComponents()).forEach(c -> c.setVisible(false));
        if (!userModel.portfoliosExist()) {
          view.displayDialog("No portfolios found.");
        } else {
          view.displayInvestAmount();
        }
        break;
      case "7":
        Arrays.stream(view.getStatusPanel().getComponents()).forEach(c -> c.setVisible(false));
        if (!userModel.portfoliosExist()) {
          view.displayDialog("No portfolios found.");
        } else {
          view.displayCostBasis();
        }
        break;
      case "8":
        Arrays.stream(view.getStatusPanel().getComponents()).forEach(c -> c.setVisible(false));
        view.createDCAStrategy();
        break;
      case "9":
        Arrays.stream(view.getStatusPanel().getComponents()).forEach(c -> c.setVisible(false));
        view.displayGetPerformance();
        break;
      default:
        Arrays.stream(view.getStatusPanel().getComponents()).forEach(c -> c.setVisible(false));
        view.displayDialog("Invalid option selected. Try again!");
    }
  }

  @Override
  public void getTickerInput(String tickerSymbol) {
    try {
      StockDatePriceDTO res = stockService.getStockPriceByTicker(tickerSymbol);
      float stockPrice = res.getPrice();
      view.setPriceDetailLabel("Latest price of " + tickerSymbol + " is " + stockPrice + " USD.");
    } catch (Exception e) {
      view.displayDialog(e.getMessage() + " Try again!");
    }
  }

  @Override
  public void getCostBasis(String portfolioName, String year, String month, String day) {
    float costBasis;
    LocalDate portfolioCostDate = null;
    if (portfolioName.isEmpty() || portfolioName.isBlank()) {
      view.displayDialog("Enter Portfolio Name to proceed further.");
    } else {
      portfolioName = portfolioName.toUpperCase();
      if (day.length() == 1) {
        day = "0" + day;
      }
      if (month.length() == 1) {
        month = "0" + month;
      }
      String dateString = year + '-' + month + '-' + day;
      if (userModel.checkPortfolioType(portfolioName) != -1) {
        if (userModel.checkPortfolioType(portfolioName) == 1) {
          try {
            portfolioCostDate = userModel.validateDate(dateString);
          } catch (DateTimeException e) {
            view.displayDialog("Date entered is invalid. Please try again.");
            view.clearDate();
            return;
          } catch (IllegalArgumentException e) {
            view.displayDialog("The date should not be a future date. Please try again.");
            view.clearDate();
            return;
          }
        }
        try {
          costBasis = userModel.getCostBasis(portfolioName, portfolioCostDate);
          view.displayCostBasisValue("The Cost Basis of Portfolio " + portfolioName + " on "
                  + portfolioCostDate + " is " + String.format("%.2f", costBasis) + " USD.");
        } catch (IllegalArgumentException e) {
          view.displayDialog("Cost Basis can only be calculated for flexible portfolios. "
                  + "Please try again.");
          view.clearDate();
          view.clearLabels();
        } catch (Exception e) {
          view.displayDialog(e.getMessage() + " Please try again.");
        }
      } else {
        view.displayDialog("There is no portfolio present with the name: " + portfolioName);
        view.clearLabels();
      }
    }
  }

  @Override
  public void getPortfolioValue(String portfolioName, String year, String month, String day) {
    float portfolioValue;
    LocalDate portfolioValueDate = null;
    if (portfolioName.isEmpty() || portfolioName.isBlank()) {
      view.displayDialog("Enter Portfolio Name to proceed further.");
    } else {
      portfolioName = portfolioName.toUpperCase();
      if (day.length() == 1) {
        day = "0" + day;
      }
      if (month.length() == 1) {
        month = "0" + month;
      }
      String dateString = year + '-' + month + '-' + day;
      if (userModel.checkPortfolioType(portfolioName) != -1) {
        try {
          portfolioValueDate = userModel.validateDate(dateString);
          portfolioValue = userModel.calculatePortfolioValue(portfolioValueDate, portfolioName);
          view.displayPortfolioValueLabel("The value of Portfolio " + portfolioName + " on "
                  + portfolioValueDate + " is " + String.format("%.2f", portfolioValue) + " USD.");
        } catch (DateTimeException e) {
          view.displayDialog("Date entered is invalid. Please try again.");
          view.clearDate();
        } catch (IllegalArgumentException e) {
          view.displayDialog("The date should not be a future date. Please try again.");
          view.clearDate();
        } catch (IOException e) {
          String exceptionMessage = String.valueOf(e);
          String ticker = exceptionMessage.substring(exceptionMessage
                  .lastIndexOf(" ") + 1);
          view.displayDialog("Value for ticker " + ticker + " not found for the given date "
                  + portfolioValueDate + ".");
        } catch (Exception e) {
          view.displayDialog(e.getMessage() + " Try again!");
        }
      } else {
        view.displayDialog("There is no portfolio present with the name: " + portfolioName);
        view.clearLabels();
      }
    }
  }

  @Override
  public void addDCAStrategy(TableModel strategyStockData, String portfolioName,
                             String investedAmount, String commissionFee,
                             String frequencyLabel, String startDate, String endDate) {
    int err = validationService.validatePortfolioName(portfolioName, userModel);
    if (err == 1 && !investedAmount.isEmpty() && !frequencyLabel.isEmpty()) {
      view.displayDialog("Portfolio Name should contain alphanumeric values.");
    } else {
      boolean existingPortfolio = userModel.checkPortfolioType(portfolioName) == 1;

      portfolioName = portfolioName.toUpperCase();
      List<DCAStockRatio> stockProportionList = new ArrayList<>();

      if (strategyStockData.getRowCount() <= 0) {
        view.displayDialog("Add stock details and weights in the stock table.");
        return;
      }

      for (int count = 0; count < strategyStockData.getRowCount(); count++) {
        try {
          stockService.checkDownloadStockData(strategyStockData.getValueAt(count, 0)
                  .toString().toUpperCase());
          stockProportionList.add(new DCAStockRatioImpl(
                  strategyStockData.getValueAt(count, 0).toString().toUpperCase(),
                  Double.parseDouble(
                          strategyStockData.getValueAt(count, 1).toString())));

        } catch (NumberFormatException e) {
          view.displayDialog("Enter valid percentage for "
                  + strategyStockData.getValueAt(count, 0).toString());
          return;
        } catch (Exception e) {
          view.displayDialog(e + ": "
                  + strategyStockData.getValueAt(count, 0).toString());
          return;
        }
      }
      try {
        if (stockProportionList.size() > 0) {
          DCAStrategyViewModel dcaPortfolio = new DCAStrategyViewModelImpl(
                  new DCAStrategyModelImpl(portfolioName, stockProportionList, startDate, endDate,
                          Double.parseDouble(investedAmount), Integer.parseInt(frequencyLabel),
                          Float.parseFloat(commissionFee)));

          DCAStrategyModel dcaModel = userModel.addDCAStrategyToList(dcaPortfolio);
          dcaModel.computeDollarCostAveraging(userModel);
          view.clearLabels();
          view.clearDate();

          if (existingPortfolio) {
            view.displayDialog("Strategy added successfully to existing portfolio "
                    + portfolioName + "!");
          } else {
            view.displayDialog("Strategy added successfully to new portfolio: "
                    + portfolioName + "!");
          }
        }
      } catch (NumberFormatException e) {
        view.displayDialog("Enter a Valid Amount");
      } catch (Exception e) {
        view.displayDialog(e.getMessage());
        if (e.getMessage().contains("Start Date entered is invalid")
                || e.getMessage().contains("Start Date cannot be after End Date.")
                || e.getMessage().contains("date should not be a future date")) {
          view.clearDate();
        }
      }
    }
  }

  @Override
  public void createFlexPortfolio(String portfolioName, String tickerSymbol, String year,
                                  String month, String day, String quantity, String commissionFee,
                                  String sell) {
    portfolioName = portfolioName.toUpperCase();
    final String pName;
    int err = validationService.validatePortfolioName(portfolioName, userModel);
    switch (err) {
      case 1:
        view.displayDialog("Portfolio Name should contain alphanumeric values.");
        return;
      case 2:
        //already exists, newly created
        if (!view.getpNameText().isEnabled()) {
          this.buySellStock(portfolioName, tickerSymbol, year, month, day,
                  quantity, commissionFee, sell);
          return;
        }
        //already exists
        view.displayDialog("Portfolio Name already exists.");
        return;
      case -1:
        pName = portfolioName;
        break;
      default:
        view.displayDialog("Something went wrong! Try again!");
        return;
    }

    tickerSymbol = tickerSymbol.toUpperCase();
    if (day.length() == 1) {
      day = "0" + day;
    }
    if (month.length() == 1) {
      month = "0" + month;
    }
    String dateString = year + '-' + month + '-' + day;
    StockViewModelFlex currStock;
    try {
      stockService.checkDownloadStockData(tickerSymbol);
      LocalDate transactionDate = userModel.validateDate(dateString);

      final float stockPrice =
              stockService.getStockActualPriceByDate(tickerSymbol, transactionDate);
      StockDatePriceDTO res = new StockDatePriceDTO(tickerSymbol, stockPrice, dateString);

      try {
        Integer.parseInt(quantity);
      } catch (NumberFormatException e) {
        view.displayDialog("Invalid value for quantity");
        return;
      }

      try {
        Float.parseFloat(commissionFee);
      } catch (NumberFormatException e) {
        view.displayDialog("Invalid value for commissionFee");
        return;
      }

      try {
        if (Boolean.parseBoolean(sell)) {
          view.displayDialog("Stocks cannot be sold before buying.");
          return;
        }
      } catch (Exception e) {
        view.displayDialog("Invalid value for buy sell. Something went wrong!");
        return;
      }

      currStock =
              new StockModelFlexImpl(res.getTicker(), res.getPrice(), Integer.parseInt(quantity),
                      res.getDate(), !Boolean.parseBoolean(sell),
                      Float.parseFloat(commissionFee));
    } catch (DateTimeException e) {
      view.displayDialog("Date entered is invalid. Please try again.");
      view.clearDate();
      return;
    } catch (IllegalArgumentException e) {
      view.displayDialog(e.getMessage() + " Please try again.");
      return;
    } catch (Exception e) {
      view.displayDialog(e.getMessage() + " Try again.");
      return;
    }

    try {
      List<StockViewModelFlex> stockList = new ArrayList<>();
      stockList.add(currStock);
      userModel.saveFlexPortfolio(pName, stockList, true);
      view.displayDialog("Portfolio saved.");
      view.clearLabels();
      view.clearDate();
      view.getpNameText().setEnabled(false);
    } catch (Exception e) {
      view.displayDialog(e.getMessage());
    }
  }

  @Override
  public void getPerformance(String portfolioName, String startDate, String endDate) {

    if (userModel.checkPortfolioType(portfolioName) != 1) {
      view.displayDialog("There is no flexible portfolio with the given name.");
      return;
    }
    try {
      TimeStampBinsDTO timeStampBinsDTO = getTimeStampRangeInput(startDate, endDate);
      List<Float> performanceVal = new ArrayList<>();
      for (LocalDate date : timeStampBinsDTO.getTimeStampBins()) {
        try {
          float price = userModel.calculatePortfolioValue(date, portfolioName);
          performanceVal.add(price);
        } catch (Exception e) {
          view.displayDialog(e.getMessage() + " Something went wrong.");
          return;
        }
      }
      int stepSize = getStepSize(performanceVal);
      view.displayPerformanceChart(performanceVal, timeStampBinsDTO);

    } catch (IllegalArgumentException e) {
      view.displayDialog(e.getMessage());
    } catch (DateTimeParseException e) {
      view.displayDialog("Enter valid date!");
    }


  }

  @Override
  public void buySellStock(String portfolioName, String tickerSymbol, String year, String month,
                           String day, String quantity, String commissionFee, String sell) {
    portfolioName = portfolioName.toUpperCase();
    tickerSymbol = tickerSymbol.toUpperCase();
    if (day.length() == 1) {
      day = "0" + day;
    }
    if (month.length() == 1) {
      month = "0" + month;
    }
    String dateString = year + '-' + month + '-' + day;
    StockViewModelFlex currStock;
    if (userModel.checkPortfolioType(portfolioName) == 1) {
      try {
        stockService.checkDownloadStockData(tickerSymbol);
        LocalDate transactionDate = userModel.validateDate(dateString);

        final float stockPrice = stockService.getStockActualPriceByDate(tickerSymbol,
                transactionDate);
        StockDatePriceDTO res = new StockDatePriceDTO(tickerSymbol, stockPrice, dateString);

        currStock =
                new StockModelFlexImpl(res.getTicker(), res.getPrice(), Float.parseFloat(quantity),
                        res.getDate(), !Boolean.parseBoolean(sell),
                        Float.parseFloat(commissionFee));
      } catch (DateTimeException e) {
        view.displayDialog("Date entered is invalid. Please try again.");
        view.clearDate();
        return;
      } catch (IllegalArgumentException e) {
        view.displayDialog(e.getMessage() + " Please try again.");
        //view.clearDate();
        return;
      } catch (Exception e) {
        view.displayDialog(e.getMessage() + " Try again.");
        return;
      }

      if (Boolean.parseBoolean(sell)) {
        try {
          validationService.transactionSequence(userModel, currStock, portfolioName);
        } catch (IllegalArgumentException e) {
          view.displayDialog(e.getMessage() + " Try again.");
          return;
        }
      }
      try {
        userModel.saveFlexStock(portfolioName, currStock);
        view.displayDialog(Boolean.parseBoolean(sell) ? "Stock sold." : "Stock bought.");
        view.clearDate();
        view.clearLabels();

      } catch (IOException e) {
        view.displayDialog(e.getMessage() + " Try again.");
      }
    } else {
      view.displayDialog("There is no flexible portfolio present with the name: " + portfolioName);
    }

  }

  @Override
  public void getTickerPriceDate(String tickerSymbol, String year, String month, String day) {
    tickerSymbol = tickerSymbol.toUpperCase();
    float stockValue;
    LocalDate transactiondate = null;
    if (day.length() == 1) {
      day = "0" + day;
    }
    if (month.length() == 1) {
      month = "0" + month;
    }
    String dateString = year + '-' + month + '-' + day;

    try {
      transactiondate = userModel.validateDate(dateString);
      stockService.checkDownloadStockData(tickerSymbol);
      stockValue = stockService.getStockActualPriceByDate(tickerSymbol, transactiondate);
      view.setPriceDetailLabel("Price of " + tickerSymbol + " on " + transactiondate + " is "
              + stockValue + " USD.");
    } catch (IOException e) {
      view.displayDialog(e.getMessage() + " Please try again.");
      view.getPriceDetailLabel().setText("Ticker details will be displayed here");
    } catch (DateTimeException e) {
      view.displayDialog("Date entered is invalid. Please try again.");
      view.clearDate();
      view.getPriceDetailLabel().setText("Ticker details will be displayed here");
    } catch (IllegalArgumentException e) {
      view.displayDialog(e.getMessage());
      view.getPriceDetailLabel().setText("Ticker details will be displayed here");
      view.clearDate();
    }
  }

  @Override
  public void getPortfolioData(String portfolioName) {
    portfolioName = portfolioName.toUpperCase();
    try {
      if (userModel.checkPortfolioType(portfolioName) == 1) {
        HashMap<String, StockModel> stockMap
                = userViewModel.getFlexPortfolios().get(portfolioName).getStockMap();
        HashSet<String> tickerSymbolSet = new HashSet<>();
        for (StockModel stock : stockMap.values()) {
          tickerSymbolSet.add(stock.getTickerSymbol());
        }
        view.displayStockTicker(formatStockData(tickerSymbolSet));
      } else {
        view.displayDialog("There is no flexible portfolio present with the name: "
                + portfolioName);
      }
    } catch (Exception e) {
      view.displayDialog(e.getMessage() + " Please try again.");
    }
  }

  @Override
  public void investInPortfolio(String portfolioName, String investedAmount,
                                String commissionFee, String year, String month, String day,
                                TableModel stockWeightsTable) {
    portfolioName = portfolioName.toUpperCase();
    List<DCAStockRatio> stockProportionList = new ArrayList<>();
    if (userModel.checkPortfolioType(portfolioName) == 1) {
      if (stockWeightsTable.getRowCount() <= 0) {
        view.displayDialog("Fetch stock details and add weights in the stock table.");
        return;
      }
      for (int count = 0; count < stockWeightsTable.getRowCount(); count++) {
        try {
          stockProportionList.add(new DCAStockRatioImpl(
                  stockWeightsTable.getValueAt(count, 1).toString().toUpperCase(),
                  Double.parseDouble(stockWeightsTable
                          .getValueAt(count, 2).toString())));

        } catch (NumberFormatException e) {
          view.displayDialog("Enter valid percentage for "
                  + stockWeightsTable.getValueAt(count, 1).toString());
          return;
        } catch (Exception e) {
          view.displayDialog(e + ": "
                  + stockWeightsTable.getValueAt(count, 1).toString());
          return;
        }
      }
      try {
        if (day.length() == 1) {
          day = "0" + day;
        }
        if (month.length() == 1) {
          month = "0" + month;
        }
        String dateString = year + '-' + month + '-' + day;

        if (stockProportionList.size() > 0) {
          DCAStrategyViewModel dcaPortfolio = new DCAStrategyViewModelImpl(
                  new DCAStrategyModelImpl(portfolioName, stockProportionList,
                          dateString, dateString,
                          Double.parseDouble(investedAmount), 0,
                          Float.parseFloat(commissionFee)));

          DCAStrategyModel dcaModel = userModel.addDCAStrategyToList(dcaPortfolio);
          dcaModel.computeDollarCostAveraging(userModel);
          view.displayDialog("Amount Invested in " + portfolioName + "!");
          view.clearDate();
          view.clearLabels();

        }
      } catch (NumberFormatException e) {
        view.displayDialog("Enter a Valid Amount");
      } catch (Exception e) {
        if (e.getMessage().contains("Start Date cannot be after End Date.")
                || e.getMessage().contains("date should not be a future date")) {
          view.displayDialog(e.getMessage());
          view.clearDate();
        } else if (e.getMessage().contains("Date entered is invalid")) {
          view.displayDialog("Transaction Date entered is invalid.");
          view.clearDate();
        } else {
          view.displayDialog(e.getMessage());
        }
      }
    } else {
      view.displayDialog("There is no flexible portfolio present with the name: " + portfolioName);
      view.clearLabels();
      view.clearDate();
    }
  }

  private String[][] formatStockData(HashSet<String> tickerSymbolSet) {
    if (tickerSymbolSet.size() > 0) {
      int size = tickerSymbolSet.size();
      String[][] data = new String[size][3];
      int i = 0;
      for (String ticker : tickerSymbolSet) {
        data[i][0] = (i + 1) + "";
        data[i][1] = ticker;
        data[i][2] = "";
        i++;
      }
      return data;
    }
    String[][] data = new String[1][3];
    data[0][0] = "";
    data[0][1] = "No stocks found!";
    data[0][2] = "";
    return data;
  }

  @Override
  public void getComposition(JToggleButton flexType, String portfolioName,
                             String year, String month, String day) {
    String[] header = {"#", "Stock", "Quantity", "Composition (%)"};
    String[][] emptyData = {};
    try {
      LocalDate date;
      int type = userModel.checkPortfolioType(portfolioName);
      if (type == 0 && !flexType.isSelected()) {
        date = LocalDate.now();
      } else if (flexType.isSelected() && type == 1) {
        //flexible portfolios
        try {
          String dateString = year + "-" + month + "-" + day;
          date = userModel.validateDate(dateString);
        } catch (DateTimeException e) {
          view.displayDialog("Invalid date.");
          view.getCompositionTable().setModel(new DefaultTableModel(emptyData, header));
          return;
        } catch (IllegalArgumentException e) {
          //if (e.getMessage().contains("The date should not be a future date")) {
          view.displayDialog(e.getMessage());
          view.getCompositionTable().setModel(new DefaultTableModel(emptyData, header));
          return;
          //}
        }
      } else {
        view.displayDialog("Portfolio name does not match! Try again!");
        view.getCompositionTable().setModel(new DefaultTableModel(emptyData, header));
        return;
      }
      HashMap<String, Float> portfolioComposition =
              userModel.calculateComposition(portfolioName, date);
      view.getCompositionTable().setModel(new DefaultTableModel(
              formatCompositionData(portfolioComposition), header));
    } catch (Exception e) {
      view.displayDialog(e.getMessage());
      view.getCompositionTable().setModel(new DefaultTableModel(emptyData, header));
    }
  }

  private String[][] formatCompositionData(HashMap<String, Float> compositionMap) {
    if (compositionMap.size() > 0) {
      int size = compositionMap.size();
      DecimalFormat df = new DecimalFormat("#.##");
      //df.setRoundingMode(RoundingMode.CEILING);
      String[][] data = new String[size][4];
      int i = 0;
      for (String key : compositionMap.keySet()) {
        data[i][0] = (i + 1) + "";
        String[] keyVal = key.split("\\s+");
        data[i][1] = keyVal[0];
        data[i][2] = keyVal[1];
        data[i][3] = df.format(compositionMap.get(key));
        i++;
      }
      return data;
    }
    String[][] data = new String[1][4];
    data[0][0] = "";
    data[0][1] = "No Portfolios found!";
    data[0][2] = "";
    data[0][3] = "";
    return data;
  }

  private String[][] formatPortfolioData(HashMap<String, PortfolioModel> portfolios) {
    if (portfolios.size() > 0) {
      int size = portfolios.size();
      String[][] data = new String[size][2];
      int i = 0;
      for (PortfolioModel p : portfolios.values()) {
        data[i][0] = (i + 1) + "";
        data[i][1] = p.getPortfolioName();
        i++;
      }
      return data;
    }
    String[][] data = new String[1][2];
    data[0][0] = "";
    data[0][1] = "No Portfolios found!";
    return data;
  }

  private TimeStampBinsDTO getTimeStampRangeInput(String startDateStr, String endDateStr)
          throws IllegalArgumentException {
    LocalDate startDate = LocalDate.parse(startDateStr);
    LocalDate endDate = LocalDate.parse(endDateStr);
    validationService.validateStartEndDate(startDate, endDate);
    TimeStampBinsDTO timeStampBinsDTO = calculateDayBins(startDate, endDate);
    return timeStampBinsDTO;
  }

  private TimeStampBinsDTO calculateDayBins(LocalDate startDate, LocalDate endDate) {
    startDate = startDate.with(firstDayOfMonth());
    long range = ChronoUnit.DAYS.between(startDate, endDate) + 1;
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

  private int getStepSize(List<Float> performanceVal) {
    float minPrice = Collections.min(performanceVal);
    float maxPrice = Collections.max(performanceVal);

    float priceRange = maxPrice - minPrice;

    double scale1 = priceRange / 50;
    int stepSize = (int) Math.ceil(priceRange / 50);
    return stepSize;
  }

}
