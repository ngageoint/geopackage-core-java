package mil.nga.giat.geopackage.db;

import java.io.Closeable;

import com.j256.ormlite.support.ConnectionSource;

/**
 * GeoPackage Connection used to define common functionality within different
 * connection types
 * 
 * @author osbornb
 */
public interface GeoPackageCoreConnection extends Closeable {

	/**
	 * Get a connection source
	 * 
	 * @return
	 */
	public ConnectionSource getConnectionSource();

	/**
	 * Execute the sql
	 * 
	 * @param sql
	 */
	public void execSQL(String sql);

	/**
	 * Check if the table exists
	 * 
	 * @param tableName
	 * @return
	 */
	public boolean tableExists(String tableName);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void close();

}
