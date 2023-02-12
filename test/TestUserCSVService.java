import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;

import model.PortfolioModel;
import model.PortfolioModelInflexImpl;
import model.StockModel;
import model.UserModelImpl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * A JUnit test class for the UserCSVService class.
 */
public class TestUserCSVService {
  @Test
  public void testLoadExternalPortfolio() throws IOException {

    HashMap<String, PortfolioModel> expectedPortfolios = new HashMap<>();
    expectedPortfolios.put("PORTFOLIO1", new PortfolioModelInflexImpl("PORTFOLIO1",
            TestHelper.getStockListMock1()));
    expectedPortfolios.put("PORTFOLIO2", new PortfolioModelInflexImpl("PORTFOLIO2",
            TestHelper.getStockListMock2()));
    HashMap<String, StockModel> expectedStocks = expectedPortfolios.get("PORTFOLIO1").getStockMap();

    UserModelImpl userModelImpl = new UserModelImpl(null, null, null);
    userModelImpl.loadUser("res/testfiles/validFileUpload.csv");
    HashMap<String, PortfolioModel> actualPortfolios = userModelImpl.getFlexPortfolios();
    HashMap<String, StockModel> actualStocks = actualPortfolios.get("PORTFOLIO1").getStockMap();
    Assert.assertEquals(expectedPortfolios.size(), actualPortfolios.size());
    Assert.assertEquals(expectedStocks.size(), actualStocks.size());
    assertTrue(actualPortfolios.containsKey("PORTFOLIO1"));
    assertTrue(actualPortfolios.containsKey("PORTFOLIO2"));

    assertTrue(actualStocks.containsKey("META"));
    assertTrue(actualStocks.containsKey("GOOG"));
    assertTrue(actualStocks.containsKey("AAPL"));
    assertTrue(actualStocks.containsKey("NKLA"));
    assertFalse(actualStocks.containsKey("TSLA"));
  }
}
