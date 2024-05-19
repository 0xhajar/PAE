package be.vinci.pae.dal.contact;

import be.vinci.pae.business.contact.ContactDTO;
import be.vinci.pae.dal.DALBackServices;
import be.vinci.pae.dal.utils.DAOServices;
import be.vinci.pae.exceptions.ConflictException;
import be.vinci.pae.exceptions.FatalException;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of ContactDAO.
 */
public class ContactDAOImpl implements ContactDAO {

  @Inject
  private DALBackServices dalServices;
  @Inject
  private DAOServices daoServices;

  @Override
  public ContactDTO getOneById(int id) {
    try (PreparedStatement ps = dalServices.getPS(
        "SELECT *"
            + "FROM pae.contacts, pae.users, pae.companies WHERE contact_idCompany = "
            + "company_idCompany AND contact_idStudent = user_idUser AND contact_idContact = ?;")) {
      ps.setInt(1, id);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          String prefix = "contact";
          return (ContactDTO) daoServices.getDataFromRs(rs, prefix);
        }
      }
    } catch (SQLException e) {
      throw new FatalException(e);
    }
    return null;
  }

  /**
   * Update a contact.
   *
   * @param contact the contact to refuse
   */
  @Override
  public void updateContact(ContactDTO contact) {
    try (PreparedStatement ps = dalServices.getPS(
        "UPDATE pae.contacts "
            + "SET contact_idCompany = ?, contact_idStudent = ?, contact_state = ?, "
            + "contact_meetPlace = ?, contact_refusalReason = ?, contact_academicYear = ?,"
            + " contact_version = ?"
            + "WHERE contact_idContact = ? AND contact_version = ? RETURNING contact_idContact;")) {
      ps.setInt(1, contact.getIdCompany());
      ps.setInt(2, contact.getIdStudent());
      ps.setString(3, contact.getState().toString());
      ps.setString(4, contact.getMeetPlace());
      ps.setString(5, contact.getRefusalReason());
      ps.setString(6, contact.getAcademicYear());
      ps.setInt(7, contact.getVersion() + 1);
      ps.setInt(8, contact.getIdContact());
      ps.setInt(9, contact.getVersion());
      try (ResultSet rs = ps.executeQuery()) {
        if (!rs.next() && getOneById(contact.getIdContact()).getVersion() != contact.getVersion()) {
          throw new ConflictException("Version mismatch");
        }
      }
    } catch (SQLException e) {
      throw new FatalException(e);
    }
  }

  @Override
  public ContactDTO addContact(ContactDTO contact) {
    try (PreparedStatement ps = dalServices.getPS(
        "INSERT INTO pae.contacts (contact_idCompany, contact_idStudent, contact_state, "
            + "contact_academicYear, contact_version) "
            + "VALUES (?, ?, ?, ?, 1) RETURNING contact_idContact;")) {
      ps.setInt(1, contact.getIdCompany());
      ps.setInt(2, contact.getIdStudent());
      ps.setString(3, contact.getState().toString());
      ps.setString(4, contact.getAcademicYear());
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          contact.setIdContact(rs.getInt(1));
          return contact;
        }
      }
    } catch (SQLException e) {
      throw new FatalException(e);
    }
    return null;
  }

  @Override
  public ContactDTO getContactAccepted(int idUser) {
    try (PreparedStatement ps = dalServices.getPS(
        "SELECT * FROM pae.contacts, pae.users, pae.companies WHERE contact_idCompany = "
            + "company_idCompany AND contact_idStudent = user_idUser AND "
            + "contact_idStudent = ? AND "
            + "contact_state = 'ACCEPTED';")) {
      ps.setInt(1, idUser);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          String prefix = "contact";
          return (ContactDTO) daoServices.getDataFromRs(rs, prefix);
        }
      }
    } catch (SQLException e) {
      throw new FatalException(e);
    }

    return null;
  }

  @Override
  public ContactDTO getCompanyContact(int idUser, int idCompany, String academicYear) {
    try (PreparedStatement ps = dalServices.getPS(
        "SELECT * FROM pae.contacts, pae.users, pae.companies WHERE contact_idCompany = "
            + "company_idCompany AND contact_idStudent = user_idUser AND contact_idStudent = ? AND"
            + " contact_idCompany = ? AND contact_academicYear = ?;")) {
      ps.setInt(1, idUser);
      ps.setInt(2, idCompany);
      ps.setString(3, academicYear);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          String prefix = "contact";
          return (ContactDTO) daoServices.getDataFromRs(rs, prefix);
        }
      }
    } catch (SQLException e) {
      throw new FatalException(e);
    }
    return null;
  }

  @Override
  public List<ContactDTO> getContactsByCompany(int idCompany) {
    try (PreparedStatement ps = dalServices.getPS(
        "SELECT * FROM pae.contacts, pae.users, pae.companies WHERE contact_idCompany = "
            + "company_idCompany AND contact_idStudent = user_idUser AND "
            + "contact_idCompany = ?;")) {
      ps.setInt(1, idCompany);
      try (ResultSet rs = ps.executeQuery()) {
        List<ContactDTO> contacts = new ArrayList<>();
        while (rs.next()) {
          String prefix = "contact";
          contacts.add((ContactDTO) daoServices.getDataFromRs(rs, prefix));
        }
        return contacts;
      }
    } catch (SQLException e) {
      throw new FatalException(e);
    }
  }


  /**
   * Get the contacts of a student.
   *
   * @param idStudent the id of the student
   * @return the contacts
   */
  @Override
  public List<ContactDTO> getContactsByStudentId(int idStudent) {
    List<ContactDTO> contacts = new ArrayList<>();
    try (PreparedStatement ps = dalServices.getPS(
        "SELECT * FROM pae.contacts, pae.users, pae.companies WHERE contact_idCompany = "
            + "company_idCompany AND contact_idStudent = user_idUser AND "
            + "contact_idStudent = ?;")) {
      ps.setInt(1, idStudent);
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          String prefix = "contact";
          contacts.add((ContactDTO) daoServices.getDataFromRs(rs, prefix));
        }
        return contacts;
      }
    } catch (SQLException e) {
      throw new FatalException(e);
    }
  }

  /**
   * Get all the contacts.
   *
   * @return the contacts
   */
  @Override
  public List<ContactDTO> getAllContacts() {
    List<ContactDTO> contacts = new ArrayList<>();
    try {
      PreparedStatement ps = dalServices.getPS(
          "SELECT * FROM pae.contacts, pae.users u, pae.companies com WHERE "
              + "contact_idCompany = "
              + "company_idCompany AND contact_idStudent = user_idUser");
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          String prefix = "contact";
          contacts.add((ContactDTO) daoServices.getDataFromRs(rs, prefix));
        }
        return contacts;
      }
    } catch (SQLException e) {
      throw new FatalException(e);
    }
  }
}
