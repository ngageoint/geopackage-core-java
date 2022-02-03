package mil.nga.geopackage.db;

import org.junit.Test;

import junit.framework.TestCase;

/**
 * Pagination Test
 * 
 * @author osbornb
 */
public class PaginationTest {

	/**
	 * Test pagination find
	 */
	@Test
	public void testFind() {

		testFind("SELECT... LIMIT 5", 5);
		testFind("SELECT... LIMIT 10", 10);
		testFind("SELECT... LIMIT     30", 30);
		testFind("SELECT... LIMIT     40        ", 40);
		testFind("SELECT... LIMIT 15,10", 10, 15);
		testFind("SELECT... LIMIT 7 , 5 ", 5, 7);
		testFind("SELECT... LIMIT   100    ,     50      ", 50, 100);
		testFind("SELECT... LIMIT 10 OFFSET 15", 10, 15);
		testFind("SELECT... LIMIT 5 OFFSET 7 ", 5, 7);
		testFind("SELECT... LIMIT   50    OFFSET     100      ", 50, 100);
		testFind("SELECT... LIMIT -15", -15);
		testFind("SELECT... LIMIT -15,-10", -10, 0);
		testFind("SELECT... LIMIT -15 , -10", -10, 0);
		testFind("SELECT... LIMIT -10 OFFSET -15", -10, 0);

	}

	/**
	 * Test pagination replace
	 */
	@Test
	public void testReplace() {

		testReplace("SELECT... LIMIT 5", "SELECT... LIMIT 5 OFFSET 5");
		testReplace("SELECT... LIMIT 15,10", "SELECT... LIMIT 10 OFFSET 25");
		testReplace("SELECT... LIMIT 5 OFFSET 7",
				"SELECT... LIMIT 5 OFFSET 12");
		testReplace("SELECT... LIMIT -15", "SELECT... LIMIT -15");
		testReplace("SELECT... LIMIT -15,-10", "SELECT... LIMIT -10 OFFSET 0");
		testReplace("SELECT... LIMIT -15,10", "SELECT... LIMIT 10 OFFSET 10");

	}

	/**
	 * Test pagination find
	 * 
	 * @param sql
	 *            SQL statement
	 * @param limit
	 *            limit
	 */
	private void testFind(String sql, int limit) {
		testFind(sql, limit, null);
	}

	/**
	 * Test pagination find
	 * 
	 * @param sql
	 *            SQL statement
	 * @param limit
	 *            limit
	 * @param offset
	 *            offset
	 */
	private void testFind(String sql, int limit, long offset) {
		testFind(sql, limit, Long.valueOf(offset));
	}

	/**
	 * Test pagination find
	 * 
	 * @param sql
	 *            SQL statement
	 * @param limit
	 *            limit
	 * @param offset
	 *            offset
	 */
	private void testFind(String sql, int limit, Long offset) {

		Pagination pagination = Pagination.find(sql);
		TestCase.assertEquals(limit, pagination.getLimit());
		boolean hasOffset = offset != null;
		TestCase.assertEquals(hasOffset, pagination.hasOffset());
		if (hasOffset) {
			TestCase.assertEquals(offset, pagination.getOffset());
		} else {
			TestCase.assertNull(pagination.getOffset());
		}

		pagination = Pagination.find(sql.toLowerCase());
		TestCase.assertEquals(limit, pagination.getLimit());
		hasOffset = offset != null;
		TestCase.assertEquals(hasOffset, pagination.hasOffset());
		if (hasOffset) {
			TestCase.assertEquals(offset, pagination.getOffset());
		} else {
			TestCase.assertNull(pagination.getOffset());
		}

	}

	/**
	 * Test pagination replace
	 * 
	 * @param sql
	 *            SQL statement
	 * @param expected
	 *            expected SQL
	 */
	public void testReplace(String sql, String expected) {

		Pagination pagination = Pagination.find(sql);
		pagination.incrementOffset();
		TestCase.assertEquals(expected, pagination.replace(sql));

	}

}
