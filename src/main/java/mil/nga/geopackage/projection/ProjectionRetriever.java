package mil.nga.geopackage.projection;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Retrieves the proj4 projection parameter string for an EPSG code
 * 
 * @author osbornb
 */
public class ProjectionRetriever {

	/**
	 * Logger
	 */
	private static final Logger log = Logger
			.getLogger(ProjectionRetriever.class.getName());

	/**
	 * Projections property file name prefix
	 * 
	 * @since 1.2.3
	 */
	public static final String PROJECTIONS_PROPERTY_FILE_PREFIX = "projections";

	/**
	 * Projections property file name suffix
	 * 
	 * @since 1.2.3
	 */
	public static final String PROJECTIONS_PROPERTY_FILE_SUFFIX = "properties";

	/**
	 * Properties for each authority
	 */
	private static final Map<String, Properties> properties = new HashMap<>();

	/**
	 * Get the proj4 projection string for the EPSG coordinate code
	 * 
	 * @param epsg
	 *            epsg code
	 * @return proj4 projection
	 */
	public static String getProjection(long epsg) {
		return getProjection(ProjectionConstants.AUTHORITY_EPSG, epsg);
	}

	/**
	 * Get the proj4 projection string for the authority coordinate code
	 * 
	 * @param authority
	 *            coordinate authority
	 * @param code
	 *            coordinate code
	 * @return proj4 projection
	 * @since 1.2.3
	 */
	public static String getProjection(String authority, long code) {
		return getProjection(authority, String.valueOf(code));
	}

	/**
	 * Get the proj4 projection string for the authority coordinate code
	 * 
	 * @param authority
	 *            coordinate authority
	 * @param code
	 *            coordinate code
	 * @return proj4 projection
	 * @since 1.2.3
	 */
	public static String getProjection(String authority, String code) {

		Properties authorityProperties = getOrCreateProjections(authority);

		String projection = authorityProperties.getProperty(code);

		return projection;
	}

	/**
	 * Get or create the projection properties
	 * 
	 * @param authority
	 *            coordinate authority
	 * @return projection properties
	 * @since 1.2.3
	 */
	public static Properties getOrCreateProjections(String authority) {

		String authorityKey = authority.toLowerCase();

		Properties authorityProperties = properties.get(authorityKey);
		if (authorityProperties == null) {
			loadProperties(authorityKey);
			authorityProperties = properties.get(authorityKey);
		}

		return authorityProperties;
	}

	/**
	 * Get the projection properties for the authority
	 * 
	 * @param authority
	 *            coordinate authority
	 * @return projection properties
	 * @since 1.2.3
	 */
	public static Properties getProjections(String authority) {
		return properties.get(authority.toLowerCase());
	}

	/**
	 * Clear the properties for all authorities
	 * 
	 * @since 1.2.3
	 */
	public static void clear() {
		properties.clear();
	}

	/**
	 * Clear the properties for the authority
	 * 
	 * @param authority
	 *            coordinate authority
	 * @since 1.2.3
	 */
	public static void clear(String authority) {
		properties.remove(authority.toLowerCase());
	}

	/**
	 * Clear the property for the authority code
	 * 
	 * @param authority
	 *            coordinate authority
	 * @param code
	 *            coordinate code
	 * @since 1.2.3
	 */
	public static void clear(String authority, long code) {
		clear(authority, String.valueOf(code));
	}

	/**
	 * Clear the property for the authority code
	 * 
	 * @param authority
	 *            coordinate authority
	 * @param code
	 *            coordinate code
	 * @since 1.2.3
	 */
	public static void clear(String authority, String code) {
		Properties properties = getProjections(authority);
		if (properties != null) {
			properties.remove(code);
		}
	}

	/**
	 * Load the projection properties from the authority configuration
	 * properties file.
	 * 
	 * @param authority
	 *            coordinate authority key
	 */
	private static void loadProperties(String authority) {

		String authorityFile = propertyFileName(authority);
		InputStream in = ProjectionRetriever.class.getResourceAsStream("/"
				+ authorityFile);

		setProjections(authority, in);
	}

	/**
	 * Get the property file name for the authority
	 * 
	 * Resulting File Name Format: {@value #PROJECTIONS_PROPERTY_FILE_PREFIX}
	 * .lower_case_authority.{@value #PROJECTIONS_PROPERTY_FILE_SUFFIX}
	 * 
	 * @param authority
	 *            coordinate authority
	 * @return property file name
	 * @since 1.2.3
	 */
	public static String propertyFileName(String authority) {
		return PROJECTIONS_PROPERTY_FILE_PREFIX + "." + authority.toLowerCase()
				+ "." + PROJECTIONS_PROPERTY_FILE_SUFFIX;
	}

	/**
	 * Set the projections for the authority with the properties input stream
	 * 
	 * @param authority
	 *            coordinate authority
	 * @param propertiesStream
	 *            properties input stream
	 * @since 1.2.3
	 */
	public static void setProjections(String authority,
			InputStream propertiesStream) {

		Properties authorityProperties = new Properties();

		if (propertiesStream != null) {
			try {
				authorityProperties.load(propertiesStream);
			} catch (Exception e) {
				log.log(Level.WARNING,
						"Failed to load authority: " + authority, e);
			} finally {
				try {
					propertiesStream.close();
				} catch (IOException e) {
					log.log(Level.WARNING, "Failed to close authority: "
							+ authority, e);
				}
			}
		} else {
			log.log(Level.WARNING, "Failed to load authority: " + authority);
		}

		setProjections(authority, authorityProperties);
	}

	/**
	 * Set the authority projections as the provided properties
	 * 
	 * @param authority
	 *            coordinate authority
	 * @param authorityProperties
	 *            authority projection properties
	 * @since 1.2.3
	 */
	public static void setProjections(String authority,
			Properties authorityProperties) {
		properties.put(authority.toLowerCase(), authorityProperties);
	}

	/**
	 * Set the projections for the authority with the properties file
	 * 
	 * @param authority
	 *            coordinate authority
	 * @param propertiesFile
	 *            properties file
	 * @throws FileNotFoundException
	 * @since 1.2.3
	 */
	public static void setProjections(String authority, File propertiesFile)
			throws FileNotFoundException {
		InputStream intpuStream = new FileInputStream(propertiesFile);
		setProjections(authority, intpuStream);
	}

	/**
	 * Set the projection for the authority and code, creating the authority if
	 * needed
	 * 
	 * @param authority
	 *            coordinate authority
	 * @param code
	 *            coordinate code
	 * @param projection
	 *            proj4 projection
	 * @since 1.2.3
	 */
	public static void setProjection(String authority, long code,
			String projection) {
		setProjection(authority, String.valueOf(code), projection);
	}

	/**
	 * Set the projection for the authority and code, creating the authority if
	 * needed
	 * 
	 * @param authority
	 *            coordinate authority
	 * @param code
	 *            coordinate code
	 * @param projection
	 *            proj4 projection
	 * @since 1.2.3
	 */
	public static void setProjection(String authority, String code,
			String projection) {
		Properties authorityProperties = getOrCreateProjections(authority);
		authorityProperties.setProperty(code, projection);
	}

}
