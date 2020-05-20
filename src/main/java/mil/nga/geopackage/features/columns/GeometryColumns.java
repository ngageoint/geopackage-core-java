package mil.nga.geopackage.features.columns;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.contents.Contents;
import mil.nga.geopackage.contents.ContentsDataType;
import mil.nga.geopackage.db.TableColumnKey;
import mil.nga.geopackage.srs.SpatialReferenceSystem;
import mil.nga.sf.GeometryType;
import mil.nga.sf.proj.Projection;

/**
 * Geometry Columns object. Identifies the geometry columns in tables that
 * contain user data representing features.
 * 
 * @author osbornb
 */
@DatabaseTable(tableName = "gpkg_geometry_columns", daoClass = GeometryColumnsDao.class)
public class GeometryColumns {

	/**
	 * Table name
	 */
	public static final String TABLE_NAME = "gpkg_geometry_columns";

	/**
	 * tableName field name
	 */
	public static final String COLUMN_TABLE_NAME = Contents.COLUMN_TABLE_NAME;

	/**
	 * columnName field name
	 */
	public static final String COLUMN_COLUMN_NAME = "column_name";

	/**
	 * id 1 field name, tableName
	 */
	public static final String COLUMN_ID_1 = COLUMN_TABLE_NAME;

	/**
	 * id 2 field name, columnName
	 */
	public static final String COLUMN_ID_2 = COLUMN_COLUMN_NAME;

	/**
	 * geometryTypeName field name
	 */
	public static final String COLUMN_GEOMETRY_TYPE_NAME = "geometry_type_name";

	/**
	 * srsId field name
	 */
	public static final String COLUMN_SRS_ID = SpatialReferenceSystem.COLUMN_SRS_ID;

	/**
	 * z field name
	 */
	public static final String COLUMN_Z = "z";

	/**
	 * m field name
	 */
	public static final String COLUMN_M = "m";

	/**
	 * Foreign key to Contents by table name
	 */
	@DatabaseField(columnName = COLUMN_TABLE_NAME, canBeNull = false, unique = true, foreign = true, foreignAutoRefresh = true)
	private Contents contents;

	/**
	 * Name of the table containing the geometry column
	 */
	@DatabaseField(columnName = COLUMN_TABLE_NAME, id = true, canBeNull = false, uniqueCombo = true, readOnly = true)
	private String tableName;

	/**
	 * Name of a column in the feature table that is a Geometry Column
	 */
	@DatabaseField(columnName = COLUMN_COLUMN_NAME, canBeNull = false, uniqueCombo = true)
	private String columnName;

	/**
	 * Name from Geometry Type Codes (Core) or Geometry Type Codes (Extension)
	 * in Geometry Types (Normative)
	 */
	@DatabaseField(columnName = COLUMN_GEOMETRY_TYPE_NAME, canBeNull = false)
	private String geometryTypeName;

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
	 * 0: z values prohibited; 1: z values mandatory; 2: z values optional
	 */
	@DatabaseField(columnName = COLUMN_Z, canBeNull = false)
	private byte z;

	/**
	 * 0: m values prohibited; 1: m values mandatory; 2: m values optional
	 */
	@DatabaseField(columnName = COLUMN_M, canBeNull = false)
	private byte m;

	/**
	 * Default Constructor
	 */
	public GeometryColumns() {

	}

	/**
	 * Copy Constructor
	 * 
	 * @param geometryColumns
	 *            geometry columns to copy
	 * @since 1.3.0
	 */
	public GeometryColumns(GeometryColumns geometryColumns) {
		contents = geometryColumns.contents;
		tableName = geometryColumns.tableName;
		columnName = geometryColumns.columnName;
		geometryTypeName = geometryColumns.geometryTypeName;
		srs = geometryColumns.srs;
		srsId = geometryColumns.srsId;
		z = geometryColumns.z;
		m = geometryColumns.m;
	}

	/**
	 * Get the id
	 * 
	 * @return table column key
	 */
	public TableColumnKey getId() {
		return new TableColumnKey(tableName, columnName);
	}

	/**
	 * Set the id
	 * 
	 * @param id
	 *            id
	 */
	public void setId(TableColumnKey id) {
		tableName = id.getTableName();
		columnName = id.getColumnName();
	}

	/**
	 * Get the contents
	 * 
	 * @return contents
	 */
	public Contents getContents() {
		return contents;
	}

	/**
	 * Set the contents
	 * 
	 * @param contents
	 *            contents
	 */
	public void setContents(Contents contents) {
		this.contents = contents;
		if (contents != null) {
			// Verify the Contents have a features data type (Spec Requirement
			// 23)
			if (!contents.isFeaturesTypeOrUnknown()) {
				throw new GeoPackageException("The "
						+ Contents.class.getSimpleName() + " of a "
						+ GeometryColumns.class.getSimpleName()
						+ " must have a data type of "
						+ ContentsDataType.FEATURES.getName()
						+ ". actual type: " + contents.getDataTypeName());
			}
			tableName = contents.getId();
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
	 * Set the table name
	 * 
	 * @param tableName
	 *            table name
	 * @since 4.0.0
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	/**
	 * Get the column name
	 * 
	 * @return column name
	 */
	public String getColumnName() {
		return columnName;
	}

	/**
	 * Set the column name
	 * 
	 * @param columnName
	 *            column name
	 */
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	/**
	 * Get the geometry type
	 * 
	 * @return geometry type
	 */
	public GeometryType getGeometryType() {
		return GeometryType.fromName(geometryTypeName);
	}

	/**
	 * Set the geometry type
	 * 
	 * @param geometryType
	 *            geometry type
	 */
	public void setGeometryType(GeometryType geometryType) {
		this.geometryTypeName = geometryType.getName();
	}

	/**
	 * Get the geometry type name
	 * 
	 * @return geometry type name
	 */
	public String getGeometryTypeName() {
		return geometryTypeName;
	}

	/**
	 * Get the srs
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
		srsId = srs != null ? srs.getId() : -1;
	}

	/**
	 * Get the srs id
	 * 
	 * @return srs id
	 */
	public long getSrsId() {
		return srsId;
	}

	/**
	 * Set the srs id
	 * 
	 * @param srsId
	 *            srs id
	 * @since 4.0.0
	 */
	public void setSrsId(long srsId) {
		this.srsId = srsId;
	}

	/**
	 * Get the z
	 * 
	 * @return z
	 */
	public byte getZ() {
		return z;
	}

	/**
	 * Set the z
	 * 
	 * @param z
	 *            z
	 */
	public void setZ(byte z) {
		validateValues(COLUMN_Z, z);
		this.z = z;
	}

	/**
	 * Get the m
	 * 
	 * @return m
	 */
	public byte getM() {
		return m;
	}

	/**
	 * Set the m
	 * 
	 * @param m
	 *            m
	 */
	public void setM(byte m) {
		validateValues(COLUMN_M, m);
		this.m = m;
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

	/**
	 * Validate the z and m byte values. They must be 0 for prohibited, 1 for
	 * mandatory, or 2 for optional. (Spec Requirement 27 & 28)
	 * 
	 * @param column
	 * @param value
	 */
	private void validateValues(String column, byte value) {
		if (value < 0 || value > 2) {
			throw new GeoPackageException(column
					+ " value must be 0 for prohibited, 1 for mandatory, or 2 for optional");
		}
	}

}
