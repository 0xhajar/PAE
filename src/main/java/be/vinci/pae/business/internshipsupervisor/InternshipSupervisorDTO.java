package be.vinci.pae.business.internshipsupervisor;

import be.vinci.pae.business.company.CompanyDTO;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * The Interface InternshipSupervisorDTO.
 */
@JsonDeserialize(as = InternshipSupervisorImpl.class)
public interface InternshipSupervisorDTO {

  /**
   * getter for idInternshipSupervisor.
   *
   * @return the idInternshipSupervisor
   */
  int getIdInternshipSupervisor();

  /**
   * setter for idInternshipSupervisor.
   *
   * @param idInternshipSupervisor the idInternshipSupervisor to set
   */
  void setIdInternshipSupervisor(int idInternshipSupervisor);

  /**
   * getter for firstName.
   *
   * @return the firstName
   */
  String getFirstName();

  /**
   * setter for firstName.
   *
   * @param firstName the firstName to set
   */
  void setFirstName(String firstName);

  /**
   * getter for lastName.
   *
   * @return the lastName
   */
  String getLastName();

  /**
   * setter for lastName.
   *
   * @param lastName the lastName to set
   */
  void setLastName(String lastName);

  /**
   * getter for email.
   *
   * @return the email
   */
  String getEmail();

  /**
   * setter for email.
   *
   * @param email the email to set
   */
  void setEmail(String email);

  /**
   * getter for phoneNumber.
   *
   * @return the phoneNumber
   */
  String getPhoneNumber();

  /**
   * setter for phoneNumber.
   *
   * @param phoneNumber the phoneNumber to set
   */
  void setPhoneNumber(String phoneNumber);

  /**
   * getter for idCompany.
   *
   * @return the idCompany
   */
  int getIdCompany();

  /**
   * setter for idCompany.
   *
   * @param idCompany the idCompany to set
   */
  void setIdCompany(int idCompany);

  /**
   * getter for company.
   *
   * @return the company
   */
  CompanyDTO getCompany();

  /**
   * setter for company.
   *
   * @param company the company to set
   */
  void setCompany(CompanyDTO company);

  /**
   * getter for version.
   *
   * @return the version
   */
  int getVersion();

  /**
   * setter for version.
   *
   * @param version the version to set
   */
  void setVersion(int version);
}
