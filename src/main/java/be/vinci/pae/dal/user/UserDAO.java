package be.vinci.pae.dal.user;

import be.vinci.pae.business.user.UserDTO;
import java.util.List;
import java.util.Map;

/**
 * Interface of UserDAOImpl.
 */
public interface UserDAO {

  /**
   * Get one user from email.
   *
   * @param email the email of the user
   * @return the user found, null if no user was found
   */
  UserDTO getOneByEmail(String email);

  /**
   * Get one user from id.
   *
   * @param id the id of the user
   * @return the user found, null if no user was found
   */
  UserDTO getOneById(int id);

  /**
   * Add a user to the database.
   *
   * @param user the user to add
   * @return the user added
   */
  UserDTO addUser(UserDTO user);

  /**
   * Get all users.
   *
   * @return the list of all users
   */
  List<Map<String, Object>> getAllUsers();

  /**
   * Update a user in the database.
   *
   * @param user the user to update
   * @return the updated user
   */
  UserDTO updateUser(UserDTO user);

  /**
   * Get all students.
   *
   * @return the list of all students
   */
  List<UserDTO> getStudents();
}
