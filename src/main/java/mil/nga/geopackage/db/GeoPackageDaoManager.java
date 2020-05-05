package mil.nga.geopackage.db;

import mil.nga.geopackage.core.contents.Contents;
import mil.nga.geopackage.core.srs.SpatialReferenceSystem;
import mil.nga.geopackage.core.srs.SpatialReferenceSystemSfSql;
import mil.nga.geopackage.core.srs.SpatialReferenceSystemSqlMm;
import mil.nga.geopackage.extension.Extensions;
import mil.nga.geopackage.extension.coverage.GriddedCoverage;
import mil.nga.geopackage.extension.coverage.GriddedTile;
import mil.nga.geopackage.extension.nga.contents.ContentsId;
import mil.nga.geopackage.extension.nga.index.GeometryIndex;
import mil.nga.geopackage.extension.nga.index.TableIndex;
import mil.nga.geopackage.extension.nga.link.FeatureTileLink;
import mil.nga.geopackage.extension.nga.scale.TileScaling;
import mil.nga.geopackage.extension.related.ExtendedRelation;
import mil.nga.geopackage.features.columns.GeometryColumns;
import mil.nga.geopackage.features.columns.GeometryColumnsSfSql;
import mil.nga.geopackage.features.columns.GeometryColumnsSqlMm;
import mil.nga.geopackage.metadata.Metadata;
import mil.nga.geopackage.metadata.reference.MetadataReference;
import mil.nga.geopackage.schema.columns.DataColumns;
import mil.nga.geopackage.schema.constraints.DataColumnConstraints;
import mil.nga.geopackage.tiles.matrix.TileMatrix;
import mil.nga.geopackage.tiles.matrixset.TileMatrixSet;

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
		// TODO when ormlite-core version > 5.1 is released, replace with:
		// "DaoManager.unregisterDaos(connectionSource);"
		// See https://github.com/j256/ormlite-core/pull/149
		unregisterDao(connectionSource, Contents.class,
				SpatialReferenceSystem.class,
				SpatialReferenceSystemSfSql.class,
				SpatialReferenceSystemSqlMm.class, Extensions.class,
				GriddedCoverage.class, GriddedTile.class, GeometryIndex.class,
				TableIndex.class, FeatureTileLink.class,
				ExtendedRelation.class, TileScaling.class,
				GeometryColumns.class, GeometryColumnsSfSql.class,
				GeometryColumnsSqlMm.class, Metadata.class,
				MetadataReference.class, DataColumns.class,
				DataColumnConstraints.class, TileMatrix.class,
				TileMatrixSet.class, ContentsId.class);
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
