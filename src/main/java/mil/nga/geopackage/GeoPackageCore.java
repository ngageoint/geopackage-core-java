package mil.nga.geopackage;

import java.io.Closeable;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Callable;

import mil.nga.geopackage.attributes.AttributesTable;
import mil.nga.geopackage.attributes.AttributesTableMetadata;
import mil.nga.geopackage.contents.Contents;
import mil.nga.geopackage.contents.ContentsDao;
import mil.nga.geopackage.contents.ContentsDataType;
import mil.nga.geopackage.db.GeoPackageCoreConnection;
import mil.nga.geopackage.db.GeoPackageDao;
import mil.nga.geopackage.db.GeoPackageTableCreator;
import mil.nga.geopackage.extension.ExtensionManager;
import mil.nga.geopackage.extension.ExtensionsDao;
import mil.nga.geopackage.features.columns.GeometryColumnsDao;
import mil.nga.geopackage.features.user.FeatureTable;
import mil.nga.geopackage.features.user.FeatureTableMetadata;
import mil.nga.geopackage.srs.SpatialReferenceSystemDao;
import mil.nga.geopackage.tiles.matrix.TileMatrixDao;
import mil.nga.geopackage.tiles.matrixset.TileMatrixSetDao;
import mil.nga.geopackage.tiles.user.TileTable;
import mil.nga.geopackage.tiles.user.TileTableMetadata;
import mil.nga.geopackage.user.UserColumn;
import mil.nga.geopackage.user.UserTable;
import mil.nga.sf.proj.Projection;

/**
 * A single GeoPackage database connection
 * 
 * @author osbornb
 */
public interface GeoPackageCore extends Closeable {

	/**
	 * Close the GeoPackage connection
	 */
	public void close();

	/**
	 * Get the GeoPackage name
	 * 
	 * @return name
	 */
	public String getName();

	/**
	 * Get the GeoPackage path
	 * 
	 * @return path
	 */
	public String getPath();

	/**
	 * Get the SQLite database
	 * 
	 * @return connection
	 */
	public GeoPackageCoreConnection getDatabase();

	/**
	 * Get the Table Creator
	 * 
	 * @return table creator
	 * @since 4.0.0
	 */
	public GeoPackageTableCreator getTableCreator();

	/**
	 * Is the GeoPackage writable
	 * 
	 * @return true if writable
	 */
	public boolean isWritable();

	/**
	 * Get the application id
	 *
	 * @return application id
	 * @since 1.2.1
	 */
	public String getApplicationId();

	/**
	 * Get the application id integer
	 * 
	 * @return application id integer
	 * @since 4.0.0
	 */
	public Integer getApplicationIdInteger();

	/**
	 * Get the application id as a hex string prefixed with 0x
	 * 
	 * @return application id hex string
	 * @since 4.0.0
	 */
	public String getApplicationIdHex();

	/**
	 * Get the user version
	 *
	 * @return user version
	 * @since 1.2.1
	 */
	public Integer getUserVersion();

	/**
	 * Get the major user version
	 *
	 * @return major user version
	 * @since 1.2.1
	 */
	public Integer getUserVersionMajor();

	/**
	 * Get the minor user version
	 *
	 * @return minor user version
	 * @since 1.2.1
	 */
	public Integer getUserVersionMinor();

	/**
	 * Get the patch user version
	 *
	 * @return patch user version
	 * @since 1.2.1
	 */
	public Integer getUserVersionPatch();

	/**
	 * Get the feature tables
	 * 
	 * @return table names
	 */
	public List<String> getFeatureTables();

	/**
	 * Get the tile tables
	 * 
	 * @return table names
	 */
	public List<String> getTileTables();

	/**
	 * Get the attributes tables
	 * 
	 * @return table name
	 * @since 1.2.1
	 */
	public List<String> getAttributesTables();

	/**
	 * Get the tables for the contents data type
	 * 
	 * @param type
	 *            data type
	 * @return table names
	 * @since 1.2.1
	 */
	public List<String> getTables(ContentsDataType type);

	/**
	 * Get the tables for the contents data types
	 * 
	 * @param types
	 *            data types
	 * @return table names
	 * @since 4.0.0
	 */
	public List<String> getTables(ContentsDataType... types);

	/**
	 * Get the tables for the contents data type
	 * 
	 * @param type
	 *            data type
	 * @return table names
	 * @since 3.0.1
	 */
	public List<String> getTables(String type);

	/**
	 * Get the tables for the contents data types
	 * 
	 * @param types
	 *            data types
	 * @return table names
	 * @since 4.0.0
	 */
	public List<String> getTables(String... types);

	/**
	 * Get the contents for the data type
	 * 
	 * @param type
	 *            data type
	 * @return contents
	 * @since 4.0.0
	 */
	public List<Contents> getTypeContents(ContentsDataType type);

	/**
	 * Get the contents for the data types
	 * 
	 * @param types
	 *            data types
	 * @return contents
	 * @since 4.0.0
	 */
	public List<Contents> getTypeContents(ContentsDataType... types);

	/**
	 * Get the contents for the data type
	 * 
	 * @param type
	 *            data type
	 * @return contents
	 * @since 3.0.1
	 */
	public List<Contents> getTypeContents(String type);

	/**
	 * Get the contents for the data types
	 * 
	 * @param types
	 *            data types
	 * @return contents
	 * @since 4.0.0
	 */
	public List<Contents> getTypeContents(String... types);

	/**
	 * Get all tables
	 * 
	 * @return table names
	 * @since 1.1.7
	 */
	public List<String> getTables();

	/**
	 * Check if the table is a feature table
	 * 
	 * @param table
	 *            table name
	 * @return true if a feature table
	 * @since 1.1.7
	 */
	public boolean isFeatureTable(String table);

	/**
	 * Check if the table is a tile table
	 * 
	 * @param table
	 *            table name
	 * @return true if a tile table
	 * @since 1.1.7
	 */
	public boolean isTileTable(String table);

	/**
	 * Check if the table is an attribute table
	 * 
	 * @param table
	 *            table name
	 * @return true if an attribute table
	 * @since 3.3.0
	 */
	public boolean isAttributeTable(String table);

	/**
	 * Check if the table is the provided type
	 * 
	 * @param table
	 *            table name
	 * @param type
	 *            data type
	 * @return true if the type of table
	 * @since 4.0.0
	 */
	public boolean isTableType(String table, ContentsDataType type);

	/**
	 * Check if the table is one the provided types
	 * 
	 * @param table
	 *            table name
	 * @param types
	 *            data types
	 * @return true if the type of table
	 * @since 4.0.0
	 */
	public boolean isTableType(String table, ContentsDataType... types);

	/**
	 * Check if the table is the provided type
	 * 
	 * @param table
	 *            table name
	 * @param type
	 *            data type
	 * @return true if the type of table
	 * @since 4.0.0
	 */
	public boolean isTableType(String table, String type);

	/**
	 * Check if the table is one the provided types
	 * 
	 * @param table
	 *            table name
	 * @param types
	 *            data types
	 * @return true if the type of table
	 * @since 4.0.0
	 */
	public boolean isTableType(String table, String... types);

	/**
	 * Check if the table exists as a user contents table
	 * 
	 * @param table
	 *            table name
	 * @return true if a user contents table
	 * @since 3.2.0
	 */
	public boolean isContentsTable(String table);

	/**
	 * Check if the table exists
	 * 
	 * @param table
	 *            table name
	 * @return true if a table
	 * @since 1.2.1
	 */
	public boolean isTable(String table);

	/**
	 * Check if the view exists
	 * 
	 * @param view
	 *            view name
	 * @return true if a view
	 * @since 4.0.0
	 */
	public boolean isView(String view);

	/**
	 * Check if the table or view exists
	 * 
	 * @param name
	 *            table or view name
	 * @return true if a table or view
	 * @since 4.0.0
	 */
	public boolean isTableOrView(String name);

	/**
	 * Get the contents of the user table
	 * 
	 * @param table
	 *            table name
	 * @return contents
	 * @since 3.0.1
	 */
	public Contents getTableContents(String table);

	/**
	 * Get the contents data type of the user table
	 * 
	 * @param table
	 *            table name
	 * @return table type
	 * @since 3.0.1
	 */
	public String getTableType(String table);

	/**
	 * Get the contents data type of the user table
	 * 
	 * @param table
	 *            table name
	 * @return table type or null if not an enumerated type
	 * @since 3.3.0
	 */
	public ContentsDataType getTableDataType(String table);

	/**
	 * Get the bounding box for all table contents in the provided projection
	 * 
	 * @param projection
	 *            desired bounding box projection
	 * 
	 * @return bounding box
	 * @since 3.1.0
	 */
	public BoundingBox getContentsBoundingBox(Projection projection);

	/**
	 * Get the bounding box for all tables in the provided projection, including
	 * contents and table metadata
	 * 
	 * @param projection
	 *            desired bounding box projection
	 * 
	 * @return bounding box
	 * @since 3.1.0
	 */
	public BoundingBox getBoundingBox(Projection projection);

	/**
	 * Get the bounding box for all tables in the provided projection, including
	 * contents, table metadata, and manual queries if enabled
	 * 
	 * @param projection
	 *            desired bounding box projection
	 * @param manual
	 *            manual query flag, true to determine missing bounds manually
	 * 
	 * @return bounding box
	 * @since 3.1.0
	 */
	public BoundingBox getBoundingBox(Projection projection, boolean manual);

	/**
	 * Get the bounding box for all tables in the provided projection, using
	 * only table metadata
	 * 
	 * @param projection
	 *            desired bounding box projection
	 * 
	 * @return bounding box
	 * @since 4.0.0
	 */
	public BoundingBox getTableBoundingBox(Projection projection);

	/**
	 * Get the bounding box for all tables in the provided projection, using
	 * only table metadata and manual queries if enabled
	 * 
	 * @param projection
	 *            desired bounding box projection
	 * @param manual
	 *            manual query flag, true to determine missing bounds manually
	 * 
	 * @return bounding box
	 * @since 4.0.0
	 */
	public BoundingBox getTableBoundingBox(Projection projection,
			boolean manual);

	/**
	 * Get the bounding box from the contents for the table in the table's
	 * projection
	 * 
	 * @param table
	 *            table name
	 * 
	 * @return bounding box
	 * @since 3.1.0
	 */
	public BoundingBox getContentsBoundingBox(String table);

	/**
	 * Get the bounding box from the contents for the table in the provided
	 * projection
	 * 
	 * @param projection
	 *            desired bounding box projection
	 * @param table
	 *            table name
	 * 
	 * @return bounding box
	 * @since 3.1.0
	 */
	public BoundingBox getContentsBoundingBox(Projection projection,
			String table);

	/**
	 * Get the bounding box for the table in the table's projection, including
	 * contents and table metadata
	 * 
	 * @param table
	 *            table name
	 * 
	 * @return bounding box
	 * @since 3.1.0
	 */
	public BoundingBox getBoundingBox(String table);

	/**
	 * Get the bounding box for the table in the provided projection, including
	 * contents and table metadata
	 * 
	 * @param projection
	 *            desired bounding box projection
	 * @param table
	 *            table name
	 * 
	 * @return bounding box
	 * @since 3.1.0
	 */
	public BoundingBox getBoundingBox(Projection projection, String table);

	/**
	 * Get the bounding box for the table in the table's projection, including
	 * contents, table metadata, and manual queries if enabled
	 * 
	 * @param table
	 *            table name
	 * @param manual
	 *            manual query flag, true to determine missing bounds manually
	 * 
	 * @return bounding box
	 * @since 3.1.0
	 */
	public BoundingBox getBoundingBox(String table, boolean manual);

	/**
	 * Get the bounding box for the table in the provided projection, including
	 * contents, table metadata, and manual queries if enabled
	 * 
	 * @param projection
	 *            desired bounding box projection
	 * @param table
	 *            table name
	 * @param manual
	 *            manual query flag, true to determine missing bounds manually
	 * 
	 * @return bounding box
	 * @since 3.1.0
	 */
	public BoundingBox getBoundingBox(Projection projection, String table,
			boolean manual);

	/**
	 * Get the bounding box for the table in the table's projection, using only
	 * table metadata
	 * 
	 * @param table
	 *            table name
	 * 
	 * @return bounding box
	 * @since 4.0.0
	 */
	public BoundingBox getTableBoundingBox(String table);

	/**
	 * Get the bounding box for the table in the provided projection, using only
	 * table metadata
	 * 
	 * @param projection
	 *            desired bounding box projection
	 * @param table
	 *            table name
	 * 
	 * @return bounding box
	 * @since 4.0.0
	 */
	public BoundingBox getTableBoundingBox(Projection projection, String table);

	/**
	 * Get the bounding box for the table in the table's projection, using only
	 * table metadata and manual queries if enabled
	 * 
	 * @param table
	 *            table name
	 * @param manual
	 *            manual query flag, true to determine missing bounds manually
	 * 
	 * @return bounding box
	 * @since 4.0.0
	 */
	public BoundingBox getTableBoundingBox(String table, boolean manual);

	/**
	 * Get the bounding box for the table in the provided projection, using only
	 * table metadata and manual queries if enabled
	 * 
	 * @param projection
	 *            desired bounding box projection
	 * @param table
	 *            table name
	 * @param manual
	 *            manual query flag, true to determine missing bounds manually
	 * 
	 * @return bounding box
	 * @since 4.0.0
	 */
	public BoundingBox getTableBoundingBox(Projection projection, String table,
			boolean manual);

	/**
	 * Get the projection of the table contents
	 * 
	 * @param table
	 *            table name
	 * @return projection
	 * @since 4.0.0
	 */
	public Projection getContentsProjection(String table);

	/**
	 * Get the projection of the table
	 * 
	 * @param table
	 *            table name
	 * @return projection
	 * @since 4.0.0
	 */
	public Projection getProjection(String table);

	/**
	 * Get the feature table bounding box
	 * 
	 * @param projection
	 *            desired projection
	 * @param table
	 *            table name
	 * @param manual
	 *            true to manually query if not indexed
	 * @return bounding box
	 * @since 3.5.0
	 */
	public BoundingBox getFeatureBoundingBox(Projection projection,
			String table, boolean manual);

	/**
	 * Get a Spatial Reference System DAO
	 * 
	 * @return Spatial Reference System DAO
	 */
	public SpatialReferenceSystemDao getSpatialReferenceSystemDao();

	/**
	 * Get a Contents DAO
	 * 
	 * @return Contents DAO
	 */
	public ContentsDao getContentsDao();

	/**
	 * Get a Geometry Columns DAO
	 * 
	 * @return Geometry Columns DAO
	 */
	public GeometryColumnsDao getGeometryColumnsDao();

	/**
	 * Create the Geometry Columns table if it does not already exist
	 * 
	 * @return true if created
	 */
	public boolean createGeometryColumnsTable();

	/**
	 * Create a new feature table
	 * 
	 * WARNING: only creates the feature table, call
	 * {@link #createFeatureTable(FeatureTableMetadata)}) instead to create both
	 * the table and required GeoPackage metadata
	 * 
	 * @param table
	 *            feature table
	 */
	public void createFeatureTable(FeatureTable table);

	/**
	 * Create a new feature table with GeoPackage metadata including: geometry
	 * columns table and row, user feature table, and contents row.
	 * 
	 * @param metadata
	 *            feature table metadata
	 * @return feature table
	 * @since 4.0.0
	 */
	public FeatureTable createFeatureTable(FeatureTableMetadata metadata);

	/**
	 * Get a Tile Matrix Set DAO
	 * 
	 * @return Tile Matrix Set DAO
	 */
	public TileMatrixSetDao getTileMatrixSetDao();

	/**
	 * Create the Tile Matrix Set table if it does not already exist
	 * 
	 * @return true if created
	 */
	public boolean createTileMatrixSetTable();

	/**
	 * Get a Tile Matrix DAO
	 * 
	 * @return Tile Matrix DAO
	 */
	public TileMatrixDao getTileMatrixDao();

	/**
	 * Create the Tile Matrix table if it does not already exist
	 * 
	 * @return true if created
	 */
	public boolean createTileMatrixTable();

	/**
	 * Create a new tile table
	 * 
	 * WARNING: only creates the tile table, call
	 * {@link #createTileTable(TileTableMetadata)}) instead to create both the
	 * table and required GeoPackage metadata
	 * 
	 * @param table
	 *            tile table
	 */
	public void createTileTable(TileTable table);

	/**
	 * Create a new tile table with GeoPackage metadata including: tile matrix
	 * set table and row, tile matrix table, user tile table, and contents row.
	 * 
	 * @param metadata
	 *            tile table metadata
	 * @return tile table
	 * @since 4.0.0
	 */
	public TileTable createTileTable(TileTableMetadata metadata);

	/**
	 * Create a new attributes table
	 * 
	 * WARNING: only creates the attributes table, call
	 * {@link #createAttributesTable(AttributesTableMetadata)}) instead to
	 * create both the table and required GeoPackage metadata
	 * 
	 * @param table
	 *            attributes table
	 * @since 1.2.1
	 */
	public void createAttributesTable(AttributesTable table);

	/**
	 * Create a new attributes table with GeoPackage metadata including: user
	 * attributes table and contents row.
	 * 
	 * @param metadata
	 *            attributes table metadata
	 * @return attributes table
	 * @since 4.0.0
	 */
	public AttributesTable createAttributesTable(
			AttributesTableMetadata metadata);

	/**
	 * Get an Extensions DAO
	 * 
	 * @return Extensions DAO
	 */
	public ExtensionsDao getExtensionsDao();

	/**
	 * Create the Extensions table if it does not already exist
	 * 
	 * @return true if created
	 */
	public boolean createExtensionsTable();

	/**
	 * Delete the user table (a feature or tile table) and all GeoPackage
	 * metadata
	 * 
	 * @param table
	 *            table name
	 */
	public void deleteTable(String table);

	/**
	 * Attempt to delete the user table (a feature or tile table) and all
	 * GeoPackage metadata quietly
	 * 
	 * @param tableName
	 *            table name
	 */
	public void deleteTableQuietly(String tableName);

	/**
	 * Verify the GeoPackage is writable and throw an exception if it is not
	 */
	public void verifyWritable();

	/**
	 * Create a dao
	 *
	 * @param type
	 *            dao class type
	 * @param <D>
	 *            dao type
	 * @param <T>
	 *            class type
	 * @return base dao implementation
	 * @since 1.1.0
	 */
	public <D extends GeoPackageDao<T, ?>, T> D createDao(Class<T> type);

	/**
	 * Execute the sql on the GeoPackage database
	 * 
	 * @param sql
	 *            sql statement
	 * @since 1.1.2
	 */
	public void execSQL(String sql);

	/**
	 * Begin a transaction
	 * 
	 * @since 3.3.0
	 */
	public void beginTransaction();

	/**
	 * End a transaction successfully
	 * 
	 * @since 3.3.0
	 */
	public void endTransaction();

	/**
	 * Fail a transaction
	 * 
	 * @since 3.3.0
	 */
	public void failTransaction();

	/**
	 * End a transaction
	 * 
	 * @param successful
	 *            true if the transaction was successful, false to rollback or
	 *            not commit
	 * @since 3.3.0
	 */
	public void endTransaction(boolean successful);

	/**
	 * End a transaction as successful and begin a new transaction
	 *
	 * @since 3.3.0
	 */
	public void endAndBeginTransaction();

	/**
	 * Commit changes on the connection
	 * 
	 * @since 3.3.0
	 */
	public void commit();

	/**
	 * Determine if currently within a transaction
	 * 
	 * @return true if in transaction
	 * 
	 * @since 3.3.0
	 */
	public boolean inTransaction();

	/**
	 * Execute the {@link Callable} class inside an ORMLite transaction
	 * 
	 * @param callable
	 *            Callable to execute inside of the transaction.
	 * @param <T>
	 *            callable type
	 * @return The object returned by the callable.
	 * @throws SQLException
	 *             upon transaction error
	 * @since 3.3.0
	 */
	public <T> T callInTransaction(Callable<T> callable) throws SQLException;

	/**
	 * If foreign keys is disabled and there are no foreign key violations,
	 * enables foreign key checks, else logs violations
	 * 
	 * @return true if enabled or already enabled, false if foreign key
	 *         violations and not enabled
	 * @since 3.3.0
	 */
	public boolean enableForeignKeys();

	/**
	 * Query for the foreign keys value
	 * 
	 * @return true if enabled, false if disabled
	 * @since 3.3.0
	 */
	public boolean foreignKeys();

	/**
	 * Change the foreign keys state
	 * 
	 * @param on
	 *            true to turn on, false to turn off
	 * @return previous foreign keys value
	 * @since 3.3.0
	 */
	public boolean foreignKeys(boolean on);

	/**
	 * Drop the table if it exists. Drops the table with the table name, not
	 * limited to GeoPackage specific tables.
	 * 
	 * @param table
	 *            table name
	 * @since 1.1.5
	 */
	public void dropTable(String table);

	/**
	 * Drop the table if it exists. Drops the table with the table name, not
	 * limited to GeoPackage specific tables.
	 *
	 * @param view
	 *            view name
	 * @since 4.0.0
	 */
	public void dropView(String view);

	/**
	 * Rename the table
	 * 
	 * @param tableName
	 *            table name
	 * @param newTableName
	 *            new table name
	 * @since 3.3.0
	 */
	public void renameTable(String tableName, String newTableName);

	/**
	 * Copy the table with transferred contents and extensions
	 * 
	 * @param tableName
	 *            table name
	 * @param newTableName
	 *            new table name
	 * @since 3.3.0
	 */
	public void copyTable(String tableName, String newTableName);

	/**
	 * Copy the table with transferred contents but no extensions
	 * 
	 * @param tableName
	 *            table name
	 * @param newTableName
	 *            new table name
	 * @since 3.3.0
	 */
	public void copyTableNoExtensions(String tableName, String newTableName);

	/**
	 * Copy the table but leave the user table empty and without extensions
	 * 
	 * @param tableName
	 *            table name
	 * @param newTableName
	 *            new table name
	 * @since 3.3.0
	 */
	public void copyTableAsEmpty(String tableName, String newTableName);

	/**
	 * Rebuild the GeoPackage, repacking it into a minimal amount of disk space
	 * 
	 * @since 3.3.0
	 */
	public void vacuum();

	/**
	 * Get an extension manager on the GeoPackage
	 * 
	 * @return extension manager
	 * @since 4.0.0
	 */
	public ExtensionManager getExtensionManager();

	/**
	 * Create a new user table
	 * 
	 * @param table
	 *            user table
	 * @since 3.0.1
	 */
	public void createUserTable(UserTable<? extends UserColumn> table);

}
