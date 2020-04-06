package mil.nga.geopackage.core.srs;

import java.sql.SQLException;

import com.j256.ormlite.support.ConnectionSource;

import mil.nga.geopackage.db.GeoPackageDao;

/**
 * SQL/MM Spatial Reference System Data Access Object
 * 
 * @author osbornb
 */
public class SpatialReferenceSystemSqlMmDao
		extends GeoPackageDao<SpatialReferenceSystemSqlMm, Integer> {

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
