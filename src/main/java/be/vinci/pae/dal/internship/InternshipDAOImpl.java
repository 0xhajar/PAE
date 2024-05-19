package be.vinci.pae.dal.internship;

import be.vinci.pae.business.internship.InternshipDTO;
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
 * Implementation of InternshipDAO.
 */
public class InternshipDAOImpl implements InternshipDAO {

  @Inject
  private DALBackServices dalServices;
  @Inject
  private DAOServices daoServices;

  @Override
  public InternshipDTO getOneById(int id) {
    try (PreparedStatement ps = dalServices.getPS(
        "SELECT * FROM pae.internships WHERE internship_idInternship = ?")) {
      ps.setInt(1, id);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          String prefix = "internship";
          return (InternshipDTO) daoServices.getDataFromRs(rs, prefix);
        }
      }
    } catch (SQLException e) {
      throw new FatalException(e);
    }
    return null;
  }

  /**
   * Get an internship by its id.
   *
   * @param id the id
   * @return the internship
   */
  @Override
  public List<InternshipDTO> getInternshipById(int id) {
    List<InternshipDTO> internships = new ArrayList<>();
    try (PreparedStatement ps = dalServices.getPS(
        "SELECT * FROM pae.contacts, pae.users, pae.companies, pae.internships, "
            + "pae.internshipSupervisors WHERE\n"
            + "contact_idCompany = company_idCompany AND\n"
            + "contact_idStudent = user_idUser AND\n"
            + "internshipSupervisor_idCompany = company_idCompany AND\n"
            + "internship_idStudent = user_idUser AND\n"
            + "internship_idContact = contact_idContact AND\n"
            + "internship_idInternshipSupervisor = internshipSupervisor_idInternshipSupervisor\n"
            + " AND internship_idCompany = company_idCompany AND\n"
            + "contact_idStudent = ?")) {
      ps.setInt(1, id);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          String prefix = "internship";
          internships.add((InternshipDTO) daoServices.getDataFromRs(rs, prefix));
        }
        return internships;
      }
    } catch (SQLException e) {
      throw new FatalException(e);
    }
  }

  @Override
  public InternshipDTO addInternship(InternshipDTO internship) {
    try (PreparedStatement ps = dalServices.getPS(
        "INSERT INTO pae.internships (internship_idCompany, internship_idStudent, "
            + "internship_idContact, internship_idInternshipSupervisor, "
            + "internship_internshipproject, "
            + "internship_signatureDate, internship_version) "
            + "VALUES (?, ?, ?, ?, ?, ?, 1) RETURNING internship_idInternship;")) {
      ps.setInt(1, internship.getIdCompany());
      ps.setInt(2, internship.getIdStudent());
      ps.setInt(3, internship.getIdContact());
      ps.setInt(4, internship.getIdInternshipSupervisor());
      ps.setString(5, internship.getInternshipProject());
      ps.setDate(6, internship.getSignatureDate());
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          internship.setIdInternship(rs.getInt(1));
          return internship;
        }
      }
    } catch (SQLException e) {
      throw new FatalException(e);
    }
    return null;
  }

  @Override
  public InternshipDTO getInternshipByStudentId(int id) {
    try (PreparedStatement ps = dalServices.getPS(
        "SELECT * FROM pae.internships, pae.contacts, pae.users, pae.companies, "
            + "pae.internshipSupervisors WHERE\n"
            + "internship_idCompany = company_idCompany AND\n"
            + "internship_idStudent = user_idUser AND\n"
            + "internship_idContact = contact_idContact AND\n"
            + "internship_idInternshipSupervisor = internshipSupervisor_idInternshipSupervisor\n"
            + " AND internship_idCompany = company_idCompany AND\n"
            + "contact_idStudent = user_idUser AND\n"
            + "internship_idStudent = ?")) {
      ps.setInt(1, id);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          String prefix = "internship";
          return (InternshipDTO) daoServices.getDataFromRs(rs, prefix);
        }
      }
    } catch (SQLException e) {
      throw new FatalException(e);
    }
    return null;
  }

  @Override
  public InternshipDTO updateInternship(InternshipDTO internship, String subject) {
    try (PreparedStatement updateInternship = dalServices.getPS(
        "UPDATE pae.internships SET internship_internshipproject = ?, "
            + "internship_version = internship_version + 1 "
            + "WHERE internship_idInternship = ? "
            + "AND internship_version = ? RETURNING internship_idInternship;")) {
      updateInternship.setString(1, subject);
      updateInternship.setInt(2, internship.getIdInternship());
      updateInternship.setInt(3, internship.getVersion());
      try (ResultSet rs = updateInternship.executeQuery()) {
        if (!rs.next() && getOneById(internship.getIdInternship()).getVersion()
            != internship.getVersion()) {
          throw new ConflictException("Version mismatch");
        }
        return internship;
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
