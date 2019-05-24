package mil.nga.geopackage.db.table;

/**
 * Table raw or unparsed constraint
 * 
 * @author osbornb
 * @since 3.3.0
 */
public class RawConstraint extends Constraint {

	/**
	 * SQL statement
	 */
	private String sql = null;

	/**
	 * Constructor
	 * 
	 * @param sql
	 *            constraint SQL
	 */
	public RawConstraint(String sql) {
		this(ConstraintParser.getType(sql), sql);
	}

	/**
	 * Constructor
	 * 
	 * @param type
	 *            constraint type
	 * @param sql
	 *            constraint SQL
	 */
	public RawConstraint(ConstraintType type, String sql) {
		this(type, ConstraintParser.getName(sql), sql);
	}

	/**
	 * Constructor
	 * 
	 * @param type
	 *            constraint type
	 * @param name
	 *            constraint name
	 * @param sql
	 *            constraint SQL
	 */
	public RawConstraint(ConstraintType type, String name, String sql) {
		super(type, name);
		this.sql = sql;
	}

	/**
	 * Copy Constructor
	 * 
	 * @param userRawConstraint
	 *            user raw constraint
	 */
	public RawConstraint(RawConstraint userRawConstraint) {
		super(userRawConstraint.getType(), userRawConstraint.getName());
		this.sql = userRawConstraint.getSql();
	}

	/**
	 * Get the constraint SQL
	 * 
	 * @return SQL
	 */
	public String getSql() {
		return sql;
	}

	/**
	 * Set the constraint SQL
	 * 
	 * @param sql
	 *            constraint SQL
	 */
	public void setSql(String sql) {
		this.sql = sql;
	}

	/**
	 * Set the type from the constraint SQL
	 * 
	 * @param sql
	 *            constraint SQL
	 */
	public void setTypeFromSql(String sql) {
		setType(ConstraintParser.getType(sql));
	}

	/**
	 * Set the name from the constraint SQL
	 * 
	 * @param sql
	 *            constraint SQL
	 */
	public void setNameFromSql(String sql) {
		setName(ConstraintParser.getName(sql));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String buildSql() {
		String sql = this.sql;
		if (!sql.toUpperCase().startsWith(CONSTRAINT)) {
			sql = buildNameSql() + sql;
		}
		return sql;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Constraint copy() {
		return new RawConstraint(this);
	}

}
