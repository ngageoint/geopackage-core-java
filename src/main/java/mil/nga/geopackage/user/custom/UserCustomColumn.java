package mil.nga.geopackage.user.custom;

import mil.nga.geopackage.db.GeoPackageDataType;
import mil.nga.geopackage.db.table.TableColumn;
import mil.nga.geopackage.user.UserColumn;
import mil.nga.geopackage.user.UserTable;

/**
 * User Custom column
 * 
 * @author osbornb
 * @since 3.0.1
 */
public class UserCustomColumn extends UserColumn {

	/**
	 * Create a new primary key column
	 * 
	 * @param name
	 *            name
	 * @return user custom column
	 * @since 3.3.0
	 */
	public static UserCustomColumn createPrimaryKeyColumn(String name) {
		return createPrimaryKeyColumn(name, UserTable.DEFAULT_AUTOINCREMENT);
	}

	/**
	 * Create a new primary key column
	 * 
	 * @param name
	 *            name
	 * @param autoincrement
	 *            autoincrement flag
	 * @return user custom column
	 * @since 4.0.0
	 */
	public static UserCustomColumn createPrimaryKeyColumn(String name,
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
	 * @return user custom column
	 */
	public static UserCustomColumn createPrimaryKeyColumn(int index,
			String name) {
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
	 * @return user custom column
	 * @since 4.0.0
	 */
	public static UserCustomColumn createPrimaryKeyColumn(int index,
			String name, boolean autoincrement) {
		return new UserCustomColumn(index, name, GeoPackageDataType.INTEGER,
				null, true, null, true, autoincrement);
	}

	/**
	 * Create a new column
	 * 
	 * @param name
	 *            name
	 * @param type
	 *            data type
	 * @return user custom column
	 * @since 3.3.0
	 */
	public static UserCustomColumn createColumn(String name,
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
	 * @return user custom column
	 * @since 3.3.0
	 */
	public static UserCustomColumn createColumn(int index, String name,
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
	 * @return user custom column
	 * @since 3.3.0
	 */
	public static UserCustomColumn createColumn(String name,
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
	 * @return user custom column
	 * @since 3.3.0
	 */
	public static UserCustomColumn createColumn(int index, String name,
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
	 * @return user custom column
	 * @since 3.3.0
	 */
	public static UserCustomColumn createColumn(String name,
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
	 *            type
	 * @param notNull
	 *            not null flag
	 * @param defaultValue
	 *            default value
	 * @return user custom column
	 */
	public static UserCustomColumn createColumn(int index, String name,
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
	 * @return user custom column
	 * @since 3.3.0
	 */
	public static UserCustomColumn createColumn(String name,
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
	 * @return user custom column
	 * @since 3.3.0
	 */
	public static UserCustomColumn createColumn(int index, String name,
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
	 * @return user custom column
	 * @since 3.3.0
	 */
	public static UserCustomColumn createColumn(String name,
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
	 *            type
	 * @param max
	 *            max value
	 * @param notNull
	 *            not null flag
	 * @param defaultValue
	 *            default value
	 * @return user custom column
	 */
	public static UserCustomColumn createColumn(int index, String name,
			GeoPackageDataType type, Long max, boolean notNull,
			Object defaultValue) {
		return new UserCustomColumn(index, name, type, max, notNull,
				defaultValue, false, false);
	}

	/**
	 * Create a new column
	 * 
	 * @param tableColumn
	 *            table column
	 * @return user custom column
	 * @since 3.3.0
	 */
	public static UserCustomColumn createColumn(TableColumn tableColumn) {
		return new UserCustomColumn(tableColumn);
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
	 * @param autoincrement
	 *            autoincrement flag
	 */
	private UserCustomColumn(int index, String name,
			GeoPackageDataType dataType, Long max, boolean notNull,
			Object defaultValue, boolean primaryKey, boolean autoincrement) {
		super(index, name, dataType, max, notNull, defaultValue, primaryKey,
				autoincrement);
	}

	/**
	 * Constructor
	 * 
	 * @param tableColumn
	 *            table column
	 */
	private UserCustomColumn(TableColumn tableColumn) {
		super(tableColumn);
	}

	/**
	 * Copy Constructor
	 * 
	 * @param userCustomColumn
	 *            user custom column
	 * @since 3.3.0
	 */
	public UserCustomColumn(UserCustomColumn userCustomColumn) {
		super(userCustomColumn);
	}

	/**
	 * Copy the column
	 * 
	 * @return copied column
	 * @since 3.3.0
	 */
	public UserCustomColumn copy() {
		return new UserCustomColumn(this);
	}

}
