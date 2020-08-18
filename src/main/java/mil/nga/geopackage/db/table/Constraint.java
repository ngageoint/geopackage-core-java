package mil.nga.geopackage.db.table;

import mil.nga.geopackage.db.CoreSQLUtils;

/**
 * Table or column constraint
 * 
 * @author osbornb
 * @since 3.3.0
 */
public abstract class Constraint implements Comparable<Constraint> {

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
	 * Optional order
	 */
	private Integer order;

	/**
	 * Constructor
	 * 
	 * @param type
	 *            constraint type
	 */
	protected Constraint(ConstraintType type) {
		this(type, null, null);
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
		this(type, name, null);
	}

	/**
	 * Constructor
	 * 
	 * @param type
	 *            constraint type
	 * @param order
	 *            constraint order
	 */
	protected Constraint(ConstraintType type, Integer order) {
		this(type, null, order);
	}

	/**
	 * Constructor
	 * 
	 * @param type
	 *            constraint type
	 * @param name
	 *            constraint name
	 * @param order
	 *            constraint order
	 */
	protected Constraint(ConstraintType type, String name, Integer order) {
		this.type = type;
		this.name = name;
		this.order = order;
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
	 * Get the order
	 * 
	 * @return order number or null
	 * @since 4.0.1
	 */
	public Integer getOrder() {
		return order;
	}

	/**
	 * Set the order to a number or null
	 * 
	 * @param order
	 *            order number
	 * @since 4.0.1
	 */
	public void setOrder(Integer order) {
		this.order = order;
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
	 * {@inheritDoc}
	 */
	@Override
	public int compareTo(Constraint constraint) {
		return getOrder(order) - getOrder(constraint.order) <= 0 ? -1 : 1;
	}

	/**
	 * Get the int order from the integer
	 * 
	 * @param order
	 *            order
	 * @return int value or max if null
	 */
	private int getOrder(Integer order) {
		return order != null ? order : Integer.MAX_VALUE;
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
