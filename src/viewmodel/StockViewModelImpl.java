package viewmodel;

/**
 * Class that implements read only methods of Stock View model.
 */
public class StockViewModelImpl implements StockViewModel {

  private final StockViewModel stock;

  public StockViewModelImpl(StockViewModel stock) {
    this.stock = stock;
  }

  /**
   * Method to return the Ticker Symbol of the given stock.
   *
   * @return tickerSymbol of the Stock
   */
  @Override
  public String getTickerSymbol() {
    return stock.getTickerSymbol();
  }

  /**
   * Method to return the buying price of the given stock.
   *
   * @return buying price of the Stock
   */
  @Override
  public float getInvestedPrice() {
    return stock.getInvestedPrice();
  }

  /**
   * Method to return the quantity of the given stock.
   *
   * @return quantity of the Stock
   */
  @Override
  public float getStockQuantity() {
    return stock.getStockQuantity();
  }

}
