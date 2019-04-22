package mil.nga.geopackage.db;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * SQLite Master table queries (sqlite_master)
 * 
 * @author osbornb
 * @since 3.2.1
 */
public class SQLiteMaster {

	/**
	 * SQLiteMaster query results
	 */
	private final List<List<Object>> results;

	/**
	 * Mapping between result columns and indices
	 */
	private final Map<SQLiteMasterColumn, Integer> columns = new HashMap<>();

	/**
	 * Query result count
	 */
	private final int count;

	/**
	 * Constructor
	 * 
	 * @param results
	 *            query results
	 * @param columns
	 *            query columns
	 */
	private SQLiteMaster(List<List<Object>> results,
			SQLiteMasterColumn[] columns) {

		if (columns != null) {

			this.results = results;
			this.count = results.size();

			for (int i = 0; i < columns.length; i++) {
				this.columns.put(columns[i], i);
			}

		} else {
			// Count only result
			this.results = new ArrayList<>();
			this.count = ((Number) results.get(0).get(0)).intValue();
		}

	}

	/**
	 * Result count
	 * 
	 * @return count
	 */
	public int count() {
		return count;
	}

	/**
	 * Get the columns in the result
	 * 
	 * @return columns
	 */
	public Set<SQLiteMasterColumn> columns() {
		return columns.keySet();
	}

	/**
	 * Get the type
	 * 
	 * @param row
	 *            row index
	 * @return type
	 */
	public SQLiteMasterType getType(int row) {
		return SQLiteMasterType.valueOf(getTypeString(row).toUpperCase());
	}

	/**
	 * Get the type string
	 * 
	 * @param row
	 *            row index
	 * @return type string
	 */
	public String getTypeString(int row) {
		return (String) getValue(row, SQLiteMasterColumn.TYPE);
	}

	/**
	 * Get the name
	 * 
	 * @param row
	 *            row index
	 * @return name
	 */
	public String getName(int row) {
		return (String) getValue(row, SQLiteMasterColumn.NAME);
	}

	/**
	 * Get the table name
	 * 
	 * @param row
	 *            row index
	 * @return name
	 */
	public String getTableName(int row) {
		return (String) getValue(row, SQLiteMasterColumn.TBL_NAME);
	}

	/**
	 * Get the rootpage
	 * 
	 * @param row
	 *            row index
	 * @return name
	 */
	public Long getRootpage(int row) {
		return (Long) getValue(row, SQLiteMasterColumn.ROOTPAGE);
	}

	/**
	 * Get the sql
	 * 
	 * @param row
	 *            row index
	 * @return name
	 */
	public String getSql(int row) {
		return (String) getValue(row, SQLiteMasterColumn.SQL);
	}

	/**
	 * Get the value of the column at the row index
	 * 
	 * @param row
	 *            row index
	 * @param column
	 *            column type
	 * @return value
	 */
	public Object getValue(int row, SQLiteMasterColumn column) {
		return getValue(getRow(row), column);
	}

	/**
	 * Get the row at the row index
	 * 
	 * @param row
	 *            row index
	 * @return row column values
	 */
	public List<Object> getRow(int row) {
		if (row < 0 || row >= results.size()) {
			String message;
			if (results.isEmpty()) {
				message = "Results are empty";
			} else {
				message = "Row index: " + row + ", not within range 0 to "
						+ (results.size() - 1);
			}
			throw new IndexOutOfBoundsException(message);
		}
		return results.get(row);
	}

	/**
	 * Get the value in the row at the column index
	 * 
	 * @param row
	 *            row
	 * @param column
	 *            column type
	 * @return value
	 */
	public Object getValue(List<Object> row, SQLiteMasterColumn column) {
		return row.get(getColumnIndex(column));
	}

	/**
	 * Get the column index of the column type
	 * 
	 * @param column
	 *            column type
	 * @return column index
	 */
	public int getColumnIndex(SQLiteMasterColumn column) {
		Integer index = columns.get(column);
		if (index == null) {
			throw new IndexOutOfBoundsException(
					"Column does not exist in row values: " + column);
		}
		return index;
	}

	/**
	 * Count the sqlite_master table
	 * 
	 * @param db
	 *            connection
	 * @return count
	 */
	public static int count(GeoPackageCoreConnection db) {
		return count(db, new SQLiteMasterType[] {}, null);
	}

	/**
	 * Query the sqlite_master table
	 * 
	 * @param db
	 *            connection
	 * @return SQLiteMaster result
	 */
	public static SQLiteMaster query(GeoPackageCoreConnection db) {
		return query(db, null);
	}

	/**
	 * Count the sqlite_master table
	 * 
	 * @param db
	 *            connection
	 * @param tableName
	 *            table name
	 * @return count
	 */
	public static int count(GeoPackageCoreConnection db, String tableName) {
		return count(db, new SQLiteMasterType[] {}, tableName);
	}

	/**
	 * Query the sqlite_master table
	 * 
	 * @param db
	 *            connection
	 * @param tableName
	 *            table name
	 * @return SQLiteMaster result
	 */
	public static SQLiteMaster query(GeoPackageCoreConnection db,
			String tableName) {
		return query(db, SQLiteMasterColumn.values(),
				new SQLiteMasterType[] {}, tableName);
	}

	/**
	 * Query the sqlite_master table
	 * 
	 * @param db
	 *            connection
	 * @param columns
	 *            result columns
	 * @return SQLiteMaster result
	 */
	public static SQLiteMaster queryForColumns(GeoPackageCoreConnection db,
			Collection<SQLiteMasterColumn> columns) {
		return queryForColumns(db, columns, null);
	}

	/**
	 * Query the sqlite_master table
	 * 
	 * @param db
	 *            connection
	 * @param columns
	 *            result columns
	 * @param tableName
	 *            table name
	 * @return SQLiteMaster result
	 */
	public static SQLiteMaster queryForColumns(GeoPackageCoreConnection db,
			Collection<SQLiteMasterColumn> columns, String tableName) {
		return query(db, columns.toArray(new SQLiteMasterColumn[0]),
				new SQLiteMasterType[] {}, tableName);
	}

	/**
	 * Count the sqlite_master table
	 * 
	 * @param db
	 *            connection
	 * @param type
	 *            result type
	 * @return count
	 */
	public static int countByType(GeoPackageCoreConnection db,
			SQLiteMasterType type) {
		return countByType(db, type, null);
	}

	/**
	 * Query the sqlite_master table
	 * 
	 * @param db
	 *            connection
	 * @param type
	 *            result type
	 * @return SQLiteMaster result
	 */
	public static SQLiteMaster queryByType(GeoPackageCoreConnection db,
			SQLiteMasterType type) {
		return queryByType(db, type, null);
	}

	/**
	 * Count the sqlite_master table
	 * 
	 * @param db
	 *            connection
	 * @param types
	 *            result types
	 * @return count
	 */
	public static int countByType(GeoPackageCoreConnection db,
			Collection<SQLiteMasterType> types) {
		return countByType(db, types, null);
	}

	/**
	 * Query the sqlite_master table
	 * 
	 * @param db
	 *            connection
	 * @param types
	 *            result types
	 * @return SQLiteMaster result
	 */
	public static SQLiteMaster queryByType(GeoPackageCoreConnection db,
			Collection<SQLiteMasterType> types) {
		return queryByType(db, types, null);
	}

	/**
	 * Count the sqlite_master table
	 * 
	 * @param db
	 *            connection
	 * @param type
	 *            result type
	 * @param tableName
	 *            table name
	 * @return count
	 */
	public static int countByType(GeoPackageCoreConnection db,
			SQLiteMasterType type, String tableName) {
		return count(db, type, tableName);
	}

	/**
	 * Query the sqlite_master table
	 * 
	 * @param db
	 *            connection
	 * @param type
	 *            result type
	 * @param tableName
	 *            table name
	 * @return SQLiteMaster result
	 */
	public static SQLiteMaster queryByType(GeoPackageCoreConnection db,
			SQLiteMasterType type, String tableName) {
		return query(db, SQLiteMasterColumn.values(), type, tableName);
	}

	/**
	 * Count the sqlite_master table
	 * 
	 * @param db
	 *            connection
	 * @param types
	 *            result types
	 * @param tableName
	 *            table name
	 * @return count
	 */
	public static int countByType(GeoPackageCoreConnection db,
			Collection<SQLiteMasterType> types, String tableName) {
		return count(db, types.toArray(new SQLiteMasterType[0]), tableName);
	}

	/**
	 * Query the sqlite_master table
	 * 
	 * @param db
	 *            connection
	 * @param types
	 *            result types
	 * @param tableName
	 *            table name
	 * @return SQLiteMaster result
	 */
	public static SQLiteMaster queryByType(GeoPackageCoreConnection db,
			Collection<SQLiteMasterType> types, String tableName) {
		return query(db, SQLiteMasterColumn.values(),
				types.toArray(new SQLiteMasterType[0]), tableName);
	}

	/**
	 * 
	 * Count the sqlite_master table
	 * 
	 * @param db
	 *            connection
	 * @param types
	 *            result types
	 * @return count
	 */
	public static int count(GeoPackageCoreConnection db,
			Collection<SQLiteMasterType> types) {
		return count(db, types, null);
	}

	/**
	 * 
	 * Query the sqlite_master table
	 * 
	 * @param db
	 *            connection
	 * @param columns
	 *            result columns
	 * @param type
	 *            result type
	 * @return SQLiteMaster result
	 */
	public static SQLiteMaster query(GeoPackageCoreConnection db,
			Collection<SQLiteMasterColumn> columns, SQLiteMasterType type) {
		return query(db, columns, type, null);
	}

	/**
	 * 
	 * Query the sqlite_master table
	 * 
	 * @param db
	 *            connection
	 * @param columns
	 *            result columns
	 * @param types
	 *            result types
	 * @return SQLiteMaster result
	 */
	public static SQLiteMaster query(GeoPackageCoreConnection db,
			Collection<SQLiteMasterColumn> columns,
			Collection<SQLiteMasterType> types) {
		return query(db, columns, types, null);
	}

	/**
	 * Query the sqlite_master table
	 * 
	 * @param db
	 *            connection
	 * @param columns
	 *            result columns
	 * @param type
	 *            result type
	 * @param tableName
	 *            table name
	 * @return SQLiteMaster result
	 */
	public static SQLiteMaster query(GeoPackageCoreConnection db,
			Collection<SQLiteMasterColumn> columns, SQLiteMasterType type,
			String tableName) {
		return query(db, columns.toArray(new SQLiteMasterColumn[0]), type,
				tableName);
	}

	/**
	 * Count the sqlite_master table
	 * 
	 * @param db
	 *            connection
	 * @param types
	 *            result types
	 * @param tableName
	 *            table name
	 * @return count
	 */
	public static int count(GeoPackageCoreConnection db,
			Collection<SQLiteMasterType> types, String tableName) {
		return count(db, types.toArray(new SQLiteMasterType[0]), tableName);
	}

	/**
	 * Query the sqlite_master table
	 * 
	 * @param db
	 *            connection
	 * @param columns
	 *            result columns
	 * @param types
	 *            result types
	 * @param tableName
	 *            table name
	 * @return SQLiteMaster result
	 */
	public static SQLiteMaster query(GeoPackageCoreConnection db,
			Collection<SQLiteMasterColumn> columns,
			Collection<SQLiteMasterType> types, String tableName) {
		return query(db, columns.toArray(new SQLiteMasterColumn[0]),
				types.toArray(new SQLiteMasterType[0]), tableName);
	}

	/**
	 * Count the sqlite_master table
	 * 
	 * @param db
	 *            connection
	 * @param type
	 *            result type
	 * @param tableName
	 *            table name
	 * @return count
	 */
	public static int count(GeoPackageCoreConnection db, SQLiteMasterType type,
			String tableName) {
		return count(db, new SQLiteMasterType[] { type }, tableName);
	}

	/**
	 * Query the sqlite_master table
	 * 
	 * @param db
	 *            connection
	 * @param columns
	 *            result columns
	 * @param type
	 *            result type
	 * @param tableName
	 *            table name
	 * @return SQLiteMaster result
	 */
	public static SQLiteMaster query(GeoPackageCoreConnection db,
			SQLiteMasterColumn[] columns, SQLiteMasterType type,
			String tableName) {
		return query(db, columns, new SQLiteMasterType[] { type }, tableName);
	}

	/**
	 * Count the sqlite_master table
	 * 
	 * @param db
	 *            connection
	 * @param types
	 *            result types
	 * @param tableName
	 *            table name
	 * @return count
	 */
	public static int count(GeoPackageCoreConnection db,
			SQLiteMasterType[] types, String tableName) {
		return query(db, null, types, tableName).count();
	}

	/**
	 * Query the sqlite_master table
	 * 
	 * @param db
	 *            connection
	 * @param columns
	 *            result columns
	 * @param types
	 *            result types
	 * @param tableName
	 *            table name
	 * @return SQLiteMaster result
	 */
	public static SQLiteMaster query(GeoPackageCoreConnection db,
			SQLiteMasterColumn[] columns, SQLiteMasterType[] types,
			String tableName) {

		StringBuilder sql = new StringBuilder("SELECT ");
		List<String> args = new ArrayList<>();

		if (columns != null && columns.length > 0) {

			for (int i = 0; i < columns.length; i++) {
				if (i > 0) {
					sql.append(", ");
				}
				sql.append(columns[i].name().toLowerCase());
			}

		} else {
			sql.append("count(*)");
		}

		sql.append(" FROM sqlite_master");

		if (tableName != null) {
			sql.append(" WHERE tbl_name = ?");
			args.add(tableName);
		}

		if (types != null && types.length > 0) {

			if (tableName != null) {
				sql.append(" AND");
			} else {
				sql.append(" WHERE");
			}

			sql.append(" type IN (");
			for (int i = 0; i < types.length; i++) {
				if (i > 0) {
					sql.append(", ");
				}
				sql.append("?");
				args.add(types[i].name().toLowerCase());
			}
			sql.append(")");
		}

		List<List<Object>> results = db.queryResults(sql.toString(),
				args.toArray(new String[0]));

		SQLiteMaster sqliteMaster = new SQLiteMaster(results, columns);

		return sqliteMaster;
	}

}
