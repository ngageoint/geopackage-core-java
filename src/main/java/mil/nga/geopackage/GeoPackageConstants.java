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
	public static final String EXTENSION = "gpkg";

	/**
	 * Extension to GeoPackage extension files
	 * 
	 * @deprecated in GeoPackage version 1.2
	 */
	public static final String EXTENDED_EXTENSION = "gpkx";

	/**
	 * GeoPackage Media Type (MIME type)
	 * 
	 * @since 3.5.0
	 */
	public static final String MEDIA_TYPE = "application/geopackage+sqlite3";

	/**
	 * GeoPackage application id
	 */
	public static final String APPLICATION_ID = "GPKG";

	/**
	 * GeoPackage user version
	 * 
	 * @since 1.2.1
	 */
	public static final int USER_VERSION = 10300;

	/**
	 * Expected magic number
	 */
	public static final String GEOMETRY_MAGIC_NUMBER = "GP";

	/**
	 * Expected version 1 value
	 */
	public static final byte GEOMETRY_VERSION_1 = 0;

	/**
	 * SQLite header string prefix
	 */
	public static final String SQLITE_HEADER_PREFIX = "SQLite format 3";

	/**
	 * SQLite default application id
	 * 
	 * @since 4.0.0
	 */
	public static final String SQLITE_APPLICATION_ID = "SQLite";

	/**
	 * GeoPackage author
	 */
	public static final String EXTENSION_AUTHOR = EXTENSION;

	/**
	 * Geometry extension prefix
	 */
	public static final String GEOMETRY_EXTENSION_PREFIX = "geom";

}
