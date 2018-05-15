package mil.nga.geopackage.extension.related_tables;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Describes the relationships between a base table, a related data table,
 * and a mapping table
 * 
 * @author jyutzler
 */
@DatabaseTable(tableName = "gpkgext_relations", daoClass = ExtendedRelationsDao.class)
public class ExtendedRelations {

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
	@DatabaseField(columnName = COLUMN_ID, id = true, canBeNull = false)
	private long id;

	/**
	 * Name of the table containing the base data (e.g., features) to relate
	 */
	@DatabaseField(columnName = COLUMN_BASE_TABLE_NAME, canBeNull = false)
	private String baseTableName;

	/**
	 * Name of the primary key column in base_table_name
	 */
	@DatabaseField(columnName = COLUMN_BASE_PRIMARY_COLUMN, canBeNull = false)
	private String basePrimaryColumn;

	/**
	 * Name of the table containing the related information
	 */
	@DatabaseField(columnName = COLUMN_RELATED_TABLE_NAME, canBeNull = false)
	private String relatedTableName;

	/**
	 * Name of the primary key column in related_table_name
	 */
	@DatabaseField(columnName = COLUMN_RELATED_PRIMARY_COLUMN, canBeNull = false)
	private String relatedPrimaryColumn;

	/**
	 * Name of the relation
	 */
	@DatabaseField(columnName = COLUMN_RELATION_NAME, canBeNull = false)
	private String relationName;

	/**
	 * Name of a mapping table
	 */
	@DatabaseField(columnName = COLUMN_MAPPING_TABLE_NAME, canBeNull = false)
	private String mappingTableName;

	/**
	 * Default Constructor
	 */
	public ExtendedRelations() {

	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getBaseTableName() {
		return baseTableName;
	}

	public void setBaseTableName(String baseTableName) {
		this.baseTableName = baseTableName;
	}

	public String getBasePrimaryColumn() {
		return basePrimaryColumn;
	}

	public void setBasePrimaryColumn(String basePrimaryColumn) {
		this.basePrimaryColumn = basePrimaryColumn;
	}

	public String getRelatedTableName() {
		return relatedTableName;
	}

	public void setRelatedTableName(String relatedTableName) {
		this.relatedTableName = relatedTableName;
	}

	public String getRelatedPrimaryColumn() {
		return relatedPrimaryColumn;
	}

	public void setRelatedPrimaryColumn(String relatedPrimaryColumn) {
		this.relatedPrimaryColumn = relatedPrimaryColumn;
	}

	public String getRelationName() {
		return relationName;
	}

	public void setRelationName(String relationName) {
		this.relationName = relationName;
	}

	public String getMappingTableName() {
		return mappingTableName;
	}

	public void setMappingTableName(String mappingTableName) {
		this.mappingTableName = mappingTableName;
	}

}
