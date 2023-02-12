import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;

import model.PortfolioModel;
import model.PortfolioModelInflexImpl;
import model.StockModel;

import static org.junit.Assert.fail;

/**
 * A JUnit test class for the Portfolio class.
 */
public class PortfolioModelTest {
  private String pName;
  private HashMap<String, StockModel> stockList;
  private PortfolioModel p;

  @Before
  public void setup() {
    pName = "TESTPORTFOLIO";
    stockList = TestHelper.getStockListMock();
  }

  @Test
  public void testConstructorNullParam() {
    //case 1 : Null portfolioName
    stockList = TestHelper.getStockListMock();
    try {
      p = new PortfolioModelInflexImpl(null, stockList);
      Assert.fail("Must throw an exception for null portfolio name.");
    } catch (IllegalArgumentException e) {
      //pass
    }

    //case 2 : Null stockList
    pName = "TestPortfolio";
    try {
      p = new PortfolioModelInflexImpl(pName, null);
      Assert.fail("Must throw an exception for null stock list.");
    } catch (IllegalArgumentException e) {
      //pass
    }
  }

  @Test
  public void testConstructorEmptyParam() {
    //Check Empty Constructor - compile time error
    //Portfolio p = new Portfolio();

    //case 1 : Empty portfolio name
    stockList = TestHelper.getStockListMock();
    try {
      p = new PortfolioModelInflexImpl("", stockList);
      Assert.fail("Must throw an exception for empty portfolio name.");
    } catch (IllegalArgumentException e) {
      //pass
    }

    //case 2 : Empty stockList
    pName = "testPortfolio";
    stockList = new HashMap<>();
    try {
      p = new PortfolioModelInflexImpl(pName, stockList);
      Assert.fail("Must throw an exception for empty stock list.");
    } catch (IllegalArgumentException e) {
      //pass
    }
  }

  @Test
  public void testConstructorValid() {
    String pDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    try {
      p = new PortfolioModelInflexImpl(pName, stockList);
      Assert.assertNotEquals("Portfolio cannot be null.", p, null);
      Assert.assertEquals("Portfolio Name does not match.", pName, p.getPortfolioName());
      Assert.assertEquals("Portfolio stock list does not match.", stockList, p.getStockMap());
    } catch (IllegalArgumentException e) {
      Assert.fail("Must not throw IllegalArgumentException.\n" + e);
    }
  }

  @Test
  public void testGetTotalQuantity() {
    HashMap<String, PortfolioModel> portfolio = TestHelper.getPortfolioListMock();
    Assert.assertEquals(75, portfolio.get("PORTFOLIO1")
                    .getTotalQuantity(null, null, null));
    Assert.assertEquals(20, portfolio.get("PORTFOLIO2")
                    .getTotalQuantity(null, null, null));
  }

  @Test
  public void testGetStockComposition() {
    HashMap<String, PortfolioModel> portfolio = TestHelper.getPortfolioListMock();
    HashMap<String, StockModel> stocks1 = portfolio.get("PORTFOLIO1").getStockMap();
    HashMap<String, Float> stockCompositionMap =
            portfolio.get("PORTFOLIO1").getStockComposition(null);

    Assert.assertNotNull(stockCompositionMap);
    float composition = 0;
    for (Float comp : stockCompositionMap.values()) {
      composition = composition + comp;
    }
    Assert.assertEquals(100.00, composition, 0);
  }

  @Test
  public void testCalculateStockValue() throws Exception {
    DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDate date = LocalDate.parse("2022-11-01", format);

    HashMap<String, PortfolioModel> portfolio = TestHelper.getPortfolioListMock();
    Assert.assertEquals("5013.20", String.format("%.2f",
            portfolio.get("PORTFOLIO1").calculateStockValue(date)));
    Assert.assertEquals("2170.90", String.format("%.2f",
            portfolio.get("PORTFOLIO2").calculateStockValue(date)));
    date = LocalDate.parse("1997-10-01", format);
    try {
      portfolio.get("PORTFOLIO1").calculateStockValue(date);
      fail("Did not throw an exception for stock data not available for given date");
    } catch (IOException e) {
      //pass
    }
    date = LocalDate.parse("2020-12-18", format);
    Assert.assertEquals("64259.88", String.format("%.2f",
            portfolio.get("PORTFOLIO1").calculateStockValue(date)));
  }

}
