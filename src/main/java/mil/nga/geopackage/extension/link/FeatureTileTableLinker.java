package mil.nga.geopackage.extension.link;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import mil.nga.geopackage.GeoPackageCore;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.extension.ExtensionScopeType;
import mil.nga.geopackage.extension.Extensions;
import mil.nga.geopackage.extension.ExtensionsDao;
import mil.nga.geopackage.property.GeoPackageProperties;
import mil.nga.geopackage.property.PropertyConstants;

/**
 * Feature Tile Table linker, used to link feature and tile tables together when
 * the tiles represent the feature data
 * 
 * @author osbornb
 * @since 1.1.5
 */
public class FeatureTileTableLinker {

	/**
	 * Extension author
	 */
	public static final String EXTENSION_AUTHOR = "nga";

	/**
	 * Extension name without the author
	 */
	public static final String EXTENSION_NAME_NO_AUTHOR = "feature_tile_link";

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
	 * GeoPackage
	 */
	private final GeoPackageCore geoPackage;

	/**
	 * Extensions DAO
	 */
	private final ExtensionsDao extensionsDao;

	/**
	 * Feature Tile Link DAO
	 */
	private final FeatureTileLinkDao featureTileLinkDao;

	/**
	 * Constructor
	 * 
	 * @param geoPackage
	 */
	public FeatureTileTableLinker(GeoPackageCore geoPackage) {
		this.geoPackage = geoPackage;
		extensionsDao = geoPackage.getExtensionsDao();
		featureTileLinkDao = geoPackage.getFeatureTileLinkDao();
	}

	/**
	 * Get the GeoPackage
	 * 
	 * @return GeoPackage
	 */
	public GeoPackageCore getGeoPackage() {
		return geoPackage;
	}

	/**
	 * Get the feature tile link DAO
	 * 
	 * @return dao
	 */
	public FeatureTileLinkDao getDao() {
		return featureTileLinkDao;
	}

	/**
	 * Link a feature and tile table together. Does nothing if already linked.
	 * 
	 * @param featureTable
	 * @param tileTable
	 */
	public void link(String featureTable, String tileTable) {

		if (!isLinked(featureTable, tileTable)) {
			getOrCreateExtension();

			try {
				if (!featureTileLinkDao.isTableExists()) {
					geoPackage.createFeatureTileLinkTable();
				}

				FeatureTileLink link = new FeatureTileLink();
				link.setFeatureTableName(featureTable);
				link.setTileTableName(tileTable);

				featureTileLinkDao.create(link);
			} catch (SQLException e) {
				throw new GeoPackageException(
						"Failed to create feature tile link for GeoPackage: "
								+ geoPackage.getName() + ", Feature Table: "
								+ featureTable + ", Tile Table: " + tileTable,
						e);
			}
		}
	}

	/**
	 * Determine if the feature table is linked to the tile table
	 * 
	 * @param featureTable
	 * @param tileTable
	 * @return true if linked
	 */
	public boolean isLinked(String featureTable, String tileTable) {
		FeatureTileLink link = getLink(featureTable, tileTable);
		return link != null;
	}

	/**
	 * Get the feature and tile table link if it exists
	 * 
	 * @param featureTable
	 * @param tileTable
	 * @return link or null
	 */
	public FeatureTileLink getLink(String featureTable, String tileTable) {
		FeatureTileLink link = null;

		if (featureTileLinksActive()) {
			FeatureTileLinkKey id = new FeatureTileLinkKey(featureTable,
					tileTable);
			try {
				link = featureTileLinkDao.queryForId(id);
			} catch (SQLException e) {
				throw new GeoPackageException(
						"Failed to get feature tile link for GeoPackage: "
								+ geoPackage.getName() + ", Feature Table: "
								+ featureTable + ", Tile Table: " + tileTable,
						e);
			}
		}

		return link;
	}

	/**
	 * Query for feature tile links by feature table
	 * 
	 * @param featureTable
	 * @return links
	 */
	public List<FeatureTileLink> queryForFeatureTable(String featureTable) {

		List<FeatureTileLink> links = null;

		if (featureTileLinksActive()) {
			links = featureTileLinkDao.queryForFeatureTableName(featureTable);
		} else {
			links = new ArrayList<FeatureTileLink>();
		}

		return links;
	}

	/**
	 * Query for feature tile links by tile table
	 * 
	 * @param tileTable
	 * @return links
	 */
	public List<FeatureTileLink> queryForTileTable(String tileTable) {

		List<FeatureTileLink> links = null;

		if (featureTileLinksActive()) {
			links = featureTileLinkDao.queryForTileTableName(tileTable);
		} else {
			links = new ArrayList<FeatureTileLink>();
		}

		return links;
	}

	/**
	 * Delete the feature tile table link
	 * 
	 * @param featureTable
	 * @param tileTable
	 * @return true if deleted
	 */
	public boolean deleteLink(String featureTable, String tileTable) {

		boolean deleted = false;

		try {
			if (featureTileLinkDao.isTableExists()) {
				FeatureTileLinkKey id = new FeatureTileLinkKey(featureTable,
						tileTable);
				deleted = featureTileLinkDao.deleteById(id) > 0;
			}
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to delete feature tile link for GeoPackage: "
							+ geoPackage.getName() + ", Feature Table: "
							+ featureTable + ", Tile Table: " + tileTable, e);
		}

		return deleted;
	}

	/**
	 * Delete the feature tile table links for the feature or tile table
	 * 
	 * @param table
	 * @return links deleted
	 */
	public int deleteLinks(String table) {

		int deleted = 0;

		try {
			if (featureTileLinkDao.isTableExists()) {
				deleted = featureTileLinkDao.deleteByTableName(table);
			}
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to delete feature tile links for GeoPackage: "
							+ geoPackage.getName() + ", Table: " + table, e);
		}

		return deleted;
	}

	/**
	 * Get or create if needed the extension
	 * 
	 * @return extensions object
	 */
	private Extensions getOrCreateExtension() {
		Extensions extension = getExtension();

		if (extension == null) {
			try {
				if (!extensionsDao.isTableExists()) {
					geoPackage.createExtensionsTable();
				}

				extension = new Extensions();
				extension.setTableName(null);
				extension.setColumnName(null);
				extension.setExtensionName(EXTENSION_AUTHOR,
						EXTENSION_NAME_NO_AUTHOR);
				extension.setDefinition(EXTENSION_DEFINITION);
				extension.setScope(ExtensionScopeType.READ_WRITE);

				extensionsDao.create(extension);
			} catch (SQLException e) {
				throw new GeoPackageException("Failed to create '"
						+ EXTENSION_NAME + "' extension for GeoPackage: "
						+ geoPackage.getName(), e);
			}
		}

		return extension;
	}

	/**
	 * Get the extension
	 * 
	 * @param tableName
	 * @return extensions object or null if one does not exist
	 */
	public Extensions getExtension() {

		Extensions extension = null;
		try {
			if (extensionsDao.isTableExists()) {
				extension = extensionsDao.queryByExtension(EXTENSION_NAME,
						null, null);

			}
		} catch (SQLException e) {
			throw new GeoPackageException("Failed to query for '"
					+ EXTENSION_NAME + "' extension for GeoPackage: "
					+ geoPackage.getName(), e);
		}
		return extension;
	}

	/**
	 * Determine if the feature tile link extension and table exists
	 * 
	 * @return
	 */
	private boolean featureTileLinksActive() {

		boolean active = false;

		if (getExtension() != null) {
			try {
				active = featureTileLinkDao.isTableExists();
			} catch (SQLException e) {
				throw new GeoPackageException(
						"Failed to check if the feature tile link table exists for GeoPackage: "
								+ geoPackage.getName(), e);
			}
		}

		return active;
	}

}
