package mil.nga.geopackage.srs;

import mil.nga.geopackage.contents.Contents;
import mil.nga.geopackage.features.columns.GeometryColumns;
import mil.nga.geopackage.tiles.matrixset.TileMatrixSet;
import mil.nga.sf.proj.Projection;
import mil.nga.sf.proj.ProjectionFactory;
import mil.nga.sf.proj.ProjectionTransform;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Spatial Reference System object. The coordinate reference system definitions
 * it contains are referenced by the GeoPackage {@link Contents} and
 * {@link GeometryColumns} objects to relate the vector and tile data in user
 * tables to locations on the earth.
 * 
 * @author osbornb
 */
@DatabaseTable(tableName = "gpkg_spatial_ref_sys", daoClass = SpatialReferenceSystemDao.class)
public class SpatialReferenceSystem {

	/**
	 * Table name
	 */
	public static final String TABLE_NAME = "gpkg_spatial_ref_sys";

	/**
	 * srsName field name
	 */
	public static final String COLUMN_SRS_NAME = "srs_name";

	/**
	 * srsId field name
	 */
	public static final String COLUMN_SRS_ID = "srs_id";

	/**
	 * id field name, srsId
	 */
	public static final String COLUMN_ID = COLUMN_SRS_ID;

	/**
	 * organization field name
	 */
	public static final String COLUMN_ORGANIZATION = "organization";

	/**
	 * organizationCoordsysId field name
	 */
	public static final String COLUMN_ORGANIZATION_COORDSYS_ID = "organization_coordsys_id";

	/**
	 * definition field name
	 */
	public static final String COLUMN_DEFINITION = "definition";

	/**
	 * description field name
	 */
	public static final String COLUMN_DESCRIPTION = "description";

	/**
	 * Human readable name of this SRS
	 */
	@DatabaseField(columnName = COLUMN_SRS_NAME, canBeNull = false)
	private String srsName;

	/**
	 * Unique identifier for each Spatial Reference System within a GeoPackage
	 */
	@DatabaseField(columnName = COLUMN_SRS_ID, id = true, canBeNull = false)
	private long srsId;

	/**
	 * Case-insensitive name of the defining organization e.g. EPSG or epsg
	 */
	@DatabaseField(columnName = COLUMN_ORGANIZATION, canBeNull = false)
	private String organization;

	/**
	 * Numeric ID of the Spatial Reference System assigned by the organization
	 */
	@DatabaseField(columnName = COLUMN_ORGANIZATION_COORDSYS_ID, canBeNull = false)
	private long organizationCoordsysId;

	/**
	 * Well-known Text [32] Representation of the Spatial Reference System
	 */
	@DatabaseField(columnName = COLUMN_DEFINITION, canBeNull = false)
	private String definition;

	/**
	 * Human readable description of this SRS
	 */
	@DatabaseField(columnName = COLUMN_DESCRIPTION)
	private String description;

	/**
	 * Well-known Text [34] Representation of the Spatial Reference System
	 */
	private String definition_12_063;

	/**
	 * Contents
	 */
	@ForeignCollectionField(eager = false)
	private ForeignCollection<Contents> contents;

	/**
	 * Geometry Columns
	 */
	@ForeignCollectionField(eager = false)
	private ForeignCollection<GeometryColumns> geometryColumns;

	/**
	 * Matrix Tile Set
	 */
	@ForeignCollectionField(eager = false)
	private ForeignCollection<TileMatrixSet> tileMatrixSet;

	/**
	 * Default Constructor
	 */
	public SpatialReferenceSystem() {

	}

	/**
	 * Copy Constructor
	 * 
	 * @param srs
	 *            srs to copy
	 * @since 1.3.0
	 */
	public SpatialReferenceSystem(SpatialReferenceSystem srs) {
		srsName = srs.srsName;
		srsId = srs.srsId;
		organization = srs.organization;
		organizationCoordsysId = srs.organizationCoordsysId;
		definition = srs.definition;
		description = srs.description;
		definition_12_063 = srs.definition_12_063;
	}

	/**
	 * Get the id
	 * 
	 * @return id
	 */
	public long getId() {
		return srsId;
	}

	/**
	 * Set the id
	 * 
	 * @param id
	 *            id
	 */
	public void setId(long id) {
		this.srsId = id;
	}

	/**
	 * Get the srs name
	 * 
	 * @return srs name
	 */
	public String getSrsName() {
		return srsName;
	}

	/**
	 * Set the srs name
	 * 
	 * @param srsName
	 *            srs name
	 */
	public void setSrsName(String srsName) {
		this.srsName = srsName;
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
	 */
	public void setSrsId(long srsId) {
		this.srsId = srsId;
	}

	/**
	 * Get the organization
	 * 
	 * @return organization
	 */
	public String getOrganization() {
		return organization;
	}

	/**
	 * Set the organization
	 * 
	 * @param organization
	 *            organization
	 */
	public void setOrganization(String organization) {
		this.organization = organization;
	}

	/**
	 * Get the organization coordsys id
	 * 
	 * @return organization coordsys id
	 */
	public long getOrganizationCoordsysId() {
		return organizationCoordsysId;
	}

	/**
	 * Set the organization coordsys id
	 * 
	 * @param organizationCoordsysId
	 *            organization coordsys id
	 */
	public void setOrganizationCoordsysId(long organizationCoordsysId) {
		this.organizationCoordsysId = organizationCoordsysId;
	}

	/**
	 * Get the definition
	 * 
	 * @return definition
	 */
	public String getDefinition() {
		return definition;
	}

	/**
	 * Set the definition
	 * 
	 * @param definition
	 *            definition
	 */
	public void setDefinition(String definition) {
		this.definition = definition;
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
	 * Get the 12_063 WKT definition
	 * 
	 * @return 12_06 3WKT definition
	 * @since 1.2.1
	 */
	public String getDefinition_12_063() {
		return definition_12_063;
	}

	/**
	 * Set the 12_063 WKT definition
	 * 
	 * @param definition_12_063
	 *            12_063 WKT definition
	 * @since 1.2.1
	 */
	public void setDefinition_12_063(String definition_12_063) {
		this.definition_12_063 = definition_12_063;
	}

	/**
	 * Get the contents
	 * 
	 * @return contents
	 */
	public ForeignCollection<Contents> getContents() {
		return contents;
	}

	/**
	 * Get the geometry columns
	 * 
	 * @return geometry columns
	 */
	public ForeignCollection<GeometryColumns> getGeometryColumns() {
		return geometryColumns;
	}

	/**
	 * Get the tile matrix set
	 * 
	 * @return tile matrix set
	 */
	public ForeignCollection<TileMatrixSet> getTileMatrixSet() {
		return tileMatrixSet;
	}

	/**
	 * Get the projection for the Spatial Reference System
	 * 
	 * @return projection
	 * @since 3.0.0
	 */
	public Projection getProjection() {

		String authority = getOrganization();
		long code = getOrganizationCoordsysId();
		String definition = getDefinition_12_063();
		if (definition == null) {
			definition = getDefinition();
		}

		Projection projection = ProjectionFactory.getProjection(authority,
				code, null, definition);

		return projection;
	}

	/**
	 * Get the projection transform from the provided projection to the Spatial
	 * Reference System projection
	 * 
	 * @param projection
	 *            from projection
	 * @return projection transform
	 * @since 3.0.0
	 */
	public ProjectionTransform getTransformation(Projection projection) {
		Projection projectionTo = getProjection();
		return projection.getTransformation(projectionTo);
	}

}
