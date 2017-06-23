package mil.nga.geopackage.projection;

import java.util.Arrays;

import junit.framework.TestCase;

import org.junit.Test;

public class ProjectionFactoryTest {

	@Test
	public void testCustomProjection() {

		int epsg = 100001;
		String authority = "test";

		Projection projection = ProjectionFactory
				.getProjection(
						authority,
						epsg++,
						"+proj=tmerc +lat_0=0 +lon_0=121 +k=1 +x_0=500000 +y_0=0 +ellps=krass +units=m +no_defs");
		TestCase.assertNotNull(projection);

		String[] params = new String[] { "+proj=tmerc", "+lat_0=0",
				"+lon_0=121", "+k=1", "+x_0=500000", "+y_0=0", "+ellps=krass",
				"+units=m", "+no_defs" };
		Projection projection2 = ProjectionFactory.getProjection(authority,
				epsg++, params);
		TestCase.assertNotNull(projection2);

		try {
			ProjectionFactory
					.getProjection(
							authority,
							epsg++,
							"+proj=tmerc +lat_0=0 +lon_0=121 +k=1 +x_0=500000 +y_0=0 +ellps=krass +units=m +no_defs +invalid");
			TestCase.fail("Invalid projection did not fail");
		} catch (Exception e) {
			// pass
		}

		try {
			String[] params2 = Arrays.copyOf(params, params.length + 1);
			params2[params2.length - 1] = "+invalid";
			ProjectionFactory.getProjection(authority, epsg++, params2);
			TestCase.fail("Invalid projection did not fail");
		} catch (Exception e) {
			// pass
		}

		try {
			ProjectionFactory.getProjection(epsg++);
			TestCase.fail("Invalid projection did not fail");
		} catch (Exception e) {
			// pass
		}

	}

}
