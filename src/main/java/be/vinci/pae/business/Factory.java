package be.vinci.pae.business;

import be.vinci.pae.business.company.CompanyDTO;
import be.vinci.pae.business.contact.ContactDTO;
import be.vinci.pae.business.internship.InternshipDTO;
import be.vinci.pae.business.internshipsupervisor.InternshipSupervisorDTO;
import be.vinci.pae.business.user.UserDTO;

/**
 * Interface of FactoryImpl.
 */
public interface Factory {

  /**
   * Create a new UserImpl.
   *
   * @return a new UserImpl
   */
  UserDTO getUser();

  /**
   * Create a new ContactImpl.
   *
   * @return a new ContactImpl
   */
  ContactDTO getContact();

  /**
   * Create a new CompanyImpl.
   *
   * @return a new CompanyImpl
   */
  CompanyDTO getCompany();

  /**
   * Create a new InternshipImpl.
   *
   * @return a new InternshipImpl
   */
  InternshipDTO getInternship();

  /**
   * Create a new InternshipSupervisorImpl.
   *
   * @return a new InternshipSupervisorImpl
   */
  InternshipSupervisorDTO getInternshipSupervisor();
}
