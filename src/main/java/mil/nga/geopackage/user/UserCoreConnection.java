package mil.nga.geopackage.user;

/**
 * GeoPackage Connection used to define common functionality within different
 * connection types
 * 
 * @param <TColumn>
 * @param <TTable>
 * @param <TRow>
 * @param <TResult>
 * 
 * @author osbornb
 */
public abstract class UserCoreConnection<TColumn extends UserColumn, TTable extends UserTable<TColumn>, TRow extends UserCoreRow<TColumn, TTable>, TResult extends UserCoreResult<TColumn, TTable, TRow>> {

	/**
	 * Runs the provided SQL and returns a {@link UserCoreResult} over the
	 * result set.
	 * 
	 * @param sql
	 * @param selectionArgs
	 * @return result
	 */
	public abstract TResult rawQuery(String sql, String[] selectionArgs);

	/**
	 * Query the given table, returning a {@link UserCoreResult} over the result
	 * set.
	 * 
	 * @param table
	 * @param columns
	 * @param selection
	 * @param selectionArgs
	 * @param groupBy
	 * @param having
	 * @param orderBy
	 * @return result
	 */
	public abstract TResult query(String table, String[] columns,
			String selection, String[] selectionArgs, String groupBy,
			String having, String orderBy);

	/**
	 * Query the given URL, returning a {@link UserCoreResult} over the result
	 * set.
	 * 
	 * @param table
	 * @param columns
	 * @param selection
	 * @param selectionArgs
	 * @param groupBy
	 * @param having
	 * @param orderBy
	 * @param limit
	 * @return result
	 */
	public abstract TResult query(String table, String[] columns,
			String selection, String[] selectionArgs, String groupBy,
			String having, String orderBy, String limit);

}
