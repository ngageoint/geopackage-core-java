package mil.nga.geopackage.extension.im.portrayal;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Describes the stylesheets
 * 
 * @author jyutzler
 * @since 4.0.0
 */
@DatabaseTable(tableName = "gpkgext_stylesheets", daoClass = StylesheetsDao.class)
public class Stylesheets {
    public static final String TABLE_NAME = "gpkgext_stylesheets";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_STYLE_ID = "style_id";
    public static final String COLUMN_FORMAT = "format";
    public static final String COLUMN_STYLESHEET = "stylesheet";

    @DatabaseField(columnName = COLUMN_ID, generatedId = true, canBeNull = false)
    private long id;

    @DatabaseField(columnName = COLUMN_STYLE_ID, canBeNull = false)
    private long style_id;

    @DatabaseField(columnName = COLUMN_FORMAT)
    private String format;

    @DatabaseField(columnName = COLUMN_STYLESHEET, dataType = DataType.BYTE_ARRAY)
    private byte[] stylesheet;

    public Stylesheets() {
    }

    public Stylesheets(long id, long style_id, String format, byte[] stylesheet) {
        this.id = id;
        this.style_id = style_id;
        this.format = format;
        this.stylesheet = stylesheet;
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

    public long getStyle_id() {
        return style_id;
    }

    public void setStyle_id(long style_id) {
        this.style_id = style_id;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public Object getStylesheet() {
        return stylesheet;
    }

    public void setStylesheet(byte[] stylesheet) {
        this.stylesheet = stylesheet;
    }
}
