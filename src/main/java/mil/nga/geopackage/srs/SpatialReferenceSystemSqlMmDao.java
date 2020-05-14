package mil.nga.geopackage.srs;

import java.sql.SQLException;

import com.j256.ormlite.support.ConnectionSource;

import mil.nga.geopackage.GeoPackageCore;
import mil.nga.geopackage.db.GeoPackageDao;

/**
 * SQL/MM Spatial Reference System Data Access Object
 * 
 * @author osbornb
 */
public class SpatialReferenceSystemSqlMmDao
		extends GeoPackageDao<SpatialReferenceSystemSqlMm, Integer> {

	/**
	 * Get a DAO
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * @return DAO
	 * @since 4.0.0
	 */
	public static SpatialReferenceSystemSqlMmDao getDao(
			GeoPackageCore geoPackage) {
		SpatialReferenceSystemSqlMmDao dao = geoPackage
				.createDao(SpatialReferenceSystemSqlMm.class);
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
