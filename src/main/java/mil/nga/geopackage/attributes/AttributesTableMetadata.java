package mil.nga.geopackage.attributes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import mil.nga.geopackage.contents.ContentsDataType;
import mil.nga.geopackage.db.table.Constraint;
import mil.nga.geopackage.user.UserTableMetadata;

// TODO
public class AttributesTableMetadata
		extends UserTableMetadata<AttributesColumn> {

	public static final String DEFAULT_DATA_TYPE = ContentsDataType.ATTRIBUTES
			.getName();

	public static AttributesTableMetadata create() {
		return new AttributesTableMetadata();
	}

	public static AttributesTableMetadata create(boolean autoincrement) {
		return new AttributesTableMetadata(null, null, autoincrement, null,
				null);
	}

	public static AttributesTableMetadata create(String tableName) {
		return new AttributesTableMetadata(tableName, null, null, null);
	}

	public static AttributesTableMetadata create(String tableName,
			boolean autoincrement) {
		return new AttributesTableMetadata(tableName, null, autoincrement, null,
				null);
	}

	public static AttributesTableMetadata create(String tableName,
			List<AttributesColumn> additionalColumns) {
		return new AttributesTableMetadata(tableName, null, additionalColumns,
				null);
	}

	public static AttributesTableMetadata create(String tableName,
			boolean autoincrement, List<AttributesColumn> additionalColumns) {
		return new AttributesTableMetadata(tableName, null, autoincrement,
				additionalColumns, null);
	}

	public static AttributesTableMetadata create(String tableName,
			List<AttributesColumn> additionalColumns,
			Collection<Constraint> constraints) {
		return new AttributesTableMetadata(tableName, null, additionalColumns,
				constraints);
	}

	public static AttributesTableMetadata create(String tableName,
			boolean autoincrement, List<AttributesColumn> additionalColumns,
			Collection<Constraint> constraints) {
		return new AttributesTableMetadata(tableName, null, autoincrement,
				additionalColumns, constraints);
	}

	public static AttributesTableMetadata create(String tableName,
			String idColumnName, List<AttributesColumn> additionalColumns) {
		return new AttributesTableMetadata(tableName, idColumnName,
				additionalColumns, null);
	}

	public static AttributesTableMetadata create(String tableName,
			String idColumnName, boolean autoincrement,
			List<AttributesColumn> additionalColumns) {
		return new AttributesTableMetadata(tableName, idColumnName,
				autoincrement, additionalColumns, null);
	}

	public static AttributesTableMetadata create(String tableName,
			String idColumnName, List<AttributesColumn> additionalColumns,
			Collection<Constraint> constraints) {
		return new AttributesTableMetadata(tableName, idColumnName,
				additionalColumns, constraints);
	}

	public static AttributesTableMetadata create(String tableName,
			String idColumnName, boolean autoincrement,
			List<AttributesColumn> additionalColumns,
			Collection<Constraint> constraints) {
		return new AttributesTableMetadata(tableName, idColumnName,
				autoincrement, additionalColumns, constraints);
	}

	public static AttributesTableMetadata create(AttributesColumns columns) {
		return new AttributesTableMetadata(columns.getTableName(),
				columns.getColumns(), null);
	}

	public static AttributesTableMetadata create(AttributesColumns columns,
			Collection<Constraint> constraints) {
		return new AttributesTableMetadata(columns.getTableName(),
				columns.getColumns(), constraints);
	}

	public static AttributesTableMetadata create(AttributesTable table) {
		return new AttributesTableMetadata(table.getTableName(),
				table.getColumns(), table.getConstraints());
	}

	public static AttributesTableMetadata createTyped(String dataType,
			String tableName) {
		return new AttributesTableMetadata(dataType, tableName, null, null,
				null);
	}

	public static AttributesTableMetadata createTyped(String dataType,
			String tableName, boolean autoincrement) {
		return new AttributesTableMetadata(dataType, tableName, null,
				autoincrement, null, null);
	}

	public static AttributesTableMetadata createTyped(String dataType,
			String tableName, List<AttributesColumn> additionalColumns) {
		return new AttributesTableMetadata(dataType, tableName, null,
				additionalColumns, null);
	}

	public static AttributesTableMetadata createTyped(String dataType,
			String tableName, boolean autoincrement,
			List<AttributesColumn> additionalColumns) {
		return new AttributesTableMetadata(dataType, tableName, null,
				autoincrement, additionalColumns, null);
	}

	public static AttributesTableMetadata createTyped(String dataType,
			String tableName, List<AttributesColumn> additionalColumns,
			Collection<Constraint> constraints) {
		return new AttributesTableMetadata(dataType, tableName, null,
				additionalColumns, constraints);
	}

	public static AttributesTableMetadata createTyped(String dataType,
			String tableName, boolean autoincrement,
			List<AttributesColumn> additionalColumns,
			Collection<Constraint> constraints) {
		return new AttributesTableMetadata(dataType, tableName, null,
				autoincrement, additionalColumns, constraints);
	}

	public static AttributesTableMetadata createTyped(String dataType,
			String tableName, String idColumnName,
			List<AttributesColumn> additionalColumns) {
		return new AttributesTableMetadata(dataType, tableName, idColumnName,
				additionalColumns, null);
	}

	public static AttributesTableMetadata createTyped(String dataType,
			String tableName, String idColumnName, boolean autoincrement,
			List<AttributesColumn> additionalColumns) {
		return new AttributesTableMetadata(dataType, tableName, idColumnName,
				autoincrement, additionalColumns, null);
	}

	public static AttributesTableMetadata createTyped(String dataType,
			String tableName, String idColumnName,
			List<AttributesColumn> additionalColumns,
			Collection<Constraint> constraints) {
		return new AttributesTableMetadata(dataType, tableName, idColumnName,
				additionalColumns, constraints);
	}

	public static AttributesTableMetadata createTyped(String dataType,
			String tableName, String idColumnName, boolean autoincrement,
			List<AttributesColumn> additionalColumns,
			Collection<Constraint> constraints) {
		return new AttributesTableMetadata(dataType, tableName, idColumnName,
				autoincrement, additionalColumns, constraints);
	}

	public static AttributesTableMetadata createTyped(String dataType,
			AttributesColumns columns) {
		return new AttributesTableMetadata(dataType, columns.getTableName(),
				columns.getColumns(), null, false);
	}

	public static AttributesTableMetadata createTyped(String dataType,
			AttributesColumns columns, Collection<Constraint> constraints) {
		return new AttributesTableMetadata(dataType, columns.getTableName(),
				columns.getColumns(), constraints, false);
	}

	public static AttributesTableMetadata createTyped(String dataType,
			AttributesTable table) {
		return new AttributesTableMetadata(dataType, table.getTableName(),
				table.getColumns(), table.getConstraints(), false);
	}

	protected Collection<Constraint> constraints;

	public AttributesTableMetadata() {

	}

	public AttributesTableMetadata(String tableName, String idColumnName,
			List<AttributesColumn> additionalColumns,
			Collection<Constraint> constraints) {
		this(null, tableName, idColumnName, additionalColumns, constraints);
	}

	public AttributesTableMetadata(String dataType, String tableName,
			String idColumnName, List<AttributesColumn> additionalColumns,
			Collection<Constraint> constraints) {
		this.dataType = dataType;
		this.tableName = tableName;
		this.idColumnName = idColumnName;
		this.additionalColumns = additionalColumns;
		this.constraints = constraints;
	}

	public AttributesTableMetadata(String tableName, String idColumnName,
			boolean autoincrement, List<AttributesColumn> additionalColumns,
			Collection<Constraint> constraints) {
		this(null, tableName, idColumnName, autoincrement, additionalColumns,
				constraints);
	}

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

	public AttributesTableMetadata(String tableName,
			List<AttributesColumn> columns,
			Collection<Constraint> constraints) {
		this(null, tableName, columns, constraints, false);
	}

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

	public Collection<Constraint> getConstraints() {
		return constraints;
	}

	public void setConstraints(Collection<Constraint> constraints) {
		this.constraints = constraints;
	}

}
