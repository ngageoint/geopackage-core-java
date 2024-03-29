package mil.nga.geopackage.tiles;

import mil.nga.geopackage.BoundingBox;
import mil.nga.geopackage.tiles.matrix.TileMatrix;
import mil.nga.proj.Projection;
import mil.nga.proj.ProjectionConstants;
import mil.nga.proj.ProjectionFactory;
import mil.nga.sf.Point;
import mil.nga.sf.proj.GeometryTransform;

/**
 * Tile Bounding Box utility methods
 *
 * @author osbornb
 */
public class TileBoundingBoxUtils {

	/**
	 * Web mercator projection
	 */
	private static final Projection webMercator = ProjectionFactory
			.getProjection(ProjectionConstants.EPSG_WEB_MERCATOR);

	/**
	 * WGS84 projection
	 */
	private static final Projection wgs84 = ProjectionFactory
			.getProjection(ProjectionConstants.EPSG_WORLD_GEODETIC_SYSTEM);

	/**
	 * Get the overlapping bounding box between the two bounding boxes
	 *
	 * @param boundingBox
	 *            bounding box 1
	 * @param boundingBox2
	 *            bounding box 2
	 * @return bounding box
	 */
	public static BoundingBox overlap(BoundingBox boundingBox,
			BoundingBox boundingBox2) {
		return boundingBox.overlap(boundingBox2);
	}

	/**
	 * Get the overlapping bounding box between the two bounding boxes
	 *
	 * @param boundingBox
	 *            bounding box
	 * @param boundingBox2
	 *            bounding box 2
	 * @param allowEmpty
	 *            allow empty latitude and/or longitude ranges when determining
	 *            overlap
	 *
	 * @return bounding box
	 * @since 2.0.0
	 */
	public static BoundingBox overlap(BoundingBox boundingBox,
			BoundingBox boundingBox2, boolean allowEmpty) {
		return boundingBox.overlap(boundingBox2, allowEmpty);
	}

	/**
	 * Get the overlapping bounding box between the two bounding boxes adjusting
	 * the second box to an Anti-Meridian complementary version based upon the
	 * max longitude
	 *
	 * @param boundingBox
	 *            bounding box
	 * @param boundingBox2
	 *            bounding box 2
	 * @param maxLongitude
	 *            max longitude of the world for the current bounding box units
	 *
	 * @return bounding box
	 * @since 2.0.0
	 */
	public static BoundingBox overlap(BoundingBox boundingBox,
			BoundingBox boundingBox2, double maxLongitude) {
		return overlap(boundingBox, boundingBox2, maxLongitude, false);
	}

	/**
	 * Get the overlapping bounding box between the two bounding boxes adjusting
	 * the second box to an Anti-Meridian complementary version based upon the
	 * max longitude
	 *
	 * @param boundingBox
	 *            bounding box
	 * @param boundingBox2
	 *            bounding box 2
	 * @param maxLongitude
	 *            max longitude of the world for the current bounding box units
	 * @param allowEmpty
	 *            allow empty latitude and/or longitude ranges when determining
	 *            overlap
	 *
	 * @return bounding box
	 * @since 2.0.0
	 */
	public static BoundingBox overlap(BoundingBox boundingBox,
			BoundingBox boundingBox2, double maxLongitude, boolean allowEmpty) {

		BoundingBox bbox2 = boundingBox2;

		double adjustment = 0.0;

		if (maxLongitude > 0) {
			if (boundingBox.getMinLongitude() > boundingBox2
					.getMaxLongitude()) {
				adjustment = maxLongitude * 2.0;
			} else if (boundingBox.getMaxLongitude() < boundingBox2
					.getMinLongitude()) {
				adjustment = maxLongitude * -2.0;
			}
		}

		if (adjustment != 0.0) {
			bbox2 = boundingBox2.copy();
			bbox2.setMinLongitude(bbox2.getMinLongitude() + adjustment);
			bbox2.setMaxLongitude(bbox2.getMaxLongitude() + adjustment);
		}

		return overlap(boundingBox, bbox2, allowEmpty);
	}

	/**
	 * Determine if the point is within the bounding box
	 *
	 * @param point
	 *            bounding box
	 * @param boundingBox
	 *            bounding box
	 *
	 * @return true if within the bounding box
	 * @since 2.0.0
	 */
	public static boolean isPointInBoundingBox(Point point,
			BoundingBox boundingBox) {
		BoundingBox pointBoundingbox = new BoundingBox(point.getX(),
				point.getY(), point.getX(), point.getY());
		return boundingBox.intersects(pointBoundingbox, true);
	}

	/**
	 * Determine if the point is within the bounding box
	 *
	 * @param point
	 *            bounding box
	 * @param boundingBox
	 *            bounding box
	 * @param maxLongitude
	 *            max longitude of the world for the current bounding box units
	 *
	 * @return true if within the bounding box
	 * @since 2.0.0
	 */
	public static boolean isPointInBoundingBox(Point point,
			BoundingBox boundingBox, double maxLongitude) {
		BoundingBox pointBoundingbox = new BoundingBox(point.getX(),
				point.getY(), point.getX(), point.getY());
		BoundingBox overlap = overlap(boundingBox, pointBoundingbox,
				maxLongitude, true);
		return overlap != null;
	}

	/**
	 * Get the union bounding box combining the two bounding boxes
	 *
	 * @param boundingBox
	 *            bounding box 1
	 * @param boundingBox2
	 *            bounding box 2
	 * @return bounding box
	 */
	public static BoundingBox union(BoundingBox boundingBox,
			BoundingBox boundingBox2) {
		return boundingBox.union(boundingBox2);
	}

	/**
	 * Get the X pixel for where the longitude fits into the bounding box
	 *
	 * @param width
	 *            width
	 * @param boundingBox
	 *            bounding box
	 * @param longitude
	 *            longitude
	 * @return x pixel
	 */
	public static float getXPixel(long width, BoundingBox boundingBox,
			double longitude) {

		double boxWidth = boundingBox.getMaxLongitude()
				- boundingBox.getMinLongitude();
		double offset = longitude - boundingBox.getMinLongitude();
		double percentage = offset / boxWidth;
		float pixel = (float) (percentage * width);

		return pixel;
	}

	/**
	 * Get the longitude from the pixel location, bounding box, and image width
	 * 
	 * @param width
	 *            width
	 * @param boundingBox
	 *            bounding box
	 * @param pixel
	 *            pixel
	 * @return longitude
	 */
	public static double getLongitudeFromPixel(long width,
			BoundingBox boundingBox, float pixel) {
		return getLongitudeFromPixel(width, boundingBox, boundingBox, pixel);
	}

	/**
	 * Get the longitude from the pixel location, bounding box, tile bounding
	 * box (when different from bounding box), and image width
	 * 
	 * @param width
	 *            width
	 * @param boundingBox
	 *            bounding box
	 * @param tileBoundingBox
	 *            tile bounding box
	 * @param pixel
	 *            pixel
	 * @return longitude
	 * @since 3.2.0
	 */
	public static double getLongitudeFromPixel(long width,
			BoundingBox boundingBox, BoundingBox tileBoundingBox, float pixel) {

		double boxWidth = tileBoundingBox.getMaxLongitude()
				- tileBoundingBox.getMinLongitude();
		double percentage = pixel / width;
		double offset = percentage * boxWidth;
		double longitude = offset + boundingBox.getMinLongitude();

		return longitude;
	}

	/**
	 * Get the Y pixel for where the latitude fits into the bounding box
	 *
	 * @param height
	 *            height
	 * @param boundingBox
	 *            bounding box
	 * @param latitude
	 *            latitude
	 * @return y pixel
	 */
	public static float getYPixel(long height, BoundingBox boundingBox,
			double latitude) {

		double boxHeight = boundingBox.getMaxLatitude()
				- boundingBox.getMinLatitude();
		double offset = boundingBox.getMaxLatitude() - latitude;
		double percentage = offset / boxHeight;
		float pixel = (float) (percentage * height);

		return pixel;
	}

	/**
	 * Get the latitude from the pixel location, bounding box, and image height
	 * 
	 * @param height
	 *            height
	 * @param boundingBox
	 *            bounding box
	 * @param pixel
	 *            pixel
	 * @return latitude
	 */
	public static double getLatitudeFromPixel(long height,
			BoundingBox boundingBox, float pixel) {
		return getLatitudeFromPixel(height, boundingBox, boundingBox, pixel);
	}

	/**
	 * Get the latitude from the pixel location, bounding box, tile bounding box
	 * (when different from bounding box), and image height
	 * 
	 * @param height
	 *            height
	 * @param boundingBox
	 *            bounding box
	 * @param tileBoundingBox
	 *            tile bounding box
	 * @param pixel
	 *            pixel
	 * @return latitude
	 * @since 3.2.0
	 */
	public static double getLatitudeFromPixel(long height,
			BoundingBox boundingBox, BoundingBox tileBoundingBox, float pixel) {

		double boxHeight = tileBoundingBox.getMaxLatitude()
				- tileBoundingBox.getMinLatitude();
		double percentage = pixel / height;
		double offset = percentage * boxHeight;
		double latitude = boundingBox.getMaxLatitude() - offset;

		return latitude;
	}

	/**
	 * Get the tile bounding box from the XYZ tile coordinates and zoom level
	 *
	 * @param x
	 *            x coordinate
	 * @param y
	 *            y coordinate
	 * @param zoom
	 *            zoom level
	 * @return bounding box
	 */
	public static BoundingBox getBoundingBox(int x, int y, long zoom) {

		int tilesPerSide = tilesPerSide(zoom);
		double tileWidthDegrees = tileWidthDegrees(tilesPerSide);
		double tileHeightDegrees = tileHeightDegrees(tilesPerSide);

		double minLon = -180.0 + (x * tileWidthDegrees);
		double maxLon = minLon + tileWidthDegrees;

		double maxLat = 90.0 - (y * tileHeightDegrees);
		double minLat = maxLat - tileHeightDegrees;

		BoundingBox box = new BoundingBox(minLon, minLat, maxLon, maxLat);

		return box;
	}

	/**
	 * Get the Web Mercator tile bounding box from the XYZ tile coordinates and
	 * zoom level
	 *
	 * @param x
	 *            x coordinate
	 * @param y
	 *            y coordinate
	 * @param zoom
	 *            zoom level
	 * @return bounding box
	 */
	public static BoundingBox getWebMercatorBoundingBox(long x, long y,
			long zoom) {
		return getWebMercatorBoundingBox(new TileGrid(x, y, x, y), zoom);
	}

	/**
	 * Get the Web Mercator tile bounding box from the XYZ tile grid and zoom
	 * level
	 *
	 * @param tileGrid
	 *            tile grid
	 * @param zoom
	 *            zoom level
	 * @return bounding box
	 */
	public static BoundingBox getWebMercatorBoundingBox(TileGrid tileGrid,
			long zoom) {

		double tileSize = tileSizeWithZoom(zoom);

		double minLon = (-1 * ProjectionConstants.WEB_MERCATOR_HALF_WORLD_WIDTH)
				+ (tileGrid.getMinX() * tileSize);
		double maxLon = (-1 * ProjectionConstants.WEB_MERCATOR_HALF_WORLD_WIDTH)
				+ ((tileGrid.getMaxX() + 1) * tileSize);
		double minLat = ProjectionConstants.WEB_MERCATOR_HALF_WORLD_WIDTH
				- ((tileGrid.getMaxY() + 1) * tileSize);
		double maxLat = ProjectionConstants.WEB_MERCATOR_HALF_WORLD_WIDTH
				- (tileGrid.getMinY() * tileSize);

		BoundingBox box = new BoundingBox(minLon, minLat, maxLon, maxLat);

		return box;
	}

	/**
	 * Get the Projected tile bounding box from the XYZ tile coordinates and
	 * zoom level
	 *
	 * @param projectionEpsg
	 *            projection epsg
	 * @param x
	 *            x coordinate
	 * @param y
	 *            y coordinate
	 * @param zoom
	 *            zoom level
	 * @return bounding box
	 */
	public static BoundingBox getProjectedBoundingBox(Long projectionEpsg,
			int x, int y, long zoom) {
		return getProjectedBoundingBox(ProjectionConstants.AUTHORITY_EPSG,
				projectionEpsg, x, y, zoom);
	}

	/**
	 * Get the Projected tile bounding box from the XYZ tile coordinates and
	 * zoom level
	 *
	 * @param authority
	 *            projection authority
	 * @param code
	 *            projection code
	 * @param x
	 *            x coordinate
	 * @param y
	 *            y coordinate
	 * @param zoom
	 *            zoom level
	 * @return bounding box
	 * @since 1.3.0
	 */
	public static BoundingBox getProjectedBoundingBox(String authority,
			Long code, int x, int y, long zoom) {

		BoundingBox boundingBox = getWebMercatorBoundingBox(x, y, zoom);

		if (code != null) {
			GeometryTransform transform = GeometryTransform.create(webMercator,
					authority, code);
			boundingBox = boundingBox.transform(transform);
		}

		return boundingBox;
	}

	/**
	 * Get the Projected tile bounding box from the XYZ tile coordinates and
	 * zoom level
	 *
	 * @param projection
	 *            projection
	 * @param x
	 *            x coordinate
	 * @param y
	 *            y coordinate
	 * @param zoom
	 *            zoom level
	 * @return bounding box
	 */
	public static BoundingBox getProjectedBoundingBox(Projection projection,
			long x, long y, long zoom) {

		BoundingBox boundingBox = getWebMercatorBoundingBox(x, y, zoom);

		if (projection != null) {
			GeometryTransform transform = GeometryTransform.create(webMercator,
					projection);
			boundingBox = boundingBox.transform(transform);
		}

		return boundingBox;
	}

	/**
	 * Get the Projected tile bounding box from the XYZ tile tileGrid and zoom
	 * level
	 *
	 * @param projectionEpsg
	 *            projection epsg
	 * @param tileGrid
	 *            tile grid
	 * @param zoom
	 *            zoom level
	 * @return bounding box
	 */
	public static BoundingBox getProjectedBoundingBox(Long projectionEpsg,
			TileGrid tileGrid, long zoom) {
		return getProjectedBoundingBox(ProjectionConstants.AUTHORITY_EPSG,
				projectionEpsg, tileGrid, zoom);
	}

	/**
	 * Get the Projected tile bounding box from the XYZ tile tileGrid and zoom
	 * level
	 *
	 * @param authority
	 *            projection authority
	 * @param code
	 *            projection code
	 * @param tileGrid
	 *            tile grid
	 * @param zoom
	 *            zoom level
	 * @return bounding box
	 * @since 1.3.0
	 */
	public static BoundingBox getProjectedBoundingBox(String authority,
			Long code, TileGrid tileGrid, long zoom) {

		BoundingBox boundingBox = getWebMercatorBoundingBox(tileGrid, zoom);

		if (code != null) {
			GeometryTransform transform = GeometryTransform.create(webMercator,
					authority, code);
			boundingBox = boundingBox.transform(transform);
		}

		return boundingBox;
	}

	/**
	 * Get the Projected tile bounding box from the XYZ tile grid and zoom level
	 *
	 * @param projection
	 *            projection
	 * @param tileGrid
	 *            tile grid
	 * @param zoom
	 *            zoom level
	 * @return bounding box
	 */
	public static BoundingBox getProjectedBoundingBox(Projection projection,
			TileGrid tileGrid, long zoom) {

		BoundingBox boundingBox = getWebMercatorBoundingBox(tileGrid, zoom);

		if (projection != null) {
			GeometryTransform transform = GeometryTransform.create(webMercator,
					projection);
			boundingBox = boundingBox.transform(transform);
		}

		return boundingBox;
	}

	/**
	 * Get the WGS84 tile bounding box from the XYZ tile tileGrid and zoom level
	 *
	 * @param tileGrid
	 *            tile grid
	 * @param zoom
	 *            zoom level
	 * @return bounding box
	 * @since 6.2.0
	 */
	public static BoundingBox getBoundingBoxAsWGS84(TileGrid tileGrid,
			long zoom) {
		return getProjectedBoundingBox(
				(long) ProjectionConstants.EPSG_WORLD_GEODETIC_SYSTEM, tileGrid,
				zoom);
	}

	/**
	 * Get the Projected tile bounding box from the WGS84 XYZ tile coordinates
	 * and zoom level
	 *
	 * @param projectionEpsg
	 *            projection epsg
	 * @param x
	 *            x coordinate
	 * @param y
	 *            y coordinate
	 * @param zoom
	 *            zoom level
	 * @return bounding box
	 * @since 6.0.1
	 */
	public static BoundingBox getProjectedBoundingBoxFromWGS84(
			Long projectionEpsg, int x, int y, long zoom) {
		return getProjectedBoundingBoxFromWGS84(
				ProjectionConstants.AUTHORITY_EPSG, projectionEpsg, x, y, zoom);
	}

	/**
	 * Get the Projected tile bounding box from the WGS84 XYZ tile coordinates
	 * and zoom level
	 *
	 * @param authority
	 *            projection authority
	 * @param code
	 *            projection code
	 * @param x
	 *            x coordinate
	 * @param y
	 *            y coordinate
	 * @param zoom
	 *            zoom level
	 * @return bounding box
	 * @since 6.0.1
	 */
	public static BoundingBox getProjectedBoundingBoxFromWGS84(String authority,
			Long code, int x, int y, long zoom) {

		BoundingBox boundingBox = getWGS84BoundingBox(x, y, zoom);

		if (code != null) {
			GeometryTransform transform = GeometryTransform.create(wgs84,
					authority, code);
			boundingBox = boundingBox.transform(transform);
		}

		return boundingBox;
	}

	/**
	 * Get the Projected tile bounding box from the WGS84 XYZ tile coordinates
	 * and zoom level
	 *
	 * @param projection
	 *            projection
	 * @param x
	 *            x coordinate
	 * @param y
	 *            y coordinate
	 * @param zoom
	 *            zoom level
	 * @return bounding box
	 * @since 6.0.1
	 */
	public static BoundingBox getProjectedBoundingBoxFromWGS84(
			Projection projection, long x, long y, long zoom) {

		BoundingBox boundingBox = getWGS84BoundingBox(x, y, zoom);

		if (projection != null) {
			GeometryTransform transform = GeometryTransform.create(wgs84,
					projection);
			boundingBox = boundingBox.transform(transform);
		}

		return boundingBox;
	}

	/**
	 * Get the Projected tile bounding box from the WGS84 XYZ tile tileGrid and
	 * zoom level
	 *
	 * @param projectionEpsg
	 *            projection epsg
	 * @param tileGrid
	 *            tile grid
	 * @param zoom
	 *            zoom level
	 * @return bounding box
	 * @since 6.0.1
	 */
	public static BoundingBox getProjectedBoundingBoxFromWGS84(
			Long projectionEpsg, TileGrid tileGrid, long zoom) {
		return getProjectedBoundingBoxFromWGS84(
				ProjectionConstants.AUTHORITY_EPSG, projectionEpsg, tileGrid,
				zoom);
	}

	/**
	 * Get the Projected tile bounding box from the WGS84 XYZ tile tileGrid and
	 * zoom level
	 *
	 * @param authority
	 *            projection authority
	 * @param code
	 *            projection code
	 * @param tileGrid
	 *            tile grid
	 * @param zoom
	 *            zoom level
	 * @return bounding box
	 * @since 6.0.1
	 */
	public static BoundingBox getProjectedBoundingBoxFromWGS84(String authority,
			Long code, TileGrid tileGrid, long zoom) {

		BoundingBox boundingBox = getWGS84BoundingBox(tileGrid, zoom);

		if (code != null) {
			GeometryTransform transform = GeometryTransform.create(wgs84,
					authority, code);
			boundingBox = boundingBox.transform(transform);
		}

		return boundingBox;
	}

	/**
	 * Get the Projected tile bounding box from the WGS84 XYZ tile grid and zoom
	 * level
	 *
	 * @param projection
	 *            projection
	 * @param tileGrid
	 *            tile grid
	 * @param zoom
	 *            zoom level
	 * @return bounding box
	 * @since 6.0.1
	 */
	public static BoundingBox getProjectedBoundingBoxFromWGS84(
			Projection projection, TileGrid tileGrid, long zoom) {

		BoundingBox boundingBox = getWGS84BoundingBox(tileGrid, zoom);

		if (projection != null) {
			GeometryTransform transform = GeometryTransform.create(wgs84,
					projection);
			boundingBox = boundingBox.transform(transform);
		}

		return boundingBox;
	}

	/**
	 * Get the tile grid for the location specified as WGS84
	 * 
	 * @param point
	 *            point
	 * @param zoom
	 *            zoom level
	 * @return tile grid
	 * @since 1.1.0
	 */
	public static TileGrid getTileGridFromWGS84(Point point, long zoom) {
		Projection projection = ProjectionFactory
				.getProjection(ProjectionConstants.EPSG_WORLD_GEODETIC_SYSTEM);
		return getTileGrid(point, zoom, projection);
	}

	/**
	 * Get the tile grid for the location specified as the projection
	 * 
	 * @param point
	 *            point
	 * @param zoom
	 *            zoom level
	 * @param projection
	 *            projection
	 * @return tile grid
	 * @since 1.1.0
	 */
	public static TileGrid getTileGrid(Point point, long zoom,
			Projection projection) {
		GeometryTransform toWebMercator = GeometryTransform.create(projection,
				ProjectionConstants.EPSG_WEB_MERCATOR);
		Point webMercatorPoint = toWebMercator.transform(point);
		return getTileGridFromWebMercator(webMercatorPoint, zoom);
	}

	/**
	 * Get the tile grid for the location specified as web mercator
	 * 
	 * @param point
	 *            point
	 * @param zoom
	 *            zoom level
	 * @return tile grid
	 * @since 6.2.0
	 */
	public static TileGrid getTileGridFromWebMercator(Point point, long zoom) {
		BoundingBox boundingBox = new BoundingBox(point.getX(), point.getY(),
				point.getX(), point.getY());
		return getTileGrid(boundingBox, zoom);
	}

	/**
	 * Get the tile grid that includes the entire tile bounding box
	 *
	 * @param webMercatorBoundingBox
	 *            web mercator bounding box
	 * @param zoom
	 *            zoom level
	 * @return tile grid
	 */
	public static TileGrid getTileGrid(BoundingBox webMercatorBoundingBox,
			long zoom) {

		int tilesPerSide = tilesPerSide(zoom);
		double tileSize = tileSize(tilesPerSide);

		int minX = (int) ((webMercatorBoundingBox.getMinLongitude()
				+ ProjectionConstants.WEB_MERCATOR_HALF_WORLD_WIDTH)
				/ tileSize);
		double tempMaxX = (webMercatorBoundingBox.getMaxLongitude()
				+ ProjectionConstants.WEB_MERCATOR_HALF_WORLD_WIDTH) / tileSize;
		int maxX = (int) (tempMaxX
				- ProjectionConstants.WEB_MERCATOR_PRECISION);
		maxX = Math.min(maxX, tilesPerSide - 1);

		int minY = (int) (((webMercatorBoundingBox.getMaxLatitude()
				- ProjectionConstants.WEB_MERCATOR_HALF_WORLD_WIDTH) * -1)
				/ tileSize);
		double tempMaxY = ((webMercatorBoundingBox.getMinLatitude()
				- ProjectionConstants.WEB_MERCATOR_HALF_WORLD_WIDTH) * -1)
				/ tileSize;
		int maxY = (int) (tempMaxY
				- ProjectionConstants.WEB_MERCATOR_PRECISION);
		maxY = Math.min(maxY, tilesPerSide - 1);

		TileGrid grid = new TileGrid(minX, minY, maxX, maxY);

		return grid;
	}

	/**
	 * Get the bounds of the XYZ tile at the point and zoom level
	 *
	 * @param projection
	 *            point and bounding box projection
	 * @param point
	 *            point location
	 * @param zoom
	 *            zoom level
	 * @return bounding box
	 * @since 6.2.0
	 */
	public static BoundingBox getTileBounds(Projection projection, Point point,
			int zoom) {
		TileGrid tileGrid = getTileGrid(point, zoom, projection);
		return getProjectedBoundingBox(projection, tileGrid, zoom);
	}

	/**
	 * Get the WGS84 bounds of the XYZ tile at the WGS84 point and zoom level
	 *
	 * @param point
	 *            WGS84 point
	 * @param zoom
	 *            zoom level
	 * @return WGS84 bounding box
	 * @since 6.2.0
	 */
	public static BoundingBox getTileBoundsForWGS84(Point point, int zoom) {
		Projection projection = ProjectionFactory
				.getProjection(ProjectionConstants.EPSG_WORLD_GEODETIC_SYSTEM);
		return getTileBounds(projection, point, zoom);
	}

	/**
	 * Get the web mercator bounds of the XYZ tile at the web mercator point and
	 * zoom level
	 *
	 * @param point
	 *            web mercator point
	 * @param zoom
	 *            zoom level
	 * @return web mercator bounding box
	 * @since 6.2.0
	 */
	public static BoundingBox getTileBoundsForWebMercator(Point point,
			int zoom) {
		Projection projection = ProjectionFactory
				.getProjection(ProjectionConstants.EPSG_WEB_MERCATOR);
		return getTileBounds(projection, point, zoom);
	}

	/**
	 * Get the bounds of the WGS84 tile at the point and zoom level
	 *
	 * @param projection
	 *            point and bounding box projection
	 * @param point
	 *            point location
	 * @param zoom
	 *            zoom level
	 * @return bounding box
	 * @since 6.2.0
	 */
	public static BoundingBox getWGS84TileBounds(Projection projection,
			Point point, int zoom) {
		TileGrid tileGrid = getTileGridWGS84(point, zoom, projection);
		return getProjectedBoundingBoxFromWGS84(projection, tileGrid, zoom);
	}

	/**
	 * Get the WGS84 bounds of the WGS84 tile at the WGS84 point and zoom level
	 *
	 * @param point
	 *            WGS84 point
	 * @param zoom
	 *            zoom level
	 * @return WGS84 bounding box
	 * @since 6.2.0
	 */
	public static BoundingBox getWGS84TileBoundsForWGS84(Point point,
			int zoom) {
		Projection projection = ProjectionFactory
				.getProjection(ProjectionConstants.EPSG_WORLD_GEODETIC_SYSTEM);
		return getWGS84TileBounds(projection, point, zoom);
	}

	/**
	 * Get the web mercator bounds of the WGS84 tile at the web mercator point
	 * and zoom level
	 *
	 * @param point
	 *            web mercator point
	 * @param zoom
	 *            zoom level
	 * @return web mercator bounding box
	 * @since 6.2.0
	 */
	public static BoundingBox getWGS84TileBoundsForWebMercator(Point point,
			int zoom) {
		Projection projection = ProjectionFactory
				.getProjection(ProjectionConstants.EPSG_WEB_MERCATOR);
		return getWGS84TileBounds(projection, point, zoom);
	}

	/**
	 * Convert the bounding box coordinates to a new web mercator bounding box
	 *
	 * @param boundingBox
	 *            bounding box
	 * @return bounding box
	 */
	public static BoundingBox toWebMercator(BoundingBox boundingBox) {

		double minLatitude = Math.max(boundingBox.getMinLatitude(),
				ProjectionConstants.WEB_MERCATOR_MIN_LAT_RANGE);
		double maxLatitude = Math.min(boundingBox.getMaxLatitude(),
				ProjectionConstants.WEB_MERCATOR_MAX_LAT_RANGE);

		Point lowerLeftPoint = new Point(false, false,
				boundingBox.getMinLongitude(), minLatitude);
		Point upperRightPoint = new Point(false, false,
				boundingBox.getMaxLongitude(), maxLatitude);

		GeometryTransform toWebMercator = GeometryTransform.create(
				ProjectionConstants.EPSG_WORLD_GEODETIC_SYSTEM,
				ProjectionConstants.EPSG_WEB_MERCATOR);
		lowerLeftPoint = toWebMercator.transform(lowerLeftPoint);
		upperRightPoint = toWebMercator.transform(upperRightPoint);

		BoundingBox mercatorBox = new BoundingBox(lowerLeftPoint.getX(),
				lowerLeftPoint.getY(), upperRightPoint.getX(),
				upperRightPoint.getY());

		return mercatorBox;
	}

	/**
	 * Get the tile size in meters
	 *
	 * @param tilesPerSide
	 *            tiles per side
	 * @return tile size
	 */
	public static double tileSize(int tilesPerSide) {
		return tileSize(tilesPerSide,
				2 * ProjectionConstants.WEB_MERCATOR_HALF_WORLD_WIDTH);
	}

	/**
	 * Get the zoom level from the tile size in meters
	 * 
	 * @param tileSize
	 *            tile size in meters
	 * @return zoom level
	 * @since 1.2.0
	 */
	public static double zoomLevelOfTileSize(double tileSize) {
		return zoomLevelOfTileSize(tileSize,
				2 * ProjectionConstants.WEB_MERCATOR_HALF_WORLD_WIDTH);
	}

	/**
	 * Get the tile size in length units
	 * 
	 * @param tilesPerSide
	 *            tiles per side
	 * @param totalLength
	 *            total length
	 * @return tile size
	 * @since 6.2.0
	 */
	public static double tileSize(int tilesPerSide, double totalLength) {
		return totalLength / tilesPerSide;
	}

	/**
	 * Get the zoom level from the tile size in length units
	 * 
	 * @param tileSize
	 *            tile size in units
	 * @param totalLength
	 *            total length
	 * @return zoom level
	 * @since 6.2.0
	 */
	public static double zoomLevelOfTileSize(double tileSize,
			double totalLength) {
		double tilesPerSide = totalLength / tileSize;
		double zoom = Math.log(tilesPerSide) / Math.log(2);
		return zoom;
	}

	/**
	 * Get the tile size in length units at the zoom level
	 *
	 * @param zoom
	 *            zoom level
	 * @param totalLength
	 *            total length
	 * @return tile size in units
	 * @since 6.2.0
	 */
	public static double tileSizeWithZoom(long zoom, double totalLength) {
		int tilesPerSide = tilesPerSide(zoom);
		double tileSize = tileSize(tilesPerSide, totalLength);
		return tileSize;
	}

	/**
	 * Get the tile width in degrees
	 *
	 * @param tilesPerSide
	 *            tiles per side
	 * @return tile width degrees
	 */
	public static double tileWidthDegrees(int tilesPerSide) {
		return 360.0 / tilesPerSide;
	}

	/**
	 * Get the tile height in degrees
	 *
	 * @param tilesPerSide
	 *            tiles per side
	 * @return tile height degrees
	 */
	public static double tileHeightDegrees(int tilesPerSide) {
		return 180.0 / tilesPerSide;
	}

	/**
	 * Get the tiles per side, width and height, at the zoom level
	 *
	 * @param zoom
	 *            zoom level
	 * @return tiles per side
	 */
	public static int tilesPerSide(long zoom) {
		return (int) Math.pow(2, zoom);
	}

	/**
	 * Get the tile size in meters at the zoom level
	 *
	 * @param zoom
	 *            zoom level
	 *
	 * @return tile size in meters
	 * @since 2.0.0
	 */
	public static double tileSizeWithZoom(long zoom) {
		int tilesPerSide = tilesPerSide(zoom);
		double tileSize = tileSize(tilesPerSide);
		return tileSize;
	}

	/**
	 * Get the tolerance distance in meters for the zoom level and pixels length
	 *
	 * @param zoom
	 *            zoom level
	 * @param pixels
	 *            pixel length
	 *
	 * @return tolerance distance in meters
	 * @since 2.0.0
	 */
	public static double toleranceDistance(long zoom, int pixels) {
		double tileSize = tileSizeWithZoom(zoom);
		double tolerance = tileSize / pixels;
		return tolerance;
	}

	/**
	 * Get the tolerance distance in meters for the zoom level and pixels length
	 *
	 * @param zoom
	 *            zoom level
	 * @param pixelWidth
	 *            pixel width
	 * @param pixelHeight
	 *            pixel height
	 *
	 * @return tolerance distance in meters
	 * @since 2.0.0
	 */
	public static double toleranceDistance(long zoom, int pixelWidth,
			int pixelHeight) {
		return toleranceDistance(zoom, Math.max(pixelWidth, pixelHeight));
	}

	/**
	 * Get the standard y tile location as TMS or a TMS y location as standard
	 * 
	 * @param zoom
	 *            zoom level
	 * @param y
	 *            y coordinate
	 * @return opposite tile format y
	 */
	public static int getYAsOppositeTileFormat(long zoom, int y) {
		int tilesPerSide = tilesPerSide(zoom);
		int oppositeY = tilesPerSide - y - 1;
		return oppositeY;
	}

	/**
	 * Get the zoom level from the tiles per side
	 *
	 * @param tilesPerSide
	 *            tiles per side
	 * @return zoom level
	 */
	public static int zoomFromTilesPerSide(int tilesPerSide) {
		return (int) (Math.log(tilesPerSide) / Math.log(2));
	}

	/**
	 * Get the tile grid
	 *
	 * @param totalBox
	 *            total bounding box
	 * @param matrixWidth
	 *            matrix width
	 * @param matrixHeight
	 *            matrix height
	 * @param boundingBox
	 *            bounding box
	 * @return tile grid
	 */
	public static TileGrid getTileGrid(BoundingBox totalBox, long matrixWidth,
			long matrixHeight, BoundingBox boundingBox) {

		long minColumn = getTileColumn(totalBox, matrixWidth,
				boundingBox.getMinLongitude());
		long maxColumn = getTileColumn(totalBox, matrixWidth,
				boundingBox.getMaxLongitude());

		if (minColumn < matrixWidth && maxColumn >= 0) {
			if (minColumn < 0) {
				minColumn = 0;
			}
			if (maxColumn >= matrixWidth) {
				maxColumn = matrixWidth - 1;
			}
		}

		long maxRow = getTileRow(totalBox, matrixHeight,
				boundingBox.getMinLatitude());
		long minRow = getTileRow(totalBox, matrixHeight,
				boundingBox.getMaxLatitude());

		if (minRow < matrixHeight && maxRow >= 0) {
			if (minRow < 0) {
				minRow = 0;
			}
			if (maxRow >= matrixHeight) {
				maxRow = matrixHeight - 1;
			}
		}

		TileGrid tileGrid = new TileGrid(minColumn, minRow, maxColumn, maxRow);

		return tileGrid;
	}

	/**
	 * Get the tile column of the longitude in constant units
	 *
	 * @param totalBox
	 *            total bounding box
	 * @param matrixWidth
	 *            matrix width
	 * @param longitude
	 *            in constant units
	 * @return tile column if in the range, -1 if before,
	 *         {@link TileMatrix#getMatrixWidth()} if after
	 */
	public static long getTileColumn(BoundingBox totalBox, long matrixWidth,
			double longitude) {

		double minX = totalBox.getMinLongitude();
		double maxX = totalBox.getMaxLongitude();

		long tileId;
		if (longitude < minX) {
			tileId = -1;
		} else if (longitude >= maxX) {
			tileId = matrixWidth;
		} else {
			double matrixWidthMeters = totalBox.getMaxLongitude()
					- totalBox.getMinLongitude();
			double tileWidth = matrixWidthMeters / matrixWidth;
			tileId = (long) ((longitude - minX) / tileWidth);
		}

		return tileId;
	}

	/**
	 * Get the tile row of the latitude in constant units
	 *
	 * @param totalBox
	 *            total bounding box
	 * @param matrixHeight
	 *            matrix height
	 * @param latitude
	 *            in constant units
	 * @return tile row if in the range, -1 if before,
	 *         {@link TileMatrix#getMatrixHeight()} if after
	 */
	public static long getTileRow(BoundingBox totalBox, long matrixHeight,
			double latitude) {

		double minY = totalBox.getMinLatitude();
		double maxY = totalBox.getMaxLatitude();

		long tileId;
		if (latitude <= minY) {
			tileId = matrixHeight;
		} else if (latitude > maxY) {
			tileId = -1;
		} else {
			double matrixHeightMeters = totalBox.getMaxLatitude()
					- totalBox.getMinLatitude();
			double tileHeight = matrixHeightMeters / matrixHeight;
			tileId = (long) ((maxY - latitude) / tileHeight);
		}

		return tileId;
	}

	/**
	 * Get the bounding box of the tile column and row in the tile matrix using
	 * the total bounding box with constant units
	 *
	 * @param totalBox
	 *            total bounding box
	 * @param tileMatrix
	 *            tile matrix
	 * @param tileColumn
	 *            tile column
	 * @param tileRow
	 *            tile row
	 * @return bounding box
	 * @since 1.2.0
	 */
	public static BoundingBox getBoundingBox(BoundingBox totalBox,
			TileMatrix tileMatrix, long tileColumn, long tileRow) {
		return getBoundingBox(totalBox, tileMatrix.getMatrixWidth(),
				tileMatrix.getMatrixHeight(), tileColumn, tileRow);
	}

	/**
	 * Get the bounding box of the tile column and row in the tile width and
	 * height bounds using the total bounding box with constant units
	 *
	 * @param totalBox
	 *            total bounding box
	 * @param tileMatrixWidth
	 *            matrix width
	 * @param tileMatrixHeight
	 *            matrix height
	 * @param tileColumn
	 *            tile column
	 * @param tileRow
	 *            tile row
	 * @return bounding box
	 * @since 1.2.0
	 */
	public static BoundingBox getBoundingBox(BoundingBox totalBox,
			long tileMatrixWidth, long tileMatrixHeight, long tileColumn,
			long tileRow) {
		TileGrid tileGrid = new TileGrid(tileColumn, tileRow, tileColumn,
				tileRow);
		return getBoundingBox(totalBox, tileMatrixWidth, tileMatrixHeight,
				tileGrid);
	}

	/**
	 * Get the bounding box of the tile grid in the tile matrix using the total
	 * bounding box with constant units
	 * 
	 * @param totalBox
	 *            total bounding box
	 * @param tileMatrix
	 *            tile matrix
	 * @param tileGrid
	 *            tile grid
	 * @return bounding box
	 * @since 1.2.0
	 */
	public static BoundingBox getBoundingBox(BoundingBox totalBox,
			TileMatrix tileMatrix, TileGrid tileGrid) {
		return getBoundingBox(totalBox, tileMatrix.getMatrixWidth(),
				tileMatrix.getMatrixHeight(), tileGrid);
	}

	/**
	 * Get the bounding box of the tile grid in the tile width and height bounds
	 * using the total bounding box with constant units
	 * 
	 * @param totalBox
	 *            total bounding box
	 * @param tileMatrixWidth
	 *            matrix width
	 * @param tileMatrixHeight
	 *            matrix height
	 * @param tileGrid
	 *            tile grid
	 * @return bounding box
	 * @since 1.2.0
	 */
	public static BoundingBox getBoundingBox(BoundingBox totalBox,
			long tileMatrixWidth, long tileMatrixHeight, TileGrid tileGrid) {

		// Get the tile width
		double matrixMinX = totalBox.getMinLongitude();
		double matrixMaxX = totalBox.getMaxLongitude();
		double matrixWidth = matrixMaxX - matrixMinX;
		double tileWidth = matrixWidth / tileMatrixWidth;

		// Find the longitude range
		double minLon = matrixMinX + (tileWidth * tileGrid.getMinX());
		double maxLon = matrixMinX + (tileWidth * (tileGrid.getMaxX() + 1));

		// Get the tile height
		double matrixMinY = totalBox.getMinLatitude();
		double matrixMaxY = totalBox.getMaxLatitude();
		double matrixHeight = matrixMaxY - matrixMinY;
		double tileHeight = matrixHeight / tileMatrixHeight;

		// Find the latitude range
		double maxLat = matrixMaxY - (tileHeight * tileGrid.getMinY());
		double minLat = matrixMaxY - (tileHeight * (tileGrid.getMaxY() + 1));

		BoundingBox boundingBox = new BoundingBox(minLon, minLat, maxLon,
				maxLat);

		return boundingBox;
	}

	/**
	 * Get the zoom level of where the web mercator bounding box fits into the
	 * complete world
	 *
	 * @param webMercatorBoundingBox
	 *            web mercator bounding box
	 * @return zoom level
	 */
	public static int getZoomLevel(BoundingBox webMercatorBoundingBox) {

		double worldLength = ProjectionConstants.WEB_MERCATOR_HALF_WORLD_WIDTH
				* 2;

		double longitudeDistance = webMercatorBoundingBox.getMaxLongitude()
				- webMercatorBoundingBox.getMinLongitude();
		double latitudeDistance = webMercatorBoundingBox.getMaxLatitude()
				- webMercatorBoundingBox.getMinLatitude();

		if (longitudeDistance <= 0) {
			longitudeDistance = Double.MIN_VALUE;
		}
		if (latitudeDistance <= 0) {
			latitudeDistance = Double.MIN_VALUE;
		}

		int widthTiles = (int) (worldLength / longitudeDistance);
		int heightTiles = (int) (worldLength / latitudeDistance);

		int tilesPerSide = Math.min(widthTiles, heightTiles);
		tilesPerSide = Math.max(tilesPerSide, 1);

		int zoom = zoomFromTilesPerSide(tilesPerSide);

		return zoom;
	}

	/**
	 * Get the pixel x size for the bounding box with matrix width and tile
	 * width
	 * 
	 * @param webMercatorBoundingBox
	 *            web mercator bounding box
	 * @param matrixWidth
	 *            matrix width
	 * @param tileWidth
	 *            tile width
	 * @return pixel x size
	 */
	public static double getPixelXSize(BoundingBox webMercatorBoundingBox,
			long matrixWidth, int tileWidth) {
		double pixelXSize = (webMercatorBoundingBox.getMaxLongitude()
				- webMercatorBoundingBox.getMinLongitude()) / matrixWidth
				/ tileWidth;
		return pixelXSize;
	}

	/**
	 * Get the pixel y size for the bounding box with matrix height and tile
	 * height
	 * 
	 * @param webMercatorBoundingBox
	 *            web mercator bounding box
	 * @param matrixHeight
	 *            matrix height
	 * @param tileHeight
	 *            tile height
	 * @return pixel y size
	 */
	public static double getPixelYSize(BoundingBox webMercatorBoundingBox,
			long matrixHeight, int tileHeight) {
		double pixelYSize = (webMercatorBoundingBox.getMaxLatitude()
				- webMercatorBoundingBox.getMinLatitude()) / matrixHeight
				/ tileHeight;
		return pixelYSize;
	}

	/**
	 * Bound the web mercator bounding box within the limits
	 * 
	 * @param boundingBox
	 *            web mercator bounding box
	 * @return bounding box
	 * @since 3.5.0
	 */
	public static BoundingBox boundWebMercatorBoundingBox(
			BoundingBox boundingBox) {
		BoundingBox bounded = boundingBox.copy();
		bounded.setMinLongitude(Math.max(bounded.getMinLongitude(),
				-1 * ProjectionConstants.WEB_MERCATOR_HALF_WORLD_WIDTH));
		bounded.setMaxLongitude(Math.min(bounded.getMaxLongitude(),
				ProjectionConstants.WEB_MERCATOR_HALF_WORLD_WIDTH));
		bounded.setMinLatitude(Math.max(bounded.getMinLatitude(),
				-1 * ProjectionConstants.WEB_MERCATOR_HALF_WORLD_WIDTH));
		bounded.setMaxLatitude(Math.min(bounded.getMaxLatitude(),
				ProjectionConstants.WEB_MERCATOR_HALF_WORLD_WIDTH));
		return bounded;
	}

	/**
	 * Bound the upper and lower bounds of the WGS84 bounding box with web
	 * mercator limits
	 * 
	 * @param boundingBox
	 *            wgs84 bounding box
	 * @return bounding box
	 * @since 1.1.6
	 */
	public static BoundingBox boundWgs84BoundingBoxWithWebMercatorLimits(
			BoundingBox boundingBox) {
		return boundDegreesBoundingBoxWithWebMercatorLimits(boundingBox);
	}

	/**
	 * Bound the upper and lower bounds of the degrees bounding box with web
	 * mercator limits
	 * 
	 * @param boundingBox
	 *            degrees bounding box
	 * @return bounding box
	 * @since 1.3.1
	 */
	public static BoundingBox boundDegreesBoundingBoxWithWebMercatorLimits(
			BoundingBox boundingBox) {
		BoundingBox bounded = boundingBox.copy();
		if (bounded
				.getMinLatitude() < ProjectionConstants.WEB_MERCATOR_MIN_LAT_RANGE) {
			bounded.setMinLatitude(
					ProjectionConstants.WEB_MERCATOR_MIN_LAT_RANGE);
		}
		if (bounded
				.getMaxLatitude() < ProjectionConstants.WEB_MERCATOR_MIN_LAT_RANGE) {
			bounded.setMaxLatitude(
					ProjectionConstants.WEB_MERCATOR_MIN_LAT_RANGE);
		}
		if (bounded
				.getMaxLatitude() > ProjectionConstants.WEB_MERCATOR_MAX_LAT_RANGE) {
			bounded.setMaxLatitude(
					ProjectionConstants.WEB_MERCATOR_MAX_LAT_RANGE);
		}
		if (bounded
				.getMinLatitude() > ProjectionConstants.WEB_MERCATOR_MAX_LAT_RANGE) {
			bounded.setMinLatitude(
					ProjectionConstants.WEB_MERCATOR_MAX_LAT_RANGE);
		}
		return bounded;
	}

	/**
	 * Get the WGS84 tile grid for the point specified as WGS84
	 * 
	 * @param point
	 *            point
	 * @param zoom
	 *            zoom level
	 * @return tile grid
	 * @since 6.2.0
	 */
	public static TileGrid getTileGridWGS84FromWGS84(Point point, long zoom) {
		BoundingBox boundingBox = new BoundingBox(point.getX(), point.getY(),
				point.getX(), point.getY());
		return getTileGridWGS84(boundingBox, zoom);
	}

	/**
	 * Get the WGS84 tile grid for the point specified as the projection
	 * 
	 * @param point
	 *            point
	 * @param zoom
	 *            zoom level
	 * @param projection
	 *            projection
	 * @return tile grid
	 * @since 6.2.0
	 */
	public static TileGrid getTileGridWGS84(Point point, long zoom,
			Projection projection) {
		GeometryTransform toWGS84 = GeometryTransform.create(projection,
				ProjectionConstants.EPSG_WORLD_GEODETIC_SYSTEM);
		Point wgs84Point = toWGS84.transform(point);
		return getTileGridWGS84FromWGS84(wgs84Point, zoom);
	}

	/**
	 * Get the WGS84 tile grid for the point specified as web mercator
	 * 
	 * @param point
	 *            point
	 * @param zoom
	 *            zoom level
	 * @return tile grid
	 * @since 6.2.0
	 */
	public static TileGrid getTileGridWGS84FromWebMercator(Point point,
			long zoom) {
		Projection projection = ProjectionFactory
				.getProjection(ProjectionConstants.EPSG_WEB_MERCATOR);
		return getTileGridWGS84(point, zoom, projection);
	}

	/**
	 * Get the WGS84 tile grid that includes the entire tile bounding box
	 *
	 * @param boundingBox
	 *            wgs84 bounding box
	 * @param zoom
	 *            zoom level
	 *
	 * @return tile grid
	 * @since 1.2.0
	 */
	public static TileGrid getTileGridWGS84(BoundingBox boundingBox,
			long zoom) {

		int tilesPerLat = tilesPerWGS84LatSide(zoom);
		int tilesPerLon = tilesPerWGS84LonSide(zoom);

		double tileSizeLat = tileSizeLatPerWGS84Side(tilesPerLat);
		double tileSizeLon = tileSizeLonPerWGS84Side(tilesPerLon);

		int minX = (int) ((boundingBox.getMinLongitude()
				+ ProjectionConstants.WGS84_HALF_WORLD_LON_WIDTH)
				/ tileSizeLon);
		double tempMaxX = (boundingBox.getMaxLongitude()
				+ ProjectionConstants.WGS84_HALF_WORLD_LON_WIDTH) / tileSizeLon;
		int maxX = (int) tempMaxX;
		if (tempMaxX % 1 == 0) {
			maxX--;
		}
		maxX = Math.min(maxX, tilesPerLon - 1);

		int minY = (int) (((boundingBox.getMaxLatitude()
				- ProjectionConstants.WGS84_HALF_WORLD_LAT_HEIGHT) * -1)
				/ tileSizeLat);
		double tempMaxY = ((boundingBox.getMinLatitude()
				- ProjectionConstants.WGS84_HALF_WORLD_LAT_HEIGHT) * -1)
				/ tileSizeLat;
		int maxY = (int) tempMaxY;
		if (tempMaxY % 1 == 0) {
			maxY--;
		}
		maxY = Math.min(maxY, tilesPerLat - 1);

		TileGrid grid = new TileGrid(minX, minY, maxX, maxY);

		return grid;
	}

	/**
	 * Get the WGS84 tile bounding box from the WGS84 XYZ tile coordinates and
	 * zoom level
	 *
	 * @param x
	 *            x coordinate
	 * @param y
	 *            y coordinate
	 * @param zoom
	 *            zoom level
	 * @return bounding box
	 * @since 6.0.1
	 */
	public static BoundingBox getWGS84BoundingBox(long x, long y, long zoom) {
		return getWGS84BoundingBox(new TileGrid(x, y, x, y), zoom);
	}

	/**
	 * Get the WGS84 tile bounding box from the WGS84 tile grid and zoom level
	 *
	 * @param tileGrid
	 *            tile grid
	 * @param zoom
	 *            zoom
	 *
	 * @return wgs84 bounding box
	 * @since 1.2.0
	 */
	public static BoundingBox getWGS84BoundingBox(TileGrid tileGrid,
			long zoom) {

		int tilesPerLat = tilesPerWGS84LatSide(zoom);
		int tilesPerLon = tilesPerWGS84LonSide(zoom);

		double tileSizeLat = tileSizeLatPerWGS84Side(tilesPerLat);
		double tileSizeLon = tileSizeLonPerWGS84Side(tilesPerLon);

		double minLon = (-1 * ProjectionConstants.WGS84_HALF_WORLD_LON_WIDTH)
				+ (tileGrid.getMinX() * tileSizeLon);
		double maxLon = (-1 * ProjectionConstants.WGS84_HALF_WORLD_LON_WIDTH)
				+ ((tileGrid.getMaxX() + 1) * tileSizeLon);
		double minLat = ProjectionConstants.WGS84_HALF_WORLD_LAT_HEIGHT
				- ((tileGrid.getMaxY() + 1) * tileSizeLat);
		double maxLat = ProjectionConstants.WGS84_HALF_WORLD_LAT_HEIGHT
				- (tileGrid.getMinY() * tileSizeLat);

		BoundingBox box = new BoundingBox(minLon, minLat, maxLon, maxLat);

		return box;
	}

	/**
	 * Get the tiles per latitude side at the zoom level
	 *
	 * @param zoom
	 *            zoom level
	 *
	 * @return tiles per latitude side
	 * @since 1.2.0
	 */
	public static int tilesPerWGS84LatSide(long zoom) {
		return tilesPerSide(zoom);
	}

	/**
	 * Get the tiles per longitude side at the zoom level
	 *
	 * @param zoom
	 *            zoom level
	 *
	 * @return tiles per longitude side
	 * @since 1.2.0
	 */
	public static int tilesPerWGS84LonSide(long zoom) {
		return 2 * tilesPerSide(zoom);
	}

	/**
	 * Get the tile height in degrees latitude
	 *
	 * @param tilesPerLat
	 *            tiles per latitude side
	 *
	 * @return degrees
	 * @since 1.2.0
	 */
	public static double tileSizeLatPerWGS84Side(int tilesPerLat) {
		return (2 * ProjectionConstants.WGS84_HALF_WORLD_LAT_HEIGHT)
				/ tilesPerLat;
	}

	/**
	 * Get the tile height in degrees longitude
	 *
	 * @param tilesPerLon
	 *            tiles per longitude side
	 *
	 * @return degrees
	 * @since 1.2.0
	 */
	public static double tileSizeLonPerWGS84Side(int tilesPerLon) {
		return (2 * ProjectionConstants.WGS84_HALF_WORLD_LON_WIDTH)
				/ tilesPerLon;
	}

	/**
	 * Get the tile grid starting from the tile grid and current zoom to the new
	 * zoom level
	 * 
	 * @param tileGrid
	 *            current tile grid
	 * @param fromZoom
	 *            current zoom level
	 * @param toZoom
	 *            new zoom level
	 * @return tile grid at new zoom level
	 * @since 2.0.1
	 */
	public static TileGrid tileGridZoom(TileGrid tileGrid, long fromZoom,
			long toZoom) {

		TileGrid newTileGrid = null;

		long zoomChange = toZoom - fromZoom;
		if (zoomChange > 0) {
			newTileGrid = tileGridZoomIncrease(tileGrid, zoomChange);
		} else if (zoomChange < 0) {
			zoomChange = Math.abs(zoomChange);
			newTileGrid = tileGridZoomDecrease(tileGrid, zoomChange);
		} else {
			newTileGrid = tileGrid;
		}

		return newTileGrid;
	}

	/**
	 * Get the tile grid starting from the tile grid and zooming in / increasing
	 * the number of levels
	 * 
	 * @param tileGrid
	 *            current tile grid
	 * @param zoomLevels
	 *            number of zoom levels to increase by
	 * @return tile grid at new zoom level
	 * @since 2.0.1
	 */
	public static TileGrid tileGridZoomIncrease(TileGrid tileGrid,
			long zoomLevels) {
		long minX = tileGridMinZoomIncrease(tileGrid.getMinX(), zoomLevels);
		long maxX = tileGridMaxZoomIncrease(tileGrid.getMaxX(), zoomLevels);
		long minY = tileGridMinZoomIncrease(tileGrid.getMinY(), zoomLevels);
		long maxY = tileGridMaxZoomIncrease(tileGrid.getMaxY(), zoomLevels);
		TileGrid newTileGrid = new TileGrid(minX, minY, maxX, maxY);
		return newTileGrid;
	}

	/**
	 * Get the tile grid starting from the tile grid and zooming out /
	 * decreasing the number of levels
	 * 
	 * @param tileGrid
	 *            current tile grid
	 * @param zoomLevels
	 *            number of zoom levels to decrease by
	 * @return tile grid at new zoom level
	 * @since 2.0.1
	 */
	public static TileGrid tileGridZoomDecrease(TileGrid tileGrid,
			long zoomLevels) {
		long minX = tileGridMinZoomDecrease(tileGrid.getMinX(), zoomLevels);
		long maxX = tileGridMaxZoomDecrease(tileGrid.getMaxX(), zoomLevels);
		long minY = tileGridMinZoomDecrease(tileGrid.getMinY(), zoomLevels);
		long maxY = tileGridMaxZoomDecrease(tileGrid.getMaxY(), zoomLevels);
		TileGrid newTileGrid = new TileGrid(minX, minY, maxX, maxY);
		return newTileGrid;
	}

	/**
	 * Get the new tile grid min value starting from the tile grid min and
	 * zooming in / increasing the number of levels
	 * 
	 * @param min
	 *            tile grid min value
	 * @param zoomLevels
	 *            number of zoom levels to increase by
	 * @return tile grid min value at new zoom level
	 * @since 2.0.1
	 */
	public static long tileGridMinZoomIncrease(long min, long zoomLevels) {
		return min * (long) Math.pow(2, zoomLevels);
	}

	/**
	 * Get the new tile grid max value starting from the tile grid max and
	 * zooming in / increasing the number of levels
	 * 
	 * @param max
	 *            tile grid max value
	 * @param zoomLevels
	 *            number of zoom levels to increase by
	 * @return tile grid max value at new zoom level
	 * @since 2.0.1
	 */
	public static long tileGridMaxZoomIncrease(long max, long zoomLevels) {
		return (max + 1) * (long) Math.pow(2, zoomLevels) - 1;
	}

	/**
	 * Get the new tile grid min value starting from the tile grid min and
	 * zooming out / decreasing the number of levels
	 * 
	 * @param min
	 *            tile grid min value
	 * @param zoomLevels
	 *            number of zoom levels to decrease by
	 * @return tile grid min value at new zoom level
	 * @since 2.0.1
	 */
	public static long tileGridMinZoomDecrease(long min, long zoomLevels) {
		return (long) Math.floor(min / Math.pow(2, zoomLevels));
	}

	/**
	 * Get the new tile grid max value starting from the tile grid max and
	 * zooming out / decreasing the number of levels
	 * 
	 * @param max
	 *            tile grid max value
	 * @param zoomLevels
	 *            number of zoom levels to decrease by
	 * @return tile grid max value at new zoom level
	 * @since 2.0.1
	 */
	public static long tileGridMaxZoomDecrease(long max, long zoomLevels) {
		return (long) Math.ceil((max + 1) / Math.pow(2, zoomLevels) - 1);
	}

}
