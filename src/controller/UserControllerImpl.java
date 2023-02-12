package controller;

import java.io.PrintStream;

import view.TextViewConsoleImpl;
import viewmodel.UserViewModel;

/**
 * UserControllerImpl for UserController that describes its implementation.
 */
public class UserControllerImpl implements UserController {

  @Override
  public void getPortfolios(UserViewModel userView, PrintStream out) {
    TextViewConsoleImpl view = new TextViewConsoleImpl();
    view.displayPortfolios(out, userView.getFlexPortfolios(), userView.getInflexPortfolios());
  }

}
