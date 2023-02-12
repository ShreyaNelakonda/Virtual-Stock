package dto;

/**
 * Data transfer object for Stock Date and Price.
 */
public class StockDatePriceDTO {
  private final String ticker;

  private final float price;

  private final String date;

  /**
   * Constructor to initialize the stockDTO object.
   *
   * @param ticker stock symbol
   * @param price  price of the stock
   * @param date   date of the transaction
   */
  public StockDatePriceDTO(String ticker, float price, String date) {
    this.ticker = ticker;
    this.price = price;
    this.date = date;
  }

  /**
   * Returns stock symbol.
   *
   * @return stock symbol
   */
  public String getTicker() {
    return ticker;
  }

  /**
   * Returns stock price.
   *
   * @return stock price
   */
  public float getPrice() {
    return price;
  }

  /**
   * Returns stock date.
   *
   * @return stock date
   */
  public String getDate() {
    return date;
  }
}
