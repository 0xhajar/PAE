package be.vinci.pae.business.internship;

import be.vinci.pae.business.company.CompanyDTO;
import be.vinci.pae.business.contact.ContactDTO;
import be.vinci.pae.business.contact.ContactUCC;
import be.vinci.pae.business.internshipsupervisor.InternshipSupervisorDTO;
import be.vinci.pae.business.user.UserDTO;
import be.vinci.pae.dal.DALServices;
import be.vinci.pae.dal.company.CompanyDAO;
import be.vinci.pae.dal.contact.ContactDAO;
import be.vinci.pae.dal.internship.InternshipDAO;
import be.vinci.pae.dal.internshipsupervisor.InternshipSupervisorDAO;
import be.vinci.pae.exceptions.NotFoundException;
import jakarta.inject.Inject;
import java.time.LocalDate;
import java.util.List;

/**
 * Implementation of InternshipUCC.
 */
public class InternshipUCCImpl implements InternshipUCC {

  @Inject
  private DALServices dalServices;

  @Inject
  private InternshipDAO internshipDAO;

  @Inject
  private InternshipSupervisorDAO internshipSupervisorDAO;

  @Inject
  private ContactDAO contactDAO;

  @Inject
  private CompanyDAO companyDAO;

  @Inject
  private ContactUCC contactUCC;

  /**
   * Get an internship by its id.
   *
   * @param user the user
   * @return the internship
   */
  @Override
  public InternshipDTO getInternshipByUser(UserDTO user) {
    try {
      dalServices.open();
      InternshipDTO result = internshipDAO.getInternshipById(user.getIdUser()).stream()
          .filter(
              internship -> internship.getSignatureDate().toLocalDate().getYear() == LocalDate.now()
                  .getYear()).findAny()
          .orElseThrow(() -> new NotFoundException("Internship not found for the current year"));
      return result;
    } finally {
      dalServices.close();
    }
  }

  @Override
  public InternshipDTO getInternshipById(int id) {
    try {
      dalServices.open();
      List<InternshipDTO> internships = internshipDAO.getInternshipById(id);
      if (internships.isEmpty()) {
        return null;
      }
      return internships.get(0);
    } finally {
      dalServices.close();
    }
  }

  @Override
  public InternshipDTO addInternship(InternshipDTO internship) {
    try {
      dalServices.start();
      InternshipSupervisorDTO internshipSupervisor = internshipSupervisorDAO
          .getInternshipSupervisorById(internship.getIdInternshipSupervisor());
      CompanyDTO company = companyDAO.getCompanyById(internship.getIdCompany());
      if (internshipSupervisor == null) {
        throw new NotFoundException("Internship supervisor not found");
      }
      if (company == null) {
        throw new NotFoundException("Company not found");
      }
      if (internshipDAO.getInternshipByStudentId(internship.getIdStudent()) != null) {
        throw new NotFoundException("You already have an internship");
      }
      if (internship.getIdCompany() != internshipSupervisor.getIdCompany()) {
        throw new NotFoundException("Company and internship supervisor don't match");
      }
      contactUCC.acceptContact(internship.getIdContact(), internship.getIdStudent());
      internship = internshipDAO.addInternship(internship);
      ContactDTO contact = contactDAO.getOneById(internship.getIdContact());
      internship.setContact(contact);
      if (internship.getIdCompany() != contact.getIdCompany()) {
        throw new NotFoundException("Company and contact don't match");
      }
      InternshipSupervisorDTO supervisor = internshipSupervisorDAO
          .getInternshipSupervisorById(internship.getIdInternshipSupervisor());
      internship.setInternshipSupervisor(supervisor);
      return internship;
    } catch (Exception e) {
      dalServices.rollback();
      throw e;
    } finally {
      dalServices.commit();
    }
  }

  @Override
  public InternshipDTO updateInternshipSubject(InternshipDTO internship, String subject) {
    try {
      dalServices.open();
      InternshipDTO result = internshipDAO.updateInternship(internship, subject);
      if (result == null) {
        throw new NotFoundException("Internship not found");
      }
      return result;
    } finally {
      dalServices.close();
    }
  }

}
