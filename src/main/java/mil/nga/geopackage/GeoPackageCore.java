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
import mil.nga.geopackage.extension.portrayal.StylesDao;
import mil.nga.geopackage.extension.portrayal.StylesheetsDao;
import mil.nga.geopackage.extension.portrayal.SymbolContentDao;
import mil.nga.geopackage.extension.portrayal.SymbolImagesDao;
import mil.nga.geopackage.extension.portrayal.SymbolsDao;
import mil.nga.geopackage.extension.related.ExtendedRelationsDao;
import mil.nga.geopackage.extension.scale.TileScalingDao;
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
	 * Get the user version
	 *
	 * @return user version
	 * @since 1.2.1
	 */
	public int getUserVersion();

	/**
	 * Get the major user version
	 *
	 * @return major user version
	 * @since 1.2.1
	 */
	public int getUserVersionMajor();

	/**
	 * Get the minor user version
	 *
	 * @return minor user version
	 * @since 1.2.1
	 */
	public int getUserVersionMinor();

	/**
	 * Get the patch user version
	 *
	 * @return patch user version
	 * @since 1.2.1
	 */
	public int getUserVersionPatch();

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
	 * Get the tables for the contents data type
	 * 
	 * @param type
	 *            data type
	 * @return table names
	 * @since 3.0.1
	 */
	public List<String> getTables(String type);

	/**
	 * Get the feature and tile tables
	 * 
	 * @return table names
	 * @since 1.2.1
	 */
	public List<String> getFeatureAndTileTables();

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
	 * @param type
	 *            data type
	 * @param table
	 *            table name
	 * @return true if the type of table
	 * @since 1.2.1
	 */
	public boolean isTableType(ContentsDataType type, String table);

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
	public boolean isTableType(String type, String table);

	/**
	 * Check if the table exists as a feature or tile table
	 * 
	 * @param table
	 *            table name
	 * @return true if a feature or tile table
	 * @since 1.1.7
	 */
	public boolean isFeatureOrTileTable(String table);

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
	 * Get a SQL/MM Spatial Reference System DAO
	 * 
	 * @return SQL/MM Spatial Reference System DAO
	 */
	public SpatialReferenceSystemSqlMmDao getSpatialReferenceSystemSqlMmDao();

	/**
	 * Get a SF/SQL Spatial Reference System DAO
	 * 
	 * @return SF/SQL Spatial Reference System DAO
	 */
	public SpatialReferenceSystemSfSqlDao getSpatialReferenceSystemSfSqlDao();

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
	 * Get a SQL/MM Geometry Columns DAO
	 * 
	 * @return SQL/MM Geometry Columns DAO
	 */
	public GeometryColumnsSqlMmDao getGeometryColumnsSqlMmDao();

	/**
	 * Get a SF/SQL Geometry Columns DAO
	 * 
	 * @return SF/SQL Geometry Columns DAO
	 */
	public GeometryColumnsSfSqlDao getGeometryColumnsSfSqlDao();

	/**
	 * Create the Geometry Columns table if it does not already exist
	 * 
	 * @return true if created
	 */
	public boolean createGeometryColumnsTable();

	/**
	 * Create a new feature table
	 * 
	 * @param table
	 *            feature table
	 */
	public void createFeatureTable(FeatureTable table);

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
	public GeometryColumns createFeatureTableWithMetadata(
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
	public GeometryColumns createFeatureTableWithMetadata(
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
	public GeometryColumns createFeatureTableWithMetadata(
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
	public GeometryColumns createFeatureTableWithMetadata(
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
	public GeometryColumns createFeatureTableWithMetadata(
			GeometryColumns geometryColumns, BoundingBox boundingBox,
			long srsId, List<FeatureColumn> columns);

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
	 * @param table
	 *            tile table
	 */
	public void createTileTable(TileTable table);

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
	public TileMatrixSet createTileTableWithMetadata(String tableName,
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
	public TileMatrixSet createTileTableWithMetadata(ContentsDataType dataType,
			String tableName, BoundingBox contentsBoundingBox,
			long contentsSrsId, BoundingBox tileMatrixSetBoundingBox,
			long tileMatrixSetSrsId);

	/**
	 * Get a Data Columns DAO
	 * 
	 * @return Data Columns DAO
	 */
	public DataColumnsDao getDataColumnsDao();

	/**
	 * Create the Data Columns table if it does not already exist
	 * 
	 * @return true if created
	 */
	public boolean createDataColumnsTable();

	/**
	 * Get a Data Column Constraints DAO
	 * 
	 * @return Data Column Constraints DAO
	 */
	public DataColumnConstraintsDao getDataColumnConstraintsDao();

	/**
	 * Create the Data Column Constraints table if it does not already exist
	 * 
	 * @return true if created
	 */
	public boolean createDataColumnConstraintsTable();

	/**
	 * Get a Metadata DAO
	 * 
	 * @return Metadata DAO
	 */
	public MetadataDao getMetadataDao();

	/**
	 * Create the Metadata table if it does not already exist
	 * 
	 * @return true if created
	 */
	public boolean createMetadataTable();

	/**
	 * Get a Metadata Reference DAO
	 * 
	 * @return Metadata Reference DAO
	 */
	public MetadataReferenceDao getMetadataReferenceDao();

	/**
	 * Create the Metadata Reference table if it does not already exist
	 * 
	 * @return true if created
	 */
	public boolean createMetadataReferenceTable();

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
	 * @since 3.5.1
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
	 * Get a 2D Gridded Coverage DAO
	 * 
	 * @return 2d gridded coverage dao
	 * @since 1.2.1
	 */
	public GriddedCoverageDao getGriddedCoverageDao();

	/**
	 * Create the 2D Gridded Coverage Table if it does not exist
	 * 
	 * @return true if created
	 * @since 1.2.1
	 */
	public boolean createGriddedCoverageTable();

	/**
	 * Get a 2D Gridded Tile DAO
	 * 
	 * @return 2d gridded tile dao
	 * @since 1.2.1
	 */
	public GriddedTileDao getGriddedTileDao();

	/**
	 * Create the 2D Gridded Tile Table if it does not exist
	 * 
	 * @return true if created
	 * @since 1.2.1
	 */
	public boolean createGriddedTileTable();

	/**
	 * Get a Table Index DAO
	 * 
	 * @return table index dao
	 * @since 1.1.0
	 */
	public TableIndexDao getTableIndexDao();

	/**
	 * Create the Table Index Table if it does not exist
	 * 
	 * @return true if created
	 * @since 1.1.0
	 */
	public boolean createTableIndexTable();

	/**
	 * Get a Geometry Index DAO
	 * 
	 * @return geometry index dao
	 * @since 1.1.0
	 */
	public GeometryIndexDao getGeometryIndexDao();

	/**
	 * Create Geometry Index Table if it does not exist and index it
	 * 
	 * @return true if created
	 * @since 1.1.0
	 */
	public boolean createGeometryIndexTable();

	/**
	 * Index the Geometry Index Table if needed
	 * 
	 * @return true if indexed
	 * @since 3.1.0
	 */
	public boolean indexGeometryIndexTable();

	/**
	 * Un-index the Geometry Index Table if needed
	 * 
	 * @return true if unindexed
	 * @since 3.1.0
	 */
	public boolean unindexGeometryIndexTable();

	/**
	 * Get a Feature Tile Link DAO
	 * 
	 * @return feature tile link dao
	 * @since 1.1.5
	 */
	public FeatureTileLinkDao getFeatureTileLinkDao();

	/**
	 * Create the Feature Tile Link Table if it does not exist
	 * 
	 * @return true if created
	 * @since 1.1.5
	 */
	public boolean createFeatureTileLinkTable();

	/**
	 * Create a new attributes table (only the attributes table is created, no
	 * Contents entry is created)
	 * 
	 * @param table
	 *            attributes table
	 * @since 1.2.1
	 */
	public void createAttributesTable(AttributesTable table);

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
	public AttributesTable createAttributesTableWithId(String tableName,
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
	public AttributesTable createAttributesTableWithId(String tableName,
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
	public AttributesTable createAttributesTable(String tableName,
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
	public AttributesTable createAttributesTable(String tableName,
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
	public AttributesTable createAttributesTable(String tableName,
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
	public AttributesTable createAttributesTable(String tableName,
			List<AttributesColumn> columns, Collection<Constraint> constraints);

	/**
	 * Get a Tile Scaling DAO
	 * 
	 * @return tile scaling dao
	 * @since 2.0.2
	 */
	public TileScalingDao getTileScalingDao();

	/**
	 * Create the Tile Scaling Table if it does not exist
	 * 
	 * @return true if created
	 * @since 2.0.2
	 */
	public boolean createTileScalingTable();

	/**
	 * Get a Extended Relations DAO
	 * 
	 * @return extended relations dao
	 * @since 3.0.1
	 */
	public ExtendedRelationsDao getExtendedRelationsDao();

	/**
	 * Create the Extended Relations Table if it does not exist
	 * 
	 * @return true if created
	 * @since 3.0.1
	 */
	public boolean createExtendedRelationsTable();

	/**
	 * Get a Contents Id DAO
	 * 
	 * @return contents id dao
	 * @since 3.2.0
	 */
	public ContentsIdDao getContentsIdDao();

	/**
	 * Create the Contents Id Table if it does not exist
	 * 
	 * @return true if created
	 * @since 3.2.0
	 */
	public boolean createContentsIdTable();

	/**
	 * Create a new user table
	 * 
	 * @param table
	 *            user table
	 * @since 3.0.1
	 */
	public void createUserTable(UserTable<? extends UserColumn> table);

	/**
	 * Get the Vector Tiles Layers DAO
	 * 
	 * @return vector tiles layers dao
	 * @since 3.5.1
	 */
	public VectorTilesLayersDao getVectorTilesLayersDao();

	/**
	 * Get the Vector Tiles Fields DAO
	 * 
	 * @return vector tiles fields dao
	 * @since 3.5.1
	 */
	public VectorTilesFieldsDao getVectorTilesFieldsDao();

	/**
	 * Get the Portrayal Extension Styles DAO
	 * 
	 * @return styles dao
	 * @since 3.5.1
	 */
	public StylesDao getStylesDao();

	/**
	 * Get the Portrayal Extension Stylesheets DAO
	 * 
	 * @return stylesheets dao
	 * @since 3.5.1
	 */
	public StylesheetsDao getStylesheetsDao();

	/**
	 * Get the Portrayal Extension Symbol Content DAO
	 * 
	 * @return symbol content dao
	 * @since 3.5.1
	 */
	public SymbolContentDao getSymbolContentDao();

	/**
	 * Get the Portrayal Extension Symbol Images DAO
	 * 
	 * @return symbol images dao
	 * @since 3.5.1
	 */
	public SymbolImagesDao getSymbolImagesDao();

	/**
	 * Get the Portrayal Extension Symbols DAO
	 * 
	 * @return symbols dao
	 * @since 3.5.1
	 */
	public SymbolsDao getSymbolsDao();

	/**
	 * Create the Vector Tiles Extension tables if they do not exist
	 *
	 * @return true if any table is created
	 * @since 3.5.1
	 */
	public boolean createVectorTilesTables();

	/**
	 * Create the Portrayal Extension tables if they do not exist
	 *
	 * @return true if any table is created
	 * @since 3.5.1
	 */
	public boolean createPortrayalTables();

}
