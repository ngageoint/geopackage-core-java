package mil.nga.geopackage.srs;

import java.sql.SQLException;

import com.j256.ormlite.support.ConnectionSource;

import mil.nga.geopackage.GeoPackageCore;
import mil.nga.geopackage.db.GeoPackageCoreConnection;
import mil.nga.geopackage.db.GeoPackageDao;

/**
 * SF/SQL Spatial Reference System Data Access Object
 * 
 * @author osbornb
 */
public class SpatialReferenceSystemSfSqlDao
		extends GeoPackageDao<SpatialReferenceSystemSfSql, Integer> {

	/**
	 * Create the DAO
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * @return dao
	 * @since 4.0.0
	 */
	public static SpatialReferenceSystemSfSqlDao create(
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
	public static SpatialReferenceSystemSfSqlDao create(
			GeoPackageCoreConnection db) {
		SpatialReferenceSystemSfSqlDao dao = GeoPackageDao.createDao(db,
				SpatialReferenceSystemSfSql.class);
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
	public SpatialReferenceSystemSfSqlDao(ConnectionSource connectionSource,
			Class<SpatialReferenceSystemSfSql> dataClass) throws SQLException {
		super(connectionSource, dataClass);
	}

}
