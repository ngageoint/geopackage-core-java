package mil.nga.geopackage.user;

import java.util.ArrayList;
import java.util.List;

/**
 * User table unique constraint for one or more columns
 * 
 * @param <TColumn>
 *            column type
 * 
 * @author osbornb
 */
public class UserUniqueConstraint<TColumn extends UserColumn> {

	/**
	 * Columns included in the unique constraint
	 */
	private final List<TColumn> columns = new ArrayList<TColumn>();

	/**
	 * Constructor
	 */
	public UserUniqueConstraint() {

	}

	/**
	 * Constructor
	 * 
	 * @param columns
	 *            columns
	 */
	public UserUniqueConstraint(
			@SuppressWarnings("unchecked") TColumn... columns) {
		for (TColumn column : columns) {
			add(column);
		}
	}

	/**
	 * Copy Constructor
	 * 
	 * @param userUniqueConstraint
	 *            user unique constraint
	 * @since 3.2.1
	 */
	public UserUniqueConstraint(
			UserUniqueConstraint<TColumn> userUniqueConstraint) {
		for (TColumn column : userUniqueConstraint.columns) {
			@SuppressWarnings("unchecked")
			TColumn copiedColumn = (TColumn) column.copy();
			add(copiedColumn);
		}
	}

	/**
	 * Copy the user unique constraint
	 * 
	 * @return copied column
	 * @since 3.2.1
	 */
	public UserUniqueConstraint<TColumn> copy() {
		return new UserUniqueConstraint<TColumn>(this);
	}

	/**
	 * Add a column
	 * 
	 * @param column
	 *            column
	 */
	public void add(TColumn column) {
		columns.add(column);
	}

	/**
	 * Get the columns
	 * 
	 * @return columns
	 */
	public List<TColumn> getColumns() {
		return columns;
	}

}
