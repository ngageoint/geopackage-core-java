package mil.nga.geopackage.db;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mil.nga.geopackage.GeoPackageException;

/**
 * Query pagination configuration
 * 
 * @author osbornb
 * @since 6.2.0
 */
public class Pagination {

	/**
	 * Limit SQL statement
	 */
	private static final String LIMIT = "LIMIT";

	/**
	 * Offset SQL statement
	 */
	private static final String OFFSET = "OFFSET";

	/**
	 * Expression 1 pattern group
	 */
	private static final String EXPRESSION1 = "expr1";

	/**
	 * Expression Separator pattern group
	 */
	private static final String SEPARATOR = "separator";

	/**
	 * Expression 1 pattern group
	 */
	private static final String EXPRESSION2 = "expr2";

	/**
	 * Limit pattern
	 */
	private static final Pattern limitPattern = Pattern
			.compile(
					"\\s" + LIMIT + "\\s+(?<" + EXPRESSION1
							+ ">-?+\\d+)(\\s*(?<" + SEPARATOR + ">(" + OFFSET
							+ "|,))\\s*(?<" + EXPRESSION2 + ">-?\\d+))?",
					Pattern.CASE_INSENSITIVE);

	/**
	 * Find the pagination offset and limit from the SQL statement
	 * 
	 * @param sql
	 *            SQL statement
	 * @return pagination or null if not found
	 */
	public static Pagination find(String sql) {
		Pagination pagination = null;
		Matcher matcher = limitPattern.matcher(sql);
		if (matcher.find()) {
			int limit;
			Long offset = null;
			String expr1 = matcher.group(EXPRESSION1);
			String separator = matcher.group(SEPARATOR);
			if (separator != null) {
				String expr2 = matcher.group(EXPRESSION2);
				if (separator.equalsIgnoreCase(OFFSET)) {
					limit = Integer.valueOf(expr1);
					offset = Long.valueOf(expr2);
				} else {
					limit = Integer.valueOf(expr2);
					offset = Long.valueOf(expr1);
				}
			} else {
				limit = Integer.valueOf(expr1);
			}
			pagination = new Pagination(limit, offset);
		}
		return pagination;
	}

	/**
	 * Replace the pagination limit and offset in the SQL statement
	 * 
	 * @param pagination
	 *            pagination settings
	 * @param sql
	 *            SQL statement
	 * @return modified SQL statement
	 */
	public static String replace(Pagination pagination, String sql) {
		String replaced = null;
		Matcher matcher = limitPattern.matcher(sql);
		if (matcher.find()) {
			replaced = matcher.replaceFirst(" " + pagination.toString());
		} else {
			throw new GeoPackageException(
					"SQL statement is not a paginated query: " + sql);
		}
		return replaced;
	}

	/**
	 * Limit
	 */
	private int limit;

	/**
	 * Offset
	 */
	private Long offset;

	/**
	 * Constructor
	 * 
	 * @param limit
	 *            upper bound number of rows
	 */
	public Pagination(int limit) {
		this(limit, null);
	}

	/**
	 * Constructor
	 * 
	 * @param limit
	 *            upper bound number of rows
	 * @param offset
	 *            row result starting offset
	 */
	public Pagination(int limit, long offset) {
		this(limit, Long.valueOf(offset));
	}

	/**
	 * Constructor
	 * 
	 * @param limit
	 *            upper bound number of rows
	 * @param offset
	 *            row result starting offset
	 */
	public Pagination(int limit, Long offset) {
		setLimit(limit);
		setOffset(offset);
	}

	/**
	 * Get the limit
	 * 
	 * @return upper bound number of rows
	 */
	public int getLimit() {
		return limit;
	}

	/**
	 * Set the limit
	 * 
	 * @param limit
	 *            upper bound number of rows
	 */
	public void setLimit(int limit) {
		this.limit = limit;
	}

	/**
	 * Is there positive limit
	 * 
	 * @return true if limit above 0
	 */
	public boolean hasLimit() {
		return limit > 0;
	}

	/**
	 * Get the offset
	 * 
	 * @return row result starting offset
	 */
	public Long getOffset() {
		return offset;
	}

	/**
	 * Is there an offset
	 * 
	 * @return true if has an offset
	 */
	public boolean hasOffset() {
		return offset != null;
	}

	/**
	 * Set the offset
	 * 
	 * @param offset
	 *            row result starting offset
	 */
	public void setOffset(Long offset) {
		if (offset != null && offset < 0) {
			offset = 0L;
		}
		this.offset = offset;
	}

	/**
	 * If the limit is positive, increment the offset
	 */
	public void incrementOffset() {
		if (limit > 0) {
			incrementOffset(limit);
		}
	}

	/**
	 * Increment the offset by the count
	 * 
	 * @param count
	 *            count to increment
	 */
	public void incrementOffset(long count) {
		if (this.offset == null) {
			this.offset = 0L;
		}
		this.offset += count;
	}

	/**
	 * Replace the limit and offset in the SQL statement with the pagination
	 * values
	 * 
	 * @param sql
	 *            SQL statement
	 * @return modified SQL statement
	 */
	public String replace(String sql) {
		return replace(this, sql);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder sql = new StringBuilder();
		sql.append(LIMIT);
		sql.append(" ");
		sql.append(limit);
		if (hasOffset()) {
			sql.append(" ");
			sql.append(OFFSET);
			sql.append(" ");
			sql.append(offset);
		}
		return sql.toString();
	}

}
