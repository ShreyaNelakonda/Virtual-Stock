import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import model.StockModelImpl;

/**
 * A JUnit test class for the Stock class.
 */
public class StockModelTest {

  private String tickerSymbol;
  private StockModelImpl stock;
  private final float composition = 23;

  @Before
  public void setup() {
    tickerSymbol = "APPL";
    stock = new StockModelImpl(tickerSymbol, (float) 47.23, 13);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testEmptyTickerSymbol() {
    String tickerSymbol = "";
    StockModelImpl emptyTickerSymbol = new StockModelImpl(tickerSymbol, 100, 2);
  }

  @Test
  public void testCurrentPriceInvalid() {
    StockModelImpl testCurrentPriceNegative;
    StockModelImpl testCurrentPriceZero;

    try {
      testCurrentPriceNegative = new StockModelImpl("GOOG", -10, 2);
      Assert.fail("Must throw an exception for negative price.");
    } catch (IllegalArgumentException e) {
      //pass
    }

    try {
      testCurrentPriceZero = new StockModelImpl("GOOG", 0, 2);
      Assert.fail("Must throw an exception if price is zero.");
    } catch (IllegalArgumentException e) {
      //pass
    }
  }

  @Test
  public void testQuantityInvalid() {
    StockModelImpl testQuantityNegative;
    StockModelImpl testQuantityZero;

    try {
      testQuantityNegative = new StockModelImpl("GOOG", 100, -2);
      Assert.fail("Must throw an exception for negative quantity.");
    } catch (IllegalArgumentException e) {
      //pass
    }

    try {
      testQuantityZero = new StockModelImpl("GOOG", 100, 0);
      Assert.fail("Must throw an exception is quantity is zero.");
    } catch (IllegalArgumentException e) {
      //pass
    }
  }

  @Test
  public void testStockConstructor() {
    StockModelImpl stockConstructor = new StockModelImpl("GOOG", 100, 15);
    Assert.assertEquals("GOOG", stockConstructor.getTickerSymbol());
    Assert.assertEquals(100, stockConstructor.getInvestedPrice(), 0);
    Assert.assertEquals(15, stockConstructor.getStockQuantity(), 0);

    stockConstructor = new StockModelImpl("GOOG", (float) 93.89, (float) (0.5));
    Assert.assertEquals("GOOG", stockConstructor.getTickerSymbol());
    Assert.assertEquals("93.89", String.format("%.2f", stockConstructor.getInvestedPrice()));
    Assert.assertEquals(0.5, stockConstructor.getStockQuantity(), 0);
  }

  @Test
  public void testStockGetters() {
    Assert.assertEquals(tickerSymbol, stock.getTickerSymbol());
    Assert.assertEquals("47.23", String.format("%.2f", stock.getInvestedPrice()));
    Assert.assertEquals(13, stock.getStockQuantity(), 0);
  }

}