package mil.nga.geopackage.db.table;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import junit.framework.TestCase;
import mil.nga.geopackage.db.GeoPackageTableCreator;

/**
 * Table constraint test
 * 
 * @author osbornb
 */
public class ConstraintTest {

	/**
	 * Test parsing constraints in the GeoPackage database tables
	 */
	@Test
	public void testTables() {

		testSQLScript(GeoPackageTableCreator.SPATIAL_REFERENCE_SYSTEM, 0, 0, 0,
				0, createNames());
		testSQLScript(GeoPackageTableCreator.CONTENTS, 0, 0, 0, 1,
				createNames("fk_gc_r_srs_id"));
		testSQLScript(GeoPackageTableCreator.GEOMETRY_COLUMNS, 1, 1, 0, 2,
				createNames("pk_geom_cols", "uk_gc_table_name", "fk_gc_tn",
						"fk_gc_srs"));
		testSQLScript(GeoPackageTableCreator.TILE_MATRIX_SET, 0, 0, 0, 2,
				createNames("fk_gtms_table_name", "fk_gtms_srs"));
		testSQLScript(GeoPackageTableCreator.TILE_MATRIX, 1, 0, 0, 1,
				createNames("pk_ttm", "fk_tmm_table_name"));
		testSQLScript(GeoPackageTableCreator.DATA_COLUMNS, 1, 1, 0, 0,
				createNames("pk_gdc", "gdc_tn"));
		testSQLScript(GeoPackageTableCreator.DATA_COLUMN_CONSTRAINTS, 0, 1, 0,
				0, createNames("gdcc_ntv"));
		testSQLScript(GeoPackageTableCreator.METADATA, 0, 0, 0, 0,
				createNames());
		testSQLScript(GeoPackageTableCreator.METADATA_REFERENCE, 0, 0, 0, 2,
				createNames("crmr_mfi_fk", "crmr_mpi_fk"));
		testSQLScript(GeoPackageTableCreator.EXTENSIONS, 0, 1, 0, 0,
				createNames("ge_tce"));
		testSQLScript(GeoPackageTableCreator.GRIDDED_COVERAGE, 0, 0, 1, 1,
				createNames("fk_g2dgtct_name", null));
		testSQLScript(GeoPackageTableCreator.GRIDDED_TILE, 0, 1, 0, 1,
				createNames("fk_g2dgtat_name", null));
		testSQLScript(GeoPackageTableCreator.EXTENDED_RELATIONS, 0, 0, 0, 0,
				createNames());
		testSQLScript(GeoPackageTableCreator.TABLE_INDEX, 0, 0, 0, 0,
				createNames());
		testSQLScript(GeoPackageTableCreator.GEOMETRY_INDEX, 1, 0, 0, 1,
				createNames("pk_ngi", "fk_ngi_nti_tn"));
		testSQLScript(GeoPackageTableCreator.FEATURE_TILE_LINK, 1, 0, 0, 0,
				createNames("pk_nftl"));
		testSQLScript(GeoPackageTableCreator.TILE_SCALING, 0, 0, 1, 1,
				createNames("fk_nts_gtms_tn", null));
		testSQLScript(GeoPackageTableCreator.CONTENTS_ID, 0, 1, 0, 1,
				createNames("uk_nci_table_name", "fk_nci_gc_tn"));

	}

	/**
	 * Create a list of names
	 * 
	 * @param names
	 *            names
	 * @return names
	 */
	private List<String> createNames(String... names) {
		return Arrays.asList(names);
	}

	/**
	 * Test the database script for constraint parsing
	 * 
	 * @param script
	 *            database script
	 * @param primaryKey
	 *            expected primary key count
	 * @param unique
	 *            expected unique count
	 * @param check
	 *            expected check count
	 * @param foreignKey
	 *            expected foreign key count
	 * @param names
	 *            expected constraint names
	 */
	private void testSQLScript(String script, int primaryKey, int unique,
			int check, int foreignKey, List<String> names) {

		int count = 0;
		int primaryKeyCount = 0;
		int uniqueCount = 0;
		int checkCount = 0;
		int foreignKeyCount = 0;

		List<String> statements = GeoPackageTableCreator.readSQLScript(script);
		for (String sql : statements) {

			List<Constraint> constraints = testConstraint(sql, names);

			count += constraints.size();

			primaryKeyCount += ConstraintParser
					.getConstraints(sql, ConstraintType.PRIMARY_KEY).size();
			uniqueCount += ConstraintParser
					.getConstraints(sql, ConstraintType.UNIQUE).size();
			checkCount += ConstraintParser
					.getConstraints(sql, ConstraintType.CHECK).size();
			foreignKeyCount += ConstraintParser
					.getConstraints(sql, ConstraintType.FOREIGN_KEY).size();
		}

		TestCase.assertEquals(primaryKey + unique + check + foreignKey, count);
		TestCase.assertEquals(primaryKeyCount, primaryKey);
		TestCase.assertEquals(uniqueCount, unique);
		TestCase.assertEquals(checkCount, check);
		TestCase.assertEquals(foreignKeyCount, foreignKey);

	}

	/**
	 * Test parsing the table SQL for constraints
	 * 
	 * @param sql
	 *            table SQL
	 * @param names
	 *            expected constraint names
	 * @return constraints
	 */
	private List<Constraint> testConstraint(String sql, List<String> names) {

		int primaryKeyCount = 0;
		int uniqueCount = 0;
		int checkCount = 0;
		int foreignKeyCount = 0;

		List<String> primaryKeyNames = new ArrayList<>();
		List<String> uniqueNames = new ArrayList<>();
		List<String> checkNames = new ArrayList<>();
		List<String> foreignKeyNames = new ArrayList<>();

		List<Constraint> constraints = ConstraintParser.getConstraints(sql);
		for (int i = 0; i < constraints.size(); i++) {

			Constraint constraint = constraints.get(i);
			String name = names.get(i);

			testConstraint(constraint, name, constraint.getType());

			switch (constraint.getType()) {
			case PRIMARY_KEY:
				primaryKeyCount++;
				primaryKeyNames.add(name);
				break;
			case UNIQUE:
				uniqueCount++;
				uniqueNames.add(name);
				break;
			case CHECK:
				checkCount++;
				checkNames.add(name);
				break;
			case FOREIGN_KEY:
				foreignKeyCount++;
				foreignKeyNames.add(name);
				break;
			}
		}

		testConstraints(
				ConstraintParser.getConstraints(sql,
						ConstraintType.PRIMARY_KEY),
				primaryKeyNames, primaryKeyCount, ConstraintType.PRIMARY_KEY);
		testConstraints(
				ConstraintParser.getConstraints(sql, ConstraintType.UNIQUE),
				uniqueNames, uniqueCount, ConstraintType.UNIQUE);
		testConstraints(
				ConstraintParser.getConstraints(sql, ConstraintType.CHECK),
				checkNames, checkCount, ConstraintType.CHECK);
		testConstraints(
				ConstraintParser.getConstraints(sql,
						ConstraintType.FOREIGN_KEY),
				foreignKeyNames, foreignKeyCount, ConstraintType.FOREIGN_KEY);

		return constraints;
	}

	/**
	 * Test the constraints
	 * 
	 * @param constraints
	 *            constraints
	 * @param names
	 *            expected names
	 * @param count
	 *            expected count
	 * @param type
	 *            constraint type
	 */
	private void testConstraints(List<Constraint> constraints,
			List<String> names, int count, ConstraintType type) {
		TestCase.assertEquals(count, constraints.size());
		for (int i = 0; i < constraints.size(); i++) {
			Constraint constraint = constraints.get(i);
			String name = names.get(i);
			testConstraint(constraint, name, type);
		}
	}

	/**
	 * Test the constraint
	 * 
	 * @param constraint
	 *            constraint
	 * @param name
	 *            expected name
	 * @param type
	 *            constraint type
	 */
	private void testConstraint(Constraint constraint, String name,
			ConstraintType type) {
		testConstraintHelper(constraint, name, type);
		String constraintSql = constraint.buildSql();
		testConstraintHelper(ConstraintParser.getConstraint(constraintSql),
				name, type);
		testConstraintHelper(
				ConstraintParser.getConstraint(constraintSql, type), name,
				type);
	}

	/**
	 * Test the constraint
	 * 
	 * @param constraint
	 *            constraint
	 * @param name
	 *            expected name
	 * @param type
	 *            constraint type
	 */
	private void testConstraintHelper(Constraint constraint, String name,
			ConstraintType type) {
		TestCase.assertNotNull(constraint);
		String constraintSql = constraint.buildSql();
		TestCase.assertEquals(type, constraint.getType());
		TestCase.assertTrue(ConstraintParser.isType(type, constraintSql));
		TestCase.assertEquals(type, ConstraintParser.getType(constraintSql));
		TestCase.assertEquals(name, constraint.getName());
		TestCase.assertEquals(name, ConstraintParser.getName(constraintSql));
	}

}
