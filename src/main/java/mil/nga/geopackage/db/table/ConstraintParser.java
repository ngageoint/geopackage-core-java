package mil.nga.geopackage.db.table;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.db.CoreSQLUtils;

/**
 * SQL constraint parser from create table statements
 * 
 * @author osbornb
 * @since 3.2.1
 */
public class ConstraintParser {

	/**
	 * Regex prefix for ignoring: case insensitive, dotall mode (match line
	 * terminators), and start of line
	 */
	private static String REGEX_PREFIX = "(?i)(?s)^";

	/**
	 * Constraint keyword with name regex
	 */
	private static String CONSTRAINT_REGEX = "CONSTRAINT\\s+(\".+\"|\\S+)\\s";

	/**
	 * Constraint name pattern
	 */
	private static Pattern NAME_PATTERN = Pattern
			.compile(REGEX_PREFIX + CONSTRAINT_REGEX);

	/**
	 * Name pattern matcher group
	 */
	private static int NAME_GROUP = 1;

	/**
	 * Compile a pattern for the constraint regex: {@link #REGEX_PREFIX},
	 * optional {@link #CONSTRAINT_REGEX}, and constraint regex
	 * 
	 * @param constraintRegex
	 *            constraint regex
	 * @return pattern
	 */
	private static Pattern compilePattern(String constraintRegex) {
		return Pattern.compile(REGEX_PREFIX + "(" + CONSTRAINT_REGEX + "+)?"
				+ constraintRegex);
	}

	/**
	 * Primary key constraint pattern
	 */
	private static Pattern PRIMARY_KEY_PATTERN = compilePattern(
			"PRIMARY\\s+KEY");

	/**
	 * Unique constraint pattern
	 */
	private static Pattern UNIQUE_PATTERN = compilePattern("UNIQUE");

	/**
	 * Check constraint pattern
	 */
	private static Pattern CHECK_PATTERN = compilePattern("CHECK");

	/**
	 * Foreign key constraint pattern
	 */
	private static Pattern FOREIGN_KEY_PATTERN = compilePattern(
			"FOREIGN\\s+KEY");

	/**
	 * Get the constraints for the table SQL
	 * 
	 * @param tableSql
	 *            table SQL
	 * @return constraints
	 */
	public static List<Constraint> getConstraints(String tableSql) {
		return getConstraints(tableSql, null);
	}

	/**
	 * Get the constraints for the table SQL of the specified type
	 * 
	 * @param tableSql
	 *            table SQL
	 * @param type
	 *            constraint type
	 * @return constraints
	 */
	public static List<Constraint> getConstraints(String tableSql,
			ConstraintType type) {

		List<Constraint> constraints = new ArrayList<>();

		// Find the start and end of the column definitions and table
		// constraints
		int start = tableSql.indexOf("(");
		int end = tableSql.lastIndexOf(")");

		if (start >= 0 && end >= 0) {

			String definitions = tableSql.substring(start + 1, end).trim();

			// Parse the column definitions and table constraints, divided by
			// columns when not within parentheses. Create constraints when
			// found.
			int openParentheses = 0;
			int sqlStart = 0;

			for (int i = 0; i < definitions.length(); i++) {
				char character = definitions.charAt(i);
				if (character == '(') {
					openParentheses++;
				} else if (character == ')') {
					openParentheses--;
				} else if (character == ',' && openParentheses == 0) {
					String sql = definitions.substring(sqlStart, i);
					addConstraint(constraints, sql, type);
					sqlStart = i + 1;
				}
			}
			if (sqlStart < definitions.length()) {
				String sql = definitions.substring(sqlStart,
						definitions.length());
				addConstraint(constraints, sql, type);
			}
		}

		return constraints;
	}

	/**
	 * Add a constraint if the SQL is a constraint and of the constraint type
	 * 
	 * @param constraints
	 *            constraints to add to
	 * @param sql
	 *            SQL statement
	 * @param type
	 *            constraint type or null for all constraint types
	 */
	private static void addConstraint(List<Constraint> constraints, String sql,
			ConstraintType type) {
		Constraint constraint = getConstraint(sql, type);
		if (constraint != null) {
			constraints.add(constraint);
		}
	}

	/**
	 * Attempt to get a constraint by parsing the SQL statement
	 * 
	 * @param sql
	 *            SQL statement
	 * @return constraint or null
	 */
	public static Constraint getConstraint(String sql) {
		return getConstraint(sql, null);
	}

	/**
	 * Attempt to get a constraint of a specified type by parsing the SQL
	 * statement
	 * 
	 * @param sql
	 *            SQL statement
	 * @param type
	 *            matching constraint type or null for all constraint types
	 * @return constraint or null
	 */
	public static Constraint getConstraint(String sql, ConstraintType type) {

		Constraint constraint = null;

		sql = sql.trim();
		ConstraintType constraintType = getType(sql);
		if (constraintType != null
				&& (type == null || constraintType == type)) {
			String name = getName(sql);
			constraint = new RawConstraint(constraintType, name, sql);
		}

		return constraint;
	}

	/**
	 * Determine if the constraint SQL is the constraint type
	 * 
	 * @param type
	 *            constraint type
	 * @param constraintSql
	 *            constraint SQL
	 * @return true if the constraint type
	 */
	public static boolean isType(ConstraintType type, String constraintSql) {
		return getPattern(type).matcher(constraintSql).find();
	}

	/**
	 * Get the constraint pattern
	 * 
	 * @param type
	 *            constraint type
	 * @return pattern
	 */
	private static Pattern getPattern(ConstraintType type) {

		Pattern pattern = null;

		switch (type) {
		case PRIMARY_KEY:
			pattern = PRIMARY_KEY_PATTERN;
			break;
		case UNIQUE:
			pattern = UNIQUE_PATTERN;
			break;
		case CHECK:
			pattern = CHECK_PATTERN;
			break;
		case FOREIGN_KEY:
			pattern = FOREIGN_KEY_PATTERN;
			break;
		default:
			throw new GeoPackageException(
					"Unsupporte constraint type: " + type);
		}

		return pattern;
	}

	/**
	 * Get the constraint type of the constraint SQL
	 * 
	 * @param constraintSql
	 *            constraint SQL
	 * @return constraint type or null
	 */
	public static ConstraintType getType(String constraintSql) {
		ConstraintType type = null;

		if (isType(ConstraintType.PRIMARY_KEY, constraintSql)) {
			type = ConstraintType.PRIMARY_KEY;
		} else if (isType(ConstraintType.UNIQUE, constraintSql)) {
			type = ConstraintType.UNIQUE;
		} else if (isType(ConstraintType.CHECK, constraintSql)) {
			type = ConstraintType.CHECK;
		} else if (isType(ConstraintType.FOREIGN_KEY, constraintSql)) {
			type = ConstraintType.FOREIGN_KEY;
		}

		return type;
	}

	/**
	 * Get the constraint name if it has one
	 * 
	 * @param constraintSql
	 *            constraint sql
	 * @return constraint name or null
	 */
	public static String getName(String constraintSql) {
		String name = null;
		Matcher matcher = NAME_PATTERN.matcher(constraintSql);
		if (matcher.find()) {
			name = CoreSQLUtils.quoteUnwrap(matcher.group(NAME_GROUP));
		}
		return name;
	}

}
