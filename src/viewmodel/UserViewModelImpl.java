package viewmodel;

import java.util.HashMap;
import java.util.List;

import model.DCAStrategyModel;
import model.PortfolioModel;

/**
 * Class that implements read only UserModel operations.
 */
public class UserViewModelImpl implements UserViewModel {

  private final UserViewModel user;

  public UserViewModelImpl(UserViewModel user) {
    this.user = user;
  }

  /**
   * Returns userName of the given account.
   *
   * @return userName of the given account
   */
  @Override
  public String getUserName() {
    return user.getUserName();
  }

  /**
   * Returns portfolios associated with the given account.
   *
   * @return portfolios associated with the given account
   */
  @Override
  public HashMap<String,PortfolioModel> getFlexPortfolios() {
    return user.getFlexPortfolios();
  }

  @Override
  public HashMap<String, PortfolioModel> getInflexPortfolios() {
    return user.getInflexPortfolios();
  }

  public List<DCAStrategyModel> getDcaStrategyList() {
    return user.getDcaStrategyList();
  }

}
