package controller;

import java.io.PrintStream;

import viewmodel.UserViewModel;

/**
 * UserController Interface that provides methods that support User operations.
 */
public interface UserController {

  /**
   * Gets list of portfolios for the given user.
   *
   * @param user current user
   * @param out  output stream
   */
  void getPortfolios(UserViewModel user, PrintStream out);
}
