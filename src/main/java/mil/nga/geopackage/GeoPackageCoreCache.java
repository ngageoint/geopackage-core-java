package mil.nga.geopackage;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
	 * Get the names of the cached GeoPackages
	 * 
	 * @return set of cached GeoPackage names
	 * @since 1.0.1
	 */
	public Set<String> getNames() {
		return cache.keySet();
	}

	/**
	 * Get the cached GeoPackages
	 * 
	 * @return collection of cached GeoPackages
	 * @since 1.0.1
	 */
	public Collection<T> getGeoPackages() {
		return cache.values();
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
	 * Remove the GeoPackage with the name but does not close it, call
	 * {@link #close(String)} to close and remove
	 * 
	 * @param name
	 * @return removed GeoPackage
	 */
	public T remove(String name) {
		return cache.remove(name);
	}

	/**
	 * Clears all cached GeoPackages but does not close them, call
	 * {@link #closeAll()} to close and clear all GeoPackages
	 * 
	 * @since 1.0.1
	 */
	public void clear() {
		cache.clear();
	}

	/**
	 * Remove and close the GeoPackage with name, same as {@link #close(String)}
	 * 
	 * @param name
	 * @return true if found, removed, and closed
	 */
	public boolean removeAndClose(String name) {
		return close(name);
	}

	/**
	 * Close the GeoPackage with name
	 * 
	 * @param name
	 * @return true if found and closed
	 * @since 1.0.1
	 */
	public boolean close(String name) {
		T geoPackage = remove(name);
		if (geoPackage != null) {
			geoPackage.close();
		}
		return geoPackage != null;
	}

	/**
	 * Close GeoPackages not specified in the retain GeoPackage names
	 * 
	 * @param retain
	 * @since 1.0.1
	 */
	public void closeRetain(Collection<String> retain) {
		Set<String> close = new HashSet<>(cache.keySet());
		close.removeAll(retain);
		for (String name : close) {
			close(name);
		}
	}

	/**
	 * Close GeoPackages with names
	 * 
	 * @param names
	 * @since 1.0.1
	 */
	public void close(Collection<String> names) {
		for (String name : names) {
			close(name);
		}
	}

}
