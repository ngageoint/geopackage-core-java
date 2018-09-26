package mil.nga.geopackage.db;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import mil.nga.geopackage.GeoPackageException;

/**
 * Database Result utilities
 * 
 * @author osbornb
 * @since 3.1.0
 */
public class ResultUtils {

	/**
	 * Logger
	 */
	private static final Logger logger = Logger.getLogger(ResultUtils.class
			.getName());

	/**
	 * Integer field type
	 */
	public static final int FIELD_TYPE_INTEGER = 1;

	/**
	 * Float field type
	 */
	public static final int FIELD_TYPE_FLOAT = 2;

	/**
	 * String field type
	 */
	public static final int FIELD_TYPE_STRING = 3;

	/**
	 * Blob field type
	 */
	public static final int FIELD_TYPE_BLOB = 4;

	/**
	 * Null field type
	 */
	public static final int FIELD_TYPE_NULL = 0;

	/**
	 * Get the value from the cursor from the provided column
	 *
	 * @param result
	 *            result
	 * @param index
	 *            index
	 * @return value value
	 */
	public static Object getValue(Result result, int index) {
		return getValue(result, index, null);
	}

	/**
	 * Get the value from the cursor from the provided column
	 *
	 * @param result
	 *            result
	 * @param index
	 *            index
	 * @param dataType
	 *            data type
	 * @return value value
	 */
	public static Object getValue(Result result, int index,
			GeoPackageDataType dataType) {

		Object value = null;

		int type = result.getType(index);

		switch (type) {

		case FIELD_TYPE_INTEGER:
			value = getIntegerValue(result, index, dataType);
			break;

		case FIELD_TYPE_FLOAT:
			value = getFloatValue(result, index, dataType);
			break;

		case FIELD_TYPE_STRING:
			String stringValue = result.getString(index);

			if (dataType != null
					&& (dataType == GeoPackageDataType.DATE || dataType == GeoPackageDataType.DATETIME)) {
				DateConverter converter = DateConverter.converter(dataType);
				try {
					value = converter.dateValue(stringValue);
				} catch (Exception e) {
					logger.log(Level.WARNING,
							"Invalid " + dataType + " format: " + stringValue
									+ ", String value used", e);
					value = stringValue;
				}
			} else {
				value = stringValue;
			}
			break;

		case FIELD_TYPE_BLOB:
			value = result.getBlob(index);
			break;

		case FIELD_TYPE_NULL:
			// leave value as null
		}

		return value;
	}

	/**
	 * Get the integer value from the cursor of the column
	 *
	 * @param result
	 *            result
	 * @param index
	 *            index
	 * @param dataType
	 *            data type
	 * @return integer value
	 */
	public static Object getIntegerValue(Result result, int index,
			GeoPackageDataType dataType) {

		Object value = null;

		if (dataType == null) {
			dataType = GeoPackageDataType.INTEGER;
		}

		switch (dataType) {

		case BOOLEAN:
			short booleanValue = result.getShort(index);
			value = booleanValue == 0 ? Boolean.FALSE : Boolean.TRUE;
			break;
		case TINYINT:
			value = (byte) result.getShort(index);
			break;
		case SMALLINT:
			value = result.getShort(index);
			break;
		case MEDIUMINT:
			value = result.getInt(index);
			break;
		case INT:
		case INTEGER:
			value = result.getLong(index);
			break;
		default:
			throw new GeoPackageException("Data Type " + dataType
					+ " is not an integer type");
		}

		if (result.wasNull()) {
			value = null;
		}

		return value;
	}

	/**
	 * Get the float value from the cursor of the column
	 *
	 * @param result
	 *            result
	 * @param index
	 *            index
	 * @param dataType
	 *            data type
	 * @return float value
	 */
	public static Object getFloatValue(Result result, int index,
			GeoPackageDataType dataType) {

		Object value = null;

		if (dataType == null) {
			dataType = GeoPackageDataType.DOUBLE;
		}

		switch (dataType) {

		case FLOAT:
			value = result.getFloat(index);
			break;
		case DOUBLE:
		case REAL:
		case INTEGER:
		case INT:
			value = result.getDouble(index);
			break;
		default:
			throw new GeoPackageException("Data Type " + dataType
					+ " is not a float type");
		}

		if (result.wasNull()) {
			value = null;
		}

		return value;
	}

	/**
	 * Build single result value from the column
	 * 
	 * @param result
	 *            result
	 * @param column
	 *            column index
	 * @param dataType
	 *            GeoPackage data type
	 * @return value
	 * @since 3.1.0
	 */
	public static Object buildSingleResult(Result result, int column,
			GeoPackageDataType dataType) {

		Object value = null;
		try {
			if (result.moveToNext()) {
				value = result.getValue(column, dataType);
			}
		} finally {
			result.close();
		}

		return value;
	}

	/**
	 * Build single column result rows from the result and the optional limit
	 * 
	 * @param result
	 *            result
	 * @param column
	 *            column index
	 * @param dataType
	 *            GeoPackage data type
	 * @param limit
	 *            result row limit
	 * @return single column results
	 * @since 3.1.0
	 */
	public static List<Object> buildSingleColumnResults(Result result,
			int column, GeoPackageDataType dataType, Integer limit) {

		List<Object> results = new ArrayList<>();
		try {
			while (result.moveToNext()) {
				Object value = result.getValue(column, dataType);
				results.add(value);
				if (limit != null && results.size() >= limit) {
					break;
				}
			}
		} finally {
			result.close();
		}

		return results;
	}

	/**
	 * Build the result rows from the result and the optional limit
	 * 
	 * @param result
	 *            result
	 * @param dataTypes
	 *            column data types
	 * @param limit
	 *            result row limit
	 * @return results
	 */
	public static List<List<Object>> buildResults(Result result,
			GeoPackageDataType[] dataTypes, Integer limit) {

		List<List<Object>> results = new ArrayList<>();
		try {
			int columns = result.getColumnCount();
			while (result.moveToNext()) {
				List<Object> row = new ArrayList<>();
				for (int i = 0; i < columns; i++) {
					row.add(result.getValue(i, dataTypes != null ? dataTypes[i]
							: null));
				}
				results.add(row);
				if (limit != null && results.size() >= limit) {
					break;
				}
			}
		} finally {
			result.close();
		}

		return results;
	}

}
