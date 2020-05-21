package mil.nga.geopackage.features.user;

import java.util.ArrayList;
import java.util.List;

import mil.nga.geopackage.BoundingBox;
import mil.nga.geopackage.contents.ContentsDataType;
import mil.nga.geopackage.features.columns.GeometryColumns;
import mil.nga.geopackage.user.UserTableMetadata;
import mil.nga.sf.GeometryType;

// TODO
public class FeatureTableMetadata extends UserTableMetadata<FeatureColumn> {

	public static final String DEFAULT_DATA_TYPE = ContentsDataType.FEATURES
			.getName();

	public static final String DEFAULT_COLUMN_NAME = "geometry";

	public static final GeometryType DEFAULT_GEOMETRY_TYPE = GeometryType.GEOMETRY;

	public static FeatureTableMetadata create() {
		return new FeatureTableMetadata();
	}

	public static FeatureTableMetadata create(boolean autoincrement) {
		return new FeatureTableMetadata(null, null, autoincrement, null, null);
	}

	public static FeatureTableMetadata create(GeometryColumns geometryColumns) {
		return new FeatureTableMetadata(geometryColumns, null, null, null);
	}

	public static FeatureTableMetadata create(GeometryColumns geometryColumns,
			boolean autoincrement) {
		return new FeatureTableMetadata(geometryColumns, null, autoincrement,
				null, null);
	}

	public static FeatureTableMetadata create(GeometryColumns geometryColumns,
			BoundingBox boundingBox) {
		return new FeatureTableMetadata(geometryColumns, null, null,
				boundingBox);
	}

	public static FeatureTableMetadata create(GeometryColumns geometryColumns,
			boolean autoincrement, BoundingBox boundingBox) {
		return new FeatureTableMetadata(geometryColumns, null, autoincrement,
				null, boundingBox);
	}

	public static FeatureTableMetadata create(GeometryColumns geometryColumns,
			String idColumnName) {
		return new FeatureTableMetadata(geometryColumns, idColumnName, null,
				null);
	}

	public static FeatureTableMetadata create(GeometryColumns geometryColumns,
			String idColumnName, boolean autoincrement) {
		return new FeatureTableMetadata(geometryColumns, idColumnName,
				autoincrement, null, null);
	}

	public static FeatureTableMetadata create(GeometryColumns geometryColumns,
			String idColumnName, BoundingBox boundingBox) {
		return new FeatureTableMetadata(geometryColumns, idColumnName, null,
				boundingBox);
	}

	public static FeatureTableMetadata create(GeometryColumns geometryColumns,
			String idColumnName, boolean autoincrement,
			BoundingBox boundingBox) {
		return new FeatureTableMetadata(geometryColumns, idColumnName,
				autoincrement, null, boundingBox);
	}

	public static FeatureTableMetadata create(GeometryColumns geometryColumns,
			List<FeatureColumn> additionalColumns) {
		return new FeatureTableMetadata(geometryColumns, null,
				additionalColumns, null);
	}

	public static FeatureTableMetadata create(GeometryColumns geometryColumns,
			boolean autoincrement, List<FeatureColumn> additionalColumns) {
		return new FeatureTableMetadata(geometryColumns, null, autoincrement,
				additionalColumns, null);
	}

	public static FeatureTableMetadata create(GeometryColumns geometryColumns,
			List<FeatureColumn> additionalColumns, BoundingBox boundingBox) {
		return new FeatureTableMetadata(geometryColumns, null,
				additionalColumns, boundingBox);
	}

	public static FeatureTableMetadata create(GeometryColumns geometryColumns,
			boolean autoincrement, List<FeatureColumn> additionalColumns,
			BoundingBox boundingBox) {
		return new FeatureTableMetadata(geometryColumns, null, autoincrement,
				additionalColumns, boundingBox);
	}

	public static FeatureTableMetadata create(GeometryColumns geometryColumns,
			String idColumnName, List<FeatureColumn> additionalColumns) {
		return new FeatureTableMetadata(geometryColumns, idColumnName,
				additionalColumns, null);
	}

	public static FeatureTableMetadata create(GeometryColumns geometryColumns,
			String idColumnName, boolean autoincrement,
			List<FeatureColumn> additionalColumns) {
		return new FeatureTableMetadata(geometryColumns, idColumnName,
				autoincrement, additionalColumns, null);
	}

	public static FeatureTableMetadata create(GeometryColumns geometryColumns,
			String idColumnName, List<FeatureColumn> additionalColumns,
			BoundingBox boundingBox) {
		return new FeatureTableMetadata(geometryColumns, idColumnName,
				additionalColumns, boundingBox);
	}

	public static FeatureTableMetadata create(GeometryColumns geometryColumns,
			String idColumnName, boolean autoincrement,
			List<FeatureColumn> additionalColumns, BoundingBox boundingBox) {
		return new FeatureTableMetadata(geometryColumns, idColumnName,
				autoincrement, additionalColumns, boundingBox);
	}

	public static FeatureTableMetadata create(GeometryColumns geometryColumns,
			FeatureColumns columns) {
		return new FeatureTableMetadata(geometryColumns, null,
				columns.getColumns());
	}

	public static FeatureTableMetadata create(GeometryColumns geometryColumns,
			BoundingBox boundingBox, FeatureColumns columns) {
		return new FeatureTableMetadata(geometryColumns, boundingBox,
				columns.getColumns());
	}

	public static FeatureTableMetadata create(GeometryColumns geometryColumns,
			FeatureTable table) {
		return new FeatureTableMetadata(geometryColumns, null,
				table.getColumns());
	}

	public static FeatureTableMetadata create(GeometryColumns geometryColumns,
			BoundingBox boundingBox, FeatureTable table) {
		return new FeatureTableMetadata(geometryColumns, boundingBox,
				table.getColumns());
	}

	public static FeatureTableMetadata createTyped(String dataType) {
		return new FeatureTableMetadata(dataType, null, null, null, null);
	}

	public static FeatureTableMetadata createTyped(String dataType,
			boolean autoincrement) {
		return new FeatureTableMetadata(dataType, null, null, autoincrement,
				null, null);
	}

	public static FeatureTableMetadata createTyped(String dataType,
			GeometryColumns geometryColumns) {
		return new FeatureTableMetadata(dataType, geometryColumns, null, null,
				null);
	}

	public static FeatureTableMetadata createTyped(String dataType,
			GeometryColumns geometryColumns, boolean autoincrement) {
		return new FeatureTableMetadata(dataType, geometryColumns, null,
				autoincrement, null, null);
	}

	public static FeatureTableMetadata createTyped(String dataType,
			GeometryColumns geometryColumns, BoundingBox boundingBox) {
		return new FeatureTableMetadata(dataType, geometryColumns, null, null,
				boundingBox);
	}

	public static FeatureTableMetadata createTyped(String dataType,
			GeometryColumns geometryColumns, boolean autoincrement,
			BoundingBox boundingBox) {
		return new FeatureTableMetadata(dataType, geometryColumns, null,
				autoincrement, null, boundingBox);
	}

	public static FeatureTableMetadata createTyped(String dataType,
			GeometryColumns geometryColumns, String idColumnName) {
		return new FeatureTableMetadata(dataType, geometryColumns, idColumnName,
				null, null);
	}

	public static FeatureTableMetadata createTyped(String dataType,
			GeometryColumns geometryColumns, String idColumnName,
			boolean autoincrement) {
		return new FeatureTableMetadata(dataType, geometryColumns, idColumnName,
				autoincrement, null, null);
	}

	public static FeatureTableMetadata createTyped(String dataType,
			GeometryColumns geometryColumns, String idColumnName,
			BoundingBox boundingBox) {
		return new FeatureTableMetadata(dataType, geometryColumns, idColumnName,
				null, boundingBox);
	}

	public static FeatureTableMetadata createTyped(String dataType,
			GeometryColumns geometryColumns, String idColumnName,
			boolean autoincrement, BoundingBox boundingBox) {
		return new FeatureTableMetadata(dataType, geometryColumns, idColumnName,
				autoincrement, null, boundingBox);
	}

	public static FeatureTableMetadata createTyped(String dataType,
			GeometryColumns geometryColumns,
			List<FeatureColumn> additionalColumns) {
		return new FeatureTableMetadata(dataType, geometryColumns, null,
				additionalColumns, null);
	}

	public static FeatureTableMetadata createTyped(String dataType,
			GeometryColumns geometryColumns, boolean autoincrement,
			List<FeatureColumn> additionalColumns) {
		return new FeatureTableMetadata(dataType, geometryColumns, null,
				autoincrement, additionalColumns, null);
	}

	public static FeatureTableMetadata createTyped(String dataType,
			GeometryColumns geometryColumns,
			List<FeatureColumn> additionalColumns, BoundingBox boundingBox) {
		return new FeatureTableMetadata(dataType, geometryColumns, null,
				additionalColumns, boundingBox);
	}

	public static FeatureTableMetadata createTyped(String dataType,
			GeometryColumns geometryColumns, boolean autoincrement,
			List<FeatureColumn> additionalColumns, BoundingBox boundingBox) {
		return new FeatureTableMetadata(dataType, geometryColumns, null,
				autoincrement, additionalColumns, boundingBox);
	}

	public static FeatureTableMetadata createTyped(String dataType,
			GeometryColumns geometryColumns, String idColumnName,
			List<FeatureColumn> additionalColumns) {
		return new FeatureTableMetadata(dataType, geometryColumns, idColumnName,
				additionalColumns, null);
	}

	public static FeatureTableMetadata createTyped(String dataType,
			GeometryColumns geometryColumns, String idColumnName,
			boolean autoincrement, List<FeatureColumn> additionalColumns) {
		return new FeatureTableMetadata(dataType, geometryColumns, idColumnName,
				autoincrement, additionalColumns, null);
	}

	public static FeatureTableMetadata createTyped(String dataType,
			GeometryColumns geometryColumns, String idColumnName,
			List<FeatureColumn> additionalColumns, BoundingBox boundingBox) {
		return new FeatureTableMetadata(dataType, geometryColumns, idColumnName,
				additionalColumns, boundingBox);
	}

	public static FeatureTableMetadata createTyped(String dataType,
			GeometryColumns geometryColumns, String idColumnName,
			boolean autoincrement, List<FeatureColumn> additionalColumns,
			BoundingBox boundingBox) {
		return new FeatureTableMetadata(dataType, geometryColumns, idColumnName,
				autoincrement, additionalColumns, boundingBox);
	}

	public static FeatureTableMetadata createTyped(String dataType,
			GeometryColumns geometryColumns, FeatureColumns columns) {
		return new FeatureTableMetadata(dataType, geometryColumns, null,
				columns.getColumns());
	}

	public static FeatureTableMetadata createTyped(String dataType,
			GeometryColumns geometryColumns, BoundingBox boundingBox,
			FeatureColumns columns) {
		return new FeatureTableMetadata(dataType, geometryColumns, boundingBox,
				columns.getColumns());
	}

	public static FeatureTableMetadata createTyped(String dataType,
			GeometryColumns geometryColumns, FeatureTable table) {
		return new FeatureTableMetadata(dataType, geometryColumns, null,
				table.getColumns());
	}

	public static FeatureTableMetadata createTyped(String dataType,
			GeometryColumns geometryColumns, BoundingBox boundingBox,
			FeatureTable table) {
		return new FeatureTableMetadata(dataType, geometryColumns, boundingBox,
				table.getColumns());
	}

	protected BoundingBox boundingBox;

	protected GeometryColumns geometryColumns;

	public FeatureTableMetadata() {

	}

	public FeatureTableMetadata(GeometryColumns geometryColumns,
			String idColumnName, List<FeatureColumn> additionalColumns,
			BoundingBox boundingBox) {
		this(null, geometryColumns, idColumnName, additionalColumns,
				boundingBox);
	}

	public FeatureTableMetadata(String dataType,
			GeometryColumns geometryColumns, String idColumnName,
			List<FeatureColumn> additionalColumns, BoundingBox boundingBox) {
		this.dataType = dataType;
		this.geometryColumns = geometryColumns;
		this.idColumnName = idColumnName;
		this.additionalColumns = additionalColumns;
		this.boundingBox = boundingBox;
	}

	public FeatureTableMetadata(GeometryColumns geometryColumns,
			String idColumnName, boolean autoincrement,
			List<FeatureColumn> additionalColumns, BoundingBox boundingBox) {
		this(null, geometryColumns, idColumnName, autoincrement,
				additionalColumns, boundingBox);
	}

	public FeatureTableMetadata(String dataType,
			GeometryColumns geometryColumns, String idColumnName,
			boolean autoincrement, List<FeatureColumn> additionalColumns,
			BoundingBox boundingBox) {
		this.dataType = dataType;
		this.geometryColumns = geometryColumns;
		this.idColumnName = idColumnName;
		this.autoincrement = autoincrement;
		this.additionalColumns = additionalColumns;
		this.boundingBox = boundingBox;
	}

	public FeatureTableMetadata(GeometryColumns geometryColumns,
			BoundingBox boundingBox, List<FeatureColumn> columns) {
		this(null, geometryColumns, boundingBox, columns);
	}

	public FeatureTableMetadata(String dataType,
			GeometryColumns geometryColumns, BoundingBox boundingBox,
			List<FeatureColumn> columns) {
		this.dataType = dataType;
		this.geometryColumns = geometryColumns;
		this.boundingBox = boundingBox;
		this.columns = columns;
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
	public List<FeatureColumn> buildColumns() {

		List<FeatureColumn> featureColumns = getColumns();

		if (featureColumns == null) {

			featureColumns = new ArrayList<>();
			featureColumns.add(FeatureColumn.createPrimaryKeyColumn(
					getIdColumnName(), isAutoincrement()));
			featureColumns.add(FeatureColumn
					.createGeometryColumn(getColumnName(), getGeometryType()));

			List<FeatureColumn> additional = getAdditionalColumns();
			if (additional != null) {
				featureColumns.addAll(additional);
			}

		}

		return featureColumns;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getTableName() {
		String tableName = null;
		if (geometryColumns != null) {
			tableName = geometryColumns.getTableName();
		}
		if (tableName == null) {
			tableName = super.getTableName();
			if (geometryColumns != null) {
				geometryColumns.setTableName(tableName);
			}
		}
		return tableName;
	}

	public BoundingBox getBoundingBox() {
		return boundingBox;
	}

	public void setBoundingBox(BoundingBox boundingBox) {
		this.boundingBox = boundingBox;
	}

	public GeometryColumns getGeometryColumns() {
		return geometryColumns;
	}

	public void setGeometryColumns(GeometryColumns geometryColumns) {
		this.geometryColumns = geometryColumns;
	}

	public String getColumnName() {
		String columnName = null;
		if (geometryColumns != null) {
			columnName = geometryColumns.getColumnName();
		}
		if (columnName == null) {
			columnName = DEFAULT_COLUMN_NAME;
			if (geometryColumns != null) {
				geometryColumns.setColumnName(columnName);
			}
		}
		return columnName;
	}

	public GeometryType getGeometryType() {
		GeometryType geometryType = null;
		if (geometryColumns != null) {
			geometryType = geometryColumns.getGeometryType();
		}
		if (geometryType == null) {
			geometryType = DEFAULT_GEOMETRY_TYPE;
			if (geometryColumns != null) {
				geometryColumns.setGeometryType(geometryType);
			}
		}
		return geometryType;
	}

}
