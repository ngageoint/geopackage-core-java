package mil.nga.geopackage.extension.ecere.tile_matrix_set;


import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Describes a tile matrix
 * and is used to populate the `gpkg_tile_matrix` view
 * 
 * @author jyutzler
 * @since 4.0.0
 */
@DatabaseTable(tableName = ExtTileMatrix.TABLE_NAME, daoClass = ExtTileMatrixDao.class)
public class ExtTileMatrix {
    public static final String TABLE_NAME = "gpkgext_tile_matrix";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TMS_ID = "tms_id";
    public static final String COLUMN_ZOOM_LEVEL = "zoom_level";
    public static final String COLUMN_MATRIX_WIDTH = "matrix_width";
    public static final String COLUMN_MATRIX_HEIGHT = "matrix_height";
    public static final String COLUMN_TILE_WIDTH = "tile_width";
    public static final String COLUMN_TILE_HEIGHT = "tile_height";
    public static final String COLUMN_PIXEL_X_SIZE = "pixel_x_size";
    public static final String COLUMN_PIXEL_Y_SIZE = "pixel_y_size";
    public static final String COLUMN_TOP = "top";
    public static final String COLUMN_LEFT = "left";
    public static final String COLUMN_SCALE_DENOMINATOR = "scale_denominator";

    @DatabaseField(columnName = COLUMN_ID, generatedId = true, canBeNull = false)
    private long id;

    @DatabaseField(columnName = COLUMN_TMS_ID, foreign = true, foreignAutoRefresh = true)
    private ExtTileMatrixSet tms;

    @DatabaseField(columnName = COLUMN_TMS_ID, canBeNull = false, readOnly = true)
    private Long tmsId;

    @DatabaseField(columnName = COLUMN_ZOOM_LEVEL, canBeNull = false)
    private long zoomLevel;

    @DatabaseField(columnName = COLUMN_MATRIX_WIDTH, canBeNull = false)
    private long matrixWidth;

    @DatabaseField(columnName = COLUMN_MATRIX_HEIGHT, canBeNull = false)
    private long matrixHeight;

    @DatabaseField(columnName = COLUMN_TILE_WIDTH, canBeNull = false)
    private long tileWidth;

    @DatabaseField(columnName = COLUMN_TILE_HEIGHT, canBeNull = false)
    private long tileHeight;

    @DatabaseField(columnName = COLUMN_PIXEL_X_SIZE, canBeNull = false)
    private double pixelXSize;

    @DatabaseField(columnName = COLUMN_PIXEL_Y_SIZE, canBeNull = false)
    private double pixelYSize;

    @DatabaseField(columnName = COLUMN_TOP, canBeNull = false)
    private double top;

    @DatabaseField(columnName = COLUMN_LEFT, canBeNull = false)
    private double left;

    @DatabaseField(columnName = COLUMN_SCALE_DENOMINATOR)
    private Double scaleDenominator;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ExtTileMatrixSet getTms() {
        return tms;
    }

    public void setTms(ExtTileMatrixSet tms) {
        this.tms = tms;
        tmsId = tms != null ? tms.getId() : null;
    }

    public Long getTmsId() {
        return tmsId;
    }

    public long getZoomLevel() {
        return zoomLevel;
    }

    public void setZoomLevel(long zoomLevel) {
        this.zoomLevel = zoomLevel;
    }

    public long getMatrixWidth() {
        return matrixWidth;
    }

    public void setMatrixWidth(long matrixWidth) {
        this.matrixWidth = matrixWidth;
    }

    public long getMatrixHeight() {
        return matrixHeight;
    }

    public void setMatrixHeight(long matrixHeight) {
        this.matrixHeight = matrixHeight;
    }

    public long getTileWidth() {
        return tileWidth;
    }

    public void setTileWidth(long tileWidth) {
        this.tileWidth = tileWidth;
    }

    public long getTileHeight() {
        return tileHeight;
    }

    public void setTileHeight(long tileHeight) {
        this.tileHeight = tileHeight;
    }

    public double getPixelXSize() {
        return pixelXSize;
    }

    public void setPixelXSize(double pixelXSize) {
        this.pixelXSize = pixelXSize;
    }

    public double getPixelYSize() {
        return pixelYSize;
    }

    public void setPixelYSize(double pixelYSize) {
        this.pixelYSize = pixelYSize;
    }

    public double getTop() {
        return top;
    }

    public void setTop(double top) {
        this.top = top;
    }

    public double getLeft() {
        return left;
    }

    public void setLeft(double left) {
        this.left = left;
    }

    public Double getScaleDenominator() {
        return scaleDenominator;
    }

    public void setScaleDenominator(Double scaleDenominator) {
        this.scaleDenominator = scaleDenominator;
    }
}
