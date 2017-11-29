package mil.nga.geopackage.extension.coverage;

import mil.nga.geopackage.tiles.matrix.TileMatrix;

/**
 * Tiled Gridded Coverage Data results containing the coverage values from a
 * requested area from a tile matrix zoom level
 * 
 * @author osbornb
 * @since 2.0.1
 */
public class CoverageDataResults {

	/**
	 * Double array of coverage data values stored as [row][column]
	 */
	private final Double[][] values;

	/**
	 * Tile matrix used to find the coverage data values
	 */
	private final TileMatrix tileMatrix;

	/**
	 * Coverage data results height
	 */
	private int height;

	/**
	 * Coverage data results width
	 */
	private int width;

	/**
	 * Constructor
	 * 
	 * @param values
	 *            coverage data values
	 * @param tileMatrix
	 *            tile matrix
	 */
	public CoverageDataResults(Double[][] values, TileMatrix tileMatrix) {
		this.values = values;
		this.tileMatrix = tileMatrix;
		height = values.length;
		width = values[0].length;
	}

	/**
	 * Get the double array of coverage data values stored as [row][column]
	 * 
	 * @return coverage data values
	 */
	public Double[][] getValues() {
		return values;
	}

	/**
	 * Get the tile matrix used to find the coverage data values
	 * 
	 * @return tile matrix
	 */
	public TileMatrix getTileMatrix() {
		return tileMatrix;
	}

	/**
	 * Get the results height
	 * 
	 * @return height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Get the results width
	 * 
	 * @return width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Get the coverage data value at the row and column
	 * 
	 * @param row
	 *            row index
	 * @param column
	 *            column index
	 * @return coverage data value
	 */
	public Double getValue(int row, int column) {
		return values[row][column];
	}

	/**
	 * Get the zoom level of the results
	 * 
	 * @return zoom level
	 */
	public long getZoomLevel() {
		return tileMatrix.getZoomLevel();
	}

}
