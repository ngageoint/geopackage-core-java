package mil.nga.geopackage.core.srs;

import java.sql.SQLException;

import com.j256.ormlite.support.ConnectionSource;

import mil.nga.geopackage.db.GeoPackageDao;

/**
 * SF/SQL Spatial Reference System Data Access Object
 * 
 * @author osbornb
 */
public class SpatialReferenceSystemSfSqlDao
		extends GeoPackageDao<SpatialReferenceSystemSfSql, Integer> {

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
