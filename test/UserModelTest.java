import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import model.PortfolioModel;
import model.PortfolioModelInflexImpl;
import model.StockModel;
import model.UserModel;
import model.UserModelImpl;
import viewmodel.StockViewModelFlex;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * A JUnit test class for the UserModel class.
 */
public class UserModelTest {
  UserModel userModel;

  /**
   * Test to check Constructor for UserModel with empty portfolios.
   */
  @Test
  public void testUserConstructorWithoutPortfolio() {
    userModel = new UserModelImpl(null, null, null);
    Assert.assertNotEquals("User cannot be null.", null, userModel);
    Assert.assertEquals("Username does not match.", "test",
            userModel.getUserName());
    Assert.assertEquals("Constructor failed for flexible portfolio.",
            new HashMap<String, PortfolioModel>(), userModel.getFlexPortfolios());
    Assert.assertEquals("Constructor failed for inflexible portfolio.",
            new HashMap<String, PortfolioModel>(), userModel.getInflexPortfolios());
  }

  /**
   * Test to check Constructor for UserModel with non-empty portfolios.
   */
  @Test
  public void testUserConstructorWithFlexInflexPortfolio() {
    userModel = new UserModelImpl(TestHelper.getFlexPortfolio(), TestHelper.getInflexPortfolio(),
            null);
    Assert.assertNotEquals("User cannot be null.", null, userModel);
    Assert.assertEquals("Username does not match.", "test",
            userModel.getUserName());
    Assert.assertNotEquals("Constructor failed. Flex Portfolio cannot be null.",
            null, userModel.getFlexPortfolios());
    Assert.assertEquals("Portfolios must have same content.",
            TestHelper.getFlexPortfolio().get("P1").getPortfolioName(),
            userModel.getFlexPortfolios().get("P1").getPortfolioName());

    Assert.assertNotEquals("Constructor failed. Inflex Portfolio cannot be null.",
            null, userModel.getInflexPortfolios());
    Assert.assertEquals("Portfolios must have same content.",
            TestHelper.getInflexPortfolio().get("P1").getPortfolioName(),
            userModel.getFlexPortfolios().get("P1").getPortfolioName());
  }

  /**
   * Test to check Constructor for UserModel with empty portfolios.
   */
  @Test
  public void testAddInflexPortfolio() {
    userModel = new UserModelImpl(null, TestHelper.getInflexPortfolio(), null);
    Assert.assertNotEquals("Constructor failed. Portfolio cannot be null.",
            null, userModel.getInflexPortfolios());
    Assert.assertEquals("Portfolios must have same content.",
            TestHelper.getInflexPortfolio().get("P1").getPortfolioName(),
            userModel.getInflexPortfolios().get("P1").getPortfolioName());

    //Adding new portfolio to the User
    PortfolioModel newPortfolio = new PortfolioModelInflexImpl("P4",
            TestHelper.getStockListMock());
    userModel.addPortfolio(newPortfolio);

    HashMap<String, PortfolioModel> exp = TestHelper.getInflexPortfolio();
    exp.put("P4", newPortfolio);
    Assert.assertEquals(exp, userModel.getInflexPortfolios());
  }

  @Test
  public void testPortfoliosExist() {
    userModel = new UserModelImpl(null, null, null);
    assertFalse(userModel.portfoliosExist());
    HashMap<String, PortfolioModel> portfolio = TestHelper.getPortfolioListMock();
    userModel = new UserModelImpl(portfolio, null, null);
    assertTrue(userModel.portfoliosExist());
  }

  @Test
  public void testValidatePortfolioName() {
    userModel = new UserModelImpl(null, null, null);
    assertFalse(userModel.validatePortfolioNameDuplicate("portfolio"));
    HashMap<String, PortfolioModel> portfolio = TestHelper.getPortfolioListMock();
    userModel = new UserModelImpl(portfolio, null, null);
    assertFalse(userModel.validatePortfolioNameDuplicate("portfolio"));
    assertTrue(userModel.validatePortfolioNameDuplicate("PORTFOLIO1"));
  }

  @Test
  public void testValidateDate() {
    HashMap<String, PortfolioModel> portfolio = TestHelper.getPortfolioListMock();
    userModel = new UserModelImpl(portfolio, null, null);
    LocalDate portfolioValueDate;
    try {
      userModel.validateDate("11/09/2022");
      fail("Did not throw an exception for invalid date input");
    } catch (DateTimeException e) {
      //pass
    }
    try {
      userModel.validateDate("2022-20-09");
      fail("Did not throw an exception for invalid date input");
    } catch (DateTimeException e) {
      //pass
    }
    try {
      userModel.validateDate("2045-11-05");
      fail("Did not throw an exception for future date");
    } catch (IllegalArgumentException e) {
      //pass
    }
    portfolioValueDate = userModel.validateDate("2022-11-01");
    DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDate expectedDate = LocalDate.parse("2022-11-01", format);
    Assert.assertEquals(expectedDate, portfolioValueDate);
  }

  @Test
  public void testCalculatePortfolioValue() throws Exception {
    DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDate date = LocalDate.parse("2022-11-01", format);

    HashMap<String, PortfolioModel> portfolio = TestHelper.getPortfolioListMock();
    userModel = new UserModelImpl(portfolio, null, null);
    Assert.assertEquals("5013.20", String.format("%.2f",
            userModel.calculatePortfolioValue(date, "PORTFOLIO1")));
    Assert.assertEquals("2170.90", String.format("%.2f",
            userModel.calculatePortfolioValue(date, "PORTFOLIO2")));
    date = LocalDate.parse("1997-10-01", format);
    try {
      userModel.calculatePortfolioValue(date, "PORTFOLIO2");
      fail("Did not throw an exception for stock data not available for given date");
    } catch (IOException e) {
      //pass
    }
    date = LocalDate.parse("2020-12-18", format);
    Assert.assertEquals("64259.88", String.format("%.2f",
            userModel.calculatePortfolioValue(date, "PORTFOLIO1")));
  }

  @Test
  public void testCalculateComposition() {
    HashMap<String, Float> stockCompositionMap = new HashMap<>();
    HashMap<String, StockModel> slist = TestHelper.getStockListMock();

    HashMap<String, PortfolioModel> portfolio = TestHelper.getPortfolioListMock();
    userModel = new UserModelImpl(portfolio, null, null);
    try {
      stockCompositionMap = userModel.calculateComposition("PORTFOLIO1", null);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    Assert.assertNotNull(stockCompositionMap);
    float composition = 0;
    for (Float comp : stockCompositionMap.values()) {
      composition = composition + comp;
    }
    Assert.assertEquals(100.00, composition, 0);
  }

  @Test
  public void testLoadUser() throws IOException {

    HashMap<String, PortfolioModel> expectedPortfolios = new HashMap<>();
    expectedPortfolios.put("PORTFOLIO1", new PortfolioModelInflexImpl("PORTFOLIO1",
            TestHelper.getStockListMock1()));
    expectedPortfolios.put("PORTFOLIO2", new PortfolioModelInflexImpl("PORTFOLIO2",
            TestHelper.getStockListMock2()));

    HashMap<String, StockModel> expectedStocks = expectedPortfolios.get("PORTFOLIO1").getStockMap();

    UserModelImpl userModelImpl = new UserModelImpl(null, null, null);
    userModelImpl.loadUser("res/testfiles/validFileUpload.csv");
    HashMap<String, PortfolioModel> actualPortfolios = userModelImpl.getInflexPortfolios();
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

  @Test
  public void testCostBasisInflex() {
    HashMap<String, PortfolioModel> portfolio = TestHelper.getPortfolioListMock();
    userModel = new UserModelImpl(portfolio, null, null);
    try {
      userModel.getCostBasis("PORTFOLIO1", null);
      fail("Did not throw an exception for calling cost basis for inflexible portfolio.");
    } catch (Exception e) {
      //pass
    }
  }

  @Test
  public void testCostBasisFlex() throws Exception {
    HashMap<String, PortfolioModel> portfolio = TestHelper.getPortfolioListMockFlex();
    DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDate date = LocalDate.parse("2020-06-10", format);
    LocalDate date1 = LocalDate.parse("2020-09-25", format);
    LocalDate date2 = LocalDate.parse("2021-06-16", format);
    LocalDate date3 = LocalDate.parse("2021-11-25", format);

    userModel = new UserModelImpl(portfolio, null, null);
    Assert.assertEquals("2.40",
            String.format("%.2f", userModel.getCostBasis("PORTFOLIOFLEX2", date2)));
    Assert.assertEquals("603.98",
            String.format("%.2f", userModel.getCostBasis("PORTFOLIOFLEX2", date3)));
  }

  @Test
  public void testCalculateCompositionFlex() {
    HashMap<String, Float> stockCompositionMap = new HashMap<>();
    HashMap<String, Float> stockCompositionMap1 = new HashMap<>();
    HashMap<String, StockModel> slist = TestHelper.getStockListMockFlex1();

    HashMap<String, PortfolioModel> portfolio = TestHelper.getPortfolioListMockFlex();
    userModel = new UserModelImpl(portfolio, null, null);

    DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDate date = LocalDate.parse("2022-10-12", format);
    LocalDate date1 = LocalDate.parse("2010-05-25", format);

    try {
      stockCompositionMap = userModel.calculateComposition("PORTFOLIOFLEX1", date);
      stockCompositionMap1 = userModel.calculateComposition("PORTFOLIOFLEX2", date1);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    Assert.assertNotNull(stockCompositionMap);
    Assert.assertNotNull(stockCompositionMap1);
    float composition = 0;
    for (Float comp : stockCompositionMap.values()) {
      composition = composition + comp;
    }
    Assert.assertEquals(100.00, composition, 0);

    float composition1 = 0;
    for (Float comp : stockCompositionMap1.values()) {
      composition1 = composition1 + comp;
    }
    Assert.assertEquals(100.00, composition1, 0);
  }

  @Test
  public void testCalculatePortfolioValueFlex() throws Exception {
    DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDate date = LocalDate.parse("2022-11-01", format);

    HashMap<String, PortfolioModel> portfolio = TestHelper.getPortfolioListMockFlex();
    userModel = new UserModelImpl(portfolio, null, null);
    Assert.assertEquals("1942.80", String.format("%.2f",
            userModel.calculatePortfolioValue(date, "PORTFOLIOFLEX1")));
    Assert.assertEquals("903.90", String.format("%.2f",
            userModel.calculatePortfolioValue(date, "PORTFOLIOFLEX2")));

    LocalDate datePast = LocalDate.parse("2013-10-01", format);
    Assert.assertEquals("0.00", String.format("%.2f",
            userModel.calculatePortfolioValue(datePast, "PORTFOLIOFLEX2")));

    date = LocalDate.parse("2020-12-18", format);
    Assert.assertEquals("23259.72", String.format("%.2f",
            userModel.calculatePortfolioValue(date, "PORTFOLIOFLEX1")));
  }

  @Test
  public void testLoadUserFlex() throws IOException {

    HashMap<String, PortfolioModel> expectedPortfolios = new HashMap<>();
    expectedPortfolios.put("PORTFOLIOFLEX1", new PortfolioModelInflexImpl("PORTFOLIOFLEX1",
            TestHelper.getStockListMock1()));
    expectedPortfolios.put("PORTFOLIOFLEX2", new PortfolioModelInflexImpl("PORTFOLIOFLEX2",
            TestHelper.getStockListMock2()));

    HashMap<String, StockModel> expectedStocks =
            expectedPortfolios.get("PORTFOLIOFLEX1").getStockMap();

    UserModelImpl userModelImpl = new UserModelImpl(null, null, null);
    userModelImpl.loadUser("res/testfiles/fileUploadFlex.csv");
    HashMap<String, PortfolioModel> actualPortfolios = userModelImpl.getFlexPortfolios();
    HashMap<String, StockModel> actualStocks =
            actualPortfolios.get("PORTFOLIOFLEX1").getStockMap();
    Assert.assertEquals(expectedPortfolios.size(), actualPortfolios.size());
    Assert.assertEquals(expectedStocks.size(), actualStocks.size());
    assertTrue(actualPortfolios.containsKey("PORTFOLIOFLEX1"));
    assertTrue(actualPortfolios.containsKey("PORTFOLIOFLEX2"));
  }

  @Test
  public void testUserSaveModelFlex() {
    UserModelImpl userModelImpl = new UserModelImpl(null, null, null);
    HashMap<String, StockModel> stockListMockFlex = TestHelper.getStockListMockFlex1();
    List<StockViewModelFlex> list = new ArrayList<>(stockListMockFlex.values());
    try {
      userModelImpl.saveFlexPortfolio("test", list, true);
      Assert.assertEquals(stockListMockFlex,userModelImpl.getFlexPortfolios());
    } catch (IOException e) {
      Assert.fail("Do not throw an exception.");
    }
  }
}
