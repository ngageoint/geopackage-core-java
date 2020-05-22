package mil.nga.geopackage.tiles.user;

import java.util.List;

import mil.nga.geopackage.BoundingBox;
import mil.nga.geopackage.contents.ContentsDataType;
import mil.nga.geopackage.user.UserTableMetadata;

/**
 * Tile Table Metadata for defining table creation information
 * 
 * @author osbornb
 * @since 4.0.0
 */
public class TileTableMetadata extends UserTableMetadata<TileColumn> {

	/**
	 * Default data type
	 */
	public static final String DEFAULT_DATA_TYPE = ContentsDataType.TILES
			.getName();

	/**
	 * Create metadata
	 * 
	 * @return metadata
	 */
	public static TileTableMetadata create() {
		return new TileTableMetadata();
	}

	/**
	 * Create metadata
	 * 
	 * @param autoincrement
	 *            autoincrement ids
	 * @return metadata
	 */
	public static TileTableMetadata create(boolean autoincrement) {
		return new TileTableMetadata(null, autoincrement, null, null, null, -1);
	}

	/**
	 * Create metadata
	 * 
	 * @param tableName
	 *            table name
	 * @param tileBoundingBox
	 *            tile bounding box
	 * @param tileSrsId
	 *            tile SRS id
	 * @return metadata
	 */
	public static TileTableMetadata create(String tableName,
			BoundingBox tileBoundingBox, long tileSrsId) {
		return new TileTableMetadata(tableName, null, null, tileBoundingBox,
				tileSrsId);
	}

	/**
	 * Create metadata
	 * 
	 * @param tableName
	 *            table name
	 * @param autoincrement
	 *            autoincrement ids
	 * @param tileBoundingBox
	 *            tile bounding box
	 * @param tileSrsId
	 *            tile SRS id
	 * @return metadata
	 */
	public static TileTableMetadata create(String tableName,
			boolean autoincrement, BoundingBox tileBoundingBox,
			long tileSrsId) {
		return new TileTableMetadata(tableName, autoincrement, null, null,
				tileBoundingBox, tileSrsId);
	}

	/**
	 * Create metadata
	 * 
	 * @param tableName
	 *            table name
	 * @param contentsBoundingBox
	 *            contents bounding box
	 * @param tileBoundingBox
	 *            tile bounding box
	 * @param tileSrsId
	 *            tile SRS id
	 * @return metadata
	 */
	public static TileTableMetadata create(String tableName,
			BoundingBox contentsBoundingBox, BoundingBox tileBoundingBox,
			long tileSrsId) {
		return new TileTableMetadata(tableName, contentsBoundingBox, null,
				tileBoundingBox, tileSrsId);
	}

	/**
	 * Create metadata
	 * 
	 * @param tableName
	 *            table name
	 * @param autoincrement
	 *            autoincrement ids
	 * @param contentsBoundingBox
	 *            contents bounding box
	 * @param tileBoundingBox
	 *            tile bounding box
	 * @param tileSrsId
	 *            tile SRS id
	 * @return metadata
	 */
	public static TileTableMetadata create(String tableName,
			boolean autoincrement, BoundingBox contentsBoundingBox,
			BoundingBox tileBoundingBox, long tileSrsId) {
		return new TileTableMetadata(tableName, autoincrement,
				contentsBoundingBox, null, tileBoundingBox, tileSrsId);
	}

	/**
	 * Create metadata
	 * 
	 * @param tableName
	 *            table name
	 * @param contentsBoundingBox
	 *            contents bounding box
	 * @param contentsSrsId
	 *            contents SRS id
	 * @param tileBoundingBox
	 *            tile bounding box
	 * @param tileSrsId
	 *            tile SRS id
	 * @return metadata
	 */
	public static TileTableMetadata create(String tableName,
			BoundingBox contentsBoundingBox, long contentsSrsId,
			BoundingBox tileBoundingBox, long tileSrsId) {
		return new TileTableMetadata(tableName, contentsBoundingBox,
				contentsSrsId, tileBoundingBox, tileSrsId);
	}

	/**
	 * Create metadata
	 * 
	 * @param tableName
	 *            table name
	 * @param autoincrement
	 *            autoincrement ids
	 * @param contentsBoundingBox
	 *            contents bounding box
	 * @param contentsSrsId
	 *            contents SRS id
	 * @param tileBoundingBox
	 *            tile bounding box
	 * @param tileSrsId
	 *            tile SRS id
	 * @return metadata
	 */
	public static TileTableMetadata create(String tableName,
			boolean autoincrement, BoundingBox contentsBoundingBox,
			long contentsSrsId, BoundingBox tileBoundingBox, long tileSrsId) {
		return new TileTableMetadata(tableName, autoincrement,
				contentsBoundingBox, contentsSrsId, tileBoundingBox, tileSrsId);
	}

	/**
	 * Create metadata
	 * 
	 * @param dataType
	 *            data type
	 * @param tableName
	 *            table name
	 * @param tileBoundingBox
	 *            tile bounding box
	 * @param tileSrsId
	 *            tile SRS id
	 * @return metadata
	 */
	public static TileTableMetadata createTyped(String dataType,
			String tableName, BoundingBox tileBoundingBox, long tileSrsId) {
		return new TileTableMetadata(dataType, tableName, null, null,
				tileBoundingBox, tileSrsId);
	}

	/**
	 * Create metadata
	 * 
	 * @param dataType
	 *            data type
	 * @param tableName
	 *            table name
	 * @param autoincrement
	 *            autoincrement ids
	 * @param tileBoundingBox
	 *            tile bounding box
	 * @param tileSrsId
	 *            tile SRS id
	 * @return metadata
	 */
	public static TileTableMetadata createTyped(String dataType,
			String tableName, boolean autoincrement,
			BoundingBox tileBoundingBox, long tileSrsId) {
		return new TileTableMetadata(dataType, tableName, autoincrement, null,
				null, tileBoundingBox, tileSrsId);
	}

	/**
	 * Create metadata
	 * 
	 * @param dataType
	 *            data type
	 * @param tableName
	 *            table name
	 * @param contentsBoundingBox
	 *            contents bounding box
	 * @param tileBoundingBox
	 *            tile bounding box
	 * @param tileSrsId
	 *            tile SRS id
	 * @return metadata
	 */
	public static TileTableMetadata createTyped(String dataType,
			String tableName, BoundingBox contentsBoundingBox,
			BoundingBox tileBoundingBox, long tileSrsId) {
		return new TileTableMetadata(dataType, tableName, contentsBoundingBox,
				null, tileBoundingBox, tileSrsId);
	}

	/**
	 * Create metadata
	 * 
	 * @param dataType
	 *            data type
	 * @param tableName
	 *            table name
	 * @param autoincrement
	 *            autoincrement ids
	 * @param contentsBoundingBox
	 *            contents bounding box
	 * @param tileBoundingBox
	 *            tile bounding box
	 * @param tileSrsId
	 *            tile SRS id
	 * @return metadata
	 */
	public static TileTableMetadata createTyped(String dataType,
			String tableName, boolean autoincrement,
			BoundingBox contentsBoundingBox, BoundingBox tileBoundingBox,
			long tileSrsId) {
		return new TileTableMetadata(dataType, tableName, autoincrement,
				contentsBoundingBox, null, tileBoundingBox, tileSrsId);
	}

	/**
	 * Create metadata
	 * 
	 * @param dataType
	 *            data type
	 * @param tableName
	 *            table name
	 * @param contentsBoundingBox
	 *            contents bounding box
	 * @param contentsSrsId
	 *            contents SRS id
	 * @param tileBoundingBox
	 *            tile bounding box
	 * @param tileSrsId
	 *            tile SRS id
	 * @return metadata
	 */
	public static TileTableMetadata createTyped(String dataType,
			String tableName, BoundingBox contentsBoundingBox,
			long contentsSrsId, BoundingBox tileBoundingBox, long tileSrsId) {
		return new TileTableMetadata(dataType, tableName, contentsBoundingBox,
				contentsSrsId, tileBoundingBox, tileSrsId);
	}

	/**
	 * Create metadata
	 * 
	 * @param dataType
	 *            data type
	 * @param tableName
	 *            table name
	 * @param autoincrement
	 *            autoincrement ids
	 * @param contentsBoundingBox
	 *            contents bounding box
	 * @param contentsSrsId
	 *            contents SRS id
	 * @param tileBoundingBox
	 *            tile bounding box
	 * @param tileSrsId
	 *            tile SRS id
	 * @return metadata
	 */
	public static TileTableMetadata createTyped(String dataType,
			String tableName, boolean autoincrement,
			BoundingBox contentsBoundingBox, long contentsSrsId,
			BoundingBox tileBoundingBox, long tileSrsId) {
		return new TileTableMetadata(dataType, tableName, autoincrement,
				contentsBoundingBox, contentsSrsId, tileBoundingBox, tileSrsId);
	}

	/**
	 * Contents bounding box
	 */
	private BoundingBox contentsBoundingBox;

	/**
	 * Contents SRS id
	 */
	private Long contentsSrsId;

	/**
	 * Tile bounding box
	 */
	private BoundingBox tileBoundingBox;

	/**
	 * Tile SRS id
	 */
	private long tileSrsId = -1;

	/**
	 * Constructor
	 */
	public TileTableMetadata() {

	}

	/**
	 * Constructor
	 * 
	 * @param tableName
	 *            table name
	 * @param contentsBoundingBox
	 *            contents bounding box
	 * @param contentsSrsId
	 *            contents SRS id
	 * @param tileBoundingBox
	 *            tile bounding box
	 * @param tileSrsId
	 *            tile SRS id
	 */
	public TileTableMetadata(String tableName, BoundingBox contentsBoundingBox,
			Long contentsSrsId, BoundingBox tileBoundingBox, long tileSrsId) {
		this(null, tableName, contentsBoundingBox, contentsSrsId,
				tileBoundingBox, tileSrsId);
	}

	/**
	 * Constructor
	 * 
	 * @param dataType
	 *            data type
	 * @param tableName
	 *            table name
	 * @param contentsBoundingBox
	 *            contents bounding box
	 * @param contentsSrsId
	 *            contents SRS id
	 * @param tileBoundingBox
	 *            tile bounding box
	 * @param tileSrsId
	 *            tile SRS id
	 */
	public TileTableMetadata(String dataType, String tableName,
			BoundingBox contentsBoundingBox, Long contentsSrsId,
			BoundingBox tileBoundingBox, long tileSrsId) {
		this.dataType = dataType;
		this.tableName = tableName;
		this.contentsBoundingBox = contentsBoundingBox;
		this.contentsSrsId = contentsSrsId;
		this.tileBoundingBox = tileBoundingBox;
		this.tileSrsId = tileSrsId;
	}

	/**
	 * Constructor
	 * 
	 * @param tableName
	 *            table name
	 * @param autoincrement
	 *            autoincrement ids
	 * @param contentsBoundingBox
	 *            contents bounding box
	 * @param contentsSrsId
	 *            contents SRS id
	 * @param tileBoundingBox
	 *            tile bounding box
	 * @param tileSrsId
	 *            tile SRS id
	 */
	public TileTableMetadata(String tableName, boolean autoincrement,
			BoundingBox contentsBoundingBox, Long contentsSrsId,
			BoundingBox tileBoundingBox, long tileSrsId) {
		this(null, tableName, autoincrement, contentsBoundingBox, contentsSrsId,
				tileBoundingBox, tileSrsId);
	}

	/**
	 * Constructor
	 * 
	 * @param dataType
	 *            data type
	 * @param tableName
	 *            table name
	 * @param autoincrement
	 *            autoincrement ids
	 * @param contentsBoundingBox
	 *            contents bounding box
	 * @param contentsSrsId
	 *            contents SRS id
	 * @param tileBoundingBox
	 *            tile bounding box
	 * @param tileSrsId
	 *            tile SRS id
	 */
	public TileTableMetadata(String dataType, String tableName,
			boolean autoincrement, BoundingBox contentsBoundingBox,
			Long contentsSrsId, BoundingBox tileBoundingBox, long tileSrsId) {
		this.dataType = dataType;
		this.tableName = tableName;
		this.autoincrement = autoincrement;
		this.contentsBoundingBox = contentsBoundingBox;
		this.contentsSrsId = contentsSrsId;
		this.tileBoundingBox = tileBoundingBox;
		this.tileSrsId = tileSrsId;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDefaultDataType() {
		return DEFAULT_DATA_TYPE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<TileColumn> buildColumns() {

		List<TileColumn> tileColumns = getColumns();

		if (tileColumns == null) {
			tileColumns = TileTable.createRequiredColumns(isAutoincrement());
		}

		return tileColumns;
	}

	/**
	 * Get the contents bounding box
	 * 
	 * @return contents bounding box
	 */
	public BoundingBox getContentsBoundingBox() {
		return contentsBoundingBox != null ? contentsBoundingBox
				: getTileBoundingBox();
	}

	/**
	 * Set the contents bounding box
	 * 
	 * @param contentsBoundingBox
	 *            contents bounding box
	 */
	public void setContentsBoundingBox(BoundingBox contentsBoundingBox) {
		this.contentsBoundingBox = contentsBoundingBox;
	}

	/**
	 * Get the contents SRS id
	 * 
	 * @return contents SRS id
	 */
	public long getContentsSrsId() {
		return contentsSrsId != null ? contentsSrsId : getTileSrsId();
	}

	/**
	 * Set the contents SRS id
	 * 
	 * @param contentsSrsId
	 *            SRS id
	 */
	public void setContentsSrsId(Long contentsSrsId) {
		this.contentsSrsId = contentsSrsId;
	}

	/**
	 * Get the tile bounding box
	 * 
	 * @return tile bounding box
	 */
	public BoundingBox getTileBoundingBox() {
		return tileBoundingBox;
	}

	/**
	 * Set the tile bounding box
	 * 
	 * @param tileBoundingBox
	 *            tile bounding box
	 */
	public void setTileBoundingBox(BoundingBox tileBoundingBox) {
		this.tileBoundingBox = tileBoundingBox;
	}

	/**
	 * Get the tile SRS id
	 * 
	 * @return tile SRS id
	 */
	public long getTileSrsId() {
		return tileSrsId;
	}

	/**
	 * Set the tile SRS id
	 * 
	 * @param tileSrsId
	 *            SRS id
	 */
	public void setTileSrsId(long tileSrsId) {
		this.tileSrsId = tileSrsId;
	}

}
