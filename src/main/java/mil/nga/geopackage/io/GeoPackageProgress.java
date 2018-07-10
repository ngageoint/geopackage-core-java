package mil.nga.geopackage.io;

/**
 * GeoPackage Progress interface for receiving progress information and handling
 * cancellations
 * 
 * @author osbornb
 */
public interface GeoPackageProgress {

	/**
	 * Set the max progress value
	 * 
	 * @param max
	 *            max value
	 */
	public void setMax(int max);

	/**
	 * Add to the total progress
	 * 
	 * @param progress
	 *            progress made
	 */
	public void addProgress(int progress);

	/**
	 * Is the process still active
	 * 
	 * @return true if active, false if cancelled
	 */
	public boolean isActive();

	/**
	 * Should the progress so far be deleted when cancelled ({@link #isActive()}
	 * becomes false)
	 * 
	 * @return true to cleanup progress
	 */
	public boolean cleanupOnCancel();

}
