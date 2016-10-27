package mil.nga.geopackage.extension;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Indicates that a particular extension applies to a GeoPackage, a table in a
 * GeoPackage or a column of a table in a GeoPackage. An application that access
 * a GeoPackage can query the gpkg_extensions table instead of the contents of
 * all the user data tables to determine if it has the required capabilities to
 * read or write to tables with extensions, and to “fail fast” and return an
 * error message if it does not.
 * 
 * @author osbornb
 */
@DatabaseTable(tableName = "gpkg_extensions", daoClass = ExtensionsDao.class)
public class Extensions {

	/**
	 * Divider between extension name parts
	 */
	public static final String EXTENSION_NAME_DIVIDER = "_";

	/**
	 * Table name
	 */
	public static final String TABLE_NAME = "gpkg_extensions";

	/**
	 * tableName field name
	 */
	public static final String COLUMN_TABLE_NAME = "table_name";

	/**
	 * columnName field name
	 */
	public static final String COLUMN_COLUMN_NAME = "column_name";

	/**
	 * extensionName field name
	 */
	public static final String COLUMN_EXTENSION_NAME = "extension_name";

	/**
	 * definition field name
	 */
	public static final String COLUMN_DEFINITION = "definition";

	/**
	 * scope field name
	 */
	public static final String COLUMN_SCOPE = "scope";

	/**
	 * Name of the table that requires the extension. When NULL, the extension
	 * is required for the entire GeoPackage. SHALL NOT be NULL when the
	 * column_name is not NULL.
	 */
	@DatabaseField(columnName = COLUMN_TABLE_NAME, uniqueCombo = true)
	private String tableName;

	/**
	 * Name of the column that requires the extension. When NULL, the extension
	 * is required for the entire table.
	 */
	@DatabaseField(columnName = COLUMN_COLUMN_NAME, uniqueCombo = true)
	private String columnName;

	/**
	 * The case sensitive name of the extension that is required, in the form
	 * <author>_<extension_name>.
	 */
	@DatabaseField(columnName = COLUMN_EXTENSION_NAME, canBeNull = false, uniqueCombo = true)
	private String extensionName;

	/**
	 * Definition of the extension in the form specfied by the template in
	 * GeoPackage Extension Template (Normative) or reference thereto.
	 */
	@DatabaseField(columnName = COLUMN_DEFINITION, canBeNull = false)
	private String definition;

	/**
	 * Indicates scope of extension effects on readers / writers: read-write or
	 * write-only in lowercase.
	 */
	@DatabaseField(columnName = COLUMN_SCOPE, canBeNull = false)
	private String scope;

	/**
	 * Default Constructor
	 */
	public Extensions() {

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
		this.tableName = tableName;
		if (tableName == null) {
			columnName = null;
		}
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
		this.columnName = columnName;
	}

	/**
	 * Get the extension name
	 * 
	 * @return extension name
	 */
	public String getExtensionName() {
		return extensionName;
	}

	/**
	 * Set the extension name
	 * 
	 * @param extensionName
	 *            extension name
	 */
	public void setExtensionName(String extensionName) {
		this.extensionName = extensionName;
	}

	/**
	 * Set the extension name by combining the required parts
	 * 
	 * @param author
	 * @param extensionName
	 */
	public void setExtensionName(String author, String extensionName) {
		setExtensionName(buildExtensionName(author, extensionName));
	}

	/**
	 * Get the author from the beginning of the extension name
	 * 
	 * @return author
	 */
	public String getAuthor() {
		return getAuthor(extensionName);
	}

	/**
	 * Get the extension name with the author prefix removed
	 * 
	 * @return extension name without the author
	 */
	public String getExtensionNameNoAuthor() {
		return getExtensionNameNoAuthor(extensionName);
	}

	/**
	 * Get the definition
	 * 
	 * @return definition
	 */
	public String getDefinition() {
		return definition;
	}

	/**
	 * Set the definition
	 * 
	 * @param definition
	 *            definition
	 */
	public void setDefinition(String definition) {
		this.definition = definition;
	}

	/**
	 * Get the extension scope type
	 * 
	 * @return extension scope type
	 */
	public ExtensionScopeType getScope() {
		return ExtensionScopeType.fromValue(scope);
	}

	/**
	 * Set the extension scope type
	 * 
	 * @param scope
	 *            extension scope type
	 */
	public void setScope(ExtensionScopeType scope) {
		this.scope = scope.getValue();
	}

	/**
	 * Build the extension name by combining the required parts
	 * 
	 * @param author
	 *            extension author
	 * @param extensionName
	 *            extension name
	 * @return extension name
	 * @since 1.1.0
	 */
	public static String buildExtensionName(String author, String extensionName) {
		return author + EXTENSION_NAME_DIVIDER + extensionName;
	}

	/**
	 * Get the author from the beginning of the extension name
	 * 
	 * @param extensionName
	 *            extension name
	 * @return author extension author
	 * @since 1.1.0
	 */
	public static String getAuthor(String extensionName) {
		String author = null;
		if (extensionName != null) {
			author = extensionName.substring(0,
					extensionName.indexOf(EXTENSION_NAME_DIVIDER));
		}
		return author;
	}

	/**
	 * Get the extension name with the author prefix removed
	 * 
	 * @param extensionName
	 *            extension name
	 * @return extension name, no author
	 * @since 1.1.0
	 */
	public static String getExtensionNameNoAuthor(String extensionName) {
		String value = null;
		if (extensionName != null) {
			value = extensionName.substring(
					extensionName.indexOf(EXTENSION_NAME_DIVIDER) + 1,
					extensionName.length());
		}
		return value;
	}

}
