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
		setFile(file);
		setFileName(fileName);
	}

	/**
	 * Constructor
	 * 
	 * @param path
	 *            GeoPackage path
	 */
	public GeoPackageFile(String path) {
		setFile(path);
		setFileName(path);
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
		setFile(path);
		setFileName(fileName);
	}

	/**
	 * Constructor
	 * 
	 * @param path
	 *            GeoPackage path
	 * @param fileName
	 *            file name
	 */
	public GeoPackageFile(String path, String fileName) {
		setFile(path);
		setFileName(fileName);
	}

	/**
	 * Constructor
	 * 
	 * @param file
	 *            GeoPackage file
	 * @param fileName
	 *            file name
	 */
	public GeoPackageFile(File file, String fileName) {
		setFile(file);
		setFileName(fileName);
	}

	/**
	 * Constructor
	 * 
	 * @param file
	 *            GeoPackage file
	 */
	public GeoPackageFile(File file) {
		setFile(file);
		setFileName(file);
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
	 * Set the GeoPackage file
	 * 
	 * @param file
	 *            GeoPackage file
	 */
	public void setFile(File file) {
		this.file = file;
	}

	/**
	 * Set the GeoPackage file by path
	 * 
	 * @param path
	 *            GeoPackage file path
	 */
	public void setFile(String path) {
		this.file = path != null ? new File(path) : null;
	}

	/**
	 * Get the DGIWG file name
	 * 
	 * @return DGIWG file name
	 */
	public GeoPackageFileName getFileName() {
		return fileName;
	}

	/**
	 * Get the file name
	 * 
	 * @return file name
	 */
	public String getName() {
		return fileName != null ? fileName.getName() : null;
	}

	/**
	 * Set the DGIWG file name
	 * 
	 * @param fileName
	 *            DGIWG file name
	 */
	public void setFileName(GeoPackageFileName fileName) {
		this.fileName = fileName;
	}

	/**
	 * Set the DGIWG file name
	 * 
	 * @param file
	 *            GeoPackage file
	 */
	public void setFileName(File file) {
		this.fileName = file != null ? new GeoPackageFileName(file) : null;
	}

	/**
	 * Set the DGIWG file name
	 * 
	 * @param path
	 *            GeoPackage file path
	 */
	public void setFileName(String path) {
		this.fileName = path != null ? new GeoPackageFileName(path) : null;
	}

}
