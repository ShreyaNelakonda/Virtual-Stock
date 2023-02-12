package viewmodel;

/**
 * Interface that describes methods of read only Stock View model.
 */
public interface StockViewModel {

  /**
   * Method to return the Ticker Symbol of the given stock.
   *
   * @return tickerSymbol of the Stock
   */
  String getTickerSymbol();

  /**
   * Method to return the buying price of the given stock.
   *
   * @return buying price of the Stock
   */
  float getInvestedPrice();

  /**
   * Method to return the quantity of the given stock.
   *
   * @return quantity of the Stock
   */
  float getStockQuantity();

}
