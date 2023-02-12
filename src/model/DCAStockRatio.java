package model;

/**
 * Interface supports the implementation the dollar cost averaging strategy by storing the
 * stock ticker and the percentage of amount to be invested in that stock.
 */
public interface DCAStockRatio {

  /**
   * Method to get the ticker symbol of the stock.
   *
   * @return ticker symbol of the stock
   */
  String getTicker();

  /**
   * Method to get the percentage of total invested amount that should
   * be invested in the given stock.
   *
   * @return the percentage of amount to be invested in the given stock
   */
  double getProportion();
}
