package be.vinci.pae.business.internshipsupervisor;

import be.vinci.pae.business.company.CompanyDTO;
import be.vinci.pae.dal.DALServices;
import be.vinci.pae.dal.company.CompanyDAO;
import be.vinci.pae.dal.internshipsupervisor.InternshipSupervisorDAO;
import be.vinci.pae.exceptions.ConflictException;
import be.vinci.pae.exceptions.NotFoundException;
import jakarta.inject.Inject;
import java.util.List;

/**
 * Implementation of InternshipSupervisorUCC.
 */
public class InternshipSupervisorUCCImpl implements InternshipSupervisorUCC {

  @Inject
  private DALServices dalServices;

  @Inject
  private InternshipSupervisorDAO internshipSupervisorDAO;

  @Inject
  private CompanyDAO companyDAO;

  @Override
  public List<InternshipSupervisorDTO> getSupervisorsByCompanyID(int idCompany) {
    try {
      dalServices.open();
      CompanyDTO company = companyDAO.getCompanyById(idCompany);
      if (company == null) {
        throw new NotFoundException("Company not found");
      }
      return internshipSupervisorDAO.getSupervisorsByCompanyID(idCompany);
    } finally {
      dalServices.close();
    }
  }

  @Override
  public InternshipSupervisorDTO addInternshipSupervisor(
      InternshipSupervisorDTO internshipSupervisor) {
    try {
      dalServices.open();
      CompanyDTO companyDTO = companyDAO.getCompanyById(internshipSupervisor.getIdCompany());
      if (companyDTO == null) {
        throw new NotFoundException("L'entreprise n'existe pas.");
      }
      InternshipSupervisorDTO internshipSupervisorDTO = internshipSupervisorDAO
          .getSupervisorByEmail(internshipSupervisor.getEmail());
      if (internshipSupervisorDTO != null) {
        throw new ConflictException("Un superviseur avec cet email existe déjà.");
      }
      internshipSupervisor = internshipSupervisorDAO.addInternshipSupervisor(internshipSupervisor);
      internshipSupervisor.setCompany(companyDTO);
      return internshipSupervisor;
    } finally {
      dalServices.close();
    }
  }

  @Override
  public List<InternshipSupervisorDTO> getAllSupervisors() {
    try {
      dalServices.open();
      return internshipSupervisorDAO.getAllSupervisors();
    } finally {
      dalServices.close();
    }
  }
}
