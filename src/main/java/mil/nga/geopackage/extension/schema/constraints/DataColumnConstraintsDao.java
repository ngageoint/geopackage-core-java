package mil.nga.geopackage.extension.schema.constraints;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.PreparedUpdate;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.support.ConnectionSource;

import mil.nga.geopackage.GeoPackageCore;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.db.GeoPackageCoreConnection;
import mil.nga.geopackage.db.GeoPackageDao;
import mil.nga.geopackage.extension.schema.columns.DataColumns;
import mil.nga.geopackage.extension.schema.columns.DataColumnsDao;

/**
 * Data Column Constraints Data Access Object
 * 
 * @author osbornb
 */
public class DataColumnConstraintsDao
		extends GeoPackageDao<DataColumnConstraints, Void> {

	/**
	 * Create the DAO
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * @return dao
	 * @since 4.0.0
	 */
	public static DataColumnConstraintsDao create(GeoPackageCore geoPackage) {
		return create(geoPackage.getDatabase());
	}

	/**
	 * Create the DAO
	 * 
	 * @param db
	 *            database connection
	 * @return dao
	 * @since 4.0.0
	 */
	public static DataColumnConstraintsDao create(GeoPackageCoreConnection db) {
		return GeoPackageDao.createDao(db, DataColumnConstraints.class);
	}

	/**
	 * Data Columns DAO
	 */
	private DataColumnsDao dataColumnsDao;

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
	public DataColumnConstraintsDao(ConnectionSource connectionSource,
			Class<DataColumnConstraints> dataClass) throws SQLException {
		super(connectionSource, dataClass);
	}

	/**
	 * Delete the Data Columns Constraints, cascading
	 * 
	 * @param constraints
	 *            data column constraints
	 * @return deleted count
	 * @throws SQLException
	 *             upon failure
	 */
	public int deleteCascade(DataColumnConstraints constraints)
			throws SQLException {
		int count = 0;

		if (constraints != null) {

			// Check if the last remaining constraint with the constraint name
			// is being deleted
			List<DataColumnConstraints> remainingConstraints = queryByConstraintName(
					constraints.getConstraintName());
			if (remainingConstraints.size() == 1) {

				DataColumnConstraints remainingConstraint = remainingConstraints
						.get(0);

				// Compare the name, type, and value
				if (remainingConstraint.getConstraintName()
						.equals(constraints.getConstraintName())
						&& remainingConstraint.getConstraintType()
								.equals(constraints.getConstraintType())
						&& (remainingConstraint.getValue() == null
								? constraints.getValue() == null
								: remainingConstraint.getValue()
										.equals(constraints.getValue()))) {

					// Delete Data Columns
					DataColumnsDao dao = getDataColumnsDao();
					List<DataColumns> dataColumnsCollection = dao
							.queryByConstraintName(
									constraints.getConstraintName());
					if (!dataColumnsCollection.isEmpty()) {
						dao.delete(dataColumnsCollection);
					}
				}
			}

			// Delete
			count = delete(constraints);
		}
		return count;
	}

	/**
	 * Delete the collection of Data Column Constraints, cascading
	 * 
	 * @param constraintsCollection
	 *            constraints collection
	 * @return deleted count
	 * @throws SQLException
	 *             upon failure
	 */
	public int deleteCascade(
			Collection<DataColumnConstraints> constraintsCollection)
			throws SQLException {
		int count = 0;
		if (constraintsCollection != null) {
			for (DataColumnConstraints constraints : constraintsCollection) {
				count += deleteCascade(constraints);
			}
		}
		return count;
	}

	/**
	 * Delete the Data Column Constraints matching the prepared query, cascading
	 * 
	 * @param preparedDelete
	 *            prepared delete query
	 * @return deleted count
	 * @throws SQLException
	 *             upon failure
	 */
	public int deleteCascade(
			PreparedQuery<DataColumnConstraints> preparedDelete)
			throws SQLException {
		int count = 0;
		if (preparedDelete != null) {
			List<DataColumnConstraints> constraintsList = query(preparedDelete);
			count = deleteCascade(constraintsList);
		}
		return count;
	}

	/**
	 * Get or create a Data Columns DAO
	 * 
	 * @return data columns dao
	 */
	private DataColumnsDao getDataColumnsDao() {
		if (dataColumnsDao == null) {
			dataColumnsDao = DataColumnsDao.create(db);
		}
		return dataColumnsDao;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * Update using the unique columns
	 */
	@Override
	public int update(DataColumnConstraints dataColumnConstraints)
			throws SQLException {

		UpdateBuilder<DataColumnConstraints, Void> ub = updateBuilder();
		ub.updateColumnValue(DataColumnConstraints.COLUMN_MIN,
				dataColumnConstraints.getMin());
		ub.updateColumnValue(DataColumnConstraints.COLUMN_MIN_IS_INCLUSIVE,
				dataColumnConstraints.getMinIsInclusive());
		ub.updateColumnValue(DataColumnConstraints.COLUMN_MAX,
				dataColumnConstraints.getMax());
		ub.updateColumnValue(DataColumnConstraints.COLUMN_MAX_IS_INCLUSIVE,
				dataColumnConstraints.getMaxIsInclusive());
		ub.updateColumnValue(DataColumnConstraints.COLUMN_DESCRIPTION,
				dataColumnConstraints.getDescription());

		setUniqueWhere(ub.where(), dataColumnConstraints.getConstraintName(),
				dataColumnConstraints.getConstraintType(),
				dataColumnConstraints.getValue());

		PreparedUpdate<DataColumnConstraints> update = ub.prepare();
		int updated = update(update);

		return updated;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * Delete using the unique columns
	 */
	@Override
	public int delete(DataColumnConstraints dataColumnConstraints)
			throws SQLException {

		DeleteBuilder<DataColumnConstraints, Void> db = deleteBuilder();

		setUniqueWhere(db.where(), dataColumnConstraints.getConstraintName(),
				dataColumnConstraints.getConstraintType(),
				dataColumnConstraints.getValue());

		int deleted = db.delete();

		return deleted;
	}

	/**
	 * Query by the constraint name
	 * 
	 * @param constraintName
	 *            constraint name
	 * @return data column constraints
	 * @throws SQLException
	 *             upon failure
	 */
	public List<DataColumnConstraints> queryByConstraintName(
			String constraintName) throws SQLException {
		return queryForEq(DataColumnConstraints.COLUMN_CONSTRAINT_NAME,
				constraintName);
	}

	/**
	 * Query by the unique column values
	 * 
	 * @param constraintName
	 *            constraint name
	 * @param constraintType
	 *            constraint type
	 * @param value
	 *            value
	 * @return data column constraints
	 * @throws SQLException
	 *             upon failure
	 */
	public DataColumnConstraints queryByUnique(String constraintName,
			DataColumnConstraintType constraintType, String value)
			throws SQLException {

		DataColumnConstraints constraint = null;

		QueryBuilder<DataColumnConstraints, Void> qb = queryBuilder();
		setUniqueWhere(qb.where(), constraintName, constraintType, value);
		List<DataColumnConstraints> constraints = qb.query();
		if (!constraints.isEmpty()) {

			if (constraints.size() > 1) {
				throw new GeoPackageException("More than one "
						+ DataColumnConstraints.class.getSimpleName()
						+ " was found for unique constraint. Name: "
						+ constraintName + ", Type: " + constraintType
						+ ", Value: " + value);
			}

			constraint = constraints.get(0);
		}

		return constraint;
	}

	/**
	 * Set the unique column criteria in the where clause
	 * 
	 * @param where
	 *            where clause
	 * @param constraintName
	 *            constraint name
	 * @param constraintType
	 *            constraint type
	 * @param value
	 *            value
	 * @throws SQLException
	 */
	private void setUniqueWhere(Where<DataColumnConstraints, Void> where,
			String constraintName, DataColumnConstraintType constraintType,
			String value) throws SQLException {

		where.eq(DataColumnConstraints.COLUMN_CONSTRAINT_NAME, constraintName)
				.and().eq(DataColumnConstraints.COLUMN_CONSTRAINT_TYPE,
						constraintType.getValue());
		if (value == null) {
			where.and().isNull(DataColumnConstraints.COLUMN_VALUE);
		} else {
			where.and().eq(DataColumnConstraints.COLUMN_VALUE, value);
		}

	}

}
