package controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.time.DateTimeException;
import java.util.Scanner;

import model.UserModel;
import service.stocks.StockCSVServiceImpl;
import service.stocks.StocksDataService;
import view.TextView;
import viewmodel.UserViewModel;
import viewmodel.UserViewModelImpl;

/**
 * Main Controller Implementation that implements methods of MainController.
 */
public class MainControllerImpl implements MainController {

  private final PrintStream out;
  private final UserModel userModel;
  private final TextView view;

  private final Scanner sc;
  private final UserViewModel viewModel;

  private final StocksDataService service = StockCSVServiceImpl.getInstance();

  /**
   * Generates MainView, UserModel, UserViewModel and sets input/output stream.
   *
   * @param userModel user model object
   * @param view view to display
   * @param in   input stream
   * @param out  output stream
   */
  public MainControllerImpl(UserModel userModel, TextView view, InputStream in, PrintStream out) {
    this.userModel = userModel;
    this.viewModel = new UserViewModelImpl(this.userModel);
    this.view = view;
    this.out = out;
    this.sc = new Scanner(in);
  }

  @Override
  public void init() {
    service.clearStockDatastore();
    while (true) {
      view.displayInitProfile(this.out);

      int inOpt = getInputOption();
      if (inOpt == 1) {
        if (loadUserFromPath(null)) {
          view.fileLoadSuccess("profile", out);
          break;
        }
      } else if (inOpt == 2) {
        view.getFilepath(out);
        String filepath = sc.next();
        if (loadUserFromPath(filepath)) {
          view.fileLoadSuccess("external", out);
          break;
        }
      } else if (inOpt == 3) {
        if (createNewUser()) {
          break;
        }
      } else {
        view.displayInputError(this.out);
      }
    }
    this.main();
  }

  /**
   * Method to display a "goodbye" message and exit the application.
   */
  private void exitApplication() {
    view.displayExit(out);
    service.clearStockDatastore();
  }

  @Override
  public void main() {
    boolean run = true;
    while (run) {
      view.displayMainMenuCommonOption(out);
      int input = getInputOption();
      run = checkInput(input);
    }
  }

  /**
   * A private method that transfers control to respective controller based on user input.
   *
   * @param inOpt option selected by end-user
   */
  private boolean checkInput(int inOpt) {
    boolean displayMainMenu = true;
    boolean portfolioFlexType;
    switch (inOpt) {
      case 1:
        view.displayChoosePortfolioType(out);
        int input = getInputOption();
        portfolioFlexType = (input == 1);
        if (portfolioFlexType) {
          new PortfolioControllerImpl().createFlexPortfolio(this.userModel, this.sc, this.out);
        } else {
          new PortfolioControllerImpl().createInflexPortfolio(this.userModel, this.sc, this.out);
        }
        break;
      case 2:
        new UserControllerImpl().getPortfolios(this.viewModel, this.out);
        break;
      case 3:
        new PortfolioControllerImpl().checkPortfolioComposition(
                this.userModel, this.sc, this.out);
        break;
      case 4:
        new PortfolioControllerImpl().getPortfolioValue(this.userModel,
                this.sc, this.out);
        break;
      case 5:
        new PortfolioControllerImpl().updateFlexPortfolio(this.userModel,
                this.sc, this.out, this.view);
        break;
      case 6:
        new PortfolioControllerImpl().getCostBasis(this.userModel,
                this.sc, this.out);
        break;
      case 7:
        new PortfolioControllerImpl().portfolioPerformance(this.userModel,
                this.sc, this.out, this.view);
        break;
      default:
        exitApplication();
        return false;
    }
    return displayMainMenu;
  }

  /**
   * Loads user profile from the file location mentioned.
   *
   * @param filePath location from which file needs to be loaded. if null, it loads from data store.
   * @return true if file is loaded successful, otherwise false
   */
  private boolean loadUserFromPath(String filePath) {
    try {
      this.userModel.loadUser(filePath);
    } catch (NumberFormatException e) {
      view.errorFractionalShares(this.out);
      return false;
    } catch (IllegalArgumentException e) {
      if (e.getMessage().contains("Quantity should be greater than 0")) {
        view.displayQuantityLessZero(this.out);
      } else if (e.getMessage().contains("Commission Fee should be greater than 0.")) {
        view.displayFileUploadInvalidData("Commission Fee", this.out);
      } else if (e.getMessage().contains("The date should not be a future date.")) {
        view.futureDate(this.out);
      } else if (e.getMessage().contains("Stock data not available on")) {
        view.displayFileUploadInvalidData("Transaction Date", this.out);
      } else {
        view.errorInvalidFile(this.out);
      }
      return false;
    } catch (DateTimeException e) {
      view.displayFileUploadInvalidData("Date Format", this.out);
      return false;
    } catch (Exception e) {
      view.errorInvalidFile(this.out);
      return false;
    }
    return true;
  }

  /**
   * Initializes new user in the app.
   * Creates an object & clears saved portfolios.
   *
   * @return true is user is created otherwise false
   */
  private boolean createNewUser() {
    try {
      this.userModel.createUser();
      view.displayUserCreated(this.out);
      return true;
    } catch (IOException e) {
      view.displayExceptionMessage(this.out, e.getMessage());
    } catch (Exception e) {
      view.displayExceptionMessage(this.out, "Something went wrong.");
    }
    return false;
  }

  /**
   * Method to fetch user input for the init and main menu options.
   * It reads an integer value starting at 0.
   * Negative numbers are not allowed as menu options.
   * If user enters non-integer then it sets it to -1, which invalid.
   *
   * @return option entered by the user if opt >= 0, otherwise it returns negative number
   */
  private int getInputOption() {
    int opt;
    try {
      opt = Integer.parseInt(sc.next());
    } catch (IllegalArgumentException i) {
      opt = -1;
    }
    return opt;
  }
}
