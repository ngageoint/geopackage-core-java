package mil.nga.geopackage.tiles.matrixset;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import mil.nga.geopackage.BoundingBox;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.contents.Contents;
import mil.nga.geopackage.contents.ContentsDataType;
import mil.nga.geopackage.srs.SpatialReferenceSystem;
import mil.nga.sf.proj.Projection;
import mil.nga.sf.proj.ProjectionTransform;

/**
 * Tile Matrix Set object. Defines the minimum bounding box (min_x, min_y,
 * max_x, max_y) and spatial reference system (srs_id) for all content in a tile
 * pyramid user data table.
 * 
 * @author osbornb
 */
@DatabaseTable(tableName = "gpkg_tile_matrix_set", daoClass = TileMatrixSetDao.class)
public class TileMatrixSet {

	/**
	 * Table name
	 */
	public static final String TABLE_NAME = "gpkg_tile_matrix_set";

	/**
	 * tableName field name
	 */
	public static final String COLUMN_TABLE_NAME = Contents.COLUMN_TABLE_NAME;

	/**
	 * id field name, tableName
	 */
	public static final String COLUMN_ID = COLUMN_TABLE_NAME;

	/**
	 * srsId field name
	 */
	public static final String COLUMN_SRS_ID = SpatialReferenceSystem.COLUMN_SRS_ID;

	/**
	 * minX field name
	 */
	public static final String COLUMN_MIN_X = "min_x";

	/**
	 * minY field name
	 */
	public static final String COLUMN_MIN_Y = "min_y";

	/**
	 * maxX field name
	 */
	public static final String COLUMN_MAX_X = "max_x";

	/**
	 * maxY field name
	 */
	public static final String COLUMN_MAX_Y = "max_y";

	/**
	 * Foreign key to Contents by table name
	 */
	@DatabaseField(columnName = COLUMN_TABLE_NAME, canBeNull = false, foreign = true, foreignAutoRefresh = true)
	private Contents contents;

	/**
	 * Tile Pyramid User Data Table Name
	 */
	@DatabaseField(columnName = COLUMN_TABLE_NAME, id = true, canBeNull = false, readOnly = true)
	private String tableName;

	/**
	 * Spatial Reference System ID: gpkg_spatial_ref_sys.srs_id
	 */
	@DatabaseField(columnName = COLUMN_SRS_ID, canBeNull = false, foreign = true, foreignAutoRefresh = true)
	private SpatialReferenceSystem srs;

	/**
	 * Unique identifier for each Spatial Reference System within a GeoPackage
	 */
	@DatabaseField(columnName = COLUMN_SRS_ID, canBeNull = false, readOnly = true)
	private long srsId;

	/**
	 * Bounding box minimum easting or longitude for all content in table_name
	 */
	@DatabaseField(columnName = COLUMN_MIN_X, canBeNull = false)
	private double minX;

	/**
	 * Bounding box minimum northing or latitude for all content in table_name
	 */
	@DatabaseField(columnName = COLUMN_MIN_Y, canBeNull = false)
	private double minY;

	/**
	 * Bounding box maximum easting or longitude for all content in table_name
	 */
	@DatabaseField(columnName = COLUMN_MAX_X, canBeNull = false)
	private double maxX;

	/**
	 * Bounding box maximum northing or latitude for all content in table_name
	 */
	@DatabaseField(columnName = COLUMN_MAX_Y, canBeNull = false)
	private double maxY;

	/**
	 * Default Constructor
	 */
	public TileMatrixSet() {

	}

	/**
	 * Copy Constructor
	 * 
	 * @param tileMatrixSet
	 *            tile matrix set to copy
	 * @since 1.3.0
	 */
	public TileMatrixSet(TileMatrixSet tileMatrixSet) {
		contents = tileMatrixSet.contents;
		tableName = tileMatrixSet.tableName;
		srs = tileMatrixSet.srs;
		srsId = tileMatrixSet.srsId;
		minX = tileMatrixSet.minX;
		minY = tileMatrixSet.minY;
		maxX = tileMatrixSet.maxX;
		maxY = tileMatrixSet.maxY;
	}

	public String getId() {
		return tableName;
	}

	public void setId(String id) {
		this.tableName = id;
	}

	public Contents getContents() {
		return contents;
	}

	public void setContents(Contents contents) {
		this.contents = contents;
		if (contents != null) {
			// Verify the Contents have a tiles data type (Spec Requirement 33)
			if (!contents.isTilesTypeOrUnknown()) {
				throw new GeoPackageException("The "
						+ Contents.class.getSimpleName() + " of a "
						+ TileMatrixSet.class.getSimpleName()
						+ " must have a data type of "
						+ ContentsDataType.TILES.getName() + ". actual type: "
						+ contents.getDataTypeName());
			}
			tableName = contents.getId();
		} else {
			tableName = null;
		}
	}

	public String getTableName() {
		return tableName;
	}

	public SpatialReferenceSystem getSrs() {
		return srs;
	}

	public void setSrs(SpatialReferenceSystem srs) {
		this.srs = srs;
		srsId = srs != null ? srs.getId() : -1;
	}

	public long getSrsId() {
		return srsId;
	}

	public double getMinX() {
		return minX;
	}

	public void setMinX(double minX) {
		this.minX = minX;
	}

	public double getMinY() {
		return minY;
	}

	public void setMinY(double minY) {
		this.minY = minY;
	}

	public double getMaxX() {
		return maxX;
	}

	public void setMaxX(double maxX) {
		this.maxX = maxX;
	}

	public double getMaxY() {
		return maxY;
	}

	public void setMaxY(double maxY) {
		this.maxY = maxY;
	}

	/**
	 * Get a bounding box
	 * 
	 * @return bounding box
	 */
	public BoundingBox getBoundingBox() {
		BoundingBox boundingBox = new BoundingBox(getMinX(), getMinY(),
				getMaxX(), getMaxY());
		return boundingBox;
	}

	/**
	 * Get a bounding box in the provided projection
	 * 
	 * @param projection
	 *            desired projection
	 * 
	 * @return bounding box
	 * @since 3.1.0
	 */
	public BoundingBox getBoundingBox(Projection projection) {
		BoundingBox boundingBox = getBoundingBox();
		if (projection != null) {
			ProjectionTransform transform = getProjection()
					.getTransformation(projection);
			if (!transform.isSameProjection()) {
				boundingBox = boundingBox.transform(transform);
			}
		}
		return boundingBox;
	}

	/**
	 * Set a bounding box
	 * 
	 * @param boundingBox
	 *            bounding box
	 */
	public void setBoundingBox(BoundingBox boundingBox) {
		setMinX(boundingBox.getMinLongitude());
		setMaxX(boundingBox.getMaxLongitude());
		setMinY(boundingBox.getMinLatitude());
		setMaxY(boundingBox.getMaxLatitude());
	}

	/**
	 * Get the projection
	 * 
	 * @return projection
	 * @since 3.1.0
	 */
	public Projection getProjection() {
		return getSrs().getProjection();
	}

}
