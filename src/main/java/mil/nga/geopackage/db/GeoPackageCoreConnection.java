package mil.nga.geopackage.db;

import java.io.Closeable;
import java.nio.ByteBuffer;

import mil.nga.geopackage.GeoPackageConstants;

import com.j256.ormlite.support.ConnectionSource;

/**
 * GeoPackage Connection used to define common functionality within different
 * connection types
 * 
 * @author osbornb
 */
public abstract class GeoPackageCoreConnection implements Closeable {

	/**
	 * Get a connection source
	 * 
	 * @return
	 */
	public abstract ConnectionSource getConnectionSource();

	/**
	 * Execute the sql
	 * 
	 * @param sql
	 */
	public abstract void execSQL(String sql);

	/**
	 * Convenience method for deleting rows in the database.
	 * 
	 * @param table
	 * @param whereClause
	 * @param whereArgs
	 * @return rows deleted
	 */
	public abstract int delete(String table, String whereClause,
			String[] whereArgs);

	/**
	 * Get a count of results
	 * 
	 * @param table
	 * @param where
	 * @param args
	 * @return count
	 */
	public abstract int count(String table, String where, String[] args);

	/**
	 * Get the min result of the column
	 * 
	 * @param table
	 * @param column
	 * @param where
	 * @param args
	 * @return min or null
	 * @since 1.1.1
	 */
	public abstract Integer min(String table, String column, String where,
			String[] args);

	/**
	 * Get the max result of the column
	 * 
	 * @param table
	 * @param column
	 * @param where
	 * @param args
	 * @return max or null
	 * @since 1.1.1
	 */
	public abstract Integer max(String table, String column, String where,
			String[] args);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public abstract void close();

	/**
	 * Check if the table exists
	 * 
	 * @param tableName
	 * @return
	 */
	public boolean tableExists(String tableName) {
		return count("sqlite_master", "tbl_name = ?",
				new String[] { tableName }) > 0;
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
	public abstract boolean columnExists(String tableName, String columnName);

	/**
	 * Add a new column to the table
	 * 
	 * @param tableName
	 * @param columnName
	 * @param columnDef
	 * @since 1.1.8
	 */
	public void addColumn(String tableName, String columnName, String columnDef) {
		execSQL("ALTER TABLE " + CoreSQLUtils.quoteWrap(tableName)
				+ " ADD COLUMN " + CoreSQLUtils.quoteWrap(columnName) + " "
				+ columnDef + ";");
	}

	/**
	 * Query for a single result string
	 * 
	 * @param sql
	 *            sql statement
	 * @param args
	 *            sql arguments
	 * @return single result object
	 * @since 1.1.8
	 */
	public abstract String querySingleStringResult(String sql, String[] args);

	/**
	 * Set the GeoPackage application id
	 */
	public void setApplicationId() {
		// Set the application id as a GeoPackage
		int applicationId = ByteBuffer
				.wrap(GeoPackageConstants.APPLICATION_ID.getBytes())
				.asIntBuffer().get();
		execSQL(String.format("PRAGMA application_id = %d;", applicationId));
	}

}
