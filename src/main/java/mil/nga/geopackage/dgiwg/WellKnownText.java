package mil.nga.geopackage.dgiwg;

import mil.nga.crs.CRSType;
import mil.nga.crs.geo.Ellipsoids;
import mil.nga.crs.geo.GeoDatums;
import mil.nga.crs.geo.PrimeMeridians;
import mil.nga.crs.wkt.CRSKeyword;
import mil.nga.geopackage.GeoPackageException;

/**
 * DGIWG (Defence Geospatial Information Working Group) Well-Known Text
 * constants
 * 
 * @author osbornb
 * @since 6.6.0
 */
public class WellKnownText {

	/**
	 * CRS name replacement
	 */
	private static final String CRS_NAME = wrapReplacement("crs_name");

	/**
	 * CRS type replacement
	 */
	private static final String CRS_TYPE = wrapReplacement("crs_type");

	/**
	 * Base CRS name replacement
	 */
	private static final String BASE_NAME = wrapReplacement("base_name");

	/**
	 * Reference name replacement
	 */
	private static final String REFERENCE_NAME = wrapReplacement(
			"reference_name");

	/**
	 * Ellipsoid name replacement
	 */
	private static final String ELLIPSOID_NAME = wrapReplacement(
			"ellipsoid_name");

	/**
	 * Semi major axis replacement
	 */
	private static final String SEMI_MAJOR_AXIS = wrapReplacement(
			"semi_major_axis");

	/**
	 * Inverse flattening replacement
	 */
	private static final String INVERSE_FLATTENING = wrapReplacement(
			"inverse_flattening");

	/**
	 * Prime meridian name replacement
	 */
	private static final String PRIME_MERIDIAN_NAME = wrapReplacement(
			"prime_meridian_name");

	/**
	 * Prime meridian international reference meridian longitude replacement
	 */
	private static final String IRM_LONGITUDE = wrapReplacement(
			"irm_longitude");

	/**
	 * Latitude of origin replacement
	 */
	private static final String LATITUDE_OF_ORIGIN = wrapReplacement(
			"latitude_of_origin");

	/**
	 * Central meridian replacement
	 */
	private static final String CENTRAL_MERIDIAN = wrapReplacement(
			"central_meridian");

	/**
	 * Scale factor replacement
	 */
	private static final String SCALE_FACTOR = wrapReplacement("scale_factor");

	/**
	 * False easting replacement
	 */
	private static final String FALSE_EASTING = wrapReplacement(
			"false_easting");

	/**
	 * False northing replacement
	 */
	private static final String FALSE_NORTHING = wrapReplacement(
			"false_northing");

	/**
	 * Standard parallel 1 replacement
	 */
	private static final String STANDARD_PARALLEL_1 = wrapReplacement(
			"standard_parallel_1");

	/**
	 * Standard parallel 2 replacement
	 */
	private static final String STANDARD_PARALLEL_2 = wrapReplacement(
			"standard_parallel_2");

	/**
	 * Identifier unique identifier
	 */
	private static final String IDENTIFIER_UNIQUE_ID = wrapReplacement(
			"identifier_id");

	/**
	 * UTM Zone number replacement
	 */
	private static final String ZONE = wrapReplacement("zone");

	/**
	 * UTM Zone direction replacement
	 */
	private static final String DIRECTION = wrapReplacement("direction");

	/**
	 * Wrap the replacement value with < and >
	 * 
	 * @param value
	 *            replacement value
	 * @return wrapped value
	 */
	private static final String wrapReplacement(String value) {
		return "<" + value + ">";
	}

	/**
	 * ETRS89-extended / LAEA Europe
	 */
	public static String EPSG_3035 = "PROJCS[\"ETRS89-extended / LAEA Europe\","
			+ "GEOGCS[\"ETRS89\","
			+ "DATUM[\"European_Terrestrial_Reference_System_1989\","
			+ "SPHEROID[\"GRS 1980\",6378137,298.257222101,"
			+ "AUTHORITY[\"EPSG\",\"7019\"]],"
			+ "AUTHORITY[\"EPSG\",\"6258\"]],"
			+ "PRIMEM[\"Greenwich\",0,AUTHORITY[\"EPSG\",\"8901\"]],"
			+ "UNIT[\"degree\",0.0174532925199433,"
			+ "AUTHORITY[\"EPSG\",\"9122\"]],"
			+ "AUTHORITY[\"EPSG\",\"4258\"]],"
			+ "PROJECTION[\"Lambert_Azimuthal_Equal_Area\"],"
			+ "PARAMETER[\"latitude_of_center\",52],"
			+ "PARAMETER[\"longitude_of_center\",10],"
			+ "PARAMETER[\"false_easting\",4321000],"
			+ "PARAMETER[\"false_northing\",3210000],"
			+ "UNIT[\"metre\",1,AUTHORITY[\"EPSG\",\"9001\"]],"
			+ "AUTHORITY[\"EPSG\",\"3035\"]]";

	/**
	 * WGS 84 / World Mercator
	 */
	public static String EPSG_3395 = "PROJCRS[\"WGS 84 / World Mercator\","
			+ "BASEGEODCRS[\"WGS 84\","
			+ "DATUM[\"World Geodetic System 1984\","
			+ "ELLIPSOID[\"WGS 84\",6378137,298.257223563]]],"
			+ "CONVERSION[\"Mercator\","
			+ "METHOD[\"Mercator (variant A)\",ID[\"EPSG\",\"9804\"]],"
			+ "PARAMETER[\"Latitude of natural origin\",0,"
			+ "ANGLEUNIT[\"degree\",0.0174532925199433]],"
			+ "PARAMETER[\"Longitude of natural origin\",0,"
			+ "ANGLEUNIT[\"degree\",0.0174532925199433]],"
			+ "PARAMETER[\"Scale factor at natural origin\",1,"
			+ "SCALEUNIT[\"unity\",1.0]],"
			+ "PARAMETER[\"False easting\",0,LENGTHUNIT[\"metre\",1.0]],"
			+ "PARAMETER[\"False northing\",0,LENGTHUNIT[\"metre\",1.0]],"
			+ "ID[\"EPSG\",\"19833\"]],CS[Cartesian,2],"
			+ "AXIS[\"Easting (E)\",east,ORDER[1]],"
			+ "AXIS[\"Northing (N)\",north,ORDER[2]],"
			+ "LENGTHUNIT[\"metre\",1.0],ID[\"EPSG\",\"3395\"]]";

	/**
	 * EGM2008 geoid height
	 */
	public static String EPSG_3855 = "VERTCRS[\"EGM2008 geoid height\",VDATUM[\"EGM2008 geoid\","
			+ "ANCHOR[\"WGS 84 ellipsoid\"]],CS[vertical,1],"
			+ "AXIS[\"Gravity-related height (H)\",up],"
			+ "LENGTHUNIT[\"metre\",1.0],ID[\"EPSG\",\"3855\"]]";

	/**
	 * WGS 84 / Pseudo-Mercator
	 */
	public static String EPSG_3857 = "PROJCS[\"WGS 84 / Pseudo-Mercator\","
			+ "GEOGCS[\"WGS 84\",DATUM[\"WGS_1984\","
			+ "SPHEROID[\"WGS 84\",6378137,298.257223563,"
			+ "ID[\"EPSG\",\"7030\"]],ID[\"EPSG\",\"6326\"]],"
			+ "PRIMEM[\"Greenwich\",0,ID[\"EPSG\",\"8901\"]],"
			+ "UNIT[\"degree\",0.0174532925199433,"
			+ "ID[\"EPSG\",\"9122\"]],ID[\"EPSG\",\"4326\"]],"
			+ "PROJECTION[\"Mercator_1SP\"],"
			+ "PARAMETER[\"central_meridian\",0],"
			+ "PARAMETER[\"scale_factor\",1],"
			+ "PARAMETER[\"false_easting\",0],"
			+ "PARAMETER[\"false_northing\",0],"
			+ "UNIT[\"metre\",1,ID[\"EPSG\",\"9001\"]],"
			+ "AXIS[\"X\",EAST],AXIS[\"Y\",NORTH],ID[\"EPSG\",\"3857\"]]";

	/**
	 * NAD83 / Canada Atlas Lambert
	 */
	public static String EPSG_3978 = "PROJCS[\"NAD83 / Canada Atlas Lambert\","
			+ "GEOGCS[\"NAD83\",DATUM[\"North_American_Datum_1983\","
			+ "SPHEROID[\"GRS 1980\",6378137,298.257222101]],"
			+ "PRIMEM[\"Greenwich\",0,AUTHORITY[\"EPSG\",\"8901\"]],"
			+ "UNIT[\"degree\",0.0174532925199433,"
			+ "AUTHORITY[\"EPSG\",\"9122\"]],"
			+ "AUTHORITY[\"EPSG\",\"4269\"]],"
			+ "PROJECTION[\"Lambert_Conformal_Conic_2SP\"],"
			+ "PARAMETER[\"latitude_of_origin\",49],"
			+ "PARAMETER[\"central_meridian\",-95],"
			+ "PARAMETER[\"standard_parallel_1\",49],"
			+ "PARAMETER[\"standard_parallel_2\",77],"
			+ "PARAMETER[\"false_easting\",0],"
			+ "PARAMETER[\"false_northing\",0],"
			+ "UNIT[\"metre\",1,AUTHORITY[\"EPSG\",\"9001\"]],"
			+ "AXIS[\"Easting\",EAST],AXIS[\"Northing\",NORTH],"
			+ "AUTHORITY[\"EPSG\",\"3978\"]]";

	/**
	 * WGS 84 Geographic 2D
	 */
	public static String EPSG_4326 = "GEOCCS[\"WGS 84\","
			+ "DATUM[\"WGS_1984\","
			+ "SPHEROID[\"WGS84\",6378137,298.257223563]],"
			+ "PRIMEM[\"Greenwich\",0],"
			+ "UNIT[\"degree\",0.0174532925199433]]";

	/**
	 * WGS 84 Geographic 3D
	 */
	public static String EPSG_4979 = "GEODCRS[\"WGS 84\","
			+ "DATUM[\"World Geodetic System 1984\","
			+ "ELLIPSOID[\"WGS 84\",6378137,298.257223563,"
			+ "LENGTHUNIT[\"metre\",1.0]]],CS[ellipsoidal,3],"
			+ "AXIS[\"Geodetic latitude (Lat)\",north,"
			+ "ANGLEUNIT[\"degree\",0.0174532925199433]],"
			+ "AXIS[\"Geodetic longitude (Long)\",east,"
			+ "ANGLEUNIT[\"degree\",0.0174532925199433]],"
			+ "AXIS[\"Ellipsoidal height (h)\",up,"
			+ "LENGTHUNIT[\"metre\",1.0]],ID[\"EPSG\",4979]]";

	/**
	 * WGS 84 / UPS North (E,N)
	 */
	public static String EPSG_5041 = "PROJCRS[\"WGS 84 / UPS North (E,N)\","
			+ "BASEGEODCRS[\"WGS 84\","
			+ "DATUM[\"World Geodetic System 1984\","
			+ "ELLIPSOID[\"WGS 84\",6378137,298.257223563,"
			+ "LENGTHUNIT[\"metre\",1.0]]]],"
			+ "CONVERSION[\"Universal Polar Stereographic North\","
			+ "METHOD[\"Polar Stereographic (variant A)\",ID[\"EPSG\",\"9810\"]],"
			+ "PARAMETER[\"Latitude of natural origin\",90,"
			+ "ANGLEUNIT[\"degree\",0.0174532925199433]],"
			+ "PARAMETER[\"Longitude of natural origin\",0,"
			+ "ANGLEUNIT[\"degree\",0.0174532925199433]],"
			+ "PARAMETER[\"Scale factor at natural origin\",0.994,"
			+ "SCALEUNIT[\"unity\",1.0]],"
			+ "PARAMETER[\"False easting\",2000000,"
			+ "LENGTHUNIT[\"metre\",1.0]],"
			+ "PARAMETER[\"False northing\",2000000,"
			+ "LENGTHUNIT[\"metre\",1.0]],ID[\"EPSG\",\"16061\"]],"
			+ "CS[Cartesian,2],AXIS[\"Easting (E)\",south,"
			+ "MERIDIAN[90,ANGLEUNIT[\"degree\",0.0174532925199433]],"
			+ "ORDER[1]],AXIS[\"Northing (N)\",south,"
			+ "MERIDIAN[180,ANGLEUNIT[\"degree\",0.0174532925199433]],"
			+ "ORDER[2]],LENGTHUNIT[\"metre\",1.0],ID[\"EPSG\",\"5041\"]]";

	/**
	 * WGS 84 / UPS South (E,N)
	 */
	public static String EPSG_5042 = "PROJCRS[\"WGS 84 / UPS South (E,N)\","
			+ "BASEGEODCRS[\"WGS 84\","
			+ "DATUM[\"World Geodetic System 1984\","
			+ "ELLIPSOID[\"WGS 84\",6378137,298.257223563,"
			+ "LENGTHUNIT[\"metre\",1.0]]]],"
			+ "CONVERSION[\"Universal Polar Stereographic North\","
			+ "METHOD[\"Polar Stereographic (variant A)\",ID[\"EPSG\",\"9810\"]],"
			+ "PARAMETER[\"Latitude of natural origin\",-90,"
			+ "ANGLEUNIT[\"degree\",0.0174532925199433]],"
			+ "PARAMETER[\"Longitude of natural origin\",0,"
			+ "ANGLEUNIT[\"degree\",0.0174532925199433]],"
			+ "PARAMETER[\"Scale factor at natural origin\",0.994,"
			+ "SCALEUNIT[\"unity\",1.0]],"
			+ "PARAMETER[\"False easting\",2000000,"
			+ "LENGTHUNIT[\"metre\",1.0]],"
			+ "PARAMETER[\"False northing\",2000000,"
			+ "LENGTHUNIT[\"metre\",1.0]],ID[\"EPSG\",\"16161\"]],"
			+ "CS[Cartesian,2],AXIS[\"Easting (E)\",north,"
			+ "MERIDIAN[90,ANGLEUNIT[\"degree\",0.0174532925199433]],"
			+ "ORDER[1]],AXIS[\"Northing (N)\",north,"
			+ "MERIDIAN[0,ANGLEUNIT[\"degree\",0.0174532925199433]],"
			+ "ORDER[2]],LENGTHUNIT[\"metre\",1.0],ID[\"EPSG\",\"5042\"]]";

	/**
	 * WGS84 4326 + EGM2008 height 3855
	 */
	public static String EPSG_9518 = "COMPOUNDCRS[\"WGS84 Height EGM08\","
			+ "GEODCRS[\"WGS 84\",DATUM[\"World Geodetic System 1984\","
			+ "ELLIPSOID[\"WGS 84\",6378137,298.257223563,"
			+ "LENGTHUNIT[\"metre\",1.0]]],CS[ellipsoidal,2],"
			+ "AXIS[\"Geodetic latitude (Lat)\",north],"
			+ "AXIS[\"Geodetic longitude (Long)\",east],"
			+ "ANGLEUNIT[\"degree\",0.0174532925199433],ID[\"EPSG\",4326]],"
			+ EPSG_3855 + ",ID[\"EPSG\",\"9518\"]]";

	/**
	 * UTM zone 01 - 60N, 01 - 60S with substitutions
	 */
	private static String EPSG_UTM_ZONE = "PROJCS[\"WGS 84 / UTM zone " + ZONE
			+ DIRECTION + "\",GEOGCS[\"WGS 84\",DATUM[\"WGS_1984\","
			+ "SPHEROID[\"WGS84\",6378137,298.257223563,"
			+ "ID[\"EPSG\", \"7030\"]],ID[\"EPSG\", \"6326\"]],"
			+ "PRIMEM[\"Greenwich\",0,ID[\"EPSG\",\"8901\"]],"
			+ "UNIT[\"degree\", 0.0174532925199433,"
			+ "ID[\"EPSG\",\"9122\"]],ID[\"EPSG\",\"4326\"]],"
			+ "PROJECTION[\"Transverse_Mercator\"],"
			+ "PARAMETER[\"latitude_of_origin\", 0],"
			+ "PARAMETER[\"central_meridian\", " + CENTRAL_MERIDIAN + "],"
			+ "PARAMETER[\"scale_factor\",0.9996],"
			+ "PARAMETER[\"false_easting\",500000],"
			+ "PARAMETER[\"false_northing\"," + FALSE_NORTHING + "],"
			+ "UNIT[\"metre\", 1,ID[\"EPSG\",\"9001\"]],"
			+ "AXIS[\"Easting\",EAST],AXIS[\"Northing\",NORTH],"
			+ "ID[\"EPSG\",\"" + IDENTIFIER_UNIQUE_ID + "\"]]";

	/**
	 * Get the UTM Zone Well-Known Text
	 * 
	 * @param epsg
	 *            UTM Zone EPSG
	 * @return UTM Zone Well-Known Text
	 */
	public static String getUTMZone(long epsg) {

		long zone = UTMZone.getZone(epsg);
		String direction = UTMZone.getLatDirection(epsg);
		long centralMeridian = UTMZone.getCentralMeridian(zone);
		long falseNorthing = UTMZone.getFalseNorthing(epsg);

		String wkt = EPSG_UTM_ZONE;

		wkt = wkt.replaceAll(ZONE, String.valueOf(zone));
		wkt = wkt.replaceAll(DIRECTION, direction);
		wkt = wkt.replaceAll(CENTRAL_MERIDIAN, String.valueOf(centralMeridian));
		wkt = wkt.replaceAll(FALSE_NORTHING, String.valueOf(falseNorthing));
		wkt = wkt.replaceAll(IDENTIFIER_UNIQUE_ID, String.valueOf(epsg));

		return wkt;
	}

	/**
	 * Lambert Conic Conformal 1SP with substitutions
	 */
	private static String EPSG_LAMBERT_CONFORMAL_CONIC_1SP = "PROJCS[\""
			+ CRS_NAME + "\"," + CRS_TYPE + "[\"" + BASE_NAME + "\","
			+ "DATUM[\"" + REFERENCE_NAME + "\"," + "SPHEROID[\""
			+ ELLIPSOID_NAME + "\"," + SEMI_MAJOR_AXIS + ","
			+ INVERSE_FLATTENING + "]]," + "PRIMEM[\"" + PRIME_MERIDIAN_NAME
			+ "\"," + IRM_LONGITUDE + "],"
			+ "UNIT[\"degree\",0.0174532925199433]],"
			+ "PROJECTION[\"Lambert_Conformal_Conic_1SP\"],"
			+ "PARAMETER[\"latitude_of_origin\"," + LATITUDE_OF_ORIGIN + "],"
			+ "PARAMETER[\"central_meridian\"," + CENTRAL_MERIDIAN + "],"
			+ "PARAMETER[\"scale_factor\"," + SCALE_FACTOR + "],"
			+ "PARAMETER[\"false_easting\"," + FALSE_EASTING + "],"
			+ "PARAMETER[\"false_northing\"," + FALSE_NORTHING + "],"
			+ "UNIT[\"Meter\",1],"
			+ "AXIS[\"X\",EAST],AXIS[\"Y\",NORTH],AUTHORITY[\"EPSG\",\""
			+ IDENTIFIER_UNIQUE_ID + "\"]]";

	/**
	 * Lambert Conic Conformal 2SP with substitutions
	 */
	private static String EPSG_LAMBERT_CONFORMAL_CONIC_2SP = "PROJCS[\""
			+ CRS_NAME + "\"," + CRS_TYPE + "[\"" + BASE_NAME + "\","
			+ "DATUM[\"" + REFERENCE_NAME + "\"," + "SPHEROID[\""
			+ ELLIPSOID_NAME + "\"," + SEMI_MAJOR_AXIS + ","
			+ INVERSE_FLATTENING + "]]," + "PRIMEM[\"" + PRIME_MERIDIAN_NAME
			+ "\"," + IRM_LONGITUDE + "],"
			+ "UNIT[\"degree\",0.01745329251994328]],"
			+ "PROJECTION[\"Lambert_Conformal_Conic_2SP\"],"
			+ "PARAMETER[\"standard_parallel_1\"," + STANDARD_PARALLEL_1 + "],"
			+ "PARAMETER[\"standard_parallel_2\"," + STANDARD_PARALLEL_2 + "],"
			+ "PARAMETER[\"latitude_of_origin\"," + LATITUDE_OF_ORIGIN + "],"
			+ "PARAMETER[\"central_meridian\"," + CENTRAL_MERIDIAN + "],"
			+ "PARAMETER[\"false_easting\"," + FALSE_EASTING + "],"
			+ "PARAMETER[\"false_northing\"," + FALSE_NORTHING + "],"
			+ "AXIS[\"X\",EAST],AXIS[\"Y\",NORTH],AUTHORITY[\"EPSG\",\""
			+ IDENTIFIER_UNIQUE_ID + "\"]]";

	/**
	 * Get Lambert Conic Conformal 1SP well-known text
	 * 
	 * @param epsg
	 *            Lambert Conic Conformal 1SP EPSG
	 * @param name
	 *            CRS name
	 * @param crsType
	 *            CRS type
	 * @param geoDatum
	 *            {@link GeoDatums#WGS84}, {@link GeoDatums#ETRS89}, or
	 *            {@link GeoDatums#NAD83}
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
	 * @return well-known text
	 */
	public static String getLambertConicConformal1SP(long epsg, String name,
			CRSType crsType, GeoDatums geoDatum, double latitudeOfOrigin,
			double centralMeridian, double scaleFactor, double falseEasting,
			double falseNorthing) {

		String wkt = getLambertConicConformal(EPSG_LAMBERT_CONFORMAL_CONIC_1SP,
				epsg, name, crsType, geoDatum, latitudeOfOrigin,
				centralMeridian, falseEasting, falseNorthing);

		wkt = wkt.replaceAll(SCALE_FACTOR, String.valueOf(scaleFactor));

		return wkt;
	}

	/**
	 * Get Lambert Conic Conformal 2SP well-known text
	 * 
	 * @param epsg
	 *            Lambert Conic Conformal 2SP EPSG
	 * @param name
	 *            CRS name
	 * @param crsType
	 *            CRS type
	 * @param geoDatum
	 *            {@link GeoDatums#WGS84}, {@link GeoDatums#ETRS89}, or
	 *            {@link GeoDatums#NAD83}
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
	 * @return well-known text
	 */
	public static String getLambertConicConformal2SP(long epsg, String name,
			CRSType crsType, GeoDatums geoDatum, double standardParallel1,
			double standardParallel2, double latitudeOfOrigin,
			double centralMeridian, double falseEasting, double falseNorthing) {

		String wkt = getLambertConicConformal(EPSG_LAMBERT_CONFORMAL_CONIC_2SP,
				epsg, name, crsType, geoDatum, latitudeOfOrigin,
				centralMeridian, falseEasting, falseNorthing);

		wkt = wkt.replaceAll(STANDARD_PARALLEL_1,
				String.valueOf(standardParallel1));
		wkt = wkt.replaceAll(STANDARD_PARALLEL_2,
				String.valueOf(standardParallel2));

		return wkt;
	}

	/**
	 * Get Lambert Conic Conformal well-known text
	 * 
	 * @param wkt
	 *            starting well-known text
	 * @param epsg
	 *            Lambert Conic Conformal EPSG
	 * @param name
	 *            CRS name
	 * @param crsType
	 *            CRS type
	 * @param geoDatum
	 *            {@link GeoDatums#WGS84}, {@link GeoDatums#ETRS89}, or
	 *            {@link GeoDatums#NAD83}
	 * @param latitudeOfOrigin
	 *            latitude of origin
	 * @param centralMeridian
	 *            central meridian
	 * @param falseEasting
	 *            false easting
	 * @param falseNorthing
	 *            false northing
	 * @return well-known text
	 */
	private static String getLambertConicConformal(String wkt, long epsg,
			String name, CRSType crsType, GeoDatums geoDatum,
			double latitudeOfOrigin, double centralMeridian,
			double falseEasting, double falseNorthing) {

		String crsKeyword;
		switch (crsType) {
		case GEODETIC:
			crsKeyword = CRSKeyword.GEODCRS.name();
			break;
		case GEOGRAPHIC:
			crsKeyword = CRSKeyword.GEOGCS.name();
			break;
		default:
			throw new GeoPackageException("Invalid Lambert Conformal CRS type: "
					+ (crsType != null ? crsType.name() : crsType));
		}

		switch (geoDatum) {
		case WGS84:
		case ETRS89:
		case NAD83:
			break;
		default:
			throw new GeoPackageException("Invalid Lambert Conformal datum: "
					+ (geoDatum != null ? geoDatum.getName() : geoDatum));
		}

		Ellipsoids ellipsoids = geoDatum.getEllipsoid();
		PrimeMeridians primeMeridian = PrimeMeridians.GREENWICH;

		wkt = wkt.replaceAll(CRS_NAME, name);
		wkt = wkt.replaceAll(CRS_TYPE, crsKeyword);
		wkt = wkt.replaceAll(BASE_NAME, geoDatum.getCode());
		wkt = wkt.replaceAll(REFERENCE_NAME, geoDatum.getName());
		wkt = wkt.replaceAll(ELLIPSOID_NAME, ellipsoids.getName());
		wkt = wkt.replaceAll(SEMI_MAJOR_AXIS,
				String.valueOf(ellipsoids.getEquatorRadius()));
		wkt = wkt.replaceAll(INVERSE_FLATTENING,
				String.valueOf(ellipsoids.getReciprocalFlattening()));
		wkt = wkt.replaceAll(PRIME_MERIDIAN_NAME, primeMeridian.getName());
		wkt = wkt.replaceAll(IRM_LONGITUDE,
				String.valueOf(primeMeridian.getOffsetFromGreenwichDegrees()));
		wkt = wkt.replaceAll(LATITUDE_OF_ORIGIN,
				String.valueOf(latitudeOfOrigin));
		wkt = wkt.replaceAll(CENTRAL_MERIDIAN, String.valueOf(centralMeridian));
		wkt = wkt.replaceAll(FALSE_EASTING, String.valueOf(falseEasting));
		wkt = wkt.replaceAll(FALSE_NORTHING, String.valueOf(falseNorthing));
		wkt = wkt.replaceAll(IDENTIFIER_UNIQUE_ID, String.valueOf(epsg));

		return wkt;
	}

}
