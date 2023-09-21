package mil.nga.geopackage.dgiwg;

import java.util.Arrays;
import java.util.List;

/**
 * DGIWG (Defence Geospatial Information Working Group) validation error
 * 
 * @author osbornb
 * @since 6.6.0
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
	 * Requirement
	 */
	private DGIWGRequirement requirement = null;

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
	 * @param requirement
	 *            requirement
	 */
	public DGIWGValidationError(String table, String column, String value,
			String constraint, DGIWGRequirement requirement) {
		this(table, value, constraint, requirement);
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
	 * @param requirement
	 *            requirement
	 */
	public DGIWGValidationError(String table, String column, Number value,
			String constraint, DGIWGRequirement requirement) {
		this(table, column, value != null ? value.toString() : null, constraint,
				requirement);
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
	 * @param requirement
	 *            requirement
	 */
	public DGIWGValidationError(String table, String column, Number value,
			Number constraint, DGIWGRequirement requirement) {
		this(table, column, value,
				constraint != null ? constraint.toString() : null, requirement);
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
	 * @param requirement
	 *            requirement
	 */
	public DGIWGValidationError(String table, String value, String constraint,
			DGIWGRequirement requirement) {
		this(value, constraint, requirement);
		this.table = table;
	}

	/**
	 * Constructor
	 * 
	 * @param value
	 *            error causing value
	 * @param constraint
	 *            constraint or error description
	 * @param requirement
	 *            requirement
	 */
	public DGIWGValidationError(String value, String constraint,
			DGIWGRequirement requirement) {
		this(constraint, requirement);
		this.value = value;
	}

	/**
	 * Constructor
	 * 
	 * @param constraint
	 *            constraint or error description
	 * @param requirement
	 *            requirement
	 */
	public DGIWGValidationError(String constraint,
			DGIWGRequirement requirement) {
		this.constraint = constraint;
		this.requirement = requirement;
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
	 * @param requirement
	 *            requirement
	 * @param primaryKeys
	 *            primary keys
	 */
	public DGIWGValidationError(String table, String value, String constraint,
			DGIWGRequirement requirement, DGIWGValidationKey... primaryKeys) {
		this(table, null, value, constraint, requirement, primaryKeys);
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
	 * @param requirement
	 *            requirement
	 * @param primaryKeys
	 *            primary keys
	 */
	public DGIWGValidationError(String table, String column, String value,
			String constraint, DGIWGRequirement requirement,
			DGIWGValidationKey... primaryKeys) {
		this(table, column, value, constraint, requirement);
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
	 * @param requirement
	 *            requirement
	 * @param primaryKeys
	 *            primary keys
	 */
	public DGIWGValidationError(String table, String column, Number value,
			String constraint, DGIWGRequirement requirement,
			DGIWGValidationKey... primaryKeys) {
		this(table, column, value != null ? value.toString() : null, constraint,
				requirement, primaryKeys);
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
	 * @param requirement
	 *            requirement
	 * @param primaryKeys
	 *            primary keys
	 */
	public DGIWGValidationError(String table, String column, Number value,
			Number constraint, DGIWGRequirement requirement,
			DGIWGValidationKey... primaryKeys) {
		this(table, column, value,
				constraint != null ? constraint.toString() : null, requirement,
				primaryKeys);
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
	 * Get the requirement
	 * 
	 * @return requirement
	 */
	public DGIWGRequirement getRequirement() {
		return requirement;
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
			toString.append("Value: ").append(DGIWGGeoPackageUtils
					.wrapIfEmptyOrContainsWhitespace(value));
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
		if (constraint != null) {
			if (toString.length() > 0) {
				toString.append(", ");
			}
			toString.append("Constraint: ").append(DGIWGGeoPackageUtils
					.wrapIfEmptyOrContainsWhitespace(constraint));
		}
		if (requirement != null) {
			if (toString.length() > 0) {
				toString.append(", ");
			}
			toString.append("Requirement: [").append(requirement).append("]");
		}
		return toString.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((column == null) ? 0 : column.hashCode());
		result = prime * result
				+ ((constraint == null) ? 0 : constraint.hashCode());
		result = prime * result
				+ ((primaryKeys == null) ? 0 : primaryKeys.hashCode());
		result = prime * result
				+ ((requirement == null) ? 0 : requirement.hashCode());
		result = prime * result + ((table == null) ? 0 : table.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DGIWGValidationError other = (DGIWGValidationError) obj;
		if (column == null) {
			if (other.column != null)
				return false;
		} else if (!column.equals(other.column))
			return false;
		if (constraint == null) {
			if (other.constraint != null)
				return false;
		} else if (!constraint.equals(other.constraint))
			return false;
		if (primaryKeys == null) {
			if (other.primaryKeys != null)
				return false;
		} else if (!primaryKeys.equals(other.primaryKeys))
			return false;
		if (requirement != other.requirement)
			return false;
		if (table == null) {
			if (other.table != null)
				return false;
		} else if (!table.equals(other.table))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

}
