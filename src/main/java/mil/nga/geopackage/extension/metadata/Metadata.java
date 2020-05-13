package mil.nga.geopackage.extension.metadata;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Contains metadata in MIME encodings structured in accordance with any
 * authoritative metadata specification
 * 
 * @author osbornb
 */
@DatabaseTable(tableName = "gpkg_metadata", daoClass = MetadataDao.class)
public class Metadata {

	/**
	 * Table name
	 */
	public static final String TABLE_NAME = "gpkg_metadata";

	/**
	 * id field name
	 */
	public static final String COLUMN_ID = "id";

	/**
	 * scope field name
	 */
	public static final String COLUMN_SCOPE = "md_scope";

	/**
	 * standardUri field name
	 */
	public static final String COLUMN_STANDARD_URI = "md_standard_uri";

	/**
	 * mimeType field name
	 */
	public static final String COLUMN_MIME_TYPE = "mime_type";

	/**
	 * metadata field name
	 */
	public static final String COLUMN_METADATA = "metadata";

	/**
	 * Metadata primary key
	 */
	@DatabaseField(columnName = COLUMN_ID, generatedId = true, canBeNull = false)
	private long id;

	/**
	 * Case sensitive name of the data scope to which this metadata applies; see
	 * Metadata Scopes below
	 */
	@DatabaseField(columnName = COLUMN_SCOPE, canBeNull = false, defaultValue = "dataset")
	private String scope;

	/**
	 * URI reference to the metadata structure definition authority
	 */
	@DatabaseField(columnName = COLUMN_STANDARD_URI, canBeNull = false)
	private String standardUri;

	/**
	 * MIME encoding of metadata
	 */
	@DatabaseField(columnName = COLUMN_MIME_TYPE, canBeNull = false, defaultValue = "text/xml")
	private String mimeType;

	/**
	 * metadata
	 */
	@DatabaseField(columnName = COLUMN_METADATA, canBeNull = false, defaultValue = "")
	private String metadata;

	/**
	 * Default Constructor
	 */
	public Metadata() {

	}

	/**
	 * Copy Constructor
	 * 
	 * @param meta
	 *            metadata to copy
	 * @since 1.3.0
	 */
	public Metadata(Metadata meta) {
		id = meta.id;
		scope = meta.scope;
		standardUri = meta.standardUri;
		mimeType = meta.mimeType;
		metadata = meta.metadata;
	}

	/**
	 * Get the id
	 * 
	 * @return id
	 */
	public long getId() {
		return id;
	}

	/**
	 * Set the id
	 * 
	 * @param id
	 *            id
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * Get the metadata scope
	 * 
	 * @return metadata scope type
	 */
	public MetadataScopeType getMetadataScope() {
		return MetadataScopeType.fromName(scope);
	}

	/**
	 * Set the metadata scope
	 * 
	 * @param metadataScope
	 *            metadata scope type
	 */
	public void setMetadataScope(MetadataScopeType metadataScope) {
		this.scope = metadataScope.getName();
	}

	/**
	 * Get the metadata scope name
	 * 
	 * @return metadata scope name
	 * @since 4.0.0
	 */
	public String getMetadataScopeName() {
		return scope;
	}

	/**
	 * Set the metadata scope
	 * 
	 * @param metadataScope
	 *            metadata scope name
	 * @since 4.0.0
	 */
	public void setMetadataScope(String metadataScope) {
		this.scope = metadataScope;
	}

	/**
	 * Get the standard URI
	 * 
	 * @return standard URI
	 */
	public String getStandardUri() {
		return standardUri;
	}

	/**
	 * Set the standard URI
	 * 
	 * @param standardUri
	 *            standard URI
	 */
	public void setStandardUri(String standardUri) {
		this.standardUri = standardUri;
	}

	/**
	 * Get the MIME type
	 * 
	 * @return MIME type
	 */
	public String getMimeType() {
		return mimeType;
	}

	/**
	 * Set the MIME type
	 * 
	 * @param mimeType
	 *            MIME type
	 */
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	/**
	 * Get the metadata
	 * 
	 * @return metadata
	 */
	public String getMetadata() {
		return metadata;
	}

	/**
	 * Set the metadata
	 * 
	 * @param metadata
	 *            metadata
	 */
	public void setMetadata(String metadata) {
		this.metadata = metadata;
	}

}
