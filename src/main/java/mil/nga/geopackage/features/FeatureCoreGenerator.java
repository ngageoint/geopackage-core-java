package mil.nga.geopackage.features;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import mil.nga.geopackage.BoundingBox;
import mil.nga.geopackage.GeoPackageCore;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.core.srs.SpatialReferenceSystem;
import mil.nga.geopackage.core.srs.SpatialReferenceSystemDao;
import mil.nga.geopackage.db.GeoPackageDataType;
import mil.nga.geopackage.features.columns.GeometryColumns;
import mil.nga.geopackage.features.columns.GeometryColumnsDao;
import mil.nga.geopackage.features.user.FeatureColumn;
import mil.nga.geopackage.features.user.FeatureTable;
import mil.nga.geopackage.features.user.FeatureTableReader;
import mil.nga.geopackage.geom.GeoPackageGeometryData;
import mil.nga.geopackage.io.GeoPackageProgress;
import mil.nga.geopackage.schema.TableColumnKey;
import mil.nga.sf.Geometry;
import mil.nga.sf.GeometryType;
import mil.nga.sf.proj.Projection;
import mil.nga.sf.proj.ProjectionConstants;
import mil.nga.sf.proj.ProjectionFactory;

/**
 * Feature Generator
 * 
 * @author osbornb
 */
public abstract class FeatureCoreGenerator {

	/**
	 * Logger
	 */
	private static final Logger LOGGER = Logger
			.getLogger(FeatureCoreGenerator.class.getName());

	/**
	 * GeoPackage
	 */
	protected final GeoPackageCore geoPackage;

	/**
	 * Table Name
	 */
	protected final String tableName;

	/**
	 * Features bounding box
	 */
	protected BoundingBox boundingBox;

	/**
	 * Bounding Box projection
	 */
	protected Projection boundingBoxProjection;

	/**
	 * Features projection
	 */
	protected Projection projection;

	/**
	 * Number of rows to save in a single transaction
	 */
	protected int transactionLimit = 1000;

	/**
	 * GeoPackage progress
	 */
	protected GeoPackageProgress progress;

	/**
	 * Table Geometry Columns
	 */
	protected GeometryColumns geometryColumns;

	/**
	 * Table columns
	 */
	protected Map<String, FeatureColumn> columns = new HashMap<>();

	/**
	 * Spatial Reference System
	 */
	protected SpatialReferenceSystem srs;

	/**
	 * Constructor
	 *
	 * @param geoPackage
	 *            GeoPackage
	 * @param tableName
	 *            table name
	 */
	public FeatureCoreGenerator(GeoPackageCore geoPackage, String tableName) {
		geoPackage.verifyWritable();
		this.geoPackage = geoPackage;
		this.tableName = tableName;
		setProjection(null);
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
	 * Get bounding box
	 * 
	 * @return bounding box
	 */
	public BoundingBox getBoundingBox() {
		return boundingBox;
	}

	/**
	 * Set the bounding box
	 * 
	 * @param boundingBox
	 *            bounding box
	 */
	public void setBoundingBox(BoundingBox boundingBox) {
		this.boundingBox = boundingBox;
	}

	/**
	 * Get the bounding box projection
	 * 
	 * @return bounding box projection
	 */
	public Projection getBoundingBoxProjection() {
		return boundingBoxProjection;
	}

	/**
	 * Set the bounding box projection
	 * 
	 * @param boundingBoxProjection
	 *            bounding box projection
	 */
	public void setBoundingBoxProjection(Projection boundingBoxProjection) {
		this.boundingBoxProjection = boundingBoxProjection;
	}

	/**
	 * Get the projection
	 * 
	 * @return projection
	 */
	public Projection getProjection() {
		return projection;
	}

	/**
	 * Set the projection
	 * 
	 * @param projection
	 *            projection
	 */
	public void setProjection(Projection projection) {
		if (projection == null) {
			this.projection = ProjectionFactory.getProjection(
					ProjectionConstants.EPSG_WORLD_GEODETIC_SYSTEM);
		} else {
			this.projection = projection;
		}
	}

	/**
	 * Get the single transaction limit
	 * 
	 * @return transaction limit
	 */
	public int getTransactionLimit() {
		return transactionLimit;
	}

	/**
	 * Set the single transaction limit
	 * 
	 * @param transactionLimit
	 *            transaction limit
	 */
	public void setTransactionLimit(int transactionLimit) {
		this.transactionLimit = transactionLimit;
	}

	/**
	 * Get the progress
	 * 
	 * @return progress
	 */
	public GeoPackageProgress getProgress() {
		return progress;
	}

	/**
	 * Set the progress
	 * 
	 * @param progress
	 *            progress
	 */
	public void setProgress(GeoPackageProgress progress) {
		this.progress = progress;
	}

	/**
	 * Determine if the feature generator should remain active
	 * 
	 * @return true if active
	 */
	public boolean isActive() {
		return progress == null || progress.isActive();
	}

	/**
	 * Get the geometry columns
	 * 
	 * @return geometry columns
	 */
	public GeometryColumns getGeometryColumns() {
		return geometryColumns;
	}

	/**
	 * Get the columns
	 * 
	 * @return columns
	 */
	public Map<String, FeatureColumn> getColumns() {
		return columns;
	}

	/**
	 * Get the Spatial Reference System
	 * 
	 * @return srs
	 */
	public SpatialReferenceSystem getSrs() {
		return srs;
	}

	/**
	 * Generate the features
	 * 
	 * @return generated count
	 * @throws SQLException
	 *             upon error
	 */
	public abstract int generateFeatures() throws SQLException;

	/**
	 * Add a new column
	 * 
	 * @param featureColumn
	 *            feature column
	 */
	protected abstract void addColumn(FeatureColumn featureColumn);

	/**
	 * Initialize after the feature table is created
	 */
	protected void initializeTable() {
		// Override if needed
	}

	/**
	 * Save the feature
	 * 
	 * @param geometry
	 *            geometry
	 * @param values
	 *            column to value mapping
	 */
	protected abstract void saveFeature(Geometry geometry,
			Map<String, Object> values);

	/**
	 * Create the feature
	 *
	 * @param geometry
	 *            geometry
	 * @param properties
	 *            properties
	 * @throws SQLException
	 *             upon error
	 */
	protected void createFeature(Geometry geometry,
			Map<String, Object> properties) throws SQLException {

		if (srs == null) {
			createSrs();
		}

		if (geometryColumns == null) {
			createTable(properties);
		}

		Map<String, Object> values = new HashMap<>();

		for (Entry<String, Object> property : properties.entrySet()) {
			String column = property.getKey();
			Object value = getValue(column, property.getValue());
			values.put(column, value);
		}

		saveFeature(geometry, values);

	}

	/**
	 * Create the Spatial Reference System
	 * 
	 * @throws SQLException
	 *             upon error
	 */
	public void createSrs() throws SQLException {

		SpatialReferenceSystemDao srsDao = geoPackage
				.getSpatialReferenceSystemDao();
		srs = srsDao.getOrCreateCode(projection.getAuthority(),
				Long.parseLong(projection.getCode()));

	}

	/**
	 * Create the feature table
	 * 
	 * @param properties
	 *            properties
	 * @throws SQLException
	 */
	protected void createTable(Map<String, Object> properties)
			throws SQLException {

		// Create a new geometry columns or update an existing
		GeometryColumnsDao geometryColumnsDao = geoPackage
				.getGeometryColumnsDao();
		if (geometryColumnsDao.isTableExists()) {
			geometryColumns = geometryColumnsDao.queryForTableName(tableName);
		}

		boolean inTransaction = geoPackage.inTransaction();
		if (inTransaction) {
			geoPackage.endTransaction();
		}

		if (geometryColumns == null) {

			List<FeatureColumn> featureColumns = new ArrayList<>();
			for (Entry<String, Object> property : properties.entrySet()) {
				String column = property.getKey();
				FeatureColumn featureColumn = createColumn(column,
						property.getValue());
				featureColumns.add(featureColumn);
				columns.put(column, featureColumn);
			}

			// Create the feature table
			GeometryColumns geomColumns = new GeometryColumns();
			geomColumns.setId(new TableColumnKey(tableName, "geometry"));
			geomColumns.setGeometryType(GeometryType.GEOMETRY);
			geomColumns.setZ((byte) 0);
			geomColumns.setM((byte) 0);
			geometryColumns = geoPackage.createFeatureTableWithMetadata(
					geomColumns, tableName + "_id", featureColumns, boundingBox,
					srs.getSrsId());

		} else {
			FeatureTableReader tableReader = new FeatureTableReader(
					geometryColumns);
			FeatureTable featureTable = tableReader
					.readTable(geoPackage.getDatabase());
			for (FeatureColumn featureColumn : featureTable.getColumns()) {
				columns.put(featureColumn.getName(), featureColumn);
			}
		}

		initializeTable();

		if (inTransaction) {
			geoPackage.beginTransaction();
		}

	}

	/**
	 * Get the column value
	 * 
	 * @param column
	 *            column name
	 * @param value
	 *            value
	 * @return column value
	 */
	protected Object getValue(String column, Object value) {

		FeatureColumn featureColumn = getColumn(column, value);
		Object columnValue = getValue(value, featureColumn.getDataType());

		return columnValue;
	}

	/**
	 * Get the column, create if needed
	 * 
	 * @param column
	 *            column name
	 * @param value
	 *            value
	 * @return feature column
	 */
	protected FeatureColumn getColumn(String column, Object value) {

		FeatureColumn featureColumn = columns.get(column);

		if (featureColumn == null) {
			boolean inTransaction = geoPackage.inTransaction();
			if (inTransaction) {
				geoPackage.endTransaction();
			}
			featureColumn = createColumn(column, value);
			addColumn(featureColumn);
			columns.put(column, featureColumn);
			if (inTransaction) {
				geoPackage.beginTransaction();
			}
		}

		return featureColumn;
	}

	/**
	 * Create a feature column
	 * 
	 * @param name
	 *            column name
	 * @param value
	 *            value
	 * @return feature column
	 */
	protected FeatureColumn createColumn(String name, Object value) {
		GeoPackageDataType type = getType(value);
		return FeatureColumn.createColumn(name, type);
	}

	/**
	 * Create the geometry data
	 * 
	 * @param geometry
	 *            geometry
	 * @return geometry data
	 */
	protected GeoPackageGeometryData createGeometryData(Geometry geometry) {
		GeoPackageGeometryData geometryData = new GeoPackageGeometryData(
				srs.getSrsId());
		geometryData.setGeometry(geometry);
		return geometryData;
	}

	/**
	 * Get the type for the object value
	 * 
	 * @param value
	 *            value
	 * @return data type
	 */
	public static GeoPackageDataType getType(Object value) {

		GeoPackageDataType type = null;

		if (value instanceof String) {
			type = GeoPackageDataType.TEXT;
		} else if (value instanceof Boolean) {
			type = GeoPackageDataType.BOOLEAN;
		} else if (value instanceof Byte) {
			type = GeoPackageDataType.TINYINT;
		} else if (value instanceof Short) {
			type = GeoPackageDataType.SMALLINT;
		} else if (value instanceof Integer) {
			type = GeoPackageDataType.MEDIUMINT;
		} else if (value instanceof Long) {
			type = GeoPackageDataType.INT;
		} else if (value instanceof Float) {
			type = GeoPackageDataType.FLOAT;
		} else if (value instanceof Double) {
			type = GeoPackageDataType.DOUBLE;
		} else if (value instanceof byte[]) {
			type = GeoPackageDataType.BLOB;
		}

		if (type == null) {
			type = GeoPackageDataType.TEXT;
		}

		return type;
	}

	/**
	 * Get the value for the object value with the data type
	 * 
	 * @param value
	 *            value
	 * @param type
	 *            data type
	 * @return default value
	 */
	public static Object getValue(Object value, GeoPackageDataType type) {

		if (value != null && type != null) {

			switch (type) {
			case TEXT:
			case DATE:
			case DATETIME:
				value = value.toString();
				break;
			case BOOLEAN:
				value = (Boolean) value;
				break;
			case TINYINT:
				value = (Byte) value;
				break;
			case SMALLINT:
				value = (Short) value;
				break;
			case MEDIUMINT:
				value = (Integer) value;
				break;
			case INT:
			case INTEGER:
				value = (Long) value;
				break;
			case FLOAT:
				value = (Float) value;
				break;
			case DOUBLE:
			case REAL:
				value = (Double) value;
				break;
			case BLOB:
				value = (byte[]) value;
				break;
			default:
				throw new GeoPackageException("Unsupported Data Type " + type);
			}

		}

		return value;
	}

	/**
	 * Determine if the projections map has the provided projection
	 * 
	 * @param projections
	 *            projections map
	 * @param projection
	 *            projection
	 * @return true if has projection
	 */
	public boolean hasProjection(
			Map<String, Map<String, Projection>> projections,
			Projection projection) {
		return getProjection(projections, projection) != null;
	}

	/**
	 * Get the projection
	 * 
	 * @param projections
	 *            projections map
	 * @param projection
	 *            projection
	 * @return projection or null
	 */
	public Projection getProjection(
			Map<String, Map<String, Projection>> projections,
			Projection projection) {
		return getProjection(projections, projection.getAuthority(),
				projection.getCode());
	}

	/**
	 * Determine if the projections map has the provided projection
	 * 
	 * @param projections
	 *            projections map
	 * @param authority
	 *            authority
	 * @param code
	 *            code
	 * @return true if has projection
	 */
	public boolean hasProjection(
			Map<String, Map<String, Projection>> projections, String authority,
			String code) {
		return getProjection(projections, authority, code) != null;
	}

	/**
	 * Get the projection
	 * 
	 * @param projections
	 *            projections map
	 * @param authority
	 *            authority
	 * @param code
	 *            code
	 * @return projection or null
	 */
	public Projection getProjection(
			Map<String, Map<String, Projection>> projections, String authority,
			String code) {
		Projection projection = null;
		Map<String, Projection> authorityProjections = projections
				.get(authority);
		if (authorityProjections != null) {
			projection = authorityProjections.get(code);
		}
		return projection;
	}

	/**
	 * Add a projection
	 * 
	 * @param projections
	 *            projections map
	 * @param authority
	 *            authority
	 * @param code
	 *            code
	 */
	protected void addProjection(
			Map<String, Map<String, Projection>> projections, String authority,
			String code) {

		Map<String, Projection> authorityProjections = projections
				.get(authority);
		if (authorityProjections == null) {
			authorityProjections = new HashMap<>();
			projections.put(authority, authorityProjections);
		}

		try {
			Projection projection = ProjectionFactory.getProjection(authority,
					code);
			authorityProjections.put(code, projection);
		} catch (Exception e) {
			LOGGER.log(Level.WARNING, "Unable to create projection. Authority: "
					+ authority + ", Code: " + code);
		}

	}

}
