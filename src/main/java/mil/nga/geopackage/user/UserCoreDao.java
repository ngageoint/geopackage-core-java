package mil.nga.geopackage.user;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.locationtech.proj4j.units.Units;

import mil.nga.geopackage.BoundingBox;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.contents.Contents;
import mil.nga.geopackage.db.AlterTable;
import mil.nga.geopackage.db.CoreSQLUtils;
import mil.nga.geopackage.db.GeoPackageCoreConnection;
import mil.nga.geopackage.db.GeoPackageDao;
import mil.nga.geopackage.db.GeoPackageDataType;
import mil.nga.geopackage.tiles.TileBoundingBoxUtils;
import mil.nga.sf.proj.Projection;
import mil.nga.sf.proj.ProjectionConstants;
import mil.nga.sf.proj.ProjectionTransform;

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
	 * Create a GeoPackage DAO
	 * 
	 * @param <D>
	 *            DAO type
	 * @param <O>
	 *            DAO object type
	 * @param clazz
	 *            DAO class type
	 * @return GeoPackage DAO
	 * @since 4.0.0
	 */
	public <D extends GeoPackageDao<O, ?>, O> D createDao(Class<O> clazz) {
		return GeoPackageDao.createDao(db, clazz);
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
	 * Get the table columns
	 * 
	 * @return columns
	 * @since 3.5.0
	 */
	public List<TColumn> getColumns() {
		return table.getColumns();
	}

	/**
	 * Get the table column names
	 * 
	 * @return column names
	 * @since 3.5.0
	 */
	public String[] getColumnNames() {
		return table.getColumnNames();
	}

	/**
	 * Get the column count
	 * 
	 * @return column count
	 * @since 3.5.0
	 */
	public int columnCount() {
		return table.columnCount();
	}

	/**
	 * Get the contents
	 * 
	 * @return contents
	 * @since 3.3.0
	 */
	public Contents getContents() {
		return table.getContents();
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
	 * Is the primary key modifiable
	 * 
	 * @return true if the primary key is modifiable
	 * @since 4.0.0
	 */
	public boolean isPkModifiable() {
		return table.isPkModifiable();
	}

	/**
	 * Set if the primary key can be modified
	 * 
	 * @param pkModifiable
	 *            primary key modifiable flag
	 * @since 4.0.0
	 */
	public void setPkModifiable(boolean pkModifiable) {
		table.setPkModifiable(pkModifiable);
	}

	/**
	 * Is value validation against column types enabled
	 * 
	 * @return true if values are validated against column types
	 * @since 4.0.0
	 */
	public boolean isValueValidation() {
		return table.isValueValidation();
	}

	/**
	 * Set if values should validated against column types
	 * 
	 * @param valueValidation
	 *            value validation flag
	 * @since 4.0.0
	 */
	public void setValueValidation(boolean valueValidation) {
		table.setValueValidation(valueValidation);
	}

	/**
	 * Drop the user table
	 */
	public void dropTable() {
		CoreSQLUtils.dropTable(db, getTableName());
	}

	/**
	 * Raw query
	 * 
	 * @param sql
	 *            SQL
	 * @return result
	 * @since 3.5.0
	 */
	public TResult rawQuery(String sql) {
		return rawQuery(sql, null);
	}

	/**
	 * Raw query
	 * 
	 * @param sql
	 *            SQL
	 * @param selectionArgs
	 *            selection args
	 * @return result
	 * @since 3.5.0
	 */
	public TResult rawQuery(String sql, String[] selectionArgs) {
		return userDb.rawQuery(sql, selectionArgs);
	}

	/**
	 * Query for all rows
	 * 
	 * @return result
	 */
	public TResult queryForAll() {
		return query();
	}

	/**
	 * Query for all rows
	 * 
	 * @return result
	 * @since 3.5.0
	 */
	public TResult query() {
		return query(false);
	}

	/**
	 * Query for all rows
	 * 
	 * @param distinct
	 *            distinct rows
	 * @return result
	 * @since 4.0.0
	 */
	public TResult query(boolean distinct) {
		return query(distinct, table.getColumnNames());
	}

	/**
	 * Query for all rows
	 * 
	 * @param columns
	 *            columns
	 * 
	 * @return result
	 * @since 3.5.0
	 */
	public TResult query(String[] columns) {
		return query(false, columns);
	}

	/**
	 * Query for all rows
	 * 
	 * @param distinct
	 *            distinct rows
	 * @param columns
	 *            columns
	 * 
	 * @return result
	 * @since 4.0.0
	 */
	public TResult query(boolean distinct, String[] columns) {
		TResult result = userDb.query(distinct, getTableName(), columns, null,
				null, null, null, null);
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
	 * @since 3.5.0
	 */
	public TResult queryAs(String[] columnsAs) {
		return queryAs(false, columnsAs);
	}

	/**
	 * Query for all rows with "columns as" values for corresponding column
	 * indices. Non null values in the array will be used as "as" values for the
	 * corresponding column.
	 * 
	 * @param distinct
	 *            distinct rows
	 * @param columnsAs
	 *            columns as values
	 * @return result
	 * @since 4.0.0
	 */
	public TResult queryAs(boolean distinct, String[] columnsAs) {
		return query(distinct, table.getColumnNames(), columnsAs);
	}

	/**
	 * Query for all rows with "columns as" values for corresponding column
	 * indices. Non null values in the array will be used as "as" values for the
	 * corresponding column.
	 * 
	 * @param columns
	 *            columns
	 * @param columnsAs
	 *            columns as values
	 * @return result
	 * @since 3.5.0
	 */
	public TResult query(String[] columns, String[] columnsAs) {
		return query(false, columns, columnsAs);
	}

	/**
	 * Query for all rows with "columns as" values for corresponding column
	 * indices. Non null values in the array will be used as "as" values for the
	 * corresponding column.
	 * 
	 * @param distinct
	 *            distinct rows
	 * @param columns
	 *            columns
	 * @param columnsAs
	 *            columns as values
	 * @return result
	 * @since 4.0.0
	 */
	public TResult query(boolean distinct, String[] columns,
			String[] columnsAs) {
		TResult result = userDb.query(distinct, getTableName(), columns,
				columnsAs, null, null, null, null, null);
		prepareResult(result);
		return result;
	}

	/**
	 * Query SQL for all rows
	 * 
	 * @return SQL
	 * @since 3.4.0
	 */
	public String querySQL() {
		return querySQL(false);
	}

	/**
	 * Query SQL for all rows
	 * 
	 * @param distinct
	 *            distinct rows
	 * @return SQL
	 * @since 4.0.0
	 */
	public String querySQL(boolean distinct) {
		return querySQL(distinct, table.getColumnNames());
	}

	/**
	 * Query SQL for all row ids
	 * 
	 * @return SQL
	 * @since 3.4.0
	 */
	public String queryIdsSQL() {
		return queryIdsSQL(false);
	}

	/**
	 * Query SQL for all row ids
	 * 
	 * @param distinct
	 *            distinct rows
	 * @return SQL
	 * @since 4.0.0
	 */
	public String queryIdsSQL(boolean distinct) {
		return querySQL(distinct,
				new String[] { table.getPkColumn().getName() });
	}

	/**
	 * Query SQL for all rows
	 * 
	 * @param columns
	 *            columns
	 * @return SQL
	 * @since 3.4.0
	 */
	public String querySQL(String[] columns) {
		return querySQL(false, columns);
	}

	/**
	 * Query SQL for all rows
	 * 
	 * @param distinct
	 *            distinct rows
	 * @param columns
	 *            columns
	 * @return SQL
	 * @since 4.0.0
	 */
	public String querySQL(boolean distinct, String[] columns) {
		return userDb.querySQL(distinct, getTableName(), columns, null, null,
				null, null, null, null);
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
		return queryForEq(false, fieldName, value);
	}

	/**
	 * Query for the row where the field equals the value
	 * 
	 * @param distinct
	 *            distinct rows
	 * @param fieldName
	 *            field name
	 * @param value
	 *            value
	 * @return result
	 * @since 4.0.0
	 */
	public TResult queryForEq(boolean distinct, String fieldName,
			Object value) {
		return queryForEq(distinct, table.getColumnNames(), fieldName, value);
	}

	/**
	 * Query for the row where the field equals the value
	 * 
	 * @param columns
	 *            columns
	 * @param fieldName
	 *            field name
	 * @param value
	 *            value
	 * @return result
	 * @since 3.5.0
	 */
	public TResult queryForEq(String[] columns, String fieldName,
			Object value) {
		return queryForEq(false, columns, fieldName, value);
	}

	/**
	 * Query for the row where the field equals the value
	 * 
	 * @param distinct
	 *            distinct rows
	 * @param columns
	 *            columns
	 * @param fieldName
	 *            field name
	 * @param value
	 *            value
	 * @return result
	 * @since 4.0.0
	 */
	public TResult queryForEq(boolean distinct, String[] columns,
			String fieldName, Object value) {
		return queryForEq(distinct, columns, fieldName, value, null, null,
				null);
	}

	/**
	 * Count where the field equals the value
	 * 
	 * @param fieldName
	 *            field name
	 * @param value
	 *            value
	 * @return count
	 * @since 3.5.0
	 */
	public int countForEq(String fieldName, Object value) {
		return countForEq(false, null, fieldName, value);
	}

	/**
	 * Count where the field equals the value
	 * 
	 * @param column
	 *            count column name
	 * @param fieldName
	 *            field name
	 * @param value
	 *            value
	 * @return count
	 * @since 4.0.0
	 */
	public int countForEq(String column, String fieldName, Object value) {
		return countForEq(false, column, fieldName, value);
	}

	/**
	 * Count where the field equals the value
	 * 
	 * @param distinct
	 *            distinct column values
	 * @param column
	 *            count column name
	 * @param fieldName
	 *            field name
	 * @param value
	 *            value
	 * @return count
	 * @since 4.0.0
	 */
	public int countForEq(boolean distinct, String column, String fieldName,
			Object value) {
		return countForEq(distinct, column, fieldName, value, null, null, null);
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
		return queryForEq(false, fieldName, value, groupBy, having, orderBy);
	}

	/**
	 * Query for the row where the field equals the value
	 * 
	 * @param distinct
	 *            distinct rows
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
	 * @since 4.0.0
	 */
	public TResult queryForEq(boolean distinct, String fieldName, Object value,
			String groupBy, String having, String orderBy) {
		return queryForEq(distinct, table.getColumnNames(), fieldName, value,
				groupBy, having, orderBy);
	}

	/**
	 * Query for the row where the field equals the value
	 * 
	 * @param columns
	 *            columns
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
	 * @since 3.5.0
	 */
	public TResult queryForEq(String[] columns, String fieldName, Object value,
			String groupBy, String having, String orderBy) {
		return queryForEq(false, columns, fieldName, value, groupBy, having,
				orderBy);
	}

	/**
	 * Query for the row where the field equals the value
	 * 
	 * @param distinct
	 *            distinct rows
	 * @param columns
	 *            columns
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
	 * @since 4.0.0
	 */
	public TResult queryForEq(boolean distinct, String[] columns,
			String fieldName, Object value, String groupBy, String having,
			String orderBy) {
		String where = buildWhere(fieldName, value);
		String[] whereArgs = buildWhereArgs(value);
		TResult result = userDb.query(distinct, getTableName(), columns, where,
				whereArgs, groupBy, having, orderBy);
		prepareResult(result);
		return result;
	}

	/**
	 * Count where the field equals the value
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
	 * @return count
	 * @since 3.5.0
	 */
	public int countForEq(String fieldName, Object value, String groupBy,
			String having, String orderBy) {
		return countForEq(false, null, fieldName, value, groupBy, having,
				orderBy);
	}

	/**
	 * Count where the field equals the value
	 * 
	 * @param column
	 *            count column name
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
	 * @return count
	 * @since 4.0.0
	 */
	public int countForEq(String column, String fieldName, Object value,
			String groupBy, String having, String orderBy) {
		return countForEq(false, column, fieldName, value, groupBy, having,
				orderBy);
	}

	/**
	 * Count where the field equals the value
	 * 
	 * @param distinct
	 *            distinct column values
	 * @param column
	 *            count column name
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
	 * @return count
	 * @since 4.0.0
	 */
	public int countForEq(boolean distinct, String column, String fieldName,
			Object value, String groupBy, String having, String orderBy) {
		String where = buildWhere(fieldName, value);
		String[] whereArgs = buildWhereArgs(value);
		return count(distinct, column, where, whereArgs);
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
		return queryForEq(false, fieldName, value);
	}

	/**
	 * Query for the row where the field equals the value
	 * 
	 * @param distinct
	 *            distinct rows
	 * @param fieldName
	 *            field name
	 * @param value
	 *            column value
	 * @return result
	 * @since 4.0.0
	 */
	public TResult queryForEq(boolean distinct, String fieldName,
			ColumnValue value) {
		return queryForEq(distinct, table.getColumnNames(), fieldName, value);
	}

	/**
	 * Query for the row where the field equals the value
	 * 
	 * @param columns
	 *            columns
	 * @param fieldName
	 *            field name
	 * @param value
	 *            column value
	 * @return result
	 * @since 3.5.0
	 */
	public TResult queryForEq(String[] columns, String fieldName,
			ColumnValue value) {
		return queryForEq(false, columns, fieldName, value);
	}

	/**
	 * Query for the row where the field equals the value
	 * 
	 * @param distinct
	 *            distinct rows
	 * @param columns
	 *            columns
	 * @param fieldName
	 *            field name
	 * @param value
	 *            column value
	 * @return result
	 * @since 4.0.0
	 */
	public TResult queryForEq(boolean distinct, String[] columns,
			String fieldName, ColumnValue value) {
		String where = buildWhere(fieldName, value);
		String[] whereArgs = buildWhereArgs(value);
		TResult result = userDb.query(distinct, getTableName(), columns, where,
				whereArgs, null, null, null);
		prepareResult(result);
		return result;
	}

	/**
	 * Count where the field equals the value
	 * 
	 * @param fieldName
	 *            field name
	 * @param value
	 *            column value
	 * @return count
	 * @since 3.5.0
	 */
	public int countForEq(String fieldName, ColumnValue value) {
		return countForEq(false, null, fieldName, value);
	}

	/**
	 * Count where the field equals the value
	 * 
	 * @param column
	 *            count column name
	 * @param fieldName
	 *            field name
	 * @param value
	 *            column value
	 * @return count
	 * @since 4.0.0
	 */
	public int countForEq(String column, String fieldName, ColumnValue value) {
		return countForEq(false, column, fieldName, value);
	}

	/**
	 * Count where the field equals the value
	 * 
	 * @param distinct
	 *            distinct column values
	 * @param column
	 *            count column name
	 * @param fieldName
	 *            field name
	 * @param value
	 *            column value
	 * @return count
	 * @since 4.0.0
	 */
	public int countForEq(boolean distinct, String column, String fieldName,
			ColumnValue value) {
		String where = buildWhere(fieldName, value);
		String[] whereArgs = buildWhereArgs(value);
		return count(distinct, column, where, whereArgs);
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
		return queryForLike(false, fieldName, value);
	}

	/**
	 * Query for the row where the field is like the value
	 * 
	 * @param distinct
	 *            distinct rows
	 * @param fieldName
	 *            field name
	 * @param value
	 *            value
	 * @return result
	 * @since 4.0.0
	 */

	public TResult queryForLike(boolean distinct, String fieldName,
			Object value) {
		return queryForLike(distinct, table.getColumnNames(), fieldName, value);
	}

	/**
	 * Query for the row where the field is like the value
	 * 
	 * @param columns
	 *            columns
	 * @param fieldName
	 *            field name
	 * @param value
	 *            value
	 * @return result
	 * @since 3.5.0
	 */

	public TResult queryForLike(String[] columns, String fieldName,
			Object value) {
		return queryForLike(false, columns, fieldName, value);
	}

	/**
	 * Query for the row where the field is like the value
	 * 
	 * @param distinct
	 *            distinct rows
	 * @param columns
	 *            columns
	 * @param fieldName
	 *            field name
	 * @param value
	 *            value
	 * @return result
	 * @since 4.0.0
	 */

	public TResult queryForLike(boolean distinct, String[] columns,
			String fieldName, Object value) {
		return queryForLike(distinct, columns, fieldName, value, null, null,
				null);
	}

	/**
	 * Count where the field is like the value
	 * 
	 * @param fieldName
	 *            field name
	 * @param value
	 *            value
	 * @return count
	 * @since 3.5.0
	 */

	public int countForLike(String fieldName, Object value) {
		return countForLike(false, null, fieldName, value);
	}

	/**
	 * Count where the field is like the value
	 * 
	 * @param column
	 *            count column name
	 * @param fieldName
	 *            field name
	 * @param value
	 *            value
	 * @return count
	 * @since 4.0.0
	 */

	public int countForLike(String column, String fieldName, Object value) {
		return countForLike(false, column, fieldName, value);
	}

	/**
	 * Count where the field is like the value
	 * 
	 * @param distinct
	 *            distinct column values
	 * @param column
	 *            count column name
	 * @param fieldName
	 *            field name
	 * @param value
	 *            value
	 * @return count
	 * @since 4.0.0
	 */

	public int countForLike(boolean distinct, String column, String fieldName,
			Object value) {
		return countForLike(distinct, column, fieldName, value, null, null,
				null);
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
		return queryForLike(false, fieldName, value, groupBy, having, orderBy);
	}

	/**
	 * Query for the row where the field equals the value
	 * 
	 * @param distinct
	 *            distinct rows
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
	 * @since 4.0.0
	 */
	public TResult queryForLike(boolean distinct, String fieldName,
			Object value, String groupBy, String having, String orderBy) {
		return queryForLike(distinct, table.getColumnNames(), fieldName, value,
				groupBy, having, orderBy);
	}

	/**
	 * Query for the row where the field equals the value
	 * 
	 * @param columns
	 *            columns
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
	 * @since 3.5.0
	 */
	public TResult queryForLike(String[] columns, String fieldName,
			Object value, String groupBy, String having, String orderBy) {
		return queryForLike(false, columns, fieldName, value, groupBy, having,
				orderBy);
	}

	/**
	 * Query for the row where the field equals the value
	 * 
	 * @param distinct
	 *            distinct rows
	 * @param columns
	 *            columns
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
	 * @since 4.0.0
	 */
	public TResult queryForLike(boolean distinct, String[] columns,
			String fieldName, Object value, String groupBy, String having,
			String orderBy) {
		String where = buildWhereLike(fieldName, value);
		String[] whereArgs = buildWhereArgs(value);
		TResult result = userDb.query(distinct, getTableName(), columns, where,
				whereArgs, groupBy, having, orderBy);
		prepareResult(result);
		return result;
	}

	/**
	 * Count where the field equals the value
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
	 * @return count
	 * @since 3.5.0
	 */
	public int countForLike(String fieldName, Object value, String groupBy,
			String having, String orderBy) {
		return countForLike(false, null, fieldName, value, groupBy, having,
				orderBy);
	}

	/**
	 * Count where the field equals the value
	 * 
	 * @param column
	 *            count column name
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
	 * @return count
	 * @since 4.0.0
	 */
	public int countForLike(String column, String fieldName, Object value,
			String groupBy, String having, String orderBy) {
		return countForLike(false, column, fieldName, value, groupBy, having,
				orderBy);
	}

	/**
	 * Count where the field equals the value
	 * 
	 * @param distinct
	 *            distinct column values
	 * @param column
	 *            count column name
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
	 * @return count
	 * @since 4.0.0
	 */
	public int countForLike(boolean distinct, String column, String fieldName,
			Object value, String groupBy, String having, String orderBy) {
		String where = buildWhereLike(fieldName, value);
		String[] whereArgs = buildWhereArgs(value);
		return count(distinct, column, where, whereArgs);
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
		return queryForLike(false, fieldName, value);
	}

	/**
	 * Query for the row where the field is like the value
	 * 
	 * @param distinct
	 *            distinct rows
	 * @param fieldName
	 *            field name
	 * @param value
	 *            column value
	 * @return result
	 * @since 4.0.0
	 */
	public TResult queryForLike(boolean distinct, String fieldName,
			ColumnValue value) {
		return queryForLike(distinct, table.getColumnNames(), fieldName, value);
	}

	/**
	 * Query for the row where the field is like the value
	 * 
	 * @param columns
	 *            columns
	 * @param fieldName
	 *            field name
	 * @param value
	 *            column value
	 * @return result
	 * @since 3.5.0
	 */
	public TResult queryForLike(String[] columns, String fieldName,
			ColumnValue value) {
		return queryForLike(false, columns, fieldName, value);
	}

	/**
	 * Query for the row where the field is like the value
	 * 
	 * @param distinct
	 *            distinct rows
	 * @param columns
	 *            columns
	 * @param fieldName
	 *            field name
	 * @param value
	 *            column value
	 * @return result
	 * @since 4.0.0
	 */
	public TResult queryForLike(boolean distinct, String[] columns,
			String fieldName, ColumnValue value) {
		String where = buildWhereLike(fieldName, value);
		String[] whereArgs = buildWhereArgs(value);
		TResult result = userDb.query(distinct, getTableName(), columns, where,
				whereArgs, null, null, null);
		prepareResult(result);
		return result;
	}

	/**
	 * Count where the field is like the value
	 * 
	 * @param fieldName
	 *            field name
	 * @param value
	 *            column value
	 * @return count
	 * @since 3.5.0
	 */
	public int countForLike(String fieldName, ColumnValue value) {
		return countForLike(false, null, fieldName, value);
	}

	/**
	 * Count where the field is like the value
	 * 
	 * @param column
	 *            count column name
	 * @param fieldName
	 *            field name
	 * @param value
	 *            column value
	 * @return count
	 * @since 4.0.0
	 */
	public int countForLike(String column, String fieldName,
			ColumnValue value) {
		return countForLike(false, column, fieldName, value);
	}

	/**
	 * Count where the field is like the value
	 * 
	 * @param distinct
	 *            distinct column values
	 * @param column
	 *            count column name
	 * @param fieldName
	 *            field name
	 * @param value
	 *            column value
	 * @return count
	 * @since 4.0.0
	 */
	public int countForLike(boolean distinct, String column, String fieldName,
			ColumnValue value) {
		String where = buildWhereLike(fieldName, value);
		String[] whereArgs = buildWhereArgs(value);
		return count(distinct, column, where, whereArgs);
	}

	/**
	 * Query for the row where all fields match their values
	 * 
	 * @param fieldValues
	 *            field values
	 * @return result
	 */
	public TResult queryForFieldValues(Map<String, Object> fieldValues) {
		return queryForFieldValues(false, fieldValues);
	}

	/**
	 * Query for the row where all fields match their values
	 * 
	 * @param distinct
	 *            distinct rows
	 * @param fieldValues
	 *            field values
	 * @return result
	 * @since 4.0.0
	 */
	public TResult queryForFieldValues(boolean distinct,
			Map<String, Object> fieldValues) {
		return queryForFieldValues(distinct, table.getColumnNames(),
				fieldValues);
	}

	/**
	 * Query for the row where all fields match their values
	 * 
	 * @param columns
	 *            columns
	 * @param fieldValues
	 *            field values
	 * @return result
	 * @since 3.5.0
	 */
	public TResult queryForFieldValues(String[] columns,
			Map<String, Object> fieldValues) {
		return queryForFieldValues(false, columns, fieldValues);
	}

	/**
	 * Query for the row where all fields match their values
	 * 
	 * @param distinct
	 *            distinct rows
	 * @param columns
	 *            columns
	 * @param fieldValues
	 *            field values
	 * @return result
	 * @since 4.0.0
	 */
	public TResult queryForFieldValues(boolean distinct, String[] columns,
			Map<String, Object> fieldValues) {
		String where = buildWhere(fieldValues.entrySet());
		String[] whereArgs = buildWhereArgs(fieldValues.values());
		TResult result = userDb.query(distinct, getTableName(), columns, where,
				whereArgs, null, null, null);
		prepareResult(result);
		return result;
	}

	/**
	 * Count where all fields match their values
	 * 
	 * @param fieldValues
	 *            field values
	 * @return count
	 * @since 3.5.0
	 */
	public int countForFieldValues(Map<String, Object> fieldValues) {
		return countForFieldValues(false, null, fieldValues);
	}

	/**
	 * Count where all fields match their values
	 * 
	 * @param column
	 *            count column name
	 * @param fieldValues
	 *            field values
	 * @return count
	 * @since 4.0.0
	 */
	public int countForFieldValues(String column,
			Map<String, Object> fieldValues) {
		return countForFieldValues(false, column, fieldValues);
	}

	/**
	 * Count where all fields match their values
	 * 
	 * @param distinct
	 *            distinct column values
	 * @param column
	 *            count column name
	 * @param fieldValues
	 *            field values
	 * @return count
	 * @since 4.0.0
	 */
	public int countForFieldValues(boolean distinct, String column,
			Map<String, Object> fieldValues) {
		String where = buildWhere(fieldValues.entrySet());
		String[] whereArgs = buildWhereArgs(fieldValues.values());
		return count(distinct, column, where, whereArgs);
	}

	/**
	 * Query for the row where all fields match their values
	 * 
	 * @param fieldValues
	 *            field values
	 * @return result
	 */
	public TResult queryForValueFieldValues(
			Map<String, ColumnValue> fieldValues) {
		return queryForValueFieldValues(false, fieldValues);
	}

	/**
	 * Query for the row where all fields match their values
	 * 
	 * @param distinct
	 *            distinct rows
	 * @param fieldValues
	 *            field values
	 * @return result
	 * @since 4.0.0
	 */
	public TResult queryForValueFieldValues(boolean distinct,
			Map<String, ColumnValue> fieldValues) {
		return queryForValueFieldValues(distinct, table.getColumnNames(),
				fieldValues);
	}

	/**
	 * Query for the row where all fields match their values
	 * 
	 * @param columns
	 *            columns
	 * @param fieldValues
	 *            field values
	 * @return result
	 * @since 3.5.0
	 */
	public TResult queryForValueFieldValues(String[] columns,
			Map<String, ColumnValue> fieldValues) {
		return queryForValueFieldValues(false, columns, fieldValues);
	}

	/**
	 * Query for the row where all fields match their values
	 * 
	 * @param distinct
	 *            distinct rows
	 * @param columns
	 *            columns
	 * @param fieldValues
	 *            field values
	 * @return result
	 * @since 4.0.0
	 */
	public TResult queryForValueFieldValues(boolean distinct, String[] columns,
			Map<String, ColumnValue> fieldValues) {
		String where = buildValueWhere(fieldValues.entrySet());
		String[] whereArgs = buildValueWhereArgs(fieldValues.values());
		TResult result = userDb.query(distinct, getTableName(), columns, where,
				whereArgs, null, null, null);
		prepareResult(result);
		return result;
	}

	/**
	 * Count where all fields match their values
	 * 
	 * @param fieldValues
	 *            field values
	 * @return count
	 * @since 3.5.0
	 */
	public int countForValueFieldValues(Map<String, ColumnValue> fieldValues) {
		return countForValueFieldValues(false, null, fieldValues);
	}

	/**
	 * Count where all fields match their values
	 * 
	 * @param column
	 *            count column name
	 * @param fieldValues
	 *            field values
	 * @return count
	 * @since 4.0.0
	 */
	public int countForValueFieldValues(String column,
			Map<String, ColumnValue> fieldValues) {
		return countForValueFieldValues(false, column, fieldValues);
	}

	/**
	 * Count where all fields match their values
	 * 
	 * @param distinct
	 *            distinct column values
	 * @param column
	 *            count column name
	 * @param fieldValues
	 *            field values
	 * @return count
	 * @since 4.0.0
	 */
	public int countForValueFieldValues(boolean distinct, String column,
			Map<String, ColumnValue> fieldValues) {
		String where = buildValueWhere(fieldValues.entrySet());
		String[] whereArgs = buildValueWhereArgs(fieldValues.values());
		return count(distinct, column, where, whereArgs);
	}

	/**
	 * Query for the row with the provided id
	 * 
	 * @param id
	 *            id
	 * @return result
	 */
	public TResult queryForId(long id) {
		return queryForId(false, id);
	}

	/**
	 * Query for the row with the provided id
	 * 
	 * @param distinct
	 *            distinct rows
	 * @param id
	 *            id
	 * @return result
	 * @since 4.0.0
	 */
	public TResult queryForId(boolean distinct, long id) {
		return queryForId(distinct, table.getColumnNames(), id);
	}

	/**
	 * Query for the row with the provided id
	 * 
	 * @param columns
	 *            columns
	 * @param id
	 *            id
	 * @return result
	 * @since 3.5.0
	 */
	public TResult queryForId(String[] columns, long id) {
		return queryForId(false, columns, id);
	}

	/**
	 * Query for the row with the provided id
	 * 
	 * @param distinct
	 *            distinct rows
	 * @param columns
	 *            columns
	 * @param id
	 *            id
	 * @return result
	 * @since 4.0.0
	 */
	public TResult queryForId(boolean distinct, String[] columns, long id) {
		String where = getPkWhere(id);
		String[] whereArgs = getPkWhereArgs(id);
		TResult result = userDb.query(distinct, getTableName(), columns, where,
				whereArgs, null, null, null);
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
		return queryForIdRow(false, id);
	}

	/**
	 * Query for the row with the provided id
	 * 
	 * @param distinct
	 *            distinct rows
	 * @param id
	 *            id
	 * @return row
	 * @since 4.0.0
	 */
	public TRow queryForIdRow(boolean distinct, long id) {
		return queryForIdRow(distinct, table.getColumnNames(), id);
	}

	/**
	 * Query for the row with the provided id
	 * 
	 * @param columns
	 *            columns
	 * @param id
	 *            id
	 * @return row
	 * @since 3.5.0
	 */
	public TRow queryForIdRow(String[] columns, long id) {
		return queryForIdRow(false, columns, id);
	}

	/**
	 * Query for the row with the provided id
	 * 
	 * @param distinct
	 *            distinct rows
	 * @param columns
	 *            columns
	 * @param id
	 *            id
	 * @return row
	 * @since 4.0.0
	 */
	public TRow queryForIdRow(boolean distinct, String[] columns, long id) {
		TRow row = null;
		TResult readCursor = queryForId(distinct, columns, id);
		if (readCursor.moveToNext()) {
			row = readCursor.getRow();
		}
		readCursor.close();
		return row;
	}

	/**
	 * Query for ids in the nested SQL query
	 * 
	 * @param nestedSQL
	 *            nested SQL
	 * @return result
	 * @since 3.4.0
	 */
	public TResult queryIn(String nestedSQL) {
		return queryIn(false, nestedSQL);
	}

	/**
	 * Query for ids in the nested SQL query
	 * 
	 * @param distinct
	 *            distinct rows
	 * @param nestedSQL
	 *            nested SQL
	 * @return result
	 * @since 4.0.0
	 */
	public TResult queryIn(boolean distinct, String nestedSQL) {
		return queryIn(distinct, table.getColumnNames(), nestedSQL);
	}

	/**
	 * Query for ids in the nested SQL query
	 * 
	 * @param columns
	 *            columns
	 * @param nestedSQL
	 *            nested SQL
	 * @return result
	 * @since 3.5.0
	 */
	public TResult queryIn(String[] columns, String nestedSQL) {
		return queryIn(false, columns, nestedSQL);
	}

	/**
	 * Query for ids in the nested SQL query
	 * 
	 * @param distinct
	 *            distinct rows
	 * @param columns
	 *            columns
	 * @param nestedSQL
	 *            nested SQL
	 * @return result
	 * @since 4.0.0
	 */
	public TResult queryIn(boolean distinct, String[] columns,
			String nestedSQL) {
		return queryIn(distinct, columns, nestedSQL, null, null, null);
	}

	/**
	 * Get the count in the nested SQL query
	 * 
	 * @param nestedSQL
	 *            nested SQL
	 * @return count
	 * @since 3.4.0
	 */
	public int countIn(String nestedSQL) {
		return countIn(false, null, nestedSQL);
	}

	/**
	 * Get the count in the nested SQL query
	 * 
	 * @param column
	 *            count column name
	 * @param nestedSQL
	 *            nested SQL
	 * @return count
	 * @since 4.0.0
	 */
	public int countColumnIn(String column, String nestedSQL) {
		return countIn(false, column, nestedSQL);
	}

	/**
	 * Get the count in the nested SQL query
	 * 
	 * @param distinct
	 *            distinct column values
	 * @param column
	 *            count column name
	 * @param nestedSQL
	 *            nested SQL
	 * @return count
	 * @since 4.0.0
	 */
	public int countIn(boolean distinct, String column, String nestedSQL) {
		return countIn(distinct, column, nestedSQL, null, null, null);
	}

	/**
	 * Query for ids in the nested SQL query
	 * 
	 * @param nestedSQL
	 *            nested SQL
	 * @param nestedArgs
	 *            nested SQL args
	 * @return result
	 * @since 3.4.0
	 */
	public TResult queryIn(String nestedSQL, String[] nestedArgs) {
		return queryIn(false, nestedSQL, nestedArgs);
	}

	/**
	 * Query for ids in the nested SQL query
	 * 
	 * @param distinct
	 *            distinct rows
	 * @param nestedSQL
	 *            nested SQL
	 * @param nestedArgs
	 *            nested SQL args
	 * @return result
	 * @since 4.0.0
	 */
	public TResult queryIn(boolean distinct, String nestedSQL,
			String[] nestedArgs) {
		return queryIn(distinct, table.getColumnNames(), nestedSQL, nestedArgs);
	}

	/**
	 * Query for ids in the nested SQL query
	 * 
	 * @param columns
	 *            columns
	 * @param nestedSQL
	 *            nested SQL
	 * @param nestedArgs
	 *            nested SQL args
	 * @return result
	 * @since 3.5.0
	 */
	public TResult queryIn(String[] columns, String nestedSQL,
			String[] nestedArgs) {
		return queryIn(false, columns, nestedSQL, nestedArgs);
	}

	/**
	 * Query for ids in the nested SQL query
	 * 
	 * @param distinct
	 *            distinct rows
	 * @param columns
	 *            columns
	 * @param nestedSQL
	 *            nested SQL
	 * @param nestedArgs
	 *            nested SQL args
	 * @return result
	 * @since 4.0.0
	 */
	public TResult queryIn(boolean distinct, String[] columns, String nestedSQL,
			String[] nestedArgs) {
		return queryIn(distinct, columns, nestedSQL, nestedArgs, null, null);
	}

	/**
	 * Get the count in the nested SQL query
	 * 
	 * @param nestedSQL
	 *            nested SQL
	 * @param nestedArgs
	 *            nested SQL args
	 * @return count
	 * @since 3.4.0
	 */
	public int countIn(String nestedSQL, String[] nestedArgs) {
		return countIn(false, null, nestedSQL, nestedArgs);
	}

	/**
	 * Get the count in the nested SQL query
	 * 
	 * @param column
	 *            count column name
	 * @param nestedSQL
	 *            nested SQL
	 * @param nestedArgs
	 *            nested SQL args
	 * @return count
	 * @since 4.0.0
	 */
	public int countColumnIn(String column, String nestedSQL,
			String[] nestedArgs) {
		return countIn(false, column, nestedSQL, nestedArgs);
	}

	/**
	 * Get the count in the nested SQL query
	 * 
	 * @param distinct
	 *            distinct column values
	 * @param column
	 *            count column name
	 * @param nestedSQL
	 *            nested SQL
	 * @param nestedArgs
	 *            nested SQL args
	 * @return count
	 * @since 4.0.0
	 */
	public int countIn(boolean distinct, String column, String nestedSQL,
			String[] nestedArgs) {
		return countIn(distinct, column, nestedSQL, nestedArgs, null, null);
	}

	/**
	 * Query for ids in the nested SQL query
	 * 
	 * @param nestedSQL
	 *            nested SQL
	 * @param fieldValues
	 *            field values
	 * @return result
	 * @since 3.4.0
	 */
	public TResult queryIn(String nestedSQL, Map<String, Object> fieldValues) {
		return queryIn(false, nestedSQL, fieldValues);
	}

	/**
	 * Query for ids in the nested SQL query
	 * 
	 * @param distinct
	 *            distinct rows
	 * @param nestedSQL
	 *            nested SQL
	 * @param fieldValues
	 *            field values
	 * @return result
	 * @since 4.0.0
	 */
	public TResult queryIn(boolean distinct, String nestedSQL,
			Map<String, Object> fieldValues) {
		return queryIn(distinct, table.getColumnNames(), nestedSQL,
				fieldValues);
	}

	/**
	 * Query for ids in the nested SQL query
	 * 
	 * @param columns
	 *            columns
	 * @param nestedSQL
	 *            nested SQL
	 * @param fieldValues
	 *            field values
	 * @return result
	 * @since 3.5.0
	 */
	public TResult queryIn(String[] columns, String nestedSQL,
			Map<String, Object> fieldValues) {
		return queryIn(false, columns, nestedSQL, fieldValues);
	}

	/**
	 * Query for ids in the nested SQL query
	 * 
	 * @param distinct
	 *            distinct rows
	 * @param columns
	 *            columns
	 * @param nestedSQL
	 *            nested SQL
	 * @param fieldValues
	 *            field values
	 * @return result
	 * @since 4.0.0
	 */
	public TResult queryIn(boolean distinct, String[] columns, String nestedSQL,
			Map<String, Object> fieldValues) {
		return queryIn(distinct, columns, nestedSQL, null, fieldValues);
	}

	/**
	 * Get the count in the nested SQL query
	 * 
	 * @param nestedSQL
	 *            nested SQL
	 * @param fieldValues
	 *            field values
	 * @return count
	 * @since 3.4.0
	 */
	public int countIn(String nestedSQL, Map<String, Object> fieldValues) {
		return countIn(false, null, nestedSQL, fieldValues);
	}

	/**
	 * Get the count in the nested SQL query
	 * 
	 * @param column
	 *            count column name
	 * @param nestedSQL
	 *            nested SQL
	 * @param fieldValues
	 *            field values
	 * @return count
	 * @since 4.0.0
	 */
	public int countIn(String column, String nestedSQL,
			Map<String, Object> fieldValues) {
		return countIn(false, column, nestedSQL, fieldValues);
	}

	/**
	 * Get the count in the nested SQL query
	 * 
	 * @param distinct
	 *            distinct column values
	 * @param column
	 *            count column name
	 * @param nestedSQL
	 *            nested SQL
	 * @param fieldValues
	 *            field values
	 * @return count
	 * @since 4.0.0
	 */
	public int countIn(boolean distinct, String column, String nestedSQL,
			Map<String, Object> fieldValues) {
		return countIn(distinct, column, nestedSQL, null, fieldValues);
	}

	/**
	 * Query for ids in the nested SQL query
	 * 
	 * @param nestedSQL
	 *            nested SQL
	 * @param nestedArgs
	 *            nested SQL args
	 * @param fieldValues
	 *            field values
	 * @return result
	 * @since 3.4.0
	 */
	public TResult queryIn(String nestedSQL, String[] nestedArgs,
			Map<String, Object> fieldValues) {
		return queryIn(false, nestedSQL, nestedArgs, fieldValues);
	}

	/**
	 * Query for ids in the nested SQL query
	 * 
	 * @param distinct
	 *            distinct rows
	 * @param nestedSQL
	 *            nested SQL
	 * @param nestedArgs
	 *            nested SQL args
	 * @param fieldValues
	 *            field values
	 * @return result
	 * @since 4.0.0
	 */
	public TResult queryIn(boolean distinct, String nestedSQL,
			String[] nestedArgs, Map<String, Object> fieldValues) {
		return queryIn(distinct, table.getColumnNames(), nestedSQL, nestedArgs,
				fieldValues);
	}

	/**
	 * Query for ids in the nested SQL query
	 * 
	 * @param columns
	 *            columns
	 * @param nestedSQL
	 *            nested SQL
	 * @param nestedArgs
	 *            nested SQL args
	 * @param fieldValues
	 *            field values
	 * @return result
	 * @since 3.5.0
	 */
	public TResult queryIn(String[] columns, String nestedSQL,
			String[] nestedArgs, Map<String, Object> fieldValues) {
		return queryIn(false, columns, nestedSQL, nestedArgs, fieldValues);
	}

	/**
	 * Query for ids in the nested SQL query
	 * 
	 * @param distinct
	 *            distinct rows
	 * @param columns
	 *            columns
	 * @param nestedSQL
	 *            nested SQL
	 * @param nestedArgs
	 *            nested SQL args
	 * @param fieldValues
	 *            field values
	 * @return result
	 * @since 4.0.0
	 */
	public TResult queryIn(boolean distinct, String[] columns, String nestedSQL,
			String[] nestedArgs, Map<String, Object> fieldValues) {
		String where = buildWhere(fieldValues.entrySet());
		String[] whereArgs = buildWhereArgs(fieldValues.values());
		return queryIn(distinct, columns, nestedSQL, nestedArgs, where,
				whereArgs);
	}

	/**
	 * Get the count in the nested SQL query
	 * 
	 * @param nestedSQL
	 *            nested SQL
	 * @param nestedArgs
	 *            nested SQL args
	 * @param fieldValues
	 *            field values
	 * @return count
	 * @since 3.4.0
	 */
	public int countIn(String nestedSQL, String[] nestedArgs,
			Map<String, Object> fieldValues) {
		return countIn(false, null, nestedSQL, nestedArgs, fieldValues);
	}

	/**
	 * Get the count in the nested SQL query
	 * 
	 * @param column
	 *            count column name
	 * @param nestedSQL
	 *            nested SQL
	 * @param nestedArgs
	 *            nested SQL args
	 * @param fieldValues
	 *            field values
	 * @return count
	 * @since 4.0.0
	 */
	public int countIn(String column, String nestedSQL, String[] nestedArgs,
			Map<String, Object> fieldValues) {
		return countIn(false, column, nestedSQL, nestedArgs, fieldValues);
	}

	/**
	 * Get the count in the nested SQL query
	 * 
	 * @param distinct
	 *            distinct column values
	 * @param column
	 *            count column name
	 * @param nestedSQL
	 *            nested SQL
	 * @param nestedArgs
	 *            nested SQL args
	 * @param fieldValues
	 *            field values
	 * @return count
	 * @since 4.0.0
	 */
	public int countIn(boolean distinct, String column, String nestedSQL,
			String[] nestedArgs, Map<String, Object> fieldValues) {
		String where = buildWhere(fieldValues.entrySet());
		String[] whereArgs = buildWhereArgs(fieldValues.values());
		return countIn(distinct, column, nestedSQL, nestedArgs, where,
				whereArgs);
	}

	/**
	 * Query for ids in the nested SQL query
	 * 
	 * @param nestedSQL
	 *            nested SQL
	 * @param nestedArgs
	 *            nested SQL args
	 * @param where
	 *            where clause
	 * @return result
	 * @since 3.4.0
	 */
	public TResult queryIn(String nestedSQL, String[] nestedArgs,
			String where) {
		return queryIn(false, nestedSQL, nestedArgs, where);
	}

	/**
	 * Query for ids in the nested SQL query
	 * 
	 * @param distinct
	 *            distinct rows
	 * @param nestedSQL
	 *            nested SQL
	 * @param nestedArgs
	 *            nested SQL args
	 * @param where
	 *            where clause
	 * @return result
	 * @since 4.0.0
	 */
	public TResult queryIn(boolean distinct, String nestedSQL,
			String[] nestedArgs, String where) {
		return queryIn(distinct, table.getColumnNames(), nestedSQL, nestedArgs,
				where);
	}

	/**
	 * Query for ids in the nested SQL query
	 * 
	 * @param columns
	 *            columns
	 * @param nestedSQL
	 *            nested SQL
	 * @param nestedArgs
	 *            nested SQL args
	 * @param where
	 *            where clause
	 * @return result
	 * @since 3.5.0
	 */
	public TResult queryIn(String[] columns, String nestedSQL,
			String[] nestedArgs, String where) {
		return queryIn(false, columns, nestedSQL, nestedArgs, where);
	}

	/**
	 * Query for ids in the nested SQL query
	 * 
	 * @param distinct
	 *            distinct rows
	 * @param columns
	 *            columns
	 * @param nestedSQL
	 *            nested SQL
	 * @param nestedArgs
	 *            nested SQL args
	 * @param where
	 *            where clause
	 * @return result
	 * @since 4.0.0
	 */
	public TResult queryIn(boolean distinct, String[] columns, String nestedSQL,
			String[] nestedArgs, String where) {
		return queryIn(distinct, columns, nestedSQL, nestedArgs, where, null);
	}

	/**
	 * Get the count in the nested SQL query
	 * 
	 * @param nestedSQL
	 *            nested SQL
	 * @param nestedArgs
	 *            nested SQL args
	 * @param where
	 *            where clause
	 * @return count
	 * @since 3.4.0
	 */
	public int countIn(String nestedSQL, String[] nestedArgs, String where) {
		return countIn(false, null, nestedSQL, nestedArgs, where);
	}

	/**
	 * Get the count in the nested SQL query
	 * 
	 * @param column
	 *            count column name
	 * @param nestedSQL
	 *            nested SQL
	 * @param nestedArgs
	 *            nested SQL args
	 * @param where
	 *            where clause
	 * @return count
	 * @since 4.0.0
	 */
	public int countIn(String column, String nestedSQL, String[] nestedArgs,
			String where) {
		return countIn(false, column, nestedSQL, nestedArgs, where);
	}

	/**
	 * Get the count in the nested SQL query
	 * 
	 * @param distinct
	 *            distinct column values
	 * @param column
	 *            count column name
	 * @param nestedSQL
	 *            nested SQL
	 * @param nestedArgs
	 *            nested SQL args
	 * @param where
	 *            where clause
	 * @return count
	 * @since 4.0.0
	 */
	public int countIn(boolean distinct, String column, String nestedSQL,
			String[] nestedArgs, String where) {
		return countIn(distinct, column, nestedSQL, nestedArgs, where, null);
	}

	/**
	 * Query for ids in the nested SQL query
	 * 
	 * @param nestedSQL
	 *            nested SQL
	 * @param where
	 *            where clause
	 * @return result
	 * @since 3.4.0
	 */
	public TResult queryIn(String nestedSQL, String where) {
		return queryIn(false, nestedSQL, where);
	}

	/**
	 * Query for ids in the nested SQL query
	 * 
	 * @param distinct
	 *            distinct rows
	 * @param nestedSQL
	 *            nested SQL
	 * @param where
	 *            where clause
	 * @return result
	 * @since 4.0.0
	 */
	public TResult queryIn(boolean distinct, String nestedSQL, String where) {
		return queryIn(distinct, table.getColumnNames(), nestedSQL, where);
	}

	/**
	 * Query for ids in the nested SQL query
	 * 
	 * @param columns
	 *            columns
	 * @param nestedSQL
	 *            nested SQL
	 * @param where
	 *            where clause
	 * @return result
	 * @since 3.5.0
	 */
	public TResult queryIn(String[] columns, String nestedSQL, String where) {
		return queryIn(false, columns, nestedSQL, where);
	}

	/**
	 * Query for ids in the nested SQL query
	 * 
	 * @param distinct
	 *            distinct rows
	 * @param columns
	 *            columns
	 * @param nestedSQL
	 *            nested SQL
	 * @param where
	 *            where clause
	 * @return result
	 * @since 4.0.0
	 */
	public TResult queryIn(boolean distinct, String[] columns, String nestedSQL,
			String where) {
		return queryIn(distinct, columns, nestedSQL, null, where, null);
	}

	/**
	 * Get the count in the nested SQL query
	 * 
	 * @param nestedSQL
	 *            nested SQL
	 * @param where
	 *            where clause
	 * @return count
	 * @since 3.4.0
	 */
	public int countIn(String nestedSQL, String where) {
		return countIn(false, null, nestedSQL, where);
	}

	/**
	 * Get the count in the nested SQL query
	 * 
	 * @param column
	 *            count column name
	 * @param nestedSQL
	 *            nested SQL
	 * @param where
	 *            where clause
	 * @return count
	 * @since 4.0.0
	 */
	public int countIn(String column, String nestedSQL, String where) {
		return countIn(false, column, nestedSQL, where);
	}

	/**
	 * Get the count in the nested SQL query
	 * 
	 * @param distinct
	 *            distinct column values
	 * @param column
	 *            count column name
	 * @param nestedSQL
	 *            nested SQL
	 * @param where
	 *            where clause
	 * @return count
	 * @since 4.0.0
	 */
	public int countIn(boolean distinct, String column, String nestedSQL,
			String where) {
		return countIn(distinct, column, nestedSQL, null, where, null);
	}

	/**
	 * Query for ids in the nested SQL query
	 * 
	 * @param nestedSQL
	 *            nested SQL
	 * @param where
	 *            where clause
	 * @param whereArgs
	 *            where arguments
	 * @return result
	 * @since 3.4.0
	 */
	public TResult queryIn(String nestedSQL, String where, String[] whereArgs) {
		return queryIn(false, nestedSQL, where, whereArgs);
	}

	/**
	 * Query for ids in the nested SQL query
	 * 
	 * @param distinct
	 *            distinct rows
	 * @param nestedSQL
	 *            nested SQL
	 * @param where
	 *            where clause
	 * @param whereArgs
	 *            where arguments
	 * @return result
	 * @since 4.0.0
	 */
	public TResult queryIn(boolean distinct, String nestedSQL, String where,
			String[] whereArgs) {
		return queryIn(distinct, table.getColumnNames(), nestedSQL, where,
				whereArgs);
	}

	/**
	 * Query for ids in the nested SQL query
	 * 
	 * @param columns
	 *            columns
	 * @param nestedSQL
	 *            nested SQL
	 * @param where
	 *            where clause
	 * @param whereArgs
	 *            where arguments
	 * @return result
	 * @since 3.5.0
	 */
	public TResult queryIn(String[] columns, String nestedSQL, String where,
			String[] whereArgs) {
		return queryIn(false, columns, nestedSQL, where, whereArgs);
	}

	/**
	 * Query for ids in the nested SQL query
	 * 
	 * @param distinct
	 *            distinct rows
	 * @param columns
	 *            columns
	 * @param nestedSQL
	 *            nested SQL
	 * @param where
	 *            where clause
	 * @param whereArgs
	 *            where arguments
	 * @return result
	 * @since 4.0.0
	 */
	public TResult queryIn(boolean distinct, String[] columns, String nestedSQL,
			String where, String[] whereArgs) {
		return queryIn(distinct, columns, nestedSQL, null, where, whereArgs);
	}

	/**
	 * Get the count in the nested SQL query
	 * 
	 * @param nestedSQL
	 *            nested SQL
	 * @param where
	 *            where clause
	 * @param whereArgs
	 *            where arguments
	 * @return count
	 * @since 3.4.0
	 */
	public int countIn(String nestedSQL, String where, String[] whereArgs) {
		return countIn(false, null, nestedSQL, where, whereArgs);
	}

	/**
	 * Get the count in the nested SQL query
	 * 
	 * @param column
	 *            count column name
	 * @param nestedSQL
	 *            nested SQL
	 * @param where
	 *            where clause
	 * @param whereArgs
	 *            where arguments
	 * @return count
	 * @since 4.0.0
	 */
	public int countIn(String column, String nestedSQL, String where,
			String[] whereArgs) {
		return countIn(false, column, nestedSQL, where, whereArgs);
	}

	/**
	 * Get the count in the nested SQL query
	 * 
	 * @param distinct
	 *            distinct column values
	 * @param column
	 *            count column name
	 * @param nestedSQL
	 *            nested SQL
	 * @param where
	 *            where clause
	 * @param whereArgs
	 *            where arguments
	 * @return count
	 * @since 4.0.0
	 */
	public int countIn(boolean distinct, String column, String nestedSQL,
			String where, String[] whereArgs) {
		return countIn(distinct, column, nestedSQL, null, where, whereArgs);
	}

	/**
	 * Query for ids in the nested SQL query
	 * 
	 * @param nestedSQL
	 *            nested SQL
	 * @param nestedArgs
	 *            nested SQL args
	 * @param where
	 *            where clause
	 * @param whereArgs
	 *            where arguments
	 * @return result
	 * @since 3.4.0
	 */
	public TResult queryIn(String nestedSQL, String[] nestedArgs, String where,
			String[] whereArgs) {
		return queryIn(false, nestedSQL, nestedArgs, where, whereArgs);
	}

	/**
	 * Query for ids in the nested SQL query
	 * 
	 * @param distinct
	 *            distinct rows
	 * @param nestedSQL
	 *            nested SQL
	 * @param nestedArgs
	 *            nested SQL args
	 * @param where
	 *            where clause
	 * @param whereArgs
	 *            where arguments
	 * @return result
	 * @since 4.0.0
	 */
	public TResult queryIn(boolean distinct, String nestedSQL,
			String[] nestedArgs, String where, String[] whereArgs) {
		return queryIn(distinct, table.getColumnNames(), nestedSQL, nestedArgs,
				where, whereArgs);
	}

	/**
	 * Query for ids in the nested SQL query
	 * 
	 * @param columns
	 *            columns
	 * @param nestedSQL
	 *            nested SQL
	 * @param nestedArgs
	 *            nested SQL args
	 * @param where
	 *            where clause
	 * @param whereArgs
	 *            where arguments
	 * @return result
	 * @since 3.5.0
	 */
	public TResult queryIn(String[] columns, String nestedSQL,
			String[] nestedArgs, String where, String[] whereArgs) {
		return queryIn(false, columns, nestedSQL, nestedArgs, where, whereArgs);
	}

	/**
	 * Query for ids in the nested SQL query
	 * 
	 * @param distinct
	 *            distinct rows
	 * @param columns
	 *            columns
	 * @param nestedSQL
	 *            nested SQL
	 * @param nestedArgs
	 *            nested SQL args
	 * @param where
	 *            where clause
	 * @param whereArgs
	 *            where arguments
	 * @return result
	 * @since 4.0.0
	 */
	public TResult queryIn(boolean distinct, String[] columns, String nestedSQL,
			String[] nestedArgs, String where, String[] whereArgs) {
		String whereClause = buildWhereIn(nestedSQL, where);
		String[] args = buildWhereInArgs(nestedArgs, whereArgs);
		return query(distinct, columns, whereClause, args);
	}

	/**
	 * Get the count in the nested SQL query
	 * 
	 * @param nestedSQL
	 *            nested SQL
	 * @param nestedArgs
	 *            nested SQL args
	 * @param where
	 *            where clause
	 * @param whereArgs
	 *            where arguments
	 * @return count
	 * @since 3.4.0
	 */
	public int countIn(String nestedSQL, String[] nestedArgs, String where,
			String[] whereArgs) {
		return countIn(false, null, nestedSQL, nestedArgs, where, whereArgs);
	}

	/**
	 * Get the count in the nested SQL query
	 * 
	 * @param column
	 *            count column name
	 * @param nestedSQL
	 *            nested SQL
	 * @param nestedArgs
	 *            nested SQL args
	 * @param where
	 *            where clause
	 * @param whereArgs
	 *            where arguments
	 * @return count
	 * @since 4.0.0
	 */
	public int countIn(String column, String nestedSQL, String[] nestedArgs,
			String where, String[] whereArgs) {
		return countIn(false, column, nestedSQL, nestedArgs, where, whereArgs);
	}

	/**
	 * Get the count in the nested SQL query
	 * 
	 * @param distinct
	 *            distinct column values
	 * @param column
	 *            count column name
	 * @param nestedSQL
	 *            nested SQL
	 * @param nestedArgs
	 *            nested SQL args
	 * @param where
	 *            where clause
	 * @param whereArgs
	 *            where arguments
	 * @return count
	 * @since 4.0.0
	 */
	public int countIn(boolean distinct, String column, String nestedSQL,
			String[] nestedArgs, String where, String[] whereArgs) {
		String whereClause = buildWhereIn(nestedSQL, where);
		String[] args = buildWhereInArgs(nestedArgs, whereArgs);
		return count(distinct, column, whereClause, args);
	}

	/**
	 * Query for rows
	 * 
	 * @param where
	 *            where clause
	 * @return result
	 * @since 3.4.0
	 */
	public TResult query(String where) {
		return query(false, where);
	}

	/**
	 * Query for rows
	 * 
	 * @param distinct
	 *            distinct rows
	 * @param where
	 *            where clause
	 * @return result
	 * @since 4.0.0
	 */
	public TResult query(boolean distinct, String where) {
		return query(distinct, table.getColumnNames(), where);
	}

	/**
	 * Query for rows
	 * 
	 * @param columns
	 *            columns
	 * @param where
	 *            where clause
	 * @return result
	 * @since 3.5.0
	 */
	public TResult query(String[] columns, String where) {
		return query(false, columns, where);
	}

	/**
	 * Query for rows
	 * 
	 * @param distinct
	 *            distinct rows
	 * @param columns
	 *            columns
	 * @param where
	 *            where clause
	 * @return result
	 * @since 4.0.0
	 */
	public TResult query(boolean distinct, String[] columns, String where) {
		return query(distinct, columns, where, null);
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
		return query(false, where, whereArgs);
	}

	/**
	 * Query for rows
	 * 
	 * @param distinct
	 *            distinct rows
	 * @param where
	 *            where clause
	 * @param whereArgs
	 *            where arguments
	 * @return result
	 * @since 4.0.0
	 */
	public TResult query(boolean distinct, String where, String[] whereArgs) {
		return query(distinct, table.getColumnNames(), where, whereArgs);
	}

	/**
	 * Query for rows
	 * 
	 * @param columns
	 *            columns
	 * @param where
	 *            where clause
	 * @param whereArgs
	 *            where arguments
	 * @return result
	 * @since 3.5.0
	 */
	public TResult query(String[] columns, String where, String[] whereArgs) {
		return query(false, columns, where, whereArgs);
	}

	/**
	 * Query for rows
	 * 
	 * @param distinct
	 *            distinct rows
	 * @param columns
	 *            columns
	 * @param where
	 *            where clause
	 * @param whereArgs
	 *            where arguments
	 * @return result
	 * @since 4.0.0
	 */
	public TResult query(boolean distinct, String[] columns, String where,
			String[] whereArgs) {
		TResult result = userDb.query(distinct, getTableName(), columns, where,
				whereArgs, null, null, null);
		prepareResult(result);
		return result;
	}

	/**
	 * Query SQL for rows
	 * 
	 * @param where
	 *            where clause
	 * @return SQL
	 * @since 3.4.0
	 */
	public String querySQL(String where) {
		return querySQL(false, where);
	}

	/**
	 * Query SQL for rows
	 * 
	 * @param distinct
	 *            distinct rows
	 * @param where
	 *            where clause
	 * @return SQL
	 * @since 4.0.0
	 */
	public String querySQL(boolean distinct, String where) {
		return querySQL(distinct, table.getColumnNames(), where);
	}

	/**
	 * Query SQL for row ids
	 * 
	 * @param where
	 *            where clause
	 * @return SQL
	 * @since 3.4.0
	 */
	public String queryIdsSQL(String where) {
		return queryIdsSQL(false, where);
	}

	/**
	 * Query SQL for row ids
	 * 
	 * @param distinct
	 *            distinct rows
	 * @param where
	 *            where clause
	 * @return SQL
	 * @since 4.0.0
	 */
	public String queryIdsSQL(boolean distinct, String where) {
		return querySQL(distinct,
				new String[] { table.getPkColumn().getName() }, where);
	}

	/**
	 * Query SQL for rows
	 * 
	 * @param columns
	 *            columns
	 * @param where
	 *            where clause
	 * @return SQL
	 * @since 3.4.0
	 */
	public String querySQL(String[] columns, String where) {
		return querySQL(false, columns, where);
	}

	/**
	 * Query SQL for rows
	 * 
	 * @param distinct
	 *            distinct rows
	 * @param columns
	 *            columns
	 * @param where
	 *            where clause
	 * @return SQL
	 * @since 4.0.0
	 */
	public String querySQL(boolean distinct, String[] columns, String where) {
		return userDb.querySQL(distinct, getTableName(), columns, where, null,
				null, null);
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
		return query(false, where, whereArgs, groupBy, having, orderBy);
	}

	/**
	 * Query for rows
	 * 
	 * @param distinct
	 *            distinct rows
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
	 * @since 4.0.0
	 */
	public TResult query(boolean distinct, String where, String[] whereArgs,
			String groupBy, String having, String orderBy) {
		return query(distinct, table.getColumnNames(), where, whereArgs,
				groupBy, having, orderBy);
	}

	/**
	 * Query for rows
	 * 
	 * @param columns
	 *            columns
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
	 * @since 3.5.0
	 */
	public TResult query(String[] columns, String where, String[] whereArgs,
			String groupBy, String having, String orderBy) {
		return query(false, columns, where, whereArgs, groupBy, having,
				orderBy);
	}

	/**
	 * Query for rows
	 * 
	 * @param distinct
	 *            distinct rows
	 * @param columns
	 *            columns
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
	 * @since 4.0.0
	 */
	public TResult query(boolean distinct, String[] columns, String where,
			String[] whereArgs, String groupBy, String having, String orderBy) {
		TResult result = userDb.query(distinct, getTableName(), columns, where,
				whereArgs, groupBy, having, orderBy);
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
		return query(false, where, whereArgs, groupBy, having, orderBy, limit);
	}

	/**
	 * Query for rows
	 * 
	 * @param distinct
	 *            distinct rows
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
	 * @since 4.0.0
	 */
	public TResult query(boolean distinct, String where, String[] whereArgs,
			String groupBy, String having, String orderBy, String limit) {
		return query(distinct, table.getColumnNames(), where, whereArgs,
				groupBy, having, orderBy, limit);
	}

	/**
	 * Query for rows
	 * 
	 * @param columns
	 *            columns
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
	 * @since 3.5.0
	 */
	public TResult query(String[] columns, String where, String[] whereArgs,
			String groupBy, String having, String orderBy, String limit) {
		return query(false, columns, where, whereArgs, groupBy, having, orderBy,
				limit);
	}

	/**
	 * Query for rows
	 * 
	 * @param distinct
	 *            distinct rows
	 * @param columns
	 *            columns
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
	 * @since 4.0.0
	 */
	public TResult query(boolean distinct, String[] columns, String where,
			String[] whereArgs, String groupBy, String having, String orderBy,
			String limit) {
		TResult result = userDb.query(distinct, getTableName(), columns, where,
				whereArgs, groupBy, having, orderBy, limit);
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
		return queryForChunk(false, limit, offset);
	}

	/**
	 * Query for id ordered rows starting at the offset and returning no more
	 * than the limit.
	 * 
	 * @param distinct
	 *            distinct rows
	 * @param limit
	 *            chunk limit
	 * @param offset
	 *            chunk query offset
	 * @return result
	 * @since 4.0.0
	 */
	public TResult queryForChunk(boolean distinct, int limit, long offset) {
		return queryForChunk(distinct, table.getColumnNames(), limit, offset);
	}

	/**
	 * Query for id ordered rows starting at the offset and returning no more
	 * than the limit.
	 * 
	 * @param columns
	 *            columns
	 * @param limit
	 *            chunk limit
	 * @param offset
	 *            chunk query offset
	 * @return result
	 * @since 3.5.0
	 */
	public TResult queryForChunk(String[] columns, int limit, long offset) {
		return queryForChunk(false, columns, limit, offset);
	}

	/**
	 * Query for id ordered rows starting at the offset and returning no more
	 * than the limit.
	 * 
	 * @param distinct
	 *            distinct rows
	 * @param columns
	 *            columns
	 * @param limit
	 *            chunk limit
	 * @param offset
	 *            chunk query offset
	 * @return result
	 * @since 4.0.0
	 */
	public TResult queryForChunk(boolean distinct, String[] columns, int limit,
			long offset) {
		return queryForChunk(distinct, columns, null, null, limit, offset);
	}

	/**
	 * Query for id ordered rows starting at the offset and returning no more
	 * than the limit.
	 * 
	 * @param where
	 *            where clause
	 * @param whereArgs
	 *            where arguments
	 * @param limit
	 *            chunk limit
	 * @param offset
	 *            chunk query offset
	 * @return result
	 * @since 3.4.0
	 */
	public TResult queryForChunk(String where, String[] whereArgs, int limit,
			long offset) {
		return queryForChunk(false, where, whereArgs, limit, offset);
	}

	/**
	 * Query for id ordered rows starting at the offset and returning no more
	 * than the limit.
	 * 
	 * @param distinct
	 *            distinct rows
	 * @param where
	 *            where clause
	 * @param whereArgs
	 *            where arguments
	 * @param limit
	 *            chunk limit
	 * @param offset
	 *            chunk query offset
	 * @return result
	 * @since 4.0.0
	 */
	public TResult queryForChunk(boolean distinct, String where,
			String[] whereArgs, int limit, long offset) {
		return queryForChunk(distinct, table.getColumnNames(), where, whereArgs,
				limit, offset);
	}

	/**
	 * Query for id ordered rows starting at the offset and returning no more
	 * than the limit.
	 * 
	 * @param columns
	 *            columns
	 * @param where
	 *            where clause
	 * @param whereArgs
	 *            where arguments
	 * @param limit
	 *            chunk limit
	 * @param offset
	 *            chunk query offset
	 * @return result
	 * @since 3.5.0
	 */
	public TResult queryForChunk(String[] columns, String where,
			String[] whereArgs, int limit, long offset) {
		return queryForChunk(false, columns, where, whereArgs, limit, offset);
	}

	/**
	 * Query for id ordered rows starting at the offset and returning no more
	 * than the limit.
	 * 
	 * @param distinct
	 *            distinct rows
	 * @param columns
	 *            columns
	 * @param where
	 *            where clause
	 * @param whereArgs
	 *            where arguments
	 * @param limit
	 *            chunk limit
	 * @param offset
	 *            chunk query offset
	 * @return result
	 * @since 4.0.0
	 */
	public TResult queryForChunk(boolean distinct, String[] columns,
			String where, String[] whereArgs, int limit, long offset) {
		return queryForChunk(distinct, columns, where, whereArgs,
				table.getPkColumn().getName(), limit, offset);
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
		return queryForChunk(false, orderBy, limit, offset);
	}

	/**
	 * Query for ordered rows starting at the offset and returning no more than
	 * the limit.
	 * 
	 * @param distinct
	 *            distinct rows
	 * @param orderBy
	 *            order by
	 * @param limit
	 *            chunk limit
	 * @param offset
	 *            chunk query offset
	 * @return result
	 * @since 4.0.0
	 */
	public TResult queryForChunk(boolean distinct, String orderBy, int limit,
			long offset) {
		return queryForChunk(distinct, table.getColumnNames(), orderBy, limit,
				offset);
	}

	/**
	 * Query for ordered rows starting at the offset and returning no more than
	 * the limit.
	 * 
	 * @param columns
	 *            columns
	 * @param orderBy
	 *            order by
	 * @param limit
	 *            chunk limit
	 * @param offset
	 *            chunk query offset
	 * @return result
	 * @since 3.5.0
	 */
	public TResult queryForChunk(String[] columns, String orderBy, int limit,
			long offset) {
		return queryForChunk(false, columns, orderBy, limit, offset);
	}

	/**
	 * Query for ordered rows starting at the offset and returning no more than
	 * the limit.
	 * 
	 * @param distinct
	 *            distinct rows
	 * @param columns
	 *            columns
	 * @param orderBy
	 *            order by
	 * @param limit
	 *            chunk limit
	 * @param offset
	 *            chunk query offset
	 * @return result
	 * @since 4.0.0
	 */
	public TResult queryForChunk(boolean distinct, String[] columns,
			String orderBy, int limit, long offset) {
		return queryForChunk(distinct, columns, null, null, orderBy, limit,
				offset);
	}

	/**
	 * Query for ordered rows starting at the offset and returning no more than
	 * the limit.
	 * 
	 * @param where
	 *            where clause
	 * @param whereArgs
	 *            where arguments
	 * @param orderBy
	 *            order by
	 * @param limit
	 *            chunk limit
	 * @param offset
	 *            chunk query offset
	 * @return result
	 * @since 3.4.0
	 */
	public TResult queryForChunk(String where, String[] whereArgs,
			String orderBy, int limit, long offset) {
		return queryForChunk(false, where, whereArgs, orderBy, limit, offset);
	}

	/**
	 * Query for ordered rows starting at the offset and returning no more than
	 * the limit.
	 * 
	 * @param distinct
	 *            distinct rows
	 * @param where
	 *            where clause
	 * @param whereArgs
	 *            where arguments
	 * @param orderBy
	 *            order by
	 * @param limit
	 *            chunk limit
	 * @param offset
	 *            chunk query offset
	 * @return result
	 * @since 4.0.0
	 */
	public TResult queryForChunk(boolean distinct, String where,
			String[] whereArgs, String orderBy, int limit, long offset) {
		return queryForChunk(distinct, table.getColumnNames(), where, whereArgs,
				orderBy, limit, offset);
	}

	/**
	 * Query for ordered rows starting at the offset and returning no more than
	 * the limit.
	 * 
	 * @param columns
	 *            columns
	 * @param where
	 *            where clause
	 * @param whereArgs
	 *            where arguments
	 * @param orderBy
	 *            order by
	 * @param limit
	 *            chunk limit
	 * @param offset
	 *            chunk query offset
	 * @return result
	 * @since 3.5.0
	 */
	public TResult queryForChunk(String[] columns, String where,
			String[] whereArgs, String orderBy, int limit, long offset) {
		return queryForChunk(false, columns, where, whereArgs, orderBy, limit,
				offset);
	}

	/**
	 * Query for ordered rows starting at the offset and returning no more than
	 * the limit.
	 * 
	 * @param distinct
	 *            distinct rows
	 * @param columns
	 *            columns
	 * @param where
	 *            where clause
	 * @param whereArgs
	 *            where arguments
	 * @param orderBy
	 *            order by
	 * @param limit
	 *            chunk limit
	 * @param offset
	 *            chunk query offset
	 * @return result
	 * @since 4.0.0
	 */
	public TResult queryForChunk(boolean distinct, String[] columns,
			String where, String[] whereArgs, String orderBy, int limit,
			long offset) {
		return queryForChunk(distinct, columns, where, whereArgs, null, null,
				orderBy, limit, offset);
	}

	/**
	 * Query for ordered rows starting at the offset and returning no more than
	 * the limit.
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
	 *            chunk limit
	 * @param offset
	 *            chunk query offset
	 * @return result
	 * @since 3.4.0
	 */
	public TResult queryForChunk(String where, String[] whereArgs,
			String groupBy, String having, String orderBy, int limit,
			long offset) {
		return queryForChunk(false, where, whereArgs, groupBy, having, orderBy,
				limit, offset);
	}

	/**
	 * Query for ordered rows starting at the offset and returning no more than
	 * the limit.
	 * 
	 * @param distinct
	 *            distinct rows
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
	 *            chunk limit
	 * @param offset
	 *            chunk query offset
	 * @return result
	 * @since 4.0.0
	 */
	public TResult queryForChunk(boolean distinct, String where,
			String[] whereArgs, String groupBy, String having, String orderBy,
			int limit, long offset) {
		return queryForChunk(distinct, table.getColumnNames(), where, whereArgs,
				groupBy, having, orderBy, limit, offset);
	}

	/**
	 * Query for ordered rows starting at the offset and returning no more than
	 * the limit.
	 * 
	 * @param columns
	 *            columns
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
	 *            chunk limit
	 * @param offset
	 *            chunk query offset
	 * @return result
	 * @since 3.5.0
	 */
	public TResult queryForChunk(String[] columns, String where,
			String[] whereArgs, String groupBy, String having, String orderBy,
			int limit, long offset) {
		return queryForChunk(false, columns, where, whereArgs, groupBy, having,
				orderBy, limit, offset);
	}

	/**
	 * Query for ordered rows starting at the offset and returning no more than
	 * the limit.
	 * 
	 * @param distinct
	 *            distinct rows
	 * @param columns
	 *            columns
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
	 *            chunk limit
	 * @param offset
	 *            chunk query offset
	 * @return result
	 * @since 4.0.0
	 */
	public TResult queryForChunk(boolean distinct, String[] columns,
			String where, String[] whereArgs, String groupBy, String having,
			String orderBy, int limit, long offset) {
		return query(distinct, columns, where, whereArgs, groupBy, having,
				orderBy, buildLimit(limit, offset));
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
	 * Begin a transaction
	 * 
	 * @since 3.3.0
	 */
	public abstract void beginTransaction();

	/**
	 * End a transaction successfully
	 * 
	 * @since 3.3.0
	 */
	public void endTransaction() {
		endTransaction(true);
	}

	/**
	 * Fail a transaction
	 * 
	 * @since 3.3.0
	 */
	public void failTransaction() {
		endTransaction(false);
	}

	/**
	 * End a transaction
	 * 
	 * @param successful
	 *            true if the transaction was successful, false to rollback or
	 *            not commit
	 * @since 3.3.0
	 */
	public abstract void endTransaction(boolean successful);

	/**
	 * End a transaction as successful and begin a new transaction
	 *
	 * @since 3.3.0
	 */
	public void endAndBeginTransaction() {
		endTransaction();
		beginTransaction();
	}

	/**
	 * Commit changes on the connection
	 * 
	 * @since 3.3.0
	 */
	public abstract void commit();

	/**
	 * Determine if currently within a transaction
	 * 
	 * @return true if in transaction
	 * 
	 * @since 3.3.0
	 */
	public abstract boolean inTransaction();

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
		return selectionArgs.isEmpty() ? null
				: selectionArgs.toArray(new String[] {});
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
		return selectionArgs.isEmpty() ? null
				: selectionArgs.toArray(new String[] {});
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
		return selectionArgs.isEmpty() ? null
				: selectionArgs.toArray(new String[] {});
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
	 * Build where statement for ids in the nested SQL query
	 * 
	 * @param nestedSQL
	 *            nested SQL
	 * @param where
	 *            where clause
	 * @return where clause
	 * @since 3.4.0
	 */
	public String buildWhereIn(String nestedSQL, String where) {

		String nestedWhere = CoreSQLUtils.quoteWrap(
				table.getPkColumn().getName()) + " IN (" + nestedSQL + ")";

		String whereClause;
		if (where == null) {
			whereClause = nestedWhere;
		} else {
			whereClause = "(" + where + ") AND (" + nestedWhere + ")";
		}

		return whereClause;
	}

	/**
	 * Build where args for ids in the nested SQL query
	 * 
	 * @param nestedArgs
	 *            nested SQL args
	 * @param whereArgs
	 *            where arguments
	 * @return where args
	 * @since 3.4.0
	 */
	public String[] buildWhereInArgs(String[] nestedArgs, String[] whereArgs) {

		String[] args = whereArgs;

		if (args == null) {
			args = nestedArgs;
		} else if (nestedArgs != null) {
			args = (String[]) Array.newInstance(String.class,
					whereArgs.length + nestedArgs.length);
			System.arraycopy(whereArgs, 0, args, 0, whereArgs.length);
			System.arraycopy(nestedArgs, 0, args, whereArgs.length,
					nestedArgs.length);
		}

		return args;
	}

	/**
	 * Get the total count
	 * 
	 * @return count
	 */
	public int count() {
		return count(null, null, null);
	}

	/**
	 * Get the count
	 * 
	 * @param where
	 *            where clause
	 * @return count
	 * @since 3.4.0
	 */
	public int count(String where) {
		return count(null, where, null);
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
		return count(null, where, args);
	}

	/**
	 * Get a count of results
	 * 
	 * @param column
	 *            column name
	 * @return count
	 * @since 4.0.0
	 */
	public int countColumn(String column) {
		return count(false, column);
	}

	/**
	 * Get a count of results
	 * 
	 * @param distinct
	 *            distinct column values
	 * @param column
	 *            column name
	 * @return count
	 * @since 4.0.0
	 */
	public int count(boolean distinct, String column) {
		return count(distinct, column, null, null);
	}

	/**
	 * Get a count of results
	 * 
	 * @param column
	 *            column name
	 * @param where
	 *            where clause
	 * @return count
	 * @since 4.0.0
	 */
	public int count(String column, String where) {
		return count(column, where, null);
	}

	/**
	 * Get a count of results
	 * 
	 * @param column
	 *            column name
	 * @param where
	 *            where clause
	 * @param args
	 *            arguments
	 * @return count
	 * @since 4.0.0
	 */
	public int count(String column, String where, String[] args) {
		return count(false, column, where, args);
	}

	/**
	 * Get a count of results
	 * 
	 * @param distinct
	 *            distinct column values
	 * @param column
	 *            column name
	 * @param where
	 *            where clause
	 * @return count
	 * @since 4.0.0
	 */
	public int count(boolean distinct, String column, String where) {
		return count(distinct, column, where, null);
	}

	/**
	 * Get a count of results
	 * 
	 * @param distinct
	 *            distinct column values
	 * @param column
	 *            column name
	 * @param where
	 *            where clause
	 * @param args
	 *            arguments
	 * @return count
	 * @since 4.0.0
	 */
	public int count(boolean distinct, String column, String where,
			String[] args) {
		return db.count(getTableName(), distinct, column, where, args);
	}

	/**
	 * Get the min result of the column
	 * 
	 * @param <T>
	 *            return type
	 * @param column
	 *            column name
	 * @return min or null
	 * @since 4.0.0
	 */
	public <T> T min(String column) {
		return min(column, null, null);
	}

	/**
	 * Get the min result of the column
	 * 
	 * @param <T>
	 *            return type
	 * @param column
	 *            column name
	 * @param where
	 *            where clause
	 * @param args
	 *            where arugments
	 * @return min or null
	 * @since 4.0.0
	 */
	public <T> T min(String column, String where, String[] args) {
		return db.min(getTableName(), column, where, args);
	}

	/**
	 * Get the max result of the column
	 * 
	 * @param <T>
	 *            return type
	 * @param column
	 *            column name
	 * @return max or null
	 * @since 4.0.0
	 */
	public <T> T max(String column) {
		return max(column, null, null);
	}

	/**
	 * Get the max result of the column
	 * 
	 * @param <T>
	 *            return type
	 * @param column
	 *            column name
	 * @param where
	 *            where clause
	 * @param args
	 *            where arguments
	 * @return max or null
	 * @since 4.0.0
	 */
	public <T> T max(String column, String where, String[] args) {
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
	public <T> List<T> querySingleColumnTypedResults(String sql,
			String[] args) {
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
						.boundDegreesBoundingBoxWithWebMercatorLimits(
								boundingBox);
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
	 * @since 3.3.0
	 */
	public void addColumn(TColumn column) {
		CoreSQLUtils.addColumn(db, table.getTableName(), column);
		table.addColumn(column);
	}

	/**
	 * Rename column
	 * 
	 * @param column
	 *            column
	 * @param newColumnName
	 *            new column name
	 * @since 3.3.0
	 */
	public void renameColumn(TColumn column, String newColumnName) {
		renameTableColumn(column.getName(), newColumnName);
		table.renameColumn(column, newColumnName);
	}

	/**
	 * Rename column
	 * 
	 * @param columnName
	 *            column name
	 * @param newColumnName
	 *            new column name
	 * @since 3.3.0
	 */
	public void renameColumn(String columnName, String newColumnName) {
		renameTableColumn(columnName, newColumnName);
		table.renameColumn(columnName, newColumnName);
	}

	/**
	 * Rename column
	 * 
	 * @param index
	 *            column index
	 * @param newColumnName
	 *            new column name
	 * @since 3.3.0
	 */
	public void renameColumn(int index, String newColumnName) {
		renameTableColumn(table.getColumnName(index), newColumnName);
		table.renameColumn(index, newColumnName);
	}

	/**
	 * Rename a table column
	 * 
	 * @param columnName
	 *            column name
	 * @param newColumnName
	 *            new column name
	 * @since 3.3.0
	 */
	protected void renameTableColumn(String columnName, String newColumnName) {
		AlterTable.renameColumn(db, table.getTableName(), columnName,
				newColumnName);
	}

	/**
	 * Drop a column
	 * 
	 * @param column
	 *            column
	 * @since 3.3.0
	 */
	public void dropColumn(TColumn column) {
		dropColumn(column.getName());
	}

	/**
	 * Drop a column
	 * 
	 * @param index
	 *            column index
	 * @since 3.3.0
	 */
	public void dropColumn(int index) {
		dropColumn(table.getColumnName(index));
	}

	/**
	 * Drop a column
	 * 
	 * @param columnName
	 *            column name
	 * @since 3.3.0
	 */
	public void dropColumn(String columnName) {
		AlterTable.dropColumn(db, table, columnName);
	}

	/**
	 * Drop columns
	 * 
	 * @param columns
	 *            columns
	 * @since 3.3.0
	 */
	public void dropColumns(Collection<TColumn> columns) {
		List<String> columnNames = new ArrayList<>();
		for (TColumn column : columns) {
			columnNames.add(column.getName());
		}
		dropColumnNames(columnNames);
	}

	/**
	 * Drop columns
	 * 
	 * @param indexes
	 *            column indexes
	 * @since 3.3.0
	 */
	public void dropColumnIndexes(Collection<Integer> indexes) {
		List<String> columnNames = new ArrayList<>();
		for (int index : indexes) {
			columnNames.add(table.getColumnName(index));
		}
		dropColumnNames(columnNames);
	}

	/**
	 * Drop columns
	 * 
	 * @param columnNames
	 *            column names
	 * @since 3.3.0
	 */
	public void dropColumnNames(Collection<String> columnNames) {
		AlterTable.dropColumns(db, table, columnNames);
	}

	/**
	 * Alter a column
	 * 
	 * @param column
	 *            column
	 * @since 3.3.0
	 */
	public void alterColumn(TColumn column) {
		AlterTable.alterColumn(db, table, column);
	}

	/**
	 * Alter columns
	 * 
	 * @param columns
	 *            columns
	 * @since 3.3.0
	 */
	public void alterColumns(Collection<TColumn> columns) {
		AlterTable.alterColumns(db, table, columns);
	}

}
