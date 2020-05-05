package mil.nga.geopackage.extension.nga.link;

/**
 * Feature Tile Link Complex Primary Key including feature and tile table names
 * 
 * @author osbornb
 * @since 1.1.5
 */
public class FeatureTileLinkKey {

	/**
	 * Feature table name
	 */
	private String featureTableName;

	/**
	 * Tile table name
	 */
	private String tileTableName;

	/**
	 * Constructor
	 * 
	 * @param featureTableName
	 *            feature table name
	 * @param tileTableName
	 *            tile table name
	 */
	public FeatureTileLinkKey(String featureTableName, String tileTableName) {
		this.featureTableName = featureTableName;
		this.tileTableName = tileTableName;
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return featureTableName + "-" + tileTableName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((featureTableName == null) ? 0 : featureTableName.hashCode());
		result = prime * result
				+ ((tileTableName == null) ? 0 : tileTableName.hashCode());
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FeatureTileLinkKey other = (FeatureTileLinkKey) obj;
		if (featureTableName == null) {
			if (other.featureTableName != null)
				return false;
		} else if (!featureTableName.equals(other.featureTableName))
			return false;
		if (tileTableName == null) {
			if (other.tileTableName != null)
				return false;
		} else if (!tileTableName.equals(other.tileTableName))
			return false;
		return true;
	}

}
