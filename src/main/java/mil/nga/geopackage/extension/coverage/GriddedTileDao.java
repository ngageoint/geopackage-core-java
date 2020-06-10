package mil.nga.geopackage.extension.coverage;

import java.sql.SQLException;
import java.util.List;

import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedDelete;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;

import mil.nga.geopackage.GeoPackageCore;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.contents.Contents;
import mil.nga.geopackage.db.GeoPackageCoreConnection;
import mil.nga.geopackage.db.GeoPackageDao;

/**
 * Gridded Tile Data Access Object
 * 
 * @author osbornb
 * @since 1.2.1
 */
public class GriddedTileDao extends GeoPackageDao<GriddedTile, Long> {

	/**
	 * Create the DAO
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * @return dao
	 * @since 4.0.0
	 */
	public static GriddedTileDao create(GeoPackageCore geoPackage) {
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
	public static GriddedTileDao create(GeoPackageCoreConnection db) {
		return GeoPackageDao.createDao(db, GriddedTile.class);
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
	public GriddedTileDao(ConnectionSource connectionSource,
			Class<GriddedTile> dataClass) throws SQLException {
		super(connectionSource, dataClass);
	}

	/**
	 * Query by contents
	 * 
	 * @param contents
	 *            contents
	 * @return gridded tile list
	 */
	public List<GriddedTile> query(Contents contents) {
		return query(contents.getTableName());
	}

	/**
	 * Query by table name
	 * 
	 * @param tableName
	 *            table name
	 * @return gridded tile list
	 */
	public List<GriddedTile> query(String tableName) {
		List<GriddedTile> results = null;
		try {
			results = queryForEq(GriddedTile.COLUMN_TABLE_NAME, tableName);
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to query for Gridded Tile objects by Table Name: "
							+ tableName,
					e);
		}
		return results;
	}

	/**
	 * Query by table name and table id
	 * 
	 * @param tableName
	 *            table name
	 * @param tileId
	 *            tile id
	 * @return gridded tile
	 */
	public GriddedTile query(String tableName, long tileId) {
		GriddedTile griddedTile = null;
		try {
			QueryBuilder<GriddedTile, Long> qb = queryBuilder();
			qb.where().eq(GriddedTile.COLUMN_TABLE_NAME, tableName).and()
					.eq(GriddedTile.COLUMN_TABLE_ID, tileId);
			PreparedQuery<GriddedTile> query = qb.prepare();
			griddedTile = queryForFirst(query);
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to query for Gridded Tile objects by Table Name: "
							+ tableName + ", Tile Id: " + tileId,
					e);
		}
		return griddedTile;
	}

	/**
	 * Delete by contents
	 * 
	 * @param contents
	 *            contents
	 * @return deleted count
	 */
	public int delete(Contents contents) {
		return delete(contents.getTableName());
	}

	/**
	 * Delete by table name
	 * 
	 * @param tableName
	 *            table name
	 * @return deleted count
	 */
	public int delete(String tableName) {
		DeleteBuilder<GriddedTile, Long> db = deleteBuilder();

		int deleted = 0;

		try {
			db.where().eq(GriddedTile.COLUMN_TABLE_NAME, tableName);

			PreparedDelete<GriddedTile> deleteQuery = db.prepare();
			deleted = delete(deleteQuery);
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to delete Gridded Tile by Table Name: " + tableName,
					e);
		}

		return deleted;
	}

}
