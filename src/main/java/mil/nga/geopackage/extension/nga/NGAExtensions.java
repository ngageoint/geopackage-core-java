package mil.nga.geopackage.extension.nga;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import mil.nga.geopackage.GeoPackageCore;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.db.AlterTable;
import mil.nga.geopackage.db.CoreSQLUtils;
import mil.nga.geopackage.db.MappedColumn;
import mil.nga.geopackage.db.TableMapping;
import mil.nga.geopackage.extension.ExtensionManagement;
import mil.nga.geopackage.extension.ExtensionManager;
import mil.nga.geopackage.extension.Extensions;
import mil.nga.geopackage.extension.ExtensionsDao;
import mil.nga.geopackage.extension.nga.contents.ContentsId;
import mil.nga.geopackage.extension.nga.contents.ContentsIdExtension;
import mil.nga.geopackage.extension.nga.index.FeatureTableCoreIndex;
import mil.nga.geopackage.extension.nga.index.GeometryIndex;
import mil.nga.geopackage.extension.nga.index.GeometryIndexDao;
import mil.nga.geopackage.extension.nga.index.TableIndex;
import mil.nga.geopackage.extension.nga.index.TableIndexDao;
import mil.nga.geopackage.extension.nga.link.FeatureTileLink;
import mil.nga.geopackage.extension.nga.link.FeatureTileLinkDao;
import mil.nga.geopackage.extension.nga.link.FeatureTileTableCoreLinker;
import mil.nga.geopackage.extension.nga.properties.PropertiesCoreExtension;
import mil.nga.geopackage.extension.nga.scale.TileScaling;
import mil.nga.geopackage.extension.nga.scale.TileScalingDao;
import mil.nga.geopackage.extension.nga.scale.TileTableScaling;
import mil.nga.geopackage.extension.nga.style.FeatureCoreStyleExtension;
import mil.nga.geopackage.extension.related.ExtendedRelation;
import mil.nga.geopackage.extension.related.RelatedTablesCoreExtension;
import mil.nga.geopackage.extension.related.UserMappingTable;
import mil.nga.geopackage.user.UserCoreDao;
import mil.nga.geopackage.user.UserCoreRow;
import mil.nga.geopackage.user.custom.UserCustomTable;
import mil.nga.geopackage.user.custom.UserCustomTableReader;

/**
 * NGA Extensions
 * 
 * http://ngageoint.github.io/GeoPackage/docs/extensions/
 * 
 * @author osbornb
 * @since 4.0.0
 */
public class NGAExtensions extends ExtensionManagement {

	/**
	 * Logger
	 */
	private static final Logger logger = Logger
			.getLogger(NGAExtensions.class.getName());

	/**
	 * Extension author
	 */
	public static final String EXTENSION_AUTHOR = "nga";

	/**
	 * Constructor
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 */
	public NGAExtensions(GeoPackageCore geoPackage) {
		super(geoPackage);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getAuthor() {
		return EXTENSION_AUTHOR;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteTableExtensions(String table) {

		deleteGeometryIndex(table);
		deleteFeatureTileLink(table);
		deleteTileScaling(table);
		deleteProperties(table);
		deleteFeatureStyle(table);
		deleteContentsId(table);

		// Delete future extensions for the table here
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteExtensions() {

		deleteGeometryIndexExtension();
		deleteFeatureTileLinkExtension();
		deleteTileScalingExtension();
		deletePropertiesExtension();
		deleteFeatureStyleExtension();
		deleteContentsIdExtension();

		// Delete future extension tables here
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void copyTableExtensions(String table, String newTable) {

		copyContentsId(table, newTable);
		copyFeatureStyle(table, newTable);
		copyTileScaling(table, newTable);
		copyFeatureTileLink(table, newTable);
		copyGeometryIndex(table, newTable);

		// Copy future extensions for the table here
	}

	/**
	 * Delete the Geometry Index extension for the table
	 * 
	 * @param table
	 *            table name
	 */
	public void deleteGeometryIndex(String table) {

		TableIndexDao tableIndexDao = FeatureTableCoreIndex
				.getTableIndexDao(geoPackage);
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
	 */
	public void deleteGeometryIndexExtension() {

		GeometryIndexDao geometryIndexDao = FeatureTableCoreIndex
				.getGeometryIndexDao(geoPackage);
		TableIndexDao tableIndexDao = FeatureTableCoreIndex
				.getTableIndexDao(geoPackage);
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
	 * @param table
	 *            table name
	 * @param newTable
	 *            new table name
	 */
	public void copyGeometryIndex(String table, String newTable) {

		try {

			ExtensionsDao extensionsDao = geoPackage.getExtensionsDao();

			if (extensionsDao.isTableExists()) {

				List<Extensions> extensions = extensionsDao.queryByExtension(
						FeatureTableCoreIndex.EXTENSION_NAME, table);

				if (!extensions.isEmpty()) {

					Extensions extension = extensions.get(0);
					extension.setTableName(newTable);
					extensionsDao.create(extension);

					TableIndexDao tableIndexDao = FeatureTableCoreIndex
							.getTableIndexDao(geoPackage);
					if (tableIndexDao.isTableExists()) {

						TableIndex tableIndex = tableIndexDao.queryForId(table);
						if (tableIndex != null) {

							tableIndex.setTableName(newTable);
							tableIndexDao.create(tableIndex);

							if (geoPackage
									.isTableOrView(GeometryIndex.TABLE_NAME)) {

								CoreSQLUtils.transferTableContent(
										geoPackage.getDatabase(),
										GeometryIndex.TABLE_NAME,
										GeometryIndex.COLUMN_TABLE_NAME,
										newTable, table);

							}
						}
					}
				}
			}

		} catch (Exception e) {
			logger.log(Level.WARNING,
					"Failed to create Geometry Index for table: " + newTable
							+ ", copied from table: " + table,
					e);
		}

	}

	/**
	 * Delete the Feature Tile Link extensions for the table
	 * 
	 * @param table
	 *            table name
	 */
	public void deleteFeatureTileLink(String table) {

		FeatureTileLinkDao featureTileLinkDao = FeatureTileTableCoreLinker
				.getFeatureTileLinkDao(geoPackage);
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
	 */
	public void deleteFeatureTileLinkExtension() {

		FeatureTileLinkDao featureTileLinkDao = FeatureTileTableCoreLinker
				.getFeatureTileLinkDao(geoPackage);
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
	 * @param table
	 *            table name
	 * @param newTable
	 *            new table name
	 */
	public void copyFeatureTileLink(String table, String newTable) {

		try {

			ExtensionsDao extensionsDao = geoPackage.getExtensionsDao();

			if (extensionsDao.isTableExists()) {

				List<Extensions> extensions = extensionsDao.queryByExtension(
						FeatureTileTableCoreLinker.EXTENSION_NAME);

				if (!extensions.isEmpty()) {

					FeatureTileLinkDao featureTileLinkDao = FeatureTileTableCoreLinker
							.getFeatureTileLinkDao(geoPackage);
					if (featureTileLinkDao.isTableExists()) {

						for (FeatureTileLink featureTileLink : featureTileLinkDao
								.queryForFeatureTableName(table)) {
							featureTileLink.setFeatureTableName(newTable);
							featureTileLinkDao.create(featureTileLink);
						}

						for (FeatureTileLink featureTileLink : featureTileLinkDao
								.queryForTileTableName(table)) {
							featureTileLink.setTileTableName(newTable);
							featureTileLinkDao.create(featureTileLink);
						}

					}

				}
			}

		} catch (Exception e) {
			logger.log(Level.WARNING,
					"Failed to create Feature Tile Link for table: " + newTable
							+ ", copied from table: " + table,
					e);
		}

	}

	/**
	 * Delete the Tile Scaling extensions for the table
	 * 
	 * @param table
	 *            table name
	 */
	public void deleteTileScaling(String table) {

		TileScalingDao tileScalingDao = TileTableScaling
				.getTileScalingDao(geoPackage);
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
	 */
	public void deleteTileScalingExtension() {

		TileScalingDao tileScalingDao = TileTableScaling
				.getTileScalingDao(geoPackage);
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
	 * @param table
	 *            table name
	 * @param newTable
	 *            new table name
	 */
	public void copyTileScaling(String table, String newTable) {

		try {

			TileTableScaling tileTableScaling = new TileTableScaling(geoPackage,
					table);

			if (tileTableScaling.has()) {

				Extensions extension = tileTableScaling.getExtension();

				if (extension != null) {
					extension.setTableName(newTable);
					tileTableScaling.getExtensionsDao().create(extension);

					if (geoPackage.isTableOrView(TileScaling.TABLE_NAME)) {

						CoreSQLUtils.transferTableContent(
								geoPackage.getDatabase(),
								TileScaling.TABLE_NAME,
								TileScaling.COLUMN_TABLE_NAME, newTable, table);

					}
				}
			}

		} catch (Exception e) {
			logger.log(Level.WARNING,
					"Failed to create Tile Scaling for table: " + newTable
							+ ", copied from table: " + table,
					e);
		}

	}

	/**
	 * Delete the Properties extension if the deleted table is the properties
	 * table
	 * 
	 * @param table
	 *            table name
	 */
	public void deleteProperties(String table) {

		if (table.equalsIgnoreCase(PropertiesCoreExtension.TABLE_NAME)) {
			deletePropertiesExtension();
		}

	}

	/**
	 * Delete the properties extension from the GeoPackage
	 */
	public void deletePropertiesExtension() {
		getPropertiesExtension().removeExtension();
	}

	/**
	 * Get a Properties Extension used only for deletions
	 * 
	 * @return Feature Style Extension
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private PropertiesCoreExtension getPropertiesExtension() {
		return new PropertiesCoreExtension(geoPackage) {

			@Override
			protected UserCoreDao getDao() {
				return null;
			}

			@Override
			protected UserCoreRow newRow() {
				return null;
			}
		};
	}

	/**
	 * Delete the Feature Style extensions for the table
	 * 
	 * @param table
	 *            table name
	 */
	public void deleteFeatureStyle(String table) {

		FeatureCoreStyleExtension featureStyleExtension = getFeatureStyleExtension();
		if (featureStyleExtension.has(table)) {
			featureStyleExtension.deleteRelationships(table);
		}

	}

	/**
	 * Delete the Feature Style extension including the extension entries and
	 * custom tables
	 */
	public void deleteFeatureStyleExtension() {

		FeatureCoreStyleExtension featureStyleExtension = getFeatureStyleExtension();
		if (featureStyleExtension.has()) {
			featureStyleExtension.removeExtension();
		}

	}

	/**
	 * Copy the Feature Style extensions for the table. Relies on
	 * {@link ExtensionManager#copyRelatedTables(String, String)} to be called
	 * first.
	 * 
	 * @param table
	 *            table name
	 * @param newTable
	 *            new table name
	 */
	public void copyFeatureStyle(String table, String newTable) {

		try {

			FeatureCoreStyleExtension featureStyleExtension = getFeatureStyleExtension();
			if (featureStyleExtension.hasRelationship(table)) {

				Extensions extension = featureStyleExtension.get(table);

				if (extension != null) {
					extension.setTableName(newTable);
					featureStyleExtension.getExtensionsDao().create(extension);

					ContentsIdExtension contentsIdExtension = featureStyleExtension
							.getContentsId();
					Long contentsId = contentsIdExtension.getId(table);
					Long newContentsId = contentsIdExtension.getId(newTable);

					if (contentsId != null && newContentsId != null) {

						if (featureStyleExtension
								.hasTableStyleRelationship(table)) {

							copyFeatureTableStyle(featureStyleExtension,
									FeatureCoreStyleExtension.TABLE_MAPPING_TABLE_STYLE,
									table, newTable, contentsId, newContentsId);

						}

						if (featureStyleExtension
								.hasTableIconRelationship(table)) {

							copyFeatureTableStyle(featureStyleExtension,
									FeatureCoreStyleExtension.TABLE_MAPPING_TABLE_ICON,
									table, newTable, contentsId, newContentsId);

						}

					}

				}

			}

		} catch (Exception e) {
			logger.log(Level.WARNING,
					"Failed to create Feature Style for table: " + newTable
							+ ", copied from table: " + table,
					e);
		}

	}

	/**
	 * Copy the feature table style
	 * 
	 * @param featureStyleExtension
	 *            feature style extension
	 * @param mappingTablePrefix
	 *            mapping table prefix
	 * @param table
	 *            table name
	 * @param newTable
	 *            new table name
	 * @param contentsId
	 *            contents id
	 * @param newContentsId
	 *            new contents id
	 */
	private void copyFeatureTableStyle(
			FeatureCoreStyleExtension featureStyleExtension,
			String mappingTablePrefix, String table, String newTable,
			long contentsId, long newContentsId) throws SQLException {

		GeoPackageCore geoPackage = featureStyleExtension.getGeoPackage();

		String mappingTableName = featureStyleExtension
				.getMappingTableName(mappingTablePrefix, table);

		ExtensionsDao extensionsDao = featureStyleExtension.getExtensionsDao();
		List<Extensions> extensions = extensionsDao.queryByExtension(
				RelatedTablesCoreExtension.EXTENSION_NAME, mappingTableName);

		if (!extensions.isEmpty()) {

			String newMappingTableName = featureStyleExtension
					.getMappingTableName(mappingTablePrefix, newTable);

			UserCustomTable userTable = UserCustomTableReader
					.readTable(geoPackage.getDatabase(), mappingTableName);
			AlterTable.copyTable(geoPackage.getDatabase(), userTable,
					newMappingTableName, false);

			TableMapping mappingTableTableMapping = new TableMapping(userTable,
					newMappingTableName);
			MappedColumn baseIdColumn = mappingTableTableMapping
					.getColumn(UserMappingTable.COLUMN_BASE_ID);
			baseIdColumn.setConstantValue(newContentsId);
			baseIdColumn.setWhereValue(contentsId);
			CoreSQLUtils.transferTableContent(geoPackage.getDatabase(),
					mappingTableTableMapping);

			Extensions extension = extensions.get(0);
			extension.setTableName(newMappingTableName);
			extensionsDao.create(extension);

			TableMapping extendedRelationTableMapping = new TableMapping(
					geoPackage.getDatabase(), ExtendedRelation.TABLE_NAME);
			extendedRelationTableMapping
					.removeColumn(ExtendedRelation.COLUMN_ID);
			MappedColumn baseTableNameColumn = extendedRelationTableMapping
					.getColumn(ExtendedRelation.COLUMN_BASE_TABLE_NAME);
			baseTableNameColumn.setWhereValue(ContentsId.TABLE_NAME);
			MappedColumn mappingTableNameColumn = extendedRelationTableMapping
					.getColumn(ExtendedRelation.COLUMN_MAPPING_TABLE_NAME);
			mappingTableNameColumn.setConstantValue(newMappingTableName);
			mappingTableNameColumn.setWhereValue(mappingTableName);
			CoreSQLUtils.transferTableContent(geoPackage.getDatabase(),
					extendedRelationTableMapping);

		}

	}

	/**
	 * Get a Feature Style Extension used only for deletions
	 * 
	 * @return Feature Style Extension
	 */
	private FeatureCoreStyleExtension getFeatureStyleExtension() {

		RelatedTablesCoreExtension relatedTables = new RelatedTablesCoreExtension(
				geoPackage) {
		};

		return new FeatureCoreStyleExtension(geoPackage, relatedTables) {
		};
	}

	/**
	 * Delete the Contents Id extensions for the table
	 * 
	 * @param table
	 *            table name
	 */
	public void deleteContentsId(String table) {

		ContentsIdExtension contentsIdExtension = new ContentsIdExtension(
				geoPackage);
		if (contentsIdExtension.has()) {
			contentsIdExtension.delete(table);
		}

	}

	/**
	 * Delete the Contents Id extension including the extension entries and
	 * custom tables
	 */
	public void deleteContentsIdExtension() {

		ContentsIdExtension contentsIdExtension = new ContentsIdExtension(
				geoPackage);
		if (contentsIdExtension.has()) {
			contentsIdExtension.removeExtension();
		}

	}

	/**
	 * Copy the Contents Id extensions for the table
	 * 
	 * @param table
	 *            table name
	 * @param newTable
	 *            new table name
	 */
	public void copyContentsId(String table, String newTable) {

		try {

			ContentsIdExtension contentsIdExtension = new ContentsIdExtension(
					geoPackage);
			if (contentsIdExtension.has()) {
				if (contentsIdExtension.get(table) != null) {
					contentsIdExtension.create(newTable);
				}
			}

		} catch (Exception e) {
			logger.log(Level.WARNING, "Failed to create Contents Id for table: "
					+ newTable + ", copied from table: " + table, e);
		}

	}

}
