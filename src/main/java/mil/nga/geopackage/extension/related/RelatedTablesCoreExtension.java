package mil.nga.geopackage.extension.related;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mil.nga.geopackage.GeoPackageCore;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.db.CoreSQLUtils;
import mil.nga.geopackage.db.GeoPackageTableCreator;
import mil.nga.geopackage.extension.BaseExtension;
import mil.nga.geopackage.extension.ExtensionScopeType;
import mil.nga.geopackage.extension.Extensions;
import mil.nga.geopackage.property.GeoPackageProperties;
import mil.nga.geopackage.property.PropertyConstants;

/**
 * Related Tables core extension
 * 
 * @author jyutzler
 */
public abstract class RelatedTablesCoreExtension extends BaseExtension {

	/**
	 * Name
	 */
	public static final String NAME = "related_tables";

	/**
	 * ExtendedRelations Dao
	 */
	private ExtendedRelationsDao extendedRelationsDao = geoPackage
			.createDao(ExtendedRelation.class);

	/**
	 * Extension name
	 */
	public static final String EXTENSION_NAME = /*
												 * GeoPackageConstants.
												 * GEO_PACKAGE_EXTENSION_AUTHOR
												 * +
												 * Extensions.EXTENSION_NAME_DIVIDER
												 * +
												 */NAME; // Remove the comment
															// when the
															// extension is
															// adopted

	/**
	 * Extension definition URL
	 */
	public static final String DEFINITION = GeoPackageProperties.getProperty(
			PropertyConstants.EXTENSIONS, NAME);

	/**
	 * Constructor
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * 
	 */
	protected RelatedTablesCoreExtension(GeoPackageCore geoPackage) {
		super(geoPackage);
	}

	/**
	 * Get or create the extension
	 * 
	 * @return extension
	 */
	public Extensions getOrCreate() {

		Extensions extension = getOrCreate(EXTENSION_NAME, "gpkgext_relations",
				null, DEFINITION, ExtensionScopeType.READ_WRITE);

		try {
			if (!extendedRelationsDao.isTableExists()) {
				GeoPackageTableCreator tableCreator = new GeoPackageTableCreator(
						geoPackage.getDatabase());
				tableCreator.createExtendedRelations();
			}
		} catch (SQLException e) {
			throw new GeoPackageException("Failed to create '" + EXTENSION_NAME
					+ "' extension for GeoPackage: " + geoPackage.getName()
					+ ", Table Name: " + extendedRelationsDao.getTableName(), e);
		}
		return extension;
	}

	/**
	 * Determine if the GeoPackage has the extension
	 * 
	 * @return true if has extension
	 */
	public boolean has() {

		boolean exists = has(EXTENSION_NAME, null, null);

		return exists;
	}

	/**
	 * Returns the relationships defined through this extension
	 * 
	 * @return a collection of ExtendedRelation objects
	 */
	public Collection<ExtendedRelation> getRelationships() {
		Collection<ExtendedRelation> result = null;
		try {
			result = extendedRelationsDao.queryForAll();
		} catch (SQLException e) {
			throw new GeoPackageException("Failed to query for relationships "
					+ "in " + EXTENSION_NAME, e);
		}
		return result;
	}

	/**
	 * Adds a relationship between the base and related tables
	 * 
	 * @param baseTableName
	 * @param relatedTableName
	 * @param mappingTableName
	 * @param relationshipName
	 * @return The relationship that was added
	 */
	public ExtendedRelation addRelationship(String baseTableName,
			String relatedTableName, String mappingTableName,
			String relationshipName) {
		// Create the user mapping table
		UserMappingTable umt = new UserMappingTable(mappingTableName);
		GeoPackageTableCreator tableCreator = new GeoPackageTableCreator(
				geoPackage.getDatabase());
		tableCreator.createTable(umt);

		// Add a row to gpkgext_relations
		ExtendedRelation extendedRelation = new ExtendedRelation();
		extendedRelation.setBaseTableName(baseTableName);
		extendedRelation.setBasePrimaryColumn(geoPackage.getDatabase()
				.getPrimaryKeyColumnName(baseTableName));
		extendedRelation.setRelatedTableName(relatedTableName);
		extendedRelation.setRelatedPrimaryColumn(geoPackage.getDatabase()
				.getPrimaryKeyColumnName(relatedTableName));
		extendedRelation.setMappingTableName(mappingTableName);
		extendedRelation.setRelationName(relationshipName);
		try {
			extendedRelationsDao.create(extendedRelation);
		} catch (SQLException e) {
			throw new GeoPackageException("Failed to add relationship '"
					+ relationshipName + "' between " + baseTableName + " and "
					+ relatedTableName, e);
		}
		return extendedRelation;
	}

	/**
	 * Remove a specific relationship from the GeoPackage
	 * 
	 * @param baseTableName
	 * @param relatedTableName
	 * @param relationshipName
	 */
	public void removeRelationship(String baseTableName,
			String relatedTableName, String relationshipName) {
		Map<String, Object> fieldValues = new HashMap<String, Object>();
		fieldValues.put(ExtendedRelation.COLUMN_BASE_TABLE_NAME, baseTableName);
		fieldValues.put(ExtendedRelation.COLUMN_RELATED_TABLE_NAME,
				relatedTableName);
		fieldValues
				.put(ExtendedRelation.COLUMN_RELATION_NAME, relationshipName);
		List<ExtendedRelation> extendedRelations;
		try {
			extendedRelations = extendedRelationsDao
					.queryForFieldValues(fieldValues);
			extendedRelationsDao.delete(extendedRelations);
			for (ExtendedRelation extendedRelation : extendedRelations) {
				geoPackage.dropTable(extendedRelation.getMappingTableName());
			}
		} catch (SQLException e) {
			throw new GeoPackageException("Failed to remove relationship '"
					+ relationshipName + "' between " + baseTableName + " and "
					+ relatedTableName, e);
		}

	}

	/**
	 * Remove all trace of the extension
	 */
	public void removeExtension() {
		geoPackage.dropTable(ExtendedRelation.TABLE_NAME);
		try {
			extensionsDao.deleteByExtension(EXTENSION_NAME);
		} catch (SQLException e) {
			throw new GeoPackageException("Failed to remove extension '"
					+ EXTENSION_NAME + EXTENSION_NAME, e);
		}
	}

	/**
	 * 
	 * @param extendedRelation
	 * @param row
	 * @return The number of rows added, presumably 1
	 */
	public long addMapping(ExtendedRelation extendedRelation, UserMappingRow row) {

		Map<String, Object> values = new HashMap<String, Object>();
		values.put(UserMappingTable.COLUMN_BASE_ID, row.getBaseId());
		values.put(UserMappingTable.COLUMN_RELATED_ID, row.getRelatedId());
		return geoPackage.getDatabase().insert(
				extendedRelation.getMappingTableName(), values);
	}

	/**
	 * 
	 * @param extendedRelation
	 * @param row
	 * @return the number of rows removed
	 */
	public long removeMapping(ExtendedRelation extendedRelation,
			UserMappingRow row) {

		String whereClause = CoreSQLUtils
				.quoteWrap(UserMappingTable.COLUMN_BASE_ID)
				+ " = ? and "
				+ CoreSQLUtils.quoteWrap(UserMappingTable.COLUMN_RELATED_ID)
				+ " = ?";
		String[] whereArgs = { Long.toString(row.getBaseId()),
				Long.toString(row.getRelatedId()) };
		long result = geoPackage.getDatabase().delete(
				extendedRelation.getMappingTableName(), whereClause, whereArgs);
		return result;
	}

}
