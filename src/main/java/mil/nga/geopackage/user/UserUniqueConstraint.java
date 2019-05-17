package mil.nga.geopackage.user;

import java.util.ArrayList;
import java.util.List;

/**
 * User table unique constraint for one or more columns
 * 
 * @author osbornb
 */
public class UserUniqueConstraint extends UserConstraint {

	/**
	 * Unique keyword
	 */
	public static final String UNIQUE = "UNIQUE";

	/**
	 * Columns included in the unique constraint
	 */
	private final List<UserColumn> columns = new ArrayList<>();

	/**
	 * Constructor
	 */
	public UserUniqueConstraint() {

	}

	/**
	 * Constructor
	 * 
	 * @param name
	 *            constraint name
	 */
	public UserUniqueConstraint(String name) {
		super(name);
	}

	/**
	 * Constructor
	 * 
	 * @param columns
	 *            columns
	 */
	public UserUniqueConstraint(UserColumn... columns) {
		add(columns);
	}

	/**
	 * Constructor
	 * 
	 * @param name
	 *            constraint name
	 * @param columns
	 *            columns
	 */
	public UserUniqueConstraint(String name, UserColumn... columns) {
		super(name);
		add(columns);
	}

	/**
	 * Copy Constructor
	 * 
	 * @param userUniqueConstraint
	 *            user unique constraint
	 * @since 3.2.1
	 */
	public UserUniqueConstraint(UserUniqueConstraint userUniqueConstraint) {
		super(userUniqueConstraint.getName());
		for (UserColumn column : userUniqueConstraint.columns) {
			add(column.copy());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String buildSql() {
		StringBuilder sql = new StringBuilder();
		sql.append(buildNameSql());
		sql.append(UNIQUE);
		sql.append(" (");
		for (int i = 0; i < columns.size(); i++) {
			UserColumn column = columns.get(i);
			if (i > 0) {
				sql.append(", ");
			}
			sql.append(column.getName());
		}
		sql.append(")");
		return sql.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UserUniqueConstraint copy() {
		return new UserUniqueConstraint(this);
	}

	/**
	 * Add columns
	 * 
	 * @param columns
	 *            columns
	 */
	public void add(UserColumn... columns) {
		for (UserColumn column : columns) {
			this.columns.add(column);
		}
	}

	/**
	 * Get the columns
	 * 
	 * @return columns
	 */
	public List<UserColumn> getColumns() {
		return columns;
	}

}
