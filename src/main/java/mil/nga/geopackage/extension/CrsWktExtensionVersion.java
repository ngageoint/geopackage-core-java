package mil.nga.geopackage.extension;

/**
 * OGC Well known text representation of Coordinate Reference Systems extension
 * version enumeration
 * 
 * @author osbornb
 * @since 6.5.1
 */
public enum CrsWktExtensionVersion {

	/**
	 * 1
	 */
	V_1(),

	/**
	 * 1.1
	 */
	V_1_1("1.1");

	/**
	 * Version
	 */
	private final String version;

	/**
	 * Extension name suffix
	 */
	private final String suffix;

	/**
	 * Constructor
	 * 
	 * @param version
	 *            version
	 */
	private CrsWktExtensionVersion() {
		this(null);
	}

	/**
	 * Constructor
	 * 
	 * @param version
	 *            version
	 */
	private CrsWktExtensionVersion(String version) {
		if (version == null) {
			this.version = "1";
			this.suffix = "";
		} else {
			this.version = version;
			this.suffix = "_" + version.replaceAll(".", "_");
		}
	}

	/**
	 * Get the version
	 * 
	 * @return version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * Get the extension name suffix
	 * 
	 * @return extension name suffix
	 */
	public String getSuffix() {
		return suffix;
	}

}
