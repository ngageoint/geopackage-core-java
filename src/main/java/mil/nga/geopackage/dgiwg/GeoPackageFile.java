package mil.nga.geopackage.dgiwg;

import java.io.File;

/**
 * DGIWG (Defence Geospatial Information Working Group) GeoPackage File
 * 
 * @author osbornb
 * @since 6.5.1
 */
public class GeoPackageFile {

	/**
	 * GeoPackage File
	 */
	private File file;

	/**
	 * DGIWG File Name
	 */
	private GeoPackageFileName fileName;

	/**
	 * Constructor
	 * 
	 * @param file
	 *            GeoPackage file
	 * @param fileName
	 *            DGIWG File Name
	 */
	public GeoPackageFile(File file, GeoPackageFileName fileName) {
		this.file = file;
		this.fileName = fileName;
	}

	/**
	 * Constructor
	 * 
	 * @param path
	 *            GeoPackage path
	 */
	public GeoPackageFile(String path) {
		this(path, new GeoPackageFileName(path));
	}

	/**
	 * Constructor
	 * 
	 * @param path
	 *            GeoPackage path
	 * @param fileName
	 *            DGIWG file name
	 */
	public GeoPackageFile(String path, GeoPackageFileName fileName) {
		this(new File(path), fileName);
	}

	/**
	 * Constructor
	 * 
	 * @param file
	 *            GeoPackage file
	 */
	public GeoPackageFile(File file) {
		this(file, new GeoPackageFileName(file));
	}

	/**
	 * Get the GeoPackage file
	 * 
	 * @return GeoPackage file
	 */
	public File getFile() {
		return file;
	}

	/**
	 * Get the DGIWG file name
	 * 
	 * @return DGIWG file name
	 */
	public GeoPackageFileName getFileName() {
		return fileName;
	}

}
