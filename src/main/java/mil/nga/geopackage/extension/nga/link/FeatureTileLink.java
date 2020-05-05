package mil.nga.geopackage.extension.nga.link;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Feature Tile Link object, for linking a feature and tile table together
 * 
 * @author osbornb
 * @since 1.1.5
 */
@DatabaseTable(tableName = "nga_feature_tile_link", daoClass = FeatureTileLinkDao.class)
public class FeatureTileLink {

	/**
	 * Table name
	 */
	public static final String TABLE_NAME = "nga_feature_tile_link";

	/**
	 * featureTableName field name
	 */
	public static final String COLUMN_FEATURE_TABLE_NAME = "feature_table_name";

	/**
	 * tileTableName field name
	 */
	public static final String COLUMN_TILE_TABLE_NAME = "tile_table_name";

	/**
	 * Name of the feature table
	 */
	@DatabaseField(columnName = COLUMN_FEATURE_TABLE_NAME, canBeNull = false, uniqueCombo = true)
	private String featureTableName;

	/**
	 * Name of the tile table
	 */
	@DatabaseField(columnName = COLUMN_TILE_TABLE_NAME, canBeNull = false, uniqueCombo = true)
	private String tileTableName;

	/**
	 * Default Constructor
	 */
	public FeatureTileLink() {

	}

	/**
	 * Copy Constructor
	 * 
	 * @param featureTileLink
	 *            feature tile link to copy
	 * @since 1.3.0
	 */
	public FeatureTileLink(FeatureTileLink featureTileLink) {
		featureTableName = featureTileLink.featureTableName;
		tileTableName = featureTileLink.tileTableName;
	}

	/**
	 * Get the id
	 * 
	 * @return feature tile link key
	 */
	public FeatureTileLinkKey getId() {
		return new FeatureTileLinkKey(featureTableName, tileTableName);
	}

	/**
	 * Set the id
	 * 
	 * @param id
	 *            id
	 */
	public void setId(FeatureTileLinkKey id) {
		featureTableName = id.getFeatureTableName();
		tileTableName = id.getTileTableName();
	}

	/**
	 * Get the feature table name
	 * 
	 * @return feature table name
	 */
	public String getFeatureTableName() {
		return featureTableName;
	}

	/**
	 * Set the feature table name
	 * 
	 * @param featureTableName
	 *            feature table name
	 */
	public void setFeatureTableName(String featureTableName) {
		this.featureTableName = featureTableName;
	}

	/**
	 * Get the tile table name
	 * 
	 * @return tile table name
	 */
	public String getTileTableName() {
		return tileTableName;
	}

	/**
	 * Set the tile table name
	 * 
	 * @param tileTableName
	 *            tile table name
	 */
	public void setTileTableName(String tileTableName) {
		this.tileTableName = tileTableName;
	}

}
