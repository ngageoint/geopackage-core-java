package mil.nga.geopackage.extension.coverage;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.locationtech.proj4j.ProjCoordinate;

import mil.nga.geopackage.BoundingBox;
import mil.nga.geopackage.GeoPackageConstants;
import mil.nga.geopackage.GeoPackageCore;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.db.GeoPackageCoreConnection;
import mil.nga.geopackage.extension.BaseExtension;
import mil.nga.geopackage.extension.ExtensionScopeType;
import mil.nga.geopackage.extension.Extensions;
import mil.nga.geopackage.property.GeoPackageProperties;
import mil.nga.geopackage.property.PropertyConstants;
import mil.nga.geopackage.tiles.matrix.TileMatrix;
import mil.nga.geopackage.tiles.matrixset.TileMatrixSet;
import mil.nga.geopackage.tiles.user.TileTable;
import mil.nga.geopackage.tiles.user.TileTableMetadata;
import mil.nga.sf.proj.Projection;
import mil.nga.sf.proj.ProjectionTransform;

/**
 * Tiled Gridded Coverage Core Data Extension
 * 
 * http://docs.opengeospatial.org/is/17-066r1/17-066r1.html
 * 
 * @param <TImage>
 *            image type
 * 
 * @author osbornb
 * @since 2.0.1
 */
public abstract class CoverageDataCore<TImage extends CoverageDataImage>
		extends BaseExtension {

	/**
	 * Extension author
	 */
	public static final String EXTENSION_AUTHOR = GeoPackageConstants.EXTENSION_AUTHOR;

	/**
	 * Extension name without the author
	 */
	public static final String EXTENSION_NAME_NO_AUTHOR = "2d_gridded_coverage";

	/**
	 * Extension, with author and name
	 */
	public static final String EXTENSION_NAME = Extensions
			.buildExtensionName(EXTENSION_AUTHOR, EXTENSION_NAME_NO_AUTHOR);

	/**
	 * Extension definition URL
	 */
	public static final String EXTENSION_DEFINITION = GeoPackageProperties
			.getProperty(PropertyConstants.EXTENSIONS,
					EXTENSION_NAME_NO_AUTHOR);

	/**
	 * Contents Data Type
	 * 
	 * @since 4.0.0
	 */
	public static final String GRIDDED_COVERAGE = "2d-gridded-coverage";

	/**
	 * Tile Matrix Set
	 */
	private final TileMatrixSet tileMatrixSet;

	/**
	 * Gridded Coverage DAO
	 */
	private GriddedCoverageDao griddedCoverageDao;

	/**
	 * Gridded Tile DAO
	 */
	private GriddedTileDao griddedTileDao;

	/**
	 * Gridded coverage
	 */
	private GriddedCoverage griddedCoverage;

	/**
	 * Coverage data results width
	 */
	protected Integer width;

	/**
	 * Coverage data results height
	 */
	protected Integer height;

	/**
	 * Projection of the requests
	 */
	protected final Projection requestProjection;

	/**
	 * Projection of the coverage data
	 */
	protected final Projection coverageProjection;

	/**
	 * Coverage data bounding box
	 */
	protected final BoundingBox coverageBoundingBox;

	/**
	 * Flag indicating the coverage data and request projections are the same
	 */
	protected final boolean sameProjection;

	/**
	 * True if zooming in should be performed to find a tile matrix with
	 * coverage data values
	 */
	protected boolean zoomIn = true;

	/**
	 * True if zooming out should be performed to find a tile matrix with
	 * coverage data values
	 */
	protected boolean zoomOut = true;

	/**
	 * True if zoom in in before zooming out, false to zoom out first
	 */
	protected boolean zoomInBeforeOut = true;

	/**
	 * Interpolation algorithm
	 */
	protected CoverageDataAlgorithm algorithm = CoverageDataAlgorithm.NEAREST_NEIGHBOR;

	/**
	 * Value pixel encoding type
	 */
	protected GriddedCoverageEncodingType encoding = GriddedCoverageEncodingType.CENTER;

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
	protected CoverageDataCore(GeoPackageCore geoPackage,
			TileMatrixSet tileMatrixSet, Integer width, Integer height,
			Projection requestProjection) {
		super(geoPackage);

		this.tileMatrixSet = tileMatrixSet;
		griddedCoverageDao = getGriddedCoverageDao();
		griddedTileDao = getGriddedTileDao();
		queryGriddedCoverage();

		this.width = width;
		this.height = height;
		this.requestProjection = requestProjection;
		coverageProjection = tileMatrixSet.getProjection();
		coverageBoundingBox = tileMatrixSet.getBoundingBox();

		// Check if the projections have the same units
		if (requestProjection != null) {
			sameProjection = (requestProjection.getUnit().name
					.equals(coverageProjection.getUnit().name));
		} else {
			sameProjection = true;
		}
	}

	/**
	 * Constructor for creating extension rows when creating a new coverage data
	 * tile table
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * @param tileMatrixSet
	 *            tile matrix set
	 */
	protected CoverageDataCore(GeoPackageCore geoPackage,
			TileMatrixSet tileMatrixSet) {
		this(geoPackage, tileMatrixSet, null, null, null);
	}

	/**
	 * Get the coverage data value from the image at the coordinate
	 *
	 * @param griddedTile
	 *            gridded tile
	 * @param image
	 *            coverage data image
	 * @param x
	 *            x coordinate
	 * @param y
	 *            y coordinate
	 * @return coverage data value
	 */
	public abstract Double getValue(GriddedTile griddedTile, TImage image,
			int x, int y);

	/**
	 * Get the requested coverage data values with the requested width and
	 * height
	 * 
	 * @param request
	 *            coverage data request
	 * @param width
	 *            coverage data request width
	 * @param height
	 *            coverage data request height
	 * @return coverage data results
	 */
	public abstract CoverageDataResults getValues(CoverageDataRequest request,
			Integer width, Integer height);

	/**
	 * Get the requested unbounded coverage data values. Unbounded results
	 * retrieves and returns each coverage data pixel. The results size equals
	 * the width and height of all matching pixels.
	 * 
	 * @param request
	 *            coverage data request
	 * @return coverage data results
	 */
	public abstract CoverageDataResults getValuesUnbounded(
			CoverageDataRequest request);

	/**
	 * Get the Tile Matrix Set
	 * 
	 * @return Tile Matrix Set
	 */
	public TileMatrixSet getTileMatrixSet() {
		return tileMatrixSet;
	}

	/**
	 * Get the requested coverage data width
	 * 
	 * @return width
	 */
	public Integer getWidth() {
		return width;
	}

	/**
	 * Set the requested coverage data width
	 * 
	 * @param width
	 *            requested coverage data width
	 */
	public void setWidth(Integer width) {
		this.width = width;
	}

	/**
	 * Get the requested coverage data height
	 * 
	 * @return height
	 */
	public Integer getHeight() {
		return height;
	}

	/**
	 * Set the requested coverage data height
	 * 
	 * @param height
	 *            requested coverage data height
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
	 * Get the coverage data projection
	 * 
	 * @return coverage data projection
	 */
	public Projection getCoverageProjection() {
		return coverageProjection;
	}

	/**
	 * Get the coverage data bounding box
	 * 
	 * @return coverage data bounding box
	 */
	public BoundingBox getCoverageBoundingBox() {
		return coverageBoundingBox;
	}

	/**
	 * Is the request and coverage data projection the same
	 * 
	 * @return true if the same
	 */
	public boolean isSameProjection() {
		return sameProjection;
	}

	/**
	 * Is the zooming in (higher zoom level values) enabled to find matching
	 * coverage data
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
	 *            true to zoom in when finding coverage data, false to disable
	 */
	public void setZoomIn(boolean zoomIn) {
		this.zoomIn = zoomIn;
	}

	/**
	 * Is the zooming out (lower zoom level values) enabled to find matching
	 * coverage data
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
	 *            true to zoom out when finding coverage data, false to disable
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
	 * Get the interpolation algorithm
	 * 
	 * @return algorithm
	 */
	public CoverageDataAlgorithm getAlgorithm() {
		return algorithm;
	}

	/**
	 * Set the interpolation algorithm
	 * 
	 * @param algorithm
	 *            algorithm type
	 */
	public void setAlgorithm(CoverageDataAlgorithm algorithm) {
		if (algorithm == null) {
			algorithm = CoverageDataAlgorithm.NEAREST_NEIGHBOR;
		}
		this.algorithm = algorithm;
	}

	/**
	 * Get the value pixel encoding type
	 * 
	 * @return encoding type
	 * @since 2.0.1
	 */
	public GriddedCoverageEncodingType getEncoding() {
		return encoding;
	}

	/**
	 * Set the value pixel encoding type
	 * 
	 * @param encoding
	 *            encoding type
	 * @since 2.0.1
	 */
	public void setEncoding(GriddedCoverageEncodingType encoding) {
		if (encoding == null) {
			encoding = GriddedCoverageEncodingType.CENTER;
		}
		this.encoding = encoding;
	}

	/**
	 * Get or create the extension
	 * 
	 * @return extensions list
	 */
	public List<Extensions> getOrCreate() {

		// Create tables
		createGriddedCoverageTable();
		createGriddedTileTable();

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
	 * Get a 2D Gridded Coverage DAO
	 * 
	 * @return 2d gridded coverage dao
	 */
	public GriddedCoverageDao getGriddedCoverageDao() {
		if (griddedCoverageDao == null) {
			griddedCoverageDao = GriddedCoverageDao.create(geoPackage);
		}
		return griddedCoverageDao;
	}

	/**
	 * Get a 2D Gridded Coverage DAO
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * @return 2d gridded coverage dao
	 * @since 4.0.0
	 */
	public static GriddedCoverageDao getGriddedCoverageDao(
			GeoPackageCore geoPackage) {
		return GriddedCoverageDao.create(geoPackage);
	}

	/**
	 * Get a 2D Gridded Coverage DAO
	 * 
	 * @param db
	 *            database connection
	 * @return 2d gridded coverage dao
	 * @since 4.0.0
	 */
	public static GriddedCoverageDao getGriddedCoverageDao(
			GeoPackageCoreConnection db) {
		return GriddedCoverageDao.create(db);
	}

	/**
	 * Create the 2D Gridded Coverage Table if it does not exist
	 * 
	 * @return true if created
	 * @since 4.0.0
	 */
	public boolean createGriddedCoverageTable() {
		verifyWritable();

		boolean created = false;

		try {
			if (!griddedCoverageDao.isTableExists()) {
				created = geoPackage.getTableCreator()
						.createGriddedCoverage() > 0;
			}
		} catch (SQLException e) {
			throw new GeoPackageException("Failed to check if "
					+ GriddedCoverage.class.getSimpleName()
					+ " table exists and create it", e);
		}

		return created;
	}

	/**
	 * Get a 2D Gridded Tile DAO
	 * 
	 * @return 2d gridded tile dao
	 */
	public GriddedTileDao getGriddedTileDao() {
		if (griddedTileDao == null) {
			griddedTileDao = getGriddedTileDao(geoPackage);
		}
		return griddedTileDao;
	}

	/**
	 * Get a 2D Gridded Tile DAO
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * @return 2d gridded tile dao
	 * @since 4.0.0
	 */
	public static GriddedTileDao getGriddedTileDao(GeoPackageCore geoPackage) {
		return GriddedTileDao.create(geoPackage);
	}

	/**
	 * Get a 2D Gridded Tile DAO
	 * 
	 * @param db
	 *            database connection
	 * @return 2d gridded tile dao
	 * @since 4.0.0
	 */
	public static GriddedTileDao getGriddedTileDao(
			GeoPackageCoreConnection db) {
		return GriddedTileDao.create(db);
	}

	/**
	 * Create the 2D Gridded Tile Table if it does not exist
	 * 
	 * @return true if created
	 * @since 4.0.0
	 */
	public boolean createGriddedTileTable() {
		verifyWritable();

		boolean created = false;

		try {
			if (!griddedTileDao.isTableExists()) {
				created = geoPackage.getTableCreator().createGriddedTile() > 0;
			}
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to check if " + GriddedTile.class.getSimpleName()
							+ " table exists and create it",
					e);
		}

		return created;
	}

	/**
	 * Get the gridded coverage
	 * 
	 * @return gridded coverage
	 */
	public GriddedCoverage getGriddedCoverage() {
		return griddedCoverage;
	}

	/**
	 * Query and update the gridded coverage
	 * 
	 * @return gridded coverage
	 */
	public GriddedCoverage queryGriddedCoverage() {
		try {
			if (griddedCoverageDao.isTableExists()) {
				griddedCoverage = griddedCoverageDao.query(tileMatrixSet);
			}
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to get Gridded Coverage for table name: "
							+ tileMatrixSet.getTableName(),
					e);
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
							+ tileMatrixSet.getTableName(),
					e);
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
				griddedTile = griddedTileDao.query(tileMatrixSet.getTableName(),
						tileId);
			}
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to get Gridded Tile for table name: "
							+ tileMatrixSet.getTableName() + ", tile id: "
							+ tileId,
					e);
		}
		return griddedTile;
	}

	/**
	 * Get the data null value
	 * 
	 * @return data null value or null
	 */
	public Double getDataNull() {

		Double dataNull = null;
		if (griddedCoverage != null) {
			dataNull = griddedCoverage.getDataNull();
		}

		return dataNull;
	}

	/**
	 * Check the pixel value to see if it is the null equivalent
	 * 
	 * @param value
	 *            pixel value
	 * @return true if equivalent to data null
	 */
	public boolean isDataNull(double value) {
		Double dataNull = getDataNull();
		boolean isDataNull = dataNull != null && dataNull == value;
		return isDataNull;
	}

	/**
	 * Get the coverage data tile tables
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * @return table names
	 */
	public static List<String> getTables(GeoPackageCore geoPackage) {
		return geoPackage.getTables(GRIDDED_COVERAGE);
	}

	/**
	 * Reproject the coverage data to the requested projection
	 *
	 * @param values
	 *            coverage data values
	 * @param requestedCoverageWidth
	 *            requested coverage data width
	 * @param requestedCoverageHeight
	 *            requested coverage data height
	 * @param requestBoundingBox
	 *            request bounding box in the request projection
	 * @param transformRequestToCoverage
	 *            transformation from request to coverage data
	 * @param coverageBoundingBox
	 *            coverage data bounding box
	 * @return projected coverage data
	 */
	protected Double[][] reprojectCoverageData(Double[][] values,
			int requestedCoverageWidth, int requestedCoverageHeight,
			BoundingBox requestBoundingBox,
			ProjectionTransform transformRequestToCoverage,
			BoundingBox coverageBoundingBox) {

		final double requestedWidthUnitsPerPixel = (requestBoundingBox
				.getMaxLongitude() - requestBoundingBox.getMinLongitude())
				/ requestedCoverageWidth;
		final double requestedHeightUnitsPerPixel = (requestBoundingBox
				.getMaxLatitude() - requestBoundingBox.getMinLatitude())
				/ requestedCoverageHeight;

		final double tilesDistanceWidth = coverageBoundingBox.getMaxLongitude()
				- coverageBoundingBox.getMinLongitude();
		final double tilesDistanceHeight = coverageBoundingBox.getMaxLatitude()
				- coverageBoundingBox.getMinLatitude();

		final int width = values[0].length;
		final int height = values.length;

		Double[][] projectedValues = new Double[requestedCoverageHeight][requestedCoverageWidth];

		// Retrieve each coverage data value in the unprojected coverage data
		for (int y = 0; y < requestedCoverageHeight; y++) {
			for (int x = 0; x < requestedCoverageWidth; x++) {

				double longitude = requestBoundingBox.getMinLongitude()
						+ (x * requestedWidthUnitsPerPixel);
				double latitude = requestBoundingBox.getMaxLatitude()
						- (y * requestedHeightUnitsPerPixel);
				ProjCoordinate fromCoord = new ProjCoordinate(longitude,
						latitude);
				ProjCoordinate toCoord = transformRequestToCoverage
						.transform(fromCoord);
				double projectedLongitude = toCoord.x;
				double projectedLatitude = toCoord.y;

				int xPixel = (int) Math.round(((projectedLongitude
						- coverageBoundingBox.getMinLongitude())
						/ tilesDistanceWidth) * width);
				int yPixel = (int) Math
						.round(((coverageBoundingBox.getMaxLatitude()
								- projectedLatitude) / tilesDistanceHeight)
								* height);

				xPixel = Math.max(0, xPixel);
				xPixel = Math.min(width - 1, xPixel);

				yPixel = Math.max(0, yPixel);
				yPixel = Math.min(height - 1, yPixel);

				Double coverageData = values[yPixel][xPixel];
				projectedValues[y][x] = coverageData;
			}
		}

		return projectedValues;
	}

	/**
	 * Format the unbounded results from coverage data tiles into a single
	 * double array of coverage data
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
	 * @return coverage data
	 */
	protected Double[][] formatUnboundedResults(TileMatrix tileMatrix,
			Map<Long, Map<Long, Double[][]>> rowsMap, int tileCount,
			long minRow, long maxRow, long minColumn, long maxColumn) {

		// Handle formatting the results
		Double[][] values = null;
		if (!rowsMap.isEmpty()) {

			// If only one tile result, use the coverage data as the result
			if (tileCount == 1) {
				values = rowsMap.get(minRow).get(minColumn);
			} else {

				// Else, combine all results into a single coverage data result

				// Get the top left and bottom right coverage data values
				Double[][] topLeft = rowsMap.get(minRow).get(minColumn);
				Double[][] bottomRight = rowsMap.get(maxRow).get(maxColumn);

				// Determine the width and height of the top left coverage data
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

				// Create the coverage data result array
				values = new Double[height][width];

				// Copy the coverage data values from each tile results into the
				// final result arrays
				for (Map.Entry<Long, Map<Long, Double[][]>> rows : rowsMap
						.entrySet()) {

					// Determine the starting base row for this tile
					long row = rows.getKey();
					int baseRow = 0;
					if (minRow < row) {
						baseRow = firstHeight + (int) ((row - minRow - 1)
								* tileMatrix.getTileHeight());
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
									+ (int) ((column - minColumn - 1)
											* tileMatrix.getTileWidth());
						}

						// Get the tiles coverage data values
						Double[][] localValues = columns.getValue();

						// Copy the columns array at each local coverage data
						// row to the global row and column result location
						for (int localRow = 0; localRow < localValues.length; localRow++) {

							int globalRow = baseRow + localRow;

							System.arraycopy(localValues[localRow], 0,
									values[globalRow], baseColumn,
									localValues[localRow].length);
						}
					}

				}
			}

		}

		return values;
	}

	/**
	 * Determine the x source pixel location
	 * 
	 * @param x
	 *            x pixel
	 * @param destLeft
	 *            destination left most pixel
	 * @param srcLeft
	 *            source left most pixel
	 * @param widthRatio
	 *            source over destination width ratio
	 * @return x source pixel
	 */
	protected float getXSource(int x, float destLeft, float srcLeft,
			float widthRatio) {

		float dest = getXEncodedLocation(x, encoding);
		float source = getSource(dest, destLeft, srcLeft, widthRatio);

		return source;
	}

	/**
	 * Determine the y source pixel location
	 * 
	 * @param y
	 *            y pixel
	 * @param destTop
	 *            destination top most pixel
	 * @param srcTop
	 *            source top most pixel
	 * @param heightRatio
	 *            source over destination height ratio
	 * @return y source pixel
	 */
	protected float getYSource(int y, float destTop, float srcTop,
			float heightRatio) {

		float dest = getYEncodedLocation(y, encoding);
		float source = getSource(dest, destTop, srcTop, heightRatio);

		return source;
	}

	/**
	 * Determine the source pixel location
	 * 
	 * @param dest
	 *            destination pixel location
	 * @param destMin
	 *            destination minimum most pixel
	 * @param srcMin
	 *            source minimum most pixel
	 * @param ratio
	 *            source over destination length ratio
	 * @return source pixel
	 */
	private float getSource(float dest, float destMin, float srcMin,
			float ratio) {

		float destDistance = dest - destMin;
		float srcDistance = destDistance * ratio;
		float ySource = srcMin + srcDistance;

		return ySource;
	}

	/**
	 * Get the X encoded location from the base provided x
	 * 
	 * @param x
	 *            x location
	 * @param encodingType
	 *            pixel encoding type
	 * @return encoded x location
	 */
	private float getXEncodedLocation(float x,
			GriddedCoverageEncodingType encodingType) {

		float xLocation = x;

		switch (encodingType) {
		case CENTER:
		case AREA:
			xLocation += 0.5f;
			break;
		case CORNER:
			break;
		default:
			throw new GeoPackageException(
					"Unsupported Encoding Type: " + encodingType);
		}

		return xLocation;
	}

	/**
	 * Get the Y encoded location from the base provided y
	 * 
	 * @param y
	 *            y location
	 * @param encodingType
	 *            pixel encoding type
	 * @return encoded y location
	 */
	private float getYEncodedLocation(float y,
			GriddedCoverageEncodingType encodingType) {

		float yLocation = y;

		switch (encodingType) {
		case CENTER:
		case AREA:
			yLocation += 0.5f;
			break;
		case CORNER:
			yLocation += 1.0f;
			break;
		default:
			throw new GeoPackageException(
					"Unsupported Encoding Type: " + encodingType);
		}

		return yLocation;
	}

	/**
	 * Determine the nearest neighbors of the source pixel, sorted by closest to
	 * farthest neighbor
	 * 
	 * @param xSource
	 *            x source pixel
	 * @param ySource
	 *            y source pixel
	 * @return nearest neighbor pixels
	 */
	protected List<int[]> getNearestNeighbors(float xSource, float ySource) {

		List<int[]> results = new ArrayList<int[]>();

		// Get the coverage data source pixels for x and y
		CoverageDataSourcePixel xPixel = getXSourceMinAndMax(xSource);
		CoverageDataSourcePixel yPixel = getYSourceMinAndMax(ySource);

		// Determine which x pixel is the closest, the second closest, and the
		// distance to the second pixel
		int firstX;
		int secondX;
		float xDistance;
		if (xPixel.getOffset() > .5) {
			firstX = xPixel.getMax();
			secondX = xPixel.getMin();
			xDistance = 1.0f - xPixel.getOffset();
		} else {
			firstX = xPixel.getMin();
			secondX = xPixel.getMax();
			xDistance = xPixel.getOffset();
		}

		// Determine which y pixel is the closest, the second closest, and the
		// distance to the second pixel
		int firstY;
		int secondY;
		float yDistance;
		if (yPixel.getOffset() > .5) {
			firstY = yPixel.getMax();
			secondY = yPixel.getMin();
			yDistance = 1.0f - yPixel.getOffset();
		} else {
			firstY = yPixel.getMin();
			secondY = yPixel.getMax();
			yDistance = yPixel.getOffset();
		}

		// Add the closest neighbor
		results.add(new int[] { firstX, firstY });

		// Add the second and third neighbor based upon the x and y distances to
		// second coordinates
		if (xDistance <= yDistance) {
			results.add(new int[] { secondX, firstY });
			results.add(new int[] { firstX, secondY });
		} else {
			results.add(new int[] { firstX, secondY });
			results.add(new int[] { secondX, firstY });
		}

		// Add the farthest neighbor
		results.add(new int[] { secondX, secondY });

		// If right on the boundary between the forward and backwards pixel, add
		// the backwards pixel options
		if (xPixel.getOffset() == 0) {
			results.add(new int[] { xPixel.getMin() - 1, yPixel.getMin() });
			results.add(new int[] { xPixel.getMin() - 1, yPixel.getMax() });
		}
		if (yPixel.getOffset() == 0) {
			results.add(new int[] { xPixel.getMin(), yPixel.getMin() - 1 });
			results.add(new int[] { xPixel.getMax(), yPixel.getMin() - 1 });
		}
		if (xPixel.getOffset() == 0 && yPixel.getOffset() == 0) {
			results.add(new int[] { xPixel.getMin() - 1, yPixel.getMin() - 1 });
		}

		return results;
	}

	/**
	 * Get the min, max, and offset of the source X pixel
	 * 
	 * @param source
	 *            source x pixel
	 * @return source x pixel information
	 */
	protected CoverageDataSourcePixel getXSourceMinAndMax(float source) {

		int floor = (int) Math.floor(source);
		float valueLocation = getXEncodedLocation(floor,
				griddedCoverage.getGridCellEncodingType());

		CoverageDataSourcePixel pixel = getSourceMinAndMax(source, floor,
				valueLocation);
		return pixel;
	}

	/**
	 * Get the min, max, and offset of the source Y pixel
	 * 
	 * @param source
	 *            source y pixel
	 * @return source y pixel information
	 */
	protected CoverageDataSourcePixel getYSourceMinAndMax(float source) {

		int floor = (int) Math.floor(source);
		float valueLocation = getYEncodedLocation(floor,
				griddedCoverage.getGridCellEncodingType());

		CoverageDataSourcePixel pixel = getSourceMinAndMax(source, floor,
				valueLocation);
		return pixel;
	}

	/**
	 * Get the min, max, and offset of the source pixel
	 * 
	 * @param source
	 *            source pixel
	 * @param sourceFloor
	 *            source floor value
	 * @param valueLocation
	 *            value location
	 * @return source pixel information
	 */
	private CoverageDataSourcePixel getSourceMinAndMax(float source,
			int sourceFloor, float valueLocation) {

		int min = sourceFloor;
		int max = sourceFloor;
		float offset;
		if (source < valueLocation) {
			min--;
			offset = 1.0f - (valueLocation - source);
		} else {
			max++;
			offset = source - valueLocation;
		}

		return new CoverageDataSourcePixel(source, min, max, offset);
	}

	/**
	 * Get the Bilinear Interpolation coverage data value
	 * 
	 * @param sourcePixelX
	 *            source pixel x
	 * @param sourcePixelY
	 *            source pixel y
	 * @param values
	 *            2 x 2 coverage data values as [y][x]
	 * @return coverage data value
	 */
	protected Double getBilinearInterpolationValue(
			CoverageDataSourcePixel sourcePixelX,
			CoverageDataSourcePixel sourcePixelY, Double[][] values) {
		return getBilinearInterpolationValue(sourcePixelX.getOffset(),
				sourcePixelY.getOffset(), sourcePixelX.getMin(),
				sourcePixelX.getMax(), sourcePixelY.getMin(),
				sourcePixelY.getMax(), values);
	}

	/**
	 * Get the Bilinear Interpolation coverage data value
	 * 
	 * @param offsetX
	 *            x source pixel offset
	 * @param offsetY
	 *            y source pixel offset
	 * @param minX
	 *            min x value
	 * @param maxX
	 *            max x value
	 * @param minY
	 *            min y value
	 * @param maxY
	 *            max y value
	 * @param values
	 *            2 x 2 coverage data values as [y][x]
	 * @return coverage data value
	 */
	protected Double getBilinearInterpolationValue(float offsetX, float offsetY,
			float minX, float maxX, float minY, float maxY, Double[][] values) {

		Double value = null;

		if (values != null) {
			value = getBilinearInterpolationValue(offsetX, offsetY, minX, maxX,
					minY, maxY, values[0][0], values[0][1], values[1][0],
					values[1][1]);
		}

		return value;
	}

	/**
	 * Get the Bilinear Interpolation coverage data value
	 * 
	 * @param offsetX
	 *            x source pixel offset
	 * @param offsetY
	 *            y source pixel offset
	 * @param minX
	 *            min x value
	 * @param maxX
	 *            max x value
	 * @param minY
	 *            min y value
	 * @param maxY
	 *            max y value
	 * @param topLeft
	 *            top left coverage value
	 * @param topRight
	 *            top right coverage value
	 * @param bottomLeft
	 *            bottom left coverage value
	 * @param bottomRight
	 *            bottom right coverage value
	 * @return coverage data value
	 */
	protected Double getBilinearInterpolationValue(float offsetX, float offsetY,
			float minX, float maxX, float minY, float maxY, Double topLeft,
			Double topRight, Double bottomLeft, Double bottomRight) {

		Double value = null;

		if (topLeft != null && (topRight != null || minX == maxX)
				&& (bottomLeft != null || minY == maxY)
				&& (bottomRight != null || (minX == maxX && minY == maxY))) {

			float diffX = maxX - minX;

			double topRow;
			Double bottomRow;
			if (diffX == 0) {
				topRow = topLeft;
				bottomRow = bottomLeft;
			} else {
				float diffLeft = offsetX;
				float diffRight = diffX - offsetX;
				topRow = ((diffRight / diffX) * topLeft)
						+ ((diffLeft / diffX) * topRight);
				bottomRow = ((diffRight / diffX) * bottomLeft)
						+ ((diffLeft / diffX) * bottomRight);
			}

			float diffY = maxY - minY;

			double result;
			if (diffY == 0) {
				result = topRow;
			} else {
				float diffTop = offsetY;
				float diffBottom = diffY - offsetY;
				result = ((diffBottom / diffY) * topRow)
						+ ((diffTop / diffY) * bottomRow);
			}

			value = result;
		}

		return value;
	}

	/**
	 * Get the bicubic interpolation coverage data value from the 4 x 4 coverage
	 * data values
	 * 
	 * @param values
	 *            coverage data values
	 * @param sourcePixelX
	 *            source pixel x
	 * @param sourcePixelY
	 *            source pixel y
	 * @return bicubic coverage data value
	 */
	protected Double getBicubicInterpolationValue(Double[][] values,
			CoverageDataSourcePixel sourcePixelX,
			CoverageDataSourcePixel sourcePixelY) {
		return getBicubicInterpolationValue(values, sourcePixelX.getOffset(),
				sourcePixelY.getOffset());
	}

	/**
	 * Get the bicubic interpolation coverage data value from the 4 x 4 coverage
	 * data values
	 * 
	 * @param values
	 *            coverage data values
	 * @param offsetX
	 *            x source pixel offset
	 * @param offsetY
	 *            y source pixel offset
	 * @return bicubic coverage data value
	 */
	protected Double getBicubicInterpolationValue(Double[][] values,
			float offsetX, float offsetY) {

		Double value = null;

		Double[] rowValues = new Double[4];

		for (int y = 0; y < 4; y++) {
			Double rowValue = getCubicInterpolationValue(values[y][0],
					values[y][1], values[y][2], values[y][3], offsetX);
			if (rowValue == null) {
				rowValues = null;
				break;
			}
			rowValues[y] = rowValue;
		}

		if (rowValues != null) {
			value = getCubicInterpolationValue(rowValues, offsetY);
		}

		return value;
	}

	/**
	 * Interpolate 4 values using the offset between value1 and value2
	 * 
	 * @param values
	 *            coverage data values
	 * @param offset
	 *            offset between the middle two pixels
	 * @return value coverage data value
	 */
	protected Double getCubicInterpolationValue(Double[] values,
			double offset) {
		Double value = null;
		if (values != null) {
			value = getCubicInterpolationValue(values[0], values[1], values[2],
					values[3], offset);
		}
		return value;
	}

	/**
	 * Interpolate 4 values using the offset between value1 and value2
	 * 
	 * @param value0
	 *            index 0 value
	 * @param value1
	 *            index 1 value
	 * @param value2
	 *            index 2 value
	 * @param value3
	 *            index 3 value
	 * @param offset
	 *            offset between the middle two pixels
	 * @return value coverage data value
	 */
	protected Double getCubicInterpolationValue(Double value0, Double value1,
			Double value2, Double value3, double offset) {

		Double value = null;

		if (value0 != null && value1 != null && value2 != null
				&& value3 != null) {

			double coefficient0 = 2 * value1;
			double coefficient1 = value2 - value0;
			double coefficient2 = 2 * value0 - 5 * value1 + 4 * value2 - value3;
			double coefficient3 = -value0 + 3 * value1 - 3 * value2 + value3;
			value = (coefficient3 * offset * offset * offset
					+ coefficient2 * offset * offset + coefficient1 * offset
					+ coefficient0) / 2;
		}

		return value;
	}

	/**
	 * Pad the bounding box with extra space for the overlapping pixels
	 * 
	 * @param tileMatrix
	 *            tile matrix
	 * @param boundingBox
	 *            bounding box
	 * @param overlap
	 *            overlapping pixels
	 * @return padded bounding box
	 */
	protected BoundingBox padBoundingBox(TileMatrix tileMatrix,
			BoundingBox boundingBox, int overlap) {
		double lonPixelPadding = tileMatrix.getPixelXSize() * overlap;
		double latPixelPadding = tileMatrix.getPixelYSize() * overlap;
		BoundingBox paddedBoundingBox = new BoundingBox(
				boundingBox.getMinLongitude() - lonPixelPadding,
				boundingBox.getMinLatitude() - latPixelPadding,
				boundingBox.getMaxLongitude() + lonPixelPadding,
				boundingBox.getMaxLatitude() + latPixelPadding);
		return paddedBoundingBox;
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
	 * Get the pixel value as a 16 bit unsigned value at the coordinate from the
	 * 16 bit unsigned pixel values
	 * 
	 * @param unsignedPixelValues
	 *            unsigned int pixel values
	 * @param width
	 *            image width
	 * @param x
	 *            x coordinate
	 * @param y
	 *            y coordinate
	 * @return 16 bit unsigned pixel value
	 */
	public int getUnsignedPixelValue(int[] unsignedPixelValues, int width,
			int x, int y) {
		return unsignedPixelValues[(y * width) + x];
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
	 * Get the coverage data value for the "unsigned short" pixel value
	 * 
	 * @param griddedTile
	 *            gridded tile
	 * @param pixelValue
	 *            pixel value as an unsigned short
	 * @return coverage data value
	 */
	public Double getValue(GriddedTile griddedTile, short pixelValue) {
		int unsignedPixelValue = getUnsignedPixelValue(pixelValue);
		Double value = getValue(griddedTile, unsignedPixelValue);
		return value;
	}

	/**
	 * Get the coverage data value for the unsigned short pixel value
	 * 
	 * @param griddedTile
	 *            gridded tile
	 * @param unsignedPixelValue
	 *            pixel value as an unsigned 16 bit integer
	 * @return coverage data value
	 */
	public Double getValue(GriddedTile griddedTile, int unsignedPixelValue) {

		Double value = null;
		if (!isDataNull(unsignedPixelValue)) {
			value = pixelValueToValue(griddedTile,
					new Double(unsignedPixelValue));
		}

		return value;
	}

	/**
	 * Convert integer coverage typed pixel value to a coverage data value
	 * through scales and offsets
	 * 
	 * @param griddedTile
	 *            gridded tile
	 * @param pixelValue
	 *            pixel value
	 * @return coverage data value
	 */
	private Double pixelValueToValue(GriddedTile griddedTile,
			Double pixelValue) {

		Double value = pixelValue;

		if (griddedCoverage != null && griddedCoverage
				.getDataType() == GriddedCoverageDataType.INTEGER) {

			if (griddedTile != null) {
				value *= griddedTile.getScale();
				value += griddedTile.getOffset();
			}
			value *= griddedCoverage.getScale();
			value += griddedCoverage.getOffset();

		}

		return value;
	}

	/**
	 * Get the coverage data values from the "unsigned short" pixel values
	 * 
	 * @param griddedTile
	 *            gridded tile
	 * @param pixelValues
	 *            pixel values as "unsigned shorts"
	 * @return coverage data values
	 */
	public Double[] getValues(GriddedTile griddedTile, short[] pixelValues) {
		Double[] values = new Double[pixelValues.length];
		for (int i = 0; i < pixelValues.length; i++) {
			values[i] = getValue(griddedTile, pixelValues[i]);
		}
		return values;
	}

	/**
	 * Get the coverage data values from the "unsigned short" pixel values
	 * 
	 * @param griddedTile
	 *            gridded tile
	 * @param unsignedPixelValues
	 *            pixel values as 16 bit integers
	 * @return coverage data values
	 */
	public Double[] getValues(GriddedTile griddedTile,
			int[] unsignedPixelValues) {
		Double[] values = new Double[unsignedPixelValues.length];
		for (int i = 0; i < unsignedPixelValues.length; i++) {
			values[i] = getValue(griddedTile, unsignedPixelValues[i]);
		}
		return values;
	}

	/**
	 * Create the coverage data tile table
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * @param metadata
	 *            tile table metadata
	 * @return tile table
	 * @since 4.0.0
	 */
	public static TileTable createTileTable(GeoPackageCore geoPackage,
			TileTableMetadata metadata) {
		metadata.setDataType(GRIDDED_COVERAGE);
		return geoPackage.createTileTable(metadata);
	}

	/**
	 * Get the unsigned 16 bit integer pixel value of the coverage data value
	 * 
	 * @param griddedTile
	 *            gridded tile
	 * @param value
	 *            coverage data value
	 * @return 16 bit integer pixel value
	 */
	public int getUnsignedPixelValue(GriddedTile griddedTile, Double value) {

		int unsignedPixelValue = 0;

		if (value == null) {
			if (griddedCoverage != null) {
				unsignedPixelValue = griddedCoverage.getDataNull().intValue();
			}
		} else {
			double pixelValue = valueToPixelValue(griddedTile, value);
			unsignedPixelValue = (int) Math.round(pixelValue);
		}

		return unsignedPixelValue;
	}

	/**
	 * Convert integer coverage typed coverage data value to a pixel value
	 * through offsets and scales
	 * 
	 * @param griddedTile
	 *            gridded tile
	 * @param value
	 *            coverage data value
	 * @return pixel value
	 */
	private double valueToPixelValue(GriddedTile griddedTile, double value) {

		double pixelValue = value;

		if (griddedCoverage != null && griddedCoverage
				.getDataType() == GriddedCoverageDataType.INTEGER) {

			pixelValue -= griddedCoverage.getOffset();
			pixelValue /= griddedCoverage.getScale();
			if (griddedTile != null) {
				pixelValue -= griddedTile.getOffset();
				pixelValue /= griddedTile.getScale();
			}

		}

		return pixelValue;
	}

	/**
	 * Get the "unsigned short" pixel value of the coverage data value
	 * 
	 * @param griddedTile
	 *            gridded tile
	 * @param value
	 *            coverage data value
	 * @return "unsigned short" pixel value
	 */
	public short getPixelValue(GriddedTile griddedTile, Double value) {
		int unsignedPixelValue = getUnsignedPixelValue(griddedTile, value);
		short pixelValue = getPixelValue(unsignedPixelValue);
		return pixelValue;
	}

	/**
	 * Get the pixel value at the coordinate from the pixel values
	 * 
	 * @param pixelValues
	 *            pixel values
	 * @param width
	 *            image width
	 * @param x
	 *            x coordinate
	 * @param y
	 *            y coordinate
	 * @return pixel value
	 */
	public float getPixelValue(float[] pixelValues, int width, int x, int y) {
		return pixelValues[(y * width) + x];
	}

	/**
	 * Get the coverage data value for the pixel value
	 * 
	 * @param griddedTile
	 *            gridded tile
	 * @param pixelValue
	 *            pixel value
	 * @return coverage data value
	 */
	public Double getValue(GriddedTile griddedTile, float pixelValue) {

		Double value = null;
		if (!isDataNull(pixelValue)) {
			value = pixelValueToValue(griddedTile, new Double(pixelValue));
		}

		return value;
	}

	/**
	 * Get the coverage data values from the pixel values
	 * 
	 * @param griddedTile
	 *            gridded tile
	 * @param pixelValues
	 *            pixel values
	 * @return coverage data values
	 */
	public Double[] getValues(GriddedTile griddedTile, float[] pixelValues) {
		Double[] values = new Double[pixelValues.length];
		for (int i = 0; i < pixelValues.length; i++) {
			values[i] = getValue(griddedTile, pixelValues[i]);
		}
		return values;
	}

	/**
	 * Get the pixel value of the coverage data value
	 * 
	 * @param griddedTile
	 *            gridded tile
	 * @param value
	 *            coverage data value
	 * @return pixel value
	 */
	public float getFloatPixelValue(GriddedTile griddedTile, Double value) {

		double pixel = 0;
		if (value == null) {
			if (griddedCoverage != null) {
				pixel = griddedCoverage.getDataNull();
			}
		} else {
			pixel = valueToPixelValue(griddedTile, value);
		}

		float pixelValue = (float) pixel;

		return pixelValue;
	}

	/**
	 * Get the coverage data value at the coordinate
	 * 
	 * @param latitude
	 *            latitude
	 * @param longitude
	 *            longitude
	 * @return coverage data value
	 */
	public Double getValue(double latitude, double longitude) {
		CoverageDataRequest request = new CoverageDataRequest(latitude,
				longitude);
		CoverageDataResults values = getValues(request, 1, 1);
		Double value = null;
		if (values != null) {
			value = values.getValues()[0][0];
		}
		return value;
	}

	/**
	 * Get the coverage data values within the bounding box
	 * 
	 * @param requestBoundingBox
	 *            request bounding box
	 * @return coverage data results
	 */
	public CoverageDataResults getValues(BoundingBox requestBoundingBox) {
		CoverageDataRequest request = new CoverageDataRequest(
				requestBoundingBox);
		CoverageDataResults values = getValues(request);
		return values;
	}

	/**
	 * Get the coverage data values within the bounding box with the requested
	 * width and height result size
	 * 
	 * @param requestBoundingBox
	 *            request bounding box
	 * @param width
	 *            coverage data request width
	 * @param height
	 *            coverage data request height
	 * @return coverage data results
	 */
	public CoverageDataResults getValues(BoundingBox requestBoundingBox,
			Integer width, Integer height) {
		CoverageDataRequest request = new CoverageDataRequest(
				requestBoundingBox);
		CoverageDataResults values = getValues(request, width, height);
		return values;
	}

	/**
	 * Get the requested coverage data values
	 * 
	 * @param request
	 *            coverage data request
	 * @return coverage data results
	 */
	public CoverageDataResults getValues(CoverageDataRequest request) {
		CoverageDataResults values = getValues(request, width, height);
		return values;
	}

	/**
	 * Get the unbounded coverage data values within the bounding box. Unbounded
	 * results retrieves and returns each coverage data pixel. The results size
	 * equals the width and height of all matching pixels.
	 * 
	 * @param requestBoundingBox
	 *            request bounding box
	 * @return coverage data results
	 */
	public CoverageDataResults getValuesUnbounded(
			BoundingBox requestBoundingBox) {
		CoverageDataRequest request = new CoverageDataRequest(
				requestBoundingBox);
		return getValuesUnbounded(request);
	}

	/**
	 * Get the bilinear interpolation coverage data value
	 * 
	 * @param griddedTile
	 *            gridded tile
	 * @param image
	 *            image
	 * @param leftLastColumns
	 *            last columns in the tile to the left
	 * @param topLeftRows
	 *            last rows of the tile to the top left
	 * @param topRows
	 *            last rows of the tile to the top
	 * @param y
	 *            y coordinate
	 * @param x
	 *            x coordinate
	 * @param widthRatio
	 *            width source over destination ratio
	 * @param heightRatio
	 *            height source over destination ratio
	 * @param destTop
	 *            destination top most pixel
	 * @param destLeft
	 *            destination left most pixel
	 * @param srcTop
	 *            source top most pixel
	 * @param srcLeft
	 *            source left most pixel
	 * @return bilinear coverage data value
	 */
	protected Double getBilinearInterpolationValue(GriddedTile griddedTile,
			TImage image, Double[][] leftLastColumns, Double[][] topLeftRows,
			Double[][] topRows, int y, int x, float widthRatio,
			float heightRatio, float destTop, float destLeft, float srcTop,
			float srcLeft) {

		// Determine which source pixel to use
		float xSource = getXSource(x, destLeft, srcLeft, widthRatio);
		float ySource = getYSource(y, destTop, srcTop, heightRatio);

		CoverageDataSourcePixel sourcePixelX = getXSourceMinAndMax(xSource);
		CoverageDataSourcePixel sourcePixelY = getYSourceMinAndMax(ySource);

		Double[][] values = new Double[2][2];
		populateValues(griddedTile, image, leftLastColumns, topLeftRows,
				topRows, sourcePixelX, sourcePixelY, values);

		Double value = null;

		if (values != null) {
			value = getBilinearInterpolationValue(sourcePixelX, sourcePixelY,
					values);
		}

		return value;
	}

	/**
	 * Get the bicubic interpolation coverage data value
	 * 
	 * @param griddedTile
	 *            gridded tile
	 * @param image
	 *            image
	 * @param leftLastColumns
	 *            last columns in the tile to the left
	 * @param topLeftRows
	 *            last rows of the tile to the top left
	 * @param topRows
	 *            last rows of the tile to the top
	 * @param y
	 *            y coordinate
	 * @param x
	 *            x coordinate
	 * @param widthRatio
	 *            width source over destination ratio
	 * @param heightRatio
	 *            height source over destination ratio
	 * @param destTop
	 *            destination top most pixel
	 * @param destLeft
	 *            destination left most pixel
	 * @param srcTop
	 *            source top most pixel
	 * @param srcLeft
	 *            source left most pixel
	 * @return bicubic coverage data value
	 */
	protected Double getBicubicInterpolationValue(GriddedTile griddedTile,
			TImage image, Double[][] leftLastColumns, Double[][] topLeftRows,
			Double[][] topRows, int y, int x, float widthRatio,
			float heightRatio, float destTop, float destLeft, float srcTop,
			float srcLeft) {

		// Determine which source pixel to use
		float xSource = getXSource(x, destLeft, srcLeft, widthRatio);
		float ySource = getYSource(y, destTop, srcTop, heightRatio);

		CoverageDataSourcePixel sourcePixelX = getXSourceMinAndMax(xSource);
		sourcePixelX.setMin(sourcePixelX.getMin() - 1);
		sourcePixelX.setMax(sourcePixelX.getMax() + 1);

		CoverageDataSourcePixel sourcePixelY = getYSourceMinAndMax(ySource);
		sourcePixelY.setMin(sourcePixelY.getMin() - 1);
		sourcePixelY.setMax(sourcePixelY.getMax() + 1);

		Double[][] values = new Double[4][4];
		populateValues(griddedTile, image, leftLastColumns, topLeftRows,
				topRows, sourcePixelX, sourcePixelY, values);

		Double value = null;

		if (values != null) {
			value = getBicubicInterpolationValue(values, sourcePixelX,
					sourcePixelY);
		}

		return value;
	}

	/**
	 * Populate the coverage data values
	 * 
	 * @param griddedTile
	 *            gridded tile
	 * @param image
	 *            image
	 * @param leftLastColumns
	 *            last columns in the tile to the left
	 * @param topLeftRows
	 *            last rows of the tile to the top left
	 * @param topRows
	 *            last rows of the tile to the top
	 * @param pixelX
	 *            source x pixel
	 * @param pixelY
	 *            source y pixel
	 * @param values
	 *            values to populate
	 */
	private void populateValues(GriddedTile griddedTile, TImage image,
			Double[][] leftLastColumns, Double[][] topLeftRows,
			Double[][] topRows, CoverageDataSourcePixel pixelX,
			CoverageDataSourcePixel pixelY, Double[][] values) {

		populateValues(griddedTile, image, leftLastColumns, topLeftRows,
				topRows, pixelX.getMin(), pixelX.getMax(), pixelY.getMin(),
				pixelY.getMax(), values);
	}

	/**
	 * Populate the coverage data values
	 * 
	 * @param griddedTile
	 *            gridded tile
	 * @param image
	 *            image
	 * @param leftLastColumns
	 *            last columns in the tile to the left
	 * @param topLeftRows
	 *            last rows of the tile to the top left
	 * @param topRows
	 *            last rows of the tile to the top
	 * @param minX
	 *            min x coordinate
	 * @param maxX
	 *            max x coordinate
	 * @param minY
	 *            min y coordinate
	 * @param maxY
	 *            max y coordinate
	 * @param values
	 *            values to populate
	 */
	private void populateValues(GriddedTile griddedTile, TImage image,
			Double[][] leftLastColumns, Double[][] topLeftRows,
			Double[][] topRows, int minX, int maxX, int minY, int maxY,
			Double[][] values) {

		for (int yLocation = maxY; values != null
				&& yLocation >= minY; yLocation--) {
			for (int xLocation = maxX; xLocation >= minX; xLocation--) {
				Double value = getValueOverBorders(griddedTile, image,
						leftLastColumns, topLeftRows, topRows, xLocation,
						yLocation);
				if (value == null) {
					values = null;
					break;
				} else {
					values[yLocation - minY][xLocation - minX] = value;
				}
			}
		}
	}

	/**
	 * Get the nearest neighbor coverage data value
	 * 
	 * @param griddedTile
	 *            gridded tile
	 * @param image
	 *            image
	 * @param leftLastColumns
	 *            last columns in the tile to the left
	 * @param topLeftRows
	 *            last rows of the tile to the top left
	 * @param topRows
	 *            last rows of the tile to the top
	 * @param y
	 *            y coordinate
	 * @param x
	 *            x coordinate
	 * @param widthRatio
	 *            width source over destination ratio
	 * @param heightRatio
	 *            height source over destination ratio
	 * @param destTop
	 *            destination top most pixel
	 * @param destLeft
	 *            destination left most pixel
	 * @param srcTop
	 *            source top most pixel
	 * @param srcLeft
	 *            source left most pixel
	 * @return nearest neighbor coverage data value
	 */
	protected Double getNearestNeighborValue(GriddedTile griddedTile,
			TImage image, Double[][] leftLastColumns, Double[][] topLeftRows,
			Double[][] topRows, int y, int x, float widthRatio,
			float heightRatio, float destTop, float destLeft, float srcTop,
			float srcLeft) {

		// Determine which source pixel to use
		float xSource = getXSource(x, destLeft, srcLeft, widthRatio);
		float ySource = getYSource(y, destTop, srcTop, heightRatio);

		// Get the closest nearest neighbors
		List<int[]> nearestNeighbors = getNearestNeighbors(xSource, ySource);

		// Get the coverage data value from the source pixel nearest neighbors
		// until one is found
		Double value = null;
		for (int[] nearestNeighbor : nearestNeighbors) {
			value = getValueOverBorders(griddedTile, image, leftLastColumns,
					topLeftRows, topRows, nearestNeighbor[0],
					nearestNeighbor[1]);
			if (value != null) {
				break;
			}
		}

		return value;
	}

	/**
	 * Get the coverage data value from the coordinate location. If the
	 * coordinate crosses the left, top, or top left tile, attempts to get the
	 * coverage data value from previously processed border coverage data
	 * values.
	 * 
	 * @param griddedTile
	 *            gridded tile
	 * @param image
	 *            image
	 * @param leftLastColumns
	 *            last columns in the tile to the left
	 * @param topLeftRows
	 *            last rows of the tile to the top left
	 * @param topRows
	 *            last rows of the tile to the top
	 * @param y
	 *            x coordinate
	 * @param y
	 *            y coordinate
	 * @return coverage data value
	 */
	private Double getValueOverBorders(GriddedTile griddedTile, TImage image,
			Double[][] leftLastColumns, Double[][] topLeftRows,
			Double[][] topRows, int x, int y) {
		Double value = null;

		// Only handle locations in the current tile, to the left, top left, or
		// top tiles. Tiles are processed sorted by rows and columns, so values
		// to the top right, right, or any below tiles will be handled later if
		// those tiles exist
		if (x < image.getWidth() && y < image.getHeight()) {

			if (x >= 0 && y >= 0) {
				value = getValue(griddedTile, image, x, y);
			} else if (x < 0 && y < 0) {
				// Try to get the coverage data value from the top left tile
				// values
				if (topLeftRows != null) {
					int row = (-1 * y) - 1;
					if (row < topLeftRows.length) {
						int column = x + topLeftRows[row].length;
						if (column >= 0) {
							value = topLeftRows[row][column];
						}
					}
				}
			} else if (x < 0) {
				// Try to get the coverage data value from the left tile values
				if (leftLastColumns != null) {
					int column = (-1 * x) - 1;
					if (column < leftLastColumns.length) {
						int row = y;
						if (row < leftLastColumns[column].length) {
							value = leftLastColumns[column][row];
						}
					}
				}
			} else {
				// Try to get the coverage data value from the top tile values
				if (topRows != null) {
					int row = (-1 * y) - 1;
					if (row < topRows.length) {
						int column = x;
						if (column < topRows[row].length) {
							value = topRows[row][column];
						}
					}
				}
			}

		}

		return value;
	}

}
