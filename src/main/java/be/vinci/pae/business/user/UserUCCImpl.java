package be.vinci.pae.business.user;

import be.vinci.pae.business.academicyear.AcademicYear;
import be.vinci.pae.dal.DALServices;
import be.vinci.pae.dal.user.UserDAO;
import be.vinci.pae.exceptions.BadRequestException;
import be.vinci.pae.exceptions.ConflictException;
import jakarta.inject.Inject;
import java.sql.Date;
import java.util.List;
import java.util.Map;

/**
 * Implementation of UserUCC.
 */
public class UserUCCImpl implements UserUCC {

  /**
   * Injected AcademicYear.
   */
  @Inject
  private AcademicYear academicYear;

  /**
   * Injected UserDAO.
   */
  @Inject
  private UserDAO userDAO;
  /**
   * Injected DALServices.
   */
  @Inject
  private DALServices dalServices;

  /**
   * Login a user.
   *
   * @param email    the email of the user
   * @param password the password of the user
   * @return the user, null if no user was found
   */
  @Override
  public UserDTO login(String email, String password) {
    UserDTO userFound = userDAO.getOneByEmail(email);
    dalServices.close();
    // No user found for the provided email
    if (userFound == null) {
      return null;
    }
    // Check for matching password
    if (((User) userFound).checkPassword(password)) {
      return userFound;
    }
    //Password did not match
    return null;
  }

  /**
   * Register a user.
   *
   * @param user the user to register
   * @return the registered user
   */
  public UserDTO register(UserDTO user) {
    if (!((User) user).defineRole(user.getEmail())) {
      throw new BadRequestException("Invalid role");
    }
    dalServices.start();
    UserDTO userFound = userDAO.getOneByEmail(user.getEmail());
    if (userFound != null) {
      dalServices.rollback();
      throw new ConflictException("Email already exists");
    }
    if (user.getRole().equals(UserDTO.Role.STUDENT)) {
      user.setAcademicYear(academicYear.getAcademicYear());
    }
    user.setPassword(((User) user).hashPassword(user.getPassword()));
    Date registerDate = new Date(System.currentTimeMillis());
    user.setRegisterDate(registerDate);

    user = userDAO.addUser(user);
    dalServices.commit();
    return user;
  }

  /**
   * Get a user by its id.
   *
   * @param id the id of the user
   * @return the user, null if no user was found
   */
  public UserDTO getUser(int id) {
    UserDTO user = userDAO.getOneById(id);
    dalServices.close();
    return user;
  }

  /**
   * Get all users.
   *
   * @return the list of all users
   */
  public List<Map<String, Object>> getAllUsers() {
    List<Map<String, Object>> list = userDAO.getAllUsers();
    dalServices.close();
    return list;
  }

  @Override
  public UserDTO updateUser(UserDTO user, String oldPassword, String newPassword) {
    try {
      dalServices.open();
      if (!((User) user).checkPassword(oldPassword)) {
        throw new BadRequestException("Old password is wrong");
      }
      user.setPassword(((User) user).hashPassword(newPassword));
      return userDAO.updateUser(user);
    } finally {
      dalServices.close();
    }
  }

  @Override
  public UserDTO updateUser(UserDTO authenticatedUser, String firstName, String lastName,
      String email, String telephone) {
    try {
      dalServices.open();
      authenticatedUser.setFirstname(firstName);
      authenticatedUser.setLastname(lastName);
      authenticatedUser.setEmail(email);
      authenticatedUser.setPhoneNumber(telephone);
      return userDAO.updateUser(authenticatedUser);
    } finally {
      dalServices.close();
    }
  }


  @Override
  public List<UserDTO> getStudents() {
    try {
      return userDAO.getStudents();
    } catch (Exception e) {
      dalServices.close();
      throw e;
    } finally {
      dalServices.close();
    }
  }

  @Override
  public void modifyProfilePicture(UserDTO user) {
    try {
      dalServices.open();
      userDAO.updateUser(user);
    } finally {
      dalServices.close();
    }
  }

  @Override
  public void removeProfilePicture(UserDTO user) {
    try {
      dalServices.open();
      userDAO.updateUser(user);
    } finally {
      dalServices.close();
    }
  }
}