package viewmodel;

import java.time.LocalDate;

/**
 * Class that implements read only methods of Flexible Stock View model.
 */
public class StockViewModelFlexImpl extends StockViewModelImpl implements StockViewModelFlex {

  private final StockViewModelFlex stock;

  /**
   * Constructor initialize read only stock flexible object.
   * @param stock stock to be made read only
   */
  public StockViewModelFlexImpl(StockViewModelFlex stock) {
    super(stock);
    this.stock = stock;
  }

  /**
   * Method to return the date of transaction for the given stock.
   *
   * @return transactionDate of the Stock
   */
  public LocalDate getTransactionDate() {
    return stock.getTransactionDate();
  }

  /**
   * Method to return if the transaction is buying/selling stocks.
   *
   * @return if stocks are bought or sold in the transaction
   */
  public boolean getBuy() {
    return stock.getBuy();
  }

  /**
   * Method to return the commission fee for that transaction.
   *
   * @return the commission fee for the given transaction
   */
  public float getCommissionFee() {
    return stock.getCommissionFee();
  }

}
