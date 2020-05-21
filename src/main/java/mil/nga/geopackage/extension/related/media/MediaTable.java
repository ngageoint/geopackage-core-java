package mil.nga.geopackage.extension.related.media;

import java.util.ArrayList;
import java.util.List;

import mil.nga.geopackage.db.GeoPackageDataType;
import mil.nga.geopackage.extension.related.RelationType;
import mil.nga.geopackage.extension.related.UserRelatedTable;
import mil.nga.geopackage.user.UserColumn;
import mil.nga.geopackage.user.UserTable;
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
	 * Multimedia content column name
	 */
	public static final String COLUMN_DATA = "data";

	/**
	 * Mime-type of data column name
	 */
	public static final String COLUMN_CONTENT_TYPE = "content_type";

	/**
	 * Create a media table with the metadata
	 * 
	 * @param metadata
	 *            media table metadata
	 * @return media table
	 * @since 4.0.0
	 */
	public static MediaTable create(MediaTableMetadata metadata) {
		List<UserCustomColumn> columns = metadata.buildColumns();
		return new MediaTable(metadata.getTableName(), columns,
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
			idColumnName = MediaTableMetadata.DEFAULT_ID_COLUMN_NAME;
		}

		List<UserCustomColumn> columns = new ArrayList<>();
		columns.add(createIdColumn(idColumnName, autoincrement));
		columns.add(createDataColumn());
		columns.add(createContentTypeColumn());

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
			idColumnName = MediaTableMetadata.DEFAULT_ID_COLUMN_NAME;
		}

		List<UserCustomColumn> columns = new ArrayList<>();
		columns.add(
				createIdColumn(startingIndex++, idColumnName, autoincrement));
		columns.add(createDataColumn(startingIndex++));
		columns.add(createContentTypeColumn(startingIndex++));

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
	 * Create a data column
	 * 
	 * @return data column
	 * @since 3.3.0
	 */
	public static UserCustomColumn createDataColumn() {
		return createDataColumn(UserColumn.NO_INDEX);
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
				GeoPackageDataType.BLOB, true);
	}

	/**
	 * Create a content type column
	 * 
	 * @return content type column
	 * @since 3.3.0
	 */
	public static UserCustomColumn createContentTypeColumn() {
		return createContentTypeColumn(UserColumn.NO_INDEX);
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
				GeoPackageDataType.TEXT, true);
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
			idColumnName = MediaTableMetadata.DEFAULT_ID_COLUMN_NAME;
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
	public MediaTable(String tableName, List<UserCustomColumn> columns) {
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
	public MediaTable(String tableName, List<UserCustomColumn> columns,
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
	public MediaTable(UserCustomTable table) {
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
