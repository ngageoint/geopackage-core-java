package mil.nga.geopackage.tiles.reproject;

import mil.nga.geopackage.BoundingBox;
import mil.nga.geopackage.tiles.TileBoundingBoxUtils;
import mil.nga.geopackage.tiles.TileGrid;
import mil.nga.sf.proj.Projection;
import mil.nga.sf.proj.ProjectionConstants;
import mil.nga.sf.proj.ProjectionFactory;

/**
 * Web Mercator XYZ tiling optimizations
 * 
 * @author osbornb
 * @since 4.0.1
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
	public Projection projection() {
		return ProjectionFactory
				.getProjection(ProjectionConstants.EPSG_WEB_MERCATOR);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TileGrid tileGrid() {
		return new TileGrid(0, 0, 0, 0);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BoundingBox boundingBox() {
		return new BoundingBox(
				-ProjectionConstants.WEB_MERCATOR_HALF_WORLD_WIDTH,
				-ProjectionConstants.WEB_MERCATOR_HALF_WORLD_WIDTH,
				ProjectionConstants.WEB_MERCATOR_HALF_WORLD_WIDTH,
				ProjectionConstants.WEB_MERCATOR_HALF_WORLD_WIDTH);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TileGrid tileGrid(BoundingBox boundingBox, long zoom) {
		return TileBoundingBoxUtils.getTileGrid(boundingBox, (int) zoom);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BoundingBox boundingBox(TileGrid tileGrid, long zoom) {
		return TileBoundingBoxUtils.getWebMercatorBoundingBox(tileGrid,
				(int) zoom);
	}

}
