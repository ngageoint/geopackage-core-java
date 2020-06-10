package mil.nga.geopackage.srs;

import java.sql.SQLException;

import com.j256.ormlite.support.ConnectionSource;

import mil.nga.geopackage.GeoPackageCore;
import mil.nga.geopackage.db.GeoPackageCoreConnection;
import mil.nga.geopackage.db.GeoPackageDao;

/**
 * SQL/MM Spatial Reference System Data Access Object
 * 
 * @author osbornb
 */
public class SpatialReferenceSystemSqlMmDao
		extends GeoPackageDao<SpatialReferenceSystemSqlMm, Integer> {

	/**
	 * Create the DAO
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * @return dao
	 * @since 4.0.0
	 */
	public static SpatialReferenceSystemSqlMmDao create(
			GeoPackageCore geoPackage) {
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
	public static SpatialReferenceSystemSqlMmDao create(
			GeoPackageCoreConnection db) {
		SpatialReferenceSystemSqlMmDao dao = GeoPackageDao.createDao(db,
				SpatialReferenceSystemSqlMm.class);
		dao.verifyExists();
		return dao;
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
	public SpatialReferenceSystemSqlMmDao(ConnectionSource connectionSource,
			Class<SpatialReferenceSystemSqlMm> dataClass) throws SQLException {
		super(connectionSource, dataClass);
	}

}
