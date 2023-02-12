package view;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JSplitPane;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.BoxLayout;
import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.JRadioButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.ButtonGroup;

import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

import controller.GuiFeatures;
import dto.TimeStampBinsDTO;

/**
 * View Class for Graphical User Interface to display various output messages to the user based on
 * operations. This class implements GuiView Class.
 */

public class GuiViewSwingImpl extends JFrame implements GuiView {
  private final JSplitPane mainSplitPane;
  private final JPanel mainPanel;
  private final JPanel statusPanel;
  private final JButton proceedButton;
  private final JButton mainMenuButton;
  private final JButton fileLoadButton;
  private final JLabel priceDetailLabel;
  private final JButton btnCostBasis;
  private final JTextField portfolioName;
  private final JButton btnCheckComposition;
  private final JLabel costBasisLabel;
  private final JButton btnCheckValue;
  private final JLabel portfolioValueLabel;
  private final JButton btnGetPrice;
  private final JToggleButton tgBtnFlex;
  private final JTextField year;
  private final JTextField month;
  private final JTextField day;
  private final JTextField endYear;
  private final JTextField endMonth;
  private final JTextField endDay;
  private final JTextField investedAmountText;
  private final JTextField frequencyText;
  private final JTextField commissionFeeText;
  private final JButton btnAddPortfolioDCA;
  private final JButton addStockBtn;
  private final JButton btnAddStockCreate;
  private final JTextField quantityText;
  private final JTextField tickerText;
  private final JButton btnGetPriceDate;
  private final JButton getPortfolioDataBtn;
  private final JButton btnDisplayPerformance;
  private final JTable investStockTable;
  private final JButton investAmountPortfolioBtn;
  private final JTable dcaStockTable;
  private final Container conPerfromance;
  private String inputOpt;
  private JTextField pNameText;
  private JTable compositionTable;
  private String startDate;
  private String endDate;

  /**
   * Constructs a GUI View Swing Impl which opens a new window frame and sets the window details.
   */
  public GuiViewSwingImpl() {
    super();
    setTitle("Virtual Stock");
    //setSize(1000, 1000);
    setPreferredSize(new Dimension(1200, 800));
    setMinimumSize(getPreferredSize());
    setLocation(200, 0);

    mainPanel = new JPanel();
    JScrollPane mainScrollPanel = new JScrollPane(mainPanel);
    mainScrollPanel.setVisible(true);
    //mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
    //add(mainPanel);

    Border blackline = BorderFactory.createTitledBorder("Menu Option");
    statusPanel = new JPanel();
    statusPanel.setBorder(blackline);
    JScrollPane statusScrollPanel = new JScrollPane(statusPanel);
    statusScrollPanel.setVisible(true);
    //add(statusPanel);

    mainSplitPane = new JSplitPane();
    setLayout(new GridLayout());
    add(mainSplitPane);
    mainSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
    mainSplitPane.setTopComponent(mainScrollPanel);
    mainSplitPane.setBottomComponent(statusScrollPanel);

    proceedButton = new JButton("Proceed");
    mainMenuButton = new JButton("Proceed");
    fileLoadButton = new JButton("Load File");
    btnGetPrice = new JButton("View Ticker Price");
    priceDetailLabel = new JLabel();
    JButton btnAddPortfolio = new JButton();
    btnCheckComposition = new JButton("Display Composition");
    btnCostBasis = new JButton("Display Cost Basis");
    btnCheckValue = new JButton("Display Value");
    tgBtnFlex = new JToggleButton("Switch");

    year = new JTextField(6);
    month = new JTextField(4);
    day = new JTextField(4);
    portfolioName = new JTextField(10);

    costBasisLabel = new JLabel();
    portfolioValueLabel = new JLabel();

    endYear = new JTextField(6);
    endMonth = new JTextField(4);
    endDay = new JTextField(4);
    investedAmountText = new JTextField(10);
    frequencyText = new JTextField(10);
    commissionFeeText = new JTextField(10);

    btnAddPortfolioDCA = new JButton();
    addStockBtn = new JButton("Buy Stock");
    btnAddStockCreate = new JButton("Buy Stock");
    tickerText = new JTextField(10);
    quantityText = new JTextField(10);
    btnGetPriceDate = new JButton("View Ticker Price");

    getPortfolioDataBtn = new JButton("Get Stock Details");
    investStockTable = new JTable();
    btnDisplayPerformance = new JButton("Display");

    investAmountPortfolioBtn = new JButton("Invest in Portfolio");
    dcaStockTable = new JTable();
    conPerfromance = new Container();
    pack();
  }

  @Override
  public void addFeatures(GuiFeatures features) {
    proceedButton.addActionListener(evt -> features.getInitInput(inputOpt));
    fileLoadButton.addActionListener(evt -> features.getFilePath(inputOpt));
    mainMenuButton.addActionListener(evt -> features.getMainMenuInput(inputOpt));
    btnGetPrice.addActionListener(evt -> features.getTickerInput(inputOpt));
    btnAddStockCreate.addActionListener(evt -> features.createFlexPortfolio(pNameText.getText(),
            tickerText.getText(), year.getText(), month.getText(), day.getText(),
            quantityText.getText(), commissionFeeText.getText(), inputOpt));
    btnCostBasis.addActionListener(evt -> features.getCostBasis(inputOpt, year.getText(),
            month.getText(), day.getText()));
    btnCheckValue.addActionListener(evt -> features.getPortfolioValue(inputOpt, year.getText(),
            month.getText(), day.getText()));
    btnCheckComposition.addActionListener(evt -> features.getComposition(tgBtnFlex, inputOpt,
            year.getText(), month.getText(), day.getText()));
    btnAddPortfolioDCA.addActionListener(evt -> features.addDCAStrategy(dcaStockTable.getModel(),
            inputOpt, investedAmountText.getText(), commissionFeeText.getText(),
            frequencyText.getText(), startDate, endDate));
    addStockBtn.addActionListener(evt -> features.buySellStock(portfolioName.getText(),
            tickerText.getText(),
            year.getText(), month.getText(), day.getText(), quantityText.getText(),
            commissionFeeText.getText(), inputOpt));
    btnGetPriceDate.addActionListener(evt -> features.getTickerPriceDate(inputOpt,
            year.getText(), month.getText(), day.getText()));
    getPortfolioDataBtn.addActionListener(evt ->
            features.getPortfolioData(portfolioName.getText()));
    investAmountPortfolioBtn.addActionListener(evt ->
            features.investInPortfolio(portfolioName.getText(),
                    investedAmountText.getText(), commissionFeeText.getText(), year.getText(),
                    month.getText(), day.getText(), investStockTable.getModel()));
    btnDisplayPerformance.addActionListener(evt -> features.getPerformance(portfolioName.getText(),
            startDate, endDate));
  }

  @Override
  public void displayInitProfile() {
    inputOpt = "";
    mainSplitPane.getBottomComponent().setVisible(false);

    JPanel initPanel = new JPanel();
    mainPanel.add(initPanel);
    initPanel.setLayout(new BoxLayout(initPanel, BoxLayout.PAGE_AXIS));
    initPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

    JLabel radioDisplay = new JLabel("How to load your profile?");
    ButtonGroup rLoadUserGroup = new ButtonGroup();
    JRadioButton[] radioButtons = new JRadioButton[3];

    radioButtons[0] = new JRadioButton("Load a previous profile");
    radioButtons[1] = new JRadioButton("Load an external profile.");
    radioButtons[2] = new JRadioButton("Start with a new profile");
    initPanel.add(radioDisplay);

    for (int i = 0; i < radioButtons.length; i++) {
      rLoadUserGroup.add(radioButtons[i]);
      initPanel.add(radioButtons[i]);
    }
    initPanel.add(proceedButton);

    proceedButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        for (int i = 0; i < radioButtons.length; i++) {
          if (radioButtons[i].isSelected()) {
            inputOpt = i + 1 + "";
          }
        }
      }
    });
    initPanel.setVisible(true);
    pack();
  }

  @Override
  public void displayMainMenu() {
    inputOpt = "";
    mainSplitPane.setDividerLocation(350);
    mainSplitPane.getBottomComponent().setVisible(true);

    JPanel mainMenu = new JPanel();
    mainMenu.setLayout(new BoxLayout(mainMenu, BoxLayout.PAGE_AXIS));
    mainMenu.setAlignmentX(Component.CENTER_ALIGNMENT);

    JLabel menuDisplay = new JLabel("Main Menu");
    mainMenu.add(menuDisplay);

    ButtonGroup rLoadUserGroup = new ButtonGroup();
    JRadioButton[] radioButtons = new JRadioButton[9];
    radioButtons[0] = new JRadioButton("create flexible portfolio");
    radioButtons[1] = new JRadioButton("view all portfolios");
    radioButtons[2] = new JRadioButton("check portfolio composition");
    radioButtons[3] = new JRadioButton("view the price of a portfolio on a certain date");
    radioButtons[4] = new JRadioButton("purchase/sell shares in existing portfolio");
    radioButtons[5] = new JRadioButton("invest amount in existing portfolio");
    radioButtons[6] = new JRadioButton("compute cost basis");
    radioButtons[7] = new JRadioButton("create dollar cost averaging strategy");
    radioButtons[8] = new JRadioButton("check portfolio performance");
    for (int i = 0; i < radioButtons.length; i++) {
      rLoadUserGroup.add(radioButtons[i]);
      mainMenu.add(radioButtons[i]);
    }

    mainMenu.add(mainMenuButton);
    mainMenuButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        for (int i = 0; i < radioButtons.length; i++) {
          if (radioButtons[i].isSelected()) {
            inputOpt = i + 1 + "";
          }
        }
      }
    });
    this.mainPanel.add(mainMenu);
    mainMenu.setVisible(true);

    pack();
  }

  @Override
  public void displayExternalFileInput() {
    inputOpt = null;
    JPanel fileopenPanel = new JPanel();
    fileopenPanel.setLayout(new BoxLayout(fileopenPanel, BoxLayout.PAGE_AXIS));

    mainPanel.add(fileopenPanel);
    JButton fileOpenButton = new JButton("Choose external file");
    fileOpenButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    fileopenPanel.add(fileOpenButton);

    JLabel fileOpenDisplay = new JLabel("File path will appear here");
    fileOpenDisplay.setAlignmentX(Component.CENTER_ALIGNMENT);
    fileopenPanel.add(fileOpenDisplay);

    fileLoadButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    fileopenPanel.add(fileLoadButton);

    fileOpenButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        final JFileChooser fchooser = new JFileChooser(".");
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "CSV Files", "csv");
        fchooser.setFileFilter(filter);
        int retvalue = fchooser.showOpenDialog(GuiViewSwingImpl.this);
        if (retvalue == JFileChooser.APPROVE_OPTION) {
          File f = fchooser.getSelectedFile();
          inputOpt = f.getAbsolutePath();
          fileOpenDisplay.setText(inputOpt);
        }
      }
    });

    fileopenPanel.setVisible(true);
  }

  @Override
  public void displayPortfolioList(String[][] inflex, String[][] flex) {
    JPanel commonPanel = new JPanel();
    JScrollPane flexScrollPane = new JScrollPane();
    JTable flexTable = new JTable();
    JScrollPane inflexScrollPane = new JScrollPane();
    JTable inflexTable = new JTable();

    //======== commonPanel ========
    {
      commonPanel.setLayout(new BoxLayout(commonPanel, BoxLayout.LINE_AXIS));

      //======== flexScrollPane ========
      {
        String[] header = {"#", "Portfolio Name"};
        String[][] flexData = {{"", "No flexible portfolios found."}};
        if (flex.length != 0) {
          flexData = flex;
        }
        DefaultTableModel model = new DefaultTableModel(flexData, header);
        flexTable.setModel(model);
        flexTable.setEnabled(false);
        flexTable.getColumnModel().getColumn(0).setMaxWidth(5);
        flexScrollPane.setViewportView(flexTable);
        flexScrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Flexible Portfolios",
                TitledBorder.CENTER, TitledBorder.TOP));
        flexScrollPane.setPreferredSize(new Dimension(statusPanel.getWidth() / 2,
                getHeight()));

      }
      commonPanel.add(flexScrollPane);

      //======== inflexScrollPane ========
      {
        String[] header = {"#", "Portfolio Name"};
        String[][] inflexData = {{"", "No inflexible portfolios found."}};
        if (inflex.length != 0) {
          inflexData = inflex;
        }
        DefaultTableModel model = new DefaultTableModel(inflexData, header);
        inflexTable.setModel(model);
        inflexTable.setEnabled(false);
        inflexTable.getColumnModel().getColumn(0).setMaxWidth(5);
        inflexScrollPane.setViewportView(inflexTable);
        inflexScrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Inflexible Portfolios",
                TitledBorder.CENTER, TitledBorder.TOP));
        inflexScrollPane.setPreferredSize(new Dimension(statusPanel.getWidth() / 2,
                getHeight()));
      }
      commonPanel.add(inflexScrollPane);
    }
    commonPanel.setVisible(true);
    statusPanel.add(commonPanel);
    pack();
  }

  @Override
  public void displayPortfolioCreation() {
    Container c = getStatusPanel();
    c.setLayout(new BoxLayout(c, BoxLayout.PAGE_AXIS));
    JLabel title = new JLabel("Create Flexible Portfolio");
    title.setAlignmentX(CENTER_ALIGNMENT);
    title.setFont(new Font("Arial", Font.BOLD, 15));
    title.setSize(300, 30);
    c.add(title);

    //Portfolio Name
    JPanel pNamePanel = new JPanel();
    JLabel pNameLabel = new JLabel("Portfolio Name");
    pNameText = new JTextField(20);
    pNameText.setMinimumSize(new Dimension(300, 25));
    pNamePanel.add(pNameLabel);
    pNamePanel.add(pNameText);
    c.add(pNamePanel);

    //Add stock
    JPanel stockPanel = new JPanel();
    stockPanel.setLayout(new BoxLayout(stockPanel, BoxLayout.PAGE_AXIS));
    Border blackline = BorderFactory.createTitledBorder("Add Stock details.");
    stockPanel.setBorder(blackline);
    c.add(stockPanel);

    JPanel tickerPanel = new JPanel();
    JLabel tickerSymbol = new JLabel();
    tickerSymbol.setText("Enter the Ticker Symbol of the stock: ");
    tickerPanel.add(tickerSymbol);
    tickerPanel.add(tickerText);
    stockPanel.add(tickerPanel);

    JPanel portfolioTransactionPanel = new JPanel();
    JLabel transactionDate = new JLabel();
    transactionDate.setText("Enter the Transaction Date: ");
    portfolioTransactionPanel.add(transactionDate);

    JLabel transactionYear = new JLabel();
    transactionYear.setText("YYYY");
    portfolioTransactionPanel.add(transactionYear);
    portfolioTransactionPanel.add(year);

    JLabel transactionMonth = new JLabel();
    transactionMonth.setText("MM");
    portfolioTransactionPanel.add(transactionMonth);
    portfolioTransactionPanel.add(month);

    JLabel transactionDay = new JLabel();
    transactionDay.setText("DD");
    portfolioTransactionPanel.add(transactionDay);
    portfolioTransactionPanel.add(day);

    stockPanel.add(portfolioTransactionPanel);


    JPanel detailsPanel = new JPanel();
    btnGetPriceDate.setText("View Ticker Price");
    detailsPanel.add(btnGetPriceDate);
    btnGetPriceDate.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        inputOpt = null;
        inputOpt = tickerText.getText();
      }
    });

    detailsPanel.add(priceDetailLabel);
    priceDetailLabel.setText("Ticker details will be displayed here");
    stockPanel.add(detailsPanel);

    JPanel quantityPanel = new JPanel();
    JLabel quantity = new JLabel();
    quantity.setText("Enter the quantity: ");
    quantityPanel.add(quantity);

    quantityPanel.add(quantityText);
    stockPanel.add(quantityPanel);

    JPanel commFeePanel = new JPanel();
    JLabel commFee = new JLabel();
    commFee.setText("Enter the commission fee: ");
    commFeePanel.add(commFee);

    commissionFeeText.setText("0");
    commFeePanel.add(commissionFeeText);
    stockPanel.add(commFeePanel);
    JLabel dollarSymbolC = new JLabel();
    dollarSymbolC.setText("USD");
    commFeePanel.add(dollarSymbolC);

    ((AbstractDocument) day.getDocument()).setDocumentFilter(
            getDocumentIntegerFilter(1));
    ((AbstractDocument) month.getDocument()).setDocumentFilter(
            getDocumentIntegerFilter(1));
    ((AbstractDocument) year.getDocument()).setDocumentFilter(
            getDocumentIntegerFilter(3));
    ((AbstractDocument) quantityText.getDocument()).setDocumentFilter(
            getDocumentIntegerFilter(1000000));
    ((AbstractDocument) commissionFeeText.getDocument()).setDocumentFilter(
            getDocumentFloatFilter());

    JPanel addStockPanel = new JPanel();
    JPanel buySellTogglePanel = new JPanel();
    final JToggleButton tgBtnSwitch = new JToggleButton("Switch");
    JLabel buySellLabel = new JLabel();
    buySellLabel.setText("Action: Buy");
    tgBtnSwitch.setSelected(false);
    tgBtnSwitch.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {

        if (tgBtnSwitch.isSelected()) {
          buySellLabel.setText("Action: Sell");
          btnAddStockCreate.setText("Sell Stock");
        } else {
          buySellLabel.setText("Action: Buy");
          btnAddStockCreate.setText("Buy Stock");
        }
      }
    });
    buySellTogglePanel.add(buySellLabel);
    buySellTogglePanel.add(tgBtnSwitch);
    stockPanel.add(buySellTogglePanel);

    addStockPanel.add(btnAddStockCreate);
    stockPanel.add(addStockPanel);

    btnAddStockCreate.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (pNameText.getText().isEmpty() || pNameText.getText().isBlank()) {
          pNameText.grabFocus();
        } else if (tickerText.getText().isEmpty() || tickerText.getText().isBlank()) {
          tickerText.grabFocus();
        } else if (year.getText().isEmpty() || year.getText().isBlank()) {
          year.grabFocus();
        } else if (month.getText().isEmpty() || month.getText().isBlank()) {
          month.grabFocus();
        } else if (day.getText().isEmpty() || day.getText().isBlank()) {
          day.grabFocus();
        } else if (quantityText.getText().isEmpty() || quantityText.getText().isBlank()) {
          quantityText.grabFocus();
        } else {
          inputOpt = String.valueOf(tgBtnSwitch.isSelected());
        }
      }
    });
    pack();
  }

  @Override
  public void displayPortfolioComposition() {
    inputOpt = null;
    Container c = getStatusPanel();
    c.setLayout(new BoxLayout(c, BoxLayout.PAGE_AXIS));
    JPanel titlePanel = new JPanel();
    JLabel title = new JLabel("Portfolio Composition");
    title.setFont(new Font("Arial", Font.BOLD, 15));
    //title.setSize(300, 30);
    titlePanel.add(title);
    c.add(titlePanel);

    JPanel portfolioNamePanel = new JPanel();
    JLabel portName = new JLabel();
    portName.setText("Enter Portfolio Name: ");
    JTextField portfolioName = new JTextField(10);
    portfolioNamePanel.add(portName);
    portfolioNamePanel.add(portfolioName);
    c.add(portfolioNamePanel);

    JPanel portfolioDatePanel = new JPanel();

    JPanel flexTogglePanel = new JPanel();
    JLabel flexStatusLabel = new JLabel();
    flexStatusLabel.setText("Type : Inflexible");
    tgBtnFlex.setSelected(false);
    portfolioDatePanel.setVisible(false);
    tgBtnFlex.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (tgBtnFlex.isSelected()) {
          //tgBtnFlex.setText("Switch");
          portfolioDatePanel.setVisible(true);
          flexStatusLabel.setText("Type : Flexible");
        } else {
          //tgBtnFlex.setText("Type = Inflexible");
          flexStatusLabel.setText("Type : Inflexible");
          portfolioDatePanel.setVisible(false);
        }
      }
    });
    flexTogglePanel.add(flexStatusLabel);
    flexTogglePanel.add(tgBtnFlex);
    c.add(flexTogglePanel);

    JLabel portfolioDate = new JLabel();
    portfolioDate.setText("Enter the Date: ");
    portfolioDatePanel.add(portfolioDate);

    JLabel enterYear = new JLabel();
    enterYear.setText("YYYY ");
    portfolioDatePanel.add(enterYear);
    portfolioDatePanel.add(year);

    JLabel enterMonth = new JLabel();
    enterMonth.setText("MM ");
    portfolioDatePanel.add(enterMonth);
    portfolioDatePanel.add(month);

    JLabel enterDay = new JLabel();
    enterDay.setText("DD ");
    portfolioDatePanel.add(enterDay);
    portfolioDatePanel.add(day);
    c.add(portfolioDatePanel);

    ((AbstractDocument) day.getDocument()).setDocumentFilter(getDocumentIntegerFilter(1));
    ((AbstractDocument) month.getDocument()).setDocumentFilter(getDocumentIntegerFilter(1));
    ((AbstractDocument) year.getDocument()).setDocumentFilter(getDocumentIntegerFilter(3));

    JPanel checkCompPanel = new JPanel();
    checkCompPanel.add(btnCheckComposition);
    c.add(checkCompPanel);

    JPanel compositionPanel = new JPanel();
    JScrollPane scrollPanel = new JScrollPane();

    String[] header = {"#", "Stock", "Quantity", "Composition (%)"};
    String[][] data = {{"No Data Available.", ""}};
    DefaultTableModel model = new DefaultTableModel(data, header);
    compositionTable = new JTable(model);
    compositionTable.setEnabled(false);
    compositionPanel.setLayout(new BoxLayout(compositionPanel, BoxLayout.LINE_AXIS));
    scrollPanel.setViewportView(compositionTable);
    compositionPanel.add(scrollPanel);
    Border blackline = BorderFactory.createTitledBorder("Composition Details");
    compositionPanel.setBorder(blackline);
    c.add(compositionPanel);

    btnCheckComposition.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        inputOpt = portfolioName.getText();
        if (portfolioName.getText().isEmpty() || portfolioName.getText().isBlank()) {
          portfolioName.grabFocus();
          return;
        }
        if (tgBtnFlex.isSelected()) {
          if (year.getText().isEmpty() || year.getText().isBlank()) {
            displayDialog("Enter Year to proceed further.");
            year.grabFocus();
          } else if (month.getText().isEmpty() || month.getText().isBlank()) {
            displayDialog("Enter month to proceed further.");
            month.grabFocus();
          } else if (day.getText().isEmpty() || day.getText().isBlank()) {
            displayDialog("Enter day to proceed further.");
            day.grabFocus();
          }
        }
      }
    });
    pack();
  }

  private DocumentFilter getDocumentIntegerFilter(int lengthLimit) {
    DocumentFilter documentFilter = new DocumentFilter() {
      final Pattern regEx = Pattern.compile("\\d*");

      @Override
      public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
              throws BadLocationException {
        Matcher matcher = regEx.matcher(text);
        int currentLength = fb.getDocument().getLength();
        if (!matcher.matches() || (!text.isEmpty() && currentLength > lengthLimit)) {
          return;
        }
        super.replace(fb, offset, length, text, attrs);
      }
    };
    return documentFilter;
  }

  private DocumentFilter getDocumentFloatFilter() {
    DocumentFilter documentFilter = new DocumentFilter() {
      final Pattern regEx = Pattern.compile("^(\\d*\\.?\\d*)$");

      @Override
      public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
              throws BadLocationException {
        Matcher matcher = regEx.matcher(text);
        if (!matcher.matches()) {
          return;
        }
        super.replace(fb, offset, length, text, attrs);
      }
    };
    return documentFilter;
  }

  @Override
  public void displayBuySellStocks() {
    inputOpt = null;
    clearDate();
    clearLabels();
    Container c = getStatusPanel();
    c.setLayout(new BoxLayout(c, BoxLayout.PAGE_AXIS));

    JPanel titlePanel = new JPanel();
    JLabel title = new JLabel("Buy or Sell Stocks");
    title.setFont(new Font("Arial", Font.BOLD, 15));
    title.setSize(300, 30);
    titlePanel.add(title);
    c.add(titlePanel);

    JPanel portfolioNamePanel = new JPanel();
    JLabel portName = new JLabel();
    portName.setText("Enter Portfolio Name: ");
    portfolioNamePanel.add(portName);
    portfolioNamePanel.add(portfolioName);
    c.add(portfolioNamePanel);

    JPanel stockPanel = new JPanel();
    stockPanel.setLayout(new BoxLayout(stockPanel, BoxLayout.PAGE_AXIS));
    c.add(stockPanel);

    JPanel tickerPanel = new JPanel();
    JLabel tickerSymbol = new JLabel();
    tickerSymbol.setText("Enter the Ticker Symbol of the stock: ");
    tickerPanel.add(tickerSymbol);
    tickerPanel.add(tickerText);
    stockPanel.add(tickerPanel);

    JPanel portfolioTransactionPanel = new JPanel();
    JLabel transactionDate = new JLabel();
    transactionDate.setText("Enter the Transaction Date: ");
    portfolioTransactionPanel.add(transactionDate);

    JLabel transactionYear = new JLabel();
    transactionYear.setText("YYYY");
    portfolioTransactionPanel.add(transactionYear);
    portfolioTransactionPanel.add(year);

    JLabel transactionMonth = new JLabel();
    transactionMonth.setText("MM");
    portfolioTransactionPanel.add(transactionMonth);
    portfolioTransactionPanel.add(month);

    JLabel transactionDay = new JLabel();
    transactionDay.setText("DD");
    portfolioTransactionPanel.add(transactionDay);
    portfolioTransactionPanel.add(day);

    stockPanel.add(portfolioTransactionPanel);


    JPanel detailsPanel = new JPanel();
    btnGetPriceDate.setText("View Ticker Price");
    detailsPanel.add(btnGetPriceDate);
    btnGetPriceDate.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        inputOpt = null;
        inputOpt = tickerText.getText();
      }
    });

    detailsPanel.add(priceDetailLabel);
    priceDetailLabel.setText("Ticker details will be displayed here");
    stockPanel.add(detailsPanel);

    JPanel quantityPanel = new JPanel();
    JLabel quantity = new JLabel();
    quantity.setText("Enter the quantity: ");
    quantityPanel.add(quantity);

    quantityPanel.add(quantityText);
    stockPanel.add(quantityPanel);

    JPanel commFeePanel = new JPanel();
    JLabel commFee = new JLabel();
    commFee.setText("Enter the commission fee: ");
    commFeePanel.add(commFee);

    commissionFeeText.setText("0");
    commFeePanel.add(commissionFeeText);
    stockPanel.add(commFeePanel);

    ((AbstractDocument) day.getDocument()).setDocumentFilter(
            getDocumentIntegerFilter(1));
    ((AbstractDocument) month.getDocument()).setDocumentFilter(
            getDocumentIntegerFilter(1));
    ((AbstractDocument) year.getDocument()).setDocumentFilter(
            getDocumentIntegerFilter(3));
    ((AbstractDocument) quantityText.getDocument()).setDocumentFilter(
            getDocumentIntegerFilter(1000000));

    JPanel addStockPanel = new JPanel();
    JPanel buySellTogglePanel = new JPanel();
    final JToggleButton tgBtnSwitch = new JToggleButton("Switch");
    JLabel buySellLabel = new JLabel();
    buySellLabel.setText("Action: Buy");
    tgBtnSwitch.setSelected(false);
    tgBtnSwitch.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {

        if (tgBtnSwitch.isSelected()) {
          buySellLabel.setText("Action: Sell");
          addStockBtn.setText("Sell Stock");
        } else {
          buySellLabel.setText("Action: Buy");
          addStockBtn.setText("Buy Stock");
        }
      }
    });
    buySellTogglePanel.add(buySellLabel);
    buySellTogglePanel.add(tgBtnSwitch);
    stockPanel.add(buySellTogglePanel);

    addStockPanel.add(addStockBtn);
    stockPanel.add(addStockPanel);

    Border blackline = BorderFactory.createTitledBorder("Add Stock details.");
    stockPanel.setBorder(blackline);

    addStockBtn.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {
        if (portfolioName.getText().isEmpty() || portfolioName.getText().isBlank()) {
          portfolioName.grabFocus();
        } else if (tickerText.getText().isEmpty() || tickerText.getText().isBlank()) {
          tickerText.grabFocus();
          displayDialog("Enter ticker symbol to proceed further.");
        } else if (year.getText().isEmpty() || year.getText().isBlank()) {
          year.grabFocus();
        } else if (month.getText().isEmpty() || month.getText().isBlank()) {
          month.grabFocus();
        } else if (day.getText().isEmpty() || day.getText().isBlank()) {
          day.grabFocus();
        } else if (quantityText.getText().isEmpty() || quantityText.getText().isBlank()) {
          quantityText.grabFocus();
          displayDialog("Enter quantity to proceed further.");
        } else {
          inputOpt = String.valueOf(tgBtnSwitch.isSelected());
        }
      }
    });

    pack();

  }

  @Override
  public void displayInvestAmount() {
    inputOpt = null;
    clearDate();
    clearLabels();
    Container c = getStatusPanel();
    c.setLayout(new BoxLayout(c, BoxLayout.PAGE_AXIS));

    JPanel titlePanel = new JPanel();
    JLabel title = new JLabel("Invest Amount in Portfolio");
    title.setFont(new Font("Arial", Font.BOLD, 15));
    title.setSize(300, 30);
    titlePanel.add(title);
    c.add(titlePanel);

    JPanel portfolioNamePanel = new JPanel();
    JLabel portName = new JLabel();
    portName.setText("Enter Portfolio Name: ");
    portfolioNamePanel.add(portName);
    portfolioNamePanel.add(portfolioName);

    portfolioNamePanel.add(getPortfolioDataBtn);
    c.add(portfolioNamePanel);

    JPanel portfolioTransactionPanel = new JPanel();
    JLabel transactionDate = new JLabel();
    transactionDate.setText("Enter the Transaction Date: ");
    portfolioTransactionPanel.add(transactionDate);

    JLabel transactionYear = new JLabel();
    transactionYear.setText("Year");
    portfolioTransactionPanel.add(transactionYear);
    portfolioTransactionPanel.add(year);

    JLabel transactionMonth = new JLabel();
    transactionMonth.setText("Month");
    portfolioTransactionPanel.add(transactionMonth);
    portfolioTransactionPanel.add(month);

    JLabel transactionDay = new JLabel();
    transactionDay.setText("Day");
    portfolioTransactionPanel.add(transactionDay);
    portfolioTransactionPanel.add(day);

    c.add(portfolioTransactionPanel);

    ((AbstractDocument) day.getDocument()).setDocumentFilter(
            getDocumentIntegerFilter(1));
    ((AbstractDocument) month.getDocument()).setDocumentFilter(
            getDocumentIntegerFilter(1));
    ((AbstractDocument) year.getDocument()).setDocumentFilter(
            getDocumentIntegerFilter(3));

    JPanel invAmountPanel = new JPanel();
    JLabel invAmount = new JLabel();
    invAmount.setText("Enter amount to be invested in the portfolio: ");

    JLabel dollarSymbol = new JLabel();
    dollarSymbol.setText("USD");
    invAmountPanel.add(invAmount);
    invAmountPanel.add(investedAmountText);
    invAmountPanel.add(dollarSymbol);
    c.add(invAmountPanel);

    JPanel commissionPanel = new JPanel();
    JLabel commFeeLabel = new JLabel();
    commFeeLabel.setText("Enter Commission Fee: ");

    JLabel dollarSymbolC = new JLabel();
    dollarSymbolC.setText("USD");
    commissionPanel.add(commFeeLabel);
    commissionFeeText.setText("0");
    commissionPanel.add(commissionFeeText);
    commissionPanel.add(dollarSymbolC);
    c.add(commissionPanel);

    ((AbstractDocument) investedAmountText.getDocument()).setDocumentFilter(
            getDocumentFloatFilter());
    ((AbstractDocument) commissionFeeText.getDocument()).setDocumentFilter(
            getDocumentFloatFilter());

    JPanel percentagePanel = new JPanel();
    JLabel percentage = new JLabel();
    percentage.setText("Specify the weights of how the money should be invested in each "
            + "stock after fetching stock details.");
    percentagePanel.add(percentage);
    c.add(percentagePanel);

    JPanel stockTablePanel = new JPanel();
    JScrollPane scrollPane = new JScrollPane();
    scrollPane.add(investStockTable);
    stockTablePanel.add(scrollPane);
    c.add(stockTablePanel);

    String[] header = {"#", "Ticker Symbol", "Weights to be Invested (%)"};
    String[][] stockData = {};

    DefaultTableModel model = new DefaultTableModel(stockData, header);
    investStockTable.setModel(model);
    investStockTable.getColumnModel().getColumn(0).setMaxWidth(10);
    scrollPane.setViewportView(investStockTable);
    scrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
            "Stock Ticker Details", TitledBorder.CENTER, TitledBorder.TOP));
    scrollPane.setPreferredSize(new Dimension(600, 200));

    investStockTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);

    JPanel investPortfolioPanel = new JPanel();
    investPortfolioPanel.add(investAmountPortfolioBtn);
    c.add(investPortfolioPanel);

    investAmountPortfolioBtn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        investAmountPortfolioBtn.grabFocus();
      }
    });
    pack();

  }

  @Override
  public void displayStockTicker(String[][] tickerSymbolSet) {
    String[] header = {"#", "Ticker Symbol", "Weights to be Invested (%)"};

    DefaultTableModel model = new DefaultTableModel(tickerSymbolSet, header) {
      @Override
      public boolean isCellEditable(int row, int column) {
        // make read only fields except column 2
        return column == 2;
      }
    };
    investStockTable.setModel(model);

    pack();
  }

  @Override
  public void displayGetPerformance() {
    clearLabels();
    Container c = getStatusPanel();
    c.setLayout(new BoxLayout(c, BoxLayout.PAGE_AXIS));

    JPanel titlePanel = new JPanel();
    JLabel title = new JLabel("Portfolio Performance");
    title.setFont(new Font("Arial", Font.BOLD, 15));
    titlePanel.add(title);
    c.add(titlePanel);

    JPanel portfolioNamePanel = new JPanel();
    JLabel portName = new JLabel();
    portName.setText("Enter Portfolio Name: ");
    portfolioNamePanel.add(portName);
    portfolioNamePanel.add(portfolioName);
    c.add(portfolioNamePanel);

    JPanel portfolioStartDatePanel = new JPanel();
    JLabel dcaStartDate = new JLabel();
    dcaStartDate.setText("Enter the Start Date: ");
    portfolioStartDatePanel.add(dcaStartDate);

    JLabel enterStartYear = new JLabel();
    enterStartYear.setText("YYYY");
    portfolioStartDatePanel.add(enterStartYear);
    portfolioStartDatePanel.add(year);

    JLabel enterStartMonth = new JLabel();
    enterStartMonth.setText("MM");
    portfolioStartDatePanel.add(enterStartMonth);
    portfolioStartDatePanel.add(month);

    JLabel enterStartDay = new JLabel();
    enterStartDay.setText("DD");
    portfolioStartDatePanel.add(enterStartDay);
    portfolioStartDatePanel.add(day);

    c.add(portfolioStartDatePanel);

    JPanel portfolioEndDatePanel = new JPanel();
    JLabel dcaEndDate = new JLabel();
    dcaEndDate.setText("Enter the End Date: ");
    portfolioEndDatePanel.add(dcaEndDate);

    JLabel enterEndYear = new JLabel();
    enterEndYear.setText("YYYY");
    portfolioEndDatePanel.add(enterEndYear);
    portfolioEndDatePanel.add(endYear);

    JLabel enterEndMonth = new JLabel();
    enterEndMonth.setText("MM");
    portfolioEndDatePanel.add(enterEndMonth);
    portfolioEndDatePanel.add(endMonth);

    JLabel enterEndDay = new JLabel();
    enterEndDay.setText("DD");
    portfolioEndDatePanel.add(enterEndDay);
    portfolioEndDatePanel.add(endDay);

    c.add(portfolioEndDatePanel);

    ((AbstractDocument) day.getDocument()).setDocumentFilter(
            getDocumentIntegerFilter(1));
    ((AbstractDocument) month.getDocument()).setDocumentFilter(
            getDocumentIntegerFilter(1));
    ((AbstractDocument) year.getDocument()).setDocumentFilter(
            getDocumentIntegerFilter(3));

    ((AbstractDocument) endDay.getDocument()).setDocumentFilter(
            getDocumentIntegerFilter(1));
    ((AbstractDocument) endMonth.getDocument()).setDocumentFilter(
            getDocumentIntegerFilter(1));
    ((AbstractDocument) endYear.getDocument()).setDocumentFilter(
            getDocumentIntegerFilter(3));

    JPanel performanceBtnPanel = new JPanel();
    performanceBtnPanel.setLayout(new BoxLayout(performanceBtnPanel, BoxLayout.PAGE_AXIS));
    performanceBtnPanel.add(btnDisplayPerformance);
    c.add(performanceBtnPanel);

    conPerfromance.setLayout(new BoxLayout(conPerfromance, BoxLayout.PAGE_AXIS));
    conPerfromance.setPreferredSize(new Dimension(500, 500));
    c.add(conPerfromance);

    btnDisplayPerformance.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        conPerfromance.removeAll();
        if (portfolioName.getText().isBlank() || portfolioName.getText().isEmpty()) {
          portfolioName.grabFocus();
        } else if (year.getText().isBlank() || year.getText().isEmpty()) {
          year.grabFocus();
        } else if (month.getText().isBlank() || month.getText().isEmpty()) {
          month.grabFocus();
        } else if (day.getText().isBlank() || day.getText().isEmpty()) {
          day.grabFocus();
        } else if (endYear.getText().isBlank() || endYear.getText().isEmpty()) {
          endYear.grabFocus();
        } else if (endMonth.getText().isBlank() || endMonth.getText().isEmpty()) {
          endMonth.grabFocus();
        } else if (endDay.getText().isBlank() || endDay.getText().isEmpty()) {
          endDay.grabFocus();
        }
        startDate = year.getText() + '-' + month.getText() + '-' + day.getText();
        endDate = endYear.getText() + '-' + endMonth.getText() + '-' + endDay.getText();
      }
    });
    pack();
  }

  @Override
  public void displayPerformanceChart(List<Float> performanceVal,
                                      TimeStampBinsDTO timeStampBinsDTO) {
    JPanel titlePanel = new JPanel();
    JLabel title = new JLabel("Performance of Portfolio " + portfolioName.getText());
    title.setFont(new Font("Arial", Font.BOLD, 15));
    JPanel stitlePanel = new JPanel();
    JLabel subTitle = new JLabel("Chart x-axis : Date || y-axis : Portfolio Value");
    subTitle.setFont(new Font("Arial", Font.BOLD, 10));
    titlePanel.add(title);
    stitlePanel.add(subTitle);
    conPerfromance.add(titlePanel);
    conPerfromance.add(stitlePanel);

    Color[] colorArr = new Color[]{Color.RED, Color.YELLOW, Color.BLUE,
                                   Color.MAGENTA, Color.GREEN, Color.CYAN, Color.ORANGE};
    BarPanel panel = new BarPanel();
    for (int i = 0; i < performanceVal.size(); i++) {
      float pVal = performanceVal.get(i);
      panel.addBar(timestampBinFormatter(timeStampBinsDTO, i),
              (int) (pVal), colorArr[i % 7]);
    }
    panel.formatBarChart();
    conPerfromance.add(panel);
    pack();
  }

  @Override
  public void displayPortfolioValue() {
    inputOpt = null;
    clearDate();
    clearLabels();

    Container c = getStatusPanel();
    //c.setSize(new Dimension(100, 100));
    c.setLayout(new BoxLayout(c, BoxLayout.PAGE_AXIS));

    JPanel titlePanel = new JPanel();
    JLabel title = new JLabel("Portfolio Value");
    title.setFont(new Font("Arial", Font.BOLD, 15));
    title.setSize(300, 30);
    titlePanel.add(title);
    c.add(titlePanel);

    JPanel portfolioNamePanel = new JPanel();
    JLabel portName = new JLabel();
    portName.setText("Enter Portfolio Name: ");
    portfolioNamePanel.add(portName);
    portfolioNamePanel.add(portfolioName);
    c.add(portfolioNamePanel);

    JPanel portfolioDatePanel = new JPanel();
    JLabel portfolioDate = new JLabel();
    portfolioDate.setText("Enter the Date: ");
    portfolioDatePanel.add(portfolioDate);

    JLabel enterYear = new JLabel();
    enterYear.setText("Year");
    portfolioDatePanel.add(enterYear);
    portfolioDatePanel.add(year);

    JLabel enterMonth = new JLabel();
    enterMonth.setText("Month");
    portfolioDatePanel.add(enterMonth);
    portfolioDatePanel.add(month);

    JLabel enterDay = new JLabel();
    enterDay.setText("Day");
    portfolioDatePanel.add(enterDay);
    portfolioDatePanel.add(day);

    ((AbstractDocument) day.getDocument()).setDocumentFilter(
            getDocumentIntegerFilter(1));
    ((AbstractDocument) month.getDocument()).setDocumentFilter(
            getDocumentIntegerFilter(1));
    ((AbstractDocument) year.getDocument()).setDocumentFilter(
            getDocumentIntegerFilter(3));

    c.add(portfolioDatePanel);

    JPanel portfolioValuePanel = new JPanel();
    portfolioValuePanel.add(btnCheckValue);
    c.add(portfolioValuePanel);

    JPanel displayPortfolioValuePanel = new JPanel();
    portfolioValueLabel.setText("Portfolio Value will be displayed here.");
    displayPortfolioValuePanel.add(portfolioValueLabel);
    c.add(displayPortfolioValuePanel);

    Container con = new Container();
    con.setLayout(new BoxLayout(con, BoxLayout.PAGE_AXIS));
    con.setPreferredSize(new Dimension(500, 500));
    c.add(con);

    btnCheckValue.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        inputOpt = portfolioName.getText();
      }
    });

    pack();
  }

  @Override
  public void displayCostBasis() {
    inputOpt = null;
    clearDate();
    clearLabels();

    Container costBasisContainer = getStatusPanel();
    costBasisContainer.setLayout(new BoxLayout(costBasisContainer, BoxLayout.PAGE_AXIS));
    JPanel titlePanel = new JPanel();
    JLabel title = new JLabel("Portfolio Cost Basis");
    title.setFont(new Font("Arial", Font.BOLD, 15));
    title.setSize(300, 30);
    titlePanel.add(title);
    costBasisContainer.add(titlePanel);

    JPanel portfolioNamePanel = new JPanel();
    JLabel portName = new JLabel();
    portName.setText("Enter Portfolio Name: ");
    portfolioNamePanel.add(portName);
    portfolioNamePanel.add(portfolioName);
    costBasisContainer.add(portfolioNamePanel);

    JPanel portfolioDatePanel = new JPanel();
    JLabel portfolioDate = new JLabel();
    portfolioDate.setText("Enter the Date: ");
    portfolioDatePanel.add(portfolioDate);

    JLabel enterYear = new JLabel();
    enterYear.setText("Year");
    portfolioDatePanel.add(enterYear);
    portfolioDatePanel.add(year);

    JLabel enterMonth = new JLabel();
    enterMonth.setText("Month");
    portfolioDatePanel.add(enterMonth);
    portfolioDatePanel.add(month);

    JLabel enterDay = new JLabel();
    enterDay.setText("Day");
    portfolioDatePanel.add(enterDay);
    portfolioDatePanel.add(day);

    ((AbstractDocument) day.getDocument()).setDocumentFilter(
            getDocumentIntegerFilter(1));
    ((AbstractDocument) month.getDocument()).setDocumentFilter(
            getDocumentIntegerFilter(1));
    ((AbstractDocument) year.getDocument()).setDocumentFilter(
            getDocumentIntegerFilter(3));

    costBasisContainer.add(portfolioDatePanel);

    JPanel costBasisPanel = new JPanel();
    costBasisPanel.add(btnCostBasis);
    costBasisContainer.add(costBasisPanel);

    JPanel displayCostBasisPanel = new JPanel();
    costBasisLabel.setText("Cost Basis of the portfolio will be displayed here.");
    displayCostBasisPanel.add(costBasisLabel);
    costBasisContainer.add(displayCostBasisPanel);

    Container c = new Container();
    c.setLayout(new BoxLayout(c, BoxLayout.PAGE_AXIS));
    c.setPreferredSize(new Dimension(500, 500));
    costBasisContainer.add(c);

    btnCostBasis.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        inputOpt = portfolioName.getText();
      }
    });

    pack();
  }

  @Override
  public void createDCAStrategy() {
    inputOpt = null;
    clearDate();
    clearLabels();
    Container c = getStatusPanel();
    c.setLayout(new BoxLayout(c, BoxLayout.PAGE_AXIS));
    JPanel titlePanel = new JPanel();
    JLabel title = new JLabel("Dollar Cost Averaging Strategy");
    title.setFont(new Font("Arial", Font.BOLD, 15));
    title.setSize(300, 30);
    titlePanel.add(title);
    c.add(titlePanel);

    JPanel portfolioNamePanel = new JPanel();
    JLabel portName = new JLabel();
    portName.setText("Enter Portfolio Name: ");
    portfolioNamePanel.add(portName);
    portfolioNamePanel.add(portfolioName);
    c.add(portfolioNamePanel);

    JPanel portfolioStartDatePanel = new JPanel();
    JLabel dcaStartDate = new JLabel();
    dcaStartDate.setText("Enter the Start Date: ");
    portfolioStartDatePanel.add(dcaStartDate);

    JLabel enterStartYear = new JLabel();
    enterStartYear.setText("Year");
    portfolioStartDatePanel.add(enterStartYear);
    portfolioStartDatePanel.add(year);

    JLabel enterStartMonth = new JLabel();
    enterStartMonth.setText("Month");
    portfolioStartDatePanel.add(enterStartMonth);
    portfolioStartDatePanel.add(month);

    JLabel enterStartDay = new JLabel();
    enterStartDay.setText("Day");
    portfolioStartDatePanel.add(enterStartDay);
    portfolioStartDatePanel.add(day);

    c.add(portfolioStartDatePanel);

    JPanel portfolioEndDatePanel = new JPanel();
    JLabel dcaEndDate = new JLabel();
    dcaEndDate.setText("Enter the End Date: ");
    portfolioEndDatePanel.add(dcaEndDate);

    JLabel enterEndYear = new JLabel();
    enterEndYear.setText("Year");
    portfolioEndDatePanel.add(enterEndYear);
    portfolioEndDatePanel.add(endYear);

    JLabel enterEndMonth = new JLabel();
    enterEndMonth.setText("Month");
    portfolioEndDatePanel.add(enterEndMonth);
    portfolioEndDatePanel.add(endMonth);

    JLabel enterEndDay = new JLabel();
    enterEndDay.setText("Day");
    portfolioEndDatePanel.add(enterEndDay);
    portfolioEndDatePanel.add(endDay);

    ((AbstractDocument) day.getDocument()).setDocumentFilter(
            getDocumentIntegerFilter(1));
    ((AbstractDocument) month.getDocument()).setDocumentFilter(
            getDocumentIntegerFilter(1));
    ((AbstractDocument) year.getDocument()).setDocumentFilter(
            getDocumentIntegerFilter(3));

    ((AbstractDocument) endDay.getDocument()).setDocumentFilter(
            getDocumentIntegerFilter(1));
    ((AbstractDocument) endMonth.getDocument()).setDocumentFilter(
            getDocumentIntegerFilter(1));
    ((AbstractDocument) endYear.getDocument()).setDocumentFilter(
            getDocumentIntegerFilter(3));

    c.add(portfolioEndDatePanel);

    JPanel invAmountPanel = new JPanel();
    JLabel invAmount = new JLabel();
    invAmount.setText("Enter amount to be invested in the strategy: ");

    JLabel dollarSymbol = new JLabel();
    dollarSymbol.setText("USD");
    invAmountPanel.add(invAmount);
    invAmountPanel.add(investedAmountText);
    invAmountPanel.add(dollarSymbol);
    c.add(invAmountPanel);

    JPanel frequencyPanel = new JPanel();
    JLabel frequency = new JLabel();
    frequency.setText("Enter the frequency in days: ");
    frequencyPanel.add(frequency);
    frequencyPanel.add(frequencyText);
    c.add(frequencyPanel);

    JPanel commissionFeePanel = new JPanel();
    JLabel commFee = new JLabel();
    commFee.setText("Enter commission fee: ");

    JLabel dollarSymbolC = new JLabel();
    dollarSymbolC.setText("USD");
    commissionFeePanel.add(commFee);
    commissionFeePanel.add(commissionFeeText);
    commissionFeePanel.add(dollarSymbolC);
    c.add(commissionFeePanel);

    ((AbstractDocument) investedAmountText.getDocument()).setDocumentFilter(
            getDocumentFloatFilter());
    ((AbstractDocument) commissionFeeText.getDocument()).setDocumentFilter(
            getDocumentFloatFilter());
    ((AbstractDocument) frequencyText.getDocument()).setDocumentFilter(
            getDocumentIntegerFilter(1000000));

    JPanel stockDetailsPanel = new JPanel();
    stockDetailsPanel.setLayout(new BoxLayout(stockDetailsPanel, BoxLayout.PAGE_AXIS));
    c.add(stockDetailsPanel);

    Border blackline = BorderFactory.createTitledBorder("Add Stock details.");
    stockDetailsPanel.setBorder(blackline);

    JPanel tickerPanel = new JPanel();
    JLabel tickerSymbol = new JLabel();
    tickerSymbol.setText("Enter the Ticker Symbol of the stock: ");
    tickerPanel.add(tickerSymbol);
    tickerPanel.add(tickerText);
    stockDetailsPanel.add(tickerPanel);

    JPanel addStockPanel = new JPanel();
    addStockPanel.setLayout(new BoxLayout(addStockPanel, BoxLayout.PAGE_AXIS));
    JButton btnAddStockDCA = new JButton("Add Stock");
    addStockPanel.add(btnAddStockDCA);
    stockDetailsPanel.add(addStockPanel);

    btnAddStockDCA.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (tickerText.getText().isEmpty()) {
          displayDialog("Enter Ticker Symbol of the stock");
        } else {
          DefaultTableModel model = (DefaultTableModel) dcaStockTable.getModel();
          model.addRow(new Object[]{tickerText.getText().toUpperCase(), ""});
          tickerText.setText("");
        }
      }
    });

    JPanel percentagePanel = new JPanel();
    JLabel percentage = new JLabel();
    percentage.setText("Specify the weights of how the money should be invested in each "
            + "stock after fetching stock details.");
    percentagePanel.add(percentage);
    stockDetailsPanel.add(percentagePanel);

    JPanel stockTablePanel = new JPanel();
    JScrollPane scrollPane = new JScrollPane();
    scrollPane.add(investStockTable);
    stockTablePanel.add(scrollPane);
    c.add(stockTablePanel);

    String[] header = {"Ticker Symbol", "Weights to be Invested (%)"};
    String[][] stockData = {};

    DefaultTableModel model = new DefaultTableModel(stockData, header) {
      @Override
      public boolean isCellEditable(int row, int column) {
        // make read only fields except column 2
        return column == 1;
      }
    };

    dcaStockTable.setModel(model);
    scrollPane.setViewportView(dcaStockTable);
    scrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
            "Stock Ticker Details", TitledBorder.CENTER, TitledBorder.TOP));
    scrollPane.setPreferredSize(new Dimension(600, 200));

    dcaStockTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);

    JPanel addStrategyPanel = new JPanel();
    btnAddPortfolioDCA.setText("Add Strategy");
    addStrategyPanel.add(btnAddPortfolioDCA);
    c.add(addStrategyPanel);

    btnAddPortfolioDCA.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        //tableDataModelDCA = dcaStockTable.getModel();
        if (portfolioName.getText().isEmpty() || portfolioName.getText().isBlank()) {
          displayDialog("Enter Portfolio Name to proceed further.");
          portfolioName.grabFocus();
        } else if (investedAmountText.getText().isEmpty()
                || investedAmountText.getText().isBlank()) {
          displayDialog("Enter Invested Amount to proceed further.");
          investedAmountText.grabFocus();
        } else if (frequencyText.getText().isEmpty() || frequencyText.getText().isBlank()) {
          displayDialog("Enter frequency of investments to proceed further.");
          frequencyText.grabFocus();
        } else {
          inputOpt = portfolioName.getText();
          if (month.getText().length() == 1) {
            month.setText('0' + month.getText());
          }
          if (day.getText().length() == 1) {
            day.setText('0' + day.getText());
          }
          if (endMonth.getText().length() == 1) {
            endMonth.setText('0' + endMonth.getText());
          }
          if (endDay.getText().length() == 1) {
            endDay.setText('0' + endDay.getText());
          }

          startDate = year.getText() + '-' + month.getText() + '-' + day.getText();
          endDate = endYear.getText() + '-' + endMonth.getText() + '-' + endDay.getText();
        }
      }
    });

    pack();
  }

  @Override
  public JPanel getMainPanel() {
    return mainPanel;
  }

  @Override
  public JPanel getStatusPanel() {
    return statusPanel;
  }

  @Override
  public void displayDialog(String message) {
    JOptionPane.showMessageDialog(GuiViewSwingImpl.this, message, "Message",
            JOptionPane.PLAIN_MESSAGE);
  }

  @Override
  public JLabel getPriceDetailLabel() {
    return priceDetailLabel;
  }

  @Override
  public void setPriceDetailLabel(String priceDetails) {
    this.priceDetailLabel.setText(priceDetails);
    revalidate();
  }

  @Override
  public JTable getCompositionTable() {
    return compositionTable;
  }

  @Override
  public JTextField getpNameText() {
    return pNameText;
  }

  @Override
  public void displayPortfolioValueLabel(String portfolioValue) {
    this.portfolioValueLabel.setText(portfolioValue);
    revalidate();
  }

  @Override
  public void displayCostBasisValue(String costBasis) {
    this.costBasisLabel.setText(costBasis);
    revalidate();
  }

  @Override
  public void clearDate() {
    day.setText("");
    month.setText("");
    year.setText("");
    endDay.setText("");
    endMonth.setText("");
    endYear.setText("");
  }

  @Override
  public void clearLabels() {
    startDate = "";
    endDate = "";
    portfolioName.setText("");
    investedAmountText.setText("");
    commissionFeeText.setText("0");
    frequencyText.setText("");
    tickerText.setText("");
    quantityText.setText("");
    priceDetailLabel.setText("Ticker details will be displayed here");
    portfolioValueLabel.setText("Portfolio Value will be displayed here.");
    costBasisLabel.setText("Cost Basis of the portfolio will be displayed here.");
    String[] header = {"#", "Ticker Symbol", "Weights to be Invested (%)"};
    String[][] stockData = {};
    investStockTable.setModel(new DefaultTableModel(stockData, header));
    String[] headerDCA = {"Ticker Symbol", "Weights to be Invested (%)"};
    dcaStockTable.setModel(new DefaultTableModel(stockData, headerDCA));
  }

  private String timestampBinFormatter(TimeStampBinsDTO timeStampBinsDTO, int index) {
    if (timeStampBinsDTO.getType().equals(TimeStampBinsDTO.TimestampType.YEAR)) {
      return String.valueOf(timeStampBinsDTO.getTimeStampBins().get(index).getYear());
    } else if (timeStampBinsDTO.getType().equals(TimeStampBinsDTO.TimestampType.MONTH)) {
      DateTimeFormatter monthFormat = DateTimeFormatter.ofPattern("MMM yyyy",
              Locale.ENGLISH);
      String monthName = monthFormat.format(timeStampBinsDTO.getTimeStampBins().get(index));
      return monthName;
    } else {
      return timeStampBinsDTO.getTimeStampBins().get(index).toString();
    }
  }
}
