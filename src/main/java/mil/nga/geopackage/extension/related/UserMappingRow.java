package mil.nga.geopackage.extension.related;

/**
 * User Mapping Row containing the values from a single cursor row
 * 
 * @author jyutzler
 */
public class UserMappingRow {

	private long baseId;
	private long relatedId;
	/**
	 * Constructor
	 * 
	 * @param baseId
	 * @param relatedId
	 */
	public UserMappingRow(long baseId, long relatedId) {
		this.baseId = baseId;
		this.relatedId = relatedId;
	}

	/**
	 * Get the base ID
	 * 
	 * @return base ID
	 */
	public long getBaseId() {
		return baseId;
	}
	
	/**
	 * Set the base ID
	 * 
	 * @param baseId
	 */
	public void setBaseId(long baseId){
		this.baseId = baseId;
	}

	/**
	 * Get the related ID
	 * 
	 * @return related ID
	 */
	public long getRelatedId() {
		return relatedId;
	}
	
	/**
	 * Set the base ID
	 * 
	 * @param relatedId
	 */
	public void setRelatedId(long relatedId){
		this.relatedId = relatedId;
	}
}
