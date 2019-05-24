package mil.nga.geopackage.db.table;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.db.CoreSQLUtils;
import mil.nga.geopackage.db.DateConverter;
import mil.nga.geopackage.db.GeoPackageCoreConnection;
import mil.nga.geopackage.db.GeoPackageDataType;
import mil.nga.sf.GeometryType;

/**
 * Table Info queries (table_info)
 * 
 * @author osbornb
 * @since 3.3.0
 */
public class TableInfo {

	/**
	 * Logger
	 */
	private static final Logger logger = Logger
			.getLogger(TableInfo.class.getName());

	/**
	 * Index column
	 */
	public static final String CID = "cid";

	/**
	 * Index column index
	 */
	public static final int CID_INDEX = 0;

	/**
	 * Name column
	 */
	public static final String NAME = "name";

	/**
	 * Name column index
	 */
	public static final int NAME_INDEX = 1;

	/**
	 * Type column
	 */
	public static final String TYPE = "type";

	/**
	 * Type column index
	 */
	public static final int TYPE_INDEX = 2;

	/**
	 * Not null column
	 */
	public static final String NOT_NULL = "notnull";

	/**
	 * Not null column index
	 */
	public static final int NOT_NULL_INDEX = 3;

	/**
	 * Default value column
	 */
	public static final String DFLT_VALUE = "dflt_value";

	/**
	 * Default value column index
	 */
	public static final int DFLT_VALUE_INDEX = 4;

	/**
	 * Primary key column
	 */
	public static final String PK = "pk";

	/**
	 * Primary key column index
	 */
	public static final int PK_INDEX = 5;

	/**
	 * Default of NULL value
	 */
	public static final String DEFAULT_NULL = "NULL";

	/**
	 * Table name
	 */
	private final String tableName;

	/**
	 * Table columns
	 */
	private final List<TableColumn> columns;

	/**
	 * Column name to column mapping
	 */
	private final Map<String, TableColumn> namesToColumns = new HashMap<>();

	/**
	 * Primary key column names
	 */
	private final List<TableColumn> primaryKeys = new ArrayList<>();

	/**
	 * Constructor
	 * 
	 * @param tableName
	 *            table name
	 * @param columns
	 *            table columns
	 */
	private TableInfo(String tableName, List<TableColumn> columns) {
		this.tableName = tableName;
		this.columns = columns;
		for (TableColumn column : columns) {
			namesToColumns.put(column.getName(), column);
			if (column.isPrimarykey()) {
				primaryKeys.add(column);
			}
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

	/**
	 * Number of columns
	 * 
	 * @return column count
	 */
	public int numColumns() {
		return columns.size();
	}

	/**
	 * Get the columns
	 * 
	 * @return columns
	 */
	public List<TableColumn> getColumns() {
		return Collections.unmodifiableList(columns);
	}

	/**
	 * Get the column at the index
	 * 
	 * @param index
	 *            column index
	 * @return column
	 */
	public TableColumn getColumn(int index) {
		if (index < 0 || index >= columns.size()) {
			throw new IndexOutOfBoundsException("Column index: " + index
					+ ", not within range 0 to " + (columns.size() - 1));
		}
		return columns.get(index);
	}

	/**
	 * Check if the table has the column
	 * 
	 * @param name
	 *            column name
	 * @return true if has column
	 */
	public boolean hasColumn(String name) {
		return getColumn(name) != null;
	}

	/**
	 * Get the column with the name
	 * 
	 * @param name
	 *            column name
	 * @return column or null if does not exist
	 */
	public TableColumn getColumn(String name) {
		return namesToColumns.get(name);
	}

	/**
	 * Check if the table has one or more primary keys
	 * 
	 * @return true if has at least one primary key
	 */
	public boolean hasPrimaryKey() {
		return !primaryKeys.isEmpty();
	}

	/**
	 * Get the primary key columns
	 * 
	 * @return primary key columns
	 */
	public List<TableColumn> getPrimaryKeys() {
		return Collections.unmodifiableList(primaryKeys);
	}

	/**
	 * Get the single or first primary key if one exists
	 * 
	 * @return single or first primary key, null if no primary key
	 */
	public TableColumn getPrimaryKey() {
		TableColumn pk = null;
		if (hasPrimaryKey()) {
			pk = primaryKeys.get(0);
		}
		return pk;
	}

	/**
	 * Query for the table_info of the table name
	 * 
	 * @param db
	 *            connection
	 * @param tableName
	 *            table name
	 * @return table info or null if no table
	 */
	public static TableInfo info(GeoPackageCoreConnection db,
			String tableName) {

		String sql = "PRAGMA table_info(" + CoreSQLUtils.quoteWrap(tableName)
				+ ")";

		List<List<Object>> results = db.queryResults(sql, null);

		List<TableColumn> tableColumns = new ArrayList<>();

		for (List<Object> column : results) {

			int index = ((Number) column.get(CID_INDEX)).intValue();
			String name = (String) column.get(NAME_INDEX);
			String type = (String) column.get(TYPE_INDEX);
			boolean notNull = ((Number) column.get(NOT_NULL_INDEX))
					.intValue() == 1;
			String defaultValueString = (String) column.get(DFLT_VALUE_INDEX);
			boolean primaryKey = ((Number) column.get(PK_INDEX))
					.intValue() == 1;

			// If the type has a max limit on it, pull it off
			Long max = null;
			if (type != null && type.endsWith(")")) {
				int maxStart = type.indexOf("(");
				if (maxStart > -1) {
					String maxString = type.substring(maxStart + 1,
							type.length() - 1);
					if (!maxString.isEmpty()) {
						try {
							max = Long.valueOf(maxString);
							type = type.substring(0, maxStart);
						} catch (NumberFormatException e) {
							logger.log(Level.WARNING,
									"Failed to parse type max from type: "
											+ type,
									e);
						}
					}
				}
			}

			GeoPackageDataType dataType = getDataType(type);
			Object defaultValue = getDefaultValue(defaultValueString, dataType);

			TableColumn tableColumn = new TableColumn(index, name, type,
					dataType, max, notNull, defaultValueString, defaultValue,
					primaryKey);
			tableColumns.add(tableColumn);
		}

		TableInfo tableInfo = null;
		if (!tableColumns.isEmpty()) {
			tableInfo = new TableInfo(tableName, tableColumns);
		}

		return tableInfo;
	}

	/**
	 * Get the data type from the type value
	 * 
	 * @param type
	 *            type value
	 * @return data type or null
	 */
	public static GeoPackageDataType getDataType(String type) {

		GeoPackageDataType dataType = GeoPackageDataType.findName(type);

		if (dataType == null) {

			// Check if a geometry and set as a blob
			if (GeometryType.findName(type) != null) {
				dataType = GeoPackageDataType.BLOB;
			}

		}
		return dataType;
	}

	/**
	 * Get the default object value for the string default value and type
	 * 
	 * @param defaultValue
	 *            default value
	 * @param type
	 *            type
	 * @return default value
	 */
	public static Object getDefaultValue(String defaultValue, String type) {
		return getDefaultValue(defaultValue, getDataType(type));
	}

	/**
	 * Get the default object value for the string default value with the data
	 * type
	 * 
	 * @param defaultValue
	 *            default value
	 * @param type
	 *            data type
	 * @return default value
	 */
	public static Object getDefaultValue(String defaultValue,
			GeoPackageDataType type) {
		Object value = defaultValue;

		if (defaultValue != null && type != null
				&& !defaultValue.equalsIgnoreCase(DEFAULT_NULL)) {

			switch (type) {
			case TEXT:
				break;
			case DATE:
			case DATETIME:
				if (!DateConverter.isFunction(defaultValue)) {
					DateConverter converter = DateConverter.converter(type);
					try {
						value = converter.dateValue(defaultValue);
					} catch (Exception e) {
						logger.log(
								Level.WARNING, "Invalid " + type + " format: "
										+ defaultValue + ", String value used",
								e);
					}
				}
				break;
			case BOOLEAN:
				value = Integer.parseInt(defaultValue) == 0 ? Boolean.FALSE
						: Boolean.TRUE;
				break;
			case TINYINT:
				value = Byte.parseByte(defaultValue);
				break;
			case SMALLINT:
				value = Short.parseShort(defaultValue);
				break;
			case MEDIUMINT:
				value = Integer.parseInt(defaultValue);
				break;
			case INT:
			case INTEGER:
				value = Long.parseLong(defaultValue);
				break;
			case FLOAT:
				value = Float.parseFloat(defaultValue);
				break;
			case DOUBLE:
			case REAL:
				value = Double.parseDouble(defaultValue);
				break;
			case BLOB:
				value = defaultValue.getBytes();
				break;
			default:
				throw new GeoPackageException("Unsupported Data Type " + type);
			}

		}

		return value;
	}

}
