package mil.nga.geopackage.attributes;

import java.util.List;

import mil.nga.geopackage.db.table.TableColumn;
import mil.nga.geopackage.user.UserTableReader;

/**
 * Reads the metadata from an existing attributes table
 * 
 * @author osbornb
 * @since 3.3.0
 */
public class AttributesTableReader
		extends UserTableReader<AttributesColumn, AttributesTable> {

	/**
	 * Constructor
	 * 
	 * @param tableName
	 *            table name
	 */
	public AttributesTableReader(String tableName) {
		super(tableName);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected AttributesTable createTable(String tableName,
			List<AttributesColumn> columnList) {
		return new AttributesTable(tableName, columnList);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected AttributesColumn createColumn(TableColumn tableColumn) {
		return AttributesColumn.createColumn(tableColumn);
	}

}
