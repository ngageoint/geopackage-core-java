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
	 * @param heights
	 * @param tileMatrices
	 * @param length
	 *            in meters
	 * @return
	 */
	public static Long getZoomLevel(double[] widths, double[] heights,
			List<TileMatrix> tileMatrices, double length) {

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
			if (length < widths[widthIndex] * .51) {
				widthIndex = -1;
			}
		} else if (widthIndex == widths.length) {
			if (length >= widths[widthIndex - 1] / .51) {
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
			if (length < heights[heightIndex] * .51) {
				heightIndex = -1;
			}
		} else if (heightIndex == heights.length) {
			if (length >= heights[heightIndex - 1] / .51) {
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

}
