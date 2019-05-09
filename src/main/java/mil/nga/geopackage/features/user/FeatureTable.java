package mil.nga.geopackage.features.user;

import java.util.List;

import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.core.contents.Contents;
import mil.nga.geopackage.core.contents.ContentsDataType;
import mil.nga.geopackage.features.columns.GeometryColumns;
import mil.nga.geopackage.user.UserTable;
import mil.nga.sf.GeometryType;

/**
 * Represents a user feature table
 * 
 * @author osbornb
 */
public class FeatureTable extends UserTable<FeatureColumn> {

	/**
	 * Geometry column index
	 */
	private final int geometryIndex;

	/**
	 * Constructor
	 * 
	 * @param geometryColumns
	 *            geometry columns
	 * @param columns
	 *            feature columns
	 * @since 3.2.1
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
	 * @since 3.2.1
	 */
	public FeatureTable(String tableName, String geometryColumn,
			List<FeatureColumn> columns) {
		super(tableName, columns);

		Integer geometry = null;

		// Find the geometry
		for (FeatureColumn column : columns) {

			boolean isGeometryColumn = false;

			if (geometryColumn != null) {
				isGeometryColumn = geometryColumn
						.equalsIgnoreCase(column.getName());
			} else if (column.isGeometry()) {
				isGeometryColumn = column.isGeometry();
			}

			if (isGeometryColumn) {
				geometry = column.getIndex();
				break;
			}

		}

		missingCheck(geometry, GeometryType.GEOMETRY.name());
		geometryIndex = geometry;

	}

	/**
	 * Copy Constructor
	 * 
	 * @param featureTable
	 *            feature table
	 * @since 3.2.1
	 */
	public FeatureTable(FeatureTable featureTable) {
		super(featureTable);
		this.geometryIndex = featureTable.geometryIndex;
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
		return ContentsDataType.FEATURES.getName();
	}

	/**
	 * Get the geometry column index
	 * 
	 * @return geometry column index
	 */
	public int getGeometryColumnIndex() {
		return geometryIndex;
	}

	/**
	 * Get the geometry feature column
	 * 
	 * @return geometry feature column
	 */
	public FeatureColumn getGeometryColumn() {
		return getColumn(geometryIndex);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void validateContents(Contents contents) {
		// Verify the Contents have a features data type
		ContentsDataType dataType = contents.getDataType();
		if (dataType == null || dataType != ContentsDataType.FEATURES) {
			throw new GeoPackageException(
					"The " + Contents.class.getSimpleName() + " of a "
							+ FeatureTable.class.getSimpleName()
							+ " must have a data type of "
							+ ContentsDataType.FEATURES.getName());
		}
	}

}
