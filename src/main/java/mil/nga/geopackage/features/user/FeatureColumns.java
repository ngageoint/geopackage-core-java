package mil.nga.geopackage.features.user;

import java.util.List;

import mil.nga.geopackage.user.UserColumns;
import mil.nga.sf.GeometryType;

/**
 * Collection of feature columns
 * 
 * @author osbornb
 * @since 3.5.0
 */
public class FeatureColumns extends UserColumns<FeatureColumn> {

	/**
	 * Geometry column
	 */
	private String geometryColumn;

	/**
	 * Geometry column index
	 */
	private int geometryIndex = -1;

	/**
	 * Constructor
	 * 
	 * @param tableName
	 *            table name
	 * @param geometryColumn
	 *            geometry column
	 * @param columns
	 *            columns
	 */
	public FeatureColumns(String tableName, String geometryColumn,
			List<FeatureColumn> columns) {
		this(tableName, geometryColumn, columns, false);
	}

	/**
	 * Constructor
	 * 
	 * @param tableName
	 *            table name
	 * @param geometryColumn
	 *            geometry column
	 * @param columns
	 *            columns
	 * @param custom
	 *            custom column specification
	 */
	public FeatureColumns(String tableName, String geometryColumn,
			List<FeatureColumn> columns, boolean custom) {
		super(tableName, columns, custom);
		this.geometryColumn = geometryColumn;

		updateColumns();
	}

	/**
	 * Copy Constructor
	 * 
	 * @param featureColumns
	 *            feature columns
	 */
	public FeatureColumns(FeatureColumns featureColumns) {
		super(featureColumns);
		this.geometryColumn = featureColumns.geometryColumn;
		this.geometryIndex = featureColumns.geometryIndex;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FeatureColumns copy() {
		return new FeatureColumns(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void updateColumns() {
		super.updateColumns();

		Integer index = null;

		if (geometryColumn != null) {
			index = getColumnIndex(geometryColumn, false);
		} else {
			// Try to find the geometry
			for (FeatureColumn column : getColumns()) {
				if (column.isGeometry()) {
					index = column.getIndex();
					geometryColumn = column.getName();
					break;
				}
			}
		}

		if (!isCustom()) {
			missingCheck(index, GeometryType.GEOMETRY.name());
		}
		if (index != null) {
			geometryIndex = index;
		}
	}

	/**
	 * Get the geometry column name
	 * 
	 * @return geometry column name
	 */
	public String getGeometryColumnName() {
		return geometryColumn;
	}

	/**
	 * Set the geometry column name
	 * 
	 * @param geometryColumn
	 *            geometry column name
	 */
	public void setGeometryColumnName(String geometryColumn) {
		this.geometryColumn = geometryColumn;
	}

	/**
	 * Get the geometry index
	 * 
	 * @return geometry index
	 */
	public int getGeometryIndex() {
		return geometryIndex;
	}

	/**
	 * Set the geometry index
	 * 
	 * @param geometryIndex
	 *            geometry index
	 */
	public void setGeometryIndex(int geometryIndex) {
		this.geometryIndex = geometryIndex;
	}

	/**
	 * Check if the table has a geometry column
	 * 
	 * @return true if has a geometry column
	 */
	public boolean hasGeometryColumn() {
		return geometryIndex >= 0;
	}

	/**
	 * Get the geometry column
	 * 
	 * @return geometry column
	 */
	public FeatureColumn getGeometryColumn() {
		FeatureColumn column = null;
		if (hasGeometryColumn()) {
			column = getColumn(geometryIndex);
		}
		return column;
	}

}
