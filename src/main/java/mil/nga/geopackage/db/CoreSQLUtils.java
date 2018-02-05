package mil.nga.geopackage.db;

/**
 * Core SQL Utility methods
 *
 * @author osbornb
 * @since 1.2.1
 */
public class CoreSQLUtils {

	/**
	 * Wrap the name in double quotes
	 *
	 * @param name
	 *            name
	 * @return quoted name
	 */
	public static String quoteWrap(String name) {
		String quoteName = null;
		if (name != null) {
			if (name.startsWith("\"") && name.endsWith("\"")) {
				quoteName = name;
			} else {
				quoteName = "\"" + name + "\"";
			}
		}
		return quoteName;
	}

	/**
	 * Wrap the names in double quotes
	 *
	 * @param names
	 *            names
	 * @return quoted names
	 */
	public static String[] quoteWrap(String[] names) {
		String[] quoteNames = null;
		if (names != null) {
			quoteNames = new String[names.length];
			for (int i = 0; i < names.length; i++) {
				quoteNames[i] = quoteWrap(names[i]);
			}
		}
		return quoteNames;
	}

	/**
	 * Build the "columns as" query values for the provided columns and "columns
	 * as" values for use in select statements. The columns as size should equal
	 * the number of columns and only provide values at the indices for desired
	 * columns.
	 * 
	 * Example: columns = [column1, column2], columnsAs = [null, value] creates
	 * [column1, value as column2]
	 * 
	 * @param columns
	 *            columns array
	 * @param columnsAs
	 *            columns as values
	 * @return columns with as values
	 * @since 2.0.0
	 */
	public static String[] buildColumnsAs(String[] columns, String[] columnsAs) {
		String[] columnsWithAs = null;
		if (columnsAs != null) {
			columnsWithAs = new String[columns.length];
			for (int i = 0; i < columns.length; i++) {
				String column = columns[i];
				String columnsAsValue = columnsAs[i];
				String columnWithAs = null;
				if (columnsAsValue != null) {
					columnWithAs = columnsAsValue + " AS " + column;
				} else {
					columnWithAs = column;
				}
				columnsWithAs[i] = columnWithAs;
			}
		} else {
			columnsWithAs = columns;
		}
		return columnsWithAs;
	}

}
