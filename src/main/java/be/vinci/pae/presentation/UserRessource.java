/**
 * UserRessource class. This class is responsible for handling HTTP requests related to users. It
 * uses the UserUCC class to perform business logic operations and returns the results as JSON.
 */

package be.vinci.pae.presentation;

import be.vinci.pae.business.user.UserDTO;
import be.vinci.pae.business.user.UserDTO.Role;
import be.vinci.pae.business.user.UserUCC;
import be.vinci.pae.exceptions.BadRequestException;
import be.vinci.pae.exceptions.FatalException;
import be.vinci.pae.exceptions.UnauthorizedException;
import be.vinci.pae.presentation.filters.Authorize;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.glassfish.jersey.server.ContainerRequest;

/**
 * This class is a Singleton and a RESTful resource that handles HTTP requests related to users.
 */
@Singleton
@Path("/users")
public class UserRessource {

  /**
   * The UserUCC instance used to perform business logic operations related to users.
   */
  @Inject
  private UserUCC userUCC;

  /**
   * Handles HTTP POST requests to register a new user. This method uses the injected UserUCC
   * instance to register a new user and returns the registered user as a UserDTO object. The
   * UserDTO object is automatically converted to JSON by the JAX-RS runtime.
   *
   * @param json    the JSON object containing the user's information
   * @param request the HTTP request
   * @return the registered user as a UserDTO object
   */
  @POST
  @Path("/changepassword")
  @Authorize(roles = {Role.ADMIN, Role.STUDENT, Role.TEACHER})
  @Consumes(MediaType.APPLICATION_JSON)
  public Response changePassword(JsonNode json, @Context ContainerRequest request) {
    final UserDTO authenticatedUser = (UserDTO) request.getProperty("user");
    String oldPassword = json.get("oldPassword").asText();
    String newPassword = json.get("newPassword").asText();
    String confirmationPassword = json.get("confirmationPassword").asText();

    if (oldPassword == null || newPassword == null || confirmationPassword == null) {
      throw new BadRequestException("oldPassword or newPassword or confimation required");
    }

    if (oldPassword.equals(newPassword)) {
      throw new BadRequestException("New password is the same as the old one");
    }

    if (!newPassword.equals(confirmationPassword) || newPassword.isEmpty()) {
      throw new BadRequestException("New password and confirmation are not the same or empty");
    }

    if (oldPassword.isEmpty()) {
      throw new BadRequestException("oldPassword or newPassword or confirmationPassword is empty");
    }

    if (userUCC.updateUser(authenticatedUser, oldPassword, newPassword) == null) {
      throw new UnauthorizedException("oldPassword is wrong");
    }
    return Response.status(204, "Password changed").build();
  }

  /**
   * Update user information.
   *
   * @param json    json containing the new user information
   * @param request the request's context
   * @return the updated user
   */
  @PUT
  @Path("/update")
  @Authorize(roles = {Role.ADMIN, Role.STUDENT, Role.TEACHER})
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public UserDTO updateUser(JsonNode json, @Context ContainerRequest request) {
    final UserDTO authenticatedUser = (UserDTO) request.getProperty("user");
    String firstName = json.get("firstname").asText();
    String lastName = json.get("lastname").asText();
    String email = json.get("email").asText();
    String telephone = json.get("phoneNumber").asText();

    if (firstName == null || lastName == null || email == null || telephone == null) {
      throw new BadRequestException("All fields are required");
    }

    if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || telephone.isEmpty()) {
      throw new BadRequestException("All fields are required");
    }

    if (!email.matches(
        "^[a-zA-Z0-9._%+-]+\\.[a-zA-Z0-9._%+-]+@(vinci\\.be|student\\.vinci\\.be)$")) {
      throw new BadRequestException("email is not valid");
    }

    return userUCC.updateUser(authenticatedUser, firstName, lastName, email, telephone);
  }

  /**
   * Handles HTTP GET requests to fetch all users. This method uses the injected UserUCC instance to
   * fetch all users and returns them as a list of UserDTO objects. The list is automatically
   * converted to JSON by the JAX-RS runtime.
   *
   * @return a list of UserDTO objects representing all users
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize(roles = {Role.ADMIN, Role.TEACHER})
  public List<Map<String, Object>> getAllUsers() {
    return userUCC.getAllUsers();
  }

  /**
   * Get a user by its id.
   *
   * @param id the id of the user
   * @return the user
   */
  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize(roles = {Role.ADMIN, Role.TEACHER})
  public UserDTO getUserById(@PathParam("id") int id) {
    return userUCC.getUser(id);
  }

  /**
   * Get all students.
   *
   * @return the list of all students
   */
  @GET
  @Path("/students")
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize(roles = {Role.ADMIN, Role.TEACHER})
  public List<UserDTO> getStudents() {
    return userUCC.getStudents();
  }

  /**
   * Modify the profile picture of a user.
   *
   * @param request the request's context
   * @param file    the picture to upload
   * @return the updated user
   */
  @POST
  @Path("/picture/modify")
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize(roles = {Role.STUDENT, Role.TEACHER, Role.ADMIN})
  public UserDTO modifyPicture(@Context ContainerRequest request,
      @FormDataParam("file") InputStream file) {
    try {
      String encoded = Base64.getEncoder()
          .encodeToString(file.readAllBytes());
      UserDTO user = (UserDTO) request.getProperty("user");
      user.setProfilePicture(encoded);
      userUCC.modifyProfilePicture(user);
      return user;
    } catch (IOException e) {
      throw new FatalException(e);
    }
  }

  /**
   * Remove the profile picture of a user.
   *
   * @param request the request's context
   * @return the updated user
   */
  @POST
  @Path("/picture/remove")
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize(roles = {Role.STUDENT, Role.TEACHER, Role.ADMIN})
  public UserDTO removePicture(@Context ContainerRequest request) {
    UserDTO user = (UserDTO) request.getProperty("user");

    user.setProfilePicture(null);

    userUCC.removeProfilePicture(user);

    return user;
  }

}