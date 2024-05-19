package be.vinci.pae.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * Interface of DALServicesImpl.
 */
public interface DALBackServices {

  /**
   * Get a prepared statement from a request.
   *
   * @param request String containing the SQL request
   * @return the prepared statement
   */
  PreparedStatement getPS(String request);

  /**
   * Get the connection to the database.
   *
   * @return the connection to the database
   */
  Connection getConnection();

}
