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
	 * Auto increment primary key
	 */
	@DatabaseField(columnName = COLUMN_ID, generatedId = true, canBeNull = false)
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
	@DatabaseField(columnName = COLUMN_SCALE, canBeNull = false)
	private double scale = 1.0;

	/**
	 * The offset to the 0 value
	 */
	@DatabaseField(columnName = COLUMN_OFFSET, canBeNull = false)
	private double offset = 0.0;

	/**
	 * The smallest value that has meaning for this dataset
	 */
	@DatabaseField(columnName = COLUMN_PRECISION)
	private Double precision = 1.0;

	/**
	 * The value that indicates NULL
	 */
	@DatabaseField(columnName = COLUMN_DATA_NULL)
	private Double dataNull;

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
	 * Get the precision
	 * 
	 * @return smallest value that has meaning for this dataset
	 */
	public double getPrecision() {
		return precision != null ? precision : 1.0;
	}

	/**
	 * Set the precision
	 * 
	 * @param precision
	 *            smallest value that has meaning for this dataset
	 */
	public void setPrecision(Double precision) {
		this.precision = precision;
	}

	/**
	 * Get the value that indicates NULL
	 * 
	 * @return value that indicates NULL
	 */
	public Double getDataNull() {
		return dataNull;
	}

	/**
	 * Set the value that indicates NULL
	 * 
	 * @param dataNull
	 *            value that indicates NULL
	 */
	public void setDataNull(Double dataNull) {
		this.dataNull = dataNull;
	}

}
