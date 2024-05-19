package be.vinci.pae.business.internship;

import be.vinci.pae.business.user.UserDTO;

/**
 * Interface of InternshipUCCImpl.
 */
public interface InternshipUCC {

  /**
   * Get an internship by user.
   *
   * @param user the user
   * @return the internship
   */
  InternshipDTO getInternshipByUser(UserDTO user);

  /**
   * Get an internship by its id.
   *
   * @param id the id of the internship
   * @return the internship
   */
  InternshipDTO getInternshipById(int id);

  /**
   * Add an internship.
   *
   * @param internship the internship to add
   * @return the internship added
   */
  InternshipDTO addInternship(InternshipDTO internship);

  /**
   * Update the subject of an internship.
   *
   * @param internship the internship to update
   * @param subject    the subject of the internship
   * @return the internship updated
   */
  InternshipDTO updateInternshipSubject(InternshipDTO internship, String subject);
}
