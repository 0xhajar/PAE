package be.vinci.pae.exceptions;


import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Exception thrown when a token cannot be decoded.
 */
public class TokenDecodingException extends WebApplicationException {

  /**
   * The status of the exception.
   */
  public static final Status STATUS = Status.UNAUTHORIZED;

  /**
   * Constructor without a message.
   */
  public TokenDecodingException() {
    super(Response.status(STATUS)
        .build());
  }

  /**
   * Constructor with a message.
   *
   * @param message the message
   */
  public TokenDecodingException(String message) {
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
  public TokenDecodingException(Throwable cause) {
    super(cause.getMessage(), cause, STATUS);
  }
}
