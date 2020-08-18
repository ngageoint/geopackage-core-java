package mil.nga.geopackage.db.table;

import java.util.Collection;

/**
 * Column Constraints
 * 
 * @author osbornb
 * @since 3.3.0
 */
public class ColumnConstraints {

	/**
	 * Column name
	 */
	private String name;

	/**
	 * Column constraints
	 */
	private Constraints constraints = new Constraints();

	/**
	 * Constructor
	 * 
	 * @param name
	 *            column name
	 */
	public ColumnConstraints(String name) {
		this.name = name;
	}

	/**
	 * Get the column name
	 * 
	 * @return column name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the column name
	 * 
	 * @param name
	 *            column name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Add a constraint
	 * 
	 * @param constraint
	 *            constraint
	 */
	public void addConstraint(Constraint constraint) {
		constraints.add(constraint);
	}

	/**
	 * Add constraints
	 * 
	 * @param constraints
	 *            constraints
	 */
	public void addConstraints(Collection<Constraint> constraints) {
		this.constraints.add(constraints);
	}

	/**
	 * Add constraints
	 * 
	 * @param constraints
	 *            constraints
	 * @since 4.0.1
	 */
	public void addConstraints(Constraints constraints) {
		this.constraints.add(constraints);
	}

	/**
	 * Get the constraints
	 * 
	 * @return constraints
	 * @since 4.0.1
	 */
	public Constraints getConstraints() {
		return constraints;
	}

	/**
	 * Get the constraint at the index
	 * 
	 * @param index
	 *            constraint index
	 * @return constraint
	 */
	public Constraint getConstraint(int index) {
		return constraints.get(index);
	}

	/**
	 * Get the number of constraints
	 * 
	 * @return constraints count
	 */
	public int numConstraints() {
		return constraints.size();
	}

	/**
	 * Add constraints
	 * 
	 * @param constraints
	 *            constraints
	 */
	public void addConstraints(ColumnConstraints constraints) {
		if (constraints != null) {
			addConstraints(constraints.getConstraints());
		}
	}

	/**
	 * Check if there are constraints
	 * 
	 * @return true if has constraints
	 */
	public boolean hasConstraints() {
		return constraints.has();
	}

}
