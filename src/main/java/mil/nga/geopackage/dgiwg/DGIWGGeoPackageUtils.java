package mil.nga.geopackage.dgiwg;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TreeSet;

import mil.nga.geopackage.BoundingBox;
import mil.nga.geopackage.GeoPackageCore;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.contents.Contents;
import mil.nga.geopackage.contents.ContentsDataType;
import mil.nga.geopackage.srs.SpatialReferenceSystem;
import mil.nga.geopackage.srs.SpatialReferenceSystemDao;
import mil.nga.geopackage.tiles.matrix.TileMatrix;
import mil.nga.geopackage.tiles.matrixset.TileMatrixSet;
import mil.nga.geopackage.tiles.user.TileTable;

/**
 * DGIWG (Defence Geospatial Information Working Group) GeoPackage utilities
 * 
 * @author osbornb
 * @since 6.1.2
 */
public class DGIWGGeoPackageUtils {

	/**
	 * Create tiles table
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * @param table
	 *            table name
	 * @param identifier
	 *            contents identifier
	 * @param description
	 *            contents description
	 * @param informativeBounds
	 *            informative contents bounds
	 * @param srs
	 *            spatial reference system
	 * @param extentBounds
	 *            crs extent bounds
	 * @return created tile matrix set
	 */
	public static TileMatrixSet createTiles(GeoPackageCore geoPackage,
			String table, String identifier, String description,
			BoundingBox informativeBounds, SpatialReferenceSystem srs,
			BoundingBox extentBounds) {

		geoPackage.createTileMatrixSetTable();
		geoPackage.createTileMatrixTable();

		SpatialReferenceSystemDao srsDao = geoPackage
				.getSpatialReferenceSystemDao();
		try {
			srs = srsDao.createIfNotExists(srs);
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to create Spatial Reference System: "
							+ srs.getSrsName(),
					e);
		}

		if (informativeBounds == null) {
			informativeBounds = extentBounds;
		}

		Contents contents = new Contents();
		contents.setTableName(table);
		contents.setDataType(ContentsDataType.TILES);
		contents.setIdentifier(identifier);
		contents.setDescription(description);
		contents.setMinX(informativeBounds.getMinLongitude());
		contents.setMinY(informativeBounds.getMinLatitude());
		contents.setMaxX(informativeBounds.getMaxLongitude());
		contents.setMaxY(informativeBounds.getMaxLatitude());
		contents.setSrs(srs);

		TileTable tileTable = new TileTable(table);
		geoPackage.createTileTable(tileTable);

		try {
			geoPackage.getContentsDao().create(contents);
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to create Contents: " + contents.getTableName(), e);
		}

		TileMatrixSet tileMatrixSet = new TileMatrixSet();
		tileMatrixSet.setContents(contents);
		tileMatrixSet.setSrs(contents.getSrs());
		tileMatrixSet.setMinX(extentBounds.getMinLongitude());
		tileMatrixSet.setMinY(extentBounds.getMinLatitude());
		tileMatrixSet.setMaxX(extentBounds.getMaxLongitude());
		tileMatrixSet.setMaxY(extentBounds.getMaxLatitude());

		try {
			geoPackage.getTileMatrixSetDao().create(tileMatrixSet);
		} catch (SQLException e) {
			throw new GeoPackageException("Failed to create Tile Matrix Set: "
					+ tileMatrixSet.getTableName(), e);
		}

		return tileMatrixSet;
	}

	/**
	 * Create a tile matrix for a zoom level
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * @param table
	 *            table name
	 * @param boundingBox
	 *            bounding box
	 * @param minZoom
	 *            min zoom level
	 * @param maxZoom
	 *            max zoom level
	 * @param matrixWidth
	 *            matrix width
	 * @param matrixHeight
	 *            matrix height
	 */
	public static void createTileMatrices(GeoPackageCore geoPackage,
			String table, BoundingBox boundingBox, long minZoom, long maxZoom,
			long matrixWidth, long matrixHeight) {
		List<Long> zoomLevels = new ArrayList<>();
		for (long zoom = minZoom; zoom <= maxZoom; zoom++) {
			zoomLevels.add(zoom);
		}
		createTileMatrices(geoPackage, table, boundingBox, zoomLevels,
				matrixWidth, matrixHeight);
	}

	/**
	 * Create a tile matrix for a zoom level
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * @param table
	 *            table name
	 * @param boundingBox
	 *            bounding box
	 * @param zoomLevels
	 *            zoom levels
	 * @param matrixWidth
	 *            matrix width
	 * @param matrixHeight
	 *            matrix height
	 */
	public static void createTileMatrices(GeoPackageCore geoPackage,
			String table, BoundingBox boundingBox, Collection<Long> zoomLevels,
			long matrixWidth, long matrixHeight) {

		TreeSet<Long> zooms = new TreeSet<>(zoomLevels);

		long minZoom = zooms.first();
		long maxZoom = zooms.last();

		for (long zoom = minZoom; zoom <= maxZoom; zoom++) {

			if (zooms.contains(zoom)) {
				createTileMatrices(geoPackage, table, boundingBox, zoom,
						matrixWidth, matrixHeight);
			}

			matrixWidth *= 2;
			matrixHeight *= 2;

		}

	}

	/**
	 * Create a tile matrix for a zoom level
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * @param table
	 *            table name
	 * @param boundingBox
	 *            bounding box
	 * @param zoom
	 *            zoom level
	 * @param matrixWidth
	 *            matrix width
	 * @param matrixHeight
	 *            matrix height
	 */
	public static void createTileMatrices(GeoPackageCore geoPackage,
			String table, BoundingBox boundingBox, long zoom, long matrixWidth,
			long matrixHeight) {

		double pixelXSize = boundingBox.getLongitudeRange() / matrixWidth
				/ DGIWGConstants.TILE_WIDTH;
		double pixelYSize = boundingBox.getLatitudeRange() / matrixHeight
				/ DGIWGConstants.TILE_HEIGHT;

		createTileMatrix(geoPackage, table, zoom, matrixWidth, matrixHeight,
				pixelXSize, pixelYSize);
	}

	/**
	 * Create a tile matrix for a zoom level
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * @param table
	 *            table name
	 * @param zoom
	 *            zoom level
	 * @param matrixWidth
	 *            matrix width
	 * @param matrixHeight
	 *            matrix height
	 * @param pixelXSize
	 *            pixel x size
	 * @param pixelYSize
	 *            pixel y size
	 */
	public static void createTileMatrix(GeoPackageCore geoPackage, String table,
			long zoom, long matrixWidth, long matrixHeight, double pixelXSize,
			double pixelYSize) {

		if (zoom < 0) {
			throw new GeoPackageException(
					"Illegal negative zoom level: " + zoom);
		}

		Contents contents = geoPackage.getTableContents(table);
		if (contents == null) {
			throw new GeoPackageException(
					"Failed to retrieve Contents for table: " + table);
		}

		TileMatrix tileMatrix = new TileMatrix();
		tileMatrix.setContents(contents);
		tileMatrix.setZoomLevel(zoom);
		tileMatrix.setMatrixWidth(matrixWidth);
		tileMatrix.setMatrixHeight(matrixHeight);
		tileMatrix.setTileWidth(DGIWGConstants.TILE_WIDTH);
		tileMatrix.setTileHeight(DGIWGConstants.TILE_HEIGHT);
		tileMatrix.setPixelXSize(pixelXSize);
		tileMatrix.setPixelYSize(pixelYSize);

		try {
			geoPackage.getTileMatrixDao().create(tileMatrix);
		} catch (SQLException e) {
			throw new GeoPackageException("Failed to create Tile Matrix: "
					+ tileMatrix.getTableName(), e);
		}

	}

}
