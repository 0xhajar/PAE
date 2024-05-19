package be.vinci.pae.presentation.filters;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;

/**
 * Filter to add CORS headers to the container response context.
 */
@Provider
public class CorsFilter implements ContainerResponseFilter {

  /**
   * Add CORS headers to the container response context.
   *
   * @param requestContext  the request context
   * @param responseContext the response context
   * @throws IOException if an I/O exception occurs
   */
  @Override
  public void filter(ContainerRequestContext requestContext,
      ContainerResponseContext responseContext) throws IOException {
    responseContext.getHeaders().add("Access-Control-Allow-Origin", "*");
    responseContext.getHeaders()
        .add("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS");
    responseContext.getHeaders().add("Access-Control-Allow-Headers", "Content-Type, Authorization");
    responseContext.getHeaders().add("Access-Control-Allow-Credentials", "true");
  }
}