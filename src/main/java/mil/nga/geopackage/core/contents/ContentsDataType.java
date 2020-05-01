package mil.nga.geopackage.core.contents;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import mil.nga.geopackage.property.GeoPackageProperties;
import mil.nga.geopackage.property.PropertyConstants;

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
	GRIDDED_COVERAGE("2d-gridded-coverage"); // TODO remove

	/**
	 * Logger
	 */
	private static final Logger log = Logger
			.getLogger(ContentsDataType.class.getName());

	/**
	 * Mapping between data type values and the type of data
	 */
	private static final Map<String, ContentsDataType> types = new HashMap<>();

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
	 *            contents data type name
	 * @return contents data type or null
	 */
	public static ContentsDataType fromName(String name) {

		ContentsDataType dataType = null;

		if (name != null) {

			String lowerName = name.toLowerCase();
			dataType = types.get(lowerName);

			if (dataType == null && !types.containsKey(lowerName)) {

				dataType = fromCoreName(name);

				if (dataType == null) {

					String value = GeoPackageProperties.getProperty(
							PropertyConstants.CONTENTS_DATA_TYPE, name, false);
					if (value != null) {

						dataType = fromCoreName(value);

						if (dataType == null) {
							log.log(Level.WARNING,
									"Unsupported configured core data type: "
											+ value
											+ ", for contents data type: "
											+ name);
						}

					} else {
						log.log(Level.INFO,
								"Unknown core data type for contents data type: "
										+ name);
					}
				}

				types.put(lowerName, dataType);
			}
		}

		return dataType;
	}

	/**
	 * Find the core or base contents data type from the name
	 * 
	 * @param name
	 *            contents data type name
	 * @return contents data type or null
	 * @since 3.5.1
	 */
	public static ContentsDataType fromCoreName(String name) {
		ContentsDataType dataType = null;
		for (ContentsDataType type : ContentsDataType.values()) {
			if (name.equalsIgnoreCase(type.getName())) {
				dataType = type;
				break;
			}
		}
		return dataType;
	}

	/**
	 * Determine if the contents data type name is the type
	 * 
	 * @param name
	 *            contents data type name
	 * @param type
	 *            comparison contents data type
	 * @return true if matching core types
	 * @since 3.5.1
	 */
	public static boolean isType(String name, ContentsDataType type) {
		return isType(name, type, false);
	}

	/**
	 * Determine if the contents data type name is the type
	 * 
	 * @param name
	 *            contents data type name
	 * @param type
	 *            comparison contents data type
	 * @param matchUnknown
	 *            true to match unknown data types
	 * @return true if matching core types or matched unknown
	 * @since 3.5.1
	 */
	public static boolean isType(String name, ContentsDataType type,
			boolean matchUnknown) {
		boolean isType;
		ContentsDataType dataType = fromName(name);
		if (dataType != null) {
			isType = dataType == type;
		} else {
			isType = matchUnknown;
		}
		return isType;
	}

	/**
	 * Determine if the contents data type name is a features type
	 * 
	 * @param name
	 *            contents data type name
	 * @return true if a features type
	 * @since 3.5.1
	 */
	public static boolean isFeaturesType(String name) {
		return isFeaturesType(name, false);
	}

	/**
	 * Determine if the contents data type name is a features type
	 * 
	 * @param name
	 *            contents data type name
	 * @param matchUnknown
	 *            true to match unknown data types
	 * @return true if a features type or matched unknown
	 * @since 3.5.1
	 */
	public static boolean isFeaturesType(String name, boolean matchUnknown) {
		return isType(name, ContentsDataType.FEATURES, matchUnknown);
	}

	/**
	 * Determine if the contents data type name is a tiles type
	 * 
	 * @param name
	 *            contents data type name
	 * @return true if a tiles type
	 * @since 3.5.1
	 */
	public static boolean isTilesType(String name) {
		return isTilesType(name, false);
	}

	/**
	 * Determine if the contents data type name is a tiles type
	 * 
	 * @param name
	 *            contents data type name
	 * @param matchUnknown
	 *            true to match unknown data types
	 * @return true if a tiles type or matched unknown
	 * @since 3.5.1
	 */
	public static boolean isTilesType(String name, boolean matchUnknown) {
		return isType(name, ContentsDataType.TILES, matchUnknown);
	}

	/**
	 * Determine if the contents data type name is an attributes type
	 * 
	 * @param name
	 *            contents data type name
	 * @return true if an attributes type
	 * @since 3.5.1
	 */
	public static boolean isAttributesType(String name) {
		return isAttributesType(name, false);
	}

	/**
	 * Determine if the contents data type name is an attributes type
	 * 
	 * @param name
	 *            contents data type name
	 * @param matchUnknown
	 *            true to match unknown data types
	 * @return true if an attributes type or matched unknown
	 * @since 3.5.1
	 */
	public static boolean isAttributesType(String name, boolean matchUnknown) {
		return isType(name, ContentsDataType.ATTRIBUTES, matchUnknown);
	}

}
