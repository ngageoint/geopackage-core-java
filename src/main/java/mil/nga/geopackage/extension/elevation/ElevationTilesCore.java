package mil.nga.geopackage.extension.elevation;

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferUShort;
import java.awt.image.WritableRaster;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mil.nga.geopackage.BoundingBox;
import mil.nga.geopackage.GeoPackageConstants;
import mil.nga.geopackage.GeoPackageCore;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.core.contents.Contents;
import mil.nga.geopackage.core.contents.ContentsDataType;
import mil.nga.geopackage.core.srs.SpatialReferenceSystem;
import mil.nga.geopackage.core.srs.SpatialReferenceSystemDao;
import mil.nga.geopackage.extension.BaseExtension;
import mil.nga.geopackage.extension.ExtensionScopeType;
import mil.nga.geopackage.extension.Extensions;
import mil.nga.geopackage.projection.ProjectionConstants;
import mil.nga.geopackage.property.GeoPackageProperties;
import mil.nga.geopackage.property.PropertyConstants;
import mil.nga.geopackage.tiles.matrixset.TileMatrixSet;
import mil.nga.geopackage.tiles.user.TileColumn;
import mil.nga.geopackage.tiles.user.TileTable;

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
	 * Constructor
	 * 
	 * @param geoPackage
	 * @param tileMatrixSet
	 */
	protected ElevationTilesCore(GeoPackageCore geoPackage,
			TileMatrixSet tileMatrixSet) {
		super(geoPackage);
		this.tileMatrixSet = tileMatrixSet;
		griddedCoverageDao = geoPackage.getGriddedCoverageDao();
		griddedTileDao = geoPackage.getGriddedTileDao();
		queryGriddedCoverage();
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
	 * @param tableName
	 *            table name
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
	 * Get the pixel value
	 * 
	 * @param image
	 *            tile image
	 * @param x
	 *            x coordinate
	 * @param y
	 *            y coordinate
	 * @return pixel value
	 */
	public short getPixelValue(BufferedImage image, int x, int y) {
		validateImageType(image);
		WritableRaster raster = image.getRaster();
		short pixelValue = getPixelValue(raster, x, y);
		return pixelValue;
	}

	/**
	 * Get the pixel value from the raster and the coordinate
	 * 
	 * @param raster
	 *            image raster
	 * @param x
	 *            x coordinate
	 * @param y
	 *            y coordinate
	 * @return pixel value
	 */
	public short getPixelValue(WritableRaster raster, int x, int y) {
		Object pixelData = raster.getDataElements(x, y, null);
		short sdata[] = (short[]) pixelData;
		if (sdata.length != 1) {
			throw new UnsupportedOperationException(
					"This method is not supported by this color model");
		}
		short pixelValue = sdata[0];

		return pixelValue;
	}

	/**
	 * Get the pixel values of the buffered image
	 * 
	 * @param image
	 *            tile image
	 * @return pixel values
	 */
	public short[] getPixelValues(BufferedImage image) {
		validateImageType(image);
		WritableRaster raster = image.getRaster();
		short[] pixelValues = getPixelValues(raster);
		return pixelValues;
	}

	/**
	 * Get the pixel values of the raster
	 * 
	 * @param raster
	 * @return pixel values
	 */
	public short[] getPixelValues(WritableRaster raster) {
		DataBufferUShort buffer = (DataBufferUShort) raster.getDataBuffer();
		short[] pixelValues = buffer.getData();
		return pixelValues;
	}

	/**
	 * Get the pixel value at the coordinate
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
	public short getPixelValue(short[] pixelValues, int width, int x, int y) {
		return pixelValues[(y * width) + x];
	}

	/**
	 * Validate that the image type is an unsigned short
	 * 
	 * @param image
	 *            tile image
	 */
	public void validateImageType(BufferedImage image) {
		if (image.getColorModel().getTransferType() != DataBuffer.TYPE_USHORT) {
			throw new GeoPackageException(
					"The elevation tile is expected to be a 16 bit unsigned short, actual: "
							+ image.getColorModel().getTransferType());
		}
	}

	/**
	 * Get the elevation value for the pixel value
	 * 
	 * @param griddedTile
	 *            gridded tile
	 * @param pixelValue
	 *            pixel value
	 * @return elevation value
	 */
	public double getElevationValue(GriddedTile griddedTile, short pixelValue) {

		GriddedCoverage coverage = null;
		if (griddedCoverage != null && !griddedCoverage.isEmpty()) {
			coverage = griddedCoverage.get(0);
		}

		double elevation = pixelValue;

		if (griddedTile != null) {
			elevation = pixelValue * griddedTile.getScale()
					+ griddedTile.getOffset();
		}
		if (coverage != null) {
			elevation = elevation * coverage.getScale() + coverage.getOffset();
		}

		return elevation;
	}

	/**
	 * Get the elevation value
	 * 
	 * @param griddedTile
	 *            gridded tile
	 * @param image
	 *            tile image
	 * @param x
	 *            x coordinate
	 * @param y
	 *            y coordinate
	 * @return elevation value
	 */
	public double getElevationValue(GriddedTile griddedTile,
			BufferedImage image, int x, int y) {
		short pixelValue = getPixelValue(image, x, y);
		double elevation = getElevationValue(griddedTile, pixelValue);
		return elevation;
	}

	/**
	 * Get the elevation value
	 * 
	 * @param griddedTile
	 *            gridded tile
	 * @param raster
	 *            image raster
	 * @param x
	 *            x coordinate
	 * @param y
	 *            y coordinate
	 * @return elevation value
	 */
	public double getElevationValue(GriddedTile griddedTile,
			WritableRaster raster, int x, int y) {
		short pixelValue = getPixelValue(raster, x, y);
		double elevation = getElevationValue(griddedTile, pixelValue);
		return elevation;
	}

	/**
	 * Get the elevation values
	 * 
	 * @param griddedTile
	 *            gridded tile
	 * @param image
	 *            tile image
	 * @return elevation values
	 */
	public double[] getElevationValues(GriddedTile griddedTile,
			BufferedImage image) {
		short[] pixelValues = getPixelValues(image);
		double[] elevations = getElevationValues(griddedTile, pixelValues);
		return elevations;
	}

	/**
	 * Get the elevation values
	 * 
	 * @param griddedTile
	 *            gridded tile
	 * @param raster
	 *            raster image
	 * @return elevation values
	 */
	public double[] getElevationValues(GriddedTile griddedTile,
			WritableRaster raster) {
		short[] pixelValues = getPixelValues(raster);
		double[] elevations = getElevationValues(griddedTile, pixelValues);
		return elevations;
	}

	/**
	 * Get the elevation values
	 * 
	 * @param griddedTile
	 *            gridded tile
	 * @param pixelValues
	 *            pixel values
	 * @return elevation values
	 */
	public double[] getElevationValues(GriddedTile griddedTile,
			short[] pixelValues) {
		double[] elevations = new double[pixelValues.length];
		for (int i = 0; i < pixelValues.length; i++) {
			elevations[i] = getElevationValue(griddedTile, pixelValues[i]);
		}
		return elevations;
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
	public static void createTileTableWithMetadata(GeoPackageCore geoPackage,
			String tableName, BoundingBox contentsBoundingBox,
			long contentsSrsId, BoundingBox tileMatrixSetBoundingBox,
			long tileMatrixSetSrsId) {

		// Get the SRS
		SpatialReferenceSystemDao srsDao = geoPackage
				.getSpatialReferenceSystemDao();
		SpatialReferenceSystem contentsSrs;
		try {
			contentsSrs = srsDao
					.getOrCreateFromEpsg(ProjectionConstants.EPSG_EPSG_WORLD_GEODETIC_SYSTEM_GEOGRAPHICAL_3D);
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to retrieve Spatial Reference System. EPSG: "
							+ ProjectionConstants.EPSG_EPSG_WORLD_GEODETIC_SYSTEM_GEOGRAPHICAL_3D,
					e);
		}
		SpatialReferenceSystem tileMatrixSetSrs = getSrs(srsDao,
				tileMatrixSetSrsId);

		// Create the Tile Matrix Set and Tile Matrix tables
		geoPackage.createTileMatrixSetTable();
		geoPackage.createTileMatrixTable();

		// Create the user tile table
		List<TileColumn> columns = TileTable.createRequiredColumns();
		TileTable table = new TileTable(tableName, columns);
		geoPackage.createTileTable(table);

		try {
			// Create the contents
			Contents contents = new Contents();
			contents.setTableName(tableName);
			contents.setDataType(ContentsDataType.ELEVATION_TILES);
			contents.setIdentifier(tableName);
			contents.setLastChange(new Date());
			contents.setMinX(contentsBoundingBox.getMinLongitude());
			contents.setMinY(contentsBoundingBox.getMinLatitude());
			contents.setMaxX(contentsBoundingBox.getMaxLongitude());
			contents.setMaxY(contentsBoundingBox.getMaxLatitude());
			contents.setSrs(contentsSrs);
			geoPackage.getContentsDao().create(contents);

			// Create new matrix tile set
			TileMatrixSet tileMatrixSet = new TileMatrixSet();
			tileMatrixSet.setContents(contents);
			tileMatrixSet.setSrs(tileMatrixSetSrs);
			tileMatrixSet.setMinX(tileMatrixSetBoundingBox.getMinLongitude());
			tileMatrixSet.setMinY(tileMatrixSetBoundingBox.getMinLatitude());
			tileMatrixSet.setMaxX(tileMatrixSetBoundingBox.getMaxLongitude());
			tileMatrixSet.setMaxY(tileMatrixSetBoundingBox.getMaxLatitude());
			geoPackage.getTileMatrixSetDao().create(tileMatrixSet);

		} catch (RuntimeException e) {
			geoPackage.deleteTableQuietly(tableName);
			throw e;
		} catch (SQLException e) {
			geoPackage.deleteTableQuietly(tableName);
			throw new GeoPackageException(
					"Failed to create table and metadata: " + tableName, e);
		}
	}

	/**
	 * Get the Spatial Reference System by id
	 *
	 * @param srsDao
	 * @param srsId
	 * @return
	 */
	private static SpatialReferenceSystem getSrs(
			SpatialReferenceSystemDao srsDao, long srsId) {
		SpatialReferenceSystem srs;
		try {
			srs = srsDao.queryForId(srsId);
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to retrieve Spatial Reference System. SRS ID: "
							+ srsId, e);
		}
		if (srs == null) {
			throw new GeoPackageException(
					"Spatial Reference System could not be found. SRS ID: "
							+ srsId);
		}
		return srs;
	}

}
