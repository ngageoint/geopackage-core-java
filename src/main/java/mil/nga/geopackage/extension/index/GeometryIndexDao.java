package mil.nga.geopackage.extension.index;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mil.nga.geopackage.GeoPackageException;
import mil.nga.sf.GeometryEnvelope;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedDelete;
import com.j256.ormlite.stmt.PreparedUpdate;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.support.ConnectionSource;

/**
 * Geometry Index Data Access Object
 * 
 * @author osbornb
 * @since 1.1.0
 */
public class GeometryIndexDao extends
		BaseDaoImpl<GeometryIndex, GeometryIndexKey> {

	/**
	 * Constructor, required by ORMLite
	 * 
	 * @param connectionSource
	 * @param dataClass
	 * @throws SQLException
	 */
	public GeometryIndexDao(ConnectionSource connectionSource,
			Class<GeometryIndex> dataClass) throws SQLException {
		super(connectionSource, dataClass);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public GeometryIndex queryForId(GeometryIndexKey key) throws SQLException {
		GeometryIndex geometryIndex = null;
		if (key != null) {
			Map<String, Object> fieldValues = new HashMap<String, Object>();
			fieldValues
					.put(GeometryIndex.COLUMN_TABLE_NAME, key.getTableName());
			fieldValues.put(GeometryIndex.COLUMN_GEOM_ID, key.getGeomId());
			List<GeometryIndex> results = queryForFieldValues(fieldValues);
			if (!results.isEmpty()) {
				if (results.size() > 1) {
					throw new SQLException("More than one "
							+ GeometryIndex.class.getSimpleName()
							+ " returned for key. Table Name: "
							+ key.getTableName() + ", Geom Id: "
							+ key.getGeomId());
				}
				geometryIndex = results.get(0);
			}
		}
		return geometryIndex;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public GeometryIndexKey extractId(GeometryIndex data) throws SQLException {
		return data.getId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean idExists(GeometryIndexKey id) throws SQLException {
		return queryForId(id) != null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public GeometryIndex queryForSameId(GeometryIndex data) throws SQLException {
		return queryForId(data.getId());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int updateId(GeometryIndex data, GeometryIndexKey newId)
			throws SQLException {
		int count = 0;
		GeometryIndex readData = queryForId(data.getId());
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
	public int delete(GeometryIndex data) throws SQLException {
		DeleteBuilder<GeometryIndex, GeometryIndexKey> db = deleteBuilder();

		db.where().eq(GeometryIndex.COLUMN_TABLE_NAME, data.getTableName())
				.and().eq(GeometryIndex.COLUMN_GEOM_ID, data.getGeomId());

		PreparedDelete<GeometryIndex> deleteQuery = db.prepare();
		int deleted = delete(deleteQuery);
		return deleted;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int deleteById(GeometryIndexKey id) throws SQLException {
		int count = 0;
		if (id != null) {
			GeometryIndex deleteData = queryForId(id);
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
	public int deleteIds(Collection<GeometryIndexKey> idCollection)
			throws SQLException {
		int count = 0;
		if (idCollection != null) {
			for (GeometryIndexKey id : idCollection) {
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
	public int update(GeometryIndex geometryIndex) throws SQLException {

		UpdateBuilder<GeometryIndex, GeometryIndexKey> ub = updateBuilder();
		ub.updateColumnValue(GeometryIndex.COLUMN_MIN_X,
				geometryIndex.getMinX());
		ub.updateColumnValue(GeometryIndex.COLUMN_MAX_X,
				geometryIndex.getMaxX());
		ub.updateColumnValue(GeometryIndex.COLUMN_MIN_Y,
				geometryIndex.getMinY());
		ub.updateColumnValue(GeometryIndex.COLUMN_MAX_Y,
				geometryIndex.getMaxY());
		ub.updateColumnValue(GeometryIndex.COLUMN_MIN_Z,
				geometryIndex.getMinZ());
		ub.updateColumnValue(GeometryIndex.COLUMN_MAX_Z,
				geometryIndex.getMaxZ());
		ub.updateColumnValue(GeometryIndex.COLUMN_MIN_M,
				geometryIndex.getMinM());
		ub.updateColumnValue(GeometryIndex.COLUMN_MAX_M,
				geometryIndex.getMaxM());

		ub.where()
				.eq(GeometryIndex.COLUMN_TABLE_NAME,
						geometryIndex.getTableName()).and()
				.eq(GeometryIndex.COLUMN_GEOM_ID, geometryIndex.getGeomId());

		PreparedUpdate<GeometryIndex> update = ub.prepare();
		int updated = update(update);

		return updated;
	}

	/**
	 * Query by table name
	 * 
	 * @param tableName
	 *            table name
	 * @return geometry indices
	 */
	public List<GeometryIndex> queryForTableName(String tableName) {
		List<GeometryIndex> results = null;
		try {
			results = queryForEq(GeometryIndex.COLUMN_TABLE_NAME, tableName);
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to query for Geometry Index objects by Table Name: "
							+ tableName);
		}
		return results;
	}

	/**
	 * Populate a new geometry index from an envelope
	 *
	 * @param tableIndex
	 *            table index
	 * @param geomId
	 *            geometry id
	 * @param envelope
	 *            geometry envelope
	 * @return geometry index
	 */
	public GeometryIndex populate(TableIndex tableIndex, long geomId,
			GeometryEnvelope envelope) {

		GeometryIndex geometryIndex = new GeometryIndex();
		geometryIndex.setTableIndex(tableIndex);
		geometryIndex.setGeomId(geomId);
		geometryIndex.setMinX(envelope.getMinX());
		geometryIndex.setMaxX(envelope.getMaxX());
		geometryIndex.setMinY(envelope.getMinY());
		geometryIndex.setMaxY(envelope.getMaxY());
		if (envelope.hasZ()) {
			geometryIndex.setMinZ(envelope.getMinZ());
			geometryIndex.setMaxZ(envelope.getMaxZ());
		}
		if (envelope.hasM()) {
			geometryIndex.setMinM(envelope.getMinM());
			geometryIndex.setMaxM(envelope.getMaxM());
		}
		return geometryIndex;
	}

	/**
	 * Delete all geometry indices
	 * 
	 * @return rows deleted
	 * @throws SQLException
	 * @since 1.1.5
	 */
	public int deleteAll() throws SQLException {

		int count = 0;

		if (isTableExists()) {
			DeleteBuilder<GeometryIndex, GeometryIndexKey> db = deleteBuilder();
			PreparedDelete<GeometryIndex> deleteQuery = db.prepare();
			count = delete(deleteQuery);
		}

		return count;
	}

}
