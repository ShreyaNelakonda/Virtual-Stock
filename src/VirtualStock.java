import javax.swing.JFrame;

import controller.MainController;
import controller.MainControllerGuiImpl;
import controller.MainControllerImpl;
import model.UserModel;
import model.UserModelImpl;
import view.GuiView;
import view.TextView;
import view.TextViewConsoleImpl;
import view.GuiViewSwingImpl;

/**
 * Main class that setups environment and initializes the app.
 */
public class VirtualStock {

  /**
   * Main method that gets invoked first.
   * It initializes view and model for the controller
   * and passes the control to the main controller.
   *
   * @param args command line arguments
   */
  public static void main(String[] args) {
    try {
      MainController ctrl;
      UserModel model = new UserModelImpl(null, null, null);
      switch (args[0]) {
        case "text":
          TextView view = new TextViewConsoleImpl();
          ctrl = new MainControllerImpl(model, view, System.in, System.out);
          break;
        case "gui":
          GuiViewSwingImpl.setDefaultLookAndFeelDecorated(false);
          GuiView frame = new GuiViewSwingImpl();
          frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
          frame.setVisible(true);
          ctrl = new MainControllerGuiImpl(model, frame);
          break;
        default:
          throw new IllegalArgumentException("Invalid input argument.");
      }
      ctrl.init();
    } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException e) {
      System.out.println(" ERROR : Invalid input argument. "
              + "Rerun with either text or gui as first argument.");
    }
  }
}
