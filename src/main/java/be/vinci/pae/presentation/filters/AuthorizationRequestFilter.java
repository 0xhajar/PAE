package be.vinci.pae.presentation.filters;

import be.vinci.pae.business.user.UserDTO;
import be.vinci.pae.business.user.UserDTO.Role;
import be.vinci.pae.business.user.UserUCC;
import be.vinci.pae.exceptions.ForbiddenException;
import be.vinci.pae.exceptions.TokenDecodingException;
import be.vinci.pae.exceptions.UnauthorizedException;
import be.vinci.pae.utils.Config;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Filter to check if the user is authorized to access a resource.
 */
@Singleton
@Provider
@Authorize
public class AuthorizationRequestFilter implements ContainerRequestFilter {

  private final Algorithm jwtAlgorithm = Algorithm.HMAC256(Config.getProperty("JWTSecret"));
  private final JWTVerifier jwtVerifier = JWT.require(this.jwtAlgorithm).withIssuer("auth0")
      .build();
  @Context
  private ResourceInfo resourceInfo;
  @Inject
  private UserUCC myUserUCC;

  @Override
  public void filter(ContainerRequestContext requestContext) throws IOException {
    String token = requestContext.getHeaderString("Authorization");
    if (token == null) {
      throw new UnauthorizedException("A token is needed to access this resource");
    } else {
      DecodedJWT decodedToken = null;
      try {
        decodedToken = this.jwtVerifier.verify(token);
      } catch (Exception e) {
        throw new TokenDecodingException(e);
      }
      UserDTO authenticatedUser = myUserUCC.getUser(decodedToken.getClaim("user").asInt());
      if (authenticatedUser == null) {
        throw new ForbiddenException("You are forbidden to access this resource");
      }

      Method m = resourceInfo.getResourceMethod();
      Authorize authorize = m.getAnnotation(Authorize.class);
      Role[] roles = authorize.roles();

      if (!Arrays.asList(roles).contains(authenticatedUser.getRole())) {
        throw new ForbiddenException("You are forbidden to access this resource");
      }

      requestContext.setProperty("user", authenticatedUser);
    }
  }

}


