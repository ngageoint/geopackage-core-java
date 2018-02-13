package mil.nga.geopackage.extension.coverage;

/**
 * Tile Gridded Coverage Data Algorithm interpolation enumeration types
 * 
 * @author osbornb
 * @since 2.0.1
 */
public enum CoverageDataAlgorithm {

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
