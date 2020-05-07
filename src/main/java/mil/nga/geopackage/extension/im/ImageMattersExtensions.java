package mil.nga.geopackage.extension.im;

import mil.nga.geopackage.GeoPackageCore;
import mil.nga.geopackage.extension.ExtensionManagement;

/**
 * Image Matters Extensions
 * 
 * https://gitlab.com/imagemattersllc/ogc-vtp2/-/tree/master/extensions
 * 
 * @author osbornb
 * @since 4.0.0
 */
public class ImageMattersExtensions extends ExtensionManagement {

	/**
	 * Extension author
	 */
	public static final String EXTENSION_AUTHOR = "im";

	/**
	 * Constructor
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 */
	public ImageMattersExtensions(GeoPackageCore geoPackage) {
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
