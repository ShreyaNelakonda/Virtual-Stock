package service.user;

import java.io.IOException;
import java.util.HashMap;

import model.PortfolioModel;
import viewmodel.PortfolioViewModel;

/**
 * Interface that provides services to access UserDB.
 */
public interface UserDataService {

  /**
   * Creates an empty file in the system.
   * If files already exists, then it overwrites content to blank.
   *
   * @throws IOException when file creation fails
   */
  void createOverrideEmptyFile() throws IOException;

  /**
   * Method to load data from file database which was saved previously.
   */
  void loadExistingPortfolio();

  /**
   * Persists portfolio created into the User profile in the file database.
   *
   * @param flex type of portfolio
   * @param portfolio portfolio object to be saved
   * @param update    files needs to be updated or overwritten
   * @throws IOException when IO operation fails while accessing / reading the file
   */
  void savePortfolioToFile(PortfolioViewModel portfolio, boolean flex, boolean update)
          throws IOException;

  /**
   * Method loads list of portfolios and its collection of stocks from an external csv file or
   * from the file database and associates it to the given User.
   *
   * @param filePath file path to be loaded
   * @return map of portfolio model and its name
   * @throws IOException when file operations fail
   */
  HashMap<String, PortfolioModel> loadUserProfile(String filePath) throws IOException;
}
