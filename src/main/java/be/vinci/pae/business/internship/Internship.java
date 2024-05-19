package be.vinci.pae.business.internship;

import be.vinci.pae.business.internshipsupervisor.InternshipSupervisorDTO;

/**
 * Represents an Internship.
 */
public interface Internship extends InternshipDTO {

  InternshipSupervisorDTO getInternshipSupervisor();
}
