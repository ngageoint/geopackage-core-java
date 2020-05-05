package mil.nga.geopackage.extension.nga.scale;

/**
 * Tile Scaling Type enumeration for defining scaled tile searching of nearby
 * zoom levels in place of missing tiles
 * 
 * @author osbornb
 * @since 2.0.2
 */
public enum TileScalingType {

	/**
	 * Search for tiles by zooming in
	 */
	IN("in"),

	/**
	 * Search for tiles by zooming out
	 */
	OUT("out"),

	/**
	 * Search for tiles by zooming in first, and then zooming out
	 */
	IN_OUT("in_out"),

	/**
	 * Search for tiles by zooming out first, and then zooming in
	 */
	OUT_IN("out_in"),

	/**
	 * Search for tiles in closest zoom level order, zoom in levels before zoom
	 * out
	 */
	CLOSEST_IN_OUT("closest_in_out"),

	/**
	 * Search for tiles in closest zoom level order, zoom out levels before zoom
	 * in
	 */
	CLOSEST_OUT_IN("closest_out_in");

	/**
	 * Tile Scaling type name
	 */
	private final String name;

	/**
	 * Constructor
	 * 
	 * @param name
	 *            tile scaling name
	 */
	private TileScalingType(String name) {
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
	 * Get the Tile Scaling Type from the name
	 * 
	 * @param name
	 *            name
	 * @return tile scaling type
	 */
	public static TileScalingType fromName(String name) {
		TileScalingType scalingType = null;
		if (name != null) {
			for (TileScalingType type : TileScalingType.values()) {
				if (name.equals(type.getName())) {
					scalingType = type;
					break;
				}
			}
		}
		return scalingType;
	}

}
