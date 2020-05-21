package mil.nga.geopackage.tiles.user;

import java.util.List;

import mil.nga.geopackage.BoundingBox;
import mil.nga.geopackage.contents.ContentsDataType;
import mil.nga.geopackage.user.UserTableMetadata;

// TODO
public class TileTableMetadata extends UserTableMetadata<TileColumn> {

	public static final String DEFAULT_DATA_TYPE = ContentsDataType.TILES
			.getName();

	public static TileTableMetadata create() {
		return new TileTableMetadata();
	}

	public static TileTableMetadata create(boolean autoincrement) {
		return new TileTableMetadata(null, autoincrement, null, null, null, -1);
	}

	public static TileTableMetadata create(String tableName,
			BoundingBox tileBoundingBox, long tileSrsId) {
		return new TileTableMetadata(tableName, null, null, tileBoundingBox,
				tileSrsId);
	}

	public static TileTableMetadata create(String tableName,
			boolean autoincrement, BoundingBox tileBoundingBox,
			long tileSrsId) {
		return new TileTableMetadata(tableName, autoincrement, null, null,
				tileBoundingBox, tileSrsId);
	}

	public static TileTableMetadata create(String tableName,
			BoundingBox contentsBoundingBox, BoundingBox tileBoundingBox,
			long tileSrsId) {
		return new TileTableMetadata(tableName, contentsBoundingBox, null,
				tileBoundingBox, tileSrsId);
	}

	public static TileTableMetadata create(String tableName,
			boolean autoincrement, BoundingBox contentsBoundingBox,
			BoundingBox tileBoundingBox, long tileSrsId) {
		return new TileTableMetadata(tableName, autoincrement,
				contentsBoundingBox, null, tileBoundingBox, tileSrsId);
	}

	public static TileTableMetadata create(String tableName,
			BoundingBox contentsBoundingBox, long contentsSrsId,
			BoundingBox tileBoundingBox, long tileSrsId) {
		return new TileTableMetadata(tableName, contentsBoundingBox,
				contentsSrsId, tileBoundingBox, tileSrsId);
	}

	public static TileTableMetadata create(String tableName,
			boolean autoincrement, BoundingBox contentsBoundingBox,
			long contentsSrsId, BoundingBox tileBoundingBox, long tileSrsId) {
		return new TileTableMetadata(tableName, autoincrement,
				contentsBoundingBox, contentsSrsId, tileBoundingBox, tileSrsId);
	}

	public static TileTableMetadata createTyped(String dataType,
			String tableName, BoundingBox tileBoundingBox, long tileSrsId) {
		return new TileTableMetadata(dataType, tableName, null, null,
				tileBoundingBox, tileSrsId);
	}

	public static TileTableMetadata createTyped(String dataType,
			String tableName, boolean autoincrement,
			BoundingBox tileBoundingBox, long tileSrsId) {
		return new TileTableMetadata(dataType, tableName, autoincrement, null,
				null, tileBoundingBox, tileSrsId);
	}

	public static TileTableMetadata createTyped(String dataType,
			String tableName, BoundingBox contentsBoundingBox,
			BoundingBox tileBoundingBox, long tileSrsId) {
		return new TileTableMetadata(dataType, tableName, contentsBoundingBox,
				null, tileBoundingBox, tileSrsId);
	}

	public static TileTableMetadata createTyped(String dataType,
			String tableName, boolean autoincrement,
			BoundingBox contentsBoundingBox, BoundingBox tileBoundingBox,
			long tileSrsId) {
		return new TileTableMetadata(dataType, tableName, autoincrement,
				contentsBoundingBox, null, tileBoundingBox, tileSrsId);
	}

	public static TileTableMetadata createTyped(String dataType,
			String tableName, BoundingBox contentsBoundingBox,
			long contentsSrsId, BoundingBox tileBoundingBox, long tileSrsId) {
		return new TileTableMetadata(dataType, tableName, contentsBoundingBox,
				contentsSrsId, tileBoundingBox, tileSrsId);
	}

	public static TileTableMetadata createTyped(String dataType,
			String tableName, boolean autoincrement,
			BoundingBox contentsBoundingBox, long contentsSrsId,
			BoundingBox tileBoundingBox, long tileSrsId) {
		return new TileTableMetadata(dataType, tableName, autoincrement,
				contentsBoundingBox, contentsSrsId, tileBoundingBox, tileSrsId);
	}

	private BoundingBox contentsBoundingBox;

	private Long contentsSrsId;

	private BoundingBox tileBoundingBox;

	private long tileSrsId = -1;

	public TileTableMetadata() {

	}

	public TileTableMetadata(String tableName, BoundingBox contentsBoundingBox,
			Long contentsSrsId, BoundingBox tileBoundingBox, long tileSrsId) {
		this(null, tableName, contentsBoundingBox, contentsSrsId,
				tileBoundingBox, tileSrsId);
	}

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

	public TileTableMetadata(String tableName, boolean autoincrement,
			BoundingBox contentsBoundingBox, Long contentsSrsId,
			BoundingBox tileBoundingBox, long tileSrsId) {
		this(null, tableName, autoincrement, contentsBoundingBox, contentsSrsId,
				tileBoundingBox, tileSrsId);
	}

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

	public BoundingBox getContentsBoundingBox() {
		return contentsBoundingBox != null ? contentsBoundingBox
				: getTileBoundingBox();
	}

	public void setContentsBoundingBox(BoundingBox contentsBoundingBox) {
		this.contentsBoundingBox = contentsBoundingBox;
	}

	public long getContentsSrsId() {
		return contentsSrsId != null ? contentsSrsId : getTileSrsId();
	}

	public void setContentsSrsId(Long contentsSrsId) {
		this.contentsSrsId = contentsSrsId;
	}

	public BoundingBox getTileBoundingBox() {
		return tileBoundingBox;
	}

	public void setTileBoundingBox(BoundingBox tileBoundingBox) {
		this.tileBoundingBox = tileBoundingBox;
	}

	public long getTileSrsId() {
		return tileSrsId;
	}

	public void setTileSrsId(long tileSrsId) {
		this.tileSrsId = tileSrsId;
	}

}
