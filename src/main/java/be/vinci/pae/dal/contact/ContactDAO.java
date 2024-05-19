package be.vinci.pae.dal.contact;

import be.vinci.pae.business.contact.ContactDTO;
import java.util.List;

/**
 * Interface of ContactDAOImpl.
 */
public interface ContactDAO {

  /**
   * Get all contacts.
   *
   * @return the list of contacts
   */
  List<ContactDTO> getAllContacts();

  /**
   * Get all contacts by student id.
   *
   * @param id the id of the student
   * @return the list of contacts
   */
  List<ContactDTO> getContactsByStudentId(int id);

  /**
   * Get a contact by its id.
   *
   * @param id the id of the contact
   * @return the contact
   */
  ContactDTO getOneById(int id);

  /**
   * Refuse a contact.
   *
   * @param contact the contact to refuse
   */
  void updateContact(ContactDTO contact);

  /**
   * Add a contact to the database.
   *
   * @param contact the contact to add
   * @return the contact added
   */
  ContactDTO addContact(ContactDTO contact);

  /**
   * Get the contact accepted by the user.
   *
   * @param idUser the id of the user
   * @return the contact accepted or null if no contact was found
   */
  ContactDTO getContactAccepted(int idUser);

  /**
   * Get the contact of a user for a certain company.
   *
   * @param idUser       the id of the user
   * @param idCompany    the id of the company
   * @param academicYear the academic year
   * @return the contact of the user for the company or null if no contact was found
   */
  ContactDTO getCompanyContact(int idUser, int idCompany, String academicYear);

  /**
   * Get the contacts of a company.
   *
   * @param idCompany the id of the company
   * @return the contacts
   */
  List<ContactDTO> getContactsByCompany(int idCompany);

}
