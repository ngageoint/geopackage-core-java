package mil.nga.geopackage.extension.ecere;

import mil.nga.geopackage.GeoPackageCore;
import mil.nga.geopackage.extension.ExtensionManagement;
import mil.nga.geopackage.extension.ecere.tile_matrix_set.TileMatrixSetExtension;

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

		// Delete future extensions for the table here
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteExtensions() {

		deleteTileMatrixSetExtension();

		// Delete future extension tables here
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void copyTableExtensions(String table, String newTable) {

		// Copy future extensions for the table here
	}

	/**
	 * Delete the Tile Matrix Set Extension
	 */
	public void deleteTileMatrixSetExtension() {

		TileMatrixSetExtension tileMatrixSetExtension = new TileMatrixSetExtension(
				geoPackage);
		if (tileMatrixSetExtension.has()) {
			tileMatrixSetExtension.removeExtension();
		}

	}

}
