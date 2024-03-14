package mil.nga.geopackage.dgiwg;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import mil.nga.crs.CRS;
import mil.nga.crs.CRSType;
import mil.nga.crs.operation.OperationMethods;
import mil.nga.crs.projected.ProjectedCoordinateReferenceSystem;
import mil.nga.crs.wkt.CRSReader;
import mil.nga.geopackage.BoundingBox;
import mil.nga.geopackage.GeoPackageConstants;
import mil.nga.geopackage.GeoPackageCore;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.contents.ContentsDataType;
import mil.nga.geopackage.extension.CrsWktExtension;
import mil.nga.geopackage.extension.CrsWktExtensionVersion;
import mil.nga.geopackage.extension.ExtensionManager;
import mil.nga.geopackage.extension.Extensions;
import mil.nga.geopackage.extension.GeometryExtensions;
import mil.nga.geopackage.extension.WebPExtension;
import mil.nga.geopackage.extension.ZoomOtherExtension;
import mil.nga.geopackage.extension.metadata.Metadata;
import mil.nga.geopackage.extension.metadata.MetadataExtension;
import mil.nga.geopackage.extension.metadata.MetadataScopeType;
import mil.nga.geopackage.extension.metadata.reference.MetadataReference;
import mil.nga.geopackage.extension.metadata.reference.ReferenceScopeType;
import mil.nga.geopackage.extension.rtree.RTreeIndexCoreExtension;
import mil.nga.geopackage.features.columns.GeometryColumns;
import mil.nga.geopackage.srs.SpatialReferenceSystem;
import mil.nga.geopackage.tiles.matrix.TileMatrix;
import mil.nga.geopackage.tiles.matrixset.TileMatrixSet;
import mil.nga.geopackage.tiles.user.TileColumns;
import mil.nga.proj.Projection;
import mil.nga.proj.ProjectionConstants;
import mil.nga.sf.GeometryType;
import mil.nga.sf.wkb.GeometryCodes;

/**
 * Performs DGIWG (Defence Geospatial Information Working Group) GeoPackage
 * validations
 * 
 * @author osbornb
 * @since 6.6.0
 */
public class DGIWGValidate {

	/**
	 * Tile Matrix Set bounds tolerance in meters. 1 meter as defined in
	 * "Conformance Class Bounding Box (bbox)".
	 */
	public static final double TILE_MATRIX_SET_BOUNDS_TOLERANCE = 1.0;

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

		DGIWGValidationErrors errors = validateBase(geoPackage);

		for (String tileTable : geoPackage.getTileTables()) {
			errors.add(validateTileTable(geoPackage, tileTable));
		}

		for (String featureTable : geoPackage.getFeatureTables()) {
			errors.add(validateFeatureTable(geoPackage, featureTable));
		}

		return errors;
	}

	/**
	 * Validate the base GeoPackage against the DGIWG GeoPackage Profile
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * @return validation errors
	 */
	public static DGIWGValidationErrors validateBase(
			GeoPackageCore geoPackage) {

		DGIWGValidationErrors errors = new DGIWGValidationErrors();

		CrsWktExtension crsWktExtension = new CrsWktExtension(geoPackage);
		if (!crsWktExtension.hasMinimum(CrsWktExtensionVersion.V_1)) {
			errors.add(new DGIWGValidationError(Extensions.TABLE_NAME,
					Extensions.COLUMN_EXTENSION_NAME,
					CrsWktExtension.EXTENSION_NAME,
					"No mandatory CRS WKT extension",
					DGIWGRequirement.EXTENSIONS_MANDATORY,
					extensionPrimaryKeys(SpatialReferenceSystem.TABLE_NAME,
							CrsWktExtension.DEFINITION_COLUMN_NAME,
							CrsWktExtension.EXTENSION_NAME)));
		}

		errors.add(validateMetadata(geoPackage));

		return errors;
	}

	/**
	 * Validate the GeoPackage table against the DGIWG GeoPackage Profile
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * @param table
	 *            table
	 * @return validation errors
	 */
	public static DGIWGValidationErrors validate(GeoPackageCore geoPackage,
			String table) {
		List<String> tables = new ArrayList<>();
		tables.add(table);
		return validate(geoPackage, tables);
	}

	/**
	 * Validate the GeoPackage tables against the DGIWG GeoPackage Profile
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * @param tables
	 *            tables
	 * @return validation errors
	 */
	public static DGIWGValidationErrors validate(GeoPackageCore geoPackage,
			List<String> tables) {

		DGIWGValidationErrors errors = validateBase(geoPackage);

		for (String table : tables) {
			ContentsDataType dataType = geoPackage.getTableDataType(table);
			if (dataType != null) {
				switch (dataType) {
				case FEATURES:
					errors.add(validateFeatureTable(geoPackage, table));
					break;
				case TILES:
					errors.add(validateTileTable(geoPackage, table));
					break;
				default:
				}
			}

		}

		return errors;
	}

	/**
	 * Validate the GeoPackage metadata
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * @return validation errors
	 */
	public static DGIWGValidationErrors validateMetadata(
			GeoPackageCore geoPackage) {

		DGIWGValidationErrors errors = new DGIWGValidationErrors();

		List<MetadataReference> metadataReferences = DGIWGMetadata
				.queryGeoPackageDMFMetadata(geoPackage);
		if (metadataReferences != null && !metadataReferences.isEmpty()) {

			DGIWGValidationErrors metadataErrors = new DGIWGValidationErrors();

			for (MetadataReference reference : metadataReferences) {

				DGIWGValidationErrors mdErrors = new DGIWGValidationErrors();

				Metadata metadata = reference.getMetadata();

				String scope = metadata.getMetadataScopeName();
				if (!scope.equalsIgnoreCase(MetadataScopeType.SERIES.getName())
						&& !scope.equalsIgnoreCase(
								MetadataScopeType.DATASET.getName())) {
					mdErrors.add(new DGIWGValidationError(Metadata.TABLE_NAME,
							Metadata.COLUMN_SCOPE, scope,
							MetadataScopeType.SERIES.getName() + " or "
									+ MetadataScopeType.DATASET.getName(),
							DGIWGRequirement.METADATA_GPKG,
							primaryKey(metadata)));
				}

				if (!metadata.getStandardUri().toLowerCase().startsWith(
						DGIWGConstants.DMF_BASE_URI.toLowerCase())) {
					mdErrors.add(new DGIWGValidationError(Metadata.TABLE_NAME,
							Metadata.COLUMN_STANDARD_URI,
							metadata.getStandardUri(),
							DGIWGConstants.DMF_BASE_URI + "<version>",
							DGIWGRequirement.METADATA_GPKG,
							primaryKey(metadata)));
				}

				if (!metadata.getMimeType()
						.equalsIgnoreCase(DGIWGConstants.METADATA_MIME_TYPE)) {
					mdErrors.add(new DGIWGValidationError(Metadata.TABLE_NAME,
							Metadata.COLUMN_MIME_TYPE, metadata.getMimeType(),
							DGIWGConstants.METADATA_MIME_TYPE,
							DGIWGRequirement.METADATA_GPKG,
							primaryKey(metadata)));
				}

				if (!reference.getReferenceScopeName().equalsIgnoreCase(
						ReferenceScopeType.GEOPACKAGE.getValue())) {
					mdErrors.add(new DGIWGValidationError(
							MetadataReference.TABLE_NAME,
							MetadataReference.COLUMN_REFERENCE_SCOPE,
							reference.getReferenceScopeName(),
							ReferenceScopeType.GEOPACKAGE.getValue(),
							DGIWGRequirement.METADATA_ROW,
							primaryKeys(reference)));
				}

				if (reference.getTableName() != null) {
					mdErrors.add(new DGIWGValidationError(
							MetadataReference.TABLE_NAME,
							MetadataReference.COLUMN_TABLE_NAME,
							reference.getTableName(), "NULL",
							DGIWGRequirement.METADATA_ROW,
							primaryKeys(reference)));
				}

				if (reference.getColumnName() != null) {
					mdErrors.add(new DGIWGValidationError(
							MetadataReference.TABLE_NAME,
							MetadataReference.COLUMN_COLUMN_NAME,
							reference.getColumnName(), "NULL",
							DGIWGRequirement.METADATA_ROW,
							primaryKeys(reference)));
				}

				if (reference.getRowIdValue() != null) {
					mdErrors.add(new DGIWGValidationError(
							MetadataReference.TABLE_NAME,
							MetadataReference.COLUMN_ROW_ID_VALUE,
							reference.getRowIdValue(), "NULL",
							DGIWGRequirement.METADATA_ROW,
							primaryKeys(reference)));
				}

				if (reference.getParentId() != null) {
					mdErrors.add(new DGIWGValidationError(
							MetadataReference.TABLE_NAME,
							MetadataReference.COLUMN_PARENT_ID,
							reference.getParentId(), "NULL",
							DGIWGRequirement.METADATA_ROW,
							primaryKeys(reference)));
				}

				if (mdErrors.isValid()) {
					metadataErrors = null;
					break;
				}

				metadataErrors.add(mdErrors);
			}

			if (metadataErrors != null) {
				errors.add(metadataErrors);
			}

		} else {

			MetadataExtension metadataExtension = new MetadataExtension(
					geoPackage);
			if (!metadataExtension.has()) {

				boolean metadataTableExists = false;
				try {
					metadataTableExists = metadataExtension.getMetadataDao()
							.isTableExists();
				} catch (SQLException e) {
				}

				if (!metadataTableExists) {

					errors.add(new DGIWGValidationError(Extensions.TABLE_NAME,
							Extensions.COLUMN_EXTENSION_NAME,
							MetadataExtension.EXTENSION_NAME,
							"No mandatory Metadata extension",
							DGIWGRequirement.EXTENSIONS_MANDATORY,
							extensionPrimaryKeys(Metadata.TABLE_NAME,
									MetadataExtension.EXTENSION_NAME)));

				}

				boolean referenceTableExists = false;
				try {
					referenceTableExists = metadataExtension
							.getMetadataReferenceDao().isTableExists();
				} catch (SQLException e) {
				}

				if (!referenceTableExists) {

					errors.add(new DGIWGValidationError(Extensions.TABLE_NAME,
							Extensions.COLUMN_EXTENSION_NAME,
							MetadataExtension.EXTENSION_NAME,
							"No mandatory Metadata extension",
							DGIWGRequirement.EXTENSIONS_MANDATORY,
							extensionPrimaryKeys(MetadataReference.TABLE_NAME,
									MetadataExtension.EXTENSION_NAME)));

				}

			}

			DGIWGValidationKey[] keys = new DGIWGValidationKey[2];
			keys[0] = new DGIWGValidationKey(Metadata.COLUMN_STANDARD_URI,
					DGIWGConstants.DMF_BASE_URI);
			keys[1] = new DGIWGValidationKey(
					MetadataReference.COLUMN_REFERENCE_SCOPE,
					ReferenceScopeType.GEOPACKAGE.getValue());
			errors.add(new DGIWGValidationError(Metadata.TABLE_NAME,
					Metadata.COLUMN_STANDARD_URI, DGIWGConstants.DMF_BASE_URI,
					"No required metadata with DMF base URI and metadata reference 'geopackage' scope",
					DGIWGRequirement.METADATA_DMF, keys));

		}

		return errors;
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
			SpatialReferenceSystem srs = tileMatrixSet.getSrs();
			errors.add(validateTileCoordinateReferenceSystem(tileTable, srs));

			CoordinateReferenceSystem crs = CoordinateReferenceSystem
					.getCoordinateReferenceSystem(srs);
			if (crs != null) {

				BoundingBox boundingBox = crs.getBounds();
				if (!boundingBox.contains(tileMatrixSet.getBoundingBox())) {

					String crsBounds = "CRS " + crs.getAuthorityAndCode()
							+ " Bounds: " + boundingBox;

					if (tileMatrixSet.getMinX() < boundingBox.getMinLongitude()
							- TILE_MATRIX_SET_BOUNDS_TOLERANCE) {
						errors.add(new DGIWGValidationError(
								TileMatrixSet.TABLE_NAME,
								TileMatrixSet.COLUMN_MIN_X,
								tileMatrixSet.getMinX(), crsBounds,
								DGIWGRequirement.BBOX_CRS,
								primaryKey(tileMatrixSet)));
					}

					if (tileMatrixSet.getMinY() < boundingBox.getMinLatitude()
							- TILE_MATRIX_SET_BOUNDS_TOLERANCE) {
						errors.add(new DGIWGValidationError(
								TileMatrixSet.TABLE_NAME,
								TileMatrixSet.COLUMN_MIN_Y,
								tileMatrixSet.getMinY(), crsBounds,
								DGIWGRequirement.BBOX_CRS,
								primaryKey(tileMatrixSet)));
					}

					if (tileMatrixSet.getMaxX() > boundingBox.getMaxLongitude()
							+ TILE_MATRIX_SET_BOUNDS_TOLERANCE) {
						errors.add(new DGIWGValidationError(
								TileMatrixSet.TABLE_NAME,
								TileMatrixSet.COLUMN_MAX_X,
								tileMatrixSet.getMaxX(), crsBounds,
								DGIWGRequirement.BBOX_CRS,
								primaryKey(tileMatrixSet)));
					}

					if (tileMatrixSet.getMaxY() > boundingBox.getMaxLatitude()
							+ TILE_MATRIX_SET_BOUNDS_TOLERANCE) {
						errors.add(new DGIWGValidationError(
								TileMatrixSet.TABLE_NAME,
								TileMatrixSet.COLUMN_MAX_Y,
								tileMatrixSet.getMaxY(), crsBounds,
								DGIWGRequirement.BBOX_CRS,
								primaryKey(tileMatrixSet)));
					}

				}

			}

		} else {
			errors.add(new DGIWGValidationError(TileMatrixSet.TABLE_NAME,
					TileMatrixSet.COLUMN_TABLE_NAME, tileTable,
					"No Tile Matrix Set for tile table",
					DGIWGRequirement.CRS_RASTER_TILE_MATRIX_SET));
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
					DGIWGRequirement.CRS_RASTER_TILE_MATRIX_SET,
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
							DGIWGRequirement.VALIDITY_DATA_VALIDITY,
							primaryKeys(tileMatrix)));
				}

				if (tileMatrix.getTileWidth() != DGIWGConstants.TILE_WIDTH) {
					errors.add(new DGIWGValidationError(TileMatrix.TABLE_NAME,
							TileMatrix.COLUMN_TILE_WIDTH,
							tileMatrix.getTileWidth(),
							DGIWGConstants.TILE_WIDTH,
							DGIWGRequirement.TILE_SIZE_MATRIX,
							primaryKeys(tileMatrix)));
				}

				if (tileMatrix.getTileHeight() != DGIWGConstants.TILE_HEIGHT) {
					errors.add(new DGIWGValidationError(TileMatrix.TABLE_NAME,
							TileMatrix.COLUMN_TILE_HEIGHT,
							tileMatrix.getTileHeight(),
							DGIWGConstants.TILE_HEIGHT,
							DGIWGRequirement.TILE_SIZE_MATRIX,
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
										DGIWGRequirement.ZOOM_FACTOR,
										primaryKeys(tileMatrix)));
					}

					if (tileMatrix.getPixelYSize() != pixelYSize) {
						errors.add(
								new DGIWGValidationError(TileMatrix.TABLE_NAME,
										TileMatrix.COLUMN_PIXEL_Y_SIZE,
										tileMatrix.getPixelYSize(), pixelYSize,
										DGIWGRequirement.ZOOM_FACTOR,
										primaryKeys(tileMatrix)));
					}

					if (zoomChange > 1) {
						StringBuilder zoomMissing = new StringBuilder();
						zoomMissing
								.append(previousTileMatrix.getZoomLevel() + 1);
						if (zoomChange > 2) {
							zoomMissing.append(" - ");
							zoomMissing.append(tileMatrix.getZoomLevel() - 1);
						}
						errors.add(new DGIWGValidationError(
								TileMatrix.TABLE_NAME,
								TileMatrix.COLUMN_ZOOM_LEVEL,
								tileMatrix.getZoomLevel(),
								"Missing adjacent zoom level(s): "
										+ zoomMissing,
								DGIWGRequirement.ZOOM_MATRIX_SETS_MULTIPLE,
								primaryKeys(tileMatrix)));
					}

				}

				previousTileMatrix = tileMatrix;
			}

		}

		ZoomOtherExtension zoomOtherExtension = new ZoomOtherExtension(
				geoPackage);
		if (zoomOtherExtension.has(tileTable)) {
			errors.add(new DGIWGValidationError(Extensions.TABLE_NAME,
					Extensions.COLUMN_EXTENSION_NAME,
					ZoomOtherExtension.EXTENSION_NAME,
					"Zoom other intervals not allowed",
					DGIWGRequirement.EXTENSIONS_NOT_ALLOWED,
					extensionPrimaryKeys(tileTable, TileColumns.TILE_DATA,
							ZoomOtherExtension.EXTENSION_NAME)));
		}

		WebPExtension webPExtension = new WebPExtension(geoPackage);
		if (webPExtension.has(tileTable)) {
			errors.add(new DGIWGValidationError(Extensions.TABLE_NAME,
					Extensions.COLUMN_EXTENSION_NAME,
					WebPExtension.EXTENSION_NAME, "WebP encoding not allowed",
					DGIWGRequirement.EXTENSIONS_NOT_ALLOWED,
					extensionPrimaryKeys(tileTable, TileColumns.TILE_DATA,
							WebPExtension.EXTENSION_NAME)));
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
								DGIWGRequirement.CRS_RASTER_ALLOWED,
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
							DGIWGRequirement.CRS_RASTER_ALLOWED,
							primaryKey(srs)));
				}

			} else if (!errors.hasErrors()) {
				errors.add(new DGIWGValidationError(
						SpatialReferenceSystem.TABLE_NAME,
						SpatialReferenceSystem.COLUMN_DEFINITION, definition,
						"Failed to read tiles coordinate reference system definition",
						DGIWGRequirement.CRS_RASTER_ALLOWED, primaryKey(srs)));
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

			String geomColumn = geometryColumns.getColumnName();
			int z = geometryColumns.getZ();

			SpatialReferenceSystem srs = geometryColumns.getSrs();
			errors.add(validateFeatureCoordinateReferenceSystem(featureTable,
					srs, z));

			if (z != 0 && z != 1) {
				errors.add(new DGIWGValidationError(GeometryColumns.TABLE_NAME,
						GeometryColumns.COLUMN_Z, z,
						"Geometry Columns z values of prohibited (0) or mandatory (1)",
						DGIWGRequirement.VALIDITY_DATA_VALIDITY,
						primaryKeys(geometryColumns)));
			}

			CoordinateReferenceSystem crs = CoordinateReferenceSystem
					.getCoordinateReferenceSystem(srs);
			if (crs != null) {

				if (z == 0) {
					if (!crs.isDataType(DataType.FEATURES_2D)) {
						errors.add(new DGIWGValidationError(
								GeometryColumns.TABLE_NAME,
								GeometryColumns.COLUMN_Z, z,
								"Geometry Columns z value of prohibited (0) is for 2-D CRS. CRS "
										+ crs.getAuthorityAndCode() + " Types: "
										+ crs.getDataTypes(),
								DGIWGRequirement.CRS_2D_VECTOR,
								primaryKeys(geometryColumns)));
					}
					if (crs.isType(CRSType.COMPOUND)) {
						errors.add(new DGIWGValidationError(
								SpatialReferenceSystem.TABLE_NAME,
								CrsWktExtension.DEFINITION_COLUMN_NAME,
								srs.getProjectionDefinition(),
								"Compound CRS not allowed for Geometry Columns value of prohibited (0)",
								DGIWGRequirement.CRS_COMPOUND,
								primaryKey(srs)));
					}
				} else if (z == 1) {
					if (!crs.isDataType(DataType.FEATURES_3D)) {
						errors.add(new DGIWGValidationError(
								GeometryColumns.TABLE_NAME,
								GeometryColumns.COLUMN_Z, z,
								"Geometry Columns z value of mandatory (1) is for 3-D CRS. CRS "
										+ crs.getAuthorityAndCode() + " Types: "
										+ crs.getDataTypes(),
								DGIWGRequirement.CRS_3D_VECTOR,
								primaryKeys(geometryColumns)));
					}
				}

			}

			RTreeIndexCoreExtension rTreeIndexExtension = ExtensionManager
					.getRTreeIndexExtension(geoPackage);
			if (!rTreeIndexExtension.has(featureTable)) {
				errors.add(new DGIWGValidationError(Extensions.TABLE_NAME,
						Extensions.COLUMN_EXTENSION_NAME,
						RTreeIndexCoreExtension.EXTENSION_NAME,
						"No mandatory RTree extension for feature table",
						DGIWGRequirement.EXTENSIONS_MANDATORY,
						extensionPrimaryKeys(featureTable, geomColumn,
								RTreeIndexCoreExtension.EXTENSION_NAME)));
			}

			GeometryExtensions geometryExtensions = new GeometryExtensions(
					geoPackage);
			for (int i = GeometryCodes
					.getCode(GeometryType.CIRCULARSTRING); i <= GeometryCodes
							.getCode(GeometryType.SURFACE); i++) {
				GeometryType geometryType = GeometryCodes.getGeometryType(i);
				if (geometryExtensions.has(featureTable, geomColumn,
						geometryType)) {
					String geometryExtensionName = GeometryExtensions
							.getExtensionName(geometryType);
					errors.add(new DGIWGValidationError(Extensions.TABLE_NAME,
							Extensions.COLUMN_EXTENSION_NAME,
							geometryExtensionName,
							"Nonlinear geometry type not allowed",
							DGIWGRequirement.EXTENSIONS_NOT_ALLOWED,
							extensionPrimaryKeys(featureTable, geomColumn,
									geometryExtensionName)));
				}
			}

		} else {
			errors.add(new DGIWGValidationError(GeometryColumns.TABLE_NAME,
					GeometryColumns.COLUMN_TABLE_NAME, featureTable,
					"No Geometry Columns for feature table",
					DGIWGRequirement.GEOPACKAGE_BASE));
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
		return validateFeatureCoordinateReferenceSystem(featureTable, srs, 0);
	}

	/**
	 * Validate the feature coordinate reference system
	 * 
	 * @param featureTable
	 *            feature table
	 * @param srs
	 *            spatial reference system
	 * @param z
	 *            geometry z value
	 * @return validation errors
	 * @since 6.6.4
	 */
	public static DGIWGValidationErrors validateFeatureCoordinateReferenceSystem(
			String featureTable, SpatialReferenceSystem srs, int z) {

		DGIWGValidationErrors errors = new DGIWGValidationErrors();

		CoordinateReferenceSystem crs = validateCoordinateReferenceSystem(
				errors, featureTable, srs, ContentsDataType.FEATURES);

		if (crs == null) {
			DGIWGRequirement requirement = z == 1
					? DGIWGRequirement.CRS_3D_VECTOR
					: DGIWGRequirement.CRS_2D_VECTOR;
			errors.add(
					new DGIWGValidationError(SpatialReferenceSystem.TABLE_NAME,
							SpatialReferenceSystem.COLUMN_DEFINITION,
							srs.getProjectionDefinition(),
							"Unsupported features coordinate reference system",
							requirement, primaryKey(srs)));
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
						DGIWGRequirement.CRS_WKT, primaryKey(srs)));
			}

			String definition = null;
			if (crs.getType() == CRSType.COMPOUND) {
				definition = srs.getDefinition_12_063();
			} else {
				definition = srs.getProjectionDefinition();
			}
			if (definition == null || definition.isBlank()
					|| definition.trim().equalsIgnoreCase(
							GeoPackageConstants.UNDEFINED_DEFINITION)) {
				errors.add(new DGIWGValidationError(
						SpatialReferenceSystem.TABLE_NAME,
						crs.getType() == CRSType.COMPOUND
								? CrsWktExtension.DEFINITION_COLUMN_NAME
								: SpatialReferenceSystem.COLUMN_DEFINITION,
						definition,
						"Missing required coordinate reference system well-known text definition",
						DGIWGRequirement.CRS_WKT, primaryKey(srs)));
			}

			if (!srs.getSrsName().equalsIgnoreCase(crs.getName())) {
				errors.add(new DGIWGValidationError(
						SpatialReferenceSystem.TABLE_NAME,
						SpatialReferenceSystem.COLUMN_SRS_NAME,
						srs.getSrsName(), crs.getName(),
						DGIWGRequirement.CRS_WKT, primaryKey(srs)));
			}

			if (srs.getSrsId() != crs.getCode()) {
				errors.add(new DGIWGValidationError(
						SpatialReferenceSystem.TABLE_NAME,
						SpatialReferenceSystem.COLUMN_SRS_ID, srs.getSrsId(),
						crs.getCode(), DGIWGRequirement.CRS_WKT,
						primaryKey(srs)));
			}

			if (!srs.getOrganization().equalsIgnoreCase(crs.getAuthority())) {
				errors.add(new DGIWGValidationError(
						SpatialReferenceSystem.TABLE_NAME,
						SpatialReferenceSystem.COLUMN_ORGANIZATION,
						srs.getOrganization(), crs.getAuthority(),
						DGIWGRequirement.VALIDITY_DATA_VALIDITY,
						primaryKey(srs)));
			}

			if (srs.getOrganizationCoordsysId() != crs.getCode()) {
				errors.add(new DGIWGValidationError(
						SpatialReferenceSystem.TABLE_NAME,
						SpatialReferenceSystem.COLUMN_ORGANIZATION_COORDSYS_ID,
						srs.getOrganizationCoordsysId(), crs.getCode(),
						DGIWGRequirement.CRS_WKT, primaryKey(srs)));
			}

		} else {

			if (!srs.getOrganization()
					.equalsIgnoreCase(ProjectionConstants.AUTHORITY_EPSG)) {
				errors.add(new DGIWGValidationError(
						SpatialReferenceSystem.TABLE_NAME,
						SpatialReferenceSystem.COLUMN_ORGANIZATION,
						srs.getOrganization(),
						ProjectionConstants.AUTHORITY_EPSG,
						DGIWGRequirement.VALIDITY_DATA_VALIDITY,
						primaryKey(srs)));
			}

		}

		String description = srs.getDescription();
		if (description == null || description.isBlank()
				|| description.trim()
						.equalsIgnoreCase(DGIWGConstants.DESCRIPTION_UNKNOWN)
				|| description.trim()
						.equalsIgnoreCase(DGIWGConstants.DESCRIPTION_TBD)) {
			errors.add(new DGIWGValidationError(
					SpatialReferenceSystem.TABLE_NAME,
					SpatialReferenceSystem.COLUMN_DESCRIPTION,
					srs.getDescription(),
					"Invalid empty or unspecified description",
					DGIWGRequirement.VALIDITY_DATA_VALIDITY, primaryKey(srs)));
		}

		return crs;
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
	 * Get the Metadata primary key
	 * 
	 * @param metadata
	 *            metadata
	 * @return primary key
	 */
	private static DGIWGValidationKey primaryKey(Metadata metadata) {
		DGIWGValidationKey key = null;
		if (metadata != null) {
			key = new DGIWGValidationKey(Metadata.COLUMN_ID, metadata.getId());
		}
		return key;
	}

	/**
	 * Get the Metadata Reference primary key
	 * 
	 * @param reference
	 *            metadata reference
	 * @return primary key
	 */
	private static DGIWGValidationKey[] primaryKeys(
			MetadataReference reference) {
		List<DGIWGValidationKey> keys = new ArrayList<>();

		if (reference != null) {
			keys.add(new DGIWGValidationKey(MetadataReference.COLUMN_FILE_ID,
					reference.getFileId()));
			if (reference.getParentId() != null) {
				keys.add(new DGIWGValidationKey(
						MetadataReference.COLUMN_PARENT_ID,
						reference.getParentId()));
			}
		}

		return keys.toArray(new DGIWGValidationKey[] {});
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

	/**
	 * Get the Extension primary keys
	 * 
	 * @param table
	 *            table name
	 * @param extension
	 *            extension name
	 * @return primary keys
	 */
	private static DGIWGValidationKey[] extensionPrimaryKeys(String table,
			String extension) {
		return extensionPrimaryKeys(table, null, extension);
	}

	/**
	 * Get the Extension primary keys
	 * 
	 * @param table
	 *            table name
	 * @param column
	 *            column name
	 * @param extension
	 *            extension name
	 * @return primary keys
	 */
	private static DGIWGValidationKey[] extensionPrimaryKeys(String table,
			String column, String extension) {

		List<DGIWGValidationKey> keys = new ArrayList<>();

		if (table != null) {
			keys.add(new DGIWGValidationKey(Extensions.COLUMN_TABLE_NAME,
					table));
		}

		if (column != null) {
			keys.add(new DGIWGValidationKey(Extensions.COLUMN_COLUMN_NAME,
					column));
		}

		keys.add(new DGIWGValidationKey(Extensions.COLUMN_EXTENSION_NAME,
				extension));

		return keys.toArray(new DGIWGValidationKey[] {});
	}

}
