package be.vinci.pae.business.user;

import java.util.List;
import java.util.Map;

/**
 * Interface of UserUCCImpl.
 */
public interface UserUCC {

  /**
   * Try to log in a user.
   *
   * @param email    the email of the user
   * @param password the password of the user
   * @return the logged user, null if no user was found
   */
  UserDTO login(String email, String password);

  /**
   * Register a user.
   *
   * @param user the user to register
   * @return the registered user
   */
  UserDTO register(UserDTO user);

  /**
   * Get a user by its id.
   *
   * @param id the id of the user
   * @return the user, null if no user was found
   */
  UserDTO getUser(int id);

  /**
   * Get all users.
   *
   * @return the list of all users
   */
  List<Map<String, Object>> getAllUsers();

  /**
   * Update the user.
   *
   * @param user        the user to update
   * @param oldPassword the old password of the user
   * @param newPassword the new password of the user
   * @return the updated user
   */
  UserDTO updateUser(UserDTO user, String oldPassword, String newPassword);


  /**
   * Update the user.
   *
   * @param authenticatedUser the authenticated user
   * @param firstName         the first name of the user
   * @param lastName          the last name of the user
   * @param email             the email of the user
   * @param telephone         the telephone of the user
   * @return the updated user
   */
  UserDTO updateUser(UserDTO authenticatedUser, String firstName, String lastName, String email,
      String telephone);

  /**
   * Get all students.
   *
   * @return the list of all students
   */
  List<UserDTO> getStudents();

  /**
   * Update the user's profile picture.
   *
   * @param user the user to update
   */
  void modifyProfilePicture(UserDTO user);

  /**
   * Remove the user's profile picture.
   *
   * @param user the user to update
   */
  void removeProfilePicture(UserDTO user);
}
