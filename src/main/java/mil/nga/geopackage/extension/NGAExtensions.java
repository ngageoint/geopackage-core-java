package mil.nga.geopackage.extension;

import java.sql.SQLException;

import mil.nga.geopackage.GeoPackageCore;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.core.contents.ContentsDao;
import mil.nga.geopackage.extension.contents.ContentsIdExtension;
import mil.nga.geopackage.extension.index.FeatureTableCoreIndex;
import mil.nga.geopackage.extension.index.GeometryIndexDao;
import mil.nga.geopackage.extension.index.TableIndexDao;
import mil.nga.geopackage.extension.link.FeatureTileLinkDao;
import mil.nga.geopackage.extension.link.FeatureTileTableCoreLinker;
import mil.nga.geopackage.extension.properties.PropertiesCoreExtension;
import mil.nga.geopackage.extension.related.RelatedTablesCoreExtension;
import mil.nga.geopackage.extension.scale.TileScalingDao;
import mil.nga.geopackage.extension.scale.TileTableScaling;
import mil.nga.geopackage.extension.style.FeatureCoreStyleExtension;

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
		deleteProperties(geoPackage, table);
		deleteFeatureStyle(geoPackage, table);
		deleteContentsId(geoPackage, table);

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

		deleteGeometryIndexExtension(geoPackage);
		deleteFeatureTileLinkExtension(geoPackage);
		deleteTileScalingExtension(geoPackage);
		deletePropertiesExtension(geoPackage);
		deleteFeatureStyleExtension(geoPackage);
		deleteContentsIdExtension(geoPackage);

		// Delete future extension tables here
	}

	/**
	 * Copy all NGA table extensions for the table within the GeoPackage
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * @param table
	 *            table name
	 * @param newTable
	 *            new table name
	 * @since 3.2.1
	 */
	public static void copyTableExtensions(GeoPackageCore geoPackage,
			String table, String newTable) {

		copyGeometryIndex(geoPackage, table, newTable);
		copyFeatureTileLink(geoPackage, table, newTable);
		copyTileScaling(geoPackage, table, newTable);
		copyFeatureStyle(geoPackage, table, newTable);
		copyContentsId(geoPackage, table, newTable);

		// Copy future extensions for the table here
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
							+ geoPackage.getName() + ", Table: " + table,
					e);
		}
	}

	/**
	 * Delete the Geometry Index extension including the extension entries and
	 * custom tables
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * @since 3.2.0
	 */
	public static void deleteGeometryIndexExtension(GeoPackageCore geoPackage) {

		GeometryIndexDao geometryIndexDao = geoPackage.getGeometryIndexDao();
		TableIndexDao tableIndexDao = geoPackage.getTableIndexDao();
		ExtensionsDao extensionsDao = geoPackage.getExtensionsDao();

		try {
			if (geometryIndexDao.isTableExists()) {
				geoPackage.dropTable(geometryIndexDao.getTableName());
			}
			if (tableIndexDao.isTableExists()) {
				geoPackage.dropTable(tableIndexDao.getTableName());
			}
			if (extensionsDao.isTableExists()) {
				extensionsDao.deleteByExtension(
						FeatureTableCoreIndex.EXTENSION_NAME);
			}
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to delete Table Index extension and tables. GeoPackage: "
							+ geoPackage.getName(),
					e);
		}

	}

	/**
	 * Copy the Geometry Index extension for the table
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * @param table
	 *            table name
	 * @param newTable
	 *            new table name
	 * @since 3.2.1
	 */
	public static void copyGeometryIndex(GeoPackageCore geoPackage,
			String table, String newTable) {
		// TODO
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
							+ geoPackage.getName() + ", Table: " + table,
					e);
		}
	}

	/**
	 * Delete the Feature Tile Link extension including the extension entries
	 * and custom tables
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * @since 3.2.0
	 */
	public static void deleteFeatureTileLinkExtension(
			GeoPackageCore geoPackage) {

		FeatureTileLinkDao featureTileLinkDao = geoPackage
				.getFeatureTileLinkDao();
		ExtensionsDao extensionsDao = geoPackage.getExtensionsDao();
		try {
			if (featureTileLinkDao.isTableExists()) {
				geoPackage.dropTable(featureTileLinkDao.getTableName());
			}
			if (extensionsDao.isTableExists()) {
				extensionsDao.deleteByExtension(
						FeatureTileTableCoreLinker.EXTENSION_NAME);
			}
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to delete Feature Tile Link extension and table. GeoPackage: "
							+ geoPackage.getName(),
					e);
		}

	}

	/**
	 * Copy the Feature Tile Link extensions for the table
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * @param table
	 *            table name
	 * @param newTable
	 *            new table name
	 * @since 3.2.1
	 */
	public static void copyFeatureTileLink(GeoPackageCore geoPackage,
			String table, String newTable) {
		// TODO
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
	public static void deleteTileScaling(GeoPackageCore geoPackage,
			String table) {

		TileScalingDao tileScalingDao = geoPackage.getTileScalingDao();
		ExtensionsDao extensionsDao = geoPackage.getExtensionsDao();

		try {
			if (tileScalingDao.isTableExists()) {
				tileScalingDao.deleteById(table);
			}
			if (extensionsDao.isTableExists()) {
				extensionsDao.deleteByExtension(TileTableScaling.EXTENSION_NAME,
						table);
			}
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to delete Tile Scaling. GeoPackage: "
							+ geoPackage.getName() + ", Table: " + table,
					e);
		}
	}

	/**
	 * Delete the Tile Scaling extension including the extension entries and
	 * custom tables
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * @since 3.2.0
	 */
	public static void deleteTileScalingExtension(GeoPackageCore geoPackage) {

		TileScalingDao tileScalingDao = geoPackage.getTileScalingDao();
		ExtensionsDao extensionsDao = geoPackage.getExtensionsDao();
		try {
			if (tileScalingDao.isTableExists()) {
				geoPackage.dropTable(tileScalingDao.getTableName());
			}
			if (extensionsDao.isTableExists()) {
				extensionsDao
						.deleteByExtension(TileTableScaling.EXTENSION_NAME);
			}
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to delete Tile Scaling extension and table. GeoPackage: "
							+ geoPackage.getName(),
					e);
		}

	}

	/**
	 * Copy the Tile Scaling extensions for the table
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * @param table
	 *            table name
	 * @param newTable
	 *            new table name
	 * @since 3.2.1
	 */
	public static void copyTileScaling(GeoPackageCore geoPackage, String table,
			String newTable) {
		// TODO
	}

	/**
	 * Delete the Properties extension if the deleted table is the properties
	 * table
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * @param table
	 *            table name
	 * @since 3.2.0
	 */
	public static void deleteProperties(GeoPackageCore geoPackage,
			String table) {

		if (table.equalsIgnoreCase(PropertiesCoreExtension.TABLE_NAME)) {
			deletePropertiesExtension(geoPackage);
		}

	}

	/**
	 * Delete the properties extension from the GeoPackage
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * @since 3.0.2
	 */
	public static void deletePropertiesExtension(GeoPackageCore geoPackage) {

		ExtensionsDao extensionsDao = geoPackage.getExtensionsDao();

		if (geoPackage.isTable(PropertiesCoreExtension.TABLE_NAME)) {
			ContentsDao contentsDao = geoPackage.getContentsDao();
			contentsDao.deleteTable(PropertiesCoreExtension.TABLE_NAME);
		}

		try {
			if (extensionsDao.isTableExists()) {
				extensionsDao.deleteByExtension(
						PropertiesCoreExtension.EXTENSION_NAME,
						PropertiesCoreExtension.TABLE_NAME);
			}
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to delete Properties extension. GeoPackage: "
							+ geoPackage.getName(),
					e);
		}

	}

	/**
	 * Delete the Feature Style extensions for the table
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * @param table
	 *            table name
	 * @since 3.2.0
	 */
	public static void deleteFeatureStyle(GeoPackageCore geoPackage,
			String table) {

		FeatureCoreStyleExtension featureStyleExtension = getFeatureStyleExtension(
				geoPackage);
		if (featureStyleExtension.has(table)) {
			featureStyleExtension.deleteRelationships(table);
		}

	}

	/**
	 * Delete the Feature Style extension including the extension entries and
	 * custom tables
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * @since 3.2.0
	 */
	public static void deleteFeatureStyleExtension(GeoPackageCore geoPackage) {

		FeatureCoreStyleExtension featureStyleExtension = getFeatureStyleExtension(
				geoPackage);
		if (featureStyleExtension.has()) {
			featureStyleExtension.removeExtension();
		}

	}

	/**
	 * Copy the Feature Style extensions for the table
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * @param table
	 *            table name
	 * @param newTable
	 *            new table name
	 * @since 3.2.1
	 */
	public static void copyFeatureStyle(GeoPackageCore geoPackage, String table,
			String newTable) {
		// TODO
	}

	/**
	 * Get a Feature Style Extension used only for deletions
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * @return Feature Style Extension
	 */
	private static FeatureCoreStyleExtension getFeatureStyleExtension(
			GeoPackageCore geoPackage) {

		RelatedTablesCoreExtension relatedTables = new RelatedTablesCoreExtension(
				geoPackage) {
		};

		return new FeatureCoreStyleExtension(geoPackage, relatedTables) {
		};
	}

	/**
	 * Delete the Contents Id extensions for the table
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * @param table
	 *            table name
	 * @since 3.2.0
	 */
	public static void deleteContentsId(GeoPackageCore geoPackage,
			String table) {

		ContentsIdExtension contentsIdExtension = new ContentsIdExtension(
				geoPackage);
		if (contentsIdExtension.has()) {
			contentsIdExtension.delete(table);
		}

	}

	/**
	 * Delete the Contents Id extension including the extension entries and
	 * custom tables
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * @since 3.2.0
	 */
	public static void deleteContentsIdExtension(GeoPackageCore geoPackage) {

		ContentsIdExtension contentsIdExtension = new ContentsIdExtension(
				geoPackage);
		if (contentsIdExtension.has()) {
			contentsIdExtension.removeExtension();
		}

	}

	/**
	 * Copy the Contents Id extensions for the table
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * @param table
	 *            table name
	 * @param newTable
	 *            new table name
	 * @since 3.2.1
	 */
	public static void copyContentsId(GeoPackageCore geoPackage, String table,
			String newTable) {
		// TODO
	}

}
