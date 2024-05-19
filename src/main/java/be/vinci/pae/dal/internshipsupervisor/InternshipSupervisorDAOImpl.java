package be.vinci.pae.dal.internshipsupervisor;

import be.vinci.pae.business.internshipsupervisor.InternshipSupervisorDTO;
import be.vinci.pae.dal.DALBackServices;
import be.vinci.pae.dal.utils.DAOServices;
import be.vinci.pae.exceptions.FatalException;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of InternshipSupervisorDAOImpl.
 */
public class InternshipSupervisorDAOImpl implements InternshipSupervisorDAO {

  @Inject
  private DALBackServices dalServices;

  @Inject
  private DAOServices daoServices;

  @Override
  public List<InternshipSupervisorDTO> getSupervisorsByCompanyID(int idCompany) {
    try (PreparedStatement ps = dalServices.getPS(
        "SELECT * FROM pae.internshipSupervisors, pae.companies WHERE "
            + "internshipsupervisor_idCompany = company_idCompany AND "
            + "internshipsupervisor_idCompany=?;")) {
      ps.setInt(1, idCompany);
      return getInternshipSupervisorDTOS(ps);
    } catch (SQLException e) {
      throw new FatalException(e);
    }
  }

  @Override
  public InternshipSupervisorDTO addInternshipSupervisor(
      InternshipSupervisorDTO internshipSupervisor) {
    try (PreparedStatement ps = dalServices.getPS(
        "INSERT INTO pae.internshipSupervisors (internshipsupervisor_idCompany, "
            + "internshipsupervisor_firstname, internshipSupervisor_lastname, "
            + "internshipsupervisor_email, internshipsupervisor_phoneNumber, "
            + "internshipSupervisor_version)"
            + " VALUES (?, ?, ?, ?, ?, 1) RETURNING "
            + "internshipsupervisor_idInternshipSupervisor;")) {
      ps.setInt(1, internshipSupervisor.getIdCompany());
      ps.setString(2, internshipSupervisor.getFirstName());
      ps.setString(3, internshipSupervisor.getLastName());
      ps.setString(4, internshipSupervisor.getEmail());
      ps.setString(5, internshipSupervisor.getPhoneNumber());
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          internshipSupervisor.setIdInternshipSupervisor(rs.getInt(1));
          return internshipSupervisor;
        }
      }
    } catch (SQLException e) {
      throw new FatalException(e);
    }
    return null;
  }

  @Override
  public InternshipSupervisorDTO getInternshipSupervisorById(int id) {
    try (PreparedStatement ps = dalServices.getPS(
        "SELECT * FROM pae.internshipSupervisors, pae.companies WHERE "
            + "internshipsupervisor_idCompany = company_idCompany AND "
            + "internshipsupervisor_idInternshipSupervisor=?;")) {
      ps.setInt(1, id);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          String prefix = "internshipSupervisor";
          return (InternshipSupervisorDTO) daoServices.getDataFromRs(rs, prefix);
        }
      }
    } catch (SQLException e) {
      throw new FatalException(e);
    }
    return null;
  }

  @Override
  public List<InternshipSupervisorDTO> getAllSupervisors() {
    try (PreparedStatement ps = dalServices.getPS(
        "SELECT * FROM pae.internshipSupervisors, pae.companies WHERE "
            + "internshipsupervisor_idCompany = company_idCompany")) {
      return getInternshipSupervisorDTOS(ps);
    } catch (SQLException e) {
      throw new FatalException(e);
    }
  }

  @Override
  public InternshipSupervisorDTO getSupervisorByEmail(String email) {
    try (PreparedStatement ps = dalServices.getPS(
        "SELECT * FROM pae.internshipSupervisors, pae.companies WHERE "
            + "internshipsupervisor_idCompany = company_idCompany AND "
            + "internshipsupervisor_email=?;")) {
      ps.setString(1, email);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          String prefix = "internshipSupervisor";
          return (InternshipSupervisorDTO) daoServices.getDataFromRs(rs, prefix);
        }
      }
    } catch (SQLException e) {
      throw new FatalException(e);
    }
    return null;
  }

  private List<InternshipSupervisorDTO> getInternshipSupervisorDTOS(PreparedStatement ps)
      throws SQLException {
    try (ResultSet rs = ps.executeQuery()) {
      List<InternshipSupervisorDTO> list = new ArrayList<>();
      while (rs.next()) {
        String prefix = "internshipSupervisor";
        list.add((InternshipSupervisorDTO) daoServices.getDataFromRs(rs, prefix));
      }
      return list;
    }
  }
}
