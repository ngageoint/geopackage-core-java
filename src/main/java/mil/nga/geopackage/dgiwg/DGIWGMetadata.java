package mil.nga.geopackage.dgiwg;

import mil.nga.geopackage.extension.metadata.Metadata;
import mil.nga.geopackage.extension.metadata.MetadataScopeType;
import mil.nga.geopackage.extension.metadata.reference.MetadataReference;
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

}
