package mil.nga.geopackage.attributes;

import java.util.List;

import mil.nga.geopackage.user.UserColumns;

/**
 * Collection of attributes columns
 * 
 * @author osbornb
 * @since 3.5.0
 */
public class AttributesColumns extends UserColumns<AttributesColumn> {

	/**
	 * Constructor
	 * 
	 * @param tableName
	 *            table name
	 * @param columns
	 *            columns
	 */
	public AttributesColumns(String tableName, List<AttributesColumn> columns) {
		this(tableName, columns, false);
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
	public AttributesColumns(String tableName, List<AttributesColumn> columns,
			boolean custom) {
		super(tableName, columns, custom);

		updateColumns();
	}

	/**
	 * Copy Constructor
	 * 
	 * @param attributesColumns
	 *            attributes columns
	 */
	public AttributesColumns(AttributesColumns attributesColumns) {
		super(attributesColumns);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AttributesColumns copy() {
		return new AttributesColumns(this);
	}

}
