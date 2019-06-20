package mil.nga.geopackage.features;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import mil.nga.geopackage.BoundingBox;
import mil.nga.geopackage.GeoPackageCore;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.core.srs.SpatialReferenceSystem;
import mil.nga.geopackage.core.srs.SpatialReferenceSystemDao;
import mil.nga.geopackage.db.GeoPackageDataType;
import mil.nga.geopackage.features.columns.GeometryColumns;
import mil.nga.geopackage.features.columns.GeometryColumnsDao;
import mil.nga.geopackage.features.user.FeatureColumn;
import mil.nga.geopackage.geom.GeoPackageGeometryData;
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
	 * Tiles projection
	 */
	protected Projection projection = ProjectionFactory
			.getProjection(ProjectionConstants.EPSG_WORLD_GEODETIC_SYSTEM);

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
		this.projection = projection;
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
	 * @return geometry columns
	 * @throws SQLException
	 */
	protected GeometryColumns createTable(Map<String, Object> properties)
			throws SQLException {

		// Create a new geometry columns or update an existing
		GeometryColumnsDao geometryColumnsDao = geoPackage
				.getGeometryColumnsDao();
		GeometryColumns geometryColumns = null;
		if (geometryColumnsDao.isTableExists()) {
			geometryColumns = geometryColumnsDao.queryForTableName(tableName);
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
			geometryColumns = new GeometryColumns();
			geometryColumns.setId(new TableColumnKey(tableName, "geometry"));
			geometryColumns.setGeometryType(GeometryType.GEOMETRY);
			geometryColumns.setZ((byte) 0);
			geometryColumns.setM((byte) 0);
			geometryColumns = geoPackage.createFeatureTableWithMetadata(
					geometryColumns, tableName + "_id", featureColumns,
					boundingBox, srs.getSrsId());
		}

		return geometryColumns;
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
			featureColumn = createColumn(column, value);
			addColumn(featureColumn);
			columns.put(column, featureColumn);
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

}
