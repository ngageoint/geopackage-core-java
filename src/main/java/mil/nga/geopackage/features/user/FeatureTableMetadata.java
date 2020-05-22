package mil.nga.geopackage.features.user;

import java.util.ArrayList;
import java.util.List;

import mil.nga.geopackage.BoundingBox;
import mil.nga.geopackage.contents.ContentsDataType;
import mil.nga.geopackage.features.columns.GeometryColumns;
import mil.nga.geopackage.user.UserTableMetadata;
import mil.nga.sf.GeometryType;

/**
 * Feature Table Metadata for defining table creation information
 * 
 * @author osbornb
 * @since 4.0.0
 */
public class FeatureTableMetadata extends UserTableMetadata<FeatureColumn> {

	/**
	 * Default data type
	 */
	public static final String DEFAULT_DATA_TYPE = ContentsDataType.FEATURES
			.getName();

	/**
	 * Default geometry column name
	 */
	public static final String DEFAULT_COLUMN_NAME = "geometry";

	/**
	 * Default geometry type
	 */
	public static final GeometryType DEFAULT_GEOMETRY_TYPE = GeometryType.GEOMETRY;

	/**
	 * Create metadata
	 * 
	 * @return metadata
	 */
	public static FeatureTableMetadata create() {
		return new FeatureTableMetadata();
	}

	/**
	 * Create metadata
	 * 
	 * @param autoincrement
	 *            autoincrement ids
	 * @return metadata
	 */
	public static FeatureTableMetadata create(boolean autoincrement) {
		return new FeatureTableMetadata(null, null, autoincrement, null, null);
	}

	/**
	 * Create metadata
	 * 
	 * @param geometryColumns
	 *            geometry columns
	 * @return metadata
	 */
	public static FeatureTableMetadata create(GeometryColumns geometryColumns) {
		return new FeatureTableMetadata(geometryColumns, null, null, null);
	}

	/**
	 * Create metadata
	 * 
	 * @param geometryColumns
	 *            geometry columns
	 * @param autoincrement
	 *            autoincrement ids
	 * @return metadata
	 */
	public static FeatureTableMetadata create(GeometryColumns geometryColumns,
			boolean autoincrement) {
		return new FeatureTableMetadata(geometryColumns, null, autoincrement,
				null, null);
	}

	/**
	 * Create metadata
	 * 
	 * @param geometryColumns
	 *            geometry columns
	 * @param boundingBox
	 *            bounding box
	 * @return metadata
	 */
	public static FeatureTableMetadata create(GeometryColumns geometryColumns,
			BoundingBox boundingBox) {
		return new FeatureTableMetadata(geometryColumns, null, null,
				boundingBox);
	}

	/**
	 * Create metadata
	 * 
	 * @param geometryColumns
	 *            geometry columns
	 * @param autoincrement
	 *            autoincrement ids
	 * @param boundingBox
	 *            bounding box
	 * @return metadata
	 */
	public static FeatureTableMetadata create(GeometryColumns geometryColumns,
			boolean autoincrement, BoundingBox boundingBox) {
		return new FeatureTableMetadata(geometryColumns, null, autoincrement,
				null, boundingBox);
	}

	/**
	 * Create metadata
	 * 
	 * @param geometryColumns
	 *            geometry columns
	 * @param idColumnName
	 *            id column name
	 * @return metadata
	 */
	public static FeatureTableMetadata create(GeometryColumns geometryColumns,
			String idColumnName) {
		return new FeatureTableMetadata(geometryColumns, idColumnName, null,
				null);
	}

	/**
	 * Create metadata
	 * 
	 * @param geometryColumns
	 *            geometry columns
	 * @param idColumnName
	 *            id column name
	 * @param autoincrement
	 *            autoincrement ids
	 * @return metadata
	 */
	public static FeatureTableMetadata create(GeometryColumns geometryColumns,
			String idColumnName, boolean autoincrement) {
		return new FeatureTableMetadata(geometryColumns, idColumnName,
				autoincrement, null, null);
	}

	/**
	 * Create metadata
	 * 
	 * @param geometryColumns
	 *            geometry columns
	 * @param idColumnName
	 *            id column name
	 * @param boundingBox
	 *            bounding box
	 * @return metadata
	 */
	public static FeatureTableMetadata create(GeometryColumns geometryColumns,
			String idColumnName, BoundingBox boundingBox) {
		return new FeatureTableMetadata(geometryColumns, idColumnName, null,
				boundingBox);
	}

	/**
	 * Create metadata
	 * 
	 * @param geometryColumns
	 *            geometry columns
	 * @param idColumnName
	 *            id column name
	 * @param autoincrement
	 *            autoincrement ids
	 * @param boundingBox
	 *            bounding box
	 * @return metadata
	 */
	public static FeatureTableMetadata create(GeometryColumns geometryColumns,
			String idColumnName, boolean autoincrement,
			BoundingBox boundingBox) {
		return new FeatureTableMetadata(geometryColumns, idColumnName,
				autoincrement, null, boundingBox);
	}

	/**
	 * Create metadata
	 * 
	 * @param geometryColumns
	 *            geometry columns
	 * @param additionalColumns
	 *            additional columns
	 * @return metadata
	 */
	public static FeatureTableMetadata create(GeometryColumns geometryColumns,
			List<FeatureColumn> additionalColumns) {
		return new FeatureTableMetadata(geometryColumns, null,
				additionalColumns, null);
	}

	/**
	 * Create metadata
	 * 
	 * @param geometryColumns
	 *            geometry columns
	 * @param autoincrement
	 *            autoincrement ids
	 * @param additionalColumns
	 *            additional columns
	 * @return metadata
	 */
	public static FeatureTableMetadata create(GeometryColumns geometryColumns,
			boolean autoincrement, List<FeatureColumn> additionalColumns) {
		return new FeatureTableMetadata(geometryColumns, null, autoincrement,
				additionalColumns, null);
	}

	/**
	 * Create metadata
	 * 
	 * @param geometryColumns
	 *            geometry columns
	 * @param additionalColumns
	 *            additional columns
	 * @param boundingBox
	 *            bounding box
	 * @return metadata
	 */
	public static FeatureTableMetadata create(GeometryColumns geometryColumns,
			List<FeatureColumn> additionalColumns, BoundingBox boundingBox) {
		return new FeatureTableMetadata(geometryColumns, null,
				additionalColumns, boundingBox);
	}

	/**
	 * Create metadata
	 * 
	 * @param geometryColumns
	 *            geometry columns
	 * @param autoincrement
	 *            autoincrement ids
	 * @param additionalColumns
	 *            additional columns
	 * @param boundingBox
	 *            bounding box
	 * @return metadata
	 */
	public static FeatureTableMetadata create(GeometryColumns geometryColumns,
			boolean autoincrement, List<FeatureColumn> additionalColumns,
			BoundingBox boundingBox) {
		return new FeatureTableMetadata(geometryColumns, null, autoincrement,
				additionalColumns, boundingBox);
	}

	/**
	 * Create metadata
	 * 
	 * @param geometryColumns
	 *            geometry columns
	 * @param idColumnName
	 *            id column name
	 * @param additionalColumns
	 *            additional columns
	 * @return metadata
	 */
	public static FeatureTableMetadata create(GeometryColumns geometryColumns,
			String idColumnName, List<FeatureColumn> additionalColumns) {
		return new FeatureTableMetadata(geometryColumns, idColumnName,
				additionalColumns, null);
	}

	/**
	 * Create metadata
	 * 
	 * @param geometryColumns
	 *            geometry columns
	 * @param idColumnName
	 *            id column name
	 * @param autoincrement
	 *            autoincrement ids
	 * @param additionalColumns
	 *            additional columns
	 * @return metadata
	 */
	public static FeatureTableMetadata create(GeometryColumns geometryColumns,
			String idColumnName, boolean autoincrement,
			List<FeatureColumn> additionalColumns) {
		return new FeatureTableMetadata(geometryColumns, idColumnName,
				autoincrement, additionalColumns, null);
	}

	/**
	 * Create metadata
	 * 
	 * @param geometryColumns
	 *            geometry columns
	 * @param idColumnName
	 *            id column name
	 * @param additionalColumns
	 *            additional columns
	 * @param boundingBox
	 *            bounding box
	 * @return metadata
	 */
	public static FeatureTableMetadata create(GeometryColumns geometryColumns,
			String idColumnName, List<FeatureColumn> additionalColumns,
			BoundingBox boundingBox) {
		return new FeatureTableMetadata(geometryColumns, idColumnName,
				additionalColumns, boundingBox);
	}

	/**
	 * Create metadata
	 * 
	 * @param geometryColumns
	 *            geometry columns
	 * @param idColumnName
	 *            id column name
	 * @param autoincrement
	 *            autoincrement ids
	 * @param additionalColumns
	 *            additional columns
	 * @param boundingBox
	 *            bounding box
	 * @return metadata
	 */
	public static FeatureTableMetadata create(GeometryColumns geometryColumns,
			String idColumnName, boolean autoincrement,
			List<FeatureColumn> additionalColumns, BoundingBox boundingBox) {
		return new FeatureTableMetadata(geometryColumns, idColumnName,
				autoincrement, additionalColumns, boundingBox);
	}

	/**
	 * Create metadata
	 * 
	 * @param geometryColumns
	 *            geometry columns
	 * @param columns
	 *            feature columns
	 * @return metadata
	 */
	public static FeatureTableMetadata create(GeometryColumns geometryColumns,
			FeatureColumns columns) {
		return new FeatureTableMetadata(geometryColumns, null,
				columns.getColumns());
	}

	/**
	 * Create metadata
	 * 
	 * @param geometryColumns
	 *            geometry columns
	 * @param boundingBox
	 *            bounding box
	 * @param columns
	 *            feature columns
	 * @return metadata
	 */
	public static FeatureTableMetadata create(GeometryColumns geometryColumns,
			BoundingBox boundingBox, FeatureColumns columns) {
		return new FeatureTableMetadata(geometryColumns, boundingBox,
				columns.getColumns());
	}

	/**
	 * Create metadata
	 * 
	 * @param geometryColumns
	 *            geometry columns
	 * @param table
	 *            feature table
	 * @return metadata
	 */
	public static FeatureTableMetadata create(GeometryColumns geometryColumns,
			FeatureTable table) {
		return new FeatureTableMetadata(geometryColumns, null,
				table.getColumns());
	}

	/**
	 * Create metadata
	 * 
	 * @param geometryColumns
	 *            geometry columns
	 * @param boundingBox
	 *            bounding box
	 * @param table
	 *            feature table
	 * @return metadata
	 */
	public static FeatureTableMetadata create(GeometryColumns geometryColumns,
			BoundingBox boundingBox, FeatureTable table) {
		return new FeatureTableMetadata(geometryColumns, boundingBox,
				table.getColumns());
	}

	/**
	 * Create metadata
	 * 
	 * @param dataType
	 *            data type
	 * @return metadata
	 */
	public static FeatureTableMetadata createTyped(String dataType) {
		return new FeatureTableMetadata(dataType, null, null, null, null);
	}

	/**
	 * Create metadata
	 * 
	 * @param dataType
	 *            data type
	 * @param autoincrement
	 *            autoincrement ids
	 * @return metadata
	 */
	public static FeatureTableMetadata createTyped(String dataType,
			boolean autoincrement) {
		return new FeatureTableMetadata(dataType, null, null, autoincrement,
				null, null);
	}

	/**
	 * Create metadata
	 * 
	 * @param dataType
	 *            data type
	 * @param geometryColumns
	 *            geometry columns
	 * @return metadata
	 */
	public static FeatureTableMetadata createTyped(String dataType,
			GeometryColumns geometryColumns) {
		return new FeatureTableMetadata(dataType, geometryColumns, null, null,
				null);
	}

	/**
	 * Create metadata
	 * 
	 * @param dataType
	 *            data type
	 * @param geometryColumns
	 *            geometry columns
	 * @param autoincrement
	 *            autoincrement ids bounding box
	 * @return metadata
	 */
	public static FeatureTableMetadata createTyped(String dataType,
			GeometryColumns geometryColumns, boolean autoincrement) {
		return new FeatureTableMetadata(dataType, geometryColumns, null,
				autoincrement, null, null);
	}

	/**
	 * Create metadata
	 * 
	 * @param dataType
	 *            data type
	 * @param geometryColumns
	 *            geometry columns
	 * @param boundingBox
	 *            bounding box
	 * @return metadata
	 */
	public static FeatureTableMetadata createTyped(String dataType,
			GeometryColumns geometryColumns, BoundingBox boundingBox) {
		return new FeatureTableMetadata(dataType, geometryColumns, null, null,
				boundingBox);
	}

	/**
	 * Create metadata
	 * 
	 * @param dataType
	 *            data type
	 * @param geometryColumns
	 *            geometry columns
	 * @param autoincrement
	 *            autoincrement ids
	 * @param boundingBox
	 *            bounding box
	 * @return metadata
	 */
	public static FeatureTableMetadata createTyped(String dataType,
			GeometryColumns geometryColumns, boolean autoincrement,
			BoundingBox boundingBox) {
		return new FeatureTableMetadata(dataType, geometryColumns, null,
				autoincrement, null, boundingBox);
	}

	/**
	 * Create metadata
	 * 
	 * @param dataType
	 *            data type
	 * @param geometryColumns
	 *            geometry columns
	 * @param idColumnName
	 *            id column name
	 * @return metadata
	 */
	public static FeatureTableMetadata createTyped(String dataType,
			GeometryColumns geometryColumns, String idColumnName) {
		return new FeatureTableMetadata(dataType, geometryColumns, idColumnName,
				null, null);
	}

	/**
	 * Create metadata
	 * 
	 * @param dataType
	 *            data type
	 * @param geometryColumns
	 *            geometry columns
	 * @param idColumnName
	 *            id column name
	 * @param autoincrement
	 *            autoincrement ids
	 * @return metadata
	 */
	public static FeatureTableMetadata createTyped(String dataType,
			GeometryColumns geometryColumns, String idColumnName,
			boolean autoincrement) {
		return new FeatureTableMetadata(dataType, geometryColumns, idColumnName,
				autoincrement, null, null);
	}

	/**
	 * Create metadata
	 * 
	 * @param dataType
	 *            data type
	 * @param geometryColumns
	 *            geometry columns
	 * @param idColumnName
	 *            id column name
	 * @param boundingBox
	 *            bounding box
	 * @return metadata
	 */
	public static FeatureTableMetadata createTyped(String dataType,
			GeometryColumns geometryColumns, String idColumnName,
			BoundingBox boundingBox) {
		return new FeatureTableMetadata(dataType, geometryColumns, idColumnName,
				null, boundingBox);
	}

	/**
	 * Create metadata
	 * 
	 * @param dataType
	 *            data type
	 * @param geometryColumns
	 *            geometry columns
	 * @param idColumnName
	 *            id column name
	 * @param autoincrement
	 *            autoincrement ids
	 * @param boundingBox
	 *            bounding box
	 * @return metadata
	 */
	public static FeatureTableMetadata createTyped(String dataType,
			GeometryColumns geometryColumns, String idColumnName,
			boolean autoincrement, BoundingBox boundingBox) {
		return new FeatureTableMetadata(dataType, geometryColumns, idColumnName,
				autoincrement, null, boundingBox);
	}

	/**
	 * Create metadata
	 * 
	 * @param dataType
	 *            data type
	 * @param geometryColumns
	 *            geometry columns
	 * @param additionalColumns
	 *            additional columns
	 * @return metadata
	 */
	public static FeatureTableMetadata createTyped(String dataType,
			GeometryColumns geometryColumns,
			List<FeatureColumn> additionalColumns) {
		return new FeatureTableMetadata(dataType, geometryColumns, null,
				additionalColumns, null);
	}

	/**
	 * Create metadata
	 * 
	 * @param dataType
	 *            data type
	 * @param geometryColumns
	 *            geometry columns
	 * @param autoincrement
	 *            autoincrement ids
	 * @param additionalColumns
	 *            additional columns
	 * @return metadata
	 */
	public static FeatureTableMetadata createTyped(String dataType,
			GeometryColumns geometryColumns, boolean autoincrement,
			List<FeatureColumn> additionalColumns) {
		return new FeatureTableMetadata(dataType, geometryColumns, null,
				autoincrement, additionalColumns, null);
	}

	/**
	 * Create metadata
	 * 
	 * @param dataType
	 *            data type
	 * @param geometryColumns
	 *            geometry columns
	 * @param additionalColumns
	 *            additional columns
	 * @param boundingBox
	 *            bounding box
	 * @return metadata
	 */
	public static FeatureTableMetadata createTyped(String dataType,
			GeometryColumns geometryColumns,
			List<FeatureColumn> additionalColumns, BoundingBox boundingBox) {
		return new FeatureTableMetadata(dataType, geometryColumns, null,
				additionalColumns, boundingBox);
	}

	/**
	 * Create metadata
	 * 
	 * @param dataType
	 *            data type
	 * @param geometryColumns
	 *            geometry columns
	 * @param autoincrement
	 *            autoincrement ids
	 * @param additionalColumns
	 *            additional columns
	 * @param boundingBox
	 *            bounding box
	 * @return metadata
	 */
	public static FeatureTableMetadata createTyped(String dataType,
			GeometryColumns geometryColumns, boolean autoincrement,
			List<FeatureColumn> additionalColumns, BoundingBox boundingBox) {
		return new FeatureTableMetadata(dataType, geometryColumns, null,
				autoincrement, additionalColumns, boundingBox);
	}

	/**
	 * Create metadata
	 * 
	 * @param dataType
	 *            data type
	 * @param geometryColumns
	 *            geometry columns
	 * @param idColumnName
	 *            id column name
	 * @param additionalColumns
	 *            additional columns
	 * @return metadata
	 */
	public static FeatureTableMetadata createTyped(String dataType,
			GeometryColumns geometryColumns, String idColumnName,
			List<FeatureColumn> additionalColumns) {
		return new FeatureTableMetadata(dataType, geometryColumns, idColumnName,
				additionalColumns, null);
	}

	/**
	 * Create metadata
	 * 
	 * @param dataType
	 *            data type
	 * @param geometryColumns
	 *            geometry columns
	 * @param idColumnName
	 *            id column name
	 * @param autoincrement
	 *            autoincrement ids
	 * @param additionalColumns
	 *            additional columns
	 * @return metadata
	 */
	public static FeatureTableMetadata createTyped(String dataType,
			GeometryColumns geometryColumns, String idColumnName,
			boolean autoincrement, List<FeatureColumn> additionalColumns) {
		return new FeatureTableMetadata(dataType, geometryColumns, idColumnName,
				autoincrement, additionalColumns, null);
	}

	/**
	 * Create metadata
	 * 
	 * @param dataType
	 *            data type
	 * @param geometryColumns
	 *            geometry columns
	 * @param idColumnName
	 *            id column name
	 * @param additionalColumns
	 *            additional columns
	 * @param boundingBox
	 *            bounding box
	 * @return metadata
	 */
	public static FeatureTableMetadata createTyped(String dataType,
			GeometryColumns geometryColumns, String idColumnName,
			List<FeatureColumn> additionalColumns, BoundingBox boundingBox) {
		return new FeatureTableMetadata(dataType, geometryColumns, idColumnName,
				additionalColumns, boundingBox);
	}

	/**
	 * Create metadata
	 * 
	 * @param dataType
	 *            data type
	 * @param geometryColumns
	 *            geometry columns
	 * @param idColumnName
	 *            id column name
	 * @param autoincrement
	 *            autoincrement ids
	 * @param additionalColumns
	 *            additional columns
	 * @param boundingBox
	 *            bounding box
	 * @return metadata
	 */
	public static FeatureTableMetadata createTyped(String dataType,
			GeometryColumns geometryColumns, String idColumnName,
			boolean autoincrement, List<FeatureColumn> additionalColumns,
			BoundingBox boundingBox) {
		return new FeatureTableMetadata(dataType, geometryColumns, idColumnName,
				autoincrement, additionalColumns, boundingBox);
	}

	/**
	 * Create metadata
	 * 
	 * @param dataType
	 *            data type
	 * @param geometryColumns
	 *            geometry columns
	 * @param columns
	 *            feature columns
	 * @return metadata
	 */
	public static FeatureTableMetadata createTyped(String dataType,
			GeometryColumns geometryColumns, FeatureColumns columns) {
		return new FeatureTableMetadata(dataType, geometryColumns, null,
				columns.getColumns());
	}

	/**
	 * Create metadata
	 * 
	 * @param dataType
	 *            data type
	 * @param geometryColumns
	 *            geometry columns
	 * @param boundingBox
	 *            bounding box
	 * @param columns
	 *            feature columns
	 * @return metadata
	 */
	public static FeatureTableMetadata createTyped(String dataType,
			GeometryColumns geometryColumns, BoundingBox boundingBox,
			FeatureColumns columns) {
		return new FeatureTableMetadata(dataType, geometryColumns, boundingBox,
				columns.getColumns());
	}

	/**
	 * Create metadata
	 * 
	 * @param dataType
	 *            data type
	 * @param geometryColumns
	 *            geometry columns
	 * @param table
	 *            feature table
	 * @return metadata
	 */
	public static FeatureTableMetadata createTyped(String dataType,
			GeometryColumns geometryColumns, FeatureTable table) {
		return new FeatureTableMetadata(dataType, geometryColumns, null,
				table.getColumns());
	}

	/**
	 * Create metadata
	 * 
	 * @param dataType
	 *            data type
	 * @param geometryColumns
	 *            geometry columns
	 * @param boundingBox
	 *            bounding box
	 * @param table
	 *            feature table
	 * @return metadata
	 */
	public static FeatureTableMetadata createTyped(String dataType,
			GeometryColumns geometryColumns, BoundingBox boundingBox,
			FeatureTable table) {
		return new FeatureTableMetadata(dataType, geometryColumns, boundingBox,
				table.getColumns());
	}

	/**
	 * Bounding box
	 */
	protected BoundingBox boundingBox;

	/**
	 * Geometry columns
	 */
	protected GeometryColumns geometryColumns;

	/**
	 * Constructor
	 */
	public FeatureTableMetadata() {

	}

	/**
	 * Constructor
	 * 
	 * @param geometryColumns
	 *            geometry columns
	 * @param idColumnName
	 *            id column name
	 * @param additionalColumns
	 *            additional columns
	 * @param boundingBox
	 *            bounding box
	 */
	public FeatureTableMetadata(GeometryColumns geometryColumns,
			String idColumnName, List<FeatureColumn> additionalColumns,
			BoundingBox boundingBox) {
		this(null, geometryColumns, idColumnName, additionalColumns,
				boundingBox);
	}

	/**
	 * Constructor
	 * 
	 * @param dataType
	 *            data type
	 * @param geometryColumns
	 *            geometry columns
	 * @param idColumnName
	 *            id column name
	 * @param additionalColumns
	 *            additional columns
	 * @param boundingBox
	 *            bounding box
	 */
	public FeatureTableMetadata(String dataType,
			GeometryColumns geometryColumns, String idColumnName,
			List<FeatureColumn> additionalColumns, BoundingBox boundingBox) {
		this.dataType = dataType;
		this.geometryColumns = geometryColumns;
		this.idColumnName = idColumnName;
		this.additionalColumns = additionalColumns;
		this.boundingBox = boundingBox;
	}

	/**
	 * Constructor
	 * 
	 * @param geometryColumns
	 *            geometry columns
	 * @param idColumnName
	 *            id column name
	 * @param autoincrement
	 *            autoincrement ids
	 * @param additionalColumns
	 *            additional columns
	 * @param boundingBox
	 *            bounding box
	 */
	public FeatureTableMetadata(GeometryColumns geometryColumns,
			String idColumnName, boolean autoincrement,
			List<FeatureColumn> additionalColumns, BoundingBox boundingBox) {
		this(null, geometryColumns, idColumnName, autoincrement,
				additionalColumns, boundingBox);
	}

	/**
	 * Constructor
	 * 
	 * @param dataType
	 *            data type
	 * @param geometryColumns
	 *            geometry columns
	 * @param idColumnName
	 *            id column name
	 * @param autoincrement
	 *            autoincrement ids
	 * @param additionalColumns
	 *            additional columns
	 * @param boundingBox
	 *            bounding box
	 */
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

	/**
	 * Constructor
	 * 
	 * @param geometryColumns
	 *            geometry columns
	 * @param boundingBox
	 *            bounding box
	 * @param columns
	 *            columns
	 */
	public FeatureTableMetadata(GeometryColumns geometryColumns,
			BoundingBox boundingBox, List<FeatureColumn> columns) {
		this(null, geometryColumns, boundingBox, columns);
	}

	/**
	 * Constructor
	 * 
	 * @param dataType
	 *            data type
	 * @param geometryColumns
	 *            geometry columns
	 * @param boundingBox
	 *            bounding box
	 * @param columns
	 *            columns
	 */
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

	/**
	 * Get the bounding box
	 * 
	 * @return bounding box
	 */
	public BoundingBox getBoundingBox() {
		return boundingBox;
	}

	/**
	 * Set the bounding box
	 * 
	 * @param boundingBox
	 *            bounding box
	 */
	public void setBoundingBox(BoundingBox boundingBox) {
		this.boundingBox = boundingBox;
	}

	/**
	 * Get the geometry columns
	 * 
	 * @return geometry columns
	 */
	public GeometryColumns getGeometryColumns() {
		return geometryColumns;
	}

	/**
	 * Set the geometry columns
	 * 
	 * @param geometryColumns
	 *            geometry columns
	 */
	public void setGeometryColumns(GeometryColumns geometryColumns) {
		this.geometryColumns = geometryColumns;
	}

	/**
	 * Get the column name
	 * 
	 * @return column name
	 */
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

	/**
	 * Get the geometry type
	 * 
	 * @return geometry type
	 */
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
