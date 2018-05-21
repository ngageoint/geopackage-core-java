package mil.nga.geopackage.extension.related_tables;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import mil.nga.geopackage.schema.TableColumnKey;

/**
 * Extended Relations Data Access Object
 * 
 * @author jyutzler
 */
public class ExtendedRelationsDao extends
		BaseDaoImpl<ExtendedRelation, TableColumnKey> {

	/**
	 * Constructor, required by ORMLite
	 * 
	 * @param connectionSource
	 * @param dataClass
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
}
