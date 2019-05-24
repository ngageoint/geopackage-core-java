package mil.nga.geopackage.db.table;

import mil.nga.geopackage.db.GeoPackageDataType;

/**
 * Table Column from Table Info
 * 
 * @author osbornb
 * @since 3.3.0
 */
public class TableColumn {

	/**
	 * Column index
	 */
	private final int index;

	/**
	 * Column name
	 */
	private final String name;

	/**
	 * Column type
	 */
	private final String type;

	/**
	 * Column data type
	 */
	private final GeoPackageDataType dataType;

	/**
	 * Column max value
	 */
	private final Long max;

	/**
	 * Column not null flag
	 */
	private final boolean notNull;

	/**
	 * Default value as a string
	 */
	private final String defaultValueString;

	/**
	 * Default value
	 */
	private final Object defaultValue;

	/**
	 * Primary key flag
	 */
	private final boolean primarykey;

	/**
	 * Constructor
	 * 
	 * @param index
	 *            column index
	 * @param name
	 *            column name
	 * @param type
	 *            column type
	 * @param dataType
	 *            column data type
	 * @param max
	 *            max value
	 * @param notNull
	 *            not null flag
	 * @param defaultValueString
	 *            default value as a string
	 * @param defaultValue
	 *            default value
	 * @param primaryKey
	 *            primary key flag
	 */
	TableColumn(int index, String name, String type,
			GeoPackageDataType dataType, Long max, boolean notNull,
			String defaultValueString, Object defaultValue,
			boolean primaryKey) {
		this.index = index;
		this.name = name;
		this.type = type;
		this.dataType = dataType;
		this.max = max;
		this.notNull = notNull;
		this.defaultValueString = defaultValueString;
		this.defaultValue = defaultValue;
		this.primarykey = primaryKey;
	}

	/**
	 * Get the column index
	 * 
	 * @return column index
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * Get the column name
	 * 
	 * @return column name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get the column type
	 * 
	 * @return column type
	 */
	public String getType() {
		return type;
	}

	/**
	 * Get the column data type
	 * 
	 * @return column data type, may be null
	 */
	public GeoPackageDataType getDataType() {
		return dataType;
	}

	/**
	 * Is the column the data type
	 * 
	 * @param dataType
	 *            data type
	 * @return true if the data type
	 */
	public boolean isDataType(GeoPackageDataType dataType) {
		return this.dataType != null && this.dataType == dataType;
	}

	/**
	 * Get the column max value
	 * 
	 * @return max value or null if no max
	 */
	public Long getMax() {
		return max;
	}

	/**
	 * Is this a not null column?
	 * 
	 * @return true if not nullable
	 */
	public boolean isNotNull() {
		return notNull;
	}

	/**
	 * Get the default value as a string
	 * 
	 * @return default value as a string
	 */
	public String getDefaultValueString() {
		return defaultValueString;
	}

	/**
	 * Get the default value
	 * 
	 * @return default value
	 */
	public Object getDefaultValue() {
		return defaultValue;
	}

	/**
	 * Is this a primary key column?
	 * 
	 * @return true if primary key column
	 */
	public boolean isPrimarykey() {
		return primarykey;
	}

}
