package mil.nga.geopackage.extension.im.vector_tiles;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import mil.nga.geopackage.GeoPackageCore;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.db.GeoPackageCoreConnection;
import mil.nga.geopackage.extension.BaseExtension;
import mil.nga.geopackage.extension.ExtensionScopeType;
import mil.nga.geopackage.extension.Extensions;
import mil.nga.geopackage.extension.im.ImageMattersExtensions;
import mil.nga.geopackage.property.GeoPackageProperties;
import mil.nga.geopackage.property.PropertyConstants;
import mil.nga.geopackage.tiles.user.TileTable;

/**
 * Vector Tiles Extension
 * 
 * https://gitlab.com/imagemattersllc/ogc-vtp2/-/blob/master/extensions/1-vte.adoc
 * 
 * @author jyutzler
 * @since 4.0.0
 */
public class VectorTilesExtension extends BaseExtension {
	public static final String EXTENSION_AUTHOR = ImageMattersExtensions.EXTENSION_AUTHOR;
	public static final String EXTENSION_NAME_NO_AUTHOR = "vector_tiles";
	public static final String EXTENSION_NAME = Extensions
			.buildExtensionName(EXTENSION_AUTHOR, EXTENSION_NAME_NO_AUTHOR);
	public static final String EXTENSION_DEFINITION = GeoPackageProperties
			.getProperty(PropertyConstants.EXTENSIONS,
					EXTENSION_NAME_NO_AUTHOR);

	/**
	 * Contents Data Type
	 */
	public static final String VECTOR_TILES = "vector-tiles";

	private final VectorTilesLayersDao vectorTilesLayersDao;
	private final VectorTilesFieldsDao vectorTilesFieldsDao;

	/**
	 * Constructor
	 *
	 * @param geoPackage
	 *            GeoPackage
	 */
	public VectorTilesExtension(GeoPackageCore geoPackage) {

		super(geoPackage);
		vectorTilesLayersDao = getVectorTilesLayersDao();
		vectorTilesFieldsDao = getVectorTilesFieldsDao();
	}

	/**
	 *
	 * @return the extension name
	 */
	public static String getName() {
		return EXTENSION_NAME;
	}

	public boolean has() {
		return this.has(EXTENSION_NAME, VectorTilesLayers.TABLE_NAME, null)
				&& geoPackage.isTable(VectorTilesLayers.TABLE_NAME);

	}

	/**
	 * @param tableName
	 *            user tiles table name
	 * @param vtee
	 *            vector tiles encoding extension
	 * @return true if table was created, false if the table already existed
	 */
	public boolean createUserVectorTilesTable(String tableName,
			VectorTilesEncodingExtension vtee) {

		boolean created = false;

		if (!geoPackage.isTable(tableName)) {

			geoPackage.createTileTable(new TileTable(tableName));

			created = true;
		}

		getOrCreate(tableName, vtee);

		return created;
	}

	/**
	 * Get or create the extension
	 *
	 * @return extensions
	 */
	public List<Extensions> getOrCreate() {

		createVectorTilesTables();

		List<Extensions> extensions = new ArrayList<>();

		extensions.add(getOrCreate(EXTENSION_NAME, VectorTilesLayers.TABLE_NAME,
				null, EXTENSION_DEFINITION, ExtensionScopeType.READ_WRITE));
		extensions.add(getOrCreate(EXTENSION_NAME, VectorTilesFields.TABLE_NAME,
				null, EXTENSION_DEFINITION, ExtensionScopeType.READ_WRITE));

		return extensions;
	}

	/**
	 * Get or create the extension
	 *
	 * @param tilesTable
	 *            tiles table name
	 * @param vtee
	 *            vector tiles encoding extension
	 * @return extensions
	 */
	public List<Extensions> getOrCreate(String tilesTable,
			VectorTilesEncodingExtension vtee) {

		List<Extensions> extensions = getOrCreate();

		extensions.add(vtee.getOrCreate(tilesTable));

		return extensions;
	}

	public void removeExtension() {
		try {
			if (this.vectorTilesFieldsDao.isTableExists()) {
				this.geoPackage.dropTable(VectorTilesFields.TABLE_NAME);
			}
			if (this.vectorTilesLayersDao.isTableExists()) {
				List<VectorTilesLayers> vectorTilesLayers = this.vectorTilesLayersDao
						.queryForAll();
				for (VectorTilesLayers vectorTilesLayer : vectorTilesLayers) {
					this.geoPackage
							.deleteTable(vectorTilesLayer.getTableName());
					// This will pick up both the vector tiles and the encoding
					this.extensionsDao
							.deleteByTableName(vectorTilesLayer.getTableName());
				}

				this.geoPackage.dropTable(VectorTilesLayers.TABLE_NAME);
			}

			if (this.extensionsDao.isTableExists()) {
				this.extensionsDao.deleteByExtension(EXTENSION_NAME);
			}

		} catch (SQLException var4) {
			throw new GeoPackageException(
					"Failed to delete Vector Tiles extension and/or tables. GeoPackage: "
							+ this.geoPackage.getName(),
					var4);
		}
	}

	/**
	 * Get the Layers DAO
	 * 
	 * @return layers dao
	 */
	public VectorTilesLayersDao getVectorTilesLayersDao() {
		return getVectorTilesLayersDao(geoPackage);
	}

	/**
	 * Get the Layers DAO
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * @return layers dao
	 */
	public static VectorTilesLayersDao getVectorTilesLayersDao(
			GeoPackageCore geoPackage) {
		return VectorTilesLayersDao.create(geoPackage);
	}

	/**
	 * Get the Layers DAO
	 * 
	 * @param db
	 *            database connection
	 * @return layers dao
	 */
	public static VectorTilesLayersDao getVectorTilesLayersDao(
			GeoPackageCoreConnection db) {
		return VectorTilesLayersDao.create(db);
	}

	/**
	 * Get the Fields DAO
	 * 
	 * @return fields dao
	 */
	public VectorTilesFieldsDao getVectorTilesFieldsDao() {
		return getVectorTilesFieldsDao(geoPackage);
	}

	/**
	 * Get the Fields DAO
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * @return fields dao
	 */
	public static VectorTilesFieldsDao getVectorTilesFieldsDao(
			GeoPackageCore geoPackage) {
		return VectorTilesFieldsDao.create(geoPackage);
	}

	/**
	 * Get the Fields DAO
	 * 
	 * @param db
	 *            database connection
	 * @return fields dao
	 */
	public static VectorTilesFieldsDao getVectorTilesFieldsDao(
			GeoPackageCoreConnection db) {
		return VectorTilesFieldsDao.create(db);
	}

	/**
	 * Create the Vector Tiles Extension
	 * 
	 * @return true if created
	 */
	public boolean createVectorTilesTables() {
		verifyWritable();

		boolean created = false;

		VectorTilesTableCreator tableCreator = new VectorTilesTableCreator(
				geoPackage);

		try {
			if (!vectorTilesFieldsDao.isTableExists()) {
				created = tableCreator.createFields() > 0;
			}
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to check if " + VectorTilesFields.TABLE_NAME
							+ " table exists and create it",
					e);
		}

		try {
			if (!vectorTilesLayersDao.isTableExists()) {
				created = (tableCreator.createLayers() > 0) || created;
			}
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to check if " + VectorTilesLayers.TABLE_NAME
							+ " table exists and create it",
					e);
		}

		return created;
	}

}
