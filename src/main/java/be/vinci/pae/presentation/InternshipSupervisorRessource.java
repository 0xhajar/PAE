package be.vinci.pae.presentation;

import be.vinci.pae.business.internshipsupervisor.InternshipSupervisorDTO;
import be.vinci.pae.business.internshipsupervisor.InternshipSupervisorUCC;
import be.vinci.pae.business.user.UserDTO.Role;
import be.vinci.pae.exceptions.BadRequestException;
import be.vinci.pae.presentation.filters.Authorize;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

/**
 * InternshipSupervisor route.
 */

@Singleton
@Path("/internshipSupervisors")
public class InternshipSupervisorRessource {

  @Inject
  private InternshipSupervisorUCC internshipSupervisorUCC;

  /**
   * Get all supervisors by a company id.
   *
   * @param idCompany the id of the company
   * @return the list of all supervisors
   */
  @GET
  @Path("/company/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize(roles = {Role.STUDENT, Role.TEACHER})
  public List<InternshipSupervisorDTO> getSupervisorsByCompanyID(@PathParam("id") int idCompany) {
    if (idCompany < 0) {
      throw new BadRequestException("Invalid id");
    }
    return internshipSupervisorUCC.getSupervisorsByCompanyID(idCompany);
  }

  /**
   * Add an internship supervisor.
   *
   * @param internshipSupervisor the internship supervisor to add
   * @return the internship supervisor added
   */
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize(roles = {Role.STUDENT})
  public InternshipSupervisorDTO addInternshipSupervisor(
      InternshipSupervisorDTO internshipSupervisor) {
    if (internshipSupervisor.getIdCompany() < 0) {
      throw new IllegalArgumentException("Invalid Company id");
    }
    if (internshipSupervisor.getFirstName() == null || internshipSupervisor.getFirstName().isEmpty()
        || internshipSupervisor.getLastName() == null
        || internshipSupervisor.getLastName().isEmpty()) {
      throw new IllegalArgumentException("Nom invalide");
    }
    if (internshipSupervisor.getPhoneNumber() == null
        || internshipSupervisor.getPhoneNumber().isEmpty()) {
      throw new IllegalArgumentException("Numéro invalide");
    }
    return internshipSupervisorUCC.addInternshipSupervisor(internshipSupervisor);
  }

  /**
   * Get all supervisors.
   *
   * @return the list of all supervisors
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize(roles = {Role.ADMIN, Role.TEACHER})
  public List<InternshipSupervisorDTO> getAllSupervisors() {
    return internshipSupervisorUCC.getAllSupervisors();
  }
}