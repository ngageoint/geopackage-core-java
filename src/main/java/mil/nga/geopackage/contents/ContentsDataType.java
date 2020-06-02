package mil.nga.geopackage.contents;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import mil.nga.geopackage.GeoPackageException;
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
	FEATURES,

	/**
	 * Tiles
	 */
	TILES,

	/**
	 * Attributes
	 * 
	 * @since 1.2.1
	 */
	ATTRIBUTES;

	/**
	 * Get the name
	 *
	 * @return name
	 */
	public String getName() {
		return toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return name().toLowerCase();
	}

	/**
	 * Logger
	 */
	private static final Logger log = Logger
			.getLogger(ContentsDataType.class.getName());

	/**
	 * Set of core data types
	 */
	private static final Set<String> coreTypes = new HashSet<>();

	/**
	 * Mapping between data type values and the type of data
	 */
	private static final Map<String, ContentsDataType> types = new HashMap<>();

	/**
	 * Initialize core data types
	 */
	static {
		for (ContentsDataType dataType : ContentsDataType.values()) {
			String name = dataType.getName();
			coreTypes.add(name);
			types.put(name, dataType);
		}
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

				String value = GeoPackageProperties.getProperty(false,
						PropertyConstants.CONTENTS_DATA_TYPE, name);
				if (value != null) {

					dataType = types.get(value.toLowerCase());

					if (dataType == null) {
						log.log(Level.WARNING,
								"Unsupported configured data type: " + value
										+ ", for contents data type name: "
										+ name);
					}

				}

				types.put(lowerName, dataType);
			}
		}

		return dataType;
	}

	/**
	 * Determine if the type name is a registered data type
	 * 
	 * @param name
	 *            type name
	 * @return true if a core contents data type
	 * @since 4.0.0
	 */
	public static boolean isType(String name) {
		return fromName(name) != null;
	}

	/**
	 * Get the contents data type from a core type name
	 * 
	 * @param name
	 *            type name
	 * @return contents data type if core, null if not
	 * @since 4.0.0
	 */
	public static ContentsDataType fromCoreName(String name) {
		ContentsDataType dataType = null;
		if (isCoreType(name)) {
			dataType = types.get(name.toLowerCase());
		}
		return dataType;
	}

	/**
	 * Determine if the type name is a core contents data type
	 * 
	 * @param name
	 *            type name
	 * @return true if a core contents data type
	 * @since 4.0.0
	 */
	public static boolean isCoreType(String name) {
		boolean coreType = false;
		if (name != null) {
			coreType = coreTypes.contains(name.toLowerCase());
		}
		return coreType;
	}

	/**
	 * Set the type for the contents data type name
	 * 
	 * @param name
	 *            contents data type name
	 * @param type
	 *            contents data type
	 * @since 4.0.0
	 */
	public static void setType(String name, ContentsDataType type) {

		if (name != null) {

			String lowerName = name.toLowerCase();
			ContentsDataType dataType = types.get(lowerName);

			if (dataType == null) {

				types.put(lowerName, type);

			} else if (dataType != type) {

				if (coreTypes.contains(lowerName)) {
					throw new GeoPackageException(
							"Core contents data type name '" + name
									+ "' can not be changed to type '"
									+ type.getName() + "'");
				}

				log.log(Level.WARNING,
						"Changed contents data type name '" + name
								+ "' from type '" + dataType.getName()
								+ "' to type '" + type.getName() + "'");

				types.put(lowerName, type);
			}

		}

	}

	/**
	 * Determine if the contents data type name is the type
	 * 
	 * @param name
	 *            contents data type name
	 * @param type
	 *            comparison contents data type
	 * @return true if matching core types
	 * @since 4.0.0
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
	 * @since 4.0.0
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
	 * @since 4.0.0
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
	 * @since 4.0.0
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
	 * @since 4.0.0
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
	 * @since 4.0.0
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
	 * @since 4.0.0
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
	 * @since 4.0.0
	 */
	public static boolean isAttributesType(String name, boolean matchUnknown) {
		return isType(name, ContentsDataType.ATTRIBUTES, matchUnknown);
	}

}
