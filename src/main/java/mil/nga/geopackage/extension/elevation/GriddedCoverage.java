package mil.nga.geopackage.extension.elevation;

import mil.nga.geopackage.tiles.matrixset.TileMatrixSet;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Gridded Coverage object
 * 
 * @author osbornb
 * @since 1.2.1
 */
@DatabaseTable(tableName = "gpkg_2d_gridded_coverage_ancillary", daoClass = GriddedCoverageDao.class)
public class GriddedCoverage {

	/**
	 * Table name
	 */
	public static final String TABLE_NAME = "gpkg_2d_gridded_coverage_ancillary";

	/**
	 * id field name
	 */
	public static final String COLUMN_ID = "id";

	/**
	 * tileMatrixSetName column
	 */
	public static final String COLUMN_TILE_MATRIX_SET_NAME = "tile_matrix_set_name";

	/**
	 * datatype column
	 */
	public static final String COLUMN_DATATYPE = "datatype";

	/**
	 * scale column
	 */
	public static final String COLUMN_SCALE = "scale";

	/**
	 * offset column
	 */
	public static final String COLUMN_OFFSET = "offset";

	/**
	 * precision column
	 */
	public static final String COLUMN_PRECISION = "precision";

	/**
	 * dataNull column
	 */
	public static final String COLUMN_DATA_NULL = "data_null";

	/**
	 * dataMissing column
	 */
	public static final String COLUMN_DATA_MISSING = "data_missing";

	/**
	 * Auto increment primary key
	 */
	@DatabaseField(columnName = COLUMN_ID, id = true, canBeNull = false, generatedId = true)
	private long id;

	/**
	 * Foreign key to TileMatrixSet by tile matrix set name
	 */
	@DatabaseField(columnName = COLUMN_TILE_MATRIX_SET_NAME, canBeNull = false, foreign = true, foreignAutoRefresh = true)
	private TileMatrixSet tileMatrixSet;

	/**
	 * Foreign key to table_name in gpkg_tile_matrix_set
	 */
	@DatabaseField(columnName = COLUMN_TILE_MATRIX_SET_NAME, canBeNull = false)
	private String tileMatrixSetName;

	/**
	 * 'integer' or 'float'
	 */
	@DatabaseField(columnName = COLUMN_DATATYPE, canBeNull = false)
	private String datatype;

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
	 * The smallest value that has meaning for this dataset
	 */
	@DatabaseField(columnName = COLUMN_PRECISION)
	private Float precision;

	/**
	 * The value that indicates NULL
	 */
	@DatabaseField(columnName = COLUMN_DATA_NULL)
	private Float dataNull;

	/**
	 * The value that indicates data is missing
	 */
	@DatabaseField(columnName = COLUMN_DATA_MISSING)
	private Float dataMissing;

	/**
	 * Default Constructor
	 */
	public GriddedCoverage() {

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
	 * Get the tile matrix set
	 * 
	 * @return tile matrix set
	 */
	public TileMatrixSet getTileMatrixSet() {
		return tileMatrixSet;
	}

	/**
	 * Set the tile matrix set
	 * 
	 * @param tileMatrixSet
	 *            tile matrix set
	 */
	public void setTileMatrixSet(TileMatrixSet tileMatrixSet) {
		this.tileMatrixSet = tileMatrixSet;
		if (tileMatrixSet != null) {
			tileMatrixSetName = tileMatrixSet.getTableName();
		} else {
			tileMatrixSetName = null;
		}
	}

	/**
	 * Get the tile matrix set name
	 * 
	 * @return tile matrix set name
	 */
	public String getTileMatrixSetName() {
		return tileMatrixSetName;
	}

	/**
	 * Get the data type
	 * 
	 * @return data type
	 */
	public GriddedCoverageDataType getDataType() {
		return GriddedCoverageDataType.fromName(datatype);
	}

	/**
	 * Set the data type
	 * 
	 * @param datatype
	 *            data type
	 */
	public void setDataType(GriddedCoverageDataType datatype) {
		this.datatype = datatype.getName();
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
	 * Get the precision
	 * 
	 * @return smallest value that has meaning for this dataset
	 */
	public Float getPrecision() {
		return precision;
	}

	/**
	 * Set the precision
	 * 
	 * @param precision
	 *            smallest value that has meaning for this dataset
	 */
	public void setPrecision(Float precision) {
		this.precision = precision;
	}

	/**
	 * Get the value that indicates NULL
	 * 
	 * @return value that indicates NULL
	 */
	public Float getDataNull() {
		return dataNull;
	}

	/**
	 * Set the value that indicates NULL
	 * 
	 * @param dataNull
	 *            value that indicates NULL
	 */
	public void setDataNull(Float dataNull) {
		this.dataNull = dataNull;
	}

	/**
	 * Get the value that indicates data is missing
	 * 
	 * @return value that indicates data is missing
	 */
	public Float getDataMissing() {
		return dataMissing;
	}

	/**
	 * Set the value that indicates data is missing
	 * 
	 * @param dataMissing
	 *            value that indicates data is missing
	 */
	public void setDataMissing(Float dataMissing) {
		this.dataMissing = dataMissing;
	}

}
