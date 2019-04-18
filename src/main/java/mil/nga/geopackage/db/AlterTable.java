package mil.nga.geopackage.db;

/**
 * Builds and performs alter table statements
 * 
 * @author osbornb
 * @since 3.2.1
 */
public class AlterTable {

	/**
	 * Alter Table SQL
	 */
	public static final String ALTER_TABLE = "ALTER TABLE";

	/**
	 * Rename SQL
	 */
	public static final String RENAME = "RENAME";

	/**
	 * Add SQL
	 */
	public static final String ADD = "ADD";

	/**
	 * To SQL
	 */
	public static final String TO = "TO";

	/**
	 * Column SQL
	 */
	public static final String COLUMN = "COLUMN";

	/**
	 * Database connection
	 */
	private final GeoPackageCoreConnection db;

	/**
	 * Constructor
	 * 
	 * @param db
	 *            database connection
	 */
	public AlterTable(GeoPackageCoreConnection db) {
		this.db = db;
	}

	/**
	 * Create the ALTER TABLE SQL command prefix
	 * 
	 * @param table
	 *            table name
	 * @return alter table SQL prefix
	 */
	public static String alterTable(String table) {
		return ALTER_TABLE + " " + CoreSQLUtils.quoteWrap(table);
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
		return alterTable(tableName) + " " + RENAME + " " + TO + " "
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
		return alterTable(tableName) + " " + RENAME + " " + COLUMN + " "
				+ CoreSQLUtils.quoteWrap(columnName) + " " + TO + " "
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
		return alterTable(tableName) + " " + ADD + " " + COLUMN + " "
				+ CoreSQLUtils.quoteWrap(columnName) + " " + columnDef;
	}

}
