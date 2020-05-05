package mil.nga.geopackage.extension.nga.index;

import mil.nga.geopackage.GeoPackageCore;
import mil.nga.geopackage.db.GeoPackageCoreConnection;
import mil.nga.geopackage.db.GeoPackageTableCreator;

/**
 * Geometry Index Extension Table Creator
 * 
 * @author osbornb
 * @since 4.0.0
 */
public class GeometryIndexTableCreator extends GeoPackageTableCreator {

	/**
	 * Table Index property
	 */
	public static final String TABLE_INDEX = "table";

	/**
	 * Geometry Index property
	 */
	public static final String GEOMETRY_INDEX = "geometry";

	/**
	 * Index Geometry Index property
	 */
	public static final String INDEX_GEOMETRY_INDEX = "index";

	/**
	 * Unindex Geometry Index property
	 */
	public static final String UNINDEX_GEOMETRY_INDEX = "unindex";

	/**
	 * Constructor
	 *
	 * @param db
	 *            db connection
	 */
	public GeometryIndexTableCreator(GeoPackageCoreConnection db) {
		super(db);
	}

	/**
	 * Constructor
	 *
	 * @param geoPackage
	 *            GeoPackage
	 */
	public GeometryIndexTableCreator(GeoPackageCore geoPackage) {
		super(geoPackage);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getAuthor() {
		return FeatureTableCoreIndex.EXTENSION_AUTHOR;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return FeatureTableCoreIndex.EXTENSION_NAME_NO_AUTHOR;
	}

	/**
	 * Create Table Index table
	 *
	 * @return executed statements
	 */
	public int createTableIndex() {
		return execScript(TABLE_INDEX);
	}

	/**
	 * Create Geometry Index table
	 *
	 * @return executed statements
	 */
	public int createGeometryIndex() {
		return execScript(GEOMETRY_INDEX) + indexGeometryIndex();
	}

	/**
	 * Create Geometry Index table column indexes
	 *
	 * @return executed statements
	 */
	public int indexGeometryIndex() {
		return execScript(INDEX_GEOMETRY_INDEX);
	}

	/**
	 * Un-index (drop) Geometry Index table column indexes
	 *
	 * @return executed statements
	 */
	public int unindexGeometryIndex() {
		return execScript(UNINDEX_GEOMETRY_INDEX);
	}

}
