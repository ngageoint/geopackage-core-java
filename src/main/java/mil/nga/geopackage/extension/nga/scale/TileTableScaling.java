package mil.nga.geopackage.extension.nga.scale;

import java.sql.SQLException;

import com.j256.ormlite.dao.Dao.CreateOrUpdateStatus;

import mil.nga.geopackage.GeoPackageCore;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.db.GeoPackageCoreConnection;
import mil.nga.geopackage.extension.BaseExtension;
import mil.nga.geopackage.extension.ExtensionScopeType;
import mil.nga.geopackage.extension.Extensions;
import mil.nga.geopackage.extension.nga.NGAExtensions;
import mil.nga.geopackage.property.GeoPackageProperties;
import mil.nga.geopackage.property.PropertyConstants;
import mil.nga.geopackage.tiles.matrix.TileMatrix;
import mil.nga.geopackage.tiles.matrixset.TileMatrixSet;
import mil.nga.geopackage.user.UserCoreDao;

/**
 * Abstract Tile Table Scaling, for scaling tiles from nearby zoom levels for
 * missing tiles
 * 
 * http://ngageoint.github.io/GeoPackage/docs/extensions/tile-scaling.html
 * 
 * @author osbornb
 * @since 2.0.2
 */
public class TileTableScaling extends BaseExtension {

	/**
	 * Extension author
	 */
	public static final String EXTENSION_AUTHOR = NGAExtensions.EXTENSION_AUTHOR;

	/**
	 * Extension name without the author
	 */
	public static final String EXTENSION_NAME_NO_AUTHOR = "tile_scaling";

	/**
	 * Extension, with author and name
	 */
	public static final String EXTENSION_NAME = Extensions
			.buildExtensionName(EXTENSION_AUTHOR, EXTENSION_NAME_NO_AUTHOR);

	/**
	 * Extension definition URL
	 */
	public static final String EXTENSION_DEFINITION = GeoPackageProperties
			.getProperty(PropertyConstants.EXTENSIONS,
					EXTENSION_NAME_NO_AUTHOR);

	/**
	 * Table name
	 */
	private final String tableName;

	/**
	 * Tile Scaling DAO
	 */
	private final TileScalingDao tileScalingDao;

	/**
	 * Constructor
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * @param tileMatrixSet
	 *            tile matrix set
	 */
	public TileTableScaling(GeoPackageCore geoPackage,
			TileMatrixSet tileMatrixSet) {
		this(geoPackage, tileMatrixSet.getTableName());
	}

	/**
	 * Constructor
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * @param tileMatrix
	 *            tile matrix
	 */
	public TileTableScaling(GeoPackageCore geoPackage, TileMatrix tileMatrix) {
		this(geoPackage, tileMatrix.getTableName());
	}

	/**
	 * Constructor
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * @param tileDao
	 *            tile dao
	 */
	public TileTableScaling(GeoPackageCore geoPackage,
			UserCoreDao<?, ?, ?, ?> tileDao) {
		this(geoPackage, tileDao.getTableName());
	}

	/**
	 * Constructor
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * @param tableName
	 *            tile table name
	 */
	public TileTableScaling(GeoPackageCore geoPackage, String tableName) {
		super(geoPackage);
		this.tableName = tableName;
		tileScalingDao = getTileScalingDao();
	}

	/**
	 * Get the GeoPackage
	 * 
	 * @return GeoPackage
	 */
	public GeoPackageCore getGeoPackage() {
		return geoPackage;
	}

	/**
	 * Get the table name
	 * 
	 * @return table name
	 */
	public String getTableName() {
		return tableName;
	}

	/**
	 * Get the tile scaling DAO
	 * 
	 * @return dao
	 */
	public TileScalingDao getDao() {
		return tileScalingDao;
	}

	/**
	 * Determine if the GeoPackage has the extension
	 * 
	 * @return true if has extension
	 */
	public boolean has() {

		boolean exists = false;
		try {
			exists = has(EXTENSION_NAME, tableName, null)
					&& tileScalingDao.isTableExists()
					&& tileScalingDao.idExists(tableName);
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to check for tile scaling for GeoPackage: "
							+ geoPackage.getName() + ", Tile Table: "
							+ tableName,
					e);
		}

		return exists;
	}

	/**
	 * Get the tile scaling
	 * 
	 * @return tile scaling
	 */
	public TileScaling get() {
		TileScaling tileScaling = null;
		if (has()) {
			try {
				tileScaling = tileScalingDao.queryForId(tableName);
			} catch (SQLException e) {
				throw new GeoPackageException(
						"Failed to query for tile scaling for GeoPackage: "
								+ geoPackage.getName() + ", Tile Table: "
								+ tableName,
						e);
			}
		}
		return tileScaling;
	}

	/**
	 * Create the tile scaling (same as calling
	 * {@link #createOrUpdate(TileScaling)})
	 * 
	 * @param tileScaling
	 *            tile scaling
	 * @return true upon success
	 */
	public boolean create(TileScaling tileScaling) {
		return createOrUpdate(tileScaling);
	}

	/**
	 * Update the tile scaling (same as calling
	 * {@link #createOrUpdate(TileScaling)})
	 * 
	 * @param tileScaling
	 *            tile scaling
	 * @return true upon success
	 */
	public boolean update(TileScaling tileScaling) {
		return createOrUpdate(tileScaling);
	}

	/**
	 * Create or update the tile scaling
	 * 
	 * @param tileScaling
	 *            tile scaling
	 * @return true upon success
	 */
	public boolean createOrUpdate(TileScaling tileScaling) {

		boolean success = false;
		tileScaling.setTableName(tableName);

		getOrCreateExtension();
		try {
			if (!tileScalingDao.isTableExists()) {
				createTileScalingTable();
			}

			CreateOrUpdateStatus status = tileScalingDao
					.createOrUpdate(tileScaling);
			success = status.isCreated() || status.isUpdated();

		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to create or update tile scaling for GeoPackage: "
							+ geoPackage.getName() + ", Tile Table: "
							+ tableName,
					e);
		}

		return success;
	}

	/**
	 * Delete the tile table scaling for the tile table
	 * 
	 * @return true if deleted
	 */
	public boolean delete() {

		boolean deleted = false;

		try {
			if (tileScalingDao.isTableExists()) {
				deleted = tileScalingDao.deleteById(tableName) > 0;
			}
			if (extensionsDao.isTableExists()) {
				deleted = extensionsDao.deleteByExtension(EXTENSION_NAME,
						tableName) > 0 || deleted;
			}
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to delete tile table scaling for GeoPackage: "
							+ geoPackage.getName() + ", Table: " + tableName,
					e);
		}

		return deleted;
	}

	/**
	 * Get or create if needed the extension
	 * 
	 * @return extensions object
	 */
	private Extensions getOrCreateExtension() {

		Extensions extension = getOrCreate(EXTENSION_NAME, tableName, null,
				EXTENSION_DEFINITION, ExtensionScopeType.READ_WRITE);

		return extension;
	}

	/**
	 * Get the extension
	 * 
	 * @return extensions object or null if one does not exist
	 */
	public Extensions getExtension() {

		Extensions extension = get(EXTENSION_NAME, tableName, null);

		return extension;
	}

	/**
	 * Get a Tile Scaling DAO
	 * 
	 * @return tile scaling dao
	 * @since 4.0.0
	 */
	public TileScalingDao getTileScalingDao() {
		return getTileScalingDao(geoPackage);
	}

	/**
	 * Get a Tile Scaling DAO
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * @return tile scaling dao
	 * @since 4.0.0
	 */
	public static TileScalingDao getTileScalingDao(GeoPackageCore geoPackage) {
		return TileScalingDao.create(geoPackage);
	}

	/**
	 * Get a Tile Scaling DAO
	 * 
	 * @param db
	 *            database connection
	 * @return tile scaling dao
	 * @since 4.0.0
	 */
	public static TileScalingDao getTileScalingDao(
			GeoPackageCoreConnection db) {
		return TileScalingDao.create(db);
	}

	/**
	 * Create the Tile Scaling Table if it does not exist
	 * 
	 * @return true if created
	 * @since 4.0.0
	 */
	public boolean createTileScalingTable() {
		verifyWritable();

		boolean created = false;

		try {
			if (!tileScalingDao.isTableExists()) {
				TileScalingTableCreator tableCreator = new TileScalingTableCreator(
						geoPackage);
				created = tableCreator.createTileScaling() > 0;
			}
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to check if " + TileScaling.class.getSimpleName()
							+ " table exists and create it",
					e);
		}

		return created;
	}

}
