package mil.nga.geopackage.user.custom;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
		super(tableName, columns);

		if (requiredColumns != null && !requiredColumns.isEmpty()) {

			Set<String> search = new HashSet<>(requiredColumns);
			Map<String, Integer> found = new HashMap<>();

			// Find the required columns
			for (UserCustomColumn column : columns) {

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
	 * Constructor
	 * 
	 * @param userCustomTable
	 *            user custom table
	 */
	public UserCustomTable(UserCustomTable userCustomTable) {
		super(userCustomTable);
	}

}
