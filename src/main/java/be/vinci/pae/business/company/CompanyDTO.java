package be.vinci.pae.business.company;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Interface of CompanyImpl.
 */
@JsonDeserialize(as = CompanyImpl.class)
public interface CompanyDTO {

  /**
   * Get the id of the company.
   *
   * @return the id of the company
   */
  int getIdCompany();

  /**
   * Set the id of the company.
   *
   * @param idCompany the id of the company
   */
  void setIdCompany(int idCompany);

  /**
   * Get the trade name of the company.
   *
   * @return the trade name of the company
   */
  String getTradeName();

  /**
   * Set the trade name of the company.
   *
   * @param tradeName the trade name of the company
   */
  void setTradeName(String tradeName);

  /**
   * Get the designation of the company.
   *
   * @return the designation of the company
   */
  String getDesignation();

  /**
   * Set the designation of the company.
   *
   * @param designation the designation of the company
   */
  void setDesignation(String designation);

  /**
   * Get the address of the company.
   *
   * @return the address of the company
   */
  String getAddress();

  /**
   * Set the city of the company.
   *
   * @param city the city of the company
   */
  void setCity(String city);

  /**
   * Get the city of the company.
   *
   * @return the city of the company
   */
  String getCity();

  /**
   * Set the address of the company.
   *
   * @param address the address of the company
   */
  void setAddress(String address);

  /**
   * Get the phone number of the company.
   *
   * @return the phone number of the company
   */
  String getPhoneNumber();

  /**
   * Set the phone number of the company.
   *
   * @param phoneNumber the phone number of the company
   */
  void setPhoneNumber(String phoneNumber);

  /**
   * Get the email of the company.
   *
   * @return the email of the company
   */
  String getEmail();

  /**
   * Set the email of the company.
   *
   * @param email the email of the company
   */
  void setEmail(String email);

  /**
   * Get the blacklisted status of the company.
   *
   * @return the blacklisted status of the company
   */
  boolean isBlacklisted();

  /**
   * Set the blacklisted status of the company.
   *
   * @param blacklisted the blacklisted status of the company
   */
  void setBlacklisted(boolean blacklisted);

  /**
   * Get the blacklisted motivation of the company.
   *
   * @return the blacklisted motivation of the company
   */
  String getBlacklistMotivation();

  /**
   * Set the blacklisted motivation of the company.
   *
   * @param blacklistedMotivation the blacklisted motivation of the company
   */
  void setBlacklistMotivation(String blacklistedMotivation);

  /**
   * Get the version of the company.
   *
   * @return the version of the company
   */
  int getVersion();

  /**
   * Set the version of the company.
   *
   * @param version the version of the company
   */
  void setVersion(int version);
}
