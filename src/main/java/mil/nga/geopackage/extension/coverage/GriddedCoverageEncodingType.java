package mil.nga.geopackage.extension.coverage;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Gridded Coverage grid cell encoding type enumeration
 * 
 * @author osbornb
 * @since 2.0.1
 */
public enum GriddedCoverageEncodingType {

	/**
	 * Assume the value is center of grid cell (default)
	 */
	CENTER("grid-value-is-center"),

	/**
	 * Assume the entire grid cell has the same value
	 */
	AREA("grid-value-is-area"),

	/**
	 * A typical use case is for a mesh of elevation values as specified in the
	 * OGC CDB standard Clause 5.6.1 (lower left corner)
	 */
	CORNER("grid-value-is-corner");

	/**
	 * Logger
	 */
	private static final Logger logger = Logger
			.getLogger(GriddedCoverageEncodingType.class.getName());

	/**
	 * grid cell encoding name
	 */
	private final String name;

	/**
	 * Constructor
	 * 
	 * @param name
	 */
	private GriddedCoverageEncodingType(String name) {
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
	 * Get the grid cell encoding from the name
	 * 
	 * @param name
	 *            name
	 * @return grid cell encoding type
	 */
	public static GriddedCoverageEncodingType fromName(String name) {
		GriddedCoverageEncodingType encodingType = null;
		if (name != null) {
			for (GriddedCoverageEncodingType type : GriddedCoverageEncodingType
					.values()) {
				if (name.equalsIgnoreCase(type.getName())) {
					encodingType = type;
					break;
				}
			}
			if (encodingType == null) {
				logger.log(Level.WARNING, "Unsupported "
						+ GriddedCoverageEncodingType.class.getSimpleName()
						+ ": " + name + ", Defaulting to : " + CENTER.name);
				encodingType = CENTER;
			}
		} else {
			encodingType = CENTER;
		}

		return encodingType;
	}

}
