package mil.nga.geopackage.geom;

import org.locationtech.proj4j.units.Units;

import mil.nga.proj.Projection;
import mil.nga.proj.ProjectionConstants;
import mil.nga.proj.ProjectionFactory;
import mil.nga.sf.Geometry;
import mil.nga.sf.GeometryEnvelope;
import mil.nga.sf.proj.GeometryTransform;
import mil.nga.sf.util.GeometryUtils;

/**
 * Geometry Crop utilities
 * 
 * @author osbornb
 * @since 6.5.0
 */
public class GeometryCrop {

	/**
	 * Crop the geometry data with a world map envelope, defined in the provided
	 * projection
	 * 
	 * @param projection
	 *            geometry data and envelope projection
	 * @param geometryData
	 *            geometry data
	 */
	public static void crop(Projection projection,
			GeoPackageGeometryData geometryData) {
		GeometryEnvelope envelope = envelope(projection);
		crop(projection, geometryData, envelope);
	}

	/**
	 * Crop the geometry data with the envelope, both in the provided projection
	 * 
	 * @param projection
	 *            geometry data and envelope projection
	 * @param geometryData
	 *            geometry data
	 * @param envelope
	 *            geometry envelope
	 */
	public static void crop(Projection projection,
			GeoPackageGeometryData geometryData, GeometryEnvelope envelope) {

		if (geometryData != null && !geometryData.isEmpty()) {

			Geometry geometry = geometryData.getGeometry();
			Geometry bounded = crop(projection, geometry, envelope);

			geometryData.setGeometry(bounded);
		}

	}

	/**
	 * Crop the geometry with a world map envelope, defined in the provided
	 * projection
	 * 
	 * @param projection
	 *            geometry and envelope projection
	 * @param geometry
	 *            geometry
	 * @return cropped geometry
	 */
	public static Geometry crop(Projection projection, Geometry geometry) {
		GeometryEnvelope envelope = envelope(projection);
		return crop(projection, geometry, envelope);
	}

	/**
	 * Crop the geometry with the envelope, both in the provided projection
	 * 
	 * @param projection
	 *            geometry and envelope projection
	 * @param geometry
	 *            geometry
	 * @param envelope
	 *            geometry envelope
	 * @return cropped geometry
	 */
	public static Geometry crop(Projection projection, Geometry geometry,
			GeometryEnvelope envelope) {

		GeometryTransform transform = null;

		if (!projection.isUnit(Units.METRES)) {
			transform = new GeometryTransform(projection, ProjectionFactory
					.getProjection(ProjectionConstants.EPSG_WEB_MERCATOR));
			GeometryUtils.boundWGS84Transformable(geometry);
			geometry = transform.transform(geometry);
			envelope = transform.transform(envelope);
		}

		Geometry cropped = GeometryUtils.crop(geometry, envelope);

		if (transform != null) {
			transform = transform.getInverseTransformation();
			cropped = transform.transform(cropped);
			GeometryUtils.minimizeWGS84(cropped);
		}

		return cropped;
	}

	/**
	 * Get a geometry envelope for the projection
	 * 
	 * @param projection
	 *            projection
	 * @return envelope
	 */
	public static GeometryEnvelope envelope(Projection projection) {
		GeometryEnvelope envelope = null;
		if (projection.isUnit(Units.METRES)) {
			envelope = GeometryUtils.webMercatorEnvelope();
		} else {
			envelope = GeometryUtils.wgs84EnvelopeWithWebMercator();
		}
		return envelope;
	}

}
