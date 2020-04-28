package mil.nga.geopackage.extension.tile_matrix_set;

import mil.nga.geopackage.db.GeoPackageCoreConnection;
import mil.nga.geopackage.db.GeoPackageTableCreator;

/**
 * Tile Matrix Set Extension Table Creator
 * 
 * @author jyutzler
 * @author osbornb
 * @since 3.5.1
 */
public class TileMatrixSetExtensionTableCreator extends GeoPackageTableCreator {

	/**
	 * ecere_tms_create script
	 */
	public static final String EXT_TMS_CREATE = getScript("ext_tms_create");

	/**
	 * gpkgext_tile_matrix_set script
	 */
	public static final String EXT_TMS = getScript("ext_tms");

	/**
	 * gpkgext_tile_matrix script
	 */
	public static final String EXT_TM = getScript("ext_tm");

	/**
	 * gpkgext_tile_matrix_set_tables script
	 */
	public static final String EXT_TM_TABLES = getScript("ext_tm_tables");

	/**
	 * gpkgext_tile_matrix_variable_widths script
	 */
	public static final String EXT_TM_VW = getScript("ext_tm_vw");

	/**
	 * Constructor
	 *
	 * @param db
	 *            db connection
	 */
	public TileMatrixSetExtensionTableCreator(GeoPackageCoreConnection db) {
		super(db);
	}

	/**
	 * Create the Extended Tile Matrix Set table
	 *
	 * @return executed statements
	 */
	public int createExtTileMatrixSet() {
		return execSQLScript(EXT_TMS);
	}

	/**
	 * Create the Extended Tile Matrix table
	 *
	 * @return executed statements
	 */
	public int createExtTileMatrix() {
		return execSQLScript(EXT_TM);
	}

	/**
	 * Create the Extended Tile Matrix Tables table
	 *
	 * @return executed statements
	 */
	public int createExtTileMatrixTables() {
		return execSQLScript(EXT_TM_TABLES);
	}

	/**
	 * Create the Extended Tile Matrix Set table
	 *
	 * @return executed statements
	 */
	public int createExtTileMatrixVariableWidths() {
		return execSQLScript(EXT_TM_VW);
	}

}
