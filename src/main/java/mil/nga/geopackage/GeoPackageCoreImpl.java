package mil.nga.geopackage;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

import com.j256.ormlite.misc.TransactionManager;

import mil.nga.geopackage.attributes.AttributesTable;
import mil.nga.geopackage.attributes.AttributesTableMetadata;
import mil.nga.geopackage.contents.Contents;
import mil.nga.geopackage.contents.ContentsDao;
import mil.nga.geopackage.contents.ContentsDataType;
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
import mil.nga.geopackage.features.columns.GeometryColumns;
import mil.nga.geopackage.features.columns.GeometryColumnsDao;
import mil.nga.geopackage.features.user.FeatureTable;
import mil.nga.geopackage.features.user.FeatureTableMetadata;
import mil.nga.geopackage.srs.SpatialReferenceSystem;
import mil.nga.geopackage.srs.SpatialReferenceSystemDao;
import mil.nga.geopackage.tiles.matrix.TileMatrix;
import mil.nga.geopackage.tiles.matrix.TileMatrixDao;
import mil.nga.geopackage.tiles.matrixset.TileMatrixSet;
import mil.nga.geopackage.tiles.matrixset.TileMatrixSetDao;
import mil.nga.geopackage.tiles.user.TileColumn;
import mil.nga.geopackage.tiles.user.TileTable;
import mil.nga.geopackage.tiles.user.TileTableMetadata;
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
	public Integer getApplicationIdInteger() {
		return database.getApplicationIdInteger();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getApplicationIdHex() {
		return database.getApplicationIdHex();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer getUserVersion() {
		return database.getUserVersion();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer getUserVersionMajor() {
		return database.getUserVersionMajor();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer getUserVersionMinor() {
		return database.getUserVersionMinor();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer getUserVersionPatch() {
		return database.getUserVersionPatch();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<String> getFeatureTables() {
		return getTables(ContentsDataType.FEATURES);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<String> getTileTables() {
		return getTables(ContentsDataType.TILES);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<String> getAttributesTables() {
		return getTables(ContentsDataType.ATTRIBUTES);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<String> getTables(ContentsDataType type) {
		List<String> tableNames;
		try {
			tableNames = getContentsDao().getTables(type);
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
	public List<String> getTables(ContentsDataType... types) {
		List<String> tableNames;
		try {
			tableNames = getContentsDao().getTables(types);
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to retrieve tables of types: " + types, e);
		}
		return tableNames;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<String> getTables(String type) {
		List<String> tableNames;
		try {
			tableNames = getContentsDao().getTables(type);
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
	public List<String> getTables(String... types) {
		List<String> tableNames;
		try {
			tableNames = getContentsDao().getTables(types);
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to retrieve tables of types: " + types, e);
		}
		return tableNames;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Contents> getTypeContents(ContentsDataType type) {
		List<Contents> contents;
		try {
			contents = getContentsDao().getContents(type);
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to retrieve " + type + " contents", e);
		}
		return contents;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Contents> getTypeContents(ContentsDataType... types) {
		List<Contents> contents;
		try {
			contents = getContentsDao().getContents(types);
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to retrieve contents of types: " + types, e);
		}
		return contents;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Contents> getTypeContents(String type) {
		List<Contents> contents;
		try {
			contents = getContentsDao().getContents(type);
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to retrieve " + type + " contents", e);
		}
		return contents;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Contents> getTypeContents(String... types) {
		List<Contents> contents;
		try {
			contents = getContentsDao().getContents(types);
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to retrieve contents of types: " + types, e);
		}
		return contents;
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
		return isTableType(table, ContentsDataType.FEATURES);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isTileTable(String table) {
		return isTableType(table, ContentsDataType.TILES);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isAttributeTable(String table) {
		return isTableType(table, ContentsDataType.ATTRIBUTES);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isTableType(String table, ContentsDataType type) {
		return isTableType(table, new ContentsDataType[] { type });
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isTableType(String table, ContentsDataType... types) {
		Set<ContentsDataType> typeSet = new HashSet<>(Arrays.asList(types));
		return typeSet.contains(getTableDataType(table));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isTableType(String table, String type) {
		return isTableType(table, new String[] { type });
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isTableType(String table, String... types) {
		Set<String> typeSet = new HashSet<>(Arrays.asList(types));
		boolean isType = typeSet.contains(getTableType(table));
		if (!isType) {
			ContentsDataType dataType = getTableDataType(table);
			if (dataType != null) {
				isType = typeSet.contains(dataType.getName());
			}
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
		return getContentsDao().getBoundingBox(projection);
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

		BoundingBox tableBoundingBox = getTableBoundingBox(projection, manual);

		if (tableBoundingBox != null) {
			if (boundingBox != null) {
				boundingBox = boundingBox.union(tableBoundingBox);
			} else {
				boundingBox = tableBoundingBox;
			}
		}

		return boundingBox;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BoundingBox getTableBoundingBox(Projection projection) {
		return getTableBoundingBox(projection, false);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BoundingBox getTableBoundingBox(Projection projection,
			boolean manual) {

		BoundingBox boundingBox = null;

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

		BoundingBox tableBoundingBox = getTableBoundingBox(projection, table,
				manual);

		if (tableBoundingBox != null && projection == null) {
			projection = getProjection(table);
		}

		BoundingBox boundingBox = getContentsBoundingBox(projection, table);

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
	public BoundingBox getTableBoundingBox(String table) {
		return getTableBoundingBox(null, table);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BoundingBox getTableBoundingBox(Projection projection,
			String table) {
		return getTableBoundingBox(projection, table, false);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BoundingBox getTableBoundingBox(String table, boolean manual) {
		return getTableBoundingBox(null, table, manual);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BoundingBox getTableBoundingBox(Projection projection, String table,
			boolean manual) {

		BoundingBox boundingBox = null;

		String tableType = getTableType(table);
		ContentsDataType dataType = ContentsDataType.fromName(tableType);
		if (dataType != null) {
			switch (dataType) {
			case FEATURES:
				boundingBox = getFeatureBoundingBox(projection, table, manual);
				break;
			case TILES:
				TileMatrixSet tileMatrixSet = null;
				try {
					tileMatrixSet = getTileMatrixSetDao().queryForId(table);
				} catch (SQLException e) {
					throw new GeoPackageException(
							"Failed to retrieve tile matrix set for table: "
									+ table,
							e);
				}
				boundingBox = tileMatrixSet.getBoundingBox(projection);
				break;
			default:

			}
		}

		return boundingBox;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Projection getContentsProjection(String table) {
		Contents contents = getTableContents(table);
		if (contents == null) {
			throw new GeoPackageException(
					"Failed to retrieve contents for table: " + table);
		}
		return contents.getProjection();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Projection getProjection(String table) {

		Projection projection = null;

		String tableType = getTableType(table);
		ContentsDataType dataType = ContentsDataType.fromName(tableType);
		if (dataType != null) {
			switch (dataType) {
			case FEATURES:
				GeometryColumns geometryColumns = null;
				try {
					geometryColumns = getGeometryColumnsDao()
							.queryForTableName(table);
				} catch (SQLException e) {
					throw new GeoPackageException(
							"Failed to retrieve geometry columns for table: "
									+ table,
							e);
				}
				projection = geometryColumns.getProjection();
				break;
			case TILES:
				TileMatrixSet tileMatrixSet = null;
				try {
					tileMatrixSet = getTileMatrixSetDao().queryForId(table);
				} catch (SQLException e) {
					throw new GeoPackageException(
							"Failed to retrieve tile matrix set for table: "
									+ table,
							e);
				}
				projection = tileMatrixSet.getProjection();
				break;
			default:

			}
		}

		if (projection == null) {
			projection = getContentsProjection(table);
		}

		return projection;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SpatialReferenceSystemDao getSpatialReferenceSystemDao() {
		SpatialReferenceSystemDao dao = SpatialReferenceSystemDao.create(this);
		dao.setCrsWktExtension(new CrsWktExtension(this));
		return dao;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ContentsDao getContentsDao() {
		return ContentsDao.create(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public GeometryColumnsDao getGeometryColumnsDao() {
		return GeometryColumnsDao.create(this);
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
	public FeatureTable createFeatureTable(FeatureTableMetadata metadata) {

		GeometryColumns geometryColumns = metadata.getGeometryColumns();
		if (geometryColumns == null) {
			throw new GeoPackageException(
					"Geometry Columns are required to create a feature table");
		}

		// Get the SRS
		SpatialReferenceSystem srs = geometryColumns.getSrs();
		if (srs == null) {
			srs = getSrs(geometryColumns.getSrsId());
			geometryColumns.setSrs(srs);
		}

		// Create the Geometry Columns table
		createGeometryColumnsTable();

		// Create the user feature table
		String tableName = metadata.getTableName();
		FeatureTable table = new FeatureTable(tableName,
				metadata.getColumnName(), metadata.buildColumns());
		createFeatureTable(table);

		try {
			// Create the contents
			Contents contents = new Contents();
			contents.setTableName(tableName);
			contents.setDataTypeName(metadata.getDataType(),
					ContentsDataType.FEATURES);
			contents.setIdentifier(tableName);
			// contents.setLastChange(new Date());
			BoundingBox boundingBox = metadata.getBoundingBox();
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
			getGeometryColumnsDao().create(geometryColumns);

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
	public TileMatrixSetDao getTileMatrixSetDao() {
		return TileMatrixSetDao.create(this);
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
		return TileMatrixDao.create(this);
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
	public TileTable createTileTable(TileTableMetadata metadata) {

		// Get the SRS
		SpatialReferenceSystem contentsSrs = getSrs(
				metadata.getContentsSrsId());
		SpatialReferenceSystem tileMatrixSetSrs = getSrs(
				metadata.getTileSrsId());

		// Create the Tile Matrix Set and Tile Matrix tables
		createTileMatrixSetTable();
		createTileMatrixTable();

		// Create the user tile table
		String tableName = metadata.getTableName();
		List<TileColumn> columns = metadata.buildColumns();
		TileTable table = new TileTable(tableName, columns);
		createTileTable(table);

		try {
			// Create the contents
			Contents contents = new Contents();
			contents.setTableName(tableName);
			contents.setDataTypeName(metadata.getDataType(),
					ContentsDataType.TILES);
			contents.setIdentifier(tableName);
			// contents.setLastChange(new Date());
			BoundingBox contentsBoundingBox = metadata.getContentsBoundingBox();
			contents.setMinX(contentsBoundingBox.getMinLongitude());
			contents.setMinY(contentsBoundingBox.getMinLatitude());
			contents.setMaxX(contentsBoundingBox.getMaxLongitude());
			contents.setMaxY(contentsBoundingBox.getMaxLatitude());
			contents.setSrs(contentsSrs);
			getContentsDao().create(contents);

			table.setContents(contents);

			// Create new matrix tile set
			TileMatrixSet tileMatrixSet = new TileMatrixSet();
			tileMatrixSet.setContents(contents);
			tileMatrixSet.setSrs(tileMatrixSetSrs);
			BoundingBox tileMatrixSetBoundingBox = metadata
					.getTileBoundingBox();
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

		return table;
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
	public AttributesTable createAttributesTable(
			AttributesTableMetadata metadata) {

		// Build the user attributes table
		String tableName = metadata.getTableName();
		AttributesTable table = new AttributesTable(tableName,
				metadata.buildColumns());

		// Add unique constraints
		Collection<Constraint> constraints = metadata.getConstraints();
		if (constraints != null) {
			table.addConstraints(constraints);
		}

		// Create the user attributes table
		createAttributesTable(table);

		try {
			// Create the contents
			Contents contents = new Contents();
			contents.setTableName(tableName);
			contents.setDataTypeName(metadata.getDataType(),
					ContentsDataType.ATTRIBUTES);
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
	public ExtensionsDao getExtensionsDao() {
		return ExtensionsDao.create(this);
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
		return GeoPackageDao.createDao(database, type);
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
	public void createUserTable(UserTable<? extends UserColumn> table) {
		verifyWritable();

		tableCreator.createTable(table);
	}

}
