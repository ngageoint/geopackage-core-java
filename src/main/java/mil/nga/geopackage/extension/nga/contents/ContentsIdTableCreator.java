package mil.nga.geopackage.extension.nga.contents;

import mil.nga.geopackage.GeoPackageCore;
import mil.nga.geopackage.db.GeoPackageCoreConnection;
import mil.nga.geopackage.db.GeoPackageTableCreator;

/**
 * Contents Id Extension Table Creator
 * 
 * @author osbornb
 * @since 4.0.0
 */
public class ContentsIdTableCreator extends GeoPackageTableCreator {

	/**
	 * Constructor
	 *
	 * @param db
	 *            db connection
	 */
	public ContentsIdTableCreator(GeoPackageCoreConnection db) {
		super(db);
	}

	/**
	 * Constructor
	 *
	 * @param geoPackage
	 *            GeoPackage
	 */
	public ContentsIdTableCreator(GeoPackageCore geoPackage) {
		super(geoPackage);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getAuthor() {
		return ContentsIdExtension.EXTENSION_AUTHOR;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return ContentsIdExtension.EXTENSION_NAME_NO_AUTHOR;
	}

	/**
	 * Create Contents Id table
	 *
	 * @return executed statements
	 */
	public int createContentsId() {
		return execScript();
	}

}
