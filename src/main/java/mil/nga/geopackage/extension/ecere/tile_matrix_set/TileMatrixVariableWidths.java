package mil.nga.geopackage.extension.ecere.tile_matrix_set;


import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Describes the tables using a particular tile matrix
 * 
 * @author jyutzler
 * @since 4.0.0
 */
@DatabaseTable(tableName = TileMatrixVariableWidths.TABLE_NAME, daoClass = TileMatrixVariableWidthsDao.class)
public class TileMatrixVariableWidths {
    public final static String TABLE_NAME = "gpkgext_tile_matrix_variable_widths";
    public final static String COLUMN_ID = "id";
    public final static String COLUMN_TM_ID = "tm_id";
    public final static String COLUMN_MIN_ROW = "min_row";
    public final static String COLUMN_MAX_ROW = "max_row";
    public final static String COLUMN_COALESCE = "coalesce";

    @DatabaseField(columnName = COLUMN_ID, generatedId = true, canBeNull = false)
    private long id;


    @DatabaseField(columnName = COLUMN_TM_ID, foreign = true, foreignAutoRefresh = true)
    private ExtTileMatrix tm;

    @DatabaseField(columnName = COLUMN_TM_ID, readOnly = true)
    private long tmsId;

    @DatabaseField(columnName = COLUMN_MAX_ROW)
    private long maxRow;

    @DatabaseField(columnName = COLUMN_MIN_ROW)
    private long minRow;

    @DatabaseField(columnName = COLUMN_COALESCE)
    private long coalesce;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ExtTileMatrix getTm() {
        return tm;
    }

    public void setTm(ExtTileMatrix tm) {
        this.tm = tm;
    }

    public long getTmsId() {
        return tmsId;
    }

    public void setTmsId(long tmsId) {
        this.tmsId = tmsId;
    }

    public long getMaxRow() {
        return maxRow;
    }

    public void setMaxRow(long maxRow) {
        this.maxRow = maxRow;
    }

    public long getMinRow() {
        return minRow;
    }

    public void setMinRow(long minRow) {
        this.minRow = minRow;
    }

    public long getCoalesce() {
        return coalesce;
    }

    public void setCoalesce(long coalesce) {
        this.coalesce = coalesce;
    }
}
