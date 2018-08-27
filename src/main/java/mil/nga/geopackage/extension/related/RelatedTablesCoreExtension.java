package mil.nga.geopackage.extension.related;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mil.nga.geopackage.GeoPackageConstants;
import mil.nga.geopackage.GeoPackageCore;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.core.contents.Contents;
import mil.nga.geopackage.core.contents.ContentsDao;
import mil.nga.geopackage.extension.BaseExtension;
import mil.nga.geopackage.extension.ExtensionScopeType;
import mil.nga.geopackage.extension.Extensions;
import mil.nga.geopackage.extension.related.media.MediaTable;
import mil.nga.geopackage.extension.related.simple.SimpleAttributesTable;
import mil.nga.geopackage.property.GeoPackageProperties;
import mil.nga.geopackage.property.PropertyConstants;

/**
 * Related Tables core extension
 * 
 * @author jyutzler
 * @since 3.0.1
 */
public abstract class RelatedTablesCoreExtension extends BaseExtension {

	/**
	 * Extension author
	 */
	public static final String EXTENSION_AUTHOR = GeoPackageConstants.GEO_PACKAGE_EXTENSION_AUTHOR;

	/**
	 * Extension name without the author
	 */
	public static final String EXTENSION_NAME_NO_AUTHOR = "related_tables";

	/**
	 * Extension, with author and name
	 * 
	 * TODO Remove the commented sections when extension is adopted
	 */
	public static final String EXTENSION_NAME = /*
												 * Extensions.buildExtensionName(
												 * EXTENSION_AUTHOR,
												 */EXTENSION_NAME_NO_AUTHOR/* ) */;

	/**
	 * Extension definition URL
	 */
	public static final String EXTENSION_DEFINITION = GeoPackageProperties
			.getProperty(PropertyConstants.EXTENSIONS, EXTENSION_NAME_NO_AUTHOR);

	/**
	 * Extended Relations DAO
	 */
	private final ExtendedRelationsDao extendedRelationsDao;

	/**
	 * Constructor
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * 
	 */
	protected RelatedTablesCoreExtension(GeoPackageCore geoPackage) {
		super(geoPackage);
		extendedRelationsDao = geoPackage.getExtendedRelationsDao();
	}

	/**
	 * Get the extended relations DAO
	 * 
	 * @return extended relations DAO
	 */
	public ExtendedRelationsDao getExtendedRelationsDao() {
		return extendedRelationsDao;
	}

	/**
	 * Get or create the extension
	 * 
	 * @return extension
	 */
	private Extensions getOrCreate() {

		// Create table
		geoPackage.createExtendedRelationsTable();

		Extensions extension = getOrCreate(EXTENSION_NAME,
				ExtendedRelation.TABLE_NAME, null, EXTENSION_DEFINITION,
				ExtensionScopeType.READ_WRITE);

		return extension;
	}

	/**
	 * Get or create the extension
	 * 
	 * @param mappingTable
	 *            mapping table name
	 * @return extension
	 */
	private Extensions getOrCreate(String mappingTable) {

		getOrCreate();

		Extensions extension = getOrCreate(EXTENSION_NAME, mappingTable, null,
				EXTENSION_DEFINITION, ExtensionScopeType.READ_WRITE);

		return extension;
	}

	/**
	 * Determine if the GeoPackage has the extension
	 * 
	 * @return true if has extension
	 */
	public boolean has() {
		return has(EXTENSION_NAME, ExtendedRelation.TABLE_NAME, null);
	}

	/**
	 * Determine if the GeoPackage has the extension for the mapping table
	 * 
	 * @param mappingTable
	 *            mapping table name
	 * @return true if has extension
	 */
	public boolean has(String mappingTable) {
		return has() && has(EXTENSION_NAME, mappingTable, null);
	}

	/**
	 * Get the primary key of a table
	 * 
	 * @param tableName
	 *            table name
	 * @return the column name
	 */
	public abstract String getPrimaryKeyColumnName(String tableName);

	/**
	 * Set the contents in the user related table
	 * 
	 * @param table
	 *            user related table
	 */
	protected void setContents(UserRelatedTable table) {
		ContentsDao dao = geoPackage.getContentsDao();
		Contents contents = null;
		try {
			contents = dao.queryForId(table.getTableName());
		} catch (SQLException e) {
			throw new GeoPackageException("Failed to retrieve "
					+ Contents.class.getSimpleName() + " for table name: "
					+ table.getTableName(), e);
		}
		if (contents == null) {
			throw new GeoPackageException(
					"No Contents Table exists for table name: "
							+ table.getTableName());
		}
		table.setContents(contents);
	}

	/**
	 * Returns the relationships defined through this extension
	 * 
	 * @return a list of ExtendedRelation objects
	 */
	public List<ExtendedRelation> getRelationships() {
		List<ExtendedRelation> result = null;
		try {
			if (extendedRelationsDao.isTableExists()) {
				result = extendedRelationsDao.queryForAll();
			} else {
				result = new ArrayList<>();
			}
		} catch (SQLException e) {
			throw new GeoPackageException("Failed to query for relationships "
					+ "in " + EXTENSION_NAME, e);
		}
		return result;
	}

	/**
	 * Adds a relationship between the base and related table. Creates a default
	 * user mapping table if needed.
	 * 
	 * @param baseTableName
	 *            base table name
	 * @param relatedTableName
	 *            related table name
	 * @param mappingTableName
	 *            mapping table name
	 * @param relationType
	 *            relation type
	 * @return The relationship that was added
	 */
	public ExtendedRelation addRelationship(String baseTableName,
			String relatedTableName, String mappingTableName,
			RelationType relationType) {
		return addRelationship(baseTableName, relatedTableName,
				mappingTableName, relationType.getName());
	}

	/**
	 * Adds a relationship between the base and related table. Creates a default
	 * user mapping table if needed.
	 * 
	 * @param baseTableName
	 *            base table name
	 * @param relatedTableName
	 *            related table name
	 * @param mappingTableName
	 *            mapping table name
	 * @param relationAuthor
	 *            relation author
	 * @param relationName
	 *            relation name
	 * @return The relationship that was added
	 */
	public ExtendedRelation addRelationship(String baseTableName,
			String relatedTableName, String mappingTableName,
			String relationAuthor, String relationName) {
		return addRelationship(baseTableName, relatedTableName,
				mappingTableName,
				buildRelationName(relationAuthor, relationName));
	}

	/**
	 * Adds a relationship between the base and related table. Creates a default
	 * user mapping table if needed.
	 * 
	 * @param baseTableName
	 *            base table name
	 * @param relatedTableName
	 *            related table name
	 * @param mappingTableName
	 *            mapping table name
	 * @param relationName
	 *            relation name
	 * @return The relationship that was added
	 */
	public ExtendedRelation addRelationship(String baseTableName,
			String relatedTableName, String mappingTableName,
			String relationName) {

		UserMappingTable userMappingTable = UserMappingTable
				.create(mappingTableName);

		ExtendedRelation extendedRelation = addRelationship(baseTableName,
				relatedTableName, userMappingTable, relationName);

		return extendedRelation;
	}

	/**
	 * Adds a relationship between the base and related table. Creates the user
	 * mapping table if needed.
	 * 
	 * @param baseTableName
	 *            base table name
	 * @param relatedTableName
	 *            related table name
	 * @param userMappingTable
	 *            user mapping table
	 * @param relationType
	 *            relation type
	 * @return The relationship that was added
	 */
	public ExtendedRelation addRelationship(String baseTableName,
			String relatedTableName, UserMappingTable userMappingTable,
			RelationType relationType) {
		return addRelationship(baseTableName, relatedTableName,
				userMappingTable, relationType.getName());
	}

	/**
	 * Adds a relationship between the base and related table. Creates the user
	 * mapping table if needed.
	 * 
	 * @param baseTableName
	 *            base table name
	 * @param relatedTableName
	 *            related table name
	 * @param userMappingTable
	 *            user mapping table
	 * @param relationAuthor
	 *            relation author
	 * @param relationName
	 *            relation name
	 * @return The relationship that was added
	 */
	public ExtendedRelation addRelationship(String baseTableName,
			String relatedTableName, UserMappingTable userMappingTable,
			String relationAuthor, String relationName) {
		return addRelationship(baseTableName, relatedTableName,
				userMappingTable,
				buildRelationName(relationAuthor, relationName));
	}

	/**
	 * Adds a relationship between the base and related table. Creates the user
	 * mapping table if needed.
	 * 
	 * @param baseTableName
	 *            base table name
	 * @param relatedTableName
	 *            related table name
	 * @param userMappingTable
	 *            user mapping table
	 * @param relationName
	 *            relation name
	 * @return The relationship that was added
	 */
	public ExtendedRelation addRelationship(String baseTableName,
			String relatedTableName, UserMappingTable userMappingTable,
			String relationName) {

		// Validate the relation
		validateRelationship(baseTableName, relatedTableName, relationName);

		// Create the user mapping table if needed
		createUserMappingTable(userMappingTable);

		// Add a row to gpkgext_relations
		ExtendedRelation extendedRelation = new ExtendedRelation();
		extendedRelation.setBaseTableName(baseTableName);
		extendedRelation
				.setBasePrimaryColumn(getPrimaryKeyColumnName(baseTableName));
		extendedRelation.setRelatedTableName(relatedTableName);
		extendedRelation
				.setRelatedPrimaryColumn(getPrimaryKeyColumnName(relatedTableName));
		extendedRelation.setMappingTableName(userMappingTable.getTableName());
		extendedRelation.setRelationName(relationName);
		try {
			extendedRelationsDao.create(extendedRelation);
		} catch (SQLException e) {
			throw new GeoPackageException("Failed to add relationship '"
					+ relationName + "' between " + baseTableName + " and "
					+ relatedTableName, e);
		}
		return extendedRelation;
	}

	/**
	 * Adds a relationship between the base and user related table. Creates a
	 * default user mapping table and the related table if needed.
	 * 
	 * @param baseTableName
	 *            base table name
	 * @param relatedTable
	 *            user related table
	 * @param mappingTableName
	 *            user mapping table name
	 * @return The relationship that was added
	 */
	public ExtendedRelation addRelationship(String baseTableName,
			UserRelatedTable relatedTable, String mappingTableName) {

		UserMappingTable userMappingTable = UserMappingTable
				.create(mappingTableName);

		return addRelationship(baseTableName, relatedTable, userMappingTable);
	}

	/**
	 * Adds a relationship between the base and user related table. Creates the
	 * user mapping table and related table if needed.
	 * 
	 * @param baseTableName
	 *            base table name
	 * @param relatedTable
	 *            user related table
	 * @param userMappingTable
	 *            user mapping table
	 * @return The relationship that was added
	 */
	public ExtendedRelation addRelationship(String baseTableName,
			UserRelatedTable relatedTable, UserMappingTable userMappingTable) {

		// Create the related table if needed
		createRelatedTable(relatedTable);

		return addRelationship(baseTableName, relatedTable.getTableName(),
				userMappingTable, relatedTable.getRelationName());
	}

	/**
	 * Adds a features relationship between the base feature and related feature
	 * table. Creates a default user mapping table if needed.
	 * 
	 * @param baseFeaturesTableName
	 *            base features table name
	 * @param relatedFeaturesTableName
	 *            related features table name
	 * @param mappingTableName
	 *            mapping table name
	 * @return The relationship that was added
	 */
	public ExtendedRelation addFeaturesRelationship(
			String baseFeaturesTableName, String relatedFeaturesTableName,
			String mappingTableName) {
		return addRelationship(baseFeaturesTableName, relatedFeaturesTableName,
				mappingTableName, RelationType.FEATURES);
	}

	/**
	 * Adds a features relationship between the base feature and related feature
	 * table. Creates the user mapping table if needed.
	 * 
	 * @param baseFeaturesTableName
	 *            base features table name
	 * @param relatedFeaturesTableName
	 *            related features table name
	 * @param userMappingTable
	 *            user mapping table
	 * @return The relationship that was added
	 */
	public ExtendedRelation addFeaturesRelationship(
			String baseFeaturesTableName, String relatedFeaturesTableName,
			UserMappingTable userMappingTable) {
		return addRelationship(baseFeaturesTableName, relatedFeaturesTableName,
				userMappingTable, RelationType.FEATURES);
	}

	/**
	 * Adds a media relationship between the base table and user media related
	 * table. Creates a default user mapping table and the media table if
	 * needed.
	 * 
	 * @param baseTableName
	 *            base table name
	 * @param mediaTable
	 *            user media table
	 * @param mappingTableName
	 *            user mapping table name
	 * @return The relationship that was added
	 */
	public ExtendedRelation addMediaRelationship(String baseTableName,
			MediaTable mediaTable, String mappingTableName) {
		return addRelationship(baseTableName, mediaTable, mappingTableName);
	}

	/**
	 * Adds a media relationship between the base table and user media related
	 * table. Creates the user mapping table and media table if needed.
	 * 
	 * @param baseTableName
	 *            base table name
	 * @param mediaTable
	 *            user media table
	 * @param userMappingTable
	 *            user mapping table
	 * @return The relationship that was added
	 */
	public ExtendedRelation addMediaRelationship(String baseTableName,
			MediaTable mediaTable, UserMappingTable userMappingTable) {
		return addRelationship(baseTableName, mediaTable, userMappingTable);
	}

	/**
	 * Adds a simple attributes relationship between the base table and user
	 * simple attributes related table. Creates a default user mapping table and
	 * the simple attributes table if needed.
	 * 
	 * @param baseTableName
	 *            base table name
	 * @param simpleAttributesTable
	 *            user simple attributes table
	 * @param mappingTableName
	 *            user mapping table name
	 * @return The relationship that was added
	 */
	public ExtendedRelation addSimpleAttributesRelationship(
			String baseTableName, SimpleAttributesTable simpleAttributesTable,
			String mappingTableName) {
		return addRelationship(baseTableName, simpleAttributesTable,
				mappingTableName);
	}

	/**
	 * Adds a simple attributes relationship between the base table and user
	 * simple attributes related table. Creates the user mapping table and
	 * simple attributes table if needed.
	 * 
	 * @param baseTableName
	 *            base table name
	 * @param simpleAttributesTable
	 *            user simple attributes table
	 * @param userMappingTable
	 *            user mapping table
	 * @return The relationship that was added
	 */
	public ExtendedRelation addSimpleAttributesRelationship(
			String baseTableName, SimpleAttributesTable simpleAttributesTable,
			UserMappingTable userMappingTable) {
		return addRelationship(baseTableName, simpleAttributesTable,
				userMappingTable);
	}

	/**
	 * Validate that the relation name is valid between the base and related
	 * table
	 * 
	 * @param baseTableName
	 *            base table name
	 * @param relatedTableName
	 *            related table name
	 * @param relationName
	 *            relation name
	 */
	private void validateRelationship(String baseTableName,
			String relatedTableName, String relationName) {

		// Verify the base and related tables exist
		if (!geoPackage.isTable(baseTableName)) {
			throw new GeoPackageException(
					"Base Relationship table does not exist: " + baseTableName
							+ ", Relation: " + relationName);
		}
		if (!geoPackage.isTable(relatedTableName)) {
			throw new GeoPackageException(
					"Related Relationship table does not exist: "
							+ relatedTableName + ", Relation: " + relationName);
		}

		// Verify spec defined relation types
		RelationType relationType = RelationType.fromName(relationName);
		if (relationType != null) {
			validateRelationship(baseTableName, relatedTableName, relationType);
		}

	}

	/**
	 * Determine if the relation type is valid between the base and related
	 * table
	 * 
	 * @param baseTableName
	 *            base table name
	 * @param relatedTableName
	 *            related table name
	 * @param relationType
	 *            relation type
	 */
	private void validateRelationship(String baseTableName,
			String relatedTableName, RelationType relationType) {

		if (relationType != null) {

			switch (relationType) {
			case FEATURES:
				if (!geoPackage.isFeatureTable(baseTableName)) {
					throw new GeoPackageException(
							"The base table must be a feature table. Relation: "
									+ relationType.getName() + ", Base Table: "
									+ baseTableName + ", Type: "
									+ geoPackage.getTableType(baseTableName));
				}
				if (!geoPackage.isFeatureTable(relatedTableName)) {
					throw new GeoPackageException(
							"The related table must be a feature table. Relation: "
									+ relationType.getName()
									+ ", Related Table: " + relatedTableName
									+ ", Type: "
									+ geoPackage.getTableType(relatedTableName));
				}
				break;
			case SIMPLE_ATTRIBUTES:
			case MEDIA:
				if (!geoPackage.isTableType(relationType.getDataType(),
						relatedTableName)) {
					throw new GeoPackageException(
							"The related table must be a "
									+ relationType.getDataType()
									+ " table. Related Table: "
									+ relatedTableName + ", Type: "
									+ geoPackage.getTableType(relatedTableName));
				}
				break;
			default:
				throw new GeoPackageException("Unsupported relation type: "
						+ relationType);
			}

		}

	}

	/**
	 * Create a default user mapping table and extension row if either does not
	 * exist. When not created, there is no guarantee that an existing table has
	 * the same schema as the provided tabled.
	 * 
	 * @param mappingTableName
	 *            user mapping table name
	 * @return true if table was created, false if the table already existed
	 */
	public boolean createUserMappingTable(String mappingTableName) {

		UserMappingTable userMappingTable = UserMappingTable
				.create(mappingTableName);

		return createUserMappingTable(userMappingTable);
	}

	/**
	 * Create a user mapping table and extension row if either does not exist.
	 * When not created, there is no guarantee that an existing table has the
	 * same schema as the provided tabled.
	 * 
	 * @param userMappingTable
	 *            user mapping table
	 * @return true if table was created, false if the table already existed
	 */
	public boolean createUserMappingTable(UserMappingTable userMappingTable) {

		boolean created = false;

		String userMappingTableName = userMappingTable.getTableName();
		getOrCreate(userMappingTableName);

		if (!geoPackage.isTable(userMappingTableName)) {

			geoPackage.createUserTable(userMappingTable);

			created = true;
		}

		return created;
	}

	/**
	 * Create a user related table if it does not exist. When not created, there
	 * is no guarantee that an existing table has the same schema as the
	 * provided tabled.
	 * 
	 * @param relatedTable
	 *            user related table
	 * @return true if created, false if the table already existed
	 */
	public boolean createRelatedTable(UserRelatedTable relatedTable) {

		boolean created = false;

		String relatedTableName = relatedTable.getTableName();
		if (!geoPackage.isTable(relatedTableName)) {

			geoPackage.createUserTable(relatedTable);

			try {
				// Create the contents
				Contents contents = new Contents();
				contents.setTableName(relatedTableName);
				contents.setDataTypeString(relatedTable.getDataType());
				contents.setIdentifier(relatedTableName);
				ContentsDao contentsDao = geoPackage.getContentsDao();
				contentsDao.create(contents);
				contentsDao.refresh(contents);

				relatedTable.setContents(contents);

			} catch (RuntimeException e) {
				geoPackage.deleteTableQuietly(relatedTableName);
				throw e;
			} catch (SQLException e) {
				geoPackage.deleteTableQuietly(relatedTableName);
				throw new GeoPackageException(
						"Failed to create table and metadata: "
								+ relatedTableName, e);
			}

			created = true;
		}

		return created;
	}

	/**
	 * Remove a specific relationship from the GeoPackage
	 * 
	 * @param baseTableName
	 *            base table name
	 * @param relatedTableName
	 *            related table name
	 * @param relationType
	 *            relation type
	 */
	public void removeRelationship(String baseTableName,
			String relatedTableName, RelationType relationType) {
		removeRelationship(baseTableName, relatedTableName,
				relationType.getName());
	}

	/**
	 * Remove a specific relationship from the GeoPackage
	 * 
	 * @param baseTableName
	 *            base table name
	 * @param relatedTableName
	 *            related table name
	 * @param relationAuthor
	 *            relation author
	 * @param relationName
	 *            relation name
	 */
	public void removeRelationship(String baseTableName,
			String relatedTableName, String relationAuthor, String relationName) {
		removeRelationship(baseTableName, relatedTableName,
				buildRelationName(relationAuthor, relationName));
	}

	/**
	 * Remove a specific relationship from the GeoPackage
	 * 
	 * @param extendedRelation
	 *            extended relation
	 */
	public void removeRelationship(ExtendedRelation extendedRelation) {
		removeRelationship(extendedRelation.getBaseTableName(),
				extendedRelation.getRelatedTableName(),
				extendedRelation.getRelationName());
	}

	/**
	 * Remove a specific relationship from the GeoPackage
	 * 
	 * @param baseTableName
	 *            base table name
	 * @param relatedTableName
	 *            related table name
	 * @param relationName
	 *            relation name
	 */
	public void removeRelationship(String baseTableName,
			String relatedTableName, String relationName) {

		try {
			if (extendedRelationsDao.isTableExists()) {
				Map<String, Object> fieldValues = new HashMap<String, Object>();
				fieldValues.put(ExtendedRelation.COLUMN_BASE_TABLE_NAME,
						baseTableName);
				fieldValues.put(ExtendedRelation.COLUMN_RELATED_TABLE_NAME,
						relatedTableName);
				fieldValues.put(ExtendedRelation.COLUMN_RELATION_NAME,
						relationName);
				List<ExtendedRelation> extendedRelations = extendedRelationsDao
						.queryForFieldValues(fieldValues);
				for (ExtendedRelation extendedRelation : extendedRelations) {
					geoPackage.deleteTable(extendedRelation
							.getMappingTableName());
				}
				extendedRelationsDao.delete(extendedRelations);
			}
		} catch (SQLException e) {
			throw new GeoPackageException("Failed to remove relationship '"
					+ relationName + "' between " + baseTableName + " and "
					+ relatedTableName, e);
		}

	}

	/**
	 * Remove all trace of the extension
	 */
	public void removeExtension() {

		try {
			if (extendedRelationsDao.isTableExists()) {
				List<ExtendedRelation> extendedRelations = extendedRelationsDao
						.queryForAll();
				for (ExtendedRelation extendedRelation : extendedRelations) {
					geoPackage.deleteTable(extendedRelation
							.getMappingTableName());
				}
				geoPackage.dropTable(extendedRelationsDao.getTableName());
			}
			if (extensionsDao.isTableExists()) {
				extensionsDao.deleteByExtension(EXTENSION_NAME);
			}
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to delete Related Tables extension and table. GeoPackage: "
							+ geoPackage.getName(), e);
		}
	}

	/**
	 * Build the custom relation name with author
	 * 
	 * @param author
	 *            relation author
	 * @param name
	 *            base relation name
	 * @return custom relation name
	 */
	public String buildRelationName(String author, String name) {
		return "x-" + author + "_" + name;
	}

}
