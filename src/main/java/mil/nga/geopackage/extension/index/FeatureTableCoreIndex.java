package mil.nga.geopackage.extension.index;

import java.sql.SQLException;
import java.util.Date;

import mil.nga.geopackage.BoundingBox;
import mil.nga.geopackage.GeoPackageCore;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.core.contents.Contents;
import mil.nga.geopackage.core.contents.ContentsDao;
import mil.nga.geopackage.extension.BaseExtension;
import mil.nga.geopackage.extension.ExtensionScopeType;
import mil.nga.geopackage.extension.Extensions;
import mil.nga.geopackage.extension.ExtensionsDao;
import mil.nga.geopackage.geom.GeoPackageGeometryData;
import mil.nga.geopackage.io.GeoPackageProgress;
import mil.nga.geopackage.property.GeoPackageProperties;
import mil.nga.geopackage.property.PropertyConstants;
import mil.nga.sf.Geometry;
import mil.nga.sf.GeometryEnvelope;
import mil.nga.sf.util.GeometryEnvelopeBuilder;

import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedDelete;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

/**
 * Abstract core Feature Table Index NGA Extension implementation. This
 * extension is used to index Geometries within a feature table by their minimum
 * bounding box for bounding box queries. This extension is required to provide
 * an index implementation when a SQLite version is used before SpatialLite
 * support (Android).
 * 
 * @author osbornb
 * @since 1.1.0
 */
public abstract class FeatureTableCoreIndex extends BaseExtension {

	/**
	 * Extension author
	 */
	public static final String EXTENSION_AUTHOR = "nga";

	/**
	 * Extension name without the author
	 */
	public static final String EXTENSION_NAME_NO_AUTHOR = "geometry_index";

	/**
	 * Extension, with author and name
	 */
	public static final String EXTENSION_NAME = Extensions.buildExtensionName(
			EXTENSION_AUTHOR, EXTENSION_NAME_NO_AUTHOR);

	/**
	 * Extension definition URL
	 */
	public static final String EXTENSION_DEFINITION = GeoPackageProperties
			.getProperty(PropertyConstants.EXTENSIONS, EXTENSION_NAME_NO_AUTHOR);

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
	 * Constructor
	 * 
	 * @param geoPackage
	 * @param tableName
	 * @param columnName
	 */
	protected FeatureTableCoreIndex(GeoPackageCore geoPackage,
			String tableName, String columnName) {
		super(geoPackage);
		this.tableName = tableName;
		this.columnName = columnName;
		tableIndexDao = geoPackage.getTableIndexDao();
		geometryIndexDao = geoPackage.getGeometryIndexDao();
	}

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
	 */
	public void setProgress(GeoPackageProgress progress) {
		this.progress = progress;
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
			count = indexTable(tableIndex);
		}
		return count;
	}

	/**
	 * Index the feature table
	 *
	 * @param tableIndex
	 * @return count
	 */
	protected abstract int indexTable(TableIndex tableIndex);

	/**
	 * Index the geometry id and geometry data
	 * 
	 * @param tableIndex
	 * @param geomId
	 * @param geomData
	 * 
	 * @return true if indexed
	 */
	protected boolean index(TableIndex tableIndex, long geomId,
			GeoPackageGeometryData geomData) {

		boolean indexed = false;

		if (geomData != null) {

			// Get the envelope
			GeometryEnvelope envelope = geomData.getEnvelope();

			// If no envelope, build one from the geometry
			if (envelope == null) {
				Geometry geometry = geomData.getGeometry();
				if (geometry != null) {
					envelope = GeometryEnvelopeBuilder.buildEnvelope(geometry);
				}
			}

			// Create the new index row
			if (envelope != null) {
				GeometryIndex geometryIndex = geometryIndexDao.populate(
						tableIndex, geomId, envelope);
				try {
					geometryIndexDao.createOrUpdate(geometryIndex);
					indexed = true;
				} catch (SQLException e) {
					throw new GeoPackageException(
							"Failed to create or update Geometry Index. GeoPackage: "
									+ geoPackage.getName() + ", Table Name: "
									+ tableName + ", Geom Id: " + geomId, e);
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
							+ tableName, e);
		}
	}

	/**
	 * Delete the feature table index
	 * 
	 * @return true if index deleted
	 */
	public boolean deleteIndex() {

		boolean deleted = false;

		ExtensionsDao extensionsDao = geoPackage.getExtensionsDao();
		TableIndexDao tableIndexDao = geoPackage.getTableIndexDao();
		try {
			// Delete geometry indices and table index
			deleted = tableIndexDao.deleteByIdCascade(tableName) > 0;
			// Delete the extensions entry
			deleted = extensionsDao
					.deleteByExtension(EXTENSION_NAME, tableName) > 0
					|| deleted;
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to delete Table Index. GeoPackage: "
							+ geoPackage.getName() + ", Table: " + tableName, e);
		}

		return deleted;
	}

	/**
	 * Delete the index for the geometry id
	 * 
	 * @param geomId
	 * 
	 * @return deleted rows, should be 0 or 1
	 */
	public int deleteIndex(long geomId) {
		int deleted = 0;
		GeometryIndexKey key = new GeometryIndexKey(tableName, geomId);
		try {
			deleted = geometryIndexDao.deleteById(key);
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to delete index, GeoPackage: "
							+ geoPackage.getName() + ", Table Name: "
							+ tableName + ", Geometry Id: " + geomId, e);
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

					TableIndexDao tableIndexDao = geoPackage.getTableIndexDao();
					TableIndex tableIndex = tableIndexDao.queryForId(tableName);

					if (tableIndex != null) {
						Date lastIndexed = tableIndex.getLastIndexed();
						indexed = lastIndexed != null
								&& lastIndexed.getTime() >= lastChange
										.getTime();
					}
				}
			} catch (SQLException e) {
				throw new GeoPackageException(
						"Failed to check if table is indexed, GeoPackage: "
								+ geoPackage.getName() + ", Table Name: "
								+ tableName, e);
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
					geoPackage.createTableIndexTable();
				}

				tableIndex = new TableIndex();
				tableIndex.setTableName(tableName);
				tableIndex.setLastIndexed(null);

				tableIndexDao.create(tableIndex);
			} catch (SQLException e) {
				throw new GeoPackageException(
						"Failed to create Table Index for GeoPackage: "
								+ geoPackage.getName() + ", Table Name: "
								+ tableName + ", Column Name: " + columnName, e);
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
							+ tableName + ", Column Name: " + columnName, e);
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
							+ tableName + ", Column Name: " + columnName, e);
		}

		return deleted;
	}

	/**
	 * Create the Geometry Index Table if needed
	 * 
	 * @return true if created
	 */
	private boolean createGeometryIndexTable() {

		boolean created = false;

		// Create the geometry index table if needed as well
		try {
			if (!geometryIndexDao.isTableExists()) {
				created = geoPackage.createGeometryIndexTable();
			}
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to create Geometry Index table for GeoPackage: "
							+ geoPackage.getName() + ", Table Name: "
							+ tableName + ", Column Name: " + columnName, e);
		}

		return created;
	}

	/**
	 * Get or create if needed the extension
	 * 
	 * @return extensions object
	 */
	private Extensions getOrCreateExtension() {

		Extensions extension = getOrCreate(EXTENSION_NAME, tableName,
				columnName, EXTENSION_DEFINITION, ExtensionScopeType.READ_WRITE);

		return extension;
	}

	/**
	 * Get the extension
	 * 
	 * @return extensions object or null if one does not exist
	 */
	public Extensions getExtension() {

		Extensions extension = get(EXTENSION_NAME, tableName, columnName);

		return extension;
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
							+ tableName + ", Column Name: " + columnName, e);
		}

		return geometryIndices;
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
							+ tableName + ", Column Name: " + columnName, e);
		}

		return count;
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
							+ tableName + ", Column Name: " + columnName, e);
		}

		return qb;
	}

	/**
	 * Query for Geometry Index objects within the bounding box, projected
	 * correctly
	 * 
	 * @param boundingBox
	 * @return geometry indices iterator
	 */
	public CloseableIterator<GeometryIndex> query(BoundingBox boundingBox) {
		GeometryEnvelope envelope = boundingBox.buildEnvelope();
		CloseableIterator<GeometryIndex> geometryIndices = query(envelope);
		return geometryIndices;
	}

	/**
	 * Query for Geometry Index count within the bounding box, projected
	 * correctly
	 * 
	 * @param boundingBox
	 * @return count
	 */
	public long count(BoundingBox boundingBox) {
		GeometryEnvelope envelope = boundingBox.buildEnvelope();
		long count = count(envelope);
		return count;
	}

	/**
	 * Query for Geometry Index objects within the Geometry Envelope
	 * 
	 * @param envelope
	 * @return geometry indices iterator
	 */
	public CloseableIterator<GeometryIndex> query(GeometryEnvelope envelope) {

		CloseableIterator<GeometryIndex> geometryIndices = null;

		QueryBuilder<GeometryIndex, GeometryIndexKey> qb = queryBuilder(envelope);
		try {
			geometryIndices = qb.iterator();
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to query for Geometry Indices. GeoPackage: "
							+ geoPackage.getName() + ", Table Name: "
							+ tableName + ", Column Name: " + columnName, e);
		}

		return geometryIndices;
	}

	/**
	 * Query for Geometry Index count within the Geometry Envelope
	 * 
	 * @param envelope
	 * @return count
	 */
	public long count(GeometryEnvelope envelope) {
		long count = 0;

		QueryBuilder<GeometryIndex, GeometryIndexKey> qb = queryBuilder(envelope);
		try {
			count = qb.countOf();
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to query for Geometry Index count. GeoPackage: "
							+ geoPackage.getName() + ", Table Name: "
							+ tableName + ", Column Name: " + columnName, e);
		}

		return count;
	}

	/**
	 * Build a query builder to query for Geometry Index objects within the
	 * Geometry Envelope
	 * 
	 * @param envelope
	 * @return query builder
	 */
	public QueryBuilder<GeometryIndex, GeometryIndexKey> queryBuilder(
			GeometryEnvelope envelope) {

		QueryBuilder<GeometryIndex, GeometryIndexKey> qb = geometryIndexDao
				.queryBuilder();
		try {

			Where<GeometryIndex, GeometryIndexKey> where = qb.where();
			where.eq(GeometryIndex.COLUMN_TABLE_NAME, tableName).and()
					.le(GeometryIndex.COLUMN_MIN_X, envelope.getMaxX()).and()
					.ge(GeometryIndex.COLUMN_MAX_X, envelope.getMinX()).and()
					.le(GeometryIndex.COLUMN_MIN_Y, envelope.getMaxY()).and()
					.ge(GeometryIndex.COLUMN_MAX_Y, envelope.getMinY());

			if (envelope.hasZ()) {
				where.and().le(GeometryIndex.COLUMN_MIN_Z, envelope.getMaxZ())
						.and()
						.ge(GeometryIndex.COLUMN_MAX_Z, envelope.getMinZ());
			}

			if (envelope.hasM()) {
				where.and().le(GeometryIndex.COLUMN_MIN_M, envelope.getMaxM())
						.and()
						.ge(GeometryIndex.COLUMN_MAX_M, envelope.getMinM());
			}

		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to build query for Geometry Indices. GeoPackage: "
							+ geoPackage.getName() + ", Table Name: "
							+ tableName + ", Column Name: " + columnName, e);
		}

		return qb;
	}

}
