package mil.nga.geopackage.extension.ecere.tile_matrix_set;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Describes the tables using a particular tile matrix
 * 
 * @author jyutzler
 * @since 4.0.0
 */
@DatabaseTable(tableName = TileMatrixTable.TABLE_NAME, daoClass = TileMatrixTablesDao.class)
public class TileMatrixTable {
    public final static String TABLE_NAME = "gpkgext_tile_matrix_tables";
    public final static String COLUMN_TABLE_NAME = "table_name";
    public final static String COLUMN_TMS_ID = "tms_id";
    public final static String COLUMN_MIN_LEVEL = "min_level";
    public final static String COLUMN_MAX_LEVEL = "max_level";

    @DatabaseField(columnName = COLUMN_TABLE_NAME, canBeNull = false)
    private String tableName;

    @DatabaseField(columnName = COLUMN_TMS_ID, foreign = true, foreignAutoRefresh = true)
    private ExtTileMatrixSet tms;

    @DatabaseField(columnName = COLUMN_TMS_ID, readOnly = true)
    private Long tmsId;

    @DatabaseField(columnName = COLUMN_MAX_LEVEL)
    private long maxLevel;

    @DatabaseField(columnName = COLUMN_MIN_LEVEL)
    private long minLevel;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
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

    public long getMaxLevel() {
        return maxLevel;
    }

    public void setMaxLevel(long maxLevel) {
        this.maxLevel = maxLevel;
    }

    public long getMinLevel() {
        return minLevel;
    }

    public void setMinLevel(long minLevel) {
        this.minLevel = minLevel;
    }
}
