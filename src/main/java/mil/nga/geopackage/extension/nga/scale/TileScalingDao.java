package mil.nga.geopackage.extension.nga.scale;

import java.sql.SQLException;

import com.j256.ormlite.support.ConnectionSource;

import mil.nga.geopackage.GeoPackageCore;
import mil.nga.geopackage.db.GeoPackageCoreConnection;
import mil.nga.geopackage.db.GeoPackageDao;

/**
 * Tile Scaling Data Access Object
 * 
 * @author osbornb
 * @since 2.0.2
 */
public class TileScalingDao extends GeoPackageDao<TileScaling, String> {

	/**
	 * Create the DAO
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * @return dao
	 * @since 4.0.0
	 */
	public static TileScalingDao create(GeoPackageCore geoPackage) {
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
	public static TileScalingDao create(GeoPackageCoreConnection db) {
		return GeoPackageDao.createDao(db, TileScaling.class);
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
	public TileScalingDao(ConnectionSource connectionSource,
			Class<TileScaling> dataClass) throws SQLException {
		super(connectionSource, dataClass);
	}

}
