package mil.nga.geopackage.db;

import java.sql.SQLException;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;

import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.db.master.SQLiteMaster;
import mil.nga.geopackage.db.master.SQLiteMasterType;

/**
 * Abstract GeoPackage DAO
 * 
 * @author osbornb
 *
 * @param <T>
 *            The class that the code will be operating on.
 * @param <ID>
 *            The class of the ID column associated with the class. The T class
 *            does not require an ID field. The class needs an ID parameter
 *            however so you can use Void or Object to satisfy the compiler.
 * @since 4.0.0
 */
public abstract class GeoPackageDao<T, ID> extends BaseDaoImpl<T, ID> {

	/**
	 * Database connection
	 */
	protected GeoPackageCoreConnection db;

	/**
	 * Constructor, required by ORMLite
	 * 
	 * @param connectionSource
	 *            connection source
	 * @param dataClass
	 *            data class
	 * @throws SQLException
	 *             upon failure
	 */
	public GeoPackageDao(ConnectionSource connectionSource, Class<T> dataClass)
			throws SQLException {
		super(connectionSource, dataClass);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * Check if the DAO is backed by a table or a view
	 * 
	 * @return true if a table or view exists
	 */
	@Override
	public boolean isTableExists() throws SQLException {
		return super.isTableExists() || isView();
	}

	/**
	 * Check if the DAO is backed by a table or a view
	 * 
	 * @return true if a table or view exists
	 */
	public boolean isTableOrView() {
		return isTable() || isView();
	}

	/**
	 * Check if the DAO is backed by a table
	 * 
	 * @return true if a table exists
	 */
	public boolean isTable() {
		boolean table;
		try {
			table = super.isTableExists();
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to determine if a table: " + getTableName(), e);
		}
		return table;
	}

	/**
	 * Check if the DAO is backed by a view
	 * 
	 * @return true if a view exists
	 */
	public boolean isView() {
		return SQLiteMaster.count(db, SQLiteMasterType.VIEW,
				getTableName()) > 0;
	}

	/**
	 * Verify the DAO is backed by a table or view
	 * 
	 * @since 4.0.0
	 */
	public void verifyExists() {
		if (!isTableOrView()) {
			throw new GeoPackageException("Table or view does not exist for: "
					+ getDataClass().getSimpleName());
		}
	}

	/**
	 * Set the database
	 * 
	 * @param db
	 *            database connection
	 */
	public void setDatabase(GeoPackageCoreConnection db) {
		this.db = db;
	}

	/**
	 * Get the database
	 * 
	 * @return database connection
	 */
	public GeoPackageCoreConnection getDatabase() {
		return db;
	}

	/**
	 * Drop the table
	 * 
	 * @param table
	 *            table name
	 */
	public void dropTable(String table) {
		CoreSQLUtils.dropTable(db, table);
	}

	/**
	 * Check if the table exists
	 * 
	 * @param tableName
	 *            table name
	 * @return true if exists
	 */
	public boolean tableExists(String tableName) {
		return db.tableExists(tableName);
	}

	/**
	 * Check if the view exists
	 * 
	 * @param viewName
	 *            view name
	 * @return true if exists
	 */
	public boolean viewExists(String viewName) {
		return db.viewExists(viewName);
	}

	/**
	 * Check if a table or view exists with the name
	 * 
	 * @param name
	 *            table or view name
	 * @return true if exists
	 */
	public boolean tableOrViewExists(String name) {
		return db.tableOrViewExists(name);
	}

	/**
	 * Create a GeoPackage DAO
	 * 
	 * @param <D>
	 *            DAO type
	 * @param <O>
	 *            DAO object type
	 * @param clazz
	 *            DAO class type
	 * @return GeoPackage DAO
	 */
	public <D extends GeoPackageDao<O, ?>, O> D createDao(Class<O> clazz) {
		return createDao(db, clazz);
	}

	/**
	 * Create a GeoPackage DAO
	 * 
	 * @param <D>
	 *            DAO type
	 * @param <O>
	 *            DAO object type
	 * @param db
	 *            database connection
	 * @param clazz
	 *            DAO class type
	 * @return GeoPackage DAO
	 */
	public static <D extends GeoPackageDao<O, ?>, O> D createDao(
			GeoPackageCoreConnection db, Class<O> clazz) {
		D dao;
		try {
			dao = DaoManager.createDao(db.getConnectionSource(), clazz);
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to create " + clazz.getSimpleName() + " dao", e);
		}
		dao.setDatabase(db);
		return dao;
	}

	/**
	 * Create a GeoPackage DAO
	 * 
	 * @param <D>
	 *            DAO type
	 * @param <O>
	 *            DAO object type
	 * @param db
	 *            database connection
	 * @param tableConfig
	 *            table config
	 * @return GeoPackage DAO
	 * @throws SQLException
	 *             upon error
	 * @since 4.0.0
	 */
	public static <D extends GeoPackageDao<O, ?>, O> D createDao(
			GeoPackageCoreConnection db, DatabaseTableConfig<O> tableConfig)
			throws SQLException {
		D dao = DaoManager.createDao(db.getConnectionSource(), tableConfig);
		dao.setDatabase(db);
		return dao;
	}

}
