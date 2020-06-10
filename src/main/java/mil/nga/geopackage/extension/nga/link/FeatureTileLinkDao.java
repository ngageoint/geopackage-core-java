package mil.nga.geopackage.extension.nga.link;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedDelete;
import com.j256.ormlite.stmt.PreparedUpdate;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.support.ConnectionSource;

import mil.nga.geopackage.GeoPackageCore;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.db.GeoPackageCoreConnection;
import mil.nga.geopackage.db.GeoPackageDao;

/**
 * Feature Tile Link Data Access Object
 * 
 * @author osbornb
 * @since 1.1.5
 */
public class FeatureTileLinkDao
		extends GeoPackageDao<FeatureTileLink, FeatureTileLinkKey> {

	/**
	 * Create the DAO
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * @return dao
	 * @since 4.0.0
	 */
	public static FeatureTileLinkDao create(GeoPackageCore geoPackage) {
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
	public static FeatureTileLinkDao create(GeoPackageCoreConnection db) {
		return GeoPackageDao.createDao(db, FeatureTileLink.class);
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
	public FeatureTileLinkDao(ConnectionSource connectionSource,
			Class<FeatureTileLink> dataClass) throws SQLException {
		super(connectionSource, dataClass);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FeatureTileLink queryForId(FeatureTileLinkKey key)
			throws SQLException {
		FeatureTileLink featureTileLink = null;
		if (key != null) {
			Map<String, Object> fieldValues = new HashMap<String, Object>();
			fieldValues.put(FeatureTileLink.COLUMN_FEATURE_TABLE_NAME,
					key.getFeatureTableName());
			fieldValues.put(FeatureTileLink.COLUMN_TILE_TABLE_NAME,
					key.getTileTableName());
			List<FeatureTileLink> results = queryForFieldValues(fieldValues);
			if (!results.isEmpty()) {
				if (results.size() > 1) {
					throw new SQLException("More than one "
							+ FeatureTileLink.class.getSimpleName()
							+ " returned for key. Feature Table Name: "
							+ key.getFeatureTableName() + ", Tile Table Name: "
							+ key.getTileTableName());
				}
				featureTileLink = results.get(0);
			}
		}
		return featureTileLink;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FeatureTileLinkKey extractId(FeatureTileLink data)
			throws SQLException {
		return data.getId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean idExists(FeatureTileLinkKey id) throws SQLException {
		return queryForId(id) != null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FeatureTileLink queryForSameId(FeatureTileLink data)
			throws SQLException {
		return queryForId(data.getId());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int updateId(FeatureTileLink data, FeatureTileLinkKey newId)
			throws SQLException {
		int count = 0;
		FeatureTileLink readData = queryForId(data.getId());
		if (readData != null && newId != null) {
			readData.setId(newId);
			count = update(readData);
		}
		return count;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int delete(FeatureTileLink data) throws SQLException {
		DeleteBuilder<FeatureTileLink, FeatureTileLinkKey> db = deleteBuilder();

		db.where()
				.eq(FeatureTileLink.COLUMN_FEATURE_TABLE_NAME,
						data.getFeatureTableName())
				.and().eq(FeatureTileLink.COLUMN_TILE_TABLE_NAME,
						data.getTileTableName());

		PreparedDelete<FeatureTileLink> deleteQuery = db.prepare();
		int deleted = delete(deleteQuery);
		return deleted;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int deleteById(FeatureTileLinkKey id) throws SQLException {
		int count = 0;
		if (id != null) {
			FeatureTileLink deleteData = queryForId(id);
			if (deleteData != null) {
				count = delete(deleteData);
			}
		}
		return count;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int deleteIds(Collection<FeatureTileLinkKey> idCollection)
			throws SQLException {
		int count = 0;
		if (idCollection != null) {
			for (FeatureTileLinkKey id : idCollection) {
				count += deleteById(id);
			}
		}
		return count;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * Update using the complex key
	 */
	@Override
	public int update(FeatureTileLink data) throws SQLException {

		UpdateBuilder<FeatureTileLink, FeatureTileLinkKey> ub = updateBuilder();
		// Currently there are no non primary values to update

		ub.where()
				.eq(FeatureTileLink.COLUMN_FEATURE_TABLE_NAME,
						data.getFeatureTableName())
				.and().eq(FeatureTileLink.COLUMN_TILE_TABLE_NAME,
						data.getTileTableName());

		PreparedUpdate<FeatureTileLink> update = ub.prepare();
		int updated = update(update);

		return updated;
	}

	/**
	 * Query by feature table name
	 * 
	 * @param featureTableName
	 *            feature table name
	 * @return feature tile links
	 */
	public List<FeatureTileLink> queryForFeatureTableName(
			String featureTableName) {
		List<FeatureTileLink> results = null;
		try {
			results = queryForEq(FeatureTileLink.COLUMN_FEATURE_TABLE_NAME,
					featureTableName);
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to query for Feature Tile Link objects by Feature Table Name: "
							+ featureTableName);
		}
		return results;
	}

	/**
	 * Query by tile table name
	 * 
	 * @param tileTableName
	 *            tile table name
	 * @return feature tile links
	 */
	public List<FeatureTileLink> queryForTileTableName(String tileTableName) {
		List<FeatureTileLink> results = null;
		try {
			results = queryForEq(FeatureTileLink.COLUMN_TILE_TABLE_NAME,
					tileTableName);
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to query for Feature Tile Link objects by Tile Table Name: "
							+ tileTableName);
		}
		return results;
	}

	/**
	 * Delete by table name, either feature or tile table name
	 * 
	 * @param tableName
	 *            table name, feature or tile
	 * @return rows deleted
	 * @throws SQLException
	 *             upon failure
	 */
	public int deleteByTableName(String tableName) throws SQLException {
		DeleteBuilder<FeatureTileLink, FeatureTileLinkKey> db = deleteBuilder();

		db.where().eq(FeatureTileLink.COLUMN_FEATURE_TABLE_NAME, tableName).or()
				.eq(FeatureTileLink.COLUMN_TILE_TABLE_NAME, tableName);

		PreparedDelete<FeatureTileLink> deleteQuery = db.prepare();
		int deleted = delete(deleteQuery);
		return deleted;
	}

	/**
	 * Delete all feature tile links
	 * 
	 * @return rows deleted
	 * @throws SQLException
	 *             upon failure
	 * @since 1.1.5
	 */
	public int deleteAll() throws SQLException {

		int count = 0;

		if (isTableExists()) {
			DeleteBuilder<FeatureTileLink, FeatureTileLinkKey> db = deleteBuilder();
			PreparedDelete<FeatureTileLink> deleteQuery = db.prepare();
			count = delete(deleteQuery);
		}

		return count;
	}

}
