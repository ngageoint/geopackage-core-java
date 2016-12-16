package mil.nga.geopackage;

/**
 * GeoPackage constants
 * 
 * @author osbornb
 */
public class GeoPackageConstants {

	/**
	 * Extension to GeoPackage files
	 */
	public static final String GEOPACKAGE_EXTENSION = "gpkg";

	/**
	 * Extension to GeoPackage extension files
	 * 
	 * @deprecated in GeoPackage version 1.2
	 */
	public static final String GEOPACKAGE_EXTENDED_EXTENSION = "gpkx";

	/**
	 * GeoPackage application id
	 */
	public static final String APPLICATION_ID = "GPKG";

	/**
	 * GeoPackage user version
	 * 
	 * @since 1.2.1
	 */
	public static final int USER_VERSION = 10200;

	/**
	 * Expected magic number
	 */
	public static final String GEO_PACKAGE_GEOMETRY_MAGIC_NUMBER = "GP";

	/**
	 * Expected version 1 value
	 */
	public static final byte GEO_PACKAGE_GEOMETRY_VERSION_1 = 0;

	/**
	 * SQLite header string prefix
	 */
	public static final String SQLITE_HEADER_PREFIX = "SQLite format 3";

	/**
	 * GeoPackage author
	 */
	public static final String GEO_PACKAGE_EXTENSION_AUTHOR = GEOPACKAGE_EXTENSION;

	/**
	 * Geometry extension prefix
	 */
	public static final String GEOMETRY_EXTENSION_PREFIX = "geom";

}
