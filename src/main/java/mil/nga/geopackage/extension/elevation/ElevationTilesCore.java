package mil.nga.geopackage.extension.elevation;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import mil.nga.geopackage.BoundingBox;
import mil.nga.geopackage.GeoPackageConstants;
import mil.nga.geopackage.GeoPackageCore;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.core.contents.ContentsDataType;
import mil.nga.geopackage.extension.BaseExtension;
import mil.nga.geopackage.extension.ExtensionScopeType;
import mil.nga.geopackage.extension.Extensions;
import mil.nga.geopackage.projection.Projection;
import mil.nga.geopackage.projection.ProjectionFactory;
import mil.nga.geopackage.projection.ProjectionTransform;
import mil.nga.geopackage.property.GeoPackageProperties;
import mil.nga.geopackage.property.PropertyConstants;
import mil.nga.geopackage.tiles.matrix.TileMatrix;
import mil.nga.geopackage.tiles.matrixset.TileMatrixSet;
import mil.nga.geopackage.tiles.user.TileTable;

import org.osgeo.proj4j.ProjCoordinate;

/**
 * Tiled Gridded Elevation Core Data Extension
 * 
 * @author osbornb
 * @since 1.2.1
 */
public class ElevationTilesCore extends BaseExtension {

	/**
	 * Extension author
	 */
	public static final String EXTENSION_AUTHOR = GeoPackageConstants.GEO_PACKAGE_EXTENSION_AUTHOR;

	/**
	 * Extension name without the author
	 */
	public static final String EXTENSION_NAME_NO_AUTHOR = "elevation_tiles";

	/**
	 * Extension, with author and name
	 */
	public static final String EXTENSION_NAME = Extensions.buildExtensionName(
			EXTENSION_AUTHOR, EXTENSION_NAME_NO_AUTHOR);

	/**
	 * Extension definition URL
	 */
	public static final String EXTENSION_DEFINITION = GeoPackageProperties
			.getProperty(PropertyConstants.EXTENSIONS, EXTENSION_NAME_NO_AUTHOR);

	/**
	 * Tile Matrix Set
	 */
	private final TileMatrixSet tileMatrixSet;

	/**
	 * Gridded Coverage DAO
	 */
	private final GriddedCoverageDao griddedCoverageDao;

	/**
	 * Gridded Tile DAO
	 */
	private final GriddedTileDao griddedTileDao;

	/**
	 * Gridded coverage
	 */
	private List<GriddedCoverage> griddedCoverage;

	/**
	 * First Gridded coverage
	 */
	private GriddedCoverage firstGriddedCoverage;

	/**
	 * Elevation results width
	 */
	protected Integer width;

	/**
	 * Elevation results height
	 */
	protected Integer height;

	/**
	 * Projection of the requests
	 */
	protected final Projection requestProjection;

	/**
	 * Projection of the elevations
	 */
	protected final Projection elevationProjection;

	/**
	 * Elevations bounding box
	 */
	protected final BoundingBox elevationBoundingBox;

	/**
	 * Flag indicating the elevation and request projections are the same
	 */
	protected final boolean sameProjection;

	/**
	 * True if zooming in should be performed to find a tile matrix with
	 * elevation values
	 */
	protected boolean zoomIn = true;

	/**
	 * True if zooming out should be performed to find a tile matrix with
	 * elevation values
	 */
	protected boolean zoomOut = true;

	/**
	 * True if zoom in in before zooming out, false to zoom out first
	 */
	protected boolean zoomInBeforeOut = true;

	/**
	 * Constructor
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * @param tileMatrixSet
	 *            tile matrix set
	 * @param width
	 *            specified results width
	 * @param height
	 *            specified results height
	 * @param requestProjection
	 *            request projection
	 */
	protected ElevationTilesCore(GeoPackageCore geoPackage,
			TileMatrixSet tileMatrixSet, Integer width, Integer height,
			Projection requestProjection) {
		super(geoPackage);
		this.tileMatrixSet = tileMatrixSet;
		griddedCoverageDao = geoPackage.getGriddedCoverageDao();
		griddedTileDao = geoPackage.getGriddedTileDao();
		queryGriddedCoverage();

		this.width = width;
		this.height = height;
		this.requestProjection = requestProjection;
		elevationProjection = ProjectionFactory.getProjection(tileMatrixSet
				.getSrs());
		elevationBoundingBox = tileMatrixSet.getBoundingBox();

		// Check if the projections have the same units
		sameProjection = (requestProjection.getUnit().name
				.equals(elevationProjection.getUnit().name));
	}

	/**
	 * Private constructor for creating extension rows when creating a new
	 * elevation tile table
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * @param tileMatrixSet
	 *            tile matrix set
	 */
	private ElevationTilesCore(GeoPackageCore geoPackage,
			TileMatrixSet tileMatrixSet) {
		super(geoPackage);
		this.tileMatrixSet = tileMatrixSet;
		griddedCoverageDao = null;
		griddedTileDao = null;
		requestProjection = null;
		elevationProjection = null;
		elevationBoundingBox = null;
		sameProjection = false;
	}

	/**
	 * Get the Tile Matrix Set
	 * 
	 * @return Tile Matrix Set
	 */
	public TileMatrixSet getTileMatrixSet() {
		return tileMatrixSet;
	}

	/**
	 * Get the Gridded Coverage DAO
	 * 
	 * @return Gridded Coverage DAO
	 */
	public GriddedCoverageDao getGriddedCoverageDao() {
		return griddedCoverageDao;
	}

	/**
	 * Get the Gridded Tile DAO
	 * 
	 * @return Gridded Tile DAO
	 */
	public GriddedTileDao getGriddedTileDao() {
		return griddedTileDao;
	}

	/**
	 * Get the requested elevation width
	 * 
	 * @return width
	 */
	public Integer getWidth() {
		return width;
	}

	/**
	 * Set the requested elevation width
	 * 
	 * @param width
	 *            requested elevation width
	 */
	public void setWidth(Integer width) {
		this.width = width;
	}

	/**
	 * Get the requested elevation height
	 * 
	 * @return height
	 */
	public Integer getHeight() {
		return height;
	}

	/**
	 * Set the requested elevation height
	 * 
	 * @param height
	 *            requested elevation height
	 */
	public void setHeight(Integer height) {
		this.height = height;
	}

	/**
	 * Get the request projection
	 * 
	 * @return request projection
	 */
	public Projection getRequestProjection() {
		return requestProjection;
	}

	/**
	 * Get the elevation projection
	 * 
	 * @return elevation projection
	 */
	public Projection getElevationProjection() {
		return elevationProjection;
	}

	/**
	 * Get the elevation bounding box
	 * 
	 * @return elevation bounding box
	 */
	public BoundingBox getElevationBoundingBox() {
		return elevationBoundingBox;
	}

	/**
	 * Is the request and elevation projection the same
	 * 
	 * @return true if the same
	 */
	public boolean isSameProjection() {
		return sameProjection;
	}

	/**
	 * Is the zooming in (higher zoom level values) enabled to find matching
	 * elevations
	 * 
	 * @return true if zoom in enabled
	 */
	public boolean isZoomIn() {
		return zoomIn;
	}

	/**
	 * Set the zoom in enabled state
	 * 
	 * @param zoomIn
	 *            true to zoom in when finding elevations, false to disable
	 */
	public void setZoomIn(boolean zoomIn) {
		this.zoomIn = zoomIn;
	}

	/**
	 * Is the zooming out (lower zoom level values) enabled to find matching
	 * elevations
	 * 
	 * @return true if zoom out enabled
	 */
	public boolean isZoomOut() {
		return zoomOut;
	}

	/**
	 * Set the zoom out enabled state
	 * 
	 * @param zoomOut
	 *            true to zoom out when finding elevations, false to disable
	 */
	public void setZoomOut(boolean zoomOut) {
		this.zoomOut = zoomOut;
	}

	/**
	 * Is zooming in (when enabled) performed before zooming out (when enabled)
	 * 
	 * @return true to zoom in for results first, false to zoom out for results
	 *         first
	 */
	public boolean isZoomInBeforeOut() {
		return zoomInBeforeOut;
	}

	/**
	 * Set the zoom order between in and out
	 * 
	 * @param zoomInBeforeOut
	 *            true to zoom in for results first, false to zoom out first
	 */
	public void setZoomInBeforeOut(boolean zoomInBeforeOut) {
		this.zoomInBeforeOut = zoomInBeforeOut;
	}

	/**
	 * Get or create the extension
	 * 
	 * @return extensions list
	 */
	public List<Extensions> getOrCreate() {

		// Create tables
		geoPackage.createGriddedCoverageTable();
		geoPackage.createGriddedTileTable();

		List<Extensions> extensionList = new ArrayList<>();

		Extensions coverage = getOrCreate(EXTENSION_NAME,
				GriddedCoverage.TABLE_NAME, null, EXTENSION_DEFINITION,
				ExtensionScopeType.READ_WRITE);
		Extensions tile = getOrCreate(EXTENSION_NAME, GriddedTile.TABLE_NAME,
				null, EXTENSION_DEFINITION, ExtensionScopeType.READ_WRITE);
		Extensions table = getOrCreate(EXTENSION_NAME,
				tileMatrixSet.getTableName(), TileTable.COLUMN_TILE_DATA,
				EXTENSION_DEFINITION, ExtensionScopeType.READ_WRITE);

		extensionList.add(coverage);
		extensionList.add(tile);
		extensionList.add(table);

		return extensionList;
	}

	/**
	 * Determine if the Tile Matrix Set has the extension
	 * 
	 * @return true if has extension
	 */
	public boolean has() {

		boolean exists = has(EXTENSION_NAME, tileMatrixSet.getTableName(),
				TileTable.COLUMN_TILE_DATA);

		return exists;
	}

	/**
	 * Get the gridded coverage
	 * 
	 * @return gridded coverage
	 */
	public List<GriddedCoverage> getGriddedCoverage() {
		return griddedCoverage;
	}

	/**
	 * Query and updated the gridded coverage
	 * 
	 * @return gridded coverage
	 */
	public List<GriddedCoverage> queryGriddedCoverage() {
		try {
			if (griddedCoverageDao.isTableExists()) {
				griddedCoverage = griddedCoverageDao.query(tileMatrixSet);
				if (!griddedCoverage.isEmpty()) {
					firstGriddedCoverage = griddedCoverage.get(0);
				}
			}
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to get Gridded Coverage for table name: "
							+ tileMatrixSet.getTableName(), e);
		}
		return griddedCoverage;
	}

	/**
	 * Get the gridded tile
	 * 
	 * @return gridded tiles
	 */
	public List<GriddedTile> getGriddedTile() {
		List<GriddedTile> griddedTile = null;
		try {
			if (griddedTileDao.isTableExists()) {
				griddedTile = griddedTileDao
						.query(tileMatrixSet.getTableName());
			}
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to get Gridded Tile for table name: "
							+ tileMatrixSet.getTableName(), e);
		}
		return griddedTile;
	}

	/**
	 * Get the gridded tile by id
	 * 
	 * @param tileId
	 *            tile id
	 * @return gridded tile
	 */
	public GriddedTile getGriddedTile(long tileId) {
		GriddedTile griddedTile = null;
		try {
			if (griddedTileDao.isTableExists()) {
				griddedTile = griddedTileDao.query(
						tileMatrixSet.getTableName(), tileId);
			}
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to get Gridded Tile for table name: "
							+ tileMatrixSet.getTableName() + ", tile id: "
							+ tileId, e);
		}
		return griddedTile;
	}

	/**
	 * Get the pixel value as an "unsigned short" at the coordinate from the
	 * "unsigned short" pixel values
	 * 
	 * @param pixelValues
	 *            "unsigned short" pixel values
	 * @param width
	 *            image width
	 * @param x
	 *            x coordinate
	 * @param y
	 *            y coordinate
	 * @return "unsigned short" pixel value
	 */
	public short getPixelValue(short[] pixelValues, int width, int x, int y) {
		return pixelValues[(y * width) + x];
	}

	/**
	 * Get the pixel value as a 16 bit unsigned value as an integer
	 * 
	 * @param pixelValues
	 *            "unsigned short" pixel values
	 * @param width
	 *            image width
	 * @param x
	 *            x coordinate
	 * @param y
	 *            y coordinate
	 * @return unsigned int pixel value
	 */
	public int getUnsignedPixelValue(short[] pixelValues, int width, int x,
			int y) {
		short pixelValue = getPixelValue(pixelValues, width, x, y);
		int unsignedPixelValue = getUnsignedPixelValue(pixelValue);
		return unsignedPixelValue;
	}

	/**
	 * Get the unsigned pixel value. The value saved as an "unsigned short" in
	 * the short is returned as an integer which stores the positive 16 bit
	 * value
	 * 
	 * @param pixelValue
	 *            "unsigned short" pixel value
	 * @return unsigned 16 bit pixel value as an integer
	 */
	public int getUnsignedPixelValue(short pixelValue) {
		return pixelValue & 0xffff;
	}

	/**
	 * Get the "unsigned short" value from the unsigned 16 bit integer pixel
	 * value
	 * 
	 * @param unsignedPixelValue
	 *            unsigned 16 bit integer pixel value
	 * @return "unsigned short" pixel value
	 */
	public short getPixelValue(int unsignedPixelValue) {
		return (short) unsignedPixelValue;
	}

	/**
	 * Get the unsigned pixel values. The values saved as "unsigned shorts" in
	 * the short array is returned as an integer which stores the positive 16
	 * bit value
	 * 
	 * @param pixelValues
	 *            pixel values as "unsigned shorts"
	 * @return unsigned 16 bit pixel values as an integer array
	 */
	public int[] getUnsignedPixelValues(short[] pixelValues) {
		int[] unsignedValues = new int[pixelValues.length];
		for (int i = 0; i < pixelValues.length; i++) {
			unsignedValues[i] = getUnsignedPixelValue(pixelValues[i]);
		}
		return unsignedValues;
	}

	/**
	 * Get the elevation value for the "unsigned short" pixel value
	 * 
	 * @param griddedTile
	 *            gridded tile
	 * @param pixelValue
	 *            pixel value as an unsigned short
	 * @return elevation value
	 */
	public Double getElevationValue(GriddedTile griddedTile, short pixelValue) {
		int unsignedPixelValue = getUnsignedPixelValue(pixelValue);
		Double elevation = getElevationValue(griddedTile, unsignedPixelValue);
		return elevation;
	}

	/**
	 * Get the elevation value for the unsigned short pixel value
	 * 
	 * @param griddedTile
	 *            gridded tile
	 * @param unsignedPixelValue
	 *            pixel value as an unsigned 16 bit integer
	 * @return elevation value
	 */
	public Double getElevationValue(GriddedTile griddedTile,
			int unsignedPixelValue) {

		Double elevation = null;
		if (!isDataMissingOrNull(unsignedPixelValue)) {

			elevation = new Double(unsignedPixelValue);
			if (griddedTile != null) {
				elevation = elevation * griddedTile.getScale()
						+ griddedTile.getOffset();
			}
			if (firstGriddedCoverage != null) {
				elevation = elevation * firstGriddedCoverage.getScale()
						+ firstGriddedCoverage.getOffset();
			}
		}

		return elevation;
	}

	/**
	 * Get the elevation values from the "unsigned short" pixel values
	 * 
	 * @param griddedTile
	 *            gridded tile
	 * @param pixelValues
	 *            pixel values as "unsigned shorts"
	 * @return elevation values
	 */
	public Double[] getElevationValues(GriddedTile griddedTile,
			short[] pixelValues) {
		Double[] elevations = new Double[pixelValues.length];
		for (int i = 0; i < pixelValues.length; i++) {
			elevations[i] = getElevationValue(griddedTile, pixelValues[i]);
		}
		return elevations;
	}

	/**
	 * Get the data missing value
	 * 
	 * @return data missing value or null
	 */
	public Double getDataMissing() {

		Double dataMissing = null;
		if (firstGriddedCoverage != null) {
			dataMissing = firstGriddedCoverage.getDataMissing();
		}

		return dataMissing;
	}

	/**
	 * Check the unsigned pixel value to see if it is the missing equivalent
	 * 
	 * @param value
	 *            unsigned pixel value
	 * @return true if equivalent to data missing
	 */
	public boolean isDataMissing(double value) {
		Double dataMissing = getDataMissing();
		boolean isDataMissing = dataMissing != null && dataMissing == value;
		return isDataMissing;
	}

	/**
	 * Get the data null value
	 * 
	 * @return data null value or null
	 */
	public Double getDataNull() {

		Double dataNull = null;
		if (firstGriddedCoverage != null) {
			dataNull = firstGriddedCoverage.getDataNull();
		}

		return dataNull;
	}

	/**
	 * Check the unsigned pixel value to see if it is the null equivalent
	 * 
	 * @param value
	 *            unsigned pixel value
	 * @return true if equivalent to data null
	 */
	public boolean isDataNull(double value) {
		Double dataNull = getDataNull();
		boolean isDataNull = dataNull != null && dataNull == value;
		return isDataNull;
	}

	/**
	 * Check the unsigned pixel value to see if it is the missing or null
	 * equivalent
	 * 
	 * @param value
	 *            unsigned pixel value
	 * @return true if equivalent data missing or data null
	 */
	public boolean isDataMissingOrNull(double value) {
		return isDataMissing(value) || isDataNull(value);
	}

	/**
	 * Create the elevation tile table with metadata
	 * 
	 * @param geoPackage
	 * @param tableName
	 * @param contentsBoundingBox
	 * @param contentsSrsId
	 * @param tileMatrixSetBoundingBox
	 * @param tileMatrixSetSrsId
	 */
	public static TileMatrixSet createTileTableWithMetadata(
			GeoPackageCore geoPackage, String tableName,
			BoundingBox contentsBoundingBox, long contentsSrsId,
			BoundingBox tileMatrixSetBoundingBox, long tileMatrixSetSrsId) {

		TileMatrixSet tileMatrixSet = geoPackage.createTileTableWithMetadata(
				ContentsDataType.ELEVATION_TILES, tableName,
				contentsBoundingBox, contentsSrsId, tileMatrixSetBoundingBox,
				tileMatrixSetSrsId);
		ElevationTilesCore elevationTiles = new ElevationTilesCore(geoPackage,
				tileMatrixSet);
		elevationTiles.getOrCreate();
		return tileMatrixSet;
	}

	/**
	 * Get the elevation tile tables
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * @return table names
	 */
	public static List<String> getTables(GeoPackageCore geoPackage) {
		return geoPackage.getTables(ContentsDataType.ELEVATION_TILES);
	}

	/**
	 * Reproject the elevations to the requested projection
	 *
	 * @param elevations
	 *            elevations
	 * @param requestedElevationsWidth
	 *            requested elevations width
	 * @param requestedElevationsHeight
	 *            requested elevations height
	 * @param requestBoundingBox
	 *            request bounding box in the request projection
	 * @param transformRequestToElevation
	 *            transformation from request to elevations
	 * @param elevationBoundingBox
	 *            elevations bounding box
	 * @return projected elevations
	 */
	protected Double[][] reprojectElevations(Double[][] elevations,
			int requestedElevationsWidth, int requestedElevationsHeight,
			BoundingBox requestBoundingBox,
			ProjectionTransform transformRequestToElevation,
			BoundingBox elevationBoundingBox) {

		final double requestedWidthUnitsPerPixel = (requestBoundingBox
				.getMaxLongitude() - requestBoundingBox.getMinLongitude())
				/ requestedElevationsWidth;
		final double requestedHeightUnitsPerPixel = (requestBoundingBox
				.getMaxLatitude() - requestBoundingBox.getMinLatitude())
				/ requestedElevationsHeight;

		final double tilesDistanceWidth = elevationBoundingBox
				.getMaxLongitude() - elevationBoundingBox.getMinLongitude();
		final double tilesDistanceHeight = elevationBoundingBox
				.getMaxLatitude() - elevationBoundingBox.getMinLatitude();

		final int width = elevations[0].length;
		final int height = elevations.length;

		Double[][] projectedElevations = new Double[requestedElevationsHeight][requestedElevationsWidth];

		// Retrieve each elevation in the unprojected elevations
		for (int y = 0; y < requestedElevationsHeight; y++) {
			for (int x = 0; x < requestedElevationsWidth; x++) {

				double longitude = requestBoundingBox.getMinLongitude()
						+ (x * requestedWidthUnitsPerPixel);
				double latitude = requestBoundingBox.getMaxLatitude()
						- (y * requestedHeightUnitsPerPixel);
				ProjCoordinate fromCoord = new ProjCoordinate(longitude,
						latitude);
				ProjCoordinate toCoord = transformRequestToElevation
						.transform(fromCoord);
				double projectedLongitude = toCoord.x;
				double projectedLatitude = toCoord.y;

				int xPixel = (int) Math
						.round(((projectedLongitude - elevationBoundingBox
								.getMinLongitude()) / tilesDistanceWidth)
								* width);
				int yPixel = (int) Math
						.round(((elevationBoundingBox.getMaxLatitude() - projectedLatitude) / tilesDistanceHeight)
								* height);

				xPixel = Math.max(0, xPixel);
				xPixel = Math.min(width - 1, xPixel);

				yPixel = Math.max(0, yPixel);
				yPixel = Math.min(height - 1, yPixel);

				Double elevation = elevations[yPixel][xPixel];
				projectedElevations[y][x] = elevation;
			}
		}

		return projectedElevations;
	}

	/**
	 * Format the unbounded results from elevation tiles into a single double
	 * array of elevation
	 * 
	 * @param tileMatrix
	 *            tile matrix
	 * @param rowsMap
	 *            rows map
	 * @param tileCount
	 *            tile count
	 * @param minRow
	 *            min row
	 * @param maxRow
	 *            max row
	 * @param minColumn
	 *            min column
	 * @param maxColumn
	 *            max column
	 * @return elevations
	 */
	protected Double[][] formatUnboundedResults(TileMatrix tileMatrix,
			Map<Long, Map<Long, Double[][]>> rowsMap, int tileCount,
			long minRow, long maxRow, long minColumn, long maxColumn) {

		// Handle formatting the results
		Double[][] elevations = null;
		if (!rowsMap.isEmpty()) {

			// If only one tile result, use the elevations as the result
			if (tileCount == 1) {
				elevations = rowsMap.get(minRow).get(minColumn);
			} else {

				// Else, combine all results into a single elevations result

				// Get the top left and bottom right elevations
				Double[][] topLeft = rowsMap.get(minRow).get(minColumn);
				Double[][] bottomRight = rowsMap.get(maxRow).get(maxColumn);

				// Determine the width and height of the top left elevation
				// results
				int firstWidth = topLeft[0].length;
				int firstHeight = topLeft.length;

				// Determine the final result width and height
				int width = firstWidth;
				int height = firstHeight;
				if (minColumn < maxColumn) {
					width += bottomRight[0].length;
					long middleColumns = maxColumn - minColumn - 1;
					if (middleColumns > 0) {
						width += (middleColumns * tileMatrix.getTileWidth());
					}
				}
				if (minRow < maxRow) {
					height += bottomRight.length;
					long middleRows = maxRow - minRow - 1;
					if (middleRows > 0) {
						height += (middleRows * tileMatrix.getTileHeight());
					}
				}

				// Create the elevation result array
				elevations = new Double[height][width];

				// Copy the elevation values from each tile results into the
				// final result arrays
				for (Map.Entry<Long, Map<Long, Double[][]>> rows : rowsMap
						.entrySet()) {

					// Determine the starting base row for this tile
					long row = rows.getKey();
					int baseRow = 0;
					if (minRow < row) {
						baseRow = firstHeight
								+ (int) ((row - minRow - 1) * tileMatrix
										.getTileHeight());
					}

					// Get the row's columns map
					Map<Long, Double[][]> columnsMap = rows.getValue();

					for (Map.Entry<Long, Double[][]> columns : columnsMap
							.entrySet()) {

						// Determine the starting base column for this tile
						long column = columns.getKey();
						int baseColumn = 0;
						if (minColumn < column) {
							baseColumn = firstWidth
									+ (int) ((column - minColumn - 1) * tileMatrix
											.getTileWidth());
						}

						// Get the tiles elevation values
						Double[][] values = columns.getValue();

						// Copy the columns array at each local elevation row to
						// the global row and column result location
						for (int localRow = 0; localRow < values.length; localRow++) {

							int globalRow = baseRow + localRow;

							System.arraycopy(values[localRow], 0,
									elevations[globalRow], baseColumn,
									values[localRow].length);
						}
					}

				}
			}

		}

		return elevations;
	}

	/**
	 * Get the unsigned 16 bit integer pixel value of the elevation
	 * 
	 * @param griddedTile
	 *            gridded tile
	 * @param elevation
	 *            elevation value
	 * @return 16 bit integer pixel value
	 */
	public int getUnsignedPixelValue(GriddedTile griddedTile, Double elevation) {

		int unsignedPixelValue = 0;

		if (elevation == null) {
			if (firstGriddedCoverage != null) {
				unsignedPixelValue = firstGriddedCoverage.getDataNull()
						.intValue();
			}
		} else {
			double value = elevation;
			if (firstGriddedCoverage != null) {
				value = (value - firstGriddedCoverage.getOffset())
						/ firstGriddedCoverage.getScale();
			}
			if (griddedTile != null) {
				value = (value - griddedTile.getOffset())
						/ griddedTile.getScale();
			}
			unsignedPixelValue = (int) Math.round(value);
		}

		return unsignedPixelValue;
	}

	/**
	 * Get the "unsigned short" pixel value of the elevation
	 * 
	 * @param griddedTile
	 *            gridded tile
	 * @param elevation
	 *            elevation value
	 * @return "unsigned short" pixel value
	 */
	public short getPixelValue(GriddedTile griddedTile, Double elevation) {
		int unsignedPixelValue = getUnsignedPixelValue(griddedTile, elevation);
		short pixelValue = getPixelValue(unsignedPixelValue);
		return pixelValue;
	}

}
