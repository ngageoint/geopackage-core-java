package mil.nga.geopackage;

/**
 * GeoPackage exception
 * 
 * @author osbornb
 */
public class GeoPackageException extends RuntimeException {

	/**
	 * Serial version id
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 */
	public GeoPackageException() {
		super();
	}

	/**
	 * Constructor
	 * 
	 * @param message
	 *            exception message
	 */
	public GeoPackageException(String message) {
		super(message);
	}

	/**
	 * Constructor
	 * 
	 * @param message
	 *            exception message
	 * @param throwable
	 *            cause
	 */
	public GeoPackageException(String message, Throwable throwable) {
		super(message, throwable);
	}

	/**
	 * Constructor
	 * 
	 * @param throwable
	 *            cause
	 */
	public GeoPackageException(Throwable throwable) {
		super(throwable);
	}

}
