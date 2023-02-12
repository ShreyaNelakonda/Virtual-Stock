package model;

import viewmodel.DCAStrategyViewModel;

/**
 * Interface to compute the dollar cost averaging strategy for given portfolio.
 */
public interface DCAStrategyModel extends DCAStrategyViewModel {

  /**
   * Method to compute dollar cost averaging strategy for a portfolio. It adds stocks to the
   * portfolio on a recurring basis depending on the given frequency.
   * @param userModel model to which dollar cost averaging strategy will be added
   */
  void computeDollarCostAveraging(UserModel userModel);
}
