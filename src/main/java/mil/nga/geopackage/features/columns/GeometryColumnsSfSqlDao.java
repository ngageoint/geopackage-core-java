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
 * SF/SQL Geometry Columns Data Access Object
 * 
 * @author osbornb
 */
public class GeometryColumnsSfSqlDao
		extends GeoPackageDao<GeometryColumnsSfSql, TableColumnKey> {

	/**
	 * Create the DAO
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * @return dao
	 * @since 4.0.0
	 */
	public static GeometryColumnsSfSqlDao create(GeoPackageCore geoPackage) {
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
	public static GeometryColumnsSfSqlDao create(GeoPackageCoreConnection db) {
		GeometryColumnsSfSqlDao dao = GeoPackageDao.createDao(db,
				GeometryColumnsSfSql.class);
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
	public GeometryColumnsSfSqlDao(ConnectionSource connectionSource,
			Class<GeometryColumnsSfSql> dataClass) throws SQLException {
		super(connectionSource, dataClass);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public GeometryColumnsSfSql queryForId(TableColumnKey key)
			throws SQLException {
		GeometryColumnsSfSql geometryColumns = null;
		if (key != null) {
			Map<String, Object> fieldValues = new HashMap<String, Object>();
			fieldValues.put(GeometryColumnsSfSql.COLUMN_F_TABLE_NAME,
					key.getTableName());
			fieldValues.put(GeometryColumnsSfSql.COLUMN_F_GEOMETRY_COLUMN,
					key.getColumnName());
			List<GeometryColumnsSfSql> results = queryForFieldValues(
					fieldValues);
			if (!results.isEmpty()) {
				if (results.size() > 1) {
					throw new SQLException("More than one "
							+ GeometryColumnsSfSql.class.getSimpleName()
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
	public TableColumnKey extractId(GeometryColumnsSfSql data)
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
	public GeometryColumnsSfSql queryForSameId(GeometryColumnsSfSql data)
			throws SQLException {
		return queryForId(data.getId());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int updateId(GeometryColumnsSfSql data, TableColumnKey newId)
			throws SQLException {
		int count = 0;
		GeometryColumnsSfSql readData = queryForId(data.getId());
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
	public int delete(GeometryColumnsSfSql data) throws SQLException {
		DeleteBuilder<GeometryColumnsSfSql, TableColumnKey> db = deleteBuilder();

		db.where()
				.eq(GeometryColumnsSfSql.COLUMN_F_TABLE_NAME,
						data.getFTableName())
				.and().eq(GeometryColumnsSfSql.COLUMN_F_GEOMETRY_COLUMN,
						data.getFGeometryColumn());

		PreparedDelete<GeometryColumnsSfSql> deleteQuery = db.prepare();
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
			GeometryColumnsSfSql geometryColumns = queryForId(id);
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
	public int update(GeometryColumnsSfSql geometryColumns)
			throws SQLException {

		UpdateBuilder<GeometryColumnsSfSql, TableColumnKey> ub = updateBuilder();
		ub.updateColumnValue(GeometryColumnsSfSql.COLUMN_GEOMETRY_TYPE,
				geometryColumns.getGeometryTypeCode());
		ub.updateColumnValue(GeometryColumnsSfSql.COLUMN_COORD_DIMENSION,
				geometryColumns.getCoordDimension());
		ub.updateColumnValue(GeometryColumnsSfSql.COLUMN_SRID,
				geometryColumns.getSrid());

		ub.where()
				.eq(GeometryColumnsSfSql.COLUMN_F_TABLE_NAME,
						geometryColumns.getFTableName())
				.and().eq(GeometryColumnsSfSql.COLUMN_F_GEOMETRY_COLUMN,
						geometryColumns.getFGeometryColumn());

		PreparedUpdate<GeometryColumnsSfSql> update = ub.prepare();
		int updated = update(update);

		return updated;
	}

}
