package be.vinci.pae.business.company;

import java.util.List;

/**
 * Interface of CompanyUCCImpl.
 */
public interface CompanyUCC {

  /**
   * Get all the companies.
   *
   * @return all a list of companies
   */
  List<CompanyDTO> getAll();

  /**
   * Get a company by its id.
   *
   * @param id the id of the company
   * @return the company
   */
  CompanyDTO getCompanyById(int id);

  /**
   * Blacklist a company.
   *
   * @param id     the id of the company
   * @param reason the reason of the blacklist
   * @return the company blacklisted
   */
  List<Object> blacklistCompany(int id, String reason);

  /**
   * Add a company.
   *
   * @param company the company to add
   * @return the company added
   */
  CompanyDTO addCompany(CompanyDTO company);
}
