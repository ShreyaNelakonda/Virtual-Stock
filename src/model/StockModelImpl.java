package model;

import java.time.LocalDate;
import java.util.Objects;

/**
 * This class represents a single Stock for a given ticker symbol and the current price of the
 * stock and the quantity to be added to the portfolio.
 */
public class StockModelImpl implements StockModel {

  private final String tickerSymbol;
  private final float investedPrice;
  private float quantity;


  /**
   * Constructor for Stock Class that takes ticker symbol, quantity and current price as inputs.
   * @param tickerSymbol ticker symbol of the stock
   * @param currentPrice closing price of the stock on the latest working day
   * @param quantity quantity of the stock user wants to add to the portfolio
   */
  public StockModelImpl(String tickerSymbol, float currentPrice, float quantity) {
    if (tickerSymbol.isEmpty() || tickerSymbol == null) {
      throw new IllegalArgumentException("Ticker Symbol is required.");
    }

    if (currentPrice <= 0) {
      throw new IllegalArgumentException("Current Price should be greater than 0.");
    }

    if (quantity <= 0) {
      throw new IllegalArgumentException("Quantity should be greater than 0.");
    }

    //check tickerSymbol
    this.tickerSymbol = tickerSymbol.toUpperCase();
    this.investedPrice = currentPrice;
    this.quantity = quantity;
  }

  /**
   * Method to return the Ticker Symbol of the given stock.
   *
   * @return tickerSymbol of the Stock
   */
  public String getTickerSymbol() {
    return this.tickerSymbol;
  }

  /**
   * Method to return the buying price of the given stock.
   *
   * @return buying price of the Stock
   */
  public float getInvestedPrice() {
    return this.investedPrice;
  }

  /**
   * Method to return the quantity of the given stock.
   *
   * @return quantity of the Stock
   */
  public float getStockQuantity() {
    return this.quantity;
  }

  /**
   * Stock objects are same if their name is same.
   * @param o Object
   * @return if object is equal or not
   */
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StockModelImpl stock = (StockModelImpl) o;
    return tickerSymbol.equalsIgnoreCase(stock.tickerSymbol);
  }

  public int hashCode() {
    return Objects.hash(tickerSymbol);
  }

  @Override
  public LocalDate getTransactionDate() {
    return null;
  }

  @Override
  public boolean getBuy() {
    return false;
  }

  @Override
  public float getCommissionFee() {
    return 0;
  }
}
