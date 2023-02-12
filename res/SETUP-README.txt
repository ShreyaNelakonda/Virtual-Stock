Virtual Stock

PREREQUISITES
jdk 11 must be configured on your system.

SETTING UP ENVIRONMENT
1) Copy Virtual-Stock.jar from /res wherever you want to execute.
2) In the same folder, copy database folder available in /res.
3) Make sure database folder has 2 other folders StockDB and UserDB.
4) Folder structure
    > Virtual-Stock.jar
    > database
        > StockDB
        > UserDB
            > UserProfile.csv
5) Execute the file using command prompt in the same context.
    java -jar Virtual-Stock.jar text   > to run text based UI
    java -jar Virtual-Stock.jar gui    > to run GUI

EXECUTION FOR GUI
1. Load a profile as per the given choices and click on Proceed.
   1) Loading from Existing File : Automatically load last saved profile from User data store.
   2) Loading from External File : Load an external file - sample provided in res/external-profile.csv
   3) Create New Profile : Creates an empty profile, previously saved version will be overridden.

2. Once profile is loaded/created, Main Menu will be displayed.
   It lists down several features supported by the GUI.
   Choose as per your choice and click on Proceed.
   The corresponding activity will be displayed on Menu Option Panel.
   To try another feature simply check its corresponding box and click on proceed.

EXECUTION FOR TEXT
1 Loading from Existing File
   1.1 User can create a new blank profile by selecting option 1 in the INIT menu.
   1.2 If existing profile exists, then you may add new portfolios as mentioned below in step 3.
   1.3 Otherwise create a new user or load external profile(option 2 or 3).

2 Loading from External File
    2.1 User can create a new blank profile by selecting option 2 in the INIT menu.
    2.2 Enter filepath for csv file (/path/filename.csv) : res/external-profile.csv
        Make sure it does not have space in between the path mentioned.
        File can be anywhere on the system, mention the complete path.
    2.3 Format for external csv file :
        Inflexible Portfolio - Each row must contain PortfolioName, TickerSymbol, Quantity, false
        Flexible Portfolio - Each row must contain PortfolioName, TickerSymbol, Quantity, true,
                                                     TransactionDate (YYYY-MM-DD), CommissionFee,
                                                     buy (true/false)
        You may use file named external-profile.csv under res folder as an external file.

3 Create New Profile
    3.1 User can create a new blank profile by selecting option 3 in the INIT menu.

    **Create a portfolio with 3 different stocks**
        3.2 To add new portfolio, select option 1 from Main menu : to create portfolio.
        3.3 Select option 1 to create a flexible portfolio.
        3.4 Enter unique portfolio name (Alphanumeric characters) (case-insensitive): Portfolio1
        3.4 Enter ticker symbol for stock (case-insensitive) : GOOG
            [Some other valid tickers : AAPL, C, UBER, META]
        3.5 Enter the transaction date (YYYY-MM-DD) : 2021-10-05
        3.6 Check the price of the stock on the given transaction date.
        3.7 Select option 1 to buy the stock.
        3.8 Enter quantity (positive integer) : 50
        3.9 Enter commission fee for the transaction : 1.5
        3.10 View the data of the transaction
        3.11 Select option 1 to save this stock to the portfolio.

        3.12 Press 1 to add more stocks to the portfolio
        3.13 Enter ticker symbol for stock (case-insensitive) : AAPL
        3.14 Enter the transaction date (YYYY-MM-DD) : 2022-05-24
        3.15 Check the price of the stock on the given transaction date.
        3.16 Select option 1 to buy the stock.
        3.17 Enter quantity (positive integer) : 23
        3.18 Enter commission fee for the transaction : 3.8
        3.19 View the data of the transaction
        3.20 Select option 1 to save this stock to the portfolio.

        3.21 Press 1 to add more stocks to the portfolio
        3.22 Enter ticker symbol for stock (case-insensitive) : META
        3.23 Enter the transaction date (YYYY-MM-DD) : 2022-08-01
        3.24 Check the price of the stock on the given transaction date.
        3.25 Select option 1 to buy the stock.
        3.26 Enter quantity (positive integer) : 15
        3.27 Enter commission fee for the transaction : 2.6
        3.28 View the data of the transaction
        3.29 Select option 1 to save this stock to the portfolio.

        3.30 Select any key to go back to the main menu. (Portfolio will be saved in the UserProfile.csv file)

    **Query portfolio value on a specific date
        1. Select option 4 from main menu to view to price of the portfolio
        2. Enter the name of the portfolio : portfolio1
        3. Enter the date (YYYY-MM-DD) : 2021-12-06
        4. View the value of the portfolio on the given date.
            (Only 50 shares of GOOG stock is present in the portfolio on 2021-12-06)

        5. Select option 4 from main menu to view to price of the portfolio
        6. Enter the name of the portfolio : portfolio1
        7. Enter the date (YYYY-MM-DD) : 2021-12-06
        8. View the value of the portfolio on the given date.
            (all the 3 stocks are present in the portfolio, but the portfolio value has decreased
            because the value of GOOG has decreased)

    **Compute Cost Basis of the portfolio on a specific date
        1. Select option 6 from main menu to view to price of the portfolio
        2. Enter the name of the portfolio : portfolio1
        3. Enter the date (YYYY-MM-DD)
        4. View the Cost Basis of the portfolio on the given date.

        5. Select option 6 from main menu to view to price of the portfolio
        6. Enter the name of the portfolio : portfolio1
        7. Enter the date (YYYY-MM-DD)
        8. View the Cost Basis of the portfolio on the given date.


Data supported:
Program supports all the ticker symbols supported by alphavantage API.
Stocks are available from the first date provided by alphavantage API.
Stock data for today's date (business day) is available after the market closes.