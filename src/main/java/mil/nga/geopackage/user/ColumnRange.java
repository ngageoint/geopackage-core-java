package mil.nga.geopackage.user;

/**
 * Column Range wrapper to specify a range and additional attributes, such as a
 * tolerance for floating point numbers
 * 
 * @author osbornb
 * @since 6.6.7
 */
public class ColumnRange {

	/**
	 * Min Value
	 */
	private final Number min;

	/**
	 * Max Value
	 */
	private final Number max;

	/**
	 * Value tolerance
	 */
	private final Double tolerance;

	/**
	 * Constructor
	 * 
	 * @param min
	 *            min value
	 * @param max
	 *            max value
	 */
	public ColumnRange(Number min, Number max) {
		this(min, max, null);
	}

	/**
	 * Constructor
	 * 
	 * @param min
	 *            min value
	 * @param max
	 *            max value
	 * @param tolerance
	 *            tolerance
	 */
	public ColumnRange(Number min, Number max, Double tolerance) {
		this.min = min;
		this.max = max;
		this.tolerance = tolerance;
	}

	/**
	 * Get the min value
	 * 
	 * @return min value
	 */
	public Number getMin() {
		return min;
	}

	/**
	 * Get the max value
	 * 
	 * @return max value
	 */
	public Number getMax() {
		return max;
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
