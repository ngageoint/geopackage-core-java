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

	/**
	 * GeoPackage
	 */
	public static final String GEO_PACKAGE = "geopackage";

	/**
	 * Spatial Reference Systems
	 */
	public static final String SRS = GeoPackageProperties
			.buildProperty(GEO_PACKAGE, "srs");

	/**
	 * WGS 84 SRS
	 */
	public static final String WGS_84 = GeoPackageProperties.buildProperty(SRS,
			"wgs84");

	/**
	 * Undefined Cartesian SRS
	 */
	public static final String UNDEFINED_CARTESIAN = GeoPackageProperties
			.buildProperty(SRS, "undefined_cartesian");

	/**
	 * Undefined Geographic SRS
	 */
	public static final String UNDEFINED_GEOGRAPHIC = GeoPackageProperties
			.buildProperty(SRS, "undefined_geographic");

	/**
	 * Web Mercator SRS
	 */
	public static final String WEB_MERCATOR = GeoPackageProperties
			.buildProperty(SRS, "web_mercator");

	/**
	 * WGS 84 3D SRS
	 * 
	 * @since 1.2.1
	 */
	public static final String WGS_84_3D = GeoPackageProperties
			.buildProperty(SRS, "wgs84_3d");

	/**
	 * SRS name
	 */
	public static final String SRS_NAME = "srs_name";

	/**
	 * SRS id
	 */
	public static final String SRS_ID = "srs_id";

	/**
	 * SRS organization
	 */
	public static final String ORGANIZATION = "organization";

	/**
	 * SRS Organization Coordinate System ID
	 */
	public static final String ORGANIZATION_COORDSYS_ID = "organization_coordsys_id";

	/**
	 * SRS definition
	 */
	public static final String DEFINITION = "definition";

	/**
	 * SRS description
	 */
	public static final String DESCRIPTION = "description";

	/**
	 * SRS definition_12_063
	 *
	 * @since 1.2.1
	 */
	public static final String DEFINITION_12_063 = "definition_12_063";

	/**
	 * SQL script
	 */
	public static final String SQL = "sql";

	/**
	 * GeoPackage extensions
	 */
	public static final String EXTENSIONS = GeoPackageProperties
			.buildProperty(GEO_PACKAGE, "extensions");

	/**
	 * Custom data types
	 * 
	 * @since 4.0.0
	 */
	public static final String CONTENTS_DATA_TYPE = GeoPackageProperties
			.buildProperty(EXTENSIONS, "contents", "data_type");

	/**
	 * Feature generator
	 */
	public static final String FEATURE_GENERATOR = GeoPackageProperties
			.buildProperty(GEO_PACKAGE, "feature_generator");

	/**
	 * Feature generator download attempts
	 */
	public static final String FEATURE_GENERATOR_DOWNLOAD_ATTEMPTS = "downloadAttempts";

}
