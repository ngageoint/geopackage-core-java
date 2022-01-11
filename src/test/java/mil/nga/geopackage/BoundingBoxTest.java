package mil.nga.geopackage;

import org.junit.Test;

import junit.framework.TestCase;
import mil.nga.proj.ProjectionConstants;
import mil.nga.proj.ProjectionFactory;
import mil.nga.sf.Point;

/**
 * Bounding Box Tests
 * 
 * @author osbornb
 */
public class BoundingBoxTest {

	/**
	 * Test centroid methods
	 */
	@Test
	public void testCentroid() {

		BoundingBox boundingBox = new BoundingBox(5.0, 10.0, 30.0, 55.0);

		Point centroid = boundingBox.getCentroid();

		TestCase.assertEquals(17.5, centroid.getX());
		TestCase.assertEquals(32.5, centroid.getY());

		Point centroid2 = boundingBox.getCentroid(ProjectionFactory
				.getProjection(ProjectionConstants.EPSG_WEB_MERCATOR));

		TestCase.assertEquals(centroid.getX(), centroid2.getX());
		TestCase.assertEquals(centroid.getY(), centroid2.getY());

		Point geometryCentroid = boundingBox.buildGeometry().getCentroid();

		TestCase.assertEquals(geometryCentroid.getX(), centroid.getX());
		TestCase.assertEquals(geometryCentroid.getY(), centroid.getY());

		Point degreesCentroid = boundingBox.getDegreesCentroid();

		TestCase.assertEquals(17.5, degreesCentroid.getX(), 0.00000000000001);
		TestCase.assertEquals(33.12597587060761, degreesCentroid.getY());

		Point degreesCentroid2 = boundingBox.getCentroid(ProjectionFactory
				.getProjection(ProjectionConstants.EPSG_WORLD_GEODETIC_SYSTEM));

		TestCase.assertEquals(degreesCentroid.getX(), degreesCentroid2.getX());
		TestCase.assertEquals(degreesCentroid.getY(), degreesCentroid2.getY());

		Point geometryDegreesCentroid = boundingBox.buildGeometry()
				.getDegreesCentroid();

		TestCase.assertEquals(geometryDegreesCentroid.getX(),
				degreesCentroid.getX());
		TestCase.assertEquals(geometryDegreesCentroid.getY(),
				degreesCentroid.getY());

	}

}
