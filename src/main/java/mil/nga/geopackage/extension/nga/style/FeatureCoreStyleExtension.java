package mil.nga.geopackage.extension.nga.style;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import mil.nga.geopackage.GeoPackageCore;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.extension.BaseExtension;
import mil.nga.geopackage.extension.ExtensionScopeType;
import mil.nga.geopackage.extension.Extensions;
import mil.nga.geopackage.extension.nga.NGAExtensions;
import mil.nga.geopackage.extension.nga.contents.ContentsId;
import mil.nga.geopackage.extension.nga.contents.ContentsIdExtension;
import mil.nga.geopackage.extension.related.RelatedTablesCoreExtension;
import mil.nga.geopackage.property.GeoPackageProperties;
import mil.nga.geopackage.property.PropertyConstants;

/**
 * Feature Core Style extension
 * 
 * http://ngageoint.github.io/GeoPackage/docs/extensions/feature-style.html
 * 
 * @author osbornb
 * @since 3.2.0
 */
public abstract class FeatureCoreStyleExtension extends BaseExtension {

	/**
	 * Extension author
	 */
	public static final String EXTENSION_AUTHOR = NGAExtensions.EXTENSION_AUTHOR;

	/**
	 * Extension name without the author
	 */
	public static final String EXTENSION_NAME_NO_AUTHOR = "feature_style";

	/**
	 * Extension, with author and name
	 */
	public static final String EXTENSION_NAME = Extensions
			.buildExtensionName(EXTENSION_AUTHOR, EXTENSION_NAME_NO_AUTHOR);

	/**
	 * Extension definition URL
	 */
	public static final String EXTENSION_DEFINITION = GeoPackageProperties
			.getProperty(PropertyConstants.EXTENSIONS,
					EXTENSION_NAME_NO_AUTHOR);

	/**
	 * Table name prefix for mapping styles
	 */
	public static final String TABLE_MAPPING_STYLE = EXTENSION_AUTHOR
			+ "_style_";

	/**
	 * Table name prefix for mapping style defaults
	 */
	public static final String TABLE_MAPPING_TABLE_STYLE = EXTENSION_AUTHOR
			+ "_style_default_";

	/**
	 * Table name prefix for mapping icons
	 */
	public static final String TABLE_MAPPING_ICON = EXTENSION_AUTHOR + "_icon_";

	/**
	 * Table name prefix for mapping icon defaults
	 */
	public static final String TABLE_MAPPING_TABLE_ICON = EXTENSION_AUTHOR
			+ "_icon_default_";

	/**
	 * Related Tables extension
	 */
	protected final RelatedTablesCoreExtension relatedTables;

	/**
	 * Contents Id extension
	 */
	protected final ContentsIdExtension contentsId;

	/**
	 * Constructor
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * @param relatedTables
	 *            related tables
	 */
	protected FeatureCoreStyleExtension(GeoPackageCore geoPackage,
			RelatedTablesCoreExtension relatedTables) {
		super(geoPackage);
		this.relatedTables = relatedTables;
		contentsId = new ContentsIdExtension(geoPackage);
	}

	/**
	 * Get or create the extension
	 * 
	 * @param featureTable
	 *            feature table
	 * @return extension
	 */
	private Extensions getOrCreate(String featureTable) {

		Extensions extension = getOrCreate(EXTENSION_NAME, featureTable, null,
				EXTENSION_DEFINITION, ExtensionScopeType.READ_WRITE);

		return extension;
	}

	/**
	 * Get the feature tables registered with the extension
	 * 
	 * @return list of feature table names
	 */
	public List<String> getTables() {
		List<String> tables = new ArrayList<>();
		List<Extensions> extensions = getExtensions(EXTENSION_NAME);
		if (extensions != null) {
			for (Extensions extension : extensions) {
				tables.add(extension.getTableName());
			}
		}
		return tables;
	}

	/**
	 * Determine if the GeoPackage has the extension
	 * 
	 * @return true if has extension
	 */
	public boolean has() {
		return super.has(EXTENSION_NAME);
	}

	/**
	 * Get the extension for the feature table
	 * 
	 * @param featureTable
	 *            feature table
	 * @return extension
	 * @since 4.0.0
	 */
	public Extensions get(String featureTable) {
		return get(EXTENSION_NAME, featureTable, null);
	}

	/**
	 * Determine if the GeoPackage has the extension for the feature table
	 * 
	 * @param featureTable
	 *            feature table
	 * @return true if has extension
	 */
	public boolean has(String featureTable) {
		return has(EXTENSION_NAME, featureTable, null);
	}

	/**
	 * Get the related tables extension
	 * 
	 * @return related tables extension
	 */
	public RelatedTablesCoreExtension getRelatedTables() {
		return relatedTables;
	}

	/**
	 * Get the contents id extension
	 * 
	 * @return contents id extension
	 */
	public ContentsIdExtension getContentsId() {
		return contentsId;
	}

	/**
	 * Create style, icon, table style, and table icon relationships for the
	 * feature table
	 * 
	 * @param featureTable
	 *            feature table
	 */
	public void createRelationships(String featureTable) {
		createStyleRelationship(featureTable);
		createTableStyleRelationship(featureTable);
		createIconRelationship(featureTable);
		createTableIconRelationship(featureTable);
	}

	/**
	 * Check if feature table has a style, icon, table style, or table icon
	 * relationships
	 * 
	 * @param featureTable
	 *            feature table
	 * @return true if has a relationship
	 */
	public boolean hasRelationship(String featureTable) {
		return hasStyleRelationship(featureTable)
				|| hasTableStyleRelationship(featureTable)
				|| hasIconRelationship(featureTable)
				|| hasTableIconRelationship(featureTable);
	}

	/**
	 * Create a style relationship for the feature table
	 * 
	 * @param featureTable
	 *            feature table
	 */
	public void createStyleRelationship(String featureTable) {
		createStyleRelationship(
				getMappingTableName(TABLE_MAPPING_STYLE, featureTable),
				featureTable, featureTable, StyleTable.TABLE_NAME);
	}

	/**
	 * Determine if a style relationship exists for the feature table
	 * 
	 * @param featureTable
	 *            feature table
	 * @return true if relationship exists
	 */
	public boolean hasStyleRelationship(String featureTable) {
		return hasStyleRelationship(
				getMappingTableName(TABLE_MAPPING_STYLE, featureTable),
				featureTable, StyleTable.TABLE_NAME);
	}

	/**
	 * Create a feature table style relationship
	 * 
	 * @param featureTable
	 *            feature table
	 */
	public void createTableStyleRelationship(String featureTable) {
		createStyleRelationship(
				getMappingTableName(TABLE_MAPPING_TABLE_STYLE, featureTable),
				featureTable, ContentsId.TABLE_NAME, StyleTable.TABLE_NAME);
	}

	/**
	 * Determine if a feature table style relationship exists
	 * 
	 * @param featureTable
	 *            feature table
	 * @return true if relationship exists
	 */
	public boolean hasTableStyleRelationship(String featureTable) {
		return hasStyleRelationship(
				getMappingTableName(TABLE_MAPPING_TABLE_STYLE, featureTable),
				ContentsId.TABLE_NAME, StyleTable.TABLE_NAME);
	}

	/**
	 * Create an icon relationship for the feature table
	 * 
	 * @param featureTable
	 *            feature table
	 */
	public void createIconRelationship(String featureTable) {
		createStyleRelationship(
				getMappingTableName(TABLE_MAPPING_ICON, featureTable),
				featureTable, featureTable, IconTable.TABLE_NAME);
	}

	/**
	 * Determine if an icon relationship exists for the feature table
	 * 
	 * @param featureTable
	 *            feature table
	 * @return true if relationship exists
	 */
	public boolean hasIconRelationship(String featureTable) {
		return hasStyleRelationship(
				getMappingTableName(TABLE_MAPPING_ICON, featureTable),
				featureTable, IconTable.TABLE_NAME);
	}

	/**
	 * Create a feature table icon relationship
	 * 
	 * @param featureTable
	 *            feature table
	 */
	public void createTableIconRelationship(String featureTable) {
		createStyleRelationship(
				getMappingTableName(TABLE_MAPPING_TABLE_ICON, featureTable),
				featureTable, ContentsId.TABLE_NAME, IconTable.TABLE_NAME);
	}

	/**
	 * Determine if a feature table icon relationship exists
	 * 
	 * @param featureTable
	 *            feature table
	 * @return true if relationship exists
	 */
	public boolean hasTableIconRelationship(String featureTable) {
		return hasStyleRelationship(
				getMappingTableName(TABLE_MAPPING_TABLE_ICON, featureTable),
				ContentsId.TABLE_NAME, IconTable.TABLE_NAME);
	}

	/**
	 * Get the mapping table name
	 * 
	 * @param tablePrefix
	 *            table name prefix
	 * @param featureTable
	 *            feature table name
	 * @return mapping table name
	 */
	public String getMappingTableName(String tablePrefix, String featureTable) {
		return tablePrefix + featureTable;
	}

	/**
	 * Check if the style extension relationship between a feature table and
	 * style extension table exists
	 * 
	 * @param mappingTableName
	 *            mapping table name
	 * @param featureTable
	 *            feature table name
	 * @param baseTable
	 *            base table name
	 * @param relatedTable
	 *            related table name
	 * @return true if relationship exists
	 */
	private boolean hasStyleRelationship(String mappingTableName,
			String baseTable, String relatedTable) {

		boolean has = false;

		try {
			has = relatedTables.hasRelations(baseTable, relatedTable,
					mappingTableName);
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to check if Feature Style Relationship exists. Base Table: "
							+ baseTable + ", Related Table: " + relatedTable
							+ ", Mapping Table: " + mappingTableName,
					e);
		}

		return has;
	}

	/**
	 * Create a style extension relationship between a feature table and style
	 * extension table
	 * 
	 * @param mappingTableName
	 *            mapping table name
	 * @param featureTable
	 *            feature table name
	 * @param baseTable
	 *            base table name
	 * @param relatedTable
	 *            related table name
	 */
	private void createStyleRelationship(String mappingTableName,
			String featureTable, String baseTable, String relatedTable) {

		if (!hasStyleRelationship(mappingTableName, baseTable, relatedTable)) {

			// Create the extension
			getOrCreate(featureTable);

			if (baseTable.equals(ContentsId.TABLE_NAME)) {
				if (!contentsId.has()) {
					contentsId.getOrCreateExtension();
				}
			}

			StyleMappingTable mappingTable = new StyleMappingTable(
					mappingTableName);

			if (relatedTable.equals(StyleTable.TABLE_NAME)) {
				relatedTables.addAttributesRelationship(baseTable,
						new StyleTable(), mappingTable);
			} else {
				relatedTables.addMediaRelationship(baseTable, new IconTable(),
						mappingTable);
			}
		}

	}

	/**
	 * Delete the style and icon table and row relationships for all feature
	 * tables
	 */
	public void deleteRelationships() {
		List<String> tables = getTables();
		for (String table : tables) {
			deleteRelationships(table);
		}
	}

	/**
	 * Delete the style and icon table and row relationships for the feature
	 * table
	 * 
	 * @param featureTable
	 *            feature table
	 */
	public void deleteRelationships(String featureTable) {
		deleteStyleRelationship(featureTable);
		deleteTableStyleRelationship(featureTable);
		deleteIconRelationship(featureTable);
		deleteTableIconRelationship(featureTable);
	}

	/**
	 * Delete a style relationship for the feature table
	 * 
	 * @param featureTable
	 *            feature table
	 */
	public void deleteStyleRelationship(String featureTable) {
		deleteStyleRelationship(
				getMappingTableName(TABLE_MAPPING_STYLE, featureTable),
				featureTable);
	}

	/**
	 * Delete a table style relationship for the feature table
	 * 
	 * @param featureTable
	 *            feature table
	 */
	public void deleteTableStyleRelationship(String featureTable) {
		deleteStyleRelationship(
				getMappingTableName(TABLE_MAPPING_TABLE_STYLE, featureTable),
				featureTable);
	}

	/**
	 * Delete a icon relationship for the feature table
	 * 
	 * @param featureTable
	 *            feature table
	 */
	public void deleteIconRelationship(String featureTable) {
		deleteStyleRelationship(
				getMappingTableName(TABLE_MAPPING_ICON, featureTable),
				featureTable);
	}

	/**
	 * Delete a table icon relationship for the feature table
	 * 
	 * @param featureTable
	 *            feature table
	 */
	public void deleteTableIconRelationship(String featureTable) {
		deleteStyleRelationship(
				getMappingTableName(TABLE_MAPPING_TABLE_ICON, featureTable),
				featureTable);
	}

	/**
	 * Delete a style extension feature table relationship and the mapping table
	 * 
	 * @param mappingTableName
	 *            mapping table name
	 * @param featureTable
	 *            feature table name
	 */
	private void deleteStyleRelationship(String mappingTableName,
			String featureTable) {

		relatedTables.removeRelationshipsWithMappingTable(mappingTableName);

		if (!hasRelationship(featureTable)) {
			try {
				if (extensionsDao.isTableExists()) {
					extensionsDao.deleteByExtension(EXTENSION_NAME,
							featureTable);
				}
			} catch (SQLException e) {
				throw new GeoPackageException(
						"Failed to delete Feature Style extension. GeoPackage: "
								+ geoPackage.getName() + ", Feature Table: "
								+ featureTable,
						e);
			}
		}

	}

	/**
	 * Completely remove and delete the extension and all styles and icons
	 */
	public void removeExtension() {

		deleteRelationships();

		geoPackage.deleteTable(StyleTable.TABLE_NAME);

		geoPackage.deleteTable(IconTable.TABLE_NAME);

		try {
			if (extensionsDao.isTableExists()) {
				extensionsDao.deleteByExtension(EXTENSION_NAME);
			}
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to delete Feature Style extension. GeoPackage: "
							+ geoPackage.getName(),
					e);
		}

	}

}
