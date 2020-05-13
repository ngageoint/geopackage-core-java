package mil.nga.geopackage.extension.schema.constraints;

import java.util.Locale;

/**
 * Enumeration of Data Column Constraint Types
 * 
 * @author osbornb
 */
public enum DataColumnConstraintType {

	/**
	 * Value range
	 */
	RANGE,

	/**
	 * Enumerated values
	 */
	ENUM,

	/**
	 * Pattern matching
	 */
	GLOB;

	/**
	 * Query value
	 */
	private final String value;

	/**
	 * Constructor
	 */
	private DataColumnConstraintType() {
		value = name().toLowerCase(Locale.US);
	}

	/**
	 * Get the value
	 * 
	 * @return value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Get the type from the value
	 * 
	 * @param value
	 *            value
	 * @return data column constraint type
	 */
	public static DataColumnConstraintType fromValue(String value) {
		return DataColumnConstraintType.valueOf(value.toUpperCase(Locale.US));
	}

}
