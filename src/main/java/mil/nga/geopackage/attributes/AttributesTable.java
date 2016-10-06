package mil.nga.geopackage.attributes;

import java.util.List;

import mil.nga.geopackage.user.UserTable;

/**
 * Represents a user attributes table
 * 
 * @author osbornb
 * @since 1.2.1
 */
public class AttributesTable extends UserTable<AttributesColumn> {

	/**
	 * Constructor
	 * 
	 * @param tableName
	 *            table name
	 * @param columns
	 *            attributes columns
	 */
	public AttributesTable(String tableName, List<AttributesColumn> columns) {
		super(tableName, columns);
	}

}
