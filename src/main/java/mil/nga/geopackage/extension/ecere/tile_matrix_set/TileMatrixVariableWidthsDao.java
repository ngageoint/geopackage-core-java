package mil.nga.geopackage.extension.ecere.tile_matrix_set;

import java.sql.SQLException;

import com.j256.ormlite.support.ConnectionSource;

import mil.nga.geopackage.GeoPackageCore;
import mil.nga.geopackage.db.GeoPackageCoreConnection;
import mil.nga.geopackage.db.GeoPackageDao;

/**
 * @author jyutzler
 * @since 4.0.0
 */
public class TileMatrixVariableWidthsDao
		extends GeoPackageDao<TileMatrixVariableWidths, Long> {

	/**
	 * Create the DAO
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * @return dao
	 */
	public static TileMatrixVariableWidthsDao create(
			GeoPackageCore geoPackage) {
		return create(geoPackage.getDatabase());
	}

	/**
	 * Create the DAO
	 * 
	 * @param db
	 *            database connection
	 * @return dao
	 */
	public static TileMatrixVariableWidthsDao create(
			GeoPackageCoreConnection db) {
		return GeoPackageDao.createDao(db, TileMatrixVariableWidths.class);
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
	public TileMatrixVariableWidthsDao(ConnectionSource connectionSource,
			Class<TileMatrixVariableWidths> dataClass) throws SQLException {
		super(connectionSource, dataClass);
	}

}
