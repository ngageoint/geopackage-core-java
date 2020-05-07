package mil.nga.geopackage.extension.im.vector_tiles;


import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Describes the layers in a vector tiles set
 * 
 * @author jyutzler
 * @since 4.0.0
 */
@DatabaseTable(tableName = "gpkgext_vt_layers", daoClass = VectorTilesLayersDao.class)
public class VectorTilesLayers {
    public static final String TABLE_NAME = "gpkgext_vt_layers";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TABLE_NAME = "table_name";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_MINZOOM = "minzoom";
    public static final String COLUMN_MAXZOOM = "maxzoom";
    public static final String COLUMN_ATTRIBUTES_TABLE_NAME = "attributes_table_name";

    @DatabaseField(columnName = COLUMN_ID, generatedId = true, canBeNull = false)
    private long id;

    @DatabaseField(columnName = COLUMN_TABLE_NAME, canBeNull = false)
    private String tableName;

    @DatabaseField(columnName = COLUMN_NAME, canBeNull = false)
    private String name;

    @DatabaseField(columnName = COLUMN_DESCRIPTION, canBeNull = true)
    private String description;

    @DatabaseField(columnName = COLUMN_MINZOOM, canBeNull = true)
    private long minZoom;

    @DatabaseField(columnName = COLUMN_MAXZOOM, canBeNull = true)
    private long maxZoom;

    @DatabaseField(columnName = COLUMN_ATTRIBUTES_TABLE_NAME, canBeNull = true)
    private String attributesTableName;

    public VectorTilesLayers() {
    }

    public VectorTilesLayers(long id, String tableName, String name, String description, long minZoom, long maxZoom, String attributesTableName) {
        this.id = id;
        this.tableName = tableName;
        this.name = name;
        this.description = description;
        this.minZoom = minZoom;
        this.maxZoom = maxZoom;
        this.attributesTableName = attributesTableName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    /**
     * Reset the id so the row can be inserted as new
     */
    public void resetId() {
        this.id = 0;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getMinZoom() {
        return minZoom;
    }

    public void setMinZoom(long minZoom) {
        this.minZoom = minZoom;
    }

    public long getMaxZoom() {
        return maxZoom;
    }

    public void setMaxZoom(long maxZoom) {
        this.maxZoom = maxZoom;
    }

    public String getAttributesTableName() {
        return attributesTableName;
    }

    public void setAttributesTableName(String attributesTableName) {
        this.attributesTableName = attributesTableName;
    }
}
