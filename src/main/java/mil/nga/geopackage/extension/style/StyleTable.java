package mil.nga.geopackage.extension.style;

import java.util.ArrayList;
import java.util.List;

import mil.nga.geopackage.db.GeoPackageDataType;
import mil.nga.geopackage.extension.related.simple.SimpleAttributesTable;
import mil.nga.geopackage.user.custom.UserCustomColumn;
import mil.nga.geopackage.user.custom.UserCustomTable;

/**
 * Style Table
 * 
 * @author osbornb
 * @since 3.1.1
 */
public class StyleTable extends SimpleAttributesTable {

	/**
	 * Table name
	 */
	public static final String TABLE_NAME = "nga_style";

	/**
	 * Feature Style name
	 */
	public static final String COLUMN_NAME = "name";

	/**
	 * Feature Style description
	 */
	public static final String COLUMN_DESCRIPTION = "description";

	/**
	 * Geometry color in hex format #RRGGBB or #RGB
	 */
	public static final String COLUMN_COLOR = "color";

	/**
	 * Geometry color opacity inclusively between 0.0 and 1.0
	 */
	public static final String COLUMN_OPACITY = "opacity";

	/**
	 * Geometry line stroke or point width >= 0.0
	 */
	public static final String COLUMN_WIDTH = "width";

	/**
	 * Closed geometry fill color in hex format #RRGGBB or #RGB
	 */
	public static final String COLUMN_FILL_COLOR = "fill_color";

	/**
	 * Closed geometry fill color opacity inclusively between 0.0 and 1.0
	 */
	public static final String COLUMN_FILL_OPACITY = "fill_opacity";

	/**
	 * Constructor
	 */
	public StyleTable() {
		super(TABLE_NAME, createColumns());
	}

	/**
	 * Constructor
	 * 
	 * @param table
	 *            user custom table
	 */
	protected StyleTable(UserCustomTable table) {
		super(table);
	}

	/**
	 * Create the style columns
	 * 
	 * @return
	 */
	private static List<UserCustomColumn> createColumns() {

		List<UserCustomColumn> columns = new ArrayList<>();
		columns.addAll(createRequiredColumns());

		int index = columns.size();
		columns.add(UserCustomColumn.createColumn(index++, COLUMN_NAME,
				GeoPackageDataType.TEXT, false, null));
		columns.add(UserCustomColumn.createColumn(index++, COLUMN_DESCRIPTION,
				GeoPackageDataType.TEXT, false, null));
		columns.add(UserCustomColumn.createColumn(index++, COLUMN_COLOR,
				GeoPackageDataType.TEXT, false, null));
		columns.add(UserCustomColumn.createColumn(index++, COLUMN_OPACITY,
				GeoPackageDataType.DOUBLE, false, null));
		columns.add(UserCustomColumn.createColumn(index++, COLUMN_WIDTH,
				GeoPackageDataType.DOUBLE, false, null));
		columns.add(UserCustomColumn.createColumn(index++, COLUMN_FILL_COLOR,
				GeoPackageDataType.TEXT, false, null));
		columns.add(UserCustomColumn.createColumn(index++, COLUMN_FILL_OPACITY,
				GeoPackageDataType.DOUBLE, false, null));

		return columns;
	}

	/**
	 * Get the name column index
	 * 
	 * @return name column index
	 */
	public int getNameColumnIndex() {
		return getColumnIndex(COLUMN_NAME);
	}

	/**
	 * Get the name column
	 * 
	 * @return name column
	 */
	public UserCustomColumn getNameColumn() {
		return getColumn(COLUMN_NAME);
	}

	/**
	 * Get the description column index
	 * 
	 * @return description column index
	 */
	public int getDescriptionColumnIndex() {
		return getColumnIndex(COLUMN_DESCRIPTION);
	}

	/**
	 * Get the description column
	 * 
	 * @return description column
	 */
	public UserCustomColumn getDescriptionColumn() {
		return getColumn(COLUMN_DESCRIPTION);
	}

	/**
	 * Get the color column index
	 * 
	 * @return color column index
	 */
	public int getColorColumnIndex() {
		return getColumnIndex(COLUMN_COLOR);
	}

	/**
	 * Get the color column
	 * 
	 * @return color column
	 */
	public UserCustomColumn getColorColumn() {
		return getColumn(COLUMN_COLOR);
	}

	/**
	 * Get the opacity column index
	 * 
	 * @return opacity column index
	 */
	public int getOpacityColumnIndex() {
		return getColumnIndex(COLUMN_OPACITY);
	}

	/**
	 * Get the opacity column
	 * 
	 * @return opacity column
	 */
	public UserCustomColumn getOpacityColumn() {
		return getColumn(COLUMN_OPACITY);
	}

	/**
	 * Get the width column index
	 * 
	 * @return width column index
	 */
	public int getWidthColumnIndex() {
		return getColumnIndex(COLUMN_WIDTH);
	}

	/**
	 * Get the width column
	 * 
	 * @return width column
	 */
	public UserCustomColumn getWidthColumn() {
		return getColumn(COLUMN_WIDTH);
	}

	/**
	 * Get the fill color column index
	 * 
	 * @return fill color column index
	 */
	public int getFillColorColumnIndex() {
		return getColumnIndex(COLUMN_FILL_COLOR);
	}

	/**
	 * Get the fill color column
	 * 
	 * @return fill color column
	 */
	public UserCustomColumn getFillColorColumn() {
		return getColumn(COLUMN_FILL_COLOR);
	}

	/**
	 * Get the fill opacity column index
	 * 
	 * @return fill opacity column index
	 */
	public int getFillOpacityColumnIndex() {
		return getColumnIndex(COLUMN_FILL_OPACITY);
	}

	/**
	 * Get the fill opacity column
	 * 
	 * @return fill opacity column
	 */
	public UserCustomColumn getFillOpacityColumn() {
		return getColumn(COLUMN_FILL_OPACITY);
	}

}
