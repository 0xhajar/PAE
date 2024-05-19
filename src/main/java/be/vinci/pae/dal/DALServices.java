package be.vinci.pae.dal;

/**
 * Interface of DALServicesImpl.
 */
public interface DALServices {

  /**
   * Close the connection.
   */
  void close();

  /**
   * Start the transaction.
   */
  void start();

  /**
   * Commit the transaction.
   */
  void commit();

  /**
   * Rollback the transaction.
   */
  void rollback();

  /**
   * Open the connection.
   */
  void open();
}
