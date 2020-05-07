package mil.nga.geopackage.extension.im.portrayal;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Describes the styles
 * 
 * @author jyutzler
 * @since 4.0.0
 */
@DatabaseTable(tableName = "gpkgext_styles", daoClass = StylesDao.class)
public class Styles {
    public static final String TABLE_NAME = "gpkgext_styles";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_STYLE = "style";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_URI = "uri";

    @DatabaseField(columnName = COLUMN_ID, generatedId = true, canBeNull = false)
    private long id;

    @DatabaseField(columnName = COLUMN_STYLE, canBeNull = false)
    private String style;

    @DatabaseField(columnName = COLUMN_DESCRIPTION)
    private String description;

    @DatabaseField(columnName = COLUMN_URI)
    private String uri;

    public Styles() {
    }

    public Styles(long id, String style, String description, String uri) {
        this.id = id;
        this.style = style;
        this.description = description;
        this.uri = uri;
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

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
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
}
