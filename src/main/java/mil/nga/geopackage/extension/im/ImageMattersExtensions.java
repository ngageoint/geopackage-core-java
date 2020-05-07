package mil.nga.geopackage.extension.im;

import mil.nga.geopackage.GeoPackageCore;
import mil.nga.geopackage.extension.ExtensionManagement;
import mil.nga.geopackage.extension.im.portrayal.PortrayalExtension;

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

		deleteVectorTiles(table);

		// Delete future extensions for the table here
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteExtensions() {

		deleteVectorTilesExtension();
		deletePortrayalExtension();

		// Delete future extension tables here

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void copyTableExtensions(String table, String newTable) {

		copyVectorTiles(table, newTable);

		// Copy future extensions for the table here
	}

	/**
	 * Delete the Vector Tiles extensions for the table
	 * 
	 * @param table
	 *            table name
	 */
	public void deleteVectorTiles(String table) {
		// TODO delete vector tiles for the table
	}

	/**
	 * Delete the Vector Tiles extension
	 */
	public void deleteVectorTilesExtension() {
		// TODO delete the vector tiles extension
	}

	/**
	 * Copy the Vector Tiles extensions for the table
	 * 
	 * @param table
	 *            table name
	 * @param newTable
	 *            new table name
	 */
	public void copyVectorTiles(String table, String newTable) {
		// TODO copy vector tiles to the new table
	}

	/**
	 * Delete the Portrayal Extension
	 */
	public void deletePortrayalExtension() {

		PortrayalExtension portrayalExtension = new PortrayalExtension(
				geoPackage);
		if (portrayalExtension.has()) {
			portrayalExtension.removeExtension();
		}

	}

}
