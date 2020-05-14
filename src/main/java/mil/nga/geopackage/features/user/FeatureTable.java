package mil.nga.geopackage.features.user;

import java.util.List;

import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.contents.Contents;
import mil.nga.geopackage.contents.ContentsDataType;
import mil.nga.geopackage.features.columns.GeometryColumns;
import mil.nga.geopackage.user.UserTable;

/**
 * Represents a user feature table
 * 
 * @author osbornb
 */
public class FeatureTable extends UserTable<FeatureColumn> {

	/**
	 * Constructor
	 * 
	 * @param geometryColumns
	 *            geometry columns
	 * @param columns
	 *            feature columns
	 * @since 3.3.0
	 */
	public FeatureTable(GeometryColumns geometryColumns,
			List<FeatureColumn> columns) {
		this(geometryColumns.getTableName(), geometryColumns.getColumnName(),
				columns);
	}

	/**
	 * Constructor
	 * 
	 * @param tableName
	 *            table name
	 * @param columns
	 *            feature columns
	 */
	public FeatureTable(String tableName, List<FeatureColumn> columns) {
		this(tableName, null, columns);
	}

	/**
	 * Constructor
	 * 
	 * @param tableName
	 *            table name
	 * @param geometryColumn
	 *            geometry column
	 * @param columns
	 *            feature columns
	 * @since 3.3.0
	 */
	public FeatureTable(String tableName, String geometryColumn,
			List<FeatureColumn> columns) {
		super(new FeatureColumns(tableName, geometryColumn, columns));
	}

	/**
	 * Copy Constructor
	 * 
	 * @param featureTable
	 *            feature table
	 * @since 3.3.0
	 */
	public FeatureTable(FeatureTable featureTable) {
		super(featureTable);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FeatureTable copy() {
		return new FeatureTable(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDataType() {
		return getDataType(ContentsDataType.FEATURES.getName());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FeatureColumns getUserColumns() {
		return (FeatureColumns) super.getUserColumns();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FeatureColumns createUserColumns(List<FeatureColumn> columns) {
		return new FeatureColumns(getTableName(), getGeometryColumnName(),
				columns, true);
	}

	/**
	 * Get the geometry column index
	 * 
	 * @return geometry column index
	 */
	public int getGeometryColumnIndex() {
		return getUserColumns().getGeometryIndex();
	}

	/**
	 * Get the geometry feature column
	 * 
	 * @return geometry feature column
	 */
	public FeatureColumn getGeometryColumn() {
		return getUserColumns().getGeometryColumn();
	}

	/**
	 * Get the geometry column name
	 * 
	 * @return geometry column name
	 * @since 3.5.0
	 */
	public String getGeometryColumnName() {
		return getUserColumns().getGeometryColumnName();
	}

	/**
	 * Get the Id and Geometry Column names
	 * 
	 * @return column names
	 * @since 3.5.0
	 */
	public String[] getIdAndGeometryColumnNames() {
		return new String[] { getPkColumnName(), getGeometryColumnName() };
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void validateContents(Contents contents) {
		// Verify the Contents have a features data type
		if (!contents.isFeaturesTypeOrUnknown()) {
			throw new GeoPackageException(
					"The " + Contents.class.getSimpleName() + " of a "
							+ FeatureTable.class.getSimpleName()
							+ " must have a data type of "
							+ ContentsDataType.FEATURES.getName()
							+ ". actual type: " + contents.getDataTypeName());
		}
	}

}
