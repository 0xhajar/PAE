package be.vinci.pae.business.user;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.sql.Date;

/**
 * Interface of UserImpl.
 */
@JsonDeserialize(as = UserImpl.class)
public interface UserDTO {

  /**
   * Get the id of the user.
   *
   * @return the id of the user
   */
  int getIdUser();

  /**
   * Set the id of the user.
   *
   * @param idUser the id of the user
   */
  void setIdUser(int idUser);

  /**
   * Get the first name of the user.
   *
   * @return the first name of the user
   */
  String getFirstname();

  /**
   * Set the first name of the user.
   *
   * @param firstname the first name of the user
   */
  void setFirstname(String firstname);

  /**
   * Get the last name of the user.
   *
   * @return the last name of the user
   */
  String getLastname();

  /**
   * Set the last name of the user.
   *
   * @param lastname the last name of the user
   */
  void setLastname(String lastname);

  /**
   * Get the email of the user.
   *
   * @return the email of the user
   */
  String getEmail();

  /**
   * Set the email of the user.
   *
   * @param email the email of the user
   */
  void setEmail(String email);

  /**
   * Get the version of the user.
   *
   * @return the version of the user
   */
  int getVersion();

  /**
   * Set the version of the user.
   *
   * @param version the version of the user
   */
  void setVersion(int version);

  /**
   * Get the password of the user.
   *
   * @return the password of the user
   */
  String getPassword();

  /**
   * Set the password of the user.
   *
   * @param password the password of the user
   */
  void setPassword(String password);

  /**
   * Get the academic year of the user.
   *
   * @return the academic year of the user
   */
  String getAcademicYear();

  /**
   * Set the academic year of the user.
   *
   * @param academicYear the academic year of the user
   */
  void setAcademicYear(String academicYear);

  /**
   * Get the phone number of the user.
   *
   * @return the phone number of the user
   */
  String getPhoneNumber();

  /**
   * Set the phone number of the user.
   *
   * @param phoneNumber the phone number of the user
   */
  void setPhoneNumber(String phoneNumber);

  /**
   * Get the register date of the user.
   *
   * @return the register date of the user
   */
  Date getRegisterDate();

  /**
   * Set the register date of the user.
   *
   * @param registerDate the register date of the user
   */
  void setRegisterDate(Date registerDate);

  /**
   * Get the profile picture of the user.
   *
   * @return the profile picture of the user
   */
  String getProfilePicture();
  
  /**
   * Set the profile picture of the user.
   *
   * @param profilePicture the profile picture of the user
   */
  void setProfilePicture(String profilePicture);

  /**
   * Get the role of the user.
   *
   * @return the role of the user
   */
  Role getRole();

  /**
   * Set the tole of the user.
   *
   * @param role the role of the user
   */
  void setRole(Role role);

  /**
   * Enum of possible user roles.
   */
  enum Role {
    /**
     * Student role.
     */
    STUDENT("étudiant"),
    /**
     * Professor role.
     */
    TEACHER("professeur"),
    /**
     * Admin role.
     */
    ADMIN("administratif");
    private final String role;

    Role(String role) {
      this.role = role;
    }

    /**
     * Get the role as a string.
     *
     * @return the role as a string
     */
    @JsonValue
    public String getRole() {
      return role;
    }
  }
}
