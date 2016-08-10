package mil.nga.geopackage.extension.elevation;

/**
 * Elevation Tiles Algorithm interpolation enumeration types
 * 
 * @author osbornb
 * @since 1.2.1
 */
public enum ElevationTilesAlgorithm {

	/**
	 * Selects the value of the nearest point and does not consider the values
	 * of neighboring points at all
	 */
	NEAREST_NEIGHBOR,

	/**
	 * Performs linear interpolation first in one direction, and then again in
	 * the other direction
	 */
	BILINEAR,

	/**
	 * Considers 16 pixels to interpolate each value
	 */
	BICUBIC;

}
