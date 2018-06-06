package mil.nga.geopackage.extension.related.simple;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.extension.related.RelationType;
import mil.nga.geopackage.extension.related.UserRelatedTable;
import mil.nga.geopackage.user.custom.UserCustomColumn;
import mil.nga.geopackage.user.custom.UserCustomTable;

/**
 * Simple Attributes Requirements Class User-Defined Related Data Table
 * 
 * @author osbornb
 * @since 3.0.1
 */
public class SimpleAttributesTable extends UserRelatedTable {

	/**
	 * User-Defined Simple Attributes Table relation name
	 */
	public static final RelationType RELATION_TYPE = RelationType.SIMPLE_ATTRIBUTES;

	/**
	 * Autoincrement primary key, optional name
	 */
	public static final String COLUMN_ID = "id";

	/**
	 * Create a simple attributes table with the columns
	 * 
	 * @param tableName
	 *            table name
	 * @param columns
	 *            columns
	 * @return simple attributes table
	 */
	public static SimpleAttributesTable create(String tableName,
			List<UserCustomColumn> columns) {
		return create(tableName, null, columns);
	}

	/**
	 * Create a simple attributes table with the id column and columns
	 * 
	 * @param tableName
	 *            table name
	 * @param idColumnName
	 *            id column name
	 * @param columns
	 *            columns
	 * @return simple attributes table
	 */
	public static SimpleAttributesTable create(String tableName,
			String idColumnName, List<UserCustomColumn> columns) {

		List<UserCustomColumn> tableColumns = new ArrayList<>();
		tableColumns.addAll(createRequiredColumns(idColumnName));

		if (columns != null) {
			tableColumns.addAll(columns);
		}

		return new SimpleAttributesTable(tableName, tableColumns,
				requiredColumns(idColumnName));
	}

	/**
	 * Create the required table columns, starting at index 0
	 * 
	 * @return user custom columns
	 */
	public static List<UserCustomColumn> createRequiredColumns() {
		return createRequiredColumns(0);
	}

	/**
	 * Create the required table columns with the id column name, starting at
	 * index 0
	 * 
	 * @param idColumnName
	 *            id column name
	 * @return user custom columns
	 */
	public static List<UserCustomColumn> createRequiredColumns(
			String idColumnName) {
		return createRequiredColumns(0, idColumnName);
	}

	/**
	 * Create the required table columns, starting at the provided index
	 * 
	 * @param startingIndex
	 *            starting index
	 * @return user custom columns
	 */
	public static List<UserCustomColumn> createRequiredColumns(int startingIndex) {
		return createRequiredColumns(startingIndex, null);
	}

	/**
	 * Create the required table columns with id column name, starting at the
	 * provided index
	 * 
	 * @param startingIndex
	 *            starting index
	 * @param idColumnName
	 *            id column name
	 * @return user custom columns
	 */
	public static List<UserCustomColumn> createRequiredColumns(
			int startingIndex, String idColumnName) {

		if (idColumnName == null) {
			idColumnName = COLUMN_ID;
		}

		List<UserCustomColumn> columns = new ArrayList<>();
		columns.add(createIdColumn(startingIndex++, idColumnName));

		return columns;
	}

	/**
	 * Create the primary key id column
	 * 
	 * @param index
	 *            column index
	 * @param idColumnName
	 *            id column name
	 * @return id column
	 */
	public static UserCustomColumn createIdColumn(int index, String idColumnName) {
		return UserCustomColumn.createPrimaryKeyColumn(index, idColumnName);
	}

	/**
	 * Get the number of required columns
	 * 
	 * @return required columns count
	 */
	public static int numRequiredColumns() {
		return requiredColumns().size();
	}

	/**
	 * Get the required columns
	 * 
	 * @return required columns
	 */
	public static List<String> requiredColumns() {
		return requiredColumns(null);
	}

	/**
	 * Get the required columns
	 * 
	 * @param idColumnName
	 *            id column name
	 * 
	 * @return required columns
	 */
	public static List<String> requiredColumns(String idColumnName) {

		if (idColumnName == null) {
			idColumnName = COLUMN_ID;
		}

		List<String> requiredColumns = new ArrayList<>();
		requiredColumns.add(idColumnName);
		return requiredColumns;
	}

	/**
	 * Constructor
	 * 
	 * @param tableName
	 *            table name
	 * @param columns
	 *            list of columns
	 * @param requiredColumns
	 *            list of required columns
	 */
	private SimpleAttributesTable(String tableName,
			List<UserCustomColumn> columns, Collection<String> requiredColumns) {
		super(tableName, RELATION_TYPE.getName(), columns, requiredColumns);
		validateColumns();
	}

	/**
	 * Constructor
	 * 
	 * @param table
	 *            user custom table
	 */
	SimpleAttributesTable(UserCustomTable table) {
		super(RELATION_TYPE.getName(), table);
		validateColumns();
	}

	/**
	 * Validate that Simple Attributes columns to verify at least one non id
	 * column exists and that all columns are simple data types
	 */
	private void validateColumns() {

		List<UserCustomColumn> columns = getColumns();
		if (columns.size() < 2) {
			throw new GeoPackageException(
					"Simple Attributes Tables require at least one non id column. Columns: "
							+ columns.size());
		}

		for (UserCustomColumn column : columns) {
			switch (column.getDataType()) {
			case TEXT:
			case INTEGER:
			case REAL:
				break;
			default:
				throw new GeoPackageException(
						"Simple Attributes Tables only support simple data types. Column: "
								+ column.getName() + ", Non Simple Data Type: "
								+ column.getDataType().name());
			}
		}
	}

	/**
	 * Get the id column index
	 * 
	 * @return id column index
	 */
	public int getIdColumnIndex() {
		return getPkColumnIndex();
	}

	/**
	 * Get the id column
	 * 
	 * @return id column
	 */
	public UserCustomColumn getIdColumn() {
		return getPkColumn();
	}

}
