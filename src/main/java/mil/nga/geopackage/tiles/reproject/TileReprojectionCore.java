package mil.nga.geopackage.tiles.reproject;

import java.util.HashMap;
import java.util.Map;

import mil.nga.geopackage.GeoPackageCore;
import mil.nga.geopackage.io.GeoPackageProgress;
import mil.nga.geopackage.tiles.TileGrid;
import mil.nga.sf.proj.Projection;

/**
 * Tile Reprojection for reprojecting an existing tile table
 * 
 * @author osbornb
 * @since 4.0.1
 */
public class TileReprojectionCore {

	/**
	 * Optional optimization
	 */
	private TileReprojectionOptimize optimize;

	/**
	 * Overwrite existing tiles at a zoom level when geographic calculations
	 * differ
	 */
	private boolean overwrite = false;

	/**
	 * Tile width in pixels
	 */
	private Long tileWidth;

	/**
	 * Tile height in pixels
	 */
	private Long tileHeight;

	/**
	 * Progress callbacks
	 */
	private GeoPackageProgress progress;

	/**
	 * GeoPackage
	 */
	private GeoPackageCore geoPackage;

	/**
	 * Table name
	 */
	private String table;

	/**
	 * Projection
	 */
	private Projection projection;

	/**
	 * Replace flag
	 */
	private boolean replace = false;

	/**
	 * Zoom level configuration map
	 */
	private Map<Long, TileReprojectionZoom> zoomConfigs = new HashMap<>();

	/**
	 * Optimization tile grid
	 */
	private TileGrid optimizeTileGrid;

	/**
	 * Optimization zoom
	 */
	private int optimizeZoom;

	/**
	 * Get the optimization
	 * 
	 * @return optimization
	 */
	public TileReprojectionOptimize getOptimize() {
		return optimize;
	}

	/**
	 * Set the optimization
	 * 
	 * @param optimize
	 *            optimization
	 */
	public void setOptimize(TileReprojectionOptimize optimize) {
		this.optimize = optimize;
	}

	/**
	 * Is overwrite enabled
	 * 
	 * @return overwrite flag
	 */
	public boolean isOverwrite() {
		return overwrite;
	}

	/**
	 * Set the overwrite flag
	 * 
	 * @param overwrite
	 *            overwrite flag
	 */
	public void setOverwrite(boolean overwrite) {
		this.overwrite = overwrite;
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
	 * Set the tile height
	 * 
	 * @param tileHeight
	 *            tile height
	 */
	public void setTileHeight(Long tileHeight) {
		this.tileHeight = tileHeight;
	}

	/**
	 * Get the progress callbacks
	 * 
	 * @return progress
	 */
	public GeoPackageProgress getProgress() {
		return progress;
	}

	/**
	 * Set the progress callbacks
	 * 
	 * @param progress
	 *            progress callbacks
	 */
	public void setProgress(GeoPackageProgress progress) {
		this.progress = progress;
	}

	/**
	 * Get the zoom level configurations
	 *
	 * @return zoom configs
	 */
	public Map<Long, TileReprojectionZoom> getZoomConfigs() {
		return zoomConfigs;
	}

}
