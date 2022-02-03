package mil.nga.geopackage.user;

import java.util.Iterator;

import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.db.Pagination;

/**
 * User Core Paginated Results for iterating and querying through chunks
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
 * @since 6.2.0
 */
public abstract class UserCorePaginatedResults<TColumn extends UserColumn, TTable extends UserTable<TColumn>, TRow extends UserCoreRow<TColumn, TTable>, TResult extends UserCoreResult<TColumn, TTable, TRow>>
		implements Iterable<TRow> {

	/**
	 * DAO
	 */
	private final UserCoreDao<TColumn, TTable, TRow, TResult> dao;

	/**
	 * Results
	 */
	private UserCoreResult<TColumn, TTable, TRow> results;

	/**
	 * SQL statement
	 */
	private final String sql;

	/**
	 * SQL arguments
	 */
	private final String[] args;

	/**
	 * SQL column names
	 */
	private final String[] columns;

	/**
	 * Paginated query settings
	 */
	private Pagination pagination;

	/**
	 * Constructor
	 * 
	 * @param dao
	 *            user core dao
	 * @param results
	 *            user core results
	 */
	protected UserCorePaginatedResults(
			UserCoreDao<TColumn, TTable, TRow, TResult> dao,
			UserCoreResult<TColumn, TTable, TRow> results) {
		this.dao = dao;
		this.results = results;
		sql = results.getSql();
		columns = results.getColumns().getColumnNames();
		args = results.getSelectionArgs();
		pagination = Pagination.find(sql);
		if (pagination == null) {
			throw new GeoPackageException(
					"Results are not paginated. SQL: " + sql);
		}
	}

	/**
	 * Get the DAO
	 * 
	 * @return data access object
	 */
	public UserCoreDao<TColumn, TTable, TRow, TResult> getDao() {
		return dao;
	}

	/**
	 * Get the current paginated results
	 * 
	 * @return current results
	 */
	public UserCoreResult<TColumn, TTable, TRow> getResults() {
		return results;
	}

	/**
	 * Get the initial SQL statement
	 * 
	 * @return SQL statement
	 */
	public String getSql() {
		return sql;
	}

	/**
	 * Get the SQL arguments
	 * 
	 * @return SQL arguments
	 */
	public String[] getArgs() {
		return args;
	}

	/**
	 * Get the SQL column names
	 * 
	 * @return SQL column names
	 */
	public String[] getColumns() {
		return columns;
	}

	/**
	 * Get the pagination
	 * 
	 * @return pagination
	 */
	public Pagination getPagination() {
		return pagination;
	}

	/**
	 * Set the pagination
	 * 
	 * @param pagination
	 *            pagination
	 */
	public void setPagination(Pagination pagination) {
		this.pagination = pagination;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<TRow> iterator() {
		return new Iterator<TRow>() {

			/**
			 * Rows iterator
			 */
			private Iterator<TRow> rows = results.iterator();

			/**
			 * {@inheritDoc}
			 */
			@Override
			public boolean hasNext() {
				boolean hasNext = rows.hasNext();
				if (!hasNext) {
					close();
					if (pagination.hasLimit()) {
						pagination.incrementOffset();
						String query = pagination.replace(sql);
						results = dao.rawQuery(query, columns, args);
						rows = results.iterator();
						hasNext = results.moveToNext();
						if (!hasNext) {
							close();
						}
					}
				}
				return hasNext;
			}

			/**
			 * {@inheritDoc}
			 */
			@Override
			public TRow next() {
				return rows.next();
			}
		};
	}

	/**
	 * Close the current results
	 */
	public void close() {
		results.close();
	}

}
