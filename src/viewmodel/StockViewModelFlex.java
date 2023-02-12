package viewmodel;

import java.time.LocalDate;

/**
 * Interface that describes methods of flexible read only Stock View model.
 */
public interface StockViewModelFlex extends StockViewModel {

  /**
   * Method to return the date of transaction for the given stock.
   *
   * @return transactionDate of the Stock
   */
  LocalDate getTransactionDate();

  /**
   * Method to return if the transaction is buying/selling stocks.
   *
   * @return if stocks are bought or sold in the transaction
   */
  boolean getBuy();

  /**
   * Method to return the commission fee for that transaction.
   *
   * @return the commission fee for the given transaction
   */
  float getCommissionFee();

}
