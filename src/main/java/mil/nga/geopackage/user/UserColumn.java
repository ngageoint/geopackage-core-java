package mil.nga.geopackage.user;

import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.db.GeoPackageDataType;

/**
 * Metadata about a single column from a user table
 * 
 * @author osbornb
 */
public abstract class UserColumn implements Comparable<UserColumn> {

	/**
	 * User Column index value
	 * 
	 * @since 3.2.1
	 */
	public static final int NO_INDEX = -1;

	/**
	 * Column index
	 */
	private int index;

	/**
	 * Column name
	 */
	private String name;

	/**
	 * Max size
	 */
	private final Long max;

	/**
	 * True if a not null column
	 */
	private final boolean notNull;

	/**
	 * Default column value
	 */
	private final Object defaultValue;

	/**
	 * True if a primary key column
	 */
	private final boolean primaryKey;

	/**
	 * Data type
	 */
	private final GeoPackageDataType dataType;

	/**
	 * Constructor
	 * 
	 * @param index
	 *            column index
	 * @param name
	 *            column name
	 * @param dataType
	 *            data type
	 * @param max
	 *            max value
	 * @param notNull
	 *            not null flag
	 * @param defaultValue
	 *            default value
	 * @param primaryKey
	 *            primary key flag
	 */
	protected UserColumn(int index, String name, GeoPackageDataType dataType,
			Long max, boolean notNull, Object defaultValue, boolean primaryKey) {
		this.index = index;
		this.name = name;
		this.max = max;
		this.notNull = notNull;
		this.defaultValue = defaultValue;
		this.primaryKey = primaryKey;
		this.dataType = dataType;

		validateMax();
	}

	/**
	 * Copy Constructor
	 * 
	 * @param userColumn
	 *            user column
	 * @since 3.2.1
	 */
	protected UserColumn(UserColumn userColumn) {
		this.index = userColumn.index;
		this.name = userColumn.name;
		this.max = userColumn.max;
		this.notNull = userColumn.notNull;
		this.defaultValue = userColumn.defaultValue;
		this.primaryKey = userColumn.primaryKey;
		this.dataType = userColumn.dataType;
	}

	/**
	 * Copy the column
	 * 
	 * @return copied column
	 * @since 3.2.1
	 */
	public abstract UserColumn copy();

	/**
	 * Check if the column has a valid index
	 * 
	 * @return true if has a valid index
	 * @since 3.2.1
	 */
	public boolean hasIndex() {
		return this.index > NO_INDEX;
	}

	/**
	 * Set the column index. Only allowed when {@link #hasIndex()} is false (
	 * {@link #getIndex()} is {@link #NO_INDEX}). Setting a valid index to an
	 * existing valid index does nothing.
	 * 
	 * @param index
	 *            column index
	 * @since 3.2.1
	 */
	public void setIndex(int index) {
		if (hasIndex()) {
			if (index != this.index) {
				throw new GeoPackageException(
						"User Column with a valid index may not be changed. Column Name: "
								+ name + ", Index: " + this.index
								+ ", Attempted Index: " + index);
			}
		} else {
			this.index = index;
		}
	}

	/**
	 * Reset the column index
	 * 
	 * @since 3.2.1
	 */
	protected void resetIndex() {
		this.index = NO_INDEX;
	}

	/**
	 * Get the index
	 * 
	 * @return index
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * Set the name
	 * 
	 * @param name
	 *            column name
	 * @since 3.2.1
	 */
	protected void setName(String name) {
		this.name = name;
	}

	/**
	 * Get the name
	 * 
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Determine if this column is named the provided name
	 * 
	 * @param name
	 *            column name
	 * @return true if named the provided name
	 * @since 3.0.1
	 */
	public boolean isNamed(String name) {
		return this.name.equals(name);
	}

	/**
	 * Determine if the column has a max value
	 * 
	 * @return true if has max value
	 * @since 3.2.1
	 */
	public boolean hasMax() {
		return max != null;
	}

	/**
	 * Get the max
	 * 
	 * @return max
	 */
	public Long getMax() {
		return max;
	}

	/**
	 * Get the is not null flag
	 * 
	 * @return not null flag
	 */
	public boolean isNotNull() {
		return notNull;
	}

	/**
	 * Determine if the column has a default value
	 * 
	 * @return true if has default value
	 * @since 3.2.1
	 */
	public boolean hasDefaultValue() {
		return defaultValue != null;
	}

	/**
	 * Get the default value
	 * 
	 * @return default value
	 */
	public Object getDefaultValue() {
		return defaultValue;
	}

	/**
	 * Get the primary key flag
	 * 
	 * @return primary key flag
	 */
	public boolean isPrimaryKey() {
		return primaryKey;
	}

	/**
	 * When not a geometry column, gets the data type
	 * 
	 * @return data type
	 */
	public GeoPackageDataType getDataType() {
		return dataType;
	}

	/**
	 * Get the database type name
	 * 
	 * @return type name
	 */
	public String getTypeName() {
		String type = null;
		if (dataType != null) {
			type = dataType.name();
		}
		return type;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * Sort by index
	 */
	@Override
	public int compareTo(UserColumn another) {
		return index - another.index;
	}

	/**
	 * Validate that if max is set, the data type is text or blob
	 */
	private void validateMax() {

		if (max != null) {
			if (dataType == null) {
				throw new GeoPackageException(
						"Column max is only supported for data typed columns. column: "
								+ name + ", max: " + max);
			} else if (dataType != GeoPackageDataType.TEXT
					&& dataType != GeoPackageDataType.BLOB) {
				throw new GeoPackageException(
						"Column max is only supported for "
								+ GeoPackageDataType.TEXT.name() + " and "
								+ GeoPackageDataType.BLOB.name()
								+ " columns. column: " + name + ", max: " + max
								+ ", type: " + dataType.name());
			}
		}

	}

}
