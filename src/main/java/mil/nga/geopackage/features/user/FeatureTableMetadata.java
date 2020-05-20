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

	private GeometryColumns geometryColumns;

	public FeatureTableMetadata() {

	}

	public FeatureTableMetadata(GeometryColumns geometryColumns) {
		this.geometryColumns = geometryColumns;
	}

	public FeatureTableMetadata(GeometryColumns geometryColumns,
			boolean autoincrement) {
		this.geometryColumns = geometryColumns;
		this.autoincrement = autoincrement;
	}

	public FeatureTableMetadata(GeometryColumns geometryColumns,
			BoundingBox boundingBox) {
		this.geometryColumns = geometryColumns;
		this.boundingBox = boundingBox;
	}

	public FeatureTableMetadata(GeometryColumns geometryColumns,
			boolean autoincrement, BoundingBox boundingBox) {
		this.geometryColumns = geometryColumns;
		this.autoincrement = autoincrement;
		this.boundingBox = boundingBox;
	}

	public FeatureTableMetadata(GeometryColumns geometryColumns,
			String idColumnName) {
		this.geometryColumns = geometryColumns;
		this.idColumnName = idColumnName;
	}

	public FeatureTableMetadata(GeometryColumns geometryColumns,
			String idColumnName, boolean autoincrement) {
		this.geometryColumns = geometryColumns;
		this.idColumnName = idColumnName;
		this.autoincrement = autoincrement;
	}

	public FeatureTableMetadata(GeometryColumns geometryColumns,
			String idColumnName, BoundingBox boundingBox) {
		this.geometryColumns = geometryColumns;
		this.idColumnName = idColumnName;
		this.boundingBox = boundingBox;
	}

	public FeatureTableMetadata(GeometryColumns geometryColumns,
			String idColumnName, boolean autoincrement,
			BoundingBox boundingBox) {
		this.geometryColumns = geometryColumns;
		this.idColumnName = idColumnName;
		this.autoincrement = autoincrement;
		this.boundingBox = boundingBox;
	}

	public FeatureTableMetadata(GeometryColumns geometryColumns,
			List<FeatureColumn> additionalColumns) {
		this.geometryColumns = geometryColumns;
		this.additionalColumns = additionalColumns;
	}

	public FeatureTableMetadata(GeometryColumns geometryColumns,
			boolean autoincrement, List<FeatureColumn> additionalColumns) {
		this.geometryColumns = geometryColumns;
		this.autoincrement = autoincrement;
		this.additionalColumns = additionalColumns;
	}

	public FeatureTableMetadata(GeometryColumns geometryColumns,
			List<FeatureColumn> additionalColumns, BoundingBox boundingBox) {
		this.geometryColumns = geometryColumns;
		this.additionalColumns = additionalColumns;
		this.boundingBox = boundingBox;
	}

	public FeatureTableMetadata(GeometryColumns geometryColumns,
			boolean autoincrement, List<FeatureColumn> additionalColumns,
			BoundingBox boundingBox) {
		this.geometryColumns = geometryColumns;
		this.autoincrement = autoincrement;
		this.additionalColumns = additionalColumns;
		this.boundingBox = boundingBox;
	}

	public FeatureTableMetadata(GeometryColumns geometryColumns,
			String idColumnName, List<FeatureColumn> additionalColumns) {
		this.geometryColumns = geometryColumns;
		this.idColumnName = idColumnName;
		this.additionalColumns = additionalColumns;
	}

	public FeatureTableMetadata(GeometryColumns geometryColumns,
			String idColumnName, boolean autoincrement,
			List<FeatureColumn> additionalColumns) {
		this.geometryColumns = geometryColumns;
		this.idColumnName = idColumnName;
		this.autoincrement = autoincrement;
		this.additionalColumns = additionalColumns;
	}

	public FeatureTableMetadata(GeometryColumns geometryColumns,
			String idColumnName, List<FeatureColumn> additionalColumns,
			BoundingBox boundingBox) {
		this.geometryColumns = geometryColumns;
		this.idColumnName = idColumnName;
		this.additionalColumns = additionalColumns;
		this.boundingBox = boundingBox;
	}

	public FeatureTableMetadata(GeometryColumns geometryColumns,
			String idColumnName, boolean autoincrement,
			List<FeatureColumn> additionalColumns, BoundingBox boundingBox) {
		this.geometryColumns = geometryColumns;
		this.idColumnName = idColumnName;
		this.autoincrement = autoincrement;
		this.additionalColumns = additionalColumns;
		this.boundingBox = boundingBox;
	}

	public FeatureTableMetadata(GeometryColumns geometryColumns,
			BoundingBox boundingBox, List<FeatureColumn> columns) {
		this.geometryColumns = geometryColumns;
		this.boundingBox = boundingBox;
		this.columns = columns;
	}

	public FeatureTableMetadata(GeometryColumns geometryColumns,
			FeatureTable table) {
		this.geometryColumns = geometryColumns;
		this.columns = table.getColumns();
	}

	public FeatureTableMetadata(GeometryColumns geometryColumns,
			BoundingBox boundingBox, FeatureTable table) {
		this.geometryColumns = geometryColumns;
		this.boundingBox = boundingBox;
		this.columns = table.getColumns();
	}

	public FeatureTableMetadata(String dataType) {
		this.dataType = dataType;
	}

	public FeatureTableMetadata(String dataType,
			GeometryColumns geometryColumns) {
		this.dataType = dataType;
		this.geometryColumns = geometryColumns;
	}

	public FeatureTableMetadata(String dataType,
			GeometryColumns geometryColumns, boolean autoincrement) {
		this.dataType = dataType;
		this.geometryColumns = geometryColumns;
		this.autoincrement = autoincrement;
	}

	public FeatureTableMetadata(String dataType,
			GeometryColumns geometryColumns, BoundingBox boundingBox) {
		this.dataType = dataType;
		this.geometryColumns = geometryColumns;
		this.boundingBox = boundingBox;
	}

	public FeatureTableMetadata(String dataType,
			GeometryColumns geometryColumns, boolean autoincrement,
			BoundingBox boundingBox) {
		this.dataType = dataType;
		this.geometryColumns = geometryColumns;
		this.autoincrement = autoincrement;
		this.boundingBox = boundingBox;
	}

	public FeatureTableMetadata(String dataType,
			GeometryColumns geometryColumns, String idColumnName) {
		this.dataType = dataType;
		this.geometryColumns = geometryColumns;
		this.idColumnName = idColumnName;
	}

	public FeatureTableMetadata(String dataType,
			GeometryColumns geometryColumns, String idColumnName,
			boolean autoincrement) {
		this.dataType = dataType;
		this.geometryColumns = geometryColumns;
		this.idColumnName = idColumnName;
		this.autoincrement = autoincrement;
	}

	public FeatureTableMetadata(String dataType,
			GeometryColumns geometryColumns, String idColumnName,
			BoundingBox boundingBox) {
		this.dataType = dataType;
		this.geometryColumns = geometryColumns;
		this.idColumnName = idColumnName;
		this.boundingBox = boundingBox;
	}

	public FeatureTableMetadata(String dataType,
			GeometryColumns geometryColumns, String idColumnName,
			boolean autoincrement, BoundingBox boundingBox) {
		this.dataType = dataType;
		this.geometryColumns = geometryColumns;
		this.idColumnName = idColumnName;
		this.autoincrement = autoincrement;
		this.boundingBox = boundingBox;
	}

	public FeatureTableMetadata(String dataType,
			GeometryColumns geometryColumns,
			List<FeatureColumn> additionalColumns) {
		this.dataType = dataType;
		this.geometryColumns = geometryColumns;
		this.additionalColumns = additionalColumns;
	}

	public FeatureTableMetadata(String dataType,
			GeometryColumns geometryColumns, boolean autoincrement,
			List<FeatureColumn> additionalColumns) {
		this.dataType = dataType;
		this.geometryColumns = geometryColumns;
		this.autoincrement = autoincrement;
		this.additionalColumns = additionalColumns;
	}

	public FeatureTableMetadata(String dataType,
			GeometryColumns geometryColumns,
			List<FeatureColumn> additionalColumns, BoundingBox boundingBox) {
		this.dataType = dataType;
		this.geometryColumns = geometryColumns;
		this.additionalColumns = additionalColumns;
		this.boundingBox = boundingBox;
	}

	public FeatureTableMetadata(String dataType,
			GeometryColumns geometryColumns, boolean autoincrement,
			List<FeatureColumn> additionalColumns, BoundingBox boundingBox) {
		this.dataType = dataType;
		this.geometryColumns = geometryColumns;
		this.autoincrement = autoincrement;
		this.additionalColumns = additionalColumns;
		this.boundingBox = boundingBox;
	}

	public FeatureTableMetadata(String dataType,
			GeometryColumns geometryColumns, String idColumnName,
			List<FeatureColumn> additionalColumns) {
		this.dataType = dataType;
		this.geometryColumns = geometryColumns;
		this.idColumnName = idColumnName;
		this.additionalColumns = additionalColumns;
	}

	public FeatureTableMetadata(String dataType,
			GeometryColumns geometryColumns, String idColumnName,
			boolean autoincrement, List<FeatureColumn> additionalColumns) {
		this.dataType = dataType;
		this.geometryColumns = geometryColumns;
		this.idColumnName = idColumnName;
		this.autoincrement = autoincrement;
		this.additionalColumns = additionalColumns;
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

	public FeatureTableMetadata(String dataType,
			GeometryColumns geometryColumns, BoundingBox boundingBox,
			List<FeatureColumn> columns) {
		this.dataType = dataType;
		this.geometryColumns = geometryColumns;
		this.boundingBox = boundingBox;
		this.columns = columns;
	}

	public FeatureTableMetadata(String dataType,
			GeometryColumns geometryColumns, FeatureTable table) {
		this.dataType = dataType;
		this.geometryColumns = geometryColumns;
		this.columns = table.getColumns();
	}

	public FeatureTableMetadata(String dataType,
			GeometryColumns geometryColumns, BoundingBox boundingBox,
			FeatureTable table) {
		this.dataType = dataType;
		this.geometryColumns = geometryColumns;
		this.boundingBox = boundingBox;
		this.columns = table.getColumns();
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
