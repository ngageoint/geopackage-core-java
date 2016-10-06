package mil.nga.geopackage.extension.elevation;

import mil.nga.geopackage.tiles.matrix.TileMatrix;

/**
 * Elevation tile results containing the elevations from a requested area from a
 * tile matrix zoom level
 * 
 * @author osbornb
 * @since 1.2.1
 */
public class ElevationTileResults {

	/**
	 * Double array of elevations stored as [row][column]
	 */
	private final Double[][] elevations;

	/**
	 * Tile matrix used to find the elevations
	 */
	private final TileMatrix tileMatrix;

	/**
	 * Elevation results height
	 */
	private int height;

	/**
	 * Elevation results width
	 */
	private int width;

	/**
	 * Constructor
	 * 
	 * @param elevations
	 *            elevation results
	 * @param tileMatrix
	 *            tile matrix
	 */
	public ElevationTileResults(Double[][] elevations, TileMatrix tileMatrix) {
		this.elevations = elevations;
		this.tileMatrix = tileMatrix;
		height = elevations.length;
		width = elevations[0].length;
	}

	/**
	 * Get the double array of elevations stored as [row][column]
	 * 
	 * @return elevations
	 */
	public Double[][] getElevations() {
		return elevations;
	}

	/**
	 * Get the tile matrix used to find the elevations
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
	 * Get the elevation at the row and column
	 * 
	 * @param row
	 *            row index
	 * @param column
	 *            column index
	 * @return elevation
	 */
	public Double getElevation(int row, int column) {
		return elevations[row][column];
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
