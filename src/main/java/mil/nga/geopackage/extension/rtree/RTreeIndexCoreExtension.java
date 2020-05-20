package mil.nga.geopackage.extension.rtree;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import mil.nga.geopackage.GeoPackageConstants;
import mil.nga.geopackage.GeoPackageCore;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.db.GeoPackageCoreConnection;
import mil.nga.geopackage.db.GeoPackageDataType;
import mil.nga.geopackage.db.GeoPackageTableCreator;
import mil.nga.geopackage.extension.BaseExtension;
import mil.nga.geopackage.extension.ExtensionScopeType;
import mil.nga.geopackage.extension.Extensions;
import mil.nga.geopackage.features.user.FeatureTable;
import mil.nga.geopackage.geom.GeoPackageGeometryData;
import mil.nga.geopackage.property.GeoPackageProperties;
import mil.nga.geopackage.property.PropertyConstants;
import mil.nga.geopackage.user.custom.UserCustomColumn;
import mil.nga.geopackage.user.custom.UserCustomTable;
import mil.nga.sf.GeometryEnvelope;

/**
 * RTree Index abstract core extension
 * 
 * https://www.geopackage.org/spec/#extension_rtree
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
	 * RTree table and trigger name prefix
	 * 
	 * @since 3.3.0
	 */
	public static final String RTREE_PREFIX = "rtree_";

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
	 * Table SQL property
	 * 
	 * @since 3.1.0
	 */
	public static final String TABLE_PROPERTY = "table";

	/**
	 * Load SQL property
	 */
	public static final String LOAD_PROPERTY = "load";

	/**
	 * Drop SQL property
	 */
	public static final String DROP_PROPERTY = "drop";

	/**
	 * Drop Force SQL property
	 * 
	 * @since 3.2.0
	 */
	public static final String DROP_FORCE_PROPERTY = "drop_force";

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
	 * ID column name
	 * 
	 * @since 3.1.0
	 */
	public static final String COLUMN_ID = "id";

	/**
	 * Min X column name
	 * 
	 * @since 3.1.0
	 */
	public static final String COLUMN_MIN_X = "minx";

	/**
	 * Max X column name
	 * 
	 * @since 3.1.0
	 */
	public static final String COLUMN_MAX_X = "maxx";

	/**
	 * Min Y column name
	 * 
	 * @since 3.1.0
	 */
	public static final String COLUMN_MIN_Y = "miny";

	/**
	 * Max Y column name
	 * 
	 * @since 3.1.0
	 */
	public static final String COLUMN_MAX_Y = "maxy";

	/**
	 * Extension name
	 */
	public static final String EXTENSION_NAME = GeoPackageConstants.EXTENSION_AUTHOR
			+ Extensions.EXTENSION_NAME_DIVIDER + NAME;

	/**
	 * Base extension property
	 */
	private static final String EXTENSION_PROPERTY = GeoPackageProperties
			.buildProperty(PropertyConstants.EXTENSIONS, NAME);

	/**
	 * Extension definition URL
	 */
	public static final String DEFINITION = GeoPackageProperties
			.getProperty(EXTENSION_PROPERTY);

	/**
	 * SQL base property
	 */
	private static final String SQL_PROPERTY = GeoPackageProperties
			.buildProperty(EXTENSION_PROPERTY, PropertyConstants.SQL);

	/**
	 * SQL substitute base property
	 */
	private static final String SUBSTITUTE_PROPERTY = GeoPackageProperties
			.buildProperty(SQL_PROPERTY, "substitute");

	/**
	 * SQL trigger base property
	 */
	private static final String TRIGGER_PROPERTY = GeoPackageProperties
			.buildProperty(SQL_PROPERTY, "trigger");

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
	 * Connection
	 */
	protected GeoPackageCoreConnection connection = null;

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
		return getOrCreate(featureTable.getTableName(),
				featureTable.getGeometryColumnName());
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
		return has(featureTable.getTableName(),
				featureTable.getGeometryColumnName());
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
		return has(EXTENSION_NAME, tableName, columnName) && connection
				.tableOrViewExists(getRTreeTableName(tableName, columnName));
	}

	/**
	 * Determine if the GeoPackage table has the extension
	 * 
	 * @param tableName
	 *            table name
	 * @return true if has extension
	 * @since 3.2.0
	 */
	public boolean has(String tableName) {
		return super.has(EXTENSION_NAME, tableName);
	}

	/**
	 * Determine if the GeoPackage has the extension for any table
	 * 
	 * @return true if has extension
	 */
	public boolean has() {
		return super.has(EXTENSION_NAME);
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
		return createFunctions(featureTable.getTableName(),
				featureTable.getGeometryColumnName());
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
		return create(featureTable.getTableName(),
				featureTable.getGeometryColumnName(),
				featureTable.getPkColumn().getName());
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
		createRTreeIndex(featureTable.getTableName(),
				featureTable.getGeometryColumnName());
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
		loadRTreeIndex(featureTable.getTableName(),
				featureTable.getGeometryColumnName(),
				featureTable.getPkColumn().getName());
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
		createAllTriggers(featureTable.getTableName(),
				featureTable.getGeometryColumnName(),
				featureTable.getPkColumn().getName());
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
	public void createInsertTrigger(String tableName, String geometryColumnName,
			String idColumnName) {

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
	 * Actions   : Remove record from rtree for old {@literal <i>}
	 *             Insert record into rtree for new {@literal <i>}
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
	 * Actions   : Remove record from rtree for old and new {@literal <i>}
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
	 * Actions   : Remove record from rtree for old {@literal <i>}
	 * </pre>
	 * 
	 * @param tableName
	 *            table name
	 * @param geometryColumnName
	 *            geometry column name
	 * @param idColumnName
	 *            id column name
	 */
	public void createDeleteTrigger(String tableName, String geometryColumnName,
			String idColumnName) {

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
		delete(featureTable.getTableName(),
				featureTable.getGeometryColumnName());
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

		if (has(tableName, geometryColumnName)) {
			drop(tableName, geometryColumnName);

			try {
				extensionsDao.deleteByExtension(EXTENSION_NAME, tableName,
						geometryColumnName);
			} catch (SQLException e) {
				throw new GeoPackageException(
						"Failed to delete RTree Index extension. GeoPackage: "
								+ geoPackage.getName() + ", Table: " + tableName
								+ ", Geometry Column: " + geometryColumnName,
						e);
			}
		}

	}

	/**
	 * Delete all RTree Index extensions for the table. Drops the triggers,
	 * RTree tables, and deletes the extensions.
	 * 
	 * @param tableName
	 *            table name
	 * 
	 * @since 3.2.0
	 */
	public void delete(String tableName) {
		try {
			if (extensionsDao.isTableExists()) {
				List<Extensions> extensions = extensionsDao
						.queryByExtension(EXTENSION_NAME, tableName);
				for (Extensions extension : extensions) {
					delete(extension.getTableName(), extension.getColumnName());
				}
			}
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to delete RTree Index extensions for table. GeoPackage: "
							+ geoPackage.getName() + ", Table: " + tableName,
					e);
		}

	}

	/**
	 * Delete all RTree Index extensions. Drops the triggers, RTree tables, and
	 * deletes the extensions.
	 * 
	 * @since 3.2.0
	 */
	public void deleteAll() {
		try {
			if (extensionsDao.isTableExists()) {
				List<Extensions> extensions = extensionsDao
						.queryByExtension(EXTENSION_NAME);
				for (Extensions extension : extensions) {
					delete(extension.getTableName(), extension.getColumnName());
				}
			}
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to delete all RTree Index extensions. GeoPackage: "
							+ geoPackage.getName(),
					e);
		}

	}

	/**
	 * Drop the the triggers and RTree table for the feature table
	 * 
	 * @param featureTable
	 *            feature table
	 */
	public void drop(FeatureTable featureTable) {
		drop(featureTable.getTableName(), featureTable.getGeometryColumnName());
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
		dropRTreeIndex(featureTable.getTableName(),
				featureTable.getGeometryColumnName());
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
		try {
			executeSQL(sqlName, tableName, geometryColumnName);
		} catch (Exception e) {
			// If no rtree module, try to delete manually
			if (e.getMessage().indexOf("no such module: rtree") > -1) {
				sqlName = GeoPackageProperties.getProperty(SQL_PROPERTY,
						DROP_FORCE_PROPERTY);
				executeSQL(sqlName, tableName, geometryColumnName);
			} else {
				throw e;
			}
		}

	}

	/**
	 * Check if the feature table has the RTree extension and if found, drop the
	 * triggers
	 * 
	 * @param featureTable
	 *            feature table
	 */
	public void dropTriggers(FeatureTable featureTable) {
		dropTriggers(featureTable.getTableName(),
				featureTable.getGeometryColumnName());
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
		dropAllTriggers(featureTable.getTableName(),
				featureTable.getGeometryColumnName());
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
	public void dropUpdate1Trigger(String tableName,
			String geometryColumnName) {
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
	public void dropUpdate2Trigger(String tableName,
			String geometryColumnName) {
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
	public void dropUpdate3Trigger(String tableName,
			String geometryColumnName) {
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
	public void dropUpdate4Trigger(String tableName,
			String geometryColumnName) {
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
	 *            table name
	 * @param geometryColumnName
	 *            geometry column name
	 * @param triggerName
	 *            trigger name
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
			String geometryColumnName, String idColumnName,
			String triggerName) {

		List<String> statements = GeoPackageTableCreator
				.readSQLScript(GeoPackageTableCreator.RTREE, sqlName);

		for (String statement : statements) {
			String sql = substituteSqlArguments(statement, tableName,
					geometryColumnName, idColumnName, triggerName);
			executeSQL(sql, triggerName != null);
		}

	}

	/**
	 * Execute the SQL statement
	 * 
	 * @param sql
	 *            SQL statement
	 * @param trigger
	 *            true if a trigger statement
	 * @since 3.1.0
	 */
	protected void executeSQL(String sql, boolean trigger) {
		connection.execSQL(sql);
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
			String geometryColumnName, String idColumnName,
			String triggerName) {

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

	/**
	 * Get or build a geometry envelope from the Geometry Data
	 * 
	 * @param data
	 *            geometry data
	 * @return geometry envelope
	 */
	protected GeometryEnvelope getEnvelope(GeoPackageGeometryData data) {
		GeometryEnvelope envelope = null;
		if (data != null) {
			envelope = data.getOrBuildEnvelope();
		}
		return envelope;
	}

	/**
	 * Get the RTree Table name for the feature table and geometry column
	 * 
	 * @param tableName
	 *            feature table name
	 * @param geometryColumnName
	 *            geometry column name
	 * @return RTree table name
	 */
	private String getRTreeTableName(String tableName,
			String geometryColumnName) {
		String sqlName = GeoPackageProperties.getProperty(SQL_PROPERTY,
				TABLE_PROPERTY);
		String rTreeTableName = substituteSqlArguments(sqlName, tableName,
				geometryColumnName, null, null);
		return rTreeTableName;
	}

	/**
	 * Get the RTree Table
	 * 
	 * @param featureTable
	 *            feature table
	 * @return RTree table
	 */
	protected UserCustomTable getRTreeTable(FeatureTable featureTable) {

		List<UserCustomColumn> columns = new ArrayList<>();
		columns.add(UserCustomColumn.createPrimaryKeyColumn(COLUMN_ID, false));
		columns.add(UserCustomColumn.createColumn(COLUMN_MIN_X,
				GeoPackageDataType.FLOAT));
		columns.add(UserCustomColumn.createColumn(COLUMN_MAX_X,
				GeoPackageDataType.FLOAT));
		columns.add(UserCustomColumn.createColumn(COLUMN_MIN_Y,
				GeoPackageDataType.FLOAT));
		columns.add(UserCustomColumn.createColumn(COLUMN_MAX_Y,
				GeoPackageDataType.FLOAT));

		String rTreeTableName = getRTreeTableName(featureTable.getTableName(),
				featureTable.getGeometryColumnName());

		UserCustomTable userCustomTable = new UserCustomTable(rTreeTableName,
				columns);

		return userCustomTable;
	}

}
