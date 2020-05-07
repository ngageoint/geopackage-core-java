package mil.nga.geopackage.extension.im.vector_tiles;

import mil.nga.geopackage.GeoPackageCore;
import mil.nga.geopackage.extension.BaseExtension;
import mil.nga.geopackage.extension.ExtensionScopeType;
import mil.nga.geopackage.extension.Extensions;
import mil.nga.geopackage.tiles.user.TileTable;

/**
 * The Vector Tiles extension requires an additional encoding extension for a
 * particular vector tileset
 * 
 * @author jyutzler
 * @since 4.0.0
 */
public abstract class VectorTilesEncodingExtension extends BaseExtension {
	/**
	 * Constructor
	 *
	 * @param geoPackage
	 *            GeoPackage
	 */
	protected VectorTilesEncodingExtension(GeoPackageCore geoPackage) {
		super(geoPackage);
	}

	/**
	 * Get or create the extension
	 *
	 * @param tilesTable
	 *            tiles table name
	 * @return Extensions
	 */
	public Extensions getOrCreate(String tilesTable) {

		return getOrCreate(getName(), tilesTable, TileTable.COLUMN_TILE_DATA,
				getDefinition(), ExtensionScopeType.READ_WRITE);
	}

	public boolean has() {
		return this.has(getName());
	}

	/**
	 *
	 * @return the extension name - this goes in gpkg_extensions.extension_name
	 */
	abstract public String getName();

	/**
	 *
	 * @return the extension definition - this goes in
	 *         gpkg_extensions.definition
	 */
	abstract public String getDefinition();

	/**
	 *
	 * @return The format type - this is used when requesting from a tiles
	 *         server
	 */
	abstract public String getType();
}
