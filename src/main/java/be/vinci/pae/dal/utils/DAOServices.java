package be.vinci.pae.dal.utils;

import java.sql.ResultSet;

/**
 * Interface of DAOServicesImpl.
 */
public interface DAOServices {

  /**
   * Get data from a ResultSet.
   *
   * @param rs     the ResultSet to get the data from
   * @param prefix the prefix of the class in lowercase
   * @return the object with the data from the ResultSet
   */
  Object getDataFromRs(ResultSet rs, String prefix);
}
