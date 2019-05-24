package mil.nga.geopackage.features.user;

import java.util.List;

import mil.nga.geopackage.db.table.TableColumn;
import mil.nga.geopackage.features.columns.GeometryColumns;
import mil.nga.geopackage.user.UserTableReader;

/**
 * Reads the metadata from an existing feature table
 * 
 * @author osbornb
 * @since 3.3.0
 */
public class FeatureTableReader
		extends UserTableReader<FeatureColumn, FeatureTable> {

	/**
	 * Geometry column name
	 */
	private final String columnName;

	/**
	 * Constructor
	 * 
	 * @param geometryColumns
	 *            geometry columns
	 */
	public FeatureTableReader(GeometryColumns geometryColumns) {
		this(geometryColumns.getTableName(), geometryColumns.getColumnName());
	}

	/**
	 * Constructor
	 * 
	 * @param tableName
	 *            table name
	 * @param geometryColumnName
	 *            geometry column name
	 */
	public FeatureTableReader(String tableName, String geometryColumnName) {
		super(tableName);
		this.columnName = geometryColumnName;
	}

	/**
	 * Constructor, uses first or only found geometry column
	 * 
	 * @param tableName
	 *            table name
	 */
	public FeatureTableReader(String tableName) {
		this(tableName, null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected FeatureTable createTable(String tableName,
			List<FeatureColumn> columnList) {
		return new FeatureTable(tableName, columnName, columnList);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected FeatureColumn createColumn(TableColumn tableColumn) {
		return FeatureColumn.createColumn(tableColumn);
	}

}
