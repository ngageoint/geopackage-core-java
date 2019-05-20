package mil.nga.geopackage.user;

import java.util.ArrayList;
import java.util.List;

import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.db.GeoPackageCoreConnection;
import mil.nga.geopackage.db.master.SQLiteMaster;
import mil.nga.geopackage.db.table.TableColumn;
import mil.nga.geopackage.db.table.TableInfo;

/**
 * Reads the metadata from an existing user table
 * 
 * @param <TColumn>
 *            column type
 * @param <TTable>
 *            table type
 * 
 * @author osbornb
 */
public abstract class UserTableReader<TColumn extends UserColumn, TTable extends UserTable<TColumn>> {

	/**
	 * Table name
	 */
	private final String tableName;

	/**
	 * Constructor
	 * 
	 * @param tableName
	 *            table name
	 */
	protected UserTableReader(String tableName) {
		this.tableName = tableName;
	}

	/**
	 * Create the table
	 * 
	 * @param tableName
	 *            table name
	 * @param columnList
	 *            column list
	 * @return table
	 */
	protected abstract TTable createTable(String tableName,
			List<TColumn> columnList);

	/**
	 * Create the column
	 * 
	 * @param tableColumn
	 *            table column
	 * @return column
	 */
	protected abstract TColumn createColumn(TableColumn tableColumn);

	/**
	 * Read the table
	 * 
	 * @param db
	 *            connection
	 * @return table
	 */
	public TTable readTable(GeoPackageCoreConnection db) {

		List<TColumn> columnList = new ArrayList<TColumn>();

		TableInfo tableInfo = TableInfo.info(db, tableName);
		if (tableInfo == null) {
			throw new GeoPackageException("Table does not exist: " + tableName);
		}

		for (TableColumn tableColumn : tableInfo.getColumns()) {
			if (tableColumn.getDataType() == null) {
				throw new GeoPackageException("Unsupported column data type "
						+ tableColumn.getType());
			}
			TColumn column = createColumn(tableColumn);
			columnList.add(column);
		}

		TTable table = createTable(tableName, columnList);

		table.addConstraints(SQLiteMaster.queryForConstraints(db, tableName));

		return table;
	}

}
