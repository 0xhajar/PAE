package be.vinci.pae.business.contact;

import be.vinci.pae.business.company.CompanyDTO;
import be.vinci.pae.business.user.UserDTO;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Implementation of Contact which inherits of ContactDTO.
 */
public class ContactImpl implements Contact {

  private int idContact;
  private int idCompany;
  // Field for the company's data
  private CompanyDTO company;
  private int idStudent;
  // Field for the student's data
  @JsonProperty("student")
  private UserDTO user;
  private State state;
  private String meetPlace;
  private String refusalReason;
  private String academicYear;

  private int version;

  @Override
  public int getIdContact() {
    return idContact;
  }

  @Override
  public void setIdContact(int idContact) {
    this.idContact = idContact;
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
  public int getIdStudent() {
    return idStudent;
  }

  @Override
  public void setIdStudent(int idStudent) {
    this.idStudent = idStudent;
  }

  public UserDTO getUser() {
    return user;
  }

  public void setUser(UserDTO user) {
    this.user = user;
  }

  @Override
  public State getState() {
    return state;
  }

  @Override
  public void setState(State state) {
    this.state = state;
  }

  @Override
  public String getMeetPlace() {
    return meetPlace;
  }

  @Override
  public void setMeetPlace(String meetPlace) {
    this.meetPlace = meetPlace;
  }

  @Override
  public String getRefusalReason() {
    return refusalReason;
  }

  @Override
  public void setRefusalReason(String refusalReason) {
    this.refusalReason = refusalReason;
  }

  @Override
  public String getAcademicYear() {
    return academicYear;
  }

  @Override
  public void setAcademicYear(String academicYear) {
    this.academicYear = academicYear;
  }

  @Override
  public int getVersion() {
    return version;
  }

  @Override
  public void setVersion(int version) {
    this.version = version;
  }

  @Override
  public boolean updateState(State state) {
    if (state.equals(State.TURNED_DOWN) && this.state.equals(State.ADMITTED)) {
      this.state = state;
      return true;
    }
    if (state.equals(State.ADMITTED) && this.state.equals(State.STARTED)) {
      this.state = state;
      return true;
    }
    if (state.equals(State.UNSUPERVISED) && (this.state.equals(State.ADMITTED) || this.state.equals(
        State.STARTED))) {
      this.state = state;
      return true;
    }
    if (state.equals(State.BLACKLISTED) && (this.state.equals(State.STARTED) || this.state.equals(
        State.ADMITTED))) {
      this.state = state;
      return true;
    }
    if (state.equals(State.ACCEPTED) && this.state.equals(State.ADMITTED)) {
      this.state = state;
      return true;
    }
    if (state.equals(State.STARTED) && this.state.equals(State.UNSUPERVISED)) {
      this.state = state;
      return true;
    }
    if (state.equals(State.ON_HOLD) && (this.state.equals(State.ADMITTED) || this.state.equals(
        State.STARTED))) {
      this.state = state;
      return true;
    }
    return false;
  }
}
