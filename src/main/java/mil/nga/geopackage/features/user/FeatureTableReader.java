package mil.nga.geopackage.features.user;

import java.util.List;

import mil.nga.geopackage.db.table.TableColumn;
import mil.nga.geopackage.features.columns.GeometryColumns;
import mil.nga.geopackage.user.UserTableReader;
import mil.nga.sf.GeometryType;

/**
 * Reads the metadata from an existing feature table
 * 
 * @author osbornb
 * @since 3.2.1
 */
public class FeatureTableReader
		extends UserTableReader<FeatureColumn, FeatureTable> {

	/**
	 * Geometry columns
	 */
	private GeometryColumns geometryColumns;

	/**
	 * Constructor
	 * 
	 * @param geometryColumns
	 *            geometry columns
	 */
	public FeatureTableReader(GeometryColumns geometryColumns) {
		super(geometryColumns.getTableName());
		this.geometryColumns = geometryColumns;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected FeatureTable createTable(String tableName,
			List<FeatureColumn> columnList) {
		return new FeatureTable(tableName, columnList);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected FeatureColumn createColumn(TableColumn tableColumn) {

		GeometryType geometryType = null;
		if (tableColumn.getName()
				.equalsIgnoreCase(geometryColumns.getColumnName())) {
			geometryType = GeometryType.fromName(tableColumn.getType());
		}

		return FeatureColumn.createColumn(tableColumn, geometryType);
	}

}
