package mil.nga.geopackage.features.user;

import java.util.List;

import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.core.contents.Contents;
import mil.nga.geopackage.core.contents.ContentsDataType;
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
	 * @param tableName
	 *            table name
	 * @param columns
	 *            feature columns
	 */
	public FeatureTable(String tableName, List<FeatureColumn> columns) {
		super(tableName, columns);

		Integer geometry = null;

		// Find the geometry
		for (FeatureColumn column : columns) {

			if (column.isGeometry()) {
				duplicateCheck(column.getIndex(), geometry,
						GeometryType.GEOMETRY.name());
				geometry = column.getIndex();
			}

		}

		missingCheck(geometry, GeometryType.GEOMETRY.name());
		geometryIndex = geometry;

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
			throw new GeoPackageException("The "
					+ Contents.class.getSimpleName() + " of a "
					+ FeatureTable.class.getSimpleName()
					+ " must have a data type of "
					+ ContentsDataType.FEATURES.getName());
		}
	}

}
