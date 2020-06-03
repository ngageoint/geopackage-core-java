package mil.nga.geopackage.db;

import java.io.Closeable;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.j256.ormlite.support.ConnectionSource;

import mil.nga.geopackage.GeoPackageConstants;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.db.master.SQLiteMaster;
import mil.nga.geopackage.db.master.SQLiteMasterType;
import mil.nga.geopackage.db.table.TableInfo;

/**
 * GeoPackage Connection used to define common functionality within different
 * connection types
 * 
 * @author osbornb
 */
public abstract class GeoPackageCoreConnection implements Closeable {

	/**
	 * Logger
	 */
	private static final Logger logger = Logger
			.getLogger(GeoPackageCoreConnection.class.getName());

	/**
	 * Connection source
	 */
	protected final ConnectionSource connectionSource;

	/**
	 * Constructor
	 *
	 * @param connectionSource
	 *            connection source
	 */
	protected GeoPackageCoreConnection(ConnectionSource connectionSource) {
		this.connectionSource = connectionSource;
	}

	/**
	 * Copy Constructor
	 *
	 * @param connection
	 *            connection
	 * @since 3.4.0
	 */
	protected GeoPackageCoreConnection(GeoPackageCoreConnection connection) {
		this(connection.connectionSource);
	}

	/**
	 * Get a connection source
	 * 
	 * @return connection source
	 */
	public ConnectionSource getConnectionSource() {
		return connectionSource;
	}

	/**
	 * Execute the sql
	 * 
	 * @param sql
	 *            sql statement
	 */
	public abstract void execSQL(String sql);

	/**
	 * Begin a transaction
	 * 
	 * @since 3.3.0
	 */
	public abstract void beginTransaction();

	/**
	 * End a transaction successfully
	 * 
	 * @since 3.3.0
	 */
	public void endTransaction() {
		endTransaction(true);
	}

	/**
	 * Fail a transaction
	 * 
	 * @since 3.3.0
	 */
	public void failTransaction() {
		endTransaction(false);
	}

	/**
	 * End a transaction
	 * 
	 * @param successful
	 *            true if the transaction was successful, false to rollback or
	 *            not commit
	 * @since 3.3.0
	 */
	public abstract void endTransaction(boolean successful);

	/**
	 * End a transaction as successful and begin a new transaction
	 *
	 * @since 3.3.0
	 */
	public void endAndBeginTransaction() {
		endTransaction();
		beginTransaction();
	}

	/**
	 * Commit changes on the connection
	 * 
	 * @since 3.3.0
	 */
	public abstract void commit();

	/**
	 * Determine if currently within a transaction
	 * 
	 * @return true if in transaction
	 * 
	 * @since 3.3.0
	 */
	public abstract boolean inTransaction();

	/**
	 * If foreign keys is disabled and there are no foreign key violations,
	 * enables foreign key checks, else logs violations
	 * 
	 * @return true if enabled or already enabled, false if foreign key
	 *         violations and not enabled
	 * @since 3.3.0
	 */
	public boolean enableForeignKeys() {
		boolean enabled = foreignKeys();
		if (!enabled) {
			List<List<Object>> violations = foreignKeyCheck();
			if (violations.isEmpty()) {
				foreignKeys(true);
				enabled = true;
			} else {
				for (List<Object> violation : violations) {
					logger.log(Level.WARNING,
							"Foreign Key violation. Table: " + violation.get(0)
									+ ", Row Id: " + violation.get(1)
									+ ", Referred Table: " + violation.get(2)
									+ ", FK Index: " + violation.get(3));
				}
			}
		}
		return enabled;
	}

	/**
	 * Query for the foreign keys value
	 * 
	 * @return true if enabled, false if disabled
	 * @since 3.3.0
	 */
	public boolean foreignKeys() {
		return CoreSQLUtils.foreignKeys(this);
	}

	/**
	 * Change the foreign keys state
	 * 
	 * @param on
	 *            true to turn on, false to turn off
	 * @return previous foreign keys value
	 * @since 3.3.0
	 */
	public boolean foreignKeys(boolean on) {
		return CoreSQLUtils.foreignKeys(this, on);
	}

	/**
	 * Perform a foreign key check
	 * 
	 * @return empty list if valid or violation errors, 4 column values for each
	 *         violation. see SQLite PRAGMA foreign_key_check
	 * @since 3.3.0
	 */
	public List<List<Object>> foreignKeyCheck() {
		return CoreSQLUtils.foreignKeyCheck(this);
	}

	/**
	 * Perform a foreign key check
	 * 
	 * @param tableName
	 *            table name
	 * @return empty list if valid or violation errors, 4 column values for each
	 *         violation. see SQLite PRAGMA foreign_key_check
	 * @since 3.3.0
	 */
	public List<List<Object>> foreignKeyCheck(String tableName) {
		return CoreSQLUtils.foreignKeyCheck(this, tableName);
	}

	/**
	 * Convenience method for deleting rows in the database.
	 * 
	 * @param table
	 *            table name
	 * @param whereClause
	 *            where clause
	 * @param whereArgs
	 *            where arguments
	 * @return rows deleted
	 */
	public abstract int delete(String table, String whereClause,
			String[] whereArgs);

	/**
	 * Get a count of results
	 * 
	 * @param table
	 *            table name
	 * @return count
	 * @since 4.0.0
	 */
	public int count(String table) {
		return count(table, null, null);
	}

	/**
	 * Get a count of results
	 * 
	 * @param table
	 *            table name
	 * @param where
	 *            where clause
	 * @param args
	 *            arguments
	 * @return count
	 */
	public int count(String table, String where, String[] args) {
		return count(table, null, where, args);
	}

	/**
	 * Get a count of results
	 * 
	 * @param table
	 *            table name
	 * @param column
	 *            column name
	 * @return count
	 * @since 4.0.0
	 */
	public int count(String table, String column) {
		return count(table, false, column);
	}

	/**
	 * Get a count of results
	 * 
	 * @param table
	 *            table name
	 * @param distinct
	 *            distinct column flag
	 * @param column
	 *            column name
	 * @return count
	 * @since 4.0.0
	 */
	public int count(String table, boolean distinct, String column) {
		return count(table, distinct, column, null, null);
	}

	/**
	 * Get a count of results
	 * 
	 * @param table
	 *            table name
	 * @param column
	 *            column name
	 * @param where
	 *            where clause
	 * @param args
	 *            arguments
	 * @return count
	 * @since 4.0.0
	 */
	public int count(String table, String column, String where, String[] args) {
		return count(table, false, column, where, args);
	}

	/**
	 * Get a count of results
	 * 
	 * @param table
	 *            table name
	 * @param distinct
	 *            distinct column flag
	 * @param column
	 *            column name
	 * @param where
	 *            where clause
	 * @param args
	 *            arguments
	 * @return count
	 * @since 4.0.0
	 */
	public int count(String table, boolean distinct, String column,
			String where, String[] args) {

		int count = 0;
		Number value = aggregateFunction("COUNT", table, distinct, column,
				where, args);
		if (value != null) {
			count = value.intValue();
		}

		return count;
	}

	/**
	 * Get the min result of the column
	 * 
	 * @param <T>
	 *            return type
	 * @param table
	 *            table name
	 * @param column
	 *            column name
	 * @return min or null
	 * @since 4.0.0
	 */
	public <T> T min(String table, String column) {
		return min(table, column, null, null);
	}

	/**
	 * Get the min result of the column
	 * 
	 * @param <T>
	 *            return type
	 * @param table
	 *            table name
	 * @param column
	 *            column name
	 * @param where
	 *            where clause
	 * @param args
	 *            where arguments
	 * @return min or null
	 * @since 4.0.0
	 */
	public <T> T min(String table, String column, String where, String[] args) {
		return aggregateFunction("MIN", table, column, where, args);
	}

	/**
	 * Get the max result of the column
	 * 
	 * @param <T>
	 *            return type
	 * @param table
	 *            table name
	 * @param column
	 *            column name
	 * @return max or null
	 * @since 4.0.0
	 */
	public <T> T max(String table, String column) {
		return max(table, column, null, null);
	}

	/**
	 * Get the max result of the column
	 * 
	 * @param <T>
	 *            return type
	 * @param table
	 *            table name
	 * @param column
	 *            column name
	 * @param where
	 *            where clause
	 * @param args
	 *            where arguments
	 * @return max or null
	 * @since 4.0.0
	 */
	public <T> T max(String table, String column, String where, String[] args) {
		return aggregateFunction("MAX", table, column, where, args);
	}

	/**
	 * Execute an aggregate function
	 * 
	 * @param <T>
	 *            return type
	 * @param function
	 *            aggregate function
	 * @param table
	 *            table name
	 * @param column
	 *            column name
	 * @return value or null
	 * @since 4.0.0
	 */
	public <T> T aggregateFunction(String function, String table,
			String column) {
		return aggregateFunction(function, table, false, column);
	}

	/**
	 * Execute an aggregate function
	 * 
	 * @param <T>
	 *            return type
	 * @param function
	 *            aggregate function
	 * @param table
	 *            table name
	 * @param distinct
	 *            distinct column flag
	 * @param column
	 *            column name
	 * @return value or null
	 * @since 4.0.0
	 */
	public <T> T aggregateFunction(String function, String table,
			boolean distinct, String column) {
		return aggregateFunction(function, table, distinct, column, null, null);
	}

	/**
	 * Execute an aggregate function
	 * 
	 * @param <T>
	 *            return type
	 * @param function
	 *            aggregate function
	 * @param table
	 *            table name
	 * @param column
	 *            column name
	 * @param where
	 *            where clause
	 * @param args
	 *            arguments
	 * @return value or null
	 * @since 4.0.0
	 */
	public <T> T aggregateFunction(String function, String table, String column,
			String where, String[] args) {
		return aggregateFunction(function, table, false, column, where, args);
	}

	/**
	 * Execute an aggregate function
	 * 
	 * @param <T>
	 *            return type
	 * @param function
	 *            aggregate function
	 * @param table
	 *            table name
	 * @param distinct
	 *            distinct column flag
	 * @param column
	 *            column name
	 * @param where
	 *            where clause
	 * @param args
	 *            arguments
	 * @return value or null
	 * @since 4.0.0
	 */
	public <T> T aggregateFunction(String function, String table,
			boolean distinct, String column, String where, String[] args) {

		StringBuilder query = new StringBuilder();
		query.append("SELECT ");
		query.append(function);
		query.append("(");
		if (column != null) {
			if (distinct) {
				query.append("DISTINCT ");
			}
			query.append(CoreSQLUtils.quoteWrap(column));
		} else {
			query.append("*");
		}
		query.append(") FROM ");
		query.append(CoreSQLUtils.quoteWrap(table));
		if (where != null) {
			query.append(" WHERE ").append(where);
		}
		String sql = query.toString();

		Object value = querySingleResult(sql, args);

		@SuppressWarnings("unchecked")
		T typedValue = (T) value;

		return typedValue;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void close() {
		GeoPackageDaoManager.unregisterDaos(connectionSource);
		connectionSource.closeQuietly();
	}

	/**
	 * Check if the table exists
	 * 
	 * @param tableName
	 *            table name
	 * @return true if exists
	 */
	public boolean tableExists(String tableName) {
		return SQLiteMaster.count(this, SQLiteMasterType.TABLE, tableName) > 0;
	}

	/**
	 * Check if the view exists
	 * 
	 * @param viewName
	 *            view name
	 * @return true if exists
	 * @since 4.0.0
	 */
	public boolean viewExists(String viewName) {
		return SQLiteMaster.count(this, SQLiteMasterType.VIEW, viewName) > 0;
	}

	/**
	 * Check if a table or view exists with the name
	 * 
	 * @param name
	 *            table or view name
	 * @return true if exists
	 * @since 4.0.0
	 */
	public boolean tableOrViewExists(String name) {
		return SQLiteMaster.count(this, new SQLiteMasterType[] {
				SQLiteMasterType.TABLE, SQLiteMasterType.VIEW }, name) > 0;
	}

	/**
	 * Check if the table column exists
	 * 
	 * @param tableName
	 *            table name
	 * @param columnName
	 *            column name
	 * @return true if column exists
	 * @since 1.1.8
	 */
	public boolean columnExists(String tableName, String columnName) {

		boolean exists = false;

		TableInfo tableInfo = TableInfo.info(this, tableName);
		if (tableInfo != null) {
			exists = tableInfo.hasColumn(columnName);
		}

		return exists;
	}

	/**
	 * Add a new column to the table
	 * 
	 * @param tableName
	 *            table name
	 * @param columnName
	 *            column name
	 * @param columnDef
	 *            column definition
	 * @since 1.1.8
	 */
	public void addColumn(String tableName, String columnName,
			String columnDef) {
		AlterTable.addColumn(this, tableName, columnName, columnDef);
	}

	/**
	 * Query the SQL for a single result object in the first column
	 * 
	 * @param sql
	 *            sql statement
	 * @param args
	 *            sql arguments
	 * @return single result object
	 * @since 3.1.0
	 */
	public Object querySingleResult(String sql, String[] args) {
		return querySingleResult(sql, args, 0);
	}

	/**
	 * Query the SQL for a single result typed object in the first column
	 * 
	 * @param <T>
	 *            result value type
	 * @param sql
	 *            sql statement
	 * @param args
	 *            sql arguments
	 * @return single result object
	 * @since 3.1.0
	 */
	public <T> T querySingleTypedResult(String sql, String[] args) {
		@SuppressWarnings("unchecked")
		T result = (T) querySingleResult(sql, args);
		return result;
	}

	/**
	 * Query the SQL for a single result object in the first column with the
	 * expected data type
	 * 
	 * @param sql
	 *            sql statement
	 * @param args
	 *            sql arguments
	 * @param dataType
	 *            GeoPackage data type
	 * @return single result object
	 * @since 3.1.0
	 */
	public Object querySingleResult(String sql, String[] args,
			GeoPackageDataType dataType) {
		return querySingleResult(sql, args, 0, dataType);
	}

	/**
	 * Query the SQL for a single result typed object in the first column with
	 * the expected data type
	 * 
	 * @param <T>
	 *            result value type
	 * @param sql
	 *            sql statement
	 * @param args
	 *            sql arguments
	 * @param dataType
	 *            GeoPackage data type
	 * @return single result object
	 * @since 3.1.0
	 */
	public <T> T querySingleTypedResult(String sql, String[] args,
			GeoPackageDataType dataType) {
		@SuppressWarnings("unchecked")
		T result = (T) querySingleResult(sql, args, dataType);
		return result;
	}

	/**
	 * Query the SQL for a single result object
	 * 
	 * @param sql
	 *            sql statement
	 * @param args
	 *            arguments
	 * @param column
	 *            column index
	 * @return result, null if no result
	 * @since 3.1.0
	 */
	public Object querySingleResult(String sql, String[] args, int column) {
		return querySingleResult(sql, args, column, null);
	}

	/**
	 * Query the SQL for a single result typed object
	 * 
	 * @param <T>
	 *            result value type
	 * @param sql
	 *            sql statement
	 * @param args
	 *            arguments
	 * @param column
	 *            column index
	 * @return result, null if no result
	 * @since 3.1.0
	 */
	public <T> T querySingleTypedResult(String sql, String[] args, int column) {
		@SuppressWarnings("unchecked")
		T result = (T) querySingleResult(sql, args, column);
		return result;
	}

	/**
	 * Query the SQL for a single result object with the expected data type
	 * 
	 * @param sql
	 *            sql statement
	 * @param args
	 *            arguments
	 * @param column
	 *            column index
	 * @param dataType
	 *            GeoPackage data type
	 * @return result, null if no result
	 * @since 3.1.0
	 */
	public abstract Object querySingleResult(String sql, String[] args,
			int column, GeoPackageDataType dataType);

	/**
	 * Query the SQL for a single result typed object with the expected data
	 * type
	 * 
	 * @param <T>
	 *            result value type
	 * @param sql
	 *            sql statement
	 * @param args
	 *            arguments
	 * @param column
	 *            column index
	 * @param dataType
	 *            GeoPackage data type
	 * @return result, null if no result
	 * @since 3.1.0
	 */
	public <T> T querySingleTypedResult(String sql, String[] args, int column,
			GeoPackageDataType dataType) {
		@SuppressWarnings("unchecked")
		T result = (T) querySingleResult(sql, args, column, dataType);
		return result;
	}

	/**
	 * Query for values from the first column
	 * 
	 * @param sql
	 *            sql statement
	 * @param args
	 *            sql arguments
	 * @return single column values
	 * @since 3.1.0
	 */
	public List<Object> querySingleColumnResults(String sql, String[] args) {
		return querySingleColumnResults(sql, args, 0, null, null);
	}

	/**
	 * Query for values from the first column
	 * 
	 * @param <T>
	 *            result value type
	 * @param sql
	 *            sql statement
	 * @param args
	 *            sql arguments
	 * @return single column values
	 * @since 3.1.0
	 */
	public <T> List<T> querySingleColumnTypedResults(String sql,
			String[] args) {
		@SuppressWarnings("unchecked")
		List<T> result = (List<T>) querySingleColumnResults(sql, args);
		return result;
	}

	/**
	 * Query for values from the first column
	 * 
	 * @param sql
	 *            sql statement
	 * @param args
	 *            arguments
	 * @param dataType
	 *            GeoPackage data type
	 * @return single column results
	 * @since 3.1.0
	 */
	public List<Object> querySingleColumnResults(String sql, String[] args,
			GeoPackageDataType dataType) {
		return querySingleColumnResults(sql, args, 0, dataType, null);
	}

	/**
	 * Query for typed values from the first column
	 * 
	 * @param <T>
	 *            result value type
	 * @param sql
	 *            sql statement
	 * @param args
	 *            arguments
	 * @param dataType
	 *            GeoPackage data type
	 * @return single column results
	 * @since 3.1.0
	 */
	public <T> List<T> querySingleColumnTypedResults(String sql, String[] args,
			GeoPackageDataType dataType) {
		@SuppressWarnings("unchecked")
		List<T> result = (List<T>) querySingleColumnResults(sql, args,
				dataType);
		return result;
	}

	/**
	 * Query for values from a single column
	 * 
	 * @param sql
	 *            sql statement
	 * @param args
	 *            arguments
	 * @param column
	 *            column index
	 * @return single column results
	 * @since 3.1.0
	 */
	public List<Object> querySingleColumnResults(String sql, String[] args,
			int column) {
		return querySingleColumnResults(sql, args, column, null, null);
	}

	/**
	 * Query for typed values from a single column
	 * 
	 * @param <T>
	 *            result value type
	 * @param sql
	 *            sql statement
	 * @param args
	 *            arguments
	 * @param column
	 *            column index
	 * @return single column results
	 * @since 3.1.0
	 */
	public <T> List<T> querySingleColumnTypedResults(String sql, String[] args,
			int column) {
		@SuppressWarnings("unchecked")
		List<T> result = (List<T>) querySingleColumnResults(sql, args, column);
		return result;
	}

	/**
	 * Query for values from a single column
	 * 
	 * @param sql
	 *            sql statement
	 * @param args
	 *            arguments
	 * @param column
	 *            column index
	 * @param dataType
	 *            GeoPackage data type
	 * @return single column results
	 * @since 3.1.0
	 */
	public List<Object> querySingleColumnResults(String sql, String[] args,
			int column, GeoPackageDataType dataType) {
		return querySingleColumnResults(sql, args, column, dataType, null);
	}

	/**
	 * Query for typed values from a single column
	 * 
	 * @param <T>
	 *            result value type
	 * @param sql
	 *            sql statement
	 * @param args
	 *            arguments
	 * @param column
	 *            column index
	 * @param dataType
	 *            GeoPackage data type
	 * @return single column results
	 * @since 3.1.0
	 */
	public <T> List<T> querySingleColumnTypedResults(String sql, String[] args,
			int column, GeoPackageDataType dataType) {
		@SuppressWarnings("unchecked")
		List<T> result = (List<T>) querySingleColumnResults(sql, args, column,
				dataType);
		return result;
	}

	/**
	 * Query for values from a single column up to the limit
	 * 
	 * @param sql
	 *            sql statement
	 * @param args
	 *            arguments
	 * @param limit
	 *            result row limit
	 * @param column
	 *            column index
	 * @return single column results
	 * @since 3.1.0
	 */
	public List<Object> querySingleColumnResults(String sql, String[] args,
			int column, Integer limit) {
		return querySingleColumnResults(sql, args, column, null, limit);
	}

	/**
	 * Query for typed values from a single column up to the limit
	 * 
	 * @param <T>
	 *            result value type
	 * @param sql
	 *            sql statement
	 * @param args
	 *            arguments
	 * @param limit
	 *            result row limit
	 * @param column
	 *            column index
	 * @return single column results
	 * @since 3.1.0
	 */
	public <T> List<T> querySingleColumnTypedResults(String sql, String[] args,
			int column, Integer limit) {
		@SuppressWarnings("unchecked")
		List<T> result = (List<T>) querySingleColumnResults(sql, args, column,
				limit);
		return result;
	}

	/**
	 * Query for values from a single column up to the limit
	 * 
	 * @param sql
	 *            sql statement
	 * @param args
	 *            arguments
	 * @param column
	 *            column index
	 * @param dataType
	 *            GeoPackage data type
	 * @param limit
	 *            result row limit
	 * @return single column results
	 * @since 3.1.0
	 */
	public abstract List<Object> querySingleColumnResults(String sql,
			String[] args, int column, GeoPackageDataType dataType,
			Integer limit);

	/**
	 * Query for typed values from a single column up to the limit
	 * 
	 * @param <T>
	 *            result value type
	 * @param sql
	 *            sql statement
	 * @param args
	 *            arguments
	 * @param column
	 *            column index
	 * @param dataType
	 *            GeoPackage data type
	 * @param limit
	 *            result row limit
	 * @return single column results
	 * @since 3.1.0
	 */
	public <T> List<T> querySingleColumnTypedResults(String sql, String[] args,
			int column, GeoPackageDataType dataType, Integer limit) {
		@SuppressWarnings("unchecked")
		List<T> result = (List<T>) querySingleColumnResults(sql, args, column,
				dataType, limit);
		return result;
	}

	/**
	 * Query for values
	 * 
	 * @param sql
	 *            sql statement
	 * @param args
	 *            arguments
	 * @return results
	 * @since 3.1.0
	 */
	public List<List<Object>> queryResults(String sql, String[] args) {
		return queryResults(sql, args, null, null);
	}

	/**
	 * Query for typed values
	 * 
	 * @param <T>
	 *            result value type
	 * @param sql
	 *            sql statement
	 * @param args
	 *            arguments
	 * @return results
	 * @since 3.1.0
	 */
	public <T> List<List<T>> queryTypedResults(String sql, String[] args) {
		@SuppressWarnings("unchecked")
		List<List<T>> result = (List<List<T>>) (Object) queryResults(sql, args);
		return result;
	}

	/**
	 * Query for values
	 * 
	 * @param sql
	 *            sql statement
	 * @param args
	 *            arguments
	 * @param dataTypes
	 *            column data types
	 * @return results
	 * @since 3.1.0
	 */
	public List<List<Object>> queryResults(String sql, String[] args,
			GeoPackageDataType[] dataTypes) {
		return queryResults(sql, args, dataTypes, null);
	}

	/**
	 * Query for typed values
	 * 
	 * @param <T>
	 *            result value type
	 * @param sql
	 *            sql statement
	 * @param args
	 *            arguments
	 * @param dataTypes
	 *            column data types
	 * @return results
	 * @since 3.1.0
	 */
	public <T> List<List<T>> queryTypedResults(String sql, String[] args,
			GeoPackageDataType[] dataTypes) {
		@SuppressWarnings("unchecked")
		List<List<T>> result = (List<List<T>>) (Object) queryResults(sql, args,
				dataTypes);
		return result;
	}

	/**
	 * Query for values in a single (first) row
	 * 
	 * @param sql
	 *            sql statement
	 * @param args
	 *            arguments
	 * @return single row results
	 * @since 3.1.0
	 */
	public List<Object> querySingleRowResults(String sql, String[] args) {
		return querySingleRowResults(sql, args, null);
	}

	/**
	 * Query for typed values in a single (first) row
	 * 
	 * @param <T>
	 *            result value type
	 * @param sql
	 *            sql statement
	 * @param args
	 *            arguments
	 * @return single row results
	 * @since 3.1.0
	 */
	public <T> List<T> querySingleRowTypedResults(String sql, String[] args) {
		@SuppressWarnings("unchecked")
		List<T> result = (List<T>) querySingleRowResults(sql, args);
		return result;
	}

	/**
	 * Query for values in a single (first) row
	 * 
	 * @param sql
	 *            sql statement
	 * @param args
	 *            arguments
	 * @param dataTypes
	 *            column data types
	 * @return single row results
	 * @since 3.1.0
	 */
	public List<Object> querySingleRowResults(String sql, String[] args,
			GeoPackageDataType[] dataTypes) {
		List<List<Object>> results = queryResults(sql, args, dataTypes, 1);
		List<Object> singleRow = null;
		if (!results.isEmpty()) {
			singleRow = results.get(0);
		}
		return singleRow;
	}

	/**
	 * Query for typed values in a single (first) row
	 * 
	 * @param <T>
	 *            result value type
	 * @param sql
	 *            sql statement
	 * @param args
	 *            arguments
	 * @param dataTypes
	 *            column data types
	 * @return single row results
	 * @since 3.1.0
	 */
	public <T> List<T> querySingleRowTypedResults(String sql, String[] args,
			GeoPackageDataType[] dataTypes) {
		@SuppressWarnings("unchecked")
		List<T> result = (List<T>) querySingleRowResults(sql, args, dataTypes);
		return result;
	}

	/**
	 * Query for values
	 * 
	 * @param sql
	 *            sql statement
	 * @param args
	 *            arguments
	 * @param limit
	 *            result row limit
	 * @return results
	 * @since 3.1.0
	 */
	public List<List<Object>> queryResults(String sql, String[] args,
			Integer limit) {
		return queryResults(sql, args, null, limit);
	}

	/**
	 * Query for typed values
	 * 
	 * @param <T>
	 *            result value type
	 * @param sql
	 *            sql statement
	 * @param args
	 *            arguments
	 * @param limit
	 *            result row limit
	 * @return results
	 * @since 3.1.0
	 */
	public <T> List<List<T>> queryTypedResults(String sql, String[] args,
			Integer limit) {
		@SuppressWarnings("unchecked")
		List<List<T>> result = (List<List<T>>) (Object) queryResults(sql, args,
				limit);
		return result;
	}

	/**
	 * Query for values up to the limit
	 * 
	 * @param sql
	 *            sql statement
	 * @param args
	 *            arguments
	 * @param dataTypes
	 *            column data types
	 * @param limit
	 *            result row limit
	 * @return results
	 * @since 3.1.0
	 */
	public abstract List<List<Object>> queryResults(String sql, String[] args,
			GeoPackageDataType[] dataTypes, Integer limit);

	/**
	 * Query for typed values up to the limit
	 * 
	 * @param <T>
	 *            result value type
	 * @param sql
	 *            sql statement
	 * @param args
	 *            arguments
	 * @param dataTypes
	 *            column data types
	 * @param limit
	 *            result row limit
	 * @return results
	 * @since 3.1.0
	 */
	public <T> List<List<T>> queryTypedResults(String sql, String[] args,
			GeoPackageDataType[] dataTypes, Integer limit) {
		@SuppressWarnings("unchecked")
		List<List<T>> result = (List<List<T>>) (Object) queryResults(sql, args,
				dataTypes, limit);
		return result;
	}

	/**
	 * Set the GeoPackage application id
	 */
	public void setApplicationId() {
		setApplicationId(GeoPackageConstants.APPLICATION_ID);
	}

	/**
	 * Set the GeoPackage application id
	 *
	 * @param applicationId
	 *            application id
	 * @since 1.2.1
	 */
	public void setApplicationId(String applicationId) {
		// Set the application id as a GeoPackage
		int applicationIdInt = ByteBuffer.wrap(applicationId.getBytes())
				.asIntBuffer().get();
		execSQL(String.format("PRAGMA application_id = %d;", applicationIdInt));
	}

	/**
	 * Get the application id
	 *
	 * @return application id
	 * @since 1.2.1
	 */
	public String getApplicationId() {
		return getApplicationId(getApplicationIdInteger());
	}

	/**
	 * Get the application id integer
	 * 
	 * @return application id integer
	 * @since 4.0.0
	 */
	public Integer getApplicationIdInteger() {
		return querySingleTypedResult("PRAGMA application_id", null,
				GeoPackageDataType.MEDIUMINT);
	}

	/**
	 * Get the application id as a hex string prefixed with 0x
	 * 
	 * @return application id hex string
	 * @since 4.0.0
	 */
	public String getApplicationIdHex() {
		String hex = null;
		Integer applicationId = getApplicationIdInteger();
		if (applicationId != null) {
			hex = "0x" + Integer.toHexString(applicationId);
		}
		return hex;
	}

	/**
	 * Get the application id string value for the application id integer
	 * 
	 * @param applicationId
	 *            application id integer
	 * @return application id
	 * @since 4.0.0
	 */
	public static String getApplicationId(Integer applicationId) {
		String id = null;
		if (applicationId != null) {
			if (applicationId == 0) {
				id = GeoPackageConstants.SQLITE_APPLICATION_ID;
			} else {
				try {
					id = new String(ByteBuffer.allocate(4).putInt(applicationId)
							.array(), "UTF-8");
				} catch (UnsupportedEncodingException e) {
					throw new GeoPackageException(
							"Unexpected application id character encoding: "
									+ applicationId,
							e);
				}
			}
		}
		return id;
	}

	/**
	 * Set the GeoPackage user version
	 * 
	 * @since 1.2.1
	 */
	public void setUserVersion() {
		setUserVersion(GeoPackageConstants.USER_VERSION);
	}

	/**
	 * Set the user version
	 *
	 * @param userVersion
	 *            user version
	 * @since 1.2.1
	 */
	public void setUserVersion(int userVersion) {
		execSQL(String.format("PRAGMA user_version = %d;", userVersion));
	}

	/**
	 * Get the user version
	 *
	 * @return user version
	 * @since 1.2.1
	 */
	public Integer getUserVersion() {
		return querySingleTypedResult("PRAGMA user_version", null,
				GeoPackageDataType.MEDIUMINT);
	}

	/**
	 * Get the user version major
	 *
	 * @return user version major
	 * @since 4.0.0
	 */
	public Integer getUserVersionMajor() {
		Integer major = null;
		Integer userVersion = getUserVersion();
		if (userVersion != null) {
			major = userVersion / 10000;
		}
		return major;
	}

	/**
	 * Get the user version minor
	 *
	 * @return user version minor
	 * @since 4.0.0
	 */
	public Integer getUserVersionMinor() {
		Integer minor = null;
		Integer userVersion = getUserVersion();
		if (userVersion != null) {
			minor = (userVersion % 10000) / 100;
		}
		return minor;
	}

	/**
	 * Get the user version patch
	 *
	 * @return user version patch
	 * @since 4.0.0
	 */
	public Integer getUserVersionPatch() {
		Integer patch = null;
		Integer userVersion = getUserVersion();
		if (userVersion != null) {
			patch = userVersion % 100;
		}
		return patch;
	}

}
