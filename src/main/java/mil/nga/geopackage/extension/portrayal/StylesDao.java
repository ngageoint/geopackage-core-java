package mil.nga.geopackage.extension.portrayal;

import com.j256.ormlite.support.ConnectionSource;
import mil.nga.geopackage.db.GeoPackageDao;

import java.sql.SQLException;

/**
 * @author jyutzler
 */
public class StylesDao extends GeoPackageDao<Styles, Long> {

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
    public StylesDao(ConnectionSource connectionSource,
                     Class<Styles> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }
}
