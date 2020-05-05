package mil.nga.geopackage.extension.nga.scale;

import java.sql.SQLException;

import com.j256.ormlite.support.ConnectionSource;

import mil.nga.geopackage.db.GeoPackageDao;

/**
 * Tile Scaling Data Access Object
 * 
 * @author osbornb
 * @since 2.0.2
 */
public class TileScalingDao extends GeoPackageDao<TileScaling, String> {

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
