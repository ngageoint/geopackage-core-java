package mil.nga.geopackage.srs;

import java.sql.SQLException;

import com.j256.ormlite.support.ConnectionSource;

import mil.nga.geopackage.GeoPackageCore;
import mil.nga.geopackage.db.GeoPackageDao;

/**
 * SF/SQL Spatial Reference System Data Access Object
 * 
 * @author osbornb
 */
public class SpatialReferenceSystemSfSqlDao
		extends GeoPackageDao<SpatialReferenceSystemSfSql, Integer> {

	/**
	 * Get a DAO
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * @return DAO
	 * @since 4.0.0
	 */
	public static SpatialReferenceSystemSfSqlDao getDao(
			GeoPackageCore geoPackage) {
		SpatialReferenceSystemSfSqlDao dao = geoPackage
				.createDao(SpatialReferenceSystemSfSql.class);
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
