package mil.nga.geopackage.extension.ecere.tile_matrix_set;

import mil.nga.geopackage.GeoPackageCore;
import mil.nga.geopackage.db.GeoPackageCoreConnection;
import mil.nga.geopackage.db.GeoPackageTableCreator;

/**
 * Tile Matrix Set Extension Table Creator
 * 
 * @author jyutzler
 * @author osbornb
 * @since 4.0.0
 */
public class TileMatrixSetTableCreator extends GeoPackageTableCreator {

	/**
	 * ecere_tms_create property
	 */
	public static final String TMS_CREATE = "tms_create";

	/**
	 * gpkgext_tile_matrix_set property
	 */
	public static final String TMS = "tms";

	/**
	 * gpkgext_tile_matrix property
	 */
	public static final String TM = "tm";

	/**
	 * gpkgext_tile_matrix_set_tables property
	 */
	public static final String TM_TABLES = "tm_tables";

	/**
	 * gpkgext_tile_matrix_variable_widths property
	 */
	public static final String TM_VW = "tm_vw";

	/**
	 * Constructor
	 *
	 * @param db
	 *            db connection
	 */
	public TileMatrixSetTableCreator(GeoPackageCoreConnection db) {
		super(db);
	}

	/**
	 * Constructor
	 *
	 * @param geoPackage
	 *            GeoPackage
	 */
	public TileMatrixSetTableCreator(GeoPackageCore geoPackage) {
		super(geoPackage);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getAuthor() {
		return TileMatrixSetExtension.EXTENSION_AUTHOR;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return TileMatrixSetExtension.EXTENSION_NAME_NO_AUTHOR;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int createTileMatrixSet() {
		return execScript(TMS);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int createTileMatrix() {
		return execScript(TM);
	}

	/**
	 * Create the Tile Matrix Tables table
	 *
	 * @return executed statements
	 */
	public int createTileMatrixTables() {
		return execScript(TM_TABLES);
	}

	/**
	 * Create the Tile Matrix Variable Widths table
	 *
	 * @return executed statements
	 */
	public int createTileMatrixVariableWidths() {
		return execScript(TM_VW);
	}

	/**
	 * Execute the creation script
	 *
	 * @return executed statements
	 */
	public int createScript() {
		return execScript(TMS_CREATE);
	}

}
