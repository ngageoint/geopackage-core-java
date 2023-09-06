package mil.nga.geopackage.dgiwg;

/**
 * DGIWG (Defence Geospatial Information Working Group) validation primary key
 * value
 * 
 * @author osbornb
 */
public class DGIWGValidationKey {

	/**
	 * Column name
	 */
	private String column;

	/**
	 * Value
	 */
	private String value;

	/**
	 * Constructor
	 * 
	 * @param column
	 *            column name
	 * @param value
	 *            column value
	 */
	public DGIWGValidationKey(String column, String value) {
		this.column = column;
		this.value = value;
	}

	/**
	 * Constructor
	 * 
	 * @param column
	 *            column name
	 * @param value
	 *            column value
	 */
	public DGIWGValidationKey(String column, Number value) {
		this(column, value.toString());
	}

	/**
	 * Get the column name
	 * 
	 * @return column
	 */
	public String getColumn() {
		return column;
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
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder toString = new StringBuilder();
		toString.append("Key Column: ").append(column);
		toString.append(", Value: ").append(
				DGIWGGeoPackageUtils.wrapIfEmptyOrContainsWhitespace(value));
		return toString.toString();
	}

}
