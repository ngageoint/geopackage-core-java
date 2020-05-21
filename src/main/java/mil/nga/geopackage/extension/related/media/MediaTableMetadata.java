package mil.nga.geopackage.extension.related.media;

import java.util.ArrayList;
import java.util.List;

import mil.nga.geopackage.user.UserTableMetadata;
import mil.nga.geopackage.user.custom.UserCustomColumn;

// TODO
public class MediaTableMetadata extends UserTableMetadata<UserCustomColumn> {

	public static MediaTableMetadata create() {
		return new MediaTableMetadata();
	}

	public static MediaTableMetadata create(String tableName) {
		return new MediaTableMetadata(tableName, null, null);
	}

	public static MediaTableMetadata create(String tableName,
			boolean autoincrement) {
		return new MediaTableMetadata(tableName, null, autoincrement, null);
	}

	public static MediaTableMetadata create(String tableName,
			List<UserCustomColumn> additionalColumns) {
		return new MediaTableMetadata(tableName, null, additionalColumns);
	}

	public static MediaTableMetadata create(String tableName,
			boolean autoincrement, List<UserCustomColumn> additionalColumns) {
		return new MediaTableMetadata(tableName, null, autoincrement,
				additionalColumns);
	}

	public static MediaTableMetadata create(String tableName,
			String idColumnName) {
		return new MediaTableMetadata(tableName, idColumnName, null);
	}

	public static MediaTableMetadata create(String tableName,
			String idColumnName, boolean autoincrement) {
		return new MediaTableMetadata(tableName, idColumnName, autoincrement,
				null);
	}

	public static MediaTableMetadata create(String tableName,
			String idColumnName, List<UserCustomColumn> additionalColumns) {
		return new MediaTableMetadata(tableName, idColumnName,
				additionalColumns);
	}

	public static MediaTableMetadata create(String tableName,
			String idColumnName, boolean autoincrement,
			List<UserCustomColumn> additionalColumns) {
		return new MediaTableMetadata(tableName, idColumnName, autoincrement,
				additionalColumns);
	}

	public MediaTableMetadata() {

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
