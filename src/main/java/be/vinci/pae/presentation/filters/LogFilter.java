package be.vinci.pae.presentation.filters;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class is a filter that logs the response of the server.
 */
@Provider
public class LogFilter implements ContainerResponseFilter {

  private final Logger logger = LogManager.getLogger(LogFilter.class);

  @Override
  public void filter(ContainerRequestContext request,
      ContainerResponseContext response) throws IOException {
    logger.info(
        response.getStatus() + " " + request.getMethod() + " " + request.getUriInfo().getPath());
  }
}
