package mil.nga.geopackage.extension.nga.scale;

import mil.nga.geopackage.GeoPackageCore;
import mil.nga.geopackage.db.GeoPackageCoreConnection;
import mil.nga.geopackage.db.GeoPackageTableCreator;

/**
 * Tile Scaling Extension Table Creator
 * 
 * @author osbornb
 * @since 4.0.0
 */
public class TileScalingTableCreator extends GeoPackageTableCreator {

	/**
	 * Constructor
	 *
	 * @param db
	 *            db connection
	 */
	public TileScalingTableCreator(GeoPackageCoreConnection db) {
		super(db);
	}

	/**
	 * Constructor
	 *
	 * @param geoPackage
	 *            GeoPackage
	 */
	public TileScalingTableCreator(GeoPackageCore geoPackage) {
		super(geoPackage);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getAuthor() {
		return TileTableScaling.EXTENSION_AUTHOR;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return TileTableScaling.EXTENSION_NAME_NO_AUTHOR;
	}

	/**
	 * Create Tile Scaling table
	 *
	 * @return executed statements
	 */
	public int createTileScaling() {
		return execScript();
	}

}
