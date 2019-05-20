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
	 * Constraint prefix regex
	 */
	public static String PREFIX_REGEX = "(?i)(?s)((CONSTRAINT\\s+(\\\".+\\\"|\\S+)\\s+)?";

	/**
	 * Constraint suffix regex
	 */
	public static String SUFFIX_REGEX = "\\s+\\(.*\\).*)[,\\)$]";

	/**
	 * Constraint pattern matcher group
	 */
	public static int CONSTRAINT_GROUP = 1;

	/**
	 * Constraint name pattern matcher group
	 */
	public static int CONSTRAINT_NAME_GROUP = 3;

	/**
	 * Constraint name suffix regex
	 */
	public static String NAME_REGEX = "(?i)(?s)CONSTRAINT\\s+(\".+\"|\\S+)\\s+";

	/**
	 * Name pattern matcher group
	 */
	public static int NAME_GROUP = 1;

	/**
	 * Primary key constraint regex
	 */
	public static String PRIMARY_KEY_REGEX = "PRIMARY\\s+KEY";

	/**
	 * Unique constraint regex
	 */
	public static String UNIQUE_REGEX = "UNIQUE";

	/**
	 * Check constraint regex
	 */
	public static String CHECK_REGEX = "CHECK";

	/**
	 * Foreign key constraint regex
	 */
	public static String FOREIGN_KEY_REGEX = "FOREIGN\\s+KEY";

	/**
	 * Constraint name pattern
	 */
	public static Pattern NAME_PATTERN = Pattern.compile(NAME_REGEX);

	/**
	 * Compile a pattern for the constraint regex
	 * 
	 * @param constraintRegex
	 *            constraint regex
	 * @return pattern
	 */
	private static Pattern compilePattern(String constraintRegex) {
		return Pattern.compile(PREFIX_REGEX + constraintRegex + SUFFIX_REGEX);
	}

	/**
	 * Primary key constraint pattern
	 */
	public static Pattern PRIMARY_KEY_PATTERN = compilePattern(
			PRIMARY_KEY_REGEX);

	/**
	 * Unique constraint pattern
	 */
	public static Pattern UNIQUE_PATTERN = compilePattern(UNIQUE_REGEX);

	/**
	 * Check constraint pattern
	 */
	public static Pattern CHECK_PATTERN = compilePattern(CHECK_REGEX);

	/**
	 * Foreign key constraint pattern
	 */
	public static Pattern FOREIGN_KEY_PATTERN = compilePattern(
			FOREIGN_KEY_REGEX);

	/**
	 * Get the constraint pattern
	 * 
	 * @param type
	 *            constraint type
	 * @return pattern
	 */
	public static Pattern getPattern(ConstraintType type) {

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
	 * Get the constraints for the table SQL
	 * 
	 * @param tableSql
	 *            table SQL
	 * @return constraints
	 */
	public static List<Constraint> getConstraints(String tableSql) {
		List<Constraint> constraints = new ArrayList<>();
		for (ConstraintType type : ConstraintType.values()) {
			constraints.addAll(getConstraints(type, tableSql));
		}
		return constraints;
	}

	/**
	 * Get the constraints of the specified type for the table SQL
	 * 
	 * @param type
	 *            constraint type
	 * @param tableSql
	 *            table SQL
	 * @return constraints
	 */
	public static List<Constraint> getConstraints(ConstraintType type,
			String tableSql) {
		List<Constraint> constraints = new ArrayList<>();
		Matcher matcher = getPattern(type).matcher(tableSql);
		while (matcher.find()) {
			String constraintSql = matcher.group(CONSTRAINT_GROUP).trim();
			String name = CoreSQLUtils
					.quoteUnwrap(matcher.group(CONSTRAINT_NAME_GROUP));
			RawConstraint constraint = new RawConstraint(type, name,
					constraintSql);
			constraints.add(constraint);
		}
		return constraints;
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
