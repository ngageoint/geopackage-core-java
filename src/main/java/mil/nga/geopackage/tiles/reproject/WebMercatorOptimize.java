package mil.nga.geopackage.tiles.reproject;

import mil.nga.geopackage.BoundingBox;
import mil.nga.geopackage.tiles.TileBoundingBoxUtils;
import mil.nga.geopackage.tiles.TileGrid;
import mil.nga.proj.Projection;
import mil.nga.proj.ProjectionConstants;
import mil.nga.proj.ProjectionFactory;

/**
 * Web Mercator XYZ tiling optimizations
 * 
 * @author osbornb
 * @since 5.0.0
 */
public class WebMercatorOptimize extends TileReprojectionOptimize {

	/**
	 * Create with minimal bounds
	 *
	 * @return web mercator optimize
	 */
	public static WebMercatorOptimize create() {
		return new WebMercatorOptimize();
	}

	/**
	 * Create with world bounds
	 *
	 * @return web mercator optimize
	 */
	public static WebMercatorOptimize createWorld() {
		return new WebMercatorOptimize(true);
	}

	/**
	 * Constructor
	 */
	public WebMercatorOptimize() {
		super();
	}

	/**
	 * Constructor
	 *
	 * @param world
	 *            world coordinate bounds
	 */
	public WebMercatorOptimize(boolean world) {
		super(world);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Projection getProjection() {
		return ProjectionFactory
				.getProjection(ProjectionConstants.EPSG_WEB_MERCATOR);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TileGrid getTileGrid() {
		return new TileGrid(0, 0, 0, 0);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BoundingBox getBoundingBox() {
		return BoundingBox.worldWebMercator();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TileGrid getTileGrid(BoundingBox boundingBox, long zoom) {
		return TileBoundingBoxUtils.getTileGrid(boundingBox, (int) zoom);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BoundingBox getBoundingBox(TileGrid tileGrid, long zoom) {
		return TileBoundingBoxUtils.getWebMercatorBoundingBox(tileGrid,
				(int) zoom);
	}

}
