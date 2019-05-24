package mil.nga.geopackage.db.table;

import java.util.ArrayList;
import java.util.List;

import mil.nga.geopackage.user.UserColumn;

/**
 * Table unique constraint for one or more columns
 * 
 * @author osbornb
 * @since 3.3.0
 */
public class UniqueConstraint extends Constraint {

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
	public UniqueConstraint() {
		super(ConstraintType.UNIQUE);
	}

	/**
	 * Constructor
	 * 
	 * @param name
	 *            constraint name
	 */
	public UniqueConstraint(String name) {
		super(ConstraintType.UNIQUE, name);
	}

	/**
	 * Constructor
	 * 
	 * @param columns
	 *            columns
	 */
	public UniqueConstraint(UserColumn... columns) {
		super(ConstraintType.UNIQUE);
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
	public UniqueConstraint(String name, UserColumn... columns) {
		super(ConstraintType.UNIQUE, name);
		add(columns);
	}

	/**
	 * Copy Constructor
	 * 
	 * @param userUniqueConstraint
	 *            user unique constraint
	 */
	public UniqueConstraint(UniqueConstraint userUniqueConstraint) {
		super(ConstraintType.UNIQUE, userUniqueConstraint.getName());
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
	public UniqueConstraint copy() {
		return new UniqueConstraint(this);
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
