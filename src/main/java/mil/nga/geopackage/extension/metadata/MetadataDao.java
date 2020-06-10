package mil.nga.geopackage.extension.metadata;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.support.ConnectionSource;

import mil.nga.geopackage.GeoPackageCore;
import mil.nga.geopackage.db.GeoPackageCoreConnection;
import mil.nga.geopackage.db.GeoPackageDao;
import mil.nga.geopackage.extension.metadata.reference.MetadataReferenceDao;

/**
 * Metadata Data Access Object
 * 
 * @author osbornb
 */
public class MetadataDao extends GeoPackageDao<Metadata, Long> {

	/**
	 * Create the DAO
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * @return dao
	 * @since 4.0.0
	 */
	public static MetadataDao create(GeoPackageCore geoPackage) {
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
	public static MetadataDao create(GeoPackageCoreConnection db) {
		return GeoPackageDao.createDao(db, Metadata.class);
	}

	/**
	 * Metadata Reference DAO
	 */
	private MetadataReferenceDao metadataReferenceDao;

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
	public MetadataDao(ConnectionSource connectionSource,
			Class<Metadata> dataClass) throws SQLException {
		super(connectionSource, dataClass);
	}

	/**
	 * Delete the Metadata, cascading
	 * 
	 * @param metadata
	 *            metadata
	 * @return deleted count
	 * @throws SQLException
	 *             upon failure
	 */
	public int deleteCascade(Metadata metadata) throws SQLException {
		int count = 0;

		if (metadata != null) {

			// Delete Metadata References and remove parent references
			MetadataReferenceDao dao = getMetadataReferenceDao();
			dao.deleteByMetadata(metadata.getId());
			dao.removeMetadataParent(metadata.getId());

			// Delete
			count = delete(metadata);
		}
		return count;
	}

	/**
	 * Delete the collection of Metadata, cascading
	 * 
	 * @param metadataCollection
	 *            metadata collection
	 * @return deleted count
	 * @throws SQLException
	 *             upon failure
	 */
	public int deleteCascade(Collection<Metadata> metadataCollection)
			throws SQLException {
		int count = 0;
		if (metadataCollection != null) {
			for (Metadata metadata : metadataCollection) {
				count += deleteCascade(metadata);
			}
		}
		return count;
	}

	/**
	 * Delete the Metadata matching the prepared query, cascading
	 * 
	 * @param preparedDelete
	 *            prepared delete query
	 * @return deleted count
	 * @throws SQLException
	 *             upon failure
	 */
	public int deleteCascade(PreparedQuery<Metadata> preparedDelete)
			throws SQLException {
		int count = 0;
		if (preparedDelete != null) {
			List<Metadata> metadataList = query(preparedDelete);
			count = deleteCascade(metadataList);
		}
		return count;
	}

	/**
	 * Delete a Metadata by id, cascading
	 * 
	 * @param id
	 *            id
	 * @return deleted count
	 * @throws SQLException
	 *             upon failure
	 */
	public int deleteByIdCascade(Long id) throws SQLException {
		int count = 0;
		if (id != null) {
			Metadata metadata = queryForId(id);
			if (metadata != null) {
				count = deleteCascade(metadata);
			}
		}
		return count;
	}

	/**
	 * Delete the Metadata with the provided ids, cascading
	 * 
	 * @param idCollection
	 *            id collection
	 * @return deleted count
	 * @throws SQLException
	 *             upon failure
	 */
	public int deleteIdsCascade(Collection<Long> idCollection)
			throws SQLException {
		int count = 0;
		if (idCollection != null) {
			for (Long id : idCollection) {
				count += deleteByIdCascade(id);
			}
		}
		return count;
	}

	/**
	 * Get or create a Metadata Reference DAO
	 * 
	 * @return metadata reference dao
	 */
	private MetadataReferenceDao getMetadataReferenceDao() {
		if (metadataReferenceDao == null) {
			metadataReferenceDao = MetadataReferenceDao.create(db);
		}
		return metadataReferenceDao;
	}

}
