package mil.nga.geopackage.extension.elevation;

import mil.nga.geopackage.core.contents.Contents;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Gridded Tile object, for indexing data within user tables
 * 
 * @author osbornb
 * @since 1.2.1
 */
@DatabaseTable(tableName = "gpkg_2d_gridded_tile_ancillary", daoClass = GriddedTileDao.class)
public class GriddedTile {

	/**
	 * Table name
	 */
	public static final String TABLE_NAME = "gpkg_2d_gridded_tile_ancillary";

	/**
	 * id field name
	 */
	public static final String COLUMN_ID = "id";

	/**
	 * tableName column
	 */
	public static final String COLUMN_TABLE_NAME = "tpudt_name";

	/**
	 * tableId column
	 */
	public static final String COLUMN_TABLE_ID = "tpudt_id";

	/**
	 * scale column
	 */
	public static final String COLUMN_SCALE = "scale";

	/**
	 * offset column
	 */
	public static final String COLUMN_OFFSET = "offset";

	/**
	 * min column
	 */
	public static final String COLUMN_MIN = "min";

	/**
	 * max column
	 */
	public static final String COLUMN_MAX = "max";

	/**
	 * mean column
	 */
	public static final String COLUMN_MEAN = "mean";

	/**
	 * standardDeviation column
	 */
	public static final String COLUMN_STANDARD_DEVIATION = "std_dev";

	/**
	 * Auto increment primary key
	 */
	@DatabaseField(columnName = COLUMN_ID, id = true, canBeNull = false, generatedId = true)
	private long id;

	/**
	 * Foreign key to Contents by tile pyramid user data table
	 */
	@DatabaseField(columnName = COLUMN_TABLE_NAME, canBeNull = false, foreign = true, foreignAutoRefresh = true)
	private Contents contents;

	/**
	 * Name of tile pyramid user data table
	 */
	@DatabaseField(columnName = COLUMN_TABLE_NAME, canBeNull = false)
	private String tableName;

	/**
	 * Foreign key to id in tile pyramid user data table
	 */
	@DatabaseField(columnName = COLUMN_TABLE_ID, canBeNull = false)
	private long tableId;

	/**
	 * Scale as a multiple relative to the unit of measure
	 */
	@DatabaseField(columnName = COLUMN_SCALE)
	private Float scale;

	/**
	 * The offset to the 0 value
	 */
	@DatabaseField(columnName = COLUMN_OFFSET)
	private Float offset;

	/**
	 * Minimum value of this tile
	 */
	@DatabaseField(columnName = COLUMN_MIN)
	private Float min;

	/**
	 * Maximum value of this tile
	 */
	@DatabaseField(columnName = COLUMN_MAX)
	private Float max;

	/**
	 * The arithmetic mean of values in this tile
	 */
	@DatabaseField(columnName = COLUMN_MEAN)
	private Float mean;

	/**
	 * The standard deviation of values in this tile
	 */
	@DatabaseField(columnName = COLUMN_STANDARD_DEVIATION)
	private Float standardDeviation;

	/**
	 * Default Constructor
	 */
	public GriddedTile() {

	}

	/**
	 * Get the id
	 * 
	 * @return id
	 */
	public long getId() {
		return id;
	}

	/**
	 * Get the contents
	 * 
	 * @return contents
	 */
	public Contents getContents() {
		return contents;
	}

	/**
	 * Set the contents
	 * 
	 * @param contents
	 *            contents
	 */
	public void setContents(Contents contents) {
		this.contents = contents;
		if (contents != null) {
			tableName = contents.getTableName();
		} else {
			tableName = null;
		}
	}

	/**
	 * Get the name of tile pyramid user data table
	 * 
	 * @return name of tile pyramid user data table
	 */
	public String getTableName() {
		return tableName;
	}

	/**
	 * Get the table id
	 * 
	 * @return table id
	 */
	public long getTableId() {
		return tableId;
	}

	/**
	 * Set the table id
	 * 
	 * @param tableId
	 *            table id
	 */
	public void setTableId(long tableId) {
		this.tableId = tableId;
	}

	/**
	 * Get the scale
	 * 
	 * @return scale as a multiple relative to the unit of measure
	 */
	public Float getScale() {
		return scale;
	}

	/**
	 * Set the scale
	 * 
	 * @param scale
	 *            scale as a multiple relative to the unit of measure
	 */
	public void setScale(Float scale) {
		this.scale = scale;
	}

	/**
	 * Get the offset
	 * 
	 * @return offset to the 0 value
	 */
	public Float getOffset() {
		return offset;
	}

	/**
	 * Set the offset
	 * 
	 * @param offset
	 *            offset to the 0 value
	 */
	public void setOffset(Float offset) {
		this.offset = offset;
	}

	/**
	 * Get the minimum value of this tile
	 * 
	 * @return minimum value of this tile
	 */
	public Float getMin() {
		return min;
	}

	/**
	 * Set the minimum value of this tile
	 * 
	 * @param min
	 *            minimum value of this tile
	 */
	public void setMin(Float min) {
		this.min = min;
	}

	/**
	 * Get the maximum value of this tile
	 * 
	 * @return maximum value of this tile
	 */
	public Float getMax() {
		return max;
	}

	/**
	 * Set the maximum value of this tile
	 * 
	 * @param max
	 *            maximum value of this tile
	 */
	public void setMax(Float max) {
		this.max = max;
	}

	/**
	 * Get the arithmetic mean of values in this tile
	 * 
	 * @return arithmetic mean of values in this tile
	 */
	public Float getMean() {
		return mean;
	}

	/**
	 * Set the arithmetic mean of values in this tile
	 * 
	 * @param mean
	 *            arithmetic mean of values in this tile
	 */
	public void setMean(Float mean) {
		this.mean = mean;
	}

	/**
	 * Get the standard deviation of values in this tile
	 * 
	 * @return standard deviation of values in this tile
	 */
	public Float getStandardDeviation() {
		return standardDeviation;
	}

	/**
	 * Set the standard deviation of values in this tile
	 * 
	 * @param standardDeviation
	 *            standard deviation of values in this tile
	 */
	public void setStandardDeviation(Float standardDeviation) {
		this.standardDeviation = standardDeviation;
	}

}
