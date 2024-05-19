package be.vinci.pae.business.company;

/**
 * Implementation of Company which inherits of CompanyDTO.
 */
public class CompanyImpl implements Company {

  private int idCompany;
  private String tradeName;
  private String designation;
  private String address;
  private String city;
  private String phoneNumber;
  private String email;
  private boolean blacklisted;
  private String blacklistMotivation;

  private int version;

  @Override
  public int getIdCompany() {
    return idCompany;
  }

  @Override
  public void setIdCompany(int idCompany) {
    this.idCompany = idCompany;
  }

  @Override
  public String getTradeName() {
    return tradeName;
  }

  @Override
  public void setTradeName(String tradeName) {
    this.tradeName = tradeName;
  }

  @Override
  public String getDesignation() {
    return designation;
  }

  @Override
  public void setDesignation(String designation) {
    this.designation = designation;
  }

  @Override
  public String getAddress() {
    return address;
  }

  @Override
  public void setAddress(String address) {
    this.address = address;
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
  public String getEmail() {
    return email;
  }

  @Override
  public void setEmail(String email) {
    this.email = email;
  }

  @Override
  public boolean isBlacklisted() {
    return blacklisted;
  }

  @Override
  public void setBlacklisted(boolean blacklisted) {
    this.blacklisted = blacklisted;
  }

  @Override
  public String getBlacklistMotivation() {
    return blacklistMotivation;
  }

  @Override
  public void setBlacklistMotivation(String blacklistMotivation) {
    this.blacklistMotivation = blacklistMotivation;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
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
