package mil.nga.geopackage.tiles.user;

import java.util.List;

import mil.nga.geopackage.db.table.TableColumn;
import mil.nga.geopackage.user.UserTableReader;

/**
 * Reads the metadata from an existing tile table
 * 
 * @author osbornb
 * @since 3.3.0
 */
public class TileTableReader extends UserTableReader<TileColumn, TileTable> {

	/**
	 * Constructor
	 * 
	 * @param tableName
	 *            table name
	 */
	public TileTableReader(String tableName) {
		super(tableName);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected TileTable createTable(String tableName,
			List<TileColumn> columnList) {
		return new TileTable(tableName, columnList);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected TileColumn createColumn(TableColumn tableColumn) {
		return TileColumn.createColumn(tableColumn);
	}

}
