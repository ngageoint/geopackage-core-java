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
	 * Get the zoom level for the provided width and height in the default units
	 * 
	 * @param widths
	 *            sorted widths
	 * @param heights
	 *            sorted heights
	 * @param tileMatrices
	 *            tile matrices
	 * @param width
	 *            in default units
	 * @param height
	 *            in default units
	 * @return tile matrix zoom level
	 * @since 1.2.1
	 */
	public static Long getZoomLevel(double[] widths, double[] heights,
			List<TileMatrix> tileMatrices, double width, double height) {
		return getZoomLevel(widths, heights, tileMatrices, width, height, true);
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
	 * Get the closest zoom level for the provided width and height in the
	 * default units
	 * 
	 * @param widths
	 *            sorted widths
	 * @param heights
	 *            sorted heights
	 * @param tileMatrices
	 *            tile matrices
	 * @param width
	 *            in default units
	 * @param height
	 *            in default units
	 * @return tile matrix zoom level
	 * @since 1.2.1
	 */
	public static Long getClosestZoomLevel(double[] widths, double[] heights,
			List<TileMatrix> tileMatrices, double width, double height) {
		return getZoomLevel(widths, heights, tileMatrices, width, height, false);
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
		return getZoomLevel(widths, heights, tileMatrices, length, length,
				lengthChecks);
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
	 * @param width
	 *            width in default units
	 * @param height
	 *            height in default units
	 * @param lengthChecks
	 *            perform length checks for values too far away from the zoom
	 *            level
	 * @return tile matrix zoom level
	 * @since 1.2.1
	 */
	private static Long getZoomLevel(double[] widths, double[] heights,
			List<TileMatrix> tileMatrices, double width, double height,
			boolean lengthChecks) {

		Long zoomLevel = null;

		// Find where the width and height fit in
		int widthIndex = Arrays.binarySearch(widths, width);
		if (widthIndex < 0) {
			widthIndex = (widthIndex + 1) * -1;
		}
		int heightIndex = Arrays.binarySearch(heights, height);
		if (heightIndex < 0) {
			heightIndex = (heightIndex + 1) * -1;
		}

		// Find the closest width or verify it isn't too small or large
		if (widthIndex == 0) {
			if (lengthChecks && width < getMinLength(widths)) {
				widthIndex = -1;
			}
		} else if (widthIndex == widths.length) {
			if (lengthChecks && width >= getMaxLength(widths)) {
				widthIndex = -1;
			} else {
				widthIndex = widthIndex - 1;
			}
		} else if (closerToZoomIn(widths, width, widthIndex)) {
			widthIndex--;
		}

		// Find the closest height or verify it isn't too small or large
		if (heightIndex == 0) {
			if (lengthChecks && height < getMinLength(heights)) {
				heightIndex = -1;
			}
		} else if (heightIndex == heights.length) {
			if (lengthChecks && height >= getMaxLength(heights)) {
				heightIndex = -1;
			} else {
				heightIndex = heightIndex - 1;
			}
		} else if (closerToZoomIn(heights, height, heightIndex)) {
			heightIndex--;
		}

		if (widthIndex >= 0 || heightIndex >= 0) {

			// Use one zoom size smaller if possible
			int index;
			if (widthIndex < 0) {
				index = heightIndex;
			} else if (heightIndex < 0) {
				index = widthIndex;
			} else {
				index = Math.min(widthIndex, heightIndex);
			}

			TileMatrix tileMatrix = getTileMatrixAtLengthIndex(tileMatrices,
					index);
			zoomLevel = tileMatrix.getZoomLevel();
		}

		return zoomLevel;
	}

	/**
	 * Determine if the length at the index is closer by a factor of two to the
	 * next zoomed in level / lower index
	 * 
	 * @param lengths
	 *            sorted lengths
	 * @param length
	 *            current length
	 * @param lengthIndex
	 *            length index
	 * @return true if closer to zoomed in length
	 */
	private static boolean closerToZoomIn(double[] lengths, double length,
			int lengthIndex) {

		// Zoom level distance to the zoomed in length
		double zoomInDistance = Math.log(length / lengths[lengthIndex - 1])
				/ Math.log(2);

		// Zoom level distance to the zoomed out length
		double zoomOutDistance = Math.log(length / lengths[lengthIndex])
				/ Math.log(.5);

		boolean zoomIn = zoomInDistance < zoomOutDistance;

		return zoomIn;
	}

	/**
	 * Get the tile matrix represented by the current length index
	 * 
	 * @param tileMatrices
	 *            tile matrices
	 * @param index
	 *            index location in sorted lengths
	 * @return tile matrix
	 */
	private static TileMatrix getTileMatrixAtLengthIndex(
			List<TileMatrix> tileMatrices, int index) {
		return tileMatrices.get(tileMatrices.size() - index - 1);
	}

	/**
	 * Get the approximate zoom level for the provided length in the default
	 * units. Tiles may or may not exist for the returned zoom level. The
	 * approximate zoom level is determined using a factor of 2 from the zoom
	 * levels with tiles.
	 * 
	 * @param widths
	 *            sorted widths
	 * @param heights
	 *            sorted heights
	 * @param tileMatrices
	 *            tile matrices
	 * @param length
	 *            length in default units
	 * @return actual or approximate tile matrix zoom level
	 * @since 2.0.2
	 */
	public static Long getApproximateZoomLevel(double[] widths,
			double[] heights, List<TileMatrix> tileMatrices, double length) {
		return getApproximateZoomLevel(widths, heights, tileMatrices, length,
				length);
	}

	/**
	 * Get the approximate zoom level for the provided width and height in the
	 * default units. Tiles may or may not exist for the returned zoom level.
	 * The approximate zoom level is determined using a factor of 2 from the
	 * zoom levels with tiles.
	 * 
	 * @param widths
	 *            sorted widths
	 * @param heights
	 *            sorted heights
	 * @param tileMatrices
	 *            tile matrices
	 * @param width
	 *            width in default units
	 * @param height
	 *            height in default units
	 * @return actual or approximate tile matrix zoom level
	 * @since 2.0.2
	 */
	public static Long getApproximateZoomLevel(double[] widths,
			double[] heights, List<TileMatrix> tileMatrices, double width,
			double height) {

		Long widthZoomLevel = getApproximateZoomLevel(widths, tileMatrices,
				width);
		Long heightZoomLevel = getApproximateZoomLevel(heights, tileMatrices,
				height);

		Long expectedZoomLevel;
		if (widthZoomLevel == null) {
			expectedZoomLevel = heightZoomLevel;
		} else if (heightZoomLevel == null) {
			expectedZoomLevel = widthZoomLevel;
		} else {
			expectedZoomLevel = Math.max(widthZoomLevel, heightZoomLevel);
		}

		return expectedZoomLevel;
	}

	/**
	 * Get the approximate zoom level for length using the factor of 2 rule
	 * between zoom levels
	 * 
	 * @param lengths
	 *            sorted lengths
	 * @param tileMatrices
	 *            tile matrices
	 * @param length
	 *            length in default units
	 * @return approximate zoom level
	 */
	private static Long getApproximateZoomLevel(double[] lengths,
			List<TileMatrix> tileMatrices, double length) {

		Long lengthZoomLevel = null;

		double minLength = lengths[0];
		double maxLength = lengths[lengths.length - 1];

		// Length is zoomed in further than available tiles
		if (length < minLength) {
			double levelsIn = Math.log(length / minLength) / Math.log(.5);
			long zoomAbove = (long) Math.floor(levelsIn);
			long zoomBelow = (long) Math.ceil(levelsIn);
			double lengthAbove = minLength * Math.pow(.5, zoomAbove);
			double lengthBelow = minLength * Math.pow(.5, zoomBelow);
			lengthZoomLevel = tileMatrices.get(tileMatrices.size() - 1)
					.getZoomLevel();
			if (lengthAbove - length <= length - lengthBelow) {
				lengthZoomLevel += zoomAbove;
			} else {
				lengthZoomLevel += zoomBelow;
			}
		}
		// Length is zoomed out further than available tiles
		else if (length > maxLength) {
			double levelsOut = Math.log(length / maxLength) / Math.log(2);
			long zoomAbove = (long) Math.ceil(levelsOut);
			long zoomBelow = (long) Math.floor(levelsOut);
			double lengthAbove = maxLength * Math.pow(2, zoomAbove);
			double lengthBelow = maxLength * Math.pow(2, zoomBelow);
			lengthZoomLevel = tileMatrices.get(0).getZoomLevel();
			if (length - lengthBelow <= lengthAbove - length) {
				lengthZoomLevel -= zoomBelow;
			} else {
				lengthZoomLevel -= zoomAbove;
			}
		}
		// Length is between the available tiles
		else {
			int lengthIndex = Arrays.binarySearch(lengths, length);
			if (lengthIndex < 0) {
				lengthIndex = (lengthIndex + 1) * -1;
			}
			double zoomDistance = Math.log(length / lengths[lengthIndex])
					/ Math.log(.5);
			long zoomLevelAbove = getTileMatrixAtLengthIndex(tileMatrices,
					lengthIndex).getZoomLevel();
			zoomLevelAbove += Math.round(zoomDistance);
			lengthZoomLevel = zoomLevelAbove;
		}

		return lengthZoomLevel;
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
