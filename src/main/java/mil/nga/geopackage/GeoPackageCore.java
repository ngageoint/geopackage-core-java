package mil.nga.geopackage;

import java.io.Closeable;
import java.util.List;

import mil.nga.geopackage.core.contents.ContentsDao;
import mil.nga.geopackage.core.srs.SpatialReferenceSystemDao;
import mil.nga.geopackage.core.srs.SpatialReferenceSystemSfSqlDao;
import mil.nga.geopackage.core.srs.SpatialReferenceSystemSqlMmDao;
import mil.nga.geopackage.db.GeoPackageCoreConnection;
import mil.nga.geopackage.extension.ExtensionsDao;
import mil.nga.geopackage.extension.index.GeometryIndexDao;
import mil.nga.geopackage.extension.index.TableIndexDao;
import mil.nga.geopackage.extension.link.FeatureTileLinkDao;
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

import com.j256.ormlite.dao.BaseDaoImpl;

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
	 * @return
	 */
	public String getName();

	/**
	 * Get the GeoPackage path
	 * 
	 * @return
	 */
	public String getPath();

	/**
	 * Get the SQLite database
	 * 
	 * @return
	 */
	public GeoPackageCoreConnection getDatabase();

	/**
	 * Is the GeoPackage writable
	 * 
	 * @return
	 */
	public boolean isWritable();

	/**
	 * Get the feature tables
	 * 
	 * @return
	 */
	public List<String> getFeatureTables();

	/**
	 * Get the tile tables
	 * 
	 * @return
	 */
	public List<String> getTileTables();

	/**
	 * Get a Spatial Reference System DAO
	 * 
	 * @return
	 */
	public SpatialReferenceSystemDao getSpatialReferenceSystemDao();

	/**
	 * Get a SQL/MM Spatial Reference System DAO
	 * 
	 * @return
	 */
	public SpatialReferenceSystemSqlMmDao getSpatialReferenceSystemSqlMmDao();

	/**
	 * Get a SF/SQL Spatial Reference System DAO
	 * 
	 * @return
	 */
	public SpatialReferenceSystemSfSqlDao getSpatialReferenceSystemSfSqlDao();

	/**
	 * Get a Contents DAO
	 * 
	 * @return
	 */
	public ContentsDao getContentsDao();

	/**
	 * Get a Geometry Columns DAO
	 * 
	 * @return
	 */
	public GeometryColumnsDao getGeometryColumnsDao();

	/**
	 * Get a SQL/MM Geometry Columns DAO
	 * 
	 * @return
	 */
	public GeometryColumnsSqlMmDao getGeometryColumnsSqlMmDao();

	/**
	 * Get a SF/SQL Geometry Columns DAO
	 * 
	 * @return
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
			GeometryColumns geometryColumns, BoundingBox boundingBox, long srsId);

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
	 * @return
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
	 * @return
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
	 */
	public void createTileTable(TileTable table);

	/**
	 * Create a new tile table and the GeoPackage metadata
	 * 
	 * @param tableName
	 * @param contentsBoundingBox
	 * @param contentsSrsId
	 * @param tileMatrixSetBoundingBox
	 * @param tileMatrixSetSrsId
	 * @return tile matrix set
	 */
	public TileMatrixSet createTileTableWithMetadata(String tableName,
			BoundingBox contentsBoundingBox, long contentsSrsId,
			BoundingBox tileMatrixSetBoundingBox, long tileMatrixSetSrsId);

	/**
	 * Get a Data Columns DAO
	 * 
	 * @return
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
	 * @return
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
	 * @return
	 */
	public MetadataDao getMetadataDao();

	/**
	 * Create the Metadata table if it does not already exist
	 * 
	 * @return
	 */
	public boolean createMetadataTable();

	/**
	 * Get a Metadata Reference DAO
	 * 
	 * @return
	 */
	public MetadataReferenceDao getMetadataReferenceDao();

	/**
	 * Create the Metadata Reference table if it does not already exist
	 * 
	 * @return
	 */
	public boolean createMetadataReferenceTable();

	/**
	 * Get an Extensions DAO
	 * 
	 * @return
	 */
	public ExtensionsDao getExtensionsDao();

	/**
	 * Create the Extensions table if it does not already exist
	 * 
	 * @return
	 */
	public boolean createExtensionsTable();

	/**
	 * Delete the user table (a feature or tile table) and all GeoPackage
	 * metadata
	 * 
	 * @param table
	 */
	public void deleteTable(String table);

	/**
	 * Attempt to delete the user table (a feature or tile table) and all
	 * GeoPackage metadata quietly
	 * 
	 * @param tableName
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
	 * @return base dao implementation
	 * @since 1.1.0
	 */
	public <T, S extends BaseDaoImpl<T, ?>> S createDao(Class<T> type);

	/**
	 * Execute the sql on the GeoPackage database
	 * 
	 * @param sql
	 * @since 1.1.2
	 */
	public void execSQL(String sql);

	/**
	 * Drop the table if it exists. Drops the table with the table name, not
	 * limited to GeoPackage specific tables.
	 * 
	 * @param table
	 * @since 1.1.5
	 */
	public void dropTable(String table);

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
	 * Create Geometry Index Table if it does not exist
	 * 
	 * @return true if created
	 * @since 1.1.0
	 */
	public boolean createGeometryIndexTable();

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

}
