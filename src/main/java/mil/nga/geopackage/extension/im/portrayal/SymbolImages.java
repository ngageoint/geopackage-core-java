package mil.nga.geopackage.extension.im.portrayal;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Describes the symbol images, i.e., a manifestation of a symbol
 * 
 * @author jyutzler
 * @since 4.0.0
 */
@DatabaseTable(tableName = "gpkgext_symbol_images", daoClass = SymbolImagesDao.class)
public class SymbolImages {
    public static final String TABLE_NAME = "gpkgext_symbol_images";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_SYMBOL_ID = "symbol_id";
    public static final String COLUMN_CONTENT_ID = "content_id";
    public static final String COLUMN_WIDTH = "width";
    public static final String COLUMN_HEIGHT = "height";
    public static final String COLUMN_OFFSET_X = "offset_x";
    public static final String COLUMN_OFFSET_Y = "offset_y";
    public static final String COLUMN_PIXEL_RATIO = "pixel_ratio";

    @DatabaseField(columnName = COLUMN_ID, generatedId = true, canBeNull = false)
    private long id;

    @DatabaseField(columnName = COLUMN_SYMBOL_ID, canBeNull = false)
    private long symbol_id;

    @DatabaseField(columnName = COLUMN_CONTENT_ID, canBeNull = false)
    private long content_id;

    @DatabaseField(columnName = COLUMN_WIDTH)
    private long width;

    @DatabaseField(columnName = COLUMN_HEIGHT)
    private long height;

    @DatabaseField(columnName = COLUMN_OFFSET_X)
    private long offsetX;

    @DatabaseField(columnName = COLUMN_OFFSET_Y)
    private long offsetY;

    @DatabaseField(columnName = COLUMN_PIXEL_RATIO)
    private long pixelRatio;

    public SymbolImages() {
    }

    public SymbolImages(long id, long symbol_id, long content_id, long width, long height, long offsetX, long offsetY, long pixelRatio) {
        this.id = id;
        this.symbol_id = symbol_id;
        this.content_id = content_id;
        this.width = width;
        this.height = height;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.pixelRatio = pixelRatio;
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

    public long getSymbol_id() {
        return symbol_id;
    }

    public void setSymbol_id(long symbol_id) {
        this.symbol_id = symbol_id;
    }

    public long getContent_id() {
        return content_id;
    }

    public void setContent_id(long content_id) {
        this.content_id = content_id;
    }

    public long getWidth() {
        return width;
    }

    public void setWidth(long width) {
        this.width = width;
    }

    public long getHeight() {
        return height;
    }

    public void setHeight(long height) {
        this.height = height;
    }

    public long getOffsetX() {
        return offsetX;
    }

    public void setOffsetX(long offsetX) {
        this.offsetX = offsetX;
    }

    public long getOffsetY() {
        return offsetY;
    }

    public void setOffsetY(long offsetY) {
        this.offsetY = offsetY;
    }

    public double getPixelRatio() {
        return pixelRatio;
    }

    public void setPixelRatio(long pixelRatio) {
        this.pixelRatio = pixelRatio;
    }
}
