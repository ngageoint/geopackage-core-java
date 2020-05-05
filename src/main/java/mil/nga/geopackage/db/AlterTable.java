package mil.nga.geopackage.db;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.db.master.SQLiteMaster;
import mil.nga.geopackage.db.master.SQLiteMasterColumn;
import mil.nga.geopackage.db.master.SQLiteMasterType;
import mil.nga.geopackage.db.table.Constraint;
import mil.nga.geopackage.db.table.RawConstraint;
import mil.nga.geopackage.extension.rtree.RTreeIndexCoreExtension;
import mil.nga.geopackage.user.UserColumn;
import mil.nga.geopackage.user.UserTable;
import mil.nga.geopackage.user.custom.UserCustomColumn;
import mil.nga.geopackage.user.custom.UserCustomTable;
import mil.nga.geopackage.user.custom.UserCustomTableReader;

/**
 * Builds and performs alter table statements
 * 
 * @author osbornb
 * @since 3.3.0
 */
public class AlterTable {

	/**
	 * Logger
	 */
	private static final Logger logger = Logger
			.getLogger(AlterTable.class.getName());

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
		dropColumns(db, table, Arrays.asList(columnName));
	}

	/**
	 * Drop columns
	 * 
	 * @param db
	 *            connection
	 * @param table
	 *            table
	 * @param columnNames
	 *            column names
	 */
	public static void dropColumns(GeoPackageCoreConnection db,
			UserTable<? extends UserColumn> table,
			Collection<String> columnNames) {

		UserTable<? extends UserColumn> newTable = table.copy();

		for (String columnName : columnNames) {
			newTable.dropColumn(columnName);
		}

		// Build the table mapping
		TableMapping tableMapping = new TableMapping(newTable, columnNames);

		alterTable(db, newTable, tableMapping);

		for (String columnName : columnNames) {
			table.dropColumn(columnName);
		}
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
	public static void dropColumn(GeoPackageCoreConnection db, String tableName,
			String columnName) {
		dropColumns(db, tableName, Arrays.asList(columnName));
	}

	/**
	 * Drop columns
	 * 
	 * @param db
	 *            connection
	 * @param tableName
	 *            table name
	 * @param columnNames
	 *            column names
	 */
	public static void dropColumns(GeoPackageCoreConnection db,
			String tableName, Collection<String> columnNames) {
		UserCustomTable userTable = UserCustomTableReader.readTable(db,
				tableName);
		dropColumns(db, userTable, columnNames);
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
		alterColumns(db, table, Arrays.asList(column));
	}

	/**
	 * Alter columns
	 * 
	 * @param db
	 *            connection
	 * @param table
	 *            table
	 * @param columns
	 *            columns
	 * @param <T>
	 *            user column type
	 */
	public static <T extends UserColumn> void alterColumns(
			GeoPackageCoreConnection db, UserTable<T> table,
			Collection<T> columns) {

		UserTable<T> newTable = table.copy();

		for (T column : columns) {
			newTable.alterColumn(column);
		}

		alterTable(db, newTable);

		for (T column : columns) {
			table.alterColumn(column);
		}
	}

	/**
	 * Alter a column
	 * 
	 * @param db
	 *            connection
	 * @param tableName
	 *            table name
	 * @param column
	 *            column
	 * @param <T>
	 *            user column type
	 */
	public static <T extends UserColumn> void alterColumn(
			GeoPackageCoreConnection db, String tableName,
			UserCustomColumn column) {
		alterColumns(db, tableName, Arrays.asList(column));
	}

	/**
	 * Alter columns
	 * 
	 * @param db
	 *            connection
	 * @param tableName
	 *            table name
	 * @param columns
	 *            columns
	 * @param <T>
	 *            user column type
	 */
	public static <T extends UserColumn> void alterColumns(
			GeoPackageCoreConnection db, String tableName,
			Collection<UserCustomColumn> columns) {
		UserCustomTable userTable = UserCustomTableReader.readTable(db,
				tableName);
		alterColumns(db, userTable, columns);
	}

	/**
	 * Copy the table and row content
	 * 
	 * @param db
	 *            connection
	 * @param table
	 *            table
	 * @param newTableName
	 *            new table name
	 */
	public static void copyTable(GeoPackageCoreConnection db,
			UserTable<? extends UserColumn> table, String newTableName) {
		copyTable(db, table, newTableName, true);
	}

	/**
	 * Copy the table
	 * 
	 * @param db
	 *            connection
	 * @param table
	 *            table
	 * @param newTableName
	 *            new table name
	 * @param transferContent
	 *            transfer row content to the new table
	 */
	public static void copyTable(GeoPackageCoreConnection db,
			UserTable<? extends UserColumn> table, String newTableName,
			boolean transferContent) {

		// Build the table mapping
		TableMapping tableMapping = new TableMapping(table, newTableName);
		tableMapping.setTransferContent(transferContent);

		alterTable(db, table, tableMapping);
	}

	/**
	 * Copy the table and row content
	 * 
	 * @param db
	 *            connection
	 * @param tableName
	 *            table name
	 * @param newTableName
	 *            new table name
	 */
	public static void copyTable(GeoPackageCoreConnection db, String tableName,
			String newTableName) {
		copyTable(db, tableName, newTableName, true);
	}

	/**
	 * Copy the table
	 * 
	 * @param db
	 *            connection
	 * @param tableName
	 *            table name
	 * @param newTableName
	 *            new table name
	 * @param transferContent
	 *            transfer row content to the new table
	 */
	public static void copyTable(GeoPackageCoreConnection db, String tableName,
			String newTableName, boolean transferContent) {
		UserCustomTable userTable = UserCustomTableReader.readTable(db,
				tableName);
		copyTable(db, userTable, newTableName, transferContent);
	}

	/**
	 * Alter a table with a new table schema assuming a default table mapping.
	 * 
	 * This removes views on the table, creates a new table, transfers the old
	 * table data to the new, drops the old table, and renames the new table to
	 * the old. Indexes, triggers, and views that reference deleted columns are
	 * not recreated. An attempt is made to recreate the others including any
	 * modifications for renamed columns.
	 * 
	 * Making Other Kinds Of Table Schema Changes:
	 * https://www.sqlite.org/lang_altertable.html
	 * 
	 * @param db
	 *            connection
	 * @param newTable
	 *            new table schema
	 */
	public static void alterTable(GeoPackageCoreConnection db,
			UserTable<? extends UserColumn> newTable) {

		// Build the table mapping
		TableMapping tableMapping = new TableMapping(newTable);

		alterTable(db, newTable, tableMapping);
	}

	/**
	 * Alter a table with a new table schema and table mapping.
	 * 
	 * Altering a table: Removes views on the table, creates a new table,
	 * transfers the old table data to the new, drops the old table, and renames
	 * the new table to the old. Indexes, triggers, and views that reference
	 * deleted columns are not recreated. An attempt is made to recreate the
	 * others including any modifications for renamed columns.
	 * 
	 * Creating a new table: Creates a new table and transfers the table data to
	 * the new. Triggers are not created on the new table. Indexes and views
	 * that reference deleted columns are not recreated. An attempt is made to
	 * create the others on the new table.
	 * 
	 * Making Other Kinds Of Table Schema Changes:
	 * https://www.sqlite.org/lang_altertable.html
	 * 
	 * @param db
	 *            connection
	 * @param newTable
	 *            new table schema
	 * @param tableMapping
	 *            table mapping
	 */
	public static void alterTable(GeoPackageCoreConnection db,
			UserTable<? extends UserColumn> newTable,
			TableMapping tableMapping) {

		// Update column constraints
		for (UserColumn column : newTable.getColumns()) {
			List<Constraint> columnConstraints = column.clearConstraints();
			for (Constraint columnConstraint : columnConstraints) {
				String updatedSql = CoreSQLUtils.modifySQL(
						columnConstraint.getName(), columnConstraint.buildSql(),
						tableMapping);
				if (updatedSql != null) {
					column.addConstraint(new RawConstraint(
							columnConstraint.getType(), updatedSql));
				}
			}
		}

		// Update table constraints
		List<Constraint> tableContraints = newTable.clearConstraints();
		for (Constraint tableConstraint : tableContraints) {
			String updatedSql = CoreSQLUtils.modifySQL(
					tableConstraint.getName(), tableConstraint.buildSql(),
					tableMapping);
			if (updatedSql != null) {
				newTable.addConstraint(new RawConstraint(
						tableConstraint.getType(), updatedSql));
			}
		}

		// Build the create table sql
		String sql = CoreSQLUtils.createTableSQL(newTable);

		alterTable(db, sql, tableMapping);
	}

	/**
	 * Alter a table with a new table SQL creation statement and table mapping.
	 * 
	 * Altering a table: Removes views on the table, creates a new table,
	 * transfers the old table data to the new, drops the old table, and renames
	 * the new table to the old. Indexes, triggers, and views that reference
	 * deleted columns are not recreated. An attempt is made to recreate the
	 * others including any modifications for renamed columns.
	 * 
	 * Creating a new table: Creates a new table and transfers the table data to
	 * the new. Triggers are not created on the new table. Indexes and views
	 * that reference deleted columns are not recreated. An attempt is made to
	 * create the others on the new table.
	 * 
	 * Making Other Kinds Of Table Schema Changes:
	 * https://www.sqlite.org/lang_altertable.html
	 * 
	 * @param db
	 *            connection
	 * @param sql
	 *            new table SQL
	 * @param tableMapping
	 *            table mapping
	 */
	public static void alterTable(GeoPackageCoreConnection db, String sql,
			TableMapping tableMapping) {

		String tableName = tableMapping.getFromTable();

		// Determine if a new table copy vs an alter table
		boolean newTable = tableMapping.isNewTable();

		// 1. Disable foreign key constraints
		boolean enableForeignKeys = CoreSQLUtils.foreignKeys(db, false);

		// 2. Start a transaction
		boolean successful = true;
		db.beginTransaction();
		try {

			// 9a. Query for views
			SQLiteMaster views = SQLiteMaster.queryViewsOnTable(db, SQLiteMaster
					.columns(SQLiteMasterColumn.NAME, SQLiteMasterColumn.SQL),
					tableName);
			// Remove the views if not a new table
			if (!newTable) {
				for (int i = 0; i < views.count(); i++) {
					String viewName = views.getName(i);
					try {
						CoreSQLUtils.dropView(db, viewName);
					} catch (Exception e) {
						logger.log(
								Level.WARNING, "Failed to drop view: "
										+ viewName + ", table: " + tableName,
								e);
					}
				}
			}

			// 3. Query indexes and triggers
			SQLiteMaster indexesAndTriggers = SQLiteMaster.query(db,
					SQLiteMaster.columns(SQLiteMasterColumn.NAME,
							SQLiteMasterColumn.TYPE, SQLiteMasterColumn.SQL),
					SQLiteMaster.types(SQLiteMasterType.INDEX,
							SQLiteMasterType.TRIGGER),
					tableName);

			// Get the temporary or new table name
			String transferTable;
			if (newTable) {
				transferTable = tableMapping.getToTable();
			} else {
				transferTable = CoreSQLUtils.tempTableName(db, "new",
						tableName);
				tableMapping.setToTable(transferTable);
			}

			// 4. Create the new table
			sql = sql.replaceFirst(tableName, transferTable);
			db.execSQL(sql);

			// If transferring content
			if (tableMapping.isTransferContent()) {

				// 5. Transfer content to new table
				CoreSQLUtils.transferTableContent(db, tableMapping);

			}

			// If altering a table
			if (!newTable) {

				// 6. Drop the old table
				CoreSQLUtils.dropTable(db, tableName);

				// 7. Rename the new table
				renameTable(db, transferTable, tableName);

				tableMapping.setToTable(tableName);
			}

			// 8. Create the indexes and triggers
			for (int i = 0; i < indexesAndTriggers.count(); i++) {
				boolean create = !newTable;
				if (!create) {
					// Don't create rtree triggers for new tables
					create = indexesAndTriggers
							.getType(i) != SQLiteMasterType.TRIGGER
							|| !indexesAndTriggers.getName(i).startsWith(
									RTreeIndexCoreExtension.RTREE_PREFIX);
				}
				if (create) {
					String tableSql = indexesAndTriggers.getSql(i);
					if (tableSql != null) {
						tableSql = CoreSQLUtils.modifySQL(db,
								indexesAndTriggers.getName(i), tableSql,
								tableMapping);
						if (tableSql != null) {
							try {
								db.execSQL(tableSql);
							} catch (Exception e) {
								logger.log(Level.WARNING, "Failed to recreate "
										+ indexesAndTriggers.getType(i)
										+ " after table alteration. table: "
										+ tableMapping.getToTable() + ", sql: "
										+ tableSql, e);
							}
						}
					}
				}
			}

			// 9b. Recreate views
			for (int i = 0; i < views.count(); i++) {
				String viewSql = views.getSql(i);
				if (viewSql != null) {
					viewSql = CoreSQLUtils.modifySQL(db, views.getName(i),
							viewSql, tableMapping);
					if (viewSql != null) {
						try {
							db.execSQL(viewSql);
						} catch (Exception e) {
							logger.log(Level.WARNING,
									"Failed to recreate view: "
											+ views.getName(i) + ", table: "
											+ tableMapping.getToTable()
											+ ", sql: " + viewSql,
									e);
						}
					}
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
			throw new GeoPackageException(
					"Foreign Key Check Violations: " + violationsMessage);
		}

	}

}
