package mil.nga.geopackage.tiles.user;

import mil.nga.geopackage.db.GeoPackageDataType;
import mil.nga.geopackage.db.table.TableColumn;
import mil.nga.geopackage.user.UserColumn;
import mil.nga.geopackage.user.UserTable;

/**
 * Tile column
 * 
 * @author osbornb
 */
public class TileColumn extends UserColumn {

	/**
	 * Create an id column
	 * 
	 * @return tile column
	 * @since 3.3.0
	 */
	public static TileColumn createIdColumn() {
		return createIdColumn(UserTable.DEFAULT_AUTOINCREMENT);
	}

	/**
	 * Create an id column
	 * 
	 * @param autoincrement
	 *            autoincrement flag
	 * @return tile column
	 * @since 4.0.0
	 */
	public static TileColumn createIdColumn(boolean autoincrement) {
		return createIdColumn(NO_INDEX, autoincrement);
	}

	/**
	 * Create an id column
	 * 
	 * @param index
	 *            index
	 * @return tile column
	 */
	public static TileColumn createIdColumn(int index) {
		return createIdColumn(index, UserTable.DEFAULT_AUTOINCREMENT);
	}

	/**
	 * Create an id column
	 * 
	 * @param index
	 *            index
	 * @param autoincrement
	 *            autoincrement flag
	 * @return tile column
	 * @since 4.0.0
	 */
	public static TileColumn createIdColumn(int index, boolean autoincrement) {
		return new TileColumn(index, TileTable.COLUMN_ID,
				GeoPackageDataType.INTEGER, null, false, null, true,
				autoincrement);
	}

	/**
	 * Create a zoom level column
	 * 
	 * @return tile column
	 * @since 3.3.0
	 */
	public static TileColumn createZoomLevelColumn() {
		return createZoomLevelColumn(NO_INDEX);
	}

	/**
	 * Create a zoom level column
	 * 
	 * @param index
	 *            index
	 * @return tile column
	 */
	public static TileColumn createZoomLevelColumn(int index) {
		return new TileColumn(index, TileTable.COLUMN_ZOOM_LEVEL,
				GeoPackageDataType.INTEGER, null, true, null, false, false);
	}

	/**
	 * Create a tile column column
	 * 
	 * @return tile column
	 * @since 3.3.0
	 */
	public static TileColumn createTileColumnColumn() {
		return createTileColumnColumn(NO_INDEX);
	}

	/**
	 * Create a tile column column
	 * 
	 * @param index
	 *            index
	 * @return tile column
	 */
	public static TileColumn createTileColumnColumn(int index) {
		return new TileColumn(index, TileTable.COLUMN_TILE_COLUMN,
				GeoPackageDataType.INTEGER, null, true, null, false, false);
	}

	/**
	 * Create a tile row column
	 * 
	 * @return tile column
	 * @since 3.3.0
	 */
	public static TileColumn createTileRowColumn() {
		return createTileRowColumn(NO_INDEX);
	}

	/**
	 * Create a tile row column
	 * 
	 * @param index
	 *            index
	 * @return tile column
	 */
	public static TileColumn createTileRowColumn(int index) {
		return new TileColumn(index, TileTable.COLUMN_TILE_ROW,
				GeoPackageDataType.INTEGER, null, true, null, false, false);
	}

	/**
	 * Create a tile data column
	 * 
	 * @return tile column
	 * @since 3.3.0
	 */
	public static TileColumn createTileDataColumn() {
		return createTileDataColumn(NO_INDEX);
	}

	/**
	 * Create a tile data column
	 * 
	 * @param index
	 *            index
	 * @return tile column
	 */
	public static TileColumn createTileDataColumn(int index) {
		return new TileColumn(index, TileTable.COLUMN_TILE_DATA,
				GeoPackageDataType.BLOB, null, true, null, false, false);
	}

	/**
	 * Create a new column
	 * 
	 * @param name
	 *            name
	 * @param type
	 *            data type
	 * @return tile column
	 * @since 3.3.0
	 */
	public static TileColumn createColumn(String name,
			GeoPackageDataType type) {
		return createColumn(NO_INDEX, name, type);
	}

	/**
	 * Create a new column
	 * 
	 * @param index
	 *            index
	 * @param name
	 *            name
	 * @param type
	 *            data type
	 * @return tile column
	 * @since 3.3.0
	 */
	public static TileColumn createColumn(int index, String name,
			GeoPackageDataType type) {
		return createColumn(index, name, type, false, null);
	}

	/**
	 * Create a new column
	 * 
	 * @param name
	 *            name
	 * @param type
	 *            data type
	 * @param notNull
	 *            not null flag
	 * @return tile column
	 * @since 3.3.0
	 */
	public static TileColumn createColumn(String name, GeoPackageDataType type,
			boolean notNull) {
		return createColumn(NO_INDEX, name, type, notNull);
	}

	/**
	 * Create a new column
	 * 
	 * @param index
	 *            index
	 * @param name
	 *            name
	 * @param type
	 *            data type
	 * @param notNull
	 *            not null flag
	 * @return tile column
	 * @since 3.3.0
	 */
	public static TileColumn createColumn(int index, String name,
			GeoPackageDataType type, boolean notNull) {
		return createColumn(index, name, type, notNull, null);
	}

	/**
	 * Create a new column
	 * 
	 * @param name
	 *            name
	 * @param type
	 *            data type
	 * @param notNull
	 *            not null flag
	 * @param defaultValue
	 *            default value
	 * @return tile column
	 * @since 3.3.0
	 */
	public static TileColumn createColumn(String name, GeoPackageDataType type,
			boolean notNull, Object defaultValue) {
		return createColumn(NO_INDEX, name, type, notNull, defaultValue);
	}

	/**
	 * Create a new column
	 * 
	 * @param index
	 *            index
	 * @param name
	 *            name
	 * @param type
	 *            type
	 * @param notNull
	 *            not null flag
	 * @param defaultValue
	 *            default value
	 * @return tile column
	 */
	public static TileColumn createColumn(int index, String name,
			GeoPackageDataType type, boolean notNull, Object defaultValue) {
		return createColumn(index, name, type, null, notNull, defaultValue);
	}

	/**
	 * Create a new column
	 * 
	 * @param name
	 *            name
	 * @param type
	 *            data type
	 * @param max
	 *            max value
	 * @return tile column
	 * @since 3.3.0
	 */
	public static TileColumn createColumn(String name, GeoPackageDataType type,
			Long max) {
		return createColumn(NO_INDEX, name, type, max);
	}

	/**
	 * Create a new column
	 * 
	 * @param index
	 *            index
	 * @param name
	 *            name
	 * @param type
	 *            data type
	 * @param max
	 *            max value
	 * @return tile column
	 * @since 3.3.0
	 */
	public static TileColumn createColumn(int index, String name,
			GeoPackageDataType type, Long max) {
		return createColumn(index, name, type, max, false, null);
	}

	/**
	 * Create a new column
	 * 
	 * @param name
	 *            name
	 * @param type
	 *            type
	 * @param max
	 *            max value
	 * @param notNull
	 *            not null flag
	 * @param defaultValue
	 *            default value
	 * @return tile column
	 * @since 3.3.0
	 */
	public static TileColumn createColumn(String name, GeoPackageDataType type,
			Long max, boolean notNull, Object defaultValue) {
		return createColumn(NO_INDEX, name, type, max, notNull, defaultValue);
	}

	/**
	 * Create a new column
	 * 
	 * @param index
	 *            index
	 * @param name
	 *            name
	 * @param type
	 *            type
	 * @param max
	 *            max value
	 * @param notNull
	 *            not null flag
	 * @param defaultValue
	 *            default value
	 * @return tile column
	 */
	public static TileColumn createColumn(int index, String name,
			GeoPackageDataType type, Long max, boolean notNull,
			Object defaultValue) {
		return new TileColumn(index, name, type, max, notNull, defaultValue,
				false, false);
	}

	/**
	 * Create a new column
	 * 
	 * @param tableColumn
	 *            table column
	 * @return tile column
	 * @since 3.3.0
	 */
	public static TileColumn createColumn(TableColumn tableColumn) {
		return new TileColumn(tableColumn);
	}

	/**
	 * Constructor
	 * 
	 * @param index
	 *            index
	 * @param name
	 *            name
	 * @param dataType
	 *            data type
	 * @param max
	 *            max value
	 * @param notNull
	 *            not null flag
	 * @param defaultValue
	 *            default value
	 * @param primaryKey
	 *            primary key
	 * @param autoincrement
	 *            autoincrement flag
	 */
	private TileColumn(int index, String name, GeoPackageDataType dataType,
			Long max, boolean notNull, Object defaultValue, boolean primaryKey,
			boolean autoincrement) {
		super(index, name, dataType, max, notNull, defaultValue, primaryKey,
				autoincrement);
	}

	/**
	 * Constructor
	 * 
	 * @param tableColumn
	 *            table column
	 */
	private TileColumn(TableColumn tableColumn) {
		super(tableColumn);
	}

	/**
	 * Copy Constructor
	 * 
	 * @param tileColumn
	 *            tile column
	 * @since 3.3.0
	 */
	public TileColumn(TileColumn tileColumn) {
		super(tileColumn);
	}

	/**
	 * Copy the column
	 * 
	 * @return copied column
	 * @since 3.3.0
	 */
	public TileColumn copy() {
		return new TileColumn(this);
	}

}
