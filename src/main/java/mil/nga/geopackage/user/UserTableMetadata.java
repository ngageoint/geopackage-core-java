package mil.nga.geopackage.user;

import java.util.List;

/**
 * User Table Metadata for defining table creation information
 * 
 * @author osbornb
 *
 * @param <TColumn>
 *            user column type
 * @since 4.0.0
 */
public abstract class UserTableMetadata<TColumn extends UserColumn> {

	/**
	 * Default ID column name
	 */
	public static final String DEFAULT_ID_COLUMN_NAME = "id";

	/**
	 * Table name
	 */
	protected String tableName;

	/**
	 * Data type
	 */
	protected String dataType;

	/**
	 * ID column name
	 */
	protected String idColumnName;

	/**
	 * ID autoincrement flag
	 */
	protected boolean autoincrement = UserTable.DEFAULT_AUTOINCREMENT;

	/**
	 * Additional table columns
	 */
	protected List<TColumn> additionalColumns;

	/**
	 * Table columns
	 */
	protected List<TColumn> columns;

	/**
	 * Constructor
	 */
	public UserTableMetadata() {

	}

	/**
	 * Get the default data type
	 * 
	 * @return default data type
	 */
	public abstract String getDefaultDataType();

	/**
	 * Build the table columns
	 * 
	 * @return table columns
	 */
	public abstract List<TColumn> buildColumns();

	/**
	 * Get the table name
	 * 
	 * @return table name
	 */
	public String getTableName() {
		return tableName;
	}

	/**
	 * Set the table name
	 * 
	 * @param tableName
	 *            table name
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	/**
	 * Get the data type
	 * 
	 * @return data type
	 */
	public String getDataType() {
		return dataType != null ? dataType : getDefaultDataType();
	}

	/**
	 * Set the data type
	 * 
	 * @param dataType
	 *            data type
	 */
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	/**
	 * Get the id column name
	 * 
	 * @return id column name
	 */
	public String getIdColumnName() {
		return idColumnName != null ? idColumnName : DEFAULT_ID_COLUMN_NAME;
	}

	/**
	 * Set the id column name
	 * 
	 * @param idColumnName
	 *            id column name
	 */
	public void setIdColumnName(String idColumnName) {
		this.idColumnName = idColumnName;
	}

	/**
	 * Is id autocincrement enabled?
	 * 
	 * @return autoincrement flag
	 */
	public boolean isAutoincrement() {
		return autoincrement;
	}

	/**
	 * Set the id autoincrement flag
	 * 
	 * @param autoincrement
	 *            autoincrement flag
	 */
	public void setAutoincrement(boolean autoincrement) {
		this.autoincrement = autoincrement;
	}

	/**
	 * Get the additional table columns
	 * 
	 * @return columns
	 */
	public List<TColumn> getAdditionalColumns() {
		return additionalColumns;
	}

	/**
	 * Set the additional table columns
	 * 
	 * @param additionalColumns
	 *            columns
	 */
	public void setAdditionalColumns(List<TColumn> additionalColumns) {
		this.additionalColumns = additionalColumns;
	}

	/**
	 * Get the table columns
	 * 
	 * @return columns
	 */
	public List<TColumn> getColumns() {
		return columns;
	}

	/**
	 * Set the table columns
	 * 
	 * @param columns
	 *            columns
	 */
	public void setColumns(List<TColumn> columns) {
		this.columns = columns;
	}

}
