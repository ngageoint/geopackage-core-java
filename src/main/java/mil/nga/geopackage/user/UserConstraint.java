package mil.nga.geopackage.user;

import mil.nga.geopackage.db.CoreSQLUtils;

/**
 * User table constraint
 * 
 * @author osbornb
 * @since 3.2.1
 */
public abstract class UserConstraint {

	/**
	 * Optional constraint name
	 */
	private String name;

	/**
	 * Constructor
	 */
	protected UserConstraint() {

	}

	/**
	 * Constructor
	 * 
	 * @param name
	 *            constraint name
	 */
	protected UserConstraint(String name) {
		this.name = name;
	}

	/**
	 * Get the name
	 * 
	 * @return name or null
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the name
	 * 
	 * @param name
	 *            name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Build the name SQL
	 * 
	 * @return name SQL
	 */
	protected String buildNameSql() {
		String sql = "";
		if (name != null) {
			sql = "CONSTRAINT " + CoreSQLUtils.quoteWrap(name) + " ";
		}
		return sql;
	}

	/**
	 * Build the constraint SQL
	 * 
	 * @return sql constraint
	 */
	public abstract String buildSql();

	/**
	 * Copy the constraint
	 * 
	 * @return copied constraint
	 */
	public abstract UserConstraint copy();

}
