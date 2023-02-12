import org.junit.Assert;
import org.junit.Test;

import java.text.DecimalFormat;

import dto.StockDatePriceDTO;
import service.stocks.StockCSVServiceImpl;
import service.stocks.StocksDataService;

/**
 * A JUnit test class for the StockCSVService class.
 */
public class StockCSVServiceTest {

  private static final DecimalFormat DFORMAT = new DecimalFormat("0.00");
  private final StocksDataService service = StockCSVServiceImpl.getInstance();

  @Test
  public void testGetInvalidStockDataFromAPI() {
    try {
      service.getStockDataFromAPI("bvdbvdsd");
      Assert.fail("Must throw an exception for an invalid stock ticker.");
    } catch (Exception e) {
      //pass
    }
  }

  @Test
  public void testGetValidStockDataFromAPI() {
    try {
      service.getStockDataFromAPI("AMZN");
    } catch (Exception e) {
      Assert.fail("Must throw an exception for an invalid stock ticker.");
    }
  }

  @Test
  public void testGetStockPriceByTickerValid() {
    try {
      StockDatePriceDTO s = service.getStockPriceByTicker("AAPL");
      Float price = s.getPrice();
      Assert.assertEquals("Price does not match.", "138.88", DFORMAT.format(price));
    } catch (Exception e) {
      Assert.fail("Must not throw an exception for valid ticker");
    }
  }

  /**
   * Test to check if validTicker() works correctly for valid ticker as argument.
   */
  @Test
  public void testValidTicker() {
    for (String ticker : TestHelper.validTickersFromFile()) {
      try {
        service.checkDataExistsForTicker(ticker);
      } catch (IllegalArgumentException e) {
        Assert.fail("Must not throw an exception for a valid ticker.");
      }
    }
  }

  /**
   * Test to check if validTicker() works correctly for invalid ticker as argument.
   */
  @Test
  public void testInvalidTicker() {
    String[] invalidList = {"xyz", "1234", "random", "hahaha", "ooo"};
    for (String ticker : invalidList) {
      try {
        service.checkDataExistsForTicker(ticker);
        Assert.fail("Must throw an exception for an invalid ticker.");
      } catch (IllegalArgumentException e) {
        //pass
      }
    }
  }
}
