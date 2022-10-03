package mil.nga.geopackage.geom;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import mil.nga.proj.Projection;
import mil.nga.proj.ProjectionConstants;
import mil.nga.proj.ProjectionFactory;
import mil.nga.sf.GeometryEnvelope;
import mil.nga.sf.LineString;
import mil.nga.sf.Point;
import mil.nga.sf.Polygon;
import mil.nga.sf.util.GeometryUtils;

/**
 * Geometry Crop Test
 * 
 * @author osbornb
 */
public class GeometryCropTest {

	/**
	 * Test crop geometry data
	 */
	@Test
	public void testCropGeometryData() {

		Projection wgs84 = ProjectionFactory
				.getProjection(ProjectionConstants.EPSG_WORLD_GEODETIC_SYSTEM);

		Polygon polygon = new Polygon();
		LineString ring = new LineString();
		ring.addPoint(new Point(100, 80));
		ring.addPoint(new Point(120, 80));
		ring.addPoint(new Point(120, 89));
		ring.addPoint(new Point(110, 80));
		ring.addPoint(new Point(100, 89));
		ring.addPoint(new Point(100, 80));
		polygon.addRing(ring);

		GeoPackageGeometryData geometryData = GeoPackageGeometryData.create(0,
				polygon);

		GeometryCrop.crop(wgs84, geometryData);

		assertTrue(GeometryUtils.wgs84EnvelopeWithWebMercator()
				.contains(geometryData.getGeometry().getEnvelope()));

	}

	/**
	 * Test crop geometry data with envelope
	 */
	@Test
	public void testCropGeometryDataWithEnvelope() {

		Projection wgs84 = ProjectionFactory
				.getProjection(ProjectionConstants.EPSG_WORLD_GEODETIC_SYSTEM);

		Polygon polygon = new Polygon();
		LineString ring = new LineString();
		ring.addPoint(new Point(-10, -10));
		ring.addPoint(new Point(0, -5));
		ring.addPoint(new Point(10, -10));
		ring.addPoint(new Point(5, 0));
		ring.addPoint(new Point(10, 10));
		ring.addPoint(new Point(0, 5));
		ring.addPoint(new Point(-10, 10));
		ring.addPoint(new Point(-5, 0));
		ring.addPoint(new Point(-10, -10));
		polygon.addRing(ring);

		GeoPackageGeometryData geometryData = GeoPackageGeometryData.create(0,
				polygon);

		GeometryEnvelope envelope = new GeometryEnvelope(-5, -5, 5, 5);

		GeometryCrop.crop(wgs84, geometryData, envelope);

		double accuracy = 0.00000000000001;
		GeometryEnvelope compareEnvelope = new GeometryEnvelope(
				envelope.getMinX() - accuracy, envelope.getMinY() - accuracy,
				envelope.getMaxX() + accuracy, envelope.getMaxY() + accuracy);

		assertTrue(compareEnvelope
				.contains(geometryData.getGeometry().getEnvelope()));

	}

}
