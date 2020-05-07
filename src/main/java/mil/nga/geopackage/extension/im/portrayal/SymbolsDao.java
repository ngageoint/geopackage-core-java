package mil.nga.geopackage.extension.im.portrayal;

import com.j256.ormlite.support.ConnectionSource;
import mil.nga.geopackage.db.GeoPackageDao;

import java.sql.SQLException;

/**
 * @author jyutzler
 * @since 4.0.0
 */
public class SymbolsDao extends GeoPackageDao<Symbols, Long> {

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
    public SymbolsDao(ConnectionSource connectionSource,
                      Class<Symbols> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }
}
