package mil.nga.geopackage.extension.related;

import mil.nga.geopackage.contents.ContentsDataType;

/**
 * Spec supported User-Defined Related Data Tables
 * 
 * @author osbornb
 * @since 3.0.1
 */
public enum RelationType {

	/**
	 * Link features with other features
	 */
	FEATURES("features", ContentsDataType.FEATURES),

	/**
	 * Relate sets of tabular text or numeric data
	 */
	SIMPLE_ATTRIBUTES("simple_attributes", ContentsDataType.ATTRIBUTES),

	/**
	 * Relate features or attributes to multimedia files such as pictures and
	 * videos
	 */
	MEDIA("media", ContentsDataType.ATTRIBUTES),

	/**
	 * Relate features or attributes with attributes
	 * 
	 * @since 3.2.0
	 */
	ATTRIBUTES(ContentsDataType.ATTRIBUTES.getName(), ContentsDataType.ATTRIBUTES),

	/**
	 * Relate features or attributes with tiles
	 * 
	 * @since 3.2.0
	 */
	TILES(ContentsDataType.TILES.getName(), ContentsDataType.TILES);

	/**
	 * Relation type name
	 */
	private final String name;

	/**
	 * Relation type contents data type
	 */
	private final String dataType;

	/**
	 * Constructor
	 * 
	 * @param name
	 *            relation name
	 * @param dataType
	 *            contents data type
	 */
	private RelationType(String name, ContentsDataType dataType) {
		this(name, dataType.getName());
	}

	/**
	 * Constructor
	 * 
	 * @param name
	 *            relation name
	 * @param dataType
	 *            contents data type
	 */
	private RelationType(String name, String dataType) {
		this.name = name;
		this.dataType = dataType;
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
	 * Get the contents data type
	 * 
	 * @return contents data type
	 * @since 3.1.0
	 */
	public String getDataType() {
		return dataType;
	}

	/**
	 * Get the Relation Type from the name
	 * 
	 * @param name
	 *            name
	 * @return relation type
	 */
	public static RelationType fromName(String name) {
		RelationType relationType = null;
		if (name != null) {
			for (RelationType type : RelationType.values()) {
				if (name.equals(type.getName())) {
					relationType = type;
					break;
				}
			}
		}
		return relationType;
	}

}
