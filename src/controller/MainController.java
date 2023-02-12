package controller;

/**
 * Controller to handle code related to initialization of the system.
 * The master controller that calls other controllers depending on the type of operations.
 * Example: It will call PortfolioController for operations related to Portfolio,
 * like getComposition, getCostBasis, etc.
 */
public interface MainController {

  /**
   * A controller() to initialize Model-View-Controller to begin the system.
   */
  void init();

  /**
   * A controller() to handle main menu for the user.
   */
  void main();

}
