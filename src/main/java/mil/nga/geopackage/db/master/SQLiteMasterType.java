package mil.nga.geopackage.db.master;

/**
 * SQLite Master table (sqlite_master) type column keywords
 * 
 * @author osbornb
 * @since 3.3.0
 */
public enum SQLiteMasterType {

	/**
	 * Table keyword
	 */
	TABLE,

	/**
	 * Index keyword
	 */
	INDEX,

	/**
	 * View keyword
	 */
	VIEW,

	/**
	 * Trigger keyword
	 */
	TRIGGER;

}
