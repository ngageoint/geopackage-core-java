package mil.nga.geopackage.projection;

import java.util.HashMap;
import java.util.Map;

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
	 * @return
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

}
