package be.vinci.pae;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import be.vinci.pae.business.Factory;
import be.vinci.pae.business.academicyear.AcademicYear;
import be.vinci.pae.business.company.Company;
import be.vinci.pae.business.contact.Contact;
import be.vinci.pae.business.contact.ContactDTO;
import be.vinci.pae.business.contact.ContactDTO.State;
import be.vinci.pae.business.contact.ContactUCC;
import be.vinci.pae.business.user.User;
import be.vinci.pae.business.user.UserDTO;
import be.vinci.pae.dal.company.CompanyDAO;
import be.vinci.pae.dal.contact.ContactDAO;
import be.vinci.pae.exceptions.NotFoundException;
import be.vinci.pae.exceptions.PreconditionFailedException;
import jakarta.ws.rs.WebApplicationException;
import java.util.ArrayList;
import java.util.List;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 * The type Contact ucc test.
 */
public class ContactUCCTest {

  static Factory factory;
  private static ContactUCC contactUCC;
  private static ContactDAO contactDAO;
  private static CompanyDAO companyDAO;
  private static AcademicYear academicYear;
  private final int idContact = 1;
  private final int idUser = 1;
  private final String refusalReason = "refusalReason";
  Contact contact;
  Company company;
  User user;

  @BeforeAll
  static void beforeAll() {
    ServiceLocator locator = ServiceLocatorUtilities.bind(new ApplicationBinderTest());
    contactUCC = locator.getService(ContactUCC.class);
    contactDAO = locator.getService(ContactDAO.class);
    companyDAO = locator.getService(CompanyDAO.class);
    academicYear = locator.getService(AcademicYear.class);
    factory = locator.getService(Factory.class);
  }

  @BeforeEach
  void setUp() {
    contact = (Contact) factory.getContact();
    contact.setIdContact(idContact);
    contact.setIdCompany(1);
    contact.setIdStudent(idUser);
    contact.setState(State.STARTED);
    Mockito.when(contactDAO.getOneById(1)).thenReturn(contact);

    company = (Company) factory.getCompany();

    Mockito.when(companyDAO.getCompanyById(1)).thenReturn(company);

    user = (User) factory.getUser();
    user.setIdUser(idUser);
    user.setRole(UserDTO.Role.STUDENT);
  }

  @Test
  @DisplayName("Test addContact with all conditions met")
  void testAddContact() {
    contact.setAcademicYear("2021-2022");
    Mockito.when(contactDAO.addContact(contact)).thenReturn(contact);
    Mockito.when(contactDAO.getContactAccepted(idUser)).thenReturn(null);
    Mockito.when(contactDAO.getCompanyContact(idUser, 1, academicYear.getAcademicYear()))
        .thenReturn(null);
    assertAll(
        () -> assertNotNull(contactUCC.addContact(contact)),
        () -> assertEquals(State.STARTED, contact.getState())
    );
  }

  @Test
  @DisplayName("Test addContact with existing contact with the same company and academic year")
  void testAddContactExistingContactSameCompanyAndAcademicYear() {
    ContactDTO contact1 = factory.getContact();
    contact1.setIdCompany(1);
    contact1.setIdStudent(idUser);
    Mockito.when(contactDAO.getCompanyContact(idUser, 1,
        academicYear.getAcademicYear())).thenReturn(contact1);
    assertThrows(PreconditionFailedException.class, () -> contactUCC.addContact(contact1));
  }

  @Test
  @DisplayName("Test addContact when there is already a contact accepted")
  void testAddContactAlreadyAccepted() {
    Mockito.when(contactDAO.getContactAccepted(idUser)).thenReturn(contact);
    assertThrows(PreconditionFailedException.class, () -> contactUCC.addContact(contact));
  }

  @Test
  @DisplayName("Test refuseContact with contact in wrong state")
  void testRefuseContactWrongState() {
    contact.setState(State.TURNED_DOWN);
    assertThrows(WebApplicationException.class,
        () -> contactUCC.refuseContact(idContact, refusalReason, idUser));
  }

  @Test
  @DisplayName("Test refuseContact with contact that doesn't belong to the user")
  void testRefuseContactWrongUser() {
    contact.setIdStudent(2);
    assertThrows(NotFoundException.class,
        () -> contactUCC.refuseContact(idContact, refusalReason, idUser));
  }

  @Test
  @DisplayName("Test refuseContact with contact not found")
  void testRefuseContactNotFound() {
    Mockito.when(contactDAO.getOneById(1)).thenReturn(null);
    assertNull(contactUCC.refuseContact(idContact, refusalReason, idUser));
  }

  @Test
  @DisplayName("Test refuseContact with existing contact in right state")
  void refuseContactTest() {
    contact.setState(State.ADMITTED);
    contactUCC.refuseContact(idContact, refusalReason, idUser);
    assertAll(
        () -> assertEquals(State.TURNED_DOWN, contact.getState()),
        () -> assertEquals(refusalReason, contact.getRefusalReason())
    );
  }


  @Test
  @DisplayName("Test addContact with company not found")
  void testAddContactCompanyNotFound() {
    contact.setIdCompany(0);
    assertThrows(WebApplicationException.class, () -> contactUCC.addContact(contact));
  }

  @Test
  @DisplayName("Test meetContact with contact in wrong state")
  void testMeetContactWrongState() {
    contact.setState(State.ADMITTED);
    assertThrows(WebApplicationException.class,
        () -> contactUCC.meetContact(idContact, "meetPlace", idUser));
  }

  @Test
  @DisplayName("Test meetContact with contact not found")
  void testMeetContactNotFound() {
    Mockito.when(contactDAO.getOneById(1)).thenReturn(null);
    assertNull(contactUCC.meetContact(idContact, "meetPlace", idUser));
  }

  @Test
  @DisplayName("Test meetContact with non-matching user id")
  void testMeetContactNonMatchingUserId() {
    int nonMatchingIdUser = 2;
    assertThrows(NotFoundException.class,
        () -> contactUCC.meetContact(idContact, "meetPlace", nonMatchingIdUser));
  }

  @Test
  @DisplayName("Test meetContact with existing contact in right state")
  void testMeetContact() {
    contact.setState(State.STARTED);
    assertAll(
        () -> assertEquals(contact.getIdContact(),
            contactUCC.meetContact(idContact, "meetPlace", idUser).getIdContact()),
        () -> assertEquals(State.ADMITTED, contact.getState()),
        () -> assertEquals("meetPlace", contact.getMeetPlace())
    );
  }

  @Test
  @DisplayName("Test unfollow contact with contact not found")
  void testUnfollowContactNotFound() {
    Mockito.when(contactDAO.getOneById(1)).thenReturn(null);
    assertNull(contactUCC.unfollowContact(idContact, idUser));
  }

  @Test
  @DisplayName("Test unfollow contact with non-matching user id")
  void testUnfollowContactNonMatchingUserId() {
    int nonMatchingIdUser = 2;
    assertThrows(NotFoundException.class,
        () -> contactUCC.unfollowContact(idContact, nonMatchingIdUser));
  }

  @Test
  @DisplayName("Test unfollow contact with contact in wrong state")
  void testUnfollowContactWrongState() {
    contact.setState(State.TURNED_DOWN);
    assertThrows(PreconditionFailedException.class,
        () -> contactUCC.unfollowContact(idContact, idUser));
  }

  @Test
  @DisplayName("Test unfollow contact with existing contact in right state")
  void testUnfollowContact() {
    contact.setState(State.ADMITTED);
    assertAll(
        () -> assertEquals(contact.getIdContact(),
            contactUCC.unfollowContact(idContact, idUser).getIdContact()),
        () -> assertEquals(State.UNSUPERVISED, contact.getState())
    );
  }

  @Test
  @DisplayName("test for getContactsByCompany method")
  void getContactsByCompanyTest() {
    int idCompany = 1;
    Mockito.when(contactDAO.getContactsByCompany(idCompany)).thenReturn(new ArrayList<>());
    assertNotNull(contactUCC.getContactsByCompany(idCompany));
  }

  @Test
  @DisplayName("test for getContactsByCompany method with wrong company id")
  void getContactsByCompanyWrongIdTest() {
    int idCompany = 1;
    Mockito.when(companyDAO.getCompanyById(idCompany)).thenReturn(null);
    assertThrows(NotFoundException.class, () -> contactUCC.getContactsByCompany(idCompany));
  }

  @Test
  @DisplayName("test for blacklistContacts method")
  void blacklistContactsTest() {
    List<ContactDTO> contacts = new ArrayList<>();
    ContactDTO contact = factory.getContact();
    contact.setState(State.ADMITTED);
    contacts.add(contact);
    ContactDTO contact1 = factory.getContact();
    contact1.setState(State.TURNED_DOWN);
    contacts.add(contact1);
    int idCompany = 1;
    Mockito.when(contactDAO.getContactsByCompany(idCompany)).thenReturn(contacts);
    assertNotNull(contactUCC.blacklistContacts(idCompany));
  }

  @Test
  @DisplayName("test for blacklistContacts method with wrong company id")
  void blacklistContactsTestWrongId() {
    int idCompany = 1;
    Mockito.when(companyDAO.getCompanyById(idCompany)).thenReturn(null);
    assertThrows(NotFoundException.class, () -> contactUCC.blacklistContacts(idCompany));
  }

  @Test
  @DisplayName("test acceptContact method with contact in wrong state")
  void testAcceptContactWrongState() {
    contact.setState(State.STARTED);
    assertThrows(PreconditionFailedException.class,
        () -> contactUCC.acceptContact(idContact, idUser));
  }

  @Test
  @DisplayName("test acceptContact method with contact not found")
  void testAcceptContactNotFound() {
    Mockito.when(contactDAO.getOneById(1)).thenReturn(null);
    assertThrows(NotFoundException.class, () -> contactUCC.acceptContact(idContact, idUser));
  }

  @Test
  @DisplayName("test acceptContact method with non-matching user id")
  void testAcceptContactNonMatchingUserId() {
    int nonMatchingIdUser = 2;
    assertThrows(NotFoundException.class,
        () -> contactUCC.acceptContact(idContact, nonMatchingIdUser));
  }

  @Test
  @DisplayName("test acceptContact method with existing contact in right state")
  void testAcceptContact() {
    contact.setState(State.ADMITTED);
    assertAll(
        () -> assertEquals(contact.getIdContact(),
            contactUCC.acceptContact(idContact, idUser).getIdContact()),
        () -> assertEquals(State.ACCEPTED, contact.getState())
    );
  }

  @Test
  @DisplayName("Test acceptContact method with multiple contacts")
  void testAcceptContactMultipleContacts() {
    contact.setState(State.ADMITTED);
    ContactDTO anotherContact = factory.getContact();
    anotherContact.setState(State.ADMITTED);
    ContactDTO anotherContact1 = factory.getContact();
    anotherContact1.setState(State.TURNED_DOWN);
    List<ContactDTO> contacts = new ArrayList<>();
    contacts.add(contact);
    contacts.add(anotherContact);
    contacts.add(anotherContact1);
    Mockito.when(contactDAO.getContactsByStudentId(idUser)).thenReturn(contacts);

    ContactDTO acceptedContact = contactUCC.acceptContact(idContact, idUser);

    assertAll(
        () -> assertEquals(contact.getIdContact(), acceptedContact.getIdContact()),
        () -> assertEquals(State.ACCEPTED, acceptedContact.getState()),
        () -> assertEquals(State.ON_HOLD, anotherContact.getState())
    );
  }

  @Test
  @DisplayName("Test to get all contacts")
  void testGetAllContacts() {
    List<ContactDTO> contacts = new ArrayList<>();
    Mockito.when(contactDAO.getAllContacts()).thenReturn(contacts);
    assertNotNull(contactUCC.getAllContacts());
  }

  @Test
  @DisplayName("Test get contact by student id")
  void testGetContactByStudentId() {
    List<ContactDTO> contacts = new ArrayList<>();
    Mockito.when(contactDAO.getContactsByStudentId(idUser)).thenReturn(contacts);
    assertNotNull(contactUCC.getContactsByStudentId(idUser));
  }

  @Test
  @DisplayName("Test follow a contact which doesn't exist")
  void testFollowContactNotFound() {
    Mockito.when(contactDAO.getOneById(1)).thenReturn(null);
    assertThrows(NotFoundException.class, () -> contactUCC.followContact(idContact, idUser));
  }

  @Test
  @DisplayName("Test follow a contact which doesnt belong to the user")
  void testFollowContactWrongUser() {
    contact.setIdStudent(2);
    assertThrows(NotFoundException.class, () -> contactUCC.followContact(idContact, idUser));
  }

  @Test
  @DisplayName("Test follow a contact which has the wrong state")
  void testFollowContactWrongState() {
    contact.setState(State.ADMITTED);
    assertThrows(PreconditionFailedException.class,
        () -> contactUCC.followContact(idContact, idUser));
  }

  @Test
  @DisplayName("Test follow a contact when everything is correct")
  void testFollowContact() {
    contact.setState(State.UNSUPERVISED);
    assertAll(
        () -> assertEquals(contact.getIdContact(),
            contactUCC.followContact(idContact, idUser).getIdContact()),
        () -> assertEquals(State.STARTED, contact.getState())
    );
  }
}



