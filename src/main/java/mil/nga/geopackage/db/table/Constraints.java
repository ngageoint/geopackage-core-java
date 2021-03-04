package mil.nga.geopackage.db.table;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * User table or column constraints
 * 
 * @author osbornb
 * @since 5.0.0
 */
public class Constraints {

	/**
	 * Constraints
	 */
	private final List<Constraint> constraints = new ArrayList<>();

	/**
	 * Type Constraints
	 */
	private final Map<ConstraintType, List<Constraint>> typedContraints = new HashMap<>();

	/**
	 * Constructor
	 */
	public Constraints() {

	}

	/**
	 * Copy Constructor
	 * 
	 * @param constraints
	 *            constraints
	 */
	public Constraints(Constraints constraints) {
		for (Constraint constraint : constraints.constraints) {
			add(constraint.copy());
		}
	}

	/**
	 * Add constraint
	 * 
	 * @param constraint
	 *            constraint
	 */
	public void add(Constraint constraint) {

		int insertLocation = Collections.binarySearch(constraints, constraint);
		if (insertLocation < 0) {
			insertLocation = -(insertLocation + 1);
		}
		constraints.add(insertLocation, constraint);

		List<Constraint> typeConstraints = typedContraints
				.get(constraint.getType());
		if (typeConstraints == null) {
			typeConstraints = new ArrayList<>();
			typedContraints.put(constraint.getType(), typeConstraints);
		}
		typeConstraints.add(constraint);
	}

	/**
	 * Add constraints
	 * 
	 * @param constraints
	 *            constraints
	 */
	public void add(Collection<Constraint> constraints) {
		for (Constraint constraint : constraints) {
			add(constraint);
		}
	}

	/**
	 * Add constraints
	 * 
	 * @param constraints
	 *            constraints
	 */
	public void add(Constraints constraints) {
		add(constraints.all());
	}

	/**
	 * Check if has constraints
	 * 
	 * @return true if has constraints
	 */
	public boolean has() {
		return !constraints.isEmpty();
	}

	/**
	 * Check if has constraints of the provided type
	 * 
	 * @param type
	 *            constraint type
	 * @return true if has constraints
	 */
	public boolean has(ConstraintType type) {
		return size(type) > 0;
	}

	/**
	 * Get the number of constraints
	 * 
	 * @return size
	 */
	public int size() {
		return constraints.size();
	}

	/**
	 * Get the number of constraints of the provided type
	 * 
	 * @param type
	 *            constraint type
	 * @return size
	 */
	public int size(ConstraintType type) {
		return get(type).size();
	}

	/**
	 * Get the constraints
	 * 
	 * @return constraints
	 */
	public List<Constraint> all() {
		return constraints;
	}

	/**
	 * Get the constraint at the index
	 * 
	 * @param index
	 *            constraint index
	 * @return constraint
	 */
	public Constraint get(int index) {
		return constraints.get(index);
	}

	/**
	 * Get the constraints of the provided type
	 * 
	 * @param type
	 *            constraint type
	 * @return constraints
	 */
	public List<Constraint> get(ConstraintType type) {
		List<Constraint> constraints = typedContraints.get(type);
		if (constraints == null) {
			constraints = new ArrayList<>();
		}
		return constraints;
	}

	/**
	 * Clear the constraints
	 * 
	 * @return cleared constraints
	 */
	public List<Constraint> clear() {
		List<Constraint> constraintsCopy = new ArrayList<>(constraints);
		constraints.clear();
		typedContraints.clear();
		return constraintsCopy;
	}

	/**
	 * Clear the constraints of the provided type
	 * 
	 * @param type
	 *            constraint type
	 * @return cleared constraints
	 */
	public List<Constraint> clear(ConstraintType type) {
		List<Constraint> typedConstraints = typedContraints.remove(type);
		if (typedConstraints == null) {
			typedConstraints = new ArrayList<>();
		} else if (!typedConstraints.isEmpty()) {
			Iterator<Constraint> constraintsIterator = constraints.iterator();
			while (constraintsIterator.hasNext()) {
				if (constraintsIterator.next().getType() == type) {
					constraintsIterator.remove();
				}
			}
		}
		return typedConstraints;
	}

	/**
	 * Copy the constraints
	 * 
	 * @return constraints
	 */
	public Constraints copy() {
		return new Constraints(this);
	}

}
