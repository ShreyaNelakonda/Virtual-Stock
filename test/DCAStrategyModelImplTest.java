import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import model.DCAStockRatio;
import model.DCAStockRatioImpl;
import model.DCAStrategyModel;
import model.DCAStrategyModelImpl;
import model.PortfolioModel;
import model.UserModel;
import model.UserModelImpl;

/**
 * Test class for DCAStrategyModelImpl class.
 */
public class DCAStrategyModelImplTest {

  DCAStockRatio dcaStockRatio1;
  DCAStockRatio dcaStockRatio2;
  DCAStockRatio dcaStockRatio3;
  List<DCAStockRatio> dcaStockRatioList;
  HashMap<String, PortfolioModel> portfolio;
  UserModel userModel;

  @Before
  public void setUp() {
    dcaStockRatio1 = new DCAStockRatioImpl("GOOG", 20.0);
    dcaStockRatio2 = new DCAStockRatioImpl("AAPL", 75);
    dcaStockRatio3 = new DCAStockRatioImpl("META", 5);
    dcaStockRatioList = new ArrayList<>();
    dcaStockRatioList.add(dcaStockRatio1);
    dcaStockRatioList.add(dcaStockRatio2);
    dcaStockRatioList.add(dcaStockRatio3);

    portfolio = TestHelper.getPortfolioListMockFlex();
    userModel = new UserModelImpl(portfolio, null, null);
  }

  @Test
  public void testDCAStockRatio() {
    DCAStockRatio dcaStockRatio = new DCAStockRatioImpl("GOOG", 20.0);
    Assert.assertEquals("GOOG", dcaStockRatio.getTicker());
    Assert.assertEquals(20.0, dcaStockRatio.getProportion(), 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDCAStockRatioNegativeProportion() {
    DCAStockRatio dcaStockRatio = new DCAStockRatioImpl("GOOG", -10);
    Assert.assertEquals("GOOG", dcaStockRatio.getTicker());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDCAStockRatioProportion() {
    DCAStockRatio dcaStockRatio = new DCAStockRatioImpl("GOOG", 110);
    Assert.assertEquals("GOOG", dcaStockRatio.getTicker());
  }

  @Test
  public void testDCAStrategyModel() {

    String startDate = "2020-01-08";
    String endDate = "2022-12-31";
    DCAStrategyModel dcaStrategy = new DCAStrategyModelImpl("TEST",
            dcaStockRatioList, startDate, endDate, 2000,
            60, 5);
    Assert.assertEquals("TEST", dcaStrategy.getPortfolioName());
    Assert.assertEquals(60, dcaStrategy.getFrequencyDays());
    Assert.assertEquals(5, dcaStrategy.getCommissionFee(), 0);
    Assert.assertEquals(2000, dcaStrategy.getInvestedAmount(), 0);
    Assert.assertEquals(LocalDate.of(2020, 1, 8),
            dcaStrategy.getStartDate());
    Assert.assertEquals(LocalDate.of(2022, 12, 31),
            dcaStrategy.getEndDate());
  }

  @Test
  public void testInvestAmountInExistingPortfolio() {
    String startDate = "2020-01-08";
    String endDate = "";
    DCAStrategyModel dcaStrategy = new DCAStrategyModelImpl("TEST",
            dcaStockRatioList, startDate, endDate, 2000,
            0, 5);
    Assert.assertEquals("TEST", dcaStrategy.getPortfolioName());
    Assert.assertEquals(60, dcaStrategy.getFrequencyDays());
    Assert.assertEquals(5, dcaStrategy.getCommissionFee(), 0);
    Assert.assertEquals(2000, dcaStrategy.getInvestedAmount(), 0);
    Assert.assertEquals(LocalDate.of(2020, 1, 8),
            dcaStrategy.getStartDate());
    Assert.assertEquals(LocalDate.of(2022, 12, 31),
            dcaStrategy.getEndDate());
  }

  @Test
  public void testDCAStrategyModelEndDateNull() {
    String startDate = "2020-01-08";
    String endDate = "--";
    DCAStrategyModel dcaStrategy = new DCAStrategyModelImpl("TEST",
            dcaStockRatioList, startDate, endDate, 2000,
            60, 5);
    Assert.assertEquals("TEST", dcaStrategy.getPortfolioName());
    Assert.assertEquals(60, dcaStrategy.getFrequencyDays());
    Assert.assertEquals(5, dcaStrategy.getCommissionFee(), 0);
    Assert.assertEquals(2000, dcaStrategy.getInvestedAmount(), 0);
    Assert.assertEquals(LocalDate.of(2020, 1, 8),
            dcaStrategy.getStartDate());
    Assert.assertEquals(LocalDate.now(),
            dcaStrategy.getEndDate());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDCAStrategyModelEndDateBeforeStart() {
    String startDate = "2022-01-08";
    String endDate = "2020-01-08";
    DCAStrategyModel dcaStrategy = new DCAStrategyModelImpl("TEST",
            dcaStockRatioList, startDate, endDate, 2000,
            60, 5);
    Assert.assertEquals(LocalDate.now(), dcaStrategy.getEndDate());
  }

  @Test(expected = DateTimeException.class)
  public void testDCAStrategyModelStartDateInvalid() {
    String[] startDate = {"abcd-01-08", "2022.02.11", "wir93irhg4", "", null};
    String endDate = "2020-01-08";
    for (String sDate : startDate) {
      DCAStrategyModel dcaStrategy = new DCAStrategyModelImpl("TEST",
              dcaStockRatioList, sDate, endDate, 2000,
              60, 5);
      Assert.assertEquals(LocalDate.now(), dcaStrategy.getEndDate());
    }
  }

  @Test(expected = DateTimeException.class)
  public void testDCAStrategyModelEndDateInvalid() {
    String[] endDate = {"abcd-01-08", "2022.02.11", "wir93irhg4", "", null};
    String startDate = "2020-01-08";
    for (String eDate : endDate) {
      DCAStrategyModel dcaStrategy = new DCAStrategyModelImpl("TEST",
              dcaStockRatioList, startDate, eDate, 2000,
              60, 5);
      Assert.assertEquals(LocalDate.now(), dcaStrategy.getEndDate());
    }
  }

  @Test
  public void testDCAStrategyEndDateFuture() {
    String startDate = "2022-10-01";
    String endDate = "2022-12-31";
    LocalDate date = LocalDate.of(2022, 10, 31);
    DCAStrategyModel dcaStrategy = new DCAStrategyModelImpl("TEST",
            dcaStockRatioList, startDate, endDate, 2000,
            60, 5);
    userModel.addDCAStrategyToList(dcaStrategy);
    dcaStrategy.computeDollarCostAveraging(userModel);
    HashMap<String, Float> stockComp = new HashMap<>();
    stockComp.put("GOOG 4.018127", (float) 26.363138);
    stockComp.put("META 0.719645", (float) 4.721628);
    stockComp.put("AAPL 10.503686", (float) 68.91522);

    try {
      Assert.assertEquals(stockComp,
              userModel.calculateComposition("TEST", date));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void testDCAStrategy() {
    String startDate = "2020-10-01";
    String endDate = "2022-10-31";
    LocalDate date = LocalDate.of(2022, 10, 31);
    DCAStrategyModel dcaStrategy = new DCAStrategyModelImpl("TEST",
            dcaStockRatioList, startDate, endDate, 2000,
            90, 5);
    userModel.addDCAStrategyToList(dcaStrategy);
    dcaStrategy.computeDollarCostAveraging(userModel);
    HashMap<String, Float> stockComp = new HashMap<>();
    stockComp.put("GOOG 5.4319015", (float) 5.200983);
    stockComp.put("META 3.718018", (float) 3.5599597);
    stockComp.put("AAPL 95.28997", (float) 91.23905);

    try {
      Assert.assertEquals(stockComp,
              userModel.calculateComposition("TEST", date));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void testDCAStrategyPortfolio() {
    String startDate = "2020-10-01";
    String endDate = "2022-12-31";
    LocalDate date = LocalDate.of(2022, 10, 31);
    DCAStrategyModel dcaStrategy = new DCAStrategyModelImpl("PORTFOLIOFLEX1",
            dcaStockRatioList, startDate, endDate, 2000,
            90, 5);
    userModel.addDCAStrategyToList(dcaStrategy);
    dcaStrategy.computeDollarCostAveraging(userModel);
    HashMap<String, Float> stockComp = new HashMap<>();
    stockComp.put("GOOG 5.4319015", (float) 5.200983);
    stockComp.put("META 3.718018", (float) 3.5599597);
    stockComp.put("AAPL 95.28997", (float) 91.23905);

    try {
      Assert.assertEquals(stockComp,
              userModel.calculateComposition("PORTFOLIOFLEX1", date));
    } catch (Exception e) {
      Assert.fail("Exception not expected.");
    }
  }

  @Test
  public void testZeroCommissionFee() {
    String startDate = "2020-10-01";
    String endDate = "2022-12-31";
    LocalDate date = LocalDate.of(2022, 10, 31);
    DCAStrategyModel dcaStrategy = new DCAStrategyModelImpl("PORTFOLIOFLEX1",
            dcaStockRatioList, startDate, endDate, 2000,
            90, 0);
    userModel.addDCAStrategyToList(dcaStrategy);
    userModel.addDCAStrategyToList(dcaStrategy);
    dcaStrategy.computeDollarCostAveraging(userModel);
    HashMap<String, Float> stockComp = new HashMap<>();
    stockComp.put("GOOG 5.4319015", (float) 5.200983);
    stockComp.put("META 3.718018", (float) 3.5599597);
    stockComp.put("AAPL 95.28997", (float) 91.23905);

    try {
      Assert.assertEquals(stockComp,
              userModel.calculateComposition("PORTFOLIOFLEX1", date));
    } catch (Exception e) {
      Assert.fail("Exception not expected.");
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testProportionMoreThan100() {
    DCAStockRatio dcaStockRatio1 = new DCAStockRatioImpl("GOOG", 30);
    DCAStockRatio dcaStockRatio2 = new DCAStockRatioImpl("AAPL", 75);
    List<DCAStockRatio> dcaStockRatioList = new ArrayList<>();
    dcaStockRatioList.add(dcaStockRatio1);
    dcaStockRatioList.add(dcaStockRatio2);

    String startDate = "2020-10-01";
    String endDate = "2022-12-31";
    DCAStrategyModel dcaStrategy = new DCAStrategyModelImpl("PORTFOLIOFLEX1",
            dcaStockRatioList, startDate, endDate, 2000,
            90, 5);
    userModel.addDCAStrategyToList(dcaStrategy);
    dcaStrategy.computeDollarCostAveraging(userModel);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNegativeAmount() {
    DCAStockRatio dcaStockRatio1 = new DCAStockRatioImpl("GOOG", 30);
    DCAStockRatio dcaStockRatio2 = new DCAStockRatioImpl("AAPL", 55);
    List<DCAStockRatio> dcaStockRatioList = new ArrayList<>();
    dcaStockRatioList.add(dcaStockRatio1);
    dcaStockRatioList.add(dcaStockRatio2);

    String startDate = "2020-10-01";
    String endDate = "2022-12-31";
    DCAStrategyModel dcaStrategy = new DCAStrategyModelImpl("PORTFOLIOFLEX1",
            dcaStockRatioList, startDate, endDate, -2000,
            90, 5);
    userModel.addDCAStrategyToList(dcaStrategy);
    dcaStrategy.computeDollarCostAveraging(userModel);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNegativeCommissionFee() {
    DCAStockRatio dcaStockRatio1 = new DCAStockRatioImpl("GOOG", 30);
    DCAStockRatio dcaStockRatio2 = new DCAStockRatioImpl("AAPL", 55);
    List<DCAStockRatio> dcaStockRatioList = new ArrayList<>();
    dcaStockRatioList.add(dcaStockRatio1);
    dcaStockRatioList.add(dcaStockRatio2);

    String startDate = "2020-10-01";
    String endDate = "2022-12-31";
    DCAStrategyModel dcaStrategy = new DCAStrategyModelImpl("PORTFOLIOFLEX1",
            dcaStockRatioList, startDate, endDate, 2000,
            90, -5);
    userModel.addDCAStrategyToList(dcaStrategy);
    dcaStrategy.computeDollarCostAveraging(userModel);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNegativeFrequency() {
    DCAStockRatio dcaStockRatio1 = new DCAStockRatioImpl("GOOG", 30);
    DCAStockRatio dcaStockRatio2 = new DCAStockRatioImpl("AAPL", 55);
    List<DCAStockRatio> dcaStockRatioList = new ArrayList<>();
    dcaStockRatioList.add(dcaStockRatio1);
    dcaStockRatioList.add(dcaStockRatio2);

    String startDate = "2020-10-01";
    String endDate = "2022-12-31";
    DCAStrategyModel dcaStrategy = new DCAStrategyModelImpl("PORTFOLIOFLEX1",
            dcaStockRatioList, startDate, endDate, 2000,
            -90, 5);
    userModel.addDCAStrategyToList(dcaStrategy);
    dcaStrategy.computeDollarCostAveraging(userModel);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testProportionLessThan100() {
    DCAStockRatio dcaStockRatio1 = new DCAStockRatioImpl("GOOG", 30);
    DCAStockRatio dcaStockRatio2 = new DCAStockRatioImpl("AAPL", 55);
    List<DCAStockRatio> dcaStockRatioList = new ArrayList<>();
    dcaStockRatioList.add(dcaStockRatio1);
    dcaStockRatioList.add(dcaStockRatio2);

    String startDate = "2020-10-01";
    String endDate = "2022-12-31";
    DCAStrategyModel dcaStrategy = new DCAStrategyModelImpl("PORTFOLIOFLEX1",
            dcaStockRatioList, startDate, endDate, 2000,
            90, 5);
    userModel.addDCAStrategyToList(dcaStrategy);
    dcaStrategy.computeDollarCostAveraging(userModel);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidTicker() {
    DCAStockRatio dcaStockRatio1 = new DCAStockRatioImpl("ASEW", 30);
    DCAStockRatio dcaStockRatio2 = new DCAStockRatioImpl("", 70);
    List<DCAStockRatio> dcaStockRatioList = new ArrayList<>();
    dcaStockRatioList.add(dcaStockRatio1);
    dcaStockRatioList.add(dcaStockRatio2);

    String startDate = "2020-10-01";
    String endDate = "2022-12-31";
    DCAStrategyModel dcaStrategy = new DCAStrategyModelImpl("PORTFOLIOFLEX1",
            dcaStockRatioList, startDate, endDate, 2000,
            90, 5);
    userModel.addDCAStrategyToList(dcaStrategy);
    dcaStrategy.computeDollarCostAveraging(userModel);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullTicker() {
    DCAStockRatio dcaStockRatio1 = new DCAStockRatioImpl(null, 30);
    DCAStockRatio dcaStockRatio2 = new DCAStockRatioImpl(null, 70);
    List<DCAStockRatio> dcaStockRatioList = new ArrayList<>();
    dcaStockRatioList.add(dcaStockRatio1);
    dcaStockRatioList.add(dcaStockRatio2);

    String startDate = "2020-10-01";
    String endDate = "2022-12-31";
    DCAStrategyModel dcaStrategy = new DCAStrategyModelImpl("PORTFOLIOFLEX1",
            dcaStockRatioList, startDate, endDate, 2000,
            90, 5);
    userModel.addDCAStrategyToList(dcaStrategy);
    dcaStrategy.computeDollarCostAveraging(userModel);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPortfolioNameEmpty() {
    DCAStockRatio dcaStockRatio1 = new DCAStockRatioImpl("GOOG", 30);
    DCAStockRatio dcaStockRatio2 = new DCAStockRatioImpl("AAPL", 55);
    List<DCAStockRatio> dcaStockRatioList = new ArrayList<>();
    dcaStockRatioList.add(dcaStockRatio1);
    dcaStockRatioList.add(dcaStockRatio2);

    String startDate = "2020-10-01";
    String endDate = "2022-12-31";
    DCAStrategyModel dcaStrategy = new DCAStrategyModelImpl("",
            dcaStockRatioList, startDate, endDate, 2000,
            90, 5);
    userModel.addDCAStrategyToList(dcaStrategy);
    dcaStrategy.computeDollarCostAveraging(userModel);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPortfolioNameNull() {
    DCAStockRatio dcaStockRatio1 = new DCAStockRatioImpl("GOOG", 30);
    DCAStockRatio dcaStockRatio2 = new DCAStockRatioImpl("AAPL", 55);
    List<DCAStockRatio> dcaStockRatioList = new ArrayList<>();
    dcaStockRatioList.add(dcaStockRatio1);
    dcaStockRatioList.add(dcaStockRatio2);

    String startDate = "2020-10-01";
    String endDate = "2022-12-31";
    DCAStrategyModel dcaStrategy = new DCAStrategyModelImpl(null,
            dcaStockRatioList, startDate, endDate, 2000,
            90, 5);
    userModel.addDCAStrategyToList(dcaStrategy);
    dcaStrategy.computeDollarCostAveraging(userModel);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPortfolioNameInvalidChar() {
    DCAStockRatio dcaStockRatio1 = new DCAStockRatioImpl("GOOG", 30);
    DCAStockRatio dcaStockRatio2 = new DCAStockRatioImpl("AAPL", 55);
    List<DCAStockRatio> dcaStockRatioList = new ArrayList<>();
    dcaStockRatioList.add(dcaStockRatio1);
    dcaStockRatioList.add(dcaStockRatio2);

    String startDate = "2020-10-01";
    String endDate = "2022-12-31";
    DCAStrategyModel dcaStrategy = new DCAStrategyModelImpl("....}}{}{",
            dcaStockRatioList, startDate, endDate, 2000,
            90, 5);
    userModel.addDCAStrategyToList(dcaStrategy);
    dcaStrategy.computeDollarCostAveraging(userModel);
  }
}