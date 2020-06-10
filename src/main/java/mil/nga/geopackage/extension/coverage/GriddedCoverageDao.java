package mil.nga.geopackage.extension.coverage;

import java.sql.SQLException;

import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedDelete;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;

import mil.nga.geopackage.GeoPackageCore;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.db.GeoPackageCoreConnection;
import mil.nga.geopackage.db.GeoPackageDao;
import mil.nga.geopackage.tiles.matrixset.TileMatrixSet;

/**
 * Gridded Coverage Data Access Object
 * 
 * @author osbornb
 * @since 1.2.1
 */
public class GriddedCoverageDao extends GeoPackageDao<GriddedCoverage, Long> {

	/**
	 * Create the DAO
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * @return dao
	 * @since 4.0.0
	 */
	public static GriddedCoverageDao create(GeoPackageCore geoPackage) {
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
	public static GriddedCoverageDao create(GeoPackageCoreConnection db) {
		return GeoPackageDao.createDao(db, GriddedCoverage.class);
	}

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
	public GriddedCoverageDao(ConnectionSource connectionSource,
			Class<GriddedCoverage> dataClass) throws SQLException {
		super(connectionSource, dataClass);
	}

	/**
	 * Query by tile matrix set
	 * 
	 * @param tileMatrixSet
	 *            tile matrix set
	 * @return gridded coverage
	 */
	public GriddedCoverage query(TileMatrixSet tileMatrixSet) {
		return query(tileMatrixSet.getTableName());
	}

	/**
	 * Query by table name
	 * 
	 * @param tileMatrixSetName
	 *            tile matrix set name
	 * @return gridded coverage
	 */
	public GriddedCoverage query(String tileMatrixSetName) {
		GriddedCoverage griddedCoverage = null;
		try {
			QueryBuilder<GriddedCoverage, Long> qb = queryBuilder();
			qb.where().eq(GriddedCoverage.COLUMN_TILE_MATRIX_SET_NAME,
					tileMatrixSetName);
			PreparedQuery<GriddedCoverage> query = qb.prepare();
			griddedCoverage = queryForFirst(query);
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to query for Gridded Coverage by Tile Matrix Set Name: "
							+ tileMatrixSetName,
					e);
		}
		return griddedCoverage;
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
	 * Delete by table name
	 * 
	 * @param tableName
	 *            table name
	 * @return deleted count
	 */
	public int delete(String tableName) {
		DeleteBuilder<GriddedCoverage, Long> db = deleteBuilder();

		int deleted = 0;

		try {
			db.where().eq(GriddedCoverage.COLUMN_TILE_MATRIX_SET_NAME,
					tableName);

			PreparedDelete<GriddedCoverage> deleteQuery = db.prepare();
			deleted = delete(deleteQuery);
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to delete Gridded Coverage by Table Name: "
							+ tableName,
					e);
		}

		return deleted;
	}

}
