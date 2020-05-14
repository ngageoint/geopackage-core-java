package mil.nga.geopackage.attributes;

import java.util.List;

import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.contents.Contents;
import mil.nga.geopackage.contents.ContentsDataType;
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
		super(new AttributesColumns(tableName, columns));
	}

	/**
	 * Copy Constructor
	 * 
	 * @param attributesTable
	 *            attributes table
	 * @since 3.3.0
	 */
	public AttributesTable(AttributesTable attributesTable) {
		super(attributesTable);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AttributesTable copy() {
		return new AttributesTable(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDataType() {
		return getDataType(ContentsDataType.ATTRIBUTES.getName());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AttributesColumns getUserColumns() {
		return (AttributesColumns) super.getUserColumns();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AttributesColumns createUserColumns(List<AttributesColumn> columns) {
		return new AttributesColumns(getTableName(), columns, true);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void validateContents(Contents contents) {
		// Verify the Contents have an attributes data type
		if (!contents.isAttributesTypeOrUnknown()) {
			throw new GeoPackageException(
					"The " + Contents.class.getSimpleName() + " of an "
							+ AttributesTable.class.getSimpleName()
							+ " must have a data type of "
							+ ContentsDataType.ATTRIBUTES.getName()
							+ ". actual type: " + contents.getDataTypeName());
		}
	}

}
