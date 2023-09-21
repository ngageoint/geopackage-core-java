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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((column == null) ? 0 : column.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DGIWGValidationKey other = (DGIWGValidationKey) obj;
		if (column == null) {
			if (other.column != null)
				return false;
		} else if (!column.equals(other.column))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

}
