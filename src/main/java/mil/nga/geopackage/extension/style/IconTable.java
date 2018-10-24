package mil.nga.geopackage.extension.style;

import java.util.ArrayList;
import java.util.List;

import mil.nga.geopackage.db.GeoPackageDataType;
import mil.nga.geopackage.extension.related.media.MediaTable;
import mil.nga.geopackage.user.custom.UserCustomColumn;
import mil.nga.geopackage.user.custom.UserCustomTable;

/**
 * Icon Table
 * 
 * @author osbornb
 * @since 3.1.1
 */
public class IconTable extends MediaTable {

	/**
	 * Table name
	 */
	public static final String TABLE_NAME = "nga_icon";

	/**
	 * Feature Icon name
	 */
	public static final String COLUMN_NAME = "name";

	/**
	 * Feature Icon description
	 */
	public static final String COLUMN_DESCRIPTION = "description";

	/**
	 * Icon display width, when null use actual icon width
	 */
	public static final String COLUMN_WIDTH = "width";

	/**
	 * Icon display height, when null use actual icon height
	 */
	public static final String COLUMN_HEIGHT = "height";

	/**
	 * UV Mapping horizontal anchor distance inclusively between 0.0 and 1.0
	 * from the left edge, when null assume 0.5 (middle of icon)
	 */
	public static final String COLUMN_ANCHOR_U = "anchor_u";

	/**
	 * UV Mapping vertical anchor distance inclusively between 0.0 and 1.0 from
	 * the top edge, when null assume 1.0 (bottom of icon)
	 */
	public static final String COLUMN_ANCHOR_V = "anchor_v";

	/**
	 * Constructor
	 */
	public IconTable() {
		super(TABLE_NAME, createColumns());
	}

	/**
	 * Constructor
	 * 
	 * @param table
	 *            user custom table
	 */
	protected IconTable(UserCustomTable table) {
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
		columns.add(UserCustomColumn.createColumn(index++, COLUMN_WIDTH,
				GeoPackageDataType.DOUBLE, false, null));
		columns.add(UserCustomColumn.createColumn(index++, COLUMN_HEIGHT,
				GeoPackageDataType.DOUBLE, false, null));
		columns.add(UserCustomColumn.createColumn(index++, COLUMN_ANCHOR_U,
				GeoPackageDataType.DOUBLE, false, null));
		columns.add(UserCustomColumn.createColumn(index++, COLUMN_ANCHOR_V,
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
	 * Get the width column index
	 * 
	 * @return opacity column index
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
	 * Get the height column index
	 * 
	 * @return height column index
	 */
	public int getHeightColumnIndex() {
		return getColumnIndex(COLUMN_HEIGHT);
	}

	/**
	 * Get the height column
	 * 
	 * @return height column
	 */
	public UserCustomColumn getHeightColumn() {
		return getColumn(COLUMN_HEIGHT);
	}

	/**
	 * Get the anchor u column index
	 * 
	 * @return anchor u column index
	 */
	public int getAnchorUColumnIndex() {
		return getColumnIndex(COLUMN_ANCHOR_U);
	}

	/**
	 * Get the anchor u column
	 * 
	 * @return anchor u column
	 */
	public UserCustomColumn getAnchorUColumn() {
		return getColumn(COLUMN_ANCHOR_U);
	}

	/**
	 * Get the anchor v column index
	 * 
	 * @return anchor v column index
	 */
	public int getAnchorVColumnIndex() {
		return getColumnIndex(COLUMN_ANCHOR_V);
	}

	/**
	 * Get the anchor v column
	 * 
	 * @return anchor v column
	 */
	public UserCustomColumn getAnchorVColumn() {
		return getColumn(COLUMN_ANCHOR_V);
	}

}
