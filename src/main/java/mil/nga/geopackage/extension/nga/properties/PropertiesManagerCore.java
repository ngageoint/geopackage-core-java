package mil.nga.geopackage.extension.nga.properties;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import mil.nga.geopackage.GeoPackageCore;
import mil.nga.geopackage.GeoPackageCoreCache;

/**
 * Properties Manager Core using the Properties Extension on a group of cached
 * GeoPackages
 * 
 * @author osbornb
 *
 * @param <T>
 *            templated GeoPackage object
 * @since 3.0.2
 */
public abstract class PropertiesManagerCore<T extends GeoPackageCore> {

	/**
	 * GeoPackage name to properties extension map
	 */
	private final Map<String, PropertiesCoreExtension<T, ?, ?, ?>> propertiesMap = new HashMap<>();

	/**
	 * Constructor
	 */
	protected PropertiesManagerCore() {

	}

	/**
	 * Constructor
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 */
	protected PropertiesManagerCore(T geoPackage) {
		addGeoPackage(geoPackage);
	}

	/**
	 * Constructor
	 * 
	 * @param geoPackages
	 *            collection of GeoPackages
	 */
	protected PropertiesManagerCore(Collection<T> geoPackages) {
		addGeoPackages(geoPackages);
	}

	/**
	 * Constructor
	 * 
	 * @param cache
	 *            GeoPackage cache
	 */
	protected PropertiesManagerCore(GeoPackageCoreCache<T> cache) {
		this(cache.getGeoPackages());
	}

	/**
	 * Create a properties extension from the GeoPackage
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * @return properties extension
	 */
	protected abstract PropertiesCoreExtension<T, ?, ?, ?> getPropertiesExtension(
			T geoPackage);

	/**
	 * Get the GeoPackage names
	 * 
	 * @return names
	 */
	public Set<String> getGeoPackageNames() {
		return propertiesMap.keySet();
	}

	/**
	 * Get the number of GeoPackages
	 * 
	 * @return GeoPackage count
	 */
	public int numGeoPackages() {
		return propertiesMap.size();
	}

	/**
	 * Get the GeoPackages
	 * 
	 * @return collection of GeoPackages
	 */
	public List<T> getGeoPackages() {
		List<T> geoPackages = new ArrayList<>();
		for (PropertiesCoreExtension<T, ?, ?, ?> properties : propertiesMap
				.values()) {
			geoPackages.add(properties.getGeoPackage());
		}
		return geoPackages;
	}

	/**
	 * Checks if the GeoPackage name exists
	 * 
	 * @param name
	 *            GeoPackage name
	 * @return true if exists
	 */
	public boolean hasGeoPackage(String name) {
		return propertiesMap.containsKey(name);
	}

	/**
	 * Get the GeoPackage for the GeoPackage name
	 * 
	 * @param name
	 *            GeoPackage name
	 * @return GeoPackage
	 */
	public T getGeoPackage(String name) {
		T geoPackage = null;
		PropertiesCoreExtension<T, ?, ?, ?> properties = propertiesMap
				.get(name);
		if (properties != null) {
			geoPackage = properties.getGeoPackage();
		}
		return geoPackage;
	}

	/**
	 * Add a collection of GeoPackages
	 * 
	 * @param geoPackages
	 *            GeoPackages
	 */
	public void addGeoPackages(Collection<T> geoPackages) {
		for (T geoPackage : geoPackages) {
			addGeoPackage(geoPackage);
		}
	}

	/**
	 * Add GeoPackage
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 */
	public void addGeoPackage(T geoPackage) {
		PropertiesCoreExtension<T, ?, ?, ?> propertiesExtension = getPropertiesExtension(geoPackage);
		propertiesMap.put(geoPackage.getName(), propertiesExtension);
	}

	/**
	 * Close all GeoPackages in the manager
	 */
	public void closeGeoPackages() {
		for (PropertiesCoreExtension<T, ?, ?, ?> properties : propertiesMap
				.values()) {
			properties.getGeoPackage().close();
		}
		propertiesMap.clear();
	}

	/**
	 * Remove the GeoPackage with the name but does not close it
	 * 
	 * @param name
	 *            GeoPackage name
	 * @return removed GeoPackage
	 */
	public T removeGeoPackage(String name) {
		T removed = null;
		PropertiesCoreExtension<T, ?, ?, ?> properties = propertiesMap
				.remove(name);
		if (properties != null) {
			removed = properties.getGeoPackage();
		}
		return removed;
	}

	/**
	 * Clears all cached GeoPackages but does not close them
	 */
	public void clearGeoPackages() {
		propertiesMap.clear();
	}

	/**
	 * Remove and close the GeoPackage with name, same as
	 * {@link #closeGeoPackage(String)}
	 * 
	 * @param name
	 *            GeoPackage name
	 * @return true if found, removed, and closed
	 */
	public boolean removeAndCloseGeoPackage(String name) {
		return closeGeoPackage(name);
	}

	/**
	 * Close the GeoPackage with name
	 * 
	 * @param name
	 *            GeoPackage name
	 * @return true if found and closed
	 */
	public boolean closeGeoPackage(String name) {
		T geoPackage = removeGeoPackage(name);
		if (geoPackage != null) {
			geoPackage.close();
		}
		return geoPackage != null;
	}

	/**
	 * Close GeoPackages not specified in the retain GeoPackage names
	 * 
	 * @param retain
	 *            GeoPackages to retain
	 */
	public void closeRetainGeoPackages(Collection<String> retain) {
		Set<String> close = new HashSet<>(propertiesMap.keySet());
		close.removeAll(retain);
		for (String name : close) {
			closeGeoPackage(name);
		}
	}

	/**
	 * Close GeoPackages with names
	 * 
	 * @param names
	 *            GeoPackage names
	 */
	public void closeGeoPackages(Collection<String> names) {
		for (String name : names) {
			closeGeoPackage(name);
		}
	}

	/**
	 * Get the number of unique properties
	 * 
	 * @return property count
	 */
	public int numProperties() {
		return getProperties().size();
	}

	/**
	 * Get the unique properties
	 * 
	 * @return set of properties
	 */
	public Set<String> getProperties() {
		Set<String> allProperties = new HashSet<>();
		for (PropertiesCoreExtension<T, ?, ?, ?> properties : propertiesMap
				.values()) {
			allProperties.addAll(properties.getProperties());
		}
		return allProperties;
	}

	/**
	 * Get the GeoPackages with the property name
	 * 
	 * @param property
	 *            property name
	 * @return GeoPackages
	 */
	public List<T> hasProperty(String property) {
		List<T> geoPackages = new ArrayList<>();
		for (PropertiesCoreExtension<T, ?, ?, ?> properties : propertiesMap
				.values()) {
			if (properties.hasProperty(property)) {
				geoPackages.add(properties.getGeoPackage());
			}
		}
		return geoPackages;
	}

	/**
	 * Get the GeoPackages missing the property name
	 * 
	 * @param property
	 *            property name
	 * @return GeoPackages
	 */
	public List<T> missingProperty(String property) {
		List<T> geoPackages = new ArrayList<>();
		for (PropertiesCoreExtension<T, ?, ?, ?> properties : propertiesMap
				.values()) {
			if (!properties.hasProperty(property)) {
				geoPackages.add(properties.getGeoPackage());
			}
		}
		return geoPackages;
	}

	/**
	 * Get the number of unique values for the property
	 * 
	 * @param property
	 *            property name
	 * @return number of values
	 */
	public int numValues(String property) {
		return getValues(property).size();
	}

	/**
	 * Check if the property has any values
	 * 
	 * @param property
	 *            property name
	 * @return true if has any values
	 */
	public boolean hasValues(String property) {
		return numValues(property) > 0;
	}

	/**
	 * Get the unique values for the property
	 * 
	 * @param property
	 *            property name
	 * @return set of values
	 */
	public Set<String> getValues(String property) {
		Set<String> allValues = new HashSet<>();
		for (PropertiesCoreExtension<T, ?, ?, ?> properties : propertiesMap
				.values()) {
			allValues.addAll(properties.getValues(property));
		}
		return allValues;
	}

	/**
	 * Get the GeoPackages with the property name and value
	 * 
	 * @param property
	 *            property name
	 * @param value
	 *            property value
	 * @return GeoPackages
	 */
	public List<T> hasValue(String property, String value) {
		List<T> geoPackages = new ArrayList<>();
		for (PropertiesCoreExtension<T, ?, ?, ?> properties : propertiesMap
				.values()) {
			if (properties.hasValue(property, value)) {
				geoPackages.add(properties.getGeoPackage());
			}
		}
		return geoPackages;
	}

	/**
	 * Get the GeoPackages missing the property name and value
	 * 
	 * @param property
	 *            property name
	 * @param value
	 *            property value
	 * @return GeoPackages
	 */
	public List<T> missingValue(String property, String value) {
		List<T> geoPackages = new ArrayList<>();
		for (PropertiesCoreExtension<T, ?, ?, ?> properties : propertiesMap
				.values()) {
			if (!properties.hasValue(property, value)) {
				geoPackages.add(properties.getGeoPackage());
			}
		}
		return geoPackages;
	}

	/**
	 * Add a property value to all GeoPackages
	 * 
	 * @param property
	 *            property name
	 * @param value
	 *            value
	 * @return number of GeoPackages added to
	 */
	public int addValue(String property, String value) {
		int count = 0;
		for (String geoPackage : propertiesMap.keySet()) {
			if (addValue(geoPackage, property, value)) {
				count++;
			}
		}
		return count;
	}

	/**
	 * Add a property value to a specified GeoPackage
	 * 
	 * @param geoPackage
	 *            GeoPackage name
	 * @param property
	 *            property name
	 * @param value
	 *            value
	 * @return true if added
	 */
	public boolean addValue(String geoPackage, String property, String value) {
		boolean added = false;
		PropertiesCoreExtension<T, ?, ?, ?> properties = propertiesMap
				.get(geoPackage);
		if (properties != null) {
			added = properties.addValue(property, value);
		}
		return added;
	}

	/**
	 * Delete the property and values from all GeoPackages
	 * 
	 * @param property
	 *            property name
	 * @return number of GeoPackages deleted from
	 */
	public int deleteProperty(String property) {
		int count = 0;
		for (String geoPackage : propertiesMap.keySet()) {
			if (deleteProperty(geoPackage, property)) {
				count++;
			}
		}
		return count;
	}

	/**
	 * Delete the property and values from a specified GeoPackage
	 * 
	 * @param geoPackage
	 *            GeoPackage name
	 * @param property
	 *            property name
	 * @return true if deleted
	 */
	public boolean deleteProperty(String geoPackage, String property) {
		boolean deleted = false;
		PropertiesCoreExtension<T, ?, ?, ?> properties = propertiesMap
				.get(geoPackage);
		if (properties != null) {
			deleted = properties.deleteProperty(property) > 0;
		}
		return deleted;
	}

	/**
	 * Delete the property value from all GeoPackages
	 * 
	 * @param property
	 *            property name
	 * @param value
	 *            property value
	 * @return number of GeoPackages deleted from
	 */
	public int deleteValue(String property, String value) {
		int count = 0;
		for (String geoPackage : propertiesMap.keySet()) {
			if (deleteValue(geoPackage, property, value)) {
				count++;
			}
		}
		return count;
	}

	/**
	 * Delete the property value from a specified GeoPackage
	 * 
	 * @param geoPackage
	 *            GeoPackage name
	 * @param property
	 *            property name
	 * @param value
	 *            property value
	 * @return true if deleted
	 */
	public boolean deleteValue(String geoPackage, String property, String value) {
		boolean deleted = false;
		PropertiesCoreExtension<T, ?, ?, ?> properties = propertiesMap
				.get(geoPackage);
		if (properties != null) {
			deleted = properties.deleteValue(property, value) > 0;
		}
		return deleted;
	}

	/**
	 * Delete all properties and values from all GeoPackages
	 * 
	 * @return number of GeoPackages deleted from
	 */
	public int deleteAll() {
		int count = 0;
		for (String geoPackage : propertiesMap.keySet()) {
			if (deleteAll(geoPackage)) {
				count++;
			}
		}
		return count;
	}

	/**
	 * Delete all properties and values from a specified GeoPackage
	 * 
	 * @param geoPackage
	 *            GeoPackage name
	 * @return true if any deleted
	 */
	public boolean deleteAll(String geoPackage) {
		boolean deleted = false;
		PropertiesCoreExtension<T, ?, ?, ?> properties = propertiesMap
				.get(geoPackage);
		if (properties != null) {
			deleted = properties.deleteAll() > 0;
		}
		return deleted;
	}

	/**
	 * Remove the extension from all GeoPackages
	 */
	public void removeExtension() {
		for (String geoPackage : propertiesMap.keySet()) {
			removeExtension(geoPackage);
		}
	}

	/**
	 * Remove the extension from a specified GeoPackage
	 * 
	 * @param geoPackage
	 *            GeoPackage name
	 */
	public void removeExtension(String geoPackage) {
		PropertiesCoreExtension<T, ?, ?, ?> properties = propertiesMap
				.get(geoPackage);
		if (properties != null) {
			properties.removeExtension();
		}
	}

}
