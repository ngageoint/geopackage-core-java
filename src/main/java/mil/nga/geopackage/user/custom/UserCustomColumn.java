package mil.nga.geopackage.user.custom;

import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.db.GeoPackageDataType;
import mil.nga.geopackage.user.UserColumn;

/**
 * User Custom column
 * 
 * @author osbornb
 * @since 3.0.1
 */
public class UserCustomColumn extends UserColumn {

	/**
	 * Create a new column
	 * 
	 * @param index
	 *            index
	 * @param name
	 *            name
	 * @param type
	 *            type
	 * @param notNull
	 *            not null flag
	 * @param defaultValue
	 *            default value
	 * @return tile column
	 */
	public static UserCustomColumn createColumn(int index, String name,
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
	 *            type
	 * @param max
	 *            max value
	 * @param notNull
	 *            not null flag
	 * @param defaultValue
	 *            default value
	 * @return tile column
	 */
	public static UserCustomColumn createColumn(int index, String name,
			GeoPackageDataType type, Long max, boolean notNull,
			Object defaultValue) {
		return new UserCustomColumn(index, name, type, max, notNull,
				defaultValue, false);
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
	 *            primary key
	 */
	UserCustomColumn(int index, String name, GeoPackageDataType dataType,
			Long max, boolean notNull, Object defaultValue, boolean primaryKey) {
		super(index, name, dataType, max, notNull, defaultValue, primaryKey);
		if (dataType == null) {
			throw new GeoPackageException(
					"Data Type is required to create column: " + name);
		}
	}

}
