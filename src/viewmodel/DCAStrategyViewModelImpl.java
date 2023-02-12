package viewmodel;

import java.time.LocalDate;
import java.util.List;

import model.DCAStockRatio;

/**
 * View Model Class implements DCAStrategyModel which computes the dollar cost averaging
 * strategy for given portfolio. This is a ready-only version of the DCAStrategyModelImpl Class.
 */
public class DCAStrategyViewModelImpl implements DCAStrategyViewModel {
  private final DCAStrategyViewModel portfolio;

  public DCAStrategyViewModelImpl(DCAStrategyViewModel portfolio) {
    this.portfolio = portfolio;
  }


  @Override
  public String getPortfolioName() {
    return this.portfolio.getPortfolioName();
  }

  @Override
  public List<DCAStockRatio> getStockMap() {
    return this.portfolio.getStockMap();
  }

  @Override
  public LocalDate getStartDate() {
    return this.portfolio.getStartDate();
  }

  @Override
  public LocalDate getEndDate() {
    return this.portfolio.getEndDate();
  }

  @Override
  public double getInvestedAmount() {
    return this.portfolio.getInvestedAmount();
  }

  @Override
  public int getFrequencyDays() {
    return this.portfolio.getFrequencyDays();
  }

  @Override
  public float getCommissionFee() {
    return this.portfolio.getCommissionFee();
  }

}
