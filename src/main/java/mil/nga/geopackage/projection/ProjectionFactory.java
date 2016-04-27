package mil.nga.geopackage.projection;

import java.util.HashMap;
import java.util.Map;

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
	 * @return projection
	 */
	public static Projection getProjection(long epsg) {
		Projection projection = projections.get(epsg);
		if (projection == null) {

			String parameters = ProjectionRetriever.getProjection(epsg);

			CoordinateReferenceSystem crs = csFactory.createFromParameters(
					String.valueOf(epsg), parameters);
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
		Projection projection = getProjection(epsg);

		return projection;
	}

}
