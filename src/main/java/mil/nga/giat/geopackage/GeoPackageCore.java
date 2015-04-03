package mil.nga.giat.geopackage;

import java.io.Closeable;
import java.util.List;

import mil.nga.giat.geopackage.core.contents.ContentsDao;
import mil.nga.giat.geopackage.core.srs.SpatialReferenceSystemDao;
import mil.nga.giat.geopackage.core.srs.SpatialReferenceSystemSfSqlDao;
import mil.nga.giat.geopackage.core.srs.SpatialReferenceSystemSqlMmDao;
import mil.nga.giat.geopackage.db.GeoPackageCoreConnection;
import mil.nga.giat.geopackage.extension.ExtensionsDao;
import mil.nga.giat.geopackage.features.columns.GeometryColumns;
import mil.nga.giat.geopackage.features.columns.GeometryColumnsDao;
import mil.nga.giat.geopackage.features.columns.GeometryColumnsSfSqlDao;
import mil.nga.giat.geopackage.features.columns.GeometryColumnsSqlMmDao;
import mil.nga.giat.geopackage.features.user.FeatureTable;
import mil.nga.giat.geopackage.metadata.MetadataDao;
import mil.nga.giat.geopackage.metadata.reference.MetadataReferenceDao;
import mil.nga.giat.geopackage.schema.columns.DataColumnsDao;
import mil.nga.giat.geopackage.schema.constraints.DataColumnConstraintsDao;
import mil.nga.giat.geopackage.tiles.matrix.TileMatrixDao;
import mil.nga.giat.geopackage.tiles.matrixset.TileMatrixSet;
import mil.nga.giat.geopackage.tiles.matrixset.TileMatrixSetDao;
import mil.nga.giat.geopackage.tiles.user.TileTable;

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
	 * Get the SQLite database
	 * 
	 * @return
	 */
	public GeoPackageCoreConnection getDatabase();

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
	 * Create a new feature table with GeoPackage metadata
	 * 
	 * @param geometryColumns
	 * @param boundingBox
	 * @param srsId
	 * @return
	 */
	public GeometryColumns createFeatureTableWithMetadata(
			GeometryColumns geometryColumns, BoundingBox boundingBox, long srsId);

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
	 * Delete the user table and all GeoPackage metadata
	 * 
	 * @param table
	 */
	public void deleteTable(String table);

	/**
	 * Attempt to delete the user table and all GeoPackage metadata quietly
	 * 
	 * @param tableName
	 */
	public void deleteTableQuietly(String tableName);

}
