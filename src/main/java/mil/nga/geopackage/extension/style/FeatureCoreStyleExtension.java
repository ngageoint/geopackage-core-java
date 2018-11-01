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
	 * Table name prefix for mapping styles
	 */
	public final String TABLE_MAPPING_STYLE = EXTENSION_AUTHOR + "_style_";

	/**
	 * Table name prefix for mapping style defaults
	 */
	public final String TABLE_MAPPING_TABLE_STYLE = EXTENSION_AUTHOR
			+ "_style_default_";

	/**
	 * Table name prefix for mapping icons
	 */
	public final String TABLE_MAPPING_ICON = EXTENSION_AUTHOR + "_icon_";

	/**
	 * Table name prefix for mapping icon defaults
	 */
	public final String TABLE_MAPPING_TABLE_ICON = EXTENSION_AUTHOR
			+ "_icon_default_";

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
		createStyleRelationship(TABLE_MAPPING_STYLE, featureTable, false, true);
	}

	/**
	 * Create a feature table style relationship
	 * 
	 * @param featureTable
	 *            feature table
	 */
	public void createTableStyleRelationship(String featureTable) {
		createStyleRelationship(TABLE_MAPPING_TABLE_STYLE, featureTable, true,
				true);
	}

	/**
	 * Create a icon relationship for the feature table
	 * 
	 * @param featureTable
	 *            feature table
	 */
	public void createIconRelationship(String featureTable) {
		createStyleRelationship(TABLE_MAPPING_ICON, featureTable, false, false);
	}

	/**
	 * Create a feature table icon relationship
	 * 
	 * @param featureTable
	 *            feature table
	 */
	public void createTableIconRelationship(String featureTable) {
		createStyleRelationship(TABLE_MAPPING_TABLE_ICON, featureTable, true,
				false);
	}

	/**
	 * Create a style extension relationship between a feature table and style
	 * extension table
	 * 
	 * @param tablePrefix
	 *            table name prefix
	 * @param featureTable
	 *            feature table name
	 * @param tableStyle
	 *            true if a table style or icon
	 * @param style
	 *            true if a style, false if an icon
	 */
	private void createStyleRelationship(String tablePrefix,
			String featureTable, boolean tableStyle, boolean style) {

		String baseTable = null;
		if (tableStyle) {
			baseTable = ContentsId.TABLE_NAME;
		} else {
			baseTable = featureTable;
		}

		String relatedTable = null;
		if (style) {
			relatedTable = StyleTable.TABLE_NAME;
		} else {
			relatedTable = IconTable.TABLE_NAME;
		}

		String mappingTableName = tablePrefix + featureTable;

		try {
			if (!relatedTables.hasRelations(baseTable, relatedTable,
					mappingTableName)) {

				if (tableStyle) {
					if (!contentsId.has()) {
						contentsId.getOrCreateExtension();
					}
				}

				StyleMappingTable mappingTable = new StyleMappingTable(
						mappingTableName);

				if (style) {
					relatedTables.addSimpleAttributesRelationship(baseTable,
							new StyleTable(), mappingTable);
				} else {
					relatedTables.addMediaRelationship(baseTable,
							new IconTable(), mappingTable);
				}
			}
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to create Feature Style Relationship. Base Table: "
							+ baseTable + ", Related Table: " + relatedTable
							+ ", Mapping Table: " + mappingTableName, e);
		}

	}

}
