package mil.nga.geopackage.extension.nga.index;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Geometry Index object, for indexing geometries within user feature tables
 * 
 * @author osbornb
 * @since 1.1.0
 */
@DatabaseTable(tableName = "nga_geometry_index", daoClass = GeometryIndexDao.class)
public class GeometryIndex {

	/**
	 * Table name
	 */
	public static final String TABLE_NAME = "nga_geometry_index";

	/**
	 * tableName field name
	 */
	public static final String COLUMN_TABLE_NAME = "table_name";

	/**
	 * Geometry Id column
	 */
	public static final String COLUMN_GEOM_ID = "geom_id";

	/**
	 * Min X
	 */
	public static final String COLUMN_MIN_X = "min_x";

	/**
	 * Max X
	 */
	public static final String COLUMN_MAX_X = "max_x";

	/**
	 * Min Y
	 */
	public static final String COLUMN_MIN_Y = "min_y";

	/**
	 * Max Y
	 */
	public static final String COLUMN_MAX_Y = "max_y";

	/**
	 * Min Z
	 */
	public static final String COLUMN_MIN_Z = "min_z";

	/**
	 * Max Z
	 */
	public static final String COLUMN_MAX_Z = "max_z";

	/**
	 * Min M
	 */
	public static final String COLUMN_MIN_M = "min_m";

	/**
	 * Max M
	 */
	public static final String COLUMN_MAX_M = "max_m";

	/**
	 * Foreign key to Extensions by table name
	 */
	@DatabaseField(columnName = COLUMN_TABLE_NAME, canBeNull = false, unique = true, foreign = true, foreignAutoRefresh = true)
	private TableIndex tableIndex;

	/**
	 * Name of the feature table
	 */
	@DatabaseField(columnName = COLUMN_TABLE_NAME, id = true, canBeNull = false, uniqueCombo = true, readOnly = true)
	private String tableName;

	/**
	 * Geometry id
	 */
	@DatabaseField(columnName = COLUMN_GEOM_ID, canBeNull = false, uniqueCombo = true)
	private long geomId;

	/**
	 * Min X
	 */
	@DatabaseField(columnName = COLUMN_MIN_X, canBeNull = false)
	private double minX;

	/**
	 * Max X
	 */
	@DatabaseField(columnName = COLUMN_MAX_X, canBeNull = false)
	private double maxX;

	/**
	 * Min Y
	 */
	@DatabaseField(columnName = COLUMN_MIN_Y, canBeNull = false)
	private double minY;

	/**
	 * Max Y
	 */
	@DatabaseField(columnName = COLUMN_MAX_Y, canBeNull = false)
	private double maxY;

	/**
	 * Min Z
	 */
	@DatabaseField(columnName = COLUMN_MIN_Z)
	private Double minZ;

	/**
	 * Max Z
	 */
	@DatabaseField(columnName = COLUMN_MAX_Z)
	private Double maxZ;

	/**
	 * Min M
	 */
	@DatabaseField(columnName = COLUMN_MIN_M)
	private Double minM;

	/**
	 * Max M
	 */
	@DatabaseField(columnName = COLUMN_MAX_M)
	private Double maxM;

	/**
	 * Default Constructor
	 */
	public GeometryIndex() {

	}

	/**
	 * Copy Constructor
	 * 
	 * @param geometryIndex
	 *            geometry index to copy
	 * @since 1.3.0
	 */
	public GeometryIndex(GeometryIndex geometryIndex) {
		tableIndex = geometryIndex.tableIndex;
		tableName = geometryIndex.tableName;
		geomId = geometryIndex.geomId;
		minX = geometryIndex.minX;
		maxX = geometryIndex.maxX;
		minY = geometryIndex.minY;
		maxY = geometryIndex.maxY;
		minZ = geometryIndex.minZ;
		maxZ = geometryIndex.maxZ;
		minM = geometryIndex.minM;
		maxM = geometryIndex.maxM;
	}

	/**
	 * Get the id
	 * 
	 * @return geometry index key
	 */
	public GeometryIndexKey getId() {
		return new GeometryIndexKey(tableName, geomId);
	}

	/**
	 * Set the id
	 * 
	 * @param id
	 *            geometry index key
	 */
	public void setId(GeometryIndexKey id) {
		tableName = id.getTableName();
		geomId = id.getGeomId();
	}

	/**
	 * Get the table index
	 * 
	 * @return table index
	 */
	public TableIndex getTableIndex() {
		return tableIndex;
	}

	/**
	 * Set the table index
	 * 
	 * @param tableIndex
	 *            table index
	 */
	public void setTableIndex(TableIndex tableIndex) {
		this.tableIndex = tableIndex;
		if (tableIndex != null) {
			tableName = tableIndex.getTableName();
		} else {
			tableName = null;
		}
	}

	/**
	 * Get the table name
	 * 
	 * @return table name
	 */
	public String getTableName() {
		return tableName;
	}

	/**
	 * Get the geometry id
	 * 
	 * @return geometry id
	 */
	public long getGeomId() {
		return geomId;
	}

	/**
	 * Set the geometry id
	 * 
	 * @param geomId
	 *            geom id
	 */
	public void setGeomId(long geomId) {
		this.geomId = geomId;
	}

	/**
	 * Get the min x
	 * 
	 * @return min x
	 */
	public double getMinX() {
		return minX;
	}

	/**
	 * Set the min x
	 * 
	 * @param minX
	 *            min x
	 */
	public void setMinX(double minX) {
		this.minX = minX;
	}

	/**
	 * Get the max x
	 * 
	 * @return max x
	 */
	public double getMaxX() {
		return maxX;
	}

	/**
	 * Set the max x
	 * 
	 * @param maxX
	 *            max x
	 */
	public void setMaxX(double maxX) {
		this.maxX = maxX;
	}

	/**
	 * Get the min y
	 * 
	 * @return min y
	 */
	public double getMinY() {
		return minY;
	}

	/**
	 * Set the min y
	 * 
	 * @param minY
	 *            min y
	 */
	public void setMinY(double minY) {
		this.minY = minY;
	}

	/**
	 * Get the max y
	 * 
	 * @return max y
	 */
	public double getMaxY() {
		return maxY;
	}

	/**
	 * Set the max y
	 * 
	 * @param maxY
	 *            max y
	 */
	public void setMaxY(double maxY) {
		this.maxY = maxY;
	}

	/**
	 * Get the min z
	 * 
	 * @return min z
	 */
	public Double getMinZ() {
		return minZ;
	}

	/**
	 * Set the min z
	 * 
	 * @param minZ
	 *            min z
	 */
	public void setMinZ(Double minZ) {
		this.minZ = minZ;
	}

	/**
	 * Get the max z
	 * 
	 * @return max z
	 */
	public Double getMaxZ() {
		return maxZ;
	}

	/**
	 * Set the max z
	 * 
	 * @param maxZ
	 *            max z
	 */
	public void setMaxZ(Double maxZ) {
		this.maxZ = maxZ;
	}

	/**
	 * Get the min m
	 * 
	 * @return min m
	 */
	public Double getMinM() {
		return minM;
	}

	/**
	 * Set the min m
	 * 
	 * @param minM
	 *            min m
	 */
	public void setMinM(Double minM) {
		this.minM = minM;
	}

	/**
	 * Get the max m
	 * 
	 * @return max m
	 */
	public Double getMaxM() {
		return maxM;
	}

	/**
	 * Set the max m
	 * 
	 * @param maxM
	 *            max m
	 */
	public void setMaxM(Double maxM) {
		this.maxM = maxM;
	}

}
