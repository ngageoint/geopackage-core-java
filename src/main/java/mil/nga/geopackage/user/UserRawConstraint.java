package mil.nga.geopackage.user;

/**
 * User table raw or unparsed constraint
 * 
 * @author osbornb
 * @since 3.2.1
 */
public class UserRawConstraint extends UserConstraint {

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
	public UserRawConstraint(String sql) {
		this.sql = sql;
	}

	/**
	 * Copy Constructor
	 * 
	 * @param userRawConstraint
	 *            user raw constraint
	 * @since 3.2.1
	 */
	public UserRawConstraint(UserRawConstraint userRawConstraint) {
		super(userRawConstraint.getName());
		setSql(userRawConstraint.getSql());
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
	 *            SQL
	 */
	public void setSql(String sql) {
		this.sql = sql;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String buildSql() {
		return sql;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UserConstraint copy() {
		return new UserRawConstraint(this);
	}

}
