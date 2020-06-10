package mil.nga.geopackage.extension.ecere.tile_matrix_set;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import mil.nga.geopackage.GeoPackageCore;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.db.GeoPackageCoreConnection;
import mil.nga.geopackage.extension.BaseExtension;
import mil.nga.geopackage.extension.ExtensionScopeType;
import mil.nga.geopackage.extension.Extensions;
import mil.nga.geopackage.extension.ecere.EcereExtensions;
import mil.nga.geopackage.property.GeoPackageProperties;
import mil.nga.geopackage.property.PropertyConstants;
import mil.nga.geopackage.tiles.matrix.TileMatrix;
import mil.nga.geopackage.tiles.matrixset.TileMatrixSet;

/**
 * Tile Matrix Set Extension
 * 
 * https://gitlab.com/imagemattersllc/ogc-vtp2/-/blob/master/extensions/14-tile-matrix-set.adoc
 * 
 * @author jyutzler
 * @since 4.0.0
 */
public class TileMatrixSetExtension extends BaseExtension {
	public static final String EXTENSION_AUTHOR = EcereExtensions.EXTENSION_AUTHOR;
	public static final String EXTENSION_NAME_NO_AUTHOR = "tms";
	public static final String EXTENSION_NAME = Extensions
			.buildExtensionName(EXTENSION_AUTHOR, EXTENSION_NAME_NO_AUTHOR);
	public static final String EXTENSION_DEFINITION = GeoPackageProperties
			.getProperty(PropertyConstants.EXTENSIONS,
					EXTENSION_NAME_NO_AUTHOR);

	private final ExtTileMatrixDao tileMatrixDao;
	private final ExtTileMatrixSetDao tileMatrixSetDao;
	private final TileMatrixTablesDao tileMatrixTablesDao;
	private final TileMatrixVariableWidthsDao tileMatrixVariableWidthsDao;

	/**
	 * Constructor
	 *
	 * @param geoPackage
	 *            GeoPackage
	 */
	public TileMatrixSetExtension(GeoPackageCore geoPackage) {

		super(geoPackage);
		tileMatrixDao = getTileMatrixDao();
		tileMatrixSetDao = getTileMatrixSetDao();
		tileMatrixTablesDao = getTileMatrixTablesDao();
		tileMatrixVariableWidthsDao = getTileMatrixVariableWidthsDao();
	}

	/**
	 * @return the extension name
	 */
	public static String getName() {
		return EXTENSION_NAME;
	}

	public boolean has() {
		return this.has(EXTENSION_NAME, TileMatrixTable.TABLE_NAME, null)
				&& geoPackage.isTable(TileMatrixTable.TABLE_NAME);

	}

	/**
	 * Get or create the extension
	 *
	 * @return extensions
	 */
	public List<Extensions> getOrCreate() {

		createTileMatrixSetTables();

		List<Extensions> extensions = new ArrayList<>();

		extensions.add(getOrCreate(EXTENSION_NAME, TileMatrixTable.TABLE_NAME,
				null, EXTENSION_DEFINITION, ExtensionScopeType.READ_WRITE));
		extensions.add(getOrCreate(EXTENSION_NAME,
				TileMatrixVariableWidths.TABLE_NAME, null, EXTENSION_DEFINITION,
				ExtensionScopeType.READ_WRITE));
		extensions.add(getOrCreate(EXTENSION_NAME, ExtTileMatrix.TABLE_NAME,
				null, EXTENSION_DEFINITION, ExtensionScopeType.READ_WRITE));
		extensions.add(getOrCreate(EXTENSION_NAME, ExtTileMatrixSet.TABLE_NAME,
				null, EXTENSION_DEFINITION, ExtensionScopeType.READ_WRITE));
		extensions.add(getOrCreate(EXTENSION_NAME, TileMatrixSet.TABLE_NAME,
				null, EXTENSION_DEFINITION, ExtensionScopeType.WRITE_ONLY));
		extensions.add(getOrCreate(EXTENSION_NAME, TileMatrix.TABLE_NAME, null,
				EXTENSION_DEFINITION, ExtensionScopeType.WRITE_ONLY));

		return extensions;
	}

	public void removeExtension() {
		try {
			// This is tricky because we have to drop the views and recreate the
			// tables
			if (has()) {
				this.geoPackage.dropView(TileMatrix.TABLE_NAME);
				this.geoPackage.dropView(TileMatrixSet.TABLE_NAME);
				this.geoPackage.createTileMatrixTable();
				this.geoPackage.createTileMatrixSetTable();
				try {
					// TODO: Run an SQL script that loads these tables back up
					// from what was originally in the views
				} catch (Exception exc) {
					// No op: if for some reason this doesn't work, we'll give
					// up and just move on
				}
			}
			if (this.tileMatrixVariableWidthsDao.isTableExists()) {
				this.geoPackage.dropTable(TileMatrixVariableWidths.TABLE_NAME);
			}
			if (this.tileMatrixTablesDao.isTableExists()) {
				this.geoPackage.dropTable(TileMatrixTable.TABLE_NAME);
			}
			if (this.tileMatrixDao.isTableExists()) {
				this.geoPackage.dropTable(ExtTileMatrix.TABLE_NAME);
			}
			if (this.tileMatrixSetDao.isTableExists()) {
				this.geoPackage.dropTable(ExtTileMatrixSet.TABLE_NAME);
			}

			if (this.extensionsDao.isTableExists()) {
				this.extensionsDao.deleteByExtension(EXTENSION_NAME);
			}
		} catch (SQLException exc) {
			throw new GeoPackageException(
					"Failed to delete Tile Matrix Set extension and/or tables. GeoPackage: "
							+ this.geoPackage.getName(),
					exc);
		}
	}

	/**
	 * Get the Tile Matrix DAO
	 * 
	 * @return tile matrix dao
	 */
	public ExtTileMatrixDao getTileMatrixDao() {
		return getTileMatrixDao(geoPackage);
	}

	/**
	 * Get the Tile Matrix DAO
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * @return tile matrix dao
	 */
	public static ExtTileMatrixDao getTileMatrixDao(GeoPackageCore geoPackage) {
		return ExtTileMatrixDao.create(geoPackage);
	}

	/**
	 * Get the Tile Matrix DAO
	 * 
	 * @param db
	 *            database connection
	 * @return tile matrix dao
	 */
	public static ExtTileMatrixDao getTileMatrixDao(
			GeoPackageCoreConnection db) {
		return ExtTileMatrixDao.create(db);
	}

	/**
	 * Get the Tile Matrix Set DAO
	 * 
	 * @return tile matrix set dao
	 */
	public ExtTileMatrixSetDao getTileMatrixSetDao() {
		return getTileMatrixSetDao(geoPackage);
	}

	/**
	 * Get the Tile Matrix Set DAO
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * @return tile matrix set dao
	 */
	public static ExtTileMatrixSetDao getTileMatrixSetDao(
			GeoPackageCore geoPackage) {
		return ExtTileMatrixSetDao.create(geoPackage);
	}

	/**
	 * Get the Tile Matrix Set DAO
	 * 
	 * @param db
	 *            database connection
	 * @return tile matrix set dao
	 */
	public static ExtTileMatrixSetDao getTileMatrixSetDao(
			GeoPackageCoreConnection db) {
		return ExtTileMatrixSetDao.create(db);
	}

	/**
	 * Get the Tile Matrix Tables DAO
	 * 
	 * @return tile matrix tables dao
	 */
	public TileMatrixTablesDao getTileMatrixTablesDao() {
		return getTileMatrixTablesDao(geoPackage);
	}

	/**
	 * Get the Tile Matrix Tables DAO
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * @return tile matrix tables dao
	 */
	public static TileMatrixTablesDao getTileMatrixTablesDao(
			GeoPackageCore geoPackage) {
		return TileMatrixTablesDao.create(geoPackage);
	}

	/**
	 * Get the Tile Matrix Tables DAO
	 * 
	 * @param db
	 *            database connection
	 * @return tile matrix tables dao
	 */
	public static TileMatrixTablesDao getTileMatrixTablesDao(
			GeoPackageCoreConnection db) {
		return TileMatrixTablesDao.create(db);
	}

	/**
	 * Get the Tile Matrix Variable Widths DAO
	 * 
	 * @return tile matrix variable widths dao
	 */
	public TileMatrixVariableWidthsDao getTileMatrixVariableWidthsDao() {
		return getTileMatrixVariableWidthsDao(geoPackage);
	}

	/**
	 * Get the Tile Matrix Variable Widths DAO
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * @return tile matrix variable widths dao
	 */
	public static TileMatrixVariableWidthsDao getTileMatrixVariableWidthsDao(
			GeoPackageCore geoPackage) {
		return TileMatrixVariableWidthsDao.create(geoPackage);
	}

	/**
	 * Get the Tile Matrix Variable Widths DAO
	 * 
	 * @param db
	 *            database connection
	 * @return tile matrix variable widths dao
	 */
	public static TileMatrixVariableWidthsDao getTileMatrixVariableWidthsDao(
			GeoPackageCoreConnection db) {
		return TileMatrixVariableWidthsDao.create(db);
	}

	/**
	 * Create the Tile Matrix Set Extension
	 * 
	 * @return true if created
	 */
	public boolean createTileMatrixSetTables() {
		verifyWritable();

		boolean created = false;

		TileMatrixSetTableCreator tableCreator = new TileMatrixSetTableCreator(
				geoPackage);

		try {
			if (!tileMatrixSetDao.isTableExists()) {
				created = tableCreator.createTileMatrixSet() > 0;
			}
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to check if " + ExtTileMatrixSet.TABLE_NAME
							+ " table exists and create it",
					e);
		}

		try {
			if (!tileMatrixDao.isTableExists()) {
				created = (tableCreator.createTileMatrix() > 0) || created;
			}
		} catch (SQLException e) {
			throw new GeoPackageException("Failed to check if "
					+ ExtTileMatrix.TABLE_NAME + " table exists and create it",
					e);
		}

		try {
			if (!tileMatrixTablesDao.isTableExists()) {
				created = (tableCreator.createTileMatrixTables() > 0)
						|| created;
			}
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to check if " + TileMatrixTable.TABLE_NAME
							+ " table exists and create it",
					e);
		}

		try {
			if (!tileMatrixVariableWidthsDao.isTableExists()) {
				created = (tableCreator.createTileMatrixVariableWidths() > 0)
						|| created;
			}
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to check if " + TileMatrixVariableWidths.TABLE_NAME
							+ " table exists and create it",
					e);
		}

		tableCreator.createScript();

		return created;
	}

}
