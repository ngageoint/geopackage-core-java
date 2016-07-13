package mil.nga.geopackage.extension.elevation;

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
 * Tiled Gridded Elevation Data Extension
 * 
 * @author osbornb
 * @since 1.2.1
 */
public class ElevationTiles extends BaseExtension {

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
	 * Constructor
	 * 
	 * @param geoPackage
	 * @param tileMatrixSet
	 */
	protected ElevationTiles(GeoPackageCore geoPackage,
			TileMatrixSet tileMatrixSet) {
		super(geoPackage);
		this.tileMatrixSet = tileMatrixSet;
		griddedCoverageDao = geoPackage.getGriddedCoverageDao();
		griddedTileDao = geoPackage.getGriddedTileDao();
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
	 * Create the elevation tile table with metadata
	 * 
	 * @param geoPackage
	 * @param tableName
	 * @param contentsBoundingBox
	 * @param contentsSrsId
	 * @param tileMatrixSetBoundingBox
	 * @param tileMatrixSetSrsId
	 * @return elevation tiles
	 */
	public static ElevationTiles createTileTableWithMetadata(
			GeoPackageCore geoPackage, String tableName,
			BoundingBox contentsBoundingBox, long contentsSrsId,
			BoundingBox tileMatrixSetBoundingBox, long tileMatrixSetSrsId) {

		ElevationTiles elevationTiles = null;

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

			elevationTiles = new ElevationTiles(geoPackage, tileMatrixSet);

		} catch (RuntimeException e) {
			geoPackage.deleteTableQuietly(tableName);
			throw e;
		} catch (SQLException e) {
			geoPackage.deleteTableQuietly(tableName);
			throw new GeoPackageException(
					"Failed to create table and metadata: " + tableName, e);
		}

		return elevationTiles;
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
