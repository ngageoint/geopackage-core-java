package mil.nga.geopackage.features.user;

import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.db.GeoPackageDataType;
import mil.nga.geopackage.db.table.TableColumn;
import mil.nga.geopackage.user.UserColumn;
import mil.nga.geopackage.user.UserTable;
import mil.nga.sf.GeometryType;

/**
 * Feature column
 * 
 * @author osbornb
 */
public class FeatureColumn extends UserColumn {

	/**
	 * Geometry type if a geometry column
	 */
	private final GeometryType geometryType;

	/**
	 * Create a new primary key column
	 * 
	 * @param name
	 *            name
	 * @return feature column
	 * @since 3.3.0
	 */
	public static FeatureColumn createPrimaryKeyColumn(String name) {
		return createPrimaryKeyColumn(name, UserTable.DEFAULT_AUTOINCREMENT);
	}

	/**
	 * Create a new primary key column
	 * 
	 * @param name
	 *            name
	 * @param autoincrement
	 *            autoincrement flag
	 * @return feature column
	 * @since 4.0.0
	 */
	public static FeatureColumn createPrimaryKeyColumn(String name,
			boolean autoincrement) {
		return createPrimaryKeyColumn(NO_INDEX, name, autoincrement);
	}

	/**
	 * Create a new primary key column
	 * 
	 * @param index
	 *            index
	 * @param name
	 *            name
	 * @return feature column
	 */
	public static FeatureColumn createPrimaryKeyColumn(int index, String name) {
		return createPrimaryKeyColumn(index, name,
				UserTable.DEFAULT_AUTOINCREMENT);
	}

	/**
	 * Create a new primary key column
	 * 
	 * @param index
	 *            index
	 * @param name
	 *            name
	 * @param autoincrement
	 *            autoincrement flag
	 * @return feature column
	 * @since 4.0.0
	 */
	public static FeatureColumn createPrimaryKeyColumn(int index, String name,
			boolean autoincrement) {
		return new FeatureColumn(index, name, GeoPackageDataType.INTEGER, null,
				true, null, true, autoincrement, null);
	}

	/**
	 * Create a new geometry column
	 * 
	 * @param name
	 *            name
	 * @param type
	 *            geometry type
	 * @return feature column
	 * @since 3.3.0
	 */
	public static FeatureColumn createGeometryColumn(String name,
			GeometryType type) {
		return createGeometryColumn(NO_INDEX, name, type);
	}

	/**
	 * Create a new geometry column
	 * 
	 * @param index
	 *            index
	 * @param name
	 *            name
	 * @param type
	 *            geometry type
	 * @return feature column
	 * @since 3.3.0
	 */
	public static FeatureColumn createGeometryColumn(int index, String name,
			GeometryType type) {
		return createGeometryColumn(index, name, type, false, null);
	}

	/**
	 * Create a new geometry column
	 * 
	 * @param name
	 *            name
	 * @param type
	 *            geometry type
	 * @param notNull
	 *            not null flag
	 * @param defaultValue
	 *            default value
	 * @return feature column
	 * @since 3.3.0
	 */
	public static FeatureColumn createGeometryColumn(String name,
			GeometryType type, boolean notNull, Object defaultValue) {
		return createGeometryColumn(NO_INDEX, name, type, false, null);
	}

	/**
	 * Create a new geometry column
	 * 
	 * @param index
	 *            index
	 * @param name
	 *            name
	 * @param type
	 *            geometry type
	 * @param notNull
	 *            not null flag
	 * @param defaultValue
	 *            default value
	 * @return feature column
	 */
	public static FeatureColumn createGeometryColumn(int index, String name,
			GeometryType type, boolean notNull, Object defaultValue) {
		if (type == null) {
			throw new GeoPackageException(
					"Geometry Type is required to create geometry column: "
							+ name);
		}
		return new FeatureColumn(index, name, GeoPackageDataType.BLOB, null,
				notNull, defaultValue, false, false, type);
	}

	/**
	 * Create a new column
	 * 
	 * @param name
	 *            name
	 * @param type
	 *            data type
	 * @return feature column
	 * @since 3.3.0
	 */
	public static FeatureColumn createColumn(String name,
			GeoPackageDataType type) {
		return createColumn(NO_INDEX, name, type);
	}

	/**
	 * Create a new column
	 * 
	 * @param index
	 *            index
	 * @param name
	 *            name
	 * @param type
	 *            data type
	 * @return feature column
	 * @since 3.3.0
	 */
	public static FeatureColumn createColumn(int index, String name,
			GeoPackageDataType type) {
		return createColumn(index, name, type, false, null);
	}

	/**
	 * Create a new column
	 * 
	 * @param name
	 *            name
	 * @param type
	 *            data type
	 * @param notNull
	 *            not null flag
	 * @return feature column
	 * @since 3.3.0
	 */
	public static FeatureColumn createColumn(String name,
			GeoPackageDataType type, boolean notNull) {
		return createColumn(NO_INDEX, name, type, notNull);
	}

	/**
	 * Create a new column
	 * 
	 * @param index
	 *            index
	 * @param name
	 *            name
	 * @param type
	 *            data type
	 * @param notNull
	 *            not null flag
	 * @return feature column
	 * @since 3.3.0
	 */
	public static FeatureColumn createColumn(int index, String name,
			GeoPackageDataType type, boolean notNull) {
		return createColumn(index, name, type, notNull, null);
	}

	/**
	 * Create a new column
	 * 
	 * @param name
	 *            name
	 * @param type
	 *            data type
	 * @param notNull
	 *            not null flag
	 * @param defaultValue
	 *            default value
	 * @return feature column
	 * @since 3.3.0
	 */
	public static FeatureColumn createColumn(String name,
			GeoPackageDataType type, boolean notNull, Object defaultValue) {
		return createColumn(NO_INDEX, name, type, notNull, defaultValue);
	}

	/**
	 * Create a new column
	 * 
	 * @param index
	 *            index
	 * @param name
	 *            name
	 * @param type
	 *            data type
	 * @param notNull
	 *            not null flag
	 * @param defaultValue
	 *            default value
	 * @return feature column
	 */
	public static FeatureColumn createColumn(int index, String name,
			GeoPackageDataType type, boolean notNull, Object defaultValue) {
		return createColumn(index, name, type, null, notNull, defaultValue);
	}

	/**
	 * Create a new column
	 * 
	 * @param name
	 *            name
	 * @param type
	 *            data type
	 * @param max
	 *            max value
	 * @return feature column
	 * @since 3.3.0
	 */
	public static FeatureColumn createColumn(String name,
			GeoPackageDataType type, Long max) {
		return createColumn(NO_INDEX, name, type, max);
	}

	/**
	 * Create a new column
	 * 
	 * @param index
	 *            index
	 * @param name
	 *            name
	 * @param type
	 *            data type
	 * @param max
	 *            max value
	 * @return feature column
	 * @since 3.3.0
	 */
	public static FeatureColumn createColumn(int index, String name,
			GeoPackageDataType type, Long max) {
		return createColumn(index, name, type, max, false, null);
	}

	/**
	 * Create a new column
	 * 
	 * @param name
	 *            name
	 * @param type
	 *            data type
	 * @param max
	 *            max value
	 * @param notNull
	 *            not null flag
	 * @param defaultValue
	 *            default value
	 * @return feature column
	 * @since 3.3.0
	 */
	public static FeatureColumn createColumn(String name,
			GeoPackageDataType type, Long max, boolean notNull,
			Object defaultValue) {
		return createColumn(NO_INDEX, name, type, max, notNull, defaultValue);
	}

	/**
	 * Create a new column
	 * 
	 * @param index
	 *            index
	 * @param name
	 *            name
	 * @param type
	 *            data type
	 * @param max
	 *            max value
	 * @param notNull
	 *            not null flag
	 * @param defaultValue
	 *            default value
	 * @return feature column
	 */
	public static FeatureColumn createColumn(int index, String name,
			GeoPackageDataType type, Long max, boolean notNull,
			Object defaultValue) {
		return new FeatureColumn(index, name, type, max, notNull, defaultValue,
				false, false, null);
	}

	/**
	 * Create a new column
	 * 
	 * @param tableColumn
	 *            table column
	 * @return feature column
	 * @since 3.3.0
	 */
	public static FeatureColumn createColumn(TableColumn tableColumn) {
		return new FeatureColumn(tableColumn);
	}

	/**
	 * Constructor
	 * 
	 * @param index
	 *            index
	 * @param name
	 *            name
	 * @param dataType
	 *            data type
	 * @param max
	 *            max value
	 * @param notNull
	 *            not null flag
	 * @param defaultValue
	 *            default value
	 * @param primaryKey
	 *            primary key flag
	 * @param autoincrement
	 *            autoincrement flag
	 * @param geometryType
	 *            geometry type
	 */
	private FeatureColumn(int index, String name, GeoPackageDataType dataType,
			Long max, boolean notNull, Object defaultValue, boolean primaryKey,
			boolean autoincrement, GeometryType geometryType) {
		super(index, name, getTypeName(name, dataType, geometryType), dataType,
				max, notNull, defaultValue, primaryKey, autoincrement);
		this.geometryType = geometryType;
	}

	/**
	 * Constructor
	 * 
	 * @param tableColumn
	 *            table column
	 */
	private FeatureColumn(TableColumn tableColumn) {
		super(tableColumn);
		this.geometryType = getGeometryType(tableColumn);
	}

	/**
	 * Get the type name from the data and geometry type
	 * 
	 * @param name
	 *            column name
	 * @param dataType
	 *            data type
	 * @param geometryType
	 *            geometry type
	 * @return type name
	 * @since 3.3.0
	 */
	protected static String getTypeName(String name,
			GeoPackageDataType dataType, GeometryType geometryType) {
		String type;
		if (geometryType != null) {
			type = geometryType.name();
		} else {
			type = getTypeName(name, dataType);
		}
		return type;
	}

	/**
	 * Attempt to get the geometry type of the table column
	 * 
	 * @param tableColumn
	 *            table column
	 * @return geometry type
	 */
	private static GeometryType getGeometryType(TableColumn tableColumn) {
		GeometryType geometryType = null;
		if (tableColumn.isDataType(GeoPackageDataType.BLOB)) {
			geometryType = GeometryType.findName(tableColumn.getType());
		}
		return geometryType;
	}

	/**
	 * Copy Constructor
	 * 
	 * @param featureColumn
	 *            feature column
	 * @since 3.3.0
	 */
	public FeatureColumn(FeatureColumn featureColumn) {
		super(featureColumn);
		this.geometryType = featureColumn.geometryType;
	}

	/**
	 * Copy the column
	 * 
	 * @return copied column
	 * @since 3.3.0
	 */
	public FeatureColumn copy() {
		return new FeatureColumn(this);
	}

	/**
	 * Determine if this column is a geometry
	 * 
	 * @return true if a geometry column
	 */
	public boolean isGeometry() {
		return geometryType != null;
	}

	/**
	 * When a geometry column, gets the geometry type
	 * 
	 * @return geometry type
	 */
	public GeometryType getGeometryType() {
		return geometryType;
	}

}
