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
public class ExtTileMatrixDao extends GeoPackageDao<ExtTileMatrix, Long> {

	/**
	 * Create the DAO
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * @return dao
	 */
	public static ExtTileMatrixDao create(GeoPackageCore geoPackage) {
		return create(geoPackage.getDatabase());
	}

	/**
	 * Create the DAO
	 * 
	 * @param db
	 *            database connection
	 * @return dao
	 */
	public static ExtTileMatrixDao create(GeoPackageCoreConnection db) {
		return GeoPackageDao.createDao(db, ExtTileMatrix.class);
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
	public ExtTileMatrixDao(ConnectionSource connectionSource,
			Class<ExtTileMatrix> dataClass) throws SQLException {
		super(connectionSource, dataClass);
	}

}
