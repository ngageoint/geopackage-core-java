package mil.nga.geopackage.extension.related.simple;

import java.util.ArrayList;
import java.util.List;

import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.db.GeoPackageDataType;
import mil.nga.geopackage.extension.related.RelationType;
import mil.nga.geopackage.extension.related.UserRelatedTable;
import mil.nga.geopackage.user.UserColumn;
import mil.nga.geopackage.user.UserTable;
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
	 * Create a simple attributes table with the metadata
	 * 
	 * @param metadata
	 *            simple attributes table metadata
	 * @return simple attributes table
	 * @since 4.0.0
	 */
	public static SimpleAttributesTable create(
			SimpleAttributesTableMetadata metadata) {
		List<UserCustomColumn> columns = metadata.buildColumns();
		return new SimpleAttributesTable(metadata.getTableName(), columns,
				metadata.getIdColumnName());
	}

	/**
	 * Create the required table columns
	 * 
	 * @return user custom columns
	 */
	public static List<UserCustomColumn> createRequiredColumns() {
		return createRequiredColumns(UserTable.DEFAULT_AUTOINCREMENT);
	}

	/**
	 * Create the required table columns
	 * 
	 * @param autoincrement
	 *            autoincrement id values
	 * @return user custom columns
	 * @since 4.0.0
	 */
	public static List<UserCustomColumn> createRequiredColumns(
			boolean autoincrement) {
		return createRequiredColumns(null, autoincrement);
	}

	/**
	 * Create the required table columns with the id column name
	 * 
	 * @param idColumnName
	 *            id column name
	 * @return user custom columns
	 */
	public static List<UserCustomColumn> createRequiredColumns(
			String idColumnName) {
		return createRequiredColumns(idColumnName,
				UserTable.DEFAULT_AUTOINCREMENT);
	}

	/**
	 * Create the required table columns with the id column name
	 * 
	 * @param idColumnName
	 *            id column name
	 * @param autoincrement
	 *            autoincrement id values
	 * @return user custom columns
	 * @since 4.0.0
	 */
	public static List<UserCustomColumn> createRequiredColumns(
			String idColumnName, boolean autoincrement) {

		if (idColumnName == null) {
			idColumnName = COLUMN_ID;
		}

		List<UserCustomColumn> columns = new ArrayList<>();
		columns.add(createIdColumn(idColumnName, autoincrement));

		return columns;
	}

	/**
	 * Create the required table columns, starting at the provided index
	 * 
	 * @param startingIndex
	 *            starting index
	 * @return user custom columns
	 */
	public static List<UserCustomColumn> createRequiredColumns(
			int startingIndex) {
		return createRequiredColumns(startingIndex,
				UserTable.DEFAULT_AUTOINCREMENT);
	}

	/**
	 * Create the required table columns, starting at the provided index
	 * 
	 * @param startingIndex
	 *            starting index
	 * @param autoincrement
	 *            autoincrement id values
	 * @return user custom columns
	 * @since 4.0.0
	 */
	public static List<UserCustomColumn> createRequiredColumns(
			int startingIndex, boolean autoincrement) {
		return createRequiredColumns(startingIndex, null, autoincrement);
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
		return createRequiredColumns(startingIndex, idColumnName,
				UserTable.DEFAULT_AUTOINCREMENT);
	}

	/**
	 * Create the required table columns with id column name, starting at the
	 * provided index
	 * 
	 * @param startingIndex
	 *            starting index
	 * @param idColumnName
	 *            id column name
	 * @param autoincrement
	 *            autoincrement id values
	 * @return user custom columns
	 * @since 4.0.0
	 */
	public static List<UserCustomColumn> createRequiredColumns(
			int startingIndex, String idColumnName, boolean autoincrement) {

		if (idColumnName == null) {
			idColumnName = COLUMN_ID;
		}

		List<UserCustomColumn> columns = new ArrayList<>();
		columns.add(
				createIdColumn(startingIndex++, idColumnName, autoincrement));

		return columns;
	}

	/**
	 * Create the primary key id column
	 * 
	 * @param idColumnName
	 *            id column name
	 * @return id column
	 * @since 3.3.0
	 */
	public static UserCustomColumn createIdColumn(String idColumnName) {
		return UserCustomColumn.createPrimaryKeyColumn(idColumnName);
	}

	/**
	 * Create the primary key id column
	 * 
	 * @param idColumnName
	 *            id column name
	 * @param autoincrement
	 *            autoincrement id values
	 * @return id column
	 * @since 4.0.0
	 */
	public static UserCustomColumn createIdColumn(String idColumnName,
			boolean autoincrement) {
		return UserCustomColumn.createPrimaryKeyColumn(idColumnName,
				autoincrement);
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
	public static UserCustomColumn createIdColumn(int index,
			String idColumnName) {
		return UserCustomColumn.createPrimaryKeyColumn(index, idColumnName);
	}

	/**
	 * Create the primary key id column
	 * 
	 * @param index
	 *            column index
	 * @param idColumnName
	 *            id column name
	 * @param autoincrement
	 *            autoincrement id values
	 * @return id column
	 * @since 4.0.0
	 */
	public static UserCustomColumn createIdColumn(int index,
			String idColumnName, boolean autoincrement) {
		return UserCustomColumn.createPrimaryKeyColumn(index, idColumnName,
				autoincrement);
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
	 */
	public SimpleAttributesTable(String tableName,
			List<UserCustomColumn> columns) {
		this(tableName, columns, null);
	}

	/**
	 * Constructor
	 * 
	 * @param tableName
	 *            table name
	 * @param columns
	 *            list of columns
	 * @param idColumnName
	 *            id column name
	 */
	public SimpleAttributesTable(String tableName,
			List<UserCustomColumn> columns, String idColumnName) {
		super(tableName, RELATION_TYPE.getName(), RELATION_TYPE.getDataType(),
				columns, requiredColumns(idColumnName));
		validateColumns();
	}

	/**
	 * Constructor
	 * 
	 * @param table
	 *            user custom table
	 */
	public SimpleAttributesTable(UserCustomTable table) {
		super(RELATION_TYPE.getName(), RELATION_TYPE.getDataType(), table);
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
			if (!isSimple(column)) {
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

	/**
	 * Determine if the column is a non nullable simple type: TEXT, INTEGER, or
	 * REAL
	 * 
	 * @param column
	 *            user column
	 * @return true if a simple column
	 */
	public static boolean isSimple(UserColumn column) {
		return column.isNotNull() && isSimple(column.getDataType());
	}

	/**
	 * Determine if the data type is a simple type: TEXT, INTEGER, or REAL
	 * storage classes
	 * 
	 * @param dataType
	 *            data type
	 * @return true if a simple column
	 */
	public static boolean isSimple(GeoPackageDataType dataType) {
		return dataType != GeoPackageDataType.BLOB;
	}

}
