package mil.nga.geopackage.extension.related;

import java.util.Collection;
import java.util.List;

import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.core.contents.Contents;
import mil.nga.geopackage.user.custom.UserCustomColumn;
import mil.nga.geopackage.user.custom.UserCustomTable;

/**
 * User Defined Related Table
 * 
 * @author osbornb
 * @since 3.0.1
 */
public class UserRelatedTable extends UserCustomTable {

	/**
	 * Relation name
	 */
	private final String relationName;

	/**
	 * Foreign key to Contents
	 */
	private Contents contents;

	/**
	 * Constructor
	 * 
	 * @param tableName
	 *            table name
	 * @param relationName
	 *            relation name
	 * @param columns
	 *            list of columns
	 */
	public UserRelatedTable(String tableName, String relationName,
			List<UserCustomColumn> columns) {
		this(tableName, relationName, columns, null);
	}

	/**
	 * Constructor
	 * 
	 * @param tableName
	 *            table name
	 * @param relationName
	 *            relation name
	 * @param columns
	 *            list of columns
	 * @param requiredColumns
	 *            list of required columns
	 */
	public UserRelatedTable(String tableName, String relationName,
			List<UserCustomColumn> columns, Collection<String> requiredColumns) {
		super(tableName, columns, requiredColumns);
		this.relationName = relationName;
	}

	/**
	 * Constructor
	 * 
	 * @param relationName
	 *            relation name
	 * @param userCustomTable
	 *            user custom table
	 */
	public UserRelatedTable(String relationName, UserCustomTable userCustomTable) {
		super(userCustomTable);
		this.relationName = relationName;
	}

	/**
	 * Get the relation name
	 * 
	 * @return relation name
	 */
	public String getRelationName() {
		return relationName;
	}

	/**
	 * Get the contents
	 * 
	 * @return contents
	 */
	public Contents getContents() {
		return contents;
	}

	/**
	 * Set the contents
	 * 
	 * @param contents
	 *            contents
	 */
	public void setContents(Contents contents) {
		this.contents = contents;
		if (contents != null) {
			// Verify the Contents have a relation name data type
			String dataType = contents.getDataTypeString();
			if (dataType == null || !dataType.equals(relationName)) {
				throw new GeoPackageException("The "
						+ Contents.class.getSimpleName() + " of a "
						+ getClass().getSimpleName()
						+ " must have a data type of " + relationName);
			}
		}
	}

}
