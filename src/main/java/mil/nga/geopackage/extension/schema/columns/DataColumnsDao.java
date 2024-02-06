package mil.nga.geopackage.extension.schema.columns;

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
import mil.nga.geopackage.contents.Contents;
import mil.nga.geopackage.contents.ContentsDao;
import mil.nga.geopackage.db.GeoPackageCoreConnection;
import mil.nga.geopackage.db.GeoPackageDao;
import mil.nga.geopackage.db.TableColumnKey;
import mil.nga.geopackage.user.UserColumn;
import mil.nga.geopackage.user.UserColumns;
import mil.nga.geopackage.user.UserTable;

/**
 * Data Columns Data Access Object
 * 
 * @author osbornb
 */
public class DataColumnsDao extends GeoPackageDao<DataColumns, TableColumnKey> {

	/**
	 * Create the DAO
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * @return dao
	 * @since 4.0.0
	 */
	public static DataColumnsDao create(GeoPackageCore geoPackage) {
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
	public static DataColumnsDao create(GeoPackageCoreConnection db) {
		return GeoPackageDao.createDao(db, DataColumns.class);
	}

	/**
	 * Contents DAO
	 */
	private ContentsDao contentsDao;

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
	public DataColumnsDao(ConnectionSource connectionSource,
			Class<DataColumns> dataClass) throws SQLException {
		super(connectionSource, dataClass);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DataColumns queryForId(TableColumnKey key) throws SQLException {
		DataColumns dataColumns = null;
		if (key != null) {
			Map<String, Object> fieldValues = new HashMap<String, Object>();
			fieldValues.put(DataColumns.COLUMN_TABLE_NAME, key.getTableName());
			fieldValues.put(DataColumns.COLUMN_COLUMN_NAME,
					key.getColumnName());
			List<DataColumns> results = queryForFieldValues(fieldValues);
			if (!results.isEmpty()) {
				if (results.size() > 1) {
					throw new SQLException(
							"More than one " + DataColumns.class.getSimpleName()
									+ " returned for key. Table Name: "
									+ key.getTableName() + ", Column Name: "
									+ key.getColumnName());
				}
				dataColumns = results.get(0);
			}
		}
		return dataColumns;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TableColumnKey extractId(DataColumns data) throws SQLException {
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
	public DataColumns queryForSameId(DataColumns data) throws SQLException {
		return queryForId(data.getId());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int updateId(DataColumns data, TableColumnKey newId)
			throws SQLException {
		int count = 0;
		DataColumns readData = queryForId(data.getId());
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
	public int delete(DataColumns data) throws SQLException {
		DeleteBuilder<DataColumns, TableColumnKey> db = deleteBuilder();

		db.where().eq(DataColumns.COLUMN_TABLE_NAME, data.getTableName()).and()
				.eq(DataColumns.COLUMN_COLUMN_NAME, data.getColumnName());

		PreparedDelete<DataColumns> deleteQuery = db.prepare();
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
			DataColumns dataColumns = queryForId(id);
			if (dataColumns != null) {
				count = delete(dataColumns);
			}
		}
		return count;
	}

	/**
	 * Delete by id with table name and column name
	 * 
	 * @param tableName
	 *            table name
	 * @param columnName
	 *            column name
	 * @return number of rows deleted
	 * @throws SQLException
	 *             upon failure
	 * @since 6.6.7
	 */
	public int deleteById(String tableName, String columnName)
			throws SQLException {
		TableColumnKey id = new TableColumnKey(tableName, columnName);
		return deleteById(id);
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
	 * <p>
	 * Update using the complex key
	 */
	@Override
	public int update(DataColumns dataColumns) throws SQLException {

		UpdateBuilder<DataColumns, TableColumnKey> ub = updateBuilder();
		ub.updateColumnValue(DataColumns.COLUMN_NAME, dataColumns.getName());
		ub.updateColumnValue(DataColumns.COLUMN_TITLE, dataColumns.getTitle());
		ub.updateColumnValue(DataColumns.COLUMN_DESCRIPTION,
				dataColumns.getDescription());
		ub.updateColumnValue(DataColumns.COLUMN_MIME_TYPE,
				dataColumns.getMimeType());
		ub.updateColumnValue(DataColumns.COLUMN_CONSTRAINT_NAME,
				dataColumns.getConstraintName());

		ub.where().eq(DataColumns.COLUMN_TABLE_NAME, dataColumns.getTableName())
				.and().eq(DataColumns.COLUMN_COLUMN_NAME,
						dataColumns.getColumnName());

		PreparedUpdate<DataColumns> update = ub.prepare();
		int updated = update(update);

		return updated;
	}

	/**
	 * Query by the constraint name
	 * 
	 * @param constraintName
	 *            constraint name
	 * @return data columns
	 * @throws SQLException
	 *             upon failure
	 */
	public List<DataColumns> queryByConstraintName(String constraintName)
			throws SQLException {
		return queryForEq(DataColumns.COLUMN_CONSTRAINT_NAME, constraintName);
	}

	/**
	 * Get DataColumn by column name and table name
	 * 
	 * @param tableName
	 *            table name to query for
	 * @param columnName
	 *            column name to query for
	 * @return DataColumns
	 * @throws SQLException
	 *             upon failure
	 */
	public DataColumns getDataColumn(String tableName, String columnName)
			throws SQLException {
		TableColumnKey id = new TableColumnKey(tableName, columnName);
		return queryForId(id);
	}

	/**
	 * Query by table name
	 * 
	 * @param tableName
	 *            table name
	 * @return data columns
	 * @throws SQLException
	 *             upon failure
	 * @since 3.3.0
	 */
	public List<DataColumns> queryByTable(String tableName)
			throws SQLException {
		return queryForEq(DataColumns.COLUMN_TABLE_NAME, tableName);
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
		DeleteBuilder<DataColumns, TableColumnKey> db = deleteBuilder();
		db.where().eq(DataColumns.COLUMN_TABLE_NAME, tableName);
		PreparedDelete<DataColumns> deleteQuery = db.prepare();
		int deleted = delete(deleteQuery);
		return deleted;
	}

	/**
	 * Save the table schema
	 * 
	 * @param table
	 *            user table
	 * @throws SQLException
	 *             upon failure
	 * @since 6.6.7
	 */
	public void saveSchema(UserTable<? extends UserColumn> table)
			throws SQLException {
		saveSchema(table.getUserColumns());
	}

	/**
	 * Save the columns schema
	 * 
	 * @param columns
	 *            user columns
	 * @throws SQLException
	 *             upon failure
	 * @since 6.6.7
	 */
	public void saveSchema(UserColumns<? extends UserColumn> columns)
			throws SQLException {
		saveSchema(columns.getTableName(), columns.getColumns());
	}

	/**
	 * Save the columns schema
	 * 
	 * @param table
	 *            table name
	 * @param columns
	 *            user columns
	 * @throws SQLException
	 *             upon failure
	 * @since 6.6.7
	 */
	public void saveSchema(String table, List<? extends UserColumn> columns)
			throws SQLException {

		for (UserColumn column : columns) {
			saveSchema(table, column);
		}

	}

	/**
	 * Save the column schema
	 * 
	 * @param table
	 *            table name
	 * @param column
	 *            user column
	 * @throws SQLException
	 *             upon failure
	 * @since 6.6.7
	 */
	public void saveSchema(String table, UserColumn column)
			throws SQLException {

		String columnName = column.getName();

		DataColumns schema = column.getSchema();
		DataColumns existing = getSchema(table, columnName);

		if (schema != null) {

			schema.setColumnName(columnName);

			if (existing != null) {
				schema.setContents(existing.getContents());
				update(schema);
			} else {
				if (schema.getContents() == null) {
					ContentsDao contentsDao = getContentsDao();
					Contents contents = contentsDao.queryForId(table);
					schema.setContents(contents);
				}
				if (schema.getContents() != null) {
					create(schema);
				}
			}

		} else if (existing != null) {
			deleteById(table, columnName);
		}

	}

	/**
	 * Load the table schema
	 * 
	 * @param table
	 *            user table
	 * @throws SQLException
	 *             upon failure
	 * @since 6.6.7
	 */
	public void loadSchema(UserTable<? extends UserColumn> table)
			throws SQLException {
		loadSchema(table.getUserColumns());
	}

	/**
	 * Load the columns schema
	 * 
	 * @param columns
	 *            user columns
	 * @throws SQLException
	 *             upon failure
	 * @since 6.6.7
	 */
	public void loadSchema(UserColumns<? extends UserColumn> columns)
			throws SQLException {
		loadSchema(columns.getTableName(), columns.getColumns());
	}

	/**
	 * Load the columns schema
	 * 
	 * @param table
	 *            table name
	 * @param columns
	 *            user columns
	 * @throws SQLException
	 *             upon failure
	 * @since 6.6.7
	 */
	public void loadSchema(String table, List<? extends UserColumn> columns)
			throws SQLException {

		if (isTableExists()) {

			for (UserColumn column : columns) {

				loadSchema(table, column);

			}

		}

	}

	/**
	 * Load the column schema
	 * 
	 * @param table
	 *            table name
	 * @param column
	 *            user column
	 * @throws SQLException
	 *             upon failure
	 * @since 6.6.7
	 */
	public void loadSchema(String table, UserColumn column)
			throws SQLException {

		column.setSchema(getSchema(table, column.getName()));

	}

	/**
	 * Get the column schema
	 * 
	 * @param table
	 *            table name
	 * @param column
	 *            column name
	 * @return column schema or null
	 * @throws SQLException
	 *             upon failure
	 * @since 6.6.7
	 */
	public DataColumns getSchema(String table, String column)
			throws SQLException {

		DataColumns schema = null;

		if (isTableExists()) {
			schema = getDataColumn(table, column);
		}

		return schema;
	}

	/**
	 * Get or create a Contents DAO
	 * 
	 * @return contents dao
	 */
	private ContentsDao getContentsDao() {
		if (contentsDao == null) {
			contentsDao = ContentsDao.create(db);
		}
		return contentsDao;
	}

}
