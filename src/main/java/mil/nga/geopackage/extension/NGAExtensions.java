package mil.nga.geopackage.extension;

import java.sql.SQLException;

import mil.nga.geopackage.GeoPackageCore;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.extension.index.FeatureTableCoreIndex;
import mil.nga.geopackage.extension.index.GeometryIndex;
import mil.nga.geopackage.extension.index.TableIndex;
import mil.nga.geopackage.extension.index.TableIndexDao;

import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

/**
 * NGA extension management class for deleting extensions for a table or in a
 * GeoPackage
 * 
 * @author osbornb
 * @since 1.1.0
 */
public class NGAExtensions {

	/**
	 * Delete all NGA table extensions for the table within the GeoPackage
	 * 
	 * @param geoPackage
	 * @param table
	 */
	public static void deleteTableExtensions(GeoPackageCore geoPackage,
			String table) {

		ExtensionsDao extensionsDao = geoPackage.getExtensionsDao();

		// Delete table index
		TableIndexDao tableIndexDao = geoPackage.getTableIndexDao();
		try {
			tableIndexDao.deleteByIdCascade(table);
			extensionsDao.deleteByExtension(
					FeatureTableCoreIndex.EXTENSION_NAME, table);
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to delete Table Index. GeoPackage: "
							+ geoPackage.getName() + ", Table: " + table, e);
		}

		// Delete future extensions for the table here
	}

	/**
	 * Delete all NGA extensions including custom extension tables for the
	 * GeoPackage
	 * 
	 * @param geoPackage
	 */
	public static void deleteExtensions(GeoPackageCore geoPackage) {
		deleteExtensions(geoPackage, false);
	}

	/**
	 * Delete all NGA extensions including custom extension tables for the
	 * GeoPackage
	 * 
	 * @param geoPackage
	 * @param ignoreErrors
	 *            true to ignore errors when deleting tables
	 */
	public static void deleteExtensions(GeoPackageCore geoPackage,
			boolean ignoreErrors) {
		ConnectionSource connectionSource = geoPackage.getDatabase()
				.getConnectionSource();

		ExtensionsDao extensionsDao = geoPackage.getExtensionsDao();

		// Delete table index tables
		try {
			TableUtils.dropTable(connectionSource, GeometryIndex.class,
					ignoreErrors);
			TableUtils.dropTable(connectionSource, TableIndex.class,
					ignoreErrors);
			extensionsDao
					.deleteByExtension(FeatureTableCoreIndex.EXTENSION_NAME);
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to delete Table Index Tables. GeoPackage: "
							+ geoPackage.getName(), e);
		}

		// Delete future extension tables here
	}

}
