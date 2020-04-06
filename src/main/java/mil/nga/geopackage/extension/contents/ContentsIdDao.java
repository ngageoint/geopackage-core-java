package mil.nga.geopackage.extension.contents;

import java.sql.SQLException;

import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedDelete;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;

import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.db.GeoPackageDao;

/**
 * Contents Id Data Access Object
 * 
 * @author osbornb
 * @since 3.2.0
 */
public class ContentsIdDao extends GeoPackageDao<ContentsId, Long> {

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
	public ContentsIdDao(ConnectionSource connectionSource,
			Class<ContentsId> dataClass) throws SQLException {
		super(connectionSource, dataClass);
	}

	/**
	 * Query by table name
	 * 
	 * @param tableName
	 *            table name
	 * @return contents id
	 */
	public ContentsId queryForTableName(String tableName) {
		ContentsId contentsId = null;
		try {
			QueryBuilder<ContentsId, Long> qb = queryBuilder();
			qb.where().eq(ContentsId.COLUMN_TABLE_NAME, tableName);
			PreparedQuery<ContentsId> query = qb.prepare();
			contentsId = queryForFirst(query);
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to query for Contents Id by Table Name: "
							+ tableName,
					e);
		}
		return contentsId;
	}

	/**
	 * Delete by table name
	 * 
	 * @param tableName
	 *            table name
	 * @return rows deleted
	 * @throws SQLException
	 *             upon failure
	 */
	public int deleteByTableName(String tableName) throws SQLException {
		DeleteBuilder<ContentsId, Long> db = deleteBuilder();
		db.where().eq(ContentsId.COLUMN_TABLE_NAME, tableName);
		PreparedDelete<ContentsId> deleteQuery = db.prepare();
		int deleted = delete(deleteQuery);
		return deleted;
	}

}
