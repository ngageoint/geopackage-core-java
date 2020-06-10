package mil.nga.geopackage.db;

import java.sql.SQLException;
import java.util.List;

import mil.nga.geopackage.GeoPackageCore;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.io.ResourceIOUtils;
import mil.nga.geopackage.property.GeoPackageProperties;
import mil.nga.geopackage.property.PropertyConstants;
import mil.nga.geopackage.srs.SpatialReferenceSystemDao;
import mil.nga.geopackage.user.UserColumn;
import mil.nga.geopackage.user.UserTable;

/**
 * Executes database scripts to create GeoPackage tables
 *
 * @author osbornb
 */
public class GeoPackageTableCreator {

	/**
	 * Directory property
	 * 
	 * @since 4.0.0
	 */
	public static final String DIRECTORY = "directory";

	/**
	 * Spatial Reference System property
	 *
	 * @since 4.0.0
	 */
	public static final String SPATIAL_REFERENCE_SYSTEM = "spatial_reference_system";

	/**
	 * Contents property
	 *
	 * @since 4.0.0
	 */
	public static final String CONTENTS = "contents";

	/**
	 * Geometry Columns property
	 * 
	 * @since 4.0.0
	 */
	public static final String GEOMETRY_COLUMNS = "geometry_columns";

	/**
	 * Tile Matrix Set property
	 *
	 * @since 4.0.0
	 */
	public static final String TILE_MATRIX_SET = "tile_matrix_set";

	/**
	 * Tile Matrix property
	 *
	 * @since 4.0.0
	 */
	public static final String TILE_MATRIX = "tile_matrix";

	/**
	 * Extensions property
	 *
	 * @since 4.0.0
	 */
	public static final String EXTENSIONS = "extensions";

	/**
	 * Schema (Data Columns) path property
	 *
	 * @since 4.0.0
	 */
	public static final String SCHEMA_PATH = "schema";

	/**
	 * Data Columns property
	 *
	 * @since 4.0.0
	 */
	public static final String DATA_COLUMNS = "data_columns";

	/**
	 * Data Column Constraints property
	 *
	 * @since 4.0.0
	 */
	public static final String DATA_COLUMN_CONSTRAINTS = "data_column_constraints";

	/**
	 * Metadata path property
	 *
	 * @since 4.0.0
	 */
	public static final String METADATA_PATH = "metadata";

	/**
	 * Metadata property
	 *
	 * @since 4.0.0
	 */
	public static final String METADATA = "metadata";

	/**
	 * Metadata Reference property
	 *
	 * @since 4.0.0
	 */
	public static final String METADATA_REFERENCE = "metadata_reference";

	/**
	 * Tiled Gridded Coverage Data path property
	 *
	 * @since 4.0.0
	 */
	public static final String GRIDDED_PATH = "gridded";

	/**
	 * Tiled Gridded Coverage Data Coverage extension property
	 *
	 * @since 4.0.0
	 */
	public static final String GRIDDED_COVERAGE = "2d_gridded_coverage";

	/**
	 * Tiled Gridded Coverage Data Tile extension property
	 *
	 * @since 4.0.0
	 */
	public static final String GRIDDED_TILE = "2d_gridded_tile";

	/**
	 * Extended Relations property
	 *
	 * @since 4.0.0
	 */
	public static final String RELATED_PATH = "related";

	/**
	 * Extended Relations property
	 *
	 * @since 4.0.0
	 */
	public static final String EXTENDED_RELATIONS = "extended_relations";

	/**
	 * RTree property
	 *
	 * @since 4.0.0
	 */
	public static final String RTREE = "rtree";

	/**
	 * Get the database script name for the property
	 *
	 * @param property
	 *            property name
	 * @return script name
	 * @since 3.3.0
	 */
	public static String getScript(String property) {
		return GeoPackageProperties.getProperty(PropertyConstants.SQL,
				property);
	}

	/**
	 * Read the SQL Script and parse the statements for the property
	 *
	 * @param property
	 *            property name
	 * @return statements
	 * @since 4.0.0
	 */
	public static List<String> readScript(String property) {
		return readSQLScript(getScript(property));
	}

	/**
	 * Read the SQL Script and parse the statements for the property
	 *
	 * @param pathProperty
	 *            path property
	 * @param property
	 *            property name
	 * @return statements
	 * @since 4.0.0
	 */
	public static List<String> readScript(String pathProperty,
			String property) {
		return readSQLScript(pathProperty, getScript(property));
	}

	/**
	 * Read the SQL Script and parse the statements
	 *
	 * @param sqlScript
	 *            SQL script property file name
	 * @return statements
	 * @since 3.3.0
	 */
	public static List<String> readSQLScript(String sqlScript) {
		return readSQLScript(null, sqlScript);
	}

	/**
	 * Read the SQL Script and parse the statements
	 *
	 * @param property
	 *            path property
	 * @param sqlScript
	 *            SQL script property file name
	 * @return statements
	 * @since 4.0.0
	 */
	public static List<String> readSQLScript(String property,
			String sqlScript) {
		String base = PropertyConstants.SQL;
		if (property != null) {
			base = GeoPackageProperties.buildProperty(base, property);
		}
		String path = GeoPackageProperties.getProperty(base, DIRECTORY);
		List<String> statements = ResourceIOUtils.parseSQLStatements(path,
				sqlScript);
		return statements;
	}

	/**
	 * SQLite database
	 */
	private final GeoPackageCoreConnection db;

	/**
	 * Constructor
	 *
	 * @param db
	 *            db connection
	 */
	public GeoPackageTableCreator(GeoPackageCoreConnection db) {
		this.db = db;
	}

	/**
	 * Constructor
	 *
	 * @param geoPackage
	 *            GeoPackage
	 * @since 4.0.0
	 */
	public GeoPackageTableCreator(GeoPackageCore geoPackage) {
		this(geoPackage.getDatabase());
	}

	/**
	 * Create Spatial Reference System table and views
	 *
	 * @return executed statements
	 */
	public int createSpatialReferenceSystem() {
		return execScript(SPATIAL_REFERENCE_SYSTEM);
	}

	/**
	 * Create Contents table
	 *
	 * @return executed statements
	 */
	public int createContents() {
		return execScript(CONTENTS);
	}

	/**
	 * Create Geometry Columns table
	 *
	 * @return executed statements
	 */
	public int createGeometryColumns() {
		return execScript(GEOMETRY_COLUMNS);
	}

	/**
	 * Create Tile Matrix Set table
	 *
	 * @return executed statements
	 */
	public int createTileMatrixSet() {
		return execScript(TILE_MATRIX_SET);
	}

	/**
	 * Create Tile Matrix table
	 *
	 * @return executed statements
	 */
	public int createTileMatrix() {
		return execScript(TILE_MATRIX);
	}

	/**
	 * Create Extensions table
	 *
	 * @return executed statements
	 */
	public int createExtensions() {
		return execScript(EXTENSIONS);
	}

	/**
	 * Create Data Columns table
	 *
	 * @return executed statements
	 */
	public int createDataColumns() {
		return execScript(SCHEMA_PATH, DATA_COLUMNS);
	}

	/**
	 * Create Data Column Constraints table
	 *
	 * @return executed statements
	 */
	public int createDataColumnConstraints() {
		return execScript(SCHEMA_PATH, DATA_COLUMN_CONSTRAINTS);
	}

	/**
	 * Create Metadata table
	 *
	 * @return executed statements
	 */
	public int createMetadata() {
		return execScript(METADATA_PATH, METADATA);
	}

	/**
	 * Create Metadata Reference table
	 *
	 * @return executed statements
	 */
	public int createMetadataReference() {
		return execScript(METADATA_PATH, METADATA_REFERENCE);
	}

	/**
	 * Create the Tiled Gridded Coverage Data Coverage extension table
	 *
	 * @return executed statements
	 * @since 1.2.1
	 */
	public int createGriddedCoverage() {
		return execScript(GRIDDED_PATH, GRIDDED_COVERAGE);
	}

	/**
	 * Create the Tiled Gridded Coverage Data Tile extension table
	 *
	 * @return executed statements
	 * @since 1.2.1
	 */
	public int createGriddedTile() {
		return execScript(GRIDDED_PATH, GRIDDED_TILE);
	}

	/**
	 * Create the Extended Relations table
	 *
	 * @return executed statements
	 * @since 3.0.1
	 */
	public int createExtendedRelations() {
		return execScript(RELATED_PATH, EXTENDED_RELATIONS);
	}

	/**
	 * Execute the database script name
	 *
	 * @return executed statements
	 * @since 4.0.0
	 */
	public int execScript() {
		return execScript(null);
	}

	/**
	 * Execute the database script name for the property
	 *
	 * @param property
	 *            property name
	 * @return executed statements
	 * @since 4.0.0
	 */
	public int execScript(String property) {
		String propertyPath = getProperty();
		if (propertyPath == null) {
			propertyPath = property;
		} else if (property != null) {
			propertyPath = GeoPackageProperties.buildProperty(propertyPath,
					property);
		}
		String sqlScript = getScript(propertyPath);
		return execSQLScript(sqlScript);
	}

	/**
	 * Execute the database script name for the property
	 *
	 * @param propertyPath
	 *            table creator property path
	 * @param property
	 *            property name
	 * @return executed statements
	 * @since 4.0.0
	 */
	public int execScript(String propertyPath, String property) {
		String sqlScript = getScript(property);
		return execSQLScript(propertyPath, sqlScript);
	}

	/**
	 * Execute the SQL Script
	 *
	 * @param sqlScript
	 *            SQL script property file name
	 * @return executed statements
	 * @since 3.3.0
	 */
	public int execSQLScript(String sqlScript) {
		return execSQLScript(getProperty(), sqlScript);
	}

	/**
	 * Execute the SQL Script
	 *
	 * @param property
	 *            table creator property path
	 * @param sqlScript
	 *            SQL script property file name
	 * @return executed statements
	 * @since 4.0.0
	 */
	public int execSQLScript(String property, String sqlScript) {
		List<String> statements = readSQLScript(property, sqlScript);

		for (String statement : statements) {
			db.execSQL(statement);
		}

		return statements.size();
	}

	/**
	 * Get the table creator property path
	 * 
	 * @return property path or null
	 * @since 4.0.0
	 */
	public String getProperty() {
		String property = getAuthor();
		String name = getName();
		if (name != null) {
			if (property == null) {
				property = name;
			} else {
				property = GeoPackageProperties.buildProperty(property, name);
			}
		}
		return property;
	}

	/**
	 * Get the table creator author
	 * 
	 * @return author or null
	 * @since 4.0.0
	 */
	public String getAuthor() {
		return null;
	}

	/**
	 * Get the table creator name
	 * 
	 * @return name or null
	 * @since 4.0.0
	 */
	public String getName() {
		return null;
	}

	/**
	 * Create the user defined table
	 *
	 * @param table
	 *            user table
	 * @param <TColumn>
	 *            column type
	 */
	public <TColumn extends UserColumn> void createTable(
			UserTable<TColumn> table) {

		// Verify the table does not already exist
		if (db.tableOrViewExists(table.getTableName())) {
			throw new GeoPackageException(
					"Table or view already exists and can not be created: "
							+ table.getTableName());
		}

		// Build the create table sql
		String sql = CoreSQLUtils.createTableSQL(table);

		// Create the table
		db.execSQL(sql);
	}

	/**
	 * Create the minimum required GeoPackage tables
	 */
	public void createRequired() {

		// Create the Spatial Reference System table (spec Requirement 10)
		createSpatialReferenceSystem();

		// Create the Contents table (spec Requirement 13)
		createContents();

		SpatialReferenceSystemDao dao = SpatialReferenceSystemDao.create(db);
		// Create the required Spatial Reference Systems (spec Requirement
		// 11)
		try {
			dao.createWgs84();
			dao.createUndefinedCartesian();
			dao.createUndefinedGeographic();
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Error creating default required Spatial Reference Systems",
					e);
		}
	}

	/**
	 * Drop the table if it exists
	 *
	 * @param table
	 *            table name
	 * @since 1.1.5
	 */
	public void dropTable(String table) {
		CoreSQLUtils.dropTable(db, table);
	}

	/**
	 * Drop the view if it exists
	 *
	 * @param view
	 *            view name
	 * @since 4.0.0
	 */
	public void dropView(String view) {
		CoreSQLUtils.dropView(db, view);
	}

}
