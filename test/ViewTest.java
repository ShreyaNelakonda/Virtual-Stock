import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.HashMap;

import controller.MainController;
import controller.MainControllerImpl;
import model.PortfolioModel;
import model.UserModel;
import model.UserModelImpl;
import view.TextView;
import view.TextViewConsoleImpl;

/**
 * A JUnit test class for the View class.
 */
public class ViewTest {

  private final ByteArrayOutputStream out = new ByteArrayOutputStream();
  HashMap<String, PortfolioModel> plist;
  UserModel model;
  TextView view;

  @Before
  public void setUp() {
    System.setOut(new PrintStream(out));
    plist = TestHelper.getPortfolioListMock();
    model = new UserModelImpl(plist, null, null);
    view = new TextViewConsoleImpl();
  }

  @Test
  public void testCreateNewProfileValid() {
    UserModel model = null;
    String expected = StringTestHelper.INIT_MENU + StringTestHelper.USER_CREATED
            + StringTestHelper.MAIN_MENU + StringTestHelper.EXIT;
    String inputString = "3 9 ";
    InputStream in = new ByteArrayInputStream(inputString.getBytes());
    MainController ctrl = new MainControllerImpl(model, view, in, new PrintStream(out));
    ctrl.init();
    Assert.assertEquals(expected, out.toString().trim());
  }

  @Test
  public void createFlexPortfolioTest() {
    String inputString = "3 1 1 AAPL AAPL 2022-10-10 1 50 2 1 jhk 3 AAPL 2022-11-10 exit";
    InputStream in = new ByteArrayInputStream(inputString.getBytes());
    MainController ctrl = new MainControllerImpl(model, view, in, new PrintStream(out));
    ctrl.init();
    String expected = StringTestHelper.INIT_MENU + StringTestHelper.USER_CREATED
            + StringTestHelper.MAIN_MENU + StringTestHelper.PTYPE_SELECT
            + StringTestHelper.PORTFOLIO_NAME + StringTestHelper.ENTER_TICKER_SYM
            + StringTestHelper.TXN_DATE + "The price [2022-10-10] of AAPL is 140.42 USD.\n"
            + StringTestHelper.TXN_FEE + "Stock | Date | Price | Fee | Qty | Type \n"
            + "AAPL | 2022-10-10 | 140.42 | 2.0 | 50.0 | Buy\n" +  StringTestHelper.ADD_TXN_MORE
            + StringTestHelper.MAIN_MENU + StringTestHelper.PORTFOLIO_NAME
            + StringTestHelper.DATE_COMPOSITION + "******* Flexible Portfolio *******\n"
            + "#  Stock | Qty | Composition\n" + "1  AAPL | 50.0 | 100.00%\n"
            + StringTestHelper.MAIN_MENU + StringTestHelper.EXIT;
    Assert.assertEquals(expected, out.toString().trim());
  }

  @Test
  public void testLoadPreviousProfileErrorLoadExternalProfileError() {
    UserModel model = null;
    String expected = StringTestHelper.INIT_MENU + StringTestHelper.FILE_LOAD_ERROR
            + StringTestHelper.INIT_MENU + StringTestHelper.EXTERNAL_FILE_PATH
            + StringTestHelper.FILE_LOAD_ERROR + StringTestHelper.INIT_MENU
            + StringTestHelper.USER_CREATED + StringTestHelper.MAIN_MENU + StringTestHelper.EXIT;
    String inputString = "1 2 wrongFile.csv 3 i ";
    InputStream in = new ByteArrayInputStream(inputString.getBytes());
    MainController ctrl = new MainControllerImpl(model, view, in, new PrintStream(out));
    ctrl.init();
    Assert.assertEquals(expected, out.toString().trim());
  }

  @Test
  public void testMainMenuCreatePortfolio() {
    UserModel model = new UserModelImpl(null, null, null);
    String expected = StringTestHelper.INIT_MENU + StringTestHelper.USER_CREATED
            + StringTestHelper.MAIN_MENU + StringTestHelper.PORTFOLIO_NAME
            + StringTestHelper.ENTER_TICKER_SYM + StringTestHelper.INVALID_TICKER
            + StringTestHelper.ENTER_TICKER_SYM + "Current price of AAPL is 138.88\n"
            + StringTestHelper.ENTER_QTY + StringTestHelper.QTY_ERROR + StringTestHelper.ENTER_QTY
            + StringTestHelper.ADD_MORE_STOCK + StringTestHelper.ENTER_TICKER_SYM
            + "Current price of AAPL is 138.88\n" + StringTestHelper.ENTER_QTY
            + StringTestHelper.ADD_MORE_STOCK + "AAPL | 138.88 | 12\n"
            + StringTestHelper.SAVE_PORTFOLIO + StringTestHelper.SAVE_PORTFOLIO_SUCCESS
            + StringTestHelper.MAIN_MENU + StringTestHelper.EXIT;
    String inputString = "3 1 test2 !21 aapl 0.8 7 1 aapl 5 o 1 k";
    InputStream in = new ByteArrayInputStream(inputString.getBytes());
    MainController ctrl = new MainControllerImpl(model, view, in, new PrintStream(out));
    ctrl.init();
    Assert.assertEquals(expected, out.toString().trim());
  }

  @Test
  public void testMainMenuViewPortfolioListCompositionDateWhenEmpty() {
    UserModel model = new UserModelImpl(null, null, null);
    String expected = StringTestHelper.INIT_MENU + StringTestHelper.USER_CREATED
            + StringTestHelper.MAIN_MENU + StringTestHelper.NO_PORTFOLIO
            + StringTestHelper.MAIN_MENU + StringTestHelper.NO_PORTFOLIO
            + StringTestHelper.MAIN_MENU + StringTestHelper.NO_PORTFOLIO
            + StringTestHelper.MAIN_MENU + StringTestHelper.EXIT;
    String inputString = "3 2 3 4 p";
    InputStream in = new ByteArrayInputStream(inputString.getBytes());
    MainController ctrl = new MainControllerImpl(model, view, in, new PrintStream(out));
    ctrl.init();
    Assert.assertEquals(expected, out.toString().trim());
  }

  @Test
  public void testLoadExternalFileContent() {
    String expected = StringTestHelper.INIT_MENU + StringTestHelper.EXTERNAL_FILE_PATH
            + StringTestHelper.FILE_LOAD_ERROR + StringTestHelper.INIT_MENU
            + StringTestHelper.EXTERNAL_FILE_PATH + StringTestHelper.FILE_UPLOAD_SUCCESS
            + StringTestHelper.MAIN_MENU + "# \t Name\n" + "1 \t P1\n" + "2 \t P2\n"
            + StringTestHelper.MAIN_MENU + StringTestHelper.PORTFOLIO_NAME
            + "There is no portfolio present with the name: XYZ\n" + StringTestHelper.MAIN_MENU
            + StringTestHelper.PORTFOLIO_NAME
            + "#  Stock | Qty | Composition\n1  META | 9 | 13.04%\n"
            + "2  GOOG | 35 | 50.72%\n3  NKLA | 25 | 36.23%\n"
            + StringTestHelper.MAIN_MENU + StringTestHelper.PORTFOLIO_NAME
            + StringTestHelper.P_DATE + "The value of Portfolio on 2022-10-01 is: 4674.37\n"
            + StringTestHelper.MAIN_MENU
            + StringTestHelper.EXIT;
    String inputString = "2 tes 2 res/testfiles/external-profile.csv "
            + "2 3 xyz 3 p1 4 p1 2022-10-01 lk ";
    InputStream in = new ByteArrayInputStream(inputString.getBytes());
    MainController ctrl = new MainControllerImpl(model, view, in, new PrintStream(out));
    ctrl.init();
    Assert.assertEquals(expected, out.toString().trim());
  }

  @Test
  public void testLoadPrevFileReadContent() {
    String expected = StringTestHelper.INIT_MENU + StringTestHelper.PROFILE_LOAD_SUCCESS
            + StringTestHelper.MAIN_MENU + StringTestHelper.PORTFOLIO_NAME
            + StringTestHelper.P_NAME_EXISTS + StringTestHelper.PORTFOLIO_NAME
            + StringTestHelper.P_NAME_FORMAT + StringTestHelper.PORTFOLIO_NAME
            + StringTestHelper.ENTER_TICKER_SYM + StringTestHelper.INVALID_TICKER
            + StringTestHelper.ENTER_TICKER_SYM + "Current price of AAPL is 138.88\n"
            + StringTestHelper.ENTER_QTY + StringTestHelper.QTY_ERROR + StringTestHelper.ENTER_QTY
            + StringTestHelper.ADD_MORE_STOCK + StringTestHelper.ENTER_TICKER_SYM
            + "Current price of AAPL is 138.88\n" + StringTestHelper.ENTER_QTY
            + StringTestHelper.ADD_MORE_STOCK + "AAPL | 138.88 | 12\n"
            + StringTestHelper.SAVE_PORTFOLIO + StringTestHelper.SAVE_PORTFOLIO_SUCCESS
            + StringTestHelper.MAIN_MENU + StringTestHelper.EXIT;
    String inputString = "1 1 PORTFOLIO1 !#ap12 newName !21 aapl 0.8 7 1 aapl 5 o 1 pk ";
    InputStream in = new ByteArrayInputStream(inputString.getBytes());
    MainController ctrl = new MainControllerImpl(model, view, in, new PrintStream(out));
    ctrl.init();
    Assert.assertEquals(expected, out.toString().trim());
  }

  @Test
  public void testInvalidInput() {
    UserModel model = null;
    String expected = StringTestHelper.INIT_MENU + StringTestHelper.INCORRECT_INPUT
            + StringTestHelper.INIT_MENU + StringTestHelper.USER_CREATED
            + StringTestHelper.MAIN_MENU + StringTestHelper.INCORRECT_INPUT
            + StringTestHelper.MAIN_MENU + StringTestHelper.INCORRECT_INPUT
            + StringTestHelper.MAIN_MENU + StringTestHelper.EXIT;
    String inputString = "as 3 sfv 9 l ";
    InputStream in = in = new ByteArrayInputStream(inputString.getBytes());
    MainController ctrl = new MainControllerImpl(model, view, in, new PrintStream(out));
    ctrl.init();
    Assert.assertEquals(expected, out.toString().trim());
  }

  @Test
  public void testEmptyProfileLoad() {
    String inputString = "1 3 lk ";
    InputStream in = new ByteArrayInputStream(inputString.getBytes());

    String expected = StringTestHelper.INIT_MENU + StringTestHelper.FILE_LOAD_ERROR
            + StringTestHelper.INIT_MENU + StringTestHelper.USER_CREATED
            + StringTestHelper.MAIN_MENU + StringTestHelper.EXIT;

    MainController ctrl = new MainControllerImpl(model, view, in, new PrintStream(out));
    ctrl.init();
    Assert.assertEquals(expected, out.toString().trim());
  }

  @Test
  public void testFileUploadInvalidPath() {
    String inputString = "2 invalid.csv 3 l ";
    InputStream in = new ByteArrayInputStream(inputString.getBytes());

    String expected = StringTestHelper.INIT_MENU + StringTestHelper.EXTERNAL_FILE_PATH
            + StringTestHelper.FILE_LOAD_ERROR + StringTestHelper.INIT_MENU
            + StringTestHelper.USER_CREATED + StringTestHelper.MAIN_MENU + StringTestHelper.EXIT;

    MainController ctrl = new MainControllerImpl(model, view, in, new PrintStream(out));
    ctrl.init();
    Assert.assertEquals(expected, out.toString().trim());
  }

  @Test
  public void testFileUploadInvalid() {
    String inputString = "2 invalid090 3 p ";
    InputStream in = new ByteArrayInputStream(inputString.getBytes());

    String expected = StringTestHelper.INIT_MENU + StringTestHelper.EXTERNAL_FILE_PATH
            + StringTestHelper.FILE_LOAD_ERROR + StringTestHelper.INIT_MENU
            + StringTestHelper.USER_CREATED + StringTestHelper.MAIN_MENU + StringTestHelper.EXIT;

    MainController ctrl = new MainControllerImpl(model, view, in, new PrintStream(out));
    ctrl.init();
    Assert.assertEquals(expected, out.toString().trim());
  }

  @Test
  public void testGetPortfolioNone() {
    String inputString = "3 2 s ";
    InputStream in = new ByteArrayInputStream(inputString.getBytes());

    String expected = StringTestHelper.INIT_MENU + StringTestHelper.USER_CREATED
            + StringTestHelper.MAIN_MENU + StringTestHelper.NO_PORTFOLIO_FOUND
            + StringTestHelper.MAIN_MENU + StringTestHelper.EXIT;

    MainController ctrl = new MainControllerImpl(model, view, in, new PrintStream(out));
    ctrl.init();
    Assert.assertEquals(expected, out.toString().trim());
  }

  @Test
  public void testGetPortfolio() {
    String inputString = "3 2 c ";
    InputStream in = new ByteArrayInputStream(inputString.getBytes());

    String getPortfolio = "# \t Name\n1 \t Portfolio1\n2 \t Portfolio2\n3 \t Portfolio3\n";

    String expected = StringTestHelper.INIT_MENU + StringTestHelper.USER_CREATED
            + StringTestHelper.MAIN_MENU + getPortfolio
            + StringTestHelper.MAIN_MENU + StringTestHelper.EXIT;

    MainController ctrl = new MainControllerImpl(model, view, in, new PrintStream(out));
    ctrl.init();
    Assert.assertEquals(expected, out.toString().trim());
  }

  @Test
  public void testPortfolioCompositionNone() {
    String inputString = "3 3 9 ";
    InputStream in = new ByteArrayInputStream(inputString.getBytes());

    String expected = StringTestHelper.INIT_MENU + StringTestHelper.USER_CREATED
            + StringTestHelper.MAIN_MENU + StringTestHelper.NO_PORTFOLIO_FOUND
            + StringTestHelper.MAIN_MENU + StringTestHelper.EXIT;

    MainController ctrl = new MainControllerImpl(model, view, in, new PrintStream(out));
    ctrl.init();
    Assert.assertEquals(expected, out.toString().trim());
  }

  @Test
  public void testPortfolioCompositionIncorrectName() {
    String inputString = "3 3 portfolioName l ";
    InputStream in = new ByteArrayInputStream(inputString.getBytes());
    String portfolioName = "portfolioName";

    String expected = StringTestHelper.INIT_MENU + StringTestHelper.USER_CREATED
            + StringTestHelper.MAIN_MENU + StringTestHelper.PORTFOLIO_NAME
            + StringTestHelper.PORTFOLIO_NAME_NOT_FOUND + portfolioName.toUpperCase() + "\n"
            + StringTestHelper.MAIN_MENU + StringTestHelper.EXIT;

    MainController ctrl = new MainControllerImpl(model, view, in, new PrintStream(out));
    ctrl.init();
    Assert.assertEquals(expected, out.toString().trim());
  }

  @Test
  public void testPortfolioComposition1() {
    String inputString = "3 3 portfolio1 9 k ";
    InputStream in = new ByteArrayInputStream(inputString.getBytes());

    String portfolioComp = "#  Stock | Qty | Composition\n"
            + "1  AAPL | 7 | 77.78%\n2  GOOG | 2 | 22.22%\n";

    String expected = StringTestHelper.INIT_MENU + StringTestHelper.USER_CREATED
            + StringTestHelper.MAIN_MENU + StringTestHelper.PORTFOLIO_NAME
            + portfolioComp + StringTestHelper.MAIN_MENU + StringTestHelper.INCORRECT_INPUT
            + StringTestHelper.MAIN_MENU + StringTestHelper.EXIT;

    MainController ctrl = new MainControllerImpl(model, view, in, new PrintStream(out));
    ctrl.init();
    Assert.assertEquals(expected, out.toString().trim());
  }

  @Test
  public void testPortfolioComposition2() {
    String inputString = "3 3 portfolio2 k ";
    InputStream in = new ByteArrayInputStream(inputString.getBytes());

    String portfolioComp = "#  Stock | Qty | Composition\n"
            + "1  AAPL | 4 | 9.76%\n2  GOOG | 13 | 31.71%\n3  C | 24 | 58.54%\n";

    String expected = StringTestHelper.INIT_MENU + StringTestHelper.USER_CREATED
            + StringTestHelper.MAIN_MENU + StringTestHelper.PORTFOLIO_NAME
            + portfolioComp + StringTestHelper.MAIN_MENU + StringTestHelper.EXIT;

    MainController ctrl = new MainControllerImpl(model, view, in, new PrintStream(out));
    ctrl.init();
    Assert.assertEquals(expected, out.toString().trim());
  }

  @Test
  public void testPortfolioComposition3() {
    String inputString = "3 3 portfolio3 k ";
    InputStream in = new ByteArrayInputStream(inputString.getBytes());

    String portfolioComp = "#  Stock | Qty | Composition\n"
            + "1  META | 14 | 41.18%\n2  DASH | 2 | 5.88%\n3  C | 18 | 52.94%\n";

    String expected = StringTestHelper.INIT_MENU + StringTestHelper.USER_CREATED
            + StringTestHelper.MAIN_MENU + StringTestHelper.PORTFOLIO_NAME
            + portfolioComp + StringTestHelper.MAIN_MENU + StringTestHelper.EXIT;

    MainController ctrl = new MainControllerImpl(model, view, in, new PrintStream(out));
    ctrl.init();
    Assert.assertEquals(expected, out.toString().trim());
  }

  @Test
  public void testPortfolioCompositionCaseInsensitive() {
    String inputString = "3 3 pOrtFolIo1 gh ";
    InputStream in = new ByteArrayInputStream(inputString.getBytes());

    String portfolioComp = "#  Stock | Qty | Composition\n"
            + "1  AAPL | 7 | 77.78%\n2  GOOG | 2 | 22.22%\n";

    String expected = StringTestHelper.INIT_MENU + StringTestHelper.USER_CREATED
            + StringTestHelper.MAIN_MENU + StringTestHelper.PORTFOLIO_NAME
            + portfolioComp + StringTestHelper.MAIN_MENU + StringTestHelper.EXIT;

    MainController ctrl = new MainControllerImpl(model, view, in, new PrintStream(out));
    ctrl.init();
    Assert.assertEquals(expected, out.toString().trim());
  }

  @Test
  public void testPortfolioValueNone() {
    String inputString = "3 4 h ";
    InputStream in = new ByteArrayInputStream(inputString.getBytes());

    String expected = StringTestHelper.INIT_MENU + StringTestHelper.USER_CREATED
            + StringTestHelper.MAIN_MENU + StringTestHelper.NO_PORTFOLIO_FOUND
            + StringTestHelper.MAIN_MENU + StringTestHelper.EXIT;

    MainController ctrl = new MainControllerImpl(model, view, in, new PrintStream(out));
    ctrl.init();
    Assert.assertEquals(expected, out.toString().trim());
  }

  @Test
  public void testPortfolioValueIncorrectName() {
    String inputString = "3 4 portfolioName h ";
    InputStream in = new ByteArrayInputStream(inputString.getBytes());
    String portfolioName = "portfolioName";

    String expected = StringTestHelper.INIT_MENU + StringTestHelper.USER_CREATED
            + StringTestHelper.MAIN_MENU + StringTestHelper.PORTFOLIO_NAME
            + StringTestHelper.PORTFOLIO_NAME_NOT_FOUND + portfolioName.toUpperCase() + "\n"
            + StringTestHelper.MAIN_MENU + StringTestHelper.EXIT;

    MainController ctrl = new MainControllerImpl(model, view, in, new PrintStream(out));
    ctrl.init();
    Assert.assertEquals(expected, out.toString().trim());
  }

  @Test
  public void testPortfolioValueIncorrectDateFormat() {
    String inputString = "3 4 portfolio1 10-31-2022 2022/10/31 2022-10-05 jhgh g ";
    InputStream in = new ByteArrayInputStream(inputString.getBytes());
    String portfolioValue = "The value of Portfolio on 2022-10-05 is: 1229.24\n";

    String expected = StringTestHelper.INIT_MENU + StringTestHelper.USER_CREATED
            + StringTestHelper.MAIN_MENU + StringTestHelper.PORTFOLIO_NAME
            + StringTestHelper.ENTER_DATE + StringTestHelper.INVALID_DATE_FORMAT
            + StringTestHelper.ENTER_DATE + StringTestHelper.INVALID_DATE_FORMAT
            + StringTestHelper.ENTER_DATE + portfolioValue
            + StringTestHelper.MAIN_MENU + StringTestHelper.INCORRECT_INPUT
            + StringTestHelper.MAIN_MENU + StringTestHelper.EXIT;

    MainController ctrl = new MainControllerImpl(model, view, in, new PrintStream(out));
    ctrl.init();
    Assert.assertEquals(expected, out.toString().trim());
  }

  @Test
  public void testPortfolioValue() {
    String inputString = "3 4 portfolio1 2022-09-17 gh ";
    InputStream in = new ByteArrayInputStream(inputString.getBytes());
    String portfolioValue = "The value of Portfolio on 2022-09-17 is: 1262.16\n";

    String expected = StringTestHelper.INIT_MENU + StringTestHelper.USER_CREATED
            + StringTestHelper.MAIN_MENU + StringTestHelper.PORTFOLIO_NAME
            + StringTestHelper.ENTER_DATE + portfolioValue
            + StringTestHelper.MAIN_MENU + StringTestHelper.EXIT;

    MainController ctrl = new MainControllerImpl(model, view, in, new PrintStream(out));
    ctrl.init();
    Assert.assertEquals(expected, out.toString().trim());
  }

  @Test
  public void testPortfolioValueFutureDate() {
    String inputString = "3 4 portfolio1 2045-10-10 2022-10-10 gh ";
    InputStream in = new ByteArrayInputStream(inputString.getBytes());
    String portfolioValue = "The value of Portfolio on 2022-10-10 is: 5576.48\n";

    String expected = StringTestHelper.INIT_MENU + StringTestHelper.USER_CREATED
            + StringTestHelper.MAIN_MENU + StringTestHelper.PORTFOLIO_NAME
            + StringTestHelper.ENTER_DATE + StringTestHelper.FUTURE_DATE
            + StringTestHelper.ENTER_DATE + portfolioValue
            + StringTestHelper.MAIN_MENU + StringTestHelper.EXIT;

    MainController ctrl = new MainControllerImpl(model, view, in, new PrintStream(out));
    ctrl.init();
    Assert.assertEquals(expected, out.toString().trim());
  }

  @Test
  public void testPortfolioValuePastDateDataNotAvailable() {
    String inputString = "3 4 portfolio1 2005-10-10 2022-10-10 hg ";
    InputStream in = new ByteArrayInputStream(inputString.getBytes());
    String portfolioValue = "The value of Portfolio on 2022-10-10 is: 5576.48\n";

    String expected = StringTestHelper.INIT_MENU + StringTestHelper.USER_CREATED
            + StringTestHelper.MAIN_MENU + StringTestHelper.PORTFOLIO_NAME
            + StringTestHelper.ENTER_DATE
            + "Value for ticker META not found for the given date 2005-10-10.\n"
            + "Please try with a future date.\n"
            + StringTestHelper.ENTER_DATE + portfolioValue
            + StringTestHelper.MAIN_MENU + StringTestHelper.EXIT;

    MainController ctrl = new MainControllerImpl(model, view, in, new PrintStream(out));
    ctrl.init();
    Assert.assertEquals(expected, out.toString().trim());
  }

  @Test
  public void testPortfolioValueMultipleInputs() {
    String inputString = "vbcv 3 4 portfolio1 2022-11-3 2008-10-09 2022/10/10 2022-10-10 gh ";
    InputStream in = new ByteArrayInputStream(inputString.getBytes());
    String portfolioValue = "The value of Portfolio on 2022-10-10 is: 5576.48\n";

    String expected = StringTestHelper.INIT_MENU + StringTestHelper.INCORRECT_INPUT
            + StringTestHelper.INIT_MENU + StringTestHelper.USER_CREATED
            + StringTestHelper.MAIN_MENU + StringTestHelper.PORTFOLIO_NAME
            + StringTestHelper.ENTER_DATE + StringTestHelper.INVALID_DATE_FORMAT
            + StringTestHelper.ENTER_DATE
            + "Value for ticker META not found for the given date 2008-10-09.\n"
            + "Please try with a future date.\n"
            + StringTestHelper.ENTER_DATE + StringTestHelper.INVALID_DATE_FORMAT
            + StringTestHelper.ENTER_DATE + portfolioValue
            + StringTestHelper.MAIN_MENU + StringTestHelper.EXIT;

    MainController ctrl = new MainControllerImpl(model, view, in, new PrintStream(out));
    ctrl.init();
    Assert.assertEquals(expected, out.toString().trim());
  }

  @Test
  public void testPortfolioValueMarketCloseDates() {
    String inputString = "3 4 portfolio1 2022-10-31 4 portfolio1 2022-10-30 "
            + "4 portfolio1 2022-10-29 4 portfolio1 2022-10-28 4 portfolio1 2022-10-27 gh ";
    InputStream in = new ByteArrayInputStream(inputString.getBytes());
    String portfolioValue31 = "The value of Portfolio on 2022-10-31 is: 1262.70\n";
    String portfolioValue30 = "The value of Portfolio on 2022-10-30 is: 1283.34\n";
    String portfolioValue29 = "The value of Portfolio on 2022-10-29 is: 1283.34\n";
    String portfolioValue28 = "The value of Portfolio on 2022-10-28 is: 1283.34\n";
    String portfolioValue27 = "The value of Portfolio on 2022-10-27 is: 1198.80\n";

    String expected = StringTestHelper.INIT_MENU + StringTestHelper.USER_CREATED
            + StringTestHelper.MAIN_MENU + StringTestHelper.PORTFOLIO_NAME
            + StringTestHelper.ENTER_DATE + portfolioValue31 + StringTestHelper.MAIN_MENU
            + StringTestHelper.PORTFOLIO_NAME + StringTestHelper.ENTER_DATE
            + portfolioValue30 + StringTestHelper.MAIN_MENU + StringTestHelper.PORTFOLIO_NAME
            + StringTestHelper.ENTER_DATE + portfolioValue29 + StringTestHelper.MAIN_MENU
            + StringTestHelper.PORTFOLIO_NAME + StringTestHelper.ENTER_DATE + portfolioValue28
            + StringTestHelper.MAIN_MENU
            + StringTestHelper.PORTFOLIO_NAME + StringTestHelper.ENTER_DATE + portfolioValue27
            + StringTestHelper.MAIN_MENU + StringTestHelper.EXIT;

    MainController ctrl = new MainControllerImpl(model, view, in, new PrintStream(out));
    ctrl.init();
    Assert.assertEquals(expected, out.toString().trim());
  }

  @Test
  public void testEmptyFileUpload() {
    String inputString = "2 emptyFile.csv 3 g ";
    InputStream in = new ByteArrayInputStream(inputString.getBytes());

    String expected = StringTestHelper.INIT_MENU + StringTestHelper.EXTERNAL_FILE_PATH
            + StringTestHelper.FILE_LOAD_ERROR + StringTestHelper.INIT_MENU
            + StringTestHelper.USER_CREATED + StringTestHelper.MAIN_MENU
            + StringTestHelper.EXIT;

    MainController ctrl = new MainControllerImpl(model, view, in, new PrintStream(out));
    ctrl.init();
    Assert.assertEquals(expected, out.toString().trim());
  }

  @Test
  public void testFileUpload() {
    String inputString = "2 res/testfiles/validFileUpload.csv 2 hg ";
    InputStream in = new ByteArrayInputStream(inputString.getBytes());

    String getPortfolio = "# \t Name\n1 \t PORTFOLIO1\n2 \t PORTFOLIO2\n";

    String expected = StringTestHelper.INIT_MENU + StringTestHelper.EXTERNAL_FILE_PATH
            + StringTestHelper.FILE_UPLOAD_SUCCESS + StringTestHelper.MAIN_MENU
            + getPortfolio + StringTestHelper.MAIN_MENU
            + StringTestHelper.EXIT;

    MainController ctrl = new MainControllerImpl(model, view, in, new PrintStream(out));
    ctrl.init();
    Assert.assertEquals(expected, out.toString().trim());
  }

  @Test
  public void testFileUploadMultiple() {
    String inputString =
            "2 emptyFile.csv 2 res/testfiles/validFileUpload.csv 2 3 PORTFOLIO1 3 portfolio2 "
            + "4 portfolio 4 portfolio2 2022/10/10 2022-10-10  gh ";
    InputStream in = new ByteArrayInputStream(inputString.getBytes());

    String getPortfolio = "# \t Name\n1 \t PORTFOLIO1\n2 \t PORTFOLIO2\n";
    String getPortfolioComp1 =
            "#  Stock | Qty | Composition\n1  META | 9 | 12.00%\n2  AAPL | 6 | 8.00%\n"
                    + "3  NKLA | 25 | 33.33%\n4  GOOG | 35 | 46.67%\n";
    String getPortfolioComp2 = "#  Stock | Qty | Composition\n"
            + "1  AAPL | 6 | 30.00%\n2  GOOG | 14 | 70.00%\n";
    String portfolioName = "portfolio";
    String portfolioValue = "The value of Portfolio on 2022-10-10 is: 2224.46\n";

    String expected = StringTestHelper.INIT_MENU + StringTestHelper.EXTERNAL_FILE_PATH
            + StringTestHelper.FILE_LOAD_ERROR + StringTestHelper.INIT_MENU
            + StringTestHelper.EXTERNAL_FILE_PATH
            + StringTestHelper.FILE_UPLOAD_SUCCESS + StringTestHelper.MAIN_MENU
            + getPortfolio + StringTestHelper.MAIN_MENU + StringTestHelper.PORTFOLIO_NAME
            + getPortfolioComp1 + StringTestHelper.MAIN_MENU + StringTestHelper.PORTFOLIO_NAME
            + getPortfolioComp2 + StringTestHelper.MAIN_MENU + StringTestHelper.PORTFOLIO_NAME
            + StringTestHelper.PORTFOLIO_NAME_NOT_FOUND
            + portfolioName.toUpperCase() + "\n"
            + StringTestHelper.MAIN_MENU + StringTestHelper.PORTFOLIO_NAME
            + StringTestHelper.ENTER_DATE + StringTestHelper.INVALID_DATE_FORMAT
            + StringTestHelper.ENTER_DATE + portfolioValue
            + StringTestHelper.MAIN_MENU + StringTestHelper.EXIT;

    MainController ctrl = new MainControllerImpl(model, view, in, new PrintStream(out));
    ctrl.init();
    Assert.assertEquals(expected, out.toString().trim());
  }

  @Test
  public void testFileUploadSameTicker() {
    String inputString = "2 res/testfiles/validFileUpload.csv 2 3 PORTFOLIO1 gh ";
    InputStream in = new ByteArrayInputStream(inputString.getBytes());

    String getPortfolio = "# \t Name\n1 \t PORTFOLIO1\n2 \t PORTFOLIO2\n";
    String getPortfolioComp = "#  Stock | Qty | Composition\n"
            + "1  META | 9 | 12.00%\n2  AAPL | 6 | 8.00%\n3  NKLA | 25 | 33.33%\n"
            + "4  GOOG | 35 | 46.67%\n";

    String expected = StringTestHelper.INIT_MENU + StringTestHelper.EXTERNAL_FILE_PATH
            + StringTestHelper.FILE_UPLOAD_SUCCESS + StringTestHelper.MAIN_MENU
            + getPortfolio + StringTestHelper.MAIN_MENU + StringTestHelper.PORTFOLIO_NAME
            + getPortfolioComp + StringTestHelper.MAIN_MENU + StringTestHelper.EXIT;

    MainController ctrl = new MainControllerImpl(model, view, in, new PrintStream(out));
    ctrl.init();
    Assert.assertEquals(expected, out.toString().trim());
  }

  @Test
  public void testProfileload() {
    String inputString = "2 validFileUpload.csv 2 3 PORTFOLIO1 3 PORTFOLIO2 gh ";
    InputStream in = new ByteArrayInputStream(inputString.getBytes());

    String getPortfolio = "# \t Name\n1 \t PORTFOLIO1\n2 \t PORTFOLIO2\n";
    String getPortfolioComp1 = "#  Stock | Qty | Composition\n1  META | 9 | 12.00%\n"
            + "2  GOOG | 35 | 46.67%\n3  AAPL | 6 | 8.00%\n4  NKLA | 25 | 33.33%\n";
    String getPortfolioComp2 = "#  Stock | Qty | Composition\n"
            + "1  GOOG | 14 | 70.00%\n2  AAPL | 6 | 30.00%\n";

    String expected = StringTestHelper.INIT_MENU + StringTestHelper.EXTERNAL_FILE_PATH
            + StringTestHelper.FILE_UPLOAD_SUCCESS + StringTestHelper.MAIN_MENU
            + getPortfolio + StringTestHelper.MAIN_MENU
            + StringTestHelper.PORTFOLIO_NAME
            + getPortfolioComp1 + StringTestHelper.MAIN_MENU + StringTestHelper.PORTFOLIO_NAME
            + getPortfolioComp2 + StringTestHelper.MAIN_MENU + StringTestHelper.EXIT;

    MainController ctrl = new MainControllerImpl(model, view, in, new PrintStream(out));
    ctrl.init();
    Assert.assertEquals(expected, out.toString().trim());

    UserModel modelNew = new UserModelImpl(null, null, null);
    String inputString1 = "1 2 3 PORTFOLIO1 3 PORTFOLIO2 5 ";
    InputStream in1 = new ByteArrayInputStream(inputString1.getBytes());

    String expected1 = StringTestHelper.INIT_MENU + StringTestHelper.PROFILE_LOAD_SUCCESS
            + StringTestHelper.MAIN_MENU
            + getPortfolio + StringTestHelper.MAIN_MENU + StringTestHelper.PORTFOLIO_NAME
            + getPortfolioComp1 + StringTestHelper.MAIN_MENU + StringTestHelper.PORTFOLIO_NAME
            + getPortfolioComp2 + StringTestHelper.MAIN_MENU + StringTestHelper.EXIT;

    MainController ctrl1 = new MainControllerImpl(modelNew, view, in1, new PrintStream(out));
    ctrl1.init();
    Assert.assertEquals(expected + "\n" + expected1, out.toString().trim());
  }

  @Test
  public void testFileUploadFractionalShare() {
    String inputString = "2 fractionalShare.csv 2 validFileUpload.csv 2 3 PORTFOLIO1 3 "
            + "PORTFOLIO2 hg ";
    InputStream in = new ByteArrayInputStream(inputString.getBytes());

    String getPortfolio = "# \t Name\n1 \t PORTFOLIO1\n2 \t PORTFOLIO2\n";
    String getPortfolioComp1 = "#  Stock | Qty | Composition\n1  META | 9 | 12.00%\n"
            + "2  GOOG | 35 | 46.67%\n3  AAPL | 6 | 8.00%\n4  NKLA | 25 | 33.33%\n";
    String getPortfolioComp2 = "#  Stock | Qty | Composition\n"
            + "1  GOOG | 14 | 70.00%\n2  AAPL | 6 | 30.00%\n";

    String expected = StringTestHelper.INIT_MENU + StringTestHelper.EXTERNAL_FILE_PATH
            + StringTestHelper.ERROR_FRACTIONAL_SHARE + StringTestHelper.INIT_MENU
            + StringTestHelper.EXTERNAL_FILE_PATH
            + StringTestHelper.FILE_UPLOAD_SUCCESS + StringTestHelper.MAIN_MENU
            + getPortfolio + StringTestHelper.MAIN_MENU + StringTestHelper.PORTFOLIO_NAME
            + getPortfolioComp1 + StringTestHelper.MAIN_MENU + StringTestHelper.PORTFOLIO_NAME
            + getPortfolioComp2 + StringTestHelper.MAIN_MENU + StringTestHelper.EXIT;

    MainController ctrl = new MainControllerImpl(model, view, in, new PrintStream(out));
    ctrl.init();
    Assert.assertEquals(expected, out.toString().trim());
  }

  @Test
  public void testFileUploadNegativeQuantity() {
    String inputString = "2 quantityNegative.csv 2 validFileUpload.csv 2 3 PORTFOLIO1 3 "
            + "PORTFOLIO2 hg ";
    InputStream in = new ByteArrayInputStream(inputString.getBytes());

    String getPortfolio = "# \t Name\n1 \t PORTFOLIO1\n2 \t PORTFOLIO2\n";
    String getPortfolioComp1 = "#  Stock | Qty | Composition\n1  META | 9 | 12.00%\n"
            + "2  GOOG | 35 | 46.67%\n3  AAPL | 6 | 8.00%\n4  NKLA | 25 | 33.33%\n";
    String getPortfolioComp2 = "#  Stock | Qty | Composition\n"
            + "1  GOOG | 14 | 70.00%\n2  AAPL | 6 | 30.00%\n";

    String expected = StringTestHelper.INIT_MENU + StringTestHelper.EXTERNAL_FILE_PATH
            + StringTestHelper.ERROR_QUANTITY_NEGATIVE + StringTestHelper.INIT_MENU
            + StringTestHelper.EXTERNAL_FILE_PATH
            + StringTestHelper.FILE_UPLOAD_SUCCESS + StringTestHelper.MAIN_MENU
            + getPortfolio + StringTestHelper.MAIN_MENU + StringTestHelper.PORTFOLIO_NAME
            + getPortfolioComp1 + StringTestHelper.MAIN_MENU + StringTestHelper.PORTFOLIO_NAME
            + getPortfolioComp2 + StringTestHelper.MAIN_MENU + StringTestHelper.EXIT;

    MainController ctrl = new MainControllerImpl(model, view, in, new PrintStream(out));
    ctrl.init();
    Assert.assertEquals(expected, out.toString().trim());
  }


  @Test
  public void testFileUploadFlex() {
    String inputString = "2 quantityNegative.csv 2 "
            + "res/testfiles/fileUploadFlex.csv 2 9 ";
    InputStream in = new ByteArrayInputStream(inputString.getBytes());

    String getPortfolio = "******* Flexible Portfolios *******\n"
            + "1 \t PORTFOLIOFLEX2\n"
            + "2 \t PORTFOLIOFLEX1\n"
            + "\n******* Inflexible Portfolios *******\n1 \t P1\n";

    String expected = StringTestHelper.INIT_MENU + StringTestHelper.EXTERNAL_FILE_PATH
            + StringTestHelper.FILE_LOAD_ERROR + StringTestHelper.INIT_MENU
            + StringTestHelper.EXTERNAL_FILE_PATH
            + StringTestHelper.FILE_UPLOAD_SUCCESS + StringTestHelper.MAIN_MENU
            + getPortfolio + StringTestHelper.MAIN_MENU + StringTestHelper.EXIT;

    MainController ctrl = new MainControllerImpl(model, view, in, new PrintStream(out));
    ctrl.init();
    Assert.assertEquals(expected, out.toString().trim());
  }

  @Test
  public void testGetCompositionFlex() {
    String inputString = "2 quantityNegative.csv 2 "
            + "res/testfiles/fileUploadFlex.csv 2 3 P1 3 PORTFOLIOFLEX1 2022-06-05 9 ";
    InputStream in = new ByteArrayInputStream(inputString.getBytes());

    String getPortfolio = "******* Flexible Portfolios *******\n"
            + "1 \t PORTFOLIOFLEX2\n"
            + "2 \t PORTFOLIOFLEX1\n"
            + "\n******* Inflexible Portfolios *******\n1 \t P1\n";
    String getPortfolioComp1 = "******* Inflexible Portfolio *******\n"
            + "#  Stock | Qty | Composition\n1  NKLA | 12.0 | 50.00%\n"
            + "2  TSLA | 8.0 | 33.33%\n3  DASH | 4.0 | 16.67%\n";
    String getPortfolioComp2 = "******* Flexible Portfolio *******\n"
            + "#  Stock | Qty | Composition\n"
            + "1  META | 9.0 | 33.33%\n"
            + "2  GOOG | 12.0 | 44.44%\n"
            + "3  AAPL | 6.0 | 22.22%\n";

    String expected = StringTestHelper.INIT_MENU + StringTestHelper.EXTERNAL_FILE_PATH
            + StringTestHelper.FILE_LOAD_ERROR + StringTestHelper.INIT_MENU
            + StringTestHelper.EXTERNAL_FILE_PATH
            + StringTestHelper.FILE_UPLOAD_SUCCESS + StringTestHelper.MAIN_MENU
            + getPortfolio + StringTestHelper.MAIN_MENU + StringTestHelper.PORTFOLIO_NAME
            + getPortfolioComp1 + StringTestHelper.MAIN_MENU + StringTestHelper.PORTFOLIO_NAME
            + StringTestHelper.DATE_COMPOSITION
            + getPortfolioComp2 + StringTestHelper.MAIN_MENU + StringTestHelper.EXIT;

    MainController ctrl = new MainControllerImpl(model, view, in, new PrintStream(out));
    ctrl.init();
    Assert.assertEquals(expected, out.toString().trim());
  }

  @Test
  public void testGetCompositionFlexPastDate() {
    String inputString = "2 quantityNegative.csv 2 "
            + "res/testfiles/fileUploadFlex.csv 2 3 PORTFOLIOFLEX1 1993-06-05 9 ";
    InputStream in = new ByteArrayInputStream(inputString.getBytes());

    String getPortfolio = "******* Flexible Portfolios *******\n"
            + "1 \t PORTFOLIOFLEX2\n"
            + "2 \t PORTFOLIOFLEX1\n"
            + "\n******* Inflexible Portfolios *******\n1 \t P1\n";
    String getPortfolioComp1 = "No stocks found.\n";

    String expected = StringTestHelper.INIT_MENU + StringTestHelper.EXTERNAL_FILE_PATH
            + StringTestHelper.FILE_LOAD_ERROR + StringTestHelper.INIT_MENU
            + StringTestHelper.EXTERNAL_FILE_PATH
            + StringTestHelper.FILE_UPLOAD_SUCCESS + StringTestHelper.MAIN_MENU
            + getPortfolio + StringTestHelper.MAIN_MENU + StringTestHelper.PORTFOLIO_NAME
            + StringTestHelper.DATE_COMPOSITION
            + getPortfolioComp1 + StringTestHelper.MAIN_MENU + StringTestHelper.EXIT;

    MainController ctrl = new MainControllerImpl(model, view, in, new PrintStream(out));
    ctrl.init();
    Assert.assertEquals(expected, out.toString().trim());
  }

  @Test
  public void testPortfolioValueFlex() {
    String inputString = "2 res/testfiles/fileUploadFlex.csv 4 P1 2022-09-17 4 PORTFOLIOFLEX1 "
            + "2020-02-16 iop ";
    InputStream in = new ByteArrayInputStream(inputString.getBytes());
    String portfolioValue = "******* Inflexible Portfolio *******\n"
            + "The value of Portfolio on 2022-09-17 is 2724.76 USD.\n";

    String portfolioValue1 = "******* Flexible Portfolio *******\n"
            + "The value of Portfolio on 2020-02-16 is 22126.20 USD.\n";

    String expected = StringTestHelper.INIT_MENU
            + StringTestHelper.EXTERNAL_FILE_PATH
            + StringTestHelper.FILE_UPLOAD_SUCCESS
            + StringTestHelper.MAIN_MENU + StringTestHelper.PORTFOLIO_NAME
            + StringTestHelper.ENTER_DATE + portfolioValue
            + StringTestHelper.MAIN_MENU + StringTestHelper.PORTFOLIO_NAME
            + StringTestHelper.ENTER_DATE + portfolioValue1
            + StringTestHelper.MAIN_MENU + StringTestHelper.EXIT;

    MainController ctrl = new MainControllerImpl(model, view, in, new PrintStream(out));
    ctrl.init();
    Assert.assertEquals(expected, out.toString().trim());
  }

  @Test
  public void testPortfolioValueFlex1() {
    String inputString = "2 res/testfiles/fileUploadFlex.csv 4 P1 2022-09-17 4 PORTFOLIOFLEX1 "
            + "2022-02-16 iop ";
    InputStream in = new ByteArrayInputStream(inputString.getBytes());
    String portfolioValue = "******* Inflexible Portfolio *******\n"
            + "The value of Portfolio on 2022-09-17 is 2724.76 USD.\n";

    String portfolioValue1 = "******* Flexible Portfolio *******\n"
            + "The value of Portfolio on 2022-02-16 is 35981.16 USD.\n";

    String expected = StringTestHelper.INIT_MENU
            + StringTestHelper.EXTERNAL_FILE_PATH
            + StringTestHelper.FILE_UPLOAD_SUCCESS
            + StringTestHelper.MAIN_MENU + StringTestHelper.PORTFOLIO_NAME
            + StringTestHelper.ENTER_DATE + portfolioValue
            + StringTestHelper.MAIN_MENU + StringTestHelper.PORTFOLIO_NAME
            + StringTestHelper.ENTER_DATE + portfolioValue1
            + StringTestHelper.MAIN_MENU + StringTestHelper.EXIT;

    MainController ctrl = new MainControllerImpl(model, view, in, new PrintStream(out));
    ctrl.init();
    Assert.assertEquals(expected, out.toString().trim());
  }

  @Test
  public void testPortfolioValueFlexPastDate() {
    String inputString = "2 res/testfiles/fileUploadFlex.csv 4 P1 2022-09-17 4 PORTFOLIOFLEX1 "
            + "2012-02-16 iop ";
    InputStream in = new ByteArrayInputStream(inputString.getBytes());
    String portfolioValue = "******* Inflexible Portfolio *******\n"
            + "The value of Portfolio on 2022-09-17 is 2724.76 USD.\n";

    String portfolioValue1 = "******* Flexible Portfolio *******\n"
            + "The value of Portfolio on 2012-02-16 is 0.00 USD.\n";

    String expected = StringTestHelper.INIT_MENU
            + StringTestHelper.EXTERNAL_FILE_PATH
            + StringTestHelper.FILE_UPLOAD_SUCCESS
            + StringTestHelper.MAIN_MENU + StringTestHelper.PORTFOLIO_NAME
            + StringTestHelper.ENTER_DATE + portfolioValue
            + StringTestHelper.MAIN_MENU + StringTestHelper.PORTFOLIO_NAME
            + StringTestHelper.ENTER_DATE + portfolioValue1
            + StringTestHelper.MAIN_MENU + StringTestHelper.EXIT;

    MainController ctrl = new MainControllerImpl(model, view, in, new PrintStream(out));
    ctrl.init();
    Assert.assertEquals(expected, out.toString().trim());
  }

  @Test
  public void testPortfolioPerformanceYear() {
    String inputString = "2 res/testfiles/fileUploadFlex.csv 7 PORTFOLIOFLEX1 1 2002"
            + " 2021 iop ";
    InputStream in = new ByteArrayInputStream(inputString.getBytes());

    String performance = "Performance of portfolio Test from Jan 2002 to Dec 2021\n"
            + "Jan 2002 : \n"
            + "Sep 2002 : \n"
            + "May 2003 : \n"
            + "Jan 2004 : \n"
            + "Sep 2004 : \n"
            + "May 2005 : \n"
            + "Jan 2006 : \n"
            + "Sep 2006 : \n"
            + "May 2007 : \n"
            + "Jan 2008 : \n"
            + "Sep 2008 : \n"
            + "May 2009 : \n"
            + "Jan 2010 : \n"
            + "Sep 2010 : \n"
            + "May 2011 : \n"
            + "Jan 2012 : \n"
            + "Sep 2012 : \n"
            + "May 2013 : \n"
            + "Jan 2014 : \n"
            + "Sep 2014 : \n"
            + "May 2015 : \n"
            + "Jan 2016 : \n"
            + "Sep 2016 : \n"
            + "May 2017 : \n"
            + "Jan 2018 : \n"
            + "Sep 2018 : *******************\n"
            + "May 2019 : ******************\n"
            + "Jan 2020 : *******************************\n"
            + "Sep 2020 : ***************************\n"
            + "May 2021 : *******************************************\n"
            + "Dec 2021 : **************************************************\n"
            + "Scale: * = $777\n"
            + "Base Amount = $0.00\n";

    String expected = StringTestHelper.INIT_MENU
            + StringTestHelper.EXTERNAL_FILE_PATH
            + StringTestHelper.FILE_UPLOAD_SUCCESS
            + StringTestHelper.MAIN_MENU + StringTestHelper.PORTFOLIO_NAME
            + StringTestHelper.PERFORMANCE_PERIOD + StringTestHelper.PERFORMANCE_START_END_YEAR
            + performance
            + StringTestHelper.MAIN_MENU + StringTestHelper.EXIT;

    MainController ctrl = new MainControllerImpl(model, view, in, new PrintStream(out));
    ctrl.init();
    Assert.assertEquals(expected, out.toString().trim());
  }

  @Test
  public void testPortfolioPerformanceMonth() {
    String inputString = "2 res/testfiles/fileUploadFlex.csv 7 PORTFOLIOFLEX1 2 2020 2"
            + " 2022 6 iop ";
    InputStream in = new ByteArrayInputStream(inputString.getBytes());

    String performance = "Performance of portfolio Test from 2020-02-01 to 2022-06-30\n"
            + "2020-02-01 : ****************\n"
            + "2020-03-02 : *********\n"
            + "2020-04-01 : \n"
            + "2020-05-01 : ********\n"
            + "2020-05-31 : ***********\n"
            + "2020-06-30 : ***********\n"
            + "2020-07-30 : ***************\n"
            + "2020-08-29 : ********************\n"
            + "2020-09-28 : **********\n"
            + "2020-10-28 : ************\n"
            + "2020-11-27 : *******************\n"
            + "2020-12-27 : ******************\n"
            + "2021-01-26 : ***********************\n"
            + "2021-02-25 : *************************\n"
            + "2021-03-27 : *************************\n"
            + "2021-04-26 : *********************************\n"
            + "2021-05-26 : *************************************\n"
            + "2021-06-25 : ****************************************\n"
            + "2021-07-25 : **********************************************\n"
            + "2021-08-24 : ************************************************\n"
            + "2021-09-23 : ************************************************\n"
            + "2021-10-23 : **********************************************\n"
            + "2021-11-22 : **************************************************\n"
            + "2021-12-22 : **************************************************\n"
            + "2022-01-21 : *****************************************\n"
            + "2022-02-20 : ***************************************\n"
            + "2022-03-22 : *********************************************\n"
            + "2022-04-21 : ************************************\n"
            + "2022-05-21 : ****************************\n"
            + "2022-06-30 : ***************************\n"
            + "Scale: * = $464\n"
            + "Base Amount = $16149.30\n";

    String expected = StringTestHelper.INIT_MENU
            + StringTestHelper.EXTERNAL_FILE_PATH
            + StringTestHelper.FILE_UPLOAD_SUCCESS
            + StringTestHelper.MAIN_MENU + StringTestHelper.PORTFOLIO_NAME
            + StringTestHelper.PERFORMANCE_PERIOD + StringTestHelper.PERFORMANCE_START_END_MONTH
            + performance
            + StringTestHelper.MAIN_MENU + StringTestHelper.EXIT;

    MainController ctrl = new MainControllerImpl(model, view, in, new PrintStream(out));
    ctrl.init();
    Assert.assertEquals(expected, out.toString().trim());
  }

  @Test
  public void testPortfolioPerformanceDay() {
    String inputString = "2 res/testfiles/fileUploadFlex.csv 7 P1 PORTFOLIOFLEX1 j 2019 10 20"
            + " 2019 10 30 iop ";
    InputStream in = new ByteArrayInputStream(inputString.getBytes());

    String performance = "Performance of portfolio Test from 2019-10-01 to 2019-10-30\n"
            + "2019-10-01 : ************\n"
            + "2019-10-02 : \n"
            + "2019-10-03 : ******\n"
            + "2019-10-04 : ***************\n"
            + "2019-10-05 : ***************\n"
            + "2019-10-06 : ***************\n"
            + "2019-10-07 : **************\n"
            + "2019-10-08 : ******\n"
            + "2019-10-09 : ************\n"
            + "2019-10-10 : ***************\n"
            + "2019-10-11 : *******************\n"
            + "2019-10-12 : *******************\n"
            + "2019-10-13 : *******************\n"
            + "2019-10-14 : *******************\n"
            + "2019-10-15 : *******************************\n"
            + "2019-10-16 : ********************************\n"
            + "2019-10-17 : ************************************\n"
            + "2019-10-18 : *******************************\n"
            + "2019-10-19 : *******************************\n"
            + "2019-10-20 : *******************************\n"
            + "2019-10-21 : *********************************\n"
            + "2019-10-22 : *****************************\n"
            + "2019-10-23 : *************************************\n"
            + "2019-10-24 : **************************************\n"
            + "2019-10-25 : ****************************************\n"
            + "2019-10-26 : ****************************************\n"
            + "2019-10-27 : ****************************************\n"
            + "2019-10-28 : **************************************************\n"
            + "2019-10-30 : **************************************\n"
            + "Scale: * = $30\n"
            + "Base Amount = $15690.96\n";


    String expected = StringTestHelper.INIT_MENU + StringTestHelper.EXTERNAL_FILE_PATH
            + StringTestHelper.FILE_UPLOAD_SUCCESS
            + StringTestHelper.MAIN_MENU + StringTestHelper.PORTFOLIO_NAME
            + StringTestHelper.CANNOT_UPDATE + StringTestHelper.PORTFOLIO_NAME
            + StringTestHelper.PERFORMANCE_PERIOD + StringTestHelper.PERFORMANCE_START_END
            + performance + StringTestHelper.MAIN_MENU
            + StringTestHelper.EXIT;

    MainController ctrl = new MainControllerImpl(model, view, in, new PrintStream(out));
    ctrl.init();
    Assert.assertEquals(expected, out.toString().trim());
  }

  @Test
  public void testPortfolioPerformanceInvalidYear() {
    String inputString = "2 res/testfiles/fileUploadFlex.csv 7 PORTFOLIOFLEX1 1 -2002 2034 2021 "
            + " 2002 2034 2002 2021 iop ";
    InputStream in = new ByteArrayInputStream(inputString.getBytes());

    String performance = "Performance of portfolio Test from Jan 2002 to Dec 2021\n"
            + "Jan 2002 : \n"
            + "Sep 2002 : \n"
            + "May 2003 : \n"
            + "Jan 2004 : \n"
            + "Sep 2004 : \n"
            + "May 2005 : \n"
            + "Jan 2006 : \n"
            + "Sep 2006 : \n"
            + "May 2007 : \n"
            + "Jan 2008 : \n"
            + "Sep 2008 : \n"
            + "May 2009 : \n"
            + "Jan 2010 : \n"
            + "Sep 2010 : \n"
            + "May 2011 : \n"
            + "Jan 2012 : \n"
            + "Sep 2012 : \n"
            + "May 2013 : \n"
            + "Jan 2014 : \n"
            + "Sep 2014 : \n"
            + "May 2015 : \n"
            + "Jan 2016 : \n"
            + "Sep 2016 : \n"
            + "May 2017 : \n"
            + "Jan 2018 : \n"
            + "Sep 2018 : *******************\n"
            + "May 2019 : ******************\n"
            + "Jan 2020 : *******************************\n"
            + "Sep 2020 : ***************************\n"
            + "May 2021 : *******************************************\n"
            + "Dec 2021 : **************************************************\n"
            + "Scale: * = $777\n"
            + "Base Amount = $0.00\n";

    String expected = StringTestHelper.INIT_MENU
            + StringTestHelper.EXTERNAL_FILE_PATH
            + StringTestHelper.FILE_UPLOAD_SUCCESS
            + StringTestHelper.MAIN_MENU + StringTestHelper.PORTFOLIO_NAME
            + StringTestHelper.PERFORMANCE_PERIOD + "Enter start year : (YYYY)\n"
            + "Incorrect input! Please try again.\n"
            + StringTestHelper.PERFORMANCE_START_END_YEAR
            + "Start date cannot be in the future.\n" + StringTestHelper.PERFORMANCE_START_END_YEAR
            + "End date cannot be in the future.\n" + StringTestHelper.PERFORMANCE_START_END_YEAR
            + performance
            + StringTestHelper.MAIN_MENU + StringTestHelper.EXIT;

    MainController ctrl = new MainControllerImpl(model, view, in, new PrintStream(out));
    ctrl.init();
    Assert.assertEquals(expected, out.toString().trim());
  }

  @Test
  public void testPortfolioPerformanceFutureStartYear() {
    String inputString = "2 res/testfiles/fileUploadFlex.csv 7 PORTFOLIOFLEX1 1 2021 2012"
            + " 2002 2021 iop ";
    InputStream in = new ByteArrayInputStream(inputString.getBytes());

    String performance = "Performance of portfolio Test from Jan 2002 to Dec 2021\n"
            + "Jan 2002 : \n"
            + "Sep 2002 : \n"
            + "May 2003 : \n"
            + "Jan 2004 : \n"
            + "Sep 2004 : \n"
            + "May 2005 : \n"
            + "Jan 2006 : \n"
            + "Sep 2006 : \n"
            + "May 2007 : \n"
            + "Jan 2008 : \n"
            + "Sep 2008 : \n"
            + "May 2009 : \n"
            + "Jan 2010 : \n"
            + "Sep 2010 : \n"
            + "May 2011 : \n"
            + "Jan 2012 : \n"
            + "Sep 2012 : \n"
            + "May 2013 : \n"
            + "Jan 2014 : \n"
            + "Sep 2014 : \n"
            + "May 2015 : \n"
            + "Jan 2016 : \n"
            + "Sep 2016 : \n"
            + "May 2017 : \n"
            + "Jan 2018 : \n"
            + "Sep 2018 : *******************\n"
            + "May 2019 : ******************\n"
            + "Jan 2020 : *******************************\n"
            + "Sep 2020 : ***************************\n"
            + "May 2021 : *******************************************\n"
            + "Dec 2021 : **************************************************\n"
            + "Scale: * = $777\n"
            + "Base Amount = $0.00\n";

    String expected = StringTestHelper.INIT_MENU
            + StringTestHelper.EXTERNAL_FILE_PATH
            + StringTestHelper.FILE_UPLOAD_SUCCESS
            + StringTestHelper.MAIN_MENU + StringTestHelper.PORTFOLIO_NAME
            + StringTestHelper.PERFORMANCE_PERIOD
            + StringTestHelper.PERFORMANCE_START_END_YEAR
            + "End date cannot be before start date.\n"
            + StringTestHelper.PERFORMANCE_START_END_YEAR
            + performance
            + StringTestHelper.MAIN_MENU + StringTestHelper.EXIT;

    MainController ctrl = new MainControllerImpl(model, view, in, new PrintStream(out));
    ctrl.init();
    Assert.assertEquals(expected, out.toString().trim());
  }

  @Test
  public void testPortfolioPerformanceMonthInvalid() {
    String inputString = "2 res/testfiles/fileUploadFlex.csv 7 PORTFOLIOFLEX1 2 2020 15"
            + " 2020 2 2022 6 iop ";
    InputStream in = new ByteArrayInputStream(inputString.getBytes());

    String performance = "Performance of portfolio Test from 2020-02-01 to 2022-06-30\n"
            + "2020-02-01 : ****************\n"
            + "2020-03-02 : *********\n"
            + "2020-04-01 : \n"
            + "2020-05-01 : ********\n"
            + "2020-05-31 : ***********\n"
            + "2020-06-30 : ***********\n"
            + "2020-07-30 : ***************\n"
            + "2020-08-29 : ********************\n"
            + "2020-09-28 : **********\n"
            + "2020-10-28 : ************\n"
            + "2020-11-27 : *******************\n"
            + "2020-12-27 : ******************\n"
            + "2021-01-26 : ***********************\n"
            + "2021-02-25 : *************************\n"
            + "2021-03-27 : *************************\n"
            + "2021-04-26 : *********************************\n"
            + "2021-05-26 : *************************************\n"
            + "2021-06-25 : ****************************************\n"
            + "2021-07-25 : **********************************************\n"
            + "2021-08-24 : ************************************************\n"
            + "2021-09-23 : ************************************************\n"
            + "2021-10-23 : **********************************************\n"
            + "2021-11-22 : **************************************************\n"
            + "2021-12-22 : **************************************************\n"
            + "2022-01-21 : *****************************************\n"
            + "2022-02-20 : ***************************************\n"
            + "2022-03-22 : *********************************************\n"
            + "2022-04-21 : ************************************\n"
            + "2022-05-21 : ****************************\n"
            + "2022-06-30 : ***************************\n"
            + "Scale: * = $464\n"
            + "Base Amount = $16149.30\n";

    String expected = StringTestHelper.INIT_MENU
            + StringTestHelper.EXTERNAL_FILE_PATH
            + StringTestHelper.FILE_UPLOAD_SUCCESS
            + StringTestHelper.MAIN_MENU + StringTestHelper.PORTFOLIO_NAME
            + StringTestHelper.PERFORMANCE_PERIOD + "Enter start year : (YYYY)\n"
            + "Enter start month : (MM)\n"
            + "Invalid value for MonthOfYear (valid values 1 - 12): 15\n"
            + StringTestHelper.PERFORMANCE_START_END_MONTH
            + performance
            + StringTestHelper.MAIN_MENU + StringTestHelper.EXIT;

    MainController ctrl = new MainControllerImpl(model, view, in, new PrintStream(out));
    ctrl.init();
    Assert.assertEquals(expected, out.toString().trim());
  }

  @Test
  public void testPortfolioPerformanceDayInvalid() {
    String inputString = "2 res/testfiles/fileUploadFlex.csv 7 P1 PORTFOLIOFLEX1 j 2019 10 34"
            + " 2019 10 01 2019 10 30 iop ";
    InputStream in = new ByteArrayInputStream(inputString.getBytes());

    String performance = "Performance of portfolio Test from 2019-10-01 to 2019-10-30\n"
            + "2019-10-01 : ************\n"
            + "2019-10-02 : \n"
            + "2019-10-03 : ******\n"
            + "2019-10-04 : ***************\n"
            + "2019-10-05 : ***************\n"
            + "2019-10-06 : ***************\n"
            + "2019-10-07 : **************\n"
            + "2019-10-08 : ******\n"
            + "2019-10-09 : ************\n"
            + "2019-10-10 : ***************\n"
            + "2019-10-11 : *******************\n"
            + "2019-10-12 : *******************\n"
            + "2019-10-13 : *******************\n"
            + "2019-10-14 : *******************\n"
            + "2019-10-15 : *******************************\n"
            + "2019-10-16 : ********************************\n"
            + "2019-10-17 : ************************************\n"
            + "2019-10-18 : *******************************\n"
            + "2019-10-19 : *******************************\n"
            + "2019-10-20 : *******************************\n"
            + "2019-10-21 : *********************************\n"
            + "2019-10-22 : *****************************\n"
            + "2019-10-23 : *************************************\n"
            + "2019-10-24 : **************************************\n"
            + "2019-10-25 : ****************************************\n"
            + "2019-10-26 : ****************************************\n"
            + "2019-10-27 : ****************************************\n"
            + "2019-10-28 : **************************************************\n"
            + "2019-10-30 : **************************************\n"
            + "Scale: * = $30\n"
            + "Base Amount = $15690.96\n";


    String expected = StringTestHelper.INIT_MENU + StringTestHelper.EXTERNAL_FILE_PATH
            + StringTestHelper.FILE_UPLOAD_SUCCESS
            + StringTestHelper.MAIN_MENU + StringTestHelper.PORTFOLIO_NAME
            + StringTestHelper.CANNOT_UPDATE + StringTestHelper.PORTFOLIO_NAME
            + StringTestHelper.PERFORMANCE_PERIOD + "Enter start year : (YYYY)\n"
            + "Enter start month : (MM)\nEnter start day : (DD)\n"
            + "Invalid value for DayOfMonth (valid values 1 - 28/31): 34\n"
            + StringTestHelper.PERFORMANCE_START_END
            + performance + StringTestHelper.MAIN_MENU
            + StringTestHelper.EXIT;

    MainController ctrl = new MainControllerImpl(model, view, in, new PrintStream(out));
    ctrl.init();
    Assert.assertEquals(expected, out.toString().trim());
  }

  @Test
  public void testPortfolioPerformanceInflex() {
    String inputString = "2 res/testfiles/fileUploadFlex.csv 7 P1 PORTFOLIOFLEX1 j 2019 10 20"
            + " 2019 10 30 iop ";
    InputStream in = new ByteArrayInputStream(inputString.getBytes());

    String performance = "Performance of portfolio PORTFOLIOFLEX1 from 2019-10-01 to 2019-10-30\n"
            + "2019-10-01 : ********************************************************************"
            + "**********************************************************************************"
            + "***********************************************************************************"
            + "*********************************************************************************"
            + "**********************************************************************************"
            + "***********************************************************************************"
            + "********************************************************\n"
            + "2019-10-02 : **********************************************************************"
            + "************************************************************************************"
            + "***********************************************************************************"
            + "***********************************************************************************"
            + "***********************************************************************************"
            + "**********************************************************************************"
            + "***************************************\n"
            + "2019-10-03 : *********************************************************************"
            + "***********************************************************************************"
            + "**********************************************************************************"
            + "***********************************************************************************"
            + "***********************************************************************************"
            + "***********************************************************************************"
            + "**********************************************\n"
            + "2019-10-04 : *********************************************************************"
            + "*********************************************************************************"
            + "**********************************************************************************"
            + "**********************************************************************************"
            + "**********************************************************************************"
            + "********************************************************************************"
            + "**************************************************************\n"
            + "2019-10-05 : ********************************************************************"
            + "**********************************************************************************"
            + "**********************************************************************************"
            + "**********************************************************************************"
            + "**********************************************************************************"
            + "********************************************************************************"
            + "*************************************************************\n"
            + "2019-10-06 : **********************************************************************"
            + "**********************************************************************************"
            + "**********************************************************************************"
            + "**********************************************************************************"
            + "**********************************************************************************"
            + "**********************************************************************************"
            + "*********************************************************\n"
            + "2019-10-07 : ********************************************************************"
            + "**********************************************************************************"
            + "*******************************************************************************"
            + "**********************************************************************************"
            + "**********************************************************************************"
            + "**********************************************************************************"
            + "**************************************************************\n"
            + "2019-10-08 : ********************************************************************"
            + "**********************************************************************************"
            + "*********************************************************************************"
            + "*********************************************************************************"
            + "*********************************************************************************"
            + "*********************************************************************************"
            + "*******************************************************\n"
            + "2019-10-09 : *********************************************************************"
            + "***********************************************************************************"
            + "*********************************************************************************"
            + "*********************************************************************************"
            + "********************************************************************************"
            + "*********************************************************************************"
            + "************************************************************\n"
            + "2019-10-10 : ********************************************************************"
            + "**********************************************************************************"
            + "*********************************************************************************"
            + "*********************************************************************************"
            + "**********************************************************************************"
            + "*********************************************************************************"
            + "***************************************************************\n"
            + "2019-10-11 : *********************************************************************"
            + "***********************************************************************************"
            + "********************************************************************************"
            + "*********************************************************************************"
            + "*********************************************************************************"
            + "*********************************************************************************"
            + "*******************************************************************\n"
            + "2019-10-12 : ********************************************************************"
            + "***********************************************************************************"
            + "*********************************************************************************"
            + "**********************************************************************************"
            + "**********************************************************************************"
            + "**********************************************************************************"
            + "****************************************************************\n"
            + "2019-10-13 : ********************************************************************"
            + "*********************************************************************************"
            + "**********************************************************************************"
            + "**********************************************************************************"
            + "*********************************************************************************"
            + "********************************************************************************"
            + "********************************************************************\n"
            + "2019-10-14 : *********************************************************************"
            + "**********************************************************************************"
            + "*********************************************************************************"
            + "*********************************************************************************"
            + "********************************************************************************"
            + "**********************************************************************************"
            + "*******************************************************************\n"
            + "2019-10-15 : ********************************************************************"
            + "**********************************************************************************"
            + "*********************************************************************************"
            + "*********************************************************************************"
            + "*********************************************************************************"
            + "*********************************************************************************"
            + "********************************************************************************\n"
            + "2019-10-16 : ********************************************************************"
            + "*********************************************************************************"
            + "********************************************************************************"
            + "*********************************************************************************"
            + "**********************************************************************************"
            + "********************************************************************************"
            + "*********************************************************************************"
            + "**\n"
            + "2019-10-17 : *******************************************************************"
            + "*******************************************************************************"
            + "********************************************************************************"
            + "********************************************************************************"
            + "********************************************************************************"
            + "*******************************************************************************"
            + "******************************************************************************"
            + "****************\n"
            + "2019-10-18 : *****************************************************************"
            + "*******************************************************************************"
            + "*******************************************************************************"
            + "********************************************************************************"
            + "*******************************************************************************"
            + "******************************************************************************"
            + "******************************************************************************"
            + "****************\n"
            + "2019-10-19 : ****************************************************************"
            + "********************************************************************************"
            + "*******************************************************************************"
            + "******************************************************************************"
            + "******************************************************************************"
            + "******************************************************************************"
            + "*****************************************************************************"
            + "********************\n"
            + "2019-10-20 : ****************************************************************"
            + "********************************************************************************"
            + "*******************************************************************************"
            + "********************************************************************************"
            + "*********************************************************************************"
            + "*********************************************************************************"
            + "*********************************************************************************"
            + "********\n"
            + "2019-10-21 : ******************************************************************"
            + "**********************************************************************************"
            + "********************************************************************************"
            + "**********************************************************************************"
            + "*********************************************************************************"
            + "*********************************************************************************"
            + "*******************************************************************************"
            + "*****\n"
            + "2019-10-22 : ********************************************************************"
            + "***********************************************************************************"
            + "**********************************************************************************"
            + "**********************************************************************************"
            + "*******************************************************************************"
            + "*******************************************************************************"
            + "*******************************************************************************\n"
            + "2019-10-23 : ******************************************************************"
            + "*******************************************************************************"
            + "*********************************************************************************"
            + "**********************************************************************************"
            + "*********************************************************************************"
            + "*********************************************************************************"
            + "*********************************************************************************"
            + "*********\n"
            + "2019-10-24 : ********************************************************************"
            + "*********************************************************************************"
            + "*********************************************************************************"
            + "********************************************************************************"
            + "********************************************************************************"
            + "********************************************************************************"
            + "*******************************************************************************"
            + "***********\n"
            + "2019-10-25 : ********************************************************************"
            + "********************************************************************************"
            + "*******************************************************************************"
            + "**********************************************************************************"
            + "*********************************************************************************"
            + "***********************************************************************************"
            + "***********************************************************************************"
            + "*******\n"
            + "2019-10-26 : *********************************************************************"
            + "**********************************************************************************"
            + "**********************************************************************************"
            + "**********************************************************************************"
            + "**********************************************************************************"
            + "********************************************************************************"
            + "*********************************************************************************"
            + "*****\n"
            + "2019-10-27 : ********************************************************************"
            + "*********************************************************************************"
            + "********************************************************************************"
            + "******************************************************************************"
            + "**********************************************************************************"
            + "*********************************************************************************"
            + "*******************************************************************************"
            + "**************\n"
            + "2019-10-28 : *******************************************************************"
            + "**********************************************************************************"
            + "*********************************************************************************"
            + "*********************************************************************************"
            + "*********************************************************************************"
            + "**********************************************************************************"
            + "********************************************************************************"
            + "*******************\n"
            + "2019-10-29 : ********************************************************************"
            + "*********************************************************************************"
            + "*********************************************************************************"
            + "*********************************************************************************"
            + "*********************************************************************************"
            + "*********************************************************************************"
            + "*********************************************************************************"
            + "********\n"
            + "2019-10-30 : *********************************************************************"
            + "***********************************************************************************"
            + "********************************************************************************"
            + "********************************************************************************"
            + "********************************************************************************"
            + "*********************************************************************************"
            + "********************************************************************************"
            + "********\n"
            + "Scale: * = $30.0\nBase Amount = $15690.961\n";

    String expected = StringTestHelper.INIT_MENU + StringTestHelper.EXTERNAL_FILE_PATH
            + StringTestHelper.FILE_UPLOAD_SUCCESS
            + StringTestHelper.MAIN_MENU + StringTestHelper.PORTFOLIO_NAME
            + StringTestHelper.CANNOT_UPDATE + StringTestHelper.PORTFOLIO_NAME
            + StringTestHelper.PERFORMANCE_PERIOD + StringTestHelper.PERFORMANCE_START_END
            + performance + StringTestHelper.MAIN_MENU
            + StringTestHelper.EXIT;

    MainController ctrl = new MainControllerImpl(model, view, in, new PrintStream(out));
    ctrl.init();
    Assert.assertEquals(expected, out.toString().trim());
  }

  @Test
  public void testCostBasis() {
    String inputString = "2 res/testfiles/fileUploadFlex.csv 6 PORTFOLIOFLEX1 2020-10-09 j ";
    InputStream in = new ByteArrayInputStream(inputString.getBytes());
    String costBasis = "Enter the Date (YYYY-MM-DD) you want to check the Portfolio Cost Basis"
            + " on: \nCost Basis for portfolio PORTFOLIOFLEX1 on 2020-10-09 is 19516.31 USD.\n";

    String expected = StringTestHelper.INIT_MENU
            + StringTestHelper.EXTERNAL_FILE_PATH
            + StringTestHelper.FILE_UPLOAD_SUCCESS
            + StringTestHelper.MAIN_MENU + StringTestHelper.PORTFOLIO_NAME
            + costBasis + StringTestHelper.MAIN_MENU + StringTestHelper.EXIT;

    MainController ctrl = new MainControllerImpl(model, view, in, new PrintStream(out));
    ctrl.init();
    Assert.assertEquals(expected, out.toString().trim());
  }

  @Test
  public void testCostBasisInflex() {
    String inputString = "2 res/testfiles/fileUploadFlex.csv 6 P1 6 PORTFOLIOFLEX1 j ";
    InputStream in = new ByteArrayInputStream(inputString.getBytes());
    String costBasis = "Cost Basis for portfolio PORTFOLIOFLEX1 is 22115.27 USD.\n";

    String expected = StringTestHelper.INIT_MENU
            + StringTestHelper.EXTERNAL_FILE_PATH
            + StringTestHelper.FILE_UPLOAD_SUCCESS
            + StringTestHelper.MAIN_MENU + StringTestHelper.PORTFOLIO_NAME
            + StringTestHelper.CANNOT_CALCULATE_COSTBASIS
            + StringTestHelper.MAIN_MENU + StringTestHelper.PORTFOLIO_NAME
            + costBasis + StringTestHelper.MAIN_MENU + StringTestHelper.EXIT;

    MainController ctrl = new MainControllerImpl(model, view, in, new PrintStream(out));
    ctrl.init();
    Assert.assertEquals(expected, out.toString().trim());
  }

}
