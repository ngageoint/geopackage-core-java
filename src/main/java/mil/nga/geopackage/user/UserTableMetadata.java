package mil.nga.geopackage.user;

import java.util.List;

import mil.nga.geopackage.BoundingBox;

// TODO
public abstract class UserTableMetadata<TColumn extends UserColumn> {

	public static final String DEFAULT_ID_COLUMN_NAME = "id";

	protected String tableName;

	protected String dataType;

	protected BoundingBox boundingBox;

	protected String idColumnName;

	protected boolean autoincrement = UserTable.DEFAULT_AUTOINCREMENT;

	protected List<TColumn> additionalColumns;

	protected List<TColumn> columns;

	public UserTableMetadata() {

	}

	public abstract String getDefaultDataType();

	public abstract List<TColumn> buildColumns();

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getDataType() {
		return dataType != null ? dataType : getDefaultDataType();
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public BoundingBox getBoundingBox() {
		return boundingBox;
	}

	public void setBoundingBox(BoundingBox boundingBox) {
		this.boundingBox = boundingBox;
	}

	public String getIdColumnName() {
		return idColumnName != null ? idColumnName : DEFAULT_ID_COLUMN_NAME;
	}

	public void setIdColumnName(String idColumnName) {
		this.idColumnName = idColumnName;
	}

	public boolean isAutoincrement() {
		return autoincrement;
	}

	public void setAutoincrement(boolean autoincrement) {
		this.autoincrement = autoincrement;
	}

	public List<TColumn> getAdditionalColumns() {
		return additionalColumns;
	}

	public void setAdditionalColumns(List<TColumn> additionalColumns) {
		this.additionalColumns = additionalColumns;
	}

	public List<TColumn> getColumns() {
		return columns;
	}

	public void setColumns(List<TColumn> columns) {
		this.columns = columns;
	}

}
