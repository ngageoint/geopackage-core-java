package mil.nga.geopackage.extension.contents;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Contents Id object, for maintaining a unique identifier for contents tables
 * 
 * @author osbornb
 * @since 3.1.1
 */
@DatabaseTable(tableName = "nga_contents_id", daoClass = ContentsIdDao.class)
public class ContentsId {

	/**
	 * Table name
	 */
	public static final String TABLE_NAME = "nga_contents_id";

	/**
	 * id field name
	 */
	public static final String COLUMN_ID = "id";

	/**
	 * tableName field name
	 */
	public static final String COLUMN_TABLE_NAME = "table_name";

	/**
	 * Id primary key
	 */
	@DatabaseField(columnName = COLUMN_ID, id = true, canBeNull = false)
	private long id;

	/**
	 * The name of the actual content table, foreign key to gpkg_contents
	 */
	@DatabaseField(columnName = COLUMN_TABLE_NAME, canBeNull = false, unique = true)
	private String tableName;

	/**
	 * Default Constructor
	 */
	public ContentsId() {

	}

	/**
	 * Copy Constructor
	 * 
	 * @param contentsId
	 *            contents id to copy
	 */
	public ContentsId(ContentsId contentsId) {
		id = contentsId.id;
		tableName = contentsId.tableName;
	}

	/**
	 * Get the id
	 * 
	 * @return id
	 */
	public long getId() {
		return id;
	}

	/**
	 * Set the id
	 * 
	 * @param id
	 *            id
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * Get the table name
	 * 
	 * @return table name
	 */
	public String getTableName() {
		return tableName;
	}

	/**
	 * Set the table name
	 * 
	 * @param tableName
	 *            table name
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

}
