package mil.nga.geopackage.extension;

import java.sql.SQLException;

import mil.nga.geopackage.GeoPackageCore;
import mil.nga.geopackage.GeoPackageException;

/**
 * Abstract base GeoPackage extension
 * 
 * @author osbornb
 * @since 1.1.8
 */
public abstract class BaseExtension {

	/**
	 * GeoPackage Core
	 */
	protected final GeoPackageCore geoPackage;

	/**
	 * Extensions DAO
	 */
	protected final ExtensionsDao extensionsDao;

	/**
	 * Constructor
	 * 
	 * @param geoPackage
	 */
	protected BaseExtension(GeoPackageCore geoPackage) {
		this.geoPackage = geoPackage;
		extensionsDao = geoPackage.getExtensionsDao();
	}

	/**
	 * Get the GeoPackage
	 * 
	 * @return geoPackage
	 */
	public GeoPackageCore getGeoPackage() {
		return geoPackage;
	}

	/**
	 * Get the Extensions DAO
	 * 
	 * @return extensions DAO
	 */
	public ExtensionsDao getExtensionsDao() {
		return extensionsDao;
	}

	/**
	 * Get the extension or create as needed
	 * 
	 * @param extensionName
	 *            extension name
	 * @param tableName
	 *            table name
	 * @param columnName
	 *            column name
	 * @param definition
	 *            extension definition
	 * @param scopeType
	 *            extension scope type
	 * @return extension
	 */
	protected Extensions getOrCreate(String extensionName, String tableName,
			String columnName, String definition, ExtensionScopeType scopeType) {

		Extensions extension = get(extensionName, tableName, columnName);

		if (extension == null) {
			try {
				if (!extensionsDao.isTableExists()) {
					geoPackage.createExtensionsTable();
				}

				extension = new Extensions();
				extension.setTableName(tableName);
				extension.setColumnName(columnName);
				extension.setExtensionName(extensionName);
				extension.setDefinition(definition);
				extension.setScope(scopeType);

				extensionsDao.create(extension);
			} catch (SQLException e) {
				throw new GeoPackageException("Failed to create '"
						+ extensionName + "' extension for GeoPackage: "
						+ geoPackage.getName() + ", Table Name: " + tableName
						+ ", Column Name: " + columnName, e);
			}
		}

		return extension;
	}

	/**
	 * Get the extension for the name, table name, and column name
	 * 
	 * @param extensionName
	 *            extension name
	 * @param tableName
	 *            table name
	 * @param columnName
	 *            column name
	 * @return extension
	 */
	protected Extensions get(String extensionName, String tableName,
			String columnName) {

		Extensions extension = null;
		try {
			if (extensionsDao.isTableExists()) {
				extension = extensionsDao.queryByExtension(extensionName,
						tableName, columnName);
			}
		} catch (SQLException e) {
			throw new GeoPackageException("Failed to query for '"
					+ extensionName + "' extension for GeoPackage: "
					+ geoPackage.getName() + ", Table Name: " + tableName
					+ ", Column Name: " + columnName, e);
		}
		return extension;
	}

	/**
	 * Determine if the GeoPackage has the extension
	 * 
	 * @param extensionName
	 *            extension name
	 * @param tableName
	 *            table name
	 * @param columnName
	 *            column name
	 * @return true if has extension
	 */
	protected boolean has(String extensionName, String tableName,
			String columnName) {

		Extensions extension = get(extensionName, tableName, columnName);
		return extension != null;
	}

}
