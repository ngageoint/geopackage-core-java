package mil.nga.geopackage.extension.nga.contents;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import mil.nga.geopackage.contents.Contents;

/**
 * Contents Id object, for maintaining a unique identifier for contents tables
 * 
 * @author osbornb
 * @since 3.2.0
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
	@DatabaseField(columnName = COLUMN_ID, generatedId = true, canBeNull = false)
	private long id;

	/**
	 * Foreign key to Contents by table name
	 */
	@DatabaseField(columnName = COLUMN_TABLE_NAME, canBeNull = false, unique = true, foreign = true, foreignAutoRefresh = true)
	private Contents contents;

	/**
	 * The name of the actual content table, foreign key to gpkg_contents
	 */
	@DatabaseField(columnName = COLUMN_TABLE_NAME, canBeNull = false, unique = true, readOnly = true)
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
		contents = contentsId.contents;
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
	 * Get the contents
	 * 
	 * @return contents
	 */
	public Contents getContents() {
		return contents;
	}

	/**
	 * Set the contents
	 * 
	 * @param contents
	 *            contents
	 */
	public void setContents(Contents contents) {
		this.contents = contents;
		if (contents != null) {
			this.tableName = contents.getId();
		} else {
			this.tableName = null;
		}
	}

	/**
	 * Get the table name
	 * 
	 * @return table name
	 */
	public String getTableName() {
		return tableName;
	}

}
