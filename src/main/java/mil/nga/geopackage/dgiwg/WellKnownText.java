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
	 * WGS 84 / World Mercator
	 */
	public static String EPSG_3395 = "PROJCRS[\"WGS 84 / World Mercator\","
			+ "BASEGEODCRS[\"WGS 84\","
			+ "DATUM[\"World Geodetic System 1984\"," + "ELLIPSOID[\"WGS"
			+ "84\",6378137,298.257223563]]]," + "CONVERSION[\"Mercator\","
			+ "METHOD[\"Mercator (variant" + "A)\",ID[\"EPSG\",\"9804\"]],"
			+ "PARAMETER[\"Latitude of natural origin\",0,"
			+ "ANGLEUNIT[\"degree\",0.0174532925199433]],"
			+ "PARAMETER[\"Longitude of natural origin\",0,"
			+ "ANGLEUNIT[\"degree\",0.0174532925199433]],"
			+ "PARAMETER[\"Scale factor at natural origin\",1,"
			+ "SCALEUNIT[\"unity\",1.0]]," + "PARAMETER[\"False"
			+ "easting\",0,LENGTHUNIT[\"metre\",1.0]]," + "PARAMETER[\"False"
			+ "northing\",0,LENGTHUNIT[\"metre\",1.0]],"
			+ "ID[\"EPSG\",\"19833\"]]," + "CS[Cartesian,2],"
			+ "AXIS[\"Easting (E)\",east,ORDER[1]],"
			+ "AXIS[\"Northing (N)\",north,ORDER[2]],"
			+ "LENGTHUNIT[\"metre\",1.0]" + "ID[\"EPSG\",\"3395\"]]";

	/**
	 * WGS 84 Geographic 3D
	 */
	public static String EPSG_4979 = "GEODCRS[\"WGS 84\","
			+ "DATUM[\"World Geodetic System 1984\","
			+ "ELLIPSOID[\"WGS 84\",6378137,298.257223563,"
			+ "LENGTHUNIT[\"metre\",1.0]]]," + "CS[ellipsoidal,3],"
			+ "AXIS[\"Geodetic latitude (Lat)\",north,"
			+ "ANGLEUNIT[\"degree\",0.0174532925199433]],"
			+ "AXIS[\"Geodetic longitude (Long)\",east,"
			+ "ANGLEUNIT[\"degree\",0.0174532925199433]],"
			+ "AXIS[\"Ellipsoidal height (h)\",up,"
			+ "LENGTHUNIT[\"metre\",1.0]]," + "ID[\"EPSG\",4979]]";

	/**
	 * UTM Zone number replacement
	 */
	private static final String ZONE = "<zone>";

	/**
	 * UTM Zone direction replacement
	 */
	private static final String DIRECTION = "<direction>";

	/**
	 * UTM Zone central meridian replacement
	 */
	private static final String CENTRAL_MERIDIAN = "<central_meridian>";

	/**
	 * UTM Zone false northing replacement
	 */
	private static final String FALSE_NORTHING = "<false_northing>";

	/**
	 * UTM Zone epsg replacement
	 */
	private static final String EPSG = "<epsg>";

	/**
	 * UTM zone 01 - 60N, 01 - 60S with substitutions
	 */
	private static String EPSG_326__ = "PROJCS[\"WGS 84 / UTM zone " + ZONE
			+ DIRECTION + "\"," + "GEOGCRS[\"WGS 84\"," + "DATUM[\"WGS_1984\","
			+ "SPHEROID[\"WGS84\",6378137,298.257223563,"
			+ "ID[\"EPSG\", \"7030\"]]," + "ID[\"EPSG\", \"6326\"]],"
			+ "PRIMEM[\"Greenwich\",0," + "ID[\"EPSG\",\"8901\"]],"
			+ "UNIT[\"degree\", 0.0174532925199433," + "ID[\"EPSG\",\"9122\"]],"
			+ "ID[\"EPSG\",\"4326\"]]," + "PROJECTION[\"Transverse_Mercator\"],"
			+ "PARAMETER[\"latitude_of_origin\", 0],"
			+ "PARAMETER[\"central_meridian\", " + CENTRAL_MERIDIAN + "],"
			+ "PARAMETER[\"scale_factor\",0.9996],"
			+ "PARAMETER[\"false_easting\",500000],"
			+ "PARAMETER[\"false_northing\"," + FALSE_NORTHING + "],"
			+ "UNIT[\"metre\", 1," + "ID[\"EPSG\",\"9001\"]],"
			+ "AXIS[\"Easting\",EAST]," + "AXIS[\"Northing\",NORTH],"
			+ "ID[\"EPSG\",\"" + EPSG + "\"]]";

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

		String wkt = EPSG_326__;

		wkt = wkt.replaceAll(ZONE, String.valueOf(zone));
		wkt = wkt.replaceAll(DIRECTION, direction);
		wkt = wkt.replaceAll(CENTRAL_MERIDIAN, String.valueOf(centralMeridian));
		wkt = wkt.replaceAll(FALSE_NORTHING, String.valueOf(falseNorthing));
		wkt = wkt.replaceAll(EPSG, String.valueOf(epsg));

		return wkt;
	}

}
