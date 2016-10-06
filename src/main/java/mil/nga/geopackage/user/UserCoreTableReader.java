package mil.nga.geopackage.user;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.db.CoreSQLUtils;

/**
 * Reads the metadata from an existing user table
 * 
 * @param <TColumn>
 * @param <TTable>
 * 
 * @author osbornb
 */
public abstract class UserCoreTableReader<TColumn extends UserColumn, TTable extends UserTable<TColumn>, TRow extends UserCoreRow<TColumn, TTable>, TResult extends UserCoreResult<TColumn, TTable, TRow>> {

	/**
	 * Logger
	 */
	private static final Logger logger = Logger
			.getLogger(UserCoreTableReader.class.getName());

	/**
	 * Index column
	 */
	private static final String CID = "cid";

	/**
	 * Name column
	 */
	private static final String NAME = "name";

	/**
	 * Type column
	 */
	private static final String TYPE = "type";

	/**
	 * Not null column
	 */
	private static final String NOT_NULL = "notnull";

	/**
	 * Primary key column
	 */
	private static final String PK = "pk";

	/**
	 * Default value column
	 */
	private static final String DFLT_VALUE = "dflt_value";

	/**
	 * Table name
	 */
	private final String tableName;

	/**
	 * Constructor
	 * 
	 * @param tableName
	 */
	protected UserCoreTableReader(String tableName) {
		this.tableName = tableName;
	}

	/**
	 * Create the table
	 * 
	 * @param tableName
	 * @param columnList
	 * @return table
	 */
	protected abstract TTable createTable(String tableName,
			List<TColumn> columnList);

	/**
	 * Create the column
	 * 
	 * @param result
	 * @param index
	 * @param name
	 * @param type
	 * @param max
	 * @param notNull
	 * @param defaultValueIndex
	 * @param primaryKey
	 * @return column
	 */
	protected abstract TColumn createColumn(TResult result, int index,
			String name, String type, Long max, boolean notNull,
			int defaultValueIndex, boolean primaryKey);

	/**
	 * Read the table
	 * 
	 * @param db
	 * @return table
	 */
	public TTable readTable(
			UserCoreConnection<TColumn, TTable, TRow, TResult> db) {

		List<TColumn> columnList = new ArrayList<TColumn>();

		TResult result = db.rawQuery(
				"PRAGMA table_info(" + CoreSQLUtils.quoteWrap(tableName) + ")",
				null);
		try {
			while (result.moveToNext()) {
				int index = result.getInt(result.getColumnIndex(CID));
				String name = result.getString(result.getColumnIndex(NAME));
				String type = result.getString(result.getColumnIndex(TYPE));
				boolean notNull = result
						.getInt(result.getColumnIndex(NOT_NULL)) == 1;
				boolean primaryKey = result.getInt(result.getColumnIndex(PK)) == 1;

				// If the type has a max limit on it, pull it off
				Long max = null;
				if (type != null && type.endsWith(")")) {
					int maxStart = type.indexOf("(");
					if (maxStart > -1) {
						String maxString = type.substring(maxStart + 1,
								type.length() - 1);
						if (!maxString.isEmpty()) {
							try {
								max = Long.valueOf(maxString);
								type = type.substring(0, maxStart);
							} catch (NumberFormatException e) {
								logger.log(Level.WARNING,
										"Failed to parse type max from type: "
												+ type, e);
							}
						}
					}
				}

				// Get the geometry or data type and default value
				int defaultValueIndex = result.getColumnIndex(DFLT_VALUE);

				TColumn column = createColumn(result, index, name, type, max,
						notNull, defaultValueIndex, primaryKey);
				columnList.add(column);
			}
		} finally {
			result.close();
		}
		if (columnList.isEmpty()) {
			throw new GeoPackageException("Table does not exist: " + tableName);
		}

		return createTable(tableName, columnList);
	}

}
