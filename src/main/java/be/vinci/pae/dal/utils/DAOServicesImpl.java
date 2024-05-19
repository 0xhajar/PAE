package be.vinci.pae.dal.utils;

import be.vinci.pae.business.Factory;
import be.vinci.pae.exceptions.FatalException;
import jakarta.inject.Inject;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Class with utility methods.
 */
public class DAOServicesImpl implements DAOServices {

  @Inject
  private Factory factory;

  @Override
  public Object getDataFromRs(ResultSet rs, String prefix) {
    try {
      // Build the class prefix (first letter in uppercase and the rest of the string)
      String classPrefix = prefix.substring(0, 1).toUpperCase() + prefix.substring(1);
      // Get the implementation class
      Class<?> implementationClass = Class.forName(
          "be.vinci.pae.business." + prefix.toLowerCase() + "." + classPrefix + "Impl");
      // Get the method to get the implementation of the class
      Method getImpl = factory.getClass().getDeclaredMethod("get" + classPrefix);
      // Get the object from the factory using the method
      Object object = getImpl.invoke(factory);
      // Set the fields of the object
      for (Field f : implementationClass.getDeclaredFields()) {
        // Get the method to set the field of the object
        Method m = implementationClass.getDeclaredMethod(
            "set" + f.getName().substring(0, 1).toUpperCase()
                + f.getName().substring(1), f.getType());
        // If field is an enum, use Enum.valueOf to get the enum value
        if (f.getType().isEnum()) {
          m.invoke(object, Enum.valueOf((Class<Enum>) Class.forName(f.getType().getName()),
              rs.getString(prefix + "_" + f.getName())));
        } else if (!f.getType().isInterface()) {
          // If field is not an interface, set the field of the object
          m.invoke(object, rs.getObject(prefix + "_" + f.getName()));
        } else {
          // If field is an interface,
          // get the data from the ResultSet and set the field of the object
          m.invoke(object, getDataFromRs(rs, f.getName()));
        }
      }
      return object;
    } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException
             | IllegalAccessException | SQLException e) {
      throw new FatalException(e);
    }
  }
}
