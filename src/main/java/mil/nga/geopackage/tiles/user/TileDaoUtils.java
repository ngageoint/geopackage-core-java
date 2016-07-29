package mil.nga.geopackage.tiles.user;

import java.util.Arrays;
import java.util.List;

import mil.nga.geopackage.tiles.matrix.TileMatrix;
import mil.nga.geopackage.tiles.matrixset.TileMatrixSet;

/**
 * Tile Data Access Object utilities
 * 
 * @author osbornb
 */
public class TileDaoUtils {

	/**
	 * Adjust the tile matrix lengths if needed. Check if the tile matrix width
	 * and height need to expand to account for pixel * number of pixels fitting
	 * into the tile matrix lengths
	 * 
	 * @param tileMatrixSet
	 * @param tileMatrices
	 */
	public static void adjustTileMatrixLengths(TileMatrixSet tileMatrixSet,
			List<TileMatrix> tileMatrices) {
		double tileMatrixWidth = tileMatrixSet.getMaxX()
				- tileMatrixSet.getMinX();
		double tileMatrixHeight = tileMatrixSet.getMaxY()
				- tileMatrixSet.getMinY();
		for (TileMatrix tileMatrix : tileMatrices) {
			int tempMatrixWidth = (int) (tileMatrixWidth / (tileMatrix
					.getPixelXSize() * tileMatrix.getTileWidth()));
			int tempMatrixHeight = (int) (tileMatrixHeight / (tileMatrix
					.getPixelYSize() * tileMatrix.getTileHeight()));
			if (tempMatrixWidth > tileMatrix.getMatrixWidth()) {
				tileMatrix.setMatrixWidth(tempMatrixWidth);
			}
			if (tempMatrixHeight > tileMatrix.getMatrixHeight()) {
				tileMatrix.setMatrixHeight(tempMatrixHeight);
			}
		}
	}

	/**
	 * Get the zoom level for the provided width and height in the default units
	 * 
	 * @param widths
	 *            sorted widths
	 * @param heights
	 *            sorted heights
	 * @param tileMatrices
	 *            tile matrices
	 * @param length
	 *            in default units
	 * @return tile matrix zoom level
	 */
	public static Long getZoomLevel(double[] widths, double[] heights,
			List<TileMatrix> tileMatrices, double length) {
		return getZoomLevel(widths, heights, tileMatrices, length, true);
	}

	/**
	 * Get the closest zoom level for the provided width and height in the
	 * default units
	 * 
	 * @param widths
	 *            sorted widths
	 * @param heights
	 *            sorted heights
	 * @param tileMatrices
	 *            tile matrices
	 * @param length
	 *            in default units
	 * @return tile matrix zoom level
	 * @since 1.2.1
	 */
	public static Long getClosestZoomLevel(double[] widths, double[] heights,
			List<TileMatrix> tileMatrices, double length) {
		return getZoomLevel(widths, heights, tileMatrices, length, false);
	}

	/**
	 * Get the zoom level for the provided width and height in the default units
	 * 
	 * @param widths
	 *            sorted widths
	 * @param heights
	 *            sorted heights
	 * @param tileMatrices
	 *            tile matrices
	 * @param length
	 *            in default units
	 * @param lengthChecks
	 *            perform length checks for values too far away from the zoom
	 *            level
	 * @return tile matrix zoom level
	 */
	private static Long getZoomLevel(double[] widths, double[] heights,
			List<TileMatrix> tileMatrices, double length, boolean lengthChecks) {

		Long zoomLevel = null;

		// Find where the width and height fit in
		int widthIndex = Arrays.binarySearch(widths, length);
		if (widthIndex < 0) {
			widthIndex = (widthIndex + 1) * -1;
		}
		int heightIndex = Arrays.binarySearch(heights, length);
		if (heightIndex < 0) {
			heightIndex = (heightIndex + 1) * -1;
		}

		// Find the closest width or verify it isn't too small or large
		if (widthIndex == 0) {
			if (lengthChecks && length < getMinLength(widths)) {
				widthIndex = -1;
			}
		} else if (widthIndex == widths.length) {
			if (lengthChecks && length >= getMaxLength(widths)) {
				widthIndex = -1;
			} else {
				widthIndex = widthIndex - 1;
			}
		} else if (length - widths[widthIndex - 1] < widths[widthIndex]
				- length) {
			widthIndex--;
		}

		// Find the closest height or verify it isn't too small or large
		if (heightIndex == 0) {
			if (lengthChecks && length < getMinLength(heights)) {
				heightIndex = -1;
			}
		} else if (heightIndex == heights.length) {
			if (lengthChecks && length >= getMaxLength(heights)) {
				heightIndex = -1;
			} else {
				heightIndex = heightIndex - 1;
			}
		} else if (length - heights[heightIndex - 1] < heights[heightIndex]
				- length) {
			heightIndex--;
		}

		if (widthIndex >= 0 && heightIndex >= 0) {

			// Use one zoom size smaller if possible
			int index = Math.min(widthIndex, heightIndex);
			if (index >= 0) {

				TileMatrix tileMatrix = tileMatrices.get(tileMatrices.size()
						- index - 1);
				zoomLevel = tileMatrix.getZoomLevel();
			}
		}

		return zoomLevel;
	}

	/**
	 * Get the max distance length that matches the tile widths and heights
	 * 
	 * @param widths
	 *            sorted tile matrix widths
	 * @param heights
	 *            sorted tile matrix heights
	 * @return max length
	 * @since 1.2.0
	 */
	public static double getMaxLength(double[] widths, double[] heights) {
		double maxWidth = getMaxLength(widths);
		double maxHeight = getMaxLength(heights);
		double maxLength = Math.min(maxWidth, maxHeight);
		return maxLength;
	}

	/**
	 * Get the min distance length that matches the tile widths and heights
	 * 
	 * @param widths
	 *            sorted tile matrix widths
	 * @param heights
	 *            sorted tile matrix heights
	 * @return min length
	 * @since 1.2.0
	 */
	public static double getMinLength(double[] widths, double[] heights) {
		double minWidth = getMinLength(widths);
		double minHeight = getMinLength(heights);
		double minLength = Math.max(minWidth, minHeight);
		return minLength;
	}

	/**
	 * Get the max length distance value from the sorted array of lengths
	 * 
	 * @param lengths
	 *            sorted tile matrix lengths
	 * @return max length
	 */
	private static double getMaxLength(double[] lengths) {
		return lengths[lengths.length - 1] / .51;
	}

	/**
	 * Get the min length distance value from the sorted array of lengths
	 * 
	 * @param lengths
	 *            sorted tile matrix lengths
	 * @return min length
	 */
	private static double getMinLength(double[] lengths) {
		return lengths[0] * .51;
	}

}
