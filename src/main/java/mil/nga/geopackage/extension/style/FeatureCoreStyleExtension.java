package mil.nga.geopackage.extension.style;

import java.util.ArrayList;
import java.util.List;

import mil.nga.geopackage.GeoPackageCore;
import mil.nga.geopackage.extension.BaseExtension;
import mil.nga.geopackage.extension.Extensions;
import mil.nga.geopackage.extension.contents.ContentsId;
import mil.nga.geopackage.extension.contents.ContentsIdExtension;
import mil.nga.geopackage.extension.related.RelatedTablesCoreExtension;
import mil.nga.geopackage.property.GeoPackageProperties;
import mil.nga.geopackage.property.PropertyConstants;
import mil.nga.sf.GeometryType;

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
	public final String TABLE_MAPPING_STYLE_DEFAULT = EXTENSION_AUTHOR
			+ "_style_default_";

	/**
	 * Table name prefix for mapping icons
	 */
	public final String TABLE_MAPPING_ICON = EXTENSION_AUTHOR + "_icon_";

	/**
	 * Table name prefix for mapping icon defaults
	 */
	public final String TABLE_MAPPING_ICON_DEFAULT = EXTENSION_AUTHOR
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

	public void createStyleRelationship(String featureTable) {
		createStyleRelationship(TABLE_MAPPING_STYLE, featureTable, false, true);
	}

	public void createStyleDefaultRelationship(String featureTable) {
		createStyleRelationship(TABLE_MAPPING_STYLE_DEFAULT, featureTable,
				true, true);
	}

	public void createIconRelationship(String featureTable) {
		createStyleRelationship(TABLE_MAPPING_ICON, featureTable, false, false);
	}

	public void createIconDefaultRelationship(String featureTable) {
		createStyleRelationship(TABLE_MAPPING_ICON_DEFAULT, featureTable, true,
				false);
	}

	private void createStyleRelationship(String tablePrefix,
			String featureTable, boolean featureDefault, boolean style) {

		String baseTableName = null;
		if (featureDefault) {
			if (!contentsId.has()) {
				contentsId.getOrCreateExtension();
			}
			baseTableName = ContentsId.TABLE_NAME;
		} else {
			baseTableName = featureTable;
		}

		StyleMappingTable mappingTable = new StyleMappingTable(tablePrefix
				+ featureTable);

		if (style) {
			relatedTables.addSimpleAttributesRelationship(baseTableName,
					new StyleTable(), mappingTable);
		} else {
			relatedTables.addMediaRelationship(baseTableName, new IconTable(),
					mappingTable);
		}

	}

	/**
	 * Geometry geometry type inheritance starting with the provided geometry
	 * type followed by parent types
	 * 
	 * @param geometryType
	 *            geometry type
	 * @return geometry types
	 */
	public static List<GeometryType> getGeometryTypeInheritance(
			GeometryType geometryType) {
		List<GeometryType> geometryTypes = new ArrayList<>();
		if (geometryType != null) {
			geometryTypes.add(geometryType);
			// TODO add parent types
			// TODO move to simple features
		}
		return geometryTypes;
	}

}
