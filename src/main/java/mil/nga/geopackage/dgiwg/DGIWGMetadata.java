package mil.nga.geopackage.dgiwg;

import java.sql.SQLException;
import java.util.List;

import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import mil.nga.geopackage.GeoPackageCore;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.extension.metadata.Metadata;
import mil.nga.geopackage.extension.metadata.MetadataDao;
import mil.nga.geopackage.extension.metadata.MetadataExtension;
import mil.nga.geopackage.extension.metadata.MetadataScopeType;
import mil.nga.geopackage.extension.metadata.reference.MetadataReference;
import mil.nga.geopackage.extension.metadata.reference.MetadataReferenceDao;
import mil.nga.geopackage.extension.metadata.reference.ReferenceScopeType;

/**
 * DGIWG (Defence Geospatial Information Working Group) Metadata utilities
 * 
 * @author osbornb
 * @since 6.5.1
 */
public class DGIWGMetadata {

	/**
	 * Create a new metadata object with a series scope
	 * 
	 * @param uri
	 *            URI
	 * @param metadata
	 *            metadata
	 * @return metadata
	 */
	public static Metadata createSeriesMetadata(String uri, String metadata) {
		return createMetadata(MetadataScopeType.SERIES, uri, metadata);
	}

	/**
	 * Create a new metadata object with a dataset scope
	 * 
	 * @param uri
	 *            URI
	 * @param metadata
	 *            metadata
	 * @return metadata
	 */
	public static Metadata createDatasetMetadata(String uri, String metadata) {
		return createMetadata(MetadataScopeType.DATASET, uri, metadata);
	}

	/**
	 * Create a new metadata object
	 * 
	 * @param scope
	 *            metadata scope type
	 * @param uri
	 *            URI
	 * @param metadata
	 *            metadata
	 * @return metadata
	 */
	public static Metadata createMetadata(MetadataScopeType scope, String uri,
			String metadata) {

		Metadata md = new Metadata();
		md.setMetadataScope(scope);
		md.setStandardUri(uri);
		md.setMimeType(DGIWGConstants.METADATA_MIME_TYPE);
		md.setMetadata(metadata);

		return md;
	}

	/**
	 * Create a new metadata reference object with a GeoPackage scope
	 * 
	 * @return metadata reference
	 */
	public static MetadataReference createGeoPackageMetadataReference() {
		return createMetadataReference(ReferenceScopeType.GEOPACKAGE);
	}

	/**
	 * Create a new metadata reference object
	 * 
	 * @param scope
	 *            metadata reference scope type
	 * @return metadata reference
	 */
	public static MetadataReference createMetadataReference(
			ReferenceScopeType scope) {

		MetadataReference reference = new MetadataReference();
		reference.setReferenceScope(scope);

		return reference;
	}

	/**
	 * Query for GeoPackage DGIWG Metadata Foundation (DMF) metadata
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * @return metadata references
	 */
	public static List<MetadataReference> queryGeoPackageDMFMetadata(
			GeoPackageCore geoPackage) {
		return queryGeoPackageMetadata(geoPackage, DGIWGConstants.DMF_BASE_URI);
	}

	/**
	 * Query for GeoPackage NSG Metadata Foundation (NMF) NSG Application Schema
	 * (NAS) metadata
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * @return metadata references
	 */
	public static List<MetadataReference> queryGeoPackageNASMetadata(
			GeoPackageCore geoPackage) {
		return queryGeoPackageMetadata(geoPackage,
				DGIWGConstants.NMF_NAS_BASE_URI);
	}

	/**
	 * Query for GeoPackage metadata
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * @param baseURI
	 *            base URI
	 * @return metadata references
	 */
	public static List<MetadataReference> queryGeoPackageMetadata(
			GeoPackageCore geoPackage, String baseURI) {

		List<MetadataReference> results = null;

		MetadataExtension metadataExtension = new MetadataExtension(geoPackage);

		if (metadataExtension.has()) {

			MetadataDao metadataDao = metadataExtension.getMetadataDao();
			MetadataReferenceDao referenceDao = metadataExtension
					.getMetadataReferenceDao();

			try {

				if (metadataDao.isTableExists()
						&& referenceDao.isTableExists()) {

					QueryBuilder<Metadata, Long> metadataBuilder = metadataDao
							.queryBuilder();
					QueryBuilder<MetadataReference, Void> referenceBuilder = referenceDao
							.queryBuilder();

					metadataBuilder.where().like(Metadata.COLUMN_STANDARD_URI,
							baseURI + "%");

					referenceBuilder.where().like(
							MetadataReference.COLUMN_REFERENCE_SCOPE,
							ReferenceScopeType.GEOPACKAGE.getValue());

					referenceBuilder.leftJoin(metadataBuilder);

					PreparedQuery<MetadataReference> preparedQuery = referenceBuilder
							.prepare();

					results = referenceDao.query(preparedQuery);

				}

			} catch (SQLException e) {
				throw new GeoPackageException("Failed to query for metadata",
						e);
			}

		}

		return results;
	}

}
