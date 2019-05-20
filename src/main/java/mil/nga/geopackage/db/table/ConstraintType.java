package mil.nga.geopackage.db.table;

/**
 * Constraint types
 * 
 * @author osbornb
 * @since 3.2.1
 */
public enum ConstraintType {

	/**
	 * Primary key constraint
	 */
	PRIMARY_KEY,

	/**
	 * Unique constraint
	 */
	UNIQUE,

	/**
	 * Check constraint
	 */
	CHECK,

	/**
	 * Foreign key constraint
	 */
	FOREIGN_KEY;

}
