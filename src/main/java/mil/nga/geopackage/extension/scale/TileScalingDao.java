package mil.nga.geopackage.extension.scale;

import java.sql.SQLException;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

/**
 * Tile Scaling Data Access Object
 * 
 * @author osbornb
 * @since 2.0.2
 */
public class TileScalingDao extends BaseDaoImpl<TileScaling, String> {

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
