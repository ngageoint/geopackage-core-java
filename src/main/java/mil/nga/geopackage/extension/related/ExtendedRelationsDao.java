package mil.nga.geopackage.extension.related;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import mil.nga.geopackage.schema.TableColumnKey;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;

/**
 * Extended Relations Data Access Object
 * 
 * @author jyutzler
 * @since 3.0.1
 */
public class ExtendedRelationsDao extends
		BaseDaoImpl<ExtendedRelation, TableColumnKey> {

	/**
	 * Constructor, required by ORMLite
	 * 
	 * @param connectionSource
	 *            connection source
	 * @param dataClass
	 *            data class
	 * @throws SQLException
	 */
	public ExtendedRelationsDao(ConnectionSource connectionSource,
			Class<ExtendedRelation> dataClass) throws SQLException {
		super(connectionSource, dataClass);
	}

	/**
	 * Get all the base table names
	 * 
	 * @return base table names
	 * @throws SQLException
	 */
	public List<String> getBaseTables() throws SQLException {

		List<String> baseTables = new ArrayList<String>();

		List<ExtendedRelation> extendedRelations = queryForAll();
		for (ExtendedRelation extendedRelation : extendedRelations) {
			baseTables.add(extendedRelation.getBaseTableName());
		}

		return baseTables;
	}

	/**
	 * Get all the related table names
	 * 
	 * @return related table names
	 * @throws SQLException
	 */
	public List<String> getRelatedTables() throws SQLException {

		List<String> relatedTables = new ArrayList<String>();

		List<ExtendedRelation> extendedRelations = queryForAll();
		for (ExtendedRelation extendedRelation : extendedRelations) {
			relatedTables.add(extendedRelation.getRelatedTableName());
		}

		return relatedTables;
	}

	/**
	 * Get the relations to the base table
	 * 
	 * @param baseTable
	 *            base table
	 * @return extended relations
	 * @throws SQLException
	 */
	public List<ExtendedRelation> getBaseTableRelations(String baseTable)
			throws SQLException {
		return queryForEq(ExtendedRelation.COLUMN_BASE_TABLE_NAME, baseTable);
	}

	/**
	 * Get the relations to the related table
	 * 
	 * @param relatedTable
	 *            related table
	 * @return extended relations
	 * @throws SQLException
	 */
	public List<ExtendedRelation> getRelatedTableRelations(String relatedTable)
			throws SQLException {
		return queryForEq(ExtendedRelation.COLUMN_RELATED_TABLE_NAME,
				relatedTable);
	}

	/**
	 * Get the relations to the table, both base table and related table
	 * 
	 * @param table
	 *            table name
	 * @return extended relations
	 * @throws SQLException
	 */
	public List<ExtendedRelation> getTableRelations(String table)
			throws SQLException {

		QueryBuilder<ExtendedRelation, TableColumnKey> qb = queryBuilder();
		qb.where().like(ExtendedRelation.COLUMN_BASE_TABLE_NAME, table).or()
				.like(ExtendedRelation.COLUMN_RELATED_TABLE_NAME, table);
		PreparedQuery<ExtendedRelation> preparedQuery = qb.prepare();

		return query(preparedQuery);
	}

}
