package mil.nga.geopackage.tiles.user;

import java.util.ArrayList;
import java.util.List;

import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.contents.Contents;
import mil.nga.geopackage.contents.ContentsDataType;
import mil.nga.geopackage.db.table.UniqueConstraint;
import mil.nga.geopackage.user.UserTable;

/**
 * Represents a user tile table
 * 
 * @author osbornb
 */
public class TileTable extends UserTable<TileColumn> {

	/**
	 * Id column name, Requirement 52
	 */
	public static final String COLUMN_ID = TileColumns.ID;

	/**
	 * Zoom level column name, Requirement 53
	 */
	public static final String COLUMN_ZOOM_LEVEL = TileColumns.ZOOM_LEVEL;

	/**
	 * Tile column column name, Requirement 54
	 */
	public static final String COLUMN_TILE_COLUMN = TileColumns.TILE_COLUMN;

	/**
	 * Tile row column name, Requirement 55
	 */
	public static final String COLUMN_TILE_ROW = TileColumns.TILE_ROW;

	/**
	 * Tile ID column name, implied requirement
	 */
	public static final String COLUMN_TILE_DATA = TileColumns.TILE_DATA;

	/**
	 * Constructor
	 *
	 * @param tableName
	 *            table name
	 * @since 4.0.0
	 */
	public TileTable(String tableName) {
		this(tableName, createRequiredColumns());
	}

	/**
	 * Constructor
	 * 
	 * @param tableName
	 *            table name
	 * @param columns
	 *            columns
	 */
	public TileTable(String tableName, List<TileColumn> columns) {
		super(new TileColumns(tableName, columns));

		// Build a unique constraint on zoom level, tile column, and tile data
		UniqueConstraint uniqueConstraint = new UniqueConstraint();
		uniqueConstraint.add(getUserColumns().getZoomLevelColumn());
		uniqueConstraint.add(getUserColumns().getTileColumnColumn());
		uniqueConstraint.add(getUserColumns().getTileRowColumn());

		// Add the unique constraint
		addConstraint(uniqueConstraint);

	}

	/**
	 * Copy Constructor
	 * 
	 * @param tileTable
	 *            tile table
	 * @since 3.3.0
	 */
	public TileTable(TileTable tileTable) {
		super(tileTable);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TileTable copy() {
		return new TileTable(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDataType() {
		return getDataType(ContentsDataType.TILES.getName());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TileColumns getUserColumns() {
		return (TileColumns) super.getUserColumns();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TileColumns createUserColumns(List<TileColumn> columns) {
		return new TileColumns(getTableName(), columns, true);
	}

	/**
	 * Get the zoom level column index
	 * 
	 * @return zoom level index
	 */
	public int getZoomLevelColumnIndex() {
		return getUserColumns().getZoomLevelIndex();
	}

	/**
	 * Get the zoom level column
	 * 
	 * @return tile column
	 */
	public TileColumn getZoomLevelColumn() {
		return getUserColumns().getZoomLevelColumn();
	}

	/**
	 * Get the tile column column index
	 * 
	 * @return tile column index
	 */
	public int getTileColumnColumnIndex() {
		return getUserColumns().getTileColumnIndex();
	}

	/**
	 * Get the tile column column
	 * 
	 * @return tile column
	 */
	public TileColumn getTileColumnColumn() {
		return getUserColumns().getTileColumnColumn();
	}

	/**
	 * Get the tile row column index
	 * 
	 * @return tile row index
	 */
	public int getTileRowColumnIndex() {
		return getUserColumns().getTileRowIndex();
	}

	/**
	 * Get the tile row column
	 * 
	 * @return tile column
	 */
	public TileColumn getTileRowColumn() {
		return getUserColumns().getTileRowColumn();
	}

	/**
	 * Get the tile data column index
	 * 
	 * @return tile data index
	 */
	public int getTileDataColumnIndex() {
		return getUserColumns().getTileDataIndex();
	}

	/**
	 * Get the tile data column
	 * 
	 * @return tile column
	 */
	public TileColumn getTileDataColumn() {
		return getUserColumns().getTileDataColumn();
	}

	/**
	 * Create the required table columns
	 * 
	 * @return tile columns
	 */
	public static List<TileColumn> createRequiredColumns() {
		return createRequiredColumns(UserTable.DEFAULT_AUTOINCREMENT);
	}

	/**
	 * Create the required table columns
	 * 
	 * @param autoincrement
	 *            autoincrement id values
	 * 
	 * @return tile columns
	 * @since 4.0.0
	 */
	public static List<TileColumn> createRequiredColumns(
			boolean autoincrement) {

		List<TileColumn> columns = new ArrayList<TileColumn>();
		columns.add(TileColumn.createIdColumn(autoincrement));
		columns.add(TileColumn.createZoomLevelColumn());
		columns.add(TileColumn.createTileColumnColumn());
		columns.add(TileColumn.createTileRowColumn());
		columns.add(TileColumn.createTileDataColumn());

		return columns;
	}

	/**
	 * Create the required table columns, starting at the provided index
	 * 
	 * @param startingIndex
	 *            starting index
	 * @return tile columns
	 */
	public static List<TileColumn> createRequiredColumns(int startingIndex) {
		return createRequiredColumns(startingIndex,
				UserTable.DEFAULT_AUTOINCREMENT);
	}

	/**
	 * Create the required table columns, starting at the provided index
	 * 
	 * @param startingIndex
	 *            starting index
	 * @param autoincrement
	 *            autoincrement id values
	 * @return tile columns
	 * @since 4.0.0
	 */
	public static List<TileColumn> createRequiredColumns(int startingIndex,
			boolean autoincrement) {

		List<TileColumn> columns = new ArrayList<TileColumn>();
		columns.add(TileColumn.createIdColumn(startingIndex++, autoincrement));
		columns.add(TileColumn.createZoomLevelColumn(startingIndex++));
		columns.add(TileColumn.createTileColumnColumn(startingIndex++));
		columns.add(TileColumn.createTileRowColumn(startingIndex++));
		columns.add(TileColumn.createTileDataColumn(startingIndex++));

		return columns;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void validateContents(Contents contents) {
		// Verify the Contents have a tiles data type
		if (!contents.isTilesTypeOrUnknown()) {
			throw new GeoPackageException(
					"The " + Contents.class.getSimpleName() + " of a "
							+ TileTable.class.getSimpleName()
							+ " must have a data type of "
							+ ContentsDataType.TILES.getName()
							+ ". actual type: " + contents.getDataTypeName());
		}
	}

}
