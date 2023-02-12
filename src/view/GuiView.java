package view;

import java.util.List;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTable;
import controller.GuiFeatures;
import dto.TimeStampBinsDTO;

/**
 * View for Graphical User Interface to display various output messages to the user based on
 * operations.
 */
public interface GuiView {

  /**
   * Method to display initial profile loading options.
   */
  void displayInitProfile();

  /**
   * Panel for the main menu options.
   *
   * @return panel for the main menu options
   */
  JPanel getMainPanel();

  /**
   * Method to pass the input values from the gui view to the controller.
   *
   * @param features GuiFeatures interface
   */
  void addFeatures(GuiFeatures features);

  /**
   * Panel for the status which displays the details for the selected main menu option.
   *
   * @return panel for displaying the details of the selected main menu option
   */
  JPanel getStatusPanel();

  /**
   * Method to display a dialog box with a message.
   *
   * @param message message to be displayed in the dialog box
   */
  void displayDialog(String message);

  /**
   * Method to display Main Menu options.
   */
  void displayMainMenu();

  /**
   * Method to choose the filepath for loading an external file.
   */
  void displayExternalFileInput();

  /**
   * Method to display the list of portfolios associated with the user.
   *
   * @param inflex details of inflexible portfolios associated with the user
   * @param flex   details of flexible portfolios associated with the user
   */
  void displayPortfolioList(String[][] inflex, String[][] flex);

  /**
   * Method to get the portfolio name text field.
   *
   * @return name of the portfolio
   */
  JTextField getpNameText();

  /**
   * Method to create portfolios.
   */
  void displayPortfolioCreation();

  /**
   * Method to display the composition of a portfolio on a certain date.
   */
  void displayPortfolioComposition();

  /**
   * Method to display the value of a portfolio on a certain date.
   */
  void displayPortfolioValue();


  /**
   * Method to display the cost basis of a portfolio on a certain date.
   */
  void displayCostBasis();

  /**
   * Method to display the screen for creating a new dollar cost averaging strategy.
   */
  void createDCAStrategy();

  /**
   * Method to get the label displaying the portfolio value on a certain date.
   *
   * @return portfolio value label
   */
  JLabel getPriceDetailLabel();

  /**
   * Method to set the label displaying the portfolio value on a certain date.
   *
   * @param priceDetails the price details of a portfolio which will be displayed by the view
   */
  void setPriceDetailLabel(String priceDetails);

  /**
   * Method to get a table which consists of the composition of stocks in a portfolio.
   *
   * @return table with the composition of of stocks in a portfolio
   */
  JTable getCompositionTable();

  /**
   * Method to display the calculated cost basis of a portfolio on a certain date.
   *
   * @param costBasis cost basis of a portfolio calculated on the given date
   */
  void displayCostBasisValue(String costBasis);

  /**
   * Method to display the value of a portfolio on a given date from the API.
   *
   * @param portfolioValue value retrieved from the API for the portfolio on the given date
   */
  void displayPortfolioValueLabel(String portfolioValue);

  /**
   * Method to clear the date text fields.
   */
  void clearDate();

  /**
   * Method to clear the values set from the components.
   */
  void clearLabels();

  /**
   * Displays gui to buy or sell new stocks in an exisiting portfolio.
   */
  void displayBuySellStocks();

  /**
   * Displays GUI to invest amount in an existing portfolio by specifying weights of how
   * much to invest in each stock.
   */
  void displayInvestAmount();

  /**
   * Displays table of stocks and weights to be invested in it.
   *
   * @param tickerSymbolSet array consisting stocks and its associated weights
   */
  void displayStockTicker(String[][] tickerSymbolSet);

  /**
   * Sets default operation on closing gui.
   *
   * @param exitOnClose type of operation on exit
   */
  void setDefaultCloseOperation(int exitOnClose);

  /**
   * Method to set the JFrame component visible.
   *
   * @param b true to make it visible, otherwise fales
   */
  void setVisible(boolean b);

  /**
   * Method to set time range to get fetch performance of the mentioned portfolio.
   */
  void displayGetPerformance();

  /**
   * Displays performance bar chart based on the given values.
   *
   * @param performanceVal   value of the performance
   * @param timeStampBinsDTO date of performance to be displayed
   */
  void displayPerformanceChart(List<Float> performanceVal,
                               TimeStampBinsDTO timeStampBinsDTO);
}
