package mil.nga.geopackage.db;

/**
 * SQLite Master table (sqlite_master) type column keywords
 * 
 * @author osbornb
 * @since 3.2.1
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
