package mil.nga.geopackage.extension.related.simple;

import java.util.ArrayList;
import java.util.List;

import mil.nga.geopackage.user.UserTableMetadata;
import mil.nga.geopackage.user.custom.UserCustomColumn;

/**
 * Simple Attributes Table Metadata for defining table creation information
 * 
 * @author osbornb
 * @since 4.0.0
 */
public class SimpleAttributesTableMetadata
		extends UserTableMetadata<UserCustomColumn> {

	/**
	 * Create metadata
	 * 
	 * @return metadata
	 */
	public static SimpleAttributesTableMetadata create() {
		return new SimpleAttributesTableMetadata();
	}

	/**
	 * Create metadata
	 * 
	 * @param tableName
	 *            table name
	 * @return metadata
	 */
	public static SimpleAttributesTableMetadata create(String tableName) {
		return new SimpleAttributesTableMetadata(tableName, null, null);
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
	public static SimpleAttributesTableMetadata create(String tableName,
			boolean autoincrement) {
		return new SimpleAttributesTableMetadata(tableName, null, autoincrement,
				null);
	}

	/**
	 * Create metadata
	 * 
	 * @param tableName
	 *            table name
	 * @param columns
	 *            columns
	 * @return metadata
	 */
	public static SimpleAttributesTableMetadata create(String tableName,
			List<UserCustomColumn> columns) {
		return new SimpleAttributesTableMetadata(tableName, null, columns);
	}

	/**
	 * Create metadata
	 * 
	 * @param tableName
	 *            table name
	 * @param autoincrement
	 *            autoincrement ids
	 * @param columns
	 *            columns
	 * @return metadata
	 */
	public static SimpleAttributesTableMetadata create(String tableName,
			boolean autoincrement, List<UserCustomColumn> columns) {
		return new SimpleAttributesTableMetadata(tableName, null, autoincrement,
				columns);
	}

	/**
	 * Create metadata
	 * 
	 * @param tableName
	 *            table name
	 * @param idColumnName
	 *            id column name
	 * @param columns
	 *            columns
	 * @return metadata
	 */
	public static SimpleAttributesTableMetadata create(String tableName,
			String idColumnName, List<UserCustomColumn> columns) {
		return new SimpleAttributesTableMetadata(tableName, idColumnName,
				columns);
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
	 * @param columns
	 *            columns
	 * @return metadata
	 */
	public static SimpleAttributesTableMetadata create(String tableName,
			String idColumnName, boolean autoincrement,
			List<UserCustomColumn> columns) {
		return new SimpleAttributesTableMetadata(tableName, idColumnName,
				autoincrement, columns);
	}

	/**
	 * Constructor
	 */
	public SimpleAttributesTableMetadata() {

	}

	/**
	 * Constructor
	 * 
	 * @param tableName
	 *            table name
	 * @param idColumnName
	 *            id column name
	 * @param columns
	 *            columns
	 */
	public SimpleAttributesTableMetadata(String tableName, String idColumnName,
			List<UserCustomColumn> columns) {
		this.tableName = tableName;
		this.idColumnName = idColumnName;
		this.additionalColumns = columns;
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
	 * @param columns
	 *            columns
	 */
	public SimpleAttributesTableMetadata(String tableName, String idColumnName,
			boolean autoincrement, List<UserCustomColumn> columns) {
		this.tableName = tableName;
		this.idColumnName = idColumnName;
		this.autoincrement = autoincrement;
		this.additionalColumns = columns;
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

		List<UserCustomColumn> simpleAttributeColumns = getColumns();

		if (simpleAttributeColumns == null) {

			simpleAttributeColumns = new ArrayList<>();
			simpleAttributeColumns
					.addAll(SimpleAttributesTable.createRequiredColumns(
							getIdColumnName(), isAutoincrement()));

			List<UserCustomColumn> additional = getAdditionalColumns();
			if (additional != null) {
				simpleAttributeColumns.addAll(additional);
			}

		}

		return simpleAttributeColumns;
	}

}
