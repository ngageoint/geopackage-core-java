package mil.nga.geopackage.user;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import mil.nga.geopackage.BoundingBox;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.db.CoreSQLUtils;
import mil.nga.geopackage.db.GeoPackageCoreConnection;
import mil.nga.geopackage.db.GeoPackageDataType;
import mil.nga.geopackage.tiles.TileBoundingBoxUtils;
import mil.nga.sf.proj.Projection;
import mil.nga.sf.proj.ProjectionConstants;
import mil.nga.sf.proj.ProjectionTransform;

import org.locationtech.proj4j.units.Units;

/**
 * Abstract User DAO for reading user tables
 * 
 * @param <TColumn>
 *            column type
 * @param <TTable>
 *            table type
 * @param <TRow>
 *            row type
 * @param <TResult>
 *            result type
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
	 *            database name
	 * @param db
	 *            GeoPackage connection
	 * @param userDb
	 *            user connection
	 * @param table
	 *            table
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
	 * Get the bounding box of the user table data
	 * 
	 * @param projection
	 *            desired projection
	 * 
	 * @return bounding box of user table data
	 * @since 3.1.0
	 */
	public abstract BoundingBox getBoundingBox(Projection projection);

	/**
	 * Project the provided bounding box in the declared projection to the user
	 * DAO projection
	 * 
	 * @param boundingBox
	 *            bounding box
	 * @param projection
	 *            projection
	 * @return projected bounding box
	 * @since 3.1.0
	 */
	public BoundingBox projectBoundingBox(BoundingBox boundingBox,
			Projection projection) {
		ProjectionTransform projectionTransform = projection
				.getTransformation(getProjection());
		BoundingBox projectedBoundingBox = boundingBox
				.transform(projectionTransform);
		return projectedBoundingBox;
	}

	/**
	 * Prepare the result before returning
	 * 
	 * @param result
	 *            result
	 * @return prepared result
	 * @since 2.0.0
	 */
	protected abstract TResult prepareResult(TResult result);

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
		db.execSQL("DROP TABLE IF EXISTS "
				+ CoreSQLUtils.quoteWrap(getTableName()));
	}

	/**
	 * Query for all rows
	 * 
	 * @return result
	 */
	public TResult queryForAll() {
		TResult result = userDb.query(getTableName(), table.getColumnNames(),
				null, null, null, null, null);
		prepareResult(result);
		return result;
	}

	/**
	 * Query for all rows with "columns as" values for corresponding column
	 * indices. Non null values in the array will be used as "as" values for the
	 * corresponding column.
	 * 
	 * @param columnsAs
	 *            columns as values
	 * @return result
	 * @since 2.0.0
	 */
	public TResult queryForAll(String[] columnsAs) {
		TResult result = userDb.query(getTableName(), table.getColumnNames(),
				columnsAs, null, null, null, null, null);
		prepareResult(result);
		return result;
	}

	/**
	 * Query for the row where the field equals the value
	 * 
	 * @param fieldName
	 *            field name
	 * @param value
	 *            value
	 * @return result
	 */
	public TResult queryForEq(String fieldName, Object value) {
		return queryForEq(fieldName, value, null, null, null);
	}

	/**
	 * Query for the row where the field equals the value
	 * 
	 * @param fieldName
	 *            field name
	 * @param value
	 *            value
	 * @param groupBy
	 *            group by
	 * @param having
	 *            having
	 * @param orderBy
	 *            order by
	 * @return result
	 */
	public TResult queryForEq(String fieldName, Object value, String groupBy,
			String having, String orderBy) {
		String where = buildWhere(fieldName, value);
		String[] whereArgs = buildWhereArgs(value);
		TResult result = userDb.query(getTableName(), table.getColumnNames(),
				where, whereArgs, groupBy, having, orderBy);
		prepareResult(result);
		return result;
	}

	/**
	 * Query for the row where the field equals the value
	 * 
	 * @param fieldName
	 *            field name
	 * @param value
	 *            column value
	 * @return result
	 */
	public TResult queryForEq(String fieldName, ColumnValue value) {
		String where = buildWhere(fieldName, value);
		String[] whereArgs = buildWhereArgs(value);
		TResult result = userDb.query(getTableName(), table.getColumnNames(),
				where, whereArgs, null, null, null);
		prepareResult(result);
		return result;
	}

	/**
	 * Query for the row where the field is like the value
	 * 
	 * @param fieldName
	 *            field name
	 * @param value
	 *            value
	 * @return result
	 * @since 3.0.1
	 */

	public TResult queryForLike(String fieldName, Object value) {
		return queryForLike(fieldName, value, null, null, null);
	}

	/**
	 * Query for the row where the field equals the value
	 * 
	 * @param fieldName
	 *            field name
	 * @param value
	 *            value
	 * @param groupBy
	 *            group by statement
	 * @param having
	 *            having statement
	 * @param orderBy
	 *            order by statement
	 * @return result
	 * @since 3.0.1
	 */
	public TResult queryForLike(String fieldName, Object value, String groupBy,
			String having, String orderBy) {
		String where = buildWhereLike(fieldName, value);
		String[] whereArgs = buildWhereArgs(value);
		TResult result = userDb.query(getTableName(), table.getColumnNames(),
				where, whereArgs, groupBy, having, orderBy);
		prepareResult(result);
		return result;
	}

	/**
	 * Query for the row where the field is like the value
	 * 
	 * @param fieldName
	 *            field name
	 * @param value
	 *            column value
	 * @return result
	 * @since 3.0.1
	 */
	public TResult queryForLike(String fieldName, ColumnValue value) {
		String where = buildWhereLike(fieldName, value);
		String[] whereArgs = buildWhereArgs(value);
		TResult result = userDb.query(getTableName(), table.getColumnNames(),
				where, whereArgs, null, null, null);
		prepareResult(result);
		return result;
	}

	/**
	 * Query for the row where all fields match their values
	 * 
	 * @param fieldValues
	 *            field values
	 * @return result
	 */
	public TResult queryForFieldValues(Map<String, Object> fieldValues) {
		String where = buildWhere(fieldValues.entrySet());
		String[] whereArgs = buildWhereArgs(fieldValues.values());
		TResult result = userDb.query(getTableName(), table.getColumnNames(),
				where, whereArgs, null, null, null);
		prepareResult(result);
		return result;
	}

	/**
	 * Query for the row where all fields match their values
	 * 
	 * @param fieldValues
	 *            field values
	 * @return result
	 */
	public TResult queryForValueFieldValues(Map<String, ColumnValue> fieldValues) {
		String where = buildValueWhere(fieldValues.entrySet());
		String[] whereArgs = buildValueWhereArgs(fieldValues.values());
		TResult result = userDb.query(getTableName(), table.getColumnNames(),
				where, whereArgs, null, null, null);
		prepareResult(result);
		return result;
	}

	/**
	 * Query for the row with the provided id
	 * 
	 * @param id
	 *            id
	 * @return result
	 */
	public TResult queryForId(long id) {
		String where = getPkWhere(id);
		String[] whereArgs = getPkWhereArgs(id);
		TResult result = userDb.query(getTableName(), table.getColumnNames(),
				where, whereArgs, null, null, null);
		prepareResult(result);
		return result;
	}

	/**
	 * Query for the row with the provided id
	 * 
	 * @param id
	 *            id
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
	 *            where clause
	 * @param whereArgs
	 *            where arguments
	 * @return result
	 */
	public TResult query(String where, String[] whereArgs) {
		TResult result = userDb.query(getTableName(), table.getColumnNames(),
				where, whereArgs, null, null, null);
		prepareResult(result);
		return result;
	}

	/**
	 * Query for rows
	 * 
	 * @param where
	 *            where clause
	 * @param whereArgs
	 *            where arguments
	 * @param groupBy
	 *            group by
	 * @param having
	 *            having
	 * @param orderBy
	 *            order by
	 * @return result
	 */
	public TResult query(String where, String[] whereArgs, String groupBy,
			String having, String orderBy) {
		TResult result = userDb.query(getTableName(), table.getColumnNames(),
				where, whereArgs, groupBy, having, orderBy);
		prepareResult(result);
		return result;
	}

	/**
	 * Query for rows
	 * 
	 * @param where
	 *            where clause
	 * @param whereArgs
	 *            where arguments
	 * @param groupBy
	 *            group by
	 * @param having
	 *            having
	 * @param orderBy
	 *            order by
	 * @param limit
	 *            limit
	 * @return result
	 */
	public TResult query(String where, String[] whereArgs, String groupBy,
			String having, String orderBy, String limit) {
		TResult result = userDb.query(getTableName(), table.getColumnNames(),
				where, whereArgs, groupBy, having, orderBy, limit);
		prepareResult(result);
		return result;
	}

	/**
	 * Query for id ordered rows starting at the offset and returning no more
	 * than the limit.
	 * 
	 * @param limit
	 *            chunk limit
	 * @param offset
	 *            chunk query offset
	 * @return result
	 * @since 3.1.0
	 */
	public TResult queryForChunk(int limit, long offset) {
		return queryForChunk(table.getPkColumn().getName(), limit, offset);
	}

	/**
	 * Query for ordered rows starting at the offset and returning no more than
	 * the limit.
	 * 
	 * @param orderBy
	 *            order by
	 * @param limit
	 *            chunk limit
	 * @param offset
	 *            chunk query offset
	 * @return result
	 * @since 3.1.0
	 */
	public TResult queryForChunk(String orderBy, int limit, long offset) {
		return query(null, null, null, null, orderBy, buildLimit(limit, offset));
	}

	/**
	 * Build a limit String with the limit and offset
	 * 
	 * @param limit
	 *            limit
	 * @param offset
	 *            offset
	 * @return limit
	 * @since 3.1.0
	 */
	public String buildLimit(int limit, long offset) {
		return offset + "," + limit;
	}

	/**
	 * Update the row
	 * 
	 * @param row
	 *            row
	 * @return number of rows affected, should be 0 or 1
	 */
	public abstract int update(TRow row);

	/**
	 * Delete the row
	 * 
	 * @param row
	 *            row
	 * @return number of rows affected, should be 0 or 1 unless the table has
	 *         duplicate rows in it
	 */
	public int delete(TRow row) {
		int numDeleted;
		if (row.hasId()) {
			numDeleted = deleteById(row.getId());
		} else {
			numDeleted = delete(buildValueWhere(row.getAsMap()),
					buildWhereArgs(row.getValues()));
		}
		return numDeleted;
	}

	/**
	 * Delete a row by id
	 * 
	 * @param id
	 *            id
	 * @return number of rows affected, should be 0 or 1
	 */
	public int deleteById(long id) {
		return db.delete(getTableName(), getPkWhere(id), getPkWhereArgs(id));
	}

	/**
	 * Delete rows matching the where clause
	 * 
	 * @param whereClause
	 *            where clause
	 * @param whereArgs
	 *            where arguments
	 * @return deleted count
	 */
	public int delete(String whereClause, String[] whereArgs) {
		return db.delete(getTableName(), whereClause, whereArgs);
	}

	/**
	 * Delete rows matching the field values
	 * 
	 * @param fieldValues
	 *            field values
	 * @return deleted count
	 * @since 3.0.2
	 */
	public int delete(Map<String, Object> fieldValues) {
		String whereClause = buildWhere(fieldValues.entrySet());
		String[] whereArgs = buildWhereArgs(fieldValues.values());
		return delete(whereClause, whereArgs);
	}

	/**
	 * Delete all rows
	 * 
	 * @return deleted count
	 * @since 3.0.2
	 */
	public int deleteAll() {
		return delete(null, null);
	}

	/**
	 * Creates a new row, same as calling {@link #insert(UserCoreRow)}
	 * 
	 * @param row
	 *            row
	 * @return row id
	 */
	public long create(TRow row) {
		return insert(row);
	}

	/**
	 * Inserts a new row
	 * 
	 * @param row
	 *            row
	 * @return row id
	 */
	public abstract long insert(TRow row);

	/**
	 * Get the primary key where clause
	 * 
	 * @param id
	 *            id
	 * @return primary key where clause
	 */
	protected String getPkWhere(long id) {
		return buildWhere(table.getPkColumn().getName(), id);
	}

	/**
	 * Get the primary key where args
	 * 
	 * @param id
	 *            id
	 * @return primary key where args
	 */
	protected String[] getPkWhereArgs(long id) {
		return buildWhereArgs(id);
	}

	/**
	 * Build where (or selection) statement from the fields
	 * 
	 * @param fields
	 *            fields
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
	 *            fields
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
	 *            field name
	 * @param value
	 *            field value
	 * @return where clause
	 */
	public String buildWhere(String field, Object value) {
		return buildWhere(field, value, "=");
	}

	/**
	 * Build where (or selection) LIKE statement for a single field
	 * 
	 * @param field
	 *            field name
	 * @param value
	 *            field value
	 * @return where clause
	 * @since 3.0.1
	 */
	public String buildWhereLike(String field, Object value) {
		return buildWhere(field, value, "LIKE");
	}

	/**
	 * Build where (or selection) statement for a single field using the
	 * provided operation
	 * 
	 * @param field
	 *            field
	 * @param value
	 *            value
	 * @param operation
	 *            operation
	 * @return where clause
	 */
	public String buildWhere(String field, Object value, String operation) {
		return CoreSQLUtils.quoteWrap(field) + " "
				+ (value != null ? operation + " ?" : "IS NULL");
	}

	/**
	 * Build where (or selection) statement for a single field
	 * 
	 * @param field
	 *            field name
	 * @param value
	 *            column value
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
				String quotedField = CoreSQLUtils.quoteWrap(field);
				where = quotedField + " >= ? AND " + quotedField + " <= ?";
			} else {
				where = buildWhere(field, value.getValue());
			}
		} else {
			where = buildWhere(field, null, null);
		}
		return where;
	}

	/**
	 * Build where (or selection) LIKE statement for a single field
	 * 
	 * @param field
	 *            field name
	 * @param value
	 *            column value
	 * @return where clause
	 * @since 3.0.1
	 */
	public String buildWhereLike(String field, ColumnValue value) {
		String where;
		if (value != null) {
			if (value.getTolerance() != null) {
				throw new GeoPackageException(
						"Field value tolerance not supported for LIKE query, Field: "
								+ field + ", Value: " + ", Tolerance: "
								+ value.getTolerance());
			}
			where = buildWhereLike(field, value.getValue());
		} else {
			where = buildWhere(field, null, null);
		}
		return where;
	}

	/**
	 * Build where (or selection) args for the values
	 * 
	 * @param values
	 *            values
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
	 *            values
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
	 *            values
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
	 *            value
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
	 *            value
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
	 *            where clause
	 * @param args
	 *            where arguments
	 * @return count
	 */
	public int count(String where, String[] args) {
		return db.count(getTableName(), where, args);
	}

	/**
	 * Get the min result of the column
	 * 
	 * @param column
	 *            column name
	 * @param where
	 *            where clause
	 * @param args
	 *            where arugments
	 * @return min or null
	 */
	public Integer min(String column, String where, String[] args) {
		return db.min(getTableName(), column, where, args);
	}

	/**
	 * Get the max result of the column
	 * 
	 * @param column
	 *            column name
	 * @param where
	 *            where clause
	 * @param args
	 *            where arguments
	 * @return max or null
	 */
	public Integer max(String column, String where, String[] args) {
		return db.max(getTableName(), column, where, args);
	}

	/**
	 * Query the SQL for a single result object in the first column
	 * 
	 * @param sql
	 *            sql statement
	 * @param args
	 *            sql arguments
	 * @return single result object
	 * @since 3.1.0
	 */
	public Object querySingleResult(String sql, String[] args) {
		return db.querySingleResult(sql, args);
	}

	/**
	 * Query the SQL for a single result typed object in the first column
	 * 
	 * @param <T>
	 *            result value type
	 * @param sql
	 *            sql statement
	 * @param args
	 *            sql arguments
	 * @return single result object
	 * @since 3.1.0
	 */
	public <T> T querySingleTypedResult(String sql, String[] args) {
		return db.querySingleTypedResult(sql, args);
	}

	/**
	 * Query the SQL for a single result object in the first column with the
	 * expected data type
	 * 
	 * @param sql
	 *            sql statement
	 * @param args
	 *            sql arguments
	 * @param dataType
	 *            GeoPackage data type
	 * @return single result object
	 * @since 3.1.0
	 */
	public Object querySingleResult(String sql, String[] args,
			GeoPackageDataType dataType) {
		return db.querySingleResult(sql, args, dataType);
	}

	/**
	 * Query the SQL for a single result typed object in the first column with
	 * the expected data type
	 * 
	 * @param <T>
	 *            result value type
	 * @param sql
	 *            sql statement
	 * @param args
	 *            sql arguments
	 * @param dataType
	 *            GeoPackage data type
	 * @return single result object
	 * @since 3.1.0
	 */
	public <T> T querySingleTypedResult(String sql, String[] args,
			GeoPackageDataType dataType) {
		return db.querySingleTypedResult(sql, args, dataType);
	}

	/**
	 * Query the SQL for a single result object
	 * 
	 * @param sql
	 *            sql statement
	 * @param args
	 *            arguments
	 * @param column
	 *            column index
	 * @return result, null if no result
	 * @since 3.1.0
	 */
	public Object querySingleResult(String sql, String[] args, int column) {
		return db.querySingleResult(sql, args, column);
	}

	/**
	 * Query the SQL for a single result typed object
	 * 
	 * @param <T>
	 *            result value type
	 * @param sql
	 *            sql statement
	 * @param args
	 *            arguments
	 * @param column
	 *            column index
	 * @return result, null if no result
	 * @since 3.1.0
	 */
	public <T> T querySingleTypedResult(String sql, String[] args, int column) {
		return db.querySingleTypedResult(sql, args, column);
	}

	/**
	 * Query the SQL for a single result object with the expected data type
	 * 
	 * @param sql
	 *            sql statement
	 * @param args
	 *            arguments
	 * @param column
	 *            column index
	 * @param dataType
	 *            GeoPackage data type
	 * @return result, null if no result
	 * @since 3.1.0
	 */
	public Object querySingleResult(String sql, String[] args, int column,
			GeoPackageDataType dataType) {
		return db.querySingleResult(sql, args, column, dataType);
	}

	/**
	 * Query the SQL for a single result typed object with the expected data
	 * type
	 * 
	 * @param <T>
	 *            result value type
	 * @param sql
	 *            sql statement
	 * @param args
	 *            arguments
	 * @param column
	 *            column index
	 * @param dataType
	 *            GeoPackage data type
	 * @return result, null if no result
	 * @since 3.1.0
	 */
	public <T> T querySingleTypedResult(String sql, String[] args, int column,
			GeoPackageDataType dataType) {
		return db.querySingleTypedResult(sql, args, column, dataType);
	}

	/**
	 * Query for values from the first column
	 * 
	 * @param sql
	 *            sql statement
	 * @param args
	 *            sql arguments
	 * @return single column values
	 * @since 3.1.0
	 */
	public List<Object> querySingleColumnResults(String sql, String[] args) {
		return db.querySingleColumnResults(sql, args);
	}

	/**
	 * Query for typed values from the first column
	 * 
	 * @param <T>
	 *            result value type
	 * @param sql
	 *            sql statement
	 * @param args
	 *            sql arguments
	 * @return single column values
	 * @since 3.1.0
	 */
	public <T> List<T> querySingleColumnTypedResults(String sql, String[] args) {
		return db.querySingleColumnTypedResults(sql, args);
	}

	/**
	 * Query for values from the first column
	 * 
	 * @param sql
	 *            sql statement
	 * @param args
	 *            arguments
	 * @param dataType
	 *            GeoPackage data type
	 * @return single column results
	 * @since 3.1.0
	 */
	public List<Object> querySingleColumnResults(String sql, String[] args,
			GeoPackageDataType dataType) {
		return db.querySingleColumnResults(sql, args, dataType);
	}

	/**
	 * Query for typed values from the first column
	 * 
	 * @param <T>
	 *            result value type
	 * @param sql
	 *            sql statement
	 * @param args
	 *            arguments
	 * @param dataType
	 *            GeoPackage data type
	 * @return single column results
	 * @since 3.1.0
	 */
	public <T> List<T> querySingleColumnTypedResults(String sql, String[] args,
			GeoPackageDataType dataType) {
		return db.querySingleColumnTypedResults(sql, args, dataType);
	}

	/**
	 * Query for values from a single column
	 * 
	 * @param sql
	 *            sql statement
	 * @param args
	 *            arguments
	 * @param column
	 *            column index
	 * @return single column results
	 * @since 3.1.0
	 */
	public List<Object> querySingleColumnResults(String sql, String[] args,
			int column) {
		return db.querySingleColumnResults(sql, args, column);
	}

	/**
	 * Query for typed values from a single column
	 * 
	 * @param <T>
	 *            result value type
	 * @param sql
	 *            sql statement
	 * @param args
	 *            arguments
	 * @param column
	 *            column index
	 * @return single column results
	 * @since 3.1.0
	 */
	public <T> List<T> querySingleColumnTypedResults(String sql, String[] args,
			int column) {
		return db.querySingleColumnTypedResults(sql, args, column);
	}

	/**
	 * Query for values from a single column
	 * 
	 * @param sql
	 *            sql statement
	 * @param args
	 *            arguments
	 * @param column
	 *            column index
	 * @param dataType
	 *            GeoPackage data type
	 * @return single column results
	 * @since 3.1.0
	 */
	public List<Object> querySingleColumnResults(String sql, String[] args,
			int column, GeoPackageDataType dataType) {
		return db.querySingleColumnResults(sql, args, column, dataType);
	}

	/**
	 * Query for typed values from a single column
	 * 
	 * @param <T>
	 *            result value type
	 * @param sql
	 *            sql statement
	 * @param args
	 *            arguments
	 * @param column
	 *            column index
	 * @param dataType
	 *            GeoPackage data type
	 * @return single column results
	 * @since 3.1.0
	 */
	public <T> List<T> querySingleColumnTypedResults(String sql, String[] args,
			int column, GeoPackageDataType dataType) {
		return db.querySingleColumnTypedResults(sql, args, column, dataType);
	}

	/**
	 * Query for values from a single column up to the limit
	 * 
	 * @param sql
	 *            sql statement
	 * @param args
	 *            arguments
	 * @param limit
	 *            result row limit
	 * @param column
	 *            column index
	 * @return single column results
	 * @since 3.1.0
	 */
	public List<Object> querySingleColumnResults(String sql, String[] args,
			int column, Integer limit) {
		return db.querySingleColumnResults(sql, args, column, limit);
	}

	/**
	 * Query for typed values from a single column up to the limit
	 * 
	 * @param <T>
	 *            result value type
	 * @param sql
	 *            sql statement
	 * @param args
	 *            arguments
	 * @param limit
	 *            result row limit
	 * @param column
	 *            column index
	 * @return single column results
	 * @since 3.1.0
	 */
	public <T> List<T> querySingleColumnTypedResults(String sql, String[] args,
			int column, Integer limit) {
		return db.querySingleColumnTypedResults(sql, args, column, limit);
	}

	/**
	 * Query for values from a single column up to the limit
	 * 
	 * @param sql
	 *            sql statement
	 * @param args
	 *            arguments
	 * @param column
	 *            column index
	 * @param dataType
	 *            GeoPackage data type
	 * @param limit
	 *            result row limit
	 * @return single column results
	 * @since 3.1.0
	 */
	public List<Object> querySingleColumnResults(String sql, String[] args,
			int column, GeoPackageDataType dataType, Integer limit) {
		return db.querySingleColumnResults(sql, args, column, dataType, limit);
	}

	/**
	 * Query for typed values from a single column up to the limit
	 * 
	 * @param <T>
	 *            result value type
	 * @param sql
	 *            sql statement
	 * @param args
	 *            arguments
	 * @param column
	 *            column index
	 * @param dataType
	 *            GeoPackage data type
	 * @param limit
	 *            result row limit
	 * @return single column results
	 * @since 3.1.0
	 */
	public <T> List<T> querySingleColumnTypedResults(String sql, String[] args,
			int column, GeoPackageDataType dataType, Integer limit) {
		return db.querySingleColumnTypedResults(sql, args, column, dataType,
				limit);
	}

	/**
	 * Query for values
	 * 
	 * @param sql
	 *            sql statement
	 * @param args
	 *            arguments
	 * @return results
	 * @since 3.1.0
	 */
	public List<List<Object>> queryResults(String sql, String[] args) {
		return db.queryResults(sql, args);
	}

	/**
	 * Query for typed values
	 * 
	 * @param <T>
	 *            result value type
	 * @param sql
	 *            sql statement
	 * @param args
	 *            arguments
	 * @return results
	 * @since 3.1.0
	 */
	public <T> List<List<T>> queryTypedResults(String sql, String[] args) {
		return db.queryTypedResults(sql, args);
	}

	/**
	 * Query for values
	 * 
	 * @param sql
	 *            sql statement
	 * @param args
	 *            arguments
	 * @param dataTypes
	 *            column data types
	 * @return results
	 * @since 3.1.0
	 */
	public List<List<Object>> queryResults(String sql, String[] args,
			GeoPackageDataType[] dataTypes) {
		return db.queryResults(sql, args, dataTypes);
	}

	/**
	 * Query for typed values
	 * 
	 * @param <T>
	 *            result value type
	 * @param sql
	 *            sql statement
	 * @param args
	 *            arguments
	 * @param dataTypes
	 *            column data types
	 * @return results
	 * @since 3.1.0
	 */
	public <T> List<List<T>> queryTypedResults(String sql, String[] args,
			GeoPackageDataType[] dataTypes) {
		return db.queryTypedResults(sql, args, dataTypes);
	}

	/**
	 * Query for string values in a single (first) row
	 * 
	 * @param sql
	 *            sql statement
	 * @param args
	 *            arguments
	 * @return single row results
	 * @since 3.1.0
	 */
	public List<Object> querySingleRowResults(String sql, String[] args) {
		return db.querySingleRowResults(sql, args);
	}

	/**
	 * Query for string typed values in a single (first) row
	 * 
	 * @param <T>
	 *            result value type
	 * @param sql
	 *            sql statement
	 * @param args
	 *            arguments
	 * @return single row results
	 * @since 3.1.0
	 */
	public <T> List<T> querySingleRowTypedResults(String sql, String[] args) {
		return db.querySingleRowTypedResults(sql, args);
	}

	/**
	 * Query for values in a single (first) row
	 * 
	 * @param sql
	 *            sql statement
	 * @param args
	 *            arguments
	 * @param dataTypes
	 *            column data types
	 * @return single row results
	 * @since 3.1.0
	 */
	public List<Object> querySingleRowResults(String sql, String[] args,
			GeoPackageDataType[] dataTypes) {
		return db.querySingleRowResults(sql, args, dataTypes);
	}

	/**
	 * Query for typed values in a single (first) row
	 * 
	 * @param <T>
	 *            result value type
	 * @param sql
	 *            sql statement
	 * @param args
	 *            arguments
	 * @param dataTypes
	 *            column data types
	 * @return single row results
	 * @since 3.1.0
	 */
	public <T> List<T> querySingleRowTypedResults(String sql, String[] args,
			GeoPackageDataType[] dataTypes) {
		return db.querySingleRowTypedResults(sql, args, dataTypes);
	}

	/**
	 * Query for values
	 * 
	 * @param sql
	 *            sql statement
	 * @param args
	 *            arguments
	 * @param limit
	 *            result row limit
	 * @return results
	 * @since 3.1.0
	 */
	public List<List<Object>> queryResults(String sql, String[] args,
			Integer limit) {
		return db.queryResults(sql, args, limit);
	}

	/**
	 * Query for typed values
	 * 
	 * @param <T>
	 *            result value type
	 * @param sql
	 *            sql statement
	 * @param args
	 *            arguments
	 * @param limit
	 *            result row limit
	 * @return results
	 * @since 3.1.0
	 */
	public <T> List<List<T>> queryTypedResults(String sql, String[] args,
			Integer limit) {
		return db.queryTypedResults(sql, args, limit);
	}

	/**
	 * Query for values up to the limit
	 * 
	 * @param sql
	 *            sql statement
	 * @param args
	 *            arguments
	 * @param dataTypes
	 *            column data types
	 * @param limit
	 *            result row limit
	 * @return results
	 * @since 3.1.0
	 */
	public List<List<Object>> queryResults(String sql, String[] args,
			GeoPackageDataType[] dataTypes, Integer limit) {
		return db.queryResults(sql, args, dataTypes, limit);
	}

	/**
	 * Query for typed values up to the limit
	 * 
	 * @param <T>
	 *            result value type
	 * @param sql
	 *            sql statement
	 * @param args
	 *            arguments
	 * @param dataTypes
	 *            column data types
	 * @param limit
	 *            result row limit
	 * @return results
	 * @since 3.1.0
	 */
	public <T> List<List<T>> queryTypedResults(String sql, String[] args,
			GeoPackageDataType[] dataTypes, Integer limit) {
		return db.queryTypedResults(sql, args, dataTypes, limit);
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
		int zoomLevel = 0;
		BoundingBox boundingBox = getBoundingBox();
		if (boundingBox != null) {
			if (projection.isUnit(Units.DEGREES)) {
				boundingBox = TileBoundingBoxUtils
						.boundDegreesBoundingBoxWithWebMercatorLimits(boundingBox);
			}
			ProjectionTransform webMercatorTransform = projection
					.getTransformation(ProjectionConstants.EPSG_WEB_MERCATOR);
			BoundingBox webMercatorBoundingBox = boundingBox
					.transform(webMercatorTransform);
			zoomLevel = TileBoundingBoxUtils
					.getZoomLevel(webMercatorBoundingBox);
		}
		return zoomLevel;
	}

	/**
	 * Build "columns as" values for the table columns with the specified
	 * columns as null
	 * 
	 * @param columns
	 *            columns to include as null
	 * @return "columns as" values
	 * @since 2.0.0
	 */
	public String[] buildColumnsAsNull(List<TColumn> columns) {
		return buildColumnsAs(columns, "null");
	}

	/**
	 * Build "columns as" values for the table columns with the specified
	 * columns as the specified value
	 * 
	 * @param columns
	 *            columns to include as value
	 * @param value
	 *            "columns as" value for specified columns
	 * @return "columns as" values
	 * @since 2.0.0
	 */
	public String[] buildColumnsAs(List<TColumn> columns, String value) {

		String[] columnsArray = buildColumnsArray(columns);

		return buildColumnsAs(columnsArray, value);
	}

	/**
	 * Build "columns as" values for the table columns with the specified
	 * columns as null
	 * 
	 * @param columns
	 *            columns to include as null
	 * @return "columns as" values
	 * @since 2.0.0
	 */
	public String[] buildColumnsAsNull(String[] columns) {
		return buildColumnsAs(columns, "null");
	}

	/**
	 * Build "columns as" values for the table columns with the specified
	 * columns as the specified value
	 * 
	 * @param columns
	 *            columns to include as value
	 * @param value
	 *            "columns as" value for specified columns
	 * @return "columns as" values
	 * @since 2.0.0
	 */
	public String[] buildColumnsAs(String[] columns, String value) {

		String[] values = new String[columns.length];
		for (int i = 0; i < columns.length; i++) {
			values[i] = value;
		}

		return buildColumnsAs(columns, values);
	}

	/**
	 * Build "columns as" values for the table columns with the specified
	 * columns as the specified values
	 * 
	 * @param columns
	 *            columns to include as value
	 * @param values
	 *            "columns as" values for specified columns
	 * @return "columns as" values
	 * @since 2.0.0
	 */
	public String[] buildColumnsAs(List<TColumn> columns, String[] values) {

		String[] columnsArray = buildColumnsArray(columns);

		return buildColumnsAs(columnsArray, values);
	}

	/**
	 * Build "columns as" values for the table columns with the specified
	 * columns as the specified values
	 * 
	 * @param columns
	 *            columns to include as value
	 * @param values
	 *            "columns as" values for specified columns
	 * @return "columns as" values
	 * @since 2.0.0
	 */
	public String[] buildColumnsAs(String[] columns, String[] values) {

		Map<String, String> columnsMap = new HashMap<>();
		for (int i = 0; i < columns.length; i++) {
			String column = columns[i];
			String value = values[i];
			columnsMap.put(column, value);
		}

		return buildColumnsAs(columnsMap);
	}

	/**
	 * Build "columns as" values for the table column to value mapping
	 * 
	 * @param columns
	 *            mapping between columns and values
	 * @return "columns as" values
	 * @since 2.0.0
	 */
	public String[] buildColumnsAs(Map<String, String> columns) {

		String[] columnNames = table.getColumnNames();
		String[] columnsAs = new String[columnNames.length];

		for (int i = 0; i < columnNames.length; i++) {
			String column = columnNames[i];
			columnsAs[i] = columns.get(column);
		}

		return columnsAs;
	}

	/**
	 * Build a columns name array from the list of columns
	 * 
	 * @param columns
	 *            column list
	 * @return column names array
	 */
	private String[] buildColumnsArray(List<TColumn> columns) {
		String[] columnsArray = new String[columns.size()];
		for (int i = 0; i < columns.size(); i++) {
			TColumn column = columns.get(i);
			columnsArray[i] = column.getName();
		}
		return columnsArray;
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

	/**
	 * Add a new column
	 * 
	 * @param column
	 *            new column
	 * @since 3.2.1
	 */
	public void addColumn(TColumn column) {
		CoreSQLUtils.addColumn(db, table.getTableName(), column);
		table.addColumn(column);
	}

	/**
	 * Drop a column
	 * 
	 * @param column
	 *            column
	 * @since 3.2.1
	 */
	public void dropColumn(TColumn column) {
		dropTableColumn(column.getName());
		table.dropColumn(column.getIndex());
	}

	/**
	 * Drop a column
	 * 
	 * @param index
	 *            column index
	 * @since 3.2.1
	 */
	public void dropColumn(int index) {
		dropTableColumn(table.getColumnName(index));
		table.dropColumn(index);
	}

	/**
	 * Drop a column
	 * 
	 * @param name
	 *            column name
	 * @since 3.2.1
	 */
	public void dropColumn(String name) {
		dropTableColumn(name);
		table.dropColumn(name);
	}

	/**
	 * Drop a column from the table
	 * 
	 * @param name
	 *            column name
	 */
	private void dropTableColumn(String name) {
		// TODO
		throw new UnsupportedOperationException("Drop column not yet supported");
		// db.execSQL("ALTER TABLE "
		// + CoreSQLUtils.quoteWrap(table.getTableName())
		// + " DROP COLUMN " + CoreSQLUtils.quoteWrap(name) + ";");
	}

}
