package mil.nga.geopackage.extension.index;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import mil.nga.geopackage.GeoPackageException;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedDelete;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.support.ConnectionSource;

/**
 * Table Index Data Access Object
 * 
 * @author osbornb
 * @since 1.1.0
 */
public class TableIndexDao extends BaseDaoImpl<TableIndex, String> {

	/**
	 * Geometry Index DAO
	 */
	private GeometryIndexDao geometryIndexDao;

	/**
	 * Constructor, required by ORMLite
	 * 
	 * @param connectionSource
	 * @param dataClass
	 * @throws SQLException
	 */
	public TableIndexDao(ConnectionSource connectionSource,
			Class<TableIndex> dataClass) throws SQLException {
		super(connectionSource, dataClass);
	}

	/**
	 * Delete the TableIndex, cascading
	 * 
	 * @param tableIndex
	 * @return rows deleted
	 * @throws SQLException
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
	 * @return rows deleted
	 * @throws SQLException
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
	 * @return rows deleted
	 * @throws SQLException
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
	 * @return rows deleted
	 * @throws SQLException
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
	 * @return rows deleted
	 * @throws SQLException
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
	 */
	public void deleteTable(String table) {
		try {
			deleteByIdCascade(table);
		} catch (SQLException e) {
			throw new GeoPackageException("Failed to delete table: " + table, e);
		}
	}

	/**
	 * Get or create a Geometry Index DAO
	 * 
	 * @return geometry index dao
	 * @throws SQLException
	 */
	private GeometryIndexDao getGeometryIndexDao() throws SQLException {
		if (geometryIndexDao == null) {
			geometryIndexDao = DaoManager.createDao(connectionSource,
					GeometryIndex.class);
		}
		return geometryIndexDao;
	}

	/**
	 * Delete all table indices, cascading to geometry indices
	 * 
	 * @return rows deleted
	 * @throws SQLException
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
