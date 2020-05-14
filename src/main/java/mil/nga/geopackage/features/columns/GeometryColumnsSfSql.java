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
import mil.nga.sf.wkb.GeometryCodes;

/**
 * SF/SQL Geometry Columns object. Identifies the geometry columns in tables
 * that contain user data representing features.
 * 
 * @author osbornb
 */
@DatabaseTable(tableName = "geometry_columns", daoClass = GeometryColumnsSfSqlDao.class)
public class GeometryColumnsSfSql {

	/**
	 * Table name
	 */
	public static final String TABLE_NAME = "geometry_columns";

	/**
	 * fTableName field name
	 */
	public static final String COLUMN_F_TABLE_NAME = "f_table_name";

	/**
	 * fGeometryColumn field name
	 */
	public static final String COLUMN_F_GEOMETRY_COLUMN = "f_geometry_column";

	/**
	 * id 1 field name, fTableName
	 */
	public static final String COLUMN_ID_1 = COLUMN_F_TABLE_NAME;

	/**
	 * id 2 field name, fGeometryColumn
	 */
	public static final String COLUMN_ID_2 = COLUMN_F_GEOMETRY_COLUMN;

	/**
	 * geometryType field name
	 */
	public static final String COLUMN_GEOMETRY_TYPE = "geometry_type";

	/**
	 * coordDimension field name
	 */
	public static final String COLUMN_COORD_DIMENSION = "coord_dimension";

	/**
	 * srid field name
	 */
	public static final String COLUMN_SRID = "srid";

	/**
	 * Foreign key to Contents by table name
	 */
	@DatabaseField(columnName = COLUMN_F_TABLE_NAME, canBeNull = false, unique = true, foreign = true, foreignAutoRefresh = true)
	private Contents contents;

	/**
	 * Name of the table containing the geometry column
	 */
	@DatabaseField(columnName = COLUMN_F_TABLE_NAME, id = true, canBeNull = false, uniqueCombo = true, readOnly = true)
	private String fTableName;

	/**
	 * Name of a column in the feature table that is a Geometry Column
	 */
	@DatabaseField(columnName = COLUMN_F_GEOMETRY_COLUMN, canBeNull = false, uniqueCombo = true)
	private String fGeometryColumn;

	/**
	 * Geometry Type Code (Core) or Geometry Type Codes (Extension) in Geometry
	 * Types (Normative)
	 */
	@DatabaseField(columnName = COLUMN_GEOMETRY_TYPE, canBeNull = false)
	private int geometryType;

	/**
	 * Coord Dimension from z and m values
	 */
	@DatabaseField(columnName = COLUMN_COORD_DIMENSION, canBeNull = false)
	private byte coordDimension;

	/**
	 * Spatial Reference System ID: gpkg_spatial_ref_sys.srs_id
	 */
	@DatabaseField(columnName = COLUMN_SRID, canBeNull = false, foreign = true, foreignAutoRefresh = true)
	private SpatialReferenceSystem srs;

	/**
	 * Unique identifier for each Spatial Reference System within a GeoPackage
	 */
	@DatabaseField(columnName = COLUMN_SRID, canBeNull = false, readOnly = true)
	private long srid;

	/**
	 * Default Constructor
	 */
	public GeometryColumnsSfSql() {

	}

	/**
	 * Copy Constructor
	 * 
	 * @param geometryColumns
	 *            geometry columns to copy
	 * @since 1.3.0
	 */
	public GeometryColumnsSfSql(GeometryColumnsSfSql geometryColumns) {
		contents = geometryColumns.contents;
		fTableName = geometryColumns.fTableName;
		fGeometryColumn = geometryColumns.fGeometryColumn;
		geometryType = geometryColumns.geometryType;
		coordDimension = geometryColumns.coordDimension;
		srs = geometryColumns.srs;
		srid = geometryColumns.srid;
	}

	/**
	 * Get the id
	 * 
	 * @return table column key
	 */
	public TableColumnKey getId() {
		return new TableColumnKey(fTableName, fGeometryColumn);
	}

	/**
	 * Set the id
	 * 
	 * @param id
	 *            id
	 */
	public void setId(TableColumnKey id) {
		fTableName = id.getTableName();
		fGeometryColumn = id.getColumnName();
	}

	public Contents getContents() {
		return contents;
	}

	public void setContents(Contents contents) {
		this.contents = contents;
		if (contents != null) {
			// Verify the Contents have a features data type (Spec Requirement
			// 23)
			if (!contents.isFeaturesTypeOrUnknown()) {
				throw new GeoPackageException("The "
						+ Contents.class.getSimpleName() + " of a "
						+ GeometryColumnsSfSql.class.getSimpleName()
						+ " must have a data type of "
						+ ContentsDataType.FEATURES.getName()
						+ ". actual type: " + contents.getDataTypeName());
			}
			fTableName = contents.getId();
		}
	}

	public String getFTableName() {
		return fTableName;
	}

	public String getFGeometryColumn() {
		return fGeometryColumn;
	}

	public void setFGeometryColumn(String fGeometryColumn) {
		this.fGeometryColumn = fGeometryColumn;
	}

	public GeometryType getGeometryType() {
		return GeometryCodes.getGeometryType(geometryType);
	}

	public void setGeometryType(GeometryType geometryType) {
		this.geometryType = GeometryCodes.getCode(geometryType);
	}

	public int getGeometryTypeCode() {
		return geometryType;
	}

	public void setCoordDimension(byte coordDimension) {
		validateCoordDimension(COLUMN_COORD_DIMENSION, coordDimension);
		this.coordDimension = coordDimension;
	}

	public byte getCoordDimension() {
		return coordDimension;
	}

	public SpatialReferenceSystem getSrs() {
		return srs;
	}

	public void setSrs(SpatialReferenceSystem srs) {
		this.srs = srs;
		if (srs != null) {
			srid = srs.getId();
		}
	}

	public long getSrid() {
		return srid;
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
	 * Validate the coord dimension, between 2 and 5 per the view
	 * 
	 * @param column
	 * @param value
	 */
	private void validateCoordDimension(String column, byte value) {
		if (value < 2 || value > 5) {
			throw new GeoPackageException(
					column + " value must be between 2 and 5");
		}
	}

}
