package be.vinci.pae.business.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.sql.Date;
import org.mindrot.jbcrypt.BCrypt;

/**
 * Implementation of User which inherits of UserDTO.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserImpl implements User {

  private int idUser;
  private String firstname;
  private String lastname;
  private String email;
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private String password;
  private String phoneNumber;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  private Date registerDate;
  private Role role;
  private String academicYear;
  private String profilePicture;
  private int version;

  @Override
  public int getIdUser() {
    return idUser;
  }

  @Override
  public void setIdUser(int idUser) {
    this.idUser = idUser;
  }

  @Override
  public String getFirstname() {
    return firstname;
  }

  @Override
  public void setFirstname(String firstname) {
    this.firstname = firstname;
  }

  @Override
  public String getLastname() {
    return lastname;
  }

  @Override
  public void setLastname(String lastname) {
    this.lastname = lastname;
  }

  @Override
  public String getEmail() {
    return email;
  }

  @Override
  public void setEmail(String email) {
    this.email = email.toLowerCase();
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
  public String getPassword() {
    return password;
  }

  @Override
  public void setPassword(String password) {
    this.password = password;
  }

  @Override
  public boolean checkPassword(String password) {
    return BCrypt.checkpw(password, this.password);
  }

  @Override
  public String hashPassword(String password) {
    return BCrypt.hashpw(password, BCrypt.gensalt());
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
  public Date getRegisterDate() {
    return registerDate;
  }

  @Override
  public void setRegisterDate(Date registerDate) {
    this.registerDate = registerDate;
  }

  @Override
  public String getProfilePicture() {
    return profilePicture;
  }

  @Override
  public void setProfilePicture(String profilePicture) {
    this.profilePicture = profilePicture;
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
  public Role getRole() {
    return role;
  }

  @Override
  public void setRole(Role role) {
    this.role = role;
  }

  @Override
  public boolean defineRole(String email) {
    if (email.matches("^[a-zA-Z0-9._%+-]+\\.[a-zA-Z0-9._%+-]+@student\\.vinci\\.be$")) {
      this.role = Role.STUDENT;
      return true;
    } else {
      return !email.matches("^[a-zA-Z0-9._%+-]+\\.[a-zA-Z0-9._%+-]+@vinci\\.be$")
          || this.role != Role.STUDENT;
    }
  }

}
