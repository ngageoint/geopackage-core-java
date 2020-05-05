package mil.nga.geopackage.extension.nga.index;

/**
 * Geometry Index Complex Primary Key including table and column name
 * 
 * @author osbornb
 * @since 1.1.0
 */
public class GeometryIndexKey {

	/**
	 * Table name
	 */
	private String tableName;

	/**
	 * Geometry id
	 */
	private long geomId;

	/**
	 * Constructor
	 * 
	 * @param tableName
	 *            table name
	 * @param geomId
	 *            geom id
	 */
	public GeometryIndexKey(String tableName, long geomId) {
		this.tableName = tableName;
		this.geomId = geomId;
	}

	/**
	 * Get the table name
	 * 
	 * @return table name
	 */
	public String getTableName() {
		return tableName;
	}

	/**
	 * Set the table name
	 * 
	 * @param tableName
	 *            table name
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	/**
	 * Get the geometry id
	 * 
	 * @return geom id
	 */
	public long getGeomId() {
		return geomId;
	}

	/**
	 * Set the geometry id
	 * 
	 * @param geomId
	 *            geom id
	 */
	public void setGeomId(long geomId) {
		this.geomId = geomId;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return tableName + ":" + geomId;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (geomId ^ (geomId >>> 32));
		result = prime * result
				+ ((tableName == null) ? 0 : tableName.hashCode());
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
		GeometryIndexKey other = (GeometryIndexKey) obj;
		if (geomId != other.geomId)
			return false;
		if (tableName == null) {
			if (other.tableName != null)
				return false;
		} else if (!tableName.equals(other.tableName))
			return false;
		return true;
	}

}
