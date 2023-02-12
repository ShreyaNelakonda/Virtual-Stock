/**
 * A JUnit helper test class for creating constants to be called for the view test.
 */
public final class StringTestHelper {
  public static final String GET_PORTFOLIO = "getPortfolio() called.";
  public static final String ADD_PORTFOLIO = "addPortfolio() called.";

  public static final String TXN_DATE = "Enter transaction date (YYYY-MM-DD) :\n";
  public static final String PTYPE_SELECT = "Select type of portfolio : \n"
          + " Enter 1 \t : Flexible Portfolio.\n"
          + " Enter Any \t : Inflexible Portfolio.\n";

  public static final String ADD_TXN_MORE = "Do you want to add this transaction to your "
          + "portfolio?\n"
          + "Enter 1 : to save.\n"
          + "Enter any : to go back to main menu\n"
          + "Stock added!\n"
          + "Do you want to add more transaction to your portfolio?\n"
          + "Enter 1 : To add more transaction.\n"
          + "Any key : To go back to main menu.\n";
  public static final String TXN_FEE = "Select type of transaction : \n"
          + " Enter 1 \t : Buy \n"
          + " Enter Any \t : Sell \n"
          + "Enter Quantity: \n"
          + "Enter commission fee for this transaction : \n";
  public static final String INIT_MENU =
          "How to load your profile?\n"
                  + " Enter 1 : to load a previous profile.\n"
                  + " Enter 2 : to load an external profile.\n"
                  + " Enter 3 : to start with a new profile.\n";

  public static final String MAIN_MENU = "Main Menu\n" + " Enter 1 \t : to create portfolio.\n"
          + " Enter 2 \t : to view all portfolios.\n"
          + " Enter 3 \t : to check portfolio composition.\n"
          + " Enter 4 \t : to view the price of a portfolio on a certain date.\n"
          + " Enter 5 \t : to purchase/sell shares in existing portfolio.\n"
          + " Enter 6 \t : to compute cost basis.\n"
          + " Enter 7 \t : to check portfolio performance.\n"
          + " Enter any \t : to exit App.\n";

  public static final String USER_CREATED = "Your profile is created.\n";

  public static final String PORTFOLIO_NAME = "Enter portfolio name : \n";

  public static final String EXIT = "Good Bye!";

  public static final String FILE_LOAD_ERROR = "File format is invalid or it does not exist. "
          + "Please load data from a valid external file or create portfolio manually.\n";

  public static final String EXTERNAL_FILE_PATH = "Enter the filepath in .csv for the "
          + "list of portfolios to be loaded: \n";

  public static final String FILE_UPLOAD_SUCCESS = "File uploaded successfully\n";

  public static final String P_NAME_EXISTS = "Portfolio name already exists. Please try again.\n";

  public static final String P_NAME_FORMAT = "Portfolio name must contain alphanumeric "
          + "characters only. Please try again.\n";

  public static final String ENTER_TICKER_SYM = "Enter ticker symbol : \n";

  public static final String INVALID_TICKER = "Ticker data does not exist. Try again.\n";

  public static final String ENTER_QTY = "Enter Quantity: \n";

  public static final String QTY_ERROR = "Invalid value. Must be Integer > 0.\n";

  public static final String ADD_MORE_STOCK = "Do you want to add more stock to your portfolio?"
          + "\nEnter 1 : To add more stock.\nAny key : To view portfolio & save.\n";

  public static final String SAVE_PORTFOLIO = "Do you want to save your portfolio?"
          + "\nEnter 1 : to save.\nEnter any : not to save.\n";

  public static final String SAVE_PORTFOLIO_SUCCESS = "Portfolio is saved successfully.\n";

  public static final String NO_PORTFOLIO = "No portfolios found.\n";

  public static final String P_DATE = "Enter the Date (YYYY-MM-DD) "
          + "you want to check the Portfolio Value on: \n";

  public static final String NO_PORTFOLIO_FOUND = "No portfolios found.\n";

  public static final String PORTFOLIO_NAME_NOT_FOUND = "There is no portfolio present with the "
          + "name: ";

  public static final String ENTER_DATE = "Enter the Date (YYYY-MM-DD) you want to check the "
          + "Portfolio Value on: \n";

  public static final String INVALID_DATE_FORMAT = "Format of the date is invalid. "
          + "Please try again.\n";

  public static final String FUTURE_DATE = "The date should not be a future date. "
          + "Please try again.\n";

  public static final String PROFILE_LOAD_SUCCESS = "Profile loaded successfully.\n";

  public static final String ERROR_FRACTIONAL_SHARE = "Some of the data in the file is invalid. \n"
          + "User can only buy whole shares. Please update the quantity and try again.\n";

  public static final String ERROR_QUANTITY_NEGATIVE = "Some of the data in the file is invalid. \n"
          + "Quantity must always be greater than 0. Please update the quantity and try again.\n";

  public static final String INCORRECT_INPUT = "Incorrect input! Please try again.\n";

  public static final String DATE_COMPOSITION = "Enter the Date (YYYY-MM-DD) you want "
          + "to check the Portfolio Composition on: \n";


  public static final String CANNOT_UPDATE = "Cannot update inflexible portfolio. Try again.\n";

  public static final String PERFORMANCE_PERIOD = "Enter 1 : year\n"
          + "Enter 2 : month\nEnter any : days\n";

  public static final String PERFORMANCE_START_END = "Enter start year : (YYYY)\n"
          + "Enter start month : (MM)\nEnter start day : (DD)\nEnter end year : (YYYY)\n"
          + "Enter end month : (MM)\nEnter end day : (DD)\n";

  public static final String PERFORMANCE_START_END_YEAR = "Enter start year : (YYYY)\n"
          + "Enter end year : (YYYY)\n";

  public static final String PERFORMANCE_START_END_MONTH = "Enter start year : (YYYY)\n"
          + "Enter start month : (MM)\nEnter end year : (YYYY)\n"
          + "Enter end month : (MM)\n";

  public static final String CANNOT_CALCULATE_COSTBASIS = "Cost Basis can only be calculated "
          + "for flexible portfolios. Please try again.\n";

}
