package mil.nga.geopackage.extension.ecere;

import mil.nga.geopackage.GeoPackageCore;
import mil.nga.geopackage.extension.ExtensionManagement;

/**
 * Ecere Extensions
 * 
 * @author osbornb
 * @since 4.0.0
 */
public class EcereExtensions extends ExtensionManagement {

	/**
	 * Extension author
	 */
	public static final String EXTENSION_AUTHOR = "ecere";

	/**
	 * Constructor
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 */
	public EcereExtensions(GeoPackageCore geoPackage) {
		super(geoPackage);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getAuthor() {
		return EXTENSION_AUTHOR;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteTableExtensions(String table) {

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteExtensions() {

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void copyTableExtensions(String table, String newTable) {

	}

}
