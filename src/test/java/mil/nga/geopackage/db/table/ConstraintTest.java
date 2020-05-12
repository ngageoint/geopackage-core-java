package mil.nga.geopackage.db.table;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import junit.framework.TestCase;
import mil.nga.geopackage.db.GeoPackageTableCreator;
import mil.nga.geopackage.extension.nga.contents.ContentsIdExtension;
import mil.nga.geopackage.extension.nga.index.FeatureTableCoreIndex;
import mil.nga.geopackage.extension.nga.index.GeometryIndexTableCreator;
import mil.nga.geopackage.extension.nga.link.FeatureTileTableCoreLinker;
import mil.nga.geopackage.extension.nga.scale.TileTableScaling;
import mil.nga.geopackage.property.GeoPackageProperties;

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
		testSQLScript(GeoPackageTableCreator.EXTENSIONS, 0, 1, 0, 0,
				createNames("ge_tce"));
		testSQLScript(GeoPackageTableCreator.SCHEMA_PATH,
				GeoPackageTableCreator.DATA_COLUMNS, 1, 1, 0, 0,
				createNames("pk_gdc", "gdc_tn"));
		testSQLScript(GeoPackageTableCreator.SCHEMA_PATH,
				GeoPackageTableCreator.DATA_COLUMN_CONSTRAINTS, 0, 1, 0, 0,
				createNames("gdcc_ntv"));
		testSQLScript(GeoPackageTableCreator.METADATA_PATH,
				GeoPackageTableCreator.METADATA, 0, 0, 0, 0,
				createNames("m_pk"));
		testSQLScript(GeoPackageTableCreator.METADATA_PATH,
				GeoPackageTableCreator.METADATA_REFERENCE, 0, 0, 0, 2,
				createNames("crmr_mfi_fk", "crmr_mpi_fk"));
		testSQLScript(GeoPackageTableCreator.GRIDDED_PATH,
				GeoPackageTableCreator.GRIDDED_COVERAGE, 0, 0, 1, 1,
				createNames("fk_g2dgtct_name", null));
		testSQLScript(GeoPackageTableCreator.GRIDDED_PATH,
				GeoPackageTableCreator.GRIDDED_TILE, 0, 1, 0, 1,
				createNames("fk_g2dgtat_name", null));
		testSQLScript(GeoPackageTableCreator.RELATED_PATH,
				GeoPackageTableCreator.EXTENDED_RELATIONS, 0, 0, 0, 0,
				createNames());
		testCommunitySQLScript(FeatureTableCoreIndex.EXTENSION_AUTHOR,
				FeatureTableCoreIndex.EXTENSION_NAME_NO_AUTHOR,
				GeometryIndexTableCreator.TABLE_INDEX, 0, 0, 0, 0,
				createNames());
		testCommunitySQLScript(FeatureTableCoreIndex.EXTENSION_AUTHOR,
				FeatureTableCoreIndex.EXTENSION_NAME_NO_AUTHOR,
				GeometryIndexTableCreator.GEOMETRY_INDEX, 1, 0, 0, 1,
				createNames("pk_ngi", "fk_ngi_nti_tn"));
		testCommunitySQLScript(FeatureTileTableCoreLinker.EXTENSION_AUTHOR,
				FeatureTileTableCoreLinker.EXTENSION_NAME_NO_AUTHOR, null, 1, 0,
				0, 0, createNames("pk_nftl"));
		testCommunitySQLScript(TileTableScaling.EXTENSION_AUTHOR,
				TileTableScaling.EXTENSION_NAME_NO_AUTHOR, null, 0, 0, 1, 1,
				createNames("fk_nts_gtms_tn", null));
		testCommunitySQLScript(ContentsIdExtension.EXTENSION_AUTHOR,
				ContentsIdExtension.EXTENSION_NAME_NO_AUTHOR, null, 0, 1, 0, 1,
				createNames("uk_nci_table_name", "fk_nci_gc_tn"));

	}

	/**
	 * Test parsing constraints in the table SQL
	 */
	@Test
	public void testSql() {

		testSQL(null, 0, 0, 0, 0, createNames());
		testSQL("", 0, 0, 0, 0, createNames());
		testSQL("CREATE TABLE table_name (\n"
				+ " id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n"
				+ " column_name INTEGER NOT NULL UNIQUE\n" + ");", 0, 0, 0, 0,
				createNames());
		testSQL("CREATE TABLE table_name ("
				+ " id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
				+ " column_name INTEGER NOT NULL UNIQUE" + ");", 0, 0, 0, 0,
				createNames());
		testSQL("CREATE TABLE table_name (\n"
				+ " id INTEGER CONSTRAINT my_pk PRIMARY KEY AUTOINCREMENT CONSTRAINT my_not_null NOT NULL,\n"
				+ " column_name INTEGER NOT NULL CONSTRAINT my_unique UNIQUE\n"
				+ ");", 0, 0, 0, 0,
				createNames("my_pk", "my_not_null", "my_unique"));
		testSQL("CREATE TABLE table_name ("
				+ " id INTEGER constraint \"my pk\" PRIMARY \nKEY    AUTOINCREMENT \tCONSTRAINT\n\n my_not_null\n NOT\t NULL,"
				+ "\ncolumn_name\nINTEGER\nNOT\nNULL\nCONSTRAINT\n\"my unique\"\nUNIQUE"
				+ ");", 0, 0, 0, 0,
				createNames("my pk", "my_not_null", "my unique"));
		testSQL("CREATE TABLE table_name (\n" + " column1 TEXT NOT NULL,\n"
				+ " column2 INTEGER NOT NULL UNIQUE,\n"
				+ " CONSTRAINT pk_name PRIMARY KEY (column1, column2)\n" + ");",
				1, 0, 0, 0, createNames("pk_name"));
		testSQL("CREATE TABLE table_name (" + " column1 TEXT NOT NULL,"
				+ " column2 INTEGER NOT NULL UNIQUE,"
				+ " CONSTRAINT    pk_name   PRIMARY KEY (column1, column2)"
				+ ");", 1, 0, 0, 0, createNames("pk_name"));
		testSQL("CREATE TABLE table_name (\n" + " column1 TEXT NOT NULL,\n"
				+ " column2 INTEGER NOT NULL,\n"
				+ " CONSTRAINT uk_name UNIQUE (column1, column2)\n" + ");", 0,
				1, 0, 0, createNames("uk_name"));
		testSQL("CREATE TABLE table_name (" + " column1 TEXT NOT NULL,"
				+ " column2 INTEGER NOT NULL," + " UNIQUE (column1, column2)"
				+ ")", 0, 1, 0, 0, createNames(1));
		testSQL("CREATE TABLE table_name (" + " column1 TEXT NOT NULL,"
				+ " column2 INTEGER NOT NULL,"
				+ " CONSTRAINT c_name CHECK (column1 in ('value1','value2'))"
				+ ")", 0, 0, 1, 0, createNames("c_name"));
		testSQL("CREATE   TABLE    table_name    ("
				+ "   column1 TEXT NOT NULL , " + " column2 INTEGER NOT NULL ,"
				+ "\tCHECK ( column1 in ( 'value1' , 'value2'  )   )  " + ")",
				0, 0, 1, 0, createNames(1));
		testSQL("CREATE TABLE table_name (" + " column1 TEXT NOT NULL,"
				+ " column2 INTEGER NOT NULL,"
				+ " CONSTRAINT fk_name FOREIGN KEY (column2) REFERENCES table_name2(column3)"
				+ ")", 0, 0, 0, 1, createNames("fk_name"));
		testSQL("CREATE TABLE table_name (" + " column1 TEXT NOT NULL,"
				+ " column2 INTEGER NOT NULL,"
				+ " FOREIGN       KEY (column2) REFERENCES table_name2(column3)"
				+ ")", 0, 0, 0, 1, createNames(1));
		testSQL("CREATE TABLE table_name (" + " column1 TEXT,"
				+ " column2 INTEGER,"
				+ " CONSTRAINT pk_name PRIMARY KEY (column1, column2),"
				+ " CONSTRAINT uk_name UNIQUE (column1, column2),"
				+ " CONSTRAINT c_name CHECK (column1 in ('value1','value2')),"
				+ " CONSTRAINT fk_name FOREIGN KEY (column2) REFERENCES table_name2(column3)"
				+ ")", 1, 1, 1, 1,
				createNames("pk_name", "uk_name", "c_name", "fk_name"));
		testSQL("CREATE TABLE table_name (" + " column1 TEXT,"
				+ " column2 INTEGER,"
				+ " CONSTRAINT \"pk name\" PRIMARY KEY (column1, column2),"
				+ " UNIQUE (column1, column2),"
				+ " CONSTRAINT \"c_name\" CHECK (column1 in ('value1','value2')),"
				+ " CONSTRAINT fk_name FOREIGN KEY (column2) REFERENCES table_name2(column3)"
				+ ")", 1, 1, 1, 1,
				createNames("pk name", null, "c_name", "fk_name"));
		testSQL("CREATE TABLE table_name (" + " column1 TEXT,"
				+ " column2 INTEGER," + " PRIMARY KEY (column1, column2),"
				+ " UNIQUE (column1, column2),"
				+ " CHECK (column1 in ('value1','value2')),"
				+ " FOREIGN KEY (column2) REFERENCES table_name2(column3)"
				+ ")", 1, 1, 1, 1, createNames(4));
		testSQL("create table table_name (" + " column1 TEXT,"
				+ " column2 INTEGER,"
				+ " constraint pk_name primary key (column1, column2),"
				+ " constraint uk_name unique (column1, column2),"
				+ " constraint c_name check (column1 in ('value1','value2')),"
				+ " constraint fk_name foreign key (column2) references table_name2(column3)"
				+ ")", 1, 1, 1, 1,
				createNames("pk_name", "uk_name", "c_name", "fk_name"));
		testSQL("create table table_name ("
				+ " column1 TEXT CONSTRAINT col1_not_null NOT NULL CONSTRAINT col1_unique UNIQUE,"
				+ " column2 INTEGER CONSTRAINT col2_check CHECK(column2 > 0),"
				+ " constraint pk_name primary key (column1, column2),"
				+ " constraint uk_name unique (column1, column2),"
				+ " constraint c_name check (column1 in ('value1','value2')),"
				+ " constraint fk_name foreign key (column2) references table_name2(column3)"
				+ ")", 1, 1, 1, 1,
				createNames("pk_name", "uk_name", "c_name", "fk_name",
						"col1_not_null", "col1_unique", "col2_check"));

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
	 * Create a list of null names
	 * 
	 * @param count
	 *            number of names
	 * @return names
	 */
	private List<String> createNames(int count) {
		List<String> names = new ArrayList<>();
		for (int i = 0; i < count; i++) {
			names.add(null);
		}
		return names;
	}

	/**
	 * Test the database script for constraint parsing
	 * 
	 * @param property
	 *            property
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
	private void testSQLScript(String property, int primaryKey, int unique,
			int check, int foreignKey, List<String> names) {
		testSQLScript(null, null, property, primaryKey, unique, check,
				foreignKey, names);
	}

	/**
	 * Test the database script for constraint parsing
	 * 
	 * @param propertyPath
	 *            property path
	 * @param property
	 *            property
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
	private void testSQLScript(String propertyPath, String property,
			int primaryKey, int unique, int check, int foreignKey,
			List<String> names) {
		testSQLScript(propertyPath, null, property, primaryKey, unique, check,
				foreignKey, names);
	}

	/**
	 * Test the database script for constraint parsing
	 * 
	 * @param communityPath
	 *            community extension path
	 * @param property
	 *            property
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
	private void testCommunitySQLScript(String author, String name,
			String property, int primaryKey, int unique, int check,
			int foreignKey, List<String> names) {
		String communityPath = GeoPackageProperties.buildProperty(author, name);
		testSQLScript(communityPath, communityPath, property, primaryKey,
				unique, check, foreignKey, names);
	}

	/**
	 * Test the database script for constraint parsing
	 * 
	 * @param propertyPath
	 *            property path
	 * @param communityPath
	 *            community extension path
	 * @param property
	 *            property
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
	private void testSQLScript(String propertyPath, String communityPath,
			String property, int primaryKey, int unique, int check,
			int foreignKey, List<String> names) {

		int count = 0;
		int primaryKeyCount = 0;
		int uniqueCount = 0;
		int checkCount = 0;
		int foreignKeyCount = 0;

		String script = GeoPackageTableCreator.getScript(
				GeoPackageProperties.buildProperty(communityPath, property));
		List<String> statements = GeoPackageTableCreator
				.readSQLScript(propertyPath, script);

		// List<String> statements = GeoPackageTableCreator
		// .readScript(pathProperty, property);
		for (String sql : statements) {

			ConstraintTestResult constraintResult = testConstraint(sql, names);

			count += constraintResult.getCount();
			primaryKeyCount += constraintResult.getPrimaryKeyCount();
			uniqueCount += constraintResult.getUniqueCount();
			checkCount += constraintResult.getCheckCount();
			foreignKeyCount += constraintResult.getForeignKeyCount();
		}

		TestCase.assertEquals(primaryKey + unique + check + foreignKey, count);
		TestCase.assertEquals(primaryKey, primaryKeyCount);
		TestCase.assertEquals(unique, uniqueCount);
		TestCase.assertEquals(check, checkCount);
		TestCase.assertEquals(foreignKey, foreignKeyCount);

	}

	/**
	 * Test the database script for constraint parsing
	 * 
	 * @param sql
	 *            table sql
	 * @param primaryKey
	 *            expected primary key count
	 * @param unique
	 *            expected unique count
	 * @param check
	 *            expected check count
	 * @param foreignKey
	 *            expected foreign key count
	 * @param names
	 *            expected constraint names, table names (null or not) followed
	 *            by columns (non null)
	 */
	private void testSQL(String sql, int primaryKey, int unique, int check,
			int foreignKey, List<String> names) {

		ConstraintTestResult constraintResult = testConstraint(sql, names);

		TestCase.assertEquals(primaryKey + unique + check + foreignKey,
				constraintResult.getCount());
		TestCase.assertEquals(primaryKey,
				constraintResult.getPrimaryKeyCount());
		TestCase.assertEquals(unique, constraintResult.getUniqueCount());
		TestCase.assertEquals(check, constraintResult.getCheckCount());
		TestCase.assertEquals(foreignKey,
				constraintResult.getForeignKeyCount());
	}

	/**
	 * Test parsing the table SQL for constraints
	 * 
	 * @param sql
	 *            table SQL
	 * @param names
	 *            expected constraint names, table names (null or not) followed
	 *            by columns (non null)
	 * @return constraint test results
	 */
	private ConstraintTestResult testConstraint(String sql,
			List<String> names) {

		int primaryKeyCount = 0;
		int uniqueCount = 0;
		int checkCount = 0;
		int foreignKeyCount = 0;

		TableConstraints constraints = ConstraintParser.getConstraints(sql);
		for (int i = 0; i < constraints.numTableConstraints(); i++) {

			Constraint constraint = constraints.getTableConstraint(i);
			String name = names.get(i);

			testConstraint(constraint, name, constraint.getType());

			switch (constraint.getType()) {
			case PRIMARY_KEY:
				primaryKeyCount++;
				break;
			case UNIQUE:
				uniqueCount++;
				break;
			case CHECK:
				checkCount++;
				break;
			case FOREIGN_KEY:
				foreignKeyCount++;
				break;
			default:
				TestCase.fail("Unexpected table constraint type: "
						+ constraint.getType());
			}
		}

		int nameIndex = constraints.numTableConstraints();

		for (String columnName : constraints.getColumnsWithConstraints()) {
			for (int i = 0; i < constraints
					.numColumnConstraints(columnName); i++) {

				Constraint constraint = constraints
						.getColumnConstraint(columnName, i);

				String name = null;
				if (constraint.getName() != null) {
					name = names.get(nameIndex++);
				}

				testConstraint(constraint, name, constraint.getType());
			}
		}

		return new ConstraintTestResult(constraints, primaryKeyCount,
				uniqueCount, checkCount, foreignKeyCount);
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
