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
public abstract class TileReprojectionCore {

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
	private long optimizeZoom;

	/**
	 * Default Constructor
	 */
	protected TileReprojectionCore() {

	}

	/**
	 * Constructor
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * @param table
	 *            table name
	 * @param projection
	 *            projection
	 */
	protected TileReprojectionCore(GeoPackageCore geoPackage, String table,
			Projection projection) {
		this.geoPackage = geoPackage;
		this.table = table;
		this.projection = projection;
	}

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

	/**
	 * Get the zoom level configuration for a zoom level
	 *
	 * @param zoom
	 *            from zoom level
	 * @return zoom config
	 */
	public TileReprojectionZoom getConfig(long zoom) {
		return zoomConfigs.get(zoom);
	}

	/**
	 * Get the zoom level configuration or create new configuration for a zoom
	 * level
	 *
	 * @param zoom
	 *            from zoom level
	 * @return zoom config
	 */
	public TileReprojectionZoom getConfigOrCreate(long zoom) {
		TileReprojectionZoom config = getConfig(zoom);
		if (config == null) {
			config = new TileReprojectionZoom(zoom);
			setConfig(config);
		}
		return config;
	}

	/**
	 * Set a zoom level configuration for a zoom level
	 *
	 * @param config
	 *            zoom configuration
	 */
	public void setConfig(TileReprojectionZoom config) {
		zoomConfigs.put(config.getZoom(), config);
	}

	/**
	 * Set a reprojected to zoom level for a zoom level
	 * 
	 * @param zoom
	 *            zoom level
	 * @param toZoom
	 *            reprojected zoom level
	 */
	public void setToZoom(long zoom, long toZoom) {
		getConfigOrCreate(zoom).setToZoom(toZoom);
	}

	/**
	 * Get a reprojected to zoom level from a zoom level, defaults as the zoom
	 * level if not set
	 *
	 * @param zoom
	 *            zoom level
	 * @return reprojected to zoom level
	 */
	public long getToZoom(long zoom) {
		long toZoom = zoom;
		TileReprojectionZoom config = getConfig(zoom);
		if (config != null && config.hasToZoom()) {
			toZoom = config.getToZoom();
		}
		return toZoom;
	}

	/**
	 * Set a reprojected tile width for a zoom level
	 *
	 * @param tileWidth
	 *            reprojected tile width
	 * @param zoom
	 *            zoom level
	 */
	public void setTileWidth(long zoom, long tileWidth) {
		getConfigOrCreate(zoom).setTileWidth(tileWidth);
	}

	/**
	 * Get a reprojected tile width from a zoom level
	 *
	 * @param zoom
	 *            zoom level
	 * @return reprojected tile width
	 */
	public Long getTileWidth(long zoom) {
		Long tileWidth = this.tileWidth;
		TileReprojectionZoom config = getConfig(zoom);
		if (config != null && config.hasTileWidth()) {
			tileWidth = config.getTileWidth();
		}
		return tileWidth;
	}

	/**
	 * Set a reprojected tile height for a zoom level
	 * 
	 * @param zoom
	 *            zoom level
	 * @param tileHeight
	 *            reprojected tile height
	 */
	public void setTileHeight(long zoom, long tileHeight) {
		getConfigOrCreate(zoom).setTileHeight(tileHeight);
	}

	/**
	 * Get a reprojected tile height from a zoom level
	 *
	 * @param zoom
	 *            zoom level
	 * @return reprojected tile height
	 */
	public Long getTileHeight(long zoom) {
		Long tileHeight = this.tileHeight;
		TileReprojectionZoom config = getConfig(zoom);
		if (config != null && config.hasTileHeight()) {
			tileHeight = config.getTileHeight();
		}
		return tileHeight;
	}

	/**
	 * Set a reprojected matrix width for a zoom level
	 *
	 * @param matrixWidth
	 *            reprojected matrix width
	 * @param zoom
	 *            zoom level
	 */
	public void setMatrixWidth(long zoom, long matrixWidth) {
		getConfigOrCreate(zoom).setMatrixWidth(matrixWidth);
	}

	/**
	 * Get a reprojected matrix width from a zoom level
	 *
	 * @param zoom
	 *            zoom level
	 * @return reprojected matrix width
	 */
	public Long getMatrixWidth(long zoom) {
		Long matrixWidth = null;
		TileReprojectionZoom config = getConfig(zoom);
		if (config != null && config.hasMatrixWidth()) {
			matrixWidth = config.getMatrixWidth();
		}
		return matrixWidth;
	}

	/**
	 * Set a reprojected matrix height for a zoom level
	 *
	 * @param matrixHeight
	 *            reprojected matrix height
	 * @param zoom
	 *            zoom level
	 */
	public void setMatrixHeight(long zoom, long matrixHeight) {
		getConfigOrCreate(zoom).setMatrixHeight(matrixHeight);
	}

	/**
	 * Get a reprojected matrix height from a zoom level
	 *
	 * @param zoom
	 *            zoom level
	 * @return reprojected matrix height
	 */
	public Long getMatrixHeight(long zoom) {
		Long matrixHeight = null;
		TileReprojectionZoom config = getConfig(zoom);
		if (config != null && config.hasMatrixHeight()) {
			matrixHeight = config.getMatrixHeight();
		}
		return matrixHeight;
	}

	/**
	 * Initialize the reprojection
	 */
	protected void initialize() {

	}

	/**
	 * Finish the reprojection
	 */
	protected void finish() {

	}

}
