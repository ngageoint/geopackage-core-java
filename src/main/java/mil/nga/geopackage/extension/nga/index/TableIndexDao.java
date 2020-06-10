package mil.nga.geopackage.extension.nga.index;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedDelete;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.support.ConnectionSource;

import mil.nga.geopackage.GeoPackageCore;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.db.GeoPackageCoreConnection;
import mil.nga.geopackage.db.GeoPackageDao;

/**
 * Table Index Data Access Object
 * 
 * @author osbornb
 * @since 1.1.0
 */
public class TableIndexDao extends GeoPackageDao<TableIndex, String> {

	/**
	 * Create the DAO
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * @return dao
	 * @since 4.0.0
	 */
	public static TableIndexDao create(GeoPackageCore geoPackage) {
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
	public static TableIndexDao create(GeoPackageCoreConnection db) {
		return GeoPackageDao.createDao(db, TableIndex.class);
	}

	/**
	 * Geometry Index DAO
	 */
	private GeometryIndexDao geometryIndexDao;

	/**
	 * Constructor, required by ORMLite
	 * 
	 * @param connectionSource
	 *            connection source
	 * @param dataClass
	 *            data class
	 * @throws SQLException
	 *             upon creation failure
	 */
	public TableIndexDao(ConnectionSource connectionSource,
			Class<TableIndex> dataClass) throws SQLException {
		super(connectionSource, dataClass);
	}

	/**
	 * Delete the TableIndex, cascading
	 * 
	 * @param tableIndex
	 *            table index
	 * @return rows deleted
	 * @throws SQLException
	 *             upon deletion error
	 */
	public int deleteCascade(TableIndex tableIndex) throws SQLException {
		int count = 0;

		if (tableIndex != null) {

			// Delete Geometry Indices
			GeometryIndexDao geometryIndexDao = getGeometryIndexDao();
			if (geometryIndexDao.isTableExists()) {
				DeleteBuilder<GeometryIndex, GeometryIndexKey> db = geometryIndexDao
						.deleteBuilder();
				db.where().eq(GeometryIndex.COLUMN_TABLE_NAME,
						tableIndex.getTableName());
				PreparedDelete<GeometryIndex> deleteQuery = db.prepare();
				geometryIndexDao.delete(deleteQuery);
			}

			count = delete(tableIndex);
		}
		return count;
	}

	/**
	 * Delete the collection of TableIndex, cascading
	 * 
	 * @param tableIndexCollection
	 *            table index collection
	 * @return rows deleted
	 * @throws SQLException
	 *             upon deletion failure
	 */
	public int deleteCascade(Collection<TableIndex> tableIndexCollection)
			throws SQLException {
		int count = 0;
		if (tableIndexCollection != null) {
			for (TableIndex tableIndex : tableIndexCollection) {
				count += deleteCascade(tableIndex);
			}
		}
		return count;
	}

	/**
	 * Delete the TableIndex matching the prepared query, cascading
	 * 
	 * @param preparedDelete
	 *            prepared query
	 * @return rows deleted
	 * @throws SQLException
	 *             upon deletion failure
	 */
	public int deleteCascade(PreparedQuery<TableIndex> preparedDelete)
			throws SQLException {
		int count = 0;
		if (preparedDelete != null) {
			List<TableIndex> tableIndexList = query(preparedDelete);
			count = deleteCascade(tableIndexList);
		}
		return count;
	}

	/**
	 * Delete a TableIndex by id, cascading
	 * 
	 * @param id
	 *            id
	 * @return rows deleted
	 * @throws SQLException
	 *             upon deletion failure
	 */
	public int deleteByIdCascade(String id) throws SQLException {
		int count = 0;
		if (id != null) {
			TableIndex tableIndex = queryForId(id);
			if (tableIndex != null) {
				count = deleteCascade(tableIndex);
			}
		}
		return count;
	}

	/**
	 * Delete the TableIndex with the provided ids, cascading
	 * 
	 * @param idCollection
	 *            id collection
	 * @return rows deleted
	 * @throws SQLException
	 *             upon deletion failure
	 */
	public int deleteIdsCascade(Collection<String> idCollection)
			throws SQLException {
		int count = 0;
		if (idCollection != null) {
			for (String id : idCollection) {
				count += deleteByIdCascade(id);
			}
		}
		return count;
	}

	/**
	 * Delete the table
	 * 
	 * @param table
	 *            table name
	 */
	public void deleteTable(String table) {
		try {
			deleteByIdCascade(table);
		} catch (SQLException e) {
			throw new GeoPackageException("Failed to delete table: " + table,
					e);
		}
	}

	/**
	 * Get or create a Geometry Index DAO
	 * 
	 * @return geometry index dao
	 */
	private GeometryIndexDao getGeometryIndexDao() {
		if (geometryIndexDao == null) {
			geometryIndexDao = GeometryIndexDao.create(db);
		}
		return geometryIndexDao;
	}

	/**
	 * Delete all table indices, cascading to geometry indices
	 * 
	 * @return rows deleted
	 * @throws SQLException
	 *             upon deletion failure
	 * @since 1.1.5
	 */
	public int deleteAllCascade() throws SQLException {

		// Delete Geometry Indices
		getGeometryIndexDao().deleteAll();

		int count = deleteAll();

		return count;
	}

	/**
	 * Delete all table indices
	 * 
	 * @return rows deleted
	 * @throws SQLException
	 *             upon deletion failure
	 * @since 1.1.5
	 */
	public int deleteAll() throws SQLException {

		int count = 0;

		if (isTableExists()) {
			DeleteBuilder<TableIndex, String> db = deleteBuilder();
			PreparedDelete<TableIndex> deleteQuery = db.prepare();
			count = delete(deleteQuery);
		}

		return count;
	}

}
