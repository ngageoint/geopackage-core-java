package mil.nga.geopackage.contents;

import java.io.IOException;
import java.util.Date;

import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import mil.nga.geopackage.BoundingBox;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.db.DateConverter;
import mil.nga.geopackage.features.columns.GeometryColumns;
import mil.nga.geopackage.persister.DatePersister;
import mil.nga.geopackage.srs.SpatialReferenceSystem;
import mil.nga.geopackage.tiles.matrix.TileMatrix;
import mil.nga.geopackage.tiles.matrixset.TileMatrixSet;
import mil.nga.sf.proj.Projection;
import mil.nga.sf.proj.ProjectionTransform;

/**
 * Contents object. Provides identifying and descriptive information that an
 * application can display to a user in a menu of geospatial data that is
 * available for access and/or update.
 * 
 * @author osbornb
 */
@DatabaseTable(tableName = "gpkg_contents", daoClass = ContentsDao.class)
public class Contents {

	/**
	 * Table name
	 */
	public static final String TABLE_NAME = "gpkg_contents";

	/**
	 * tableName field name
	 */
	public static final String COLUMN_TABLE_NAME = "table_name";

	/**
	 * id field name, tableName
	 */
	public static final String COLUMN_ID = COLUMN_TABLE_NAME;

	/**
	 * dataType field name
	 */
	public static final String COLUMN_DATA_TYPE = "data_type";

	/**
	 * identifier field name
	 */
	public static final String COLUMN_IDENTIFIER = "identifier";

	/**
	 * description field name
	 */
	public static final String COLUMN_DESCRIPTION = "description";

	/**
	 * lastChange field name
	 */
	public static final String COLUMN_LAST_CHANGE = "last_change";

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
	 * srsId field name
	 */
	public static final String COLUMN_SRS_ID = SpatialReferenceSystem.COLUMN_SRS_ID;

	/**
	 * The name of the tiles, or feature table
	 */
	@DatabaseField(columnName = COLUMN_TABLE_NAME, id = true, canBeNull = false)
	private String tableName;

	/**
	 * Type of data stored in the table:. “features” per clause Features,
	 * “tiles” per clause Tiles, or an implementer-defined value for other data
	 * tables per clause in an Extended GeoPackage.
	 */
	@DatabaseField(columnName = COLUMN_DATA_TYPE, canBeNull = false)
	private String dataType;

	/**
	 * A human-readable identifier (e.g. short name) for the table_name content
	 */
	@DatabaseField(columnName = COLUMN_IDENTIFIER, unique = true)
	private String identifier;

	/**
	 * A human-readable description for the table_name content
	 */
	@DatabaseField(columnName = COLUMN_DESCRIPTION, defaultValue = "")
	private String description;

	/**
	 * timestamp value in ISO 8601 format as defined by the strftime function
	 * %Y-%m-%dT%H:%M:%fZ format string applied to the current time
	 */
	@DatabaseField(columnName = COLUMN_LAST_CHANGE, persisterClass = DatePersister.class, defaultValue = DateConverter.DATETIME_FORMAT)
	private Date lastChange;

	/**
	 * Bounding box minimum easting or longitude for all content in table_name
	 */
	@DatabaseField(columnName = COLUMN_MIN_X)
	private Double minX;

	/**
	 * Bounding box minimum northing or latitude for all content in table_name
	 */
	@DatabaseField(columnName = COLUMN_MIN_Y)
	private Double minY;

	/**
	 * Bounding box maximum easting or longitude for all content in table_name
	 */
	@DatabaseField(columnName = COLUMN_MAX_X)
	private Double maxX;

	/**
	 * Bounding box maximum northing or latitude for all content in table_name
	 */
	@DatabaseField(columnName = COLUMN_MAX_Y)
	private Double maxY;

	/**
	 * Spatial Reference System ID
	 */
	@DatabaseField(columnName = COLUMN_SRS_ID, foreign = true, foreignAutoRefresh = true)
	private SpatialReferenceSystem srs;

	/**
	 * Unique identifier for each Spatial Reference System within a GeoPackage
	 */
	@DatabaseField(columnName = COLUMN_SRS_ID, readOnly = true)
	private Long srsId;

	/**
	 * Geometry Columns
	 */
	@ForeignCollectionField(eager = false)
	private ForeignCollection<GeometryColumns> geometryColumns;

	/**
	 * Tile Matrix Set
	 */
	@ForeignCollectionField(eager = false)
	private ForeignCollection<TileMatrixSet> tileMatrixSet;

	/**
	 * Tile Matrix
	 */
	@ForeignCollectionField(eager = false)
	private ForeignCollection<TileMatrix> tileMatrix;

	/**
	 * Default Constructor
	 */
	public Contents() {

	}

	/**
	 * Copy Constructor
	 * 
	 * @param contents
	 *            contents to copy
	 * @since 1.3.0
	 */
	public Contents(Contents contents) {
		tableName = contents.tableName;
		dataType = contents.dataType;
		identifier = contents.identifier;
		description = contents.description;
		lastChange = new Date(contents.lastChange.getTime());
		minX = contents.minX;
		maxX = contents.maxX;
		minY = contents.minY;
		maxY = contents.maxY;
		srs = contents.srs;
		srsId = contents.srsId;
	}

	/**
	 * Get the id
	 * 
	 * @return id
	 */
	public String getId() {
		return tableName;
	}

	/**
	 * Set the id
	 * 
	 * @param id
	 *            id
	 */
	public void setId(String id) {
		this.tableName = id;
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
	 * Set the table name
	 * 
	 * @param tableName
	 *            table name
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	/**
	 * Get the data type
	 * 
	 * @return data type
	 */
	public ContentsDataType getDataType() {
		return ContentsDataType.fromName(dataType);
	}

	/**
	 * Set the data type
	 * 
	 * @param dataType
	 *            data type
	 */
	public void setDataType(ContentsDataType dataType) {
		this.dataType = dataType.getName();
	}

	/**
	 * Get the data type string value
	 * 
	 * @return data type
	 * @since 4.0.0
	 */
	public String getDataTypeName() {
		return dataType;
	}

	/**
	 * Set the data type name
	 * 
	 * @param name
	 *            data type name
	 * @since 4.0.0
	 */
	public void setDataTypeName(String name) {
		this.dataType = name;
	}

	/**
	 * Set the data type name and register the core data type
	 * 
	 * @param name
	 *            data type name
	 * @param dataType
	 *            core data type
	 */
	public void setDataTypeName(String name, ContentsDataType dataType) {
		setDataTypeName(name);
		ContentsDataType.setType(name, dataType);
	}

	/**
	 * Determine if the contents data type is features
	 * 
	 * @return true if features type
	 * @since 4.0.0
	 */
	public boolean isFeaturesType() {
		return ContentsDataType.isFeaturesType(dataType);
	}

	/**
	 * Determine if the contents data type is features or unknown
	 * 
	 * @return true if features type or unknown
	 * @since 4.0.0
	 */
	public boolean isFeaturesTypeOrUnknown() {
		return ContentsDataType.isFeaturesType(dataType, true);
	}

	/**
	 * Determine if the contents data type is tiles
	 * 
	 * @return true if tiles type
	 * @since 4.0.0
	 */
	public boolean isTilesType() {
		return ContentsDataType.isTilesType(dataType);
	}

	/**
	 * Determine if the contents data type is tiles or unknown
	 * 
	 * @return true if tiles type or unknown
	 * @since 4.0.0
	 */
	public boolean isTilesTypeOrUnknown() {
		return ContentsDataType.isTilesType(dataType, true);
	}

	/**
	 * Determine if the contents data type is attributes
	 * 
	 * @return true if attributes type
	 * @since 4.0.0
	 */
	public boolean isAttributesType() {
		return ContentsDataType.isAttributesType(dataType);
	}

	/**
	 * Determine if the contents data type is attributes or unknown
	 * 
	 * @return true if attributes type or unknown
	 * @since 4.0.0
	 */
	public boolean isAttributesTypeOrUnknown() {
		return ContentsDataType.isAttributesType(dataType, true);
	}

	/**
	 * Get the identifier
	 * 
	 * @return identifier
	 */
	public String getIdentifier() {
		return identifier;
	}

	/**
	 * Set the identifier
	 * 
	 * @param identifier
	 *            identifier
	 */
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	/**
	 * Get the description
	 * 
	 * @return description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Set the description
	 * 
	 * @param description
	 *            description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Get the last change
	 * 
	 * @return last change
	 */
	public Date getLastChange() {
		return lastChange;
	}

	/**
	 * Set the last change
	 * 
	 * @param lastChange
	 *            last change
	 */
	public void setLastChange(Date lastChange) {
		this.lastChange = lastChange;
	}

	/**
	 * Get the min x
	 * 
	 * @return min x
	 */
	public Double getMinX() {
		return minX;
	}

	/**
	 * Set the min x
	 * 
	 * @param minX
	 *            min x
	 */
	public void setMinX(Double minX) {
		this.minX = minX;
	}

	/**
	 * Get the min y
	 * 
	 * @return min y
	 */
	public Double getMinY() {
		return minY;
	}

	/**
	 * Set the min y
	 * 
	 * @param minY
	 *            min y
	 */
	public void setMinY(Double minY) {
		this.minY = minY;
	}

	/**
	 * Get the max x
	 * 
	 * @return max x
	 */
	public Double getMaxX() {
		return maxX;
	}

	/**
	 * Set the max x
	 * 
	 * @param maxX
	 *            max x
	 */
	public void setMaxX(Double maxX) {
		this.maxX = maxX;
	}

	/**
	 * Get the max y
	 * 
	 * @return max y
	 */
	public Double getMaxY() {
		return maxY;
	}

	/**
	 * Set the max y
	 * 
	 * @param maxY
	 *            max y
	 */
	public void setMaxY(Double maxY) {
		this.maxY = maxY;
	}

	/**
	 * Get the SRS
	 * 
	 * @return srs
	 */
	public SpatialReferenceSystem getSrs() {
		return srs;
	}

	/**
	 * Set the srs
	 * 
	 * @param srs
	 *            srs
	 */
	public void setSrs(SpatialReferenceSystem srs) {
		this.srs = srs;
		srsId = srs != null ? srs.getId() : null;
	}

	/**
	 * Get the srs id
	 * 
	 * @return srs id
	 */
	public Long getSrsId() {
		return srsId;
	}

	/**
	 * Get the Geometry Columns, should only return one or no value
	 * 
	 * @return geometry columns
	 */
	public GeometryColumns getGeometryColumns() {
		GeometryColumns result = null;
		if (geometryColumns.size() > 1) {
			// This shouldn't happen with the unique table name constraint on
			// geometry columns
			throw new GeoPackageException(
					"Unexpected state. More than one GeometryColumn has a foreign key to the Contents. Count: "
							+ geometryColumns.size());
		} else if (geometryColumns.size() == 1) {
			CloseableIterator<GeometryColumns> iterator = geometryColumns
					.closeableIterator();
			try {
				result = iterator.next();
			} finally {
				try {
					iterator.close();
				} catch (IOException e) {
					throw new GeoPackageException(
							"Failed to close the Geometry Columns iterator", e);
				}
			}
		}
		return result;
	}

	/**
	 * Get the Tile Matrix Set, should only return one or no value
	 * 
	 * @return tile matrix set
	 */
	public TileMatrixSet getTileMatrixSet() {
		TileMatrixSet result = null;
		if (tileMatrixSet.size() > 1) {
			// This shouldn't happen with the table name primary key on tile
			// matrix set
			throw new GeoPackageException(
					"Unexpected state. More than one TileMatrixSet has a foreign key to the Contents. Count: "
							+ tileMatrixSet.size());
		} else if (tileMatrixSet.size() == 1) {
			CloseableIterator<TileMatrixSet> iterator = tileMatrixSet
					.closeableIterator();
			try {
				result = iterator.next();
			} finally {
				try {
					iterator.close();
				} catch (IOException e) {
					throw new GeoPackageException(
							"Failed to close the Tile Matrix Set iterator", e);
				}
			}
		}
		return result;
	}

	/**
	 * Get the Tile Matrix collection
	 * 
	 * @return tile matrices
	 */
	public ForeignCollection<TileMatrix> getTileMatrix() {
		return tileMatrix;
	}

	/**
	 * Get a bounding box
	 * 
	 * @return bounding box
	 */
	public BoundingBox getBoundingBox() {
		BoundingBox boundingBox = null;
		if (minX != null && maxX != null && minY != null && maxY != null) {
			boundingBox = new BoundingBox(getMinX(), getMinY(), getMaxX(),
					getMaxY());
		}
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
		if (boundingBox != null && projection != null) {
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
		Projection projection = null;
		SpatialReferenceSystem srs = getSrs();
		if (srs != null) {
			projection = srs.getProjection();
		}
		return projection;
	}

}
