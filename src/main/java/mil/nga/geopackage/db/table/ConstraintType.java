package mil.nga.geopackage.db.table;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * Constraint types
 * 
 * @author osbornb
 * @since 3.3.0
 */
public enum ConstraintType {

	/**
	 * Primary key table and column constraint
	 */
	PRIMARY_KEY,

	/**
	 * Unique table and column constraint
	 */
	UNIQUE,

	/**
	 * Check table and column constraint
	 */
	CHECK,

	/**
	 * Foreign key table and column constraint
	 */
	FOREIGN_KEY,

	/**
	 * Not null column constraint
	 */
	NOT_NULL,

	/**
	 * Default column constraint
	 */
	DEFAULT,

	/**
	 * Collate column constraint
	 */
	COLLATE;

	/**
	 * Table constraints
	 */
	public static final Set<ConstraintType> TABLE_CONSTRAINTS = new LinkedHashSet<>();
	static {
		TABLE_CONSTRAINTS.add(PRIMARY_KEY);
		TABLE_CONSTRAINTS.add(UNIQUE);
		TABLE_CONSTRAINTS.add(CHECK);
		TABLE_CONSTRAINTS.add(FOREIGN_KEY);
	}

	/**
	 * Column constraints
	 */
	public static final Set<ConstraintType> COLUMN_CONSTRAINTS = new HashSet<>();
	static {
		COLUMN_CONSTRAINTS.add(PRIMARY_KEY);
		COLUMN_CONSTRAINTS.add(NOT_NULL);
		COLUMN_CONSTRAINTS.add(UNIQUE);
		COLUMN_CONSTRAINTS.add(CHECK);
		COLUMN_CONSTRAINTS.add(DEFAULT);
		COLUMN_CONSTRAINTS.add(COLLATE);
		COLUMN_CONSTRAINTS.add(FOREIGN_KEY);
	}

	/**
	 * Table constraint parsing lookup values
	 */
	private static final Map<String, ConstraintType> tableLookup = new HashMap<>();
	static {
		for (ConstraintType type : TABLE_CONSTRAINTS) {
			addLookups(tableLookup, type);
		}
	}

	/**
	 * Column constraint parsing lookup values
	 */
	private static final Map<String, ConstraintType> columnLookup = new HashMap<>();
	static {
		for (ConstraintType type : COLUMN_CONSTRAINTS) {
			addLookups(columnLookup, type);
		}
	}

	/**
	 * Add constraint lookup values
	 * 
	 * @param lookup
	 *            lookup map
	 * @param type
	 *            constraint type
	 */
	private static void addLookups(Map<String, ConstraintType> lookup,
			ConstraintType type) {
		String name = type.name();
		String[] parts = name.split("_");
		lookup.put(parts[0], type);
		if (parts.length > 0) {
			lookup.put(name.replaceAll("_", " "), type);
		}
	}

	/**
	 * Get a matching table constraint type from the value
	 * 
	 * @param value
	 *            table constraint name value
	 * @return constraint type or null
	 */
	public static ConstraintType getTableType(String value) {
		return tableLookup.get(value.toUpperCase());
	}

	/**
	 * Get a matching column constraint type from the value
	 * 
	 * @param value
	 *            column constraint name value
	 * @return constraint type or null
	 */
	public static ConstraintType getColumnType(String value) {
		return columnLookup.get(value.toUpperCase());
	}

	/**
	 * Get a matching constraint type from the value
	 * 
	 * @param value
	 *            constraint name value
	 * @return constraint type or null
	 */
	public static ConstraintType getType(String value) {
		ConstraintType type = getTableType(value);
		if (type == null) {
			type = getColumnType(value);
		}
		return type;
	}

}
