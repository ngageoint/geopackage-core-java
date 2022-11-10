package mil.nga.geopackage.dgiwg;

/**
 * DGIWG (Defence Geospatial Information Working Group) Requirements
 * 
 * @author osbornb
 * @since 6.6.0
 */
public enum DGIWGRequirement {

	/**
	 * Requirement 1
	 */
	GEOPACKAGE_BASE("GeoPackage Base definition", "geopackage/base"),

	/**
	 * Requirement 2
	 */
	GEOPACKAGE_OPTIONS("GeoPackage Options definition", "geopackage/options"),

	/**
	 * Requirement 3
	 */
	EXTENSIONS_MANDATORY("Mandatory Extensions", "extensions/mandatory"),

	/**
	 * Requirement 4
	 */
	EXTENSIONS_OPTIONAL("Optional Extensions", "extensions/optional"),

	/**
	 * Requirement 5
	 */
	EXTENSIONS_NOT_ALLOWED("Extensions Not Allowed", "extensions/not-allowed"),

	/**
	 * Requirement 6
	 */
	EXTENSIONS_CONDITIONAL("Conditional Extensions", "extensions/conditional"),

	/**
	 * Requirement 7
	 */
	CRS_RASTER_ALLOWED("Raster CRS Allowed", "crs/raster-allowed"),

	/**
	 * Requirement 8
	 */
	CRS_RASTER_TILE_MATRIX_SET("CRS Raster tile matrix set",
			"crs/raster-tile-matrix-set"),

	/**
	 * Requirement 9
	 */
	CRS_2D_VECTOR("Two-Dimensional Vector CRS", "crs/2d-vector"),

	/**
	 * Requirement 10
	 */
	CRS_3D_VECTOR("Three-Dimensional Vector CRS", "crs/3d-vector"),

	/**
	 * Requirement 11
	 */
	CRS_WKT("Well Known Text for CRS", "crs/wkt"),

	/**
	 * Requirement 12
	 */
	CRS_COMPOUND("Compound CRS Usage", "crs/compound"),

	/**
	 * Requirement 13
	 */
	CRS_COMPOUND_WKT("Compound CRS Well Known Text", "crs/compound-wkt"),

	/**
	 * Requirement 14
	 */
	METADATA_DMF("GeoPackage Metadata DMF", "metadata/dmf"),

	/**
	 * Requirement 15
	 */
	METADATA_GPKG("GeoPackage Metadata document", "metadata/gpkg"),

	/**
	 * Requirement 16
	 */
	METADATA_ROW("Complete Row GeoPackage Metadata", "metadata/row"),

	/**
	 * Requirement 17
	 */
	METADATA_USER("User Row GeoPackage Metadata", "metadata/user"),

	/**
	 * Requirement 18
	 */
	METADATA_PRODUCT("GeoPackage Product Metadata", "metadata/product"),

	/**
	 * Requirement 19
	 */
	METADATA_PRODUCT_PARTIAL("GeoPackage Product Partial Metadata",
			"metadata/product-partial"),

	/**
	 * Requirement 20
	 */
	VALIDITY_DATA_VALIDITY("GeoPackage Data Validity",
			"validity/data-validity"),

	/**
	 * Requirement 21
	 */
	TILE_SIZE_MATRIX("Tile Matrix Width Height", "tile/size-matrix"),

	/**
	 * Requirement 22
	 */
	TILE_SIZE_DATA("Tile Pyramid Width Height", "tile/size-data"),

	/**
	 * Requirement 23
	 */
	ZOOM_FACTOR("Zoom level factor", "zoom/factor"),

	/**
	 * Requirement 24
	 */
	ZOOM_MATRIX_SETS_MULTIPLE("Tile Matrix Set with Multiple Zoom Levels",
			"zoom/matrix-sets-multiple"),

	/**
	 * Requirement 25
	 */
	ZOOM_MATRIX_SETS_ONE("Tile Matrix Set with one Zoom Level",
			"zoom/matrix-sets-one"),

	/**
	 * Requirement 26
	 */
	BBOX_CRS("Tile Matrix Set CRS Bounding box", "bbox/crs"),

	/**
	 * Requirement 27
	 */
	METADATA_TILE("Tile layer Metadata", "metadata/tile"),

	/**
	 * Requirement 28
	 */
	METADATA_FEATURE("Feature layer Metadata", "metadata/feature");

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
	 * @param name
	 *            name
	 * @param identifier
	 *            identifier
	 */
	private DGIWGRequirement(String name, String identifier) {
		this.number = ordinal() + 1;
		this.name = name;
		this.identifier = identifier;
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
