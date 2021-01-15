package mil.nga.geopackage.tiles.reproject;

import mil.nga.geopackage.BoundingBox;
import mil.nga.geopackage.tiles.TileBoundingBoxUtils;
import mil.nga.geopackage.tiles.TileGrid;
import mil.nga.sf.proj.Projection;
import mil.nga.sf.proj.ProjectionConstants;
import mil.nga.sf.proj.ProjectionFactory;

/**
 * Platte Carre (WGS84) XYZ tiling optimizations
 * 
 * @author osbornb
 * @since 4.0.1
 */
public class PlatteCarreOptimize extends TileReprojectionOptimize {

	/**
	 * Create with minimal bounds
	 *
	 * @return platte carre optimize
	 */
	public static PlatteCarreOptimize create() {
		return new PlatteCarreOptimize();
	}

	/**
	 * Create with world bounds
	 *
	 * @return platte carre optimize
	 */
	public static PlatteCarreOptimize createWorld() {
		return new PlatteCarreOptimize(true);
	}

	/**
	 * Constructor
	 */
	public PlatteCarreOptimize() {
		super();
	}

	/**
	 * Constructor
	 *
	 * @param world
	 *            world coordinate bounds
	 */
	public PlatteCarreOptimize(boolean world) {
		super(world);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Projection projection() {
		return ProjectionFactory
				.getProjection(ProjectionConstants.EPSG_WORLD_GEODETIC_SYSTEM);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TileGrid tileGrid() {
		return new TileGrid(0, 0, 1, 0);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BoundingBox boundingBox() {
		return new BoundingBox();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TileGrid tileGrid(BoundingBox boundingBox, int zoom) {
		return TileBoundingBoxUtils.getTileGridWGS84(boundingBox, zoom);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BoundingBox boundingBox(TileGrid tileGrid, int zoom) {
		return TileBoundingBoxUtils.getWGS84BoundingBox(tileGrid, zoom);
	}

}
