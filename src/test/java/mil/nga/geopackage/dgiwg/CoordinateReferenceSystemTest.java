package mil.nga.geopackage.dgiwg;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Set;

import org.junit.Test;
import org.locationtech.proj4j.datum.Datum;
import org.locationtech.proj4j.datum.Ellipsoid;

import mil.nga.crs.CRS;
import mil.nga.crs.CRSType;
import mil.nga.crs.CompoundCoordinateReferenceSystem;
import mil.nga.crs.SimpleCoordinateReferenceSystem;
import mil.nga.crs.common.Identifier;
import mil.nga.crs.geo.Ellipsoids;
import mil.nga.crs.geo.GeoCoordinateReferenceSystem;
import mil.nga.crs.geo.GeoDatum;
import mil.nga.crs.geo.GeoDatums;
import mil.nga.crs.geo.PrimeMeridian;
import mil.nga.crs.geo.PrimeMeridians;
import mil.nga.crs.operation.OperationMethod;
import mil.nga.crs.projected.ProjectedCoordinateReferenceSystem;
import mil.nga.crs.wkt.CRSReader;
import mil.nga.crs.wkt.CRSWriter;
import mil.nga.geopackage.BoundingBox;
import mil.nga.geopackage.GeoPackageConstants;
import mil.nga.geopackage.srs.SpatialReferenceSystem;
import mil.nga.proj.Projection;
import mil.nga.proj.ProjectionConstants;
import mil.nga.proj.ProjectionFactory;

/**
 * DGIWG Coordinate Reference System test
 * 
 * @author osbornb
 */
public class CoordinateReferenceSystemTest {

	/**
	 * Print out each CRS as pretty
	 */
	private static final boolean PRINT_CRS = false;

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
			assertEquals(crs.getDescription(), srs.getDescription());
			String definition = crs.getWkt();
			if (crs.getType() == CRSType.COMPOUND) {
				definition = GeoPackageConstants.UNDEFINED_DEFINITION;
			}
			assertEquals(definition, srs.getDefinition());
			assertEquals(crs.getWkt(), srs.getDefinition_12_063());

			if (PRINT_CRS) {

				BoundingBox bounds = crs.getBounds();
				BoundingBox wgs84Bounds = crs.getWGS84Bounds();

				System.out.println();
				System.out.println(crs.getAuthorityAndCode());
				System.out.println();
				System.out
						.println("Min Longitude: " + bounds.getMinLongitude());
				System.out.println("Min Latitude: " + bounds.getMinLatitude());
				System.out
						.println("Max Longitude: " + bounds.getMaxLongitude());
				System.out.println("Max Latitude: " + bounds.getMaxLatitude());

				if (!bounds.equals(wgs84Bounds)) {
					System.out.println();
					System.out.println(
							"Min Longitude: " + wgs84Bounds.getMinLongitude());
					System.out.println(
							"Min Latitude: " + wgs84Bounds.getMinLatitude());
					System.out.println(
							"Max Longitude: " + wgs84Bounds.getMaxLongitude());
					System.out.println(
							"Max Latitude: " + wgs84Bounds.getMaxLatitude());
				}

				System.out.println();
				System.out.println(CRSWriter.writePretty(crs.getWkt()));
				System.out.println();
			}

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

			String authority = crs.getAuthority();
			long code = crs.getCode();

			CRS crsObject = CRSReader.read(crs.getWkt());
			if (crsObject.getType() == CRSType.COMPOUND) {
				CompoundCoordinateReferenceSystem compound = (CompoundCoordinateReferenceSystem) crsObject;
				SimpleCoordinateReferenceSystem simple = compound
						.getCoordinateReferenceSystem(0);
				Identifier identifier = simple.getIdentifier(0);
				authority = identifier.getName();
				code = Long.valueOf(identifier.getUniqueIdentifier());
			}

			Projection projection2 = ProjectionFactory
					.getCachelessProjection(authority, code);

			compare(projection, projection2);

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
				delta);
		assertEquals(proj.getTrueScaleLatitudeDegrees(),
				proj2.getTrueScaleLatitudeDegrees(), delta);
		assertEquals(proj.getUnits(), proj2.getUnits());

	}

	/**
	 * Test Lambert Conic Conformal 1SP
	 * 
	 * @throws IOException
	 *             upon error
	 */
	@Test
	public void testLambertConicConformal1SP() throws IOException {

		long epsg = 9801;
		String name = "Lambert_Conformal_Conic (1SP)";
		GeoDatums datum = GeoDatums.NAD83;
		double latitudeOfOrigin = 25;
		double centralMeridian = -95;
		double scaleFactor = 1;
		double falseEasting = 0;
		double falseNorthing = 0;
		double standardParallel1 = 25;

		SpatialReferenceSystem srs = CoordinateReferenceSystem
				.createLambertConicConformal1SP(epsg, name, datum,
						latitudeOfOrigin, centralMeridian, scaleFactor,
						falseEasting, falseNorthing, standardParallel1);

		String wkt = WellKnownText.getLambertConicConformal1SP(epsg, name,
				datum, latitudeOfOrigin, centralMeridian, scaleFactor,
				falseEasting, falseNorthing, standardParallel1);

		testLambertConicConformal1SP(epsg, name, datum, latitudeOfOrigin,
				centralMeridian, scaleFactor, falseEasting, falseNorthing,
				standardParallel1, wkt,
				CoordinateReferenceSystem.LAMBERT_CONIC_CONFORMAL_1SP_DESCRIPTION,
				srs);

		epsg = 0000;
		name = "Unnamed Lambert_Conformal_Conic using 1SP";
		datum = GeoDatums.NAD83;
		latitudeOfOrigin = 49;
		centralMeridian = -95;
		scaleFactor = 1;
		falseEasting = 0;
		falseNorthing = 0;
		standardParallel1 = 49;

		srs = CoordinateReferenceSystem.createLambertConicConformal1SP(epsg,
				name, datum, latitudeOfOrigin, centralMeridian, scaleFactor,
				falseEasting, falseNorthing, standardParallel1);

		wkt = WellKnownText.getLambertConicConformal1SP(epsg, name, datum,
				latitudeOfOrigin, centralMeridian, scaleFactor, falseEasting,
				falseNorthing, standardParallel1);

		testLambertConicConformal1SP(epsg, name, datum, latitudeOfOrigin,
				centralMeridian, scaleFactor, falseEasting, falseNorthing,
				standardParallel1, wkt,
				CoordinateReferenceSystem.LAMBERT_CONIC_CONFORMAL_1SP_DESCRIPTION,
				srs);

	}

	/**
	 * Test Lambert Conic Conformal 2SP
	 * 
	 * @throws IOException
	 *             upon error
	 */
	@Test
	public void testLambertConicConformal2SP() throws IOException {

		long epsg = 9802;
		String name = "Lambert Conic Conformal (2SP)";
		GeoDatums datum = GeoDatums.NAD83;
		double standardParallel1 = 30;
		double standardParallel2 = 60;
		double latitudeOfOrigin = 30;
		double centralMeridian = 126;
		double falseEasting = 0;
		double falseNorthing = 0;

		SpatialReferenceSystem srs = CoordinateReferenceSystem
				.createLambertConicConformal2SP(epsg, name, datum,
						standardParallel1, standardParallel2, latitudeOfOrigin,
						centralMeridian, falseEasting, falseNorthing);

		String wkt = WellKnownText.getLambertConicConformal2SP(epsg, name,
				datum, standardParallel1, standardParallel2, latitudeOfOrigin,
				centralMeridian, falseEasting, falseNorthing);

		testLambertConicConformal2SP(epsg, name, datum, standardParallel1,
				standardParallel2, latitudeOfOrigin, centralMeridian,
				falseEasting, falseNorthing, wkt,
				CoordinateReferenceSystem.LAMBERT_CONIC_CONFORMAL_2SP_DESCRIPTION,
				srs);

		epsg = 3978;
		name = "NAD83 / Canada Atlas Lambert";
		datum = GeoDatums.NAD83;
		standardParallel1 = 49;
		standardParallel2 = 77;
		latitudeOfOrigin = 49;
		centralMeridian = -95;
		falseEasting = 0;
		falseNorthing = 0;

		srs = CoordinateReferenceSystem.createLambertConicConformal2SP(epsg,
				name, datum, standardParallel1, standardParallel2,
				latitudeOfOrigin, centralMeridian, falseEasting, falseNorthing);

		wkt = WellKnownText.getLambertConicConformal2SP(epsg, name, datum,
				standardParallel1, standardParallel2, latitudeOfOrigin,
				centralMeridian, falseEasting, falseNorthing);

		testLambertConicConformal2SP(epsg, name, datum, standardParallel1,
				standardParallel2, latitudeOfOrigin, centralMeridian,
				falseEasting, falseNorthing, wkt,
				CoordinateReferenceSystem.LAMBERT_CONIC_CONFORMAL_2SP_DESCRIPTION,
				srs);

	}

	/**
	 * Test Lambert Conic Conformal 1SP
	 * 
	 * @param epsg
	 *            EPSG code
	 * @param name
	 *            name
	 * @param datum
	 *            geo datum
	 * @param latitudeOfOrigin
	 *            latitude of origin
	 * @param centralMeridian
	 *            central meridian
	 * @param scaleFactor
	 *            scale factor
	 * @param falseEasting
	 *            false easting
	 * @param falseNorthing
	 *            false northing
	 * @param standardParallel1
	 *            standard parallel 1
	 * @param wkt
	 *            well-known text
	 * @param description
	 *            description
	 * @param srs
	 *            spatial reference system
	 * @throws IOException
	 *             upon error
	 */
	private void testLambertConicConformal1SP(long epsg, String name,
			GeoDatums datum, double latitudeOfOrigin, double centralMeridian,
			double scaleFactor, double falseEasting, double falseNorthing,
			double standardParallel1, String wkt, String description,
			SpatialReferenceSystem srs) throws IOException {

		OperationMethod method = testLambertConicConformal(epsg, name, datum,
				wkt,
				CoordinateReferenceSystem.LAMBERT_CONIC_CONFORMAL_1SP_DESCRIPTION,
				srs);

		assertEquals("Lambert_Conformal_Conic_1SP", method.getName());
		assertEquals("latitude_of_origin", method.getParameter(0).getName());
		assertEquals(latitudeOfOrigin, method.getParameter(0).getValue(), 0.0);
		assertEquals("central_meridian", method.getParameter(1).getName());
		assertEquals(centralMeridian, method.getParameter(1).getValue(), 0.0);
		assertEquals("scale_factor", method.getParameter(2).getName());
		assertEquals(scaleFactor, method.getParameter(2).getValue(), 0.0);
		assertEquals("false_easting", method.getParameter(3).getName());
		assertEquals(falseEasting, method.getParameter(3).getValue(), 0.0);
		assertEquals("false_northing", method.getParameter(4).getName());
		assertEquals(falseNorthing, method.getParameter(4).getValue(), 0.0);
		assertEquals("standard_parallel_1", method.getParameter(5).getName());
		assertEquals(standardParallel1, method.getParameter(5).getValue(), 0.0);

	}

	/**
	 * Test Lambert Conic Conformal 2SP
	 * 
	 * @param epsg
	 *            EPSG code
	 * @param name
	 *            name
	 * @param datum
	 *            geo datum
	 * @param standardParallel1
	 *            standard parallel 1
	 * @param standardParallel2
	 *            standard parallel 2
	 * @param latitudeOfOrigin
	 *            latitude of origin
	 * @param centralMeridian
	 *            central meridian
	 * @param falseEasting
	 *            false easting
	 * @param falseNorthing
	 *            false northing
	 * @param wkt
	 *            well-known text
	 * @param description
	 *            description
	 * @param srs
	 *            spatial reference system
	 * @throws IOException
	 *             upon error
	 */
	private void testLambertConicConformal2SP(long epsg, String name,
			GeoDatums datum, double standardParallel1, double standardParallel2,
			double latitudeOfOrigin, double centralMeridian,
			double falseEasting, double falseNorthing, String wkt,
			String description, SpatialReferenceSystem srs) throws IOException {

		OperationMethod method = testLambertConicConformal(epsg, name, datum,
				wkt,
				CoordinateReferenceSystem.LAMBERT_CONIC_CONFORMAL_2SP_DESCRIPTION,
				srs);

		assertEquals("Lambert_Conformal_Conic_2SP", method.getName());
		assertEquals("standard_parallel_1", method.getParameter(0).getName());
		assertEquals(standardParallel1, method.getParameter(0).getValue(), 0.0);
		assertEquals("standard_parallel_2", method.getParameter(1).getName());
		assertEquals(standardParallel2, method.getParameter(1).getValue(), 0.0);
		assertEquals("latitude_of_origin", method.getParameter(2).getName());
		assertEquals(latitudeOfOrigin, method.getParameter(2).getValue(), 0.0);
		assertEquals("central_meridian", method.getParameter(3).getName());
		assertEquals(centralMeridian, method.getParameter(3).getValue(), 0.0);
		assertEquals("false_easting", method.getParameter(4).getName());
		assertEquals(falseEasting, method.getParameter(4).getValue(), 0.0);
		assertEquals("false_northing", method.getParameter(5).getName());
		assertEquals(falseNorthing, method.getParameter(5).getValue(), 0.0);

	}

	/**
	 * Test Lambert Conic Conformal
	 * 
	 * @param epsg
	 *            EPSG code
	 * @param name
	 *            name
	 * @param datum
	 *            geo datum
	 * @param wkt
	 *            well-known text
	 * @param description
	 *            description
	 * @param srs
	 *            spatial reference system
	 * @return operation method
	 * @throws IOException
	 *             upon error
	 */
	private OperationMethod testLambertConicConformal(long epsg, String name,
			GeoDatums datum, String wkt, String description,
			SpatialReferenceSystem srs) throws IOException {

		assertEquals(name, srs.getSrsName());
		assertEquals(epsg, srs.getSrsId());
		assertEquals(ProjectionConstants.AUTHORITY_EPSG, srs.getOrganization());
		assertEquals(epsg, srs.getOrganizationCoordsysId());
		assertEquals(wkt, srs.getDefinition());
		assertEquals(description, srs.getDescription());
		assertEquals(wkt, srs.getDefinition_12_063());

		if (PRINT_CRS) {
			System.out.println();
			System.out.println(ProjectionConstants.AUTHORITY_EPSG + ":" + epsg);
			System.out.println();
			System.out.println(CRSWriter.writePretty(wkt));
			System.out.println();
		}

		Projection projection = ProjectionFactory
				.getCachelessProjectionByDefinition(wkt);
		if (projection.getAuthority() != null
				&& !projection.getAuthority().isEmpty()) {
			assertEquals(ProjectionConstants.AUTHORITY_EPSG,
					projection.getAuthority());
		}
		if (projection.getCode() != null && !projection.getCode().isEmpty()) {
			assertEquals(String.valueOf(epsg), projection.getCode());
		}
		assertEquals(wkt, projection.getDefinition());

		CRS crs = projection.getDefinitionCRS();
		assertEquals(CRSType.PROJECTED, crs.getType());

		ProjectedCoordinateReferenceSystem projected = (ProjectedCoordinateReferenceSystem) crs;
		assertEquals(name, projected.getName());
		GeoCoordinateReferenceSystem geo = projected.getBase();
		assertEquals(datum.getCode(), geo.getName());
		GeoDatum geoDatum = geo.getGeoDatum();
		assertEquals(datum.getName(), geoDatum.getName());
		Ellipsoids datumEllipsoid = datum.getEllipsoid();
		mil.nga.crs.geo.Ellipsoid ellipsoid = geoDatum.getEllipsoid();
		assertEquals(datumEllipsoid.getName(), ellipsoid.getName());
		assertEquals(datumEllipsoid.getEquatorRadius(),
				ellipsoid.getSemiMajorAxis(), 0.0);
		assertEquals(datumEllipsoid.getReciprocalFlattening(),
				ellipsoid.getInverseFlattening(), 0.0);
		PrimeMeridian primeMeridian = geoDatum.getPrimeMeridian();
		assertEquals(PrimeMeridians.GREENWICH.getName(),
				primeMeridian.getName());
		assertEquals(PrimeMeridians.GREENWICH.getOffsetFromGreenwichDegrees(),
				primeMeridian.getLongitude(), 0.0);
		OperationMethod operationMethod = projected.getMapProjection()
				.getMethod();
		assertEquals(ProjectionConstants.AUTHORITY_EPSG,
				projected.getIdentifier(0).getName());
		assertEquals(String.valueOf(epsg),
				projected.getIdentifier(0).getUniqueIdentifier());

		return operationMethod;
	}

}
