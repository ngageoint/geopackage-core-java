package mil.nga.geopackage.projection;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.core.srs.SpatialReferenceSystem;

import org.osgeo.proj4j.CRSFactory;
import org.osgeo.proj4j.CoordinateReferenceSystem;

/**
 * Projection factory for coordinate projections and transformations
 * 
 * @author osbornb
 */
public class ProjectionFactory {

	/**
	 * Logger
	 */
	private static final Logger logger = Logger
			.getLogger(ProjectionFactory.class.getName());

	/**
	 * CRS Factory
	 */
	private static final CRSFactory csFactory = new CRSFactory();

	/**
	 * Mapping of authorities to projections
	 */
	private static Map<String, AuthorityProjections> authorities = new HashMap<>();

	/**
	 * Get the projection for the EPSG code
	 * 
	 * @param epsg
	 *            EPSG coordinate code
	 * @return projection
	 */
	public static Projection getProjection(long epsg) {
		return getProjection(ProjectionConstants.AUTHORITY_EPSG,
				String.valueOf(epsg));
	}

	/**
	 * Get the projection for authority and code
	 * 
	 * @param authority
	 *            coordinate authority
	 * @param code
	 *            authority coordinate code
	 * @return projection
	 * @since 1.3.0
	 */
	public static Projection getProjection(String authority, long code) {
		return getProjection(authority, String.valueOf(code));
	}

	/**
	 * Get the projection for authority and code
	 * 
	 * @param authority
	 *            coordinate authority
	 * @param code
	 *            authority coordinate code
	 * @return projection
	 * @since 1.3.0
	 */
	public static Projection getProjection(String authority, String code) {
		return getProjection(authority, code, null, null);
	}

	/**
	 * Get the projection for authority, code, and parameter string
	 * 
	 * @param authority
	 *            coordinate authority
	 * @param code
	 *            authority coordinate code
	 * @param paramStr
	 *            proj4 string
	 * @return projection
	 * @since 1.3.0
	 */
	public static Projection getProjection(String authority, long code,
			String paramStr) {
		return getProjection(authority, String.valueOf(code), paramStr);
	}

	/**
	 * Get the projection for authority, code, and parameter string
	 * 
	 * @param authority
	 *            coordinate authority
	 * @param code
	 *            authority coordinate code
	 * @param paramStr
	 *            proj4 string
	 * @return projection
	 * @since 1.3.0
	 */
	public static Projection getProjection(String authority, String code,
			String paramStr) {
		String[] params = null;
		if (paramStr != null && !paramStr.isEmpty()) {
			params = paramStr.split("\\s+");
		}
		Projection projection = getProjection(authority, code, params);
		return projection;
	}

	/**
	 * Get the projection for authority, code, and parameters
	 * 
	 * @param authority
	 *            coordinate authority
	 * @param code
	 *            authority coordinate code
	 * @param params
	 *            proj4 params array
	 * @return projection
	 * @since 1.3.0
	 */
	public static Projection getProjection(String authority, long code,
			String[] params) {
		return getProjection(authority, String.valueOf(code), params);
	}

	/**
	 * Get the projection for authority, code, and parameters
	 * 
	 * @param authority
	 *            coordinate authority
	 * @param code
	 *            authority coordinate code
	 * @param params
	 *            proj4 params array
	 * @return projection
	 * @since 1.3.0
	 */
	public static Projection getProjection(String authority, String code,
			String[] params) {
		return getProjection(authority, code, params, null);
	}

	/**
	 * Get the projection for the authority, code, definition, and custom
	 * parameter array
	 * 
	 * @param authority
	 *            coordinate authority
	 * @param code
	 *            authority coordinate code
	 * @param params
	 *            proj4 params array
	 * @param definition
	 *            definition
	 * @return projection
	 * @since 1.3.0
	 */
	public static Projection getProjection(String authority, long code,
			String[] params, String definition) {
		return getProjection(authority, String.valueOf(code), params,
				definition);
	}

	/**
	 * Get the projection for the authority, code, definition, and custom
	 * parameter array
	 * 
	 * @param authority
	 *            coordinate authority
	 * @param code
	 *            authority coordinate code
	 * @param params
	 *            proj4 params array
	 * @param definition
	 *            definition
	 * @return projection
	 * @since 1.3.0
	 */
	public static Projection getProjection(String authority, String code,
			String[] params, String definition) {

		// Get or create the authority
		AuthorityProjections authorityProjections = getProjections(authority);

		// Check if the projection already exists
		Projection projection = authorityProjections.getProjection(code);

		if (projection == null) {

			// Try to get or create the projection from a definition
			projection = fromDefinition(authorityProjections, code, definition);

			if (projection == null) {

				// Try to create the projection from the provided params
				projection = fromParams(authorityProjections, code, params);

				if (projection == null) {

					// Try to create the projection from properties
					projection = fromProperties(authorityProjections, code);

					if (projection == null) {

						// Try to create the projection from the authority name
						projection = fromName(authorityProjections, code);

						if (projection == null) {
							throw new GeoPackageException(
									"Failed to create projection for authority: "
											+ authority + ", code: " + code
											+ ", definition: " + definition
											+ ", params: "
											+ Arrays.toString(params));
						}
					}
				}
			}
		}

		return projection;
	}

	/**
	 * Get or create projections for the authority
	 * 
	 * @param authority
	 *            coordinate authority
	 * @return authority projections
	 * @since 1.3.0
	 */
	public static AuthorityProjections getProjections(String authority) {
		AuthorityProjections authorityProjections = authorities.get(authority
				.toUpperCase());
		if (authorityProjections == null) {
			authorityProjections = new AuthorityProjections(authority);
			authorities.put(authority.toUpperCase(), authorityProjections);
		}
		return authorityProjections;
	}

	/**
	 * Clear all authority projections
	 * 
	 * @since 1.3.0
	 */
	public static void clear() {
		authorities.clear();
	}

	/**
	 * Clear the authority projections
	 * 
	 * @param authority
	 *            coordinate authority
	 * @since 1.3.0
	 */
	public static void clear(String authority) {
		getProjections(authority).clear();
	}

	/**
	 * Clear the authority projection code
	 * 
	 * @param authority
	 *            coordinate authority
	 * @param code
	 *            coordinate code
	 * @since 1.3.0
	 */
	public static void clear(String authority, long code) {
		getProjections(authority).clear(code);
	}

	/**
	 * Clear the authority projection code
	 * 
	 * @param authority
	 *            coordinate authority
	 * @param code
	 *            coordinate code
	 * @since 1.3.0
	 */
	public static void clear(String authority, String code) {
		getProjections(authority).clear(code);
	}

	/**
	 * Create a projection from the WKT definition
	 * 
	 * @param authorityProjections
	 *            authority projections
	 * @param code
	 *            coordinate code
	 * @param definition
	 *            WKT coordinate definition
	 * @return projection
	 */
	private static Projection fromDefinition(
			AuthorityProjections authorityProjections, String code,
			String definition) {

		Projection projection = null;

		if (definition != null && !definition.isEmpty()) {

			String parametersString = "";
			// TODO parse WKT definition into proj4 parameters

			// Try to create the projection from the parameters
			if (parametersString != null && !parametersString.isEmpty()) {
				try {
					CoordinateReferenceSystem crs = csFactory
							.createFromParameters(
									coordinateName(
											authorityProjections.getAuthority(),
											code), parametersString);
					projection = new Projection(
							authorityProjections.getAuthority(), code, crs);
					authorityProjections.addProjection(projection);
				} catch (Exception e) {
					logger.log(Level.WARNING,
							"Failed to create projection for authority: "
									+ authorityProjections.getAuthority()
									+ ", code: " + code + ", definition: "
									+ definition + ", parameters: "
									+ parametersString, e);
				}
			}

		}

		return projection;
	}

	/**
	 * Create a projection from the proj4 parameters
	 * 
	 * @param authorityProjections
	 *            authority projections
	 * @param code
	 *            coordinate code
	 * @param params
	 *            proj4 parameters
	 * @return projection
	 */
	private static Projection fromParams(
			AuthorityProjections authorityProjections, String code,
			String[] params) {

		Projection projection = null;

		if (params != null && params.length > 0) {
			try {
				CoordinateReferenceSystem crs = csFactory.createFromParameters(
						coordinateName(authorityProjections.getAuthority(),
								code), params);
				projection = new Projection(
						authorityProjections.getAuthority(), code, crs);
				authorityProjections.addProjection(projection);
			} catch (Exception e) {
				logger.log(
						Level.WARNING,
						"Failed to create projection for authority: "
								+ authorityProjections.getAuthority()
								+ ", code: " + code + ", parameters: "
								+ Arrays.toString(params), e);
			}
		}

		return projection;
	}

	/**
	 * Create a projection from configured coordinate properties
	 * 
	 * @param authorityProjections
	 *            authority projections
	 * @param code
	 *            coordinate code
	 * @return projection
	 */
	private static Projection fromProperties(
			AuthorityProjections authorityProjections, String code) {

		Projection projection = null;

		String parameters = ProjectionRetriever.getProjection(
				authorityProjections.getAuthority(), code);

		if (parameters != null && !parameters.isEmpty()) {
			try {
				CoordinateReferenceSystem crs = csFactory.createFromParameters(
						coordinateName(authorityProjections.getAuthority(),
								code), parameters);
				projection = new Projection(
						authorityProjections.getAuthority(), code, crs);
				authorityProjections.addProjection(projection);
			} catch (Exception e) {
				logger.log(Level.WARNING,
						"Failed to create projection for authority: "
								+ authorityProjections.getAuthority()
								+ ", code: " + code + ", parameters: "
								+ parameters, e);
			}
		}

		return projection;
	}

	/**
	 * Create a projection from the coordinate authority and code name
	 * 
	 * @param authorityProjections
	 *            authority projections
	 * @param code
	 *            coordinate code
	 * @return projection
	 */
	private static Projection fromName(
			AuthorityProjections authorityProjections, String code) {

		Projection projection = null;

		String name = coordinateName(authorityProjections.getAuthority(), code);
		try {
			CoordinateReferenceSystem crs = csFactory.createFromName(name);
			projection = new Projection(authorityProjections.getAuthority(),
					code, crs);
			authorityProjections.addProjection(projection);
		} catch (Exception e) {
			logger.log(Level.WARNING, "Failed to create projection from name: "
					+ name, e);
		}

		return projection;
	}

	/**
	 * Build a coordinate name from teh authority and code
	 * 
	 * @param authority
	 *            coordinate authority
	 * @param code
	 *            coordinate code
	 * @return name
	 */
	private static String coordinateName(String authority, String code) {
		return authority.toUpperCase() + ":" + code;
	}

	/**
	 * Get the projection for the Spatial Reference System
	 * 
	 * @param srs
	 *            spatial reference system
	 * @return projection
	 * @since 1.1.8
	 */
	public static Projection getProjection(SpatialReferenceSystem srs) {

		String authority = srs.getOrganization();
		long code = srs.getOrganizationCoordsysId();
		String definition = srs.getDefinition_12_063();
		if (definition == null) {
			definition = srs.getDefinition();
		}

		Projection projection = getProjection(authority, code, null, definition);

		return projection;
	}

}
