package mil.nga.geopackage.extension;

import java.sql.SQLException;
import java.util.List;

import mil.nga.geopackage.GeoPackageConstants;
import mil.nga.geopackage.GeoPackageCore;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.db.GeoPackageCoreConnection;
import mil.nga.geopackage.features.user.FeatureTable;
import mil.nga.geopackage.io.ResourceIOUtils;
import mil.nga.geopackage.property.GeoPackageProperties;
import mil.nga.geopackage.property.PropertyConstants;

/**
 * RTree Index abstract core extension
 * 
 * @author osbornb
 * @since 2.0.1
 */
public abstract class RTreeIndexCoreExtension extends BaseExtension {

	/**
	 * Name
	 */
	public static final String NAME = "rtree_index";

	/**
	 * Min X Function name
	 */
	public static final String MIN_X_FUNCTION = "ST_MinX";

	/**
	 * Max X Function name
	 */
	public static final String MAX_X_FUNCTION = "ST_MaxX";

	/**
	 * Min Y Function name
	 */
	public static final String MIN_Y_FUNCTION = "ST_MinY";

	/**
	 * Max Y Function name
	 */
	public static final String MAX_Y_FUNCTION = "ST_MaxY";

	/**
	 * Is Empty Function name
	 */
	public static final String IS_EMPTY_FUNCTION = "ST_IsEmpty";

	/**
	 * Create SQL property
	 */
	public static final String CREATE_PROPERTY = "create";

	/**
	 * Load SQL property
	 */
	public static final String LOAD_PROPERTY = "load";

	/**
	 * Drop SQL property
	 */
	public static final String DROP_PROPERTY = "drop";

	/**
	 * Trigger Insert name
	 */
	public static final String TRIGGER_INSERT_NAME = "insert";

	/**
	 * Trigger update 1 name
	 */
	public static final String TRIGGER_UPDATE1_NAME = "update1";

	/**
	 * Trigger update 2 name
	 */
	public static final String TRIGGER_UPDATE2_NAME = "update2";

	/**
	 * Trigger update 3 name
	 */
	public static final String TRIGGER_UPDATE3_NAME = "update3";

	/**
	 * Trigger update 4 name
	 */
	public static final String TRIGGER_UPDATE4_NAME = "update4";

	/**
	 * Trigger delete name
	 */
	public static final String TRIGGER_DELETE_NAME = "delete";

	/**
	 * Trigger drop name
	 */
	public static final String TRIGGER_DROP_PROPERTY = "drop";

	/**
	 * Extension name
	 */
	public static final String EXTENSION_NAME = GeoPackageConstants.GEO_PACKAGE_EXTENSION_AUTHOR
			+ Extensions.EXTENSION_NAME_DIVIDER + NAME;

	/**
	 * Base extension property
	 */
	private static final String EXTENSION_PROPERTY = PropertyConstants.EXTENSIONS
			+ PropertyConstants.PROPERTY_DIVIDER + NAME;

	/**
	 * Extension definition URL
	 */
	public static final String DEFINITION = GeoPackageProperties
			.getProperty(EXTENSION_PROPERTY);

	/**
	 * SQL base property
	 */
	private static final String SQL_PROPERTY = EXTENSION_PROPERTY
			+ PropertyConstants.PROPERTY_DIVIDER + PropertyConstants.SQL;

	/**
	 * SQL substitute base property
	 */
	private static final String SUBSTITUTE_PROPERTY = SQL_PROPERTY
			+ PropertyConstants.PROPERTY_DIVIDER + "substitute";

	/**
	 * SQL trigger base property
	 */
	private static final String TRIGGER_PROPERTY = SQL_PROPERTY
			+ PropertyConstants.PROPERTY_DIVIDER + "trigger";

	/**
	 * Table substitute value
	 */
	public static final String TABLE_SUBSTITUTE = GeoPackageProperties
			.getProperty(SUBSTITUTE_PROPERTY, "table");

	/**
	 * Geometry Column substitute value
	 */
	public static final String GEOMETRY_COLUMN_SUBSTITUTE = GeoPackageProperties
			.getProperty(SUBSTITUTE_PROPERTY, "geometry_column");

	/**
	 * Primary Key Column substitute value
	 */
	public static final String PK_COLUMN_SUBSTITUTE = GeoPackageProperties
			.getProperty(SUBSTITUTE_PROPERTY, "pk_column");

	/**
	 * Trigger substitute value
	 */
	public static final String TRIGGER_SUBSTITUTE = GeoPackageProperties
			.getProperty(SUBSTITUTE_PROPERTY, "trigger");

	/**
	 * SQL resource directory
	 */
	public static final String SQL_DIRECTORY = GeoPackageProperties
			.getProperty(SQL_PROPERTY, "directory");

	/**
	 * Connection
	 */
	private GeoPackageCoreConnection connection = null;

	/**
	 * Constructor
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * 
	 */
	protected RTreeIndexCoreExtension(GeoPackageCore geoPackage) {
		super(geoPackage);
		connection = geoPackage.getDatabase();
	}

	/**
	 * Get or create the extension
	 * 
	 * @param featureTable
	 *            feature table
	 * @return extension
	 */
	public Extensions getOrCreate(FeatureTable featureTable) {
		return getOrCreate(featureTable.getTableName(), featureTable
				.getGeometryColumn().getName());
	}

	/**
	 * Get or create the extension
	 * 
	 * @param tableName
	 *            table name
	 * @param columnName
	 *            column name
	 * @return extension
	 */
	public Extensions getOrCreate(String tableName, String columnName) {
		return getOrCreate(EXTENSION_NAME, tableName, columnName, DEFINITION,
				ExtensionScopeType.WRITE_ONLY);
	}

	/**
	 * Determine if the GeoPackage feature table has the extension
	 * 
	 * @param featureTable
	 *            feature table
	 * @return true if has extension
	 */
	public boolean has(FeatureTable featureTable) {
		return has(featureTable.getTableName(), featureTable
				.getGeometryColumn().getName());
	}

	/**
	 * Determine if the GeoPackage table and column has the extension
	 * 
	 * @param tableName
	 *            table name
	 * @param columnName
	 *            column name
	 * @return true if has extension
	 */
	public boolean has(String tableName, String columnName) {
		return has(EXTENSION_NAME, tableName, columnName);
	}

	/**
	 * Determine if the GeoPackage has the extension for any table
	 * 
	 * @return true if has extension
	 */
	public boolean has() {
		return has(EXTENSION_NAME, null, null);
	}

	/**
	 * Check if the feature table has the RTree extension and create the
	 * functions if needed
	 * 
	 * @param featureTable
	 *            feature table
	 * @return true if has extension and functions created
	 */
	public boolean createFunctions(FeatureTable featureTable) {
		return createFunctions(featureTable.getTableName(), featureTable
				.getGeometryColumn().getName());
	}

	/**
	 * Check if the table and column has the RTree extension and create the
	 * functions if needed
	 * 
	 * @param tableName
	 *            table name
	 * @param columnName
	 *            column name
	 * @return true if has extension and functions created
	 */
	public boolean createFunctions(String tableName, String columnName) {

		boolean created = has(tableName, columnName);
		if (created) {
			createAllFunctions();
		}
		return created;
	}

	/**
	 * Check if the GeoPackage has the RTree extension and create the functions
	 * if needed
	 * 
	 * @return true if has extension and functions created
	 */
	public boolean createFunctions() {

		boolean created = has();
		if (created) {
			createAllFunctions();
		}
		return created;
	}

	/**
	 * Create the RTree Index extension for the feature table. Creates the SQL
	 * functions, loads the tree, and creates the triggers.
	 * 
	 * @param featureTable
	 *            feature table
	 * @return extension
	 */
	public Extensions create(FeatureTable featureTable) {
		return create(featureTable.getTableName(), featureTable
				.getGeometryColumn().getName(), featureTable.getPkColumn()
				.getName());
	}

	/**
	 * Create the RTree Index extension for the feature table, geometry column,
	 * and id column. Creates the SQL functions, loads the tree, and creates the
	 * triggers.
	 * 
	 * @param tableName
	 *            table name
	 * @param geometryColumnName
	 *            geometry column name
	 * @param idColumnName
	 *            id column name
	 * @return extension
	 */
	public Extensions create(String tableName, String geometryColumnName,
			String idColumnName) {

		Extensions extension = getOrCreate(tableName, geometryColumnName);

		createAllFunctions();
		createRTreeIndex(tableName, geometryColumnName);
		loadRTreeIndex(tableName, geometryColumnName, idColumnName);
		createAllTriggers(tableName, geometryColumnName, idColumnName);

		return extension;
	}

	/**
	 * Create the RTree Index Virtual Table
	 * 
	 * @param featureTable
	 *            feature table
	 */
	public void createRTreeIndex(FeatureTable featureTable) {
		createRTreeIndex(featureTable.getTableName(), featureTable
				.getGeometryColumn().getName());
	}

	/**
	 * Create the RTree Index Virtual Table
	 * 
	 * @param tableName
	 *            table name
	 * @param geometryColumnName
	 *            geometry column name
	 */
	public void createRTreeIndex(String tableName, String geometryColumnName) {

		String sqlName = GeoPackageProperties.getProperty(SQL_PROPERTY,
				CREATE_PROPERTY);
		executeSQL(sqlName, tableName, geometryColumnName);
	}

	/**
	 * Create all connection SQL Functions for min x, max x, min y, max y, and
	 * is empty
	 */
	public void createAllFunctions() {
		createMinXFunction();
		createMaxXFunction();
		createMinYFunction();
		createMaxYFunction();
		createIsEmptyFunction();
	}

	/**
	 * Create the min x SQL function
	 */
	public abstract void createMinXFunction();

	/**
	 * Create the max x SQL function
	 */
	public abstract void createMaxXFunction();

	/**
	 * Create the min y SQL function
	 */
	public abstract void createMinYFunction();

	/**
	 * Create the max y SQL function
	 */
	public abstract void createMaxYFunction();

	/**
	 * Create the is empty SQL function
	 */
	public abstract void createIsEmptyFunction();

	/**
	 * Load the RTree Spatial Index Values
	 * 
	 * @param featureTable
	 *            feature table
	 */
	public void loadRTreeIndex(FeatureTable featureTable) {
		loadRTreeIndex(featureTable.getTableName(), featureTable
				.getGeometryColumn().getName(), featureTable.getPkColumn()
				.getName());
	}

	/**
	 * Load the RTree Spatial Index Values
	 * 
	 * @param tableName
	 *            table name
	 * @param geometryColumnName
	 *            geometry column name
	 * @param idColumnName
	 *            id column name
	 */
	public void loadRTreeIndex(String tableName, String geometryColumnName,
			String idColumnName) {

		String sqlName = GeoPackageProperties.getProperty(SQL_PROPERTY,
				LOAD_PROPERTY);
		executeSQL(sqlName, tableName, geometryColumnName, idColumnName);
	}

	/**
	 * Create Triggers to Maintain Spatial Index Values
	 * 
	 * @param featureTable
	 *            feature table
	 */
	public void createAllTriggers(FeatureTable featureTable) {
		createAllTriggers(featureTable.getTableName(), featureTable
				.getGeometryColumn().getName(), featureTable.getPkColumn()
				.getName());
	}

	/**
	 * Create Triggers to Maintain Spatial Index Values
	 * 
	 * @param tableName
	 *            table name
	 * @param geometryColumnName
	 *            geometry column name
	 * @param idColumnName
	 *            id column name
	 */
	public void createAllTriggers(String tableName, String geometryColumnName,
			String idColumnName) {

		createInsertTrigger(tableName, geometryColumnName, idColumnName);
		createUpdate1Trigger(tableName, geometryColumnName, idColumnName);
		createUpdate2Trigger(tableName, geometryColumnName, idColumnName);
		createUpdate3Trigger(tableName, geometryColumnName, idColumnName);
		createUpdate4Trigger(tableName, geometryColumnName, idColumnName);
		createDeleteTrigger(tableName, geometryColumnName, idColumnName);

	}

	/**
	 * Create insert trigger
	 * 
	 * <pre>
	 * Conditions: Insertion of non-empty geometry
	 * Actions   : Insert record into rtree
	 * </pre>
	 * 
	 * @param tableName
	 *            table name
	 * @param geometryColumnName
	 *            geometry column name
	 * @param idColumnName
	 *            id column name
	 */
	public void createInsertTrigger(String tableName,
			String geometryColumnName, String idColumnName) {

		String sqlName = GeoPackageProperties.getProperty(TRIGGER_PROPERTY,
				TRIGGER_INSERT_NAME);
		executeSQL(sqlName, tableName, geometryColumnName, idColumnName);
	}

	/**
	 * Create update 1 trigger
	 * 
	 * <pre>
	 * Conditions: Update of geometry column to non-empty geometry
	 *             No row ID change
	 * Actions   : Update record in rtree
	 * </pre>
	 * 
	 * @param tableName
	 *            table name
	 * @param geometryColumnName
	 *            geometry column name
	 * @param idColumnName
	 *            id column name
	 */
	public void createUpdate1Trigger(String tableName,
			String geometryColumnName, String idColumnName) {

		String sqlName = GeoPackageProperties.getProperty(TRIGGER_PROPERTY,
				TRIGGER_UPDATE1_NAME);
		executeSQL(sqlName, tableName, geometryColumnName, idColumnName);
	}

	/**
	 * Create update 2 trigger
	 * 
	 * <pre>
	 * Conditions: Update of geometry column to empty geometry
	 *             No row ID change
	 * Actions   : Remove record from rtree
	 * </pre>
	 * 
	 * @param tableName
	 *            table name
	 * @param geometryColumnName
	 *            geometry column name
	 * @param idColumnName
	 *            id column name
	 */
	public void createUpdate2Trigger(String tableName,
			String geometryColumnName, String idColumnName) {

		String sqlName = GeoPackageProperties.getProperty(TRIGGER_PROPERTY,
				TRIGGER_UPDATE2_NAME);
		executeSQL(sqlName, tableName, geometryColumnName, idColumnName);
	}

	/**
	 * Create update 3 trigger
	 * 
	 * <pre>
	 * Conditions: Update of any column
	 *             Row ID change
	 *             Non-empty geometry
	 * Actions   : Remove record from rtree for old <i>
	 *             Insert record into rtree for new <i>
	 * </pre>
	 * 
	 * @param tableName
	 *            table name
	 * @param geometryColumnName
	 *            geometry column name
	 * @param idColumnName
	 *            id column name
	 */
	public void createUpdate3Trigger(String tableName,
			String geometryColumnName, String idColumnName) {

		String sqlName = GeoPackageProperties.getProperty(TRIGGER_PROPERTY,
				TRIGGER_UPDATE3_NAME);
		executeSQL(sqlName, tableName, geometryColumnName, idColumnName);
	}

	/**
	 * Create update 4 trigger
	 * 
	 * <pre>
	 * Conditions: Update of any column
	 *             Row ID change
	 *             Empty geometry
	 * Actions   : Remove record from rtree for old and new <i>
	 * </pre>
	 * 
	 * @param tableName
	 *            table name
	 * @param geometryColumnName
	 *            geometry column name
	 * @param idColumnName
	 *            id column name
	 */
	public void createUpdate4Trigger(String tableName,
			String geometryColumnName, String idColumnName) {

		String sqlName = GeoPackageProperties.getProperty(TRIGGER_PROPERTY,
				TRIGGER_UPDATE4_NAME);
		executeSQL(sqlName, tableName, geometryColumnName, idColumnName);
	}

	/**
	 * Create delete trigger
	 * 
	 * <pre>
	 * Conditions: Row deleted
	 * Actions   : Remove record from rtree for old <i>
	 * </pre>
	 * 
	 * @param tableName
	 *            table name
	 * @param geometryColumnName
	 *            geometry column name
	 * @param idColumnName
	 *            id column name
	 */
	public void createDeleteTrigger(String tableName,
			String geometryColumnName, String idColumnName) {

		String sqlName = GeoPackageProperties.getProperty(TRIGGER_PROPERTY,
				TRIGGER_DELETE_NAME);
		executeSQL(sqlName, tableName, geometryColumnName, idColumnName);
	}

	/**
	 * Delete the RTree Index extension for the feature table. Drops the
	 * triggers, RTree table, and deletes the extension.
	 * 
	 * @param featureTable
	 *            feature table
	 */
	public void delete(FeatureTable featureTable) {
		delete(featureTable.getTableName(), featureTable.getGeometryColumn()
				.getName());
	}

	/**
	 * Delete the RTree Index extension for the table and geometry column. Drops
	 * the triggers, RTree table, and deletes the extension.
	 * 
	 * @param tableName
	 *            table name
	 * @param geometryColumnName
	 *            geometry column name
	 */
	public void delete(String tableName, String geometryColumnName) {

		drop(tableName, geometryColumnName);

		try {
			extensionsDao.deleteByExtension(EXTENSION_NAME, tableName,
					geometryColumnName);
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to delete RTree Index extension. GeoPackage: "
							+ geoPackage.getName() + ", Table: " + tableName
							+ ", Geometry Column: " + geometryColumnName, e);
		}

	}

	/**
	 * Drop the the triggers and RTree table for the feature table
	 * 
	 * @param featureTable
	 *            feature table
	 */
	public void drop(FeatureTable featureTable) {
		drop(featureTable.getTableName(), featureTable.getGeometryColumn()
				.getName());
	}

	/**
	 * Drop the the triggers and RTree table for the table and geometry column
	 * 
	 * @param tableName
	 *            table name
	 * @param geometryColumnName
	 *            geometry column name
	 */
	public void drop(String tableName, String geometryColumnName) {

		dropAllTriggers(tableName, geometryColumnName);
		dropRTreeIndex(tableName, geometryColumnName);

	}

	/**
	 * Drop the RTree Index Virtual Table
	 * 
	 * @param featureTable
	 *            feature table
	 */
	public void dropRTreeIndex(FeatureTable featureTable) {
		dropRTreeIndex(featureTable.getTableName(), featureTable
				.getGeometryColumn().getName());
	}

	/**
	 * Drop the RTree Index Virtual Table
	 * 
	 * @param tableName
	 *            table name
	 * @param geometryColumnName
	 *            geometry column name
	 */
	public void dropRTreeIndex(String tableName, String geometryColumnName) {

		String sqlName = GeoPackageProperties.getProperty(SQL_PROPERTY,
				DROP_PROPERTY);
		executeSQL(sqlName, tableName, geometryColumnName);
	}

	/**
	 * Check if the feature table has the RTree extension and if found, drop the
	 * triggers
	 * 
	 * @param featureTable
	 *            feature table
	 */
	public void dropTriggers(FeatureTable featureTable) {
		dropTriggers(featureTable.getTableName(), featureTable
				.getGeometryColumn().getName());
	}

	/**
	 * Check if the table and column has the RTree extension and if found, drop
	 * the triggers
	 * 
	 * @param tableName
	 *            table name
	 * @param columnName
	 *            column name
	 * @return true if dropped
	 */
	public boolean dropTriggers(String tableName, String columnName) {
		boolean dropped = has(tableName, columnName);
		if (dropped) {
			dropAllTriggers(tableName, columnName);
		}
		return dropped;
	}

	/**
	 * Drop Triggers that Maintain Spatial Index Values
	 * 
	 * @param featureTable
	 *            feature table
	 */
	public void dropAllTriggers(FeatureTable featureTable) {
		dropAllTriggers(featureTable.getTableName(), featureTable
				.getGeometryColumn().getName());
	}

	/**
	 * Drop Triggers that Maintain Spatial Index Values
	 * 
	 * @param tableName
	 *            table name
	 * @param geometryColumnName
	 *            geometry column name
	 */
	public void dropAllTriggers(String tableName, String geometryColumnName) {

		dropInsertTrigger(tableName, geometryColumnName);
		dropUpdate1Trigger(tableName, geometryColumnName);
		dropUpdate2Trigger(tableName, geometryColumnName);
		dropUpdate3Trigger(tableName, geometryColumnName);
		dropUpdate4Trigger(tableName, geometryColumnName);
		dropDeleteTrigger(tableName, geometryColumnName);

	}

	/**
	 * Drop insert trigger
	 * 
	 * @param tableName
	 *            table name
	 * @param geometryColumnName
	 *            geometry column name
	 */
	public void dropInsertTrigger(String tableName, String geometryColumnName) {
		dropTrigger(tableName, geometryColumnName, TRIGGER_INSERT_NAME);
	}

	/**
	 * Drop update 1 trigger
	 * 
	 * @param tableName
	 *            table name
	 * @param geometryColumnName
	 *            geometry column name
	 */
	public void dropUpdate1Trigger(String tableName, String geometryColumnName) {
		dropTrigger(tableName, geometryColumnName, TRIGGER_UPDATE1_NAME);
	}

	/**
	 * Drop update 2 trigger
	 * 
	 * @param tableName
	 *            table name
	 * @param geometryColumnName
	 *            geometry column name
	 */
	public void dropUpdate2Trigger(String tableName, String geometryColumnName) {
		dropTrigger(tableName, geometryColumnName, TRIGGER_UPDATE2_NAME);
	}

	/**
	 * Drop update 3 trigger
	 * 
	 * @param tableName
	 *            table name
	 * @param geometryColumnName
	 *            geometry column name
	 */
	public void dropUpdate3Trigger(String tableName, String geometryColumnName) {
		dropTrigger(tableName, geometryColumnName, TRIGGER_UPDATE3_NAME);
	}

	/**
	 * Drop update 4 trigger
	 * 
	 * @param tableName
	 *            table name
	 * @param geometryColumnName
	 *            geometry column name
	 */
	public void dropUpdate4Trigger(String tableName, String geometryColumnName) {
		dropTrigger(tableName, geometryColumnName, TRIGGER_UPDATE4_NAME);
	}

	/**
	 * Drop delete trigger
	 * 
	 * @param tableName
	 *            table name
	 * @param geometryColumnName
	 *            geometry column name
	 */
	public void dropDeleteTrigger(String tableName, String geometryColumnName) {
		dropTrigger(tableName, geometryColumnName, TRIGGER_DELETE_NAME);
	}

	/**
	 * Drop the trigger for the table, geometry column, and trigger name
	 * 
	 * @param tableName
	 * @param geometryColumnName
	 * @param triggerName
	 */
	public void dropTrigger(String tableName, String geometryColumnName,
			String triggerName) {

		String sqlName = GeoPackageProperties.getProperty(TRIGGER_PROPERTY,
				TRIGGER_DROP_PROPERTY);
		executeSQL(sqlName, tableName, geometryColumnName, null, triggerName);
	}

	/**
	 * Execute the SQL for the SQL file name while substituting values for the
	 * table and geometry column
	 * 
	 * @param sqlName
	 *            sql file name
	 * @param tableName
	 *            table name
	 * @param geometryColumnName
	 *            geometry column name
	 */
	private void executeSQL(String sqlName, String tableName,
			String geometryColumnName) {
		executeSQL(sqlName, tableName, geometryColumnName, null);
	}

	/**
	 * Execute the SQL for the SQL file name while substituting values for the
	 * table, geometry column, and id column
	 * 
	 * @param sqlName
	 *            sql file name
	 * @param tableName
	 *            table name
	 * @param geometryColumnName
	 *            geometry column name
	 * @param idColumnName
	 *            id column name
	 */
	private void executeSQL(String sqlName, String tableName,
			String geometryColumnName, String idColumnName) {
		executeSQL(sqlName, tableName, geometryColumnName, idColumnName, null);
	}

	/**
	 * Execute the SQL for the SQL file name while substituting values for the
	 * table, geometry column, id column, and trigger name
	 * 
	 * @param sqlName
	 *            sql file name
	 * @param tableName
	 *            table name
	 * @param geometryColumnName
	 *            geometry column name
	 * @param idColumnName
	 *            id column name
	 * @param triggerName
	 *            trigger name
	 */
	private void executeSQL(String sqlName, String tableName,
			String geometryColumnName, String idColumnName, String triggerName) {

		List<String> statements = ResourceIOUtils.parseSQLStatements(
				SQL_DIRECTORY, sqlName);

		for (String statement : statements) {
			String sql = substituteSqlArguments(statement, tableName,
					geometryColumnName, idColumnName, triggerName);
			connection.execSQL(sql);
		}

	}

	/**
	 * Replace the SQL arguments for the table, geometry column, id column, and
	 * trigger name
	 * 
	 * @param sql
	 *            sql to substitute
	 * @param tableName
	 *            table name
	 * @param geometryColumnName
	 *            geometry column name
	 * @param idColumnName
	 *            id column name
	 * @param triggerName
	 *            trigger name
	 * @return substituted sql
	 */
	private String substituteSqlArguments(String sql, String tableName,
			String geometryColumnName, String idColumnName, String triggerName) {

		String substituted = sql;

		substituted = substituted.replaceAll(TABLE_SUBSTITUTE, tableName);
		substituted = substituted.replaceAll(GEOMETRY_COLUMN_SUBSTITUTE,
				geometryColumnName);

		if (idColumnName != null) {
			substituted = substituted.replaceAll(PK_COLUMN_SUBSTITUTE,
					idColumnName);
		}

		if (triggerName != null) {
			substituted = substituted.replaceAll(TRIGGER_SUBSTITUTE,
					triggerName);
		}

		return substituted;
	}

}
