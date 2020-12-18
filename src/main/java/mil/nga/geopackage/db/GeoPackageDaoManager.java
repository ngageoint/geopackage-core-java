package mil.nga.geopackage.db;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;

/**
 * GeoPackage DAO Manager for cleaning up ORMLite caches
 * 
 * @author osbornb
 * @since 3.1.0
 */
public class GeoPackageDaoManager {

	/**
	 * Unregister all GeoPackage DAO with the connection source
	 * 
	 * @param connectionSource
	 *            connection source
	 */
	public static void unregisterDaos(ConnectionSource connectionSource) {
		DaoManager.unregisterDaos(connectionSource);
	}

	/**
	 * Unregister the provided DAO class types with the connection source
	 * 
	 * @param connectionSource
	 *            connection source
	 * @param classes
	 *            DAO class types
	 */
	public static void unregisterDao(ConnectionSource connectionSource,
			Class<?>... classes) {
		for (Class<?> clazz : classes) {
			unregisterDao(connectionSource, clazz);
		}
	}

	/**
	 * Unregister the provided
	 * 
	 * @param connectionSource
	 *            connection source
	 * @param clazz
	 *            DAO class type
	 */
	public static void unregisterDao(ConnectionSource connectionSource,
			Class<?> clazz) {

		Dao<?, ?> dao = DaoManager.lookupDao(connectionSource, clazz);
		if (dao != null) {
			DaoManager.unregisterDao(connectionSource, dao);
		}

	}

}
