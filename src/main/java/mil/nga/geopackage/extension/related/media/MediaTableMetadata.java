package mil.nga.geopackage.extension.related.media;

import java.util.ArrayList;
import java.util.List;

import mil.nga.geopackage.user.UserTableMetadata;
import mil.nga.geopackage.user.custom.UserCustomColumn;

/**
 * Media Table Metadata for defining table creation information
 * 
 * @author osbornb
 * @since 4.0.0
 */
public class MediaTableMetadata extends UserTableMetadata<UserCustomColumn> {

	/**
	 * Create metadata
	 * 
	 * @return metadata
	 */
	public static MediaTableMetadata create() {
		return new MediaTableMetadata();
	}

	/**
	 * Create metadata
	 * 
	 * @param tableName
	 *            table name
	 * @return metadata
	 */
	public static MediaTableMetadata create(String tableName) {
		return new MediaTableMetadata(tableName, null, null);
	}

	/**
	 * Create metadata
	 * 
	 * @param tableName
	 *            table name
	 * @param autoincrement
	 *            autoincrement ids
	 * @return metadata
	 */
	public static MediaTableMetadata create(String tableName,
			boolean autoincrement) {
		return new MediaTableMetadata(tableName, null, autoincrement, null);
	}

	/**
	 * Create metadata
	 * 
	 * @param tableName
	 *            table name
	 * @param additionalColumns
	 *            additional columns
	 * @return metadata
	 */
	public static MediaTableMetadata create(String tableName,
			List<UserCustomColumn> additionalColumns) {
		return new MediaTableMetadata(tableName, null, additionalColumns);
	}

	/**
	 * Create metadata
	 * 
	 * @param tableName
	 *            table name
	 * @param autoincrement
	 *            autoincrement ids
	 * @param additionalColumns
	 *            additional columns
	 * @return metadata
	 */
	public static MediaTableMetadata create(String tableName,
			boolean autoincrement, List<UserCustomColumn> additionalColumns) {
		return new MediaTableMetadata(tableName, null, autoincrement,
				additionalColumns);
	}

	/**
	 * Create metadata
	 * 
	 * @param tableName
	 *            table name
	 * @param idColumnName
	 *            id column name
	 * @return metadata
	 */
	public static MediaTableMetadata create(String tableName,
			String idColumnName) {
		return new MediaTableMetadata(tableName, idColumnName, null);
	}

	/**
	 * Create metadata
	 * 
	 * @param tableName
	 *            table name
	 * @param idColumnName
	 *            id column name
	 * @param autoincrement
	 *            autoincrement ids
	 * @return metadata
	 */
	public static MediaTableMetadata create(String tableName,
			String idColumnName, boolean autoincrement) {
		return new MediaTableMetadata(tableName, idColumnName, autoincrement,
				null);
	}

	/**
	 * Create metadata
	 * 
	 * @param tableName
	 *            table name
	 * @param idColumnName
	 *            id column name
	 * @param additionalColumns
	 *            additional columns
	 * @return metadata
	 */
	public static MediaTableMetadata create(String tableName,
			String idColumnName, List<UserCustomColumn> additionalColumns) {
		return new MediaTableMetadata(tableName, idColumnName,
				additionalColumns);
	}

	/**
	 * Create metadata
	 * 
	 * @param tableName
	 *            table name
	 * @param idColumnName
	 *            id column name
	 * @param autoincrement
	 *            autoincrement ids
	 * @param additionalColumns
	 *            additional columns
	 * @return metadata
	 */
	public static MediaTableMetadata create(String tableName,
			String idColumnName, boolean autoincrement,
			List<UserCustomColumn> additionalColumns) {
		return new MediaTableMetadata(tableName, idColumnName, autoincrement,
				additionalColumns);
	}

	/**
	 * Constructor
	 */
	public MediaTableMetadata() {

	}

	/**
	 * Constructor
	 * 
	 * @param tableName
	 *            table name
	 * @param idColumnName
	 *            id column name
	 * @param additionalColumns
	 *            additional columns
	 */
	public MediaTableMetadata(String tableName, String idColumnName,
			List<UserCustomColumn> additionalColumns) {
		this.tableName = tableName;
		this.idColumnName = idColumnName;
		this.additionalColumns = additionalColumns;
	}

	/**
	 * Constructor
	 * 
	 * @param tableName
	 *            table name
	 * @param idColumnName
	 *            id column name
	 * @param autoincrement
	 *            autoincrement ids
	 * @param additionalColumns
	 *            additional columns
	 */
	public MediaTableMetadata(String tableName, String idColumnName,
			boolean autoincrement, List<UserCustomColumn> additionalColumns) {
		this.tableName = tableName;
		this.idColumnName = idColumnName;
		this.autoincrement = autoincrement;
		this.additionalColumns = additionalColumns;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDefaultDataType() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<UserCustomColumn> buildColumns() {

		List<UserCustomColumn> mediaColumns = getColumns();

		if (mediaColumns == null) {

			mediaColumns = new ArrayList<>();
			mediaColumns.addAll(MediaTable.createRequiredColumns(
					getIdColumnName(), isAutoincrement()));

			List<UserCustomColumn> additional = getAdditionalColumns();
			if (additional != null) {
				mediaColumns.addAll(additional);
			}

		}

		return mediaColumns;
	}

}
