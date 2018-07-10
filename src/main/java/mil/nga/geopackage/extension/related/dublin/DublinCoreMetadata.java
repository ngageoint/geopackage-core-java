package mil.nga.geopackage.extension.related.dublin;

import mil.nga.geopackage.user.UserColumn;
import mil.nga.geopackage.user.UserCoreRow;
import mil.nga.geopackage.user.UserTable;

/**
 * Dublin Core Metadata Initiative
 * 
 * @author osbornb
 * @since 3.0.1
 */
public class DublinCoreMetadata {

	/**
	 * Check if the table has a column for the Dublin Core Type term
	 * 
	 * @param table
	 *            user table
	 * @param type
	 *            Dublin Core Type
	 * @return true if has column
	 */
	public static boolean hasColumn(UserTable<?> table, DublinCoreType type) {

		boolean hasColumn = table.hasColumn(type.getName());

		if (!hasColumn) {
			for (String synonym : type.getSynonyms()) {
				hasColumn = table.hasColumn(synonym);
				if (hasColumn) {
					break;
				}
			}
		}

		return hasColumn;
	}

	/**
	 * Check if the row has a column for the Dublin Core Type term
	 * 
	 * @param row
	 *            user row
	 * @param type
	 *            Dublin Core Type
	 * @return true if has column
	 */
	public static boolean hasColumn(UserCoreRow<?, ?> row, DublinCoreType type) {
		return hasColumn(row.getTable(), type);
	}

	/**
	 * Get the column from the table for the Dublin Core Type term
	 * 
	 * @param table
	 *            user table
	 * @param type
	 *            Dublin Core Type
	 * @param <T>
	 *            column type
	 * @return column
	 */
	public static <T extends UserColumn> T getColumn(UserTable<T> table,
			DublinCoreType type) {

		T column = null;

		if (table.hasColumn(type.getName())) {
			column = table.getColumn(type.getName());
		} else {
			for (String synonym : type.getSynonyms()) {
				if (table.hasColumn(synonym)) {
					column = table.getColumn(synonym);
					break;
				}
			}
		}

		return column;
	}

	/**
	 * Get the column from the row for the Dublin Core Type term
	 * 
	 * @param row
	 *            user row
	 * @param type
	 *            Dublin Core Type
	 * @param <T>
	 *            column type
	 * @return column
	 */
	public static <T extends UserColumn> T getColumn(UserCoreRow<T, ?> row,
			DublinCoreType type) {
		return getColumn(row.getTable(), type);
	}

	/**
	 * Get the value from the row for the Dublin Core Type term
	 * 
	 * @param row
	 *            user row
	 * @param type
	 *            Dublin Core Type
	 * @return value
	 */
	public static Object getValue(UserCoreRow<?, ?> row, DublinCoreType type) {

		UserColumn column = getColumn(row, type);

		Object value = row.getValue(column.getIndex());

		return value;
	}

	/**
	 * Set the value in the row for the Dublin Core Type term
	 * 
	 * @param row
	 *            user row
	 * @param type
	 *            Dublin Core Type
	 * @param value
	 *            value
	 */
	public static void setValue(UserCoreRow<?, ?> row, DublinCoreType type,
			Object value) {

		UserColumn column = getColumn(row, type);

		row.setValue(column.getIndex(), value);
	}

}
