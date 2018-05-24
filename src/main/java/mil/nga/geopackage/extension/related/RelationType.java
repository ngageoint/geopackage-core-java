package mil.nga.geopackage.extension.related;

/**
 * Spec suppoerted User-Defined Related Data Tables
 * 
 * @author osbornb
 * @since 3.0.1
 */
public enum RelationType {

	/**
	 * Link features with other features
	 */
	FEATURES("features"),

	/**
	 * Relate sets of tabular text or numeric data
	 */
	SIMPLE_ATTRIBUTES("simple_attributes"),

	/**
	 * Relate features or attributes to multimedia files such as pictures and
	 * videos
	 */
	MEDIA("media");

	/**
	 * Relation type name
	 */
	private final String name;

	/**
	 * Constructor
	 * 
	 * @param name
	 *            relation name
	 */
	private RelationType(String name) {
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
