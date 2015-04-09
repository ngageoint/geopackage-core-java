package mil.nga.giat.geopackage.factory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mil.nga.giat.geopackage.BoundingBox;
import mil.nga.giat.geopackage.GeoPackageCore;
import mil.nga.giat.geopackage.GeoPackageException;
import mil.nga.giat.geopackage.core.contents.Contents;
import mil.nga.giat.geopackage.core.contents.ContentsDao;
import mil.nga.giat.geopackage.core.contents.ContentsDataType;
import mil.nga.giat.geopackage.core.srs.SpatialReferenceSystem;
import mil.nga.giat.geopackage.core.srs.SpatialReferenceSystemDao;
import mil.nga.giat.geopackage.core.srs.SpatialReferenceSystemSfSql;
import mil.nga.giat.geopackage.core.srs.SpatialReferenceSystemSfSqlDao;
import mil.nga.giat.geopackage.core.srs.SpatialReferenceSystemSqlMm;
import mil.nga.giat.geopackage.core.srs.SpatialReferenceSystemSqlMmDao;
import mil.nga.giat.geopackage.db.GeoPackageCoreConnection;
import mil.nga.giat.geopackage.db.GeoPackageTableCreator;
import mil.nga.giat.geopackage.extension.Extensions;
import mil.nga.giat.geopackage.extension.ExtensionsDao;
import mil.nga.giat.geopackage.features.columns.GeometryColumns;
import mil.nga.giat.geopackage.features.columns.GeometryColumnsDao;
import mil.nga.giat.geopackage.features.columns.GeometryColumnsSfSql;
import mil.nga.giat.geopackage.features.columns.GeometryColumnsSfSqlDao;
import mil.nga.giat.geopackage.features.columns.GeometryColumnsSqlMm;
import mil.nga.giat.geopackage.features.columns.GeometryColumnsSqlMmDao;
import mil.nga.giat.geopackage.features.user.FeatureColumn;
import mil.nga.giat.geopackage.features.user.FeatureTable;
import mil.nga.giat.geopackage.metadata.Metadata;
import mil.nga.giat.geopackage.metadata.MetadataDao;
import mil.nga.giat.geopackage.metadata.reference.MetadataReference;
import mil.nga.giat.geopackage.metadata.reference.MetadataReferenceDao;
import mil.nga.giat.geopackage.schema.columns.DataColumns;
import mil.nga.giat.geopackage.schema.columns.DataColumnsDao;
import mil.nga.giat.geopackage.schema.constraints.DataColumnConstraints;
import mil.nga.giat.geopackage.schema.constraints.DataColumnConstraintsDao;
import mil.nga.giat.geopackage.tiles.matrix.TileMatrix;
import mil.nga.giat.geopackage.tiles.matrix.TileMatrixDao;
import mil.nga.giat.geopackage.tiles.matrixset.TileMatrixSet;
import mil.nga.giat.geopackage.tiles.matrixset.TileMatrixSetDao;
import mil.nga.giat.geopackage.tiles.user.TileColumn;
import mil.nga.giat.geopackage.tiles.user.TileTable;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.dao.DaoManager;

/**
 * A single GeoPackage database connection implementation
 *
 * @author osbornb
 */
public abstract class GeoPackageCoreImpl implements GeoPackageCore {

	/**
	 * GeoPackage name
	 */
	private final String name;

	/**
	 * GeoPackage file path
	 */
	private final String path;

	/**
	 * SQLite database
	 */
	private final GeoPackageCoreConnection database;

	/**
	 * Table creator
	 */
	private final GeoPackageTableCreator tableCreator;

	/**
	 * Writable GeoPackage flag
	 */
	private final boolean writable;

	/**
	 * Constructor
	 *
	 * @param name
	 * @param path
	 * @param database
	 * @param connectionSource
	 * @param tableCreator
	 * @param writable
	 */
	protected GeoPackageCoreImpl(String name, String path,
			GeoPackageCoreConnection database,
			GeoPackageTableCreator tableCreator, boolean writable) {
		this.name = name;
		this.path = path;
		this.database = database;
		this.tableCreator = tableCreator;
		this.writable = writable;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void close() {
		database.close();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getPath() {
		return path;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public GeoPackageCoreConnection getDatabase() {
		return database;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isWritable() {
		return writable;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<String> getFeatureTables() {
		GeometryColumnsDao geometryColumnsDao = getGeometryColumnsDao();
		List<String> tableNames = null;
		try {
			if (geometryColumnsDao.isTableExists()) {
				tableNames = geometryColumnsDao.getFeatureTables();
			}
		} catch (SQLException e) {
			throw new GeoPackageException("Failed to retrieve feature tables",
					e);
		}
		if (tableNames == null) {
			tableNames = new ArrayList<String>();
		}
		return tableNames;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<String> getTileTables() {
		TileMatrixSetDao tileMatrixSetDao = getTileMatrixSetDao();
		List<String> tableNames = null;
		try {
			if (tileMatrixSetDao.isTableExists()) {
				tableNames = tileMatrixSetDao.getTileTables();
			}
		} catch (SQLException e) {
			throw new GeoPackageException("Failed to retrieve tile tables", e);
		}
		if (tableNames == null) {
			tableNames = new ArrayList<String>();
		}
		return tableNames;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SpatialReferenceSystemDao getSpatialReferenceSystemDao() {
		return createDao(SpatialReferenceSystem.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SpatialReferenceSystemSqlMmDao getSpatialReferenceSystemSqlMmDao() {

		SpatialReferenceSystemSqlMmDao dao = createDao(SpatialReferenceSystemSqlMm.class);
		verifyTableExists(dao);

		return dao;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SpatialReferenceSystemSfSqlDao getSpatialReferenceSystemSfSqlDao() {

		SpatialReferenceSystemSfSqlDao dao = createDao(SpatialReferenceSystemSfSql.class);
		verifyTableExists(dao);

		return dao;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ContentsDao getContentsDao() {
		ContentsDao dao = createDao(Contents.class);
		dao.setDatabase(database);
		return dao;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public GeometryColumnsDao getGeometryColumnsDao() {
		return createDao(GeometryColumns.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public GeometryColumnsSqlMmDao getGeometryColumnsSqlMmDao() {

		GeometryColumnsSqlMmDao dao = createDao(GeometryColumnsSqlMm.class);
		verifyTableExists(dao);

		return dao;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public GeometryColumnsSfSqlDao getGeometryColumnsSfSqlDao() {

		GeometryColumnsSfSqlDao dao = createDao(GeometryColumnsSfSql.class);
		verifyTableExists(dao);

		return dao;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean createGeometryColumnsTable() {
		verifyWritable();

		boolean created = false;
		GeometryColumnsDao dao = getGeometryColumnsDao();
		try {
			if (!dao.isTableExists()) {
				created = tableCreator.createGeometryColumns() > 0;
			}
		} catch (SQLException e) {
			throw new GeoPackageException("Failed to check if "
					+ GeometryColumns.class.getSimpleName()
					+ " table exists and create it", e);
		}
		return created;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void createFeatureTable(FeatureTable table) {
		verifyWritable();

		tableCreator.createTable(table);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public GeometryColumns createFeatureTableWithMetadata(
			GeometryColumns geometryColumns, BoundingBox boundingBox, long srsId) {

		// Get the SRS
		SpatialReferenceSystem srs = getSrs(srsId);

		// Create the Geometry Columns table
		createGeometryColumnsTable();

		// Create the user feature table
		List<FeatureColumn> columns = new ArrayList<FeatureColumn>();
		columns.add(FeatureColumn.createPrimaryKeyColumn(0, "id"));
		columns.add(FeatureColumn.createGeometryColumn(1,
				geometryColumns.getColumnName(),
				geometryColumns.getGeometryType(), false, null));
		FeatureTable table = new FeatureTable(geometryColumns.getTableName(),
				columns);
		createFeatureTable(table);

		try {
			// Create the contents
			Contents contents = new Contents();
			contents.setTableName(geometryColumns.getTableName());
			contents.setDataType(ContentsDataType.FEATURES);
			contents.setIdentifier(geometryColumns.getTableName());
			contents.setLastChange(new Date());
			contents.setMinX(boundingBox.getMinLongitude());
			contents.setMinY(boundingBox.getMinLatitude());
			contents.setMaxX(boundingBox.getMaxLongitude());
			contents.setMaxY(boundingBox.getMaxLatitude());
			contents.setSrs(srs);
			getContentsDao().create(contents);

			// Create new geometry columns
			geometryColumns.setContents(contents);
			geometryColumns.setSrs(contents.getSrs());
			getGeometryColumnsDao().create(geometryColumns);

		} catch (RuntimeException e) {
			deleteTableQuietly(geometryColumns.getTableName());
			throw e;
		} catch (SQLException e) {
			deleteTableQuietly(geometryColumns.getTableName());
			throw new GeoPackageException(
					"Failed to create table and metadata: "
							+ geometryColumns.getTableName(), e);
		}

		return geometryColumns;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TileMatrixSetDao getTileMatrixSetDao() {
		return createDao(TileMatrixSet.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean createTileMatrixSetTable() {
		verifyWritable();

		boolean created = false;
		TileMatrixSetDao dao = getTileMatrixSetDao();
		try {
			if (!dao.isTableExists()) {
				created = tableCreator.createTileMatrixSet() > 0;
			}
		} catch (SQLException e) {
			throw new GeoPackageException("Failed to check if "
					+ TileMatrixSet.class.getSimpleName()
					+ " table exists and create it", e);
		}
		return created;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TileMatrixDao getTileMatrixDao() {
		return createDao(TileMatrix.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean createTileMatrixTable() {
		verifyWritable();

		boolean created = false;
		TileMatrixDao dao = getTileMatrixDao();
		try {
			if (!dao.isTableExists()) {
				created = tableCreator.createTileMatrix() > 0;
			}
		} catch (SQLException e) {
			throw new GeoPackageException("Failed to check if "
					+ TileMatrix.class.getSimpleName()
					+ " table exists and create it", e);
		}
		return created;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void createTileTable(TileTable table) {
		verifyWritable();

		tableCreator.createTable(table);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TileMatrixSet createTileTableWithMetadata(String tableName,
			BoundingBox contentsBoundingBox, long contentsSrsId,
			BoundingBox tileMatrixSetBoundingBox, long tileMatrixSetSrsId) {

		TileMatrixSet tileMatrixSet = null;

		// Get the SRS
		SpatialReferenceSystem contentsSrs = getSrs(contentsSrsId);
		SpatialReferenceSystem tileMatrixSetSrs = getSrs(tileMatrixSetSrsId);

		// Create the Tile Matrix Set and Tile Matrix tables
		createTileMatrixSetTable();
		createTileMatrixTable();

		// Create the user tile table
		List<TileColumn> columns = TileTable.createRequiredColumns();
		TileTable table = new TileTable(tableName, columns);
		createTileTable(table);

		try {
			// Create the contents
			Contents contents = new Contents();
			contents.setTableName(tableName);
			contents.setDataType(ContentsDataType.TILES);
			contents.setIdentifier(tableName);
			contents.setLastChange(new Date());
			contents.setMinX(contentsBoundingBox.getMinLongitude());
			contents.setMinY(contentsBoundingBox.getMinLatitude());
			contents.setMaxX(contentsBoundingBox.getMaxLongitude());
			contents.setMaxY(contentsBoundingBox.getMaxLatitude());
			contents.setSrs(contentsSrs);
			getContentsDao().create(contents);

			// Create new matrix tile set
			tileMatrixSet = new TileMatrixSet();
			tileMatrixSet.setContents(contents);
			tileMatrixSet.setSrs(tileMatrixSetSrs);
			tileMatrixSet.setMinX(tileMatrixSetBoundingBox.getMinLongitude());
			tileMatrixSet.setMinY(tileMatrixSetBoundingBox.getMinLatitude());
			tileMatrixSet.setMaxX(tileMatrixSetBoundingBox.getMaxLongitude());
			tileMatrixSet.setMaxY(tileMatrixSetBoundingBox.getMaxLatitude());
			getTileMatrixSetDao().create(tileMatrixSet);

		} catch (RuntimeException e) {
			deleteTableQuietly(tableName);
			throw e;
		} catch (SQLException e) {
			deleteTableQuietly(tableName);
			throw new GeoPackageException(
					"Failed to create table and metadata: " + tableName, e);
		}

		return tileMatrixSet;
	}

	/**
	 * Get the Spatial Reference System by id
	 *
	 * @param srsId
	 * @return
	 */
	private SpatialReferenceSystem getSrs(long srsId) {
		SpatialReferenceSystem srs;
		try {
			srs = getSpatialReferenceSystemDao().queryForId(srsId);
		} catch (SQLException e1) {
			throw new GeoPackageException(
					"Failed to retrieve Spatial Reference System. SRS ID: "
							+ srsId);
		}
		if (srs == null) {
			throw new GeoPackageException(
					"Spatial Reference System could not be found. SRS ID: "
							+ srsId);
		}
		return srs;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DataColumnsDao getDataColumnsDao() {
		return createDao(DataColumns.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean createDataColumnsTable() {
		verifyWritable();

		boolean created = false;
		DataColumnsDao dao = getDataColumnsDao();
		try {
			if (!dao.isTableExists()) {
				created = tableCreator.createDataColumns() > 0;
			}
		} catch (SQLException e) {
			throw new GeoPackageException("Failed to check if "
					+ DataColumns.class.getSimpleName()
					+ " table exists and create it", e);
		}
		return created;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DataColumnConstraintsDao getDataColumnConstraintsDao() {
		return createDao(DataColumnConstraints.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean createDataColumnConstraintsTable() {
		verifyWritable();

		boolean created = false;
		DataColumnConstraintsDao dao = getDataColumnConstraintsDao();
		try {
			if (!dao.isTableExists()) {
				created = tableCreator.createDataColumnConstraints() > 0;
			}
		} catch (SQLException e) {
			throw new GeoPackageException("Failed to check if "
					+ DataColumnConstraints.class.getSimpleName()
					+ " table exists and create it", e);
		}
		return created;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MetadataDao getMetadataDao() {
		return createDao(Metadata.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean createMetadataTable() {
		verifyWritable();

		boolean created = false;
		MetadataDao dao = getMetadataDao();
		try {
			if (!dao.isTableExists()) {
				created = tableCreator.createMetadata() > 0;
			}
		} catch (SQLException e) {
			throw new GeoPackageException("Failed to check if "
					+ Metadata.class.getSimpleName()
					+ " table exists and create it", e);
		}
		return created;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MetadataReferenceDao getMetadataReferenceDao() {
		return createDao(MetadataReference.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean createMetadataReferenceTable() {
		verifyWritable();

		boolean created = false;
		MetadataReferenceDao dao = getMetadataReferenceDao();
		try {
			if (!dao.isTableExists()) {
				created = tableCreator.createMetadataReference() > 0;
			}
		} catch (SQLException e) {
			throw new GeoPackageException("Failed to check if "
					+ MetadataReference.class.getSimpleName()
					+ " table exists and create it", e);
		}
		return created;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ExtensionsDao getExtensionsDao() {
		return createDao(Extensions.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean createExtensionsTable() {
		verifyWritable();

		boolean created = false;
		ExtensionsDao dao = getExtensionsDao();
		try {
			if (!dao.isTableExists()) {
				created = tableCreator.createExtensions() > 0;
			}
		} catch (SQLException e) {
			throw new GeoPackageException("Failed to check if "
					+ Extensions.class.getSimpleName()
					+ " table exists and create it", e);
		}
		return created;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteTable(String table) {
		verifyWritable();

		ContentsDao contentsDao = getContentsDao();
		contentsDao.deleteTable(table);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteTableQuietly(String tableName) {
		verifyWritable();

		try {
			deleteTable(tableName);
		} catch (Exception e) {
			// eat
		}
	}

	/**
	 * GeoPackages created with SQLite version 4.2.0+ with GeoPackage support
	 * can contain triggers and functions that are not supported in previous
	 * versions. In order to edit tables, these triggers need to be dropped. It
	 * might be possible to define the missing functions as an alternative. In
	 * Android, this would require SQLite C through NDK.
	 * 
	 * @param geometryColumns
	 */
	protected void dropSQLiteTriggers(GeometryColumns geometryColumns) {

		if (writable) {

			database.execSQL("DROP TRIGGER IF EXISTS rtree_"
					+ geometryColumns.getTableName() + "_"
					+ geometryColumns.getColumnName() + "_insert");
			database.execSQL("DROP TRIGGER IF EXISTS rtree_"
					+ geometryColumns.getTableName() + "_"
					+ geometryColumns.getColumnName() + "_update1");
			database.execSQL("DROP TRIGGER IF EXISTS rtree_"
					+ geometryColumns.getTableName() + "_"
					+ geometryColumns.getColumnName() + "_update2");
			database.execSQL("DROP TRIGGER IF EXISTS rtree_"
					+ geometryColumns.getTableName() + "_"
					+ geometryColumns.getColumnName() + "_update3");
			database.execSQL("DROP TRIGGER IF EXISTS rtree_"
					+ geometryColumns.getTableName() + "_"
					+ geometryColumns.getColumnName() + "_update4");
			database.execSQL("DROP TRIGGER IF EXISTS rtree_"
					+ geometryColumns.getTableName() + "_"
					+ geometryColumns.getColumnName() + "_delete");

		}
	}

	/**
	 * Create a dao
	 *
	 * @param type
	 * @return
	 */
	private <T, S extends BaseDaoImpl<T, ?>> S createDao(Class<T> type) {
		S dao;
		try {
			dao = DaoManager.createDao(database.getConnectionSource(), type);
		} catch (SQLException e) {
			throw new GeoPackageException("Failed to create "
					+ type.getSimpleName() + " dao", e);
		}
		return dao;
	}

	/**
	 * Verify table or view exists
	 *
	 * @param dao
	 */
	private void verifyTableExists(BaseDaoImpl<?, ?> dao) {
		try {
			if (!dao.isTableExists()) {
				throw new GeoPackageException(
						"Table or view does not exist for: "
								+ dao.getDataClass().getSimpleName());
			}
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to detect if table or view exists for dao: "
							+ dao.getDataClass().getSimpleName(), e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void verifyWritable() {
		if (!writable) {
			throw new GeoPackageException(
					"GeoPackage file is not writable. Name: " + name
							+ (path != null ? ", Path: " + path : ""));
		}
	}

}
