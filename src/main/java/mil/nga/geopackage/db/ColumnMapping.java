package mil.nga.geopackage.db;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import mil.nga.geopackage.user.UserColumn;
import mil.nga.geopackage.user.UserTable;

/**
 * Mapping between column names being mapped to and the mapped column
 * information
 * 
 * @author osbornb
 * @since 3.2.1
 */
public class ColumnMapping {

	/**
	 * Mapping between column names and mapped columns
	 */
	private final Map<String, MappedColumn> columns = new LinkedHashMap<>();

	/**
	 * Constructor
	 */
	public ColumnMapping() {

	}

	/**
	 * Constructor
	 * 
	 * @param columns
	 *            user columns
	 */
	public ColumnMapping(List<? extends UserColumn> columns) {
		for (UserColumn column : columns) {
			addColumn(new MappedColumn(column));
		}
	}

	/**
	 * Constructor
	 * 
	 * @param table
	 *            user table
	 */
	public ColumnMapping(UserTable<? extends UserColumn> table) {
		this(table.getColumns());
	}

	/**
	 * Add a column
	 * 
	 * @param column
	 *            mapped column
	 */
	public void addColumn(MappedColumn column) {
		columns.put(column.getToColumn(), column);
	}

	/**
	 * Add a column
	 * 
	 * @param columnName
	 *            column name
	 */
	public void addColumn(String columnName) {
		columns.put(columnName, new MappedColumn(columnName));
	}

	/**
	 * Get the column names
	 * 
	 * @return column names
	 */
	public Set<String> getColumnNames() {
		return columns.keySet();
	}

	/**
	 * Get the columns as an entry set
	 * 
	 * @return columns
	 */
	public Set<Entry<String, MappedColumn>> getColumns() {
		return columns.entrySet();
	}

	/**
	 * Get the mapped column for the column name
	 * 
	 * @param columnName
	 *            column name
	 * @return mapped column
	 */
	public MappedColumn getColumn(String columnName) {
		return columns.get(columnName);
	}

}
