package mil.nga.geopackage.extension.nga.style;

import java.util.ArrayList;
import java.util.List;

import mil.nga.geopackage.db.GeoPackageDataType;
import mil.nga.geopackage.extension.related.UserMappingTable;
import mil.nga.geopackage.user.custom.UserCustomColumn;
import mil.nga.geopackage.user.custom.UserCustomTable;

/**
 * Feature Style mapping table
 * 
 * @author osbornb
 * @since 3.2.0
 */
public class StyleMappingTable extends UserMappingTable {

	/**
	 * Geometry Type Name column name
	 */
	public static final String COLUMN_GEOMETRY_TYPE_NAME = "geometry_type_name";

	/**
	 * Constructor
	 * 
	 * @param tableName
	 *            table name
	 */
	public StyleMappingTable(String tableName) {
		super(tableName, createColumns());
	}

	/**
	 * Constructor
	 * 
	 * @param table
	 *            user custom table
	 */
	protected StyleMappingTable(UserCustomTable table) {
		super(table);
	}

	/**
	 * Create the style mapping columns
	 * 
	 * @return columns
	 */
	private static List<UserCustomColumn> createColumns() {

		List<UserCustomColumn> columns = new ArrayList<>();
		columns.addAll(createRequiredColumns());

		columns.add(UserCustomColumn.createColumn(COLUMN_GEOMETRY_TYPE_NAME,
				GeoPackageDataType.TEXT));

		return columns;
	}

	/**
	 * Get the geometry type name column index
	 * 
	 * @return geometry type name column index
	 */
	public int getGeometryTypeNameColumnIndex() {
		return getColumnIndex(COLUMN_GEOMETRY_TYPE_NAME);
	}

	/**
	 * Get the geometry type name column
	 * 
	 * @return geometry type name column
	 */
	public UserCustomColumn getGeometryTypeNameColumn() {
		return getColumn(COLUMN_GEOMETRY_TYPE_NAME);
	}

}
