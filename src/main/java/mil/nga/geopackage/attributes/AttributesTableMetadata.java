package mil.nga.geopackage.attributes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import mil.nga.geopackage.contents.ContentsDataType;
import mil.nga.geopackage.db.table.Constraint;
import mil.nga.geopackage.user.UserTableMetadata;

/**
 * Attributes Table Metadata for defining table creation information
 * 
 * @author osbornb
 * @since 4.0.0
 */
public class AttributesTableMetadata
		extends UserTableMetadata<AttributesColumn> {

	/**
	 * Default data type
	 */
	public static final String DEFAULT_DATA_TYPE = ContentsDataType.ATTRIBUTES
			.getName();

	/**
	 * Create metadata
	 * 
	 * @return metadata
	 */
	public static AttributesTableMetadata create() {
		return new AttributesTableMetadata();
	}

	/**
	 * Create metadata
	 * 
	 * @param autoincrement
	 *            autoincrement ids
	 * @return metadata
	 */
	public static AttributesTableMetadata create(boolean autoincrement) {
		return new AttributesTableMetadata(null, null, autoincrement, null,
				null);
	}

	/**
	 * Constructor
	 * 
	 * @param tableName
	 *            table name
	 * @return metadata
	 */
	public static AttributesTableMetadata create(String tableName) {
		return new AttributesTableMetadata(tableName, null, null, null);
	}

	/**
	 * Constructor
	 * 
	 * @param tableName
	 *            table name
	 * @param autoincrement
	 *            autoincrement ids
	 * @return metadata
	 */
	public static AttributesTableMetadata create(String tableName,
			boolean autoincrement) {
		return new AttributesTableMetadata(tableName, null, autoincrement, null,
				null);
	}

	/**
	 * Constructor
	 * 
	 * @param tableName
	 *            table name
	 * @param additionalColumns
	 *            additional columns
	 * @return metadata
	 */
	public static AttributesTableMetadata create(String tableName,
			List<AttributesColumn> additionalColumns) {
		return new AttributesTableMetadata(tableName, null, additionalColumns,
				null);
	}

	/**
	 * Constructor
	 * 
	 * @param tableName
	 *            table name
	 * @param autoincrement
	 *            autoincrement ids
	 * @param additionalColumns
	 *            additional columns
	 * @return metadata
	 */
	public static AttributesTableMetadata create(String tableName,
			boolean autoincrement, List<AttributesColumn> additionalColumns) {
		return new AttributesTableMetadata(tableName, null, autoincrement,
				additionalColumns, null);
	}

	/**
	 * Constructor
	 * 
	 * @param tableName
	 *            table name
	 * @param additionalColumns
	 *            additional columns
	 * @param constraints
	 *            constraints
	 * @return metadata
	 */
	public static AttributesTableMetadata create(String tableName,
			List<AttributesColumn> additionalColumns,
			Collection<Constraint> constraints) {
		return new AttributesTableMetadata(tableName, null, additionalColumns,
				constraints);
	}

	/**
	 * Constructor
	 * 
	 * @param tableName
	 *            table name
	 * @param autoincrement
	 *            autoincrement ids
	 * @param additionalColumns
	 *            additional columns
	 * @param constraints
	 *            constraints
	 * @return metadata
	 */
	public static AttributesTableMetadata create(String tableName,
			boolean autoincrement, List<AttributesColumn> additionalColumns,
			Collection<Constraint> constraints) {
		return new AttributesTableMetadata(tableName, null, autoincrement,
				additionalColumns, constraints);
	}

	/**
	 * Constructor
	 * 
	 * @param tableName
	 *            table name
	 * @param idColumnName
	 *            id column name
	 * @param additionalColumns
	 *            additional columns
	 * @return metadata
	 */
	public static AttributesTableMetadata create(String tableName,
			String idColumnName, List<AttributesColumn> additionalColumns) {
		return new AttributesTableMetadata(tableName, idColumnName,
				additionalColumns, null);
	}

	/**
	 * Constructor
	 * 
	 * @param tableName
	 *            table name
	 * @param idColumnName
	 *            id column name
	 * @param autoincrement
	 *            autoincrement ids
	 * @param additionalColumns
	 *            additional columns
	 * @return metadata
	 */
	public static AttributesTableMetadata create(String tableName,
			String idColumnName, boolean autoincrement,
			List<AttributesColumn> additionalColumns) {
		return new AttributesTableMetadata(tableName, idColumnName,
				autoincrement, additionalColumns, null);
	}

	/**
	 * Constructor
	 * 
	 * @param tableName
	 *            table name
	 * @param idColumnName
	 *            id column name
	 * @param additionalColumns
	 *            additional columns
	 * @param constraints
	 *            constraints
	 * @return metadata
	 */
	public static AttributesTableMetadata create(String tableName,
			String idColumnName, List<AttributesColumn> additionalColumns,
			Collection<Constraint> constraints) {
		return new AttributesTableMetadata(tableName, idColumnName,
				additionalColumns, constraints);
	}

	/**
	 * Constructor
	 * 
	 * @param tableName
	 *            table name
	 * @param idColumnName
	 *            id column name
	 * @param autoincrement
	 *            autoincrement ids
	 * @param additionalColumns
	 *            additional columns
	 * @param constraints
	 *            constraints
	 * @return metadata
	 */
	public static AttributesTableMetadata create(String tableName,
			String idColumnName, boolean autoincrement,
			List<AttributesColumn> additionalColumns,
			Collection<Constraint> constraints) {
		return new AttributesTableMetadata(tableName, idColumnName,
				autoincrement, additionalColumns, constraints);
	}

	/**
	 * Constructor
	 * 
	 * @param columns
	 *            columns
	 * @return metadata
	 */
	public static AttributesTableMetadata create(AttributesColumns columns) {
		return new AttributesTableMetadata(columns.getTableName(),
				columns.getColumns(), null);
	}

	/**
	 * Constructor
	 * 
	 * @param columns
	 *            columns
	 * @param constraints
	 *            constraints
	 * @return metadata
	 */
	public static AttributesTableMetadata create(AttributesColumns columns,
			Collection<Constraint> constraints) {
		return new AttributesTableMetadata(columns.getTableName(),
				columns.getColumns(), constraints);
	}

	/**
	 * Create metadata
	 * 
	 * @param table
	 *            attributes table
	 * @return metadata
	 */
	public static AttributesTableMetadata create(AttributesTable table) {
		return new AttributesTableMetadata(table.getTableName(),
				table.getColumns(), table.getConstraints());
	}

	/**
	 * Create metadata
	 * 
	 * @param dataType
	 *            data type
	 * @param tableName
	 *            table name
	 * @return metadata
	 */
	public static AttributesTableMetadata createTyped(String dataType,
			String tableName) {
		return new AttributesTableMetadata(dataType, tableName, null, null,
				null);
	}

	/**
	 * Create metadata
	 * 
	 * @param dataType
	 *            data type
	 * @param tableName
	 *            table name
	 * @param autoincrement
	 *            autoincrement ids
	 * @return metadata
	 */
	public static AttributesTableMetadata createTyped(String dataType,
			String tableName, boolean autoincrement) {
		return new AttributesTableMetadata(dataType, tableName, null,
				autoincrement, null, null);
	}

	/**
	 * Create metadata
	 * 
	 * @param dataType
	 *            data type
	 * @param tableName
	 *            table name
	 * @param additionalColumns
	 *            additional columns
	 * @return metadata
	 */
	public static AttributesTableMetadata createTyped(String dataType,
			String tableName, List<AttributesColumn> additionalColumns) {
		return new AttributesTableMetadata(dataType, tableName, null,
				additionalColumns, null);
	}

	/**
	 * Create metadata
	 * 
	 * @param dataType
	 *            data type
	 * @param tableName
	 *            table name
	 * @param autoincrement
	 *            autoincrement ids
	 * @param additionalColumns
	 *            additional columns
	 * @return metadata
	 */
	public static AttributesTableMetadata createTyped(String dataType,
			String tableName, boolean autoincrement,
			List<AttributesColumn> additionalColumns) {
		return new AttributesTableMetadata(dataType, tableName, null,
				autoincrement, additionalColumns, null);
	}

	/**
	 * Create metadata
	 * 
	 * @param dataType
	 *            data type
	 * @param tableName
	 *            table name
	 * @param additionalColumns
	 *            additional columns
	 * @param constraints
	 *            constraints
	 * @return metadata
	 */
	public static AttributesTableMetadata createTyped(String dataType,
			String tableName, List<AttributesColumn> additionalColumns,
			Collection<Constraint> constraints) {
		return new AttributesTableMetadata(dataType, tableName, null,
				additionalColumns, constraints);
	}

	/**
	 * Create metadata
	 * 
	 * @param dataType
	 *            data type
	 * @param tableName
	 *            table name
	 * @param autoincrement
	 *            autoincrement ids
	 * @param additionalColumns
	 *            additional columns
	 * @param constraints
	 *            constraints
	 * @return metadata
	 */
	public static AttributesTableMetadata createTyped(String dataType,
			String tableName, boolean autoincrement,
			List<AttributesColumn> additionalColumns,
			Collection<Constraint> constraints) {
		return new AttributesTableMetadata(dataType, tableName, null,
				autoincrement, additionalColumns, constraints);
	}

	/**
	 * Create metadata
	 * 
	 * @param dataType
	 *            data type
	 * @param tableName
	 *            table name
	 * @param idColumnName
	 *            id column name
	 * @param additionalColumns
	 *            additional columns
	 * @return metadata
	 */
	public static AttributesTableMetadata createTyped(String dataType,
			String tableName, String idColumnName,
			List<AttributesColumn> additionalColumns) {
		return new AttributesTableMetadata(dataType, tableName, idColumnName,
				additionalColumns, null);
	}

	/**
	 * Create metadata
	 * 
	 * @param dataType
	 *            data type
	 * @param tableName
	 *            table name
	 * @param idColumnName
	 *            id column name
	 * @param autoincrement
	 *            autoincrement ids
	 * @param additionalColumns
	 *            additional columns
	 * @return metadata
	 */
	public static AttributesTableMetadata createTyped(String dataType,
			String tableName, String idColumnName, boolean autoincrement,
			List<AttributesColumn> additionalColumns) {
		return new AttributesTableMetadata(dataType, tableName, idColumnName,
				autoincrement, additionalColumns, null);
	}

	/**
	 * Create metadata
	 * 
	 * @param dataType
	 *            data type
	 * @param tableName
	 *            table name
	 * @param idColumnName
	 *            id column name
	 * @param additionalColumns
	 *            additional columns
	 * @param constraints
	 *            constraints
	 * @return metadata
	 */
	public static AttributesTableMetadata createTyped(String dataType,
			String tableName, String idColumnName,
			List<AttributesColumn> additionalColumns,
			Collection<Constraint> constraints) {
		return new AttributesTableMetadata(dataType, tableName, idColumnName,
				additionalColumns, constraints);
	}

	/**
	 * Create metadata
	 * 
	 * @param dataType
	 *            data type
	 * @param tableName
	 *            table name
	 * @param idColumnName
	 *            id column name
	 * @param autoincrement
	 *            autoincrement ids
	 * @param additionalColumns
	 *            additional columns
	 * @param constraints
	 *            constraints
	 * @return metadata
	 */
	public static AttributesTableMetadata createTyped(String dataType,
			String tableName, String idColumnName, boolean autoincrement,
			List<AttributesColumn> additionalColumns,
			Collection<Constraint> constraints) {
		return new AttributesTableMetadata(dataType, tableName, idColumnName,
				autoincrement, additionalColumns, constraints);
	}

	/**
	 * Create metadata
	 * 
	 * @param dataType
	 *            data type
	 * @param columns
	 *            columns
	 * @return metadata
	 */
	public static AttributesTableMetadata createTyped(String dataType,
			AttributesColumns columns) {
		return new AttributesTableMetadata(dataType, columns.getTableName(),
				columns.getColumns(), null, false);
	}

	/**
	 * Create metadata
	 * 
	 * @param dataType
	 *            data type
	 * @param columns
	 *            columns
	 * @param constraints
	 *            constraints
	 * @return metadata
	 */
	public static AttributesTableMetadata createTyped(String dataType,
			AttributesColumns columns, Collection<Constraint> constraints) {
		return new AttributesTableMetadata(dataType, columns.getTableName(),
				columns.getColumns(), constraints, false);
	}

	/**
	 * Create metadata
	 * 
	 * @param dataType
	 *            data type
	 * @param table
	 *            attributes table
	 * @return metadata
	 */
	public static AttributesTableMetadata createTyped(String dataType,
			AttributesTable table) {
		return new AttributesTableMetadata(dataType, table.getTableName(),
				table.getColumns(), table.getConstraints(), false);
	}

	/**
	 * Constraints
	 */
	protected Collection<Constraint> constraints;

	/**
	 * Constructor
	 */
	public AttributesTableMetadata() {

	}

	/**
	 * Constructor
	 * 
	 * @param tableName
	 *            table name
	 * @param idColumnName
	 *            id column name
	 * @param additionalColumns
	 *            additional columns
	 * @param constraints
	 *            constraints
	 */
	public AttributesTableMetadata(String tableName, String idColumnName,
			List<AttributesColumn> additionalColumns,
			Collection<Constraint> constraints) {
		this(null, tableName, idColumnName, additionalColumns, constraints);
	}

	/**
	 * Constructor
	 * 
	 * @param dataType
	 *            data type
	 * @param tableName
	 *            table name
	 * @param idColumnName
	 *            id column name
	 * @param additionalColumns
	 *            additional columns
	 * @param constraints
	 *            constraints
	 */
	public AttributesTableMetadata(String dataType, String tableName,
			String idColumnName, List<AttributesColumn> additionalColumns,
			Collection<Constraint> constraints) {
		this.dataType = dataType;
		this.tableName = tableName;
		this.idColumnName = idColumnName;
		this.additionalColumns = additionalColumns;
		this.constraints = constraints;
	}

	/**
	 * Constructor
	 * 
	 * @param tableName
	 *            table name
	 * @param idColumnName
	 *            id column name
	 * @param autoincrement
	 *            autoincrement ids
	 * @param additionalColumns
	 *            additional columns
	 * @param constraints
	 *            constraints
	 */
	public AttributesTableMetadata(String tableName, String idColumnName,
			boolean autoincrement, List<AttributesColumn> additionalColumns,
			Collection<Constraint> constraints) {
		this(null, tableName, idColumnName, autoincrement, additionalColumns,
				constraints);
	}

	/**
	 * Constructor
	 * 
	 * @param dataType
	 *            data type
	 * @param tableName
	 *            table name
	 * @param idColumnName
	 *            id column name
	 * @param autoincrement
	 *            autoincrement ids
	 * @param additionalColumns
	 *            additional columns
	 * @param constraints
	 *            constraints
	 */
	public AttributesTableMetadata(String dataType, String tableName,
			String idColumnName, boolean autoincrement,
			List<AttributesColumn> additionalColumns,
			Collection<Constraint> constraints) {
		this.dataType = dataType;
		this.tableName = tableName;
		this.idColumnName = idColumnName;
		this.autoincrement = autoincrement;
		this.additionalColumns = additionalColumns;
		this.constraints = constraints;
	}

	/**
	 * Constructor
	 * 
	 * @param tableName
	 *            table name
	 * @param columns
	 *            columns
	 * @param constraints
	 *            constraints
	 */
	public AttributesTableMetadata(String tableName,
			List<AttributesColumn> columns,
			Collection<Constraint> constraints) {
		this(null, tableName, columns, constraints, false);
	}

	/**
	 * Constructor
	 * 
	 * @param dataType
	 *            data type
	 * @param tableName
	 *            table name
	 * @param columns
	 *            columns
	 * @param constraints
	 *            constraints
	 * @param extra
	 *            unused extra parameter for method overloading
	 */
	public AttributesTableMetadata(String dataType, String tableName,
			List<AttributesColumn> columns, Collection<Constraint> constraints,
			boolean extra) {
		this.dataType = dataType;
		this.tableName = tableName;
		this.columns = columns;
		this.constraints = constraints;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDefaultDataType() {
		return DEFAULT_DATA_TYPE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<AttributesColumn> buildColumns() {

		List<AttributesColumn> attributesColumns = getColumns();

		if (attributesColumns == null) {

			attributesColumns = new ArrayList<>();
			attributesColumns.add(AttributesColumn.createPrimaryKeyColumn(
					getIdColumnName(), isAutoincrement()));

			List<AttributesColumn> additional = getAdditionalColumns();
			if (additional != null) {
				attributesColumns.addAll(additional);
			}

		}

		return attributesColumns;
	}

	/**
	 * Get the constraints
	 * 
	 * @return constraints
	 */
	public Collection<Constraint> getConstraints() {
		return constraints;
	}

	/**
	 * Set the constraints
	 * 
	 * @param constraints
	 *            constraints
	 */
	public void setConstraints(Collection<Constraint> constraints) {
		this.constraints = constraints;
	}

}
