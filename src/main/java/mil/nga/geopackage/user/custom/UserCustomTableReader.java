package mil.nga.geopackage.user.custom;

import java.util.List;

import mil.nga.geopackage.db.GeoPackageCoreConnection;
import mil.nga.geopackage.db.table.TableColumn;
import mil.nga.geopackage.user.UserTableReader;

/**
 * Reads the metadata from an existing user custom table
 * 
 * @author osbornb
 * @since 3.3.0
 */
public class UserCustomTableReader
		extends UserTableReader<UserCustomColumn, UserCustomTable> {

	/**
	 * Constructor
	 * 
	 * @param tableName
	 *            table name
	 */
	public UserCustomTableReader(String tableName) {
		super(tableName);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected UserCustomTable createTable(String tableName,
			List<UserCustomColumn> columnList) {
		return new UserCustomTable(tableName, columnList);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected UserCustomColumn createColumn(TableColumn tableColumn) {
		return UserCustomColumn.createColumn(tableColumn);
	}

	/**
	 * Read the table
	 * 
	 * @param connection
	 *            GeoPackage connection
	 * @param tableName
	 *            table name
	 * @return table
	 */
	public static UserCustomTable readTable(GeoPackageCoreConnection connection,
			String tableName) {
		UserCustomTableReader tableReader = new UserCustomTableReader(
				tableName);
		UserCustomTable customTable = tableReader.readTable(connection);
		return customTable;
	}

}
