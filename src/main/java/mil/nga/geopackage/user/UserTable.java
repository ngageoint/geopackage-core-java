package mil.nga.geopackage.user;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import mil.nga.geopackage.contents.Contents;
import mil.nga.geopackage.db.GeoPackageDataType;
import mil.nga.geopackage.db.table.Constraint;
import mil.nga.geopackage.db.table.ConstraintType;
import mil.nga.geopackage.db.table.Constraints;

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
	 * Default id autoincrement setting
	 * 
	 * @since 4.0.0
	 */
	public static boolean DEFAULT_AUTOINCREMENT = true;

	/**
	 * Default primary key not null setting
	 * 
	 * @since 4.0.0
	 */
	public static boolean DEFAULT_PK_NOT_NULL = true;

	/**
	 * Columns
	 */
	private UserColumns<TColumn> columns;

	/**
	 * Constraints
	 */
	private final Constraints constraints;

	/**
	 * Foreign key to Contents
	 */
	private Contents contents;

	/**
	 * Constructor
	 * 
	 * @param columns
	 *            columns
	 * @since 3.5.0
	 */
	protected UserTable(UserColumns<TColumn> columns) {
		this.columns = columns;
		constraints = new Constraints();
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
		this.constraints = userTable.constraints.copy();
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
	 * Get the contents data type from the contents or use the default
	 * 
	 * @param defaultType
	 *            default data type
	 * @return contents or default data type
	 * @since 4.0.0
	 */
	protected String getDataType(String defaultType) {
		String dataType = null;
		if (contents != null) {
			dataType = contents.getDataTypeName();
		}
		if (dataType == null) {
			dataType = defaultType;
		}
		return dataType;
	}

	/**
	 * Create user columns for a subset of table columns
	 * 
	 * @param columns
	 *            columns
	 * @return user columns
	 * @since 3.5.0
	 */
	public abstract UserColumns<TColumn> createUserColumns(
			List<TColumn> columns);

	/**
	 * Create user columns for a subset of table columns
	 * 
	 * @param columnNames
	 *            column names
	 * @return user columns
	 * @since 3.5.0
	 */
	public UserColumns<TColumn> createUserColumns(String[] columnNames) {
		return createUserColumns(getColumns(columnNames));
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
	 * Get the columns from the column names
	 * 
	 * @param columnNames
	 *            column names
	 * @return columns
	 * @since 3.5.0
	 */
	public List<TColumn> getColumns(String[] columnNames) {
		List<TColumn> columns = new ArrayList<>();
		for (String columnName : columnNames) {
			columns.add(getColumn(columnName));
		}
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
	 * Get the primary key column name
	 * 
	 * @return primary key column name
	 */
	public String getPkColumnName() {
		return columns.getPkColumnName();
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
	}

	/**
	 * Add constraints
	 * 
	 * @param constraints
	 *            constraints
	 * @since 3.3.0
	 */
	public void addConstraints(Collection<Constraint> constraints) {
		this.constraints.add(constraints);
	}

	/**
	 * Add constraints
	 * 
	 * @param constraints
	 *            constraints
	 * @since 4.0.1
	 */
	public void addConstraints(Constraints constraints) {
		addConstraints(constraints.all());
	}

	/**
	 * Check if has constraints
	 * 
	 * @return true if has constraints
	 * @since 3.3.0
	 */
	public boolean hasConstraints() {
		return constraints.has();
	}

	/**
	 * Check if has constraints of the provided type
	 * 
	 * @param type
	 *            constraint type
	 * @return true if has constraints
	 * @since 4.0.1
	 */
	public boolean hasConstraints(ConstraintType type) {
		return constraints.has(type);
	}

	/**
	 * Get the constraints
	 * 
	 * @return constraints
	 * @since 4.0.1
	 */
	public Constraints getConstraints() {
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
		return constraints.get(type);
	}

	/**
	 * Clear the constraints
	 * 
	 * @return cleared constraints
	 * @since 3.3.0
	 */
	public List<Constraint> clearConstraints() {
		return constraints.clear();
	}

	/**
	 * Clear the constraints of the provided type
	 * 
	 * @param type
	 *            constraint type
	 * @return cleared constraints
	 * @since 4.0.1
	 */
	public List<Constraint> clearConstraints(ConstraintType type) {
		return constraints.clear(type);
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
	 * Is the primary key modifiable
	 * 
	 * @return true if the primary key is modifiable
	 * @since 4.0.0
	 */
	public boolean isPkModifiable() {
		return columns.isPkModifiable();
	}

	/**
	 * Set if the primary key can be modified
	 * 
	 * @param pkModifiable
	 *            primary key modifiable flag
	 * @since 4.0.0
	 */
	public void setPkModifiable(boolean pkModifiable) {
		columns.setPkModifiable(pkModifiable);
	}

	/**
	 * Is value validation against column types enabled
	 * 
	 * @return true if values are validated against column types
	 * @since 4.0.0
	 */
	public boolean isValueValidation() {
		return columns.isValueValidation();
	}

	/**
	 * Set if values should validated against column types
	 * 
	 * @param valueValidation
	 *            value validation flag
	 * @since 4.0.0
	 */
	public void setValueValidation(boolean valueValidation) {
		columns.setValueValidation(valueValidation);
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
