package mil.nga.geopackage.extension.im.portrayal;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Describes the symbols
 * 
 * @author jyutzler
 * @since 4.0.0
 */
@DatabaseTable(tableName = "gpkgext_symbols", daoClass = SymbolsDao.class)
public class Symbols {
    public static final String TABLE_NAME = "gpkgext_symbols";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_SYMBOL = "symbol";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_URI = "uri";

    @DatabaseField(columnName = COLUMN_ID, generatedId = true, canBeNull = false)
    private long id;

    @DatabaseField(columnName = COLUMN_SYMBOL, canBeNull = false)
    private String symbol;

    @DatabaseField(columnName = COLUMN_DESCRIPTION)
    private String description;

    @DatabaseField(columnName = COLUMN_URI)
    private String uri;

    public Symbols() {
    }

    public Symbols(long id, String symbol, String description, String uri) {
        this.id = id;
        this.symbol = symbol;
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

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
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
