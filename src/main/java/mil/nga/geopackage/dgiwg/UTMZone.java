package mil.nga.geopackage.dgiwg;

import mil.nga.geopackage.GeoPackageException;

/**
 * UTM Zone utilities
 * 
 * @author osbornb
 * @since 6.6.0
 */
public class UTMZone {

	/**
	 * North min EPSG code
	 */
	public static final long NORTH_MIN = 32601;

	/**
	 * North max EPSG code
	 */
	public static final long NORTH_MAX = 32660;

	/**
	 * South min EPSG code
	 */
	public static final long SOUTH_MIN = 32701;

	/**
	 * South max EPSG code
	 */
	public static final long SOUTH_MAX = 32760;

	/**
	 * Get the UTM Zone
	 * 
	 * @param epsg
	 *            UTM Zone EPSG ({@link #NORTH_MIN} - {@link #NORTH_MAX} or
	 *            {@link #SOUTH_MIN} - {@link #SOUTH_MAX})
	 * @return UTM Zone
	 */
	public static long getZone(long epsg) {
		if (!isZone(epsg)) {
			invalidUTMZone(epsg);
		}
		return epsg % 100;
	}

	/**
	 * Get the UTM Zone Latitude Direction
	 * 
	 * @param epsg
	 *            UTM Zone EPSG ({@link #NORTH_MIN} - {@link #NORTH_MAX} or
	 *            {@link #SOUTH_MIN} - {@link #SOUTH_MAX})
	 * @return latitude direction
	 */
	public static String getLatDirection(long epsg) {
		String direction = null;
		if (epsg >= NORTH_MIN && epsg <= NORTH_MAX) {
			direction = "N";
		} else if (epsg >= SOUTH_MIN && epsg <= SOUTH_MAX) {
			direction = "S";
		} else {
			invalidUTMZone(epsg);
		}
		return direction;
	}

	/**
	 * Get the central meridian
	 * 
	 * @param zone
	 *            UTM zone
	 * @return central meridian
	 */
	public static long getCentralMeridian(long zone) {
		if (zone < 1 || zone > 60) {
			throw new GeoPackageException(
					"Invalid UTM Zone: " + zone + ", Expected 1 - 60");
		}
		return -177 + ((zone - 1) * 6);
	}

	/**
	 * Get the UTM Zone False Northing
	 * 
	 * @param epsg
	 *            UTM Zone EPSG ({@link #NORTH_MIN} - {@link #NORTH_MAX} or
	 *            {@link #SOUTH_MIN} - {@link #SOUTH_MAX})
	 * @return false northing
	 */
	public static long getFalseNorthing(long epsg) {
		long falseNorthing = 0;
		if (epsg >= NORTH_MIN && epsg <= NORTH_MAX) {
			falseNorthing = 0;
		} else if (epsg >= SOUTH_MIN && epsg <= SOUTH_MAX) {
			falseNorthing = 10000000;
		} else {
			invalidUTMZone(epsg);
		}
		return falseNorthing;
	}

	/**
	 * Is the EPSG a UTM Zone
	 * 
	 * @param epsg
	 *            EPSG
	 * @return true if UTM zone
	 */
	public static boolean isZone(long epsg) {
		return isNorth(epsg) || isSouth(epsg);
	}

	/**
	 * Is the EPSG a UTM North Zone
	 * 
	 * @param epsg
	 *            EPSG
	 * @return true if UTM north zone
	 */
	public static boolean isNorth(long epsg) {
		return epsg >= NORTH_MIN && epsg <= NORTH_MAX;
	}

	/**
	 * Is the EPSG a UTM South Zone
	 * 
	 * @param epsg
	 *            EPSG
	 * @return true if UTM south zone
	 */
	public static boolean isSouth(long epsg) {
		return epsg >= SOUTH_MIN && epsg <= SOUTH_MAX;
	}

	/**
	 * Throw an invalid UTM Zone error
	 * 
	 * @param epsg
	 *            EPSG
	 */
	private static void invalidUTMZone(long epsg) {
		throw new GeoPackageException("Invalid UTM Zone EPSG: " + epsg
				+ ", Expected " + NORTH_MIN + " - " + NORTH_MAX + " or "
				+ SOUTH_MIN + " - " + SOUTH_MAX);
	}

}
