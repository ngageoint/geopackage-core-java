package mil.nga.geopackage.factory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.misc.TransactionManager;

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
import mil.nga.geopackage.db.AlterTable;
import mil.nga.geopackage.db.CoreSQLUtils;
import mil.nga.geopackage.db.GeoPackageCoreConnection;
import mil.nga.geopackage.db.GeoPackageDao;
import mil.nga.geopackage.db.GeoPackageTableCreator;
import mil.nga.geopackage.db.table.Constraint;
import mil.nga.geopackage.extension.CrsWktExtension;
import mil.nga.geopackage.extension.ExtensionManager;
import mil.nga.geopackage.extension.Extensions;
import mil.nga.geopackage.extension.ExtensionsDao;
import mil.nga.geopackage.extension.MetadataExtension;
import mil.nga.geopackage.extension.SchemaExtension;
import mil.nga.geopackage.extension.related.ExtendedRelation;
import mil.nga.geopackage.extension.related.ExtendedRelationsDao;
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
import mil.nga.geopackage.user.UserColumn;
import mil.nga.geopackage.user.UserTable;
import mil.nga.sf.proj.Projection;

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
	 * @since 4.0.0
	 */
	protected GeoPackageCoreImpl(String name, String path,
			GeoPackageCoreConnection database) {
		this(name, path, database, true);
	}

	/**
	 * Constructor
	 *
	 * @param name
	 *            name
	 * @param path
	 *            path
	 * @param database
	 *            database
	 * @param writable
	 *            true if writable
	 * @since 4.0.0
	 */
	protected GeoPackageCoreImpl(String name, String path,
			GeoPackageCoreConnection database, boolean writable) {
		this.name = name;
		this.path = path;
		this.database = database;
		this.tableCreator = new GeoPackageTableCreator(database);
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
	public GeoPackageTableCreator getTableCreator() {
		return tableCreator;
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
		return getTables(type.getName());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<String> getTables(String type) {
		ContentsDao contentDao = getContentsDao();
		List<String> tableNames;
		try {
			tableNames = contentDao.getTables(type);
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to retrieve " + type + " tables", e);
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
		return isTableType(ContentsDataType.FEATURES, table);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isTileTable(String table) {
		return isTableType(ContentsDataType.TILES, table);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isAttributeTable(String table) {
		return isTableType(ContentsDataType.ATTRIBUTES, table);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isTableType(ContentsDataType type, String table) {
		return isTableType(type.getName(), table);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isTableType(String type, String table) {
		return type.equals(getTableType(table));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isFeatureOrTileTable(String table) {
		boolean isType = false;
		Contents contents = getTableContents(table);
		if (contents != null) {
			ContentsDataType dataType = contents.getDataType();
			isType = dataType != null && (dataType == ContentsDataType.FEATURES
					|| dataType == ContentsDataType.TILES);
		}
		return isType;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isContentsTable(String table) {
		return getTableContents(table) != null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isTable(String table) {
		return database.tableExists(table);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isView(String view) {
		return database.viewExists(view);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isTableOrView(String name) {
		return database.tableOrViewExists(name);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Contents getTableContents(String table) {
		ContentsDao contentDao = getContentsDao();
		Contents contents = null;
		try {
			contents = contentDao.queryForId(table);
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to retrieve table contents: " + table, e);
		}
		return contents;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getTableType(String table) {
		String tableType = null;
		Contents contents = getTableContents(table);
		if (contents != null) {
			tableType = contents.getDataTypeName();
		}
		return tableType;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ContentsDataType getTableDataType(String table) {
		ContentsDataType tableType = null;
		Contents contents = getTableContents(table);
		if (contents != null) {
			tableType = contents.getDataType();
		}
		return tableType;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BoundingBox getContentsBoundingBox(Projection projection) {
		ContentsDao contentsDao = getContentsDao();
		BoundingBox boundingBox = contentsDao.getBoundingBox(projection);
		return boundingBox;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BoundingBox getBoundingBox(Projection projection) {
		return getBoundingBox(projection, false);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BoundingBox getBoundingBox(Projection projection, boolean manual) {

		BoundingBox boundingBox = getContentsBoundingBox(projection);

		List<String> tables = getTables();
		for (String table : tables) {
			BoundingBox tableBoundingBox = getBoundingBox(projection, table,
					manual);
			if (tableBoundingBox != null) {
				if (boundingBox != null) {
					boundingBox = boundingBox.union(tableBoundingBox);
				} else {
					boundingBox = tableBoundingBox;
				}
			}
		}

		return boundingBox;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BoundingBox getContentsBoundingBox(String table) {
		ContentsDao contentsDao = getContentsDao();
		BoundingBox boundingBox = contentsDao.getBoundingBox(table);
		return boundingBox;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BoundingBox getContentsBoundingBox(Projection projection,
			String table) {
		ContentsDao contentsDao = getContentsDao();
		BoundingBox boundingBox = contentsDao.getBoundingBox(projection, table);
		return boundingBox;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BoundingBox getBoundingBox(String table) {
		return getBoundingBox(null, table);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BoundingBox getBoundingBox(Projection projection, String table) {
		return getBoundingBox(projection, table, false);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BoundingBox getBoundingBox(String table, boolean manual) {
		return getBoundingBox(null, table, manual);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BoundingBox getBoundingBox(Projection projection, String table,
			boolean manual) {

		BoundingBox boundingBox = getContentsBoundingBox(projection, table);

		BoundingBox tableBoundingBox = null;
		String tableType = getTableType(table);
		ContentsDataType dataType = ContentsDataType.fromName(tableType);
		if (dataType != null) {
			switch (dataType) {
			case FEATURES:
				tableBoundingBox = getFeatureBoundingBox(projection, table,
						manual);
				break;
			case TILES:
				try {
					TileMatrixSet tileMatrixSet = getTileMatrixSetDao()
							.queryForId(table);
					tableBoundingBox = tileMatrixSet.getBoundingBox(projection);
				} catch (SQLException e) {
					throw new GeoPackageException(
							"Failed to retrieve tile matrix set for table: "
									+ table,
							e);
				}
				break;
			default:

			}
		}

		if (tableBoundingBox != null) {
			if (boundingBox == null) {
				boundingBox = tableBoundingBox;
			} else {
				boundingBox = boundingBox.union(tableBoundingBox);
			}
		}

		return boundingBox;
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

		SpatialReferenceSystemSqlMmDao dao = createDao(
				SpatialReferenceSystemSqlMm.class);
		verifyTableExists(dao);

		return dao;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SpatialReferenceSystemSfSqlDao getSpatialReferenceSystemSfSqlDao() {

		SpatialReferenceSystemSfSqlDao dao = createDao(
				SpatialReferenceSystemSfSql.class);
		verifyTableExists(dao);

		return dao;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ContentsDao getContentsDao() {
		return createDao(Contents.class);
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
		createUserTable(table);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public GeometryColumns createFeatureTableWithMetadata(
			GeometryColumns geometryColumns, BoundingBox boundingBox,
			long srsId) {
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
		return createFeatureTypedTableWithMetadata(
				ContentsDataType.FEATURES.getName(), geometryColumns,
				idColumnName, additionalColumns, boundingBox, srsId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public GeometryColumns createFeatureTableWithMetadata(
			GeometryColumns geometryColumns, BoundingBox boundingBox,
			long srsId, List<FeatureColumn> columns) {
		return createFeatureTypedTableWithMetadata(
				ContentsDataType.FEATURES.getName(), geometryColumns,
				boundingBox, srsId, columns);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public GeometryColumns createFeatureTypedTableWithMetadata(String dataType,
			GeometryColumns geometryColumns, BoundingBox boundingBox,
			long srsId) {
		return createFeatureTypedTableWithMetadata(dataType, geometryColumns,
				null, null, boundingBox, srsId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public GeometryColumns createFeatureTypedTableWithMetadata(String dataType,
			GeometryColumns geometryColumns, String idColumnName,
			BoundingBox boundingBox, long srsId) {
		return createFeatureTypedTableWithMetadata(dataType, geometryColumns,
				idColumnName, null, boundingBox, srsId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public GeometryColumns createFeatureTypedTableWithMetadata(String dataType,
			GeometryColumns geometryColumns,
			List<FeatureColumn> additionalColumns, BoundingBox boundingBox,
			long srsId) {
		return createFeatureTypedTableWithMetadata(dataType, geometryColumns,
				null, additionalColumns, boundingBox, srsId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public GeometryColumns createFeatureTypedTableWithMetadata(String dataType,
			GeometryColumns geometryColumns, String idColumnName,
			List<FeatureColumn> additionalColumns, BoundingBox boundingBox,
			long srsId) {

		if (idColumnName == null) {
			idColumnName = "id";
		}

		List<FeatureColumn> columns = new ArrayList<FeatureColumn>();
		columns.add(FeatureColumn.createPrimaryKeyColumn(idColumnName));
		columns.add(FeatureColumn.createGeometryColumn(
				geometryColumns.getColumnName(),
				geometryColumns.getGeometryType()));

		if (additionalColumns != null) {
			columns.addAll(additionalColumns);
		}

		return createFeatureTypedTableWithMetadata(dataType, geometryColumns,
				boundingBox, srsId, columns);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public GeometryColumns createFeatureTypedTableWithMetadata(String dataType,
			GeometryColumns geometryColumns, BoundingBox boundingBox,
			long srsId, List<FeatureColumn> columns) {

		// Get the SRS
		SpatialReferenceSystem srs = getSrs(srsId);

		// Create the Geometry Columns table
		createGeometryColumnsTable();

		// Create the user feature table
		FeatureTable table = new FeatureTable(geometryColumns, columns);
		createFeatureTable(table);

		try {
			// Create the contents
			Contents contents = new Contents();
			contents.setTableName(geometryColumns.getTableName());
			contents.setDataTypeName(dataType, ContentsDataType.FEATURES);
			contents.setIdentifier(geometryColumns.getTableName());
			// contents.setLastChange(new Date());
			if (boundingBox != null) {
				contents.setMinX(boundingBox.getMinLongitude());
				contents.setMinY(boundingBox.getMinLatitude());
				contents.setMaxX(boundingBox.getMaxLongitude());
				contents.setMaxY(boundingBox.getMaxLatitude());
			}
			contents.setSrs(srs);
			getContentsDao().create(contents);

			table.setContents(contents);

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
							+ geometryColumns.getTableName(),
					e);
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
			throw new GeoPackageException(
					"Failed to check if " + TileMatrixSet.class.getSimpleName()
							+ " table exists and create it",
					e);
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
			throw new GeoPackageException(
					"Failed to check if " + TileMatrix.class.getSimpleName()
							+ " table exists and create it",
					e);
		}
		return created;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void createTileTable(TileTable table) {
		createUserTable(table);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TileMatrixSet createTileTableWithMetadata(String tableName,
			BoundingBox tileMatrixSetBoundingBox, long tileMatrixSetSrsId) {
		return createTileTableWithMetadata(tableName, tileMatrixSetBoundingBox,
				tileMatrixSetSrsId, tileMatrixSetBoundingBox,
				tileMatrixSetSrsId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TileMatrixSet createTileTableWithMetadata(String tableName,
			BoundingBox contentsBoundingBox, long contentsSrsId,
			BoundingBox tileMatrixSetBoundingBox, long tileMatrixSetSrsId) {
		return createTileTypedTableWithMetadata(
				ContentsDataType.TILES.getName(), tableName,
				contentsBoundingBox, contentsSrsId, tileMatrixSetBoundingBox,
				tileMatrixSetSrsId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TileMatrixSet createTileTypedTableWithMetadata(String dataType,
			String tableName, BoundingBox tileMatrixSetBoundingBox,
			long tileMatrixSetSrsId) {
		return createTileTypedTableWithMetadata(dataType, tableName,
				tileMatrixSetBoundingBox, tileMatrixSetSrsId,
				tileMatrixSetBoundingBox, tileMatrixSetSrsId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TileMatrixSet createTileTypedTableWithMetadata(String dataType,
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
			contents.setDataTypeName(dataType, ContentsDataType.TILES);
			contents.setIdentifier(tableName);
			// contents.setLastChange(new Date());
			contents.setMinX(contentsBoundingBox.getMinLongitude());
			contents.setMinY(contentsBoundingBox.getMinLatitude());
			contents.setMaxX(contentsBoundingBox.getMaxLongitude());
			contents.setMaxY(contentsBoundingBox.getMaxLatitude());
			contents.setSrs(contentsSrs);
			getContentsDao().create(contents);

			table.setContents(contents);

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
	 *            srs id
	 * @return srs
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
	public void createAttributesTable(AttributesTable table) {
		createUserTable(table);
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
	public AttributesTable createAttributesTableWithId(String tableName,
			List<AttributesColumn> additionalColumns,
			Collection<Constraint> constraints) {
		return createAttributesTable(tableName, null, additionalColumns,
				constraints);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AttributesTable createAttributesTable(String tableName,
			String idColumnName, List<AttributesColumn> additionalColumns) {
		return createAttributesTable(tableName, idColumnName, additionalColumns,
				null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AttributesTable createAttributesTable(String tableName,
			String idColumnName, List<AttributesColumn> additionalColumns,
			Collection<Constraint> constraints) {
		return createAttributesTypedTable(ContentsDataType.ATTRIBUTES.getName(),
				tableName, idColumnName, additionalColumns, constraints);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AttributesTable createAttributesTable(String tableName,
			List<AttributesColumn> columns) {
		return createAttributesTable(tableName, columns, null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AttributesTable createAttributesTable(String tableName,
			List<AttributesColumn> columns,
			Collection<Constraint> constraints) {
		return createAttributesTypedTable(ContentsDataType.ATTRIBUTES.getName(),
				tableName, columns, constraints);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AttributesTable createAttributesTypedTableWithId(String dataType,
			String tableName, List<AttributesColumn> additionalColumns) {
		return createAttributesTypedTable(dataType, tableName, null,
				additionalColumns);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AttributesTable createAttributesTypedTableWithId(String dataType,
			String tableName, List<AttributesColumn> additionalColumns,
			Collection<Constraint> constraints) {
		return createAttributesTypedTable(dataType, tableName, null,
				additionalColumns, constraints);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AttributesTable createAttributesTypedTable(String dataType,
			String tableName, String idColumnName,
			List<AttributesColumn> additionalColumns) {
		return createAttributesTypedTable(dataType, tableName, idColumnName,
				additionalColumns, null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AttributesTable createAttributesTypedTable(String dataType,
			String tableName, String idColumnName,
			List<AttributesColumn> additionalColumns,
			Collection<Constraint> constraints) {

		if (idColumnName == null) {
			idColumnName = "id";
		}

		List<AttributesColumn> columns = new ArrayList<AttributesColumn>();
		columns.add(AttributesColumn.createPrimaryKeyColumn(idColumnName));

		if (additionalColumns != null) {
			columns.addAll(additionalColumns);
		}

		return createAttributesTypedTable(dataType, tableName, columns,
				constraints);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AttributesTable createAttributesTypedTable(String dataType,
			String tableName, List<AttributesColumn> columns) {
		return createAttributesTypedTable(dataType, tableName, columns, null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AttributesTable createAttributesTypedTable(String dataType,
			String tableName, List<AttributesColumn> columns,
			Collection<Constraint> constraints) {

		// Build the user attributes table
		AttributesTable table = new AttributesTable(tableName, columns);

		// Add unique constraints
		if (constraints != null) {
			table.addConstraints(constraints);
		}

		// Create the user attributes table
		createAttributesTable(table);

		try {
			// Create the contents
			Contents contents = new Contents();
			contents.setTableName(tableName);
			contents.setDataTypeName(dataType, ContentsDataType.ATTRIBUTES);
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
			throw new GeoPackageException(
					"Failed to check if " + DataColumns.class.getSimpleName()
							+ " table exists and create it",
					e);
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
			throw new GeoPackageException(
					"Failed to check if " + Metadata.class.getSimpleName()
							+ " table exists and create it",
					e);
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
			throw new GeoPackageException(
					"Failed to check if " + Extensions.class.getSimpleName()
							+ " table exists and create it",
					e);
		}
		return created;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteTable(String table) {
		verifyWritable();

		getExtensionManager().deleteTableExtensions(table);

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
	public <D extends GeoPackageDao<T, ?>, T> D createDao(Class<T> type) {
		D dao;
		try {
			dao = GeoPackageDao.createDao(database, type);
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to create " + type.getSimpleName() + " dao", e);
		}
		return dao;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void endTransaction() {
		endTransaction(true);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void failTransaction() {
		endTransaction(false);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void endAndBeginTransaction() {
		endTransaction();
		beginTransaction();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T> T callInTransaction(Callable<T> callable) throws SQLException {
		return TransactionManager
				.callInTransaction(database.getConnectionSource(), callable);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean enableForeignKeys() {
		return database.enableForeignKeys();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean foreignKeys() {
		return database.foreignKeys();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean foreignKeys(boolean on) {
		return database.foreignKeys(on);
	}

	/**
	 * Verify table or view exists
	 *
	 * @param dao
	 */
	private void verifyTableExists(Dao<?, ?> dao) {
		try {
			if (!dao.isTableExists()) {
				throw new GeoPackageException(
						"Table or view does not exist for: "
								+ dao.getDataClass().getSimpleName());
			}
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to detect if table or view exists for dao: "
							+ dao.getDataClass().getSimpleName(),
					e);
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
	public void dropView(String view) {
		tableCreator.dropView(view);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void renameTable(String tableName, String newTableName) {
		if (getTableDataType(tableName) != null) {
			copyTable(tableName, newTableName);
			deleteTable(tableName);
		} else {
			AlterTable.renameTable(database, tableName, newTableName);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void copyTable(String tableName, String newTableName) {
		copyTable(tableName, newTableName, true, true);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void copyTableNoExtensions(String tableName, String newTableName) {
		copyTable(tableName, newTableName, true, false);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void copyTableAsEmpty(String tableName, String newTableName) {
		copyTable(tableName, newTableName, false, false);
	}

	/**
	 * Copy the table
	 * 
	 * @param tableName
	 *            table name
	 * @param newTableName
	 *            new table name
	 * @param transferContent
	 *            transfer content flag
	 * @param extensions
	 *            extensions copy flag
	 */
	protected void copyTable(String tableName, String newTableName,
			boolean transferContent, boolean extensions) {

		ContentsDataType dataType = getTableDataType(tableName);
		if (dataType != null) {
			switch (dataType) {

			case ATTRIBUTES:
				copyAttributeTable(tableName, newTableName, transferContent);
				break;

			case FEATURES:
				copyFeatureTable(tableName, newTableName, transferContent);
				break;

			case TILES:
				copyTileTable(tableName, newTableName, transferContent);
				break;

			default:
				throw new GeoPackageException(
						"Unsupported data type: " + dataType);
			}
		} else {
			copyUserTable(tableName, newTableName, transferContent, false);
		}

		// Copy extensions
		if (extensions) {
			getExtensionManager().copyTableExtensions(tableName, newTableName);
		}
	}

	/**
	 * Copy the attribute table
	 * 
	 * @param tableName
	 *            table name
	 * @param newTableName
	 *            new table name
	 * @param transferContent
	 *            transfer content flag
	 * @since 3.3.0
	 */
	protected void copyAttributeTable(String tableName, String newTableName,
			boolean transferContent) {
		copyUserTable(tableName, newTableName, transferContent);
	}

	/**
	 * Copy the feature table
	 * 
	 * @param tableName
	 *            table name
	 * @param newTableName
	 *            new table name
	 * @param transferContent
	 *            transfer content flag
	 * @since 3.3.0
	 */
	protected void copyFeatureTable(String tableName, String newTableName,
			boolean transferContent) {

		GeometryColumnsDao geometryColumnsDao = getGeometryColumnsDao();
		GeometryColumns geometryColumns = null;
		try {
			geometryColumns = geometryColumnsDao.queryForTableName(tableName);
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to retrieve table geometry columns: " + tableName,
					e);
		}
		if (geometryColumns == null) {
			throw new GeoPackageException(
					"No geometry columns for table: " + tableName);
		}

		Contents contents = copyUserTable(tableName, newTableName,
				transferContent);

		geometryColumns.setContents(contents);
		try {
			geometryColumnsDao.create(geometryColumns);
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to create geometry columns for feature table: "
							+ newTableName,
					e);
		}
	}

	/**
	 * Copy the tile table
	 * 
	 * @param tableName
	 *            table name
	 * @param newTableName
	 *            new table name
	 * @param transferContent
	 *            transfer content flag
	 * @since 3.3.0
	 */
	protected void copyTileTable(String tableName, String newTableName,
			boolean transferContent) {

		TileMatrixSetDao tileMatrixSetDao = getTileMatrixSetDao();
		TileMatrixSet tileMatrixSet = null;
		try {
			tileMatrixSet = tileMatrixSetDao.queryForId(tableName);
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to retrieve table tile matrix set: " + tableName,
					e);
		}
		if (tileMatrixSet == null) {
			throw new GeoPackageException(
					"No tile matrix set for table: " + tableName);
		}

		TileMatrixDao tileMatrixDao = getTileMatrixDao();
		List<TileMatrix> tileMatrixes = null;
		try {
			tileMatrixes = tileMatrixDao
					.queryForEq(TileMatrix.COLUMN_TABLE_NAME, tableName);
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to retrieve table tile matrixes: " + tableName, e);
		}

		Contents contents = copyUserTable(tableName, newTableName,
				transferContent);

		tileMatrixSet.setContents(contents);
		try {
			tileMatrixSetDao.create(tileMatrixSet);
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to create tile matrix set for tile table: "
							+ newTableName,
					e);
		}

		for (TileMatrix tileMatrix : tileMatrixes) {
			tileMatrix.setContents(contents);
			try {
				tileMatrixDao.create(tileMatrix);
			} catch (SQLException e) {
				throw new GeoPackageException(
						"Failed to create tile matrix for tile table: "
								+ newTableName,
						e);
			}
		}

	}

	/**
	 * Copy the user table
	 * 
	 * @param tableName
	 *            table name
	 * @param newTableName
	 *            new table name
	 * @param transferContent
	 *            transfer user table content flag
	 * @return copied contents
	 * @since 3.3.0
	 */
	protected Contents copyUserTable(String tableName, String newTableName,
			boolean transferContent) {
		return copyUserTable(tableName, newTableName, transferContent, true);
	}

	/**
	 * Copy the user table
	 * 
	 * @param tableName
	 *            table name
	 * @param newTableName
	 *            new table name
	 * @param transferContent
	 *            transfer user table content flag
	 * @param validateContents
	 *            true to validate a contents was copied
	 * @return copied contents
	 * @since 3.3.0
	 */
	protected Contents copyUserTable(String tableName, String newTableName,
			boolean transferContent, boolean validateContents) {

		AlterTable.copyTable(database, tableName, newTableName,
				transferContent);

		Contents contents = copyContents(tableName, newTableName);

		if (contents == null && validateContents) {
			throw new GeoPackageException(
					"No table contents found for table: " + tableName);
		}

		return contents;
	}

	/**
	 * Copy the contents
	 * 
	 * @param tableName
	 *            table name
	 * @param newTableName
	 *            new table name
	 * @return copied contents
	 * @since 3.3.0
	 */
	protected Contents copyContents(String tableName, String newTableName) {

		Contents contents = getTableContents(tableName);

		if (contents != null) {

			contents.setTableName(newTableName);
			contents.setIdentifier(newTableName);

			try {
				getContentsDao().create(contents);
			} catch (SQLException e) {
				throw new GeoPackageException(
						"Failed to create contents for table: " + newTableName
								+ ", copied from table: " + tableName,
						e);
			}
		}

		return contents;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void vacuum() {
		CoreSQLUtils.vacuum(database);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ExtensionManager getExtensionManager() {
		return new ExtensionManager(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ExtendedRelationsDao getExtendedRelationsDao() {
		return createDao(ExtendedRelation.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean createExtendedRelationsTable() {
		verifyWritable();

		boolean created = false;
		ExtendedRelationsDao dao = getExtendedRelationsDao();
		try {
			if (!dao.isTableExists()) {
				created = tableCreator.createExtendedRelations() > 0;
			}
		} catch (SQLException e) {
			throw new GeoPackageException("Failed to check if "
					+ ExtendedRelation.class.getSimpleName()
					+ " table exists and create it", e);
		}
		return created;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void createUserTable(UserTable<? extends UserColumn> table) {
		verifyWritable();

		tableCreator.createTable(table);
	}

}
