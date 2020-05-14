package mil.nga.geopackage.extension.ecere.tile_matrix_set;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import mil.nga.geopackage.srs.SpatialReferenceSystem;

/**
 * Describes a tile matrix set
 * and is used to populate the `gpkg_tile_matrix_set` view
 * 
 * @author jyutzler
 * @since 4.0.0
 */
@DatabaseTable(tableName = ExtTileMatrixSet.TABLE_NAME, daoClass = ExtTileMatrixSetDao.class)
public class ExtTileMatrixSet {
    public static final String TABLE_NAME = "gpkgext_tile_matrix_set";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TMS = "tms";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_URI = "uri";
    public static final String COLUMN_SRS_ID = "srs_id";
    public static final String COLUMN_MIN_X = "min_x";
    public static final String COLUMN_MIN_Y = "min_y";
    public static final String COLUMN_MAX_X = "max_x";
    public static final String COLUMN_MAX_Y = "max_y";

    @DatabaseField(columnName = COLUMN_ID, generatedId = true, canBeNull = false)
    private long id;

    @DatabaseField(columnName = COLUMN_TMS, canBeNull = false)
    private String tms;

    @DatabaseField(columnName = COLUMN_DESCRIPTION)
    private String description;

    @DatabaseField(columnName = COLUMN_URI, canBeNull = false)
    private String uri;

    /**
     * Bounding box minimum easting or longitude for the TMS
     */
    @DatabaseField(columnName = COLUMN_MIN_X)
    private Double minX;

    /**
     * Bounding box minimum northing or latitude for the TMS
     */
    @DatabaseField(columnName = COLUMN_MIN_Y)
    private Double minY;

    /**
     * Bounding box maximum easting or longitude for the TMS
     */
    @DatabaseField(columnName = COLUMN_MAX_X)
    private Double maxX;

    /**
     * Bounding box maximum northing or latitude for the TMS
     */
    @DatabaseField(columnName = COLUMN_MAX_Y)
    private Double maxY;

    /**
     * Spatial Reference System ID
     */
    @DatabaseField(columnName = COLUMN_SRS_ID, foreign = true, foreignAutoRefresh = true)
    private SpatialReferenceSystem srs;

    /**
     * Unique identifier for each Spatial Reference System within a GeoPackage
     */
    @DatabaseField(columnName = COLUMN_SRS_ID, readOnly = true)
    private Long srsId;

    public long getId() {
        return id;
    }

    public void setId(long id) { this.id = id; }

    public String getTms() {
        return tms;
    }

    public void setTms(String tms) {
        this.tms = tms;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Double getMinX() {
        return minX;
    }

    public void setMinX(Double minX) {
        this.minX = minX;
    }

    public Double getMinY() {
        return minY;
    }

    public void setMinY(Double minY) {
        this.minY = minY;
    }

    public Double getMaxX() {
        return maxX;
    }

    public void setMaxX(Double maxX) {
        this.maxX = maxX;
    }

    public Double getMaxY() {
        return maxY;
    }

    public void setMaxY(Double maxY) {
        this.maxY = maxY;
    }

    public SpatialReferenceSystem getSrs() {
        return srs;
    }

    public void setSrs(SpatialReferenceSystem srs) {

        this.srs = srs;
        srsId = srs != null ? srs.getId() : null;
    }

    public Long getSrsId() {
        return srsId;
    }
}
