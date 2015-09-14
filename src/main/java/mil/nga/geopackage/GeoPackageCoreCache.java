package mil.nga.geopackage;

import java.util.HashMap;
import java.util.Map;

/**
 * Abstract GeoPackage Core Cache for maintaining and reusing open GeoPackage
 * connections
 * 
 * @author osbornb
 *
 * @param <T>
 *            templated GeoPackage object
 */
public abstract class GeoPackageCoreCache<T extends GeoPackageCore> {

	/**
	 * Cache of GeoPackage names and GeoPackages
	 */
	private Map<String, T> cache = new HashMap<String, T>();

	/**
	 * Constructor
	 */
	public GeoPackageCoreCache() {

	}

	/**
	 * Get the GeoPackage with name
	 * 
	 * @param name
	 * @return cached GeoPackage
	 */
	public T get(String name) {
		return cache.get(name);
	}

	/**
	 * Checks if the GeoPackage name exists in the cache
	 * 
	 * @param name
	 * @return true if exists
	 */
	public boolean exists(String name) {
		return cache.containsKey(name);
	}

	/**
	 * Close all GeoPackages in the cache
	 */
	public void closeAll() {
		for (T geoPackage : cache.values()) {
			geoPackage.close();
		}
		cache.clear();
	}

	/**
	 * Add a GeoPackage to the cache
	 * 
	 * @param geoPackage
	 */
	public void add(T geoPackage) {
		cache.put(geoPackage.getName(), geoPackage);
	}

	/**
	 * Remove the GeoPackage with the name
	 * 
	 * @param name
	 * @return removed GeoPackage
	 */
	public T remove(String name) {
		return cache.remove(name);
	}

	/**
	 * Remove and close the GeoPackage with name
	 * 
	 * @param name
	 * @return true if found, removed, and closed
	 */
	public boolean removeAndClose(String name) {
		T geoPackage = remove(name);
		if (geoPackage != null) {
			geoPackage.close();
		}
		return geoPackage != null;
	}

}
