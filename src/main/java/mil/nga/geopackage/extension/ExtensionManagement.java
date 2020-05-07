package mil.nga.geopackage.extension;

import mil.nga.geopackage.GeoPackageCore;

/**
 * Extension Management for deleting and copying extensions
 * 
 * @author osbornb
 * @since 4.0.0
 */
public abstract class ExtensionManagement {

	/**
	 * GeoPackage
	 */
	protected final GeoPackageCore geoPackage;

	/**
	 * Constructor
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 */
	public ExtensionManagement(GeoPackageCore geoPackage) {
		this.geoPackage = geoPackage;
	}

	/**
	 * Get the GeoPackage
	 * 
	 * @return GeoPackage
	 */
	public GeoPackageCore getGeoPackage() {
		return geoPackage;
	}

	/**
	 * Get the extension author
	 * 
	 * @return author
	 */
	public abstract String getAuthor();

	/**
	 * Delete all table extensions for the table
	 * 
	 * @param table
	 *            table name
	 */
	public abstract void deleteTableExtensions(String table);

	/**
	 * Delete all extensions including custom extension tables
	 */
	public abstract void deleteExtensions();

	/**
	 * Copy all table extensions for the table
	 * 
	 * @param table
	 *            table name
	 * @param newTable
	 *            new table name
	 */
	public abstract void copyTableExtensions(String table, String newTable);

}
