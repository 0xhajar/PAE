package be.vinci.pae;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import be.vinci.pae.business.Factory;
import be.vinci.pae.business.company.Company;
import be.vinci.pae.business.contact.Contact;
import be.vinci.pae.business.contact.ContactDTO.State;
import be.vinci.pae.business.internship.InternshipDTO;
import be.vinci.pae.business.internship.InternshipUCC;
import be.vinci.pae.business.internshipsupervisor.InternshipSupervisor;
import be.vinci.pae.business.user.UserDTO;
import be.vinci.pae.dal.company.CompanyDAO;
import be.vinci.pae.dal.contact.ContactDAO;
import be.vinci.pae.dal.internship.InternshipDAO;
import be.vinci.pae.dal.internshipsupervisor.InternshipSupervisorDAO;
import be.vinci.pae.exceptions.NotFoundException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 * InternshipUCC test class.
 */
public class InternshipUCCTest {

  static ServiceLocator locator;
  private static InternshipUCC internshipUCC;
  private static InternshipDAO internshipDAO;
  private static InternshipSupervisorDAO internshipSupervisorDAO;
  private static CompanyDAO companyDAO;
  private static ContactDAO contactDAO;
  private static Factory factory;
  InternshipDTO internship;

  UserDTO user;
  InternshipSupervisor internshipSupervisor;
  Company company;
  Contact contact;

  @BeforeAll
  static void beforeAll() {
    locator = ServiceLocatorUtilities.bind(new ApplicationBinderTest());
    internshipUCC = locator.getService(InternshipUCC.class);
    factory = locator.getService(Factory.class);
    internshipDAO = locator.getService(InternshipDAO.class);
    internshipSupervisorDAO = locator.getService(InternshipSupervisorDAO.class);
    companyDAO = locator.getService(CompanyDAO.class);
    contactDAO = locator.getService(ContactDAO.class);
  }

  @BeforeEach
  void setUp() {
    internship = (InternshipDTO) factory.getInternship();
    internship.setIdInternship(1);
    internship.setIdCompany(1);
    user = (UserDTO) factory.getUser();
    user.setIdUser(1);
    user.setRole(UserDTO.Role.STUDENT);
    internshipSupervisor = (InternshipSupervisor) factory.getInternshipSupervisor();
    internshipSupervisor.setIdInternshipSupervisor(1);
    internship.setIdCompany(1);
    internshipSupervisor.setIdCompany(1);
    company = (Company) factory.getCompany();
    company.setIdCompany(1);
    contact = (Contact) factory.getContact();
    contact.setIdContact(1);
    contact.setIdCompany(1);
    contact.setState(State.ADMITTED);
  }

  @Test
  @DisplayName("Test get internship by id method when the internship don't exists")
  void testGetInternshipNoInternships() {
    Mockito.when(internshipDAO.getInternshipById(user.getIdUser())).thenReturn(new ArrayList<>());
    assertThrows(NotFoundException.class, () -> internshipUCC.getInternshipByUser(user));
  }

  @Test
  @DisplayName("Test get internship by id method should not return null")
  void testGetInternship() {
    Date date = new Date(System.currentTimeMillis());
    internship.setSignatureDate(date);
    List<InternshipDTO> internships = new ArrayList<>();
    internships.add(internship);
    Mockito.when(internshipDAO.getInternshipById(user.getIdUser())).thenReturn(internships);
    assertNotNull(internshipUCC.getInternshipByUser(user), "Internship should not be null");
  }

  @Test
  @DisplayName("Test get internship by id method for the current year")
  void testGetInternshipByCurrentYear() {
    List<InternshipDTO> internships = new ArrayList<>();
    InternshipDTO internshipLastYear = (InternshipDTO) factory.getInternship();
    internshipLastYear.setSignatureDate(Date.valueOf(LocalDate.now().minusYears(1)));
    internships.add(internshipLastYear);
    InternshipDTO internshipThisYear = (InternshipDTO) factory.getInternship();
    internshipThisYear.setSignatureDate(Date.valueOf(LocalDate.now()));
    internships.add(internshipThisYear);

    Mockito.when(internshipDAO.getInternshipById(user.getIdUser())).thenReturn(internships);

    InternshipDTO result = internshipUCC.getInternshipByUser(user);
    assertNotNull(result, "Internship should not be null");
    assertEquals(internshipThisYear.getSignatureDate(), result.getSignatureDate(),
        "Internship of the current year should be returned");
  }

  @Test
  @DisplayName("Test to modify an internship subject")
  void testModifyInternshipSubject() {
    Mockito.when(internshipDAO.updateInternship(internship, "hi")).thenReturn(internship);
    assertNotNull(internshipUCC.updateInternshipSubject(internship, "hi"));
  }

  @Test
  @DisplayName("Test to modify an internship subject when the internship doesn't exist")
  void testModifyInternshipSubjectNotFound() {
    Mockito.when(internshipDAO.updateInternship(internship, "hi")).thenReturn(null);
    assertThrows(NotFoundException.class,
        () -> internshipUCC.updateInternshipSubject(internship, "hi"));
  }


  @Test
  @DisplayName("Test to add an internship when the internship supervisor doesn't exist")
  void testAddInternshipSupervisorNotFound() {
    Mockito.when(
            internshipSupervisorDAO.getInternshipSupervisorById(
                internship.getIdInternshipSupervisor()))
        .thenReturn(null);
    assertThrows(NotFoundException.class, () -> internshipUCC.addInternship(internship));
  }

  @Test
  @DisplayName("Test to add an internship when the company doesn't exist")
  void testAddInternshipCompanyNotFound() {
    Mockito.when(
            internshipSupervisorDAO.getInternshipSupervisorById(
                internship.getIdInternshipSupervisor()))
        .thenReturn(internshipSupervisor);
    Mockito.when(companyDAO.getCompanyById(internship.getIdCompany()))
        .thenReturn(null);
    assertThrows(NotFoundException.class, () -> internshipUCC.addInternship(internship));
  }

  @Test
  @DisplayName("Test to add an internship when the student already has an internship")
  void testAddInternshipStudentAlreadyHasInternship() {
    Mockito.when(
            internshipSupervisorDAO.getInternshipSupervisorById(
                internship.getIdInternshipSupervisor()))
        .thenReturn(internshipSupervisor);
    Mockito.when(companyDAO.getCompanyById(internship.getIdCompany())).thenReturn(company);
    Mockito.when(internshipDAO.getInternshipByStudentId(internship.getIdStudent()))
        .thenReturn(internship);
    assertThrows(NotFoundException.class, () -> internshipUCC.addInternship(internship));
  }

  @Test
  @DisplayName("Test to add an internship when the company and internship supervisor don't match")
  void testAddInternshipCompanyAndInternshipSupervisorDontMatch() {
    internshipSupervisor.setIdCompany(2);
    Mockito.when(
            internshipSupervisorDAO.getInternshipSupervisorById(
                internship.getIdInternshipSupervisor()))
        .thenReturn(internshipSupervisor);
    Mockito.when(internshipDAO.getInternshipByStudentId(internship.getIdStudent()))
        .thenReturn(null);
    Mockito.when(companyDAO.getCompanyById(internship.getIdCompany())).thenReturn(company);
    assertThrows(NotFoundException.class, () -> internshipUCC.addInternship(internship));
  }

  @Test
  @DisplayName("Test to add an internship when the contact and company don't match")
  void testAddInternshipContactAndCompanyDontMatch() {
    contact.setIdCompany(2);
    Mockito.when(
            internshipSupervisorDAO.getInternshipSupervisorById(
                internship.getIdInternshipSupervisor()))
        .thenReturn(internshipSupervisor);
    Mockito.when(internshipDAO.getInternshipByStudentId(internship.getIdStudent()))
        .thenReturn(null);

    Mockito.when(companyDAO.getCompanyById(internship.getIdCompany())).thenReturn(company);
    Mockito.when(internshipDAO.addInternship(internship)).thenReturn(internship);
    Mockito.when(contactDAO.getOneById(internship.getIdContact())).thenReturn(contact);
    assertThrows(NotFoundException.class, () -> internshipUCC.addInternship(internship));
  }

  @Test
  @DisplayName("Test to add an internship")
  void testAddInternship() {
    Mockito.when(
            internshipSupervisorDAO.getInternshipSupervisorById(
                internship.getIdInternshipSupervisor()))
        .thenReturn(internshipSupervisor);
    Mockito.when(internshipDAO.getInternshipByStudentId(internship.getIdStudent()))
        .thenReturn(null);
    Mockito.when(companyDAO.getCompanyById(internship.getIdCompany())).thenReturn(company);
    Mockito.when(internshipDAO.addInternship(internship)).thenReturn(internship);
    Mockito.when(contactDAO.getOneById(internship.getIdContact())).thenReturn(contact);
    assertNotNull(internshipUCC.addInternship(internship));
  }

  @Test
  @DisplayName("Test to get an internship by student id with non-existing id")
  public void testGetInternshipByIdNonExistingId() {
    when(internshipDAO.getInternshipById(1)).thenReturn(Collections.emptyList());
    InternshipDTO result = internshipUCC.getInternshipById(1);
    assertNull(result, "Returned internship should be null for non-existing id");
  }

  @Test
  @DisplayName("Test to get an internship by student id with existing id")
  public void testGetInternshipById_ExistingId() {
    InternshipDTO internship = factory.getInternship();
    internship.setIdInternship(1);
    when(internshipDAO.getInternshipById(1)).thenReturn(Arrays.asList(internship));
    InternshipDTO result = internshipUCC.getInternshipById(1);
    assertEquals(internship, result, "Returned internship should match the expected one");
  }

}