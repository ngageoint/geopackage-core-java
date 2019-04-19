package mil.nga.geopackage.db;

import java.util.List;
import java.util.Map;

import mil.nga.geopackage.user.UserColumn;
import mil.nga.geopackage.user.UserTable;

/**
 * Core SQL Utility methods
 *
 * @author osbornb
 * @since 1.2.1
 */
public class CoreSQLUtils {

	/**
	 * Wrap the name in double quotes
	 *
	 * @param name
	 *            name
	 * @return quoted name
	 */
	public static String quoteWrap(String name) {
		String quoteName = null;
		if (name != null) {
			if (name.startsWith("\"") && name.endsWith("\"")) {
				quoteName = name;
			} else {
				quoteName = "\"" + name + "\"";
			}
		}
		return quoteName;
	}

	/**
	 * Wrap the names in double quotes
	 *
	 * @param names
	 *            names
	 * @return quoted names
	 */
	public static String[] quoteWrap(String[] names) {
		String[] quoteNames = null;
		if (names != null) {
			quoteNames = new String[names.length];
			for (int i = 0; i < names.length; i++) {
				quoteNames[i] = quoteWrap(names[i]);
			}
		}
		return quoteNames;
	}

	/**
	 * Build the "columns as" query values for the provided columns and "columns
	 * as" values for use in select statements. The columns as size should equal
	 * the number of columns and only provide values at the indices for desired
	 * columns.
	 * 
	 * Example: columns = [column1, column2], columnsAs = [null, value] creates
	 * [column1, value as column2]
	 * 
	 * @param columns
	 *            columns array
	 * @param columnsAs
	 *            columns as values
	 * @return columns with as values
	 * @since 2.0.0
	 */
	public static String[] buildColumnsAs(String[] columns, String[] columnsAs) {
		String[] columnsWithAs = null;
		if (columnsAs != null) {
			columnsWithAs = new String[columns.length];
			for (int i = 0; i < columns.length; i++) {
				String column = columns[i];
				String columnsAsValue = columnsAs[i];
				String columnWithAs = null;
				if (columnsAsValue != null) {
					columnWithAs = columnsAsValue + " AS " + column;
				} else {
					columnWithAs = column;
				}
				columnsWithAs[i] = columnWithAs;
			}
		} else {
			columnsWithAs = columns;
		}
		return columnsWithAs;
	}

	/**
	 * Create the column SQL in the format:
	 * 
	 * "column_name" column_type[(max)] [NOT NULL] [PRIMARY KEY AUTOINCREMENT]
	 * 
	 * @param column
	 *            user column
	 * @return column SQL
	 * @since 3.2.1
	 */
	public static String columnSQL(UserColumn column) {
		return CoreSQLUtils.quoteWrap(column.getName()) + " "
				+ columnDefinition(column);
	}

	/**
	 * Create the column definition SQL in the format:
	 * 
	 * column_type[(max)] [NOT NULL] [PRIMARY KEY AUTOINCREMENT]
	 * 
	 * @param column
	 *            user column
	 * @return column definition SQL
	 * @since 3.2.1
	 */
	public static String columnDefinition(UserColumn column) {

		StringBuilder sql = new StringBuilder();

		sql.append(column.getTypeName());
		if (column.getMax() != null) {
			sql.append("(").append(column.getMax()).append(")");
		}
		if (column.isNotNull()) {
			sql.append(" NOT NULL");
		}
		if (column.isPrimaryKey()) {
			sql.append(" PRIMARY KEY AUTOINCREMENT");
		}

		return sql.toString();
	}

	/**
	 * Create SQL for adding a column
	 * 
	 * @param db
	 *            connection
	 * @param tableName
	 *            table name
	 * @param column
	 *            user column
	 * @since 3.2.1
	 */
	public static void addColumn(GeoPackageCoreConnection db, String tableName,
			UserColumn column) {
		AlterTable.addColumn(db, tableName, column.getName(),
				columnDefinition(column));
	}

	/**
	 * Query for the foreign keys value
	 * 
	 * @param db
	 *            connection
	 * @return true if enabled, false if disabled
	 * @since 3.2.1
	 */
	public static boolean foreignKeys(GeoPackageCoreConnection db) {
		Boolean foreignKeys = db.querySingleTypedResult("PRAGMA foreign_keys",
				null, GeoPackageDataType.BOOLEAN);
		return foreignKeys != null && foreignKeys;
	}

	/**
	 * Change the foreign keys state
	 * 
	 * @param db
	 *            connection
	 * @param on
	 *            true to turn on, false to turn off
	 * @return previous foreign keys value
	 * @since 3.2.1
	 */
	public static boolean foreignKeys(GeoPackageCoreConnection db, boolean on) {

		boolean foreignKeys = foreignKeys(db);

		if (foreignKeys != on) {
			String sql = foreignKeysSQL(on);
			db.execSQL(sql);
		}

		return foreignKeys;
	}

	/**
	 * Create the foreign keys SQL
	 * 
	 * @param on
	 *            true to turn on, false to turn off
	 * @return foreign keys SQL
	 * @since 3.2.1
	 */
	public static String foreignKeysSQL(boolean on) {
		return "PRAGMA foreign_keys = " + on;
	}

	/**
	 * Perform a foreign key check
	 * 
	 * @param db
	 *            connection
	 * @return empty list if valid or violation errors, 4 column values for each
	 *         violation. see SQLite PRAGMA foreign_key_check
	 * @since 3.2.1
	 */
	public static List<List<Object>> foreignKeyCheck(GeoPackageCoreConnection db) {
		String sql = foreignKeyCheckSQL();
		return db.queryResults(sql, null);
	}

	/**
	 * Perform a foreign key check
	 * 
	 * @param db
	 *            connection
	 * @param tableName
	 *            table name
	 * @return empty list if valid or violation errors, 4 column values for each
	 *         violation. see SQLite PRAGMA foreign_key_check
	 * @since 3.2.1
	 */
	public static List<List<Object>> foreignKeyCheck(
			GeoPackageCoreConnection db, String tableName) {
		String sql = foreignKeyCheckSQL(tableName);
		return db.queryResults(sql, null);
	}

	/**
	 * Create the foreign key check SQL
	 * 
	 * @return foreign key check SQL
	 * @since 3.2.1
	 */
	public static String foreignKeyCheckSQL() {
		return foreignKeyCheckSQL(null);
	}

	/**
	 * Create the foreign key check SQL
	 * 
	 * @param tableName
	 *            table name
	 * @return foreign key check SQL
	 * @since 3.2.1
	 */
	public static String foreignKeyCheckSQL(String tableName) {
		return "PRAGMA foreign_key_check"
				+ (tableName != null ? "(" + CoreSQLUtils.quoteWrap(tableName)
						+ ")" : "");
	}

	/**
	 * Drop the table if it exists
	 * 
	 * @param db
	 *            connection
	 * @param tableName
	 *            table name
	 * @since 3.2.1
	 */
	public static void dropTable(GeoPackageCoreConnection db, String tableName) {
		String sql = dropTableSQL(tableName);
		db.execSQL(sql);
	}

	/**
	 * Create the drop table if exists SQL
	 * 
	 * @param tableName
	 *            table name
	 * @return drop table SQL
	 * @since 3.2.1
	 */
	public static String dropTableSQL(String tableName) {
		return "DROP TABLE IF EXISTS " + CoreSQLUtils.quoteWrap(tableName);
	}

	/**
	 * Transfer table content from one table to another. Uses all columns in the
	 * "to table" and assumes the columns exist in the "from table".
	 * 
	 * @param db
	 *            connection
	 * @param fromTable
	 *            table name to copy from
	 * @param toTable
	 *            table to copy to
	 * @since 3.2.1
	 */
	public static void transferTableContent(GeoPackageCoreConnection db,
			String fromTable, UserTable<? extends UserColumn> toTable) {
		String sql = transferTableContentSQL(fromTable, toTable);
		db.execSQL(sql);
	}

	/**
	 * Transfer table content from one table to another
	 * 
	 * @param db
	 *            connection
	 * @param fromTable
	 *            table name to copy from
	 * @param toTable
	 *            table to copy to
	 * @param columnMapping
	 *            mapping between "to table" column names and "from table"
	 *            column names
	 * @since 3.2.1
	 */
	public static void transferTableContent(GeoPackageCoreConnection db,
			String fromTable, UserTable<? extends UserColumn> toTable,
			Map<String, String> columnMapping) {
		String sql = transferTableContentSQL(fromTable, toTable, columnMapping);
		db.execSQL(sql);
	}

	/**
	 * Create transfer table content from one table to another insert SQL. Uses
	 * all columns in the "to table" and assumes the columns exist in the
	 * "from table".
	 * 
	 * @param fromTable
	 *            table name to copy from
	 * @param toTable
	 *            table to copy to
	 * @return transfer SQL
	 * @since 3.2.1
	 */
	public static String transferTableContentSQL(String fromTable,
			UserTable<? extends UserColumn> toTable) {
		return transferTableContentSQL(fromTable, toTable, null);
	}

	/**
	 * Create transfer table content from one table to another insert SQL
	 * 
	 * @param fromTable
	 *            table name to copy from
	 * @param toTable
	 *            table to copy to
	 * @param columnMapping
	 *            mapping between "to table" column names and "from table"
	 *            column names
	 * @return transfer SQL
	 * @since 3.2.1
	 */
	public static String transferTableContentSQL(String fromTable,
			UserTable<? extends UserColumn> toTable,
			Map<String, String> columnMapping) {

		StringBuilder insert = new StringBuilder("INSERT INTO ");
		insert.append(CoreSQLUtils.quoteWrap(toTable.getTableName()));
		insert.append(" (");

		StringBuilder selectColumns = new StringBuilder();

		boolean firstColumn = true;
		for (UserColumn toColumn : toTable.getColumns()) {

			String toColumnName = toColumn.getName();
			String fromColumnName = toColumnName;

			if (columnMapping != null) {
				fromColumnName = columnMapping.get(toColumnName);
			}

			if (fromColumnName != null) {
				if (firstColumn) {
					firstColumn = false;
				} else {
					insert.append(", ");
					selectColumns.append(", ");
				}
				insert.append(CoreSQLUtils.quoteWrap(toColumnName));
				selectColumns.append(CoreSQLUtils.quoteWrap(fromColumnName));
			}

		}
		insert.append(") SELECT ");
		insert.append(selectColumns);
		insert.append(" FROM ");
		insert.append(CoreSQLUtils.quoteWrap(fromTable));

		return insert.toString();
	}

}
