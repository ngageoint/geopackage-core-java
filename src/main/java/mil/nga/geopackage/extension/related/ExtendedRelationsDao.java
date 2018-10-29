package mil.nga.geopackage.extension.related;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import mil.nga.geopackage.schema.TableColumnKey;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
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
	 *             upon failure
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
	 *             upon failure
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
	 *             upon failure
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
	 *             upon failure
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
	 *             upon failure
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
	 *             upon failure
	 */
	public List<ExtendedRelation> getTableRelations(String table)
			throws SQLException {

		QueryBuilder<ExtendedRelation, TableColumnKey> qb = queryBuilder();
		qb.where().like(ExtendedRelation.COLUMN_BASE_TABLE_NAME, table).or()
				.like(ExtendedRelation.COLUMN_RELATED_TABLE_NAME, table);
		PreparedQuery<ExtendedRelation> preparedQuery = qb.prepare();

		return query(preparedQuery);
	}

	/**
	 * Get the relations matching the non null provided values
	 * 
	 * @param baseTable
	 *            base table name
	 * @param baseColumn
	 *            base primary column name
	 * @param relatedTable
	 *            related table name
	 * @param relatedColumn
	 *            related primary column name
	 * @param relation
	 *            relation name
	 * @param mappingTable
	 *            mapping table name
	 * @return extended relations
	 * @throws SQLException
	 *             upon failure
	 * @since 3.1.1
	 */
	public List<ExtendedRelation> getRelations(String baseTable,
			String baseColumn, String relatedTable, String relatedColumn,
			String relation, String mappingTable) throws SQLException {

		QueryBuilder<ExtendedRelation, TableColumnKey> qb = queryBuilder();
		Where<ExtendedRelation, TableColumnKey> where = null;

		if (baseTable != null) {
			where = addToWhere(qb, where);
			where.like(ExtendedRelation.COLUMN_BASE_TABLE_NAME, baseTable);
		}

		if (baseColumn != null) {
			where = addToWhere(qb, where);
			where.like(ExtendedRelation.COLUMN_BASE_PRIMARY_COLUMN, baseColumn);
		}

		if (relatedTable != null) {
			where = addToWhere(qb, where);
			where.like(ExtendedRelation.COLUMN_RELATED_TABLE_NAME, relatedTable);
		}

		if (relatedColumn != null) {
			where = addToWhere(qb, where);
			where.like(ExtendedRelation.COLUMN_RELATED_PRIMARY_COLUMN,
					relatedColumn);
		}

		if (relation != null) {
			where = addToWhere(qb, where);
			where.like(ExtendedRelation.COLUMN_RELATION_NAME, relation);
		}

		if (mappingTable != null) {
			where = addToWhere(qb, where);
			where.like(ExtendedRelation.COLUMN_MAPPING_TABLE_NAME, mappingTable);
		}

		PreparedQuery<ExtendedRelation> preparedQuery = qb.prepare();

		return query(preparedQuery);
	}

	/**
	 * Add to the where clause, either as new or with an and
	 * 
	 * @param qb
	 *            query builder
	 * @param where
	 *            where clause
	 * @return where clause
	 */
	private Where<ExtendedRelation, TableColumnKey> addToWhere(
			QueryBuilder<ExtendedRelation, TableColumnKey> qb,
			Where<ExtendedRelation, TableColumnKey> where) {
		if (where == null) {
			where = qb.where();
		} else {
			where.and();
		}
		return where;
	}

}
