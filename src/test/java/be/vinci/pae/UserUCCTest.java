package be.vinci.pae;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import be.vinci.pae.business.Factory;
import be.vinci.pae.business.user.User;
import be.vinci.pae.business.user.UserDTO;
import be.vinci.pae.business.user.UserDTO.Role;
import be.vinci.pae.business.user.UserUCC;
import be.vinci.pae.dal.user.UserDAO;
import be.vinci.pae.exceptions.BadRequestException;
import be.vinci.pae.exceptions.ConflictException;
import jakarta.ws.rs.WebApplicationException;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 * Test class for the UserUCC methods.
 */
public class UserUCCTest {

  static ServiceLocator locator;
  private static UserUCC userUCC;
  private static UserDAO userDAO;
  private static Factory factory;
  UserDTO user;

  @BeforeAll
  static void beforeAll() {
    locator = ServiceLocatorUtilities.bind(new ApplicationBinderTest());
    userUCC = locator.getService(UserUCC.class);
    factory = locator.getService(Factory.class);
    userDAO = locator.getService(UserDAO.class);
  }

  @BeforeEach
  void setUp() {
    user = (User) factory.getUser();
    // Password is "admin"
    user.setPassword("$2a$10$qxZA3HtOZkH.6ZyjMwld7ukjcKA3K9wnFDa/NVQlCAMXl95.06PDO");
    Mockito.when(userDAO.getOneByEmail("admin@vinci.be")).thenReturn(user);
    Mockito.when(userDAO.getOneById(1)).thenReturn(user);
  }

  @Test
  @DisplayName("Test for the login method of UserUCC with a correct email and password")
  void loginTest() {
    assertNotNull(userUCC.login("admin@vinci.be", "admin"));
  }

  @Test
  @DisplayName("Test for the login method of UserUCC with a wrong email and good password")
  void loginTestWrongEmail() {
    assertNull(userUCC.login("wrongEmail", "admin"));
  }

  @Test
  @DisplayName("Test for the login method of UserUCC with a good email and wrong password")
  void loginTestWrongPassword() {
    assertNull(userUCC.login("admin@vinci.be", "wrongPassword"));
  }


  @Test
  @DisplayName("Test for the getUser method of UserUCC with a correct id")
  void getUserTest() {
    UserDTO testUser = userUCC.getUser(1);
    assertNotNull(testUser);
  }

  @Test
  @DisplayName("Test for the getUser method of UserUCC with a wrong id")
  void getUserTestWrongId() {
    assertNull(userUCC.getUser(0));
  }

  @Test
  @DisplayName("Test for the getUser method of UserUCC with a negative id")
  void getUserTestNegativeId() {
    assertNull(userUCC.getUser(-1));
  }

  @Test
  @DisplayName("Test for the register method of UserUCC with a student email")
  void registerTestStudent() {
    user.setEmail("test.test@student.vinci.be");
    Mockito.when(userDAO.getOneByEmail("test.test@student.vinci.be")).thenReturn(null);
    Mockito.when(userDAO.addUser(user)).thenReturn(user);
    assertAll(
        () -> assertNotNull(userUCC.register(user)),
        () -> assertEquals(Role.STUDENT, user.getRole())
    );
  }

  @Test
  @DisplayName("Test for the register method of UserUCC with a teacher email")
  void registerTestTeacher() {
    user.setEmail("test.test@vinci.be");
    user.setRole(Role.TEACHER);
    Mockito.when(userDAO.getOneByEmail("test.test@vinci.be")).thenReturn(null);
    Mockito.when(userDAO.addUser(user)).thenReturn(user);
    assertNotNull(userUCC.register(user));
  }

  @Test
  @DisplayName("Test for the register method of UserUCC with an admin email")
  void registerTestAdmin() {
    user.setEmail("admin.test@vinci.be");
    user.setRole(Role.ADMIN);
    Mockito.when(userDAO.getOneByEmail("admin.test@vinci.be")).thenReturn(null);
    Mockito.when(userDAO.addUser(user)).thenReturn(user);
    assertNotNull(userUCC.register(user));
  }

  @Test
  @DisplayName("Test for the register method of UserUCC with an existing email")
  void registerTestInvalidEmail() {
    user.setEmail("invalid.test@student.vinci.be");
    user.setRole(Role.ADMIN);
    Mockito.when(userDAO.getOneByEmail("invalid.test@student.vinci.be")).thenReturn(user);
    Mockito.when(userDAO.addUser(user)).thenReturn(user);
    assertThrows(ConflictException.class, () -> userUCC.register(user));
  }

  @Test
  @DisplayName("Test for the register method of UserUCC with an invalid role")
  void registerTestInvalidRole() {
    user.setEmail("test.invalid@vinci.be");
    user.setRole(Role.STUDENT);
    Mockito.when(userDAO.getOneByEmail("test.invalid@vinci.be")).thenReturn(null);
    Mockito.when(userDAO.addUser(user)).thenReturn(user);
    assertThrows(WebApplicationException.class, () -> userUCC.register(user));
  }

  @Test
  @DisplayName("Test getAllUsers method")
  void getAllUsersTest() {
    assertNotNull(userUCC.getAllUsers());
  }

  @Test
  @DisplayName("Test for the update password method")
  void updatePasswordTest() {
    Mockito.when(userDAO.updateUser(user)).thenReturn(user);
    assertNotNull(userUCC.updateUser(user, "admin", "test"));
  }

  @Test
  @DisplayName("Test for the update password method with wrong password")
  void updatePasswordTestWrongPassword() {
    Mockito.when(userDAO.updateUser(user)).thenReturn(user);
    assertThrows(BadRequestException.class, () -> userUCC.updateUser(user, "test", "test"));
  }

  @Test
  @DisplayName("Test for the updateUser")
  void updateUserTest() {
    user.setFirstname("newFistname");
    user.setLastname("newLastname");
    user.setEmail("new.email@student.vinci.be");
    user.setPhoneNumber("123456789");

    Mockito.when(userDAO.updateUser(user)).thenReturn(user);
    assertNotNull(
        userUCC.updateUser(user, "newFistname", "newLastname", "new.email@student.vinci.be",
            "123456789"));

  }

  @Test
  @DisplayName("Test to get all students")
  void getAllStudentsTest() {
    assertNotNull(userUCC.getStudents());
  }

  @Test
  @DisplayName("Test to get all students with an exception")
  void getAllStudentsTestException() {
    Mockito.when(userDAO.getStudents()).thenThrow(new RuntimeException());
    assertThrows(RuntimeException.class, () -> userUCC.getStudents());
  }

  @Test
  @DisplayName("Test to modify the profile picture")
  void modifyProfilePictureTest() {
    Mockito.when(userDAO.updateUser(user)).thenReturn(user);
    userUCC.modifyProfilePicture(user);
  }

  @Test
  @DisplayName("Test to remove the profile picture")
  void removeProfilePictureTest() {
    user.setProfilePicture("test");
    Mockito.when(userDAO.updateUser(user)).thenReturn(user);
    userUCC.removeProfilePicture(user);
  }
}
