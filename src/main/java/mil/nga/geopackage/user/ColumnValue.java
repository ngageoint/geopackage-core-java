package mil.nga.geopackage.user;

/**
 * Column Value wrapper to specify additional value attributes, such as a range
 * tolerance for floating point numbers
 * 
 * @author osbornb
 */
public class ColumnValue {

	/**
	 * Value
	 */
	private final Object value;

	/**
	 * Value tolerance
	 */
	private final Double tolerance;

	/**
	 * Constructor
	 * 
	 * @param value
	 *            value
	 */
	public ColumnValue(Object value) {
		this(value, null);
	}

	/**
	 * Constructor
	 * 
	 * @param value
	 *            value
	 * @param tolerance
	 *            tolerance
	 */
	public ColumnValue(Object value, Double tolerance) {
		this.value = value;
		this.tolerance = tolerance;
	}

	/**
	 * Get the value
	 * 
	 * @return value
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * Get the tolerance
	 * 
	 * @return tolerance
	 */
	public Double getTolerance() {
		return tolerance;
	}

}
