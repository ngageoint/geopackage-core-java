package mil.nga.geopackage.tiles;

import org.junit.Test;

import junit.framework.TestCase;
import mil.nga.geopackage.BoundingBox;
import mil.nga.proj.ProjectionConstants;
import mil.nga.sf.Point;
import mil.nga.sf.proj.GeometryTransform;

/**
 * Tile Bounding Box Utils Test
 * 
 * @author osbornb
 */
public class TileBoundingBoxUtilsTest {

	/**
	 * Test Tile Bounds
	 */
	@Test
	public void testTileBounds() {

		double longitude = -77.196785;
		double latitude = 38.753195;
		int zoom = 6;

		Point point = new Point(longitude, latitude);
		BoundingBox boundingBox = TileBoundingBoxUtils
				.getTileBoundsForWGS84(point, zoom);

		TestCase.assertEquals(-78.75000000000001, boundingBox.getMinLongitude(),
				0.0);
		TestCase.assertEquals(36.597889133070204, boundingBox.getMinLatitude(),
				0.0);
		TestCase.assertEquals(-73.125, boundingBox.getMaxLongitude(), 0.0);
		TestCase.assertEquals(40.97989806962014, boundingBox.getMaxLatitude(),
				0.0);

		GeometryTransform transform = GeometryTransform.create(
				ProjectionConstants.EPSG_WORLD_GEODETIC_SYSTEM,
				ProjectionConstants.EPSG_WEB_MERCATOR);
		point = transform.transform(point);
		boundingBox = TileBoundingBoxUtils.getTileBoundsForWebMercator(point,
				zoom);

		TestCase.assertEquals(-8766409.899970295, boundingBox.getMinLongitude(),
				0.0);
		TestCase.assertEquals(4383204.9499851465, boundingBox.getMinLatitude(),
				0.0);
		TestCase.assertEquals(-8140237.7642581295,
				boundingBox.getMaxLongitude(), 0.0);
		TestCase.assertEquals(5009377.085697312, boundingBox.getMaxLatitude(),
				0.0);

		longitude = 151.215026;
		latitude = -33.856686;
		zoom = 15;

		point = new Point(longitude, latitude);
		boundingBox = TileBoundingBoxUtils.getTileBoundsForWGS84(point, zoom);

		TestCase.assertEquals(151.20483398437506, boundingBox.getMinLongitude(),
				0.0);
		TestCase.assertEquals(-33.86129311351553, boundingBox.getMinLatitude(),
				0.0);
		TestCase.assertEquals(151.21582031250003, boundingBox.getMaxLongitude(),
				0.0);
		TestCase.assertEquals(-33.852169701407426, boundingBox.getMaxLatitude(),
				0.0);

		point = transform.transform(point);
		boundingBox = TileBoundingBoxUtils.getTileBoundsForWebMercator(point,
				zoom);

		TestCase.assertEquals(16832045.124622095, boundingBox.getMinLongitude(),
				0.0);
		TestCase.assertEquals(-4010192.2519534864, boundingBox.getMinLatitude(),
				0.0);
		TestCase.assertEquals(16833268.117074657, boundingBox.getMaxLongitude(),
				0.0);
		TestCase.assertEquals(-4008969.2595009245, boundingBox.getMaxLatitude(),
				0.0);

	}

	/**
	 * Test the tile grid of a point on a tile edge is the tile that starts
	 * there, and that edge points stay inside the tile matrix
	 */
	@Test
	public void testPointTileGrid() {

		double h = ProjectionConstants.WEB_MERCATOR_HALF_WORLD_WIDTH;

		// (0, 0) used to give [1,1 .. 0,0] at zoom 1
		assertTileGrid(1, 1, TileBoundingBoxUtils
				.getTileGridFromWebMercator(new Point(0, 0), 1));
		assertTileGrid(4, 4, TileBoundingBoxUtils
				.getTileGridFromWebMercator(new Point(0, 0), 3));
		assertTileGrid(4, 4,
				TileBoundingBoxUtils.getTileGridFromWGS84(new Point(0, 0), 3));
		// east and south edges of the world are in the last tile
		assertTileGrid(7, 7, TileBoundingBoxUtils
				.getTileGridFromWebMercator(new Point(h, -h), 3));
		assertTileGrid(0, 0, TileBoundingBoxUtils
				.getTileGridFromWebMercator(new Point(-h, h), 3));

		for (int zoom = 0; zoom <= 20; zoom++) {
			int tiles = TileBoundingBoxUtils.tilesPerSide(zoom);
			double size = TileBoundingBoxUtils.tileSize(tiles);
			for (int i = 0; i < tiles; i += Math.max(1, tiles / 7)) {
				TileGrid grid = TileBoundingBoxUtils.getTileGridFromWebMercator(
						new Point(-h + i * size, h - i * size), zoom);
				assertTileGrid(i, i, grid);
			}

			int lonTiles = TileBoundingBoxUtils.tilesPerWGS84LonSide(zoom);
			int latTiles = TileBoundingBoxUtils.tilesPerWGS84LatSide(zoom);
			for (int i = 0; i < latTiles; i += Math.max(1, latTiles / 7)) {
				double lon = -180 + i * 360.0 / lonTiles;
				double lat = 90 - i * 180.0 / latTiles;
				TileGrid grid = TileBoundingBoxUtils.getTileGridWGS84(
						new BoundingBox(lon, lat, lon, lat), zoom);
				assertTileGrid(i, i, grid);
			}
		}

	}

	private void assertTileGrid(long x, long y, TileGrid grid) {
		TestCase.assertEquals(x, grid.getMinX());
		TestCase.assertEquals(x, grid.getMaxX());
		TestCase.assertEquals(y, grid.getMinY());
		TestCase.assertEquals(y, grid.getMaxY());
	}

}
