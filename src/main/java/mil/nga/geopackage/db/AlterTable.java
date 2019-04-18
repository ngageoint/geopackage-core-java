package mil.nga.geopackage.db;

import java.util.List;

import mil.nga.geopackage.GeoPackageException;

/**
 * Builds and performs alter table statements
 * 
 * @author osbornb
 * @since 3.2.1
 */
public class AlterTable {

	// /**
	// * Database connection
	// */
	// private final GeoPackageCoreConnection db;
	//
	// /**
	// * Constructor
	// *
	// * @param db
	// * database connection
	// */
	// public AlterTable(GeoPackageCoreConnection db) {
	// this.db = db;
	// }

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
	 * @param tableName
	 *            table name
	 * @param columnName
	 *            column name
	 */
	public static void dropColumn(GeoPackageCoreConnection db,
			String tableName, String columnName) {

		// TODO delete this
		CoreSQLUtils.foreignKeys(db, true);

		// Making Other Kinds Of Table Schema Changes:
		// https://www.sqlite.org/lang_altertable.html

		// 1. Disable foreign key constraints
		boolean enableForeignKeys = CoreSQLUtils.foreignKeys(db, false);

		boolean successful = true;

		// 2. Start a transaction
		db.beginTransaction();
		try {

			// 3. Query indexes and triggers
			List<List<String>> indexesAndTriggers = db
					.queryTypedResults(
							"SELECT type, sql FROM sqlite_master WHERE tbl_name = ? AND type IN (?, ?)",
							new String[] { tableName, "index", "trigger" });

			// 4. Create the new table
			// TODO Create table new_<tableName>

			// 5. Transfer content to new table
			// TODO Copy from tableName to new_<tableName>: INSERT INTO new_X
			// SELECT ... FROM X

			// 6. Drop the old table
			// TODO Drop table <tableName>

			// 7. Rename the new table
			// TODO Rename new_<tableName> to <tableName>

			// 8. Create the indexes and triggers
			// TODO edit these in some cases
			for (List<String> indexOrTrigger : indexesAndTriggers) {
				db.execSQL(indexOrTrigger.get(1));
			}

			// 9. Drop and create views
			// TODO drop and create views?

			// 10. Foreign key check
			if (enableForeignKeys) {
				List<List<Object>> violations = CoreSQLUtils
						.foreignKeyCheck(db);
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
					throw new GeoPackageException(
							"Foreign Key Check Violations: "
									+ violationsMessage);
				}
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

		// TODO
		throw new UnsupportedOperationException("Drop column not yet supported");
	}

}
