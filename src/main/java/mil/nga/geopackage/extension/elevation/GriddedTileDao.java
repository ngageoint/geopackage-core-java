package mil.nga.geopackage.extension.elevation;

import java.sql.SQLException;
import java.util.List;

import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.core.contents.Contents;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedDelete;
import com.j256.ormlite.support.ConnectionSource;

/**
 * Gridded Tile Data Access Object
 * 
 * @author osbornb
 * @since 1.2.1
 */
public class GriddedTileDao extends BaseDaoImpl<GriddedTile, String> {

	/**
	 * Constructor, required by ORMLite
	 * 
	 * @param connectionSource
	 * @param dataClass
	 * @throws SQLException
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
	 * @return gridded tile list
	 */
	public List<GriddedTile> query(String tableName) {
		List<GriddedTile> results = null;
		try {
			results = queryForEq(GriddedTile.COLUMN_TABLE_NAME, tableName);
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to query for Gridded Tile objects by Table Name: "
							+ tableName);
		}
		return results;
	}

	/**
	 * Delete by contents
	 * 
	 * @param contents
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
		DeleteBuilder<GriddedTile, String> db = deleteBuilder();

		int deleted = 0;

		try {
			db.where().eq(GriddedTile.COLUMN_TABLE_NAME, tableName);

			PreparedDelete<GriddedTile> deleteQuery = db.prepare();
			deleted = delete(deleteQuery);
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to delete Gridded Tile by Table Name: " + tableName);
		}

		return deleted;
	}

}
