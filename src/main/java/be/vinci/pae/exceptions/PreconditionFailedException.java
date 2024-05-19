package be.vinci.pae.exceptions;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Class for the PreconditionFailedException.
 */
public class PreconditionFailedException extends WebApplicationException {

  /**
   * The status of the exception.
   */
  public static final Status STATUS = Status.PRECONDITION_FAILED;

  /**
   * Constructor without a message.
   */
  public PreconditionFailedException() {
    super(Response.status(STATUS)
        .build());
  }

  /**
   * Constructor with a message.
   *
   * @param message the message
   */
  public PreconditionFailedException(String message) {
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
  public PreconditionFailedException(Throwable cause) {
    super(Response.status(STATUS)
        .entity(cause.getMessage())
        .type("text/plain")
        .build());
  }
}
