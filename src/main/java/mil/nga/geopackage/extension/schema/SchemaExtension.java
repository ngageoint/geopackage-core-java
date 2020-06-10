package mil.nga.geopackage.extension.schema;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import mil.nga.geopackage.GeoPackageConstants;
import mil.nga.geopackage.GeoPackageCore;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.db.GeoPackageCoreConnection;
import mil.nga.geopackage.extension.BaseExtension;
import mil.nga.geopackage.extension.ExtensionScopeType;
import mil.nga.geopackage.extension.Extensions;
import mil.nga.geopackage.extension.schema.columns.DataColumns;
import mil.nga.geopackage.extension.schema.columns.DataColumnsDao;
import mil.nga.geopackage.extension.schema.constraints.DataColumnConstraints;
import mil.nga.geopackage.extension.schema.constraints.DataColumnConstraintsDao;
import mil.nga.geopackage.property.GeoPackageProperties;
import mil.nga.geopackage.property.PropertyConstants;

/**
 * Schema extension
 * 
 * https://www.geopackage.org/spec/#extension_schema
 * 
 * @author osbornb
 * @since 1.1.8
 */
public class SchemaExtension extends BaseExtension {

	/**
	 * Name
	 */
	public static final String NAME = "schema";

	/**
	 * Extension name
	 */
	public static final String EXTENSION_NAME = GeoPackageConstants.EXTENSION_AUTHOR
			+ Extensions.EXTENSION_NAME_DIVIDER + NAME;

	/**
	 * Extension definition URL
	 */
	public static final String DEFINITION = GeoPackageProperties
			.getProperty(PropertyConstants.EXTENSIONS, NAME);

	/**
	 * Constructor
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * 
	 */
	public SchemaExtension(GeoPackageCore geoPackage) {
		super(geoPackage);
	}

	/**
	 * Get or create the extension
	 * 
	 * @return extensions
	 */
	public List<Extensions> getOrCreate() {

		List<Extensions> extensions = new ArrayList<>();

		extensions.add(getOrCreate(EXTENSION_NAME, DataColumns.TABLE_NAME, null,
				DEFINITION, ExtensionScopeType.READ_WRITE));
		extensions.add(
				getOrCreate(EXTENSION_NAME, DataColumnConstraints.TABLE_NAME,
						null, DEFINITION, ExtensionScopeType.READ_WRITE));

		return extensions;
	}

	/**
	 * Determine if the GeoPackage has the extension
	 * 
	 * @return true if has extension
	 */
	public boolean has() {

		boolean exists = has(EXTENSION_NAME);

		return exists;
	}

	/**
	 * Remove all trace of the extension
	 * 
	 * @since 3.2.0
	 */
	public void removeExtension() {

		if (geoPackage.isTable(DataColumns.TABLE_NAME)) {
			geoPackage.dropTable(DataColumns.TABLE_NAME);
		}

		if (geoPackage.isTable(DataColumnConstraints.TABLE_NAME)) {
			geoPackage.dropTable(DataColumnConstraints.TABLE_NAME);
		}

		try {
			if (extensionsDao.isTableExists()) {
				extensionsDao.deleteByExtension(EXTENSION_NAME);
			}
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to delete Schema extension. GeoPackage: "
							+ geoPackage.getName(),
					e);
		}

	}

	/**
	 * Get a Data Columns DAO
	 * 
	 * @return Data Columns DAO
	 * @since 4.0.0
	 */
	public DataColumnsDao getDataColumnsDao() {
		return getDataColumnsDao(geoPackage);
	}

	/**
	 * Get a Data Columns DAO
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * 
	 * @return Data Columns DAO
	 * @since 4.0.0
	 */
	public static DataColumnsDao getDataColumnsDao(GeoPackageCore geoPackage) {
		return DataColumnsDao.create(geoPackage);
	}

	/**
	 * Get a Data Columns DAO
	 * 
	 * @param db
	 *            database connection
	 * 
	 * @return Data Columns DAO
	 * @since 4.0.0
	 */
	public static DataColumnsDao getDataColumnsDao(
			GeoPackageCoreConnection db) {
		return DataColumnsDao.create(db);
	}

	/**
	 * Create the Data Columns table if it does not already exist
	 * 
	 * @return true if created
	 * @since 4.0.0
	 */
	public boolean createDataColumnsTable() {
		verifyWritable();

		boolean created = false;
		DataColumnsDao dao = getDataColumnsDao();
		try {
			if (!dao.isTableExists()) {
				created = geoPackage.getTableCreator().createDataColumns() > 0;
			}
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to check if " + DataColumns.class.getSimpleName()
							+ " table exists and create it",
					e);
		}
		return created;
	}

	/**
	 * Get a Data Column Constraints DAO
	 * 
	 * @return Data Column Constraints DAO
	 * @since 4.0.0
	 */
	public DataColumnConstraintsDao getDataColumnConstraintsDao() {
		return getDataColumnConstraintsDao(geoPackage);
	}

	/**
	 * Get a Data Column Constraints DAO
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * 
	 * @return Data Column Constraints DAO
	 * @since 4.0.0
	 */
	public static DataColumnConstraintsDao getDataColumnConstraintsDao(
			GeoPackageCore geoPackage) {
		return DataColumnConstraintsDao.create(geoPackage);
	}

	/**
	 * Get a Data Column Constraints DAO
	 * 
	 * @param db
	 *            database connection
	 * 
	 * @return Data Column Constraints DAO
	 * @since 4.0.0
	 */
	public static DataColumnConstraintsDao getDataColumnConstraintsDao(
			GeoPackageCoreConnection db) {
		return DataColumnConstraintsDao.create(db);
	}

	/**
	 * Create the Data Column Constraints table if it does not already exist
	 * 
	 * @return true if created
	 * @since 4.0.0
	 */
	public boolean createDataColumnConstraintsTable() {
		verifyWritable();

		boolean created = false;
		DataColumnConstraintsDao dao = getDataColumnConstraintsDao();
		try {
			if (!dao.isTableExists()) {
				created = geoPackage.getTableCreator()
						.createDataColumnConstraints() > 0;
				if (created) {
					// Create the schema extension record
					SchemaExtension schemaExtension = new SchemaExtension(
							geoPackage);
					schemaExtension.getOrCreate();
				}
			}
		} catch (SQLException e) {
			throw new GeoPackageException("Failed to check if "
					+ DataColumnConstraints.class.getSimpleName()
					+ " table exists and create it", e);
		}
		return created;
	}

}
