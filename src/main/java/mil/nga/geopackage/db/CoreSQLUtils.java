package mil.nga.geopackage.db;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import mil.nga.geopackage.user.UserColumn;
import mil.nga.geopackage.user.UserTable;
import mil.nga.geopackage.user.UserUniqueConstraint;

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
	 * Create the user defined table SQL
	 * 
	 * @param table
	 *            user table
	 * @param <TColumn>
	 *            column type
	 * @return create table SQL
	 * @since 3.2.1
	 */
	public static <TColumn extends UserColumn> String createTableSQL(
			UserTable<TColumn> table) {

		// Build the create table sql
		StringBuilder sql = new StringBuilder();
		sql.append("CREATE TABLE ")
				.append(CoreSQLUtils.quoteWrap(table.getTableName()))
				.append(" (");

		// Add each column to the sql
		List<? extends UserColumn> columns = table.getColumns();
		for (int i = 0; i < columns.size(); i++) {
			UserColumn column = columns.get(i);
			if (i > 0) {
				sql.append(",");
			}
			sql.append("\n  ");
			sql.append(CoreSQLUtils.columnSQL(column));
		}

		// Add unique constraints
		List<UserUniqueConstraint<TColumn>> uniqueConstraints = table
				.getUniqueConstraints();
		for (int i = 0; i < uniqueConstraints.size(); i++) {
			UserUniqueConstraint<TColumn> uniqueConstraint = uniqueConstraints
					.get(i);
			sql.append(",\n  UNIQUE (");
			List<TColumn> uniqueColumns = uniqueConstraint.getColumns();
			for (int j = 0; j < uniqueColumns.size(); j++) {
				TColumn uniqueColumn = uniqueColumns.get(j);
				if (j > 0) {
					sql.append(", ");
				}
				sql.append(uniqueColumn.getName());
			}
			sql.append(")");
		}

		sql.append("\n);");

		return sql.toString();
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

		Long max = column.getMax();
		if (max != null) {
			sql.append("(").append(max).append(")");
		}

		if (column.isNotNull()) {
			sql.append(" NOT NULL");
		}

		Object defaultValue = column.getDefaultValue();
		if (defaultValue != null) {
			sql.append(" DEFAULT ");
			String value = null;
			switch (column.getDataType()) {
			case BOOLEAN:
				Boolean booleanValue = null;
				if (defaultValue instanceof Boolean) {
					booleanValue = (Boolean) defaultValue;
				} else if (defaultValue instanceof String) {
					String stringBooleanValue = (String) defaultValue;
					switch (stringBooleanValue) {
					case "0":
						booleanValue = false;
						break;
					case "1":
						booleanValue = true;
						break;
					default:
						booleanValue = Boolean.valueOf(stringBooleanValue);
					}
				}
				if (booleanValue != null) {
					if (booleanValue) {
						value = "1";
					} else {
						value = "0";
					}
				}
				break;
			case TEXT:
				value = "'" + defaultValue.toString() + "'";
				break;
			default:
			}
			if (value == null) {
				value = defaultValue.toString();
			}
			sql.append(value);
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
	 * Drop the view if it exists
	 * 
	 * @param db
	 *            connection
	 * @param viewName
	 *            view name
	 * @since 3.2.1
	 */
	public static void dropView(GeoPackageCoreConnection db, String viewName) {
		String sql = dropViewSQL(viewName);
		db.execSQL(sql);
	}

	/**
	 * Create the drop view if exists SQL
	 * 
	 * @param viewName
	 *            view name
	 * @return drop view SQL
	 * @since 3.2.1
	 */
	public static String dropViewSQL(String viewName) {
		return "DROP VIEW IF EXISTS " + CoreSQLUtils.quoteWrap(viewName);
	}

	/**
	 * Transfer table content from one table to another
	 * 
	 * @param db
	 *            connection
	 * @param fromTable
	 *            table name to copy from
	 * @param toTable
	 *            table name to copy to
	 * @param columnMapping
	 *            mapping between "to table" column names and "from table"
	 *            column names. columns without values map to the same column
	 *            name.
	 * @since 3.2.1
	 */
	public static void transferTableContent(GeoPackageCoreConnection db,
			String fromTable, String toTable, Map<String, String> columnMapping) {
		String sql = transferTableContentSQL(fromTable, toTable, columnMapping);
		db.execSQL(sql);
	}

	/**
	 * Create insert SQL to transfer table content from one table to another
	 * 
	 * @param fromTable
	 *            table name to copy from
	 * @param toTable
	 *            table name to copy to
	 * @param columnMapping
	 *            mapping between "to table" column names and "from table"
	 *            column names. columns without values map to the same column
	 *            name.
	 * @return transfer SQL
	 * @since 3.2.1
	 */
	public static String transferTableContentSQL(String fromTable,
			String toTable, Map<String, String> columnMapping) {

		StringBuilder insert = new StringBuilder("INSERT INTO ");
		insert.append(CoreSQLUtils.quoteWrap(toTable));
		insert.append(" (");

		StringBuilder selectColumns = new StringBuilder();

		for (Entry<String, String> columns : columnMapping.entrySet()) {

			String toColumn = columns.getKey();

			String fromColumn = columns.getValue();
			if (fromColumn == null) {
				fromColumn = toColumn;
			}

			if (selectColumns.length() > 0) {
				insert.append(", ");
				selectColumns.append(", ");
			}
			insert.append(CoreSQLUtils.quoteWrap(toColumn));
			selectColumns.append(CoreSQLUtils.quoteWrap(fromColumn));

		}
		insert.append(") SELECT ");
		insert.append(selectColumns);
		insert.append(" FROM ");
		insert.append(CoreSQLUtils.quoteWrap(fromTable));

		return insert.toString();
	}

	/**
	 * Get an available temporary table name. Starts with prefix_baseName and
	 * then continues with prefix#_baseName starting at 1 and increasing.
	 * 
	 * @param db
	 *            connection
	 * @param prefix
	 *            name prefix
	 * @param baseName
	 *            base name
	 * @return unused table name
	 * @since 3.2.1
	 */
	public static String tempTableName(GeoPackageCoreConnection db,
			String prefix, String baseName) {
		String name = prefix + "_" + baseName;
		int nameNumber = 0;
		while (db.tableExists(name)) {
			name = prefix + (++nameNumber) + "_" + baseName;
		}
		return name;
	}

}
