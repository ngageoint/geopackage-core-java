package mil.nga.geopackage.user;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import mil.nga.geopackage.BoundingBox;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.db.GeoPackageCoreConnection;
import mil.nga.geopackage.projection.Projection;
import mil.nga.geopackage.projection.ProjectionConstants;
import mil.nga.geopackage.projection.ProjectionTransform;
import mil.nga.geopackage.tiles.TileBoundingBoxUtils;

/**
 * Abstract User DAO for reading user tables
 * 
 * @param <TColumn>
 * @param <TTable>
 * @param <TRow>
 * @param <TResult>
 * 
 * @author osbornb
 */
public abstract class UserCoreDao<TColumn extends UserColumn, TTable extends UserTable<TColumn>, TRow extends UserCoreRow<TColumn, TTable>, TResult extends UserCoreResult<TColumn, TTable, TRow>> {

	/**
	 * Database
	 */
	private final String database;

	/**
	 * Database connection
	 */
	private final GeoPackageCoreConnection db;

	/**
	 * User Database connection
	 */
	private final UserCoreConnection<TColumn, TTable, TRow, TResult> userDb;

	/**
	 * User table
	 */
	private final TTable table;

	/**
	 * Projection
	 */
	protected Projection projection;

	/**
	 * Constructor
	 * 
	 * @param database
	 * @param db
	 * @param userDb
	 * @param table
	 */
	protected UserCoreDao(String database, GeoPackageCoreConnection db,
			UserCoreConnection<TColumn, TTable, TRow, TResult> userDb,
			TTable table) {
		this.database = database;
		this.db = db;
		this.userDb = userDb;
		this.table = table;
	}

	/**
	 * Get a new empty row
	 * 
	 * @return row
	 */
	public abstract TRow newRow();

	/**
	 * Get the bounding box of the user table data
	 * 
	 * @return bounding box of user table data
	 * @since 1.1.0
	 */
	public abstract BoundingBox getBoundingBox();

	/**
	 * Get the database
	 * 
	 * @return database
	 */
	public String getDatabase() {
		return database;
	}

	/**
	 * Get the database connection
	 * 
	 * @return database connection
	 */
	public GeoPackageCoreConnection getDb() {
		return db;
	}

	/**
	 * Get the user database connection
	 * 
	 * @return user database connection
	 */
	public UserCoreConnection<TColumn, TTable, TRow, TResult> getUserDb() {
		return userDb;
	}

	/**
	 * Get the table name
	 * 
	 * @return table name
	 */
	public String getTableName() {
		return table.getTableName();
	}

	/**
	 * Get the table
	 * 
	 * @return table
	 */
	public TTable getTable() {
		return table;
	}

	/**
	 * Get the projection
	 *
	 * @return projection
	 */
	public Projection getProjection() {
		return projection;
	}

	/**
	 * Drop the user table
	 */
	public void dropTable() {
		db.execSQL("DROP TABLE IF EXISTS " + getTableName());
	}

	/**
	 * Query for all rows
	 * 
	 * @return result
	 */
	public TResult queryForAll() {
		return (TResult) userDb.query(getTableName(), table.getColumnNames(),
				null, null, null, null, null);
	}

	/**
	 * Query for the row where the field equals the value
	 * 
	 * @param fieldName
	 * @param value
	 * @return result
	 */
	public TResult queryForEq(String fieldName, Object value) {
		return queryForEq(fieldName, value, null, null, null);
	}

	/**
	 * Query for the row where the field equals the value
	 * 
	 * @param fieldName
	 * @param value
	 * @param groupBy
	 * @param having
	 * @param orderBy
	 * @return result
	 */
	public TResult queryForEq(String fieldName, Object value, String groupBy,
			String having, String orderBy) {
		String where = buildWhere(fieldName, value);
		String[] whereArgs = buildWhereArgs(value);
		return (TResult) userDb.query(getTableName(), table.getColumnNames(),
				where, whereArgs, groupBy, having, orderBy);
	}

	/**
	 * Query for the row where the field equals the value
	 * 
	 * @param fieldName
	 * @param value
	 * @return result
	 */
	public TResult queryForEq(String fieldName, ColumnValue value) {
		String where = buildWhere(fieldName, value);
		String[] whereArgs = buildWhereArgs(value);
		return (TResult) userDb.query(getTableName(), table.getColumnNames(),
				where, whereArgs, null, null, null);
	}

	/**
	 * Query for the row where all fields match their values
	 * 
	 * @param fieldValues
	 * @return result
	 */
	public TResult queryForFieldValues(Map<String, Object> fieldValues) {
		String where = buildWhere(fieldValues.entrySet());
		String[] whereArgs = buildWhereArgs(fieldValues.values());
		return (TResult) userDb.query(getTableName(), table.getColumnNames(),
				where, whereArgs, null, null, null);
	}

	/**
	 * Query for the row where all fields match their values
	 * 
	 * @param fieldValues
	 * @return result
	 */
	public TResult queryForValueFieldValues(Map<String, ColumnValue> fieldValues) {
		String where = buildValueWhere(fieldValues.entrySet());
		String[] whereArgs = buildValueWhereArgs(fieldValues.values());
		return (TResult) userDb.query(getTableName(), table.getColumnNames(),
				where, whereArgs, null, null, null);
	}

	/**
	 * Query for the row with the provided id
	 * 
	 * @param id
	 * @return result
	 */
	public TResult queryForId(long id) {
		String where = getPkWhere(id);
		String[] whereArgs = getPkWhereArgs(id);
		return (TResult) userDb.query(getTableName(), table.getColumnNames(),
				where, whereArgs, null, null, null);
	}

	/**
	 * Query for the row with the provided id
	 * 
	 * @param id
	 * @return row
	 */
	public TRow queryForIdRow(long id) {
		TRow row = null;
		TResult readCursor = queryForId(id);
		if (readCursor.moveToNext()) {
			row = readCursor.getRow();
		}
		readCursor.close();
		return row;
	}

	/**
	 * Query for rows
	 * 
	 * @param where
	 * @param whereArgs
	 * @return result
	 */
	public TResult query(String where, String[] whereArgs) {
		return (TResult) userDb.query(getTableName(), table.getColumnNames(),
				where, whereArgs, null, null, null);
	}

	/**
	 * Query for rows
	 * 
	 * @param where
	 * @param whereArgs
	 * @param groupBy
	 * @param having
	 * @param orderBy
	 * @return result
	 */
	public TResult query(String where, String[] whereArgs, String groupBy,
			String having, String orderBy) {
		return (TResult) userDb.query(getTableName(), table.getColumnNames(),
				where, whereArgs, groupBy, having, orderBy);
	}

	/**
	 * Query for rows
	 * 
	 * @param where
	 * @param whereArgs
	 * @param groupBy
	 * @param having
	 * @param orderBy
	 * @param limit
	 * @return result
	 */
	public TResult query(String where, String[] whereArgs, String groupBy,
			String having, String orderBy, String limit) {
		return (TResult) userDb.query(getTableName(), table.getColumnNames(),
				where, whereArgs, groupBy, having, orderBy, limit);
	}

	/**
	 * Update the row
	 * 
	 * @param row
	 * @return number of rows affected, should be 0 or 1
	 */
	public abstract int update(TRow row);

	/**
	 * Delete the row
	 * 
	 * @param row
	 * @return number of rows affected, should be 0 or 1
	 */
	public int delete(TRow row) {
		return deleteById(row.getId());
	}

	/**
	 * Delete a row by id
	 * 
	 * @param id
	 * @return number of rows affected, should be 0 or 1
	 */
	public int deleteById(long id) {
		return db.delete(getTableName(), getPkWhere(id), getPkWhereArgs(id));
	}

	/**
	 * Delete rows matching the where clause
	 * 
	 * @param whereClause
	 * @param whereArgs
	 * @return deleted count
	 */
	public int delete(String whereClause, String[] whereArgs) {
		return db.delete(getTableName(), whereClause, whereArgs);
	}

	/**
	 * Creates a new row, same as calling {@link #insert(UserCoreRow)}
	 * 
	 * @param row
	 * @return row id
	 */
	public long create(TRow row) {
		return insert(row);
	}

	/**
	 * Inserts a new row
	 * 
	 * @param row
	 * @return row id
	 */
	public abstract long insert(TRow row);

	/**
	 * Get the primary key where clause
	 * 
	 * @param id
	 * @return primary key where clause
	 */
	protected String getPkWhere(long id) {
		return buildWhere(table.getPkColumn().getName(), id);
	}

	/**
	 * Get the primary key where args
	 * 
	 * @return primary key where args
	 */
	protected String[] getPkWhereArgs(long id) {
		return buildWhereArgs(id);
	}

	/**
	 * Build where (or selection) statement from the fields
	 * 
	 * @param fields
	 * @return where clause
	 */
	public String buildWhere(Set<Map.Entry<String, Object>> fields) {
		StringBuilder selection = new StringBuilder();
		for (Map.Entry<String, Object> field : fields) {
			if (selection.length() > 0) {
				selection.append(" AND ");
			}
			selection.append(buildWhere(field.getKey(), field.getValue()));
		}
		return selection.toString();
	}

	/**
	 * Build where (or selection) statement from the fields
	 * 
	 * @param fields
	 * @return where clause
	 */
	public String buildValueWhere(Set<Map.Entry<String, ColumnValue>> fields) {
		StringBuilder selection = new StringBuilder();
		for (Map.Entry<String, ColumnValue> field : fields) {
			if (selection.length() > 0) {
				selection.append(" AND ");
			}
			selection.append(buildWhere(field.getKey(), field.getValue()));
		}
		return selection.toString();
	}

	/**
	 * Build where (or selection) statement for a single field
	 * 
	 * @param field
	 * @param value
	 * @return where clause
	 */
	public String buildWhere(String field, Object value) {
		return buildWhere(field, value, "=");
	}

	/**
	 * Build where (or selection) statement for a single field using the
	 * provided operation
	 * 
	 * @param field
	 * @param value
	 * @param operation
	 * @return where clause
	 */
	public String buildWhere(String field, Object value, String operation) {
		return "\"" + field + "\" "
				+ (value != null ? operation + " ?" : "IS NULL");
	}

	/**
	 * Build where (or selection) statement for a single field
	 * 
	 * @param field
	 * @param value
	 * @return where clause
	 */
	public String buildWhere(String field, ColumnValue value) {
		String where;
		if (value != null) {
			if (value.getValue() != null && value.getTolerance() != null) {
				if (!(value.getValue() instanceof Number)) {
					throw new GeoPackageException(
							"Field value is not a number and can not use a tolerance, Field: "
									+ field + ", Value: " + value);
				}
				where = "\"" + field + "\" >= ? AND \"" + field + "\" <= ?";
			} else {
				where = buildWhere(field, value.getValue());
			}
		} else {
			where = buildWhere(field, null, "=");
		}
		return where;
	}

	/**
	 * Build where (or selection) args for the values
	 * 
	 * @param values
	 * @return where args
	 */
	public String[] buildWhereArgs(Collection<Object> values) {
		List<String> selectionArgs = new ArrayList<String>();
		for (Object value : values) {
			if (value != null) {
				selectionArgs.add(value.toString());
			}
		}
		return selectionArgs.isEmpty() ? null : selectionArgs
				.toArray(new String[] {});
	}

	/**
	 * Build where (or selection) args for the values
	 * 
	 * @param values
	 * @return where args
	 */
	public String[] buildWhereArgs(Object[] values) {
		List<String> selectionArgs = new ArrayList<String>();
		for (Object value : values) {
			if (value != null) {
				selectionArgs.add(value.toString());
			}
		}
		return selectionArgs.isEmpty() ? null : selectionArgs
				.toArray(new String[] {});
	}

	/**
	 * Build where (or selection) args for the values
	 * 
	 * @param values
	 * @return where args
	 */
	public String[] buildValueWhereArgs(Collection<ColumnValue> values) {
		List<String> selectionArgs = new ArrayList<String>();
		for (ColumnValue value : values) {
			if (value != null && value.getValue() != null) {
				if (value.getTolerance() != null) {
					String[] toleranceArgs = getValueToleranceRange(value);
					selectionArgs.add(toleranceArgs[0]);
					selectionArgs.add(toleranceArgs[1]);
				} else {
					selectionArgs.add(value.getValue().toString());
				}
			}
		}
		return selectionArgs.isEmpty() ? null : selectionArgs
				.toArray(new String[] {});
	}

	/**
	 * Build where (or selection) args for the value
	 * 
	 * @param value
	 * @return where args
	 */
	public String[] buildWhereArgs(Object value) {
		String[] args = null;
		if (value != null) {
			args = new String[] { value.toString() };
		}
		return args;
	}

	/**
	 * Build where (or selection) args for the value
	 * 
	 * @param value
	 * @return where args
	 */
	public String[] buildWhereArgs(ColumnValue value) {
		String[] args = null;
		if (value != null) {
			if (value.getValue() != null && value.getTolerance() != null) {
				args = getValueToleranceRange(value);
			} else {
				args = buildWhereArgs(value.getValue());
			}
		}
		return args;
	}

	/**
	 * Get the total count
	 * 
	 * @return count
	 */
	public int count() {
		return count(null, null);
	}

	/**
	 * Get the count
	 * 
	 * @param where
	 * @param args
	 * @return count
	 */
	public int count(String where, String[] args) {
		return db.count(getTableName(), where, args);
	}

	/**
	 * Get the min result of the column
	 * 
	 * @param column
	 * @param where
	 * @param args
	 * @return min or null
	 */
	public Integer min(String column, String where, String[] args) {
		return db.min(getTableName(), column, where, args);
	}

	/**
	 * Get the max result of the column
	 * 
	 * @param column
	 * @param where
	 * @param args
	 * @return max or null
	 */
	public Integer max(String column, String where, String[] args) {
		return db.max(getTableName(), column, where, args);
	}

	/**
	 * Get the approximate zoom level of where the bounding box of the user data
	 * fits into the world
	 * 
	 * @return zoom level
	 * @since 1.1.0
	 */
	public int getZoomLevel() {
		Projection projection = getProjection();
		if (projection == null) {
			throw new GeoPackageException(
					"No projection was set which is required to determine the zoom level");
		}
		BoundingBox boundingBox = getBoundingBox();
		ProjectionTransform webMercatorTransform = projection
				.getTransformation(ProjectionConstants.EPSG_WEB_MERCATOR);
		BoundingBox webMercatorBoundingBox = webMercatorTransform
				.transform(boundingBox);
		int zoomLevel = TileBoundingBoxUtils
				.getZoomLevel(webMercatorBoundingBox);
		return zoomLevel;
	}

	/**
	 * Get the value tolerance range min and max values
	 * 
	 * @param value
	 * @return tolerance range
	 */
	private String[] getValueToleranceRange(ColumnValue value) {
		double doubleValue = ((Number) value.getValue()).doubleValue();
		double tolerance = value.getTolerance();
		return new String[] { Double.toString(doubleValue - tolerance),
				Double.toString(doubleValue + tolerance) };
	}

}
