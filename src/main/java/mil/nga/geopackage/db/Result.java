package mil.nga.geopackage.db;

/**
 * Database result interface
 * 
 * @author osbornb
 * @since 3.0.3
 */
public interface Result {

	/**
	 * Get the value for the column
	 *
	 * @param index
	 *            index
	 * @return value
	 */
	public Object getValue(int index);

	/**
	 * Get the value for the column
	 *
	 * @param index
	 *            index
	 * @param dataType
	 *            data type
	 * @return value
	 */
	public Object getValue(int index, GeoPackageDataType dataType);

	/**
	 * Move the cursor to the next row.
	 * 
	 * @return true if another row
	 */
	public boolean moveToNext();

	/**
	 * Get the current position
	 * 
	 * @return position
	 */
	public int getPosition();

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
	 *            position
	 * @return true if moved to position
	 */
	public boolean moveToPosition(int position);

	/**
	 * Get the number of columns
	 * 
	 * @return column count
	 */
	public int getColumnCount();

	/**
	 * Returns data type of the given column's value
	 * 
	 * @param columnIndex
	 *            column index
	 * @return type
	 */
	public int getType(int columnIndex);

	/**
	 * Returns the zero-based index for the given column name, or -1 if the
	 * column doesn't exist.
	 * 
	 * @param columnName
	 *            column name
	 * @return column index
	 */
	public int getColumnIndex(String columnName);

	/**
	 * Returns the value of the requested column as a String.
	 * 
	 * @param columnIndex
	 *            column index
	 * @return string value
	 */
	public String getString(int columnIndex);

	/**
	 * Returns the value of the requested column as an long.
	 * 
	 * @param columnIndex
	 *            column index
	 * @return long value
	 */
	public long getLong(int columnIndex);

	/**
	 * Returns the value of the requested column as an int.
	 * 
	 * @param columnIndex
	 *            column index
	 * @return int value
	 */
	public int getInt(int columnIndex);

	/**
	 * Returns the value of the requested column as an short.
	 * 
	 * @param columnIndex
	 *            column index
	 * @return short value
	 */
	public short getShort(int columnIndex);

	/**
	 * Returns the value of the requested column as an double.
	 * 
	 * @param columnIndex
	 *            column index
	 * @return double value
	 */
	public double getDouble(int columnIndex);

	/**
	 * Returns the value of the requested column as an float.
	 * 
	 * @param columnIndex
	 *            column index
	 * @return float value
	 */
	public float getFloat(int columnIndex);

	/**
	 * Returns the value of the requested column as a byte array.
	 * 
	 * @param columnIndex
	 *            column index
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
