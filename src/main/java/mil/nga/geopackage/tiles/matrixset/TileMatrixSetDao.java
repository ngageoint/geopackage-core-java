package mil.nga.geopackage.tiles.matrixset;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.j256.ormlite.support.ConnectionSource;

import mil.nga.geopackage.GeoPackageCore;
import mil.nga.geopackage.contents.Contents;
import mil.nga.geopackage.db.GeoPackageCoreConnection;
import mil.nga.geopackage.db.GeoPackageDao;
import mil.nga.geopackage.srs.SpatialReferenceSystemDao;

/**
 * Tile Matrix Set Data Access Object
 * 
 * @author osbornb
 */
public class TileMatrixSetDao extends GeoPackageDao<TileMatrixSet, String> {

	/**
	 * Create the DAO
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * @return dao
	 * @since 4.0.0
	 */
	public static TileMatrixSetDao create(GeoPackageCore geoPackage) {
		return create(geoPackage.getDatabase());
	}

	/**
	 * Create the DAO
	 * 
	 * @param db
	 *            database connection
	 * @return dao
	 * @since 4.0.0
	 */
	public static TileMatrixSetDao create(GeoPackageCoreConnection db) {
		return GeoPackageDao.createDao(db, TileMatrixSet.class);
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
	public TileMatrixSetDao(ConnectionSource connectionSource,
			Class<TileMatrixSet> dataClass) throws SQLException {
		super(connectionSource, dataClass);
	}

	/**
	 * Get all the tile table names
	 * 
	 * @return tile tables
	 * @throws SQLException
	 *             upon failure
	 */
	public List<String> getTileTables() throws SQLException {

		List<String> tableNames = new ArrayList<String>();

		List<TileMatrixSet> tileMatrixSets = queryForAll();
		for (TileMatrixSet tileMatrixSet : tileMatrixSets) {
			tableNames.add(tileMatrixSet.getTableName());
		}

		return tableNames;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TileMatrixSet queryForId(String id) throws SQLException {
		TileMatrixSet tileMatrixSet = super.queryForId(id);
		if (tileMatrixSet != null) {
			updateSRS(tileMatrixSet);
		}
		return tileMatrixSet;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<TileMatrixSet> queryForEq(String fieldName, Object value)
			throws SQLException {
		List<TileMatrixSet> tileMatrixSets = super.queryForEq(fieldName, value);
		if (tileMatrixSets != null) {
			for (TileMatrixSet tileMatrixSet : tileMatrixSets) {
				updateSRS(tileMatrixSet);
			}
		}
		return tileMatrixSets;
	}

	/**
	 * Update the spatial reference system
	 * 
	 * @param tileMatrixSet
	 *            tile matrix set
	 */
	private void updateSRS(TileMatrixSet tileMatrixSet) {
		SpatialReferenceSystemDao.setExtensionValues(db,
				tileMatrixSet.getSrs());
		Contents contents = tileMatrixSet.getContents();
		if (contents != null) {
			SpatialReferenceSystemDao.setExtensionValues(db, contents.getSrs());
		}
	}

}
