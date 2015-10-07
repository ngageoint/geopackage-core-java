package mil.nga.geopackage.tiles;

import mil.nga.geopackage.BoundingBox;
import mil.nga.geopackage.projection.Projection;
import mil.nga.geopackage.projection.ProjectionConstants;
import mil.nga.geopackage.projection.ProjectionFactory;
import mil.nga.geopackage.projection.ProjectionTransform;
import mil.nga.geopackage.tiles.matrix.TileMatrix;
import mil.nga.wkb.geom.Point;

/**
 * Tile Bounding Box utility methods
 *
 * @author osbornb
 */
public class TileBoundingBoxUtils {

	/**
	 * Web mercator projection
	 */
	private static Projection webMercator = ProjectionFactory
			.getProjection(ProjectionConstants.EPSG_WEB_MERCATOR);

	/**
	 * Get the overlapping bounding box between the two bounding boxes
	 *
	 * @param boundingBox
	 * @param boundingBox2
	 * @return
	 */
	public static BoundingBox overlap(BoundingBox boundingBox,
			BoundingBox boundingBox2) {

		double minLongitude = Math.max(boundingBox.getMinLongitude(),
				boundingBox2.getMinLongitude());
		double maxLongitude = Math.min(boundingBox.getMaxLongitude(),
				boundingBox2.getMaxLongitude());
		double minLatitude = Math.max(boundingBox.getMinLatitude(),
				boundingBox2.getMinLatitude());
		double maxLatitude = Math.min(boundingBox.getMaxLatitude(),
				boundingBox2.getMaxLatitude());

		BoundingBox overlap = null;

		if (minLongitude < maxLongitude && minLatitude < maxLatitude) {
			overlap = new BoundingBox(minLongitude, maxLongitude, minLatitude,
					maxLatitude);
		}

		return overlap;
	}

	/**
	 * Get the union bounding box combining the two bounding boxes
	 *
	 * @param boundingBox
	 * @param boundingBox2
	 * @return
	 */
	public static BoundingBox union(BoundingBox boundingBox,
			BoundingBox boundingBox2) {

		double minLongitude = Math.min(boundingBox.getMinLongitude(),
				boundingBox2.getMinLongitude());
		double maxLongitude = Math.max(boundingBox.getMaxLongitude(),
				boundingBox2.getMaxLongitude());
		double minLatitude = Math.min(boundingBox.getMinLatitude(),
				boundingBox2.getMinLatitude());
		double maxLatitude = Math.max(boundingBox.getMaxLatitude(),
				boundingBox2.getMaxLatitude());

		BoundingBox union = null;

		if (minLongitude < maxLongitude && minLatitude < maxLatitude) {
			union = new BoundingBox(minLongitude, maxLongitude, minLatitude,
					maxLatitude);
		}

		return union;
	}

	/**
	 * Get the X pixel for where the longitude fits into the bounding box
	 *
	 * @param width
	 * @param boundingBox
	 * @param longitude
	 * @return
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
	 * @param boundingBox
	 * @param pixel
	 * @return
	 */
	public static double getLongitudeFromPixel(long width,
			BoundingBox boundingBox, float pixel) {

		double boxWidth = boundingBox.getMaxLongitude()
				- boundingBox.getMinLongitude();
		double percentage = pixel / width;
		double offset = percentage * boxWidth;
		double longitude = offset + boundingBox.getMinLongitude();

		return longitude;
	}

	/**
	 * Get the Y pixel for where the latitude fits into the bounding box
	 *
	 * @param height
	 * @param boundingBox
	 * @param tileRowBoundingBox
	 * @return
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
	 * @param boundingBox
	 * @param pixel
	 * @return
	 */
	public static double getLatitudeFromPixel(long height,
			BoundingBox boundingBox, float pixel) {

		double boxHeight = boundingBox.getMaxLatitude()
				- boundingBox.getMinLatitude();
		double percentage = pixel / height;
		double offset = percentage * boxHeight;
		double latitude = boundingBox.getMaxLatitude() - offset;

		return latitude;
	}

	/**
	 * Get the tile bounding box from the Google Maps API tile coordinates and
	 * zoom level
	 *
	 * @param x
	 * @param y
	 * @param zoom
	 * @return
	 */
	public static BoundingBox getBoundingBox(int x, int y, int zoom) {

		int tilesPerSide = tilesPerSide(zoom);
		double tileWidthDegrees = tileWidthDegrees(tilesPerSide);
		double tileHeightDegrees = tileHeightDegrees(tilesPerSide);

		double minLon = -180.0 + (x * tileWidthDegrees);
		double maxLon = minLon + tileWidthDegrees;

		double maxLat = 90.0 - (y * tileHeightDegrees);
		double minLat = maxLat - tileHeightDegrees;

		BoundingBox box = new BoundingBox(minLon, maxLon, minLat, maxLat);

		return box;
	}

	/**
	 * Get the Web Mercator tile bounding box from the Google Maps API tile
	 * coordinates and zoom level
	 *
	 * @param x
	 * @param y
	 * @param zoom
	 * @return
	 */
	public static BoundingBox getWebMercatorBoundingBox(long x, long y, int zoom) {

		int tilesPerSide = tilesPerSide(zoom);
		double tileSize = tileSize(tilesPerSide);

		double minLon = (-1 * ProjectionConstants.WEB_MERCATOR_HALF_WORLD_WIDTH)
				+ (x * tileSize);
		double maxLon = (-1 * ProjectionConstants.WEB_MERCATOR_HALF_WORLD_WIDTH)
				+ ((x + 1) * tileSize);
		double minLat = ProjectionConstants.WEB_MERCATOR_HALF_WORLD_WIDTH
				- ((y + 1) * tileSize);
		double maxLat = ProjectionConstants.WEB_MERCATOR_HALF_WORLD_WIDTH
				- (y * tileSize);

		BoundingBox box = new BoundingBox(minLon, maxLon, minLat, maxLat);

		return box;
	}

	/**
	 * Get the Web Mercator tile bounding box from the Google Maps API tile grid
	 * and zoom level
	 *
	 * @param x
	 * @param y
	 * @param zoom
	 * @return
	 */
	public static BoundingBox getWebMercatorBoundingBox(TileGrid tileGrid,
			int zoom) {

		int tilesPerSide = tilesPerSide(zoom);
		double tileSize = tileSize(tilesPerSide);

		double minLon = (-1 * ProjectionConstants.WEB_MERCATOR_HALF_WORLD_WIDTH)
				+ (tileGrid.getMinX() * tileSize);
		double maxLon = (-1 * ProjectionConstants.WEB_MERCATOR_HALF_WORLD_WIDTH)
				+ ((tileGrid.getMaxX() + 1) * tileSize);
		double minLat = ProjectionConstants.WEB_MERCATOR_HALF_WORLD_WIDTH
				- ((tileGrid.getMaxY() + 1) * tileSize);
		double maxLat = ProjectionConstants.WEB_MERCATOR_HALF_WORLD_WIDTH
				- (tileGrid.getMinY() * tileSize);

		BoundingBox box = new BoundingBox(minLon, maxLon, minLat, maxLat);

		return box;
	}

	/**
	 * Get the Projected tile bounding box from the Google Maps API tile
	 * coordinates and zoom level
	 *
	 * @param projectionEpsg
	 * @param x
	 * @param y
	 * @param zoom
	 * @return
	 */
	public static BoundingBox getProjectedBoundingBox(Long projectionEpsg,
			int x, int y, int zoom) {

		BoundingBox boundingBox = getWebMercatorBoundingBox(x, y, zoom);

		if (projectionEpsg != null) {
			ProjectionTransform transform = webMercator
					.getTransformation(projectionEpsg);
			boundingBox = transform.transform(boundingBox);
		}

		return boundingBox;
	}

	/**
	 * Get the Projected tile bounding box from the Google Maps API tile
	 * coordinates and zoom level
	 *
	 * @param projection
	 * @param x
	 * @param y
	 * @param zoom
	 * @return
	 */
	public static BoundingBox getProjectedBoundingBox(Projection projection,
			long x, long y, int zoom) {

		BoundingBox boundingBox = getWebMercatorBoundingBox(x, y, zoom);

		if (projection != null) {
			ProjectionTransform transform = webMercator
					.getTransformation(projection);
			boundingBox = transform.transform(boundingBox);
		}

		return boundingBox;
	}

	/**
	 * Get the Projected tile bounding box from the Google Maps API tile
	 * tileGrid and zoom level
	 *
	 * @param projectionEpsg
	 * @param tileGrid
	 * @param zoom
	 * @return
	 */
	public static BoundingBox getProjectedBoundingBox(Long projectionEpsg,
			TileGrid tileGrid, int zoom) {

		BoundingBox boundingBox = getWebMercatorBoundingBox(tileGrid, zoom);

		if (projectionEpsg != null) {
			ProjectionTransform transform = webMercator
					.getTransformation(projectionEpsg);
			boundingBox = transform.transform(boundingBox);
		}

		return boundingBox;
	}

	/**
	 * Get the Projected tile bounding box from the Google Maps API tile grid
	 * and zoom level
	 *
	 * @param projection
	 * @param tileGrid
	 * @param zoom
	 * @return
	 */
	public static BoundingBox getProjectedBoundingBox(Projection projection,
			TileGrid tileGrid, int zoom) {

		BoundingBox boundingBox = getWebMercatorBoundingBox(tileGrid, zoom);

		if (projection != null) {
			ProjectionTransform transform = webMercator
					.getTransformation(projection);
			boundingBox = transform.transform(boundingBox);
		}

		return boundingBox;
	}

	/**
	 * Get the tile grid for the location specified as WGS84
	 * 
	 * @param point
	 * @param zoom
	 * @return tile grid
	 * @since 1.1.0
	 */
	public static TileGrid getTileGridFromWGS84(Point point, int zoom) {
		Projection projection = ProjectionFactory
				.getProjection(ProjectionConstants.EPSG_WORLD_GEODETIC_SYSTEM);
		return getTileGrid(point, zoom, projection);
	}

	/**
	 * Get the tile grid for the location specified as the projection
	 * 
	 * @param point
	 * @param zoom
	 * @param projection
	 * @return tile grid
	 * @since 1.1.0
	 */
	public static TileGrid getTileGrid(Point point, int zoom,
			Projection projection) {
		ProjectionTransform toWebMercator = projection
				.getTransformation(ProjectionConstants.EPSG_WEB_MERCATOR);
		Point webMercatorPoint = toWebMercator.transform(point);
		BoundingBox boundingBox = new BoundingBox(webMercatorPoint.getX(),
				webMercatorPoint.getX(), webMercatorPoint.getY(),
				webMercatorPoint.getY());
		return getTileGrid(boundingBox, zoom);
	}

	/**
	 * Get the tile grid that includes the entire tile bounding box
	 *
	 * @param webMercatorBoundingBox
	 * @param zoom
	 * @return
	 */
	public static TileGrid getTileGrid(BoundingBox webMercatorBoundingBox,
			int zoom) {

		int tilesPerSide = tilesPerSide(zoom);
		double tileSize = tileSize(tilesPerSide);

		int minX = (int) ((webMercatorBoundingBox.getMinLongitude() + ProjectionConstants.WEB_MERCATOR_HALF_WORLD_WIDTH) / tileSize);
		double tempMaxX = (webMercatorBoundingBox.getMaxLongitude() + ProjectionConstants.WEB_MERCATOR_HALF_WORLD_WIDTH)
				/ tileSize;
		int maxX = (int) (tempMaxX - ProjectionConstants.WEB_MERCATOR_PRECISION);
		maxX = Math.min(maxX, tilesPerSide - 1);

		int minY = (int) (((webMercatorBoundingBox.getMaxLatitude() - ProjectionConstants.WEB_MERCATOR_HALF_WORLD_WIDTH) * -1) / tileSize);
		double tempMaxY = ((webMercatorBoundingBox.getMinLatitude() - ProjectionConstants.WEB_MERCATOR_HALF_WORLD_WIDTH) * -1)
				/ tileSize;
		int maxY = (int) (tempMaxY - ProjectionConstants.WEB_MERCATOR_PRECISION);
		maxY = Math.min(maxY, tilesPerSide - 1);

		TileGrid grid = new TileGrid(minX, maxX, minY, maxY);

		return grid;
	}

	/**
	 * Convert the bounding box coordinates to a new web mercator bounding box
	 *
	 * @param boundingBox
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

		ProjectionTransform toWebMercator = ProjectionFactory.getProjection(
				ProjectionConstants.EPSG_WORLD_GEODETIC_SYSTEM)
				.getTransformation(ProjectionConstants.EPSG_WEB_MERCATOR);
		lowerLeftPoint = toWebMercator.transform(lowerLeftPoint);
		upperRightPoint = toWebMercator.transform(upperRightPoint);

		BoundingBox mercatorBox = new BoundingBox(lowerLeftPoint.getX(),
				upperRightPoint.getX(), lowerLeftPoint.getY(),
				upperRightPoint.getY());

		return mercatorBox;
	}

	/**
	 * Get the tile size in meters
	 *
	 * @param tilesPerSide
	 * @return
	 */
	public static double tileSize(int tilesPerSide) {
		return (2 * ProjectionConstants.WEB_MERCATOR_HALF_WORLD_WIDTH)
				/ tilesPerSide;
	}

	/**
	 * Get the tile width in degrees
	 *
	 * @param tilesPerSide
	 * @return
	 */
	public static double tileWidthDegrees(int tilesPerSide) {
		return 360.0 / tilesPerSide;
	}

	/**
	 * Get the tile height in degrees
	 *
	 * @param tilesPerSide
	 * @return
	 */
	public static double tileHeightDegrees(int tilesPerSide) {
		return 180.0 / tilesPerSide;
	}

	/**
	 * Get the tiles per side, width and height, at the zoom level
	 *
	 * @param zoom
	 * @return
	 */
	public static int tilesPerSide(int zoom) {
		return (int) Math.pow(2, zoom);
	}

	/**
	 * Get the standard y tile location as TMS or a TMS y location as standard
	 * 
	 * @param zoom
	 * @param y
	 * @return
	 */
	public static int getYAsOppositeTileFormat(int zoom, int y) {
		int tilesPerSide = tilesPerSide(zoom);
		int oppositeY = tilesPerSide - y - 1;
		return oppositeY;
	}

	/**
	 * Get the zoom level from the tiles per side
	 *
	 * @param tilesPerSide
	 * @return
	 */
	public static int zoomFromTilesPerSide(int tilesPerSide) {
		return (int) (Math.log(tilesPerSide) / Math.log(2));
	}

	/**
	 * Get the tile grid
	 *
	 * @param webMercatorTotalBox
	 * @param matrixWidth
	 * @param matrixHeight
	 * @param webMercatorBoundingBox
	 * @return
	 */
	public static TileGrid getTileGrid(BoundingBox webMercatorTotalBox,
			long matrixWidth, long matrixHeight,
			BoundingBox webMercatorBoundingBox) {

		long minColumn = getTileColumn(webMercatorTotalBox, matrixWidth,
				webMercatorBoundingBox.getMinLongitude());
		long maxColumn = getTileColumn(webMercatorTotalBox, matrixWidth,
				webMercatorBoundingBox.getMaxLongitude());

		if (minColumn < matrixWidth && maxColumn >= 0) {
			if (minColumn < 0) {
				minColumn = 0;
			}
			if (maxColumn >= matrixWidth) {
				maxColumn = matrixWidth - 1;
			}
		}

		long maxRow = getTileRow(webMercatorTotalBox, matrixHeight,
				webMercatorBoundingBox.getMinLatitude());
		long minRow = getTileRow(webMercatorTotalBox, matrixHeight,
				webMercatorBoundingBox.getMaxLatitude());

		if (minRow < matrixHeight && maxRow >= 0) {
			if (minRow < 0) {
				minRow = 0;
			}
			if (maxRow >= matrixHeight) {
				maxRow = matrixHeight - 1;
			}
		}

		TileGrid tileGrid = new TileGrid(minColumn, maxColumn, minRow, maxRow);

		return tileGrid;
	}

	/**
	 * Get the tile column of the longitude in degrees
	 *
	 * @param webMercatorTotalBox
	 * @param matrixWidth
	 * @param longitude
	 *            in meters
	 * @return tile column if in the range, -1 if before,
	 *         {@link TileMatrix#getMatrixWidth()} if after
	 */
	public static long getTileColumn(BoundingBox webMercatorTotalBox,
			long matrixWidth, double longitude) {

		double minX = webMercatorTotalBox.getMinLongitude();
		double maxX = webMercatorTotalBox.getMaxLongitude();

		long tileId;
		if (longitude < minX) {
			tileId = -1;
		} else if (longitude >= maxX) {
			tileId = matrixWidth;
		} else {
			double matrixWidthMeters = webMercatorTotalBox.getMaxLongitude()
					- webMercatorTotalBox.getMinLongitude();
			double tileWidth = matrixWidthMeters / matrixWidth;
			tileId = (long) ((longitude - minX) / tileWidth);
		}

		return tileId;
	}

	/**
	 * Get the tile row of the latitude in degrees
	 *
	 * @param webMercatorTotalBox
	 * @param matrixHeight
	 * @param latitude
	 *            in meters
	 * @return tile row if in the range, -1 if before,
	 *         {@link TileMatrix#getMatrixHeight()} if after
	 */
	public static long getTileRow(BoundingBox webMercatorTotalBox,
			long matrixHeight, double latitude) {

		double minY = webMercatorTotalBox.getMinLatitude();
		double maxY = webMercatorTotalBox.getMaxLatitude();

		long tileId;
		if (latitude <= minY) {
			tileId = matrixHeight;
		} else if (latitude > maxY) {
			tileId = -1;
		} else {
			double matrixHeightMeters = webMercatorTotalBox.getMaxLatitude()
					- webMercatorTotalBox.getMinLatitude();
			double tileHeight = matrixHeightMeters / matrixHeight;
			tileId = (long) ((maxY - latitude) / tileHeight);
		}

		return tileId;
	}

	/**
	 * Get the web mercator bounding box of the Tile Row from the Tile Matrix
	 * zoom level
	 *
	 * @param webMercatorTotalBox
	 * @param tileMatrix
	 * @param tileColumn
	 * @param tileRow
	 * @return
	 */
	public static BoundingBox getWebMercatorBoundingBox(
			BoundingBox webMercatorTotalBox, TileMatrix tileMatrix,
			long tileColumn, long tileRow) {

		return getWebMercatorBoundingBox(webMercatorTotalBox,
				tileMatrix.getMatrixWidth(), tileMatrix.getMatrixHeight(),
				tileColumn, tileRow);
	}

	/**
	 * Get the web mercator bounding box of the Tile Row from the Tile Matrix
	 * zoom level
	 *
	 * @param webMercatorTotalBox
	 * @param matrixWidth
	 * @param matrixHeight
	 * @param tileColumn
	 * @param tileRow
	 * @return
	 */
	public static BoundingBox getWebMercatorBoundingBox(
			BoundingBox webMercatorTotalBox, long tileMatrixWidth,
			long tileMatrixHeight, long tileColumn, long tileRow) {

		// Get the tile width
		double matrixMinX = webMercatorTotalBox.getMinLongitude();
		double matrixMaxX = webMercatorTotalBox.getMaxLongitude();
		double matrixWidth = matrixMaxX - matrixMinX;
		double tileWidth = matrixWidth / tileMatrixWidth;

		// Find the longitude range
		double minLon = matrixMinX + (tileWidth * tileColumn);
		double maxLon = minLon + tileWidth;

		// Get the tile height
		double matrixMinY = webMercatorTotalBox.getMinLatitude();
		double matrixMaxY = webMercatorTotalBox.getMaxLatitude();
		double matrixHeight = matrixMaxY - matrixMinY;
		double tileHeight = matrixHeight / tileMatrixHeight;

		// Find the latitude range
		double maxLat = matrixMaxY - (tileHeight * tileRow);
		double minLat = maxLat - tileHeight;

		BoundingBox boundingBox = new BoundingBox(minLon, maxLon, minLat,
				maxLat);

		return boundingBox;
	}

	/**
	 * Get the zoom level of where the web mercator bounding box fits into the
	 * complete world
	 *
	 * @param webMercatorBoundingBox
	 * @return zoom level
	 */
	public static int getZoomLevel(BoundingBox webMercatorBoundingBox) {

		double worldLength = ProjectionConstants.WEB_MERCATOR_HALF_WORLD_WIDTH * 2;

		int widthTiles = (int) (worldLength / (webMercatorBoundingBox
				.getMaxLongitude() - webMercatorBoundingBox.getMinLongitude()));
		int heightTiles = (int) (worldLength / (webMercatorBoundingBox
				.getMaxLatitude() - webMercatorBoundingBox.getMinLatitude()));

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
	 * @param matrixWidth
	 * @param tileWidth
	 * @return
	 */
	public static double getPixelXSize(BoundingBox webMercatorBoundingBox,
			long matrixWidth, int tileWidth) {
		double pixelXSize = (webMercatorBoundingBox.getMaxLongitude() - webMercatorBoundingBox
				.getMinLongitude()) / matrixWidth / tileWidth;
		return pixelXSize;
	}

	/**
	 * Get the pixel y size for the bounding box with matrix height and tile
	 * height
	 * 
	 * @param webMercatorBoundingBox
	 * @param matrixHeight
	 * @param tileHeight
	 * @return
	 */
	public static double getPixelYSize(BoundingBox webMercatorBoundingBox,
			long matrixHeight, int tileHeight) {
		double pixelYSize = (webMercatorBoundingBox.getMaxLatitude() - webMercatorBoundingBox
				.getMinLatitude()) / matrixHeight / tileHeight;
		return pixelYSize;
	}

}
