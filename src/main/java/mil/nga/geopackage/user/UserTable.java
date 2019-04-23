package mil.nga.geopackage.user;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.core.contents.Contents;
import mil.nga.geopackage.db.GeoPackageDataType;

/**
 * Abstract user table
 * 
 * @param <TColumn>
 *            column type
 * 
 * @author osbornb
 */
public abstract class UserTable<TColumn extends UserColumn> {

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
	 * Unique constraints
	 */
	private final List<UserUniqueConstraint<TColumn>> uniqueConstraints;

	/**
	 * Foreign key to Contents
	 */
	private Contents contents;

	/**
	 * Constructor
	 * 
	 * @param tableName
	 *            table name
	 * @param columns
	 *            list of columns
	 */
	protected UserTable(String tableName, List<TColumn> columns) {
		nameToIndex = new HashMap<String, Integer>();
		uniqueConstraints = new ArrayList<>();
		this.tableName = tableName;
		this.columns = columns;

		updateColumns();
	}

	/**
	 * Copy Constructor
	 * 
	 * @param userTable
	 *            user table
	 * @since 3.2.1
	 */
	protected UserTable(UserTable<TColumn> userTable) {
		this.tableName = userTable.tableName;
		this.columnNames = new String[userTable.columnNames.length];
		System.arraycopy(userTable.columnNames, 0, this.columnNames, 0,
				this.columnNames.length);
		this.columns = new ArrayList<>();
		for (TColumn column : userTable.columns) {
			@SuppressWarnings("unchecked")
			TColumn copiedColumn = (TColumn) column.copy();
			this.columns.add(copiedColumn);
		}
		this.nameToIndex = new HashMap<String, Integer>();
		this.nameToIndex.putAll(userTable.nameToIndex);
		this.pkIndex = userTable.pkIndex;

		uniqueConstraints = new ArrayList<>();
		for (UserUniqueConstraint<TColumn> uniqueConstraint : userTable.uniqueConstraints) {
			this.uniqueConstraints.add(uniqueConstraint.copy());
		}
		this.contents = userTable.contents;
	}

	/**
	 * Copy the table
	 * 
	 * @return copied table
	 * @since 3.2.1
	 */
	public abstract UserTable<TColumn> copy();

	/**
	 * Update the table columns
	 * 
	 * @since 3.2.1
	 */
	protected void updateColumns() {

		nameToIndex.clear();

		Set<Integer> indices = new HashSet<Integer>();

		// Check for missing indices and duplicates
		List<TColumn> needsIndex = new ArrayList<>();
		for (TColumn column : columns) {
			if (column.hasIndex()) {
				int index = column.getIndex();
				if (indices.contains(index)) {
					throw new GeoPackageException("Duplicate index: " + index
							+ ", Table Name: " + tableName);
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

		pkIndex = -1;
		columnNames = new String[columns.size()];

		for (int index = 0; index < columns.size(); index++) {

			TColumn column = columns.get(index);

			if (column.getIndex() != index) {
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
	 * Get the contents data type
	 * 
	 * @return data type
	 * @since 3.2.0
	 */
	public abstract String getDataType();

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
			throw new GeoPackageException("More than one " + column
					+ " column was found for table '" + tableName + "'. Index "
					+ previousIndex + " and " + index);

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
			throw new GeoPackageException("Unexpected " + column.getName()
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
			throw new GeoPackageException("No " + column
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
		Integer index = nameToIndex.get(columnName);
		if (index == null) {
			throw new GeoPackageException("Column does not exist in table '"
					+ tableName + "', column: " + columnName);
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
	 * @since 3.0.1
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
	 * @since 3.2.1
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	/**
	 * Check if the table has a primary key column
	 * 
	 * @return true if has a primary key
	 * @since 3.0.1
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
	 * Add unique constraint
	 * 
	 * @param uniqueConstraint
	 *            unique constraint
	 */
	public void addUniqueConstraint(
			UserUniqueConstraint<TColumn> uniqueConstraint) {
		uniqueConstraints.add(uniqueConstraint);
	}

	/**
	 * Add unique constraint
	 * 
	 * @param uniqueConstraints
	 *            unique constraints
	 * @since 3.0.2
	 */
	public void addUniqueConstraints(
			List<UserUniqueConstraint<TColumn>> uniqueConstraints) {
		this.uniqueConstraints.addAll(uniqueConstraints);
	}

	/**
	 * Get the unique constraints
	 * 
	 * @return unique constraints
	 */
	public List<UserUniqueConstraint<TColumn>> getUniqueConstraints() {
		return uniqueConstraints;
	}

	/**
	 * Get the columns with the provided data type
	 * 
	 * @param type
	 *            data type
	 * @return columns
	 * @since 2.0.0
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
	 * Get the contents
	 * 
	 * @return contents
	 * @since 3.2.0
	 */
	public Contents getContents() {
		return contents;
	}

	/**
	 * Set the contents
	 * 
	 * @param contents
	 *            contents
	 * @since 3.2.0
	 */
	public void setContents(Contents contents) {
		this.contents = contents;
		if (contents != null) {
			validateContents(contents);
		}
	}

	/**
	 * Validate that the set contents are valid
	 * 
	 * @param contents
	 *            contents
	 */
	protected void validateContents(Contents contents) {

	}

	/**
	 * Add a new column
	 * 
	 * @param column
	 *            new column
	 * @since 3.2.1
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
	 * @since 3.2.1
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
	 * @since 3.2.1
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
	 * @since 3.2.1
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
	 * @since 3.2.1
	 */
	public void dropColumn(TColumn column) {
		dropColumn(column.getIndex());
	}

	/**
	 * Drop a column
	 * 
	 * @param columnName
	 *            column name
	 * @since 3.2.1
	 */
	public void dropColumn(String columnName) {
		dropColumn(getColumnIndex(columnName));
	}

	/**
	 * Drop a column
	 * 
	 * @param index
	 *            column index
	 * @since 3.2.1
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
	 * @since 3.2.1
	 */
	public void alterColumn(TColumn column) {
		TColumn existingColumn = getColumn(column.getName());
		column.setIndex(existingColumn.getIndex());
		columns.set(column.getIndex(), column);
	}

}
