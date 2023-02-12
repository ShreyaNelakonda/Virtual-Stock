In our design, we implemented MVC (Model-View-Controller) architecture.
We have created different packages to separate each MVC component.
Controller interacts with user and controls View and Model as needed.
Controller and model interacts with File System and API through service in order to get/set data.

1) MODEL
    > There are 3 entities - Stock, Portfolio and User.
    > Design supports only one user in this implementation.
    > User contains list of portfolios associated with them.
    > There are two types of implementation of portfolio models, flexible and inflexible.
    > Flexible portfolio stores stocks (ticker symbol, quantity) with transaction date, commission fee and the buy/sell flag.
    > Inflexible portfolio stores only ticker symbol and quantity.
    > Portfolio contains portfolioName and collection of unique stock transactions.
    > We implemented MVVM in order to separate program logic and user interface.
      All the getter are in the view model class and all the method which mutate are in the original model.
    > We added a new model to support Dollar Cost Averaging Strategy.
      Based on the user's input, this strategy creates flexible stocks and add it to the mentioned portfolio
      on the specified date with the specified amount.

2) CONTROLLERS
    > There are 3 controllers in the design.
    > Main controller initializes the program.
    > Since we have one view, a single controller would be minimal requirement.
    > But we created separate controllers in order to support future enhancement of multiple modular views.
    > MainController.init() will initialize the model based on user input,
      and it will pass the control to MainController.main() that displays features supported by program.
    > Based on the user input control will be transferred to the corresponding controller to perform related task.
    > UserController handles getPortfolios() for the given user.
    > PortfolioController manages createPortfolio(), portfolioComposition(), getPortfolioValue(),
      updateFlexPortfolio(), portfolioPerformance(), getCostBasis().
    > Controllers use InputStream to read the inputs from the user view.
    > In order to support new GUI view implementation apart from text view,
      we created a new implementation of existing Controller interface - MainControllerGuiImpl.

3) VIEW
    > Program supports a single view provided by Java called System.
    > PrintStream is used to display output to the user.
    > MainView class consolidates all display statements.
    > View can interact with the model to get the data from the read-only view model object.
    > In order to support new GUI view implementation we created a new view - GUI View which used Java Swing components.

4) SERVICES
    > There are three services that communicates between model/controller and data stores.
    1) UserDataService:
        > It loads and persists user profile in UserDB.
    2) StocksDataService:
        > It interacts with API and stores the stock data into the system.
        > It contains operations useful for the controller/model to communicate with the StockDB.
        > StockDB stores time-series stock data.
    3) ValidationService:
        > It contains common validation logic like date format.


DESIGN CHANGES:
  1) Created a new implementation of existing Controller interface to support interaction between GUI and Model.
  2) Created new view to add GUI components.
  3) Model was unaffected by GUI change.
  4) Created a new class to display Bar Chart to be used in GUI view.

Assumptions in Design :
1. Design supports only one User.
2. Design fetches the stock data from Alphavantage API.
3. For inflexible portfolio, current price of the stock is the latest closing price available in the data.
4. For flexible portfolio, invested price is the closing price for that stock on the transaction date.
5. For every stock the earliest date is different.
6. Storage is supported in this design using File System.
7. User cannot sell the stock on the same day user bought it since we are not considering time of transaction.
8. User cannot buy or sell stocks on a non-business day (weekends, federal holidays).
9. Dollar Cost Averaging strategy may call for executing a transaction on a holiday.
   In this case, the program chooses the next available day to invest.
