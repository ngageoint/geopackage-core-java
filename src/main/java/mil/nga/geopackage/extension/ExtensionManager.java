package mil.nga.geopackage.extension;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import mil.nga.geopackage.GeoPackageConstants;
import mil.nga.geopackage.GeoPackageCore;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.db.AlterTable;
import mil.nga.geopackage.db.CoreSQLUtils;
import mil.nga.geopackage.db.GeoPackageTableCreator;
import mil.nga.geopackage.db.MappedColumn;
import mil.nga.geopackage.db.TableMapping;
import mil.nga.geopackage.db.table.ConstraintParser;
import mil.nga.geopackage.db.table.TableConstraints;
import mil.nga.geopackage.db.table.TableInfo;
import mil.nga.geopackage.extension.coverage.CoverageDataCore;
import mil.nga.geopackage.extension.coverage.GriddedCoverage;
import mil.nga.geopackage.extension.coverage.GriddedCoverageDao;
import mil.nga.geopackage.extension.coverage.GriddedTile;
import mil.nga.geopackage.extension.coverage.GriddedTileDao;
import mil.nga.geopackage.extension.ecere.EcereExtensions;
import mil.nga.geopackage.extension.im.ImageMattersExtensions;
import mil.nga.geopackage.extension.metadata.MetadataExtension;
import mil.nga.geopackage.extension.metadata.reference.MetadataReference;
import mil.nga.geopackage.extension.metadata.reference.MetadataReferenceDao;
import mil.nga.geopackage.extension.nga.NGAExtensions;
import mil.nga.geopackage.extension.related.ExtendedRelation;
import mil.nga.geopackage.extension.related.ExtendedRelationsDao;
import mil.nga.geopackage.extension.related.RelatedTablesCoreExtension;
import mil.nga.geopackage.extension.rtree.RTreeIndexCoreExtension;
import mil.nga.geopackage.extension.schema.SchemaExtension;
import mil.nga.geopackage.extension.schema.columns.DataColumns;
import mil.nga.geopackage.extension.schema.columns.DataColumnsDao;
import mil.nga.geopackage.features.columns.GeometryColumns;
import mil.nga.geopackage.features.columns.GeometryColumnsDao;
import mil.nga.geopackage.user.custom.UserCustomColumn;
import mil.nga.geopackage.user.custom.UserCustomTable;
import mil.nga.geopackage.user.custom.UserCustomTableReader;

/**
 * GeoPackage Extension Manager for deleting and copying extensions
 * 
 * @author osbornb
 * @since 4.0.0
 */
public class ExtensionManager extends ExtensionManagement {

	/**
	 * Logger
	 */
	private static final Logger logger = Logger
			.getLogger(ExtensionManager.class.getName());

	/**
	 * Community Extensions
	 */
	private final List<ExtensionManagement> communityExtensions = new ArrayList<>();

	/**
	 * Constructor
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 */
	public ExtensionManager(GeoPackageCore geoPackage) {
		super(geoPackage);
		communityExtensions.add(new NGAExtensions(geoPackage));
		communityExtensions.add(new ImageMattersExtensions(geoPackage));
		communityExtensions.add(new EcereExtensions(geoPackage));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getAuthor() {
		return GeoPackageConstants.EXTENSION_AUTHOR;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteTableExtensions(String table) {

		// Handle deleting any extensions with extra tables here
		for (ExtensionManagement extensions : communityExtensions) {
			extensions.deleteTableExtensions(table);
		}

		deleteRTreeSpatialIndex(table);
		deleteRelatedTables(table);
		deleteGriddedCoverage(table);
		deleteSchema(table);
		deleteMetadata(table);

		delete(table);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteExtensions() {

		// Handle deleting any extensions with extra tables here
		for (ExtensionManagement extensions : communityExtensions) {
			extensions.deleteExtensions();
		}

		deleteRTreeSpatialIndexExtension();
		deleteRelatedTablesExtension();
		deleteGriddedCoverageExtension();
		deleteSchemaExtension();
		deleteMetadataExtension();
		deleteCrsWktExtension();

		delete();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void copyTableExtensions(String table, String newTable) {

		try {

			copyRTreeSpatialIndex(table, newTable);
			copyRelatedTables(table, newTable);
			copyGriddedCoverage(table, newTable);
			copySchema(table, newTable);
			copyMetadata(table, newTable);

			// Handle copying any extensions with extra tables here
			for (ExtensionManagement extensions : communityExtensions) {
				try {
					extensions.copyTableExtensions(table, newTable);
				} catch (Exception e) {
					logger.log(Level.WARNING,
							"Failed to copy '" + extensions.getAuthor()
									+ "' extensions for table: " + newTable
									+ ", copied from table: " + table,
							e);
				}
			}

		} catch (Exception e) {
			logger.log(Level.WARNING, "Failed to copy extensions for table: "
					+ newTable + ", copied from table: " + table, e);
		}

	}

	/**
	 * Delete the extensions for the table
	 * 
	 * @param table
	 *            table name
	 */
	private void delete(String table) {

		ExtensionsDao extensionsDao = geoPackage.getExtensionsDao();

		try {
			if (extensionsDao.isTableExists()) {
				extensionsDao.deleteByTableName(table);
			}
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to delete Table extensions. GeoPackage: "
							+ geoPackage.getName() + ", Table: " + table,
					e);
		}
	}

	/**
	 * Delete the extensions
	 */
	private void delete() {

		ExtensionsDao extensionsDao = geoPackage.getExtensionsDao();

		try {
			if (extensionsDao.isTableExists()) {
				geoPackage.dropTable(extensionsDao.getTableName());
			}
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to delete all extensions. GeoPackage: "
							+ geoPackage.getName(),
					e);
		}

	}

	/**
	 * Delete the RTree Spatial extension for the table
	 * 
	 * @param table
	 *            table name
	 */
	public void deleteRTreeSpatialIndex(String table) {

		RTreeIndexCoreExtension rTreeIndexExtension = getRTreeIndexExtension();
		if (rTreeIndexExtension.has(table)) {
			rTreeIndexExtension.delete(table);
		}

	}

	/**
	 * Delete the RTree Spatial extension
	 */
	public void deleteRTreeSpatialIndexExtension() {

		RTreeIndexCoreExtension rTreeIndexExtension = getRTreeIndexExtension();
		if (rTreeIndexExtension.has()) {
			rTreeIndexExtension.deleteAll();
		}

	}

	/**
	 * Copy the RTree Spatial extension for the table
	 * 
	 * @param table
	 *            table name
	 * @param newTable
	 *            new table name
	 */
	public void copyRTreeSpatialIndex(String table, String newTable) {

		try {

			RTreeIndexCoreExtension rTreeIndexExtension = getRTreeIndexExtension();
			if (rTreeIndexExtension.has(table)) {
				GeometryColumnsDao geometryColumnsDao = geoPackage
						.getGeometryColumnsDao();

				GeometryColumns geometryColumns = geometryColumnsDao
						.queryForTableName(newTable);
				if (geometryColumns != null) {
					TableInfo tableInfo = TableInfo
							.info(geoPackage.getDatabase(), newTable);
					if (tableInfo != null) {
						String pk = tableInfo.getPrimaryKey().getName();
						rTreeIndexExtension.create(newTable,
								geometryColumns.getColumnName(), pk);
					}
				}
			}

		} catch (Exception e) {
			logger.log(Level.WARNING, "Failed to create RTree for table: "
					+ newTable + ", copied from table: " + table, e);
		}
	}

	/**
	 * Get a RTree Index Extension used only for deletions
	 * 
	 * @return RTree index extension
	 */
	private RTreeIndexCoreExtension getRTreeIndexExtension() {
		return new RTreeIndexCoreExtension(geoPackage) {
			@Override
			public void createMinYFunction() {
			}

			@Override
			public void createMinXFunction() {
			}

			@Override
			public void createMaxYFunction() {
			}

			@Override
			public void createMaxXFunction() {
			}

			@Override
			public void createIsEmptyFunction() {
			}
		};
	}

	/**
	 * Delete the Related Tables extensions for the table
	 * 
	 * @param table
	 *            table name
	 */
	public void deleteRelatedTables(String table) {

		RelatedTablesCoreExtension relatedTablesExtension = getRelatedTableExtension();
		if (relatedTablesExtension.has()) {
			relatedTablesExtension.removeRelationships(table);
		}

	}

	/**
	 * Delete the Related Tables extension
	 */
	public void deleteRelatedTablesExtension() {

		RelatedTablesCoreExtension relatedTablesExtension = getRelatedTableExtension();
		if (relatedTablesExtension.has()) {
			relatedTablesExtension.removeExtension();
		}

	}

	/**
	 * Copy the Related Tables extensions for the table
	 * 
	 * @param table
	 *            table name
	 * @param newTable
	 *            new table name
	 */
	public void copyRelatedTables(String table, String newTable) {

		try {

			RelatedTablesCoreExtension relatedTablesExtension = getRelatedTableExtension();
			if (relatedTablesExtension.has()) {

				ExtendedRelationsDao extendedRelationsDao = relatedTablesExtension
						.getExtendedRelationsDao();
				ExtensionsDao extensionsDao = geoPackage.getExtensionsDao();

				List<ExtendedRelation> extendedRelations = extendedRelationsDao
						.getBaseTableRelations(table);
				for (ExtendedRelation extendedRelation : extendedRelations) {

					String mappingTableName = extendedRelation
							.getMappingTableName();

					List<Extensions> extensions = extensionsDao
							.queryByExtension(
									RelatedTablesCoreExtension.EXTENSION_NAME,
									mappingTableName);

					if (!extensions.isEmpty()) {

						String newMappingTableName = CoreSQLUtils.createName(
								geoPackage.getDatabase(), mappingTableName,
								table, newTable);

						UserCustomTable userTable = UserCustomTableReader
								.readTable(geoPackage.getDatabase(),
										mappingTableName);
						AlterTable.copyTable(geoPackage.getDatabase(),
								userTable, newMappingTableName);

						Extensions extension = extensions.get(0);
						extension.setTableName(newMappingTableName);
						extensionsDao.create(extension);

						TableMapping extendedRelationTableMapping = new TableMapping(
								geoPackage.getDatabase(),
								ExtendedRelation.TABLE_NAME);
						extendedRelationTableMapping
								.removeColumn(ExtendedRelation.COLUMN_ID);
						MappedColumn baseTableNameColumn = extendedRelationTableMapping
								.getColumn(
										ExtendedRelation.COLUMN_BASE_TABLE_NAME);
						baseTableNameColumn.setConstantValue(newTable);
						baseTableNameColumn.setWhereValue(table);
						MappedColumn mappingTableNameColumn = extendedRelationTableMapping
								.getColumn(
										ExtendedRelation.COLUMN_MAPPING_TABLE_NAME);
						mappingTableNameColumn
								.setConstantValue(newMappingTableName);
						mappingTableNameColumn.setWhereValue(mappingTableName);
						CoreSQLUtils.transferTableContent(
								geoPackage.getDatabase(),
								extendedRelationTableMapping);

					}
				}
			}

		} catch (Exception e) {
			logger.log(Level.WARNING,
					"Failed to create Related Tables for table: " + newTable
							+ ", copied from table: " + table,
					e);
		}

	}

	/**
	 * Get a Related Table Extension used only for deletions
	 * 
	 * @return Related Table Extension
	 */
	private RelatedTablesCoreExtension getRelatedTableExtension() {
		return new RelatedTablesCoreExtension(geoPackage) {
		};
	}

	/**
	 * Delete the Gridded Coverage extensions for the table
	 * 
	 * @param table
	 *            table name
	 */
	public void deleteGriddedCoverage(String table) {

		if (geoPackage.isTableType(table, CoverageDataCore.GRIDDED_COVERAGE)) {

			GriddedTileDao griddedTileDao = CoverageDataCore
					.getGriddedTileDao(geoPackage);
			GriddedCoverageDao griddedCoverageDao = CoverageDataCore
					.getGriddedCoverageDao(geoPackage);
			ExtensionsDao extensionsDao = geoPackage.getExtensionsDao();

			try {
				if (griddedTileDao.isTableExists()) {
					griddedTileDao.delete(table);
				}
				if (griddedCoverageDao.isTableExists()) {
					griddedCoverageDao.delete(table);
				}
				if (extensionsDao.isTableExists()) {
					extensionsDao.deleteByExtension(
							CoverageDataCore.EXTENSION_NAME, table);
				}
			} catch (SQLException e) {
				throw new GeoPackageException(
						"Failed to delete Table Index. GeoPackage: "
								+ geoPackage.getName() + ", Table: " + table,
						e);
			}
		}

	}

	/**
	 * Delete the Gridded Coverage extension
	 */
	public void deleteGriddedCoverageExtension() {

		List<String> coverageTables = geoPackage
				.getTables(CoverageDataCore.GRIDDED_COVERAGE);
		for (String table : coverageTables) {
			geoPackage.deleteTable(table);
		}

		GriddedTileDao griddedTileDao = CoverageDataCore
				.getGriddedTileDao(geoPackage);
		GriddedCoverageDao griddedCoverageDao = CoverageDataCore
				.getGriddedCoverageDao(geoPackage);
		ExtensionsDao extensionsDao = geoPackage.getExtensionsDao();

		try {
			if (griddedTileDao.isTableExists()) {
				geoPackage.dropTable(griddedTileDao.getTableName());
			}
			if (griddedCoverageDao.isTableExists()) {
				geoPackage.dropTable(griddedCoverageDao.getTableName());
			}
			if (extensionsDao.isTableExists()) {
				extensionsDao
						.deleteByExtension(CoverageDataCore.EXTENSION_NAME);
			}
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to delete Gridded Coverage extension and tables. GeoPackage: "
							+ geoPackage.getName(),
					e);
		}

	}

	/**
	 * Copy the Gridded Coverage extensions for the table
	 * 
	 * @param table
	 *            table name
	 * @param newTable
	 *            new table name
	 */
	public void copyGriddedCoverage(String table, String newTable) {

		try {

			if (geoPackage.isTableType(table,
					CoverageDataCore.GRIDDED_COVERAGE)) {

				ExtensionsDao extensionsDao = geoPackage.getExtensionsDao();

				if (extensionsDao.isTableExists()) {

					List<Extensions> extensions = extensionsDao
							.queryByExtension(CoverageDataCore.EXTENSION_NAME,
									table);

					if (!extensions.isEmpty()) {

						Extensions extension = extensions.get(0);
						extension.setTableName(newTable);
						extensionsDao.create(extension);

						GriddedCoverageDao griddedCoverageDao = CoverageDataCore
								.getGriddedCoverageDao(geoPackage);
						if (griddedCoverageDao.isTableExists()) {

							CoreSQLUtils.transferTableContent(
									geoPackage.getDatabase(),
									GriddedCoverage.TABLE_NAME,
									GriddedCoverage.COLUMN_TILE_MATRIX_SET_NAME,
									newTable, table, GriddedCoverage.COLUMN_ID);

						}

						GriddedTileDao griddedTileDao = CoverageDataCore
								.getGriddedTileDao(geoPackage);
						if (griddedTileDao.isTableExists()) {

							CoreSQLUtils.transferTableContent(
									geoPackage.getDatabase(),
									GriddedTile.TABLE_NAME,
									GriddedTile.COLUMN_TABLE_NAME, newTable,
									table, GriddedTile.COLUMN_ID);

						}
					}
				}
			}

		} catch (Exception e) {
			logger.log(Level.WARNING,
					"Failed to create Gridded Coverage for table: " + newTable
							+ ", copied from table: " + table,
					e);
		}

	}

	/**
	 * Delete the Schema extensions for the table
	 * 
	 * @param table
	 *            table name
	 */
	public void deleteSchema(String table) {

		DataColumnsDao dataColumnsDao = SchemaExtension
				.getDataColumnsDao(geoPackage);
		try {
			if (dataColumnsDao.isTableExists()) {
				dataColumnsDao.deleteByTableName(table);
			}
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to delete Schema extension. GeoPackage: "
							+ geoPackage.getName() + ", Table: " + table,
					e);
		}

	}

	/**
	 * Delete the Schema extension
	 */
	public void deleteSchemaExtension() {

		SchemaExtension schemaExtension = new SchemaExtension(geoPackage);
		if (schemaExtension.has()) {
			schemaExtension.removeExtension();
		}

	}

	/**
	 * Copy the Schema extensions for the table
	 * 
	 * @param table
	 *            table name
	 * @param newTable
	 *            new table name
	 */
	public void copySchema(String table, String newTable) {

		try {

			if (geoPackage.isTableOrView(DataColumns.TABLE_NAME)) {

				UserCustomTable dataColumnsTable = UserCustomTableReader
						.readTable(geoPackage.getDatabase(),
								DataColumns.TABLE_NAME);
				UserCustomColumn nameColumn = dataColumnsTable
						.getColumn(DataColumns.COLUMN_NAME);
				if (nameColumn.hasConstraints()) {
					nameColumn.clearConstraints();
					if (dataColumnsTable.hasConstraints()) {
						dataColumnsTable.clearConstraints();
						String constraintSql = GeoPackageTableCreator
								.readScript(GeoPackageTableCreator.SCHEMA_PATH,
										GeoPackageTableCreator.DATA_COLUMNS)
								.get(0);
						TableConstraints constraints = ConstraintParser
								.getConstraints(constraintSql);
						dataColumnsTable.addConstraints(
								constraints.getTableConstraints());
					}
					AlterTable.alterColumn(geoPackage.getDatabase(),
							dataColumnsTable, nameColumn);
				}

				CoreSQLUtils.transferTableContent(geoPackage.getDatabase(),
						DataColumns.TABLE_NAME, DataColumns.COLUMN_TABLE_NAME,
						newTable, table);

			}

		} catch (Exception e) {
			logger.log(Level.WARNING, "Failed to create Schema for table: "
					+ newTable + ", copied from table: " + table, e);
		}

	}

	/**
	 * Delete the Metadata extensions for the table
	 * 
	 * @param table
	 *            table name
	 */
	public void deleteMetadata(String table) {

		MetadataReferenceDao metadataReferenceDao = MetadataExtension
				.getMetadataReferenceDao(geoPackage);
		try {
			if (metadataReferenceDao.isTableExists()) {
				metadataReferenceDao.deleteByTableName(table);
			}
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to delete Metadata extension. GeoPackage: "
							+ geoPackage.getName() + ", Table: " + table,
					e);
		}

	}

	/**
	 * Delete the Metadata extension
	 */
	public void deleteMetadataExtension() {

		MetadataExtension metadataExtension = new MetadataExtension(geoPackage);
		if (metadataExtension.has()) {
			metadataExtension.removeExtension();
		}

	}

	/**
	 * Copy the Metadata extensions for the table
	 * 
	 * @param table
	 *            table name
	 * @param newTable
	 *            new table name
	 */
	public void copyMetadata(String table, String newTable) {

		try {

			if (geoPackage.isTableOrView(MetadataReference.TABLE_NAME)) {

				CoreSQLUtils.transferTableContent(geoPackage.getDatabase(),
						MetadataReference.TABLE_NAME,
						MetadataReference.COLUMN_TABLE_NAME, newTable, table);

			}

		} catch (Exception e) {
			logger.log(Level.WARNING, "Failed to create Metadata for table: "
					+ newTable + ", copied from table: " + table, e);
		}

	}

	/**
	 * Delete the WKT for Coordinate Reference Systems extension
	 */
	public void deleteCrsWktExtension() {

		CrsWktExtension crsWktExtension = new CrsWktExtension(geoPackage);
		if (crsWktExtension.has()) {
			crsWktExtension.removeExtension();
		}

	}

}
