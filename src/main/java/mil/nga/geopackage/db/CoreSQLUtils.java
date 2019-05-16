package mil.nga.geopackage.db;

import java.util.List;
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
	public static String[] buildColumnsAs(String[] columns,
			String[] columnsAs) {
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

		sql.append(column.getType());

		if (column.hasMax()) {
			sql.append("(").append(column.getMax()).append(")");
		}

		if (column.isNotNull()) {
			sql.append(" NOT NULL");
		}

		if (column.hasDefaultValue()) {
			sql.append(" DEFAULT ");
			sql.append(columnDefaultValue(column));
		}

		if (column.isPrimaryKey()) {
			sql.append(" PRIMARY KEY AUTOINCREMENT");
		}

		return sql.toString();
	}

	/**
	 * Get the column default value as a string
	 * 
	 * @param column
	 *            user column
	 * @return default value
	 * @since 3.2.1
	 */
	public static String columnDefaultValue(UserColumn column) {
		return columnDefaultValue(column.getDefaultValue(),
				column.getDataType());
	}

	/**
	 * Get the column default value as a string
	 * 
	 * @param defaultValue
	 *            default value
	 * @param dataType
	 *            data type
	 * @return default value
	 * @since 3.2.1
	 */
	public static String columnDefaultValue(Object defaultValue,
			GeoPackageDataType dataType) {

		String value = null;

		if (defaultValue != null) {

			if (dataType != null) {

				switch (dataType) {
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
					value = defaultValue.toString();
					if (!value.startsWith("'") || !value.endsWith("'")) {
						value = "'" + value + "'";
					}
					break;
				default:
				}

			}

			if (value == null) {
				value = defaultValue.toString();
			}
		}

		return value;
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
	public static List<List<Object>> foreignKeyCheck(
			GeoPackageCoreConnection db) {
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
		return "PRAGMA foreign_key_check" + (tableName != null
				? "(" + CoreSQLUtils.quoteWrap(tableName) + ")"
				: "");
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
	public static void dropTable(GeoPackageCoreConnection db,
			String tableName) {
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
	 * @param tableMapping
	 *            table mapping
	 * @since 3.2.1
	 */
	public static void transferTableContent(GeoPackageCoreConnection db,
			TableMapping tableMapping) {
		String sql = transferTableContentSQL(tableMapping);
		db.execSQL(sql);
	}

	/**
	 * Create insert SQL to transfer table content from one table to another
	 * 
	 * @param tableMapping
	 *            table mapping
	 * @return transfer SQL
	 * @since 3.2.1
	 */
	public static String transferTableContentSQL(TableMapping tableMapping) {

		StringBuilder insert = new StringBuilder("INSERT INTO ");
		insert.append(CoreSQLUtils.quoteWrap(tableMapping.getToTable()));
		insert.append(" (");

		StringBuilder selectColumns = new StringBuilder();

		StringBuilder where = new StringBuilder();
		if (tableMapping.hasWhere()) {
			where.append(tableMapping.getWhere());
		}

		for (Entry<String, MappedColumn> columnEntry : tableMapping
				.getColumns()) {

			String toColumn = columnEntry.getKey();

			MappedColumn column = columnEntry.getValue();

			if (selectColumns.length() > 0) {
				insert.append(", ");
				selectColumns.append(", ");
			}
			insert.append(CoreSQLUtils.quoteWrap(toColumn));

			if (column.hasConstantValue()) {

				selectColumns.append(column.getConstantValueAsString());

			} else {

				if (column.hasDefaultValue()) {
					selectColumns.append("ifnull(");
				}
				selectColumns
						.append(CoreSQLUtils.quoteWrap(column.getFromColumn()));
				if (column.hasDefaultValue()) {
					selectColumns.append(",");
					selectColumns.append(column.getDefaultValueAsString());
					selectColumns.append(")");
				}

			}

			if (column.hasWhereValue()) {
				if (where.length() > 0) {
					where.append(" AND ");
				}
				where.append(CoreSQLUtils.quoteWrap(column.getFromColumn()));
				where.append(" ");
				where.append(column.getWhereOperator());
				where.append(" ");
				where.append(column.getWhereValueAsString());
			}

		}
		insert.append(") SELECT ");
		insert.append(selectColumns);
		insert.append(" FROM ");
		insert.append(CoreSQLUtils.quoteWrap(tableMapping.getFromTable()));

		if (where.length() > 0) {
			insert.append(" WHERE ");
			insert.append(where);
		}

		return insert.toString();
	}

	/**
	 * Transfer table content to itself with new rows containing a new column
	 * value. All rows containing the current column value are inserted as new
	 * rows with the new column value.
	 * 
	 * @param db
	 *            connection
	 * @param tableName
	 *            table name
	 * @param columnName
	 *            column name
	 * @param newColumnValue
	 *            new column value for new rows
	 * @param currentColumnValue
	 *            column value for rows to insert as new rows
	 * @since 3.2.1
	 */
	public static void transferTableContent(GeoPackageCoreConnection db,
			String tableName, String columnName, Object newColumnValue,
			Object currentColumnValue) {
		transferTableContent(db, tableName, columnName, newColumnValue,
				currentColumnValue, null);
	}

	/**
	 * Transfer table content to itself with new rows containing a new column
	 * value. All rows containing the current column value are inserted as new
	 * rows with the new column value.
	 * 
	 * @param db
	 *            connection
	 * @param tableName
	 *            table name
	 * @param columnName
	 *            column name
	 * @param newColumnValue
	 *            new column value for new rows
	 * @param currentColumnValue
	 *            column value for rows to insert as new rows
	 * @param idColumnName
	 *            id column name
	 * @since 3.2.1
	 */
	public static void transferTableContent(GeoPackageCoreConnection db,
			String tableName, String columnName, Object newColumnValue,
			Object currentColumnValue, String idColumnName) {

		TableMapping tableMapping = new TableMapping(db, tableName);
		if (idColumnName != null) {
			tableMapping.removeColumn(idColumnName);
		}
		MappedColumn tileMatrixSetNameColumn = tableMapping
				.getColumn(columnName);
		tileMatrixSetNameColumn.setConstantValue(newColumnValue);
		tileMatrixSetNameColumn.setWhereValue(currentColumnValue);

		transferTableContent(db, tableMapping);
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

	/**
	 * Update the SQL with a name change and the table mapping modifications
	 * 
	 * @param name
	 *            statement name
	 * @param sql
	 *            SQL statement
	 * @param tableMapping
	 *            table mapping
	 * @return updated SQL, null if SQL contains a deleted column
	 * @since 3.2.1
	 */
	public static String updateSQL(String name, String sql,
			TableMapping tableMapping) {

		String updatedSql = sql;

		if (tableMapping.isNewTable()) {

			String newName;
			if (name.contains(tableMapping.getFromTable())) {
				newName = name.replaceAll(tableMapping.getFromTable(),
						tableMapping.getToTable());
			} else {
				newName = name + "_" + tableMapping.getToTable();
			}

			String updatedName = replaceName(updatedSql, name, newName);
			if (updatedName != null) {
				updatedSql = updatedName;
			}

			String updatedTable = replaceName(updatedSql,
					tableMapping.getFromTable(), tableMapping.getToTable());
			if (updatedTable != null) {
				updatedSql = updatedTable;
			}

		}

		updatedSql = updateSQL(updatedSql, tableMapping);

		return updatedSql;
	}

	/**
	 * Update the SQL with table mapping modifications
	 * 
	 * @param sql
	 *            SQL statement
	 * @param tableMapping
	 *            table mapping
	 * @return updated SQL, null if SQL contains a deleted column
	 * @since 3.2.1
	 */
	public static String updateSQL(String sql, TableMapping tableMapping) {

		String updatedSql = sql;

		for (String column : tableMapping.getDroppedColumns()) {

			String updated = replaceName(updatedSql, column, " ");
			if (updated != null) {
				updatedSql = null;
				break;
			}

		}

		if (updatedSql != null) {

			for (MappedColumn column : tableMapping.getMappedColumns()) {
				if (column.hasNewName()) {

					String updated = replaceName(updatedSql,
							column.getFromColumn(), column.getToColumn());
					if (updated != null) {
						updatedSql = updated;
					}

				}
			}

		}

		return updatedSql;
	}

	/**
	 * Replace the name (table, column, etc) in the SQL with the replacement.
	 * The name must be surrounded by non word characters (i.e. not a subset of
	 * another name).
	 * 
	 * @param sql
	 *            SQL statement
	 * @param name
	 *            name
	 * @param replacement
	 *            replacement value
	 * @return null if not modified, SQL value if replaced at least once
	 * @since 3.2.1
	 */
	public static String replaceName(String sql, String name,
			String replacement) {

		String updatedSql = null;

		// Quick check if contained in the SQL
		if (sql.contains(name)) {

			boolean updated = false;
			StringBuilder updatedSqlBuilder = new StringBuilder();

			// Split the SQL apart by the name
			String[] parts = sql.split(name);

			for (int i = 0; i <= parts.length; i++) {

				if (i > 0) {

					// Find the character before the name
					String before = "_";
					String beforePart = parts[i - 1];
					if (beforePart.isEmpty()) {
						if (i == 1) {
							// SQL starts with the name, allow
							before = " ";
						}
					} else {
						before = beforePart.substring(beforePart.length() - 1);
					}

					// Find the character after the name
					String after = "_";
					if (i < parts.length) {
						String afterPart = parts[i];
						if (!afterPart.isEmpty()) {
							after = afterPart.substring(0, 1);
						}
					} else if (sql.endsWith(name)) {
						// SQL ends with the name, allow
						after = " ";
					} else {
						break;
					}

					// Check the before and after characters for non word
					// characters
					if (before.matches("\\W") && after.matches("\\W")) {
						// Replace the name
						updatedSqlBuilder.append(replacement);
						updated = true;
					} else {
						// Preserve the name
						updatedSqlBuilder.append(name);
					}
				}

				// Add the part to the SQL
				if (i < parts.length) {
					updatedSqlBuilder.append(parts[i]);
				}

			}

			// Set if the SQL was modified
			if (updated) {
				updatedSql = updatedSqlBuilder.toString();
			}

		}

		return updatedSql;
	}

}
