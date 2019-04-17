package mil.nga.geopackage.extension.related;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Describes the relationships between a base table, a related data table, and a
 * mapping table
 * 
 * @author jyutzler
 * @author osbornb
 * @since 3.0.1
 */
@DatabaseTable(tableName = "gpkgext_relations", daoClass = ExtendedRelationsDao.class)
public class ExtendedRelation {

	/**
	 * Table name
	 */
	public static final String TABLE_NAME = "gpkgext_relations";

	/**
	 * id field name
	 */
	public static final String COLUMN_ID = "id";

	/**
	 * base_table_name field name
	 */
	public static final String COLUMN_BASE_TABLE_NAME = "base_table_name";

	/**
	 * base_primary_column field name
	 */
	public static final String COLUMN_BASE_PRIMARY_COLUMN = "base_primary_column";

	/**
	 * related_table_name field name
	 */
	public static final String COLUMN_RELATED_TABLE_NAME = "related_table_name";

	/**
	 * related_primary_column field name
	 */
	public static final String COLUMN_RELATED_PRIMARY_COLUMN = "related_primary_column";

	/**
	 * relation_name field name
	 */
	public static final String COLUMN_RELATION_NAME = "relation_name";

	/**
	 * mapping_table_name field name
	 */
	public static final String COLUMN_MAPPING_TABLE_NAME = "mapping_table_name";

	/**
	 * Extended Relations primary key
	 */
	@DatabaseField(columnName = COLUMN_ID, generatedId = true, canBeNull = false)
	private long id;

	/**
	 * Name of the table containing the base data (e.g., features) to relate
	 */
	@DatabaseField(columnName = COLUMN_BASE_TABLE_NAME, canBeNull = false)
	private String baseTableName;

	/**
	 * Name of the primary key column in base_table_name
	 */
	@DatabaseField(columnName = COLUMN_BASE_PRIMARY_COLUMN, canBeNull = false, defaultValue = "id")
	private String basePrimaryColumn;

	/**
	 * Name of the table containing the related information
	 */
	@DatabaseField(columnName = COLUMN_RELATED_TABLE_NAME, canBeNull = false)
	private String relatedTableName;

	/**
	 * Name of the primary key column in related_table_name
	 */
	@DatabaseField(columnName = COLUMN_RELATED_PRIMARY_COLUMN, canBeNull = false, defaultValue = "id")
	private String relatedPrimaryColumn;

	/**
	 * Name of the relation
	 */
	@DatabaseField(columnName = COLUMN_RELATION_NAME, canBeNull = false)
	private String relationName;

	/**
	 * Name of a mapping table
	 */
	@DatabaseField(columnName = COLUMN_MAPPING_TABLE_NAME, canBeNull = false, unique = true)
	private String mappingTableName;

	/**
	 * Default Constructor
	 */
	public ExtendedRelation() {

	}

	/**
	 * Copy Constructor
	 * 
	 * @param extendedRelation
	 *            extended relation to copy
	 */
	public ExtendedRelation(ExtendedRelation extendedRelation) {
		id = extendedRelation.id;
		baseTableName = extendedRelation.baseTableName;
		basePrimaryColumn = extendedRelation.basePrimaryColumn;
		relatedTableName = extendedRelation.relatedTableName;
		relatedPrimaryColumn = extendedRelation.relatedPrimaryColumn;
		relationName = extendedRelation.relationName;
		mappingTableName = extendedRelation.mappingTableName;
	}

	/**
	 * Getter
	 * 
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * Setter
	 * 
	 * @param id
	 *            id
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * Reset the id so the row can be inserted as new
	 */
	public void resetId() {
		id = 0;
	}

	/**
	 * Getter
	 * 
	 * @return the base table name
	 */
	public String getBaseTableName() {
		return baseTableName;
	}

	/**
	 * Setter
	 * 
	 * @param baseTableName
	 *            base table name
	 */
	public void setBaseTableName(String baseTableName) {
		this.baseTableName = baseTableName;
	}

	/**
	 * Getter
	 * 
	 * @return the name of the primary column of the base table
	 */
	public String getBasePrimaryColumn() {
		return basePrimaryColumn;
	}

	/**
	 * Setter
	 * 
	 * @param basePrimaryColumn
	 *            base primary column
	 */
	public void setBasePrimaryColumn(String basePrimaryColumn) {
		this.basePrimaryColumn = basePrimaryColumn;
	}

	/**
	 * Getter
	 * 
	 * @return the name of the related table
	 */
	public String getRelatedTableName() {
		return relatedTableName;
	}

	/**
	 * Setter
	 * 
	 * @param relatedTableName
	 *            related table name
	 */
	public void setRelatedTableName(String relatedTableName) {
		this.relatedTableName = relatedTableName;
	}

	/**
	 * Getter
	 * 
	 * @return the name of the primary column of the related table
	 */
	public String getRelatedPrimaryColumn() {
		return relatedPrimaryColumn;
	}

	/**
	 * Setter
	 * 
	 * @param relatedPrimaryColumn
	 *            related primary column
	 */
	public void setRelatedPrimaryColumn(String relatedPrimaryColumn) {
		this.relatedPrimaryColumn = relatedPrimaryColumn;
	}

	/**
	 * Getter
	 * 
	 * @return the relation name
	 */
	public String getRelationName() {
		return relationName;
	}

	/**
	 * Get the relation type for pre-known types
	 * 
	 * @return relation type or null
	 */
	public RelationType getRelationType() {
		return RelationType.fromName(getRelationName());
	}

	/**
	 * Setter
	 * 
	 * @param relationName
	 *            relation name
	 */
	public void setRelationName(String relationName) {
		this.relationName = relationName;
	}

	/**
	 * Getter
	 * 
	 * @return the mapping table name
	 */
	public String getMappingTableName() {
		return mappingTableName;
	}

	/**
	 * Setter
	 * 
	 * @param mappingTableName
	 *            mapping table name
	 */
	public void setMappingTableName(String mappingTableName) {
		this.mappingTableName = mappingTableName;
	}

}
