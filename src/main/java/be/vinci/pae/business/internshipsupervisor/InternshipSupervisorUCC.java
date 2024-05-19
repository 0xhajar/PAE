package be.vinci.pae.business.internshipsupervisor;

import java.util.List;

/**
 * Interface of InternshipSupervisorUCCImpl.
 */
public interface InternshipSupervisorUCC {

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
   * Get all supervisors.
   *
   * @return the list of all supervisors
   */
  List<InternshipSupervisorDTO> getAllSupervisors();
}
