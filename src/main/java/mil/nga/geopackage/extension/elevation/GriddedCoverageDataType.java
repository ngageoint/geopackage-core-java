package mil.nga.geopackage.extension.elevation;

/**
 * Gridded Coverage data type enumeration
 * 
 * @author osbornb
 * @since 1.2.1
 */
public enum GriddedCoverageDataType {

	/**
	 * Integer
	 */
	INTEGER("integer"),

	/**
	 * Float
	 */
	FLOAT("float");

	/**
	 * Data type name
	 */
	private final String name;

	/**
	 * Constructor
	 * 
	 * @param name
	 */
	private GriddedCoverageDataType(String name) {
		this.name = name;
	}

	/**
	 * Get the name
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get the Data Type from the name
	 * 
	 * @param name
	 * @return
	 */
	public static GriddedCoverageDataType fromName(String name) {
		GriddedCoverageDataType dataType = null;
		if (name != null) {
			for (GriddedCoverageDataType type : GriddedCoverageDataType
					.values()) {
				if (name.equals(type.getName())) {
					dataType = type;
					break;
				}
			}
		}
		return dataType;
	}

}
