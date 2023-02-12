package viewmodel;

import java.util.HashMap;
import java.util.List;

import model.DCAStrategyModel;
import model.PortfolioModel;

/**
 * Interface that describes read only UserModel operations.
 */
public interface UserViewModel {

  List<DCAStrategyModel> getDcaStrategyList();

  /**
   * Returns userName of the given account.
   *
   * @return userName of the given account
   */
  String getUserName();

  /**
   * Returns list of existing flexible portfolios for the user.
   *
   * @return list of portfolios
   */
  HashMap<String, PortfolioModel> getFlexPortfolios();

  /**
   * Returns list of existing inflexible portfolios for the user.
   *
   * @return list of portfolios
   */
  HashMap<String, PortfolioModel> getInflexPortfolios();

}
