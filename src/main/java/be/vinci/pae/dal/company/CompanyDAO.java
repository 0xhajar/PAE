package be.vinci.pae.dal.company;

import be.vinci.pae.business.company.CompanyDTO;
import java.util.List;

/**
 * Interface of CompanyDAOImpl.
 */
public interface CompanyDAO {

  /**
   * Get all companies.
   *
   * @return all companies
   */
  List<CompanyDTO> getAll();

  /**
   * Get a company by its id.
   *
   * @param id the id of the company
   * @return the company, null if no company was found
   */
  CompanyDTO getCompanyById(int id);

  /**
   * Get a company by its trade name.
   *
   * @param tradeName the trade name of the company
   * @return the company, null if no company was found
   */
  CompanyDTO getCompanyByTradeName(String tradeName);

  /**
   * Get a list of companies by their name.
   *
   * @param name        the name of the company
   * @param designation the designation of the company
   * @return the list of companies
   */
  CompanyDTO getCompanyByNameAndDesignation(String name, String designation);

  /**
   * Update a company.
   *
   * @param company the company to update
   * @return the company updated
   */
  CompanyDTO updateCompany(CompanyDTO company);

  /**
   * Add a company.
   *
   * @param company the company to add
   * @return the company added
   */
  CompanyDTO addCompany(CompanyDTO company);
}
