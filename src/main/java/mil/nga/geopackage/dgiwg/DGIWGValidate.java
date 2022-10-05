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
import mil.nga.proj.ProjectionConstants;

/**
 * Performs DGIWG (Defence Geospatial Information Working Group) GeoPackage
 * validations
 * 
 * @author osbornb
 * @since 6.5.1
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
		return validate(geoPackage).isValid();
	}

	/**
	 * Validate the GeoPackage against the DGIWG GeoPackage Profile
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * @return validation errors
	 */
	public static DGIWGValidationErrors validate(GeoPackageCore geoPackage) {

		DGIWGValidationErrors errors = new DGIWGValidationErrors();

		for (String tileTable : geoPackage.getTileTables()) {
			errors.add(validateTileTable(geoPackage, tileTable));
		}

		for (String featureTable : geoPackage.getFeatureTables()) {
			errors.add(validateFeatureTable(geoPackage, featureTable));
		}

		return errors;
	}

	/**
	 * Validate the tile coordinate reference system
	 * 
	 * @param tileTable
	 *            tile table
	 * @param srs
	 *            spatial reference system
	 * @return validation errors
	 */
	public static DGIWGValidationErrors validateTileCoordinateReferenceSystem(
			String tileTable, SpatialReferenceSystem srs) {

		DGIWGValidationErrors errors = new DGIWGValidationErrors();

		CoordinateReferenceSystem crs = validateCoordinateReferenceSystem(
				errors, tileTable, srs, ContentsDataType.TILES);

		if (crs == null) {

			Projection projection = srs.getProjection();
			String definition = projection.getDefinition();

			CRS definitionCrs = projection.getDefinitionCRS();
			if (definitionCrs == null) {
				if (definition != null) {
					try {
						definitionCrs = CRSReader.read(definition);
					} catch (IOException e) {
						errors.add(new DGIWGValidationError(
								SpatialReferenceSystem.TABLE_NAME,
								SpatialReferenceSystem.COLUMN_DEFINITION,
								definition,
								"Failed to read tiles coordinate reference system definition: "
										+ e.getMessage(),
								primaryKey(srs)));
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
					errors.add(new DGIWGValidationError(
							SpatialReferenceSystem.TABLE_NAME,
							SpatialReferenceSystem.COLUMN_DEFINITION,
							definition,
							"Unsupported tiles coordinate reference system",
							primaryKey(srs)));
				}

			} else if (!errors.hasErrors()) {
				errors.add(new DGIWGValidationError(
						SpatialReferenceSystem.TABLE_NAME,
						SpatialReferenceSystem.COLUMN_DEFINITION, definition,
						"Failed to read tiles coordinate reference system definition",
						primaryKey(srs)));
			}

		}

		return errors;
	}

	/**
	 * Validate the feature coordinate reference system
	 * 
	 * @param featureTable
	 *            feature table
	 * @param srs
	 *            spatial reference system
	 * @return validation errors
	 */
	public static DGIWGValidationErrors validateFeatureCoordinateReferenceSystem(
			String featureTable, SpatialReferenceSystem srs) {

		DGIWGValidationErrors errors = new DGIWGValidationErrors();

		CoordinateReferenceSystem crs = validateCoordinateReferenceSystem(
				errors, featureTable, srs, ContentsDataType.FEATURES);

		if (crs == null) {
			errors.add(
					new DGIWGValidationError(SpatialReferenceSystem.TABLE_NAME,
							SpatialReferenceSystem.COLUMN_DEFINITION,
							srs.getProjectionDefinition(),
							"Unsupported features coordinate reference system",
							primaryKey(srs)));
		}

		return errors;
	}

	/**
	 * Validate the coordinate reference system
	 * 
	 * @param errors
	 *            validation errors
	 * @param table
	 *            table name
	 * @param srs
	 *            spatial reference system
	 * @param type
	 *            contents data type
	 * @return coordinate reference system
	 */
	private static CoordinateReferenceSystem validateCoordinateReferenceSystem(
			DGIWGValidationErrors errors, String table,
			SpatialReferenceSystem srs, ContentsDataType type) {

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
				errors.add(new DGIWGValidationError(
						SpatialReferenceSystem.TABLE_NAME,
						SpatialReferenceSystem.COLUMN_DEFINITION,
						srs.getProjectionDefinition(),
						"Unsupported " + type.getName()
								+ " coordinate reference system",
						primaryKey(srs)));
			}

			if (!srs.getSrsName().equalsIgnoreCase(crs.getName())) {
				errors.add(new DGIWGValidationError(
						SpatialReferenceSystem.TABLE_NAME,
						SpatialReferenceSystem.COLUMN_SRS_NAME,
						srs.getSrsName(), crs.getName(), primaryKey(srs)));
			}

			if (srs.getSrsId() != crs.getCode()) {
				errors.add(new DGIWGValidationError(
						SpatialReferenceSystem.TABLE_NAME,
						SpatialReferenceSystem.COLUMN_SRS_ID, srs.getSrsId(),
						crs.getCode(), primaryKey(srs)));
			}

			if (!srs.getOrganization().equalsIgnoreCase(crs.getAuthority())) {
				errors.add(new DGIWGValidationError(
						SpatialReferenceSystem.TABLE_NAME,
						SpatialReferenceSystem.COLUMN_ORGANIZATION,
						srs.getOrganization(), crs.getAuthority(),
						primaryKey(srs)));
			}

			if (srs.getOrganizationCoordsysId() != crs.getCode()) {
				errors.add(new DGIWGValidationError(
						SpatialReferenceSystem.TABLE_NAME,
						SpatialReferenceSystem.COLUMN_ORGANIZATION_COORDSYS_ID,
						srs.getOrganizationCoordsysId(), crs.getCode(),
						primaryKey(srs)));
			}

		} else {

			if (!srs.getOrganization()
					.equalsIgnoreCase(ProjectionConstants.AUTHORITY_EPSG)) {
				errors.add(new DGIWGValidationError(
						SpatialReferenceSystem.TABLE_NAME,
						SpatialReferenceSystem.COLUMN_ORGANIZATION,
						srs.getOrganization(),
						ProjectionConstants.AUTHORITY_EPSG, primaryKey(srs)));
			}

		}

		String description = srs.getDescription();
		if (description == null || description.isBlank()
				|| description
						.equalsIgnoreCase(DGIWGConstants.DESCRIPTION_UNKNOWN)
				|| description
						.equalsIgnoreCase(DGIWGConstants.DESCRIPTION_TBD)) {
			errors.add(
					new DGIWGValidationError(SpatialReferenceSystem.TABLE_NAME,
							SpatialReferenceSystem.COLUMN_DESCRIPTION,
							srs.getDescription(),
							"Invalid empty or unspecified description",
							primaryKey(srs)));
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
	 * @return validation errors
	 */
	public static DGIWGValidationErrors validateTileTable(
			GeoPackageCore geoPackage, String tileTable) {

		DGIWGValidationErrors errors = new DGIWGValidationErrors();

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

		if (tileMatrixSet != null) {
			errors.add(validateTileCoordinateReferenceSystem(tileTable,
					tileMatrixSet.getSrs()));
		} else {
			errors.add(new DGIWGValidationError(TileMatrixSet.TABLE_NAME,
					TileMatrixSet.COLUMN_TABLE_NAME, tileTable,
					"No Tile Matrix Set for tile table"));
		}

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
			errors.add(new DGIWGValidationError(TileMatrix.TABLE_NAME,
					TileMatrix.COLUMN_TABLE_NAME, tileTable,
					"No Tile Matrices for tile table",
					primaryKey(tileMatrixSet)));
		} else {

			TileMatrix previousTileMatrix = null;

			for (TileMatrix tileMatrix : tileMatrices) {

				long zoomLevel = tileMatrix.getZoomLevel();
				if (zoomLevel < DGIWGConstants.MIN_ZOOM_LEVEL
						|| zoomLevel > DGIWGConstants.MAX_ZOOM_LEVEL) {
					errors.add(new DGIWGValidationError(TileMatrix.TABLE_NAME,
							TileMatrix.COLUMN_ZOOM_LEVEL,
							tileMatrix.getZoomLevel(),
							DGIWGConstants.MIN_ZOOM_LEVEL + " <= "
									+ TileMatrix.COLUMN_ZOOM_LEVEL + " <= "
									+ DGIWGConstants.MAX_ZOOM_LEVEL,
							primaryKeys(tileMatrix)));
				}

				if (tileMatrix.getTileWidth() != DGIWGConstants.TILE_WIDTH) {
					errors.add(new DGIWGValidationError(TileMatrix.TABLE_NAME,
							TileMatrix.COLUMN_TILE_WIDTH,
							tileMatrix.getTileWidth(),
							DGIWGConstants.TILE_WIDTH,
							primaryKeys(tileMatrix)));
				}

				if (tileMatrix.getTileHeight() != DGIWGConstants.TILE_HEIGHT) {
					errors.add(new DGIWGValidationError(TileMatrix.TABLE_NAME,
							TileMatrix.COLUMN_TILE_HEIGHT,
							tileMatrix.getTileHeight(),
							DGIWGConstants.TILE_HEIGHT,
							primaryKeys(tileMatrix)));
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
						errors.add(
								new DGIWGValidationError(TileMatrix.TABLE_NAME,
										TileMatrix.COLUMN_PIXEL_X_SIZE,
										tileMatrix.getPixelXSize(), pixelXSize,
										primaryKeys(tileMatrix)));
					}

					if (tileMatrix.getPixelYSize() != pixelYSize) {
						errors.add(
								new DGIWGValidationError(TileMatrix.TABLE_NAME,
										TileMatrix.COLUMN_PIXEL_Y_SIZE,
										tileMatrix.getPixelYSize(), pixelYSize,
										primaryKeys(tileMatrix)));
					}

				}

				previousTileMatrix = tileMatrix;
			}

		}

		return errors;
	}

	/**
	 * Validate feature table
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * @param featureTable
	 *            feature table
	 * @return validation errors
	 */
	public static DGIWGValidationErrors validateFeatureTable(
			GeoPackageCore geoPackage, String featureTable) {

		DGIWGValidationErrors errors = new DGIWGValidationErrors();

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

		if (geometryColumns != null) {
			SpatialReferenceSystem srs = geometryColumns.getSrs();
			errors.add(validateFeatureCoordinateReferenceSystem(featureTable,
					srs));

			int z = geometryColumns.getZ();
			CoordinateReferenceSystem crs = CoordinateReferenceSystem
					.getCoordinateReferenceSystem(srs);
			if (crs != null && (z == 0 || z == 1)) {

				if (z == 0) {
					if (!crs.isDataType(DataType.FEATURES_2D)) {
						errors.add(new DGIWGValidationError(
								GeometryColumns.TABLE_NAME,
								GeometryColumns.COLUMN_Z, z,
								"Geometry Columns z value of prohibited (0) is for 2-D CRS. CRS "
										+ crs.getAuthorityAndCode() + " Types: "
										+ crs.getDataTypes(),
								primaryKeys(geometryColumns)));
					}
				} else if (!crs.isDataType(DataType.FEATURES_3D)) {
					errors.add(new DGIWGValidationError(
							GeometryColumns.TABLE_NAME,
							GeometryColumns.COLUMN_Z, z,
							"Geometry Columns z value of mandatory (1) is for 3-D CRS. CRS "
									+ crs.getAuthorityAndCode() + " Types: "
									+ crs.getDataTypes(),
							primaryKeys(geometryColumns)));
				}

			} else {
				errors.add(new DGIWGValidationError(GeometryColumns.TABLE_NAME,
						GeometryColumns.COLUMN_Z, z,
						"Geometry Columns z values of prohibited (0) or mandatory (1)",
						primaryKeys(geometryColumns)));
			}

		} else {
			errors.add(new DGIWGValidationError(GeometryColumns.TABLE_NAME,
					GeometryColumns.COLUMN_TABLE_NAME, featureTable,
					"No Geometry Columns for feature table"));
		}

		return errors;
	}

	/**
	 * Get the Spatial Reference System primary key
	 * 
	 * @param srs
	 *            spatial reference system
	 * @return primary key
	 */
	private static DGIWGValidationKey primaryKey(SpatialReferenceSystem srs) {
		DGIWGValidationKey key = null;
		if (srs != null) {
			key = new DGIWGValidationKey(SpatialReferenceSystem.COLUMN_ID,
					srs.getId());
		}
		return key;
	}

	/**
	 * Get the Tile Matrix Set primary key
	 * 
	 * @param tileMatrixSet
	 *            tile matrix set
	 * @return primary key
	 */
	private static DGIWGValidationKey primaryKey(TileMatrixSet tileMatrixSet) {
		DGIWGValidationKey key = null;
		if (tileMatrixSet != null) {
			key = new DGIWGValidationKey(TileMatrixSet.COLUMN_ID,
					tileMatrixSet.getId());
		}
		return key;
	}

	/**
	 * Get the Tile Matrix primary keys
	 * 
	 * @param tileMatrix
	 *            tile matrix
	 * @return primary keys
	 */
	private static DGIWGValidationKey[] primaryKeys(TileMatrix tileMatrix) {
		DGIWGValidationKey[] keys = null;
		if (tileMatrix != null) {
			keys = new DGIWGValidationKey[] {
					new DGIWGValidationKey(TileMatrix.COLUMN_ID_1,
							tileMatrix.getId().getTableName()),
					new DGIWGValidationKey(TileMatrix.COLUMN_ID_2,
							tileMatrix.getId().getZoomLevel()) };
		}
		return keys;
	}

	/**
	 * Get the Geometry Columns primary keys
	 * 
	 * @param geometryColumns
	 *            geometry columns
	 * @return primary keys
	 */
	private static DGIWGValidationKey[] primaryKeys(
			GeometryColumns geometryColumns) {
		DGIWGValidationKey[] keys = null;
		if (geometryColumns != null) {
			keys = new DGIWGValidationKey[] {
					new DGIWGValidationKey(GeometryColumns.COLUMN_ID_1,
							geometryColumns.getId().getTableName()),
					new DGIWGValidationKey(GeometryColumns.COLUMN_ID_2,
							geometryColumns.getId().getColumnName()) };
		}
		return keys;
	}

}
