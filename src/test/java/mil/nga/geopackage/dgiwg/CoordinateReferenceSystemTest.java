package mil.nga.geopackage.dgiwg;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Set;

import org.junit.Test;
import org.locationtech.proj4j.datum.Datum;
import org.locationtech.proj4j.datum.Ellipsoid;

import mil.nga.crs.wkt.CRSWriter;
import mil.nga.geopackage.srs.SpatialReferenceSystem;
import mil.nga.proj.Projection;
import mil.nga.proj.ProjectionFactory;

/**
 * DGIWG Coordinate Reference System test
 * 
 * @author osbornb
 */
public class CoordinateReferenceSystemTest {

	/**
	 * Test the Coordinate Reference Systems
	 * 
	 * @throws IOException
	 *             upon error
	 */
	@Test
	public void testCRS() throws IOException {

		for (CoordinateReferenceSystem crs : CoordinateReferenceSystem
				.values()) {

			SpatialReferenceSystem srs = crs.createSpatialReferenceSystem();

			assertEquals(crs.getName(), srs.getSrsName());
			assertEquals(crs.getCode(), srs.getSrsId());
			assertEquals(crs.getAuthority(), srs.getOrganization());
			assertEquals(crs.getCode(), srs.getOrganizationCoordsysId());
			assertEquals(crs.getWkt(), srs.getDefinition());
			assertEquals(crs.getDescription(), srs.getDescription());
			if (crs == CoordinateReferenceSystem.EPSG_4979) {
				assertEquals(crs.getWkt(), srs.getDefinition_12_063());
			} else {
				assertNull(srs.getDefinition_12_063());
			}

			// TODO
			System.out.println(CRSWriter.writePretty(crs.getWkt()));

			Projection projection = ProjectionFactory
					.getCachelessProjectionByDefinition(crs.getWkt());
			if (projection.getAuthority() != null
					&& !projection.getAuthority().isEmpty()) {
				assertEquals(crs.getAuthority(), projection.getAuthority());
			}
			if (projection.getCode() != null
					&& !projection.getCode().isEmpty()) {
				assertEquals(String.valueOf(crs.getCode()),
						projection.getCode());
			}
			assertEquals(crs.getWkt(), projection.getDefinition());
			assertEquals(crs.getType(),
					projection.getDefinitionCRS().getType());

			Projection projection2 = ProjectionFactory
					.getCachelessProjection(crs.getAuthority(), crs.getCode());

			// TODO
			// compare(projection, projection2);

		}

	}

	/**
	 * Test Tiles 2D
	 */
	@Test
	public void testTiles2D() {

		Set<CoordinateReferenceSystem> crs = CoordinateReferenceSystem
				.getCoordinateReferenceSystems(DataType.TILES_2D);

		assertTrue(crs.contains(
				CoordinateReferenceSystem.getCoordinateReferenceSystem(3395)));
		assertTrue(crs.contains(
				CoordinateReferenceSystem.getCoordinateReferenceSystem(3857)));
		assertTrue(crs.contains(
				CoordinateReferenceSystem.getCoordinateReferenceSystem(5041)));
		assertTrue(crs.contains(
				CoordinateReferenceSystem.getCoordinateReferenceSystem(5042)));

		for (long code = UTMZone.NORTH_MIN; code <= UTMZone.NORTH_MAX; code++) {
			assertTrue(crs.contains(CoordinateReferenceSystem
					.getCoordinateReferenceSystem(code)));
		}

		for (long code = UTMZone.SOUTH_MIN; code <= UTMZone.SOUTH_MAX; code++) {
			assertTrue(crs.contains(CoordinateReferenceSystem
					.getCoordinateReferenceSystem(code)));
		}

	}

	/**
	 * Test Tiles 3D
	 */
	@Test
	public void testTiles3D() {

		Set<CoordinateReferenceSystem> crs = CoordinateReferenceSystem
				.getCoordinateReferenceSystems(DataType.TILES_3D);

		assertTrue(crs.contains(
				CoordinateReferenceSystem.getCoordinateReferenceSystem(3395)));
		assertTrue(crs.contains(
				CoordinateReferenceSystem.getCoordinateReferenceSystem(4326)));
		assertTrue(crs.contains(
				CoordinateReferenceSystem.getCoordinateReferenceSystem(4979)));
		assertTrue(crs.contains(
				CoordinateReferenceSystem.getCoordinateReferenceSystem(5041)));
		assertTrue(crs.contains(
				CoordinateReferenceSystem.getCoordinateReferenceSystem(5042)));

	}

	/**
	 * Test Features 2D
	 */
	@Test
	public void testFeatures2D() {

		Set<CoordinateReferenceSystem> crs = CoordinateReferenceSystem
				.getCoordinateReferenceSystems(DataType.FEATURES_2D);

		assertTrue(crs.contains(
				CoordinateReferenceSystem.getCoordinateReferenceSystem(4326)));

	}

	/**
	 * Test Features 3D
	 */
	@Test
	public void testFeatures3D() {

		Set<CoordinateReferenceSystem> crs = CoordinateReferenceSystem
				.getCoordinateReferenceSystems(DataType.FEATURES_3D);

		assertTrue(crs.contains(
				CoordinateReferenceSystem.getCoordinateReferenceSystem(4979)));
		assertTrue(crs.contains(
				CoordinateReferenceSystem.getCoordinateReferenceSystem(9518)));

	}

	/**
	 * Compare two projections
	 * 
	 * @param projection
	 *            projection
	 * @param projection2
	 *            projection 2
	 */
	private void compare(Projection projection, Projection projection2) {

		double delta = 0.0000000000001;

		if (projection.getAuthority() != null
				&& !projection.getAuthority().isEmpty()
				&& projection.getCode() != null
				&& !projection.getCode().isEmpty()) {
			assertEquals(projection, projection2);
		}

		org.locationtech.proj4j.CoordinateReferenceSystem crs = projection
				.getCrs();
		org.locationtech.proj4j.CoordinateReferenceSystem crs2 = projection2
				.getCrs();
		Datum datum = crs.getDatum();
		Datum datum2 = crs2.getDatum();
		Ellipsoid ellipsoid = datum.getEllipsoid();
		Ellipsoid ellipsoid2 = datum2.getEllipsoid();
		double[] transform = datum.getTransformToWGS84();
		double[] transform2 = datum2.getTransformToWGS84();
		org.locationtech.proj4j.proj.Projection proj = crs.getProjection();
		org.locationtech.proj4j.proj.Projection proj2 = crs2.getProjection();

		assertEquals(ellipsoid.getEccentricitySquared(),
				ellipsoid2.getEccentricitySquared(), 0);
		assertEquals(ellipsoid.getEquatorRadius(),
				ellipsoid2.getEquatorRadius(), 0);
		assertEquals(ellipsoid.getA(), ellipsoid2.getA(), 0);
		assertEquals(ellipsoid.getB(), ellipsoid2.getB(), 0);

		assertEquals(proj.getEllipsoid().getEccentricitySquared(),
				proj2.getEllipsoid().getEccentricitySquared(), 0);
		assertEquals(proj.getEllipsoid().getEquatorRadius(),
				proj2.getEllipsoid().getEquatorRadius(), 0);
		assertEquals(proj.getEllipsoid().getA(), proj2.getEllipsoid().getA(),
				0);
		assertEquals(proj.getEllipsoid().getB(), proj2.getEllipsoid().getB(),
				0);

		if (transform != null || transform2 != null) {
			if (transform != null && transform2 != null) {
				int transformLength = transform.length;
				int transform2Length = transform2.length;
				assertTrue(transformLength == transform2Length
						|| (transformLength == 3 && transform2Length == 7)
						|| (transformLength == 7 && transform2Length == 3));
				for (int i = 0; i < Math.max(transformLength,
						transform2Length); i++) {
					if (i < transformLength && i < transform2Length) {
						assertEquals(transform[i], transform2[i], 0);
					} else if (i < transformLength) {
						assertEquals(i < 6 ? 0.0 : 1.0, transform[i], 0);
					} else {
						assertEquals(i < 6 ? 0.0 : 1.0, transform2[i], 0);
					}
				}
			} else {
				double[] transformTest = transform != null ? transform
						: transform2;
				for (int i = 0; i < transformTest.length; i++) {
					assertEquals(0, transformTest[i], 0);
				}
			}
		}

		assertEquals(proj.getAlpha(), proj2.getAlpha(), 0);
		assertEquals(proj.getAxisOrder(), proj2.getAxisOrder());
		if (proj2.getEPSGCode() != 0) {
			assertEquals(proj.getEPSGCode(), proj2.getEPSGCode(), 0);
		}
		assertEquals(proj.getEquatorRadius(), proj2.getEquatorRadius(), 0);
		assertEquals(proj.getFalseEasting(), proj2.getFalseEasting(), 0);
		assertEquals(proj.getFalseNorthing(), proj2.getFalseNorthing(), 0);
		assertEquals(proj.getFromMetres(), proj2.getFromMetres(), 0);
		assertEquals(proj.getLonC(), proj2.getLonC(), 0);
		assertEquals(proj.getMaxLatitude(), proj2.getMaxLatitude(), 0);
		assertEquals(proj.getMaxLatitudeDegrees(),
				proj2.getMaxLatitudeDegrees(), 0);
		assertEquals(proj.getMaxLongitude(), proj2.getMaxLongitude(), 0);
		assertEquals(proj.getMaxLongitudeDegrees(),
				proj2.getMaxLongitudeDegrees(), 0);
		assertEquals(proj.getMinLatitude(), proj2.getMinLatitude(), 0);
		assertEquals(proj.getMinLatitudeDegrees(),
				proj2.getMinLatitudeDegrees(), 0);
		assertEquals(proj.getMinLongitude(), proj2.getMinLongitude(), 0);
		assertEquals(proj.getMinLongitudeDegrees(),
				proj2.getMinLongitudeDegrees(), 0);
		assertEquals(proj.getPrimeMeridian(), proj2.getPrimeMeridian());
		assertEquals(proj.getProjectionLatitude(),
				proj2.getProjectionLatitude(), 0);
		assertEquals(proj.getProjectionLatitude1(),
				proj2.getProjectionLatitude1(), 0);
		assertEquals(proj.getProjectionLatitude1Degrees(),
				proj2.getProjectionLatitude1Degrees(), 0);
		assertEquals(proj.getProjectionLatitude2(),
				proj2.getProjectionLatitude2(), 0);
		assertEquals(proj.getProjectionLatitude2Degrees(),
				proj2.getProjectionLatitude2Degrees(), 0);
		assertEquals(proj.getProjectionLatitudeDegrees(),
				proj2.getProjectionLatitudeDegrees(), 0);
		assertEquals(proj.getProjectionLongitude(),
				proj2.getProjectionLongitude(), delta);
		assertEquals(proj.getProjectionLongitudeDegrees(),
				proj2.getProjectionLongitudeDegrees(), delta);
		assertEquals(proj.getScaleFactor(), proj2.getScaleFactor(), 0);
		assertEquals(proj.getTrueScaleLatitude(), proj2.getTrueScaleLatitude(),
				0);
		assertEquals(proj.getTrueScaleLatitudeDegrees(),
				proj2.getTrueScaleLatitudeDegrees(), 0);
		assertEquals(proj.getUnits(), proj2.getUnits());

	}

}
