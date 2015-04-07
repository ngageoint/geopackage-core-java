package mil.nga.giat.geopackage.user;

import mil.nga.giat.geopackage.db.GeoPackageDataType;

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
	 * @return
	 */
	public TRow getRow(int[] columnTypes, Object[] values);

	/**
	 * Get the value for the column
	 * 
	 * @param column
	 * @return
	 */
	public Object getValue(TColumn column);

	/**
	 * Get the value for the column
	 *
	 * @param index
	 * @param dataType
	 * @return
	 */
	public Object getValue(int index, GeoPackageDataType dataType);

	/**
	 * Get the table
	 * 
	 * @return
	 */
	public TTable getTable();

	/**
	 * Get the row at the current cursor position
	 * 
	 * @return
	 */
	public TRow getRow();

	/**
	 * Move the cursor to the next row.
	 * 
	 * @return
	 */
	public boolean moveToNext();

	/**
	 * Move the cursor to the first row.
	 * 
	 * @return
	 */
	public boolean moveToFirst();

	/**
	 * Returns data type of the given column's value
	 * 
	 * @return
	 */
	public int getType(int columnIndex);

	/**
	 * Returns the zero-based index for the given column name, or -1 if the
	 * column doesn't exist.
	 * 
	 * @param columnName
	 * @return
	 */
	public int getColumnIndex(String columnName);

	/**
	 * Returns the value of the requested column as a String.
	 * 
	 * @param columnIndex
	 * @return
	 */
	public String getString(int columnIndex);

	/**
	 * Returns the value of the requested column as an long.
	 * 
	 * @param columnIndex
	 * @return
	 */
	public long getLong(int columnIndex);

	/**
	 * Returns the value of the requested column as an int.
	 * 
	 * @param columnIndex
	 * @return
	 */
	public int getInt(int columnIndex);

	/**
	 * Returns the value of the requested column as an short.
	 * 
	 * @param columnIndex
	 * @return
	 */
	public short getShort(int columnIndex);

	/**
	 * Returns the value of the requested column as an double.
	 * 
	 * @param columnIndex
	 * @return
	 */
	public double getDouble(int columnIndex);

	/**
	 * Returns the value of the requested column as an float.
	 * 
	 * @param columnIndex
	 * @return
	 */
	public float getFloat(int columnIndex);

	/**
	 * Returns the value of the requested column as a byte array.
	 * 
	 * @param columnIndex
	 * @return
	 */
	public byte[] getBlob(int columnIndex);

	/**
	 * Close the result
	 */
	public void close();

}
