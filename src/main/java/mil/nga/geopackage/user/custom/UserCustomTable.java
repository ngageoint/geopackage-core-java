package mil.nga.geopackage.user.custom;

import java.util.Collection;
import java.util.List;

import mil.nga.geopackage.user.UserTable;

/**
 * Represents a user custom table
 * 
 * @author osbornb
 * @since 3.0.1
 */
public class UserCustomTable extends UserTable<UserCustomColumn> {

	/**
	 * Constructor
	 * 
	 * @param tableName
	 *            table name
	 * @param columns
	 *            list of columns
	 */
	public UserCustomTable(String tableName, List<UserCustomColumn> columns) {
		this(tableName, columns, null);
	}

	/**
	 * Constructor
	 * 
	 * @param tableName
	 *            table name
	 * @param columns
	 *            list of columns
	 * @param requiredColumns
	 *            list of required columns
	 */
	public UserCustomTable(String tableName, List<UserCustomColumn> columns,
			Collection<String> requiredColumns) {
		this(new UserCustomColumns(tableName, columns, requiredColumns));
	}

	/**
	 * Constructor
	 * 
	 * @param columns
	 *            columns
	 * @since 3.5.0
	 */
	public UserCustomTable(UserCustomColumns columns) {
		super(columns);
	}

	/**
	 * Copy Constructor
	 * 
	 * @param userCustomTable
	 *            user custom table
	 */
	public UserCustomTable(UserCustomTable userCustomTable) {
		super(userCustomTable);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UserCustomTable copy() {
		return new UserCustomTable(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDataType() {
		return getDataType(null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UserCustomColumns getUserColumns() {
		return (UserCustomColumns) super.getUserColumns();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UserCustomColumns createUserColumns(List<UserCustomColumn> columns) {
		return new UserCustomColumns(getTableName(), columns,
				getRequiredColumns(), true);
	}

	/**
	 * Get the required columns
	 * 
	 * @return required columns
	 */
	public Collection<String> getRequiredColumns() {
		return getUserColumns().getRequiredColumns();
	}

}
