package mil.nga.geopackage.user;

import mil.nga.geopackage.db.GeoPackageDataType;

/**
 * Abstract User Cursor
 * 
 * @param <TColumn>
 * @param <TTable>
 * @param <TRow>
 * 
 * @author osbornb
 */
public interface UserCoreResult<TColumn extends UserColumn, TTable extends UserTable<TColumn>, TRow extends UserCoreRow<TColumn, TTable>> {

	/**
	 * Get a row using the column types and values
	 * 
	 * @param columnTypes
	 * @param values
	 * @return row
	 */
	public TRow getRow(int[] columnTypes, Object[] values);

	/**
	 * Get the value for the column
	 * 
	 * @param column
	 * @return value
	 */
	public Object getValue(TColumn column);

	/**
	 * Get the value for the column
	 *
	 * @param index
	 * @param dataType
	 * @return value
	 */
	public Object getValue(int index, GeoPackageDataType dataType);

	/**
	 * Get the table
	 * 
	 * @return table
	 */
	public TTable getTable();

	/**
	 * Get the row at the current cursor position
	 * 
	 * @return row
	 */
	public TRow getRow();

	/**
	 * Get the count of results
	 * 
	 * @return count
	 */
	public int getCount();

	/**
	 * Move the cursor to the next row.
	 * 
	 * @return true if another row
	 */
	public boolean moveToNext();

	/**
	 * Move the cursor to the first row.
	 * 
	 * @return true if moved to first
	 */
	public boolean moveToFirst();

	/**
	 * Move the cursor to an absolute position
	 * 
	 * @param position
	 * @return true if moved to position
	 */
	public boolean moveToPosition(int position);

	/**
	 * Returns data type of the given column's value
	 * 
	 * @return type
	 */
	public int getType(int columnIndex);

	/**
	 * Returns the zero-based index for the given column name, or -1 if the
	 * column doesn't exist.
	 * 
	 * @param columnName
	 * @return column index
	 */
	public int getColumnIndex(String columnName);

	/**
	 * Returns the value of the requested column as a String.
	 * 
	 * @param columnIndex
	 * @return string value
	 */
	public String getString(int columnIndex);

	/**
	 * Returns the value of the requested column as an long.
	 * 
	 * @param columnIndex
	 * @return long value
	 */
	public long getLong(int columnIndex);

	/**
	 * Returns the value of the requested column as an int.
	 * 
	 * @param columnIndex
	 * @return int value
	 */
	public int getInt(int columnIndex);

	/**
	 * Returns the value of the requested column as an short.
	 * 
	 * @param columnIndex
	 * @return short value
	 */
	public short getShort(int columnIndex);

	/**
	 * Returns the value of the requested column as an double.
	 * 
	 * @param columnIndex
	 * @return double value
	 */
	public double getDouble(int columnIndex);

	/**
	 * Returns the value of the requested column as an float.
	 * 
	 * @param columnIndex
	 * @return float value
	 */
	public float getFloat(int columnIndex);

	/**
	 * Returns the value of the requested column as a byte array.
	 * 
	 * @param columnIndex
	 * @return bytes value
	 */
	public byte[] getBlob(int columnIndex);

	/**
	 * Was the last value retrieved null
	 * 
	 * @return true if was null
	 */
	public boolean wasNull();

	/**
	 * Close the result
	 */
	public void close();

}
