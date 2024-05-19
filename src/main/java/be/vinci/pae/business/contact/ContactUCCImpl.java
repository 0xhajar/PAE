package be.vinci.pae.business.contact;

import be.vinci.pae.business.academicyear.AcademicYear;
import be.vinci.pae.business.company.CompanyDTO;
import be.vinci.pae.business.contact.ContactDTO.State;
import be.vinci.pae.dal.DALServices;
import be.vinci.pae.dal.company.CompanyDAO;
import be.vinci.pae.dal.contact.ContactDAO;
import be.vinci.pae.exceptions.NotFoundException;
import be.vinci.pae.exceptions.PreconditionFailedException;
import jakarta.inject.Inject;
import java.util.List;

/**
 * Implementation of ContactUCC.
 */
public class ContactUCCImpl implements ContactUCC {

  @Inject
  private ContactDAO contactDAO;
  @Inject
  private DALServices dalServices;
  @Inject
  private CompanyDAO companyDAO;
  @Inject
  private AcademicYear academicYear;

  @Override
  public List<ContactDTO> getContactsByStudentId(int idStudent) {
    try {
      dalServices.open();
      return contactDAO.getContactsByStudentId(idStudent);
    } finally {
      dalServices.close();
    }
  }

  @Override
  public ContactDTO refuseContact(int idContact, String refusalReason, int idUser) {
    try {
      dalServices.open();
      ContactDTO contact = contactDAO.getOneById(idContact);

      if (contact == null) {
        return null;
      }
      if (contact.getIdStudent() != idUser) {
        throw new NotFoundException("You don't have a contact with this id");
      }
      if (!((Contact) contact).updateState(State.TURNED_DOWN)) {
        throw new PreconditionFailedException(
            "Le contact doit être dans l'état 'pris' pour être refusé");
      }
      contact.setRefusalReason(refusalReason);
      contactDAO.updateContact(contact);
      return contact;
    } finally {
      dalServices.close();
    }
  }

  @Override
  public ContactDTO addContact(ContactDTO contact) {
    try {
      dalServices.open();
      CompanyDTO companyDTO = companyDAO.getCompanyById(contact.getIdCompany());
      if (companyDTO == null) {
        throw new NotFoundException("L'entreprise n'existe pas.");
      }
      if (contactDAO.getContactAccepted(contact.getIdStudent()) != null) {
        throw new PreconditionFailedException("Vous avez déjà un contact accepté");
      }
      contact.setAcademicYear(academicYear.getAcademicYear());
      if (contactDAO.getCompanyContact(contact.getIdStudent(), contact.getIdCompany(),
          contact.getAcademicYear()) != null) {
        throw new PreconditionFailedException(
            "Vous avez déjà un contact avec cette entreprise pour cette année académique");
      }
      contact.setState(State.STARTED);
      contact = contactDAO.addContact(contact);
      contact.setCompany(companyDTO);
      return contact;
    } finally {
      dalServices.close();
    }
  }

  @Override
  public ContactDTO meetContact(int id, String meetPlace, int idUser) {
    try {
      dalServices.open();
      Contact contact = (Contact) contactDAO.getOneById(id);
      if (contact == null) {
        return null;
      }
      if (contact.getIdStudent() != idUser) {
        throw new NotFoundException("You don't have a contact with this id");
      }
      if (!contact.updateState(State.ADMITTED)) {
        throw new PreconditionFailedException(
            "The contact must be in the state 'started' to be admitted");
      }
      contact.setState(State.ADMITTED);
      contact.setMeetPlace(meetPlace);
      contactDAO.updateContact(contact);
      return contact;
    } finally {
      dalServices.close();
    }
  }

  @Override
  public ContactDTO unfollowContact(int id, int idUser) {
    try {
      dalServices.open();
      Contact contact = (Contact) contactDAO.getOneById(id);
      if (contact == null) {
        return null;
      }
      if (contact.getIdStudent() != idUser) {
        throw new NotFoundException("You don't have a contact with this id");
      }
      if (!contact.updateState(State.UNSUPERVISED)) {
        throw new PreconditionFailedException(
            "Le contact doit être dans l'état 'pris' ou 'initié' pour être non suivi");
      }
      contact.setState(State.UNSUPERVISED);
      contactDAO.updateContact(contact);
      return contact;
    } finally {
      dalServices.close();
    }
  }

  @Override
  public ContactDTO followContact(int id, int idUser) {
    try {
      dalServices.open();
      Contact contact = (Contact) contactDAO.getOneById(id);
      if (contact == null) {
        throw new NotFoundException("Contact not found");
      }
      if (contact.getIdStudent() != idUser) {
        throw new NotFoundException("You don't have a contact with this id");
      }
      if (!contact.updateState(State.STARTED)) {
        throw new PreconditionFailedException(
            "Le contact doit être dans l'état 'non suivi' pour être suivi");
      }
      contact.setState(State.STARTED);
      contactDAO.updateContact(contact);
      return contact;
    } finally {
      dalServices.close();
    }
  }

  @Override
  public List<ContactDTO> getContactsByCompany(int idCompany) {
    try {
      dalServices.open();
      CompanyDTO company = companyDAO.getCompanyById(idCompany);
      if (company == null) {
        throw new NotFoundException("Company not found");
      }
      return contactDAO.getContactsByCompany(idCompany);
    } finally {
      dalServices.close();
    }
  }

  @Override
  public List<ContactDTO> blacklistContacts(int idCompany) {
    try {
      dalServices.start();
      List<ContactDTO> contacts = getContactsByCompany(idCompany);
      for (ContactDTO c : contacts) {
        if (((Contact) c).updateState(State.BLACKLISTED)) {
          contactDAO.updateContact(c);
        }
      }
      return contacts;
    } catch (Exception e) {
      dalServices.rollback();
      throw e;
    } finally {
      dalServices.commit();
    }
  }

  @Override
  public List<ContactDTO> getAllContacts() {
    try {
      return contactDAO.getAllContacts();
    } finally {
      dalServices.close();
    }
  }

  @Override
  public ContactDTO acceptContact(int idContact, int idUser) {
    try {
      dalServices.start();
      ContactDTO contact = contactDAO.getOneById(idContact);
      if (contact == null) {
        throw new NotFoundException("Contact not found");
      }
      if (contact.getIdStudent() != idUser) {
        throw new NotFoundException("You don't have a contact with this id");
      }
      if (!((Contact) contact).updateState(State.ACCEPTED)) {
        throw new PreconditionFailedException(
            "The contact must be in the state 'admitted' to be accepted");
      }
      List<ContactDTO> contacts = contactDAO.getContactsByStudentId(idUser);
      for (ContactDTO c : contacts) {
        if (c.getIdContact() != idContact && ((Contact) c).updateState(State.ON_HOLD)) {
          contactDAO.updateContact(c);
        }
      }
      contactDAO.updateContact(contact);
      return contact;
    } catch (Exception e) {
      dalServices.rollback();
      throw e;
    } finally {
      dalServices.commit();
    }
  }

}
