package mil.nga.geopackage.dgiwg;

/**
 * DGIWG (Defence Geospatial Information Working Group) Requirements
 * 
 * @author osbornb
 * @since 6.5.1
 */
public enum DGIWGRequirement {

	/**
	 * Requirement 1
	 */
	GEOPACKAGE_BASE("GeoPackage Base definition"),

	/**
	 * Requirement 2
	 */
	GEOPACKAGE_OPTIONS("GeoPackage Options definition"),

	/**
	 * Requirement 3
	 */
	EXTENSIONS_MANDATORY("Mandatory Extensions"),

	/**
	 * Requirement 4
	 */
	EXTENSIONS_OPTIONAL("Optional Extensions"),

	/**
	 * Requirement 5
	 */
	EXTENSIONS_NOT_ALLOWED("Extensions Not Allowed"),

	/**
	 * Requirement 6
	 */
	EXTENSIONS_CONDITIONAL("Conditional Extensions"),

	/**
	 * Requirement 7
	 */
	CRS_RASTER_ALLOWED("Raster CRS Allowed"),

	/**
	 * Requirement 8
	 */
	CRS_RASTER_TILE_MATRIX_SET("CRS Raster tile matrix set"),

	/**
	 * Requirement 9
	 */
	CRS_2D_VECTOR("Two-Dimensional Vector CRS"),

	/**
	 * Requirement 10
	 */
	CRS_3D_VECTOR("Three-Dimensional Vector CRS"),

	/**
	 * Requirement 11
	 */
	CRS_WKT("Well Known Text for CRS"),

	/**
	 * Requirement 12
	 */
	CRS_COMPOUND("Compound CRS Usage"),

	/**
	 * Requirement 13
	 */
	CRS_COMPOUND_WKT("Compound CRS Well Known Text"),

	/**
	 * Requirement 14
	 */
	METADATA_DMF("GeoPackage Metadata DMF"),

	/**
	 * Requirement 15
	 */
	METADATA_GPKG("GeoPackage Metadata document"),

	/**
	 * Requirement 16
	 */
	METADATA_ROW("Complete Row GeoPackage Metadata"),

	/**
	 * Requirement 17
	 */
	METADATA_USER("User Row GeoPackage Metadata"),

	/**
	 * Requirement 18
	 */
	METADATA_PRODUCT("GeoPackage Product Metadata"),

	/**
	 * Requirement 19
	 */
	METADATA_PRODUCT_PARTIAL("GeoPackage Product Partial Metadata"),

	/**
	 * Requirement 20
	 */
	VALIDITY("GeoPackage Data Validity"),

	/**
	 * Requirement 21
	 */
	TILE_SIZE_MATRIX("Tile Matrix Width Height"),

	/**
	 * Requirement 22
	 */
	TILE_SIZE_DATA("Tile Pyramid Width Height"),

	/**
	 * Requirement 23
	 */
	ZOOM_FACTOR("Zoom levels"),

	/**
	 * Requirement 24
	 */
	MATRIX_SETS_MULTIPLE_ZOOM("Multiple Zoom Tile Matrix Sets"),

	/**
	 * Requirement 25
	 */
	MATRIX_SETS_ONE_ZOOM("Tile Matrix sets with one Zoom Level"),

	/**
	 * Requirement 26
	 */
	BBOX_CRS("Tile Matrix Set CRS Bounding box"),

	/**
	 * Requirement 27
	 */
	BBOX_TILE_PYRAMID_MBR("Bounding box Tile Pyramid"),

	/**
	 * Requirement 28
	 */
	TILE_METADATA("Tile layer Metadata"),

	/**
	 * Requirement 29
	 */
	VECTOR_METADATA("Vector layer Metadata");

	/**
	 * Identifier prefix
	 */
	public static final String IDENTIFIER_PREFIX = "/req/";

	/**
	 * Number
	 */
	private final int number;

	/**
	 * Name
	 */
	private final String name;

	/**
	 * Identifier
	 */
	private final String identifier;

	/**
	 * Constructor
	 * 
	 * @param dataType
	 *            data type
	 * @param dimension
	 *            dimension
	 */
	private DGIWGRequirement(String name) {
		this.number = ordinal() + 1;
		this.name = name;
		this.identifier = name().toLowerCase().replaceAll("_", "-");
	}

	/**
	 * Get the requirement number
	 * 
	 * @return number
	 */
	public int getNumber() {
		return number;
	}

	/**
	 * Get the requirement name
	 * 
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get the requirement identifier
	 * 
	 * @return identifier
	 */
	public String getIdentifier() {
		return identifier;
	}

	/**
	 * Get the full requirement identifier with prefix
	 * 
	 * @return identifier
	 */
	public String getFullIdentifier() {
		return IDENTIFIER_PREFIX + identifier;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder toString = new StringBuilder();
		toString.append("Number: ").append(number);
		toString.append(", Name: ").append(name);
		toString.append(", Identifier: ").append(getFullIdentifier());
		return toString.toString();
	}

}
