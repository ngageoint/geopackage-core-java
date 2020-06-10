package mil.nga.geopackage.extension.nga.index;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedDelete;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import mil.nga.geopackage.BoundingBox;
import mil.nga.geopackage.GeoPackageCore;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.contents.Contents;
import mil.nga.geopackage.contents.ContentsDao;
import mil.nga.geopackage.db.GeoPackageCoreConnection;
import mil.nga.geopackage.extension.BaseExtension;
import mil.nga.geopackage.extension.ExtensionScopeType;
import mil.nga.geopackage.extension.Extensions;
import mil.nga.geopackage.extension.nga.NGAExtensions;
import mil.nga.geopackage.geom.GeoPackageGeometryData;
import mil.nga.geopackage.io.GeoPackageProgress;
import mil.nga.geopackage.property.GeoPackageProperties;
import mil.nga.geopackage.property.PropertyConstants;
import mil.nga.sf.GeometryEnvelope;
import mil.nga.sf.proj.Projection;
import mil.nga.sf.proj.ProjectionTransform;

/**
 * Abstract core Feature Table Index NGA Extension implementation. This
 * extension is used to index Geometries within a feature table by their minimum
 * bounding box for bounding box queries. This extension is required to provide
 * an index implementation when a SQLite version is used before SpatialLite
 * support (Android).
 * 
 * http://ngageoint.github.io/GeoPackage/docs/extensions/geometry-index.html
 * 
 * @author osbornb
 * @since 1.1.0
 */
public abstract class FeatureTableCoreIndex extends BaseExtension {

	/**
	 * Logger
	 */
	private static final Logger logger = Logger
			.getLogger(FeatureTableCoreIndex.class.getName());

	/**
	 * Extension author
	 */
	public static final String EXTENSION_AUTHOR = NGAExtensions.EXTENSION_AUTHOR;

	/**
	 * Extension name without the author
	 */
	public static final String EXTENSION_NAME_NO_AUTHOR = "geometry_index";

	/**
	 * Extension, with author and name
	 */
	public static final String EXTENSION_NAME = Extensions
			.buildExtensionName(EXTENSION_AUTHOR, EXTENSION_NAME_NO_AUTHOR);

	/**
	 * Extension definition URL
	 */
	public static final String EXTENSION_DEFINITION = GeoPackageProperties
			.getProperty(PropertyConstants.EXTENSIONS,
					EXTENSION_NAME_NO_AUTHOR);

	/**
	 * Table name
	 */
	private final String tableName;

	/**
	 * Column name
	 */
	private final String columnName;

	/**
	 * Table Index DAO
	 */
	private final TableIndexDao tableIndexDao;

	/**
	 * Geometry Index DAO
	 */
	private final GeometryIndexDao geometryIndexDao;

	/**
	 * Progress
	 */
	protected GeoPackageProgress progress;

	/**
	 * Query single chunk limit
	 */
	protected int chunkLimit = 1000;

	/**
	 * Query range tolerance
	 */
	protected double tolerance = .00000000000001;

	/**
	 * Constructor
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * @param tableName
	 *            table name
	 * @param columnName
	 *            column name
	 */
	protected FeatureTableCoreIndex(GeoPackageCore geoPackage, String tableName,
			String columnName) {
		super(geoPackage);
		this.tableName = tableName;
		this.columnName = columnName;
		tableIndexDao = getTableIndexDao();
		geometryIndexDao = getGeometryIndexDao();
	}

	/**
	 * Get the feature projection
	 * 
	 * @return projection
	 * @since 3.1.0
	 */
	public abstract Projection getProjection();

	/**
	 * Get the GeoPackage
	 * 
	 * @return GeoPackage
	 */
	public GeoPackageCore getGeoPackage() {
		return geoPackage;
	}

	/**
	 * Get the table name
	 * 
	 * @return table name
	 */
	public String getTableName() {
		return tableName;
	}

	/**
	 * Get the column name
	 * 
	 * @return column name
	 */
	public String getColumnName() {
		return columnName;
	}

	/**
	 * Set the progress tracker
	 *
	 * @param progress
	 *            progress tracker
	 */
	public void setProgress(GeoPackageProgress progress) {
		this.progress = progress;
	}

	/**
	 * Get the SQL query chunk limit
	 * 
	 * @return chunk limit
	 * @since 3.1.0
	 */
	public int getChunkLimit() {
		return chunkLimit;
	}

	/**
	 * Set the SQL query chunk limit
	 * 
	 * @param chunkLimit
	 *            chunk limit
	 * @since 3.1.0
	 */
	public void setChunkLimit(int chunkLimit) {
		this.chunkLimit = chunkLimit;
	}

	/**
	 * Get the query range tolerance
	 * 
	 * @return query range tolerance
	 * @since 3.1.0
	 */
	public double getTolerance() {
		return tolerance;
	}

	/**
	 * Set the query range tolerance
	 * 
	 * @param tolerance
	 *            query range tolerance
	 * @since 3.1.0
	 */
	public void setTolerance(double tolerance) {
		this.tolerance = tolerance;
	}

	/**
	 * Index the feature table if needed
	 *
	 * @return count
	 */
	public int index() {
		return index(false);
	}

	/**
	 * Index the feature table
	 *
	 * @param force
	 *            true to force re-indexing
	 * @return count
	 */
	public int index(boolean force) {
		int count = 0;
		if (force || !isIndexed()) {
			getOrCreateExtension();
			TableIndex tableIndex = getOrCreateTableIndex();
			createOrClearGeometryIndices();
			unindexGeometryIndexTable();
			count = indexTable(tableIndex);
			indexGeometryIndexTable();
		}
		return count;
	}

	/**
	 * Index the feature table
	 *
	 * @param tableIndex
	 *            table index
	 * @return count
	 */
	protected abstract int indexTable(TableIndex tableIndex);

	/**
	 * Index the geometry id and geometry data
	 * 
	 * @param tableIndex
	 *            table index
	 * @param geomId
	 *            geometry id
	 * @param geomData
	 *            geometry data
	 * 
	 * @return true if indexed
	 */
	protected boolean index(TableIndex tableIndex, long geomId,
			GeoPackageGeometryData geomData) {

		boolean indexed = false;

		if (geomData != null) {

			// Get or build the envelope
			GeometryEnvelope envelope = geomData.getOrBuildEnvelope();

			// Create the new index row
			if (envelope != null) {
				GeometryIndex geometryIndex = geometryIndexDao
						.populate(tableIndex, geomId, envelope);
				try {
					geometryIndexDao.createOrUpdate(geometryIndex);
					indexed = true;
				} catch (SQLException e) {
					throw new GeoPackageException(
							"Failed to create or update Geometry Index. GeoPackage: "
									+ geoPackage.getName() + ", Table Name: "
									+ tableName + ", Geom Id: " + geomId,
							e);
				}
			}
		}

		return indexed;
	}

	/**
	 * Update the last indexed time
	 */
	protected void updateLastIndexed() {

		TableIndex tableIndex = new TableIndex();
		tableIndex.setTableName(tableName);
		tableIndex.setLastIndexed(new Date());

		try {
			tableIndexDao.createOrUpdate(tableIndex);
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to update last indexed date. GeoPackage: "
							+ geoPackage.getName() + ", Table Name: "
							+ tableName,
					e);
		}
	}

	/**
	 * Delete the feature table index
	 * 
	 * @return true if index deleted
	 */
	public boolean deleteIndex() {

		boolean deleted = false;

		try {
			// Delete geometry indices and table index
			if (tableIndexDao.isTableExists()) {
				deleted = tableIndexDao.deleteByIdCascade(tableName) > 0;
			}
			// Delete the extensions entry
			if (extensionsDao.isTableExists()) {
				deleted = extensionsDao.deleteByExtension(EXTENSION_NAME,
						tableName) > 0 || deleted;
			}
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to delete Table Index. GeoPackage: "
							+ geoPackage.getName() + ", Table: " + tableName,
					e);
		}

		return deleted;
	}

	/**
	 * Delete the index for the geometry id
	 * 
	 * @param geomId
	 *            geometry id
	 * 
	 * @return deleted rows, should be 0 or 1
	 */
	public int deleteIndex(long geomId) {
		int deleted = 0;
		GeometryIndexKey key = new GeometryIndexKey(tableName, geomId);
		try {
			deleted = geometryIndexDao.deleteById(key);
		} catch (SQLException e) {
			throw new GeoPackageException("Failed to delete index, GeoPackage: "
					+ geoPackage.getName() + ", Table Name: " + tableName
					+ ", Geometry Id: " + geomId, e);
		}
		return deleted;
	}

	/**
	 * Determine if the feature table is indexed
	 * 
	 * @return true if indexed
	 */
	public boolean isIndexed() {
		boolean indexed = false;
		Extensions extension = getExtension();
		if (extension != null) {

			ContentsDao contentsDao = geoPackage.getContentsDao();
			try {
				Contents contents = contentsDao.queryForId(tableName);
				if (contents != null) {
					Date lastChange = contents.getLastChange();

					TableIndex tableIndex = tableIndexDao.queryForId(tableName);

					if (tableIndex != null) {
						Date lastIndexed = tableIndex.getLastIndexed();
						indexed = lastIndexed != null && lastIndexed
								.getTime() >= lastChange.getTime();
					}
				}
			} catch (SQLException e) {
				throw new GeoPackageException(
						"Failed to check if table is indexed, GeoPackage: "
								+ geoPackage.getName() + ", Table Name: "
								+ tableName,
						e);
			}
		}
		return indexed;
	}

	/**
	 * Get or create if needed the table index
	 * 
	 * @return table index
	 */
	private TableIndex getOrCreateTableIndex() {
		TableIndex tableIndex = getTableIndex();

		if (tableIndex == null) {
			try {
				if (!tableIndexDao.isTableExists()) {
					createTableIndexTable();
				}

				tableIndex = new TableIndex();
				tableIndex.setTableName(tableName);
				tableIndex.setLastIndexed(null);

				tableIndexDao.create(tableIndex);
			} catch (SQLException e) {
				throw new GeoPackageException(
						"Failed to create Table Index for GeoPackage: "
								+ geoPackage.getName() + ", Table Name: "
								+ tableName + ", Column Name: " + columnName,
						e);
			}
		}

		return tableIndex;
	}

	/**
	 * Get the table index
	 * 
	 * @return table index
	 */
	public TableIndex getTableIndex() {

		TableIndex tableIndex = null;
		try {
			if (tableIndexDao.isTableExists()) {
				tableIndex = tableIndexDao.queryForId(tableName);
			}
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to query for Table Index for GeoPackage: "
							+ geoPackage.getName() + ", Table Name: "
							+ tableName + ", Column Name: " + columnName,
					e);
		}
		return tableIndex;
	}

	/**
	 * Get the date last indexed
	 * 
	 * @return last indexed date or null
	 */
	public Date getLastIndexed() {
		Date lastIndexed = null;
		TableIndex tableIndex = getTableIndex();
		if (tableIndex != null) {
			lastIndexed = tableIndex.getLastIndexed();
		}
		return lastIndexed;
	}

	/**
	 * Clear the Geometry Indices, or create the table if needed
	 */
	private void createOrClearGeometryIndices() {

		if (!createGeometryIndexTable()) {
			clearGeometryIndices();
		}

	}

	/**
	 * Clear the Geometry Indices for the table name
	 * 
	 * @return number of rows deleted
	 */
	private int clearGeometryIndices() {
		int deleted = 0;
		DeleteBuilder<GeometryIndex, GeometryIndexKey> db = geometryIndexDao
				.deleteBuilder();
		try {
			db.where().eq(GeometryIndex.COLUMN_TABLE_NAME, tableName);
			PreparedDelete<GeometryIndex> deleteQuery = db.prepare();
			deleted = geometryIndexDao.delete(deleteQuery);
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to clear Geometry Index rows for GeoPackage: "
							+ geoPackage.getName() + ", Table Name: "
							+ tableName + ", Column Name: " + columnName,
					e);
		}

		return deleted;
	}

	/**
	 * Get or create if needed the extension
	 * 
	 * @return extensions object
	 */
	private Extensions getOrCreateExtension() {
		return getOrCreate(EXTENSION_NAME, tableName, columnName,
				EXTENSION_DEFINITION, ExtensionScopeType.READ_WRITE);
	}

	/**
	 * Get the extension
	 * 
	 * @return extensions object or null if one does not exist
	 */
	public Extensions getExtension() {
		return get(EXTENSION_NAME, tableName, columnName);
	}

	/**
	 * Get a Table Index DAO
	 * 
	 * @return table index dao
	 * @since 4.0.0
	 */
	public TableIndexDao getTableIndexDao() {
		return getTableIndexDao(geoPackage);
	}

	/**
	 * Get a Table Index DAO
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * @return table index dao
	 * @since 4.0.0
	 */
	public static TableIndexDao getTableIndexDao(GeoPackageCore geoPackage) {
		return TableIndexDao.create(geoPackage);
	}

	/**
	 * Get a Table Index DAO
	 * 
	 * @param db
	 *            database connection
	 * @return table index dao
	 * @since 4.0.0
	 */
	public static TableIndexDao getTableIndexDao(GeoPackageCoreConnection db) {
		return TableIndexDao.create(db);
	}

	/**
	 * Create the Table Index Table if it does not exist
	 * 
	 * @return true if created
	 * @since 4.0.0
	 */
	public boolean createTableIndexTable() {
		verifyWritable();

		boolean created = false;

		try {
			if (!geometryIndexDao.isTableExists()) {
				GeometryIndexTableCreator tableCreator = new GeometryIndexTableCreator(
						geoPackage);
				created = tableCreator.createTableIndex() > 0;
			}
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to check if " + TableIndex.class.getSimpleName()
							+ " table exists and create it",
					e);
		}
		return created;
	}

	/**
	 * Get a Geometry Index DAO
	 * 
	 * @return geometry index dao
	 * @since 4.0.0
	 */
	public GeometryIndexDao getGeometryIndexDao() {
		return getGeometryIndexDao(geoPackage);
	}

	/**
	 * Get a Geometry Index DAO
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * @return geometry index dao
	 * @since 4.0.0
	 */
	public static GeometryIndexDao getGeometryIndexDao(
			GeoPackageCore geoPackage) {
		return GeometryIndexDao.create(geoPackage);
	}

	/**
	 * Get a Geometry Index DAO
	 * 
	 * @param db
	 *            database connection
	 * @return geometry index dao
	 * @since 4.0.0
	 */
	public static GeometryIndexDao getGeometryIndexDao(
			GeoPackageCoreConnection db) {
		return GeometryIndexDao.create(db);
	}

	/**
	 * Create Geometry Index Table if it does not exist and index it
	 * 
	 * @return true if created
	 * @since 4.0.0
	 */
	public boolean createGeometryIndexTable() {
		verifyWritable();

		boolean created = false;

		try {
			if (!geometryIndexDao.isTableExists()) {
				GeometryIndexTableCreator tableCreator = new GeometryIndexTableCreator(
						geoPackage);
				created = tableCreator.createGeometryIndex() > 0;
			}
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to check if " + GeometryIndex.class.getSimpleName()
							+ " table exists and create it",
					e);
		}
		return created;
	}

	/**
	 * Index the Geometry Index Table if needed
	 * 
	 * @return true if indexed
	 * @since 4.0.0
	 */
	public boolean indexGeometryIndexTable() {
		verifyWritable();

		boolean indexed = false;

		try {
			if (geometryIndexDao.isTableExists()) {
				GeometryIndexTableCreator tableCreator = new GeometryIndexTableCreator(
						geoPackage);
				indexed = tableCreator.indexGeometryIndex() > 0;
			}
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to check if " + GeometryIndex.class.getSimpleName()
							+ " table exists to index",
					e);
		}
		return indexed;
	}

	/**
	 * Un-index the Geometry Index Table if needed
	 * 
	 * @return true if unindexed
	 * @since 4.0.0
	 */
	public boolean unindexGeometryIndexTable() {
		verifyWritable();

		boolean unindexed = false;

		try {
			if (geometryIndexDao.isTableExists()) {
				GeometryIndexTableCreator tableCreator = new GeometryIndexTableCreator(
						geoPackage);
				unindexed = tableCreator.unindexGeometryIndex() > 0;
			}
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to check if " + GeometryIndex.class.getSimpleName()
							+ " table exists to unindex",
					e);
		}
		return unindexed;
	}

	/**
	 * Query for all Geometry Index objects
	 * 
	 * @return geometry indices iterator
	 */
	public CloseableIterator<GeometryIndex> query() {

		CloseableIterator<GeometryIndex> geometryIndices = null;

		QueryBuilder<GeometryIndex, GeometryIndexKey> qb = queryBuilder();

		try {
			geometryIndices = qb.iterator();
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to query for all Geometry Indices. GeoPackage: "
							+ geoPackage.getName() + ", Table Name: "
							+ tableName + ", Column Name: " + columnName,
					e);
		}

		return geometryIndices;
	}

	/**
	 * Query SQL for all row ids
	 * 
	 * @return SQL
	 * @since 3.4.0
	 */
	public String queryIdsSQL() {
		return queryIdsSQL(queryBuilder());
	}

	/**
	 * Query for all Geometry Index count
	 * 
	 * @return count
	 */
	public long count() {
		long count = 0;

		QueryBuilder<GeometryIndex, GeometryIndexKey> qb = queryBuilder();
		try {
			count = qb.countOf();
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to query for Geometry Index count. GeoPackage: "
							+ geoPackage.getName() + ", Table Name: "
							+ tableName + ", Column Name: " + columnName,
					e);
		}

		return count;
	}

	/**
	 * Query for the bounds of the feature table index
	 * 
	 * @return bounding box
	 * @since 3.1.0
	 */
	public BoundingBox getBoundingBox() {

		GenericRawResults<Object[]> results = null;
		Object[] values = null;
		try {
			results = geometryIndexDao.queryRaw(
					"SELECT MIN(" + GeometryIndex.COLUMN_MIN_X + "), MIN("
							+ GeometryIndex.COLUMN_MIN_Y + "), MAX("
							+ GeometryIndex.COLUMN_MAX_X + "), MAX("
							+ GeometryIndex.COLUMN_MAX_Y + ") FROM "
							+ GeometryIndex.TABLE_NAME + " WHERE "
							+ GeometryIndex.COLUMN_TABLE_NAME + " = ?",
					new DataType[] { DataType.DOUBLE, DataType.DOUBLE,
							DataType.DOUBLE, DataType.DOUBLE },
					tableName);
			values = results.getFirstResult();
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to query for indexed feature bounds: " + tableName,
					e);
		} finally {
			if (results != null) {
				try {
					results.close();
				} catch (IOException e) {
					logger.log(Level.WARNING,
							"Failed to close bounds query results", e);
				}
			}
		}

		BoundingBox boundingBox = new BoundingBox((double) values[0],
				(double) values[1], (double) values[2], (double) values[3]);

		return boundingBox;
	}

	/**
	 * Query for the feature index bounds and return in the provided projection
	 * 
	 * @param projection
	 *            desired projection
	 * @return bounding box
	 * @since 3.1.0
	 */
	public BoundingBox getBoundingBox(Projection projection) {
		BoundingBox boundingBox = getBoundingBox();
		if (boundingBox != null && projection != null) {
			ProjectionTransform projectionTransform = getProjection()
					.getTransformation(projection);
			boundingBox = boundingBox.transform(projectionTransform);
		}
		return boundingBox;
	}

	/**
	 * Build a query builder to query for all Geometry Index objects
	 * 
	 * @return query builder
	 */
	public QueryBuilder<GeometryIndex, GeometryIndexKey> queryBuilder() {

		QueryBuilder<GeometryIndex, GeometryIndexKey> qb = geometryIndexDao
				.queryBuilder();

		try {
			qb.where().eq(GeometryIndex.COLUMN_TABLE_NAME, tableName);
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to build query for all Geometry Indices. GeoPackage: "
							+ geoPackage.getName() + ", Table Name: "
							+ tableName + ", Column Name: " + columnName,
					e);
		}

		return qb;
	}

	/**
	 * Query for Geometry Index objects within the bounding box, projected
	 * correctly
	 * 
	 * @param boundingBox
	 *            bounding box
	 * @return geometry indices iterator
	 */
	public CloseableIterator<GeometryIndex> query(BoundingBox boundingBox) {
		GeometryEnvelope envelope = boundingBox.buildEnvelope();
		CloseableIterator<GeometryIndex> geometryIndices = query(envelope);
		return geometryIndices;
	}

	/**
	 * Query for Geometry Index objects within the bounding box, projected
	 * correctly
	 * 
	 * @param boundingBox
	 *            bounding box
	 * @param projection
	 *            projection of the provided bounding box
	 * @return geometry indices iterator
	 */
	public CloseableIterator<GeometryIndex> query(BoundingBox boundingBox,
			Projection projection) {

		BoundingBox featureBoundingBox = getFeatureBoundingBox(boundingBox,
				projection);

		CloseableIterator<GeometryIndex> geometryIndices = query(
				featureBoundingBox);

		return geometryIndices;
	}

	/**
	 * Query for Geometry Index count within the bounding box, projected
	 * correctly
	 * 
	 * @param boundingBox
	 *            bounding box
	 * @return count
	 */
	public long count(BoundingBox boundingBox) {
		GeometryEnvelope envelope = boundingBox.buildEnvelope();
		long count = count(envelope);
		return count;
	}

	/**
	 * Query for Geometry Index count within the bounding box, projected
	 * correctly
	 * 
	 * @param boundingBox
	 *            bounding box
	 * @param projection
	 *            projection of the provided bounding box
	 * @return count
	 */
	public long count(BoundingBox boundingBox, Projection projection) {

		BoundingBox featureBoundingBox = getFeatureBoundingBox(boundingBox,
				projection);

		long count = count(featureBoundingBox);

		return count;
	}

	/**
	 * Query for Geometry Index objects within the Geometry Envelope
	 * 
	 * @param envelope
	 *            geometry envelope
	 * @return geometry indices iterator
	 */
	public CloseableIterator<GeometryIndex> query(GeometryEnvelope envelope) {

		CloseableIterator<GeometryIndex> geometryIndices = null;

		QueryBuilder<GeometryIndex, GeometryIndexKey> qb = queryBuilder(
				envelope);
		try {
			geometryIndices = qb.iterator();
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to query for Geometry Indices. GeoPackage: "
							+ geoPackage.getName() + ", Table Name: "
							+ tableName + ", Column Name: " + columnName,
					e);
		}

		return geometryIndices;
	}

	/**
	 * Query SQL for all row ids
	 * 
	 * @param envelope
	 *            geometry envelope
	 * @return SQL
	 * @since 3.4.0
	 */
	public String queryIdsSQL(GeometryEnvelope envelope) {
		return queryIdsSQL(queryBuilder(envelope));
	}

	/**
	 * Query for Geometry Index count within the Geometry Envelope
	 * 
	 * @param envelope
	 *            geometry envelope
	 * @return count
	 */
	public long count(GeometryEnvelope envelope) {
		long count = 0;

		QueryBuilder<GeometryIndex, GeometryIndexKey> qb = queryBuilder(
				envelope);
		try {
			count = qb.countOf();
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to query for Geometry Index count. GeoPackage: "
							+ geoPackage.getName() + ", Table Name: "
							+ tableName + ", Column Name: " + columnName,
					e);
		}

		return count;
	}

	/**
	 * Build a query builder to query for Geometry Index objects within the
	 * Geometry Envelope
	 * 
	 * @param envelope
	 *            geometry envelope
	 * @return query builder
	 */
	public QueryBuilder<GeometryIndex, GeometryIndexKey> queryBuilder(
			GeometryEnvelope envelope) {

		QueryBuilder<GeometryIndex, GeometryIndexKey> qb = geometryIndexDao
				.queryBuilder();
		try {

			double minX = envelope.getMinX() - tolerance;
			double maxX = envelope.getMaxX() + tolerance;
			double minY = envelope.getMinY() - tolerance;
			double maxY = envelope.getMaxY() + tolerance;

			Where<GeometryIndex, GeometryIndexKey> where = qb.where();
			where.eq(GeometryIndex.COLUMN_TABLE_NAME, tableName).and()
					.le(GeometryIndex.COLUMN_MIN_X, maxX).and()
					.ge(GeometryIndex.COLUMN_MAX_X, minX).and()
					.le(GeometryIndex.COLUMN_MIN_Y, maxY).and()
					.ge(GeometryIndex.COLUMN_MAX_Y, minY);

			if (envelope.hasZ()) {
				double minZ = envelope.getMinZ() - tolerance;
				double maxZ = envelope.getMaxZ() + tolerance;
				where.and().le(GeometryIndex.COLUMN_MIN_Z, maxZ).and()
						.ge(GeometryIndex.COLUMN_MAX_Z, minZ);
			}

			if (envelope.hasM()) {
				double minM = envelope.getMinM() - tolerance;
				double maxM = envelope.getMaxM() + tolerance;
				where.and().le(GeometryIndex.COLUMN_MIN_M, maxM).and()
						.ge(GeometryIndex.COLUMN_MAX_M, minM);
			}

		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to build query for Geometry Indices. GeoPackage: "
							+ geoPackage.getName() + ", Table Name: "
							+ tableName + ", Column Name: " + columnName,
					e);
		}

		return qb;
	}

	/**
	 * Get the bounding box in the feature projection from the bounding box in
	 * the provided projection
	 * 
	 * @param boundingBox
	 *            bounding box
	 * @param projection
	 *            projection
	 * @return feature projected bounding box
	 */
	protected BoundingBox getFeatureBoundingBox(BoundingBox boundingBox,
			Projection projection) {
		ProjectionTransform projectionTransform = projection
				.getTransformation(getProjection());
		BoundingBox featureBoundingBox = boundingBox
				.transform(projectionTransform);
		return featureBoundingBox;
	}

	/**
	 * Build SQL for selecting ids from the query builder
	 * 
	 * @param qb
	 *            query builder
	 * @return SQL
	 */
	private String queryIdsSQL(
			QueryBuilder<GeometryIndex, GeometryIndexKey> qb) {
		qb.selectRaw(GeometryIndex.COLUMN_GEOM_ID);
		return prepareStatementString(qb);
	}

	/**
	 * Prepare a statement string from a query builder
	 * 
	 * @param qb
	 *            query builder
	 * @return statement
	 */
	private String prepareStatementString(
			QueryBuilder<GeometryIndex, GeometryIndexKey> qb) {
		String sql = null;
		try {
			sql = qb.prepareStatementString();
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to prepare statement from query builder", e);
		}
		return sql;
	}

}
