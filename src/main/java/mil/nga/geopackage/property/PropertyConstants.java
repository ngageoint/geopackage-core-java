package mil.nga.geopackage.property;

/**
 * GeoPackage property constants
 * 
 * @author osbornb
 */
public class PropertyConstants {

	/**
	 * Property file name
	 */
	public static final String PROPERTIES_FILE = "geopackage.properties";

	/**
	 * Property part divider
	 */
	public static final String PROPERTY_DIVIDER = ".";

	public static final String GEO_PACKAGE = "geopackage";

	public static final String SRS = GEO_PACKAGE + PROPERTY_DIVIDER + "srs";

	public static final String WGS_84 = SRS + PROPERTY_DIVIDER + "wgs84";

	public static final String UNDEFINED_CARTESIAN = SRS + PROPERTY_DIVIDER
			+ "undefined_cartesian";

	public static final String UNDEFINED_GEOGRAPHIC = SRS + PROPERTY_DIVIDER
			+ "undefined_geographic";

	public static final String WEB_MERCATOR = SRS + PROPERTY_DIVIDER
			+ "web_mercator";

	/**
	 * @since 1.2.1
	 */
	public static final String WGS_84_3D = SRS + PROPERTY_DIVIDER + "wgs84_3d";

	public static final String SRS_NAME = "srs_name";

	public static final String SRS_ID = "srs_id";

	public static final String ORGANIZATION = "organization";

	public static final String ORGANIZATION_COORDSYS_ID = "organization_coordsys_id";

	public static final String DEFINITION = "definition";

	public static final String DESCRIPTION = "description";

	/**
	 * @since 1.2.1
	 */
	public static final String DEFINITION_12_063 = "definition_12_063";

	public static final String SQL = "sql";

	public static final String EXTENSIONS = GEO_PACKAGE + PROPERTY_DIVIDER
			+ "extensions";

}
