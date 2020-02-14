package mil.nga.geopackage;

import java.io.Closeable;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;

import mil.nga.geopackage.attributes.AttributesColumn;
import mil.nga.geopackage.attributes.AttributesTable;
import mil.nga.geopackage.core.contents.Contents;
import mil.nga.geopackage.core.contents.ContentsDao;
import mil.nga.geopackage.core.contents.ContentsDataType;
import mil.nga.geopackage.core.srs.SpatialReferenceSystemDao;
import mil.nga.geopackage.core.srs.SpatialReferenceSystemSfSqlDao;
import mil.nga.geopackage.core.srs.SpatialReferenceSystemSqlMmDao;
import mil.nga.geopackage.db.GeoPackageCoreConnection;
import mil.nga.geopackage.db.GeoPackageDao;
import mil.nga.geopackage.db.table.Constraint;
import mil.nga.geopackage.extension.ExtensionsDao;
import mil.nga.geopackage.extension.contents.ContentsIdDao;
import mil.nga.geopackage.extension.coverage.GriddedCoverageDao;
import mil.nga.geopackage.extension.coverage.GriddedTileDao;
import mil.nga.geopackage.extension.index.GeometryIndexDao;
import mil.nga.geopackage.extension.index.TableIndexDao;
import mil.nga.geopackage.extension.link.FeatureTileLinkDao;
import mil.nga.geopackage.extension.portrayal.*;
import mil.nga.geopackage.extension.related.ExtendedRelationsDao;
import mil.nga.geopackage.extension.scale.TileScalingDao;
import mil.nga.geopackage.extension.tile_matrix_set.ExtTileMatrixDao;
import mil.nga.geopackage.extension.tile_matrix_set.ExtTileMatrixSetDao;
import mil.nga.geopackage.extension.tile_matrix_set.TileMatrixTablesDao;
import mil.nga.geopackage.extension.tile_matrix_set.TileMatrixVariableWidthsDao;
import mil.nga.geopackage.extension.vector_tiles.VectorTilesFieldsDao;
import mil.nga.geopackage.extension.vector_tiles.VectorTilesLayersDao;
import mil.nga.geopackage.features.columns.GeometryColumns;
import mil.nga.geopackage.features.columns.GeometryColumnsDao;
import mil.nga.geopackage.features.columns.GeometryColumnsSfSqlDao;
import mil.nga.geopackage.features.columns.GeometryColumnsSqlMmDao;
import mil.nga.geopackage.features.user.FeatureColumn;
import mil.nga.geopackage.features.user.FeatureTable;
import mil.nga.geopackage.metadata.MetadataDao;
import mil.nga.geopackage.metadata.reference.MetadataReferenceDao;
import mil.nga.geopackage.schema.columns.DataColumnsDao;
import mil.nga.geopackage.schema.constraints.DataColumnConstraintsDao;
import mil.nga.geopackage.tiles.matrix.TileMatrixDao;
import mil.nga.geopackage.tiles.matrixset.TileMatrixSet;
import mil.nga.geopackage.tiles.matrixset.TileMatrixSetDao;
import mil.nga.geopackage.tiles.user.TileTable;
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
	void close();

	/**
	 * Get the GeoPackage name
	 * 
	 * @return name
	 */
	String getName();

	/**
	 * Get the GeoPackage path
	 * 
	 * @return path
	 */
	String getPath();

	/**
	 * Get the SQLite database
	 * 
	 * @return connection
	 */
	GeoPackageCoreConnection getDatabase();

	/**
	 * Is the GeoPackage writable
	 * 
	 * @return true if writable
	 */
	boolean isWritable();

	/**
	 * Get the application id
	 *
	 * @return application id
	 * @since 1.2.1
	 */
	String getApplicationId();

	/**
	 * Get the user version
	 *
	 * @return user version
	 * @since 1.2.1
	 */
	int getUserVersion();

	/**
	 * Get the major user version
	 *
	 * @return major user version
	 * @since 1.2.1
	 */
	int getUserVersionMajor();

	/**
	 * Get the minor user version
	 *
	 * @return minor user version
	 * @since 1.2.1
	 */
	int getUserVersionMinor();

	/**
	 * Get the patch user version
	 *
	 * @return patch user version
	 * @since 1.2.1
	 */
	int getUserVersionPatch();

	/**
	 * Get the feature tables
	 * 
	 * @return table names
	 */
	List<String> getFeatureTables();

	/**
	 * Get the tile tables
	 * 
	 * @return table names
	 */
	List<String> getTileTables();

	/**
	 * Get the attributes tables
	 * 
	 * @return table name
	 * @since 1.2.1
	 */
	List<String> getAttributesTables();

	/**
	 * Get the tables for the contents data type
	 * 
	 * @param type
	 *            data type
	 * @return table names
	 * @since 1.2.1
	 */
	List<String> getTables(ContentsDataType type);

	/**
	 * Get the tables for the contents data type
	 * 
	 * @param type
	 *            data type
	 * @return table names
	 * @since 3.0.1
	 */
	List<String> getTables(String type);

	/**
	 * Get the feature and tile tables
	 * 
	 * @return table names
	 * @since 1.2.1
	 */
	List<String> getFeatureAndTileTables();

	/**
	 * Get all tables
	 * 
	 * @return table names
	 * @since 1.1.7
	 */
	List<String> getTables();

	/**
	 * Check if the table is a feature table
	 * 
	 * @param table
	 *            table name
	 * @return true if a feature table
	 * @since 1.1.7
	 */
	boolean isFeatureTable(String table);

	/**
	 * Check if the table is a tile table
	 * 
	 * @param table
	 *            table name
	 * @return true if a tile table
	 * @since 1.1.7
	 */
	boolean isTileTable(String table);

	/**
	 * Check if the table is an attribute table
	 * 
	 * @param table
	 *            table name
	 * @return true if an attribute table
	 * @since 3.3.0
	 */
	boolean isAttributeTable(String table);

	/**
	 * Check if the table is the provided type
	 * 
	 * @param type
	 *            data type
	 * @param table
	 *            table name
	 * @return true if the type of table
	 * @since 1.2.1
	 */
	boolean isTableType(ContentsDataType type, String table);

	/**
	 * Check if the table is the provided type
	 * 
	 * @param type
	 *            data type
	 * @param table
	 *            table name
	 * @return true if the type of table
	 * @since 3.0.1
	 */
	boolean isTableType(String type, String table);

	/**
	 * Check if the table exists as a feature or tile table
	 * 
	 * @param table
	 *            table name
	 * @return true if a feature or tile table
	 * @since 1.1.7
	 */
	boolean isFeatureOrTileTable(String table);

	/**
	 * Check if the table exists as a user contents table
	 * 
	 * @param table
	 *            table name
	 * @return true if a user contents table
	 * @since 3.2.0
	 */
	boolean isContentsTable(String table);

	/**
	 * Check if the table exists
	 * 
	 * @param table
	 *            table name
	 * @return true if a table
	 * @since 1.2.1
	 */
	boolean isTable(String table);

	/**
	 * Check if the view exists
	 * 
	 * @param view
	 *            view name
	 * @return true if a view
	 * @since 3.5.1
	 */
	public boolean isView(String view);

	/**
	 * Check if the table or view exists
	 * 
	 * @param name
	 *            table or view name
	 * @return true if a table or view
	 * @since 3.5.1
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
	Contents getTableContents(String table);

	/**
	 * Get the contents data type of the user table
	 * 
	 * @param table
	 *            table name
	 * @return table type
	 * @since 3.0.1
	 */
	String getTableType(String table);

	/**
	 * Get the contents data type of the user table
	 * 
	 * @param table
	 *            table name
	 * @return table type or null if not an enumerated type
	 * @since 3.3.0
	 */
	ContentsDataType getTableDataType(String table);

	/**
	 * Get the bounding box for all table contents in the provided projection
	 * 
	 * @param projection
	 *            desired bounding box projection
	 * 
	 * @return bounding box
	 * @since 3.1.0
	 */
	BoundingBox getContentsBoundingBox(Projection projection);

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
	BoundingBox getBoundingBox(Projection projection);

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
	BoundingBox getBoundingBox(Projection projection, boolean manual);

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
	BoundingBox getContentsBoundingBox(String table);

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
	BoundingBox getContentsBoundingBox(Projection projection,
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
	BoundingBox getBoundingBox(String table);

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
	BoundingBox getBoundingBox(Projection projection, String table);

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
	BoundingBox getBoundingBox(String table, boolean manual);

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
	BoundingBox getBoundingBox(Projection projection, String table,
			boolean manual);

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
	SpatialReferenceSystemDao getSpatialReferenceSystemDao();

	/**
	 * Get a SQL/MM Spatial Reference System DAO
	 * 
	 * @return SQL/MM Spatial Reference System DAO
	 */
	SpatialReferenceSystemSqlMmDao getSpatialReferenceSystemSqlMmDao();

	/**
	 * Get a SF/SQL Spatial Reference System DAO
	 * 
	 * @return SF/SQL Spatial Reference System DAO
	 */
	SpatialReferenceSystemSfSqlDao getSpatialReferenceSystemSfSqlDao();

	/**
	 * Get a Contents DAO
	 * 
	 * @return Contents DAO
	 */
	ContentsDao getContentsDao();

	/**
	 * Get a Geometry Columns DAO
	 * 
	 * @return Geometry Columns DAO
	 */
	GeometryColumnsDao getGeometryColumnsDao();

	/**
	 * Get a SQL/MM Geometry Columns DAO
	 * 
	 * @return SQL/MM Geometry Columns DAO
	 */
	GeometryColumnsSqlMmDao getGeometryColumnsSqlMmDao();

	/**
	 * Get a SF/SQL Geometry Columns DAO
	 * 
	 * @return SF/SQL Geometry Columns DAO
	 */
	GeometryColumnsSfSqlDao getGeometryColumnsSfSqlDao();

	/**
	 * Create the Geometry Columns table if it does not already exist
	 * 
	 * @return true if created
	 */
	boolean createGeometryColumnsTable();

	/**
	 * Create a new feature table
	 * 
	 * @param table
	 *            feature table
	 */
	void createFeatureTable(FeatureTable table);

	/**
	 * Create a new feature table with GeoPackage metadata. Create the Geometry
	 * Columns table if needed, create a user feature table, create a new
	 * Contents, insert the new Geometry Columns.
	 * 
	 * The user feature table will be created with 2 columns, an id column named
	 * "id" and a geometry column using {@link GeometryColumns#getColumnName()}.
	 * 
	 * @param geometryColumns
	 *            geometry columns to create
	 * @param boundingBox
	 *            contents bounding box
	 * @param srsId
	 *            spatial reference system id
	 * @return geometry columns
	 */
	GeometryColumns createFeatureTableWithMetadata(
			GeometryColumns geometryColumns, BoundingBox boundingBox,
			long srsId);

	/**
	 * Create a new feature table with GeoPackage metadata. Create the Geometry
	 * Columns table if needed, create a user feature table, create a new
	 * Contents, insert the new Geometry Columns.
	 * 
	 * The user feature table will be created with 2 columns, an id column with
	 * the provided name and a geometry column using
	 * {@link GeometryColumns#getColumnName()}.
	 * 
	 * @param geometryColumns
	 *            geometry columns to create
	 * @param idColumnName
	 *            id column name
	 * @param boundingBox
	 *            contents bounding box
	 * @param srsId
	 *            spatial reference system id
	 * @return geometry columns
	 * @since 1.1.1
	 */
	GeometryColumns createFeatureTableWithMetadata(
			GeometryColumns geometryColumns, String idColumnName,
			BoundingBox boundingBox, long srsId);

	/**
	 * Create a new feature table with GeoPackage metadata. Create the Geometry
	 * Columns table if needed, create a user feature table, create a new
	 * Contents, insert the new Geometry Columns.
	 * 
	 * The user feature table will be created with 2 + additionalColumns.size()
	 * columns, an id column named "id", a geometry column using
	 * {@link GeometryColumns#getColumnName()}, and the provided additional
	 * columns.
	 * 
	 * @param geometryColumns
	 *            geometry columns to create
	 * @param additionalColumns
	 *            additional user feature table columns to create in addition to
	 *            id and geometry columns
	 * @param boundingBox
	 *            contents bounding box
	 * @param srsId
	 *            spatial reference system id
	 * @return geometry columns
	 * @since 1.1.1
	 */
	GeometryColumns createFeatureTableWithMetadata(
			GeometryColumns geometryColumns,
			List<FeatureColumn> additionalColumns, BoundingBox boundingBox,
			long srsId);

	/**
	 * Create a new feature table with GeoPackage metadata. Create the Geometry
	 * Columns table if needed, create a user feature table, create a new
	 * Contents, insert the new Geometry Columns.
	 * 
	 * The user feature table will be created with 2 + additionalColumns.size()
	 * columns, an id column with the provided name, a geometry column using
	 * {@link GeometryColumns#getColumnName()}, and the provided additional
	 * columns.
	 * 
	 * @param geometryColumns
	 *            geometry columns to create
	 * @param idColumnName
	 *            id column name
	 * @param additionalColumns
	 *            additional user feature table columns to create in addition to
	 *            id and geometry columns
	 * @param boundingBox
	 *            contents bounding box
	 * @param srsId
	 *            spatial reference system id
	 * @return geometry columns
	 * @since 1.1.1
	 */
	GeometryColumns createFeatureTableWithMetadata(
			GeometryColumns geometryColumns, String idColumnName,
			List<FeatureColumn> additionalColumns, BoundingBox boundingBox,
			long srsId);

	/**
	 * Create a new feature table with GeoPackage metadata. Create the Geometry
	 * Columns table if needed, create a user feature table, create a new
	 * Contents, insert the new Geometry Columns.
	 * 
	 * The user feature table will be created using only the provided columns.
	 * These should include the id column and the geometry column defined in
	 * {@link GeometryColumns#getColumnName()}
	 * 
	 * @param geometryColumns
	 *            geometry columns to create
	 * @param boundingBox
	 *            contents bounding box
	 * @param srsId
	 *            spatial reference system id
	 * @param columns
	 *            user feature table columns to create
	 * @return geometry columns
	 * @since 1.1.1
	 */
	GeometryColumns createFeatureTableWithMetadata(
			GeometryColumns geometryColumns, BoundingBox boundingBox,
			long srsId, List<FeatureColumn> columns);

	/**
	 * Get a Tile Matrix Set DAO
	 * 
	 * @return Tile Matrix Set DAO
	 */
	TileMatrixSetDao getTileMatrixSetDao();

	/**
	 * Create the Tile Matrix Set table if it does not already exist
	 * 
	 * @return true if created
	 */
	boolean createTileMatrixSetTable();

	/**
	 * Get a Tile Matrix DAO
	 * 
	 * @return Tile Matrix DAO
	 */
	TileMatrixDao getTileMatrixDao();

	/**
	 * Create the Tile Matrix table if it does not already exist
	 * 
	 * @return true if created
	 */
	boolean createTileMatrixTable();

	/**
	 * Create a new tile table
	 * 
	 * @param table
	 *            tile table
	 */
	void createTileTable(TileTable table);

	/**
	 * Create a new tile table and the GeoPackage metadata
	 * 
	 * @param tableName
	 *            table name
	 * @param contentsBoundingBox
	 *            contents bounding box
	 * @param contentsSrsId
	 *            contents SRS id
	 * @param tileMatrixSetBoundingBox
	 *            tile matrix set bounding box
	 * @param tileMatrixSetSrsId
	 *            tile matrix set SRS id
	 * @return tile matrix set
	 */
	TileMatrixSet createTileTableWithMetadata(String tableName,
			BoundingBox contentsBoundingBox, long contentsSrsId,
			BoundingBox tileMatrixSetBoundingBox, long tileMatrixSetSrsId);

	/**
	 * Create a new tile table of the specified type and the GeoPackage metadata
	 * 
	 * @param dataType
	 *            contents data type
	 * @param tableName
	 *            table name
	 * @param contentsBoundingBox
	 *            contents bounding box
	 * @param contentsSrsId
	 *            contents SRS id
	 * @param tileMatrixSetBoundingBox
	 *            tile matrix set bounding box
	 * @param tileMatrixSetSrsId
	 *            tile matrix set SRS id
	 * @return tile matrix set
	 * @since 1.2.1
	 */
	TileMatrixSet createTileTableWithMetadata(ContentsDataType dataType,
			String tableName, BoundingBox contentsBoundingBox,
			long contentsSrsId, BoundingBox tileMatrixSetBoundingBox,
			long tileMatrixSetSrsId);

	/**
	 * Get a Data Columns DAO
	 * 
	 * @return Data Columns DAO
	 */
	DataColumnsDao getDataColumnsDao();

	/**
	 * Create the Data Columns table if it does not already exist
	 * 
	 * @return true if created
	 */
	boolean createDataColumnsTable();

	/**
	 * Get a Data Column Constraints DAO
	 * 
	 * @return Data Column Constraints DAO
	 */
	DataColumnConstraintsDao getDataColumnConstraintsDao();

	/**
	 * Create the Data Column Constraints table if it does not already exist
	 * 
	 * @return true if created
	 */
	boolean createDataColumnConstraintsTable();

	/**
	 * Get a Metadata DAO
	 * 
	 * @return Metadata DAO
	 */
	MetadataDao getMetadataDao();

	/**
	 * Create the Metadata table if it does not already exist
	 * 
	 * @return true if created
	 */
	boolean createMetadataTable();

	/**
	 * Get a Metadata Reference DAO
	 * 
	 * @return Metadata Reference DAO
	 */
	MetadataReferenceDao getMetadataReferenceDao();

	/**
	 * Create the Metadata Reference table if it does not already exist
	 * 
	 * @return true if created
	 */
	boolean createMetadataReferenceTable();

	/**
	 * Get an Extensions DAO
	 * 
	 * @return Extensions DAO
	 */
	ExtensionsDao getExtensionsDao();

	/**
	 * Create the Extensions table if it does not already exist
	 * 
	 * @return true if created
	 */
	boolean createExtensionsTable();

	/**
	 * Delete the user table (a feature or tile table) and all GeoPackage
	 * metadata
	 * 
	 * @param table
	 *            table name
	 */
	void deleteTable(String table);

	/**
	 * Attempt to delete the user table (a feature or tile table) and all
	 * GeoPackage metadata quietly
	 * 
	 * @param tableName
	 *            table name
	 */
	void deleteTableQuietly(String tableName);

	/**
	 * Verify the GeoPackage is writable and throw an exception if it is not
	 */
	void verifyWritable();

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
	<D extends GeoPackageDao<T, ?>, T> D createDao(Class<T> type);

	/**
	 * Execute the sql on the GeoPackage database
	 * 
	 * @param sql
	 *            sql statement
	 * @since 1.1.2
	 */
	void execSQL(String sql);

	/**
	 * Begin a transaction
	 * 
	 * @since 3.3.0
	 */
	void beginTransaction();

	/**
	 * End a transaction successfully
	 * 
	 * @since 3.3.0
	 */
	void endTransaction();

	/**
	 * Fail a transaction
	 * 
	 * @since 3.3.0
	 */
	void failTransaction();

	/**
	 * End a transaction
	 * 
	 * @param successful
	 *            true if the transaction was successful, false to rollback or
	 *            not commit
	 * @since 3.3.0
	 */
	void endTransaction(boolean successful);

	/**
	 * End a transaction as successful and begin a new transaction
	 *
	 * @since 3.3.0
	 */
	void endAndBeginTransaction();

	/**
	 * Commit changes on the connection
	 * 
	 * @since 3.3.0
	 */
	void commit();

	/**
	 * Determine if currently within a transaction
	 * 
	 * @return true if in transaction
	 * 
	 * @since 3.3.0
	 */
	boolean inTransaction();

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
	<T> T callInTransaction(Callable<T> callable) throws SQLException;

	/**
	 * If foreign keys is disabled and there are no foreign key violations,
	 * enables foreign key checks, else logs violations
	 * 
	 * @return true if enabled or already enabled, false if foreign key
	 *         violations and not enabled
	 * @since 3.3.0
	 */
	boolean enableForeignKeys();

	/**
	 * Query for the foreign keys value
	 * 
	 * @return true if enabled, false if disabled
	 * @since 3.3.0
	 */
	boolean foreignKeys();

	/**
	 * Change the foreign keys state
	 * 
	 * @param on
	 *            true to turn on, false to turn off
	 * @return previous foreign keys value
	 * @since 3.3.0
	 */
	boolean foreignKeys(boolean on);

	/**
	 * Drop the table if it exists. Drops the table with the table name, not
	 * limited to GeoPackage specific tables.
	 *
	 * @param table
	 *            table name
	 * @since 1.1.5
	 */
	void dropTable(String table);

	/**
	 * Drop the table if it exists. Drops the table with the table name, not
	 * limited to GeoPackage specific tables.
	 *
	 * @param view
	 *            view name
	 */
	void dropView(String view);

	/**
	 * Rename the table
	 * 
	 * @param tableName
	 *            table name
	 * @param newTableName
	 *            new table name
	 * @since 3.3.0
	 */
	void renameTable(String tableName, String newTableName);

	/**
	 * Copy the table with transferred contents and extensions
	 * 
	 * @param tableName
	 *            table name
	 * @param newTableName
	 *            new table name
	 * @since 3.3.0
	 */
	void copyTable(String tableName, String newTableName);

	/**
	 * Copy the table with transferred contents but no extensions
	 * 
	 * @param tableName
	 *            table name
	 * @param newTableName
	 *            new table name
	 * @since 3.3.0
	 */
	void copyTableNoExtensions(String tableName, String newTableName);

	/**
	 * Copy the table but leave the user table empty and without extensions
	 * 
	 * @param tableName
	 *            table name
	 * @param newTableName
	 *            new table name
	 * @since 3.3.0
	 */
	void copyTableAsEmpty(String tableName, String newTableName);

	/**
	 * Rebuild the GeoPackage, repacking it into a minimal amount of disk space
	 * 
	 * @since 3.3.0
	 */
	void vacuum();

	/**
	 * Get a 2D Gridded Coverage DAO
	 * 
	 * @return 2d gridded coverage dao
	 * @since 1.2.1
	 */
	GriddedCoverageDao getGriddedCoverageDao();

	/**
	 * Create the 2D Gridded Coverage Table if it does not exist
	 * 
	 * @return true if created
	 * @since 1.2.1
	 */
	boolean createGriddedCoverageTable();

	/**
	 * Get a 2D Gridded Tile DAO
	 * 
	 * @return 2d gridded tile dao
	 * @since 1.2.1
	 */
	GriddedTileDao getGriddedTileDao();

	/**
	 * Create the 2D Gridded Tile Table if it does not exist
	 * 
	 * @return true if created
	 * @since 1.2.1
	 */
	boolean createGriddedTileTable();

	/**
	 * Get a Table Index DAO
	 * 
	 * @return table index dao
	 * @since 1.1.0
	 */
	TableIndexDao getTableIndexDao();

	/**
	 * Create the Table Index Table if it does not exist
	 * 
	 * @return true if created
	 * @since 1.1.0
	 */
	boolean createTableIndexTable();

	/**
	 * Get a Geometry Index DAO
	 * 
	 * @return geometry index dao
	 * @since 1.1.0
	 */
	GeometryIndexDao getGeometryIndexDao();

	/**
	 * Create Geometry Index Table if it does not exist and index it
	 * 
	 * @return true if created
	 * @since 1.1.0
	 */
	boolean createGeometryIndexTable();

	/**
	 * Index the Geometry Index Table if needed
	 * 
	 * @return true if indexed
	 * @since 3.1.0
	 */
	boolean indexGeometryIndexTable();

	/**
	 * Un-index the Geometry Index Table if needed
	 * 
	 * @return true if unindexed
	 * @since 3.1.0
	 */
	boolean unindexGeometryIndexTable();

	/**
	 * Get a Feature Tile Link DAO
	 * 
	 * @return feature tile link dao
	 * @since 1.1.5
	 */
	FeatureTileLinkDao getFeatureTileLinkDao();

	/**
	 * Create the Feature Tile Link Table if it does not exist
	 * 
	 * @return true if created
	 * @since 1.1.5
	 */
	boolean createFeatureTileLinkTable();

	/**
	 * Create a new attributes table (only the attributes table is created, no
	 * Contents entry is created)
	 * 
	 * @param table
	 *            attributes table
	 * @since 1.2.1
	 */
	void createAttributesTable(AttributesTable table);

	/**
	 * Create a new attributes table and a new Contents
	 * 
	 * The attributes table will be created with 1 + additionalColumns.size()
	 * columns, an id column named "id" and the provided additional columns.
	 * 
	 * @param tableName
	 *            table name
	 * @param additionalColumns
	 *            additional attributes table columns to create in addition to
	 *            id
	 * @return attributes table
	 * @since 1.2.1
	 */
	AttributesTable createAttributesTableWithId(String tableName,
			List<AttributesColumn> additionalColumns);

	/**
	 * Create a new attributes table and a new Contents
	 * 
	 * The attributes table will be created with 1 + additionalColumns.size()
	 * columns, an id column named "id" and the provided additional columns.
	 * 
	 * @param tableName
	 *            table name
	 * @param additionalColumns
	 *            additional attributes table columns to create in addition to
	 *            id
	 * @param constraints
	 *            constraints
	 * @return attributes table
	 * @since 3.3.0
	 */
	AttributesTable createAttributesTableWithId(String tableName,
			List<AttributesColumn> additionalColumns,
			Collection<Constraint> constraints);

	/**
	 * Create a new attributes table and a new Contents
	 * 
	 * The attributes table will be created with 1 + additionalColumns.size()
	 * columns, an id column with the provided name and the provided additional
	 * columns.
	 * 
	 * @param tableName
	 *            table name
	 * @param idColumnName
	 *            id column name
	 * @param additionalColumns
	 *            additional attributes table columns to create in addition to
	 *            id
	 * @return attributes table
	 * @since 1.2.1
	 */
	AttributesTable createAttributesTable(String tableName,
			String idColumnName, List<AttributesColumn> additionalColumns);

	/**
	 * Create a new attributes table and a new Contents
	 * 
	 * The attributes table will be created with 1 + additionalColumns.size()
	 * columns, an id column with the provided name and the provided additional
	 * columns.
	 * 
	 * @param tableName
	 *            table name
	 * @param idColumnName
	 *            id column name
	 * @param additionalColumns
	 *            additional attributes table columns to create in addition to
	 *            id
	 * @param constraints
	 *            constraints
	 * @return attributes table
	 * @since 3.3.0
	 */
	AttributesTable createAttributesTable(String tableName,
			String idColumnName, List<AttributesColumn> additionalColumns,
			Collection<Constraint> constraints);

	/**
	 * Create a new attributes table and a new Contents
	 * 
	 * The attributes table will be created with columns.size() columns and must
	 * include an integer id column
	 * 
	 * @param tableName
	 *            table name
	 * @param columns
	 *            table columns to create
	 * @return attributes table
	 * @since 1.2.1
	 */
	AttributesTable createAttributesTable(String tableName,
			List<AttributesColumn> columns);

	/**
	 * Create a new attributes table and a new Contents
	 * 
	 * The attributes table will be created with columns.size() columns and must
	 * include an integer id column
	 * 
	 * @param tableName
	 *            table name
	 * @param columns
	 *            table columns to create
	 * @param constraints
	 *            constraints
	 * @return attributes table
	 * @since 3.3.0
	 */
	AttributesTable createAttributesTable(String tableName,
			List<AttributesColumn> columns, Collection<Constraint> constraints);

	/**
	 * Get a Tile Scaling DAO
	 * 
	 * @return tile scaling dao
	 * @since 2.0.2
	 */
	TileScalingDao getTileScalingDao();

	/**
	 * Create the Tile Scaling Table if it does not exist
	 * 
	 * @return true if created
	 * @since 2.0.2
	 */
	boolean createTileScalingTable();

	/**
	 * Get a Extended Relations DAO
	 * 
	 * @return extended relations dao
	 * @since 3.0.1
	 */
	ExtendedRelationsDao getExtendedRelationsDao();

	/**
	 * Create the Extended Relations Table if it does not exist
	 * 
	 * @return true if created
	 * @since 3.0.1
	 */
	boolean createExtendedRelationsTable();

	/**
	 * Get a Contents Id DAO
	 * 
	 * @return contents id dao
	 * @since 3.2.0
	 */
	ContentsIdDao getContentsIdDao();

	/**
	 * Create the Contents Id Table if it does not exist
	 * 
	 * @return true if created
	 * @since 3.2.0
	 */
	boolean createContentsIdTable();

	/**
	 * Create a new user table
	 * 
	 * @param table
	 *            user table
	 * @since 3.0.1
	 */
	void createUserTable(UserTable<? extends UserColumn> table);

	VectorTilesLayersDao getVectorTilesLayersDao();
	VectorTilesFieldsDao getVectorTilesFieldsDao();
	StylesDao getStylesDao();
	StylesheetsDao getStylesheetsDao();
	SymbolContentDao getSymbolContentDao();
	SymbolImagesDao getSymbolImagesDao();
	SymbolsDao getSymbolsDao();
	ExtTileMatrixDao getExtTileMatrixDao();
	ExtTileMatrixSetDao getExtTileMatrixSetDao();
	TileMatrixTablesDao getTileMatrixTablesDao();
	TileMatrixVariableWidthsDao getTileMatrixVariableWidthsDao();

	/**
	 * Create the Vector Tiles Extension tables if they do not exist
	 *
	 * @return true if any table is created
	 */
	boolean createVectorTilesTables();

	/**
	 * Create the Portrayal Extension tables if they do not exist
	 *
	 * @return true if any table is created
	 */
	boolean createPortrayalTables();

	/**
	 * Create the Tile Matrix Set Extension tables and views if they do not exist
	 *
	 * @return true if any table is created
	 */
	boolean createTileMatrixSetExtension();
}
