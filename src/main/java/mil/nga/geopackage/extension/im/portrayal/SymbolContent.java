package mil.nga.geopackage.extension.im.portrayal;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Describes the symbol content, e.g., actual files containing symbol(s)
 * 
 * @author jyutzler
 * @since 4.0.0
 */
@DatabaseTable(tableName = "gpkgext_symbol_content", daoClass = SymbolContentDao.class)
public class SymbolContent {
    public static final String TABLE_NAME = "gpkgext_symbol_content";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_FORMAT = "format";
    public static final String COLUMN_CONTENT = "content";

    @DatabaseField(columnName = COLUMN_ID, generatedId = true, canBeNull = false)
    private long id;

    @DatabaseField(columnName = COLUMN_FORMAT)
    private String format;

    @DatabaseField(columnName = COLUMN_CONTENT, dataType = DataType.BYTE_ARRAY)
    private byte[] content;

    public SymbolContent() {
    }

    public SymbolContent(long id, String format, byte[] content) {
        this.id = id;
        this.format = format;
        this.content = content;
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

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
}
