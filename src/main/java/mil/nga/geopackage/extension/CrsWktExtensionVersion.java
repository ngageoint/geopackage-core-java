package mil.nga.geopackage.extension;

import java.util.Arrays;
import java.util.List;

/**
 * OGC Well known text representation of Coordinate Reference Systems extension
 * version enumeration
 * 
 * @author osbornb
 * @since 6.5.1
 */
public enum CrsWktExtensionVersion {

	/**
	 * 1.0
	 */
	V_1("1.0"),

	/**
	 * 1.1
	 */
	V_1_1("1.1");

	/**
	 * First version
	 */
	public static CrsWktExtensionVersion FIRST = V_1;

	/**
	 * Latest supported version
	 */
	public static CrsWktExtensionVersion LATEST = V_1_1;

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
	private CrsWktExtensionVersion(String version) {
		this.version = version;
		if (version.equals("1.0")) {
			this.suffix = "";
		} else {
			this.suffix = "_" + version.replaceAll("\\.", "_");
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

	/**
	 * Is the version at or above the minimum version
	 * 
	 * @param version
	 *            extension version
	 * @return true if at or above the minimum version
	 */
	public boolean isMinimum(CrsWktExtensionVersion version) {
		return compareTo(version) >= 0;
	}

	/**
	 * Versions at and above this version
	 * 
	 * @return versions at minimum
	 */
	public List<CrsWktExtensionVersion> atMinimum() {
		List<CrsWktExtensionVersion> list = Arrays.asList(values());
		return list.subList(ordinal(), list.size());
	}

}
