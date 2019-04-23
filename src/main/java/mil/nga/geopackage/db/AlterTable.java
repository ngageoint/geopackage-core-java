package mil.nga.geopackage.db;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.db.master.SQLiteMaster;
import mil.nga.geopackage.db.master.SQLiteMasterColumn;
import mil.nga.geopackage.db.master.SQLiteMasterQuery;
import mil.nga.geopackage.db.master.SQLiteMasterType;
import mil.nga.geopackage.user.UserColumn;
import mil.nga.geopackage.user.UserTable;

/**
 * Builds and performs alter table statements
 * 
 * @author osbornb
 * @since 3.2.1
 */
public class AlterTable {

	/**
	 * Logger
	 */
	private static final Logger logger = Logger.getLogger(AlterTable.class
			.getName());

	/**
	 * Create the ALTER TABLE SQL command prefix
	 * 
	 * @param table
	 *            table name
	 * @return alter table SQL prefix
	 */
	public static String alterTable(String table) {
		return "ALTER TABLE " + CoreSQLUtils.quoteWrap(table);
	}

	/**
	 * Rename a table
	 * 
	 * @param db
	 *            connection
	 * @param tableName
	 *            table name
	 * @param newTableName
	 *            new table name
	 */
	public static void renameTable(GeoPackageCoreConnection db,
			String tableName, String newTableName) {
		String sql = renameTableSQL(tableName, newTableName);
		db.execSQL(sql);
	}

	/**
	 * Create the rename table SQL
	 * 
	 * @param tableName
	 *            table name
	 * @param newTableName
	 *            new table name
	 * @return rename table SQL
	 */
	public static String renameTableSQL(String tableName, String newTableName) {
		return alterTable(tableName) + " RENAME TO "
				+ CoreSQLUtils.quoteWrap(newTableName);
	}

	/**
	 * Rename a column
	 * 
	 * @param db
	 *            connection
	 * @param tableName
	 *            table name
	 * @param columnName
	 *            column name
	 * @param newColumnName
	 *            new column name
	 */
	public static void renameColumn(GeoPackageCoreConnection db,
			String tableName, String columnName, String newColumnName) {
		String sql = renameColumnSQL(tableName, columnName, newColumnName);
		db.execSQL(sql);
	}

	/**
	 * Create the rename column SQL
	 * 
	 * @param tableName
	 *            table name
	 * @param columnName
	 *            column name
	 * @param newColumnName
	 *            new column name
	 * @return rename table SQL
	 */
	public static String renameColumnSQL(String tableName, String columnName,
			String newColumnName) {
		return alterTable(tableName) + " RENAME COLUMN "
				+ CoreSQLUtils.quoteWrap(columnName) + " TO "
				+ CoreSQLUtils.quoteWrap(newColumnName);
	}

	/**
	 * Add a column
	 * 
	 * @param db
	 *            connection
	 * @param tableName
	 *            table name
	 * @param columnName
	 *            column name
	 * @param columnDef
	 *            column definition
	 */
	public static void addColumn(GeoPackageCoreConnection db, String tableName,
			String columnName, String columnDef) {
		String sql = addColumnSQL(tableName, columnName, columnDef);
		db.execSQL(sql);
	}

	/**
	 * Create the add column SQL
	 * 
	 * @param tableName
	 *            table name
	 * @param columnName
	 *            column name
	 * @param columnDef
	 *            column definition
	 * @return add column SQL
	 */
	public static String addColumnSQL(String tableName, String columnName,
			String columnDef) {
		return alterTable(tableName) + " ADD COLUMN "
				+ CoreSQLUtils.quoteWrap(columnName) + " " + columnDef;
	}

	/**
	 * Drop a column
	 * 
	 * @param db
	 *            connection
	 * @param table
	 *            table
	 * @param columnName
	 *            column name
	 */
	public static void dropColumn(GeoPackageCoreConnection db,
			UserTable<? extends UserColumn> table, String columnName) {

		UserTable<? extends UserColumn> newTable = table.copy();

		newTable.dropColumn(columnName);

		alterTable(db, table.getTableName(), newTable);

		table.dropColumn(columnName);
	}

	/**
	 * Alter a column
	 * 
	 * @param db
	 *            connection
	 * @param table
	 *            table
	 * @param column
	 *            column
	 * @param <T>
	 *            user column type
	 */
	public static <T extends UserColumn> void alterColumn(
			GeoPackageCoreConnection db, UserTable<T> table, T column) {

		UserTable<T> newTable = table.copy();

		newTable.alterColumn(column);

		alterTable(db, table.getTableName(), newTable);

		table.alterColumn(column);
	}

	/**
	 * Alter a table with a new table schema and column mapping. This creates a
	 * new table, migrates the data, drops the old table, and renames the new
	 * table to the old. Views on the table are attempted to be dropped and
	 * recreated. Indexes and triggers are attempted to be recreated.
	 * 
	 * Making Other Kinds Of Table Schema Changes:
	 * https://www.sqlite.org/lang_altertable.html
	 * 
	 * @param db
	 *            connection
	 * @param tableName
	 *            table name
	 * @param newTable
	 *            new table schema
	 */
	public static void alterTable(GeoPackageCoreConnection db,
			String tableName, UserTable<? extends UserColumn> newTable) {

		// Build the create table sql
		String sql = CoreSQLUtils.createTableSQL(newTable);

		// Build the column mapping
		Map<String, String> columnMapping = columnMapping(newTable);

		alterTable(db, tableName, sql, columnMapping);
	}

	/**
	 * Alter a table with a new table schema and column mapping. This creates a
	 * new table, migrates the data, drops the old table, and renames the new
	 * table to the old. Views on the table are attempted to be dropped and
	 * recreated. Indexes and triggers are attempted to be recreated.
	 * 
	 * Making Other Kinds Of Table Schema Changes:
	 * https://www.sqlite.org/lang_altertable.html
	 * 
	 * @param db
	 *            connection
	 * @param tableName
	 *            table name
	 * @param sql
	 *            new table SQL
	 * @param columnMapping
	 *            mapping between new table column names and existing table
	 *            column names. columns without values map to the same column
	 *            name.
	 */
	public static void alterTable(GeoPackageCoreConnection db,
			String tableName, String sql, Map<String, String> columnMapping) {

		// 1. Disable foreign key constraints
		boolean enableForeignKeys = CoreSQLUtils.foreignKeys(db, false);

		// 2. Start a transaction
		boolean successful = true;
		db.beginTransaction();
		try {

			// 9a. Query for views and remove them
			SQLiteMaster views = SQLiteMaster.query(db, SQLiteMaster.columns(
					SQLiteMasterColumn.NAME, SQLiteMasterColumn.SQL),
					SQLiteMasterType.VIEW, SQLiteMasterQuery
							.createTableViewQuery(tableName));
			for (int i = 0; i < views.count(); i++) {
				String viewName = views.getName(i);
				try {
					CoreSQLUtils.dropView(db, viewName);
				} catch (Exception e) {
					logger.log(Level.WARNING, "Failed to drop view: "
							+ viewName + ", table: " + tableName, e);
				}
			}

			// 3. Query indexes and triggers
			SQLiteMaster indexesAndTriggers = SQLiteMaster.query(db,
					SQLiteMaster.columns(SQLiteMasterColumn.TYPE,
							SQLiteMasterColumn.SQL), SQLiteMaster.types(
							SQLiteMasterType.INDEX, SQLiteMasterType.TRIGGER),
					tableName);

			// 4. Create the new table
			String tempTableName = CoreSQLUtils.tempTableName(db, "new",
					tableName);
			sql = sql.replaceFirst(tableName, tempTableName);
			db.execSQL(sql);

			// 5. Transfer content to new table
			CoreSQLUtils.transferTableContent(db, tableName, tempTableName,
					columnMapping);

			// 6. Drop the old table
			CoreSQLUtils.dropTable(db, tableName);

			// 7. Rename the new table
			renameTable(db, tempTableName, tableName);

			// 8. Create the indexes and triggers (those not affected by the
			// schema change)
			for (int i = 0; i < indexesAndTriggers.count(); i++) {
				String tableSql = indexesAndTriggers.getSql(i);
				try {
					db.execSQL(tableSql);
				} catch (Exception e) {
					logger.log(Level.WARNING, "Failed to recreate "
							+ indexesAndTriggers.getType(i)
							+ " after table alteration. table: " + tableName
							+ ", sql: " + tableSql, e);
				}
			}

			// 9b. Recreate views
			for (int i = 0; i < views.count(); i++) {
				String viewSql = views.getSql(i);
				try {
					db.execSQL(viewSql);
				} catch (Exception e) {
					logger.log(Level.WARNING, "Failed to recreate view: "
							+ views.getName(i) + ", table: " + tableName
							+ ", sql: " + viewSql, e);
				}
			}

			// 10. Foreign key check
			if (enableForeignKeys) {
				foreignKeyCheck(db);
			}

		} catch (Throwable e) {
			successful = false;
			throw e;
		} finally {
			// 11. Commit the transaction
			db.endTransaction(successful);
		}

		// 12. Re-enable foreign key constraints
		if (enableForeignKeys) {
			CoreSQLUtils.foreignKeys(db, true);
		}

	}

	/**
	 * Get a column mapping from the table
	 * 
	 * @param table
	 *            user table
	 * @return column mapping with only key values
	 */
	public static Map<String, String> columnMapping(
			UserTable<? extends UserColumn> table) {
		Map<String, String> columnMapping = new LinkedHashMap<>();
		for (UserColumn column : table.getColumns()) {
			columnMapping.put(column.getName(), null);
		}
		return columnMapping;
	}

	/**
	 * Perform a foreign key check for violations
	 * 
	 * @param db
	 *            connection
	 */
	private static void foreignKeyCheck(GeoPackageCoreConnection db) {

		List<List<Object>> violations = CoreSQLUtils.foreignKeyCheck(db);

		if (!violations.isEmpty()) {
			StringBuilder violationsMessage = new StringBuilder();
			for (int i = 0; i < violations.size(); i++) {
				if (i > 0) {
					violationsMessage.append("; ");
				}
				violationsMessage.append(i + 1).append(": ");
				List<Object> violation = violations.get(i);
				for (int j = 0; j < violation.size(); j++) {
					if (j > 0) {
						violationsMessage.append(", ");
					}
					violationsMessage.append(violation.get(j));
				}
			}
			throw new GeoPackageException("Foreign Key Check Violations: "
					+ violationsMessage);
		}

	}

}
