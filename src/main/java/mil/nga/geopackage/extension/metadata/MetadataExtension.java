package mil.nga.geopackage.extension.metadata;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import mil.nga.geopackage.GeoPackageConstants;
import mil.nga.geopackage.GeoPackageCore;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.db.GeoPackageCoreConnection;
import mil.nga.geopackage.extension.BaseExtension;
import mil.nga.geopackage.extension.ExtensionScopeType;
import mil.nga.geopackage.extension.Extensions;
import mil.nga.geopackage.extension.metadata.reference.MetadataReference;
import mil.nga.geopackage.extension.metadata.reference.MetadataReferenceDao;
import mil.nga.geopackage.property.GeoPackageProperties;
import mil.nga.geopackage.property.PropertyConstants;

/**
 * Metadata extension
 * 
 * https://www.geopackage.org/spec/#extension_metadata
 * 
 * @author osbornb
 * @since 1.1.8
 */
public class MetadataExtension extends BaseExtension {

	/**
	 * Name
	 */
	public static final String NAME = "metadata";

	/**
	 * Extension name
	 */
	public static final String EXTENSION_NAME = GeoPackageConstants.EXTENSION_AUTHOR
			+ Extensions.EXTENSION_NAME_DIVIDER + NAME;

	/**
	 * Extension definition URL
	 */
	public static final String DEFINITION = GeoPackageProperties
			.getProperty(PropertyConstants.EXTENSIONS, NAME);

	/**
	 * Constructor
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * 
	 */
	public MetadataExtension(GeoPackageCore geoPackage) {
		super(geoPackage);
	}

	/**
	 * Get or create the extension
	 * 
	 * @return extensions
	 */
	public List<Extensions> getOrCreate() {

		List<Extensions> extensions = new ArrayList<>();

		extensions.add(getOrCreate(EXTENSION_NAME, Metadata.TABLE_NAME, null,
				DEFINITION, ExtensionScopeType.READ_WRITE));
		extensions.add(getOrCreate(EXTENSION_NAME, MetadataReference.TABLE_NAME,
				null, DEFINITION, ExtensionScopeType.READ_WRITE));

		return extensions;
	}

	/**
	 * Determine if the GeoPackage has the extension
	 * 
	 * @return true if has extension
	 */
	public boolean has() {

		boolean exists = has(EXTENSION_NAME);

		return exists;
	}

	/**
	 * Remove all trace of the extension
	 * 
	 * @since 3.2.0
	 */
	public void removeExtension() {

		if (geoPackage.isTable(MetadataReference.TABLE_NAME)) {
			geoPackage.dropTable(MetadataReference.TABLE_NAME);
		}

		if (geoPackage.isTable(Metadata.TABLE_NAME)) {
			geoPackage.dropTable(Metadata.TABLE_NAME);
		}

		try {
			if (extensionsDao.isTableExists()) {
				extensionsDao.deleteByExtension(EXTENSION_NAME);
			}
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to delete Metadata extension. GeoPackage: "
							+ geoPackage.getName(),
					e);
		}

	}

	/**
	 * Get a Metadata DAO
	 * 
	 * @return Metadata DAO
	 * @since 4.0.0
	 */
	public MetadataDao getMetadataDao() {
		return getMetadataDao(geoPackage);
	}

	/**
	 * Get a Metadata DAO
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * @return Metadata DAO
	 * @since 4.0.0
	 */
	public static MetadataDao getMetadataDao(GeoPackageCore geoPackage) {
		return MetadataDao.create(geoPackage);
	}

	/**
	 * Get a Metadata DAO
	 * 
	 * @param db
	 *            database connection
	 * @return Metadata DAO
	 * @since 4.0.0
	 */
	public static MetadataDao getMetadataDao(GeoPackageCoreConnection db) {
		return MetadataDao.create(db);
	}

	/**
	 * Create the Metadata table if it does not already exist
	 * 
	 * @return true if created
	 * @since 4.0.0
	 */
	public boolean createMetadataTable() {
		verifyWritable();

		boolean created = false;
		MetadataDao dao = getMetadataDao();
		try {
			if (!dao.isTableExists()) {
				created = geoPackage.getTableCreator().createMetadata() > 0;
				if (created) {
					// Create the metadata extension record
					MetadataExtension metadataExtension = new MetadataExtension(
							geoPackage);
					metadataExtension.getOrCreate();
				}
			}
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to check if " + Metadata.class.getSimpleName()
							+ " table exists and create it",
					e);
		}
		return created;
	}

	/**
	 * Get a Metadata Reference DAO
	 * 
	 * @return Metadata Reference DAO
	 * @since 4.0.0
	 */
	public MetadataReferenceDao getMetadataReferenceDao() {
		return getMetadataReferenceDao(geoPackage);
	}

	/**
	 * Get a Metadata Reference DAO
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * @return Metadata Reference DAO
	 * @since 4.0.0
	 */
	public static MetadataReferenceDao getMetadataReferenceDao(
			GeoPackageCore geoPackage) {
		return MetadataReferenceDao.create(geoPackage);
	}

	/**
	 * Get a Metadata Reference DAO
	 * 
	 * @param db
	 *            database connection
	 * @return Metadata Reference DAO
	 * @since 4.0.0
	 */
	public static MetadataReferenceDao getMetadataReferenceDao(
			GeoPackageCoreConnection db) {
		return MetadataReferenceDao.create(db);
	}

	/**
	 * Create the Metadata Reference table if it does not already exist
	 * 
	 * @return true if created
	 * @since 4.0.0
	 */
	public boolean createMetadataReferenceTable() {
		verifyWritable();

		boolean created = false;
		MetadataReferenceDao dao = getMetadataReferenceDao();
		try {
			if (!dao.isTableExists()) {
				created = geoPackage.getTableCreator()
						.createMetadataReference() > 0;
			}
		} catch (SQLException e) {
			throw new GeoPackageException("Failed to check if "
					+ MetadataReference.class.getSimpleName()
					+ " table exists and create it", e);
		}
		return created;
	}

}
