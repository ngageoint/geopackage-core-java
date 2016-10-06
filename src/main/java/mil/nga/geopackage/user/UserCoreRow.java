package mil.nga.geopackage.user;

import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.db.GeoPackageDataType;

/**
 * User Row containing the values from a single cursor row
 * 
 * @param <TColumn>
 * @param <TTable>
 * 
 * @author osbornb
 */
public abstract class UserCoreRow<TColumn extends UserColumn, TTable extends UserTable<TColumn>> {

	/**
	 * User table
	 */
	protected final TTable table;

	/**
	 * Cursor column types of this row, based upon the data values
	 */
	protected final int[] columnTypes;

	/**
	 * Array of row values
	 */
	protected final Object[] values;

	/**
	 * Constructor
	 * 
	 * @param table
	 * @param columnTypes
	 * @param values
	 */
	protected UserCoreRow(TTable table, int[] columnTypes, Object[] values) {
		this.table = table;
		this.columnTypes = columnTypes;
		this.values = values;
	}

	/**
	 * Constructor to create an empty row
	 * 
	 * @param table
	 */
	protected UserCoreRow(TTable table) {
		this.table = table;
		// Default column types will all be 0 which is null
		// (Cursor.FIELD_TYPE_NULL)
		this.columnTypes = new int[table.columnCount()];
		this.values = new Object[table.columnCount()];
	}

	/**
	 * Get the column count
	 * 
	 * @return column count
	 */
	public int columnCount() {
		return table.columnCount();
	}

	/**
	 * Get the column names
	 * 
	 * @return column names
	 */
	public String[] getColumnNames() {
		return table.getColumnNames();
	}

	/**
	 * Get the column name at the index
	 * 
	 * @param index
	 * @return column name
	 */
	public String getColumnName(int index) {
		return table.getColumnName(index);
	}

	/**
	 * Get the index of the column name
	 * 
	 * @param columnName
	 * @return column index
	 */
	public int getColumnIndex(String columnName) {
		return table.getColumnIndex(columnName);
	}

	/**
	 * Get the value at the index
	 * 
	 * @param index
	 * @return value
	 */
	public Object getValue(int index) {
		return values[index];
	}

	/**
	 * Get the value of the column name
	 * 
	 * @param columnName
	 * @return value
	 */
	public Object getValue(String columnName) {
		return values[table.getColumnIndex(columnName)];
	}

	/**
	 * Get the row values
	 * 
	 * @return values
	 */
	public Object[] getValues() {
		return values;
	}

	/**
	 * Get the row column data types
	 * 
	 * @return row column types
	 */
	public int[] getRowColumnTypes() {
		return columnTypes;
	}

	/**
	 * Get the Cursor column data type at the index
	 * 
	 * @param index
	 * @return row column type
	 */
	public int getRowColumnType(int index) {
		return columnTypes[index];
	}

	/**
	 * Get the Cursor column data type of the column name
	 * 
	 * @param columnName
	 * @return row column type
	 */
	public int getRowColumnType(String columnName) {
		return columnTypes[table.getColumnIndex(columnName)];
	}

	/**
	 * Get the table
	 * 
	 * @return table
	 */
	public TTable getTable() {
		return table;
	}

	/**
	 * Get the column at the index
	 * 
	 * @param index
	 * @return column
	 */
	public TColumn getColumn(int index) {
		return table.getColumn(index);
	}

	/**
	 * Get the column of the column name
	 * 
	 * @param columnName
	 * @return column
	 */
	public TColumn getColumn(String columnName) {
		return table.getColumn(columnName);
	}

	/**
	 * Get the id value, which is the value of the primary key
	 * 
	 * @return id
	 */
	public long getId() {
		long id;
		Object objectValue = getValue(getPkColumnIndex());
		if (objectValue == null) {
			throw new GeoPackageException("Row Id was null. Table: "
					+ table.getTableName() + ", Column Index: "
					+ getPkColumnIndex() + ", Column Name: "
					+ getPkColumn().getName());
		}
		if (objectValue instanceof Number) {
			id = ((Number) objectValue).longValue();
		} else {
			throw new GeoPackageException("Row Id was not a number. Table: "
					+ table.getTableName() + ", Column Index: "
					+ getPkColumnIndex() + ", Column Name: "
					+ getPkColumn().getName());
		}

		return id;
	}

	/**
	 * Get the primary key column index
	 * 
	 * @return primary key column index
	 */
	public int getPkColumnIndex() {
		return table.getPkColumnIndex();
	}

	/**
	 * Get the primary key column
	 * 
	 * @return primary key column
	 */
	public TColumn getPkColumn() {
		return table.getPkColumn();
	}

	/**
	 * Set the column value at the index
	 * 
	 * @param index
	 * @param value
	 */
	public void setValue(int index, Object value) {
		if (index == table.getPkColumnIndex()) {
			throw new GeoPackageException(
					"Can not update the primary key of the row. Table Name: "
							+ table.getTableName() + ", Index: " + index
							+ ", Name: " + table.getPkColumn().getName());
		}
		values[index] = value;
	}

	/**
	 * Set the column value of the column name
	 * 
	 * @param columnName
	 * @param value
	 */
	public void setValue(String columnName, Object value) {
		setValue(getColumnIndex(columnName), value);
	}

	/**
	 * Set the id, package access only for the DAO
	 * 
	 * @param id
	 */
	void setId(long id) {
		values[getPkColumnIndex()] = id;
	}

	/**
	 * Clears the id so the row can be used as part of an insert or create
	 */
	public void resetId() {
		values[getPkColumnIndex()] = null;
	}

	/**
	 * Validate the value and its actual value types against the column data
	 * type class
	 * 
	 * @param column
	 * @param value
	 * @param valueTypes
	 */
	protected void validateValue(TColumn column, Object value,
			Class<?>... valueTypes) {

		GeoPackageDataType dataType = column.getDataType();
		Class<?> dataTypeClass = dataType.getClassType();

		boolean valid = false;
		for (Class<?> valueType : valueTypes) {
			if (valueType.equals(dataTypeClass)) {
				valid = true;
				break;
			}
		}

		if (!valid) {
			throw new GeoPackageException("Illegal value. Column: "
					+ column.getName() + ", Value: " + value
					+ ", Expected Type: " + dataTypeClass.getSimpleName()
					+ ", Actual Type: " + valueTypes[0].getSimpleName());
		}

	}

}
