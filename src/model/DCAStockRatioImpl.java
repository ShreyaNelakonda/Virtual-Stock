package model;

import java.io.IOException;

import service.stocks.StockCSVServiceImpl;

/**
 * Class implement DCAStockRatio which stores the stock ticker and the percentage of amount
 * to be invested in that stock.
 */
public class DCAStockRatioImpl implements DCAStockRatio {

  private final String tickerName;
  private final double proportion;

  /**
   * Constructs a DCAStockRatio which consists of the stock ticker symbol and the percentage of
   * amount to be invested in that stock.
   *
   * @param tickerName ticker symbol of the stock
   * @param proportion percentage of amount to be invested that stock ticker symbol
   */
  public DCAStockRatioImpl(String tickerName, double proportion) {
    try {
      if (tickerName == null || tickerName.equals("")) {
        throw new IllegalArgumentException("Ticker cannot be empty");
      }
      StockCSVServiceImpl.getInstance().checkDownloadStockData(tickerName);
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("Stock Ticker does not exist");
    } catch (IOException e) {
      throw new RuntimeException("Unable to update data for " + tickerName);
    }

    if (proportion > 100) {
      throw new IllegalArgumentException("Percentage of amount to be invested in a stock"
              + " cannot be greater than 100%.");
    }
    if (proportion < 0) {
      throw new IllegalArgumentException("Percentage of amount to be invested in a stock "
              + "cannot be less than 0");
    }
    this.tickerName = tickerName.toUpperCase();
    this.proportion = proportion;
  }

  @Override
  public String getTicker() {
    return this.tickerName;
  }

  @Override
  public double getProportion() {
    return this.proportion;
  }
}
