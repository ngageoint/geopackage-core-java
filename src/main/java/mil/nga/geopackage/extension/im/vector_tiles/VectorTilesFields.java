package mil.nga.geopackage.extension.im.vector_tiles;


import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Describes the fields in a vector tiles set
 * 
 * @author jyutzler
 * @since 4.0.0
 */
@DatabaseTable(tableName = "gpkgext_vt_fields", daoClass = VectorTilesFieldsDao.class)
public class VectorTilesFields {
    public static final String TABLE_NAME = "gpkgext_vt_fields";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_LAYER_ID = "layer_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_TYPE = "type";

    @DatabaseField(columnName = COLUMN_ID, generatedId = true, canBeNull = false)
    private long id;

    @DatabaseField(columnName = COLUMN_LAYER_ID, canBeNull = false)
    private long layerId;

    @DatabaseField(columnName = COLUMN_NAME, canBeNull = false)
    private String name;

    @DatabaseField(columnName = COLUMN_TYPE, canBeNull = false)
    private String type;

    public VectorTilesFields() {
    }

    public VectorTilesFields(long id, long layerId, String name, String type) {
        this.id = id;
        this.layerId = layerId;
        this.name = name;
        this.type = type;
    }

    /**
     * Reset the id so the row can be inserted as new
     */
    public void resetId() {
        this.id = 0;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getLayerId() {
        return layerId;
    }

    public void setLayerId(long layerId) {
        this.layerId = layerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
