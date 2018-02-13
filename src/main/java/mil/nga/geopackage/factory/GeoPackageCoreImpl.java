package mil.nga.geopackage.factory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import mil.nga.geopackage.BoundingBox;
import mil.nga.geopackage.GeoPackageCore;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.attributes.AttributesColumn;
import mil.nga.geopackage.attributes.AttributesTable;
import mil.nga.geopackage.core.contents.Contents;
import mil.nga.geopackage.core.contents.ContentsDao;
import mil.nga.geopackage.core.contents.ContentsDataType;
import mil.nga.geopackage.core.srs.SpatialReferenceSystem;
import mil.nga.geopackage.core.srs.SpatialReferenceSystemDao;
import mil.nga.geopackage.core.srs.SpatialReferenceSystemSfSql;
import mil.nga.geopackage.core.srs.SpatialReferenceSystemSfSqlDao;
import mil.nga.geopackage.core.srs.SpatialReferenceSystemSqlMm;
import mil.nga.geopackage.core.srs.SpatialReferenceSystemSqlMmDao;
import mil.nga.geopackage.db.GeoPackageCoreConnection;
import mil.nga.geopackage.db.GeoPackageTableCreator;
import mil.nga.geopackage.extension.CrsWktExtension;
import mil.nga.geopackage.extension.Extensions;
import mil.nga.geopackage.extension.ExtensionsDao;
import mil.nga.geopackage.extension.GeoPackageExtensions;
import mil.nga.geopackage.extension.MetadataExtension;
import mil.nga.geopackage.extension.SchemaExtension;
import mil.nga.geopackage.extension.coverage.GriddedCoverage;
import mil.nga.geopackage.extension.coverage.GriddedCoverageDao;
import mil.nga.geopackage.extension.coverage.GriddedTile;
import mil.nga.geopackage.extension.coverage.GriddedTileDao;
import mil.nga.geopackage.extension.index.GeometryIndex;
import mil.nga.geopackage.extension.index.GeometryIndexDao;
import mil.nga.geopackage.extension.index.TableIndex;
import mil.nga.geopackage.extension.index.TableIndexDao;
import mil.nga.geopackage.extension.link.FeatureTileLink;
import mil.nga.geopackage.extension.link.FeatureTileLinkDao;
import mil.nga.geopackage.features.columns.GeometryColumns;
import mil.nga.geopackage.features.columns.GeometryColumnsDao;
import mil.nga.geopackage.features.columns.GeometryColumnsSfSql;
import mil.nga.geopackage.features.columns.GeometryColumnsSfSqlDao;
import mil.nga.geopackage.features.columns.GeometryColumnsSqlMm;
import mil.nga.geopackage.features.columns.GeometryColumnsSqlMmDao;
import mil.nga.geopackage.features.user.FeatureColumn;
import mil.nga.geopackage.features.user.FeatureTable;
import mil.nga.geopackage.metadata.Metadata;
import mil.nga.geopackage.metadata.MetadataDao;
import mil.nga.geopackage.metadata.reference.MetadataReference;
import mil.nga.geopackage.metadata.reference.MetadataReferenceDao;
import mil.nga.geopackage.schema.columns.DataColumns;
import mil.nga.geopackage.schema.columns.DataColumnsDao;
import mil.nga.geopackage.schema.constraints.DataColumnConstraints;
import mil.nga.geopackage.schema.constraints.DataColumnConstraintsDao;
import mil.nga.geopackage.tiles.matrix.TileMatrix;
import mil.nga.geopackage.tiles.matrix.TileMatrixDao;
import mil.nga.geopackage.tiles.matrixset.TileMatrixSet;
import mil.nga.geopackage.tiles.matrixset.TileMatrixSetDao;
import mil.nga.geopackage.tiles.user.TileColumn;
import mil.nga.geopackage.tiles.user.TileTable;

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
	protected final boolean writable;

	/**
	 * Constructor
	 *
	 * @param name
	 *            name
	 * @param path
	 *            path
	 * @param database
	 *            database
	 * @param tableCreator
	 *            table creator
	 * @param writable
	 *            true if writable
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
	public String getApplicationId() {
		return database.getApplicationId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getUserVersion() {
		return database.getUserVersion();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getUserVersionMajor() {
		return getUserVersion() / 10000;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getUserVersionMinor() {
		return (getUserVersion() % 10000) / 100;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getUserVersionPatch() {
		return getUserVersion() % 100;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<String> getFeatureTables() {
		List<String> tableNames = getTables(ContentsDataType.FEATURES);
		return tableNames;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<String> getTileTables() {
		List<String> tableNames = getTables(ContentsDataType.TILES);
		return tableNames;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<String> getAttributesTables() {
		List<String> tableNames = getTables(ContentsDataType.ATTRIBUTES);
		return tableNames;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<String> getTables(ContentsDataType type) {
		ContentsDao contentDao = getContentsDao();
		List<String> tableNames;
		try {
			tableNames = contentDao.getTables(type);
		} catch (SQLException e) {
			throw new GeoPackageException("Failed to retrieve "
					+ type.getName() + " tables", e);
		}
		return tableNames;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<String> getFeatureAndTileTables() {
		List<String> tables = new ArrayList<String>();
		tables.addAll(getFeatureTables());
		tables.addAll(getTileTables());
		return tables;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<String> getTables() {
		ContentsDao contentDao = getContentsDao();
		List<String> tables;
		try {
			tables = contentDao.getTables();
		} catch (SQLException e) {
			throw new GeoPackageException("Failed to retrieve tables", e);
		}
		return tables;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isFeatureTable(String table) {
		Set<String> featureTables = new HashSet<String>(getFeatureTables());
		return featureTables.contains(table);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isTileTable(String table) {
		Set<String> tileTables = new HashSet<String>(getTileTables());
		return tileTables.contains(table);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isTableType(ContentsDataType type, String table) {
		Set<String> tables = new HashSet<String>(getTables(type));
		return tables.contains(table);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isFeatureOrTileTable(String table) {
		Set<String> tables = new HashSet<String>(getFeatureAndTileTables());
		return tables.contains(table);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isTable(String table) {
		Set<String> tables = new HashSet<String>(getTables());
		return tables.contains(table);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SpatialReferenceSystemDao getSpatialReferenceSystemDao() {
		SpatialReferenceSystemDao dao = createDao(SpatialReferenceSystem.class);
		dao.setCrsWktExtension(new CrsWktExtension(this));
		return dao;
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
		return createFeatureTableWithMetadata(geometryColumns, null, null,
				boundingBox, srsId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public GeometryColumns createFeatureTableWithMetadata(
			GeometryColumns geometryColumns, String idColumnName,
			BoundingBox boundingBox, long srsId) {
		return createFeatureTableWithMetadata(geometryColumns, idColumnName,
				null, boundingBox, srsId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public GeometryColumns createFeatureTableWithMetadata(
			GeometryColumns geometryColumns,
			List<FeatureColumn> additionalColumns, BoundingBox boundingBox,
			long srsId) {
		return createFeatureTableWithMetadata(geometryColumns, null,
				additionalColumns, boundingBox, srsId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public GeometryColumns createFeatureTableWithMetadata(
			GeometryColumns geometryColumns, String idColumnName,
			List<FeatureColumn> additionalColumns, BoundingBox boundingBox,
			long srsId) {

		if (idColumnName == null) {
			idColumnName = "id";
		}

		List<FeatureColumn> columns = new ArrayList<FeatureColumn>();
		columns.add(FeatureColumn.createPrimaryKeyColumn(0, idColumnName));
		columns.add(FeatureColumn.createGeometryColumn(1,
				geometryColumns.getColumnName(),
				geometryColumns.getGeometryType(), false, null));

		if (additionalColumns != null) {
			columns.addAll(additionalColumns);
		}

		return createFeatureTableWithMetadata(geometryColumns, boundingBox,
				srsId, columns);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public GeometryColumns createFeatureTableWithMetadata(
			GeometryColumns geometryColumns, BoundingBox boundingBox,
			long srsId, List<FeatureColumn> columns) {

		// Get the SRS
		SpatialReferenceSystem srs = getSrs(srsId);

		// Create the Geometry Columns table
		createGeometryColumnsTable();

		// Create the user feature table
		FeatureTable table = new FeatureTable(geometryColumns.getTableName(),
				columns);
		createFeatureTable(table);

		try {
			// Create the contents
			Contents contents = new Contents();
			contents.setTableName(geometryColumns.getTableName());
			contents.setDataType(ContentsDataType.FEATURES);
			contents.setIdentifier(geometryColumns.getTableName());
			// contents.setLastChange(new Date());
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
		return createTileTableWithMetadata(ContentsDataType.TILES, tableName,
				contentsBoundingBox, contentsSrsId, tileMatrixSetBoundingBox,
				tileMatrixSetSrsId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TileMatrixSet createTileTableWithMetadata(ContentsDataType dataType,
			String tableName, BoundingBox contentsBoundingBox,
			long contentsSrsId, BoundingBox tileMatrixSetBoundingBox,
			long tileMatrixSetSrsId) {

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
			contents.setDataType(dataType);
			contents.setIdentifier(tableName);
			// contents.setLastChange(new Date());
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
				if (created) {
					// Create the schema extension record
					SchemaExtension schemaExtension = new SchemaExtension(this);
					schemaExtension.getOrCreate();
				}
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
				if (created) {
					// Create the metadata extension record
					MetadataExtension metadataExtension = new MetadataExtension(
							this);
					metadataExtension.getOrCreate();
				}
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

		GeoPackageExtensions.deleteTableExtensions(this, table);

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
	 * {@inheritDoc}
	 */
	@Override
	public <T, S extends BaseDaoImpl<T, ?>> S createDao(Class<T> type) {
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void dropTable(String table) {
		tableCreator.dropTable(table);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public GriddedCoverageDao getGriddedCoverageDao() {
		return createDao(GriddedCoverage.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean createGriddedCoverageTable() {
		verifyWritable();

		boolean created = false;
		GriddedCoverageDao dao = getGriddedCoverageDao();
		try {
			if (!dao.isTableExists()) {
				created = tableCreator.createGriddedCoverage() > 0;
			}
		} catch (SQLException e) {
			throw new GeoPackageException("Failed to check if "
					+ GriddedCoverage.class.getSimpleName()
					+ " table exists and create it", e);
		}
		return created;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public GriddedTileDao getGriddedTileDao() {
		return createDao(GriddedTile.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean createGriddedTileTable() {
		verifyWritable();

		boolean created = false;
		GriddedTileDao dao = getGriddedTileDao();
		try {
			if (!dao.isTableExists()) {
				created = tableCreator.createGriddedTile() > 0;
			}
		} catch (SQLException e) {
			throw new GeoPackageException("Failed to check if "
					+ GriddedTile.class.getSimpleName()
					+ " table exists and create it", e);
		}
		return created;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TableIndexDao getTableIndexDao() {
		return createDao(TableIndex.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean createTableIndexTable() {
		verifyWritable();

		boolean created = false;
		TableIndexDao dao = getTableIndexDao();
		try {
			if (!dao.isTableExists()) {
				created = tableCreator.createTableIndex() > 0;
			}
		} catch (SQLException e) {
			throw new GeoPackageException("Failed to check if "
					+ TableIndex.class.getSimpleName()
					+ " table exists and create it", e);
		}
		return created;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public GeometryIndexDao getGeometryIndexDao() {
		return createDao(GeometryIndex.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean createGeometryIndexTable() {
		verifyWritable();

		boolean created = false;
		GeometryIndexDao dao = getGeometryIndexDao();
		try {
			if (!dao.isTableExists()) {
				created = tableCreator.createGeometryIndex() > 0;
			}
		} catch (SQLException e) {
			throw new GeoPackageException("Failed to check if "
					+ GeometryIndex.class.getSimpleName()
					+ " table exists and create it", e);
		}
		return created;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FeatureTileLinkDao getFeatureTileLinkDao() {
		return createDao(FeatureTileLink.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean createFeatureTileLinkTable() {
		verifyWritable();

		boolean created = false;
		FeatureTileLinkDao dao = getFeatureTileLinkDao();
		try {
			if (!dao.isTableExists()) {
				created = tableCreator.createFeatureTileLink() > 0;
			}
		} catch (SQLException e) {
			throw new GeoPackageException("Failed to check if "
					+ FeatureTileLink.class.getSimpleName()
					+ " table exists and create it", e);
		}
		return created;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void createAttributesTable(AttributesTable table) {
		verifyWritable();

		tableCreator.createTable(table);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AttributesTable createAttributesTableWithId(String tableName,
			List<AttributesColumn> additionalColumns) {
		return createAttributesTable(tableName, null, additionalColumns);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AttributesTable createAttributesTable(String tableName,
			String idColumnName, List<AttributesColumn> additionalColumns) {

		if (idColumnName == null) {
			idColumnName = "id";
		}

		List<AttributesColumn> columns = new ArrayList<AttributesColumn>();
		columns.add(AttributesColumn.createPrimaryKeyColumn(0, idColumnName));

		if (additionalColumns != null) {
			columns.addAll(additionalColumns);
		}

		return createAttributesTable(tableName, columns);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AttributesTable createAttributesTable(String tableName,
			List<AttributesColumn> columns) {

		// Create the user attributes table
		AttributesTable table = new AttributesTable(tableName, columns);
		createAttributesTable(table);

		try {
			// Create the contents
			Contents contents = new Contents();
			contents.setTableName(tableName);
			contents.setDataType(ContentsDataType.ATTRIBUTES);
			contents.setIdentifier(tableName);
			// contents.setLastChange(new Date());
			getContentsDao().create(contents);

			table.setContents(contents);

		} catch (RuntimeException e) {
			deleteTableQuietly(tableName);
			throw e;
		} catch (SQLException e) {
			deleteTableQuietly(tableName);
			throw new GeoPackageException(
					"Failed to create table and metadata: " + tableName, e);
		}

		return table;
	}

}
