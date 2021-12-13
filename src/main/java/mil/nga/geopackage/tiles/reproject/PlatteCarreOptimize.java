package mil.nga.geopackage.tiles.reproject;

import mil.nga.geopackage.BoundingBox;
import mil.nga.geopackage.tiles.TileBoundingBoxUtils;
import mil.nga.geopackage.tiles.TileGrid;
import mil.nga.proj.Projection;
import mil.nga.proj.ProjectionConstants;
import mil.nga.proj.ProjectionFactory;

/**
 * Platte Carre (WGS84) XYZ tiling optimizations
 * 
 * @author osbornb
 * @since 5.0.0
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
	public Projection getProjection() {
		return ProjectionFactory
				.getProjection(ProjectionConstants.EPSG_WORLD_GEODETIC_SYSTEM);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TileGrid getTileGrid() {
		return new TileGrid(0, 0, 1, 0);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BoundingBox getBoundingBox() {
		return BoundingBox.worldWGS84();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TileGrid getTileGrid(BoundingBox boundingBox, long zoom) {
		return TileBoundingBoxUtils.getTileGridWGS84(boundingBox, (int) zoom);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BoundingBox getBoundingBox(TileGrid tileGrid, long zoom) {
		return TileBoundingBoxUtils.getWGS84BoundingBox(tileGrid, (int) zoom);
	}

}
