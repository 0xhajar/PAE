package be.vinci.pae.dal.user;

import be.vinci.pae.business.user.UserDTO;
import be.vinci.pae.dal.DALBackServices;
import be.vinci.pae.dal.utils.DAOServices;
import be.vinci.pae.exceptions.ConflictException;
import be.vinci.pae.exceptions.FatalException;
import be.vinci.pae.exceptions.NotFoundException;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementation of UserDAO.
 */
public class UserDAOImpl implements UserDAO {

  @Inject
  private DALBackServices dalBackServices;
  @Inject
  private DAOServices daoServices;

  @Override
  public UserDTO getOneByEmail(String email) {
    try (PreparedStatement getUser = dalBackServices.getPS(
        "SELECT * FROM pae.users WHERE user_email = ?")) {
      getUser.setString(1, email);
      try (ResultSet rs = getUser.executeQuery()) {
        if (rs.next()) {
          return (UserDTO) daoServices.getDataFromRs(rs, "user");
        }
      }
    } catch (SQLException e) {
      throw new FatalException(e);
    }
    return null;
  }

  /**
   * Get a user by its id.
   *
   * @param id the id of the user
   * @return the user, null if no user was found
   */
  @Override
  public UserDTO getOneById(int id) {
    try (PreparedStatement getUser = dalBackServices.getPS(
        "SELECT * FROM pae.users WHERE user_idUser = ?")) {
      getUser.setInt(1, id);
      try (ResultSet rs = getUser.executeQuery()) {
        if (rs.next()) {
          return (UserDTO) daoServices.getDataFromRs(rs, "user");
        }
      }
    } catch (SQLException e) {
      throw new FatalException(e);
    }
    return null;
  }

  @Override
  public UserDTO addUser(UserDTO user) {
    try (PreparedStatement addUser = dalBackServices.getPS(
        "INSERT INTO pae.users (user_lastname, user_firstname, "
            + "user_email, user_password, user_phoneNumber, user_registerDate,"
            + " user_role, user_academicYear, user_profilePicture, user_version) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, 1) "
            + "RETURNING user_idUser")) {
      setPs(addUser, user);
      try (ResultSet rs = addUser.executeQuery()) {
        if (rs.next()) {
          user.setIdUser(rs.getInt(1));
          return user;
        }
      }
    } catch (SQLException e) {
      throw new FatalException(e);
    }
    return null;
  }

  @Override
  public List<Map<String, Object>> getAllUsers() {
    List<Map<String, Object>> users = new ArrayList<>();
    try (PreparedStatement getUsers = dalBackServices.getPS(
        "SELECT u.*, exists(\n"
            + "    SELECT contact_idcontact FROM pae.contacts WHERE contact_state = 'ACCEPTED' "
            + "AND u.user_iduser = contact_idstudent\n"
            + ") as \"accepted_contact\" FROM pae.users u")) {
      try (ResultSet rs = getUsers.executeQuery()) {
        while (rs.next()) {
          UserDTO user = (UserDTO) daoServices.getDataFromRs(rs, "user");
          boolean acceptedContact = rs.getBoolean("accepted_contact");
          Map<String, Object> userMap = new HashMap<>();
          userMap.put("user", user);
          userMap.put("accepted_contact", acceptedContact);
          users.add(userMap);
        }
      }
    } catch (SQLException e) {
      throw new FatalException(e);
    }
    return users;
  }

  @Override
  public UserDTO updateUser(UserDTO user) {
    try (PreparedStatement updateUser = dalBackServices.getPS(
        "UPDATE pae.users SET user_lastname = ?, user_firstname = ?,"
            + " user_email = ?, user_password = ?, user_phoneNumber = ?,"
            + " user_registerDate = ?, user_role = ?, user_academicYear = ?, "
            + " user_profilePicture = ?, "
            + " user_version = ? WHERE user_idUser = ?"
            + " AND user_version = ? RETURNING user_idUser")) {
      setPs(updateUser, user);
      updateUser.setInt(10, user.getVersion() + 1);
      updateUser.setInt(11, user.getIdUser());
      updateUser.setInt(12, user.getVersion());
      try (ResultSet rs = updateUser.executeQuery()) {
        if (getOneById(user.getIdUser()) == null) {
          throw new NotFoundException("User not found");
        }
        if (!rs.next() && getOneById(user.getIdUser()).getVersion() != user.getVersion()) {
          throw new ConflictException("Version mismatch");
        }
        return user;
      }
    } catch (SQLException e) {
      throw new FatalException(e);
    }
  }

  @Override
  public List<UserDTO> getStudents() {
    try (PreparedStatement getStudents = dalBackServices.getPS(
        "SELECT * FROM pae.users WHERE user_role = 'STUDENT'")) {
      try (ResultSet rs = getStudents.executeQuery()) {
        List<UserDTO> students = new ArrayList<>();
        while (rs.next()) {
          students.add((UserDTO) daoServices.getDataFromRs(rs, "user"));
        }
        return students;
      }
    } catch (SQLException e) {
      throw new FatalException(e);
    }
  }

  /**
   * Set the PreparedStatement.
   *
   * @param ps   the PreparedStatement
   * @param user the user
   * @throws SQLException if an error occurs while setting the PreparedStatement
   */
  private void setPs(PreparedStatement ps, UserDTO user) throws SQLException {
    ps.setString(1, user.getLastname());
    ps.setString(2, user.getFirstname());
    ps.setString(3, user.getEmail());
    ps.setString(4, user.getPassword());
    ps.setString(5, user.getPhoneNumber());
    ps.setDate(6, user.getRegisterDate());
    ps.setString(7, user.getRole().toString());
    ps.setString(8, user.getAcademicYear());
    ps.setString(9, user.getProfilePicture());
  }
}
