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
public class ExtTileMatrixSetDao extends GeoPackageDao<ExtTileMatrixSet, Long> {

	/**
	 * Create the DAO
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * @return dao
	 */
	public static ExtTileMatrixSetDao create(GeoPackageCore geoPackage) {
		return create(geoPackage.getDatabase());
	}

	/**
	 * Create the DAO
	 * 
	 * @param db
	 *            database connection
	 * @return dao
	 */
	public static ExtTileMatrixSetDao create(GeoPackageCoreConnection db) {
		return GeoPackageDao.createDao(db, ExtTileMatrixSet.class);
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
	public ExtTileMatrixSetDao(ConnectionSource connectionSource,
			Class<ExtTileMatrixSet> dataClass) throws SQLException {
		super(connectionSource, dataClass);
	}

}
