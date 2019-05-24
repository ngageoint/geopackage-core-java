package mil.nga.geopackage.db.table;

import mil.nga.geopackage.db.CoreSQLUtils;

/**
 * User table constraint
 * 
 * @author osbornb
 * @since 3.3.0
 */
public abstract class Constraint {

	/**
	 * Constraint keyword
	 */
	public static final String CONSTRAINT = "CONSTRAINT";

	/**
	 * Optional constraint name
	 */
	private String name;

	/**
	 * Constraint type
	 */
	private ConstraintType type;

	/**
	 * Constructor
	 * 
	 * @param type
	 *            constraint type
	 */
	protected Constraint(ConstraintType type) {
		this(type, null);
	}

	/**
	 * Constructor
	 * 
	 * @param type
	 *            constraint type
	 * @param name
	 *            constraint name
	 */
	protected Constraint(ConstraintType type, String name) {
		this.type = type;
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
	 * Get the constraint type
	 * 
	 * @return constraint type
	 */
	public ConstraintType getType() {
		return type;
	}

	/**
	 * Set the constraint type
	 * 
	 * @param type
	 *            constraint type
	 */
	public void setType(ConstraintType type) {
		this.type = type;
	}

	/**
	 * Build the name SQL
	 * 
	 * @return name SQL
	 */
	protected String buildNameSql() {
		String sql = "";
		if (name != null) {
			sql = CONSTRAINT + " " + CoreSQLUtils.quoteWrap(name) + " ";
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
	public abstract Constraint copy();

}
