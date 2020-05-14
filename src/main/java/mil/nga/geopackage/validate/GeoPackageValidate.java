package mil.nga.geopackage.validate;

import java.io.File;
import java.sql.SQLException;

import mil.nga.geopackage.GeoPackageConstants;
import mil.nga.geopackage.GeoPackageCore;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.contents.Contents;
import mil.nga.geopackage.io.GeoPackageIOUtils;
import mil.nga.geopackage.srs.SpatialReferenceSystem;

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
	 *            GeoPackage file
	 * @return true if GeoPackage extension
	 */
	public static boolean hasGeoPackageExtension(File file) {
		String extension = GeoPackageIOUtils.getFileExtension(file);
		return isGeoPackageExtension(extension);
	}

	/**
	 * Check the file name extension to see if it is a GeoPackage
	 * 
	 * @param name
	 *            GeoPackage file name
	 * @return true if GeoPackage extension
	 * @since 3.5.0
	 */
	public static boolean hasGeoPackageExtension(String name) {
		String extension = GeoPackageIOUtils.getFileExtension(name);
		return isGeoPackageExtension(extension);
	}

	/**
	 * Check if a GeoPackage extension
	 * 
	 * @param extension
	 *            file extension
	 * @return true if GeoPackage extension
	 * @since 3.5.0
	 */
	public static boolean isGeoPackageExtension(String extension) {
		return extension != null && (extension
				.equalsIgnoreCase(GeoPackageConstants.EXTENSION)
				|| extension.equalsIgnoreCase(
						GeoPackageConstants.EXTENDED_EXTENSION));
	}

	/**
	 * Validate the extension file as a GeoPackage
	 * 
	 * @param file
	 *            GeoPackage file
	 */
	public static void validateGeoPackageExtension(File file) {
		if (!hasGeoPackageExtension(file)) {
			throw new GeoPackageException("GeoPackage database file '" + file
					+ "' does not have a valid extension of '"
					+ GeoPackageConstants.EXTENSION + "' or '"
					+ GeoPackageConstants.EXTENDED_EXTENSION + "'");
		}
	}

	/**
	 * Validate the extension file name as a GeoPackage
	 * 
	 * @param name
	 *            GeoPackage file name
	 * @since 3.5.0
	 */
	public static void validateGeoPackageExtension(String name) {
		if (!hasGeoPackageExtension(name)) {
			throw new GeoPackageException("GeoPackage database file name '"
					+ name + "' does not have a valid extension of '"
					+ GeoPackageConstants.EXTENSION + "' or '"
					+ GeoPackageConstants.EXTENDED_EXTENSION + "'");
		}
	}

	/**
	 * Add a GeoPackage extension if one does not exist
	 * 
	 * @param file
	 *            GeoPackage file
	 * @return GeoPackage file with extension
	 * @since 3.5.0
	 */
	public static File addGeoPackageExtension(File file) {
		if (!hasGeoPackageExtension(file)) {
			file = new File(file.getAbsolutePath() + "."
					+ GeoPackageConstants.EXTENSION);
		}
		return file;
	}

	/**
	 * Add a GeoPackage extension if one does not exist
	 * 
	 * @param name
	 *            GeoPackage file name
	 * @return GeoPackage name with extension
	 * @since 3.5.0
	 */
	public static String addGeoPackageExtension(String name) {
		if (!hasGeoPackageExtension(name)) {
			name += "." + GeoPackageConstants.EXTENSION;
		}
		return name;
	}

	/**
	 * Check the GeoPackage for the minimum required tables
	 * 
	 * @param geoPackage
	 *            GeoPackage
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
	 *            GeoPackage
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
