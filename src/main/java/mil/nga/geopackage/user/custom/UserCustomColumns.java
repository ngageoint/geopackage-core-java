package mil.nga.geopackage.user.custom;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import mil.nga.geopackage.user.UserColumns;

/**
 * Collection of user custom columns
 * 
 * @author osbornb
 * @since 3.5.0
 */
public class UserCustomColumns extends UserColumns<UserCustomColumn> {

	/**
	 * Required columns
	 */
	private Collection<String> requiredColumns;

	/**
	 * Constructor
	 * 
	 * @param tableName
	 *            table name
	 * @param columns
	 *            columns
	 */
	public UserCustomColumns(String tableName, List<UserCustomColumn> columns) {
		this(tableName, columns, null);
	}

	/**
	 * Constructor
	 * 
	 * @param tableName
	 *            table name
	 * @param columns
	 *            columns
	 * @param requiredColumns
	 *            list of required columns
	 */
	public UserCustomColumns(String tableName, List<UserCustomColumn> columns,
			Collection<String> requiredColumns) {
		this(tableName, columns, requiredColumns, false);
	}

	/**
	 * Constructor
	 * 
	 * @param tableName
	 *            table name
	 * @param columns
	 *            columns
	 * @param custom
	 *            custom column specification
	 */
	public UserCustomColumns(String tableName, List<UserCustomColumn> columns,
			boolean custom) {
		this(tableName, columns, null, custom);
	}

	/**
	 * Constructor
	 * 
	 * @param tableName
	 *            table name
	 * @param columns
	 *            columns
	 * @param requiredColumns
	 *            list of required columns
	 * @param custom
	 *            custom column specification
	 */
	public UserCustomColumns(String tableName, List<UserCustomColumn> columns,
			Collection<String> requiredColumns, boolean custom) {
		super(tableName, columns, custom);
		this.requiredColumns = requiredColumns;

		updateColumns();
	}

	/**
	 * Copy Constructor
	 * 
	 * @param userCustomColumns
	 *            user custom columns
	 */
	public UserCustomColumns(UserCustomColumns userCustomColumns) {
		super(userCustomColumns);
		if (userCustomColumns.requiredColumns != null) {
			this.requiredColumns = new ArrayList<>(
					userCustomColumns.requiredColumns);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UserCustomColumns copy() {
		return new UserCustomColumns(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void updateColumns() {
		super.updateColumns();

		if (!isCustom() && requiredColumns != null
				&& !requiredColumns.isEmpty()) {

			Set<String> search = new HashSet<>(requiredColumns);
			Map<String, Integer> found = new HashMap<>();

			// Find the required columns
			for (UserCustomColumn column : getColumns()) {

				String columnName = column.getName();
				int columnIndex = column.getIndex();

				if (search.contains(columnName)) {
					Integer previousIndex = found.get(columnName);
					duplicateCheck(columnIndex, previousIndex, columnName);
					found.put(columnName, columnIndex);
				}
			}

			// Verify the required columns were found
			for (String requiredColumn : search) {
				missingCheck(found.get(requiredColumn), requiredColumn);
			}
		}
	}

	/**
	 * Get the required columns
	 * 
	 * @return required columns
	 */
	public Collection<String> getRequiredColumns() {
		return requiredColumns;
	}

	/**
	 * Set the required columns
	 * 
	 * @param requiredColumns
	 *            required columns
	 */
	public void setRequiredColumns(Collection<String> requiredColumns) {
		this.requiredColumns = requiredColumns;
	}

}
