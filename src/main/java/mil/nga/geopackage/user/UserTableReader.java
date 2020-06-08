package mil.nga.geopackage.user;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.db.GeoPackageCoreConnection;
import mil.nga.geopackage.db.master.SQLiteMaster;
import mil.nga.geopackage.db.table.ColumnConstraints;
import mil.nga.geopackage.db.table.TableColumn;
import mil.nga.geopackage.db.table.TableConstraints;
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
	 * Logger
	 */
	private static final Logger log = Logger
			.getLogger(UserTableReader.class.getName());

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

		TableConstraints constraints = SQLiteMaster.queryForConstraints(db,
				tableName);

		for (TableColumn tableColumn : tableInfo.getColumns()) {
			if (tableColumn.getDataType() == null) {
				log.log(Level.SEVERE,
						"Unexpected column data type: '" + tableColumn.getType()
								+ "', column: " + tableColumn.getName());
			}
			TColumn column = createColumn(tableColumn);

			ColumnConstraints columnConstraints = constraints
					.getColumnConstraints(column.getName());
			if (columnConstraints != null
					&& columnConstraints.hasConstraints()) {
				column.clearConstraints();
				column.addConstraints(columnConstraints);
			}

			columnList.add(column);
		}

		TTable table = createTable(tableName, columnList);

		table.addConstraints(constraints.getTableConstraints());

		return table;
	}

}
