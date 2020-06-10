package mil.nga.geopackage.srs;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;

import mil.nga.geopackage.GeoPackageCore;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.contents.Contents;
import mil.nga.geopackage.contents.ContentsDao;
import mil.nga.geopackage.db.GeoPackageCoreConnection;
import mil.nga.geopackage.db.GeoPackageDao;
import mil.nga.geopackage.extension.CrsWktExtension;
import mil.nga.geopackage.features.columns.GeometryColumns;
import mil.nga.geopackage.features.columns.GeometryColumnsDao;
import mil.nga.geopackage.property.GeoPackageProperties;
import mil.nga.geopackage.property.PropertyConstants;
import mil.nga.geopackage.tiles.matrixset.TileMatrixSet;
import mil.nga.geopackage.tiles.matrixset.TileMatrixSetDao;
import mil.nga.sf.proj.ProjectionConstants;

/**
 * Spatial Reference System Data Access Object
 * 
 * @author osbornb
 */
public class SpatialReferenceSystemDao
		extends GeoPackageDao<SpatialReferenceSystem, Long> {

	/**
	 * Create the DAO
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * @return dao
	 * @since 4.0.0
	 */
	public static SpatialReferenceSystemDao create(GeoPackageCore geoPackage) {
		return create(geoPackage.getDatabase());
	}

	/**
	 * Create the DAO
	 * 
	 * @param db
	 *            database connection
	 * @return dao
	 * @since 4.0.0
	 */
	public static SpatialReferenceSystemDao create(
			GeoPackageCoreConnection db) {
		return GeoPackageDao.createDao(db, SpatialReferenceSystem.class);
	}

	/**
	 * Contents DAO
	 */
	private ContentsDao contentsDao;

	/**
	 * Geometry Columns DAO
	 */
	private GeometryColumnsDao geometryColumnsDao;

	/**
	 * Tile Matrix Set DAO
	 */
	private TileMatrixSetDao tileMatrixSetDao;

	/**
	 * CRS WKT Extension
	 */
	private CrsWktExtension crsWktExtension;

	/**
	 * Constructor, required by ORMLite
	 * 
	 * @param connectionSource
	 *            connection source
	 * @param dataClass
	 *            data class
	 * @throws SQLException
	 *             upon creation failure
	 */
	public SpatialReferenceSystemDao(ConnectionSource connectionSource,
			Class<SpatialReferenceSystem> dataClass) throws SQLException {
		super(connectionSource, dataClass);
	}

	/**
	 * Set the CRS WKT Extension
	 * 
	 * @param crsWktExtension
	 *            CRS WKT Extension
	 * @since 1.1.8
	 */
	public void setCrsWktExtension(CrsWktExtension crsWktExtension) {
		this.crsWktExtension = crsWktExtension;
	}

	/**
	 * Determine if the SRS table contains the extension definition 12 063
	 * column for CRS WKT
	 * 
	 * @return true if has extension
	 * @since 1.2.1
	 */
	public boolean hasDefinition_12_063() {
		return crsWktExtension != null && crsWktExtension.has();
	}

	/**
	 * Creates the required EPSG WGS84 Spatial Reference System (spec
	 * Requirement 11)
	 *
	 * @return spatial reference system
	 * @throws SQLException
	 *             upon creation failure
	 */
	public SpatialReferenceSystem createWgs84() throws SQLException {

		SpatialReferenceSystem srs = new SpatialReferenceSystem();
		srs.setSrsName(GeoPackageProperties.getProperty(
				PropertyConstants.WGS_84, PropertyConstants.SRS_NAME));
		srs.setSrsId(GeoPackageProperties.getIntegerProperty(
				PropertyConstants.WGS_84, PropertyConstants.SRS_ID));
		srs.setOrganization(GeoPackageProperties.getProperty(
				PropertyConstants.WGS_84, PropertyConstants.ORGANIZATION));
		srs.setOrganizationCoordsysId(GeoPackageProperties.getIntegerProperty(
				PropertyConstants.WGS_84,
				PropertyConstants.ORGANIZATION_COORDSYS_ID));
		srs.setDefinition(GeoPackageProperties.getProperty(
				PropertyConstants.WGS_84, PropertyConstants.DEFINITION));
		srs.setDescription(GeoPackageProperties.getProperty(
				PropertyConstants.WGS_84, PropertyConstants.DESCRIPTION));
		create(srs);
		if (hasDefinition_12_063()) {
			srs.setDefinition_12_063(
					GeoPackageProperties.getProperty(PropertyConstants.WGS_84,
							PropertyConstants.DEFINITION_12_063));
			crsWktExtension.updateDefinition(srs.getSrsId(),
					srs.getDefinition_12_063());
		}

		return srs;
	}

	/**
	 * Creates the required Undefined Cartesian Spatial Reference System (spec
	 * Requirement 11)
	 *
	 * @return spatial reference system
	 * @throws SQLException
	 *             upon creation failure
	 */
	public SpatialReferenceSystem createUndefinedCartesian()
			throws SQLException {

		SpatialReferenceSystem srs = new SpatialReferenceSystem();
		srs.setSrsName(GeoPackageProperties.getProperty(
				PropertyConstants.UNDEFINED_CARTESIAN,
				PropertyConstants.SRS_NAME));
		srs.setSrsId(GeoPackageProperties.getIntegerProperty(
				PropertyConstants.UNDEFINED_CARTESIAN,
				PropertyConstants.SRS_ID));
		srs.setOrganization(GeoPackageProperties.getProperty(
				PropertyConstants.UNDEFINED_CARTESIAN,
				PropertyConstants.ORGANIZATION));
		srs.setOrganizationCoordsysId(GeoPackageProperties.getIntegerProperty(
				PropertyConstants.UNDEFINED_CARTESIAN,
				PropertyConstants.ORGANIZATION_COORDSYS_ID));
		srs.setDefinition(GeoPackageProperties.getProperty(
				PropertyConstants.UNDEFINED_CARTESIAN,
				PropertyConstants.DEFINITION));
		srs.setDescription(GeoPackageProperties.getProperty(
				PropertyConstants.UNDEFINED_CARTESIAN,
				PropertyConstants.DESCRIPTION));
		create(srs);
		if (hasDefinition_12_063()) {
			srs.setDefinition_12_063(GeoPackageProperties.getProperty(
					PropertyConstants.UNDEFINED_CARTESIAN,
					PropertyConstants.DEFINITION_12_063));
			crsWktExtension.updateDefinition(srs.getSrsId(),
					srs.getDefinition_12_063());
		}

		return srs;
	}

	/**
	 * Creates the required Undefined Geographic Spatial Reference System (spec
	 * Requirement 11)
	 *
	 * @return spatial reference system
	 * @throws SQLException
	 *             upon creation failure
	 */
	public SpatialReferenceSystem createUndefinedGeographic()
			throws SQLException {

		SpatialReferenceSystem srs = new SpatialReferenceSystem();
		srs.setSrsName(GeoPackageProperties.getProperty(
				PropertyConstants.UNDEFINED_GEOGRAPHIC,
				PropertyConstants.SRS_NAME));
		srs.setSrsId(GeoPackageProperties.getIntegerProperty(
				PropertyConstants.UNDEFINED_GEOGRAPHIC,
				PropertyConstants.SRS_ID));
		srs.setOrganization(GeoPackageProperties.getProperty(
				PropertyConstants.UNDEFINED_GEOGRAPHIC,
				PropertyConstants.ORGANIZATION));
		srs.setOrganizationCoordsysId(GeoPackageProperties.getIntegerProperty(
				PropertyConstants.UNDEFINED_GEOGRAPHIC,
				PropertyConstants.ORGANIZATION_COORDSYS_ID));
		srs.setDefinition(GeoPackageProperties.getProperty(
				PropertyConstants.UNDEFINED_GEOGRAPHIC,
				PropertyConstants.DEFINITION));
		srs.setDescription(GeoPackageProperties.getProperty(
				PropertyConstants.UNDEFINED_GEOGRAPHIC,
				PropertyConstants.DESCRIPTION));
		create(srs);
		if (hasDefinition_12_063()) {
			srs.setDefinition_12_063(GeoPackageProperties.getProperty(
					PropertyConstants.UNDEFINED_GEOGRAPHIC,
					PropertyConstants.DEFINITION_12_063));
			crsWktExtension.updateDefinition(srs.getSrsId(),
					srs.getDefinition_12_063());
		}

		return srs;
	}

	/**
	 * Creates the Web Mercator Spatial Reference System if it does not already
	 * exist
	 *
	 * @return spatial reference system
	 * @throws SQLException
	 *             upon creation failure
	 */
	public SpatialReferenceSystem createWebMercator() throws SQLException {

		SpatialReferenceSystem srs = new SpatialReferenceSystem();
		srs.setSrsName(GeoPackageProperties.getProperty(
				PropertyConstants.WEB_MERCATOR, PropertyConstants.SRS_NAME));
		srs.setSrsId(GeoPackageProperties.getIntegerProperty(
				PropertyConstants.WEB_MERCATOR, PropertyConstants.SRS_ID));
		srs.setOrganization(
				GeoPackageProperties.getProperty(PropertyConstants.WEB_MERCATOR,
						PropertyConstants.ORGANIZATION));
		srs.setOrganizationCoordsysId(GeoPackageProperties.getIntegerProperty(
				PropertyConstants.WEB_MERCATOR,
				PropertyConstants.ORGANIZATION_COORDSYS_ID));
		srs.setDefinition(GeoPackageProperties.getProperty(
				PropertyConstants.WEB_MERCATOR, PropertyConstants.DEFINITION));
		srs.setDescription(GeoPackageProperties.getProperty(
				PropertyConstants.WEB_MERCATOR, PropertyConstants.DESCRIPTION));
		create(srs);
		if (hasDefinition_12_063()) {
			srs.setDefinition_12_063(GeoPackageProperties.getProperty(
					PropertyConstants.WEB_MERCATOR,
					PropertyConstants.DEFINITION_12_063));
			crsWktExtension.updateDefinition(srs.getSrsId(),
					srs.getDefinition_12_063());
		}

		return srs;
	}

	/**
	 * Creates the required EPSG WGS84 Geographical 3D Spatial Reference System
	 *
	 * @return spatial reference system
	 * @throws SQLException
	 *             upon creation failure
	 * @since 1.2.1
	 */
	public SpatialReferenceSystem createWgs84Geographical3D()
			throws SQLException {

		SpatialReferenceSystem srs = new SpatialReferenceSystem();
		srs.setSrsName(GeoPackageProperties.getProperty(
				PropertyConstants.WGS_84_3D, PropertyConstants.SRS_NAME));
		srs.setSrsId(GeoPackageProperties.getIntegerProperty(
				PropertyConstants.WGS_84_3D, PropertyConstants.SRS_ID));
		srs.setOrganization(GeoPackageProperties.getProperty(
				PropertyConstants.WGS_84_3D, PropertyConstants.ORGANIZATION));
		srs.setOrganizationCoordsysId(GeoPackageProperties.getIntegerProperty(
				PropertyConstants.WGS_84_3D,
				PropertyConstants.ORGANIZATION_COORDSYS_ID));
		srs.setDefinition(GeoPackageProperties.getProperty(
				PropertyConstants.WGS_84_3D, PropertyConstants.DEFINITION));
		srs.setDescription(GeoPackageProperties.getProperty(
				PropertyConstants.WGS_84_3D, PropertyConstants.DESCRIPTION));
		create(srs);
		if (hasDefinition_12_063()) {
			srs.setDefinition_12_063(GeoPackageProperties.getProperty(
					PropertyConstants.WGS_84_3D,
					PropertyConstants.DEFINITION_12_063));
			crsWktExtension.updateDefinition(srs.getSrsId(),
					srs.getDefinition_12_063());
		}

		return srs;
	}

	/**
	 * Query to get the definition 12 063 value if the extension exists
	 * 
	 * @param srsId
	 *            srs id
	 * @return definition or null
	 * @since 1.2.1
	 */
	public String getDefinition_12_063(long srsId) {
		String definition = null;
		if (hasDefinition_12_063()) {
			definition = crsWktExtension.getDefinition(srsId);
		}
		return definition;
	}

	/**
	 * Query and set the definition 12 063 in the srs object if the extension
	 * exists
	 * 
	 * @param srs
	 *            spatial reference system
	 * @since 1.2.1
	 */
	public void setDefinition_12_063(SpatialReferenceSystem srs) {
		if (srs != null) {
			String definition = getDefinition_12_063(srs.getSrsId());
			if (definition != null) {
				srs.setDefinition_12_063(definition);
			}
		}
	}

	/**
	 * Query and set the definition 12 063 in the srs objects if the extension
	 * exists
	 * 
	 * @param srsList
	 *            srs list
	 * @since 1.2.1
	 */
	public void setDefinition_12_063(
			Collection<SpatialReferenceSystem> srsList) {
		for (SpatialReferenceSystem srs : srsList) {
			setDefinition_12_063(srs);
		}
	}

	/**
	 * Update the definition 12 063 in the database if the extension exists
	 * 
	 * @param srsId
	 *            srs id
	 * @param definition
	 *            definition
	 * @since 1.2.1
	 */
	public void updateDefinition_12_063(long srsId, String definition) {
		if (hasDefinition_12_063()) {
			crsWktExtension.updateDefinition(srsId, definition);
		}
	}

	/**
	 * Update the definition 12 063 in the database if the extension exists
	 * 
	 * @param srs
	 *            srs
	 * @since 1.2.1
	 */
	public void updateDefinition_12_063(SpatialReferenceSystem srs) {
		if (srs != null) {
			String definition = srs.getDefinition_12_063();
			if (definition != null) {
				updateDefinition_12_063(srs.getSrsId(), definition);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SpatialReferenceSystem queryForId(Long id) throws SQLException {
		SpatialReferenceSystem srs = super.queryForId(id);
		setDefinition_12_063(srs);
		return srs;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SpatialReferenceSystem queryForFirst(
			PreparedQuery<SpatialReferenceSystem> preparedQuery)
			throws SQLException {
		SpatialReferenceSystem srs = super.queryForFirst(preparedQuery);
		setDefinition_12_063(srs);
		return srs;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<SpatialReferenceSystem> queryForAll() throws SQLException {
		List<SpatialReferenceSystem> srsList = super.queryForAll();
		setDefinition_12_063(srsList);
		return srsList;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<SpatialReferenceSystem> queryForEq(String fieldName,
			Object value) throws SQLException {
		List<SpatialReferenceSystem> srsList = super.queryForEq(fieldName,
				value);
		setDefinition_12_063(srsList);
		return srsList;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<SpatialReferenceSystem> query(
			PreparedQuery<SpatialReferenceSystem> preparedQuery)
			throws SQLException {
		List<SpatialReferenceSystem> srsList = super.query(preparedQuery);
		setDefinition_12_063(srsList);
		return srsList;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<SpatialReferenceSystem> queryForMatching(
			SpatialReferenceSystem matchObj) throws SQLException {
		List<SpatialReferenceSystem> srsList = super.queryForMatching(matchObj);
		setDefinition_12_063(srsList);
		return srsList;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<SpatialReferenceSystem> queryForMatchingArgs(
			SpatialReferenceSystem matchObj) throws SQLException {
		List<SpatialReferenceSystem> srsList = super.queryForMatchingArgs(
				matchObj);
		setDefinition_12_063(srsList);
		return srsList;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<SpatialReferenceSystem> queryForFieldValues(
			Map<String, Object> fieldValues) throws SQLException {
		List<SpatialReferenceSystem> srsList = super.queryForFieldValues(
				fieldValues);
		setDefinition_12_063(srsList);
		return srsList;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<SpatialReferenceSystem> queryForFieldValuesArgs(
			Map<String, Object> fieldValues) throws SQLException {
		List<SpatialReferenceSystem> srsList = super.queryForFieldValuesArgs(
				fieldValues);
		setDefinition_12_063(srsList);
		return srsList;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SpatialReferenceSystem queryForSameId(SpatialReferenceSystem data)
			throws SQLException {
		SpatialReferenceSystem srs = super.queryForSameId(data);
		setDefinition_12_063(srs);
		return srs;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int create(SpatialReferenceSystem srs) throws SQLException {
		int result = super.create(srs);
		updateDefinition_12_063(srs);
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SpatialReferenceSystem createIfNotExists(SpatialReferenceSystem data)
			throws SQLException {
		SpatialReferenceSystem srs = super.createIfNotExists(data);
		updateDefinition_12_063(srs);
		return srs;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CreateOrUpdateStatus createOrUpdate(SpatialReferenceSystem data)
			throws SQLException {
		CreateOrUpdateStatus status = super.createOrUpdate(data);
		updateDefinition_12_063(data);
		return status;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int update(SpatialReferenceSystem data) throws SQLException {
		int result = super.update(data);
		updateDefinition_12_063(data);
		return result;
	}

	/**
	 * Get or Create the Spatial Reference System for the provided epsg
	 * 
	 * @param epsg
	 *            epsg
	 * @return srs
	 * @throws SQLException
	 *             upon query or creation failure
	 * @since 1.2.0
	 */
	public SpatialReferenceSystem getOrCreateFromEpsg(long epsg)
			throws SQLException {
		return getOrCreateCode(ProjectionConstants.AUTHORITY_EPSG, epsg);
	}

	/**
	 * Get or Create the Spatial Reference System for the provided organization
	 * and id
	 * 
	 * @param organization
	 *            organization
	 * @param coordsysId
	 *            coordsys id
	 * @return srs
	 * @throws SQLException
	 *             upon query or creation failure
	 * @since 1.3.0
	 */
	public SpatialReferenceSystem getOrCreateCode(String organization,
			long coordsysId) throws SQLException {

		SpatialReferenceSystem srs = queryForOrganizationCoordsysId(
				organization, coordsysId);

		srs = createIfNeeded(srs, organization, coordsysId);

		return srs;
	}

	/**
	 * Query for the organization coordsys id
	 * 
	 * @param organization
	 *            organization
	 * @param organizationCoordsysId
	 *            organization coordsys id
	 * @return srs
	 * @throws SQLException
	 *             upon query failure
	 * @since 1.3.0
	 */
	public SpatialReferenceSystem queryForOrganizationCoordsysId(
			String organization, long organizationCoordsysId)
			throws SQLException {
		SpatialReferenceSystem srs = null;

		QueryBuilder<SpatialReferenceSystem, Long> qb = queryBuilder();
		qb.where().like(SpatialReferenceSystem.COLUMN_ORGANIZATION,
				organization);
		qb.where().eq(SpatialReferenceSystem.COLUMN_ORGANIZATION_COORDSYS_ID,
				organizationCoordsysId);
		PreparedQuery<SpatialReferenceSystem> preparedQuery = qb.prepare();

		List<SpatialReferenceSystem> results = query(preparedQuery);

		if (!results.isEmpty()) {
			if (results.size() > 1) {
				throw new SQLException("More than one "
						+ SpatialReferenceSystem.class.getSimpleName()
						+ " returned for Organization: " + organization
						+ ", Organization Coordsys Id: "
						+ organizationCoordsysId);
			}
			srs = results.get(0);
		}
		return srs;
	}

	/**
	 * Create the srs if needed
	 * 
	 * @param srs
	 *            srs if exists or null
	 * @param organization
	 *            organization
	 * @param id
	 *            coordinate id
	 * @return srs
	 * @throws SQLException
	 *             on an error
	 */
	private SpatialReferenceSystem createIfNeeded(SpatialReferenceSystem srs,
			String organization, long id) throws SQLException {

		if (srs == null) {

			if (organization
					.equalsIgnoreCase(ProjectionConstants.AUTHORITY_EPSG)) {

				switch ((int) id) {
				case ProjectionConstants.EPSG_WORLD_GEODETIC_SYSTEM:
					srs = createWgs84();
					break;
				case ProjectionConstants.EPSG_WEB_MERCATOR:
					srs = createWebMercator();
					break;
				case ProjectionConstants.EPSG_WORLD_GEODETIC_SYSTEM_GEOGRAPHICAL_3D:
					srs = createWgs84Geographical3D();
					break;
				default:
					throw new GeoPackageException(
							"Spatial Reference System not supported for metadata creation. Organization: "
									+ organization + ", id: " + id);
				}
			} else if (organization
					.equalsIgnoreCase(ProjectionConstants.AUTHORITY_NONE)) {
				switch ((int) id) {
				case ProjectionConstants.UNDEFINED_CARTESIAN:
					srs = createUndefinedCartesian();
					break;
				case ProjectionConstants.UNDEFINED_GEOGRAPHIC:
					srs = createUndefinedGeographic();
					break;
				default:
					throw new GeoPackageException(
							"Spatial Reference System not supported for metadata creation. Organization: "
									+ organization + ", id: " + id);
				}
			} else {
				throw new GeoPackageException(
						"Spatial Reference System not supported for metadata creation. Organization: "
								+ organization + ", id: " + id);
			}
		} else {
			setDefinition_12_063(srs);
		}

		return srs;
	}

	/**
	 * Delete the Spatial Reference System, cascading
	 * 
	 * @param srs
	 *            spatial reference system
	 * @return deleted count
	 * @throws SQLException
	 *             upon deletion failure
	 */
	public int deleteCascade(SpatialReferenceSystem srs) throws SQLException {
		int count = 0;

		if (srs != null) {

			// Delete Contents
			ForeignCollection<Contents> contentsCollection = srs.getContents();
			if (!contentsCollection.isEmpty()) {
				ContentsDao dao = getContentsDao();
				dao.deleteCascade(contentsCollection);
			}

			// Delete Geometry Columns
			GeometryColumnsDao geometryColumnsDao = getGeometryColumnsDao();
			if (geometryColumnsDao.isTableExists()) {
				ForeignCollection<GeometryColumns> geometryColumnsCollection = srs
						.getGeometryColumns();
				if (!geometryColumnsCollection.isEmpty()) {
					geometryColumnsDao.delete(geometryColumnsCollection);
				}
			}

			// Delete Tile Matrix Set
			TileMatrixSetDao tileMatrixSetDao = getTileMatrixSetDao();
			if (tileMatrixSetDao.isTableExists()) {
				ForeignCollection<TileMatrixSet> tileMatrixSetCollection = srs
						.getTileMatrixSet();
				if (!tileMatrixSetCollection.isEmpty()) {
					tileMatrixSetDao.delete(tileMatrixSetCollection);
				}
			}

			// Delete
			count = delete(srs);
		}
		return count;
	}

	/**
	 * Delete the collection of Spatial Reference Systems, cascading
	 * 
	 * @param srsCollection
	 *            spatial reference system collection
	 * @return deleted count
	 * @throws SQLException
	 *             upon deletion failure
	 */
	public int deleteCascade(Collection<SpatialReferenceSystem> srsCollection)
			throws SQLException {
		int count = 0;
		if (srsCollection != null) {
			for (SpatialReferenceSystem srs : srsCollection) {
				count += deleteCascade(srs);
			}
		}
		return count;
	}

	/**
	 * Delete the Spatial Reference Systems matching the prepared query,
	 * cascading
	 * 
	 * @param preparedDelete
	 *            prepared delete query
	 * @return deleted count
	 * @throws SQLException
	 *             upon deletion failure
	 */
	public int deleteCascade(
			PreparedQuery<SpatialReferenceSystem> preparedDelete)
			throws SQLException {
		int count = 0;
		if (preparedDelete != null) {
			List<SpatialReferenceSystem> srsList = query(preparedDelete);
			count = deleteCascade(srsList);
		}
		return count;
	}

	/**
	 * Delete a Spatial Reference System by id, cascading
	 * 
	 * @param id
	 *            id
	 * @return deleted count
	 * @throws SQLException
	 *             upon deletion failure
	 */
	public int deleteByIdCascade(Long id) throws SQLException {
		int count = 0;
		if (id != null) {
			SpatialReferenceSystem srs = queryForId(id);
			if (srs != null) {
				count = deleteCascade(srs);
			}
		}
		return count;
	}

	/**
	 * Delete the Spatial Reference Systems with the provided ids, cascading
	 * 
	 * @param idCollection
	 *            id collection
	 * @return deleted count
	 * @throws SQLException
	 *             upon deletion failure
	 */
	public int deleteIdsCascade(Collection<Long> idCollection)
			throws SQLException {
		int count = 0;
		if (idCollection != null) {
			for (Long id : idCollection) {
				count += deleteByIdCascade(id);
			}
		}
		return count;
	}

	/**
	 * Get or create a Contents DAO
	 * 
	 * @return contents dao
	 */
	private ContentsDao getContentsDao() {
		if (contentsDao == null) {
			contentsDao = ContentsDao.create(db);
		}
		return contentsDao;
	}

	/**
	 * Get or create a Geometry Columns DAO
	 * 
	 * @return geometry columns dao
	 */
	private GeometryColumnsDao getGeometryColumnsDao() {
		if (geometryColumnsDao == null) {
			geometryColumnsDao = GeometryColumnsDao.create(db);
		}
		return geometryColumnsDao;
	}

	/**
	 * Get or create a Tile Matrix Set DAO
	 * 
	 * @return tile matrix set dao
	 */
	private TileMatrixSetDao getTileMatrixSetDao() {
		if (tileMatrixSetDao == null) {
			tileMatrixSetDao = TileMatrixSetDao.create(db);
		}
		return tileMatrixSetDao;
	}

}
