package model;

import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import service.user.UserCSVServiceImpl;
import service.user.UserDataService;
import service.validations.ValidationServiceImpl;
import viewmodel.DCAStrategyViewModel;
import viewmodel.StockViewModel;
import viewmodel.StockViewModelFlex;

/**
 * This class implements UserModel interface which represents User Account
 * that contains list of portfolios associated with the user.
 */
public class UserModelImpl implements UserModel {

  private final String userName;

  private final HashMap<String, PortfolioModel> flexPortfolios;

  private final HashMap<String, PortfolioModel> inflexPortfolios;

  private final List<DCAStrategyModel> dcaStrategyList;
  private final UserDataService service = new UserCSVServiceImpl();

  /**
   * Constructor to create user object with zero or more portfolios.
   *
   * @param flexPortfolios list of flexible portfolio associated with the user
   * @param inflexPortfolios list of inflexible portfolios associated with the user
   * @param dcaStrategyList list of dollar cost averaging strategy in the given portfolio
   */
  public UserModelImpl(HashMap<String, PortfolioModel> flexPortfolios,
                       HashMap<String, PortfolioModel> inflexPortfolios,
                       List<DCAStrategyModel> dcaStrategyList) {

    //TODO : later : now single user
    this.userName = "test";
    if (dcaStrategyList != null) {
      this.dcaStrategyList = dcaStrategyList;
    } else {
      this.dcaStrategyList = new ArrayList<>();
    }

    if (flexPortfolios != null) {
      this.flexPortfolios = flexPortfolios;
    } else {
      this.flexPortfolios = new HashMap<>();
    }

    if (inflexPortfolios != null) {
      this.inflexPortfolios = inflexPortfolios;
    } else {
      this.inflexPortfolios = new HashMap<>();
    }
  }

  @Override
  public List<DCAStrategyModel> getDcaStrategyList() {
    return dcaStrategyList;
  }

  @Override
  public DCAStrategyModel addDCAStrategyToList(DCAStrategyViewModel dca) {
    DCAStrategyModel dcaModel = new DCAStrategyModelImpl(dca.getPortfolioName(),
            dca.getStockMap(), dca.getStartDate().toString(),
            dca.getEndDate().toString(), dca.getInvestedAmount(), dca.getFrequencyDays(),
            dca.getCommissionFee());
    this.dcaStrategyList.add(dcaModel);
    return dcaModel;

  }

  /**
   * Returns userName of the given account.
   *
   * @return userName of the given account
   */
  @Override
  public String getUserName() {
    return userName;
  }

  /**
   * Returns flexible portfolios associated with the given account.
   *
   * @return portfolios associated with the given account
   */
  @Override
  public HashMap<String, PortfolioModel> getFlexPortfolios() {
    return this.flexPortfolios;
  }

  /**
   * Returns inflexible portfolios associated with the given account.
   *
   * @return portfolios associated with the given account
   */
  @Override
  public HashMap<String, PortfolioModel> getInflexPortfolios() {
    return this.inflexPortfolios;
  }

  @Override
  public void addPortfolio(PortfolioModel p) {
    if (p != null) {
      this.inflexPortfolios.put(p.getPortfolioName(), p);
    }
  }

  @Override
  public void addFlexPortfolio(PortfolioModel p) {
    if (p != null) {
      this.flexPortfolios.put(p.getPortfolioName(), p);
    }
  }

  /**
   * Method to check if the user has existing portfolios. Returns true if the profile loaded
   * has portfolios, else false.
   *
   * @return true if the profile loaded has portfolios, else false
   */
  @Override
  public boolean portfoliosExist() {
    return this.flexPortfolios.size() != 0 || this.inflexPortfolios.size() != 0;
  }

  /**
   * Method to check if the portfolio name given as input by the user exists for the profile.
   * Returns true if portfolio is present, else false.
   *
   * @return true if a portfolio exists with the given name input, else false
   */
  @Override
  public boolean validatePortfolioNameDuplicate(String portfolioName) {
    portfolioName = portfolioName.toUpperCase();
    return this.flexPortfolios.containsKey(portfolioName)
            || this.inflexPortfolios.containsKey(portfolioName);
  }

  /**
   * Method to validate if the date input from the user on which they want to check the
   * portfolio value is correct or not.
   *
   * @param dateString date value given as input for getting the portfolio value
   * @return portfolioValueDate the date on which user wants to get the portfolio value
   */
  @Override
  public LocalDate validateDate(String dateString) {
    LocalDate portfolioValueDate;
    try {
      portfolioValueDate = new ValidationServiceImpl().validateDateFormat(dateString);
    } catch (DateTimeException e) {
      throw new DateTimeException("Invalid date format.");
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException(e);
    }
    return portfolioValueDate;
  }

  /**
   * Method to calculate and return value of the portfolio on a certain date.
   *
   * @param date          date on which to get the portfolio value
   * @param portfolioName portfolio for which value needs to be calculated
   * @return value of the portfolio on the given date
   */
  @Override
  public float calculatePortfolioValue(LocalDate date, String portfolioName) throws Exception {
    float portfolioValue = 0;
    PortfolioModel portfolio = getPortfolio(portfolioName);
    try {
      if (checkPortfolioType(portfolioName) == -1) {
        throw new IllegalArgumentException("Portfolio does not exist");
      }
      if (date != null) {
        validateDate(date.toString());
      }
      else {
        throw new IllegalArgumentException("Enter date");
      }
      portfolioValue = portfolio.calculateStockValue(date);
    } catch (DateTimeException e) {
      throw new DateTimeException("Invalid date format.");
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException(e);
    } catch (IOException e) {
      throw new IOException(e);
    }
    return portfolioValue;
  }

  /**
   * Method to return the portfolio depending on whether it's flexible or inflexible.
   *
   * @param portfolioName name of the portfolio to get the
   * @return portfolio model as flexible/inflexible
   */
  private PortfolioModel getPortfolio(String portfolioName) {
    PortfolioModel portfolio = null;
    portfolioName = portfolioName.toUpperCase();
    if (checkPortfolioType(portfolioName) == 1) {
      portfolio = this.flexPortfolios.get(portfolioName);
    } else if (checkPortfolioType(portfolioName) == 0) {
      portfolio = this.inflexPortfolios.get(portfolioName);
    } else {
      throw new IllegalArgumentException("Portfolio does not exist.");
    }
    return portfolio;
  }

  /**
   * Method to calculate and return the composition of a stock (percentage of the stock)
   * in a portfolio.
   *
   * @param portfolioName portfolio for which value needs to be calculated
   * @return hashmap storing the stock data and the percentage of that stock in a given portfolio
   */
  @Override
  public HashMap<String, Float> calculateComposition(String portfolioName,
                                                     LocalDate portfolioCompDate) {
    HashMap<String, Float> stockComposition;
    PortfolioModel portfolio = getPortfolio(portfolioName);
    try {
      if (checkPortfolioType(portfolioName) == -1) {
        throw new IllegalArgumentException("Portfolio does not exist");
      }
      if (checkPortfolioType(portfolioName) == 1) {
        validateDate(portfolioCompDate.toString());
      }
      stockComposition = portfolio.getStockComposition(portfolioCompDate);
    } catch (DateTimeException e) {
      throw new DateTimeException("Invalid date format.");
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException(e);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return stockComposition;
  }

  /**
   * Method loads list of portfolios and its collection of stocks from an external csv file or
   * from the file database and associates it to the given User.
   *
   * @throws IOException when file operations fail
   */
  public void loadUser(String filePath) throws IOException {
    try {
      boolean flexType;
      HashMap<String, PortfolioModel> portfolioListFromFile =
              new UserCSVServiceImpl().loadUserProfile(filePath);

      boolean update = false;
      for (PortfolioModel portfolio : portfolioListFromFile.values()) {
        if (portfolio instanceof PortfolioModelInflexImpl) {
          flexType = false;
          this.addPortfolio(portfolio);
        } else {
          flexType = true;
          this.addFlexPortfolio(portfolio);
        }
        //writing the data from external file to internal User database
        if (filePath != null) {
          new UserCSVServiceImpl().savePortfolioToFile(portfolio, flexType, update);
          update = true;
        }
      }

    } catch (IOException e) {
      throw new IOException("File format is invalid or it does not exist. "
              + "Please load data from a valid external file or create portfolio manually.");
    } catch (NumberFormatException e) {
      throw new NumberFormatException("Cannot buy fractional shares. Please buy whole shares.");
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException(e);
    } catch (DateTimeException e) {
      throw new DateTimeException("Invalid date format.");
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Method to check if the portfolio name given as input by the user exists for the profile.
   * Returns true if portfolio is present, else false.
   *
   * @return true if a portfolio exists with the given name input, else false
   */
  @Override
  public int checkPortfolioType(String portfolioName) {
    portfolioName = portfolioName.toUpperCase();
    if (this.flexPortfolios.containsKey(portfolioName)) {
      return 1;
    } else if (this.inflexPortfolios.containsKey(portfolioName)) {
      return 0;
    }
    return -1;
  }

  @Override
  public void saveFlexPortfolio(String pName, List<StockViewModelFlex> stockSet, boolean b)
          throws IOException {
    try {
      HashMap<String, StockModel> stockMap = new HashMap<>();
      for (Iterator<StockViewModelFlex> it = stockSet.iterator(); it.hasNext(); ) {
        StockViewModelFlex s = it.next();
        StockModel stock = new StockModelFlexImpl(s.getTickerSymbol(), s.getInvestedPrice(),
                s.getStockQuantity(),
                s.getTransactionDate().toString(), s.getBuy(), s.getCommissionFee());
        stockMap.put(s.getTickerSymbol() + s.getTransactionDate() + s.getBuy(), stock);
      }

      PortfolioModel p = new PortfolioModelFlexImpl(pName, stockMap);

      service.savePortfolioToFile(p, true, true);
      this.addFlexPortfolio(p);
    } catch (IOException e) {
      throw new IOException("Unable to save the file. Try again!");
    }
  }

  @Override
  public void createUser() throws IOException {
    new UserCSVServiceImpl().createOverrideEmptyFile();
  }

  @Override
  public boolean validatePortfolioNameFormat(String pName) {
    if (pName == null || pName.equals("")) {
      return false;
    }
    return pName.matches("^[a-zA-Z0-9]*$");
  }

  @Override
  public void savePortfolio(String pName, HashSet<StockViewModel> stockSet, boolean flexType)
          throws IOException {
    try {
      HashMap<String, StockModel> stockMap = new HashMap<>();
      for (Iterator<StockViewModel> it = stockSet.iterator(); it.hasNext(); ) {
        StockViewModel s = it.next();
        StockModel stock = new StockModelImpl(s.getTickerSymbol(), s.getInvestedPrice(),
                s.getStockQuantity());
        stockMap.put(s.getTickerSymbol(), stock);
      }

      PortfolioModel p;
      if (flexType) {
        p = new PortfolioModelFlexImpl(pName, stockMap);
      } else {
        p = new PortfolioModelInflexImpl(pName, stockMap);
      }
      service.savePortfolioToFile(p, flexType, true);
      this.addPortfolio(p);
    } catch (IOException e) {
      throw new IOException("Unable to save the file. Try again!");
    }
  }

  /**
   * Method to calculate and return the cost basis (i.e. the total amount of money invested
   * in a portfolio) for a portfolio. Cost basis will only be calculated for flexible portfolios.
   *
   * @return cost basis for a flexible portfolio
   */
  @Override
  public float getCostBasis(String portfolioName, LocalDate portfolioCostDate) {
    PortfolioModel portfolio = getPortfolio(portfolioName);
    float costBasis = 0;
    try {
      if (checkPortfolioType(portfolioName) == -1) {
        throw new IllegalArgumentException("Portfolio does not exist");
      }
      if (checkPortfolioType(portfolioName) == 1) {
        validateDate(portfolioCostDate.toString());
      }
      costBasis = portfolio.calculateCostBasis(portfolioCostDate);
    } catch (DateTimeException e) {
      throw new DateTimeException("Invalid date format.");
    } catch (IllegalArgumentException | IOException e) {
      throw new IllegalArgumentException(e);
    }
    return costBasis;
  }

  @Override
  public void saveFlexStock(String pName, StockViewModelFlex s) throws IOException {
    new ValidationServiceImpl().composeFlexStocks(this.getFlexPortfolios().get(pName)
            .getStockMap(), s);

    boolean update = false;
    for (PortfolioModel portfolio : this.getFlexPortfolios().values()) {
      boolean flexType = true;
      new UserCSVServiceImpl().savePortfolioToFile(portfolio, flexType, update);
      update = true;
    }
    for (PortfolioModel portfolio : this.getInflexPortfolios().values()) {
      boolean flexType = false;
      new UserCSVServiceImpl().savePortfolioToFile(portfolio, flexType, update);
      update = true;
    }
  }
}