package mil.nga.geopackage.extension.metadata;

import java.util.Locale;

/**
 * Metadata Scope type enumeration from spec Table 15
 * 
 * @author osbornb
 */
public enum MetadataScopeType {

	/**
	 * Metadata information scope is undefined
	 */
	UNDEFINED("undefined", "NA", "Metadata information scope is undefined"),

	/**
	 * Information applies to the field session
	 */
	FIELD_SESSION("fieldSession", "012",
			"Information applies to the field session"),

	/**
	 * Information applies to the collection session
	 */
	COLLECTION_SESSION("collectionSession", "004",
			"Information applies to the collection session"),

	/**
	 * Information applies to the (dataset) series
	 */
	SERIES("series", "006", "Information applies to the (dataset) series"),

	/**
	 * Information applies to the (geographic feature) dataset
	 */
	DATASET("dataset", "005",
			"Information applies to the (geographic feature) dataset"),

	/**
	 * Information applies to a feature type (class)
	 */
	FEATURE_TYPE("featureType", "010",
			"Information applies to a feature type (class)"),

	/**
	 * Information applies to a feature (instance)
	 */
	FEATURE("feature", "009", "Information applies to a feature (instance)"),

	/**
	 * Information applies to the attribute class
	 */
	ATTRIBUTE_TYPE("attributeType", "002",
			"Information applies to the attribute class"),

	/**
	 * Information applies to the characteristic of a feature (instance)
	 */
	ATTRIBUTE("attribute", "001",
			"Information applies to the characteristic of a feature (instance)"),

	/**
	 * Information applies to a tile, a spatial subset of geographic data
	 */
	TILE("tile", "016",
			"Information applies to a tile, a spatial subset of geographic data"),

	/**
	 * Information applies to a copy or imitation of an existing or hypothetical
	 * object
	 */
	MODEL("model", "015",
			"Information applies to a copy or imitation of an existing or hypothetical object"),

	/**
	 * Metadata applies to a feature catalog
	 */
	CATALOG("catalog", "NA", "Metadata applies to a feature catalog"),

	/**
	 * Metadata applies to an application schema
	 */
	SCHEMA("schema", "NA", "Metadata applies to an application schema"),

	/**
	 * Metadata applies to a taxonomy or knowledge system
	 */
	TAXONOMY("taxonomy", "NA",
			"Metadata applies to a taxonomy or knowledge system"),

	/**
	 * Information applies to a computer program or routine
	 */
	SOFTWARE("software", "013",
			"Information applies to a computer program or routine"),

	/**
	 * Information applies to a capability which a service provider entity makes
	 * available to a service user entity through a set of interfaces that
	 * define a behavior, such as a use case
	 */
	SERVICE("service", "014",
			"Information applies to a capability which a service provider entity makes available to a service user entity through a set of interfaces that define a behaviour, such as a use case"),

	/**
	 * Information applies to the collection hardware class
	 */
	COLLECTION_HARDWARE("collectionHardware", "003",
			"Information applies to the collection hardware class"),

	/**
	 * Information applies to non-geographic data
	 */
	NON_GEOGRAPHIC_DATASET("nonGeographicDataset", "007",
			"Information applies to non-geographic data"),

	/**
	 * Information applies to a dimension group
	 */
	DIMENSION_GROUP("dimensionGroup", "008",
			"Information applies to a dimension group"),

	/**
	 * Information applies to a specific style
	 * 
	 * @since 4.0.0
	 */
	STYLE("style", "NA", "Information applies to a specific style");

	/**
	 * Name
	 */
	private final String name;

	/**
	 * Code
	 */
	private final String code;

	/**
	 * Definition
	 */
	private final String definition;

	/**
	 * Constructor
	 * 
	 * @param name
	 *            name of the data scope
	 * @param code
	 *            scope code
	 * @param definition
	 *            scope definition
	 */
	private MetadataScopeType(String name, String code, String definition) {
		this.name = name;
		this.code = code;
		this.definition = definition;
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
	 * Get the code
	 * 
	 * @return code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * Get the definition
	 * 
	 * @return definition
	 */
	public String getDefinition() {
		return definition;
	}

	/**
	 * Get the metadata scope from the name
	 * 
	 * @param name
	 *            name
	 * @return scope type
	 */
	public static MetadataScopeType fromName(String name) {

		StringBuilder enumName = new StringBuilder();

		for (String part : name.split("(?<!^)(?=[A-Z])")) {
			if (enumName.length() > 0) {
				enumName.append("_");
			}
			enumName.append(part.toUpperCase(Locale.US));
		}

		return MetadataScopeType.valueOf(enumName.toString());
	}

}
