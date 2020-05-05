package mil.nga.geopackage.extension.nga.scale;

import mil.nga.geopackage.tiles.matrixset.TileMatrixSet;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Tile Scaling object, for scaling tiles from nearby zoom levels for missing
 * tiles
 * 
 * @author osbornb
 * @since 2.0.2
 */
@DatabaseTable(tableName = "nga_tile_scaling", daoClass = TileScalingDao.class)
public class TileScaling {

	/**
	 * Table name
	 */
	public static final String TABLE_NAME = "nga_tile_scaling";

	/**
	 * tableName column
	 */
	public static final String COLUMN_TABLE_NAME = "table_name";

	/**
	 * scalingType field name
	 */
	public static final String COLUMN_SCALING_TYPE = "scaling_type";

	/**
	 * zoomIn field name
	 */
	public static final String COLUMN_ZOOM_IN = "zoom_in";

	/**
	 * zoomOut field name
	 */
	public static final String COLUMN_ZOOM_OUT = "zoom_out";

	/**
	 * Foreign key to table_name in gpkg_tile_matrix_set
	 */
	@DatabaseField(columnName = COLUMN_TABLE_NAME, id = true, canBeNull = false)
	private String tableName;

	/**
	 * Tile Scaling behavior type
	 */
	@DatabaseField(columnName = COLUMN_SCALING_TYPE, canBeNull = false)
	private String scalingType;

	/**
	 * Max zoom levels in to search
	 */
	@DatabaseField(columnName = COLUMN_ZOOM_IN)
	private Long zoomIn;

	/**
	 * Max zoom levels out to search
	 */
	@DatabaseField(columnName = COLUMN_ZOOM_OUT)
	private Long zoomOut;

	/**
	 * Default Constructor
	 */
	public TileScaling() {

	}

	/**
	 * Constructor
	 * 
	 * @param tileMatrixSet
	 *            tile matrix set
	 * @param scalingType
	 *            scaling type
	 * @param zoomIn
	 *            max zoom in levels
	 * @param zoomOut
	 *            max zoom out levels
	 */
	public TileScaling(TileMatrixSet tileMatrixSet,
			TileScalingType scalingType, Long zoomIn, Long zoomOut) {
		setTileMatrixSet(tileMatrixSet);
		setScalingType(scalingType);
		this.zoomIn = zoomIn;
		this.zoomOut = zoomOut;
	}

	/**
	 * Constructor
	 * 
	 * @param tableName
	 *            table name
	 * @param scalingType
	 *            scaling type
	 * @param zoomIn
	 *            max zoom in levels
	 * @param zoomOut
	 *            max zoom out levels
	 */
	public TileScaling(String tableName, TileScalingType scalingType,
			Long zoomIn, Long zoomOut) {
		this.tableName = tableName;
		setScalingType(scalingType);
		this.zoomIn = zoomIn;
		this.zoomOut = zoomOut;
	}

	/**
	 * Constructor
	 * 
	 * @param scalingType
	 *            scaling type
	 * @param zoomIn
	 *            max zoom in levels
	 * @param zoomOut
	 *            max zoom out levels
	 */
	public TileScaling(TileScalingType scalingType, Long zoomIn, Long zoomOut) {
		setScalingType(scalingType);
		this.zoomIn = zoomIn;
		this.zoomOut = zoomOut;
	}

	/**
	 * Copy Constructor
	 * 
	 * @param tileScaling
	 *            tile scaling to copy
	 */
	public TileScaling(TileScaling tileScaling) {
		tableName = tileScaling.tableName;
		scalingType = tileScaling.scalingType;
		zoomIn = tileScaling.zoomIn;
		zoomOut = tileScaling.zoomOut;
	}

	/**
	 * Set the tile matrix set
	 * 
	 * @param tileMatrixSet
	 *            tile matrix set
	 */
	public void setTileMatrixSet(TileMatrixSet tileMatrixSet) {
		setTableName(tileMatrixSet != null ? tileMatrixSet.getTableName()
				: null);
	}

	/**
	 * Get the table name of the tile table
	 * 
	 * @return table name of the tile table
	 */
	public String getTableName() {
		return tableName;
	}

	/**
	 * Set the table name of the tile table
	 * 
	 * @param tableName
	 *            table name of the tile table
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	/**
	 * Get the tile scaling type
	 * 
	 * @return tile scaling type
	 */
	public TileScalingType getScalingType() {
		return TileScalingType.fromName(scalingType);
	}

	/**
	 * Set the tile scaling type
	 * 
	 * @param scalingType
	 *            tile scaling type
	 */
	public void setScalingType(TileScalingType scalingType) {
		this.scalingType = scalingType.getName();
	}

	/**
	 * Get the tile scaling type string value
	 * 
	 * @return tile scaling type string
	 */
	public String getScalingTypeString() {
		return scalingType;
	}

	/**
	 * Set the tile scaling type string value
	 * 
	 * @param scalingType
	 *            tile scaling type string
	 */
	public void setScalingTypeString(String scalingType) {
		this.scalingType = scalingType;
	}

	/**
	 * Get the max levels to zoom in
	 * 
	 * @return zoom in levels
	 */
	public Long getZoomIn() {
		return zoomIn;
	}

	/**
	 * Set the max levels to zoom in
	 * 
	 * @param zoomIn
	 *            zoom in levels
	 */
	public void setZoomIn(Long zoomIn) {
		this.zoomIn = zoomIn;
	}

	/**
	 * Get the max levels to zoom out
	 * 
	 * @return zoom out levels
	 */
	public Long getZoomOut() {
		return zoomOut;
	}

	/**
	 * Set the max levels to zoom out
	 * 
	 * @param zoomOut
	 *            zoom out level
	 */
	public void setZoomOut(Long zoomOut) {
		this.zoomOut = zoomOut;
	}

	/**
	 * Is zoom in tile search enabled
	 *
	 * @return true if zoom in for tiles is allowed
	 */
	public boolean isZoomIn() {
		return (zoomIn == null || zoomIn > 0) && scalingType != null
				&& getScalingType() != TileScalingType.OUT;
	}

	/**
	 * Is zoom out tile search enabled
	 *
	 * @return true if zoom out for tiles is allowed
	 */
	public boolean isZoomOut() {
		return (zoomOut == null || zoomOut > 0) && scalingType != null
				&& getScalingType() != TileScalingType.IN;
	}

}
