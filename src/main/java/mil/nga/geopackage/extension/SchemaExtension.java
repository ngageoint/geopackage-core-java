package mil.nga.geopackage.extension;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import mil.nga.geopackage.GeoPackageConstants;
import mil.nga.geopackage.GeoPackageCore;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.property.GeoPackageProperties;
import mil.nga.geopackage.property.PropertyConstants;
import mil.nga.geopackage.schema.columns.DataColumns;
import mil.nga.geopackage.schema.constraints.DataColumnConstraints;

/**
 * Schema extension
 * 
 * https://www.geopackage.org/spec/#extension_schema
 * 
 * @author osbornb
 * @since 1.1.8
 */
public class SchemaExtension extends BaseExtension {

	/**
	 * Name
	 */
	public static final String NAME = "schema";

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
	public SchemaExtension(GeoPackageCore geoPackage) {
		super(geoPackage);
	}

	/**
	 * Get or create the extension
	 * 
	 * @return extensions
	 */
	public List<Extensions> getOrCreate() {

		List<Extensions> extensions = new ArrayList<>();

		extensions.add(getOrCreate(EXTENSION_NAME, DataColumns.TABLE_NAME, null,
				DEFINITION, ExtensionScopeType.READ_WRITE));
		extensions.add(
				getOrCreate(EXTENSION_NAME, DataColumnConstraints.TABLE_NAME,
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

		if (geoPackage.isTable(DataColumns.TABLE_NAME)) {
			geoPackage.dropTable(DataColumns.TABLE_NAME);
		}

		if (geoPackage.isTable(DataColumnConstraints.TABLE_NAME)) {
			geoPackage.dropTable(DataColumnConstraints.TABLE_NAME);
		}

		try {
			if (extensionsDao.isTableExists()) {
				extensionsDao.deleteByExtension(EXTENSION_NAME);
			}
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to delete Schema extension. GeoPackage: "
							+ geoPackage.getName(),
					e);
		}

	}

}
