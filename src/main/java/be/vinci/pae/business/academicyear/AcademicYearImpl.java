package be.vinci.pae.business.academicyear;

import java.time.LocalDate;

/**
 * Implementation of AcademicYear.
 */
public class AcademicYearImpl implements AcademicYear {

  @Override
  public String getAcademicYear() {
    LocalDate date = LocalDate.now();
    // Tester les 2 lignes suivantes
    if (date.getMonthValue() >= 9) {
      return date.getYear() + "-" + (date.getYear() + 1);
    }
    return (date.getYear() - 1) + "-" + date.getYear();
  }

}
