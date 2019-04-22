package mil.nga.geopackage.db;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import mil.nga.geopackage.GeoPackageException;
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

		alterTable(db, table, newTable);

		table.dropColumn(columnName);
	}

	/**
	 * Alter a table with a new table schema. Assumes all columns in the new
	 * table exist with the same name in the existing table.
	 * 
	 * Making Other Kinds Of Table Schema Changes:
	 * https://www.sqlite.org/lang_altertable.html
	 * 
	 * @param db
	 *            connection
	 * @param table
	 *            table
	 * @param newTable
	 *            new table
	 */
	public static void alterTable(GeoPackageCoreConnection db,
			UserTable<? extends UserColumn> table,
			UserTable<? extends UserColumn> newTable) {
		alterTable(db, table, newTable, null);
	}

	/**
	 * Alter a table with a new table schema and column mapping. This creates a
	 * new table, migrates the data, drops the old table, and renames the new
	 * table to the old. Views on the table should be handled before and after
	 * calling this method. Indexes and triggers are attempted to be re-created
	 * (for those not affected by the schema change).
	 * 
	 * Making Other Kinds Of Table Schema Changes:
	 * https://www.sqlite.org/lang_altertable.html
	 * 
	 * @param db
	 *            connection
	 * @param table
	 *            table
	 * @param newTable
	 *            new table
	 * @param columnMapping
	 *            mapping between new table column names and existing table
	 *            column names
	 */
	public static void alterTable(GeoPackageCoreConnection db,
			UserTable<? extends UserColumn> table,
			UserTable<? extends UserColumn> newTable,
			Map<String, String> columnMapping) {

		// 1. Disable foreign key constraints
		boolean enableForeignKeys = CoreSQLUtils.foreignKeys(db, false);

		// 2. Start a transaction
		boolean successful = true;
		db.beginTransaction();
		try {

			// 3. Query indexes and triggers
			SQLiteMaster sqliteMaster = SQLiteMaster.query(db,
					new SQLiteMasterColumn[] { SQLiteMasterColumn.TYPE,
							SQLiteMasterColumn.SQL }, new SQLiteMasterType[] {
							SQLiteMasterType.INDEX, SQLiteMasterType.TRIGGER },
					table.getTableName());

			// 4. Create the new table
			newTable.setTableName("new_" + table.getTableName());
			GeoPackageTableCreator tableCreator = new GeoPackageTableCreator(db);
			tableCreator.createTable(newTable);

			// 5. Transfer content to new table
			CoreSQLUtils.transferTableContent(db, table.getTableName(),
					newTable, columnMapping);

			// 6. Drop the old table
			CoreSQLUtils.dropTable(db, table.getTableName());

			// 7. Rename the new table
			renameTable(db, newTable.getTableName(), table.getTableName());

			// 8. Create the indexes and triggers (those not affected by the
			// schema change)
			for (int i = 0; i < sqliteMaster.count(); i++) {
				String sql = sqliteMaster.getSql(i);
				try {
					db.execSQL(sql);
				} catch (Exception e) {
					logger.log(Level.WARNING, "Failed to recreate "
							+ sqliteMaster.getType(i)
							+ " after table alteration. sql: " + sql, e);
				}
			}

			// 9. Drop and create views
			// Views need to be handled before and after altering the table

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
