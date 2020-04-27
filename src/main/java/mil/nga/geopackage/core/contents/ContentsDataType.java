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
	GRIDDED_COVERAGE("2d-gridded-coverage"),

	/**
	 * Vector Tiles Extension
	 */
	VECTOR_TILES("vector-tiles");

	/**
	 * Data type name
	 */
	private final String name;

	/**
	 * Constructor
	 *
	 * @param name
	 *            data type name
	 */
	private ContentsDataType(final String name) {
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

	/**
	 * Is the data type a tiles type
	 * 
	 * @return true if a tiles type
	 * @since 3.5.1
	 */
	public boolean isTilesType() {
		return (this == TILES) || (this == GRIDDED_COVERAGE)
				|| (this == VECTOR_TILES);
	}

}
