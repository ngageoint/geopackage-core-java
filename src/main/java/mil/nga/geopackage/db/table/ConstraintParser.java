package mil.nga.geopackage.db.table;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mil.nga.geopackage.db.CoreSQLUtils;

/**
 * SQL constraint parser from create table statements
 * 
 * @author osbornb
 * @since 3.3.0
 */
public class ConstraintParser {

	/**
	 * Regex prefix for ignoring: case insensitive, dotall mode (match line
	 * terminators), and start of line
	 */
	private static final String REGEX_PREFIX = "(?i)(?s)^";

	/**
	 * Constraint name regex suffix
	 */
	private static final String CONSTRAINT_NAME_REGEX_SUFFIX = "CONSTRAINT\\s+(\".+\"|\\S+)\\s";

	/**
	 * Constraint name regex
	 */
	private static final String CONSTRAINT_NAME_REGEX = REGEX_PREFIX
			+ CONSTRAINT_NAME_REGEX_SUFFIX;

	/**
	 * Constraint name and definition regex
	 */
	private static final String CONSTRAINT_REGEX = REGEX_PREFIX + "("
			+ CONSTRAINT_NAME_REGEX_SUFFIX + ")?(.*)";

	/**
	 * Constraint name pattern
	 */
	private static final Pattern NAME_PATTERN = Pattern
			.compile(CONSTRAINT_NAME_REGEX);

	/**
	 * Constraint name pattern name matcher group
	 */
	private static final int NAME_PATTERN_NAME_GROUP = 1;

	/**
	 * Constraint name and definition pattern
	 */
	private static final Pattern CONSTRAINT_PATTERN = Pattern
			.compile(CONSTRAINT_REGEX);

	/**
	 * Constraint name and definition pattern name matcher group
	 */
	private static final int CONSTRAINT_PATTERN_NAME_GROUP = 2;

	/**
	 * Constraint name and definition pattern definition matcher group
	 */
	private static final int CONSTRAINT_PATTERN_DEFINITION_GROUP = 3;

	/**
	 * Get the constraints for the table SQL
	 * 
	 * @param tableSql
	 *            table SQL
	 * @return constraints
	 */
	public static TableConstraints getConstraints(String tableSql) {

		TableConstraints constraints = new TableConstraints();

		// Find the start and end of the column definitions and table
		// constraints
		int start = -1;
		int end = -1;
		if (tableSql != null) {
			start = tableSql.indexOf("(");
			end = tableSql.lastIndexOf(")");
		}

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
					String constraintSql = definitions.substring(sqlStart, i);
					addConstraints(constraints, constraintSql);
					sqlStart = i + 1;
				}
			}
			if (sqlStart < definitions.length()) {
				String constraintSql = definitions.substring(sqlStart,
						definitions.length());
				addConstraints(constraints, constraintSql);
			}
		}

		return constraints;
	}

	/**
	 * Add constraints of the optional type or all constraints
	 * 
	 * @param constraints
	 *            constraints to add to
	 * @param constraintSQL
	 *            constraint SQL statement
	 */
	private static void addConstraints(TableConstraints constraints,
			String constraintSql) {
		Constraint constraint = getTableConstraint(constraintSql);
		if (constraint != null) {
			constraints.addTableConstraint(constraint);
		} else {
			ColumnConstraints columnConstraints = getColumnConstraints(
					constraintSql);
			if (columnConstraints.hasConstraints()) {
				constraints.addColumnConstraints(columnConstraints);
			}
		}
	}

	/**
	 * Attempt to get column constraints by parsing the SQL statement
	 * 
	 * @param constraintSql
	 *            constraint SQL statement
	 * @return constraints
	 */
	public static ColumnConstraints getColumnConstraints(String constraintSql) {

		String[] parts = constraintSql.trim().split("\\s+");
		String columnName = CoreSQLUtils.quoteUnwrap(parts[0]);

		ColumnConstraints constraints = new ColumnConstraints(columnName);

		int constraintIndex = -1;
		ConstraintType constraintType = null;

		for (int i = 1; i < parts.length; i++) {
			String part = parts[i];

			if (Constraint.CONSTRAINT.equalsIgnoreCase(part)) {

				if (constraintType != null) {
					constraints.addConstraint(createConstraint(parts,
							constraintIndex, i, constraintType));
					constraintType = null;
				}

				constraintIndex = i;

			} else {

				ConstraintType type = ConstraintType.getColumnType(part);
				if (type != null) {

					if (constraintType != null) {
						constraints.addConstraint(createConstraint(parts,
								constraintIndex, i, constraintType));
						constraintIndex = -1;
					}

					if (constraintIndex < 0) {
						constraintIndex = i;
					}
					constraintType = type;

				}
			}
		}

		if (constraintType != null) {
			constraints.addConstraint(createConstraint(parts, constraintIndex,
					parts.length, constraintType));
		}

		return constraints;
	}

	/**
	 * Create a constraint from the SQL parts with the range for the type
	 * 
	 * @param parts
	 *            SQL parts
	 * @param startIndex
	 *            start index (inclusive)
	 * @param endIndex
	 *            end index (exclusive)
	 * @param type
	 *            constraint type
	 * @return constraint
	 */
	private static Constraint createConstraint(String[] parts, int startIndex,
			int endIndex, ConstraintType type) {

		StringBuilder constraintSql = new StringBuilder();
		for (int i = startIndex; i < endIndex; i++) {
			if (constraintSql.length() > 0) {
				constraintSql.append(" ");
			}
			constraintSql.append(parts[i]);
		}

		String sql = constraintSql.toString();
		String name = getName(sql);

		return new RawConstraint(type, name, sql);
	}

	/**
	 * Attempt to get the constraint by parsing the SQL statement
	 * 
	 * @param constraintSql
	 *            constraint SQL statement
	 * @param table
	 *            true to search for a table constraint, false to search for a
	 *            column constraint
	 * @return constraint or null
	 */
	private static Constraint getConstraint(String constraintSql,
			boolean table) {

		Constraint constraint = null;

		String[] nameAndDefinition = getNameAndDefinition(constraintSql);

		String definition = nameAndDefinition[1];
		if (definition != null) {

			String prefix = definition.split("\\s+")[0];
			ConstraintType type = null;
			if (table) {
				type = ConstraintType.getTableType(prefix);
			} else {
				type = ConstraintType.getColumnType(prefix);
			}

			if (type != null) {
				constraint = new RawConstraint(type, nameAndDefinition[0],
						constraintSql.trim());
			}
		}

		return constraint;
	}

	/**
	 * Attempt to get a table constraint by parsing the SQL statement
	 * 
	 * @param constraintSql
	 *            constraint SQL statement
	 * @return constraint or null
	 */
	public static Constraint getTableConstraint(String constraintSql) {
		return getConstraint(constraintSql, true);
	}

	/**
	 * Check if the SQL is a table type constraint
	 * 
	 * @param constraintSql
	 *            constraint SQL statement
	 * @return true if a table constraint
	 */
	public static boolean isTableConstraint(String constraintSql) {
		return getTableConstraint(constraintSql) != null;
	}

	/**
	 * Get the table constraint type of the constraint SQL
	 * 
	 * @param constraintSql
	 *            constraint SQL
	 * @return constraint type or null
	 */
	public static ConstraintType getTableType(String constraintSql) {
		ConstraintType type = null;
		Constraint constraint = getTableConstraint(constraintSql);
		if (constraint != null) {
			type = constraint.getType();
		}
		return type;
	}

	/**
	 * Determine if the table constraint SQL is the constraint type
	 * 
	 * @param type
	 *            constraint type
	 * @param constraintSql
	 *            constraint SQL
	 * @return true if the constraint type
	 */
	public static boolean isTableType(ConstraintType type,
			String constraintSql) {
		boolean isType = false;
		ConstraintType constraintType = getTableType(constraintSql);
		if (constraintType != null) {
			isType = type == constraintType;
		}
		return isType;
	}

	/**
	 * Attempt to get a column constraint by parsing the SQL statement
	 * 
	 * @param constraintSql
	 *            constraint SQL statement
	 * @return constraint or null
	 */
	public static Constraint getColumnConstraint(String constraintSql) {
		return getConstraint(constraintSql, false);
	}

	/**
	 * Check if the SQL is a column type constraint
	 * 
	 * @param constraintSql
	 *            constraint SQL statement
	 * @return true if a column constraint
	 */
	public static boolean isColumnConstraint(String constraintSql) {
		return getColumnConstraint(constraintSql) != null;
	}

	/**
	 * Get the column constraint type of the constraint SQL
	 * 
	 * @param constraintSql
	 *            constraint SQL
	 * @return constraint type or null
	 */
	public static ConstraintType getColumnType(String constraintSql) {
		ConstraintType type = null;
		Constraint constraint = getColumnConstraint(constraintSql);
		if (constraint != null) {
			type = constraint.getType();
		}
		return type;
	}

	/**
	 * Determine if the column constraint SQL is the constraint type
	 * 
	 * @param type
	 *            constraint type
	 * @param constraintSql
	 *            constraint SQL
	 * @return true if the constraint type
	 */
	public static boolean isColumnType(ConstraintType type,
			String constraintSql) {
		boolean isType = false;
		ConstraintType constraintType = getColumnType(constraintSql);
		if (constraintType != null) {
			isType = type == constraintType;
		}
		return isType;
	}

	/**
	 * Attempt to get a constraint by parsing the SQL statement
	 * 
	 * @param constraintSql
	 *            constraint SQL statement
	 * @return constraint or null
	 */
	public static Constraint getConstraint(String constraintSql) {
		Constraint constraint = getTableConstraint(constraintSql);
		if (constraint == null) {
			constraint = getColumnConstraint(constraintSql);
		}
		return constraint;
	}

	/**
	 * Check if the SQL is a constraint
	 * 
	 * @param constraintSql
	 *            constraint SQL statement
	 * @return true if a constraint
	 */
	public static boolean isConstraint(String constraintSql) {
		return getConstraint(constraintSql) != null;
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
		Constraint constraint = getConstraint(constraintSql);
		if (constraint != null) {
			type = constraint.getType();
		}
		return type;
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
		boolean isType = false;
		ConstraintType constraintType = getType(constraintSql);
		if (constraintType != null) {
			isType = type == constraintType;
		}
		return isType;
	}

	/**
	 * Get the constraint name if it has one
	 * 
	 * @param constraintSql
	 *            constraint SQL
	 * @return constraint name or null
	 */
	public static String getName(String constraintSql) {
		String name = null;
		Matcher matcher = NAME_PATTERN.matcher(constraintSql);
		if (matcher.find()) {
			name = CoreSQLUtils
					.quoteUnwrap(matcher.group(NAME_PATTERN_NAME_GROUP));
		}
		return name;
	}

	/**
	 * Get the constraint name and remaining definition
	 * 
	 * @param constraintSql
	 *            constraint SQL
	 * @return array with name or null at index 0, definition at index 1
	 */
	public static String[] getNameAndDefinition(String constraintSql) {
		String parts[] = null;
		Matcher matcher = CONSTRAINT_PATTERN.matcher(constraintSql.trim());
		if (matcher.find()) {
			String name = CoreSQLUtils
					.quoteUnwrap(matcher.group(CONSTRAINT_PATTERN_NAME_GROUP));
			if (name != null) {
				name = name.trim();
			}
			String definition = matcher
					.group(CONSTRAINT_PATTERN_DEFINITION_GROUP);
			if (definition != null) {
				definition = definition.trim();
			}
			parts = new String[] { name, definition };
		}
		return parts;
	}

}
