package mil.nga.geopackage.user;

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
import mil.nga.geopackage.db.table.Constraints;
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
	 * Not Null Constraint Order
	 */
	public static final int NOT_NULL_CONSTRAINT_ORDER = 1;

	/**
	 * Default Value Constraint Order
	 */
	public static final int DEFAULT_VALUE_CONSTRAINT_ORDER = 2;

	/**
	 * Primary Key Constraint Order
	 */
	public static final int PRIMARY_KEY_CONSTRAINT_ORDER = 3;

	/**
	 * Autoincrement Constraint Order
	 */
	public static final int AUTOINCREMENT_CONSTRAINT_ORDER = 4;

	/**
	 * Unique Constraint Order
	 */
	public static final int UNIQUE_CONSTRAINT_ORDER = 5;

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
	 * True if unique column
	 */
	private boolean unique;

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
	private final Constraints constraints;

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
		this.constraints = new Constraints();

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
		this.constraints = userColumn.constraints.copy();
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
		if (this.notNull != notNull) {
			if (notNull) {
				addNotNullConstraint();
			} else {
				removeNotNullConstraint();
			}
		}
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
		removeDefaultValueConstraint();
		if (defaultValue != null) {
			addDefaultValueConstraint(defaultValue);
		}
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
		if (this.primaryKey != primaryKey) {
			if (primaryKey) {
				addPrimaryKeyConstraint();
			} else {
				removeAutoincrementConstraint();
				removePrimaryKeyConstraint();
			}
		}
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
		if (this.autoincrement != autoincrement) {
			if (autoincrement) {
				addAutoincrementConstraint();
			} else {
				removeAutoincrementConstraint();
			}
		}
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
	 * Set the unique flag
	 * 
	 * @param unique
	 *            unique flag
	 * @since 5.0.0
	 */
	public void setUnique(boolean unique) {
		if (this.unique != unique) {
			if (unique) {
				addUniqueConstraint();
			} else {
				removeUniqueConstraint();
			}
		}
		this.unique = unique;
	}

	/**
	 * Get the unique flag
	 * 
	 * @return unique flag
	 * @since 5.0.0
	 */
	public boolean isUnique() {
		return unique;
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
		return constraints.has();
	}

	/**
	 * Check if has constraints of the provided type
	 * 
	 * @param type
	 *            constraint type
	 * @return true if has constraints
	 * @since 5.0.0
	 */
	public boolean hasConstraints(ConstraintType type) {
		return constraints.has(type);
	}

	/**
	 * Get the constraints
	 * 
	 * @return constraints
	 * @since 5.0.0
	 */
	public Constraints getConstraints() {
		return constraints;
	}

	/**
	 * Get the constraints of the provided type
	 * 
	 * @param type
	 *            constraint type
	 * @return constraints
	 * @since 5.0.0
	 */
	public List<Constraint> getConstraints(ConstraintType type) {
		return constraints.get(type);
	}

	/**
	 * Clear the constraints
	 * 
	 * @return cleared constraints
	 * @since 3.3.0
	 */
	public List<Constraint> clearConstraints() {
		return clearConstraints(true);
	}

	/**
	 * Clear the constraints
	 * 
	 * @param reset
	 *            true to reset constraint settings
	 * @return cleared constraints
	 * @since 3.3.0
	 */
	public List<Constraint> clearConstraints(boolean reset) {

		if (reset) {
			primaryKey = false;
			unique = false;
			notNull = false;
			defaultValue = null;
			autoincrement = false;
		}

		return constraints.clear();
	}

	/**
	 * Clear the constraints of the provided type
	 * 
	 * @param type
	 *            constraint type
	 * @return cleared constraints
	 * @since 5.0.0
	 */
	public List<Constraint> clearConstraints(ConstraintType type) {

		switch (type) {
		case PRIMARY_KEY:
			primaryKey = false;
			break;
		case UNIQUE:
			unique = false;
			break;
		case NOT_NULL:
			notNull = false;
			break;
		case DEFAULT:
			defaultValue = null;
			break;
		case AUTOINCREMENT:
			autoincrement = false;
			break;
		default:

		}

		return constraints.clear(type);
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

		if (constraint.getOrder() == null) {
			setConstraintOrder(constraint);
		}

		constraints.add(constraint);

		switch (constraint.getType()) {
		case PRIMARY_KEY:
			primaryKey = true;
			break;
		case UNIQUE:
			unique = true;
			break;
		case NOT_NULL:
			notNull = true;
			break;
		case DEFAULT:
			break;
		case AUTOINCREMENT:
			autoincrement = true;
			break;
		default:

		}

	}

	/**
	 * Set the constraint order by constraint type
	 * 
	 * @param constraint
	 *            constraint
	 */
	public void setConstraintOrder(Constraint constraint) {

		Integer order = null;

		switch (constraint.getType()) {
		case PRIMARY_KEY:
			order = PRIMARY_KEY_CONSTRAINT_ORDER;
			break;
		case UNIQUE:
			order = UNIQUE_CONSTRAINT_ORDER;
			break;
		case NOT_NULL:
			order = NOT_NULL_CONSTRAINT_ORDER;
			break;
		case DEFAULT:
			order = DEFAULT_VALUE_CONSTRAINT_ORDER;
			break;
		case AUTOINCREMENT:
			order = AUTOINCREMENT_CONSTRAINT_ORDER;
			break;
		default:

		}

		constraint.setOrder(order);
	}

	/**
	 * Add a constraint
	 * 
	 * @param constraint
	 *            constraint
	 * @since 3.3.0
	 */
	public void addConstraint(String constraint) {
		addConstraint(new RawConstraint(constraint));
	}

	/**
	 * Add a constraint
	 * 
	 * @param type
	 *            constraint type
	 * @param constraint
	 *            constraint
	 * @since 5.0.0
	 */
	public void addConstraint(ConstraintType type, String constraint) {
		addConstraint(type, null, constraint);
	}

	/**
	 * Add a constraint
	 * 
	 * @param type
	 *            constraint type
	 * @param order
	 *            constraint order
	 * @param constraint
	 *            constraint
	 * @since 5.0.0
	 */
	public void addConstraint(ConstraintType type, Integer order,
			String constraint) {
		addConstraint(new RawConstraint(type, order, constraint));
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
	 * Add constraints
	 * 
	 * @param constraints
	 *            constraints
	 * @since 5.0.0
	 */
	public void addConstraints(Constraints constraints) {
		addConstraints(constraints.all());
	}

	/**
	 * Add a not null constraint
	 * 
	 * @since 3.3.0
	 */
	public void addNotNullConstraint() {
		addConstraint(ConstraintType.NOT_NULL, NOT_NULL_CONSTRAINT_ORDER,
				"NOT NULL");
	}

	/**
	 * Remove a not null constraint
	 * 
	 * @since 5.0.0
	 */
	public void removeNotNullConstraint() {
		clearConstraints(ConstraintType.NOT_NULL);
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
		addConstraint(ConstraintType.DEFAULT, DEFAULT_VALUE_CONSTRAINT_ORDER,
				"DEFAULT " + CoreSQLUtils.columnDefaultValue(defaultValue,
						getDataType()));
	}

	/**
	 * Remove a default value constraint
	 * 
	 * @since 5.0.0
	 */
	public void removeDefaultValueConstraint() {
		clearConstraints(ConstraintType.DEFAULT);
	}

	/**
	 * Add a primary key constraint
	 * 
	 * @since 3.3.0
	 */
	public void addPrimaryKeyConstraint() {
		addConstraint(ConstraintType.PRIMARY_KEY, PRIMARY_KEY_CONSTRAINT_ORDER,
				"PRIMARY KEY");
	}

	/**
	 * Remove a primary key constraint
	 * 
	 * @since 5.0.0
	 */
	public void removePrimaryKeyConstraint() {
		clearConstraints(ConstraintType.PRIMARY_KEY);
	}

	/**
	 * Add an autoincrement constraint
	 * 
	 * @since 4.0.0
	 */
	public void addAutoincrementConstraint() {
		addConstraint(ConstraintType.AUTOINCREMENT,
				AUTOINCREMENT_CONSTRAINT_ORDER, "AUTOINCREMENT");
	}

	/**
	 * Remove an autoincrement constraint
	 * 
	 * @since 5.0.0
	 */
	public void removeAutoincrementConstraint() {
		clearConstraints(ConstraintType.AUTOINCREMENT);
	}

	/**
	 * Add a unique constraint
	 * 
	 * @since 3.3.0
	 */
	public void addUniqueConstraint() {
		addConstraint(ConstraintType.UNIQUE, UNIQUE_CONSTRAINT_ORDER, "UNIQUE");
	}

	/**
	 * Remove a unique constraint
	 * 
	 * @since 5.0.0
	 */
	public void removeUniqueConstraint() {
		clearConstraints(ConstraintType.UNIQUE);
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
	 * <p>
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
