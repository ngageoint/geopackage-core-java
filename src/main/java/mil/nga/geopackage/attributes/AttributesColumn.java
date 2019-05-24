package mil.nga.geopackage.attributes;

import mil.nga.geopackage.db.GeoPackageDataType;
import mil.nga.geopackage.db.table.TableColumn;
import mil.nga.geopackage.user.UserColumn;

/**
 * Attributes column
 * 
 * @author osbornb
 * @since 1.2.1
 */
public class AttributesColumn extends UserColumn {

	/**
	 * Create a new primary key column
	 * 
	 * @param name
	 *            name
	 * @return attributes column
	 * @since 3.3.0
	 */
	public static AttributesColumn createPrimaryKeyColumn(String name) {
		return createPrimaryKeyColumn(NO_INDEX, name);
	}

	/**
	 * Create a new primary key column
	 * 
	 * @param index
	 *            index
	 * @param name
	 *            name
	 * @return attributes column
	 */
	public static AttributesColumn createPrimaryKeyColumn(int index,
			String name) {
		return new AttributesColumn(index, name, GeoPackageDataType.INTEGER,
				null, true, null, true);
	}

	/**
	 * Create a new column
	 * 
	 * @param name
	 *            name
	 * @param type
	 *            data type
	 * @return attributes column
	 * @since 3.3.0
	 */
	public static AttributesColumn createColumn(String name,
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
	 * @return attributes column
	 * @since 3.3.0
	 */
	public static AttributesColumn createColumn(int index, String name,
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
	 * @return attributes column
	 * @since 3.3.0
	 */
	public static AttributesColumn createColumn(String name,
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
	 * @return attributes column
	 * @since 3.3.0
	 */
	public static AttributesColumn createColumn(int index, String name,
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
	 * @return attributes column
	 * @since 3.3.0
	 */
	public static AttributesColumn createColumn(String name,
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
	 * @return attributes column
	 */
	public static AttributesColumn createColumn(int index, String name,
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
	 * @return attributes column
	 * @since 3.3.0
	 */
	public static AttributesColumn createColumn(String name,
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
	 * @return attributes column
	 * @since 3.3.0
	 */
	public static AttributesColumn createColumn(int index, String name,
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
	 * @return attributes column
	 * @since 3.3.0
	 */
	public static AttributesColumn createColumn(String name,
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
	 * @return attributes column
	 */
	public static AttributesColumn createColumn(int index, String name,
			GeoPackageDataType type, Long max, boolean notNull,
			Object defaultValue) {
		return new AttributesColumn(index, name, type, max, notNull,
				defaultValue, false);
	}

	/**
	 * Create a new column
	 * 
	 * @param tableColumn
	 *            table column
	 * @return attributes column
	 * @since 3.3.0
	 */
	public static AttributesColumn createColumn(TableColumn tableColumn) {
		return new AttributesColumn(tableColumn);
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
	 */
	private AttributesColumn(int index, String name,
			GeoPackageDataType dataType, Long max, boolean notNull,
			Object defaultValue, boolean primaryKey) {
		super(index, name, dataType, max, notNull, defaultValue, primaryKey);
	}

	/**
	 * Constructor
	 * 
	 * @param tableColumn
	 *            table column
	 */
	private AttributesColumn(TableColumn tableColumn) {
		super(tableColumn);
	}

	/**
	 * Copy Constructor
	 * 
	 * @param attributesColumn
	 *            attributes column
	 * @since 3.3.0
	 */
	public AttributesColumn(AttributesColumn attributesColumn) {
		super(attributesColumn);
	}

	/**
	 * Copy the column
	 * 
	 * @return copied column
	 * @since 3.3.0
	 */
	public AttributesColumn copy() {
		return new AttributesColumn(this);
	}

}
