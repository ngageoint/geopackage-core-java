package mil.nga.geopackage.user;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.core.contents.Contents;
import mil.nga.geopackage.db.GeoPackageDataType;
import mil.nga.geopackage.db.table.Constraint;
import mil.nga.geopackage.db.table.ConstraintType;

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
	 * Columns
	 */
	private UserColumns<TColumn> columns;

	/**
	 * Constraints
	 */
	private final List<Constraint> constraints;

	/**
	 * Type Constraints
	 */
	private final Map<ConstraintType, List<Constraint>> typedContraints;

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
		this.columns = new UserColumns<TColumn>(tableName, columns);
		constraints = new ArrayList<>();
		typedContraints = new HashMap<>();
	}

	/**
	 * Copy Constructor
	 * 
	 * @param userTable
	 *            user table
	 * @since 3.3.0
	 */
	protected UserTable(UserTable<TColumn> userTable) {
		this.columns = userTable.columns.copy();
		constraints = new ArrayList<>();
		typedContraints = new HashMap<>();
		for (Constraint constraint : userTable.constraints) {
			addConstraint(constraint.copy());
		}
		this.contents = userTable.contents;
	}

	/**
	 * Copy the table
	 * 
	 * @return copied table
	 * @since 3.3.0
	 */
	public abstract UserTable<TColumn> copy();

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
					+ " column was found for table '" + columns.getTableName()
					+ "'. Index " + previousIndex + " and " + index);

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
					+ " column data type was found for table '"
					+ columns.getTableName() + "', expected: " + expected.name()
					+ ", actual: " + (actual != null ? actual.name() : "null"));
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
			throw new GeoPackageException(
					"No " + column + " column was found for table '"
							+ columns.getTableName() + "'");
		}
	}

	/**
	 * Get the user columns
	 * 
	 * @return user columns
	 * @since 3.5.0
	 */
	public UserColumns<TColumn> getUserColumns() {
		return columns;
	}

	/**
	 * Get the column index of the column name
	 * 
	 * @param columnName
	 *            column name
	 * @return column index
	 */
	public int getColumnIndex(String columnName) {
		return columns.getColumnIndex(columnName);
	}

	/**
	 * Get the array of column names
	 * 
	 * @return column names
	 */
	public String[] getColumnNames() {
		return columns.getColumnNames();
	}

	/**
	 * Get the column name at the index
	 * 
	 * @param index
	 *            column index
	 * @return column name
	 */
	public String getColumnName(int index) {
		return columns.getColumnName(index);
	}

	/**
	 * Get the list of columns
	 * 
	 * @return columns
	 */
	public List<TColumn> getColumns() {
		return columns.getColumns();
	}

	/**
	 * Get the column at the index
	 * 
	 * @param index
	 *            column index
	 * @return column
	 */
	public TColumn getColumn(int index) {
		return columns.getColumn(index);
	}

	/**
	 * Get the column of the column name
	 * 
	 * @param columnName
	 *            column name
	 * @return column
	 */
	public TColumn getColumn(String columnName) {
		return columns.getColumn(columnName);
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
		return columns.hasColumn(columnName);
	}

	/**
	 * Get the column count
	 * 
	 * @return column count
	 */
	public int columnCount() {
		return columns.columnCount();
	}

	/**
	 * Get the table name
	 * 
	 * @return table name
	 */
	public String getTableName() {
		return columns.getTableName();
	}

	/**
	 * Set the table name
	 * 
	 * @param tableName
	 *            table name
	 * @since 3.3.0
	 */
	public void setTableName(String tableName) {
		columns.setTableName(tableName);
	}

	/**
	 * Check if the table has a primary key column
	 * 
	 * @return true if has a primary key
	 * @since 3.0.1
	 */
	public boolean hasPkColumn() {
		return columns.hasPkColumn();
	}

	/**
	 * Get the primary key column index
	 * 
	 * @return primary key column index
	 */
	public int getPkColumnIndex() {
		return columns.getPkColumnIndex();
	}

	/**
	 * Get the primary key column
	 * 
	 * @return primary key column
	 */
	public TColumn getPkColumn() {
		return columns.getPkColumn();
	}

	/**
	 * Add constraint
	 * 
	 * @param constraint
	 *            constraint
	 * @since 3.3.0
	 */
	public void addConstraint(Constraint constraint) {
		constraints.add(constraint);
		List<Constraint> typeConstraints = typedContraints
				.get(constraint.getType());
		if (typeConstraints == null) {
			typeConstraints = new ArrayList<>();
			typedContraints.put(constraint.getType(), typeConstraints);
		}
		typeConstraints.add(constraint);
	}

	/**
	 * Add constraints
	 * 
	 * @param constraints
	 *            constraints
	 * @since 3.3.0
	 */
	public void addConstraints(Collection<Constraint> constraints) {
		for (Constraint constraint : constraints) {
			addConstraint(constraint);
		}
	}

	/**
	 * Check if has constraints
	 * 
	 * @return true if has constraints
	 * @since 3.3.0
	 */
	public boolean hasConstraints() {
		return !constraints.isEmpty();
	}

	/**
	 * Get the constraints
	 * 
	 * @return constraints
	 * @since 3.3.0
	 */
	public List<Constraint> getConstraints() {
		return constraints;
	}

	/**
	 * Get the constraints of the provided type
	 * 
	 * @param type
	 *            constraint type
	 * @return constraints
	 * @since 3.3.0
	 */
	public List<Constraint> getConstraints(ConstraintType type) {
		List<Constraint> constraints = typedContraints.get(type);
		if (constraints == null) {
			constraints = new ArrayList<>();
		}
		return constraints;
	}

	/**
	 * Clear the constraints
	 * 
	 * @return cleared constraints
	 * @since 3.3.0
	 */
	public List<Constraint> clearConstraints() {
		List<Constraint> constraintsCopy = new ArrayList<>(constraints);
		constraints.clear();
		typedContraints.clear();
		return constraintsCopy;
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
		return columns.columnsOfType(type);
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
	 * @since 3.3.0
	 */
	public void addColumn(TColumn column) {
		columns.addColumn(column);
	}

	/**
	 * Rename a column
	 * 
	 * @param column
	 *            column
	 * @param newColumnName
	 *            new column name
	 * @since 3.3.0
	 */
	public void renameColumn(TColumn column, String newColumnName) {
		columns.renameColumn(column, newColumnName);
	}

	/**
	 * Rename a column
	 * 
	 * @param columnName
	 *            column name
	 * @param newColumnName
	 *            new column name
	 * @since 3.3.0
	 */
	public void renameColumn(String columnName, String newColumnName) {
		columns.renameColumn(columnName, newColumnName);
	}

	/**
	 * Rename a column
	 * 
	 * @param index
	 *            column index
	 * @param newColumnName
	 *            new column name
	 * @since 3.3.0
	 */
	public void renameColumn(int index, String newColumnName) {
		columns.renameColumn(index, newColumnName);
	}

	/**
	 * Drop a column
	 * 
	 * @param column
	 *            column to drop
	 * @since 3.3.0
	 */
	public void dropColumn(TColumn column) {
		columns.dropColumn(column);
	}

	/**
	 * Drop a column
	 * 
	 * @param columnName
	 *            column name
	 * @since 3.3.0
	 */
	public void dropColumn(String columnName) {
		columns.dropColumn(columnName);
	}

	/**
	 * Drop a column
	 * 
	 * @param index
	 *            column index
	 * @since 3.3.0
	 */
	public void dropColumn(int index) {
		columns.dropColumn(index);
	}

	/**
	 * Alter a column
	 * 
	 * @param column
	 *            altered column
	 * @since 3.3.0
	 */
	public void alterColumn(TColumn column) {
		columns.alterColumn(column);
	}

}
