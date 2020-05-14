package mil.nga.geopackage.extension.coverage;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import mil.nga.geopackage.contents.Contents;

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
	@DatabaseField(columnName = COLUMN_ID, generatedId = true, canBeNull = false)
	private long id;

	/**
	 * Foreign key to Contents by tile pyramid user data table
	 */
	@DatabaseField(columnName = COLUMN_TABLE_NAME, canBeNull = false, foreign = true, foreignAutoRefresh = true)
	private Contents contents;

	/**
	 * Name of tile pyramid user data table
	 */
	@DatabaseField(columnName = COLUMN_TABLE_NAME, canBeNull = false, readOnly = true)
	private String tableName;

	/**
	 * Foreign key to id in tile pyramid user data table
	 */
	@DatabaseField(columnName = COLUMN_TABLE_ID, canBeNull = false)
	private long tableId;

	/**
	 * Scale as a multiple relative to the unit of measure
	 */
	@DatabaseField(columnName = COLUMN_SCALE, canBeNull = false)
	private double scale = 1.0;

	/**
	 * The offset to the 0 value
	 */
	@DatabaseField(columnName = COLUMN_OFFSET, canBeNull = false)
	private double offset = 0.0;

	/**
	 * Minimum value of this tile
	 */
	@DatabaseField(columnName = COLUMN_MIN)
	private Double min;

	/**
	 * Maximum value of this tile
	 */
	@DatabaseField(columnName = COLUMN_MAX)
	private Double max;

	/**
	 * The arithmetic mean of values in this tile
	 */
	@DatabaseField(columnName = COLUMN_MEAN)
	private Double mean;

	/**
	 * The standard deviation of values in this tile
	 */
	@DatabaseField(columnName = COLUMN_STANDARD_DEVIATION)
	private Double standardDeviation;

	/**
	 * Default Constructor
	 */
	public GriddedTile() {

	}

	/**
	 * Copy Constructor
	 * 
	 * @param griddedTile
	 *            gridded tile to copy
	 * @since 1.3.0
	 */
	public GriddedTile(GriddedTile griddedTile) {
		id = griddedTile.id;
		contents = griddedTile.contents;
		tableName = griddedTile.tableName;
		tableId = griddedTile.tableId;
		scale = griddedTile.scale;
		offset = griddedTile.offset;
		min = griddedTile.min;
		max = griddedTile.max;
		mean = griddedTile.mean;
		standardDeviation = griddedTile.standardDeviation;
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
	public double getScale() {
		return scale;
	}

	/**
	 * Set the scale
	 * 
	 * @param scale
	 *            scale as a multiple relative to the unit of measure
	 */
	public void setScale(double scale) {
		this.scale = scale;
	}

	/**
	 * Get the offset
	 * 
	 * @return offset to the 0 value
	 */
	public double getOffset() {
		return offset;
	}

	/**
	 * Set the offset
	 * 
	 * @param offset
	 *            offset to the 0 value
	 */
	public void setOffset(double offset) {
		this.offset = offset;
	}

	/**
	 * Get the minimum value of this tile
	 * 
	 * @return minimum value of this tile
	 */
	public Double getMin() {
		return min;
	}

	/**
	 * Set the minimum value of this tile
	 * 
	 * @param min
	 *            minimum value of this tile
	 */
	public void setMin(Double min) {
		this.min = min;
	}

	/**
	 * Get the maximum value of this tile
	 * 
	 * @return maximum value of this tile
	 */
	public Double getMax() {
		return max;
	}

	/**
	 * Set the maximum value of this tile
	 * 
	 * @param max
	 *            maximum value of this tile
	 */
	public void setMax(Double max) {
		this.max = max;
	}

	/**
	 * Get the arithmetic mean of values in this tile
	 * 
	 * @return arithmetic mean of values in this tile
	 */
	public Double getMean() {
		return mean;
	}

	/**
	 * Set the arithmetic mean of values in this tile
	 * 
	 * @param mean
	 *            arithmetic mean of values in this tile
	 */
	public void setMean(Double mean) {
		this.mean = mean;
	}

	/**
	 * Get the standard deviation of values in this tile
	 * 
	 * @return standard deviation of values in this tile
	 */
	public Double getStandardDeviation() {
		return standardDeviation;
	}

	/**
	 * Set the standard deviation of values in this tile
	 * 
	 * @param standardDeviation
	 *            standard deviation of values in this tile
	 */
	public void setStandardDeviation(Double standardDeviation) {
		this.standardDeviation = standardDeviation;
	}

}
