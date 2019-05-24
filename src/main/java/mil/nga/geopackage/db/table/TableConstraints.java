package mil.nga.geopackage.db.table;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Table Constraints including column constraint
 * 
 * @author osbornb
 * @since 3.3.0
 */
public class TableConstraints {

	/**
	 * Table constraints
	 */
	private List<Constraint> constraints = new ArrayList<>();

	/**
	 * Column constraints
	 */
	private Map<String, ColumnConstraints> columnConstraints = new LinkedHashMap<>();

	/**
	 * Constructor
	 */
	public TableConstraints() {

	}

	/**
	 * Add a table constraint
	 * 
	 * @param constraint
	 *            constraint
	 */
	public void addTableConstraint(Constraint constraint) {
		constraints.add(constraint);
	}

	/**
	 * Add table constraints
	 * 
	 * @param constraints
	 *            constraints
	 */
	public void addTableConstraints(Collection<Constraint> constraints) {
		this.constraints.addAll(constraints);
	}

	/**
	 * Get the table constraints
	 * 
	 * @return table constraints
	 */
	public List<Constraint> getTableConstraints() {
		return constraints;
	}

	/**
	 * Get the table constraint at the index
	 * 
	 * @param index
	 *            constraint index
	 * @return table constraint
	 */
	public Constraint getTableConstraint(int index) {
		return constraints.get(index);
	}

	/**
	 * Get the number of table constraints
	 * 
	 * @return table constraints count
	 */
	public int numTableConstraints() {
		return constraints.size();
	}

	/**
	 * Add a column constraint
	 * 
	 * @param columnName
	 *            column name
	 * @param constraint
	 *            constraint
	 */
	public void addColumnConstraint(String columnName, Constraint constraint) {
		getOrCreateColumnConstraints(columnName).addConstraint(constraint);
	}

	/**
	 * Add column constraints
	 * 
	 * @param columnName
	 *            column name
	 * @param constraints
	 *            constraints
	 */
	public void addColumnConstraints(String columnName,
			Collection<Constraint> constraints) {
		getOrCreateColumnConstraints(columnName).addConstraints(constraints);
	}

	/**
	 * Add column constraints
	 * 
	 * @param constraints
	 *            constraints
	 */
	public void addColumnConstraints(ColumnConstraints constraints) {
		getOrCreateColumnConstraints(constraints.getName())
				.addConstraints(constraints);
	}

	/**
	 * Get or create the column constraints for the column name
	 * 
	 * @param columnName
	 *            column name
	 * @return column constraints
	 */
	private ColumnConstraints getOrCreateColumnConstraints(String columnName) {
		ColumnConstraints constraints = columnConstraints.get(columnName);
		if (constraints == null) {
			constraints = new ColumnConstraints(columnName);
			columnConstraints.put(columnName, constraints);
		}
		return constraints;
	}

	/**
	 * Add column constraints
	 * 
	 * @param constraints
	 *            column constraints
	 */
	public void addColumnConstraints(
			Map<String, ColumnConstraints> constraints) {
		addColumnConstraints(constraints.values());
	}

	/**
	 * Add column constraints
	 * 
	 * @param constraints
	 *            column constraints
	 */
	public void addColumnConstraints(
			Collection<ColumnConstraints> constraints) {
		for (ColumnConstraints columnConstraints : constraints) {
			addColumnConstraints(columnConstraints);
		}
	}

	/**
	 * Get the column constraints
	 * 
	 * @return column constraints
	 */
	public Map<String, ColumnConstraints> getColumnConstraints() {
		return columnConstraints;
	}

	/**
	 * Get the column names with constraints
	 * 
	 * @return column names
	 */
	public Set<String> getColumnsWithConstraints() {
		return columnConstraints.keySet();
	}

	/**
	 * Get the column constraints
	 * 
	 * @param columnName
	 *            column name
	 * @return constraints
	 */
	public ColumnConstraints getColumnConstraints(String columnName) {
		return columnConstraints.get(columnName);
	}

	/**
	 * Get the column constraint at the index
	 * 
	 * @param columnName
	 *            column name
	 * @param index
	 *            constraint index
	 * @return column constraint
	 */
	public Constraint getColumnConstraint(String columnName, int index) {
		Constraint constraint = null;
		ColumnConstraints columnConstraints = getColumnConstraints(columnName);
		if (columnConstraints != null) {
			constraint = columnConstraints.getConstraint(index);
		}
		return constraint;
	}

	/**
	 * Get the number of column constraints for the column name
	 * 
	 * @param columnName
	 *            column name
	 * @return column constraints count
	 */
	public int numColumnConstraints(String columnName) {
		int count = 0;
		ColumnConstraints columnConstraints = getColumnConstraints(columnName);
		if (columnConstraints != null) {
			count = columnConstraints.numConstraints();
		}
		return count;
	}

	/**
	 * Add table constraints
	 * 
	 * @param constraints
	 *            table constraints
	 */
	public void addConstraints(TableConstraints constraints) {
		if (constraints != null) {
			addTableConstraints(constraints.getTableConstraints());
			addColumnConstraints(constraints.getColumnConstraints());
		}
	}

	/**
	 * Check if there are constraints
	 * 
	 * @return true if has constraints
	 */
	public boolean hasConstraints() {
		return hasTableConstraints() || hasColumnConstraints();
	}

	/**
	 * Check if there are table constraints
	 * 
	 * @return true if has table constraints
	 */
	public boolean hasTableConstraints() {
		return !constraints.isEmpty();
	}

	/**
	 * Check if there are column constraints
	 * 
	 * @return true if has column constraints
	 */
	public boolean hasColumnConstraints() {
		return !columnConstraints.isEmpty();
	}

	/**
	 * Check if there are column constraints for the column name
	 * 
	 * @param columnName
	 *            column name
	 * @return true if has column constraints
	 */
	public boolean hasColumnConstraints(String columnName) {
		return numColumnConstraints(columnName) > 0;
	}

}
