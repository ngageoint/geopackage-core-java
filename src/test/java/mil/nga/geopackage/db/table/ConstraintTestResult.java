package mil.nga.geopackage.db.table;

/**
 * Constraint test result
 * 
 * @author osbornb
 */
public class ConstraintTestResult {

	private TableConstraints constraints;
	private int primaryKeyCount = 0;
	private int uniqueCount = 0;
	private int checkCount = 0;
	private int foreignKeyCount = 0;

	/**
	 * Constructor
	 * 
	 * @param constraints
	 *            constraints
	 * @param primaryKeyCount
	 *            primary key count
	 * @param uniqueCount
	 *            unique count
	 * @param checkCount
	 *            check count
	 * @param foreignKeyCount
	 *            foreign key count
	 */
	public ConstraintTestResult(TableConstraints constraints,
			int primaryKeyCount, int uniqueCount, int checkCount,
			int foreignKeyCount) {
		this.constraints = constraints;
		this.primaryKeyCount = primaryKeyCount;
		this.uniqueCount = uniqueCount;
		this.checkCount = checkCount;
		this.foreignKeyCount = foreignKeyCount;
	}

	/**
	 * Get the constraints
	 * 
	 * @return constraints
	 */
	public TableConstraints getConstraints() {
		return constraints;
	}

	/**
	 * Get the count
	 * 
	 * @return count
	 */
	public int getCount() {
		return constraints.numTableConstraints();
	}

	/**
	 * Get the primary key count
	 * 
	 * @return primary key count
	 */
	public int getPrimaryKeyCount() {
		return primaryKeyCount;
	}

	/**
	 * Get the unique count
	 * 
	 * @return unique count
	 */
	public int getUniqueCount() {
		return uniqueCount;
	}

	/**
	 * Get the check count
	 * 
	 * @return check count
	 */
	public int getCheckCount() {
		return checkCount;
	}

	/**
	 * Get the foreign key count
	 * 
	 * @return foreign key count
	 */
	public int getForeignKeyCount() {
		return foreignKeyCount;
	}

}
