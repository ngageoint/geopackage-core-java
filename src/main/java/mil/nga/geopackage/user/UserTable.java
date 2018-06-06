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
 * Abstract user table
 * 
 * @param <TColumn>
 * 
 * @author osbornb
 */
public abstract class UserTable<TColumn extends UserColumn> {

	/**
	 * Table name
	 */
	private final String tableName;

	/**
	 * Array of column names
	 */
	private final String[] columnNames;

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
	private final int pkIndex;

	/**
	 * Unique constraints
	 */
	private final List<UserUniqueConstraint<TColumn>> uniqueConstraints;

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
		uniqueConstraints = new ArrayList<UserUniqueConstraint<TColumn>>();
		this.tableName = tableName;
		this.columns = columns;

		Integer pk = null;

		Set<Integer> indices = new HashSet<Integer>();

		// Build the column name array for queries, find the primary key and
		// geometry
		this.columnNames = new String[columns.size()];
		for (TColumn column : columns) {

			int index = column.getIndex();

			if (column.isPrimaryKey()) {
				if (pk != null) {
					throw new GeoPackageException(
							"More than one primary key column was found for table '"
									+ tableName + "'. Index " + pk + " and "
									+ index);
				}
				pk = index;
			}

			// Check for duplicate indices
			if (indices.contains(index)) {
				throw new GeoPackageException("Duplicate index: " + index
						+ ", Table Name: " + tableName);
			}
			indices.add(index);

			columnNames[index] = column.getName();
			nameToIndex.put(column.getName(), index);
		}

		if (pk != null) {
			pkIndex = pk;
		} else {
			pkIndex = -1;
		}

		// Verify the columns have ordered indices without gaps
		for (int i = 0; i < columns.size(); i++) {
			if (!indices.contains(i)) {
				throw new GeoPackageException("No column found at index: " + i
						+ ", Table Name: " + tableName);
			}
		}

		// Sort the columns by index
		Collections.sort(columns);
	}

	/**
	 * Constructor, re-uses existing memory structures, not a copy
	 * 
	 * @param userTable
	 *            user table
	 */
	protected UserTable(UserTable<TColumn> userTable) {
		this.tableName = userTable.tableName;
		this.columnNames = userTable.columnNames;
		this.columns = userTable.columns;
		this.nameToIndex = userTable.nameToIndex;
		this.pkIndex = userTable.pkIndex;
		this.uniqueConstraints = userTable.uniqueConstraints;
	}

	/**
	 * Check for duplicate column names
	 * 
	 * @param index
	 * @param previousIndex
	 * @param column
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

}
