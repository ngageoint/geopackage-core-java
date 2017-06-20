package mil.nga.geopackage.projection;

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
	 * Mapping of EPSG projection codes to projections
	 */
	private static Map<Long, Projection> projections = new HashMap<Long, Projection>();

	/**
	 * CRS Factory
	 */
	private static final CRSFactory csFactory = new CRSFactory();

	/**
	 * Get the projection for the EPSG code
	 * 
	 * @param epsg
	 *            EPSG code
	 * @return projection
	 */
	public static Projection getProjection(long epsg) {
		String[] params = null;
		Projection projection = getProjection(epsg, params);
		return projection;
	}

	/**
	 * Get the projection for the EPSG code and custom parameter string
	 * 
	 * @param epsg
	 *            EPSG code
	 * @param paramStr
	 *            proj4 string
	 * @return projection
	 * @since 1.2.3
	 */
	public static Projection getProjection(long epsg, String paramStr) {
		String[] params = null;
		if (paramStr != null && !paramStr.isEmpty()) {
			params = paramStr.split("\\s+");
		}
		Projection projection = getProjection(epsg, params);
		return projection;
	}

	/**
	 * Get the projection for the EPSG code and custom parameter array
	 * 
	 * @param epsg
	 *            EPSG code
	 * @param params
	 *            proj4 params array
	 * @return projection
	 * @since 1.2.3
	 */
	public static Projection getProjection(long epsg, String[] params) {
		Projection projection = projections.get(epsg);
		if (projection == null) {

			CoordinateReferenceSystem crs = null;
			String parameters = null;
			String epsgName = null;

			// Try to create the projection from the provided params
			if (params != null && params.length > 0) {
				try {
					crs = csFactory.createFromParameters(String.valueOf(epsg),
							params);
				} catch (Exception e) {
					logger.log(Level.WARNING,
							"Failed to create projection for epsg " + epsg
									+ " from custom provided parameters: "
									+ params, e);
				}
			}

			if (crs == null) {

				// Get the projection parameters from the properties
				if (epsg == -1 || epsg == 0) {
					parameters = ProjectionRetriever
							.getProjection(ProjectionConstants.EPSG_WORLD_GEODETIC_SYSTEM);
				} else {
					parameters = ProjectionRetriever.getProjection(epsg);
				}

				// Try to create the projection from the parameters
				if (parameters != null) {
					try {
						crs = csFactory.createFromParameters(
								String.valueOf(epsg), parameters);
					} catch (Exception e) {
						logger.log(Level.WARNING,
								"Failed to create projection for epsg " + epsg
										+ " from parameters: " + parameters, e);
					}
				}

				// If failed try to create the projection from the EPSG name
				if (crs == null) {
					epsgName = "EPSG:" + epsg;
					try {
						crs = csFactory.createFromName(epsgName);
					} catch (Exception e) {
						logger.log(Level.WARNING,
								"Failed to create projection from name: "
										+ epsgName, e);
					}
				}
			}

			// Throw an error if projection could not be supported
			if (crs == null) {
				throw new GeoPackageException(
						"Failed to create projection for EPSG " + epsg
								+ ". Property params: " + parameters
								+ ". EPSG Name: " + epsgName
								+ ". Custom params: " + params);
			}

			projection = new Projection(epsg, crs);

			projections.put(epsg, projection);
		}
		return projection;
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

		long epsg = srs.getOrganizationCoordsysId();
		String definition = srs.getDefinition_12_063();
		if (definition == null) {
			definition = srs.getDefinition();
		}

		String paramStr = null;
		// TODO parse WKT definition into proj4 parameters

		Projection projection = getProjection(epsg, paramStr);

		return projection;
	}

}
