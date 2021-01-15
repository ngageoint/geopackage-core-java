package mil.nga.geopackage.tiles.reproject;

/**
 * Optional Tile Reprojection configuration for a zoom level
 * 
 * @author osbornb
 * @since 4.0.1
 */
public class TileReprojectionZoom {

	/**
	 * Zoom level
	 */
	private long zoom;

	/**
	 * Reprojected new zoom level
	 */
	private Long toZoom;

	/**
	 * Number of columns at the zoom level
	 */
	private Long matrixWidth;

	/**
	 * Number of rows at the zoom level
	 */
	private Long matrixHeight;

	/**
	 * Tile width in pixels
	 */
	private Long tileWidth;

	/**
	 * Tile height in pixels
	 */
	private Long tileHeight;

	/**
	 * Constructor
	 * 
	 * @param zoom
	 *            zoom level
	 */
	public TileReprojectionZoom(long zoom) {
		this.zoom = zoom;
	}

	/**
	 * Get the zoom level
	 * 
	 * @return zoom level
	 */
	public long getZoom() {
		return zoom;
	}

	/**
	 * Get the reprojected new zoom level
	 * 
	 * @return to zoom
	 */
	public Long getToZoom() {
		return toZoom;
	}

	/**
	 * Has to zoom level value
	 *
	 * @return true if has value
	 */
	public boolean hasToZoom() {
		return toZoom != null;
	}

	/**
	 * Set the reprojected new zoom level
	 * 
	 * @param toZoom
	 *            to zoom
	 */
	public void setToZoom(Long toZoom) {
		this.toZoom = toZoom;
	}

	/**
	 * Get the matrix width
	 * 
	 * @return matrix width
	 */
	public Long getMatrixWidth() {
		return matrixWidth;
	}

	/**
	 * Has matrix width value
	 *
	 * @return true if has value
	 */
	public boolean hasMatrixWidth() {
		return matrixWidth != null;
	}

	/**
	 * Set the matrix width
	 * 
	 * @param matrixWidth
	 *            matrix width
	 */
	public void setMatrixWidth(Long matrixWidth) {
		this.matrixWidth = matrixWidth;
	}

	/**
	 * Get the matrix height
	 * 
	 * @return matrix height
	 */
	public Long getMatrixHeight() {
		return matrixHeight;
	}

	/**
	 * Has matrix height value
	 *
	 * @return true if has value
	 */
	public boolean hasMatrixHeight() {
		return matrixHeight != null;
	}

	/**
	 * Set the matrix height
	 * 
	 * @param matrixHeight
	 *            matrix height
	 */
	public void setMatrixHeight(Long matrixHeight) {
		this.matrixHeight = matrixHeight;
	}

	/**
	 * Get the tile width
	 * 
	 * @return tile width
	 */
	public Long getTileWidth() {
		return tileWidth;
	}

	/**
	 * Has tile width value
	 *
	 * @return true if has value
	 */
	public boolean hasTileWidth() {
		return tileWidth != null;
	}

	/**
	 * Set the tile width
	 * 
	 * @param tileWidth
	 *            tile width
	 */
	public void setTileWidth(Long tileWidth) {
		this.tileWidth = tileWidth;
	}

	/**
	 * Get the tile height
	 * 
	 * @return tile height
	 */
	public Long getTileHeight() {
		return tileHeight;
	}

	/**
	 * Has tile height value
	 *
	 * @return true if has value
	 */
	public boolean hasTileHeight() {
		return tileHeight != null;
	}

	/**
	 * Set the tile height
	 * 
	 * @param tileHeight
	 *            tile height
	 */
	public void setTileHeight(Long tileHeight) {
		this.tileHeight = tileHeight;
	}

}
