package mil.nga.geopackage.extension;

import mil.nga.geopackage.GeoPackageConstants;
import mil.nga.geopackage.GeoPackageCore;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.property.GeoPackageProperties;
import mil.nga.geopackage.property.PropertyConstants;
import mil.nga.wkb.geom.GeometryType;

/**
 * Geometry Extensions utility methods and constants
 * 
 * @author osbornb
 */
public class GeometryExtensions extends BaseExtension {

	/**
	 * Geometry Types Extension definition URL
	 * 
	 * @since 1.1.8
	 */
	public static final String GEOMETRY_TYPES_EXTENSION_DEFINITION = GeoPackageProperties
			.getProperty(PropertyConstants.EXTENSIONS, "geometry_types");

	/**
	 * User Geometry Types Extension definition URL
	 * 
	 * @since 1.1.8
	 * @deprecated as of 1.2.1, On August 15, 2016 the GeoPackage SWG voted to
	 *             remove this extension from the standard due to
	 *             interoperability concerns. (GeoPackage version 1.2)
	 */
	public static final String USER_GEOMETRY_TYPES_EXTENSION_DEFINITION = GeoPackageProperties
			.getProperty(PropertyConstants.EXTENSIONS, "user_geometry_types");

	/**
	 * Constructor
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * @since 1.1.8
	 */
	public GeometryExtensions(GeoPackageCore geoPackage) {
		super(geoPackage);
	}

	/**
	 * Get or create the extension, non-linear geometry type
	 * 
	 * @param tableName
	 *            table name
	 * @param columnName
	 *            column name
	 * @param geometryType
	 *            geometry type
	 * @return extension
	 * @since 1.1.8
	 */
	public Extensions getOrCreate(String tableName, String columnName,
			GeometryType geometryType) {

		String extensionName = getExtensionName(geometryType);
		Extensions extension = getOrCreate(extensionName, tableName,
				columnName, GEOMETRY_TYPES_EXTENSION_DEFINITION,
				ExtensionScopeType.READ_WRITE);

		return extension;
	}

	/**
	 * Determine if the GeoPackage has the extension, non-linear geometry type
	 * 
	 * @param tableName
	 *            table name
	 * @param columnName
	 *            column name
	 * @param geometryType
	 *            geometry type
	 * @return true if has extension
	 * @since 1.1.8
	 */
	public boolean has(String tableName, String columnName,
			GeometryType geometryType) {

		String extensionName = getExtensionName(geometryType);
		boolean exists = has(extensionName, tableName, columnName);

		return exists;
	}

	/**
	 * Determine if the geometry type is an extension
	 * 
	 * @param geometryType
	 *            geometry type
	 * @return true if extension
	 */
	public static boolean isExtension(GeometryType geometryType) {
		return geometryType.getCode() > GeometryType.GEOMETRYCOLLECTION
				.getCode();
	}

	/**
	 * Determine if the geometry type is non standard
	 * 
	 * @param geometryType
	 *            geometry type
	 * @return true if non standard
	 * @since 2.0.1
	 */
	public static boolean isNonStandard(GeometryType geometryType) {
		return geometryType.getCode() > GeometryType.SURFACE.getCode();
	}

	/**
	 * Determine if the geometry type is a GeoPackage extension
	 * 
	 * @param geometryType
	 *            geometry type
	 * @return true if a GeoPackage extension, false if user-defined
	 */
	public static boolean isGeoPackageExtension(GeometryType geometryType) {
		return geometryType.getCode() >= GeometryType.CIRCULARSTRING.getCode()
				&& geometryType.getCode() <= GeometryType.SURFACE.getCode();
	}

	/**
	 * Get the extension name of a GeoPackage extension Geometry
	 * 
	 * @param geometryType
	 *            geometry type
	 * @return extension name
	 */
	public static String getExtensionName(GeometryType geometryType) {

		if (!isExtension(geometryType)) {
			throw new GeoPackageException(GeometryType.class.getSimpleName()
					+ " is not an extension: " + geometryType.getName());
		}

		if (!isGeoPackageExtension(geometryType)) {
			throw new GeoPackageException(
					GeometryType.class.getSimpleName()
							+ " is not a GeoPackage extension, User-Defined requires an author: "
							+ geometryType.getName());
		}

		String extensionName = GeoPackageConstants.GEO_PACKAGE_EXTENSION_AUTHOR
				+ Extensions.EXTENSION_NAME_DIVIDER
				+ GeoPackageConstants.GEOMETRY_EXTENSION_PREFIX
				+ Extensions.EXTENSION_NAME_DIVIDER + geometryType.getName();

		return extensionName;
	}

	/**
	 * Get or create the extension, user defined geometry type
	 * 
	 * @param tableName
	 *            table name
	 * @param columnName
	 *            column name
	 * @param author
	 *            user defined author
	 * @param geometryType
	 *            geometry type
	 * @return extension
	 * @since 1.1.8
	 * @deprecated as of 1.2.1, On August 15, 2016 the GeoPackage SWG voted to
	 *             remove this extension from the standard due to
	 *             interoperability concerns. (GeoPackage version 1.2)
	 */
	public Extensions getOrCreate(String tableName, String columnName,
			String author, GeometryType geometryType) {

		String extensionName = getExtensionName(author, geometryType);
		String description = isGeoPackageExtension(geometryType) ? GEOMETRY_TYPES_EXTENSION_DEFINITION
				: USER_GEOMETRY_TYPES_EXTENSION_DEFINITION;
		Extensions extension = getOrCreate(extensionName, tableName,
				columnName, description, ExtensionScopeType.READ_WRITE);

		return extension;
	}

	/**
	 * Determine if the GeoPackage has the extension, user defined geometry type
	 * 
	 * @param tableName
	 *            table name
	 * @param columnName
	 *            column name
	 * @param author
	 *            user defined author
	 * @param geometryType
	 *            geometry type
	 * @return true if has extension
	 * @since 1.1.8
	 * @deprecated as of 1.2.1, On August 15, 2016 the GeoPackage SWG voted to
	 *             remove this extension from the standard due to
	 *             interoperability concerns. (GeoPackage version 1.2)
	 */
	public boolean has(String tableName, String columnName, String author,
			GeometryType geometryType) {

		String extensionName = getExtensionName(author, geometryType);
		boolean exists = has(extensionName, tableName, columnName);

		return exists;
	}

	/**
	 * Get the extension name of a extension Geometry, either user-defined or
	 * GeoPackage extension
	 * 
	 * @param author
	 *            author
	 * @param geometryType
	 *            geometry type
	 * @return extension name
	 * @deprecated as of 1.2.1, On August 15, 2016 the GeoPackage SWG voted to
	 *             remove this extension from the standard due to
	 *             interoperability concerns. (GeoPackage version 1.2)
	 */
	public static String getExtensionName(String author,
			GeometryType geometryType) {

		if (!isExtension(geometryType)) {
			throw new GeoPackageException(GeometryType.class.getSimpleName()
					+ " is not an extension: " + geometryType.getName());
		}

		String extensionName = (isGeoPackageExtension(geometryType) ? GeoPackageConstants.GEO_PACKAGE_EXTENSION_AUTHOR
				: author)
				+ Extensions.EXTENSION_NAME_DIVIDER
				+ GeoPackageConstants.GEOMETRY_EXTENSION_PREFIX
				+ Extensions.EXTENSION_NAME_DIVIDER + geometryType.getName();

		return extensionName;
	}

}
