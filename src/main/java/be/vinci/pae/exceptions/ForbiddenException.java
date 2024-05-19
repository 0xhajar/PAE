package be.vinci.pae.exceptions;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Class for the ForbiddenException.
 */
public class ForbiddenException extends WebApplicationException {

  /**
   * The status of the exception.
   */
  public static final Status STATUS = Status.FORBIDDEN;

  /**
   * Constructor without a message.
   */
  public ForbiddenException() {
    super(Response.status(STATUS)
        .build());
  }

  /**
   * Constructor with a message.
   *
   * @param message the message
   */
  public ForbiddenException(String message) {
    super(Response.status(STATUS)
        .entity(message)
        .type("text/plain")
        .build());
  }

  /**
   * Constructor with a cause.
   *
   * @param cause the cause
   */
  public ForbiddenException(Throwable cause) {
    super(Response.status(STATUS)
        .entity(cause.getMessage())
        .type("text/plain")
        .build());
  }
}
