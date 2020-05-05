package mil.nga.geopackage.extension.im.portrayal;

import com.j256.ormlite.support.ConnectionSource;
import mil.nga.geopackage.db.GeoPackageDao;

import java.sql.SQLException;

/**
 * @author jyutzler
 */
public class SymbolContentDao extends GeoPackageDao<SymbolContent, Long> {

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
    public SymbolContentDao(ConnectionSource connectionSource,
                            Class<SymbolContent> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }
}
