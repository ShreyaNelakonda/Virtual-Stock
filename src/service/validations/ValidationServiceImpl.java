package service.validations;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import model.PortfolioModel;
import model.StockModel;
import model.StockModelFlexImpl;
import model.StockModelImpl;
import model.UserModel;
import viewmodel.StockViewModel;
import viewmodel.StockViewModelFlex;

/**
 * Validation service implementation for ValidationService.
 */
public class ValidationServiceImpl implements ValidationService {

  @Override
  public LocalDate validateDateFormat(String dateString) throws IllegalArgumentException {
    try {
      LocalDate date = LocalDate.parse(dateString,
              DateTimeFormatter.ofPattern("uuuu-MM-dd")
                      .withResolverStyle(ResolverStyle.STRICT));
      LocalDate today = LocalDate.now();
      if (date.isAfter(today)) {
        throw new IllegalArgumentException("The date should not be a future date. "
                + "Please try again.");
      }
      return date;
    } catch (DateTimeException e) {
      throw new DateTimeException("Date entered or the format is invalid. Please try again.");
    }
  }

  @Override
  public void composeInflexStocks(HashSet<StockViewModel> stockSet, float price,
                                  String ticker, int qty) {
    StockViewModel existingStock = stockSet.stream()
            .filter(stock -> ticker.equalsIgnoreCase(stock.getTickerSymbol()))
            .findFirst()
            .orElse(null);

    if (existingStock != null) {
      stockSet.remove(existingStock);
      int newQty = (int) (existingStock.getStockQuantity() + qty);
      StockViewModel updatedStock = new StockModelImpl(ticker, price, newQty);
      stockSet.add(updatedStock);
    } else {
      StockViewModel newStock = new StockModelImpl(ticker, price, qty);
      stockSet.add(newStock);
    }
  }

  @Override
  public void composeFlexStocks(HashMap<String, StockModel> stockMap,
                                StockViewModelFlex stockData) {
    List<StockModel> stockList = new ArrayList<StockModel>(stockMap.values());

    StockViewModelFlex existingStock = stockList.stream()
                    .filter(stock -> (stockData.getTickerSymbol()
                            .equalsIgnoreCase(stock.getTickerSymbol()))
                    && (stockData.getTransactionDate().equals(stock.getTransactionDate()))
                    && (stockData.getBuy() == stock.getBuy()))
            .findFirst().orElse(null);
    if (existingStock != null) {
      float newQty = (existingStock.getStockQuantity() + stockData.getStockQuantity());
      float newCommission = existingStock.getCommissionFee() + stockData.getCommissionFee();
      StockModel stock = new StockModelFlexImpl(stockData.getTickerSymbol(),
              stockData.getInvestedPrice(), newQty, stockData.getTransactionDate().toString(),
              stockData.getBuy(), newCommission);
      stockMap.put(stock.getTickerSymbol() + "" + stock.getTransactionDate() + ""
              + stock.getBuy(), stock);
    } else {
      StockModel newStock = new StockModelFlexImpl(stockData.getTickerSymbol(),
              stockData.getInvestedPrice(), stockData.getStockQuantity(),
              stockData.getTransactionDate().toString(), stockData.getBuy(),
              stockData.getCommissionFee());
      stockMap.put(newStock.getTickerSymbol() + "" + newStock.getTransactionDate() + ""
                      + newStock.getBuy(),
              newStock);
    }
  }

  @Override
  public void validateTwoDatesAfter(LocalDate actualDate, LocalDate compareDate) {
    if (actualDate.isAfter(compareDate)) {
      throw new IllegalArgumentException("Date details are not available.");
    }
  }

  @Override
  public void transactionSequence(UserModel userModel, StockViewModelFlex stockData,
                                  String pname) {
    PortfolioModel portfolio = userModel.getFlexPortfolios().get(pname);
    HashMap<String, StockModel> stockList = portfolio.getStockMap();

    List<StockModel> stockBeforeNew = stockList.entrySet().stream()
            .filter(stock -> stock.getValue().getTickerSymbol()
                    .equals(stockData.getTickerSymbol())
                    && stock.getValue().getTransactionDate()
                    .isBefore(stockData.getTransactionDate()))
            .map(Entry::getValue)
            .collect(Collectors.toList());

    List<StockViewModelFlex> boughtStocks = stockBeforeNew.stream()
            .filter(stock -> (stock.getBuy())).collect(Collectors.toList());

    List<StockViewModelFlex> soldStocks = stockBeforeNew.stream()
            .filter(stock -> (!stock.getBuy())).collect(Collectors.toList());

    List<StockModel> soldStockOnSameDate = stockList.entrySet().stream()
            .filter(stock -> stock.getValue().getTickerSymbol()
                    .equals(stockData.getTickerSymbol())
                    && stock.getValue().getTransactionDate()
                    .isEqual(stockData.getTransactionDate())
                    && !stock.getValue().getBuy())
            .map(Entry::getValue)
            .collect(Collectors.toList());

    double buyQty = boughtStocks.stream().mapToDouble(x -> x.getStockQuantity()).sum();
    double soldQty = soldStocks.stream().mapToDouble(x -> x.getStockQuantity()).sum();
    double soldQtySameDay = soldStockOnSameDate.stream()
            .mapToDouble(x -> x.getStockQuantity()).sum();

    double newSoldQty = soldQtySameDay + soldQty + stockData.getStockQuantity();

    if (newSoldQty > buyQty) {
      throw new IllegalArgumentException("Not enough shares of  " + stockData.getTickerSymbol()
              + " available in the portfolio to sell.");
    }
  }

  @Override
  public void validateStartEndDate(LocalDate startDate, LocalDate endDate) {
    if (startDate.isAfter(LocalDate.now())) {
      throw new IllegalArgumentException("Start date cannot be in the future.");
    }
    if (endDate.isAfter(LocalDate.now())) {
      throw new IllegalArgumentException("End date cannot be in the future.");
    }
    if (startDate.isAfter(endDate)) {
      throw new IllegalArgumentException("End date cannot be before start date.");
    }
  }

  /**
   * Method to validate portfolio name.
   * 1) checks for alphanumeric characters.
   * 2) checks for duplicate case-insensitive names.
   *
   * @param pName     name of the portfolio
   * @param userModel user profile
   * @return int corresponding to its validation error, otherwise -1
   */
  @Override
  public int validatePortfolioName(String pName, UserModel userModel) {
    if (!userModel.validatePortfolioNameFormat(pName)) {
      return 1;
    } else if (userModel.validatePortfolioNameDuplicate(pName)) {
      return 2;
    }
    return -1;
  }
}
