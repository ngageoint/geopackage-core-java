package mil.nga.geopackage.extension;

import mil.nga.geopackage.GeoPackageConstants;
import mil.nga.geopackage.GeoPackageCore;
import mil.nga.geopackage.property.GeoPackageProperties;
import mil.nga.geopackage.property.PropertyConstants;
import mil.nga.geopackage.tiles.user.TileTable;

/**
 * Zoom Other Intervals extension
 * <p>
 * <a href=
 * "https://www.geopackage.org/spec/#extension_zoom_other_intervals">https://www.geopackage.org/spec/#extension_zoom_other_intervals</a>
 * 
 * @author osbornb
 * @since 1.1.8
 */
public class ZoomOtherExtension extends BaseExtension {

	/**
	 * Name
	 */
	public static final String NAME = "zoom_other";

	/**
	 * Extension name
	 */
	public static final String EXTENSION_NAME = GeoPackageConstants.EXTENSION_AUTHOR
			+ Extensions.EXTENSION_NAME_DIVIDER + NAME;

	/**
	 * Extension definition URL
	 */
	public static final String DEFINITION = GeoPackageProperties
			.getProperty(PropertyConstants.EXTENSIONS, NAME);

	/**
	 * Constructor
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * 
	 */
	public ZoomOtherExtension(GeoPackageCore geoPackage) {
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
