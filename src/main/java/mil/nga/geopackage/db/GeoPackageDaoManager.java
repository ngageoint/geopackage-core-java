package mil.nga.geopackage.db;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;

import mil.nga.geopackage.contents.Contents;
import mil.nga.geopackage.extension.Extensions;
import mil.nga.geopackage.extension.coverage.GriddedCoverage;
import mil.nga.geopackage.extension.coverage.GriddedTile;
import mil.nga.geopackage.extension.ecere.tile_matrix_set.ExtTileMatrix;
import mil.nga.geopackage.extension.ecere.tile_matrix_set.ExtTileMatrixSet;
import mil.nga.geopackage.extension.ecere.tile_matrix_set.TileMatrixTable;
import mil.nga.geopackage.extension.ecere.tile_matrix_set.TileMatrixVariableWidths;
import mil.nga.geopackage.extension.im.portrayal.Styles;
import mil.nga.geopackage.extension.im.portrayal.Stylesheets;
import mil.nga.geopackage.extension.im.portrayal.SymbolContent;
import mil.nga.geopackage.extension.im.portrayal.SymbolImages;
import mil.nga.geopackage.extension.im.portrayal.Symbols;
import mil.nga.geopackage.extension.im.vector_tiles.VectorTilesFields;
import mil.nga.geopackage.extension.im.vector_tiles.VectorTilesLayers;
import mil.nga.geopackage.extension.metadata.Metadata;
import mil.nga.geopackage.extension.metadata.reference.MetadataReference;
import mil.nga.geopackage.extension.nga.contents.ContentsId;
import mil.nga.geopackage.extension.nga.index.GeometryIndex;
import mil.nga.geopackage.extension.nga.index.TableIndex;
import mil.nga.geopackage.extension.nga.link.FeatureTileLink;
import mil.nga.geopackage.extension.nga.scale.TileScaling;
import mil.nga.geopackage.extension.related.ExtendedRelation;
import mil.nga.geopackage.extension.schema.columns.DataColumns;
import mil.nga.geopackage.extension.schema.constraints.DataColumnConstraints;
import mil.nga.geopackage.features.columns.GeometryColumns;
import mil.nga.geopackage.features.columns.GeometryColumnsSfSql;
import mil.nga.geopackage.features.columns.GeometryColumnsSqlMm;
import mil.nga.geopackage.srs.SpatialReferenceSystem;
import mil.nga.geopackage.srs.SpatialReferenceSystemSfSql;
import mil.nga.geopackage.srs.SpatialReferenceSystemSqlMm;
import mil.nga.geopackage.tiles.matrix.TileMatrix;
import mil.nga.geopackage.tiles.matrixset.TileMatrixSet;

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
				SpatialReferenceSystem.class, SpatialReferenceSystemSfSql.class,
				SpatialReferenceSystemSqlMm.class, GeometryColumns.class,
				GeometryColumnsSfSql.class, GeometryColumnsSqlMm.class,
				TileMatrix.class, TileMatrixSet.class, Metadata.class,
				MetadataReference.class, DataColumns.class,
				DataColumnConstraints.class, Extensions.class,
				GriddedCoverage.class, GriddedTile.class,
				ExtendedRelation.class, ContentsId.class, GeometryIndex.class,
				TableIndex.class, FeatureTileLink.class, TileScaling.class,
				ExtTileMatrix.class, ExtTileMatrixSet.class,
				TileMatrixTable.class, TileMatrixVariableWidths.class,
				Styles.class, Stylesheets.class, SymbolContent.class,
				SymbolImages.class, Symbols.class, VectorTilesFields.class,
				VectorTilesLayers.class);
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
