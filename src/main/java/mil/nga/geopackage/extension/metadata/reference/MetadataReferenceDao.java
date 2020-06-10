package mil.nga.geopackage.extension.metadata.reference;

import java.sql.SQLException;
import java.util.List;

import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedDelete;
import com.j256.ormlite.stmt.PreparedUpdate;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.support.ConnectionSource;

import mil.nga.geopackage.GeoPackageCore;
import mil.nga.geopackage.db.GeoPackageCoreConnection;
import mil.nga.geopackage.db.GeoPackageDao;

/**
 * Metadata Reference Data Access Object
 * 
 * @author osbornb
 */
public class MetadataReferenceDao
		extends GeoPackageDao<MetadataReference, Void> {

	/**
	 * Create the DAO
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * @return dao
	 * @since 4.0.0
	 */
	public static MetadataReferenceDao create(GeoPackageCore geoPackage) {
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
	public static MetadataReferenceDao create(GeoPackageCoreConnection db) {
		return GeoPackageDao.createDao(db, MetadataReference.class);
	}

	/**
	 * Constructor, required by ORMLite
	 * 
	 * @param connectionSource
	 *            connection
	 * @param dataClass
	 *            data class
	 * @throws SQLException
	 *             upon failure
	 */
	public MetadataReferenceDao(ConnectionSource connectionSource,
			Class<MetadataReference> dataClass) throws SQLException {
		super(connectionSource, dataClass);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * Update using the foreign key columns
	 */
	@Override
	public int update(MetadataReference metadataReference) throws SQLException {

		UpdateBuilder<MetadataReference, Void> ub = updateBuilder();
		ub.updateColumnValue(MetadataReference.COLUMN_REFERENCE_SCOPE,
				metadataReference.getReferenceScope().getValue());
		ub.updateColumnValue(MetadataReference.COLUMN_TABLE_NAME,
				metadataReference.getTableName());
		ub.updateColumnValue(MetadataReference.COLUMN_COLUMN_NAME,
				metadataReference.getColumnName());
		ub.updateColumnValue(MetadataReference.COLUMN_ROW_ID_VALUE,
				metadataReference.getRowIdValue());
		ub.updateColumnValue(MetadataReference.COLUMN_TIMESTAMP,
				metadataReference.getTimestamp());

		setFkWhere(ub.where(), metadataReference.getFileId(),
				metadataReference.getParentId());

		PreparedUpdate<MetadataReference> update = ub.prepare();
		int updated = update(update);

		return updated;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * Delete using the foreign key columns
	 */
	@Override
	public int delete(MetadataReference metadataReference) throws SQLException {

		DeleteBuilder<MetadataReference, Void> db = deleteBuilder();

		setFkWhere(db.where(), metadataReference.getFileId(),
				metadataReference.getParentId());

		int deleted = db.delete();

		return deleted;
	}

	/**
	 * Delete metadata references with foreign keys to the metadata file id
	 * 
	 * @param fileId
	 *            file id
	 * @return deleted count
	 * @throws SQLException
	 *             upon failure
	 */
	public int deleteByMetadata(long fileId) throws SQLException {

		DeleteBuilder<MetadataReference, Void> db = deleteBuilder();
		db.where().eq(MetadataReference.COLUMN_FILE_ID, fileId);

		int deleted = db.delete();

		return deleted;
	}

	/**
	 * Remove metadata references (by updating the field to null) with foreign
	 * keys to the metadata parent id
	 * 
	 * @param parentId
	 *            parent id
	 * @return updated count
	 * @throws SQLException
	 *             upon failure
	 */
	public int removeMetadataParent(long parentId) throws SQLException {

		UpdateBuilder<MetadataReference, Void> ub = updateBuilder();
		ub.updateColumnValue(MetadataReference.COLUMN_PARENT_ID, null);

		ub.where().eq(MetadataReference.COLUMN_PARENT_ID, parentId);

		PreparedUpdate<MetadataReference> update = ub.prepare();
		int updated = update(update);

		return updated;
	}

	/**
	 * Query by the metadata ids
	 * 
	 * @param fileId
	 *            file id
	 * @param parentId
	 *            parent id
	 * @return metadata references
	 * @throws SQLException
	 *             upon failure
	 */
	public List<MetadataReference> queryByMetadata(long fileId, Long parentId)
			throws SQLException {

		QueryBuilder<MetadataReference, Void> qb = queryBuilder();
		setFkWhere(qb.where(), fileId, parentId);
		List<MetadataReference> metadataReferences = qb.query();

		return metadataReferences;
	}

	/**
	 * Query by the metadata ids
	 * 
	 * @param fileId
	 *            file id
	 * @return metadata references
	 * @throws SQLException
	 *             upon failure
	 */
	public List<MetadataReference> queryByMetadata(long fileId)
			throws SQLException {

		QueryBuilder<MetadataReference, Void> qb = queryBuilder();
		qb.where().eq(MetadataReference.COLUMN_FILE_ID, fileId);
		List<MetadataReference> metadataReferences = qb.query();

		return metadataReferences;
	}

	/**
	 * Query by the metadata parent ids
	 * 
	 * @param parentId
	 *            parent id
	 * @return metadata references
	 * @throws SQLException
	 *             upon failure
	 */
	public List<MetadataReference> queryByMetadataParent(long parentId)
			throws SQLException {

		QueryBuilder<MetadataReference, Void> qb = queryBuilder();
		qb.where().eq(MetadataReference.COLUMN_PARENT_ID, parentId);
		List<MetadataReference> metadataReferences = qb.query();

		return metadataReferences;
	}

	/**
	 * Query by table name
	 * 
	 * @param tableName
	 *            table name
	 * @return metadata references
	 * @throws SQLException
	 *             upon failure
	 * @since 3.3.0
	 */
	public List<MetadataReference> queryByTable(String tableName)
			throws SQLException {
		return queryForEq(MetadataReference.COLUMN_TABLE_NAME, tableName);
	}

	/**
	 * Set the foreign key column criteria in the where clause
	 * 
	 * @param where
	 *            where clause
	 * @param fileId
	 *            file id
	 * @param parentId
	 *            parent id
	 * @throws SQLException
	 */
	private void setFkWhere(Where<MetadataReference, Void> where, long fileId,
			Long parentId) throws SQLException {

		where.eq(MetadataReference.COLUMN_FILE_ID, fileId);
		if (parentId == null) {
			where.and().isNull(MetadataReference.COLUMN_PARENT_ID);
		} else {
			where.and().eq(MetadataReference.COLUMN_PARENT_ID, parentId);
		}

	}

	/**
	 * Delete by table name
	 * 
	 * @param tableName
	 *            table name
	 * @return rows deleted
	 * @throws SQLException
	 *             upon failure
	 * @since 3.2.0
	 */
	public int deleteByTableName(String tableName) throws SQLException {
		DeleteBuilder<MetadataReference, Void> db = deleteBuilder();
		db.where().eq(MetadataReference.COLUMN_TABLE_NAME, tableName);
		PreparedDelete<MetadataReference> deleteQuery = db.prepare();
		int deleted = delete(deleteQuery);
		return deleted;
	}

}
