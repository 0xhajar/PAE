package be.vinci.pae.exceptions;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Class for the FatalException.
 */
public class FatalException extends WebApplicationException {

  /**
   * The status of the exception.
   */
  public static final Status STATUS = Status.INTERNAL_SERVER_ERROR;

  /**
   * Constructor without a message.
   */
  public FatalException() {
    super(Response.status(STATUS)
        .build());
  }

  /**
   * Constructor with a message.
   *
   * @param message the message
   */
  public FatalException(String message) {
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
  public FatalException(Throwable cause) {
    super(cause);
  }

}
