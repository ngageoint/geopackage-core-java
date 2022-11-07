package mil.nga.geopackage.extension.metadata.reference;

import java.util.Date;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.db.DateConverter;
import mil.nga.geopackage.extension.metadata.Metadata;
import mil.nga.geopackage.persister.DatePersister;

/**
 * Links metadata in the gpkg_metadata table to data in the feature, and tiles
 * tables
 * 
 * @author osbornb
 */
@DatabaseTable(tableName = "gpkg_metadata_reference", daoClass = MetadataReferenceDao.class)
public class MetadataReference {

	/**
	 * Table name
	 */
	public static final String TABLE_NAME = "gpkg_metadata_reference";

	/**
	 * referenceScope field name
	 */
	public static final String COLUMN_REFERENCE_SCOPE = "reference_scope";

	/**
	 * tableName field name
	 */
	public static final String COLUMN_TABLE_NAME = "table_name";

	/**
	 * columnName field name
	 */
	public static final String COLUMN_COLUMN_NAME = "column_name";

	/**
	 * rowIdValue field name
	 */
	public static final String COLUMN_ROW_ID_VALUE = "row_id_value";

	/**
	 * timestamp field name
	 */
	public static final String COLUMN_TIMESTAMP = "timestamp";

	/**
	 * mdFileId field name
	 */
	public static final String COLUMN_FILE_ID = "md_file_id";

	/**
	 * mdParentId field name
	 */
	public static final String COLUMN_PARENT_ID = "md_parent_id";

	/**
	 * Lowercase metadata reference scope; one of ‘geopackage’,
	 * ‘table’,‘column’, ’row’, ’row/col’
	 */
	@DatabaseField(columnName = COLUMN_REFERENCE_SCOPE, canBeNull = false)
	private String referenceScope;

	/**
	 * Name of the table to which this metadata reference applies, or NULL for
	 * reference_scope of ‘geopackage’.
	 */
	@DatabaseField(columnName = COLUMN_TABLE_NAME)
	private String tableName;

	/**
	 * Name of the column to which this metadata reference applies; NULL for
	 * reference_scope of ‘geopackage’,‘table’ or ‘row’, or the name of a column
	 * in the table_name table for reference_scope of ‘column’ or ‘row/col’
	 */
	@DatabaseField(columnName = COLUMN_COLUMN_NAME)
	private String columnName;

	/**
	 * NULL for reference_scope of ‘geopackage’, ‘table’ or ‘column’, or the
	 * rowed of a row record in the table_name table for reference_scope of
	 * ‘row’ or ‘row/col’
	 */
	@DatabaseField(columnName = COLUMN_ROW_ID_VALUE)
	private Long rowIdValue;

	/**
	 * timestamp value in ISO 8601 format as defined by the strftime function
	 * '%Y-%m-%dT%H:%M:%fZ' format string applied to the current time
	 */
	@DatabaseField(columnName = COLUMN_TIMESTAMP, persisterClass = DatePersister.class, canBeNull = false, defaultValue = DateConverter.DATETIME_FORMAT)
	private Date timestamp;

	/**
	 * Metadata
	 */
	@DatabaseField(columnName = COLUMN_FILE_ID, canBeNull = false, foreign = true, foreignAutoRefresh = true)
	private Metadata metadata;

	/**
	 * gpkg_metadata table id column value for the metadata to which this
	 * gpkg_metadata_reference applies
	 */
	@DatabaseField(columnName = COLUMN_FILE_ID, canBeNull = false, readOnly = true)
	private long fileId;

	/**
	 * Parent Metadata
	 */
	@DatabaseField(columnName = COLUMN_PARENT_ID, foreign = true, foreignAutoRefresh = true)
	private Metadata parentMetadata;

	/**
	 * gpkg_metadata table id column value for the hierarchical parent
	 * gpkg_metadata for the gpkg_metadata to which this gpkg_metadata_reference
	 * applies, or NULL if md_file_id forms the root of a metadata hierarchy
	 */
	@DatabaseField(columnName = COLUMN_PARENT_ID, readOnly = true)
	private Long parentId;

	/**
	 * Default Constructor
	 */
	public MetadataReference() {

	}

	/**
	 * Copy Constructor
	 * 
	 * @param metadataReference
	 *            metadata reference to copy
	 * @since 1.3.0
	 */
	public MetadataReference(MetadataReference metadataReference) {
		referenceScope = metadataReference.referenceScope;
		tableName = metadataReference.tableName;
		columnName = metadataReference.columnName;
		rowIdValue = metadataReference.rowIdValue;
		timestamp = new Date(metadataReference.timestamp.getTime());
		metadata = metadataReference.metadata;
		fileId = metadataReference.fileId;
		parentMetadata = metadataReference.parentMetadata;
		parentId = metadataReference.parentId;
	}

	/**
	 * Get the reference scope
	 * 
	 * @return reference scope
	 */
	public ReferenceScopeType getReferenceScope() {
		return ReferenceScopeType.fromValue(referenceScope);
	}

	/**
	 * Set the reference scope
	 * 
	 * @param referenceScope
	 *            reference scope
	 */
	public void setReferenceScope(ReferenceScopeType referenceScope) {
		this.referenceScope = referenceScope.getValue();
		switch (referenceScope) {
		case GEOPACKAGE:
			setTableName(null);
			setColumnName(null);
			setRowIdValue(null);
			break;
		case TABLE:
			setColumnName(null);
			setRowIdValue(null);
			break;
		case ROW:
			setColumnName(null);
			break;
		case COLUMN:
			setRowIdValue(null);
			break;
		case ROW_COL:
			break;
		default:

		}
	}

	/**
	 * Get the reference scope name
	 * 
	 * @return reference scope name
	 * @since 6.6.0
	 */
	public String getReferenceScopeName() {
		return referenceScope;
	}

	/**
	 * Get the table name
	 * 
	 * @return table name
	 */
	public String getTableName() {
		return tableName;
	}

	/**
	 * Set the table name
	 * 
	 * @param tableName
	 *            table name
	 */
	public void setTableName(String tableName) {
		if (referenceScope != null && tableName != null
				&& getReferenceScope().equals(ReferenceScopeType.GEOPACKAGE)) {
			throw new GeoPackageException("The table name must be null for "
					+ ReferenceScopeType.GEOPACKAGE + " reference scope");
		}
		this.tableName = tableName;

	}

	/**
	 * Get the column name
	 * 
	 * @return column name
	 */
	public String getColumnName() {
		return columnName;
	}

	/**
	 * Set the column name
	 * 
	 * @param columnName
	 *            column name
	 */
	public void setColumnName(String columnName) {
		if (referenceScope != null && columnName != null) {
			ReferenceScopeType scopeType = getReferenceScope();
			if (scopeType.equals(ReferenceScopeType.GEOPACKAGE)
					|| scopeType.equals(ReferenceScopeType.TABLE)
					|| scopeType.equals(ReferenceScopeType.ROW)) {
				throw new GeoPackageException(
						"The column name must be null for " + scopeType
								+ " reference scope");
			}
		}
		this.columnName = columnName;
	}

	/**
	 * Get the row id value
	 * 
	 * @return row id value
	 */
	public Long getRowIdValue() {
		return rowIdValue;
	}

	/**
	 * Set the row id value
	 * 
	 * @param rowIdValue
	 *            row id value
	 */
	public void setRowIdValue(Long rowIdValue) {
		if (referenceScope != null && rowIdValue != null) {
			ReferenceScopeType scopeType = getReferenceScope();
			if (scopeType.equals(ReferenceScopeType.GEOPACKAGE)
					|| scopeType.equals(ReferenceScopeType.TABLE)
					|| scopeType.equals(ReferenceScopeType.COLUMN)) {
				throw new GeoPackageException(
						"The row id value must be null for " + scopeType
								+ " reference scope");
			}
		}
		this.rowIdValue = rowIdValue;
	}

	/**
	 * Get the timestamp
	 * 
	 * @return timestamp
	 */
	public Date getTimestamp() {
		return timestamp;
	}

	/**
	 * Set the timestamp
	 * 
	 * @param timestamp
	 *            timestamp
	 */
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * Get the metadata
	 * 
	 * @return metadata
	 */
	public Metadata getMetadata() {
		return metadata;
	}

	/**
	 * Set the metadata
	 * 
	 * @param metadata
	 *            metadata
	 */
	public void setMetadata(Metadata metadata) {
		this.metadata = metadata;
		fileId = metadata != null ? metadata.getId() : -1;
	}

	/**
	 * Get the file id
	 * 
	 * @return file id
	 */
	public long getFileId() {
		return fileId;
	}

	/**
	 * Get the parent metadata
	 * 
	 * @return parent metadata
	 */
	public Metadata getParentMetadata() {
		return parentMetadata;
	}

	/**
	 * Set the parent metadata
	 * 
	 * @param parentMetadata
	 *            parent metadata
	 */
	public void setParentMetadata(Metadata parentMetadata) {
		this.parentMetadata = parentMetadata;
		parentId = parentMetadata != null ? parentMetadata.getId() : -1;
	}

	/**
	 * Get the parent id
	 * 
	 * @return parent id
	 */
	public Long getParentId() {
		return parentId;
	}

}
