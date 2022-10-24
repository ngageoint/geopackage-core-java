package mil.nga.geopackage.extension;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import mil.nga.geopackage.GeoPackageConstants;
import mil.nga.geopackage.GeoPackageCore;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.db.AlterTable;
import mil.nga.geopackage.db.GeoPackageCoreConnection;
import mil.nga.geopackage.property.GeoPackageProperties;
import mil.nga.geopackage.property.PropertyConstants;
import mil.nga.geopackage.srs.SpatialReferenceSystem;

/**
 * OGC Well known text representation of Coordinate Reference Systems extension
 * <p>
 * <a href=
 * "http://www.geopackage.org/spec/#extension_crs_wkt">http://www.geopackage.org/spec/#extension_crs_wkt</a>
 * 
 * @author osbornb
 * @since 1.1.8
 */
public class CrsWktExtension extends BaseExtension {

	/**
	 * Name
	 */
	public static final String NAME = "crs_wkt";

	/**
	 * Extension name
	 */
	public static final String EXTENSION_NAME = GeoPackageConstants.EXTENSION_AUTHOR
			+ Extensions.EXTENSION_NAME_DIVIDER + NAME;
	/**
	 * Latest supported extension version
	 * 
	 * @since 6.5.1
	 */
	public static final CrsWktExtensionVersion VERSION = CrsWktExtensionVersion.V_1_1;

	/**
	 * Extension definition URL
	 */
	public static final String DEFINITION = GeoPackageProperties
			.getProperty(PropertyConstants.EXTENSIONS, NAME);

	/**
	 * Extension new column name
	 * 
	 * @since 6.5.1
	 */
	public static final String DEFINITION_COLUMN_NAME = GeoPackageProperties
			.getProperty(PropertyConstants.EXTENSIONS, NAME, "definition",
					"column_name");

	/**
	 * Extension new column definition
	 * 
	 * @since 6.5.1
	 */
	public static final String DEFINITION_COLUMN_DEF = GeoPackageProperties
			.getProperty(PropertyConstants.EXTENSIONS, NAME, "definition",
					"column_def");

	/**
	 * Extension epoch column name
	 * 
	 * @since 6.5.1
	 */
	public static final String EPOCH_COLUMN_NAME = GeoPackageProperties
			.getProperty(PropertyConstants.EXTENSIONS, NAME, "epoch",
					"column_name");

	/**
	 * Extension epoch column definition
	 * 
	 * @since 6.5.1
	 */
	public static final String EPOCH_COLUMN_DEF = GeoPackageProperties
			.getProperty(PropertyConstants.EXTENSIONS, NAME, "epoch",
					"column_def");

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
	public CrsWktExtension(GeoPackageCore geoPackage) {
		super(geoPackage);
		connection = geoPackage.getDatabase();
	}

	/**
	 * Get or create the extension
	 * 
	 * @return extension
	 * @since 6.5.1
	 */
	public List<Extensions> getOrCreate() {
		return getOrCreate(VERSION);
	}

	/**
	 * Get or create the extension
	 * 
	 * @param version
	 *            extension version
	 * @return extension
	 * @since 6.5.1
	 */
	public List<Extensions> getOrCreate(CrsWktExtensionVersion version) {

		List<Extensions> extensions = new ArrayList<>();

		extensions.add(getOrCreate(EXTENSION_NAME,
				SpatialReferenceSystem.TABLE_NAME, DEFINITION_COLUMN_NAME,
				DEFINITION, ExtensionScopeType.READ_WRITE));

		if (!hasDefinitionColumn()) {
			createDefinitionColumn();
		}

		if (version == CrsWktExtensionVersion.V_1_1) {

			String name = getExtensionName(version);
			extensions.add(getOrCreate(name, SpatialReferenceSystem.TABLE_NAME,
					DEFINITION_COLUMN_NAME, DEFINITION,
					ExtensionScopeType.READ_WRITE));
			extensions.add(getOrCreate(name, SpatialReferenceSystem.TABLE_NAME,
					EPOCH_COLUMN_NAME, DEFINITION,
					ExtensionScopeType.READ_WRITE));

			if (!hasEpochColumn()) {
				createEpochColumn();
			}

		}

		return extensions;
	}

	/**
	 * Determine if the GeoPackage has the extension
	 * 
	 * @return true if has extension
	 */
	public boolean has() {

		// TODO

		boolean exists = has(EXTENSION_NAME);

		if (exists) {
			exists = hasDefinitionColumn();
		}

		return exists;
	}

	/**
	 * Get the extension name for the version
	 * 
	 * @param version
	 *            extension version
	 * @return extension name
	 * @since 6.5.1
	 */
	public String getExtensionName(CrsWktExtensionVersion version) {
		return EXTENSION_NAME + version.getSuffix();
	}

	/**
	 * Update the extension definition
	 * 
	 * @param srsId
	 *            srs id
	 * @param definition
	 *            definition
	 */
	public void updateDefinition(long srsId, String definition) {
		connection.execSQL("UPDATE " + SpatialReferenceSystem.TABLE_NAME
				+ " SET " + DEFINITION_COLUMN_NAME + " = '" + definition
				+ "' WHERE " + SpatialReferenceSystem.COLUMN_SRS_ID + " = "
				+ srsId);
	}

	/**
	 * Get the extension definition
	 * 
	 * @param srsId
	 *            srs id
	 * @return definition
	 */
	public String getDefinition(long srsId) {
		String definition = connection.querySingleTypedResult(
				"SELECT " + DEFINITION_COLUMN_NAME + " FROM "
						+ SpatialReferenceSystem.TABLE_NAME + " WHERE "
						+ SpatialReferenceSystem.COLUMN_SRS_ID + " = ?",
				new String[] { String.valueOf(srsId) });
		return definition;
	}

	/**
	 * Update the extension epoch
	 * 
	 * @param srsId
	 *            srs id
	 * @param epoch
	 *            epoch
	 * @since 6.5.1
	 */
	public void updateEpoch(long srsId, Double epoch) {
		connection.execSQL("UPDATE " + SpatialReferenceSystem.TABLE_NAME
				+ " SET " + EPOCH_COLUMN_NAME + " = '" + epoch + "' WHERE "
				+ SpatialReferenceSystem.COLUMN_SRS_ID + " = " + srsId);
	}

	/**
	 * Get the extension epoch
	 * 
	 * @param srsId
	 *            srs id
	 * @return epoch
	 * @since 6.5.1
	 */
	public Double getEpoch(long srsId) {
		Double epoch = connection.querySingleTypedResult(
				"SELECT " + EPOCH_COLUMN_NAME + " FROM "
						+ SpatialReferenceSystem.TABLE_NAME + " WHERE "
						+ SpatialReferenceSystem.COLUMN_SRS_ID + " = ?",
				new String[] { String.valueOf(srsId) });
		return epoch;
	}

	/**
	 * Create the extension definition column
	 */
	private void createDefinitionColumn() {

		AlterTable.addColumn(connection, SpatialReferenceSystem.TABLE_NAME,
				DEFINITION_COLUMN_NAME, DEFINITION_COLUMN_DEF);

		// Update the existing known SRS values
		updateDefinition(
				GeoPackageProperties.getIntegerProperty(
						PropertyConstants.WGS_84, PropertyConstants.SRS_ID),
				GeoPackageProperties.getProperty(PropertyConstants.WGS_84,
						PropertyConstants.DEFINITION_12_063));
		updateDefinition(
				GeoPackageProperties.getIntegerProperty(
						PropertyConstants.UNDEFINED_CARTESIAN,
						PropertyConstants.SRS_ID),
				GeoPackageProperties.getProperty(
						PropertyConstants.UNDEFINED_CARTESIAN,
						PropertyConstants.DEFINITION_12_063));
		updateDefinition(
				GeoPackageProperties.getIntegerProperty(
						PropertyConstants.UNDEFINED_GEOGRAPHIC,
						PropertyConstants.SRS_ID),
				GeoPackageProperties.getProperty(
						PropertyConstants.UNDEFINED_GEOGRAPHIC,
						PropertyConstants.DEFINITION_12_063));
		updateDefinition(GeoPackageProperties.getIntegerProperty(
				PropertyConstants.WEB_MERCATOR, PropertyConstants.SRS_ID),
				GeoPackageProperties.getProperty(PropertyConstants.WEB_MERCATOR,
						PropertyConstants.DEFINITION_12_063));
	}

	/**
	 * Create the extension epoch column
	 */
	private void createEpochColumn() {
		AlterTable.addColumn(connection, SpatialReferenceSystem.TABLE_NAME,
				EPOCH_COLUMN_NAME, EPOCH_COLUMN_DEF);
	}

	/**
	 * Determine if the GeoPackage SRS table has the extension definition column
	 * 
	 * @return true if has column
	 */
	private boolean hasDefinitionColumn() {
		boolean exists = connection.columnExists(
				SpatialReferenceSystem.TABLE_NAME, DEFINITION_COLUMN_NAME);
		return exists;
	}

	/**
	 * Determine if the GeoPackage SRS table has the extension epoch column
	 * 
	 * @return true if has column
	 */
	private boolean hasEpochColumn() {
		return connection.columnExists(SpatialReferenceSystem.TABLE_NAME,
				EPOCH_COLUMN_NAME);
	}

	/**
	 * Remove the extension. Leaves the column and values.
	 * 
	 * @since 3.2.0
	 */
	public void removeExtension() {

		// TODO

		try {
			if (extensionsDao.isTableExists()) {
				extensionsDao.deleteByExtension(EXTENSION_NAME);
			}
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to delete CRS WKT extension. GeoPackage: "
							+ geoPackage.getName(),
					e);
		}

	}

}
