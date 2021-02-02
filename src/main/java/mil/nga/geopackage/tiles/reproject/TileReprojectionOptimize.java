package mil.nga.geopackage.tiles.reproject;

import mil.nga.geopackage.BoundingBox;
import mil.nga.geopackage.tiles.TileGrid;
import mil.nga.sf.proj.Projection;

/**
 * Tile Reprojection Optimizations
 * 
 * @author osbornb
 * @since 4.0.1
 */
public abstract class TileReprojectionOptimize {

	/**
	 * Create a Web Mercator optimization, minimally tile bounded
	 *
	 * @return tile reprojection optimize
	 */
	public static TileReprojectionOptimize webMercator() {
		return WebMercatorOptimize.create();
	}

	/**
	 * Create a Platte Carre (WGS84) optimization, minimally tile bounded
	 *
	 * @return tile reprojection optimize
	 */
	public static TileReprojectionOptimize platteCarre() {
		return PlatteCarreOptimize.create();
	}

	/**
	 * Create a Web Mercator optimization, world bounded with XYZ tile
	 * coordinates
	 *
	 * @return tile reprojection optimize
	 */
	public static TileReprojectionOptimize webMercatorWorld() {
		return WebMercatorOptimize.createWorld();
	}

	/**
	 * Create a Platte Carre (WGS84) optimization, world bounded with XYZ tile
	 * coordinates
	 *
	 * @return tile reprojection optimize
	 */
	public static TileReprojectionOptimize platteCarreWorld() {
		return PlatteCarreOptimize.createWorld();
	}

	/**
	 * World tile coordinate bounds (XYZ), as opposed to minimal tile fitting
	 * bounds (default)
	 */
	private boolean world;

	/**
	 * Constructor
	 */
	public TileReprojectionOptimize() {
		this(false);
	}

	/**
	 * Constructor
	 *
	 * @param world
	 *            world coordinate bounds
	 */
	public TileReprojectionOptimize(boolean world) {
		this.world = world;
	}

	/**
	 * Is world tile coordinate bounds (XYZ), as opposed to minimal tile fitting
	 * bounds
	 * 
	 * @return world flag
	 */
	public boolean isWorld() {
		return world;
	}

	/**
	 * Set the world tile coordinate bounds (XYZ) vs minimal tile fitting bounds
	 * flag
	 * 
	 * @param world
	 *            world bounds flag
	 */
	public void setWorld(boolean world) {
		this.world = world;
	}

	/**
	 * Get the optimization projection
	 *
	 * @return projection
	 */
	public abstract Projection getProjection();

	/**
	 * Get the world tile grid of the optimization projection
	 *
	 * @return tile grid
	 */
	public abstract TileGrid getTileGrid();

	/**
	 * Get the world bounding box of the optimization projection
	 *
	 * @return bounding box
	 */
	public abstract BoundingBox getBoundingBox();

	/**
	 * Get the tile grid of the bounding box at the zoom
	 *
	 * @param boundingBox
	 *            bounding box
	 * @param zoom
	 *            zoom level
	 *
	 * @return tile grid
	 */
	public abstract TileGrid getTileGrid(BoundingBox boundingBox, long zoom);

	/**
	 * Get the bounding box of the tile grid at the zoom
	 *
	 * @param tileGrid
	 *            tile grid
	 * @param zoom
	 *            zoom level
	 *
	 * @return bounding box
	 */
	public abstract BoundingBox getBoundingBox(TileGrid tileGrid, long zoom);

}
