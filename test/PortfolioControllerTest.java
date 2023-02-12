import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import controller.MainController;
import controller.MainControllerImpl;
import model.DCAStrategyModel;
import model.PortfolioModel;
import model.UserModel;
import view.TextView;
import view.TextViewConsoleImpl;
import viewmodel.DCAStrategyViewModel;
import viewmodel.StockViewModel;
import viewmodel.StockViewModelFlex;

/**
 * A JUnit test class for the PortfolioControllerImpl class.
 */
public class PortfolioControllerTest {

  @Test
  public void testGetPortfolioValue() {
    StringBuilder mockLog = new StringBuilder();
    HashMap<String, PortfolioModel> portfolio = TestHelper.getPortfolioListMock();
    UserModel userModel = new MockUserModel(mockLog, portfolio);
    TextView view = new TextViewConsoleImpl();

    OutputStream out = new ByteArrayOutputStream();
    InputStream in = new ByteArrayInputStream(("3 4 PORTFOLIO1 "
            + "2022/10/09 2022-10-20 5").getBytes());

    MainController ctrl = new MainControllerImpl(userModel, view, in, new PrintStream(out));
    ctrl.init();

    String expected = "portfoliosExist() called validatePortfolioName() portfolioName is PORTFOLIO1"
            + " validateDate() dateString is 2022/10/09 validateDate() dateString is 2022-10-20"
            + " calculatePortfolioValue() date is 2022-10-20 calculatePortfolioValue() "
            + "portfolioName is PORTFOLIO1";

    Assert.assertEquals(expected, mockLog.toString());
  }

  @Test
  public void testCheckPortfolioComposition() {
    StringBuilder mockLog = new StringBuilder();
    HashMap<String, PortfolioModel> portfolio = TestHelper.getPortfolioListMock();
    UserModel userModel = new MockUserModel(mockLog, portfolio);
    TextView view = new TextViewConsoleImpl();

    OutputStream out = new ByteArrayOutputStream();
    InputStream in = new ByteArrayInputStream(("3 3 PORTFOLIO1 5").getBytes());

    MainController ctrl = new MainControllerImpl(userModel, view, in, new PrintStream(out));
    ctrl.init();

    String expected = " portfoliosExist() called validatePortfolioName() portfolioName is"
            + " PORTFOLIO1 calculateComposition() portfolioName is PORTFOLIO1";

    Assert.assertEquals(expected, mockLog.toString());
  }

  static class MockUserModel implements UserModel {
    private final HashMap<String, PortfolioModel> portfolios;
    private final StringBuilder log;

    MockUserModel(StringBuilder log, HashMap<String, PortfolioModel> portfolios) {
      this.log = log;
      if (portfolios != null) {
        this.portfolios = portfolios;
      } else {
        this.portfolios = new HashMap<>();
      }
    }

    @Override
    public List<DCAStrategyModel> getDcaStrategyList() {
      return null;
    }

    @Override
    public String getUserName() {
      return null;
    }

    @Override
    public DCAStrategyModel addDCAStrategyToList(DCAStrategyViewModel dcaPortfolio) {
      return null;
    }

    @Override
    public void addPortfolio(PortfolioModel p) {
      if (p != null) {
        this.portfolios.put(p.getPortfolioName(), p);
      }
      log.append(StringTestHelper.ADD_PORTFOLIO);
    }

    @Override
    public boolean portfoliosExist() {
      log.append(" portfoliosExist() called");
      return this.portfolios.size() != 0;
    }

    @Override
    public boolean validatePortfolioNameDuplicate(String portfolioName) {
      log.append(" validatePortfolioName() portfolioName is " + portfolioName);
      return this.portfolios.containsKey(portfolioName);
    }

    @Override
    public LocalDate validateDate(String dateString) {
      log.append(" validateDate() dateString is " + dateString);
      LocalDate portfolioValueDate;
      try {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        portfolioValueDate = LocalDate.parse(dateString, format);
        if (portfolioValueDate.isAfter(LocalDate.parse("2022-11-03"))) {
          throw new IllegalArgumentException("Portfolio Values are available only till 2022-11-03."
                  + "Please try again.");
        }
      } catch (DateTimeException e) {
        throw new DateTimeException("Invalid date format.");
      } catch (IllegalArgumentException e) {
        throw new IllegalArgumentException(e);
      }
      return portfolioValueDate;
    }

    @Override
    public float calculatePortfolioValue(LocalDate date, String portfolioName) {
      log.append(" calculatePortfolioValue() date is " + date);
      log.append(" calculatePortfolioValue() portfolioName is " + portfolioName);
      return 0;
    }

    @Override
    public HashMap<String, Float> calculateComposition(String portfolioName,
                                                       LocalDate portfolioCompDate) {
      log.append(" calculateComposition() portfolioName is " + portfolioName);
      return null;
    }

    @Override
    public void loadUser(String filePath) {
      log.append(" loadUser() called with" + filePath);
    }

    @Override
    public void createUser() throws Exception {
      log.append(" createUser() called");
    }

    @Override
    public boolean validatePortfolioNameFormat(String pName) {
      return true;
    }

    @Override
    public void savePortfolio(String pName, HashSet<StockViewModel> stockMap, boolean flexType)
            throws IOException {
      log.append(" savePortfolio() called with" + pName + " and " + flexType);
    }

    @Override
    public void addFlexPortfolio(PortfolioModel p) {
      //do nothing
    }

    @Override
    public int checkPortfolioType(String portfolioName) {
      return 1;
    }

    @Override
    public void saveFlexPortfolio(String pName, List<StockViewModelFlex> stockSet, boolean b)
            throws IOException {
      //do nothing
    }

    @Override
    public float getCostBasis(String portfolioName, LocalDate portfolioCostDate) {
      log.append(" getCostBasis() date is " + portfolioCostDate);
      log.append(" getCostBasis() portfolioName is " + portfolioName);
      return 0;
    }

    @Override
    public void saveFlexStock(String pName, StockViewModelFlex currStock) {
      //do nothing
    }

    @Override
    public HashMap<String, PortfolioModel> getFlexPortfolios() {
      log.append(StringTestHelper.GET_PORTFOLIO);
      return new HashMap<>();
    }

    @Override
    public HashMap<String, PortfolioModel> getInflexPortfolios() {
      return null;
    }
  }

}