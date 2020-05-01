package mil.nga.geopackage.extension.vector_tiles;

import mil.nga.geopackage.GeoPackageCore;
import mil.nga.geopackage.db.GeoPackageCoreConnection;
import mil.nga.geopackage.db.GeoPackageTableCreator;

/**
 * Vector Tiles Extension Table Creator
 * 
 * @author jyutzler
 * @author osbornb
 * @since 3.5.1
 */
public class VectorTilesTableCreator extends GeoPackageTableCreator {

	/**
	 * Fields property
	 */
	public static final String FIELDS = "fields";

	/**
	 * Layers property
	 */
	public static final String LAYERS = "layers";

	/**
	 * Constructor
	 *
	 * @param db
	 *            db connection
	 */
	public VectorTilesTableCreator(GeoPackageCoreConnection db) {
		super(db);
	}

	/**
	 * Constructor
	 *
	 * @param geoPackage
	 *            GeoPackage
	 */
	public VectorTilesTableCreator(GeoPackageCore geoPackage) {
		super(geoPackage);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getAuthor() {
		return VectorTilesExtension.EXTENSION_AUTHOR;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return VectorTilesExtension.EXTENSION_NAME_NO_AUTHOR;
	}

	/**
	 * Create the Layers table
	 *
	 * @return executed statements
	 */
	public int createLayers() {
		return execScript(LAYERS);
	}

	/**
	 * Create the Fields table
	 *
	 * @return executed statements
	 */
	public int createFields() {
		return execScript(FIELDS);
	}

}
