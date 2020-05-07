package mil.nga.geopackage.extension.im.portrayal;

import com.j256.ormlite.support.ConnectionSource;
import mil.nga.geopackage.db.GeoPackageDao;

import java.sql.SQLException;

/**
 * @author jyutzler
 * @since 4.0.0
 */
public class StylesheetsDao extends GeoPackageDao<Stylesheets, Long> {

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
    public StylesheetsDao(ConnectionSource connectionSource,
                          Class<Stylesheets> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }
}
