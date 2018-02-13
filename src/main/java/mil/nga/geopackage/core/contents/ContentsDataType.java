package mil.nga.geopackage.core.contents;

/**
 * Contents data type enumeration
 * 
 * @author osbornb
 */
public enum ContentsDataType {

	/**
	 * Features
	 */
	FEATURES("features"),

	/**
	 * Tiles
	 */
	TILES("tiles"),

	/**
	 * Attributes
	 * 
	 * @since 1.2.1
	 */
	ATTRIBUTES("attributes"),

	/**
	 * Tiled Gridded Coverage Data Extension
	 * 
	 * @since 2.0.1
	 */
	GRIDDED_COVERAGE("2d-gridded-coverage");

	/**
	 * Data type name
	 */
	private final String name;

	/**
	 * Constructor
	 * 
	 * @param name
	 */
	private ContentsDataType(String name) {
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
	 * Get the Data Type from the name
	 * 
	 * @param name
	 *            name
	 * @return contents data type
	 */
	public static ContentsDataType fromName(String name) {
		ContentsDataType dataType = null;
		if (name != null) {
			for (ContentsDataType type : ContentsDataType.values()) {
				if (name.equals(type.getName())) {
					dataType = type;
					break;
				}
			}
		}
		return dataType;
	}

}
