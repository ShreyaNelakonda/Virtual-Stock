import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import controller.MainController;
import controller.MainControllerImpl;
import controller.UserController;
import controller.UserControllerImpl;
import model.DCAStrategyModel;
import model.PortfolioModel;
import model.UserModel;
import view.TextViewConsoleImpl;
import viewmodel.DCAStrategyViewModel;
import viewmodel.StockViewModel;
import viewmodel.StockViewModelFlex;

/**
 * A JUnit test class for the UserControllerImpl class.
 */
public class UserControllerImplTest {

  @Test
  public void testGetPortfolios() {
    UserController ctrl = new UserControllerImpl();
    StringBuilder mockLog = new StringBuilder();
    UserModel user = new MockUserModel(mockLog);
    OutputStream out = new ByteArrayOutputStream();
    String expected = StringTestHelper.GET_PORTFOLIO;

    ctrl.getPortfolios(user, new PrintStream(out));
    Assert.assertEquals("Result does not match.", expected, mockLog.toString());
  }

  @Test
  public void testLoadExternalProfile() {
    StringBuilder mockLog = new StringBuilder();
    UserModel userModel = new MockUserModel(mockLog);
    OutputStream out = new ByteArrayOutputStream();
    TextViewConsoleImpl view = new TextViewConsoleImpl();
    String filePath = "validFileUpload.csv";
    String expected = "filePath " + filePath;

    String inputString = "2 validFileUpload.csv 5 ";
    InputStream in = new ByteArrayInputStream(inputString.getBytes());
    MainController ctrl = new MainControllerImpl(userModel, view, in, new PrintStream(out));
    ctrl.init();

    Assert.assertEquals("Result does not match.", expected, mockLog.toString());
  }

  static class MockUserModel implements UserModel {
    private final StringBuilder log;

    MockUserModel(StringBuilder log) {
      this.log = log;
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
      log.append(StringTestHelper.ADD_PORTFOLIO);
    }

    @Override
    public boolean portfoliosExist() {
      return false;
    }

    @Override
    public boolean validatePortfolioNameDuplicate(String portfolioName) {
      return false;
    }

    @Override
    public LocalDate validateDate(String dateString) {
      return null;
    }

    @Override
    public float calculatePortfolioValue(LocalDate date, String portfolioName) throws Exception {
      return 0;
    }

    @Override
    public HashMap<String, Float> calculateComposition(String portfolioName,
                                                       LocalDate portfolioCompDate)
            throws Exception {
      return null;
    }

    @Override
    public void loadUser(String filePath) throws IOException {
      log.append("filePath " + filePath);
    }

    @Override
    public void createUser() throws Exception {
      //do nothing
    }

    @Override
    public boolean validatePortfolioNameFormat(String pName) {
      return false;
    }

    @Override
    public void savePortfolio(String pName, HashSet<StockViewModel> stockMap, boolean flexType)
            throws IOException {
      //do nothing
    }

    @Override
    public void addFlexPortfolio(PortfolioModel p) {
      //do nothing
    }

    @Override
    public int checkPortfolioType(String portfolioName) {
      return 0;
    }

    @Override
    public void saveFlexPortfolio(String pName, List<StockViewModelFlex> stockSet, boolean b)
            throws IOException {
      //do nothing
    }

    @Override
    public float getCostBasis(String portfolioName, LocalDate portfolioCostDate) {
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
