package mil.nga.geopackage.dgiwg;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import mil.nga.crs.CRS;
import mil.nga.crs.CRSType;
import mil.nga.crs.operation.OperationMethods;
import mil.nga.crs.projected.ProjectedCoordinateReferenceSystem;
import mil.nga.crs.wkt.CRSReader;
import mil.nga.geopackage.GeoPackageCore;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.contents.ContentsDataType;
import mil.nga.geopackage.features.columns.GeometryColumns;
import mil.nga.geopackage.srs.SpatialReferenceSystem;
import mil.nga.geopackage.tiles.matrix.TileMatrix;
import mil.nga.geopackage.tiles.matrixset.TileMatrixSet;
import mil.nga.proj.Projection;

/**
 * Performs DGIWG (Defence Geospatial Information Working Group) GeoPackage
 * validations
 * 
 * @author osbornb
 * @since 6.1.2
 */
public class DGIWGValidate {

	/**
	 * Is the GeoPackage valid according to the DGIWG GeoPackage Profile
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * @return true if valid
	 */
	public static boolean isValid(GeoPackageCore geoPackage) {
		return false; // TODO
	}

	/**
	 * Validate the GeoPackage against the DGIWG GeoPackage Profile
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 */
	public static void validate(GeoPackageCore geoPackage) {

		for (String tileTable : geoPackage.getTileTables()) {
			validateTileTable(geoPackage, tileTable);
		}

		for (String featureTable : geoPackage.getFeatureTables()) {
			validateFeatureTable(geoPackage, featureTable);
		}

	}

	/**
	 * Validate the tile coordinate reference system
	 * 
	 * @param tileTable
	 *            tile table
	 * @param srs
	 *            spatial reference system
	 */
	public static void validateTileCoordinateReferenceSystem(String tileTable,
			SpatialReferenceSystem srs) {

		CoordinateReferenceSystem crs = validateCoordinateReferenceSystem(
				tileTable, srs, ContentsDataType.TILES);

		if (crs == null) {

			Projection projection = srs.getProjection();

			CRS definitionCrs = projection.getDefinitionCRS();
			if (definitionCrs == null) {
				String definition = projection.getDefinition();
				if (definition != null) {
					try {
						definitionCrs = CRSReader.read(definition);
					} catch (IOException e) {
						// TODO validation error
						throw new GeoPackageException("TODO");
					}
				}
			}

			if (definitionCrs != null) {

				boolean valid = false;

				if (definitionCrs.getType() == CRSType.PROJECTED
						&& definitionCrs instanceof ProjectedCoordinateReferenceSystem) {
					ProjectedCoordinateReferenceSystem projected = (ProjectedCoordinateReferenceSystem) definitionCrs;
					OperationMethods operationMethod = projected
							.getMapProjection().getMethod().getMethod();
					switch (operationMethod) {
					case LAMBERT_CONIC_CONFORMAL_1SP:
					case LAMBERT_CONIC_CONFORMAL_2SP:
						valid = true;
						break;
					default:
					}
				}

				if (!valid) {
					// TODO validation error
					throw new GeoPackageException("TODO");
				}

			} else {
				// TODO validation error
				throw new GeoPackageException("TODO");
			}

		}

	}

	/**
	 * Validate the feature coordinate reference system
	 * 
	 * @param featureTable
	 *            feature table
	 * @param srs
	 *            spatial reference system
	 */
	public static void validateFeatureCoordinateReferenceSystem(
			String featureTable, SpatialReferenceSystem srs) {

		CoordinateReferenceSystem crs = validateCoordinateReferenceSystem(
				featureTable, srs, ContentsDataType.FEATURES);

		if (crs == null) {
			// TODO validation error
			throw new GeoPackageException("TODO");
		}

	}

	/**
	 * Validate the coordinate reference system
	 * 
	 * @param table
	 *            table name
	 * @param srs
	 *            spatial reference system
	 * @param type
	 *            contents data type
	 * @return coordinate reference system
	 */
	private static CoordinateReferenceSystem validateCoordinateReferenceSystem(
			String table, SpatialReferenceSystem srs, ContentsDataType type) {

		CoordinateReferenceSystem crs = CoordinateReferenceSystem
				.getCoordinateReferenceSystem(srs);

		if (crs != null) {

			boolean valid = false;

			for (DataType dataType : crs.getDataTypes()) {
				if (dataType.getDataType() == type) {
					valid = true;
					break;
				}
			}

			if (!valid) {
				// TODO validation error
				throw new GeoPackageException("TODO");
			}

			if (!srs.getSrsName().equalsIgnoreCase(crs.getName())) {
				// TODO validation error
				throw new GeoPackageException("TODO");
			}

			if (srs.getSrsId() != crs.getCode()) {
				// TODO validation error
				throw new GeoPackageException("TODO");
			}

			if (!srs.getOrganization().equalsIgnoreCase(crs.getAuthority())) {
				// TODO validation error
				throw new GeoPackageException("TODO");
			}

			if (srs.getOrganizationCoordsysId() != crs.getCode()) {
				// TODO validation error
				throw new GeoPackageException("TODO");
			}

		}

		return crs;
	}

	/**
	 * Validate tile table
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * @param tileTable
	 *            tile table
	 */
	public static void validateTileTable(GeoPackageCore geoPackage,
			String tileTable) {

		TileMatrixSet tileMatrixSet = null;
		try {
			tileMatrixSet = geoPackage.getTileMatrixSetDao()
					.queryForId(tileTable);
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to retrieve Tile Matrix Set for tile table: "
							+ tileTable,
					e);
		}

		validateTileCoordinateReferenceSystem(tileTable,
				tileMatrixSet.getSrs());

		List<TileMatrix> tileMatrices;
		try {
			tileMatrices = geoPackage.getTileMatrixDao()
					.queryForTableName(tileTable);
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to retrieve Tile Matrices for tile table: "
							+ tileTable,
					e);
		}

		if (tileMatrices == null || tileMatrices.isEmpty()) {
			// TODO validation error
			throw new GeoPackageException("TODO");
		} else {

			TileMatrix previousTileMatrix = null;

			for (TileMatrix tileMatrix : tileMatrices) {

				if (tileMatrix.getTileWidth() != DGIWGConstants.TILE_WIDTH) {
					// TODO validation error
					throw new GeoPackageException("TODO");
				}

				if (tileMatrix.getTileHeight() != DGIWGConstants.TILE_HEIGHT) {
					// TODO validation error
					throw new GeoPackageException("TODO");
				}

				if (previousTileMatrix != null) {

					long zoomChange = tileMatrix.getZoomLevel()
							- previousTileMatrix.getZoomLevel();
					double factor = Math.pow(2, zoomChange);
					double pixelXSize = previousTileMatrix.getPixelXSize()
							/ factor;
					double pixelYSize = previousTileMatrix.getPixelYSize()
							/ factor;

					if (tileMatrix.getPixelXSize() != pixelXSize) {
						// TODO validation error
						throw new GeoPackageException("TODO");
					}

					if (tileMatrix.getPixelYSize() != pixelYSize) {
						// TODO validation error
						throw new GeoPackageException("TODO");
					}

				}

				previousTileMatrix = tileMatrix;
			}

		}

	}

	/**
	 * Validate feature table
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * @param featureTable
	 *            feature table
	 */
	public static void validateFeatureTable(GeoPackageCore geoPackage,
			String featureTable) {

		GeometryColumns geometryColumns = null;
		try {
			geometryColumns = geoPackage.getGeometryColumnsDao()
					.queryForTableName(featureTable);
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to retrieve Geometry Columns for feature table: "
							+ featureTable,
					e);
		}

		validateFeatureCoordinateReferenceSystem(featureTable,
				geometryColumns.getSrs());

	}

}
