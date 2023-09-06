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
import mil.nga.geopackage.db.CoreSQLUtils;
import mil.nga.geopackage.extension.metadata.Metadata;
import mil.nga.geopackage.extension.metadata.MetadataDao;
import mil.nga.geopackage.extension.metadata.MetadataExtension;
import mil.nga.geopackage.extension.metadata.MetadataScopeType;
import mil.nga.geopackage.extension.metadata.reference.MetadataReference;
import mil.nga.geopackage.extension.metadata.reference.MetadataReferenceDao;
import mil.nga.geopackage.features.columns.GeometryColumns;
import mil.nga.geopackage.features.user.FeatureColumn;
import mil.nga.geopackage.features.user.FeatureTable;
import mil.nga.geopackage.features.user.FeatureTableMetadata;
import mil.nga.geopackage.srs.SpatialReferenceSystem;
import mil.nga.geopackage.srs.SpatialReferenceSystemDao;
import mil.nga.geopackage.tiles.matrix.TileMatrix;
import mil.nga.geopackage.tiles.matrixset.TileMatrixSet;
import mil.nga.geopackage.tiles.user.TileTable;
import mil.nga.geopackage.user.UserTable;
import mil.nga.geopackage.user.UserTableMetadata;
import mil.nga.sf.GeometryType;

/**
 * DGIWG (Defence Geospatial Information Working Group) GeoPackage utilities
 * 
 * @author osbornb
 * @since 6.6.0
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
			srsDao.createOrUpdate(srs);
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
	 * Create tile matrices for zoom levels
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
	 * Create tile matrices for zoom levels
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
				createTileMatrix(geoPackage, table, boundingBox, zoom,
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
	public static void createTileMatrix(GeoPackageCore geoPackage, String table,
			BoundingBox boundingBox, long zoom, long matrixWidth,
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

	/**
	 * Create features table
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * @param table
	 *            table name
	 * @param identifier
	 *            contents identifier
	 * @param description
	 *            contents description
	 * @param bounds
	 *            contents bounds
	 * @param geometryType
	 *            geometry type
	 * @param dataType
	 *            data type
	 * @param columns
	 *            feature columns
	 * @param srs
	 *            spatial reference system
	 * @return created tile matrix set
	 */
	public static GeometryColumns createFeatures(GeoPackageCore geoPackage,
			String table, String identifier, String description,
			BoundingBox bounds, GeometryType geometryType, DataType dataType,
			List<FeatureColumn> columns, SpatialReferenceSystem srs) {

		geoPackage.createGeometryColumnsTable();

		SpatialReferenceSystemDao srsDao = geoPackage
				.getSpatialReferenceSystemDao();
		try {
			srsDao.createOrUpdate(srs);
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to create Spatial Reference System: "
							+ srs.getSrsName(),
					e);
		}

		Contents contents = new Contents();
		contents.setTableName(table);
		contents.setDataType(ContentsDataType.FEATURES);
		contents.setIdentifier(identifier);
		contents.setDescription(description);
		contents.setMinX(bounds.getMinLongitude());
		contents.setMinY(bounds.getMinLatitude());
		contents.setMaxX(bounds.getMaxLongitude());
		contents.setMaxY(bounds.getMaxLatitude());
		contents.setSrs(srs);

		boolean hasPk = false;
		boolean hasGeometry = false;
		if (columns == null) {
			columns = new ArrayList<>();
		} else {
			for (FeatureColumn column : columns) {
				if (column.isPrimaryKey()) {
					hasPk = true;
					if (hasGeometry) {
						break;
					}
				} else if (column.isGeometry()) {
					hasGeometry = true;
					if (hasPk) {
						break;
					}
				}
			}
		}
		if (!hasGeometry) {
			columns.add(0, FeatureColumn.createGeometryColumn(
					FeatureTableMetadata.DEFAULT_COLUMN_NAME, geometryType));
		}
		if (!hasPk) {
			columns.add(0,
					FeatureColumn.createPrimaryKeyColumn(
							UserTableMetadata.DEFAULT_ID_COLUMN_NAME,
							UserTable.DEFAULT_AUTOINCREMENT));
		}

		FeatureTable featureTable = new FeatureTable(table, columns);
		geoPackage.createFeatureTable(featureTable);

		try {
			geoPackage.getContentsDao().create(contents);
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to create Contents: " + contents.getTableName(), e);
		}

		GeometryColumns geometryColumns = new GeometryColumns();
		geometryColumns.setContents(contents);
		geometryColumns.setColumnName(featureTable.getGeometryColumnName());
		geometryColumns.setGeometryType(geometryType);
		geometryColumns.setSrs(srs);
		geometryColumns.setZ(dataType.getZ());
		geometryColumns.setM((byte) 0);

		try {
			geoPackage.getGeometryColumnsDao().create(geometryColumns);
		} catch (SQLException e) {
			throw new GeoPackageException("Failed to create Geometry Columns: "
					+ geometryColumns.getTableName(), e);
		}

		return geometryColumns;
	}

	/**
	 * Create metadata and metadata reference
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * @param metadata
	 *            metadata
	 * @param reference
	 *            metadata reference
	 */
	public static void createMetadata(GeoPackageCore geoPackage,
			Metadata metadata, MetadataReference reference) {

		createMetadata(geoPackage, metadata);
		createMetadataReference(geoPackage, metadata, reference);

	}

	/**
	 * Create metadata
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * @param metadata
	 *            metadata
	 */
	public static void createMetadata(GeoPackageCore geoPackage,
			Metadata metadata) {

		MetadataExtension metadataExtension = new MetadataExtension(geoPackage);
		metadataExtension.createMetadataTable();
		MetadataDao metadataDao = metadataExtension.getMetadataDao();

		try {
			metadataDao.create(metadata);
		} catch (SQLException e) {
			throw new GeoPackageException("Failed to create Metadata", e);
		}

	}

	/**
	 * Create metadata reference
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * @param metadata
	 *            the reference metadata
	 * @param reference
	 *            metadata reference
	 */
	public static void createMetadataReference(GeoPackageCore geoPackage,
			Metadata metadata, MetadataReference reference) {

		reference.setMetadata(metadata);
		createMetadataReference(geoPackage, reference);

	}

	/**
	 * Create metadata reference
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * @param reference
	 *            metadata reference
	 */
	public static void createMetadataReference(GeoPackageCore geoPackage,
			MetadataReference reference) {

		MetadataExtension metadataExtension = new MetadataExtension(geoPackage);
		metadataExtension.createMetadataReferenceTable();
		MetadataReferenceDao metadataReferenceDao = metadataExtension
				.getMetadataReferenceDao();

		try {
			metadataReferenceDao.create(reference);
		} catch (SQLException e) {
			throw new GeoPackageException("Failed to create Metadata Reference",
					e);
		}

	}

	/**
	 * Create GeoPackage metadata with a series scope and metadata reference
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * @param uri
	 *            URI
	 * @param metadata
	 *            metadata
	 * @return metadata reference
	 */
	public static MetadataReference createGeoPackageSeriesMetadata(
			GeoPackageCore geoPackage, String uri, String metadata) {
		return createGeoPackageMetadata(geoPackage, MetadataScopeType.SERIES,
				uri, metadata);
	}

	/**
	 * Create GeoPackage metadata with a dataset scope and metadata reference
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * @param uri
	 *            URI
	 * @param metadata
	 *            metadata
	 * @return metadata reference
	 */
	public static MetadataReference createGeoPackageDatasetMetadata(
			GeoPackageCore geoPackage, String uri, String metadata) {
		return createGeoPackageMetadata(geoPackage, MetadataScopeType.DATASET,
				uri, metadata);
	}

	/**
	 * Create GeoPackage metadata and metadata reference
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * @param scope
	 *            metadata scope type
	 * @param uri
	 *            URI
	 * @param metadata
	 *            metadata
	 * @return metadata reference
	 */
	public static MetadataReference createGeoPackageMetadata(
			GeoPackageCore geoPackage, MetadataScopeType scope, String uri,
			String metadata) {
		Metadata md = DGIWGMetadata.createMetadata(scope, uri, metadata);
		MetadataReference reference = DGIWGMetadata
				.createGeoPackageMetadataReference();
		createMetadata(geoPackage, md, reference);
		return reference;
	}

	/**
	 * Create metadata and metadata reference
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * @param scope
	 *            metadata scope type
	 * @param uri
	 *            URI
	 * @param metadata
	 *            metadata
	 * @param reference
	 *            metadata reference
	 * @return metadata reference
	 */
	public static MetadataReference createMetadata(GeoPackageCore geoPackage,
			MetadataScopeType scope, String uri, String metadata,
			MetadataReference reference) {
		Metadata md = DGIWGMetadata.createMetadata(scope, uri, metadata);
		createMetadata(geoPackage, md, reference);
		return reference;
	}

	/**
	 * Wrap the value in single quotes if an empty string or contains whitespace
	 * 
	 * @param value
	 *            value
	 * @return wrapped value
	 * @since 6.6.3
	 */
	public static String wrapIfEmptyOrContainsWhitespace(String value) {
		String wrapped = null;
		if (CoreSQLUtils.isEmptyOrContainsWhitespace(value)) {
			wrapped = CoreSQLUtils.singleQuoteWrap(value);
		} else {
			wrapped = value;
		}
		return wrapped;
	}

}
