package be.vinci.pae.main;

import be.vinci.pae.presentation.filters.LogFilter;
import be.vinci.pae.presentation.mappers.WebExceptionMapper;
import be.vinci.pae.utils.ApplicationBinder;
import be.vinci.pae.utils.Config;
import java.io.IOException;
import java.net.URI;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;

/**
 * Main class.
 */
public class Main {

  private static final String BASE_URI;

  static {
    Config.load("dev.properties");
    // Base URI the Grizzly HTTP server will listen on
    BASE_URI = Config.getProperty("BASE_URI");
  }

  /**
   * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
   *
   * @return Grizzly HTTP server.
   */
  public static HttpServer startServer() {
    // create a resource config that scans for JAX-RS resources and providers
    // in "be.vinci.pae.presentation" package

    final ResourceConfig rc = new ResourceConfig().packages("be.vinci.pae.presentation")
        .register(ApplicationBinder.class)
        .register(LogFilter.class)
        .register(WebExceptionMapper.class)
        .register(MultiPartFeature.class);

    // create and start a new instance of grizzly http server
    // exposing the Jersey application at BASE_URI
    return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
  }

  /**
   * Main method.
   *
   * @param args args
   * @throws IOException IOException
   */
  public static void main(String[] args) throws IOException {
    final HttpServer server = startServer();
    System.out.println(String.format("Jersey app started with endpoints available at "
        + "%s%nHit Ctrl-C to stop it...", BASE_URI));
    System.in.read();
    server.shutdownNow();
  }

}
