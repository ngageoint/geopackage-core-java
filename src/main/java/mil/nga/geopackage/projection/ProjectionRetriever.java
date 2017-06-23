package mil.nga.geopackage.projection;

import java.io.IOException;
import java.io.InputStream;
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
	 * Projections property file name
	 */
	public static final String PROJECTIONS_PROPERTY_FILE = "projections.properties";

	/**
	 * Properties
	 */
	private static Properties mProperties;

	/**
	 * Get the proj4 projection string for the coordinate code
	 * 
	 * @param authority
	 *            coordinate authority
	 * @param code
	 *            coordinate code
	 * @return projection
	 */
	public static String getProjection(String authority, long code) {
		return getProjection(authority, String.valueOf(code));
	}

	/**
	 * Get the proj4 projection string for the coordinate code
	 * 
	 * @param authority
	 *            coordinate authority
	 * @param code
	 *            coordinate code
	 * @return projection
	 */
	public static synchronized String getProjection(String authority,
			String code) {

		// TODO support multiple authorities

		if (mProperties == null) {
			mProperties = initializeConfigurationProperties();
		}
		return mProperties.getProperty(code);
	}

	/**
	 * Initialize the configuration properties
	 * 
	 * @return
	 */
	private static Properties initializeConfigurationProperties() {
		Properties properties = new Properties();

		InputStream in = ProjectionRetriever.class.getResourceAsStream("/"
				+ PROJECTIONS_PROPERTY_FILE);
		if (in != null) {
			try {
				properties.load(in);
			} catch (Exception e) {
				log.log(Level.SEVERE,
						"Failed to load projections properties file: "
								+ PROJECTIONS_PROPERTY_FILE, e);
			} finally {
				try {
					in.close();
				} catch (IOException e) {
					log.log(Level.WARNING,
							"Failed to close projections properties file: "
									+ PROJECTIONS_PROPERTY_FILE, e);
				}
			}
		} else {
			log.log(Level.SEVERE,
					"Failed to load projections properties, file not found: "
							+ PROJECTIONS_PROPERTY_FILE);
		}

		return properties;
	}

}
