package mil.nga.geopackage.extension.vector_tiles;

import com.j256.ormlite.support.ConnectionSource;
import mil.nga.geopackage.db.GeoPackageDao;

import java.sql.SQLException;

/**
 * @author jyutzler
 */
public class VectorTilesLayersDao extends GeoPackageDao<VectorTilesLayers, Long> {

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
