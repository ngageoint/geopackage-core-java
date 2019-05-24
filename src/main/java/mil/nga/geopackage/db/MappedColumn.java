package mil.nga.geopackage.db;

import mil.nga.geopackage.db.table.TableColumn;
import mil.nga.geopackage.user.UserColumn;

/**
 * Mapped column, to a column and potentially from a differently named column
 * 
 * @author osbornb
 * @since 3.3.0
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
	 * Constant value
	 */
	private Object constantValue;

	/**
	 * Where value
	 */
	private Object whereValue;

	/**
	 * Where value comparison operator (=, <, etc)
	 */
	private String whereOperator;

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
	public MappedColumn(String toColumn, String fromColumn, Object defaultValue,
			GeoPackageDataType dataType) {
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
	 * Constructor
	 * 
	 * @param column
	 *            table column
	 */
	public MappedColumn(TableColumn column) {
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
	 * Determine if the column has a new name
	 * 
	 * @return true if the to and from column names are different
	 */
	public boolean hasNewName() {
		return fromColumn != null && !fromColumn.equals(toColumn);
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

	/**
	 * Check if the column has a constant value
	 * 
	 * @return true if has a constant value
	 */
	public boolean hasConstantValue() {
		return constantValue != null;
	}

	/**
	 * Get the constant value
	 * 
	 * @return constant value
	 */
	public Object getConstantValue() {
		return constantValue;
	}

	/**
	 * Get the constant value as a string
	 * 
	 * @return constant value as string
	 */
	public String getConstantValueAsString() {
		return CoreSQLUtils.columnDefaultValue(constantValue, dataType);
	}

	/**
	 * Set the constant value
	 * 
	 * @param constantValue
	 *            constant value
	 */
	public void setConstantValue(Object constantValue) {
		this.constantValue = constantValue;
	}

	/**
	 * Check if the column has a where value
	 * 
	 * @return true if has a where value
	 */
	public boolean hasWhereValue() {
		return whereValue != null;
	}

	/**
	 * Get the where value
	 * 
	 * @return where value
	 */
	public Object getWhereValue() {
		return whereValue;
	}

	/**
	 * Get the where value as a string
	 * 
	 * @return where value as string
	 */
	public String getWhereValueAsString() {
		return CoreSQLUtils.columnDefaultValue(whereValue, dataType);
	}

	/**
	 * Set the where value
	 * 
	 * @param whereValue
	 *            where value
	 */
	public void setWhereValue(Object whereValue) {
		this.whereValue = whereValue;
	}

	/**
	 * Set the where value
	 * 
	 * @param whereValue
	 *            where value
	 * @param whereOperator
	 *            where operator
	 */
	public void setWhereValue(Object whereValue, String whereOperator) {
		this.whereValue = whereValue;
		setWhereOperator(whereOperator);
	}

	/**
	 * Get the where operator
	 * 
	 * @return where operator
	 */
	public String getWhereOperator() {
		return whereOperator != null ? whereOperator : "=";
	}

	/**
	 * Set the where operator
	 * 
	 * @param whereOperator
	 *            where operator
	 */
	public void setWhereOperator(String whereOperator) {
		this.whereOperator = whereOperator;
	}

}
