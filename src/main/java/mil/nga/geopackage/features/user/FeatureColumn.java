package mil.nga.geopackage.features.user;

import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.db.GeoPackageDataType;
import mil.nga.geopackage.user.UserColumn;
import mil.nga.wkb.geom.GeometryType;

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
	 * @param index
	 *            index
	 * @param name
	 *            name
	 * @return feature column
	 */
	public static FeatureColumn createPrimaryKeyColumn(int index, String name) {
		return new FeatureColumn(index, name, GeoPackageDataType.INTEGER, null,
				true, null, true, null);
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
				notNull, defaultValue, false, type);
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
				false, null);
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
	 * @param geometryType
	 *            geometry type
	 */
	FeatureColumn(int index, String name, GeoPackageDataType dataType,
			Long max, boolean notNull, Object defaultValue, boolean primaryKey,
			GeometryType geometryType) {
		super(index, name, dataType, max, notNull, defaultValue, primaryKey);
		this.geometryType = geometryType;
		if (dataType == null) {
			throw new GeoPackageException(
					"Data Type is required to create column: " + name);
		}
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

	/**
	 * {@inheritDoc}
	 * 
	 * Either the geometry or data type
	 */
	@Override
	public String getTypeName() {
		String type;
		if (isGeometry()) {
			type = geometryType.name();
		} else {
			type = super.getTypeName();
		}
		return type;
	}

}
