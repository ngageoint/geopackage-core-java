package mil.nga.geopackage.contents;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.support.ConnectionSource;

import mil.nga.geopackage.BoundingBox;
import mil.nga.geopackage.GeoPackageCore;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.db.GeoPackageCoreConnection;
import mil.nga.geopackage.db.GeoPackageDao;
import mil.nga.geopackage.features.columns.GeometryColumns;
import mil.nga.geopackage.features.columns.GeometryColumnsDao;
import mil.nga.geopackage.tiles.matrix.TileMatrix;
import mil.nga.geopackage.tiles.matrix.TileMatrixDao;
import mil.nga.geopackage.tiles.matrixset.TileMatrixSet;
import mil.nga.geopackage.tiles.matrixset.TileMatrixSetDao;
import mil.nga.sf.proj.Projection;

/**
 * Contents Data Access Object
 * 
 * @author osbornb
 */
public class ContentsDao extends GeoPackageDao<Contents, String> {

	/**
	 * Create the DAO
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * @return dao
	 * @since 4.0.0
	 */
	public static ContentsDao create(GeoPackageCore geoPackage) {
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
	public static ContentsDao create(GeoPackageCoreConnection db) {
		return GeoPackageDao.createDao(db, Contents.class);
	}

	/**
	 * Geometry Columns DAO
	 */
	private GeometryColumnsDao geometryColumnsDao;

	/**
	 * Tile Matrix Set DAO
	 */
	private TileMatrixSetDao tileMatrixSetDao;

	/**
	 * Tile Matrix DAO
	 */
	private TileMatrixDao tileMatrixDao;

	/**
	 * Constructor, required by ORMLite
	 * 
	 * @param connectionSource
	 *            connection source
	 * @param dataClass
	 *            data class
	 * @throws SQLException
	 *             upon creation error
	 */
	public ContentsDao(ConnectionSource connectionSource,
			Class<Contents> dataClass) throws SQLException {
		super(connectionSource, dataClass);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * Verify optional tables have been created
	 */
	@Override
	public int create(Contents contents) throws SQLException {
		verifyCreate(contents);
		return super.create(contents);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * Verify optional tables have been created
	 */
	@Override
	public Contents createIfNotExists(Contents contents) throws SQLException {
		verifyCreate(contents);
		return super.createIfNotExists(contents);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * Verify optional tables have been created
	 */
	@Override
	public CreateOrUpdateStatus createOrUpdate(Contents contents)
			throws SQLException {
		verifyCreate(contents);
		return super.createOrUpdate(contents);
	}

	/**
	 * Get table names by data type
	 * 
	 * @param dataType
	 *            data type
	 * @return table names
	 * @throws SQLException
	 *             upon query error
	 * @since 1.2.1
	 */
	public List<String> getTables(ContentsDataType dataType)
			throws SQLException {
		return getTables(dataType.getName());
	}

	/**
	 * Get table names by data types
	 * 
	 * @param dataTypes
	 *            data types
	 * @return table names
	 * @throws SQLException
	 *             upon query error
	 * @since 4.0.0
	 */
	public List<String> getTables(ContentsDataType... dataTypes)
			throws SQLException {
		return getTables(dataTypeNames(dataTypes));
	}

	/**
	 * Get table names by data type
	 * 
	 * @param dataType
	 *            data type
	 * @return table names
	 * @throws SQLException
	 *             upon query error
	 * @since 3.0.1
	 */
	public List<String> getTables(String dataType) throws SQLException {
		return getTables(new String[] { dataType });
	}

	/**
	 * Get table names by data types
	 * 
	 * @param dataTypes
	 *            data types
	 * @return table names
	 * @throws SQLException
	 *             upon query error
	 * @since 4.0.0
	 */
	public List<String> getTables(String... dataTypes) throws SQLException {
		return tableNames(getContents(Contents.COLUMN_TABLE_NAME, dataTypes));
	}

	/**
	 * Get contents by data type
	 * 
	 * @param dataType
	 *            data type
	 * @return list of contents
	 * @throws SQLException
	 *             upon query error
	 * @since 1.2.1
	 */
	public List<Contents> getContents(ContentsDataType dataType)
			throws SQLException {
		return getContents(dataType.getName());
	}

	/**
	 * Get contents by data types
	 * 
	 * @param dataTypes
	 *            data types
	 * @return list of contents
	 * @throws SQLException
	 *             upon query error
	 * @since 4.0.0
	 */
	public List<Contents> getContents(ContentsDataType... dataTypes)
			throws SQLException {
		return getContents(dataTypeNames(dataTypes));
	}

	/**
	 * Get contents by data type
	 * 
	 * @param dataType
	 *            data type
	 * @return list of contents
	 * @throws SQLException
	 *             upon query error
	 * @since 3.0.1
	 */
	public List<Contents> getContents(String dataType) throws SQLException {
		return queryForEq(Contents.COLUMN_DATA_TYPE, dataType);
	}

	/**
	 * Get contents by data types
	 * 
	 * @param dataTypes
	 *            data types
	 * @return list of contents
	 * @throws SQLException
	 *             upon query error
	 * @since 4.0.0
	 */
	public List<Contents> getContents(String... dataTypes) throws SQLException {
		return getContents(null, dataTypes);
	}

	/**
	 * Get table names
	 *
	 * @return table names
	 * @throws SQLException
	 *             upon query error
	 * @since 1.2.1
	 */
	public List<String> getTables() throws SQLException {
		return tableNames(
				getContents(Contents.COLUMN_TABLE_NAME, new String[] {}));
	}

	/**
	 * Get the bounding box for all tables in the provided projection
	 * 
	 * @param projection
	 *            desired bounding box projection
	 * 
	 * @return bounding box
	 * @since 3.1.0
	 */
	public BoundingBox getBoundingBox(Projection projection) {

		BoundingBox boundingBox = null;

		List<String> tables = null;
		try {
			tables = getTables();
		} catch (SQLException e) {
			throw new GeoPackageException("Failed to query for contents tables",
					e);
		}

		for (String table : tables) {
			BoundingBox tableBoundingBox = getBoundingBox(projection, table);
			if (tableBoundingBox != null) {
				if (boundingBox != null) {
					boundingBox = boundingBox.union(tableBoundingBox);
				} else {
					boundingBox = tableBoundingBox;
				}
			}
		}

		return boundingBox;
	}

	/**
	 * Get the bounding box for the table in the table's projection
	 * 
	 * @param table
	 *            table name
	 * 
	 * @return bounding box
	 * @since 3.1.0
	 */
	public BoundingBox getBoundingBox(String table) {
		return getBoundingBox(null, table);
	}

	/**
	 * Get the bounding box for the table in the provided projection
	 * 
	 * @param projection
	 *            desired bounding box projection
	 * @param table
	 *            table name
	 * 
	 * @return bounding box
	 * @since 3.1.0
	 */
	public BoundingBox getBoundingBox(Projection projection, String table) {

		BoundingBox boundingBox = null;

		Contents contents = null;
		try {
			contents = queryForId(table);
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to query for contents of table: " + table, e);
		}

		if (contents == null) {
			throw new GeoPackageException("No contents for table: " + table);
		}

		boundingBox = contents.getBoundingBox(projection);

		return boundingBox;
	}

	/**
	 * Delete the Contents, cascading
	 * 
	 * @param contents
	 *            contents
	 * @return deleted count
	 * @throws SQLException
	 *             upon deletion error
	 */
	public int deleteCascade(Contents contents) throws SQLException {
		int count = 0;

		if (contents != null) {

			ContentsDataType dataType = contents.getDataType();

			if (dataType != null) {

				switch (dataType) {
				case FEATURES:

					// Delete Geometry Columns
					GeometryColumnsDao geometryColumnsDao = getGeometryColumnsDao();
					if (geometryColumnsDao.isTableExists()) {
						GeometryColumns geometryColumns = contents
								.getGeometryColumns();
						if (geometryColumns != null) {
							geometryColumnsDao.delete(geometryColumns);
						}
					}

					break;

				case TILES:

					// Delete Tile Matrix collection
					TileMatrixDao tileMatrixDao = getTileMatrixDao();
					if (tileMatrixDao.isTableExists()) {
						ForeignCollection<TileMatrix> tileMatrixCollection = contents
								.getTileMatrix();
						if (!tileMatrixCollection.isEmpty()) {
							tileMatrixDao.delete(tileMatrixCollection);
						}
					}

					// Delete Tile Matrix Set
					TileMatrixSetDao tileMatrixSetDao = getTileMatrixSetDao();
					if (tileMatrixSetDao.isTableExists()) {
						TileMatrixSet tileMatrixSet = contents
								.getTileMatrixSet();
						if (tileMatrixSet != null) {
							tileMatrixSetDao.delete(tileMatrixSet);
						}
					}

					break;

				case ATTRIBUTES:

					dropTable(contents.getTableName());

					break;

				}

			} else {
				dropTable(contents.getTableName());
			}

			count = delete(contents);
		}

		return count;
	}

	/**
	 * Delete the Contents, cascading optionally including the user table
	 * 
	 * @param contents
	 *            contents
	 * @param userTable
	 *            true if a user table
	 * @return deleted count
	 * @throws SQLException
	 *             upon deletion error
	 */
	public int deleteCascade(Contents contents, boolean userTable)
			throws SQLException {
		int count = deleteCascade(contents);

		if (userTable) {
			dropTable(contents.getTableName());
		}

		return count;
	}

	/**
	 * Delete the collection of Contents, cascading
	 * 
	 * @param contentsCollection
	 *            contents collection
	 * @return deleted count
	 * @throws SQLException
	 *             upon deletion error
	 */
	public int deleteCascade(Collection<Contents> contentsCollection)
			throws SQLException {
		return deleteCascade(contentsCollection, false);
	}

	/**
	 * Delete the collection of Contents, cascading optionally including the
	 * user table
	 * 
	 * @param contentsCollection
	 *            contents collection
	 * @param userTable
	 *            true if a user table
	 * @return deleted count
	 * @throws SQLException
	 *             upon deletion error
	 */
	public int deleteCascade(Collection<Contents> contentsCollection,
			boolean userTable) throws SQLException {
		int count = 0;
		if (contentsCollection != null) {
			for (Contents contents : contentsCollection) {
				count += deleteCascade(contents, userTable);
			}
		}
		return count;
	}

	/**
	 * Delete the Contents matching the prepared query, cascading
	 * 
	 * @param preparedDelete
	 *            prepared delete query
	 * @return deleted count
	 * @throws SQLException
	 *             upon deletion error
	 */
	public int deleteCascade(PreparedQuery<Contents> preparedDelete)
			throws SQLException {
		return deleteCascade(preparedDelete, false);
	}

	/**
	 * Delete the Contents matching the prepared query, cascading optionally
	 * including the user table
	 * 
	 * @param preparedDelete
	 *            prepared delete query
	 * @param userTable
	 *            true if a user table
	 * @return deleted count
	 * @throws SQLException
	 *             upon deletion error
	 */
	public int deleteCascade(PreparedQuery<Contents> preparedDelete,
			boolean userTable) throws SQLException {
		int count = 0;
		if (preparedDelete != null) {
			List<Contents> contentsList = query(preparedDelete);
			count = deleteCascade(contentsList, userTable);
		}
		return count;
	}

	/**
	 * Delete a Contents by id, cascading
	 * 
	 * @param id
	 *            id
	 * @return deleted count
	 * @throws SQLException
	 *             upon deletion error
	 */
	public int deleteByIdCascade(String id) throws SQLException {
		return deleteByIdCascade(id, false);
	}

	/**
	 * Delete a Contents by id, cascading optionally including the user table
	 * 
	 * @param id
	 *            id
	 * @param userTable
	 *            true if a user table
	 * @return deleted count
	 * @throws SQLException
	 *             upon deletion error
	 */
	public int deleteByIdCascade(String id, boolean userTable)
			throws SQLException {
		int count = 0;
		if (id != null) {
			Contents contents = queryForId(id);
			if (contents != null) {
				count = deleteCascade(contents, userTable);
			} else if (userTable) {
				dropTable(id);
			}
		}
		return count;
	}

	/**
	 * Delete the Contents with the provided ids, cascading
	 * 
	 * @param idCollection
	 *            id collection
	 * @return deleted count
	 * @throws SQLException
	 *             upon deletion error
	 */
	public int deleteIdsCascade(Collection<String> idCollection)
			throws SQLException {
		return deleteIdsCascade(idCollection, false);
	}

	/**
	 * Delete the Contents with the provided ids, cascading optionally including
	 * the user table
	 * 
	 * @param idCollection
	 *            id collection
	 * @param userTable
	 *            true if a user table
	 * @return deleted count
	 * @throws SQLException
	 *             upon deletion error
	 */
	public int deleteIdsCascade(Collection<String> idCollection,
			boolean userTable) throws SQLException {
		int count = 0;
		if (idCollection != null) {
			for (String id : idCollection) {
				count += deleteByIdCascade(id, userTable);
			}
		}
		return count;
	}

	/**
	 * Delete the table
	 * 
	 * @param table
	 *            table name
	 */
	public void deleteTable(String table) {
		try {
			deleteByIdCascade(table, true);
		} catch (SQLException e) {
			throw new GeoPackageException("Failed to delete table: " + table,
					e);
		}
	}

	/**
	 * Get the contents
	 * 
	 * @param column
	 *            select column
	 * @param dataTypes
	 *            data types
	 * @return contents
	 * @throws SQLException
	 *             upon query error
	 */
	private List<Contents> getContents(String column, String... dataTypes)
			throws SQLException {
		QueryBuilder<Contents, String> qb = queryBuilder();
		if (column != null) {
			qb.selectColumns(column);
		}
		if (dataTypes != null && dataTypes.length > 0) {
			Where<Contents, String> where = qb.where();
			for (int i = 0; i < dataTypes.length; i++) {
				if (i > 0) {
					where.or();
				}
				where.eq(Contents.COLUMN_DATA_TYPE, dataTypes[i]);
			}
		}
		PreparedQuery<Contents> query = qb.prepare();
		return query(query);
	}

	/**
	 * Get the data type names from the data types
	 * 
	 * @param dataTypes
	 *            data types
	 * @return data type names
	 */
	private String[] dataTypeNames(ContentsDataType... dataTypes) {
		String[] types = new String[dataTypes.length];
		if (dataTypes != null) {
			for (int i = 0; i < dataTypes.length; i++) {
				types[i] = dataTypes[i].getName();
			}
		}
		return types;
	}

	/**
	 * Get the tables names from the contents
	 * 
	 * @param contents
	 *            contents
	 * @return table names
	 */
	private List<String> tableNames(List<Contents> contents) {
		List<String> tableNames = new ArrayList<String>();
		if (contents != null) {
			for (Contents content : contents) {
				tableNames.add(content.getTableName());
			}
		}
		return tableNames;
	}

	/**
	 * Verify the tables are in the expected state for the Contents create
	 * 
	 * @param contents
	 *            contents
	 * @throws SQLException
	 *             upon verify creation error
	 */
	private void verifyCreate(Contents contents) throws SQLException {
		ContentsDataType dataType = contents.getDataType();
		if (dataType != null) {
			switch (dataType) {
			case FEATURES:

				// Features require Geometry Columns table (Spec Requirement 21)
				GeometryColumnsDao geometryColumnsDao = getGeometryColumnsDao();
				if (!geometryColumnsDao.isTableExists()) {
					throw new GeoPackageException("A data type of "
							+ dataType.getName() + " requires the "
							+ GeometryColumns.class.getSimpleName()
							+ " table to first be created using the GeoPackage.");
				}

				break;

			case TILES:
				verifyTiles(dataType);
				break;

			case ATTRIBUTES:
				break;

			default:
				throw new GeoPackageException(
						"Unsupported data type: " + dataType);
			}
		}

		// Verify the feature or tile table exists
		if (!tableOrViewExists(contents.getTableName())) {
			throw new GeoPackageException(
					"No table or view exists for Content Table Name: "
							+ contents.getTableName()
							+ ". Table must first be created.");
		}
	}

	/**
	 * Verify the required tile tables exist
	 * 
	 * @param dataType
	 *            data type
	 * @throws SQLException
	 *             upon tiles verification error
	 */
	private void verifyTiles(ContentsDataType dataType) throws SQLException {
		// Tiles require Tile Matrix Set table (Spec Requirement 37)
		TileMatrixSetDao tileMatrixSetDao = getTileMatrixSetDao();
		if (!tileMatrixSetDao.isTableExists()) {
			throw new GeoPackageException("A data type of " + dataType.getName()
					+ " requires the " + TileMatrixSet.class.getSimpleName()
					+ " table to first be created using the GeoPackage.");
		}

		// Tiles require Tile Matrix table (Spec Requirement 41)
		TileMatrixDao tileMatrixDao = getTileMatrixDao();
		if (!tileMatrixDao.isTableExists()) {
			throw new GeoPackageException("A data type of " + dataType.getName()
					+ " requires the " + TileMatrix.class.getSimpleName()
					+ " table to first be created using the GeoPackage.");
		}
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

	/**
	 * Get or create a Tile Matrix DAO
	 * 
	 * @return tile matrix dao
	 */
	private TileMatrixDao getTileMatrixDao() {
		if (tileMatrixDao == null) {
			tileMatrixDao = TileMatrixDao.create(db);
		}
		return tileMatrixDao;
	}

}
