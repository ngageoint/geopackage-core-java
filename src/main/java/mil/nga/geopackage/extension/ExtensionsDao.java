package mil.nga.geopackage.extension;

import java.sql.SQLException;
import java.util.List;

import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedUpdate;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.support.ConnectionSource;

import mil.nga.geopackage.GeoPackageCore;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.db.GeoPackageCoreConnection;
import mil.nga.geopackage.db.GeoPackageDao;

/**
 * Extensions Data Access Object
 * 
 * @author osbornb
 */
public class ExtensionsDao extends GeoPackageDao<Extensions, Void> {

	/**
	 * Create the DAO
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * @return dao
	 * @since 4.0.0
	 */
	public static ExtensionsDao create(GeoPackageCore geoPackage) {
		return create(geoPackage.getDatabase());
	}

	/**
	 * Create the DAO
	 * 
	 * @param db
	 *            database connection
	 * @return dao
	 * @since 4.0.0
	 */
	public static ExtensionsDao create(GeoPackageCoreConnection db) {
		return GeoPackageDao.createDao(db, Extensions.class);
	}

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
	public ExtensionsDao(ConnectionSource connectionSource,
			Class<Extensions> dataClass) throws SQLException {
		super(connectionSource, dataClass);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * Update using the unique columns
	 */
	@Override
	public int update(Extensions extensions) throws SQLException {

		UpdateBuilder<Extensions, Void> ub = updateBuilder();
		ub.updateColumnValue(Extensions.COLUMN_DEFINITION,
				extensions.getDefinition());
		ub.updateColumnValue(Extensions.COLUMN_SCOPE,
				extensions.getScope().getValue());

		setUniqueWhere(ub.where(), extensions.getExtensionName(), true,
				extensions.getTableName(), true, extensions.getColumnName());

		PreparedUpdate<Extensions> update = ub.prepare();
		int updated = update(update);

		return updated;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * Delete using the unique columns
	 */
	@Override
	public int delete(Extensions extensions) throws SQLException {

		DeleteBuilder<Extensions, Void> db = deleteBuilder();

		setUniqueWhere(db.where(), extensions.getExtensionName(), true,
				extensions.getTableName(), true, extensions.getColumnName());

		int deleted = db.delete();

		return deleted;
	}

	/**
	 * Delete by extension name
	 * 
	 * @param extensionName
	 *            extension name
	 * @return deleted count
	 * @throws SQLException
	 *             upon failure
	 */
	public int deleteByExtension(String extensionName) throws SQLException {

		DeleteBuilder<Extensions, Void> db = deleteBuilder();
		db.where().eq(Extensions.COLUMN_EXTENSION_NAME, extensionName);

		int deleted = db.delete();

		return deleted;
	}

	/**
	 * Delete by extension name and table name
	 * 
	 * @param extensionName
	 *            extension name
	 * @param tableName
	 *            table name
	 * @return deleted count
	 * @throws SQLException
	 *             upon failure
	 */
	public int deleteByExtension(String extensionName, String tableName)
			throws SQLException {

		DeleteBuilder<Extensions, Void> db = deleteBuilder();

		setUniqueWhere(db.where(), extensionName, true, tableName, false, null);

		int deleted = db.delete();

		return deleted;
	}

	/**
	 * Delete by extension name, table name, and column name
	 * 
	 * @param extensionName
	 *            extension name
	 * @param tableName
	 *            table name
	 * @param columnName
	 *            column name
	 * @return deleted count
	 * @throws SQLException
	 *             upon failure
	 */
	public int deleteByExtension(String extensionName, String tableName,
			String columnName) throws SQLException {

		DeleteBuilder<Extensions, Void> db = deleteBuilder();

		setUniqueWhere(db.where(), extensionName, true, tableName, true,
				columnName);

		int deleted = db.delete();

		return deleted;
	}

	/**
	 * Delete by table name
	 * 
	 * @param tableName
	 *            table name
	 * @return rows deleted
	 * @throws SQLException
	 *             upon failure
	 * @since 1.1.8
	 */
	public int deleteByTableName(String tableName) throws SQLException {

		DeleteBuilder<Extensions, Void> db = deleteBuilder();

		db.where().eq(Extensions.COLUMN_TABLE_NAME, tableName);

		int deleted = db.delete();

		return deleted;
	}

	/**
	 * Delete all extensions
	 * 
	 * @return rows deleted
	 * @throws SQLException
	 *             upon failure
	 * @since 1.1.8
	 */
	public int deleteAll() throws SQLException {

		DeleteBuilder<Extensions, Void> db = deleteBuilder();
		int deleted = db.delete();

		return deleted;
	}

	/**
	 * Query by extension name
	 * 
	 * @param extensionName
	 *            extension name
	 * @return extensions
	 * @throws SQLException
	 *             upon failure
	 */
	public List<Extensions> queryByExtension(String extensionName)
			throws SQLException {

		QueryBuilder<Extensions, Void> qb = queryBuilder();

		setUniqueWhere(qb.where(), extensionName, false, null, false, null);

		List<Extensions> extensions = qb.query();

		return extensions;
	}

	/**
	 * Query by extension name and table name
	 * 
	 * @param extensionName
	 *            extension name
	 * @param tableName
	 *            table name
	 * @return extensions
	 * @throws SQLException
	 *             upon failure
	 */
	public List<Extensions> queryByExtension(String extensionName,
			String tableName) throws SQLException {

		QueryBuilder<Extensions, Void> qb = queryBuilder();

		setUniqueWhere(qb.where(), extensionName, true, tableName, false, null);

		List<Extensions> extensions = qb.query();

		return extensions;
	}

	/**
	 * Query by extension name, table name, and column name
	 * 
	 * @param extensionName
	 *            extension name
	 * @param tableName
	 *            table name
	 * @param columnName
	 *            column name
	 * @return extensions
	 * @throws SQLException
	 *             upon failure
	 */
	public Extensions queryByExtension(String extensionName, String tableName,
			String columnName) throws SQLException {

		QueryBuilder<Extensions, Void> qb = queryBuilder();

		setUniqueWhere(qb.where(), extensionName, true, tableName, true,
				columnName);

		List<Extensions> extensions = qb.query();

		Extensions extension = null;
		if (extensions.size() > 1) {
			throw new GeoPackageException("More than one "
					+ Extensions.class.getSimpleName()
					+ " existed for unique combination of Extension Name: "
					+ extensionName + ", Table Name: " + tableName
					+ ", Column Name: " + columnName);
		} else if (extensions.size() == 1) {
			extension = extensions.get(0);
		}

		return extension;
	}

	/**
	 * Set the unique column criteria in the where clause
	 * 
	 * @param where
	 *            where clause
	 * @param extensionName
	 *            extension name
	 * @param queryTableName
	 *            query table name
	 * @param tableName
	 *            table name
	 * @param queryColumnName
	 *            query column name
	 * @param columnName
	 *            column name
	 * @throws SQLException
	 */
	private void setUniqueWhere(Where<Extensions, Void> where,
			String extensionName, boolean queryTableName, String tableName,
			boolean queryColumnName, String columnName) throws SQLException {

		where.eq(Extensions.COLUMN_EXTENSION_NAME, extensionName);

		if (queryTableName) {
			if (tableName == null) {
				where.and().isNull(Extensions.COLUMN_TABLE_NAME);
			} else {
				where.and().eq(Extensions.COLUMN_TABLE_NAME, tableName);
			}
		}

		if (queryColumnName) {
			if (columnName == null) {
				where.and().isNull(Extensions.COLUMN_COLUMN_NAME);
			} else {
				where.and().eq(Extensions.COLUMN_COLUMN_NAME, columnName);
			}
		}

	}

}
