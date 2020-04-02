package mil.nga.geopackage.user;

import mil.nga.geopackage.db.Result;

/**
 * Abstract User Cursor
 * 
 * @param <TColumn>
 *            column type
 * @param <TTable>
 *            table type
 * @param <TRow>
 *            row type
 * 
 * @author osbornb
 */
public interface UserCoreResult<TColumn extends UserColumn, TTable extends UserTable<TColumn>, TRow extends UserCoreRow<TColumn, TTable>>
		extends Result {

	/**
	 * Get a row using the column types and values
	 * 
	 * @param columnTypes
	 *            column types
	 * @param values
	 *            values
	 * @return row
	 */
	public TRow getRow(int[] columnTypes, Object[] values);

	/**
	 * Get the value for the column
	 * 
	 * @param column
	 *            column
	 * @return value
	 */
	public Object getValue(TColumn column);

	/**
	 * Get the value for the column index
	 * 
	 * @param index
	 *            column index
	 * @return value
	 * @since 3.4.0
	 */
	public Object getValue(int index);

	/**
	 * Get the value for the column name
	 * 
	 * @param columnName
	 *            column name
	 * @return value
	 * @since 3.4.0
	 */
	public Object getValue(String columnName);

	/**
	 * Get the primary key value
	 * 
	 * @return value
	 * @since 3.4.0
	 */
	public long getId();

	/**
	 * Get the table
	 * 
	 * @return table
	 */
	public TTable getTable();

	/**
	 * Get the table name
	 * 
	 * @return table name
	 * @since 3.5.0
	 */
	public String getTableName();

	/**
	 * Get the columns
	 * 
	 * @return columns
	 * @since 3.5.0
	 */
	public UserColumns<TColumn> getColumns();

	/**
	 * Get the row at the current cursor position
	 * 
	 * @return row
	 */
	public TRow getRow();

	/**
	 * Get the count of results
	 * 
	 * @return count, -1 if not able to determine
	 */
	public int getCount();

}
