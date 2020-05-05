package mil.nga.geopackage.extension.nga.style;

import java.util.ArrayList;
import java.util.List;

import mil.nga.geopackage.attributes.AttributesColumn;
import mil.nga.geopackage.attributes.AttributesTable;
import mil.nga.geopackage.db.GeoPackageDataType;

/**
 * Style Table
 * 
 * @author osbornb
 * @since 3.2.0
 */
public class StyleTable extends AttributesTable {

	/**
	 * Table name
	 */
	public static final String TABLE_NAME = "nga_style";

	/**
	 * id name
	 */
	public static final String COLUMN_ID = "id";

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
	 * Geometry line stroke or point width greater than or equal to 0.0
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
	 * @param attributesTable
	 *            attributes table
	 */
	public StyleTable(AttributesTable attributesTable) {
		this();
		setContents(attributesTable.getContents());
	}

	/**
	 * Create the style columns
	 * 
	 * @return columns
	 */
	private static List<AttributesColumn> createColumns() {

		List<AttributesColumn> columns = new ArrayList<>();

		columns.add(AttributesColumn.createPrimaryKeyColumn(COLUMN_ID));
		columns.add(AttributesColumn.createColumn(COLUMN_NAME,
				GeoPackageDataType.TEXT));
		columns.add(AttributesColumn.createColumn(COLUMN_DESCRIPTION,
				GeoPackageDataType.TEXT));
		columns.add(AttributesColumn.createColumn(COLUMN_COLOR,
				GeoPackageDataType.TEXT));
		columns.add(AttributesColumn.createColumn(COLUMN_OPACITY,
				GeoPackageDataType.REAL));
		columns.add(AttributesColumn.createColumn(COLUMN_WIDTH,
				GeoPackageDataType.REAL));
		columns.add(AttributesColumn.createColumn(COLUMN_FILL_COLOR,
				GeoPackageDataType.TEXT));
		columns.add(AttributesColumn.createColumn(COLUMN_FILL_OPACITY,
				GeoPackageDataType.REAL));

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
	public AttributesColumn getNameColumn() {
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
	public AttributesColumn getDescriptionColumn() {
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
	public AttributesColumn getColorColumn() {
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
	public AttributesColumn getOpacityColumn() {
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
	public AttributesColumn getWidthColumn() {
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
	public AttributesColumn getFillColorColumn() {
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
	public AttributesColumn getFillOpacityColumn() {
		return getColumn(COLUMN_FILL_OPACITY);
	}

}
