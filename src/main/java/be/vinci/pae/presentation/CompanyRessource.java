package be.vinci.pae.presentation;

import be.vinci.pae.business.company.CompanyDTO;
import be.vinci.pae.business.company.CompanyUCC;
import be.vinci.pae.business.contact.ContactDTO;
import be.vinci.pae.business.contact.ContactUCC;
import be.vinci.pae.business.user.UserDTO.Role;
import be.vinci.pae.presentation.filters.Authorize;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

/**
 * Company route.
 */
@Singleton
@Path("/companies")
public class CompanyRessource {

  private final ObjectMapper jsonMapper = new ObjectMapper();
  @Inject
  private CompanyUCC companyUCC;
  @Inject
  private ContactUCC contactUCC;

  /**
   * Get all companies.
   *
   * @return the list of all companies
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize(roles = {Role.STUDENT, Role.TEACHER})
  public List<CompanyDTO> getAll() {
    return companyUCC.getAll();
  }


  /**
   * Blacklist a company.
   *
   * @param id   the id of the company
   * @param json the reason of the blacklist
   * @return the company and its contacts
   */
  @POST
  @Path("/{id}/blacklist")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize(roles = {Role.TEACHER})
  public ObjectNode blacklistCompany(@PathParam("id") int id,
      JsonNode json) {
    if (id <= 0) {
      throw new BadRequestException("L'id de l'entreprise doit être positif");
    }
    if (json.get("reason") == null || json.get("reason").asText().isEmpty()) {
      throw new BadRequestException("La raison de blacklist doit être spécifiée");
    }
    List<Object> list = companyUCC.blacklistCompany(id, json.get("reason").asText());
    ObjectNode result = jsonMapper.createObjectNode();
    result.put("company", jsonMapper.convertValue(list.get(0), ObjectNode.class));
    list.remove(0);
    ArrayNode o = jsonMapper.createArrayNode();
    for (Object object : list) {
      o.add(jsonMapper.convertValue(object, ObjectNode.class));
    }
    result.put("contacts", o);
    return result;
  }

  /**
   * Add a company.
   *
   * @param company the company to add
   * @return the added company
   */
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize(roles = {Role.STUDENT})
  public CompanyDTO addCompany(CompanyDTO company) {
    if (company.getTradeName() == null || company.getAddress() == null) {
      throw new BadRequestException("Name or adress is missing");
    }
    return companyUCC.addCompany(company);
  }

  /**
   * Get all companies with their contacts.
   *
   * @return the list of all companies with their contacts
   */
  @GET
  @Path("/contacts")
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize(roles = {Role.TEACHER})
  public ArrayNode getAllWithContacts() {
    List<CompanyDTO> companies = companyUCC.getAll();
    List<ContactDTO> contacts = contactUCC.getAllContacts();

    ArrayNode companiesArray = jsonMapper.createArrayNode();
    for (CompanyDTO company : companies) {
      ObjectNode companyNode = jsonMapper.convertValue(company, ObjectNode.class);
      ArrayNode contactsArray = jsonMapper.createArrayNode();
      for (ContactDTO contact : contacts) {
        if (contact.getIdCompany() == company.getIdCompany()) {
          contactsArray.add(jsonMapper.convertValue(contact, ObjectNode.class));
        }
      }
      companyNode.put("contacts", contactsArray);
      companiesArray.add(companyNode);
    }
    return companiesArray;
  }

}
