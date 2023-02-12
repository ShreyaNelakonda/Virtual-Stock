package model;

import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import service.stocks.StockCSVServiceImpl;
import service.stocks.StocksDataService;
import service.validations.ValidationServiceImpl;
import viewmodel.StockViewModelFlex;

/**
 * Class implements DCAStrategyModel which computes the dollar cost averaging strategy for given
 * portfolio.
 */
public class DCAStrategyModelImpl implements DCAStrategyModel {

  private final String portfolioName;
  private final List<DCAStockRatio> stockMap;
  private final LocalDate startDate;
  private final double investedAmount;
  private final int frequencyDays;
  private final StocksDataService stockService = StockCSVServiceImpl.getInstance();
  private final ValidationServiceImpl validationService = new ValidationServiceImpl();
  private final float commissionFee;
  private LocalDate endDate;

  /**
   * Constructs a DCAStrategyModel which consists of all the details needed to compute dollar cost
   * average strategy for a portfolio.
   *
   * @param portfolioName  name of the portfolio
   * @param stockMap       map of stocks and the percentage of amount to be invested in that stock
   * @param startDate      start date of the dollar cost averaging strategy
   * @param endDate        end date of the dollar cost averaging strategy
   * @param investedAmount amount invested in the strategy
   * @param frequencyDays  frequency in days at which stocks should be invested the portfolio
   * @param commissionFee  commission fee for the investment transaction
   */
  public DCAStrategyModelImpl(String portfolioName, List<DCAStockRatio> stockMap,
                              String startDate, String endDate,
                              double investedAmount, int frequencyDays, float commissionFee) {

    if (portfolioName == null || portfolioName.equals("")) {
      throw new IllegalArgumentException("Portfolio Name cannot be empty");
    }
    if (startDate == null) {
      throw new IllegalArgumentException("Dates cannot be empty");
    }
    if (endDate == null) {
      endDate = "";
    }
    if (stockMap == null) {
      throw new IllegalArgumentException("No StockList found.");
    }
    if (!endDate.equals("--")) {
      try {
        this.endDate = new ValidationServiceImpl().validateDateFormat(endDate);
      } catch (DateTimeException e) {
        throw new DateTimeException("End Date entered is invalid. Please try again.");
      } catch (IllegalArgumentException e) {
        this.endDate = LocalDate.now();
      }
    } else {
      this.endDate = LocalDate.now();
    }
    try {
      this.startDate = new ValidationServiceImpl().validateDateFormat(startDate);
    } catch (DateTimeException e) {
      throw new DateTimeException("Start Date entered is invalid. Please try again.");
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("Start date should not be a future date. "
              + "Please try again.");
    }
    try {
      validationService.validateTwoDatesAfter(this.startDate, this.endDate);
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("Start Date cannot be after End Date.");
    }
    if (investedAmount <= 0) {
      throw new IllegalArgumentException("Invested amount must be greater than 0.");
    }
    if (commissionFee < 0) {
      throw new IllegalArgumentException("Commission Fee cannot be less than 0.");
    }
    if (frequencyDays < 0) {
      throw new IllegalArgumentException("Frequency of investments cannot be less than 0.");
    }
    this.portfolioName = portfolioName;
    this.stockMap = stockMap;
    this.commissionFee = commissionFee;
    this.investedAmount = investedAmount;
    this.frequencyDays = frequencyDays;
  }

  @Override
  public void computeDollarCostAveraging(UserModel userModel) {
    try {
      validateProportion();
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException(e);
    }
    List<LocalDate> dayBins = new ArrayList<>();
    List<StockViewModelFlex> stockList = new ArrayList<>();
    //double amountToInvest = this.investedAmount - this.commissionFee;
    dayBins.add(this.startDate);
    LocalDate date = startDate;
    while (endDate.isAfter(date.plusDays(frequencyDays)) && !endDate.equals(startDate)) {
      date = date.plusDays(frequencyDays);
      dayBins.add(date);
    }
    for (DCAStockRatio stock : stockMap) {
      String tickerSymbol = stock.getTicker();
      double proportion = stock.getProportion();
      double amountInvStock = (this.investedAmount * (proportion / 100)) - this.commissionFee;
      for (LocalDate transactionDate : dayBins) {
        try {
          if (transactionDate.isBefore(LocalDate.now())) {
            float currentPrice = stockService.getStockValueOnCertainDate(tickerSymbol,
                    transactionDate);
            float quantity = (float) (amountInvStock / currentPrice);

            stockList.add(new StockModelFlexImpl(tickerSymbol, currentPrice, quantity,
                    String.valueOf(transactionDate), true, 0));
          }
        } catch (IOException e) {
          throw new IllegalArgumentException("Ticker details not available.");
        }
      }
    }
    if (userModel.checkPortfolioType(portfolioName) == 1) {
      for (StockViewModelFlex stock : stockList) {
        try {
          userModel.saveFlexStock(portfolioName, stock);
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      }
    } else if (userModel.checkPortfolioType(portfolioName) == -1) {
      try {
        userModel.saveFlexPortfolio(portfolioName, stockList, true);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }

  private void validateProportion() {
    double proportion = 0;
    for (DCAStockRatio stock : stockMap) {
      proportion = proportion + stock.getProportion();
    }
    if (proportion != 100) {
      throw new IllegalArgumentException("Sum of percentage to be invested in each stock must "
              + "be equal to 100%");
    }
  }

  @Override
  public String getPortfolioName() {
    return this.portfolioName;
  }

  @Override
  public List<DCAStockRatio> getStockMap() {
    return this.stockMap;
  }

  @Override
  public LocalDate getStartDate() {
    return this.startDate;
  }

  @Override
  public LocalDate getEndDate() {
    return this.endDate;
  }

  @Override
  public double getInvestedAmount() {
    return this.investedAmount;
  }

  @Override
  public int getFrequencyDays() {
    return this.frequencyDays;
  }

  @Override
  public float getCommissionFee() {
    return this.commissionFee;
  }
}
