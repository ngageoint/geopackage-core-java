package mil.nga.geopackage.features.columns;

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
import mil.nga.geopackage.db.GeoPackageCoreConnection;
import mil.nga.geopackage.db.GeoPackageDao;
import mil.nga.geopackage.db.TableColumnKey;

/**
 * SQL/MM Geometry Columns Data Access Object
 * 
 * @author osbornb
 */
public class GeometryColumnsSqlMmDao
		extends GeoPackageDao<GeometryColumnsSqlMm, TableColumnKey> {

	/**
	 * Create the DAO
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * @return dao
	 * @since 4.0.0
	 */
	public static GeometryColumnsSqlMmDao create(GeoPackageCore geoPackage) {
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
	public static GeometryColumnsSqlMmDao create(GeoPackageCoreConnection db) {
		GeometryColumnsSqlMmDao dao = GeoPackageDao.createDao(db,
				GeometryColumnsSqlMm.class);
		dao.verifyExists();
		return dao;
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
	public GeometryColumnsSqlMmDao(ConnectionSource connectionSource,
			Class<GeometryColumnsSqlMm> dataClass) throws SQLException {
		super(connectionSource, dataClass);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public GeometryColumnsSqlMm queryForId(TableColumnKey key)
			throws SQLException {
		GeometryColumnsSqlMm geometryColumns = null;
		if (key != null) {
			Map<String, Object> fieldValues = new HashMap<String, Object>();
			fieldValues.put(GeometryColumnsSqlMm.COLUMN_TABLE_NAME,
					key.getTableName());
			fieldValues.put(GeometryColumnsSqlMm.COLUMN_COLUMN_NAME,
					key.getColumnName());
			List<GeometryColumnsSqlMm> results = queryForFieldValues(
					fieldValues);
			if (!results.isEmpty()) {
				if (results.size() > 1) {
					throw new SQLException("More than one "
							+ GeometryColumnsSqlMm.class.getSimpleName()
							+ " returned for key. Table Name: "
							+ key.getTableName() + ", Column Name: "
							+ key.getColumnName());
				}
				geometryColumns = results.get(0);
			}
		}
		return geometryColumns;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TableColumnKey extractId(GeometryColumnsSqlMm data)
			throws SQLException {
		return data.getId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean idExists(TableColumnKey id) throws SQLException {
		return queryForId(id) != null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public GeometryColumnsSqlMm queryForSameId(GeometryColumnsSqlMm data)
			throws SQLException {
		return queryForId(data.getId());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int updateId(GeometryColumnsSqlMm data, TableColumnKey newId)
			throws SQLException {
		int count = 0;
		GeometryColumnsSqlMm readData = queryForId(data.getId());
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
	public int delete(GeometryColumnsSqlMm data) throws SQLException {
		DeleteBuilder<GeometryColumnsSqlMm, TableColumnKey> db = deleteBuilder();

		db.where()
				.eq(GeometryColumnsSqlMm.COLUMN_TABLE_NAME, data.getTableName())
				.and().eq(GeometryColumnsSqlMm.COLUMN_COLUMN_NAME,
						data.getColumnName());

		PreparedDelete<GeometryColumnsSqlMm> deleteQuery = db.prepare();
		int deleted = delete(deleteQuery);
		return deleted;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int deleteById(TableColumnKey id) throws SQLException {
		int count = 0;
		if (id != null) {
			GeometryColumnsSqlMm geometryColumns = queryForId(id);
			if (geometryColumns != null) {
				count = delete(geometryColumns);
			}
		}
		return count;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int deleteIds(Collection<TableColumnKey> idCollection)
			throws SQLException {
		int count = 0;
		if (idCollection != null) {
			for (TableColumnKey id : idCollection) {
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
	public int update(GeometryColumnsSqlMm geometryColumns)
			throws SQLException {

		UpdateBuilder<GeometryColumnsSqlMm, TableColumnKey> ub = updateBuilder();
		ub.updateColumnValue(GeometryColumnsSqlMm.COLUMN_GEOMETRY_TYPE_NAME,
				geometryColumns.getGeometryTypeName());
		ub.updateColumnValue(GeometryColumnsSqlMm.COLUMN_SRS_ID,
				geometryColumns.getSrsId());
		// Don't update srs name since it is in another table

		ub.where()
				.eq(GeometryColumnsSqlMm.COLUMN_TABLE_NAME,
						geometryColumns.getTableName())
				.and().eq(GeometryColumnsSqlMm.COLUMN_COLUMN_NAME,
						geometryColumns.getColumnName());

		PreparedUpdate<GeometryColumnsSqlMm> update = ub.prepare();
		int updated = update(update);

		return updated;
	}

}
