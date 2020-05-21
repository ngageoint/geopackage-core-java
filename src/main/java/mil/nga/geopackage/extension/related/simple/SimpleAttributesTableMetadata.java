package mil.nga.geopackage.extension.related.simple;

import java.util.ArrayList;
import java.util.List;

import mil.nga.geopackage.user.UserTableMetadata;
import mil.nga.geopackage.user.custom.UserCustomColumn;

// TODO
public class SimpleAttributesTableMetadata
		extends UserTableMetadata<UserCustomColumn> {

	public static SimpleAttributesTableMetadata create() {
		return new SimpleAttributesTableMetadata();
	}

	public static SimpleAttributesTableMetadata create(String tableName) {
		return new SimpleAttributesTableMetadata(tableName, null, null);
	}

	public static SimpleAttributesTableMetadata create(String tableName,
			boolean autoincrement) {
		return new SimpleAttributesTableMetadata(tableName, null, autoincrement,
				null);
	}

	public static SimpleAttributesTableMetadata create(String tableName,
			List<UserCustomColumn> columns) {
		return new SimpleAttributesTableMetadata(tableName, null, columns);
	}

	public static SimpleAttributesTableMetadata create(String tableName,
			boolean autoincrement, List<UserCustomColumn> columns) {
		return new SimpleAttributesTableMetadata(tableName, null, autoincrement,
				columns);
	}

	public static SimpleAttributesTableMetadata create(String tableName,
			String idColumnName, List<UserCustomColumn> columns) {
		return new SimpleAttributesTableMetadata(tableName, idColumnName,
				columns);
	}

	public static SimpleAttributesTableMetadata create(String tableName,
			String idColumnName, boolean autoincrement,
			List<UserCustomColumn> columns) {
		return new SimpleAttributesTableMetadata(tableName, idColumnName,
				autoincrement, columns);
	}

	public SimpleAttributesTableMetadata() {

	}

	public SimpleAttributesTableMetadata(String tableName, String idColumnName,
			List<UserCustomColumn> columns) {
		this.tableName = tableName;
		this.idColumnName = idColumnName;
		this.additionalColumns = columns;
	}

	public SimpleAttributesTableMetadata(String tableName, String idColumnName,
			boolean autoincrement, List<UserCustomColumn> columns) {
		this.tableName = tableName;
		this.idColumnName = idColumnName;
		this.autoincrement = autoincrement;
		this.additionalColumns = columns;
	}

	@Override
	public String getDefaultDataType() {
		return null;
	}

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
