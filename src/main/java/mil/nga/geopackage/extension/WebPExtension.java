package mil.nga.geopackage.extension;

import mil.nga.geopackage.GeoPackageConstants;
import mil.nga.geopackage.GeoPackageCore;
import mil.nga.geopackage.property.GeoPackageProperties;
import mil.nga.geopackage.property.PropertyConstants;
import mil.nga.geopackage.tiles.user.TileTable;

/**
 * WebP extension
 * 
 * https://www.geopackage.org/spec/#extension_tiles_webp
 * 
 * @author osbornb
 * @since 1.1.8
 */
public class WebPExtension extends BaseExtension {

	/**
	 * Name
	 */
	public static final String NAME = "webp";

	/**
	 * Extension name
	 */
	public static final String EXTENSION_NAME = GeoPackageConstants.EXTENSION_AUTHOR
			+ Extensions.EXTENSION_NAME_DIVIDER + NAME;

	/**
	 * Extension definition URL
	 */
	public static final String DEFINITION = GeoPackageProperties.getProperty(
			PropertyConstants.EXTENSIONS, NAME);

	/**
	 * Constructor
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * 
	 */
	public WebPExtension(GeoPackageCore geoPackage) {
		super(geoPackage);
	}

	/**
	 * Get or create the extension
	 * 
	 * @param tableName
	 *            table name
	 * @return extension
	 */
	public Extensions getOrCreate(String tableName) {

		Extensions extension = getOrCreate(EXTENSION_NAME, tableName,
				TileTable.COLUMN_TILE_DATA, DEFINITION,
				ExtensionScopeType.READ_WRITE);

		return extension;
	}

	/**
	 * Determine if the GeoPackage has the extension
	 * 
	 * @param tableName
	 *            table name
	 * @return true if has extension
	 */
	public boolean has(String tableName) {

		boolean exists = has(EXTENSION_NAME, tableName,
				TileTable.COLUMN_TILE_DATA);

		return exists;
	}

}
