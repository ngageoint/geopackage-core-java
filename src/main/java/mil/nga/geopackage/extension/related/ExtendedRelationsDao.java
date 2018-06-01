package mil.nga.geopackage.extension.related;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import mil.nga.geopackage.schema.TableColumnKey;

import com.j256.ormlite.dao.BaseDaoImpl;
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
}
