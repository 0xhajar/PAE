package be.vinci.pae;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import be.vinci.pae.business.Factory;
import be.vinci.pae.business.company.CompanyDTO;
import be.vinci.pae.business.company.CompanyUCC;
import be.vinci.pae.dal.company.CompanyDAO;
import be.vinci.pae.exceptions.ConflictException;
import be.vinci.pae.exceptions.NotFoundException;
import java.util.ArrayList;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 * CompanyUCC test class.
 */
public class CompanyUCCTest {

  static ServiceLocator locator;
  private static CompanyUCC companyUCC;
  private static CompanyDAO companyDAO;
  private static Factory factory;
  CompanyDTO company;

  @BeforeAll
  static void beforeAll() {
    locator = ServiceLocatorUtilities.bind(new ApplicationBinderTest());
    companyUCC = locator.getService(CompanyUCC.class);
    factory = locator.getService(Factory.class);
    companyDAO = locator.getService(CompanyDAO.class);
  }

  @BeforeEach
  void setUp() {
    company = factory.getCompany();
    company.setIdCompany(1);
    company.setTradeName("Test");
    company.setBlacklisted(false);
    Mockito.when(companyDAO.getCompanyById(1)).thenReturn(company);
    Mockito.when(companyDAO.getCompanyByTradeName("Test")).thenReturn(company);
    Mockito.when(companyDAO.getCompanyByNameAndDesignation("Test", "Test")).thenReturn(company);
  }

  @Test
  @DisplayName("Test get all companies method when there are companies in the database")
  void testGetAllCompanies() {
    Mockito.when(companyDAO.getAll()).thenReturn(new ArrayList<>());
    assertNotNull(companyUCC.getAll());
  }

  @Test
  @DisplayName("Test get company by id method when the company exists")
  void testGetCompanyById() {
    assertNotNull(companyUCC.getCompanyById(1));
  }

  @Test
  @DisplayName("for for blacklistCompany method")
  void blacklistCompanyTest() {
    int idCompany = 1;
    String refusalReason = "Test";
    assertNotNull(companyUCC.blacklistCompany(idCompany, refusalReason));
  }

  @Test
  @DisplayName("for for blacklistCompany method with wrong id")
  void blacklistCompanyWrongIdTest() {
    int idCompany = 1;
    String refusalReason = "Test";
    Mockito.when(companyDAO.getCompanyById(idCompany)).thenReturn(null);
    assertThrows(NotFoundException.class,
        () -> companyUCC.blacklistCompany(idCompany, refusalReason));
  }

  @Test
  @DisplayName("for for blacklistCompany method with wrong state")
  void blacklistCompanyWrongStateTest() {
    int idCompany = 1;
    String refusalReason = "Test";
    CompanyDTO company = factory.getCompany();
    company.setBlacklisted(true);
    Mockito.when(companyDAO.getCompanyById(idCompany)).thenReturn(company);
    assertThrows(ConflictException.class,
        () -> companyUCC.blacklistCompany(idCompany, refusalReason));
  }

  @Test
  @DisplayName("test to add a company that already exists, only tradename")
  void testAddCompanyNull() {
    assertThrows(ConflictException.class, () -> companyUCC.addCompany(company));
  }

  @Test
  @DisplayName("test to add a company that already exists with this name and designation")
  void testAddMatchingExistingCompany() {
    company.setDesignation("Test");
    assertThrows(ConflictException.class, () -> companyUCC.addCompany(company));
  }

  @Test
  @DisplayName("test to add a company")
  void testAddCompany() {
    company.setIdCompany(4);
    company.setTradeName("Test2");
    Mockito.when(companyDAO.getCompanyById(company.getIdCompany())).thenReturn(null);
    Mockito.when(companyDAO.addCompany(company)).thenReturn(company);
    assertNotNull(companyUCC.addCompany(company));
  }

}
