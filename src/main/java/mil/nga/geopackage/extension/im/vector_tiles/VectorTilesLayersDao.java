package mil.nga.geopackage.extension.im.vector_tiles;

import java.sql.SQLException;

import com.j256.ormlite.support.ConnectionSource;

import mil.nga.geopackage.GeoPackageCore;
import mil.nga.geopackage.db.GeoPackageCoreConnection;
import mil.nga.geopackage.db.GeoPackageDao;

/**
 * @author jyutzler
 * @since 4.0.0
 */
public class VectorTilesLayersDao
		extends GeoPackageDao<VectorTilesLayers, Long> {

	/**
	 * Create the DAO
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * @return dao
	 */
	public static VectorTilesLayersDao create(GeoPackageCore geoPackage) {
		return create(geoPackage.getDatabase());
	}

	/**
	 * Create the DAO
	 * 
	 * @param db
	 *            database connection
	 * @return dao
	 */
	public static VectorTilesLayersDao create(GeoPackageCoreConnection db) {
		return GeoPackageDao.createDao(db, VectorTilesLayers.class);
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
	public VectorTilesLayersDao(ConnectionSource connectionSource,
			Class<VectorTilesLayers> dataClass) throws SQLException {
		super(connectionSource, dataClass);
	}

}
