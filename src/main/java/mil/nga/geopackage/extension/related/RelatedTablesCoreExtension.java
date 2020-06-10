package mil.nga.geopackage.extension.related;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import mil.nga.geopackage.GeoPackageConstants;
import mil.nga.geopackage.GeoPackageCore;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.attributes.AttributesTable;
import mil.nga.geopackage.contents.Contents;
import mil.nga.geopackage.contents.ContentsDao;
import mil.nga.geopackage.db.GeoPackageCoreConnection;
import mil.nga.geopackage.extension.BaseExtension;
import mil.nga.geopackage.extension.ExtensionScopeType;
import mil.nga.geopackage.extension.Extensions;
import mil.nga.geopackage.extension.related.media.MediaTable;
import mil.nga.geopackage.extension.related.simple.SimpleAttributesTable;
import mil.nga.geopackage.property.GeoPackageProperties;
import mil.nga.geopackage.property.PropertyConstants;
import mil.nga.geopackage.tiles.user.TileTable;
import mil.nga.geopackage.user.UserColumn;
import mil.nga.geopackage.user.UserTable;
import mil.nga.geopackage.user.custom.UserCustomColumn;
import mil.nga.geopackage.user.custom.UserCustomTable;
import mil.nga.geopackage.user.custom.UserCustomTableReader;

/**
 * Related Tables core extension
 * 
 * http://docs.opengeospatial.org/is/18-000/18-000.html
 * 
 * @author jyutzler
 * @author osbornb
 * @since 3.0.1
 */
public abstract class RelatedTablesCoreExtension extends BaseExtension {

	/**
	 * Extension author
	 */
	public static final String EXTENSION_AUTHOR = GeoPackageConstants.EXTENSION_AUTHOR;

	/**
	 * Extension name without the author
	 */
	public static final String EXTENSION_NAME_NO_AUTHOR = "related_tables";

	/**
	 * Extension, with author and name
	 */
	public static final String EXTENSION_NAME = Extensions
			.buildExtensionName(EXTENSION_AUTHOR, EXTENSION_NAME_NO_AUTHOR);

	/**
	 * Extension definition URL
	 */
	public static final String EXTENSION_DEFINITION = GeoPackageProperties
			.getProperty(PropertyConstants.EXTENSIONS,
					EXTENSION_NAME_NO_AUTHOR);

	/**
	 * Extended Relations DAO
	 */
	private ExtendedRelationsDao extendedRelationsDao;

	/**
	 * Constructor
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * 
	 */
	protected RelatedTablesCoreExtension(GeoPackageCore geoPackage) {
		super(geoPackage);
		extendedRelationsDao = getExtendedRelationsDao();
	}

	/**
	 * Get or create the extension
	 * 
	 * @return extension
	 */
	private Extensions getOrCreate() {

		// Create table
		createExtendedRelationsTable();

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
		return has(EXTENSION_NAME, ExtendedRelation.TABLE_NAME, null)
				&& geoPackage.isTable(ExtendedRelation.TABLE_NAME);
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
	 * Get a Extended Relations DAO
	 * 
	 * @return extended relations dao
	 */
	public ExtendedRelationsDao getExtendedRelationsDao() {
		if (extendedRelationsDao == null) {
			extendedRelationsDao = getExtendedRelationsDao(geoPackage);
		}
		return extendedRelationsDao;
	}

	/**
	 * Get a Extended Relations DAO
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * @return extended relations dao
	 * @since 4.0.0
	 */
	public static ExtendedRelationsDao getExtendedRelationsDao(
			GeoPackageCore geoPackage) {
		return ExtendedRelationsDao.create(geoPackage);
	}

	/**
	 * Get a Extended Relations DAO
	 * 
	 * @param db
	 *            database connection
	 * @return extended relations dao
	 * @since 4.0.0
	 */
	public static ExtendedRelationsDao getExtendedRelationsDao(
			GeoPackageCoreConnection db) {
		return ExtendedRelationsDao.create(db);
	}

	/**
	 * Create the Extended Relations Table if it does not exist
	 * 
	 * @return true if created
	 * @since 4.0.0
	 */
	public boolean createExtendedRelationsTable() {
		verifyWritable();

		boolean created = false;

		try {
			if (!extendedRelationsDao.isTableExists()) {
				created = geoPackage.getTableCreator()
						.createExtendedRelations() > 0;
			}
		} catch (SQLException e) {
			throw new GeoPackageException("Failed to check if "
					+ ExtendedRelation.class.getSimpleName()
					+ " table exists and create it", e);
		}

		return created;
	}

	/**
	 * Get the primary key of a table
	 * 
	 * @param tableName
	 *            table name
	 * @return the column name
	 */
	public String getPrimaryKeyColumnName(String tableName) {
		UserCustomTable table = UserCustomTableReader
				.readTable(geoPackage.getDatabase(), tableName);
		UserCustomColumn pkColumn = table.getPkColumn();
		if (pkColumn == null) {
			throw new GeoPackageException(
					"Found no primary key for table " + tableName);
		}
		return pkColumn.getName();
	}

	/**
	 * Set the contents in the user table
	 * 
	 * @param table
	 *            user table
	 */
	public void setContents(UserTable<? extends UserColumn> table) {
		ContentsDao dao = geoPackage.getContentsDao();
		Contents contents = null;
		try {
			contents = dao.queryForId(table.getTableName());
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to retrieve " + Contents.class.getSimpleName()
							+ " for table name: " + table.getTableName(),
					e);
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
		extendedRelation.setRelatedPrimaryColumn(
				getPrimaryKeyColumnName(relatedTableName));
		extendedRelation.setMappingTableName(userMappingTable.getTableName());
		extendedRelation.setRelationName(relationName);
		try {
			extendedRelationsDao.create(extendedRelation);
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to add relationship '" + relationName + "' between "
							+ baseTableName + " and " + relatedTableName,
					e);
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
		return addRelationship(baseTableName, relatedTable,
				relatedTable.getRelationName(), mappingTableName);
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
		return addRelationship(baseTableName, relatedTable,
				relatedTable.getRelationName(), userMappingTable);
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
	 * @since 3.2.0
	 */
	public ExtendedRelation addRelationship(String baseTableName,
			UserTable<? extends UserColumn> relatedTable,
			String mappingTableName) {
		return addRelationship(baseTableName, relatedTable,
				relatedTable.getDataType(), mappingTableName);
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
	 * @since 3.2.0
	 */
	public ExtendedRelation addRelationship(String baseTableName,
			UserTable<? extends UserColumn> relatedTable,
			UserMappingTable userMappingTable) {
		return addRelationship(baseTableName, relatedTable,
				relatedTable.getDataType(), userMappingTable);
	}

	/**
	 * Adds a relationship between the base and user related table. Creates a
	 * default user mapping table and the related table if needed.
	 * 
	 * @param baseTableName
	 *            base table name
	 * @param relatedTable
	 *            user related table
	 * @param relationName
	 *            relation name
	 * @param mappingTableName
	 *            user mapping table name
	 * @return The relationship that was added
	 * @since 3.2.0
	 */
	public ExtendedRelation addRelationship(String baseTableName,
			UserTable<? extends UserColumn> relatedTable, String relationName,
			String mappingTableName) {

		UserMappingTable userMappingTable = UserMappingTable
				.create(mappingTableName);

		return addRelationship(baseTableName, relatedTable, relationName,
				userMappingTable);
	}

	/**
	 * Adds a relationship between the base and user related table. Creates the
	 * user mapping table and related table if needed.
	 * 
	 * @param baseTableName
	 *            base table name
	 * @param relatedTable
	 *            user related table
	 * @param relationName
	 *            relation name
	 * @param userMappingTable
	 *            user mapping table
	 * @return The relationship that was added
	 * @since 3.2.0
	 */
	public ExtendedRelation addRelationship(String baseTableName,
			UserTable<? extends UserColumn> relatedTable, String relationName,
			UserMappingTable userMappingTable) {

		// Create the related table if needed
		createRelatedTable(relatedTable);

		return addRelationship(baseTableName, relatedTable.getTableName(),
				userMappingTable, relationName);
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
	 * Adds an attributes relationship between the base table and related
	 * attributes table. Creates a default user mapping table if needed.
	 * 
	 * @param baseTableName
	 *            base table name
	 * @param relatedAttributesTableName
	 *            related attributes table name
	 * @param mappingTableName
	 *            mapping table name
	 * @return The relationship that was added
	 * @since 3.2.0
	 */
	public ExtendedRelation addAttributesRelationship(String baseTableName,
			String relatedAttributesTableName, String mappingTableName) {
		return addRelationship(baseTableName, relatedAttributesTableName,
				mappingTableName, RelationType.ATTRIBUTES);
	}

	/**
	 * Adds an attributes relationship between the base table and related
	 * attributes table. Creates the user mapping table if needed.
	 * 
	 * @param baseTableName
	 *            base table name
	 * @param relatedAttributesTableName
	 *            related attributes table name
	 * @param userMappingTable
	 *            user mapping table
	 * @return The relationship that was added
	 * @since 3.2.0
	 */
	public ExtendedRelation addAttributesRelationship(String baseTableName,
			String relatedAttributesTableName,
			UserMappingTable userMappingTable) {
		return addRelationship(baseTableName, relatedAttributesTableName,
				userMappingTable, RelationType.ATTRIBUTES);
	}

	/**
	 * Adds an attributes relationship between the base table and user
	 * attributes related table. Creates a default user mapping table and the
	 * attributes table if needed.
	 * 
	 * @param baseTableName
	 *            base table name
	 * @param attributesTable
	 *            user attributes table
	 * @param mappingTableName
	 *            user mapping table name
	 * @return The relationship that was added
	 * @since 3.2.0
	 */
	public ExtendedRelation addAttributesRelationship(String baseTableName,
			AttributesTable attributesTable, String mappingTableName) {
		return addRelationship(baseTableName, attributesTable,
				mappingTableName);
	}

	/**
	 * Adds an attributes relationship between the base table and user
	 * attributes related table. Creates the user mapping table and an
	 * attributes table if needed.
	 * 
	 * @param baseTableName
	 *            base table name
	 * @param attributesTable
	 *            user attributes table
	 * @param userMappingTable
	 *            user mapping table
	 * @return The relationship that was added
	 * @since 3.2.0
	 */
	public ExtendedRelation addAttributesRelationship(String baseTableName,
			AttributesTable attributesTable,
			UserMappingTable userMappingTable) {
		return addRelationship(baseTableName, attributesTable,
				userMappingTable);
	}

	/**
	 * Adds a tiles relationship between the base table and related tiles table.
	 * Creates a default user mapping table if needed.
	 * 
	 * @param baseTableName
	 *            base table name
	 * @param relatedTilesTableName
	 *            related tiles table name
	 * @param mappingTableName
	 *            mapping table name
	 * @return The relationship that was added
	 * @since 3.2.0
	 */
	public ExtendedRelation addTilesRelationship(String baseTableName,
			String relatedTilesTableName, String mappingTableName) {
		return addRelationship(baseTableName, relatedTilesTableName,
				mappingTableName, RelationType.TILES);
	}

	/**
	 * Adds a tiles relationship between the base table and related tiles table.
	 * Creates the user mapping table if needed.
	 * 
	 * @param baseTableName
	 *            base table name
	 * @param relatedTilesTableName
	 *            related tiles table name
	 * @param userMappingTable
	 *            user mapping table
	 * @return The relationship that was added
	 * @since 3.2.0
	 */
	public ExtendedRelation addTilesRelationship(String baseTableName,
			String relatedTilesTableName, UserMappingTable userMappingTable) {
		return addRelationship(baseTableName, relatedTilesTableName,
				userMappingTable, RelationType.TILES);
	}

	/**
	 * Adds a tiles relationship between the base table and user tiles related
	 * table. Creates a default user mapping table and the tile table if needed.
	 * 
	 * @param baseTableName
	 *            base table name
	 * @param tileTable
	 *            user tile table
	 * @param mappingTableName
	 *            user mapping table name
	 * @return The relationship that was added
	 * @since 3.2.0
	 */
	public ExtendedRelation addTilesRelationship(String baseTableName,
			TileTable tileTable, String mappingTableName) {
		return addRelationship(baseTableName, tileTable, mappingTableName);
	}

	/**
	 * Adds a tiles relationship between the base table and user tiles related
	 * table. Creates the user mapping table and a tile table if needed.
	 * 
	 * @param baseTableName
	 *            base table name
	 * @param tileTable
	 *            user tile table
	 * @param userMappingTable
	 *            user mapping table
	 * @return The relationship that was added
	 * @since 3.2.0
	 */
	public ExtendedRelation addTilesRelationship(String baseTableName,
			TileTable tileTable, UserMappingTable userMappingTable) {
		return addRelationship(baseTableName, tileTable, userMappingTable);
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
		if (!geoPackage.isTableOrView(baseTableName)) {
			throw new GeoPackageException(
					"Base Relationship table does not exist: " + baseTableName
							+ ", Relation: " + relationName);
		}
		if (!geoPackage.isTableOrView(relatedTableName)) {
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

			if (!geoPackage.isTableType(relatedTableName,
					relationType.getDataType())) {
				throw new GeoPackageException("The related table must be a "
						+ relationType.getDataType() + " table. Related Table: "
						+ relatedTableName + ", Type: "
						+ geoPackage.getTableType(relatedTableName));
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

		if (!geoPackage.isTableOrView(userMappingTableName)) {

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
	 * @since 3.2.0
	 */
	public boolean createRelatedTable(
			UserTable<? extends UserColumn> relatedTable) {

		boolean created = false;

		String relatedTableName = relatedTable.getTableName();
		if (!geoPackage.isTableOrView(relatedTableName)) {

			geoPackage.createUserTable(relatedTable);

			try {
				// Create the contents
				Contents contents = new Contents();
				contents.setTableName(relatedTableName);
				contents.setDataTypeName(relatedTable.getDataType());
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
								+ relatedTableName,
						e);
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
			String relatedTableName, String relationAuthor,
			String relationName) {
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

		try {
			if (extendedRelationsDao.isTableExists()) {
				geoPackage.deleteTable(extendedRelation.getMappingTableName());
				extendedRelationsDao.delete(extendedRelation);
			}
		} catch (SQLException e) {
			throw new GeoPackageException("Failed to remove relationship '"
					+ extendedRelation.getRelationName() + "' between "
					+ extendedRelation.getBaseTableName() + " and "
					+ extendedRelation.getRelatedTableName()
					+ " with mapping table "
					+ extendedRelation.getMappingTableName(), e);
		}
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
				List<ExtendedRelation> extendedRelations = getRelations(
						baseTableName, relatedTableName, relationName, null);
				for (ExtendedRelation extendedRelation : extendedRelations) {
					removeRelationship(extendedRelation);
				}
			}
		} catch (SQLException e) {
			throw new GeoPackageException("Failed to remove relationship '"
					+ relationName + "' between " + baseTableName + " and "
					+ relatedTableName, e);
		}

	}

	/**
	 * Remove all relationships that include the table
	 * 
	 * @param table
	 *            base or related table name
	 * @since 3.2.0
	 */
	public void removeRelationships(String table) {
		try {
			if (extendedRelationsDao.isTableExists()) {
				List<ExtendedRelation> extendedRelations = extendedRelationsDao
						.getTableRelations(table);
				for (ExtendedRelation extendedRelation : extendedRelations) {
					removeRelationship(extendedRelation);
				}
			}
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to remove relationships for table: " + table, e);
		}
	}

	/**
	 * Remove all relationships with the mapping table
	 * 
	 * @param mappingTable
	 *            mapping table
	 * @since 3.2.0
	 */
	public void removeRelationshipsWithMappingTable(String mappingTable) {
		try {
			if (extendedRelationsDao.isTableExists()) {
				List<ExtendedRelation> extendedRelations = getRelations(null,
						null, mappingTable);
				for (ExtendedRelation extendedRelation : extendedRelations) {
					removeRelationship(extendedRelation);
				}
			}
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to remove relationships for mapping table: "
							+ mappingTable,
					e);
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
					geoPackage.deleteTable(
							extendedRelation.getMappingTableName());
				}
				geoPackage.dropTable(extendedRelationsDao.getTableName());
			}
			if (extensionsDao.isTableExists()) {
				extensionsDao.deleteByExtension(EXTENSION_NAME);
			}
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to delete Related Tables extension and table. GeoPackage: "
							+ geoPackage.getName(),
					e);
		}
	}

	/**
	 * Determine if has one or more relations matching the base table and
	 * related table
	 * 
	 * @param baseTable
	 *            base table name
	 * @param relatedTable
	 *            related table name
	 * @return true if has relations
	 * @throws SQLException
	 *             upon failure
	 * @since 3.2.0
	 */
	public boolean hasRelations(String baseTable, String relatedTable)
			throws SQLException {
		return hasRelations(baseTable, null, relatedTable, null, null, null);
	}

	/**
	 * Get the relations to the base table and related table
	 * 
	 * @param baseTable
	 *            base table name
	 * @param relatedTable
	 *            related table name
	 * @return extended relations
	 * @since 3.2.0
	 */
	public List<ExtendedRelation> getRelations(String baseTable,
			String relatedTable) {
		return getRelations(baseTable, null, relatedTable, null, null, null);
	}

	/**
	 * Determine if has one or more relations matching the non null provided
	 * values
	 * 
	 * @param baseTable
	 *            base table name
	 * @param relatedTable
	 *            related table name
	 * @param mappingTable
	 *            mapping table name
	 * @return true if has relations
	 * @throws SQLException
	 *             upon failure
	 * @since 3.2.0
	 */
	public boolean hasRelations(String baseTable, String relatedTable,
			String mappingTable) throws SQLException {
		return hasRelations(baseTable, null, relatedTable, null, null,
				mappingTable);
	}

	/**
	 * Get the relations matching the non null provided values
	 * 
	 * @param baseTable
	 *            base table name
	 * @param relatedTable
	 *            related table name
	 * @param mappingTable
	 *            mapping table name
	 * @return extended relations
	 * @throws SQLException
	 *             upon failure
	 * @since 3.2.0
	 */
	public List<ExtendedRelation> getRelations(String baseTable,
			String relatedTable, String mappingTable) throws SQLException {
		return getRelations(baseTable, null, relatedTable, null, null,
				mappingTable);
	}

	/**
	 * Determine if has one or more relations matching the non null provided
	 * values
	 * 
	 * @param baseTable
	 *            base table name
	 * @param relatedTable
	 *            related table name
	 * @param relation
	 *            relation name
	 * @param mappingTable
	 *            mapping table name
	 * @return true if has relations
	 * @since 3.2.0
	 */
	public boolean hasRelations(String baseTable, String relatedTable,
			String relation, String mappingTable) {
		return hasRelations(baseTable, null, relatedTable, null, relation,
				mappingTable);
	}

	/**
	 * Get the relations matching the non null provided values
	 * 
	 * @param baseTable
	 *            base table name
	 * @param relatedTable
	 *            related table name
	 * @param relation
	 *            relation name
	 * @param mappingTable
	 *            mapping table name
	 * @return extended relations
	 * @throws SQLException
	 *             upon failure
	 * @since 3.2.0
	 */
	public List<ExtendedRelation> getRelations(String baseTable,
			String relatedTable, String relation, String mappingTable)
			throws SQLException {
		return getRelations(baseTable, null, relatedTable, null, relation,
				mappingTable);
	}

	/**
	 * Determine if has one or more relations matching the non null provided
	 * values
	 * 
	 * @param baseTable
	 *            base table name
	 * @param baseColumn
	 *            base primary column name
	 * @param relatedTable
	 *            related table name
	 * @param relatedColumn
	 *            related primary column name
	 * @param relation
	 *            relation name
	 * @param mappingTable
	 *            mapping table name
	 * @return true if has relations
	 * @since 3.2.0
	 */
	public boolean hasRelations(String baseTable, String baseColumn,
			String relatedTable, String relatedColumn, String relation,
			String mappingTable) {
		return !getRelations(baseTable, baseColumn, relatedTable, relatedColumn,
				relation, mappingTable).isEmpty();
	}

	/**
	 * Get the relations matching the non null provided values
	 * 
	 * @param baseTable
	 *            base table name
	 * @param baseColumn
	 *            base primary column name
	 * @param relatedTable
	 *            related table name
	 * @param relatedColumn
	 *            related primary column name
	 * @param relation
	 *            relation name
	 * @param mappingTable
	 *            mapping table name
	 * @return extended relations
	 * @since 3.2.0
	 */
	public List<ExtendedRelation> getRelations(String baseTable,
			String baseColumn, String relatedTable, String relatedColumn,
			String relation, String mappingTable) {

		List<ExtendedRelation> relations = null;

		try {
			if (extendedRelationsDao.isTableExists()) {
				relations = extendedRelationsDao.getRelations(baseTable,
						baseColumn, relatedTable, relatedColumn, relation,
						mappingTable);
			} else {
				relations = new ArrayList<>();
			}
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to get relationships. Base Table: " + baseTable
							+ ", Base Column: " + baseColumn
							+ ", Related Table: " + relatedTable
							+ ", Related Column: " + relatedColumn
							+ ", Relation: " + relation + ", Mapping Table: "
							+ mappingTable,
					e);
		}

		return relations;
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
