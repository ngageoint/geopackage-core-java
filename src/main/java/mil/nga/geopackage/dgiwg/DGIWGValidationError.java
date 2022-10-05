package mil.nga.geopackage.dgiwg;

import java.util.Arrays;
import java.util.List;

/**
 * DGIWG (Defence Geospatial Information Working Group) validation error
 * 
 * @author osbornb
 * @since 6.5.1
 */
public class DGIWGValidationError {

	/**
	 * Table name
	 */
	private String table = null;

	/**
	 * Column name
	 */
	private String column = null;

	/**
	 * Error causing value
	 */
	private String value = null;

	/**
	 * Constraint
	 */
	private String constraint = null;

	/**
	 * Row primary keys
	 */
	private List<DGIWGValidationKey> primaryKeys = null;

	/**
	 * Constructor
	 * 
	 * @param table
	 *            table name
	 * @param column
	 *            column name
	 * @param value
	 *            error causing value
	 * @param constraint
	 *            constraint or error description
	 */
	public DGIWGValidationError(String table, String column, String value,
			String constraint) {
		this(table, value, constraint);
		this.column = column;
	}

	/**
	 * Constructor
	 * 
	 * @param table
	 *            table name
	 * @param column
	 *            column name
	 * @param value
	 *            error causing value
	 * @param constraint
	 *            constraint or error description
	 */
	public DGIWGValidationError(String table, String column, Number value,
			String constraint) {
		this(table, column, value != null ? value.toString() : null,
				constraint);
	}

	/**
	 * Constructor
	 * 
	 * @param table
	 *            table name
	 * @param column
	 *            column name
	 * @param value
	 *            error causing value
	 * @param constraint
	 *            constraint value
	 */
	public DGIWGValidationError(String table, String column, Number value,
			Number constraint) {
		this(table, column, value,
				constraint != null ? constraint.toString() : null);
	}

	/**
	 * Constructor
	 * 
	 * @param table
	 *            table name
	 * @param value
	 *            error causing value
	 * @param constraint
	 *            constraint or error description
	 */
	public DGIWGValidationError(String table, String value, String constraint) {
		this(value, constraint);
		this.table = table;
	}

	/**
	 * Constructor
	 * 
	 * @param value
	 *            error causing value
	 * @param constraint
	 *            constraint or error description
	 */
	public DGIWGValidationError(String value, String constraint) {
		this(constraint);
		this.value = value;
	}

	/**
	 * Constructor
	 * 
	 * @param constraint
	 *            constraint or error description
	 */
	public DGIWGValidationError(String constraint) {
		this.constraint = constraint;
	}

	/**
	 * Constructor
	 * 
	 * @param table
	 *            table name
	 * @param value
	 *            error causing value
	 * @param constraint
	 *            constraint or error description
	 * @param primaryKeys
	 *            primary keys
	 */
	public DGIWGValidationError(String table, String value, String constraint,
			DGIWGValidationKey... primaryKeys) {
		this(table, null, value, constraint, primaryKeys);
	}

	/**
	 * Constructor
	 * 
	 * @param table
	 *            table name
	 * @param column
	 *            column name
	 * @param value
	 *            error causing value
	 * @param constraint
	 *            constraint or error description
	 * @param primaryKeys
	 *            primary keys
	 */
	public DGIWGValidationError(String table, String column, String value,
			String constraint, DGIWGValidationKey... primaryKeys) {
		this(table, column, value, constraint);
		if (primaryKeys != null && primaryKeys.length > 0
				&& primaryKeys[0] != null) {
			this.primaryKeys = Arrays.asList(primaryKeys);
		}
	}

	/**
	 * Constructor
	 * 
	 * @param table
	 *            table name
	 * @param column
	 *            column name
	 * @param value
	 *            error causing value
	 * @param constraint
	 *            constraint or error description
	 * @param primaryKeys
	 *            primary keys
	 */
	public DGIWGValidationError(String table, String column, Number value,
			String constraint, DGIWGValidationKey... primaryKeys) {
		this(table, column, value != null ? value.toString() : null, constraint,
				primaryKeys);
	}

	/**
	 * Constructor
	 * 
	 * @param table
	 *            table name
	 * @param column
	 *            column name
	 * @param value
	 *            error causing value
	 * @param constraint
	 *            constraint or error description
	 * @param primaryKeys
	 *            primary keys
	 */
	public DGIWGValidationError(String table, String column, Number value,
			Number constraint, DGIWGValidationKey... primaryKeys) {
		this(table, column, value,
				constraint != null ? constraint.toString() : null, primaryKeys);
	}

	/**
	 * Get the table
	 * 
	 * @return table
	 */
	public String getTable() {
		return table;
	}

	/**
	 * Get the column
	 * 
	 * @return column
	 */
	public String getColumn() {
		return column;
	}

	/**
	 * Get the error causing value
	 * 
	 * @return value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Get the constraint or error description
	 * 
	 * @return constraint
	 */
	public String getConstraint() {
		return constraint;
	}

	/**
	 * Get row primary keys
	 * 
	 * @return primary keys
	 */
	public List<DGIWGValidationKey> getPrimaryKeys() {
		return primaryKeys;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder toString = new StringBuilder();
		if (table != null) {
			toString.append("Table: ").append(table);
		}
		if (column != null) {
			if (toString.length() > 0) {
				toString.append(", ");
			}
			toString.append("Column: ").append(column);
		}
		if (value != null) {
			if (toString.length() > 0) {
				toString.append(", ");
			}
			toString.append("Value: ").append(value);
		}
		if (primaryKeys != null) {
			for (DGIWGValidationKey key : primaryKeys) {
				if (!key.getColumn().equalsIgnoreCase(column)) {
					if (toString.length() > 0) {
						toString.append(", ");
					}
					toString.append(key);
				}
			}
		}
		if (value != null) {
			if (toString.length() > 0) {
				toString.append(", ");
			}
			toString.append("Constraint: ").append(constraint);
		}
		return toString.toString();
	}

}
