package mil.nga.geopackage.extension.nga.index;

import java.util.Date;

import mil.nga.geopackage.persister.DatePersister;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Table Index object, for indexing data within user tables
 * 
 * @author osbornb
 * @since 1.1.0
 */
@DatabaseTable(tableName = "nga_table_index", daoClass = TableIndexDao.class)
public class TableIndex {

	/**
	 * Table name
	 */
	public static final String TABLE_NAME = "nga_table_index";

	/**
	 * tableName field name
	 */
	public static final String COLUMN_TABLE_NAME = "table_name";

	/**
	 * Last indexed column
	 */
	public static final String COLUMN_LAST_INDEXED = "last_indexed";

	/**
	 * Name of the table
	 */
	@DatabaseField(columnName = COLUMN_TABLE_NAME, id = true, canBeNull = false)
	private String tableName;

	/**
	 * Last indexed date
	 */
	@DatabaseField(columnName = COLUMN_LAST_INDEXED, persisterClass = DatePersister.class)
	private Date lastIndexed;

	/**
	 * Geometry Indices
	 */
	@ForeignCollectionField(eager = false)
	private ForeignCollection<GeometryIndex> geometryIndices;

	/**
	 * Default Constructor
	 */
	public TableIndex() {

	}

	/**
	 * Copy Constructor
	 * 
	 * @param tableIndex
	 *            table index to copy
	 * @since 1.3.0
	 */
	public TableIndex(TableIndex tableIndex) {
		tableName = tableIndex.tableName;
		lastIndexed = new Date(tableIndex.lastIndexed.getTime());
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

	/**
	 * Get the last indexed date
	 * 
	 * @return last indexed date
	 */
	public Date getLastIndexed() {
		return lastIndexed;
	}

	/**
	 * Set the last indexed date
	 * 
	 * @param lastIndexed
	 *            last indexed date
	 */
	public void setLastIndexed(Date lastIndexed) {
		this.lastIndexed = lastIndexed;
	}

	/**
	 * Get the Geometry Indices
	 * 
	 * @return collection of geometry indices
	 */
	public ForeignCollection<GeometryIndex> getGeometryIndices() {
		return geometryIndices;
	}

}
