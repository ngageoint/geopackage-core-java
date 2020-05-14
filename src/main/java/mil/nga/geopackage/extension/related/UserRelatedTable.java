package mil.nga.geopackage.extension.related;

import java.util.Collection;
import java.util.List;

import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.contents.Contents;
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
	 * Contents data type
	 */
	private final String dataType;

	/**
	 * Constructor
	 * 
	 * @param tableName
	 *            table name
	 * @param relationName
	 *            relation name
	 * @param dataType
	 *            contents data type
	 * @param columns
	 *            list of columns
	 * @since 3.1.0
	 */
	public UserRelatedTable(String tableName, String relationName,
			String dataType, List<UserCustomColumn> columns) {
		this(tableName, relationName, dataType, columns, null);
	}

	/**
	 * Constructor
	 * 
	 * @param tableName
	 *            table name
	 * @param relationName
	 *            relation name
	 * @param dataType
	 *            contents data type
	 * @param columns
	 *            list of columns
	 * @param requiredColumns
	 *            list of required columns
	 * @since 3.1.0
	 */
	public UserRelatedTable(String tableName, String relationName,
			String dataType, List<UserCustomColumn> columns,
			Collection<String> requiredColumns) {
		super(tableName, columns, requiredColumns);
		this.relationName = relationName;
		this.dataType = dataType;
	}

	/**
	 * Constructor
	 * 
	 * @param relationName
	 *            relation name
	 * @param dataType
	 *            contents data type
	 * @param userCustomTable
	 *            user custom table
	 * @since 3.1.0
	 */
	public UserRelatedTable(String relationName, String dataType,
			UserCustomTable userCustomTable) {
		super(userCustomTable);
		this.relationName = relationName;
		this.dataType = dataType;
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
	 * {@inheritDoc}
	 */
	@Override
	public String getDataType() {
		return dataType;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void validateContents(Contents contents) {
		// Verify the Contents have a relation name data type
		String contentsDataType = contents.getDataTypeName();
		if (contentsDataType == null || !contentsDataType.equals(dataType)) {
			throw new GeoPackageException("The "
					+ Contents.class.getSimpleName() + " of a "
					+ getClass().getSimpleName() + " must have a data type of "
					+ dataType);
		}
	}

}
