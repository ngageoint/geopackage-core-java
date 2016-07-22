package mil.nga.geopackage.extension.elevation;

import java.sql.SQLException;
import java.util.List;

import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.tiles.matrixset.TileMatrixSet;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedDelete;
import com.j256.ormlite.support.ConnectionSource;

/**
 * Gridded Coverage Data Access Object
 * 
 * @author osbornb
 * @since 1.2.1
 */
public class GriddedCoverageDao extends BaseDaoImpl<GriddedCoverage, Long> {

	/**
	 * Constructor, required by ORMLite
	 * 
	 * @param connectionSource
	 * @param dataClass
	 * @throws SQLException
	 */
	public GriddedCoverageDao(ConnectionSource connectionSource,
			Class<GriddedCoverage> dataClass) throws SQLException {
		super(connectionSource, dataClass);
	}

	/**
	 * Query by tile matrix set
	 * 
	 * @param tileMatrixSet
	 *            tile matrix set
	 * @return gridded coverage list
	 */
	public List<GriddedCoverage> query(TileMatrixSet tileMatrixSet) {
		return query(tileMatrixSet.getTableName());
	}

	/**
	 * Query by table name
	 * 
	 * @param tileMatrixSetName
	 *            tile matrix set name
	 * @return gridded coverage list
	 */
	public List<GriddedCoverage> query(String tileMatrixSetName) {
		List<GriddedCoverage> results = null;
		try {
			results = queryForEq(GriddedCoverage.COLUMN_TILE_MATRIX_SET_NAME,
					tileMatrixSetName);
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to query for Gridded Coverage objects by Tile Matrix Set Name: "
							+ tileMatrixSetName, e);
		}
		return results;
	}

	/**
	 * Delete by tile matrix set
	 * 
	 * @param tileMatrixSet
	 *            tile matrix set
	 * @return deleted count
	 */
	public int delete(TileMatrixSet tileMatrixSet) {
		return delete(tileMatrixSet.getTableName());
	}

	/**
	 * Delete by tile matrix set name
	 * 
	 * @param tileMatrixSetName
	 *            tile matrix set name
	 * @return deleted count
	 */
	public int delete(String tileMatrixSetName) {
		DeleteBuilder<GriddedCoverage, Long> db = deleteBuilder();

		int deleted = 0;

		try {
			db.where().eq(GriddedCoverage.COLUMN_TILE_MATRIX_SET_NAME,
					tileMatrixSetName);

			PreparedDelete<GriddedCoverage> deleteQuery = db.prepare();
			deleted = delete(deleteQuery);
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to delete Gridded Coverage by Tile Matrix Set Name: "
							+ tileMatrixSetName, e);
		}

		return deleted;
	}

}
