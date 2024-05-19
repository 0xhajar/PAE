package be.vinci.pae;

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
import be.vinci.pae.dal.DALServices;
import be.vinci.pae.dal.company.CompanyDAO;
import be.vinci.pae.dal.contact.ContactDAO;
import be.vinci.pae.dal.internship.InternshipDAO;
import be.vinci.pae.dal.internshipsupervisor.InternshipSupervisorDAO;
import be.vinci.pae.dal.user.UserDAO;
import jakarta.inject.Singleton;
import jakarta.ws.rs.ext.Provider;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.mockito.Mockito;

/**
 * This class is used to bind the interfaces to their implementation.
 */
@Provider
public class ApplicationBinderTest extends AbstractBinder {

  @Override
  protected void configure() {
    bind(UserUCCImpl.class).to(UserUCC.class).in(Singleton.class);
    bind(Mockito.mock(UserDAO.class)).to(UserDAO.class);
    bind(FactoryImpl.class).to(Factory.class).in(Singleton.class);
    bind(ContactUCCImpl.class).to(ContactUCC.class).in(Singleton.class);
    bind(Mockito.mock(ContactDAO.class)).to(ContactDAO.class);
    bind(Mockito.mock(DALServices.class)).to(DALServices.class);
    bind(Mockito.mock(CompanyDAO.class)).to(CompanyDAO.class);
    bind(CompanyUCCImpl.class).to(CompanyUCC.class).in(Singleton.class);
    bind(AcademicYearImpl.class).to(AcademicYear.class).in(Singleton.class);
    bind(Mockito.mock(InternshipDAO.class)).to(InternshipDAO.class);
    bind(InternshipUCCImpl.class).to(InternshipUCC.class).in(Singleton.class);
    bind(Mockito.mock(InternshipSupervisorDAO.class)).to(InternshipSupervisorDAO.class);
    bind(InternshipSupervisorUCCImpl.class).to(InternshipSupervisorUCC.class).in(Singleton.class);
  }
}
