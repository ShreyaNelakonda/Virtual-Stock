package model;

import java.time.DateTimeException;
import java.time.LocalDate;

import service.validations.ValidationServiceImpl;

/**
 * StockModelFlexImpl that implements methods of Flexible Stock model.
 */
public class StockModelFlexImpl extends StockModelImpl implements StockModel {

  private final LocalDate dateOfTransaction;
  private final boolean buy;
  private final float commissionFee;

  /**
   * Constructor for Stock Class that takes ticker symbol, quantity, current price, transaction
   * date and buy/sell as inputs.
   *
   * @param tickerSymbol    ticker symbol of the stock
   * @param currentPrice    closing price of the stock on the latest working day
   * @param quantity        quantity of the stock user wants to add to the portfolio
   * @param transactionDate date of the transaction for that stock
   * @param buy             boolean to check whether the stock is bought or sold
   * @param commissionFee   commission fee charged for the transaction
   */
  public StockModelFlexImpl(String tickerSymbol, float currentPrice, float quantity,
                            String transactionDate, boolean buy, float commissionFee) {

    super(tickerSymbol, currentPrice, quantity);

    try {
      this.dateOfTransaction = new ValidationServiceImpl().validateDateFormat(transactionDate);
    } catch (DateTimeException e) {
      throw new DateTimeException("Invalid date format.");
    }
    if (commissionFee < 0) {
      throw new IllegalArgumentException("Commission Fee should be greater than or equal to 0.");
    }
    this.buy = buy;
    this.commissionFee = commissionFee;
  }

  /**
   * Method to return the date of transaction for the given stock.
   *
   * @return transactionDate of the Stock
   */
  public LocalDate getTransactionDate() {
    return this.dateOfTransaction;
  }

  /**
   * Method to return if the transaction is buying/selling stocks.
   *
   * @return if stocks are bought or sold in the transaction
   */
  public boolean getBuy() {
    return this.buy;
  }

  /**
   * Method to return the commission fee for that transaction.
   *
   * @return the commission fee for the given transaction
   */
  public float getCommissionFee() {
    return this.commissionFee;
  }

  /**
   * Method to return the Ticker Symbol of the given stock.
   *
   * @return tickerSymbol of the Stock
   */
  public String getTickerSymbol() {
    return super.getTickerSymbol();
  }

  /**
   * Method to return the buying price of the given stock.
   *
   * @return buying price of the Stock
   */
  public float getInvestedPrice() {
    return super.getInvestedPrice();
  }

  /**
   * Method to return the quantity of the given stock.
   *
   * @return quantity of the Stock
   */
  public float getStockQuantity() {
    return super.getStockQuantity();
  }
}
