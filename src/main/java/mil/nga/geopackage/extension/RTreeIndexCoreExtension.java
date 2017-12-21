package mil.nga.geopackage.extension;

import mil.nga.geopackage.GeoPackageConstants;
import mil.nga.geopackage.GeoPackageCore;
import mil.nga.geopackage.db.GeoPackageCoreConnection;
import mil.nga.geopackage.features.user.FeatureTable;
import mil.nga.geopackage.property.GeoPackageProperties;
import mil.nga.geopackage.property.PropertyConstants;

/**
 * RTree Index extension
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
	 * Extension name
	 */
	public static final String EXTENSION_NAME = GeoPackageConstants.GEO_PACKAGE_EXTENSION_AUTHOR
			+ Extensions.EXTENSION_NAME_DIVIDER + NAME;

	/**
	 * Extension definition URL
	 */
	public static final String DEFINITION = GeoPackageProperties.getProperty(
			PropertyConstants.EXTENSIONS, NAME);

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
	public RTreeIndexCoreExtension(GeoPackageCore geoPackage) {
		super(geoPackage);
		connection = geoPackage.getDatabase();
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

		Extensions extension = getOrCreate(EXTENSION_NAME, tableName,
				columnName, DEFINITION, ExtensionScopeType.WRITE_ONLY);

		return extension;
	}

	/**
	 * Determine if the GeoPackage has the extension
	 * 
	 * @param tableName
	 *            table name
	 * @param columnName
	 *            column name
	 * @return true if has extension
	 */
	public boolean has(String tableName, String columnName) {

		boolean exists = has(EXTENSION_NAME, tableName, columnName);

		return exists;
	}

	public boolean has(FeatureTable featureTable) {
		return has(featureTable.getTableName(), featureTable
				.getGeometryColumn().getName());
	}

	public Extensions create(FeatureTable featureTable) {

		String tableName = featureTable.getTableName();
		String geometryColumnName = featureTable.getGeometryColumn().getName();
		String idColumnName = featureTable.getPkColumn().getName();

		Extensions extension = getOrCreate(tableName, geometryColumnName);

		createRTree(tableName, geometryColumnName);
		createFunctions();
		loadRTree(tableName, geometryColumnName, idColumnName);
		createTriggers(tableName, geometryColumnName, idColumnName);

		return extension;
	}

	/**
	 * Create Virtual Table
	 * 
	 * @param tableName
	 *            table name
	 * @param geometryColumnName
	 *            geometry column name
	 */
	private void createRTree(String tableName, String geometryColumnName) {

		String sql = "CREATE VIRTUAL TABLE rtree_<t>_<c> USING rtree(id, minx, maxx, miny, maxy)";

		sql = replaceSqlArguments(sql, tableName, geometryColumnName);

		connection.execSQL(sql);
	}

	private void createFunctions() {
		createMinXFunction("ST_MinX");
		createMaxXFunction("ST_MaxX");
		createMinYFunction("ST_MinY");
		createMaxYFunction("ST_MaxY");
		createIsEmptyFunction("ST_IsEmpty");
	}

	protected abstract void createMinXFunction(String name);

	protected abstract void createMaxXFunction(String name);

	protected abstract void createMinYFunction(String name);

	protected abstract void createMaxYFunction(String name);

	protected abstract void createIsEmptyFunction(String name);

	/**
	 * Load Spatial Index Values
	 * 
	 * @param tableName
	 *            table name
	 * @param geometryColumnName
	 *            geometry column name
	 * @param idColumnName
	 *            id column name
	 */
	private void loadRTree(String tableName, String geometryColumnName,
			String idColumnName) {

		String sql = "INSERT OR REPLACE INTO rtree_<t>_<c>"
				+ " SELECT <i>, st_minx(<c>), st_maxx(<c>), st_miny(<c>), st_maxy(<c>) FROM <t>;";

		sql = replaceSqlArguments(sql, tableName, geometryColumnName,
				idColumnName);

		connection.execSQL(sql);
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
	private void createTriggers(String tableName, String geometryColumnName,
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
	private void createInsertTrigger(String tableName,
			String geometryColumnName, String idColumnName) {

		String sql = "CREATE TRIGGER rtree_<t>_<c>_insert AFTER INSERT ON <t>"
				+ " WHEN (new.<c> NOT NULL AND NOT ST_IsEmpty(NEW.<c>))"
				+ " BEGIN" + " INSERT OR REPLACE INTO rtree_<t>_<c> VALUES ("
				+ " NEW.<i>," + " ST_MinX(NEW.<c>), ST_MaxX(NEW.<c>),"
				+ " ST_MinY(NEW.<c>), ST_MaxY(NEW.<c>)" + " );" + " END;";

		sql = replaceSqlArguments(sql, tableName, geometryColumnName,
				idColumnName);

		connection.execSQL(sql);
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
	private void createUpdate1Trigger(String tableName,
			String geometryColumnName, String idColumnName) {

		String sql = "CREATE TRIGGER rtree_<t>_<c>_update1 AFTER UPDATE OF <c> ON <t>"
				+ " WHEN OLD.<i> = NEW.<i> AND"
				+ " (NEW.<c> NOTNULL AND NOT ST_IsEmpty(NEW.<c>))"
				+ " BEGIN"
				+ " INSERT OR REPLACE INTO rtree_<t>_<c> VALUES ("
				+ " NEW.<i>,"
				+ " ST_MinX(NEW.<c>), ST_MaxX(NEW.<c>),"
				+ " ST_MinY(NEW.<c>), ST_MaxY(NEW.<c>)" + " );" + " END;";

		sql = replaceSqlArguments(sql, tableName, geometryColumnName,
				idColumnName);

		connection.execSQL(sql);
	}

	/**
	 * Create update 2 trigger
	 * 
	 * <pre>
	 * 
	 * </pre>
	 * 
	 * @param tableName
	 *            table name
	 * @param geometryColumnName
	 *            geometry column name
	 * @param idColumnName
	 *            id column name
	 */
	private void createUpdate2Trigger(String tableName,
			String geometryColumnName, String idColumnName) {

		String sql = "CREATE TRIGGER rtree_<t>_<c>_update2 AFTER UPDATE OF <c> ON <t>"
				+ " WHEN OLD.<i> = NEW.<i> AND"
				+ " (NEW.<c> ISNULL OR ST_IsEmpty(NEW.<c>))"
				+ " BEGIN"
				+ " DELETE FROM rtree_<t>_<c> WHERE id = OLD.<i>;" + "END;";

		sql = replaceSqlArguments(sql, tableName, geometryColumnName,
				idColumnName);

		connection.execSQL(sql);
	}

	/**
	 * Create update 3 trigger
	 * 
	 * <pre>
	 * 
	 * </pre>
	 * 
	 * @param tableName
	 *            table name
	 * @param geometryColumnName
	 *            geometry column name
	 * @param idColumnName
	 *            id column name
	 */
	private void createUpdate3Trigger(String tableName,
			String geometryColumnName, String idColumnName) {

		String sql = "CREATE TRIGGER rtree_<t>_<c>_update3 AFTER UPDATE OF <c> ON <t>"
				+ " WHEN OLD.<i> != NEW.<i> AND"
				+ " (NEW.<c> NOTNULL AND NOT ST_IsEmpty(NEW.<c>))"
				+ " BEGIN"
				+ " DELETE FROM rtree_<t>_<c> WHERE id = OLD.<i>;"
				+ " INSERT OR REPLACE INTO rtree_<t>_<c> VALUES ("
				+ " NEW.<i>,"
				+ " ST_MinX(NEW.<c>), ST_MaxX(NEW.<c>),"
				+ " ST_MinY(NEW.<c>), ST_MaxY(NEW.<c>)" + " );" + " END;";

		sql = replaceSqlArguments(sql, tableName, geometryColumnName,
				idColumnName);

		connection.execSQL(sql);
	}

	/**
	 * Create update 4 trigger
	 * 
	 * <pre>
	 * 
	 * </pre>
	 * 
	 * @param tableName
	 *            table name
	 * @param geometryColumnName
	 *            geometry column name
	 * @param idColumnName
	 *            id column name
	 */
	private void createUpdate4Trigger(String tableName,
			String geometryColumnName, String idColumnName) {

		String sql = "CREATE TRIGGER rtree_<t>_<c>_update4 AFTER UPDATE ON <t>"
				+ " WHEN OLD.<i> != NEW.<i> AND"
				+ " (NEW.<c> ISNULL OR ST_IsEmpty(NEW.<c>))" + " BEGIN"
				+ " DELETE FROM rtree_<t>_<c> WHERE id IN (OLD.<i>, NEW.<i>);"
				+ " END;";

		sql = replaceSqlArguments(sql, tableName, geometryColumnName,
				idColumnName);

		connection.execSQL(sql);
	}

	/**
	 * Create delete trigger
	 * 
	 * <pre>
	 * 
	 * </pre>
	 * 
	 * @param tableName
	 *            table name
	 * @param geometryColumnName
	 *            geometry column name
	 * @param idColumnName
	 *            id column name
	 */
	private void createDeleteTrigger(String tableName,
			String geometryColumnName, String idColumnName) {

		String sql = "CREATE TRIGGER rtree_<t>_<c>_delete AFTER DELETE ON <t>"
				+ " WHEN old.<c> NOT NULL" + " BEGIN"
				+ " DELETE FROM rtree_<t>_<c> WHERE id = OLD.<i>;" + " END;";

		sql = replaceSqlArguments(sql, tableName, geometryColumnName,
				idColumnName);

		connection.execSQL(sql);
	}

	private String replaceSqlArguments(String sql, String tableName,
			String geometryColumnName) {
		return replaceSqlArguments(sql, tableName, geometryColumnName, null);
	}

	private String replaceSqlArguments(String sql, String tableName,
			String geometryColumnName, String idColumnName) {
		String replaced = sql;
		replaced = replaced.replaceAll("<t>", tableName);
		replaced = replaced.replaceAll("<c>", geometryColumnName);
		if (idColumnName != null) {
			replaced = replaced.replaceAll("<i>", idColumnName);
		}
		return replaced;
	}

}
