package be.vinci.pae.business.internshipsupervisor;

import be.vinci.pae.business.company.CompanyDTO;

/**
 * Implementation of InternshipSupervisor.
 */
public class InternshipSupervisorImpl implements InternshipSupervisor {

  private int idInternshipSupervisor;

  private String firstName;

  private String lastName;

  private String email;

  private String phoneNumber;
  private int idCompany;

  private CompanyDTO company;
  private int version;

  @Override
  public int getIdInternshipSupervisor() {
    return idInternshipSupervisor;
  }

  @Override
  public void setIdInternshipSupervisor(int idInternshipSupervisor) {
    this.idInternshipSupervisor = idInternshipSupervisor;

  }

  @Override
  public String getFirstName() {
    return firstName;
  }

  @Override
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  @Override
  public String getLastName() {
    return lastName;
  }

  @Override
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  @Override
  public String getEmail() {
    return email;
  }

  @Override
  public void setEmail(String email) {
    this.email = email;
  }

  @Override
  public String getPhoneNumber() {
    return phoneNumber;
  }

  @Override
  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  @Override
  public int getIdCompany() {
    return idCompany;
  }

  @Override
  public void setIdCompany(int idCompany) {
    this.idCompany = idCompany;
  }

  @Override
  public CompanyDTO getCompany() {
    return company;
  }

  @Override
  public void setCompany(CompanyDTO company) {
    this.company = company;
  }

  @Override
  public int getVersion() {
    return version;
  }

  @Override
  public void setVersion(int version) {
    this.version = version;
  }
}
