package mil.nga.geopackage.dgiwg;

/**
 * DGIWG (Defence Geospatial Information Working Group) Well-Known Text
 * constants
 * 
 * @author osbornb
 * @since 6.1.2
 */
public class WellKnownText {

	/**
	 * CRS name replacement
	 */
	private static final String CRS_NAME = wrapReplacement("crs_name");

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
			+ "GEOGCRS[\"WGS 84\",DATUM[\"WGS_1984\","
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
	public static String EPSG_9518 = "COMPOUNDCRS[\"WGS84 Height (EGM08)\","
			+ "GEODCRS[\"WGS 84\",DATUM[\"World Geodetic System 1984\","
			+ "ELLIPSOID[\"WGS 84\",6378137,298.257223563,"
			+ "LENGTHUNIT[\"metre\",1.0]]],CS[ellipsoidal,2],"
			+ "AXIS[\"Geodetic latitude (Lat)\",north],"
			+ "AXIS[\"Geodetic longitude (Long)\",east],"
			+ "ANGLEUNIT[\"degree\",0.0174532925199433],ID[\"EPSG\",4326]],"
			+ EPSG_3855 + ",ID[\"EPSG\",\"9518\"]]";

	/**
	 * Lambert Conformal Conic 1SP TODO
	 */
	public static String EPSG_9801 = "PROJCS[\"Lambert_Conformal_Conic (1SP)\","
			+ "GEODCRS[\"GCS_North_American_1983\","
			+ "DATUM[\"North_American_Datum_1983\","
			+ "SPHEROID[\"GRS_1980\",6371000,0]]," + "PRIMEM[\"Greenwich\",0],"
			+ "UNIT[\"Degree\",0.017453292519943295]],"
			+ "PROJECTION[\"Lambert_Conformal_Conic_1SP\"],"
			+ "PARAMETER[\"latitude_of_origin\",25],"
			+ "PARAMETER[\"central_meridian\",-95],"
			+ "PARAMETER[\"scale_factor\",1],"
			+ "PARAMETER[\"false_easting\",0],"
			+ "PARAMETER[\"false_northing\",0],"
			+ "PARAMETER[\"standard_parallel_1\",25],"
			+ "UNIT[\"Meter\",1],AUTHORITY[\"EPSG\",\"9801\"]]";

	// TODO just validate CRS has either Lambert Conformal Conic 1SP or 2SP?

	public static String TEMP_1SP = "PROJCS[\"Unnamed Lambert_Conformal_Conic using 1SP\","
			+ "GEOGCS[\"NAD83\"," + "DATUM[\"North_American_Datum_1983\","
			+ "SPHEROID[\"GRS 1980\",6378137,298.257222101,AUTHORITY[\"EPSG\",\"7019\"]],"
			+ "AUTHORITY[\"EPSG\",\"6269\"]],"
			+ "PRIMEM[\"Greenwich\",0,AUTHORITY[\"EPSG\",\"8901\"]],"
			+ "UNIT[\"degree\",0.0174532925199433,AUTHORITY[\"EPSG\",\"9108\"]],"
			+ "PROJECTION[\"Lambert_Conformal_Conic_1SP\"],"
			+ "PARAMETER[\"latitude_of_origin\",49],"
			+ "PARAMETER[\"central_meridian\",-95],"
			+ "PARAMETER[\"scale_factor\",1],"
			+ "PARAMETER[\"false_easting\",0],"
			+ "PARAMETER[\"false_northing\",0]," + "UNIT[\"Meter\",1]],"
			+ "AUTHORITY[\"EPSG\",\"0000\"]],"
			+ "AXIS[\"X\",EAST],AXIS[\"Y\",NORTH]]";

	public static String EPSG_LAMBERT_CONFORMAL_CONIC_1SP = "PROJCS[\""
			+ CRS_NAME + "\"," + "GEODCRS[\"" + BASE_NAME + "\"," + "DATUM[\""
			+ REFERENCE_NAME + "\"," + "SPHEROID[\"" + ELLIPSOID_NAME + "\","
			+ SEMI_MAJOR_AXIS + "," + INVERSE_FLATTENING + "]]," + "PRIMEM[\""
			+ PRIME_MERIDIAN_NAME + "\"," + IRM_LONGITUDE + "],"
			+ "UNIT[\"degree\",0.0174532925199433]],"
			+ "PROJECTION[\"Lambert_Conformal_Conic_1SP\"],"
			+ "PARAMETER[\"latitude_of_origin\",<latitude>],"
			+ "PARAMETER[\"central_meridian\",<longitude>],"
			+ "PARAMETER[\"scale_factor\",1],"
			+ "PARAMETER[\"false_easting\",0],"
			+ "PARAMETER[\"false_northing\",0],"
			+ "PARAMETER[\"standard_parallel_1\",25]," + "UNIT[\"Meter\",1],"
			+ "AUTHORITY[\"EPSG\",\"" + IDENTIFIER_UNIQUE_ID + "\"]]";

	/*
	 * If PROJECTION is Lambert_Conformal_Conic_1SP, then DATUM = WGS84,
	 * European_Terrestrial_Reference_System_1989 , North_American_Datum_1983
	 * SPHEROID = WGS84, GRS 1980 PRIMEM = Greenwich, <irm_longitude> value
	 * between 0.0 and 359.0. PARAMETER – values according to a valid Lambert
	 * Conic Conformal projection for 1 standard parallel in Table-20 in section
	 * 7.2. PROJECTION AUTHORITY = Valid EPSG code for this LCC 1SP projection.
	 */

	/**
	 * Lambert Conformal Conic 2SP TODO
	 */
	public static String EPSG_9802 = "PROJCS[\"Lambert Conic Conformal (2SP)\","
			+ "GEODCRS[\"GCS_North_American_1983\","
			+ "DATUM[\"North_American_Datum_1983\","
			+ "SPHEROID[\"GRS_1980\",6378160,298.2539162964695]],"
			+ "PRIMEM[\"Greenwich\",0],"
			+ "UNIT[\"degree\",0.0174532925199433]],"
			+ "PROJECTION[\"Lambert_Conformal_Conic_2SP\"],"
			+ "PARAMETER[\"standard_parallel_1\",30],"
			+ "PARAMETER[\"standard_parallel_2\",60],"
			+ "PARAMETER[\"latitude_of_origin\",30],"
			+ "PARAMETER[\"central_meridian\",126],"
			+ "PARAMETER[\"false_easting\",0],"
			+ "PARAMETER[\"false_northing\",0],"
			+ "AUTHORITY[\"EPSG\",\"9802\"]]";

	public static String TEMP_2SP = "PROJCS[\"NAD83 / Canada Atlas Lambert\","
			+ "GEOGCS[\"NAD83\"," + "DATUM[\"North_American_Datum_1983\","
			+ "SPHEROID[\"GRS 1980\",6378137,298.257222101,AUTHORITY[\"EPSG\",\"7019\"]],"
			+ "AUTHORITY[\"EPSG\",\"6269\"]],"
			+ "PRIMEM[\"Greenwich\",0,AUTHORITY[\"EPSG\",\"8901\"]],"
			+ "UNIT[\"degree\",0.01745329251994328,AUTHORITY[\"EPSG\",\"9122\"]],"
			+ "AUTHORITY[\"EPSG\",\"4269\"]],"
			+ "UNIT[\"metre\",1,AUTHORITY[\"EPSG\",\"9001\"]],"
			+ "PROJECTION[\"Lambert_Conformal_Conic_2SP\"],"
			+ "PARAMETER[\"standard_parallel_1\",49],"
			+ "PARAMETER[\"standard_parallel_2\",77],"
			+ "PARAMETER[\"latitude_of_origin\",49],"
			+ "PARAMETER[\"central_meridian\",-95],"
			+ "PARAMETER[\"false_easting\",0],"
			+ "PARAMETER[\"false_northing\",0],"
			+ "AUTHORITY[\"EPSG\",\"3978\"],"
			+ "AXIS[\"Easting\",EAST],AXIS[\"Northing\",NORTH]]";

	public static String EPSG_LAMBERT_CONFORMAL_CONIC_2SP = "PROJCS[\""
			+ CRS_NAME + "\"," + "GEODCS[\"" + BASE_NAME + "\"," + "DATUM[\""
			+ REFERENCE_NAME + "\"," + "SPHEROID[\"" + ELLIPSOID_NAME + "\","
			+ SEMI_MAJOR_AXIS + "," + INVERSE_FLATTENING + "]]," + "PRIMEM[\""
			+ PRIME_MERIDIAN_NAME + "\"," + IRM_LONGITUDE + "],"
			+ "UNIT[\"degree\",0.01745329251994328]],"
			+ "PROJECTION[\"Lambert_Conformal_Conic_2SP\"],"
			+ "PARAMETER[\"standard_parallel_1\",<latitude>],"
			+ "PARAMETER[\"standard_parallel_2\",<longitude>],"
			+ "PARAMETER[\"latitude_of_origin\",<latitude>],"
			+ "PARAMETER[\"central_meridian\",126],"
			+ "PARAMETER[\"false_easting\",0],"
			+ "PARAMETER[\"false_northing\",0]," + "AUTHORITY[\"EPSG\",\""
			+ IDENTIFIER_UNIQUE_ID + "\"]]";

	/*
	 * If PROJECTION is Lambert_Conformal_Conic_2SP, then DATUM = WGS84,
	 * European_Terrestrial_Reference_System_1989 , North_American_Datum_1983
	 * SPHEROID = WGS84, GRS 1980 PRIMEM = Greenwich, <irm_longitude> value
	 * between 0.0 and 359.0. PARAMETER – values according to a valid Lambert
	 * Conic Conformal projection for 2 standard parallels in Table-21 in
	 * section 7.2. PROJECTION AUTHORITY = Valid EPSG code for this LCC 2SP
	 * projection.
	 */

	// CRS_NAME = name
	// GeoDatums = WGS84, ETRS89, NAD83
	// BASE_NAME = GeoDatums.code
	// REFERENCE_NAME = GeoDatums.name
	// ELLIPSOID_NAME = GeoDatums.Ellipsoids.name
	// SEMI_MAJOR_AXIS = GeoDatums.Ellipsoids.equatorRadius
	// INVERSE_FLATTENING = GeoDatums.Ellipsoids.reciprocalFlattening
	// PRIME_MERIDIAN_NAME = PrimeMeridians.name
	// IRM_LONGITUDE = PrimeMeridians.offsetFromGreenwichDegrees
	// IDENTIFIER_UNIQUE_ID = epsg

	/**
	 * UTM zone 01 - 60N, 01 - 60S with substitutions
	 */
	private static String EPSG_UTM_ZONE = "PROJCS[\"WGS 84 / UTM zone " + ZONE
			+ DIRECTION + "\",GEOGCRS[\"WGS 84\",DATUM[\"WGS_1984\","
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

}
