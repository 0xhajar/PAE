package be.vinci.pae.utils;

import be.vinci.pae.business.Factory;
import be.vinci.pae.business.FactoryImpl;
import be.vinci.pae.business.academicyear.AcademicYear;
import be.vinci.pae.business.academicyear.AcademicYearImpl;
import be.vinci.pae.business.company.CompanyUCC;
import be.vinci.pae.business.company.CompanyUCCImpl;
import be.vinci.pae.business.contact.ContactUCC;
import be.vinci.pae.business.contact.ContactUCCImpl;
import be.vinci.pae.business.internship.InternshipUCC;
import be.vinci.pae.business.internship.InternshipUCCImpl;
import be.vinci.pae.business.internshipsupervisor.InternshipSupervisorUCC;
import be.vinci.pae.business.internshipsupervisor.InternshipSupervisorUCCImpl;
import be.vinci.pae.business.user.UserUCC;
import be.vinci.pae.business.user.UserUCCImpl;
import be.vinci.pae.dal.DALBackServices;
import be.vinci.pae.dal.DALServices;
import be.vinci.pae.dal.DALServicesImpl;
import be.vinci.pae.dal.company.CompanyDAO;
import be.vinci.pae.dal.company.CompanyDAOImpl;
import be.vinci.pae.dal.contact.ContactDAO;
import be.vinci.pae.dal.contact.ContactDAOImpl;
import be.vinci.pae.dal.internship.InternshipDAO;
import be.vinci.pae.dal.internship.InternshipDAOImpl;
import be.vinci.pae.dal.internshipsupervisor.InternshipSupervisorDAO;
import be.vinci.pae.dal.internshipsupervisor.InternshipSupervisorDAOImpl;
import be.vinci.pae.dal.user.UserDAO;
import be.vinci.pae.dal.user.UserDAOImpl;
import be.vinci.pae.dal.utils.DAOServices;
import be.vinci.pae.dal.utils.DAOServicesImpl;
import jakarta.inject.Singleton;
import jakarta.ws.rs.ext.Provider;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

/**
 * This class is used to bind the interfaces to their implementation.
 */
@Provider
public class ApplicationBinder extends AbstractBinder {

  @Override
  protected void configure() {
    bind(UserUCCImpl.class).to(UserUCC.class).in(Singleton.class);
    bind(UserDAOImpl.class).to(UserDAO.class).in(Singleton.class);
    bind(FactoryImpl.class).to(Factory.class).in(Singleton.class);
    bind(ContactUCCImpl.class).to(ContactUCC.class).in(Singleton.class);
    bind(ContactDAOImpl.class).to(ContactDAO.class).in(Singleton.class);
    bind(CompanyUCCImpl.class).to(CompanyUCC.class).in(Singleton.class);
    bind(CompanyDAOImpl.class).to(CompanyDAO.class).in(Singleton.class);
    bind(DALServicesImpl.class).to(DALServices.class).to(DALBackServices.class).in(Singleton.class);
    bind(DAOServicesImpl.class).to(DAOServices.class).in(Singleton.class);
    bind(AcademicYearImpl.class).to(AcademicYear.class).in(Singleton.class);
    bind(InternshipUCCImpl.class).to(InternshipUCC.class).in(Singleton.class);
    bind(InternshipDAOImpl.class).to(InternshipDAO.class).in(Singleton.class);
    bind(InternshipSupervisorDAOImpl.class).to(InternshipSupervisorDAO.class).in(Singleton.class);
    bind(InternshipSupervisorUCCImpl.class).to(InternshipSupervisorUCC.class).in(Singleton.class);
  }
}
