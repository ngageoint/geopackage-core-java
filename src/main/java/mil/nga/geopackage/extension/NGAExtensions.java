package mil.nga.geopackage.extension;

import java.sql.SQLException;

import mil.nga.geopackage.GeoPackageCore;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.extension.index.FeatureTableCoreIndex;
import mil.nga.geopackage.extension.index.GeometryIndex;
import mil.nga.geopackage.extension.index.GeometryIndexDao;
import mil.nga.geopackage.extension.index.TableIndex;
import mil.nga.geopackage.extension.index.TableIndexDao;
import mil.nga.geopackage.extension.link.FeatureTileLink;
import mil.nga.geopackage.extension.link.FeatureTileLinkDao;
import mil.nga.geopackage.extension.link.FeatureTileTableCoreLinker;
import mil.nga.geopackage.extension.scale.TileScaling;
import mil.nga.geopackage.extension.scale.TileScalingDao;
import mil.nga.geopackage.extension.scale.TileTableScaling;

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
	 *            GeoPackage
	 * @param table
	 *            table name
	 */
	public static void deleteTableExtensions(GeoPackageCore geoPackage,
			String table) {

		deleteGeometryIndex(geoPackage, table);
		deleteFeatureTileLink(geoPackage, table);
		deleteTileScaling(geoPackage, table);

		// Delete future extensions for the table here
	}

	/**
	 * Delete all NGA extensions including custom extension tables for the
	 * GeoPackage
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 */
	public static void deleteExtensions(GeoPackageCore geoPackage) {
		deleteExtensions(geoPackage, false);
	}

	/**
	 * Delete all NGA extensions including custom extension tables for the
	 * GeoPackage
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * @param ignoreErrors
	 *            true to ignore errors when deleting tables
	 */
	public static void deleteExtensions(GeoPackageCore geoPackage,
			boolean ignoreErrors) {

		deleteGeometryIndexExtension(geoPackage, ignoreErrors);
		deleteFeatureTileLinkExtension(geoPackage, ignoreErrors);
		deleteTileScalingExtension(geoPackage, ignoreErrors);

		// Delete future extension tables here
	}

	/**
	 * Delete the Geometry Index extension for the table
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * @param table
	 *            table name
	 * @since 1.1.5
	 */
	public static void deleteGeometryIndex(GeoPackageCore geoPackage,
			String table) {

		TableIndexDao tableIndexDao = geoPackage.getTableIndexDao();
		ExtensionsDao extensionsDao = geoPackage.getExtensionsDao();

		try {
			if (tableIndexDao.isTableExists()) {
				tableIndexDao.deleteByIdCascade(table);
			}
			if (extensionsDao.isTableExists()) {
				extensionsDao.deleteByExtension(
						FeatureTableCoreIndex.EXTENSION_NAME, table);
			}
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to delete Table Index. GeoPackage: "
							+ geoPackage.getName() + ", Table: " + table, e);
		}
	}

	/**
	 * Delete the Geometry Index extension including the extension entries and
	 * custom tables
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * @param ignoreErrors
	 *            true to ignore errors
	 * @since 1.1.5
	 */
	public static void deleteGeometryIndexExtension(GeoPackageCore geoPackage,
			boolean ignoreErrors) {

		GeometryIndexDao geometryIndexDao = geoPackage.getGeometryIndexDao();
		TableIndexDao tableIndexDao = geoPackage.getTableIndexDao();
		ExtensionsDao extensionsDao = geoPackage.getExtensionsDao();
		ConnectionSource connectionSource = geoPackage.getDatabase()
				.getConnectionSource();

		try {
			if (geometryIndexDao.isTableExists()) {
				TableUtils.dropTable(connectionSource, GeometryIndex.class,
						ignoreErrors);
			}
			if (tableIndexDao.isTableExists()) {
				TableUtils.dropTable(connectionSource, TableIndex.class,
						ignoreErrors);
			}
			if (extensionsDao.isTableExists()) {
				extensionsDao
						.deleteByExtension(FeatureTableCoreIndex.EXTENSION_NAME);
			}
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to delete Table Index extension and tables. GeoPackage: "
							+ geoPackage.getName(), e);
		}

	}

	/**
	 * Delete the Feature Tile Link extensions for the table
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * @param table
	 *            table name
	 * @since 1.1.5
	 */
	public static void deleteFeatureTileLink(GeoPackageCore geoPackage,
			String table) {

		FeatureTileLinkDao featureTileLinkDao = geoPackage
				.getFeatureTileLinkDao();
		try {
			if (featureTileLinkDao.isTableExists()) {
				featureTileLinkDao.deleteByTableName(table);
			}
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to delete Feature Tile Link. GeoPackage: "
							+ geoPackage.getName() + ", Table: " + table, e);
		}
	}

	/**
	 * Delete the Feature Tile Link extension including the extension entries
	 * and custom tables
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * @param ignoreErrors
	 *            true to ignore errors
	 * @since 1.1.5
	 */
	public static void deleteFeatureTileLinkExtension(
			GeoPackageCore geoPackage, boolean ignoreErrors) {

		FeatureTileLinkDao featureTileLinkDao = geoPackage
				.getFeatureTileLinkDao();
		ExtensionsDao extensionsDao = geoPackage.getExtensionsDao();
		ConnectionSource connectionSource = geoPackage.getDatabase()
				.getConnectionSource();
		try {
			if (featureTileLinkDao.isTableExists()) {
				TableUtils.dropTable(connectionSource, FeatureTileLink.class,
						ignoreErrors);
			}
			if (extensionsDao.isTableExists()) {
				extensionsDao
						.deleteByExtension(FeatureTileTableCoreLinker.EXTENSION_NAME);
			}
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to delete Feature Tile Link extension and table. GeoPackage: "
							+ geoPackage.getName(), e);
		}

	}

	/**
	 * Delete the Tile Scaling extensions for the table
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * @param table
	 *            table name
	 * @since 2.0.2
	 */
	public static void deleteTileScaling(GeoPackageCore geoPackage, String table) {

		TileScalingDao tileScalingDao = geoPackage.getTileScalingDao();
		ExtensionsDao extensionsDao = geoPackage.getExtensionsDao();

		try {
			if (tileScalingDao.isTableExists()) {
				tileScalingDao.deleteById(table);
			}
			if (extensionsDao.isTableExists()) {
				extensionsDao.deleteByExtension(
						TileTableScaling.EXTENSION_NAME, table);
			}
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to delete Tile Scaling. GeoPackage: "
							+ geoPackage.getName() + ", Table: " + table, e);
		}
	}

	/**
	 * Delete the Tile Scaling extension including the extension entries and
	 * custom tables
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * @param ignoreErrors
	 *            true to ignore errors
	 * @since 2.0.2
	 */
	public static void deleteTileScalingExtension(GeoPackageCore geoPackage,
			boolean ignoreErrors) {

		TileScalingDao tileScalingDao = geoPackage.getTileScalingDao();
		ExtensionsDao extensionsDao = geoPackage.getExtensionsDao();
		ConnectionSource connectionSource = geoPackage.getDatabase()
				.getConnectionSource();
		try {
			if (tileScalingDao.isTableExists()) {
				TableUtils.dropTable(connectionSource, TileScaling.class,
						ignoreErrors);
			}
			if (extensionsDao.isTableExists()) {
				extensionsDao
						.deleteByExtension(TileTableScaling.EXTENSION_NAME);
			}
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to delete Tile Scaling extension and table. GeoPackage: "
							+ geoPackage.getName(), e);
		}

	}

}
