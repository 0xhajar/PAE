package be.vinci.pae.presentation.filters;

import be.vinci.pae.business.user.UserDTO.Role;
import jakarta.ws.rs.NameBinding;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation to bind the Authorize filter to the resources.
 */
@NameBinding
@Retention(RetentionPolicy.RUNTIME)
public @interface Authorize {

  /**
   * Get the roles that are allowed to access the resource.
   *
   * @return the list of roles
   */
  Role[] roles() default {};
}
