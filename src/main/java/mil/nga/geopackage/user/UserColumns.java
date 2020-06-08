package mil.nga.geopackage.user;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.db.GeoPackageDataType;

/**
 * Abstract collection of columns from a user table, representing a full set of
 * table columns or a subset from a query
 * 
 * @param <TColumn>
 *            column type
 * 
 * @author osbornb
 * @since 3.5.0
 */
public abstract class UserColumns<TColumn extends UserColumn> {

	/**
	 * Logger
	 */
	private static final Logger log = Logger
			.getLogger(UserColumns.class.getName());

	/**
	 * Table name, null when a pre-ordered subset of columns for a query
	 */
	private String tableName;

	/**
	 * Array of column names
	 */
	private String[] columnNames;

	/**
	 * List of columns
	 */
	private final List<TColumn> columns;

	/**
	 * Custom column specification flag (subset of table columns or different
	 * ordering)
	 */
	private boolean custom;

	/**
	 * Mapping between (lower cased) column names and their index
	 */
	private final Map<String, Integer> nameToIndex;

	/**
	 * Primary key column index
	 */
	private int pkIndex;

	/**
	 * Indicates if the primary key is modifiable
	 */
	private boolean pkModifiable = false;

	/**
	 * Indicates if values are validated against column types
	 */
	private boolean valueValidation = true;

	/**
	 * Constructor
	 * 
	 * @param tableName
	 *            table name
	 * @param columns
	 *            columns
	 * @param custom
	 *            custom column specification
	 */
	protected UserColumns(String tableName, List<TColumn> columns,
			boolean custom) {
		this.tableName = tableName;
		this.columns = columns;
		this.custom = custom;
		nameToIndex = new HashMap<String, Integer>();
	}

	/**
	 * Copy Constructor
	 * 
	 * @param userColumns
	 *            user columns
	 */
	protected UserColumns(UserColumns<TColumn> userColumns) {
		this.tableName = userColumns.tableName;
		this.columnNames = new String[userColumns.columnNames.length];
		System.arraycopy(userColumns.columnNames, 0, this.columnNames, 0,
				this.columnNames.length);
		this.columns = new ArrayList<>();
		for (TColumn column : userColumns.columns) {
			@SuppressWarnings("unchecked")
			TColumn copiedColumn = (TColumn) column.copy();
			this.columns.add(copiedColumn);
		}
		this.nameToIndex = new HashMap<String, Integer>();
		this.nameToIndex.putAll(userColumns.nameToIndex);
		this.pkIndex = userColumns.pkIndex;
		this.pkModifiable = userColumns.pkModifiable;
		this.valueValidation = userColumns.valueValidation;
	}

	/**
	 * Copy the user columns
	 * 
	 * @return copied user columns
	 */
	public abstract UserColumns<TColumn> copy();

	/**
	 * Update the table columns
	 */
	protected void updateColumns() {

		nameToIndex.clear();

		if (!custom) {

			Set<Integer> indices = new HashSet<Integer>();

			// Check for missing indices and duplicates
			List<TColumn> needsIndex = new ArrayList<>();
			for (TColumn column : columns) {
				if (column.hasIndex()) {
					int index = column.getIndex();
					if (indices.contains(index)) {
						throw new GeoPackageException("Duplicate index: "
								+ index + ", Table Name: " + tableName);
					} else {
						indices.add(index);
					}
				} else {
					needsIndex.add(column);
				}
			}

			// Update columns that need an index
			int currentIndex = -1;
			for (TColumn column : needsIndex) {
				while (indices.contains(++currentIndex)) {
				}
				column.setIndex(currentIndex);
			}

			// Sort the columns by index
			Collections.sort(columns);

		}

		pkIndex = -1;
		columnNames = new String[columns.size()];

		for (int index = 0; index < columns.size(); index++) {

			TColumn column = columns.get(index);
			String columnName = column.getName();
			String lowerCaseColumnName = columnName.toLowerCase();

			if (!custom) {

				if (column.getIndex() != index) {
					throw new GeoPackageException("No column found at index: "
							+ index + ", Table Name: " + tableName);
				}

				if (nameToIndex.containsKey(lowerCaseColumnName)) {
					throw new GeoPackageException(
							"Duplicate column found at index: " + index
									+ ", Table Name: " + tableName + ", Name: "
									+ columnName);
				}

			}

			if (column.isPrimaryKey()) {
				if (pkIndex != -1) {
					StringBuilder error = new StringBuilder(
							"More than one primary key column was found for ");
					if (custom) {
						error.append("custom specified table columns");
					} else {
						error.append("table");
					}
					error.append(". table: " + tableName + ", index1: "
							+ pkIndex + ", index2: " + index);
					if (custom) {
						error.append(", columns: " + columnNames);
					}
					throw new GeoPackageException(error.toString());
				}
				pkIndex = index;
			}

			columnNames[index] = columnName;
			nameToIndex.put(lowerCaseColumnName, index);
		}

	}

	/**
	 * Check for duplicate column names
	 * 
	 * @param index
	 *            index
	 * @param previousIndex
	 *            previous index
	 * @param column
	 *            column
	 */
	protected void duplicateCheck(int index, Integer previousIndex,
			String column) {
		if (previousIndex != null) {
			log.log(Level.SEVERE,
					"More than one " + column + " column was found for table '"
							+ tableName + "'. Index " + previousIndex + " and "
							+ index);
		}
	}

	/**
	 * Check for the expected data type
	 * 
	 * @param expected
	 *            expected data type
	 * @param column
	 *            user column
	 */
	protected void typeCheck(GeoPackageDataType expected, TColumn column) {
		GeoPackageDataType actual = column.getDataType();
		if (actual == null || !actual.equals(expected)) {
			log.log(Level.SEVERE, "Unexpected " + column.getName()
					+ " column data type was found for table '" + tableName
					+ "', expected: " + expected.name() + ", actual: "
					+ (actual != null ? actual.name() : "null"));
		}
	}

	/**
	 * Check for missing columns
	 * 
	 * @param index
	 *            column index
	 * @param column
	 *            user column
	 */
	protected void missingCheck(Integer index, String column) {
		if (index == null) {
			log.log(Level.SEVERE, "No " + column
					+ " column was found for table '" + tableName + "'");
		}
	}

	/**
	 * Get the column index of the column name
	 * 
	 * @param columnName
	 *            column name
	 * @return column index
	 */
	public int getColumnIndex(String columnName) {
		return getColumnIndex(columnName, true);
	}

	/**
	 * Get the column index of the column name
	 * 
	 * @param columnName
	 *            column name
	 * @param required
	 *            column existence is required
	 * @return column index
	 */
	public Integer getColumnIndex(String columnName, boolean required) {
		Integer index = nameToIndex.get(columnName.toLowerCase());
		if (required && index == null) {
			StringBuilder error = new StringBuilder(
					"Column does not exist in ");
			if (custom) {
				error.append("custom specified table columns");
			} else {
				error.append("table");
			}
			error.append(". table: " + tableName + ", column: " + columnName);
			if (custom) {
				error.append(", columns: " + columnNames);
			}
			throw new GeoPackageException(error.toString());
		}
		return index;
	}

	/**
	 * Get the array of column names
	 * 
	 * @return column names
	 */
	public String[] getColumnNames() {
		return columnNames;
	}

	/**
	 * Get the column name at the index
	 * 
	 * @param index
	 *            column index
	 * @return column name
	 */
	public String getColumnName(int index) {
		return columnNames[index];
	}

	/**
	 * Get the list of columns
	 * 
	 * @return columns
	 */
	public List<TColumn> getColumns() {
		return columns;
	}

	/**
	 * Get the column at the index
	 * 
	 * @param index
	 *            column index
	 * @return column
	 */
	public TColumn getColumn(int index) {
		return columns.get(index);
	}

	/**
	 * Get the column of the column name
	 * 
	 * @param columnName
	 *            column name
	 * @return column
	 */
	public TColumn getColumn(String columnName) {
		return getColumn(getColumnIndex(columnName));
	}

	/**
	 * Check if the table has the column
	 * 
	 * @param columnName
	 *            column name
	 * @return true if has the column
	 */
	public boolean hasColumn(String columnName) {
		return nameToIndex.containsKey(columnName.toLowerCase());
	}

	/**
	 * Get the column count
	 * 
	 * @return column count
	 */
	public int columnCount() {
		return columns.size();
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
	 * Set the table name
	 * 
	 * @param tableName
	 *            table name
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	/**
	 * Is custom column specification (partial and/or ordering)
	 * 
	 * @return custom flag
	 */
	public boolean isCustom() {
		return custom;
	}

	/**
	 * Set the custom column specification flag
	 * 
	 * @param custom
	 *            custom flag
	 */
	public void setCustom(boolean custom) {
		this.custom = custom;
	}

	/**
	 * Check if the table has a primary key column
	 * 
	 * @return true if has a primary key
	 */
	public boolean hasPkColumn() {
		return pkIndex >= 0;
	}

	/**
	 * Get the primary key column index
	 * 
	 * @return primary key column index
	 */
	public int getPkColumnIndex() {
		return pkIndex;
	}

	/**
	 * Get the primary key column
	 * 
	 * @return primary key column
	 */
	public TColumn getPkColumn() {
		TColumn column = null;
		if (hasPkColumn()) {
			column = columns.get(pkIndex);
		}
		return column;
	}

	/**
	 * Get the primary key column name
	 * 
	 * @return primary key column name
	 */
	public String getPkColumnName() {
		String name = null;
		TColumn pkColumn = getPkColumn();
		if (pkColumn != null) {
			name = pkColumn.getName();
		}
		return name;
	}

	/**
	 * Get the columns with the provided data type
	 * 
	 * @param type
	 *            data type
	 * @return columns
	 */
	public List<TColumn> columnsOfType(GeoPackageDataType type) {
		List<TColumn> columnsOfType = new ArrayList<>();
		for (TColumn column : columns) {
			if (column.getDataType() == type) {
				columnsOfType.add(column);
			}
		}
		return columnsOfType;
	}

	/**
	 * Is the primary key modifiable
	 * 
	 * @return true if the primary key is modifiable
	 * @since 4.0.0
	 */
	public boolean isPkModifiable() {
		return pkModifiable;
	}

	/**
	 * Set if the primary key can be modified
	 * 
	 * @param pkModifiable
	 *            primary key modifiable flag
	 * @since 4.0.0
	 */
	public void setPkModifiable(boolean pkModifiable) {
		this.pkModifiable = pkModifiable;
	}

	/**
	 * Is value validation against column types enabled
	 * 
	 * @return true if values are validated against column types
	 * @since 4.0.0
	 */
	public boolean isValueValidation() {
		return valueValidation;
	}

	/**
	 * Set if values should validated against column types
	 * 
	 * @param valueValidation
	 *            value validation flag
	 * @since 4.0.0
	 */
	public void setValueValidation(boolean valueValidation) {
		this.valueValidation = valueValidation;
	}

	/**
	 * Add a new column
	 * 
	 * @param column
	 *            new column
	 */
	public void addColumn(TColumn column) {
		columns.add(column);
		updateColumns();
	}

	/**
	 * Rename a column
	 * 
	 * @param column
	 *            column
	 * @param newColumnName
	 *            new column name
	 */
	public void renameColumn(TColumn column, String newColumnName) {
		renameColumn(column.getName(), newColumnName);
		column.setName(newColumnName);
	}

	/**
	 * Rename a column
	 * 
	 * @param columnName
	 *            column name
	 * @param newColumnName
	 *            new column name
	 */
	public void renameColumn(String columnName, String newColumnName) {
		renameColumn(getColumnIndex(columnName), newColumnName);
	}

	/**
	 * Rename a column
	 * 
	 * @param index
	 *            column index
	 * @param newColumnName
	 *            new column name
	 */
	public void renameColumn(int index, String newColumnName) {
		columns.get(index).setName(newColumnName);
		updateColumns();
	}

	/**
	 * Drop a column
	 * 
	 * @param column
	 *            column to drop
	 */
	public void dropColumn(TColumn column) {
		dropColumn(column.getIndex());
	}

	/**
	 * Drop a column
	 * 
	 * @param columnName
	 *            column name
	 */
	public void dropColumn(String columnName) {
		dropColumn(getColumnIndex(columnName));
	}

	/**
	 * Drop a column
	 * 
	 * @param index
	 *            column index
	 */
	public void dropColumn(int index) {
		columns.remove(index);
		for (int i = index; i < columns.size(); i++) {
			columns.get(i).resetIndex();
		}
		updateColumns();
	}

	/**
	 * Alter a column
	 * 
	 * @param column
	 *            altered column
	 */
	public void alterColumn(TColumn column) {
		TColumn existingColumn = getColumn(column.getName());
		column.setIndex(existingColumn.getIndex());
		columns.set(column.getIndex(), column);
	}

}
