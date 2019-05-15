package mil.nga.geopackage.extension;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import mil.nga.geopackage.GeoPackageConstants;
import mil.nga.geopackage.GeoPackageCore;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.metadata.Metadata;
import mil.nga.geopackage.metadata.reference.MetadataReference;
import mil.nga.geopackage.property.GeoPackageProperties;
import mil.nga.geopackage.property.PropertyConstants;

/**
 * Metadata extension
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
	public static final String EXTENSION_NAME = GeoPackageConstants.GEO_PACKAGE_EXTENSION_AUTHOR
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

}
