package mil.nga.tiff.io;

/**
 * TIFF File Directory Entry
 * 
 * @author osbornb
 */
public class FileDirectoryEntry {

	/**
	 * Field Tag Type
	 */
	private final FieldTagType fieldTag;

	/**
	 * Field Type
	 */
	private final FieldType fieldType;

	/**
	 * Type Count
	 */
	private final long typeCount;

	/**
	 * Values
	 */
	private final Object values;

	/**
	 * Constructor
	 * 
	 * @param fieldTag
	 *            field tag type
	 * @param fieldType
	 *            field type
	 * @param typeCount
	 *            type count
	 * @param values
	 *            values
	 */
	public FileDirectoryEntry(FieldTagType fieldTag, FieldType fieldType,
			long typeCount, Object values) {
		this.fieldTag = fieldTag;
		this.fieldType = fieldType;
		this.typeCount = typeCount;
		this.values = values;
	}

	/**
	 * Get the field tag type
	 * 
	 * @return field tag type
	 */
	public FieldTagType getFieldTag() {
		return fieldTag;
	}

	/**
	 * Get the field type
	 * 
	 * @return field type
	 */
	public FieldType getFieldType() {
		return fieldType;
	}

	/**
	 * Get the type count
	 * 
	 * @return type count
	 */
	public long getTypeCount() {
		return typeCount;
	}

	/**
	 * Get the values
	 * 
	 * @return values
	 */
	public Object getValues() {
		return values;
	}

}
