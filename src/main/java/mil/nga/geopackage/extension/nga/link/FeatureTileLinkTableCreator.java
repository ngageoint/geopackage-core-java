package mil.nga.geopackage.extension.nga.link;

import mil.nga.geopackage.GeoPackageCore;
import mil.nga.geopackage.db.GeoPackageCoreConnection;
import mil.nga.geopackage.db.GeoPackageTableCreator;

/**
 * Feature Tile Link Extension Table Creator
 * 
 * @author osbornb
 * @since 4.0.0
 */
public class FeatureTileLinkTableCreator extends GeoPackageTableCreator {

	/**
	 * Constructor
	 *
	 * @param db
	 *            db connection
	 */
	public FeatureTileLinkTableCreator(GeoPackageCoreConnection db) {
		super(db);
	}

	/**
	 * Constructor
	 *
	 * @param geoPackage
	 *            GeoPackage
	 */
	public FeatureTileLinkTableCreator(GeoPackageCore geoPackage) {
		super(geoPackage);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getAuthor() {
		return FeatureTileTableCoreLinker.EXTENSION_AUTHOR;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return FeatureTileTableCoreLinker.EXTENSION_NAME_NO_AUTHOR;
	}

	/**
	 * Create Feature Tile Link table
	 *
	 * @return executed statements
	 */
	public int createFeatureTileLink() {
		return execScript();
	}

}
