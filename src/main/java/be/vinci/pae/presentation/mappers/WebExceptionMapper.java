package be.vinci.pae.presentation.mappers;

import be.vinci.pae.exceptions.FatalException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * WebExceptionMapper class.
 */
@Provider
public class WebExceptionMapper implements ExceptionMapper<Throwable> {

  private final Logger logger = LogManager.getLogger("WebExceptionMapper");

  @Override
  public Response toResponse(Throwable exception) {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);
    exception.printStackTrace(pw);
    if (exception instanceof FatalException) {
      logger.fatal(sw.toString());
      return Response.status(((WebApplicationException) exception).getResponse().getStatus())
          .entity(exception.getMessage())
          .build();
    }
    if (exception instanceof WebApplicationException) {
      logger.warn(sw.toString());
      return Response.status(((WebApplicationException) exception).getResponse().getStatus())
          .entity(exception.getMessage())
          .build();
    }
    logger.error(sw.toString());
    return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
        .entity(exception.getMessage())
        .build();
  }
}
