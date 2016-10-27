package mil.nga.geopackage.validate;

import java.io.File;
import java.sql.SQLException;

import mil.nga.geopackage.GeoPackageConstants;
import mil.nga.geopackage.GeoPackageCore;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.core.contents.Contents;
import mil.nga.geopackage.core.srs.SpatialReferenceSystem;
import mil.nga.geopackage.io.GeoPackageIOUtils;

/**
 * Performs GeoPackage validations
 * 
 * @author osbornb
 */
public class GeoPackageValidate {

	/**
	 * Check the file extension to see if it is a GeoPackage
	 * 
	 * @param file
	 * @return true if GeoPackage extension
	 */
	public static boolean hasGeoPackageExtension(File file) {
		String extension = GeoPackageIOUtils.getFileExtension(file);
		boolean isGeoPackage = extension != null
				&& (extension
						.equalsIgnoreCase(GeoPackageConstants.GEOPACKAGE_EXTENSION) || extension
						.equalsIgnoreCase(GeoPackageConstants.GEOPACKAGE_EXTENDED_EXTENSION));
		return isGeoPackage;
	}

	/**
	 * Validate the extension file as a GeoPackage
	 * 
	 * @param file
	 */
	public static void validateGeoPackageExtension(File file) {
		if (!hasGeoPackageExtension(file)) {
			throw new GeoPackageException("GeoPackage database file '" + file
					+ "' does not have a valid extension of '"
					+ GeoPackageConstants.GEOPACKAGE_EXTENSION + "' or '"
					+ GeoPackageConstants.GEOPACKAGE_EXTENDED_EXTENSION + "'");
		}
	}

	/**
	 * Check the GeoPackage for the minimum required tables
	 * 
	 * @param geoPackage
	 * @return true if has minimum tables
	 */
	public static boolean hasMinimumTables(GeoPackageCore geoPackage) {
		boolean hasMinimum;
		try {
			hasMinimum = geoPackage.getSpatialReferenceSystemDao()
					.isTableExists()
					&& geoPackage.getContentsDao().isTableExists();
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to check for required minimum GeoPackage tables. GeoPackage Name: "
							+ geoPackage.getName());
		}
		return hasMinimum;
	}

	/**
	 * Validate the GeoPackage has the minimum required tables
	 * 
	 * @param geoPackage
	 */
	public static void validateMinimumTables(GeoPackageCore geoPackage) {
		if (!hasMinimumTables(geoPackage)) {
			throw new GeoPackageException(
					"Invalid GeoPackage. Does not contain required tables: "
							+ SpatialReferenceSystem.TABLE_NAME + " & "
							+ Contents.TABLE_NAME + ", GeoPackage Name: "
							+ geoPackage.getName());
		}
	}

}
