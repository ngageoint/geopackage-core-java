package mil.nga.geopackage.extension;

import java.sql.SQLException;

import mil.nga.geopackage.GeoPackageCore;
import mil.nga.geopackage.GeoPackageException;

/**
 * GeoPackage extension management class for deleting extensions for a table or
 * in a GeoPackage
 * 
 * @author osbornb
 * @since 1.1.8
 */
public class GeoPackageExtensions {

	/**
	 * Delete all table extensions for the table within the GeoPackage
	 * 
	 * @param geoPackage
	 * @param table
	 */
	public static void deleteTableExtensions(GeoPackageCore geoPackage,
			String table) {

		// Handle deleting any extensions with extra tables here
		NGAExtensions.deleteTableExtensions(geoPackage, table);

		delete(geoPackage, table);
	}

	/**
	 * Delete all extensions
	 * 
	 * @param geoPackage
	 */
	public static void deleteExtensions(GeoPackageCore geoPackage) {
		deleteExtensions(geoPackage, false);
	}

	/**
	 * Delete all extensions
	 * 
	 * @param geoPackage
	 * @param ignoreErrors
	 *            true to ignore errors when deleting tables
	 */
	public static void deleteExtensions(GeoPackageCore geoPackage,
			boolean ignoreErrors) {

		// Handle deleting any extensions with extra tables here
		NGAExtensions.deleteExtensions(geoPackage, ignoreErrors);

		delete(geoPackage, ignoreErrors);
	}

	/**
	 * Delete the extensions for the table
	 * 
	 * @param geoPackage
	 * @param table
	 */
	private static void delete(GeoPackageCore geoPackage, String table) {

		ExtensionsDao extensionsDao = geoPackage.getExtensionsDao();

		try {
			if (extensionsDao.isTableExists()) {
				extensionsDao.deleteByTableName(table);
			}
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to delete Table extensions. GeoPackage: "
							+ geoPackage.getName() + ", Table: " + table, e);
		}
	}

	/**
	 * Delete the extensions
	 * 
	 * @param geoPackage
	 * @param ignoreErrors
	 */
	private static void delete(GeoPackageCore geoPackage, boolean ignoreErrors) {

		ExtensionsDao extensionsDao = geoPackage.getExtensionsDao();

		try {
			if (extensionsDao.isTableExists()) {
				extensionsDao.deleteAll();
			}
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to delete all extensions. GeoPackage: "
							+ geoPackage.getName(), e);
		}

	}

}
