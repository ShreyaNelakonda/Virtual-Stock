import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import controller.MainController;
import controller.MainControllerImpl;
import model.UserModel;
import model.UserModelImpl;
import view.TextView;
import view.TextViewConsoleImpl;

/**
 * A JUnit test class for the MainControllerImpl class.
 */
public class MainControllerImplTest {
  private final ByteArrayOutputStream out = new ByteArrayOutputStream();

  @Before
  public void setUp() {
    System.setOut(new PrintStream(out));
  }

  @Test
  public void testGo() {
    UserModel model = new UserModelImpl(null, null, null);
    TextView view = new TextViewConsoleImpl();
    String expected = StringTestHelper.INIT_MENU + StringTestHelper.USER_CREATED
            + StringTestHelper.MAIN_MENU + StringTestHelper.EXIT;
    String inputString = "3 lk ";
    InputStream in = new ByteArrayInputStream(inputString.getBytes());
    MainController ctrl = new MainControllerImpl(model, view, in, new PrintStream(out));
    ctrl.init();
    Assert.assertEquals(expected, out.toString().trim());
  }

  @Test
  public void testLoadExternalProfile() {
    StringBuilder mockLog = new StringBuilder();
    UserModel userModel = new UserControllerImplTest.MockUserModel(mockLog);
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
}
