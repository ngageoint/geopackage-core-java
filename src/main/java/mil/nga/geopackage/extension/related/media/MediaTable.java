package mil.nga.geopackage.extension.related.media;

import java.util.ArrayList;
import java.util.List;

import mil.nga.geopackage.db.GeoPackageDataType;
import mil.nga.geopackage.extension.related.RelationType;
import mil.nga.geopackage.extension.related.UserRelatedTable;
import mil.nga.geopackage.user.custom.UserCustomColumn;
import mil.nga.geopackage.user.custom.UserCustomTable;

/**
 * Media Requirements Class User-Defined Related Data Table
 * 
 * @author osbornb
 * @since 3.0.1
 */
public class MediaTable extends UserRelatedTable {

	/**
	 * User-Defined Media Table relation name
	 */
	public static final RelationType RELATION_TYPE = RelationType.MEDIA;

	/**
	 * Autoincrement primary key, optional name
	 */
	public static final String COLUMN_ID = "id";

	/**
	 * Multimedia content column name
	 */
	public static final String COLUMN_DATA = "data";

	/**
	 * Mime-type of data column name
	 */
	public static final String COLUMN_CONTENT_TYPE = "content_type";

	/**
	 * Create a media table with the minimum required columns
	 * 
	 * @param tableName
	 *            table name
	 * @return media table
	 */
	public static MediaTable create(String tableName) {
		return create(tableName, null, null);
	}

	/**
	 * Create a media table with the minimum required columns followed by the
	 * additional columns
	 * 
	 * @param tableName
	 *            table name
	 * @param additionalColumns
	 *            additional columns
	 * @return media table
	 */
	public static MediaTable create(String tableName,
			List<UserCustomColumn> additionalColumns) {
		return create(tableName, null, additionalColumns);
	}

	/**
	 * Create a media table with the id column and minimum required columns
	 * 
	 * @param tableName
	 *            table name
	 * @param idColumnName
	 *            id column name
	 * @return media table
	 */
	public static MediaTable create(String tableName, String idColumnName) {
		return create(tableName, idColumnName, null);
	}

	/**
	 * Create a media table with the id column and minimum required columns
	 * followed by the additional columns
	 * 
	 * @param tableName
	 *            table name
	 * @param idColumnName
	 *            id column name
	 * @param additionalColumns
	 *            additional columns
	 * @return media table
	 */
	public static MediaTable create(String tableName, String idColumnName,
			List<UserCustomColumn> additionalColumns) {

		List<UserCustomColumn> columns = new ArrayList<>();
		columns.addAll(createRequiredColumns(idColumnName));

		if (additionalColumns != null) {
			columns.addAll(additionalColumns);
		}

		return new MediaTable(tableName, columns, idColumnName);
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
		columns.add(createDataColumn(startingIndex++));
		columns.add(createContentTypeColumn(startingIndex++));

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
	 * Create a data column
	 * 
	 * @param index
	 *            column index
	 * @return data column
	 */
	public static UserCustomColumn createDataColumn(int index) {
		return UserCustomColumn.createColumn(index, COLUMN_DATA,
				GeoPackageDataType.BLOB, true, null);
	}

	/**
	 * Create a content type column
	 * 
	 * @param index
	 *            column index
	 * @return content type column
	 */
	public static UserCustomColumn createContentTypeColumn(int index) {
		return UserCustomColumn.createColumn(index, COLUMN_CONTENT_TYPE,
				GeoPackageDataType.TEXT, true, null);
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
		requiredColumns.add(COLUMN_DATA);
		requiredColumns.add(COLUMN_CONTENT_TYPE);
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
	protected MediaTable(String tableName, List<UserCustomColumn> columns) {
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
	protected MediaTable(String tableName, List<UserCustomColumn> columns,
			String idColumnName) {
		super(tableName, RELATION_TYPE.getName(), RELATION_TYPE.getDataType(),
				columns, requiredColumns(idColumnName));
	}

	/**
	 * Constructor
	 * 
	 * @param table
	 *            user custom table
	 */
	protected MediaTable(UserCustomTable table) {
		super(RELATION_TYPE.getName(), RELATION_TYPE.getDataType(), table);
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
	 * Get the data column index
	 * 
	 * @return data column index
	 */
	public int getDataColumnIndex() {
		return getColumnIndex(COLUMN_DATA);
	}

	/**
	 * Get the data column
	 * 
	 * @return data column
	 */
	public UserCustomColumn getDataColumn() {
		return getColumn(COLUMN_DATA);
	}

	/**
	 * Get the content type column index
	 * 
	 * @return content type column index
	 */
	public int getContentTypeColumnIndex() {
		return getColumnIndex(COLUMN_CONTENT_TYPE);
	}

	/**
	 * Get the content type column
	 * 
	 * @return content type column
	 */
	public UserCustomColumn getContentTypeColumn() {
		return getColumn(COLUMN_CONTENT_TYPE);
	}

}
