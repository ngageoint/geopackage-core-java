package mil.nga.geopackage.extension.related;

import java.util.ArrayList;
import java.util.List;

import mil.nga.geopackage.db.GeoPackageDataType;
import mil.nga.geopackage.user.UserColumn;
import mil.nga.geopackage.user.custom.UserCustomColumn;
import mil.nga.geopackage.user.custom.UserCustomTable;

/**
 * Contains user mapping table factory and utility methods
 * 
 * @author jyutzler
 * @author osbornb
 * @since 3.0.1
 */
public class UserMappingTable extends UserCustomTable {

	/**
	 * Base ID column name
	 */
	public static final String COLUMN_BASE_ID = "base_id";

	/**
	 * Related ID column name
	 */
	public static final String COLUMN_RELATED_ID = "related_id";

	/**
	 * Create a user mapping table with the minimum required columns
	 * 
	 * @param tableName
	 *            table name
	 * @return user mapping table
	 */
	public static UserMappingTable create(String tableName) {
		return create(tableName, null);
	}

	/**
	 * Create a user mapping table with the minimum required columns followed by
	 * the additional columns
	 * 
	 * @param tableName
	 *            table name
	 * @param additionalColumns
	 *            additional columns
	 * @return user mapping table
	 */
	public static UserMappingTable create(String tableName,
			List<UserCustomColumn> additionalColumns) {

		List<UserCustomColumn> columns = new ArrayList<>();
		columns.addAll(createRequiredColumns());

		if (additionalColumns != null) {
			columns.addAll(additionalColumns);
		}

		return new UserMappingTable(tableName, columns);
	}

	/**
	 * Create the required table columns
	 * 
	 * @return user custom columns
	 */
	public static List<UserCustomColumn> createRequiredColumns() {

		List<UserCustomColumn> columns = new ArrayList<>();
		columns.add(createBaseIdColumn());
		columns.add(createRelatedIdColumn());

		return columns;
	}

	/**
	 * Create the required table columns, starting at the provided index
	 * 
	 * @param startingIndex
	 *            starting index
	 * @return user custom columns
	 */
	public static List<UserCustomColumn> createRequiredColumns(int startingIndex) {

		List<UserCustomColumn> columns = new ArrayList<>();
		columns.add(createBaseIdColumn(startingIndex++));
		columns.add(createRelatedIdColumn(startingIndex++));

		return columns;
	}

	/**
	 * Create a base id column
	 * 
	 * @return base id column
	 * @since 3.3.0
	 */
	public static UserCustomColumn createBaseIdColumn() {
		return createBaseIdColumn(UserColumn.NO_INDEX);
	}

	/**
	 * Create a base id column
	 * 
	 * @param index
	 *            column index
	 * @return base id column
	 */
	public static UserCustomColumn createBaseIdColumn(int index) {
		return UserCustomColumn.createColumn(index, COLUMN_BASE_ID,
				GeoPackageDataType.INTEGER, true);
	}

	/**
	 * Create a related id column
	 * 
	 * @return related id column
	 * @since 3.3.0
	 */
	public static UserCustomColumn createRelatedIdColumn() {
		return createRelatedIdColumn(UserColumn.NO_INDEX);
	}

	/**
	 * Create a related id column
	 * 
	 * @param index
	 *            column index
	 * @return related id column
	 */
	public static UserCustomColumn createRelatedIdColumn(int index) {
		return UserCustomColumn.createColumn(index, COLUMN_RELATED_ID,
				GeoPackageDataType.INTEGER, true);
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
		List<String> requiredColumns = new ArrayList<>();
		requiredColumns.add(COLUMN_BASE_ID);
		requiredColumns.add(COLUMN_RELATED_ID);
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
	protected UserMappingTable(String tableName, List<UserCustomColumn> columns) {
		super(tableName, columns, requiredColumns());
	}

	/**
	 * Constructor
	 * 
	 * @param table
	 *            user custom table
	 */
	protected UserMappingTable(UserCustomTable table) {
		super(table);
	}

	/**
	 * Get the base id column index
	 * 
	 * @return base id column index
	 */
	public int getBaseIdColumnIndex() {
		return getColumnIndex(COLUMN_BASE_ID);
	}

	/**
	 * Get the base id column
	 * 
	 * @return base id column
	 */
	public UserCustomColumn getBaseIdColumn() {
		return getColumn(COLUMN_BASE_ID);
	}

	/**
	 * Get the related id column index
	 * 
	 * @return related id column index
	 */
	public int getRelatedIdColumnIndex() {
		return getColumnIndex(COLUMN_RELATED_ID);
	}

	/**
	 * Get the related id column
	 * 
	 * @return related id column
	 */
	public UserCustomColumn getRelatedIdColumn() {
		return getColumn(COLUMN_RELATED_ID);
	}

}
