package mil.nga.geopackage.extension.nga.contents;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.stmt.QueryBuilder;

import mil.nga.geopackage.GeoPackageCore;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.contents.Contents;
import mil.nga.geopackage.contents.ContentsDao;
import mil.nga.geopackage.contents.ContentsDataType;
import mil.nga.geopackage.db.GeoPackageCoreConnection;
import mil.nga.geopackage.extension.BaseExtension;
import mil.nga.geopackage.extension.ExtensionScopeType;
import mil.nga.geopackage.extension.Extensions;
import mil.nga.geopackage.extension.nga.NGAExtensions;
import mil.nga.geopackage.property.GeoPackageProperties;
import mil.nga.geopackage.property.PropertyConstants;

/**
 * This extension assigns a unique integer identifier to tables defined in the
 * contents. Allows foreign key referencing to a contents (text based primary
 * key) by an integer identifier.
 * 
 * http://ngageoint.github.io/GeoPackage/docs/extensions/contents-id.html
 * 
 * @author osbornb
 * @since 3.2.0
 */
public class ContentsIdExtension extends BaseExtension {

	/**
	 * Logger
	 */
	private static final Logger logger = Logger
			.getLogger(ContentsIdExtension.class.getName());

	/**
	 * Extension author
	 */
	public static final String EXTENSION_AUTHOR = NGAExtensions.EXTENSION_AUTHOR;

	/**
	 * Extension name without the author
	 */
	public static final String EXTENSION_NAME_NO_AUTHOR = "contents_id";

	/**
	 * Extension, with author and name
	 */
	public static final String EXTENSION_NAME = Extensions
			.buildExtensionName(EXTENSION_AUTHOR, EXTENSION_NAME_NO_AUTHOR);

	/**
	 * Extension definition URL
	 */
	public static final String EXTENSION_DEFINITION = GeoPackageProperties
			.getProperty(PropertyConstants.EXTENSIONS,
					EXTENSION_NAME_NO_AUTHOR);

	/**
	 * Contents Id DAO
	 */
	private final ContentsIdDao contentsIdDao;

	/**
	 * Constructor
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 */
	public ContentsIdExtension(GeoPackageCore geoPackage) {
		super(geoPackage);
		contentsIdDao = getContentsIdDao();
	}

	/**
	 * Get the contents id DAO
	 * 
	 * @return dao
	 */
	public ContentsIdDao getDao() {
		return contentsIdDao;
	}

	/**
	 * Determine if the GeoPackage has the extension
	 * 
	 * @return true if has extension
	 */
	public boolean has() {

		boolean exists = false;
		try {
			exists = has(EXTENSION_NAME, null, null)
					&& contentsIdDao.isTableExists();
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to check for contents id for GeoPackage: "
							+ geoPackage.getName(),
					e);
		}

		return exists;
	}

	/**
	 * Get the contents id object
	 * 
	 * @param contents
	 *            contents
	 * @return contents id or null
	 */
	public ContentsId get(Contents contents) {
		return get(contents.getTableName());
	}

	/**
	 * Get the contents id object
	 * 
	 * @param tableName
	 *            table name
	 * @return contents id or null
	 */
	public ContentsId get(String tableName) {
		ContentsId contentsId = null;
		try {
			if (contentsIdDao.isTableExists()) {
				contentsId = contentsIdDao.queryForTableName(tableName);
			}
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to query contents id for GeoPackage: "
							+ geoPackage.getName() + ", Table Name: "
							+ tableName,
					e);
		}
		return contentsId;
	}

	/**
	 * Get the contents id
	 * 
	 * @param contents
	 *            contents
	 * @return contents id or null
	 */
	public Long getId(Contents contents) {
		return getId(contents.getTableName());
	}

	/**
	 * Get the contents id
	 * 
	 * @param tableName
	 *            table name
	 * @return contents id or null
	 */
	public Long getId(String tableName) {
		Long id = null;
		ContentsId contentsId = get(tableName);
		if (contentsId != null) {
			id = contentsId.getId();
		}
		return id;
	}

	/**
	 * Create a contents id
	 * 
	 * @param contents
	 *            contents
	 * @return new contents id
	 */
	public ContentsId create(Contents contents) {
		return create(contents.getTableName());
	}

	/**
	 * Create a contents id
	 * 
	 * @param tableName
	 *            table name
	 * @return new contents id
	 */
	public ContentsId create(String tableName) {

		if (!has()) {
			getOrCreateExtension();
		}

		ContentsId contentsId = new ContentsId();
		Contents contents = geoPackage.getTableContents(tableName);
		contentsId.setContents(contents);
		try {
			contentsIdDao.create(contentsId);
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to create contents id for GeoPackage: "
							+ geoPackage.getName() + ", Table Name: "
							+ tableName,
					e);
		}
		return contentsId;
	}

	/**
	 * Create a contents id
	 * 
	 * @param contents
	 *            contents
	 * @return new contents id
	 */
	public long createId(Contents contents) {
		return createId(contents.getTableName());
	}

	/**
	 * Create a contents id
	 * 
	 * @param tableName
	 *            table name
	 * @return new contents id
	 */
	public long createId(String tableName) {
		ContentsId contentsId = create(tableName);
		return contentsId.getId();
	}

	/**
	 * Get or create a contents id
	 * 
	 * @param contents
	 *            contents
	 * @return new or existing contents id
	 */
	public ContentsId getOrCreate(Contents contents) {
		return getOrCreate(contents.getTableName());
	}

	/**
	 * Get or create a contents id
	 * 
	 * @param tableName
	 *            table name
	 * @return new or existing contents id
	 */
	public ContentsId getOrCreate(String tableName) {
		ContentsId contentsId = get(tableName);
		if (contentsId == null) {
			contentsId = create(tableName);
		}
		return contentsId;
	}

	/**
	 * Get or create a contents id
	 * 
	 * @param contents
	 *            contents
	 * @return new or existing contents id
	 */
	public long getOrCreateId(Contents contents) {
		return getOrCreateId(contents.getTableName());
	}

	/**
	 * Get or create a contents id
	 * 
	 * @param tableName
	 *            table name
	 * @return new or existing contents id
	 */
	public long getOrCreateId(String tableName) {
		ContentsId contentsId = getOrCreate(tableName);
		return contentsId.getId();
	}

	/**
	 * Delete the contents id for the contents
	 * 
	 * @param contents
	 *            contents
	 * 
	 * @return true if deleted
	 */
	public boolean delete(Contents contents) {
		return delete(contents.getTableName());
	}

	/**
	 * Delete the contents id for the table
	 * 
	 * @param tableName
	 *            table name
	 * 
	 * @return true if deleted
	 */
	public boolean delete(String tableName) {
		boolean deleted = false;
		try {
			if (contentsIdDao.isTableExists()) {
				deleted = contentsIdDao.deleteByTableName(tableName) > 0;
			}
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to delete Contents Id extension table. GeoPackage: "
							+ geoPackage.getName() + ", Table Name: "
							+ tableName,
					e);
		}
		return deleted;
	}

	/**
	 * Create contents ids for contents currently without
	 * 
	 * @return newly created contents ids count
	 */
	public int createIds() {
		return createIds("");
	}

	/**
	 * Create contents ids for contents of the data type and currently without
	 * 
	 * @param type
	 *            contents data type
	 * @return newly created contents ids count
	 */
	public int createIds(ContentsDataType type) {
		return createIds(type.getName());
	}

	/**
	 * Create contents ids for contents of the data type and currently without
	 * 
	 * @param type
	 *            contents data type
	 * @return newly created contents ids count
	 */
	public int createIds(String type) {

		getOrCreateExtension();

		List<String> tables = getMissing(type);

		for (String tableName : tables) {
			getOrCreate(tableName);
		}

		return tables.size();
	}

	/**
	 * Delete all contents ids
	 * 
	 * @return deleted contents ids count
	 */
	public int deleteIds() {
		int deleted = 0;
		try {
			if (contentsIdDao.isTableExists()) {
				deleted = contentsIdDao
						.delete(contentsIdDao.deleteBuilder().prepare());
			}
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to delete all contents ids. GeoPackage: "
							+ geoPackage.getName(),
					e);
		}
		return deleted;
	}

	/**
	 * Delete contents ids for contents of the data type
	 * 
	 * @param type
	 *            contents data type
	 * @return deleted contents ids count
	 */
	public int deleteIds(ContentsDataType type) {
		return deleteIds(type.getName());
	}

	/**
	 * Delete contents ids for contents of the data type
	 * 
	 * @param type
	 *            contents data type
	 * @return deleted contents ids count
	 */
	public int deleteIds(String type) {
		int deleted = 0;
		try {
			if (contentsIdDao.isTableExists()) {
				List<ContentsId> contentsIds = getIds(type);
				deleted = contentsIdDao.delete(contentsIds);
			}
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to delete contents ids by type. GeoPackage: "
							+ geoPackage.getName() + ", Type: " + type,
					e);
		}
		return deleted;
	}

	/**
	 * Get all contents ids
	 * 
	 * @return contents ids
	 */
	public List<ContentsId> getIds() {
		List<ContentsId> contentsIds = null;
		try {
			if (contentsIdDao.isTableExists()) {
				contentsIds = contentsIdDao.queryForAll();
			} else {
				contentsIds = new ArrayList<>();
			}
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to query for all contents ids. GeoPackage: "
							+ geoPackage.getName(),
					e);
		}
		return contentsIds;
	}

	/**
	 * Get the count of contents ids
	 * 
	 * @return count
	 */
	public long count() {
		long count = 0;
		if (has()) {
			try {
				count = contentsIdDao.countOf();
			} catch (SQLException e) {
				throw new GeoPackageException(
						"Failed to count contents ids. GeoPackage: "
								+ geoPackage.getName(),
						e);
			}
		}
		return count;
	}

	/**
	 * Get by contents data type
	 * 
	 * @param type
	 *            contents data type
	 * @return contents ids
	 */
	public List<ContentsId> getIds(ContentsDataType type) {
		return getIds(type.getName());
	}

	/**
	 * Get by contents data type
	 * 
	 * @param type
	 *            contents data type
	 * @return contents ids
	 */
	public List<ContentsId> getIds(String type) {

		List<ContentsId> contentsIds = null;

		ContentsDao contentsDao = geoPackage.getContentsDao();

		try {

			if (contentsIdDao.isTableExists()) {

				QueryBuilder<Contents, String> contentsQueryBuilder = contentsDao
						.queryBuilder();
				QueryBuilder<ContentsId, Long> contentsIdQueryBuilder = contentsIdDao
						.queryBuilder();

				contentsQueryBuilder.where().eq(Contents.COLUMN_DATA_TYPE,
						type);
				contentsIdQueryBuilder.join(contentsQueryBuilder);

				contentsIds = contentsIdQueryBuilder.query();

			} else {
				contentsIds = new ArrayList<>();
			}
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to query for contents id by contents data type. GeoPackage: "
							+ geoPackage.getName() + ", Type: " + type,
					e);
		}

		return contentsIds;
	}

	/**
	 * Get contents without contents ids
	 * 
	 * @return table names without contents ids
	 */
	public List<String> getMissing() {
		return getMissing("");
	}

	/**
	 * Get contents by data type without contents ids
	 * 
	 * @param type
	 *            contents data type
	 * @return table names without contents ids
	 */
	public List<String> getMissing(ContentsDataType type) {
		return getMissing(type.getName());
	}

	/**
	 * Get contents by data type without contents ids
	 * 
	 * @param type
	 *            contents data type
	 * @return table names without contents ids
	 */
	public List<String> getMissing(String type) {

		List<String> missing = new ArrayList<>();

		ContentsDao contentsDao = geoPackage.getContentsDao();

		GenericRawResults<String[]> results = null;
		try {

			StringBuilder query = new StringBuilder();
			query.append("SELECT ");
			query.append(Contents.COLUMN_TABLE_NAME);
			query.append(" FROM ");
			query.append(Contents.TABLE_NAME);

			StringBuilder where = new StringBuilder();

			String[] queryArgs;
			if (type != null && !type.isEmpty()) {
				where.append(Contents.COLUMN_DATA_TYPE);
				where.append(" = ?");
				queryArgs = new String[] { type };
			} else {
				queryArgs = new String[] {};
				type = null;
			}

			if (contentsIdDao.isTableExists()) {
				if (where.length() > 0) {
					where.append(" AND ");
				}
				where.append(Contents.COLUMN_TABLE_NAME);
				where.append(" NOT IN (SELECT ");
				where.append(ContentsId.COLUMN_TABLE_NAME);
				where.append(" FROM ");
				where.append(ContentsId.TABLE_NAME);
				where.append(")");
			}

			if (where.length() > 0) {
				query.append(" WHERE ").append(where);
			}

			results = contentsDao.queryRaw(query.toString(), queryArgs);
			for (String[] resultRow : results) {
				missing.add(resultRow[0]);
			}
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to query for missing contents ids. GeoPackage: "
							+ geoPackage.getName() + ", Type: " + type,
					e);
		} finally {
			if (results != null) {
				try {
					results.close();
				} catch (IOException e) {
					logger.log(Level.WARNING,
							"Failed to close generic raw results from missing contents ids query. type: "
									+ type,
							e);
				}
			}
		}

		return missing;
	}

	/**
	 * Get or create if needed the extension
	 * 
	 * @return extensions object
	 */
	public Extensions getOrCreateExtension() {

		// Create table
		createContentsIdTable();

		Extensions extension = getOrCreate(EXTENSION_NAME, null, null,
				EXTENSION_DEFINITION, ExtensionScopeType.READ_WRITE);

		ContentsDao contentsDao = geoPackage.getContentsDao();
		try {

			if (contentsDao.queryForId(ContentsId.TABLE_NAME) == null) {

				Contents contents = new Contents();
				contents.setTableName(ContentsId.TABLE_NAME);
				contents.setDataTypeName(Extensions.TABLE_NAME);
				contents.setIdentifier(ContentsId.TABLE_NAME);

				contentsDao.create(contents);

			}

		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to create contents entry for contents id. GeoPackage: "
							+ geoPackage.getName(),
					e);
		}

		return extension;
	}

	/**
	 * Get the extension
	 * 
	 * @return extensions object or null if one does not exist
	 */
	public Extensions getExtension() {

		Extensions extension = get(EXTENSION_NAME, null, null);

		return extension;
	}

	/**
	 * Remove all trace of the extension
	 */
	public void removeExtension() {

		try {
			if (contentsIdDao.isTableExists()) {
				geoPackage.dropTable(contentsIdDao.getTableName());
			}
			if (extensionsDao.isTableExists()) {
				extensionsDao.deleteByExtension(EXTENSION_NAME);
			}
			geoPackage.getContentsDao().deleteById(ContentsId.TABLE_NAME);
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to delete Contents Id extension and table. GeoPackage: "
							+ geoPackage.getName(),
					e);
		}
	}

	/**
	 * Get a Contents Id DAO
	 * 
	 * @return contents id dao
	 * @since 4.0.0
	 */
	public ContentsIdDao getContentsIdDao() {
		return getContentsIdDao(geoPackage);
	}

	/**
	 * Get a Contents Id DAO
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * @return contents id dao
	 * @since 4.0.0
	 */
	public static ContentsIdDao getContentsIdDao(GeoPackageCore geoPackage) {
		return ContentsIdDao.create(geoPackage);
	}

	/**
	 * Get a Contents Id DAO
	 * 
	 * @param db
	 *            database connection
	 * @return contents id dao
	 * @since 4.0.0
	 */
	public static ContentsIdDao getContentsIdDao(GeoPackageCoreConnection db) {
		return ContentsIdDao.create(db);
	}

	/**
	 * Create the Contents Id Table if it does not exist
	 * 
	 * @return true if created
	 * @since 4.0.0
	 */
	public boolean createContentsIdTable() {
		verifyWritable();

		boolean created = false;

		try {
			if (!contentsIdDao.isTableExists()) {
				ContentsIdTableCreator tableCreator = new ContentsIdTableCreator(
						geoPackage);
				created = tableCreator.createContentsId() > 0;
			}
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to check if " + ContentsId.class.getSimpleName()
							+ " table exists and create it",
					e);
		}

		return created;
	}

}
