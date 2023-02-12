package model;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;

import service.stocks.StockCSVServiceImpl;

/**
 * PortfolioModelFlexImpl implements methods related to Flexible Portfolios.
 */
public class PortfolioModelFlexImpl extends PortfolioModelImpl {
    /**
     * Constructor for Portfolio that takes portfolioName and list of stocks as parameters.
     *
     * @param portfolioName name of the portfolio
     * @param stockMap      list of stocks in portfolio
     * @throws IllegalArgumentException when validation of parameters fails
     */
    public PortfolioModelFlexImpl(String portfolioName, HashMap<String, StockModel> stockMap)
            throws IllegalArgumentException {
        super(portfolioName, stockMap);
    }

    /**
     * Method to calculate and return value of the portfolio on a certain date by summing the
     * value of all the stocks in the portfolio on that date.
     *
     * @param date date on which to get the portfolio value
     * @return value of the portfolio on the given date
     */
    @Override
    public float calculateStockValue(LocalDate date) throws IOException {
        float portfolioValueCertainDate = 0;
        float valueOfEachStock;
        HashMap<String, StockModel> stocks = this.getStockMap();
        for (StockModel stockFlex : stocks.values()) {
            String tickerSymbol = stockFlex.getTickerSymbol();
            if (stockFlex.getTransactionDate().isBefore(date)) {
                try {
                    valueOfEachStock = StockCSVServiceImpl.getInstance()
                            .getStockValueOnCertainDate(tickerSymbol, date);
                } catch (IllegalArgumentException e) {
                    valueOfEachStock = 0;
                } catch (IOException e) {
                    throw new IOException(e);
                }
            } else {
                valueOfEachStock = 0;
            }
            HashMap<String, String> stockBuyMap = new HashMap<>();
            HashMap<String, String> stockSellMap = new HashMap<>();
            stockTransactionMap(stockBuyMap, stockSellMap);

            float stockQuantity = 0;

            if (stockFlex.getBuy()) {
                stockQuantity = (stockQuantity + stockFlex.getStockQuantity());
            } else {
                stockQuantity = (stockQuantity - stockFlex.getStockQuantity());
            }

            portfolioValueCertainDate = portfolioValueCertainDate
                    + (valueOfEachStock * stockQuantity);
        }
        return portfolioValueCertainDate;
    }

    /**
     * Method to calculate and return the cost basis (i.e. the total amount of money invested
     * in a portfolio) for a portfolio. Cost basis will only be calculated for flexible portfolios.
     *
     * @return cost basis for a flexible portfolio
     */
    @Override
    public float calculateCostBasis(LocalDate portfolioCostDate) {
        float costBasis = 0;
        HashMap<String, StockModel> stocks = this.getStockMap();
        for (StockModel stockFlex : stocks.values()) {
            if (!stockFlex.getTransactionDate().isAfter(portfolioCostDate)) {
                if (stockFlex.getBuy()) {
                    costBasis = costBasis + stockFlex.getCommissionFee()
                            + (stockFlex.getInvestedPrice() * stockFlex.getStockQuantity());
                }
                if (!stockFlex.getBuy()) {
                    costBasis = costBasis + stockFlex.getCommissionFee();
                }
            }

        }
        return costBasis;
    }

    /**
     * Method to calculate and return the composition of a stock (percentage of the stock)
     * in a portfolio.
     *
     * @return hashmap storing the stock data and the percentage of that stock in a given portfolio
     */
    @Override
    public HashMap<String, Float> getStockComposition(LocalDate portfolioCompDate) {

        HashMap<String, Float> stockComposition = new HashMap<>();
        HashMap<String, String> stockBuyMap = new HashMap<>();
        HashMap<String, String> stockSellMap = new HashMap<>();
        HashMap<String, Float> stockQuantity = new HashMap<>();

        float totalStockQuantity = this.getTotalQuantity(portfolioCompDate, stockBuyMap, stockSellMap);

        for (StockModel stock : this.getStockMap().values()) {
            if (stock.getTransactionDate().isBefore(portfolioCompDate)) {
                String tickerComp = "";
                float comp = 0;

                if (stock.getBuy()) {
                    if (stockQuantity.containsKey(stock.getTickerSymbol())) {
                        stockComposition.remove(stock.getTickerSymbol() + " "
                                + stockQuantity.get(stock.getTickerSymbol()));
                        stockQuantity.put(stock.getTickerSymbol(),
                                (stock.getStockQuantity() + stockQuantity.get(stock.getTickerSymbol())));
                    } else {
                        stockQuantity.put(stock.getTickerSymbol(), stock.getStockQuantity());
                    }
                } else {
                    if (stockQuantity.containsKey(stock.getTickerSymbol())) {
                        stockComposition.remove(stock.getTickerSymbol() + " "
                                + stockQuantity.get(stock.getTickerSymbol()));
                        stockQuantity.put(stock.getTickerSymbol(),
                                (stockQuantity.get(stock.getTickerSymbol()) - stock.getStockQuantity()));
                    } else {
                        stockQuantity.put(stock.getTickerSymbol(), -stock.getStockQuantity());
                    }
                }
                comp = ((stockQuantity.get(stock.getTickerSymbol()))
                        / totalStockQuantity) * 100;
                tickerComp = stock.getTickerSymbol() + " " + stockQuantity.get(stock.getTickerSymbol());
                stockComposition.put(tickerComp, comp);
            }
        }
        return stockComposition;
    }

    /**
     * Method to calculate the total quantity of all the stocks in a portfolio. Value will be fixed
     * for inflexible portfolio and will change depending on the date for a flexible portfolio.
     *
     * @return quantity of stocks present in a portfolio
     */
    @Override
    public float getTotalQuantity(LocalDate portfolioCompDate, HashMap<String, String> stockBuyMap,
                                  HashMap<String, String> stockSellMap) {
        float totalStockQuantity = 0;

        for (StockModel stock : this.getStockMap().values()) {
            if (((stock.getTransactionDate().isBefore(portfolioCompDate)))) {
                if (stock.getBuy()) {
                    totalStockQuantity = totalStockQuantity + stock.getStockQuantity();
                } else {
                    totalStockQuantity = totalStockQuantity - stock.getStockQuantity();
                }
            }
        }
        return totalStockQuantity;
    }

    private void stockTransactionMap(HashMap<String, String> stockBuyMap,
                                     HashMap<String, String> stockSellMap) {
        for (StockModel stock : this.getStockMap().values()) {
            if (stock.getBuy()) {
                if (stockBuyMap.containsKey(stock.getTickerSymbol())) {
                    stockBuyMap.put(stock.getTickerSymbol(), stock.getTransactionDate()
                            + " " + stock.getStockQuantity() + " "
                            + stockBuyMap.get(stock.getTickerSymbol()));
                } else {
                    stockBuyMap.put(stock.getTickerSymbol(), stock.getTransactionDate()
                            + " " + stock.getStockQuantity());
                }
            } else {
                if (stockSellMap.containsKey(stock.getTickerSymbol())) {
                    stockSellMap.put(stock.getTickerSymbol(), stock.getTransactionDate()
                            + " " + stock.getStockQuantity() + " "
                            + stockSellMap.get(stock.getTickerSymbol()));
                } else {
                    stockSellMap.put(stock.getTickerSymbol(), stock.getTransactionDate()
                            + " " + stock.getStockQuantity());
                }
            }
        }
    }
}
