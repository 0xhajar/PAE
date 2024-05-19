package be.vinci.pae.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * This class is used to load a config file and get values of properties in it.
 */
public class Config {

  private static Properties props;

  /**
   * Loads the provided file.
   *
   * @param file the provided file
   */
  public static void load(String file) {
    props = new Properties();
    try (InputStream input = new FileInputStream(file)) {
      props.load(input);
    } catch (IOException e) {
      throw new RuntimeException("Error while loading config file");
    }
  }

  /**
   * Get string value associated to provided key.
   *
   * @param key the provided key
   * @return the value associated to the key
   */
  public static String getProperty(String key) {
    return props.getProperty(key);
  }

  /**
   * Get int value associated to provided key.
   *
   * @param key the provided key
   * @return the value associated to the key
   */
  public static Integer getIntProperty(String key) {
    return Integer.parseInt(props.getProperty(key));
  }

  /**
   * Get boolean value associated to provided key.
   *
   * @param key the provided key
   * @return the value associated to the key
   */
  public static boolean getBoolProperty(String key) {
    return Boolean.parseBoolean(props.getProperty(key));
  }

}
