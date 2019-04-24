package mil.nga.geopackage.db;

import mil.nga.geopackage.user.UserColumn;

/**
 * Mapped column, to a column and potentially from a differently named column
 * 
 * @author osbornb
 * @since 3.2.1
 */
public class MappedColumn {

	/**
	 * To column
	 */
	private String toColumn;

	/**
	 * From column or null if the same as to column
	 */
	private String fromColumn;

	/**
	 * Default to column value
	 */
	private Object defaultValue;

	/**
	 * Column data type
	 */
	private GeoPackageDataType dataType;

	/**
	 * Constructor
	 * 
	 * @param toColumn
	 *            to Column
	 */
	public MappedColumn(String toColumn) {
		this.toColumn = toColumn;
	}

	/**
	 * Constructor
	 * 
	 * @param toColumn
	 *            to column
	 * @param fromColumn
	 *            from column
	 */
	public MappedColumn(String toColumn, String fromColumn) {
		this(toColumn);
		this.fromColumn = fromColumn;
	}

	/**
	 * Constructor
	 * 
	 * @param toColumn
	 *            to column
	 * @param fromColumn
	 *            from column
	 * @param defaultValue
	 *            default value
	 * @param dataType
	 *            data type
	 */
	public MappedColumn(String toColumn, String fromColumn,
			Object defaultValue, GeoPackageDataType dataType) {
		this(toColumn, fromColumn);
		this.defaultValue = defaultValue;
		this.dataType = dataType;
	}

	/**
	 * Constructor
	 * 
	 * @param toColumn
	 *            to column
	 * @param defaultValue
	 *            default value
	 * @param dataType
	 *            data type
	 */
	public MappedColumn(String toColumn, Object defaultValue,
			GeoPackageDataType dataType) {
		this(toColumn, null, defaultValue, dataType);
	}

	/**
	 * Constructor
	 * 
	 * @param column
	 *            user column
	 */
	public MappedColumn(UserColumn column) {
		this(column.getName(), column.getDefaultValue(), column.getDataType());
	}

	/**
	 * Get the to column
	 * 
	 * @return to column
	 */
	public String getToColumn() {
		return toColumn;
	}

	/**
	 * Set the to column
	 * 
	 * @param toColumn
	 *            to column
	 */
	public void setToColumn(String toColumn) {
		this.toColumn = toColumn;
	}

	/**
	 * Get the from column
	 * 
	 * @return from column
	 */
	public String getFromColumn() {
		String column = fromColumn;
		if (column == null) {
			column = toColumn;
		}
		return column;
	}

	/**
	 * Set the from column
	 * 
	 * @param fromColumn
	 *            from column
	 */
	public void setFromColumn(String fromColumn) {
		this.fromColumn = fromColumn;
	}

	/**
	 * Check if the column has a default value
	 * 
	 * @return true if has a default value
	 */
	public boolean hasDefaultValue() {
		return defaultValue != null;
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
	 * Get the default value as a string
	 * 
	 * @return default value as string
	 */
	public String getDefaultValueAsString() {
		return CoreSQLUtils.columnDefaultValue(defaultValue, dataType);
	}

	/**
	 * Set the default value
	 * 
	 * @param defaultValue
	 *            default value
	 */
	public void setDefaultValue(Object defaultValue) {
		this.defaultValue = defaultValue;
	}

	/**
	 * Get the data type
	 * 
	 * @return data type
	 */
	public GeoPackageDataType getDataType() {
		return dataType;
	}

	/**
	 * Set the data type
	 * 
	 * @param dataType
	 *            data type
	 */
	public void setDataType(GeoPackageDataType dataType) {
		this.dataType = dataType;
	}

}
