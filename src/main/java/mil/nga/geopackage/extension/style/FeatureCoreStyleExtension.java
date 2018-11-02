package mil.nga.geopackage.extension.style;

import java.sql.SQLException;

import mil.nga.geopackage.GeoPackageCore;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.extension.BaseExtension;
import mil.nga.geopackage.extension.Extensions;
import mil.nga.geopackage.extension.contents.ContentsId;
import mil.nga.geopackage.extension.contents.ContentsIdExtension;
import mil.nga.geopackage.extension.related.RelatedTablesCoreExtension;
import mil.nga.geopackage.property.GeoPackageProperties;
import mil.nga.geopackage.property.PropertyConstants;

/**
 * Feature Core Style extension
 * 
 * @author osbornb
 * @since 3.1.1
 */
public abstract class FeatureCoreStyleExtension extends BaseExtension {

	/**
	 * Extension author
	 */
	public static final String EXTENSION_AUTHOR = "nga";

	/**
	 * Extension name without the author
	 */
	public static final String EXTENSION_NAME_NO_AUTHOR = "feature_style";

	/**
	 * Extension, with author and name
	 */
	public static final String EXTENSION_NAME = Extensions.buildExtensionName(
			EXTENSION_AUTHOR, EXTENSION_NAME_NO_AUTHOR);

	/**
	 * Extension definition URL
	 */
	public static final String EXTENSION_DEFINITION = GeoPackageProperties
			.getProperty(PropertyConstants.EXTENSIONS, EXTENSION_NAME_NO_AUTHOR);

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
							+ ", Mapping Table: " + mappingTableName, e);
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

}
