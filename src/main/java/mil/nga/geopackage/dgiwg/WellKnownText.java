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
			+ "BASEGEOGCRS[\"WGS 84\","
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
	public static String EPSG_4326 = "GEOGCS[\"WGS 84\","
			+ "DATUM[\"WGS_1984\","
			+ "SPHEROID[\"WGS84\",6378137,298.257223563]],"
			+ "PRIMEM[\"Greenwich\",0],"
			+ "UNIT[\"degree\",0.0174532925199433]]";

	/**
	 * WGS 84 Geographic 3D
	 */
	public static String EPSG_4979 = "GEOGCRS[\"WGS 84\","
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
			+ "VERTCRS[\"EGM2008 geoid height\",VDATUM[\"EGM2008 geoid\","
			+ "ANCHOR[\"WGS 84 ellipsoid\"]],CS[vertical,1],"
			+ "AXIS[\"Gravity-related height (H)\",up],"
			+ "LENGTHUNIT[\"metre\",1.0],ID[\"EPSG\",\"3855\"]],"
			+ "ID[\"EPSG\",\"9518\"]]";

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
	private static String EPSG_32___ = "PROJCS[\"WGS 84 / UTM zone " + ZONE
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

		String wkt = EPSG_32___;

		wkt = wkt.replaceAll(ZONE, String.valueOf(zone));
		wkt = wkt.replaceAll(DIRECTION, direction);
		wkt = wkt.replaceAll(CENTRAL_MERIDIAN, String.valueOf(centralMeridian));
		wkt = wkt.replaceAll(FALSE_NORTHING, String.valueOf(falseNorthing));
		wkt = wkt.replaceAll(EPSG, String.valueOf(epsg));

		return wkt;
	}

}
