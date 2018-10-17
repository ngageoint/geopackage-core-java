package mil.nga.geopackage.extension.contents;

import java.sql.SQLException;

import mil.nga.geopackage.GeoPackageCore;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.core.contents.Contents;
import mil.nga.geopackage.extension.BaseExtension;
import mil.nga.geopackage.extension.ExtensionScopeType;
import mil.nga.geopackage.extension.Extensions;
import mil.nga.geopackage.property.GeoPackageProperties;
import mil.nga.geopackage.property.PropertyConstants;

/**
 * This extension assigns a unique integer identifier to tables defined in the
 * contents. Allows foreign key referencing to a contents (text based primary
 * key) by an integer identifier.
 * 
 * @author osbornb
 * @since 3.1.1
 */
public class ContentsIdExtension extends BaseExtension {

	/**
	 * Extension author
	 */
	public static final String EXTENSION_AUTHOR = "nga";

	/**
	 * Extension name without the author
	 */
	public static final String EXTENSION_NAME_NO_AUTHOR = "contents_id";

	/**
	 * Extension, with author and name
	 */
	public static final String EXTENSION_NAME = Extensions.buildExtensionName(
			EXTENSION_AUTHOR, EXTENSION_NAME_NO_AUTHOR);

	/**
	 * Extension definition URL
	 */
	public static final String EXTENSION_DEFINITION = GeoPackageProperties
			.getProperty(PropertyConstants.EXTENSIONS, EXTENSION_NAME_NO_AUTHOR);

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
		contentsIdDao = geoPackage.getContentsIdDao();
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
							+ geoPackage.getName(), e);
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
		if (has()) {
			contentsId = contentsIdDao.queryForTableName(tableName);
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

		getOrCreateExtension();

		ContentsId contentsId = new ContentsId();
		contentsId.setTableName(tableName);
		try {
			contentsIdDao.create(contentsId);
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to create contents id for GeoPackage: "
							+ geoPackage.getName() + ", Table Name: "
							+ tableName, e);
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
			deleted = contentsIdDao.deleteByTableName(tableName) > 0;
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to delete Contents Id extension table. GeoPackage: "
							+ geoPackage.getName() + ", Table Name: "
							+ tableName, e);
		}
		return deleted;
	}

	/**
	 * Get or create if needed the extension
	 * 
	 * @return extensions object
	 */
	private Extensions getOrCreateExtension() {

		Extensions extension = getOrCreate(EXTENSION_NAME, null, null,
				EXTENSION_DEFINITION, ExtensionScopeType.READ_WRITE);

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
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to delete Contents Id extension and table. GeoPackage: "
							+ geoPackage.getName(), e);
		}
	}

}
