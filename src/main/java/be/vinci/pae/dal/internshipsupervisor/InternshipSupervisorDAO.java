package be.vinci.pae.dal.internshipsupervisor;

import be.vinci.pae.business.internshipsupervisor.InternshipSupervisorDTO;
import java.util.List;

/**
 * Interface of InternshipSupervisorDAOImpl.
 */
public interface InternshipSupervisorDAO {

  /**
   * Get all the internship supervisors.
   *
   * @param idCompany the id of the company
   * @return all a list of internship supervisors
   */
  List<InternshipSupervisorDTO> getSupervisorsByCompanyID(int idCompany);

  /**
   * Add an internship supervisor.
   *
   * @param internshipSupervisor the internship supervisor to add
   * @return the internship supervisor added
   */
  InternshipSupervisorDTO addInternshipSupervisor(InternshipSupervisorDTO internshipSupervisor);

  /**
   * Get an internship supervisor by its id.
   *
   * @param id the id of the internship supervisor
   * @return the internship supervisor
   */
  InternshipSupervisorDTO getInternshipSupervisorById(int id);

  /**
   * Get all supervisors.
   *
   * @return the list of all supervisors
   */
  List<InternshipSupervisorDTO> getAllSupervisors();

  /**
   * Get an internship supervisor by its email.
   *
   * @param email the email of the internship supervisor
   * @return the internship supervisor or null if not found
   */
  InternshipSupervisorDTO getSupervisorByEmail(String email);
}
