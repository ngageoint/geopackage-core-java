package mil.nga.geopackage.db;

import java.io.Closeable;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.List;

import mil.nga.geopackage.GeoPackageConstants;
import mil.nga.geopackage.GeoPackageException;

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
	 * @return connection source
	 */
	public abstract ConnectionSource getConnectionSource();

	/**
	 * Execute the sql
	 * 
	 * @param sql
	 *            sql statement
	 */
	public abstract void execSQL(String sql);

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
	 * @param where
	 *            where clause
	 * @param args
	 *            arguments
	 * @return count
	 */
	public abstract int count(String table, String where, String[] args);

	/**
	 * Get the min result of the column
	 * 
	 * @param table
	 *            table name
	 * @param column
	 *            column name
	 * @param where
	 *            where clause
	 * @param args
	 *            where arguments
	 * @return min or null
	 * @since 1.1.1
	 */
	public abstract Integer min(String table, String column, String where,
			String[] args);

	/**
	 * Get the max result of the column
	 * 
	 * @param table
	 *            table name
	 * @param column
	 *            column name
	 * @param where
	 *            where clause
	 * @param args
	 *            where arguments
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
	 *            table name
	 * @return true if exists
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
	 *            table name
	 * @param columnName
	 *            column name
	 * @param columnDef
	 *            column definition
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
	 * Query for a single result integer
	 * 
	 * @param sql
	 *            sql statement
	 * @param args
	 *            sql arguments
	 * @return single result object
	 * @since 1.2.1
	 */
	public abstract Integer querySingleIntResult(String sql, String[] args);

	/**
	 * Query for a single result object
	 * 
	 * @param sql
	 *            sql statement
	 * @param args
	 *            sql arguments
	 * @return single result object
	 * @since 3.0.2
	 */
	public abstract Object querySingleObjectResult(String sql, String[] args);

	/**
	 * Query for values from a single column
	 * 
	 * @param sql
	 *            sql statement
	 * @param args
	 *            sql arguments
	 * @return single column values
	 * @since 3.0.2
	 */
	public abstract List<Object> querySingleColumnResults(String sql,
			String[] args);

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
		String applicationId = null;
		Integer applicationIdInteger = querySingleIntResult(
				"PRAGMA application_id", null);
		if (applicationIdInteger != null) {
			try {
				applicationId = new String(ByteBuffer.allocate(4)
						.putInt(applicationIdInteger).array(), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				throw new GeoPackageException(
						"Unexpected application id character encoding", e);
			}
		}
		return applicationId;
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
	public int getUserVersion() {
		int userVersion = -1;
		Integer userVersionInteger = querySingleIntResult(
				"PRAGMA user_version", null);
		if (userVersionInteger != null) {
			userVersion = userVersionInteger;
		}
		return userVersion;
	}

}
