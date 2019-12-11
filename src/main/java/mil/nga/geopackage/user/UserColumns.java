package mil.nga.geopackage.user;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.db.GeoPackageDataType;

/**
 * Collection of columns from a user table
 * 
 * @param <TColumn>
 *            column type
 * 
 * @author osbornb
 * @since 3.5.0
 */
public class UserColumns<TColumn extends UserColumn> {

	/**
	 * Table name
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
	 * Mapping between column names and their index
	 */
	private final Map<String, Integer> nameToIndex;

	/**
	 * Primary key column index
	 */
	private int pkIndex;

	/**
	 * Constructor
	 * 
	 * @param tableName
	 *            table name
	 * @param columns
	 *            columns
	 */
	public UserColumns(String tableName, List<TColumn> columns) {
		this.tableName = tableName;
		this.columns = columns;
		nameToIndex = new HashMap<String, Integer>();

		updateColumns();
	}

	/**
	 * Constructor
	 * 
	 * @param columns
	 *            columns
	 */
	public UserColumns(List<TColumn> columns) {
		this(null, columns);
	}

	/**
	 * Copy Constructor
	 * 
	 * @param userTable
	 *            user table
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
	}

	/**
	 * Copy the user columns
	 * 
	 * @return copied user columns
	 */
	public UserColumns<TColumn> copy() {
		return new UserColumns<TColumn>(this);
	}

	/**
	 * Update the table columns
	 */
	protected void updateColumns() {

		nameToIndex.clear();

		if (tableName != null) {

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

			if (tableName != null && column.getIndex() != index) {
				throw new GeoPackageException("No column found at index: "
						+ index + ", Table Name: " + tableName);
			}

			if (column.isPrimaryKey()) {
				if (pkIndex != -1) {
					throw new GeoPackageException(
							"More than one primary key column was found for table '"
									+ tableName + "'. Index " + pkIndex
									+ " and " + index);
				}
				pkIndex = index;
			}

			String columnName = column.getName();
			columnNames[index] = columnName;
			nameToIndex.put(columnName, index);
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
		Integer index = nameToIndex.get(columnName);
		if (index == null) {
			StringBuilder error = new StringBuilder("Column does not exist");
			if (tableName != null) {
				error.append(" in table '" + tableName + "', column");
			}
			error.append(": " + columnName);
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
		return nameToIndex.containsKey(columnName);
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
