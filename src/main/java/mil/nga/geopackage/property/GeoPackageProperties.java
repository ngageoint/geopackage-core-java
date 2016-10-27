package mil.nga.geopackage.property;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * GeoPackage property loader
 * 
 * @author osbornb
 */
public class GeoPackageProperties {

	/**
	 * Logger
	 */
	private static final Logger log = Logger
			.getLogger(GeoPackageProperties.class.getName());

	/**
	 * Properties
	 */
	private static Properties mProperties;

	/**
	 * Get a required property by key
	 * 
	 * @param key
	 *            key
	 * @return value
	 */
	public static String getProperty(String key) {
		return getProperty(key, true);
	}

	/**
	 * Get a property by key
	 * 
	 * @param key
	 *            key
	 * @param required
	 *            true if required
	 * @return value
	 */
	public static synchronized String getProperty(String key, boolean required) {
		if (mProperties == null) {
			mProperties = initializeConfigurationProperties();
		}
		String value = mProperties.getProperty(key);
		if (value == null && required) {
			throw new RuntimeException("Property not found: " + key);
		}
		return value;
	}

	/**
	 * Get a required property by base property and property name
	 * 
	 * @param base
	 *            base property
	 * @param property
	 *            property
	 * @return value
	 */
	public static String getProperty(String base, String property) {
		return getProperty(base, property, true);
	}

	/**
	 * Get a property by base property and property name
	 * 
	 * @param base
	 *            base property
	 * @param property
	 *            property
	 * @param required
	 *            true if required
	 * @return value
	 */
	public static synchronized String getProperty(String base, String property,
			boolean required) {
		return getProperty(
				base + PropertyConstants.PROPERTY_DIVIDER + property, required);
	}

	/**
	 * Get a required integer property by key
	 * 
	 * @param key
	 *            key
	 * @return integer value
	 */
	public static int getIntegerProperty(String key) {
		return getIntegerProperty(key, true);
	}

	/**
	 * Get an integer property by key
	 * 
	 * @param key
	 *            key
	 * @param required
	 *            true if required
	 * @return integer value
	 */
	public static Integer getIntegerProperty(String key, boolean required) {
		Integer value = null;
		String stringValue = getProperty(key, required);
		if (stringValue != null) {
			value = Integer.valueOf(stringValue);
		}
		return value;
	}

	/**
	 * Get a required integer property by base property and property name
	 * 
	 * @param base
	 *            base property
	 * @param property
	 *            property
	 * @return integer value
	 */
	public static int getIntegerProperty(String base, String property) {
		return getIntegerProperty(base, property, true);
	}

	/**
	 * Get an integer property by base property and property name
	 * 
	 * @param base
	 *            base property
	 * @param property
	 *            property
	 * @param required
	 *            true if required
	 * @return integer value
	 */
	public static Integer getIntegerProperty(String base, String property,
			boolean required) {
		return getIntegerProperty(base + PropertyConstants.PROPERTY_DIVIDER
				+ property, required);
	}

	/**
	 * Get a required float by key
	 * 
	 * @param key
	 *            key
	 * @return float value
	 */
	public static float getFloatProperty(String key) {
		return getFloatProperty(key, true);
	}

	/**
	 * Get a float by key
	 * 
	 * @param key
	 *            key
	 * @param required
	 *            true if required
	 * @return float value
	 */
	public static Float getFloatProperty(String key, boolean required) {
		Float value = null;
		String stringValue = getProperty(key, required);
		if (stringValue != null) {
			value = Float.valueOf(stringValue);
		}
		return value;
	}

	/**
	 * Get a required boolean by key
	 * 
	 * @param key
	 *            key
	 * @return boolean value
	 */
	public static boolean getBooleanProperty(String key) {
		return getBooleanProperty(key, true);
	}

	/**
	 * Get a boolean by key
	 * 
	 * @param key
	 *            key
	 * @param required
	 *            true if required
	 * @return boolean value
	 */
	public static Boolean getBooleanProperty(String key, boolean required) {
		Boolean value = null;
		String stringValue = getProperty(key, required);
		if (stringValue != null) {
			value = Boolean.valueOf(stringValue);
		}
		return value;
	}

	/**
	 * Initialize the configuration properties
	 * 
	 * @return properties
	 */
	private static Properties initializeConfigurationProperties() {
		Properties properties = new Properties();

		InputStream in = GeoPackageProperties.class.getResourceAsStream("/"
				+ PropertyConstants.PROPERTIES_FILE);
		if (in != null) {
			try {
				properties.load(in);
			} catch (Exception e) {
				log.log(Level.SEVERE, "Failed to load properties file: "
						+ PropertyConstants.PROPERTIES_FILE, e);
			} finally {
				try {
					in.close();
				} catch (IOException e) {
					log.log(Level.WARNING, "Failed to close properties file: "
							+ PropertyConstants.PROPERTIES_FILE, e);
				}
			}
		} else {
			log.log(Level.SEVERE, "Failed to load properties, file not found: "
					+ PropertyConstants.PROPERTIES_FILE);
		}

		return properties;
	}
}
