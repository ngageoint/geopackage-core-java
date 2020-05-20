package mil.nga.geopackage.extension.related.media;

import java.util.ArrayList;
import java.util.List;

import mil.nga.geopackage.user.UserTableMetadata;
import mil.nga.geopackage.user.custom.UserCustomColumn;

// TODO
public class MediaTableMetadata extends UserTableMetadata<UserCustomColumn> {

	public MediaTableMetadata() {

	}

	public MediaTableMetadata(String tableName) {
		this.tableName = tableName;
	}

	public MediaTableMetadata(String tableName, boolean autoincrement) {
		this.tableName = tableName;
		this.autoincrement = autoincrement;
	}

	public MediaTableMetadata(String tableName,
			List<UserCustomColumn> additionalColumns) {
		this.tableName = tableName;
		this.additionalColumns = additionalColumns;
	}

	public MediaTableMetadata(String tableName, boolean autoincrement,
			List<UserCustomColumn> additionalColumns) {
		this.tableName = tableName;
		this.autoincrement = autoincrement;
		this.additionalColumns = additionalColumns;
	}

	public MediaTableMetadata(String tableName, String idColumnName) {
		this.tableName = tableName;
		this.idColumnName = idColumnName;
	}

	public MediaTableMetadata(String tableName, String idColumnName,
			boolean autoincrement) {
		this.tableName = tableName;
		this.idColumnName = idColumnName;
		this.autoincrement = autoincrement;
	}

	public MediaTableMetadata(String tableName, String idColumnName,
			List<UserCustomColumn> additionalColumns) {
		this.tableName = tableName;
		this.idColumnName = idColumnName;
		this.additionalColumns = additionalColumns;
	}

	public MediaTableMetadata(String tableName, String idColumnName,
			boolean autoincrement, List<UserCustomColumn> additionalColumns) {
		this.tableName = tableName;
		this.idColumnName = idColumnName;
		this.autoincrement = autoincrement;
		this.additionalColumns = additionalColumns;
	}

	@Override
	public String getDefaultDataType() {
		return null;
	}

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
