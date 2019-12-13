package mil.nga.geopackage.tiles.user;

import java.util.List;

import mil.nga.geopackage.db.GeoPackageDataType;
import mil.nga.geopackage.user.UserColumns;

/**
 * Collection of tile columns
 * 
 * @author osbornb
 * @since 3.5.0
 */
public class TileColumns extends UserColumns<TileColumn> {

	/**
	 * Id column name, Requirement 52
	 */
	public static final String ID = "id";

	/**
	 * Zoom level column name, Requirement 53
	 */
	public static final String ZOOM_LEVEL = "zoom_level";

	/**
	 * Tile column column name, Requirement 54
	 */
	public static final String TILE_COLUMN = "tile_column";

	/**
	 * Tile row column name, Requirement 55
	 */
	public static final String TILE_ROW = "tile_row";

	/**
	 * Tile ID column name, implied requirement
	 */
	public static final String TILE_DATA = "tile_data";

	/**
	 * Zoom level column index
	 */
	private int zoomLevelIndex = -1;

	/**
	 * Tile column column index
	 */
	private int tileColumnIndex = -1;

	/**
	 * Tile row column index
	 */
	private int tileRowIndex = -1;

	/**
	 * Tile data column index
	 */
	private int tileDataIndex = -1;

	/**
	 * Constructor
	 * 
	 * @param tableName
	 *            table name
	 * @param columns
	 *            columns
	 */
	public TileColumns(String tableName, List<TileColumn> columns) {
		this(tableName, columns, false);
	}

	/**
	 * Constructor
	 * 
	 * @param tableName
	 *            table name
	 * @param columns
	 *            columns
	 * @param custom
	 *            custom column specification
	 */
	public TileColumns(String tableName, List<TileColumn> columns,
			boolean custom) {
		super(tableName, columns, custom);

		updateColumns();
	}

	/**
	 * Copy Constructor
	 * 
	 * @param tileColumns
	 *            tile columns
	 */
	public TileColumns(TileColumns tileColumns) {
		super(tileColumns);
		this.zoomLevelIndex = tileColumns.zoomLevelIndex;
		this.tileColumnIndex = tileColumns.tileColumnIndex;
		this.tileRowIndex = tileColumns.tileRowIndex;
		this.tileDataIndex = tileColumns.tileDataIndex;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TileColumns copy() {
		return new TileColumns(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void updateColumns() {
		super.updateColumns();

		// Find the required columns

		Integer zoomLevel = getColumnIndex(ZOOM_LEVEL, false);
		if (!isCustom()) {
			missingCheck(zoomLevel, ZOOM_LEVEL);
		}
		if (zoomLevel != null) {
			typeCheck(GeoPackageDataType.INTEGER, getColumn(zoomLevel));
			zoomLevelIndex = zoomLevel;
		}

		Integer tileColumn = getColumnIndex(TILE_COLUMN, false);
		if (!isCustom()) {
			missingCheck(tileColumn, TILE_COLUMN);
		}
		if (tileColumn != null) {
			typeCheck(GeoPackageDataType.INTEGER, getColumn(tileColumn));
			tileColumnIndex = tileColumn;
		}

		Integer tileRow = getColumnIndex(TILE_ROW, false);
		if (!isCustom()) {
			missingCheck(tileRow, TILE_ROW);
		}
		if (tileRow != null) {
			typeCheck(GeoPackageDataType.INTEGER, getColumn(tileRow));
			tileRowIndex = tileRow;
		}

		Integer tileData = getColumnIndex(TILE_DATA, false);
		if (!isCustom()) {
			missingCheck(tileData, TILE_DATA);
		}
		if (tileData != null) {
			typeCheck(GeoPackageDataType.BLOB, getColumn(tileData));
			tileDataIndex = tileData;
		}

	}

	/**
	 * Get the zoom level index
	 * 
	 * @return zoom level index
	 */
	public int getZoomLevelIndex() {
		return zoomLevelIndex;
	}

	/**
	 * Set the zoom level index
	 * 
	 * @param zoomLevelIndex
	 *            zoom level index
	 */
	public void setZoomLevelIndex(int zoomLevelIndex) {
		this.zoomLevelIndex = zoomLevelIndex;
	}

	/**
	 * Check if has a zoom level column
	 * 
	 * @return true if has a zoom level column
	 */
	public boolean hasZoomLevelColumn() {
		return zoomLevelIndex >= 0;
	}

	/**
	 * Get the zoom level column
	 * 
	 * @return zoom level column
	 */
	public TileColumn getZoomLevelColumn() {
		TileColumn column = null;
		if (hasZoomLevelColumn()) {
			column = getColumn(zoomLevelIndex);
		}
		return column;
	}

	/**
	 * Get the tile column index
	 * 
	 * @return tile column index
	 */
	public int getTileColumnIndex() {
		return tileColumnIndex;
	}

	/**
	 * Set the tile column index
	 * 
	 * @param tileColumnIndex
	 *            tile column index
	 */
	public void setTileColumnIndex(int tileColumnIndex) {
		this.tileColumnIndex = tileColumnIndex;
	}

	/**
	 * Check if has a tile column column
	 * 
	 * @return true if has a tile column column
	 */
	public boolean hasTileColumnColumn() {
		return tileColumnIndex >= 0;
	}

	/**
	 * Get the tile column column
	 * 
	 * @return tile column column
	 */
	public TileColumn getTileColumnColumn() {
		TileColumn column = null;
		if (hasTileColumnColumn()) {
			column = getColumn(tileColumnIndex);
		}
		return column;
	}

	/**
	 * Get the tile row index
	 * 
	 * @return tile row index
	 */
	public int getTileRowIndex() {
		return tileRowIndex;
	}

	/**
	 * Set the tile row index
	 * 
	 * @param tileRowIndex
	 *            tile row index
	 */
	public void setTileRowIndex(int tileRowIndex) {
		this.tileRowIndex = tileRowIndex;
	}

	/**
	 * Check if has a tile row column
	 * 
	 * @return true if has a tile row column
	 */
	public boolean hasTileRowColumn() {
		return tileRowIndex >= 0;
	}

	/**
	 * Get the tile row column
	 * 
	 * @return tile row column
	 */
	public TileColumn getTileRowColumn() {
		TileColumn column = null;
		if (hasTileRowColumn()) {
			column = getColumn(tileRowIndex);
		}
		return column;
	}

	/**
	 * Get the tile data index
	 * 
	 * @return tile data index
	 */
	public int getTileDataIndex() {
		return tileDataIndex;
	}

	/**
	 * Set the tile data index
	 * 
	 * @param tileDataIndex
	 *            tile data index
	 */
	public void setTileDataIndex(int tileDataIndex) {
		this.tileDataIndex = tileDataIndex;
	}

	/**
	 * Check if has a tile data column
	 * 
	 * @return true if has a tile data column
	 */
	public boolean hasTileDataColumn() {
		return tileDataIndex >= 0;
	}

	/**
	 * Get the tile data column
	 * 
	 * @return tile data column
	 */
	public TileColumn getTileDataColumn() {
		TileColumn column = null;
		if (hasTileDataColumn()) {
			column = getColumn(tileDataIndex);
		}
		return column;
	}

}
