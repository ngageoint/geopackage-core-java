package mil.nga.geopackage.db.master;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import mil.nga.geopackage.db.CoreSQLUtils;

/**
 * Query on the SQLiteMaster table
 * 
 * @author osbornb
 * @since 3.3.0
 */
public class SQLiteMasterQuery {

	/**
	 * Combine operation for multiple queries
	 */
	private final String combineOperation;

	/**
	 * List of queries
	 */
	private final List<String> queries = new ArrayList<>();

	/**
	 * List of arguments
	 */
	private final List<String> arguments = new ArrayList<>();

	/**
	 * Create a query with the combine operation
	 * 
	 * @param combineOperation
	 *            combine operation
	 */
	private SQLiteMasterQuery(String combineOperation) {
		this.combineOperation = combineOperation;
	}

	/**
	 * Add an equality query
	 * 
	 * @param column
	 *            column
	 * @param value
	 *            value
	 */
	public void add(SQLiteMasterColumn column, String value) {
		add(column, "=", value);
	}

	/**
	 * Add a query
	 * 
	 * @param column
	 *            column
	 * @param operation
	 *            operation
	 * @param value
	 *            value
	 */
	public void add(SQLiteMasterColumn column, String operation, String value) {
		validateAdd();
		queries.add(
				"LOWER(" + CoreSQLUtils.quoteWrap(column.name().toLowerCase())
						+ ") " + operation + " LOWER(?)");
		arguments.add(value);
	}

	/**
	 * Add an is null query
	 * 
	 * @param column
	 *            column
	 */
	public void addIsNull(SQLiteMasterColumn column) {
		validateAdd();
		queries.add(CoreSQLUtils.quoteWrap(column.name().toLowerCase())
				+ " IS NULL");
	}

	/**
	 * Add an is not null query
	 * 
	 * @param column
	 *            column
	 */
	public void addIsNotNull(SQLiteMasterColumn column) {
		validateAdd();
		queries.add(CoreSQLUtils.quoteWrap(column.name().toLowerCase())
				+ " IS NOT NULL");
	}

	/**
	 * Validate the state of the query when adding to the query
	 */
	private void validateAdd() {
		if (combineOperation == null && !queries.isEmpty()) {
			throw new IllegalStateException(
					"Query without a combination operation supports only a single query");
		}
	}

	/**
	 * Determine a query has been set
	 * 
	 * @return true if has a query
	 */
	public boolean has() {
		return !queries.isEmpty();
	}

	/**
	 * Build the query SQL
	 * 
	 * @return sql
	 */
	public String buildSQL() {
		StringBuilder sql = new StringBuilder();
		if (queries.size() > 1) {
			sql.append("( ");
		}
		for (int i = 0; i < queries.size(); i++) {
			if (i > 0) {
				sql.append(" ");
				sql.append(combineOperation);
				sql.append(" ");
			}
			sql.append(queries.get(i));
		}
		if (queries.size() > 1) {
			sql.append(" )");
		}
		return sql.toString();
	}

	/**
	 * Get the query arguments
	 * 
	 * @return arguments
	 */
	public List<String> getArguments() {
		return arguments;
	}

	/**
	 * Create an empty query that supports a single query
	 * 
	 * @return query
	 */
	public static SQLiteMasterQuery create() {
		return new SQLiteMasterQuery(null);
	}

	/**
	 * Create a query with multiple queries combined by an OR
	 * 
	 * @return query
	 */
	public static SQLiteMasterQuery createOr() {
		return new SQLiteMasterQuery("OR");
	}

	/**
	 * Create a query with multiple queries combined by an AND
	 * 
	 * @return query
	 */
	public static SQLiteMasterQuery createAnd() {
		return new SQLiteMasterQuery("AND");
	}

	/**
	 * Create a single equality query
	 * 
	 * @param column
	 *            column
	 * @param value
	 *            value
	 * @return query
	 */
	public static SQLiteMasterQuery create(SQLiteMasterColumn column,
			String value) {
		SQLiteMasterQuery query = create();
		query.add(column, value);
		return query;
	}

	/**
	 * Create a single query
	 * 
	 * @param column
	 *            column
	 * @param operation
	 *            operation
	 * @param value
	 *            value
	 * @return query
	 */
	public static SQLiteMasterQuery create(SQLiteMasterColumn column,
			String operation, String value) {
		SQLiteMasterQuery query = create();
		query.add(column, operation, value);
		return query;
	}

	/**
	 * Create an equality query with multiple values for a single column
	 * combined with an OR
	 * 
	 * @param column
	 *            column
	 * @param values
	 *            value
	 * @return query
	 */
	public static SQLiteMasterQuery createOr(SQLiteMasterColumn column,
			Collection<String> values) {
		SQLiteMasterQuery query = createOr();
		for (String value : values) {
			query.add(column, value);
		}
		return query;
	}

	/**
	 * Create a query with multiple values for a single column combined with an
	 * OR
	 * 
	 * @param column
	 *            column
	 * @param operation
	 *            operation
	 * @param values
	 *            value
	 * @return query
	 */
	public static SQLiteMasterQuery createOr(SQLiteMasterColumn column,
			String operation, Collection<String> values) {
		SQLiteMasterQuery query = createOr();
		for (String value : values) {
			query.add(column, operation, value);
		}
		return query;
	}

	/**
	 * Create an equality query with multiple values for a single column
	 * combined with an AND
	 * 
	 * @param column
	 *            column
	 * @param values
	 *            value
	 * @return query
	 */
	public static SQLiteMasterQuery createAnd(SQLiteMasterColumn column,
			Collection<String> values) {
		SQLiteMasterQuery query = createAnd();
		for (String value : values) {
			query.add(column, value);
		}
		return query;
	}

	/**
	 * Create a query with multiple values for a single column combined with an
	 * AND
	 * 
	 * @param column
	 *            column
	 * @param operation
	 *            operation
	 * @param values
	 *            value
	 * @return query
	 */
	public static SQLiteMasterQuery createAnd(SQLiteMasterColumn column,
			String operation, Collection<String> values) {
		SQLiteMasterQuery query = createAnd();
		for (String value : values) {
			query.add(column, operation, value);
		}
		return query;
	}

	/**
	 * Create a query to find views in the sql column referring to the table
	 * 
	 * @param tableName
	 *            table name
	 * @return query
	 */
	public static SQLiteMasterQuery createTableViewQuery(String tableName) {

		List<String> queries = new ArrayList<>();
		queries.add("%\"" + tableName + "\"%");
		queries.add("% " + tableName + " %");
		queries.add("%," + tableName + " %");
		queries.add("% " + tableName + ",%");
		queries.add("%," + tableName + ",%");
		queries.add("% " + tableName);
		queries.add("%," + tableName);

		return SQLiteMasterQuery.createOr(SQLiteMasterColumn.SQL, "LIKE",
				queries);
	}

}
