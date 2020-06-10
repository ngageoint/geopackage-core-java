package mil.nga.geopackage.user;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.db.CoreSQLUtils;
import mil.nga.geopackage.db.GeoPackageDataType;
import mil.nga.geopackage.db.table.ColumnConstraints;
import mil.nga.geopackage.db.table.Constraint;
import mil.nga.geopackage.db.table.ConstraintType;
import mil.nga.geopackage.db.table.RawConstraint;
import mil.nga.geopackage.db.table.TableColumn;

/**
 * Metadata about a single column from a user table
 * 
 * @author osbornb
 */
public abstract class UserColumn implements Comparable<UserColumn> {

	/**
	 * Logger
	 */
	private static final Logger log = Logger
			.getLogger(UserColumn.class.getName());

	/**
	 * User Column index value
	 * 
	 * @since 3.3.0
	 */
	public static final int NO_INDEX = -1;

	/**
	 * Column index
	 */
	private int index;

	/**
	 * Column name
	 */
	private String name;

	/**
	 * Max size
	 */
	private Long max;

	/**
	 * True if a not null column
	 */
	private boolean notNull;

	/**
	 * Default column value
	 */
	private Object defaultValue;

	/**
	 * True if a primary key column
	 */
	private boolean primaryKey;

	/**
	 * True if primary key is autoincrement
	 */
	private boolean autoincrement;

	/**
	 * Type
	 */
	private String type;

	/**
	 * Data type
	 */
	private GeoPackageDataType dataType;

	/**
	 * List of column constraints
	 */
	private final List<Constraint> constraints = new ArrayList<>();

	/**
	 * Constructor
	 * 
	 * @param index
	 *            column index
	 * @param name
	 *            column name
	 * @param dataType
	 *            data type
	 * @param max
	 *            max value
	 * @param notNull
	 *            not null flag
	 * @param defaultValue
	 *            default value
	 * @param primaryKey
	 *            primary key flag
	 * @param autoincrement
	 *            autoincrement flag
	 * @since 4.0.0
	 */
	protected UserColumn(int index, String name, GeoPackageDataType dataType,
			Long max, boolean notNull, Object defaultValue, boolean primaryKey,
			boolean autoincrement) {
		this(index, name, getTypeName(name, dataType), dataType, max, notNull,
				defaultValue, primaryKey, autoincrement);
	}

	/**
	 * Constructor
	 * 
	 * @param index
	 *            column index
	 * @param name
	 *            column name
	 * @param type
	 *            string type
	 * @param dataType
	 *            data type
	 * @param max
	 *            max value
	 * @param notNull
	 *            not null flag
	 * @param defaultValue
	 *            default value
	 * @param primaryKey
	 *            primary key flag
	 * @param autoincrement
	 *            autoincrement flag
	 * @since 4.0.0
	 */
	protected UserColumn(int index, String name, String type,
			GeoPackageDataType dataType, Long max, boolean notNull,
			Object defaultValue, boolean primaryKey, boolean autoincrement) {
		this.index = index;
		this.name = name;
		this.max = max;
		this.notNull = notNull;
		this.defaultValue = defaultValue;
		this.primaryKey = primaryKey;
		this.autoincrement = autoincrement;
		this.type = type;
		this.dataType = dataType;

		validateDataType(name, dataType);
		validateMax();

		addDefaultConstraints();
	}

	/**
	 * Constructor
	 * 
	 * @param tableColumn
	 *            table column
	 * @since 3.3.0
	 */
	protected UserColumn(TableColumn tableColumn) {
		this(tableColumn.getIndex(), tableColumn.getName(),
				tableColumn.getType(), tableColumn.getDataType(),
				tableColumn.getMax(),
				tableColumn.isNotNull() || tableColumn.isPrimarykey(),
				tableColumn.getDefaultValue(), tableColumn.isPrimarykey(),
				tableColumn.isPrimarykey() && UserTable.DEFAULT_AUTOINCREMENT);
	}

	/**
	 * Copy Constructor
	 * 
	 * @param userColumn
	 *            user column
	 * @since 3.3.0
	 */
	protected UserColumn(UserColumn userColumn) {
		this.index = userColumn.index;
		this.name = userColumn.name;
		this.max = userColumn.max;
		this.notNull = userColumn.notNull;
		this.defaultValue = userColumn.defaultValue;
		this.primaryKey = userColumn.primaryKey;
		this.autoincrement = userColumn.autoincrement;
		this.type = userColumn.type;
		this.dataType = userColumn.dataType;
		for (Constraint constraint : userColumn.constraints) {
			addConstraint(constraint.copy());
		}
	}

	/**
	 * Get the type name from the data type
	 * 
	 * @param name
	 *            column name
	 * @param dataType
	 *            data type
	 * @return type name
	 * @since 3.3.0
	 */
	protected static String getTypeName(String name,
			GeoPackageDataType dataType) {
		validateDataType(name, dataType);
		return dataType.name();
	}

	/**
	 * Validate the data type
	 * 
	 * @param name
	 *            column name
	 * 
	 * @param dataType
	 *            data type
	 * @since 3.3.0
	 */
	protected static void validateDataType(String name,
			GeoPackageDataType dataType) {
		if (dataType == null) {
			log.log(Level.SEVERE, "Column is missing a data type: " + name);
		}
	}

	/**
	 * Copy the column
	 * 
	 * @return copied column
	 * @since 3.3.0
	 */
	public abstract UserColumn copy();

	/**
	 * Check if the column has a valid index
	 * 
	 * @return true if has a valid index
	 * @since 3.3.0
	 */
	public boolean hasIndex() {
		return this.index > NO_INDEX;
	}

	/**
	 * Set the column index. Only allowed when {@link #hasIndex()} is false (
	 * {@link #getIndex()} is {@link #NO_INDEX}). Setting a valid index to an
	 * existing valid index does nothing.
	 * 
	 * @param index
	 *            column index
	 * @since 3.3.0
	 */
	public void setIndex(int index) {
		if (hasIndex()) {
			if (index != this.index) {
				throw new GeoPackageException(
						"User Column with a valid index may not be changed. Column Name: "
								+ name + ", Index: " + this.index
								+ ", Attempted Index: " + index);
			}
		} else {
			this.index = index;
		}
	}

	/**
	 * Reset the column index
	 * 
	 * @since 3.3.0
	 */
	public void resetIndex() {
		this.index = NO_INDEX;
	}

	/**
	 * Get the index
	 * 
	 * @return index
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * Set the name
	 * 
	 * @param name
	 *            column name
	 * @since 3.3.0
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Get the name
	 * 
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Determine if this column is named the provided name
	 * 
	 * @param name
	 *            column name
	 * @return true if named the provided name
	 * @since 3.0.1
	 */
	public boolean isNamed(String name) {
		return this.name.equals(name);
	}

	/**
	 * Determine if the column has a max value
	 * 
	 * @return true if has max value
	 * @since 3.3.0
	 */
	public boolean hasMax() {
		return max != null;
	}

	/**
	 * Set the max
	 * 
	 * @param max
	 *            max
	 * @since 3.3.0
	 */
	public void setMax(Long max) {
		this.max = max;
	}

	/**
	 * Get the max
	 * 
	 * @return max
	 */
	public Long getMax() {
		return max;
	}

	/**
	 * Set the not null flag
	 * 
	 * @param notNull
	 *            not null flag
	 * @since 3.3.0
	 */
	public void setNotNull(boolean notNull) {
		this.notNull = notNull;
	}

	/**
	 * Get the is not null flag
	 * 
	 * @return not null flag
	 */
	public boolean isNotNull() {
		return notNull;
	}

	/**
	 * Determine if the column has a default value
	 * 
	 * @return true if has default value
	 * @since 3.3.0
	 */
	public boolean hasDefaultValue() {
		return defaultValue != null;
	}

	/**
	 * Set the default value
	 * 
	 * @param defaultValue
	 *            default value
	 * @since 3.3.0
	 */
	public void setDefaultValue(Object defaultValue) {
		this.defaultValue = defaultValue;
	}

	/**
	 * Get the default value
	 * 
	 * @return default value
	 */
	public Object getDefaultValue() {
		return defaultValue;
	}

	/**
	 * Set the primary key flag
	 * 
	 * @param primaryKey
	 *            primary key flag
	 * @since 3.3.0
	 */
	public void setPrimaryKey(boolean primaryKey) {
		this.primaryKey = primaryKey;
	}

	/**
	 * Get the primary key flag
	 * 
	 * @return primary key flag
	 */
	public boolean isPrimaryKey() {
		return primaryKey;
	}

	/**
	 * Set the autoincrement flag
	 * 
	 * @param autoincrement
	 *            autoincrement flag
	 * @since 4.0.0
	 */
	public void setAutoincrement(boolean autoincrement) {
		this.autoincrement = autoincrement;
	}

	/**
	 * Get the autoincrement flag
	 * 
	 * @return autoincrement flag
	 * @since 4.0.0
	 */
	public boolean isAutoincrement() {
		return autoincrement;
	}

	/**
	 * Set the data type
	 * 
	 * @param dataType
	 *            data type
	 * @since 3.3.0
	 */
	public void setDataType(GeoPackageDataType dataType) {
		this.dataType = dataType;
	}

	/**
	 * Get the data type
	 * 
	 * @return data type
	 */
	public GeoPackageDataType getDataType() {
		return dataType;
	}

	/**
	 * Set the database type
	 * 
	 * @param type
	 *            database type
	 * @since 3.3.0
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Get the database type
	 * 
	 * @return type
	 */
	public String getType() {
		return type;
	}

	/**
	 * Check if has constraints
	 * 
	 * @return true if has constraints
	 * @since 3.3.0
	 */
	public boolean hasConstraints() {
		return !constraints.isEmpty();
	}

	/**
	 * Get the constraints
	 * 
	 * @return constraints
	 * @since 3.3.0
	 */
	public List<Constraint> getConstraints() {
		return constraints;
	}

	/**
	 * Clear the constraints
	 * 
	 * @return cleared constraints
	 * @since 3.3.0
	 */
	public List<Constraint> clearConstraints() {
		List<Constraint> constraintsCopy = new ArrayList<>(constraints);
		constraints.clear();
		return constraintsCopy;
	}

	/**
	 * Add the default constraints that are enabled (not null, default value,
	 * primary key) from the column properties
	 * 
	 * @since 3.3.0
	 */
	public void addDefaultConstraints() {
		if (isNotNull()) {
			addNotNullConstraint();
		}
		if (hasDefaultValue()) {
			addDefaultValueConstraint(getDefaultValue());
		}
		if (isPrimaryKey()) {
			addPrimaryKeyConstraint();
		}
		if (isAutoincrement()) {
			addAutoincrementConstraint();
		}
	}

	/**
	 * Add a constraint
	 * 
	 * @param constraint
	 *            constraint
	 * @since 3.3.0
	 */
	public void addConstraint(Constraint constraint) {
		constraints.add(constraint);
	}

	/**
	 * Add a constraint
	 * 
	 * @param constraint
	 *            constraint
	 * @since 3.3.0
	 */
	public void addConstraint(String constraint) {
		constraints.add(new RawConstraint(constraint));
	}

	/**
	 * Add constraints
	 * 
	 * @param constraints
	 *            constraints
	 * @since 3.3.0
	 */
	public void addConstraints(Collection<Constraint> constraints) {
		for (Constraint constraint : constraints) {
			addConstraint(constraint);
		}
	}

	/**
	 * Add constraints
	 * 
	 * @param constraints
	 *            constraints
	 * @since 3.3.0
	 */
	public void addConstraints(ColumnConstraints constraints) {
		addConstraints(constraints.getConstraints());
	}

	/**
	 * Add a not null constraint
	 * 
	 * @since 3.3.0
	 */
	public void addNotNullConstraint() {
		setNotNull(true);
		addConstraint("NOT NULL");
	}

	/**
	 * Add a default value constraint
	 * 
	 * @param defaultValue
	 *            default value
	 * 
	 * @since 3.3.0
	 */
	public void addDefaultValueConstraint(Object defaultValue) {
		setDefaultValue(defaultValue);
		addConstraint("DEFAULT " + CoreSQLUtils.columnDefaultValue(this));
	}

	/**
	 * Add a primary key constraint
	 * 
	 * @since 3.3.0
	 */
	public void addPrimaryKeyConstraint() {
		setPrimaryKey(true);
		addConstraint("PRIMARY KEY");
	}

	/**
	 * Add an autoincrement constraint
	 * 
	 * @since 4.0.0
	 */
	public void addAutoincrementConstraint() {
		setAutoincrement(true);
		addConstraint("AUTOINCREMENT");
	}

	/**
	 * Add a unique constraint
	 * 
	 * @since 3.3.0
	 */
	public void addUniqueConstraint() {
		addConstraint("UNIQUE");
	}

	/**
	 * Build the SQL for the constraint
	 * 
	 * @param constraint
	 *            constraint
	 * @return SQL or null
	 * @since 4.0.0
	 */
	public String buildConstraintSql(Constraint constraint) {
		String sql = null;
		if (UserTable.DEFAULT_PK_NOT_NULL || !isPrimaryKey()
				|| constraint.getType() != ConstraintType.NOT_NULL) {
			sql = constraint.buildSql();
		}
		return sql;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * Sort by index
	 */
	@Override
	public int compareTo(UserColumn another) {
		return index - another.index;
	}

	/**
	 * Validate that if max is set, the data type is text or blob
	 */
	private void validateMax() {

		if (max != null) {
			if (dataType == null) {
				log.log(Level.SEVERE,
						"Column max set on a column without a data type. column: "
								+ name + ", max: " + max);
			} else if (dataType != GeoPackageDataType.TEXT
					&& dataType != GeoPackageDataType.BLOB) {
				throw new GeoPackageException(
						"Column max is only supported for "
								+ GeoPackageDataType.TEXT.name() + " and "
								+ GeoPackageDataType.BLOB.name()
								+ " columns. column: " + name + ", max: " + max
								+ ", type: " + dataType.name());
			}
		}

	}

}
