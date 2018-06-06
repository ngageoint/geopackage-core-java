package mil.nga.geopackage.extension.related.dublin;

/**
 * Dublin Core Metadata Initiative term types
 * 
 * @author osbornb
 * @since 3.0.1
 */
public enum DublinCoreType {

	/**
	 * A point or period of time associated with an event in the lifecycle of
	 * the resource.
	 */
	DATE("date"),

	/**
	 * An account of the resource.
	 */
	DESCRIPTION("description"),

	/**
	 * The file format, physical medium, or dimensions of the resource.
	 */
	FORMAT("format", "content_type"),

	/**
	 * An unambiguous reference to the resource within a given context.
	 */
	IDENTIFIER("identifier", "id"),

	/**
	 * A related resource from which the described resource is derived.
	 */
	SOURCE("source"),

	/**
	 * A name given to the resource.
	 */
	TITLE("title");

	/**
	 * Term type name
	 */
	private final String name;

	/**
	 * Term type synonyms
	 */
	private final String[] synonyms;

	/**
	 * Constructor
	 * 
	 * @param name
	 *            term name
	 * @param synonyms
	 *            synonymous column names
	 */
	private DublinCoreType(String name, String... synonyms) {
		this.name = name;
		this.synonyms = synonyms;
	}

	/**
	 * Get the term name
	 * 
	 * @return term name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get the synonymous column names
	 * 
	 * @return synonyms
	 */
	public String[] getSynonyms() {
		return synonyms;
	}

	/**
	 * Get the Dublin Core Type from the term name
	 * 
	 * @param name
	 *            term name
	 * @return Dublin Core type
	 */
	public static DublinCoreType fromName(String name) {
		DublinCoreType dublinCoreType = null;
		if (name != null) {
			for (DublinCoreType type : DublinCoreType.values()) {
				if (name.equals(type.getName())) {
					dublinCoreType = type;
					break;
				}
			}
		}
		return dublinCoreType;
	}

}
