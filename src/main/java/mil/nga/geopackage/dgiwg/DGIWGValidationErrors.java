package mil.nga.geopackage.dgiwg;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * DGIWG (Defence Geospatial Information Working Group) validation errors
 * 
 * @author osbornb
 * @since 6.6.0
 */
public class DGIWGValidationErrors implements Iterable<DGIWGValidationError> {

	/**
	 * Errors
	 */
	private final Set<DGIWGValidationError> errors = new LinkedHashSet<>();

	/**
	 * Constructor
	 */
	public DGIWGValidationErrors() {

	}

	/**
	 * Constructor
	 * 
	 * @param error
	 *            validation error
	 */
	public DGIWGValidationErrors(DGIWGValidationError error) {
		add(error);
	}

	/**
	 * Constructor
	 * 
	 * @param errors
	 *            validation errors
	 */
	public DGIWGValidationErrors(Collection<DGIWGValidationError> errors) {
		add(errors);
	}

	/**
	 * Constructor
	 * 
	 * @param errors
	 *            validation errors
	 */
	public DGIWGValidationErrors(DGIWGValidationErrors errors) {
		add(errors);
	}

	/**
	 * Add a validation error
	 * 
	 * @param error
	 *            validation error
	 */
	public void add(DGIWGValidationError error) {
		errors.add(error);
	}

	/**
	 * Add validation errors
	 * 
	 * @param errors
	 *            validation errors
	 */
	public void add(Collection<DGIWGValidationError> errors) {
		this.errors.addAll(errors);
	}

	/**
	 * Add validation errors
	 * 
	 * @param errors
	 *            validation errors
	 */
	public void add(DGIWGValidationErrors errors) {
		add(errors.getErrors());
	}

	/**
	 * Check if valid
	 * 
	 * @return true if valid
	 */
	public boolean isValid() {
		return errors.isEmpty();
	}

	/**
	 * Has errors
	 * 
	 * @return true if has errors
	 */
	public boolean hasErrors() {
		return !isValid();
	}

	/**
	 * Get the number of errors
	 * 
	 * @return error count
	 */
	public int numErrors() {
		return errors.size();
	}

	/**
	 * Get the validation errors
	 * 
	 * @return errors
	 */
	public List<DGIWGValidationError> getErrors() {
		return new ArrayList<>(errors);
	}

	/**
	 * Get the validation error at the index
	 * 
	 * @param index
	 *            error index
	 * @return error
	 */
	public DGIWGValidationError getError(int index) {
		return getErrors().get(index);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<DGIWGValidationError> iterator() {
		return errors.iterator();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder value = new StringBuilder();
		int countLength = length(numErrors());
		int number = 0;
		for (DGIWGValidationError error : this) {
			if (number > 0) {
				value.append("\n");
			}
			value.append(++number);
			int padding = countLength - length(number);
			for (int i = 0; i < padding; i++) {
				value.append(" ");
			}
			value.append(" - ");
			value.append(error);
		}
		return value.toString();
	}

	/**
	 * Get the string length of the number
	 * 
	 * @param number
	 *            number
	 * @return string length
	 */
	private static int length(int number) {
		return (int) (Math.log10(number) + 1);
	}

}
