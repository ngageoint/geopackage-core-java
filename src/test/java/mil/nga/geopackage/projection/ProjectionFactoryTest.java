package mil.nga.geopackage.projection;

import java.util.Arrays;
import java.util.Properties;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

public class ProjectionFactoryTest {

	private final String authority = "Test";
	private final long code = 100001;

	@Before
	public void clear() {
		ProjectionFactory.clear();
		ProjectionRetriever.clear();
	}

	@Test
	public void testCustomProjection() {

		long authorityCode = code;

		Projection projection = ProjectionFactory
				.getProjection(
						authority,
						authorityCode++,
						"+proj=tmerc +lat_0=0 +lon_0=121 +k=1 +x_0=500000 +y_0=0 +ellps=krass +units=m +no_defs");
		TestCase.assertNotNull(projection);

		String[] params = new String[] { "+proj=tmerc", "+lat_0=0",
				"+lon_0=121", "+k=1", "+x_0=500000", "+y_0=0", "+ellps=krass",
				"+units=m", "+no_defs" };
		Projection projection2 = ProjectionFactory.getProjection(authority,
				authorityCode++, params);
		TestCase.assertNotNull(projection2);

		try {
			ProjectionFactory
					.getProjection(
							authority,
							authorityCode++,
							"+proj=tmerc +lat_0=0 +lon_0=121 +k=1 +x_0=500000 +y_0=0 +ellps=krass +units=m +no_defs +invalid");
			TestCase.fail("Invalid projection did not fail");
		} catch (Exception e) {
			// pass
		}

		try {
			String[] params2 = Arrays.copyOf(params, params.length + 1);
			params2[params2.length - 1] = "+invalid";
			ProjectionFactory
					.getProjection(authority, authorityCode++, params2);
			TestCase.fail("Invalid projection did not fail");
		} catch (Exception e) {
			// pass
		}

		try {
			ProjectionFactory.getProjection(authorityCode++);
			TestCase.fail("Invalid projection did not fail");
		} catch (Exception e) {
			// pass
		}

	}

	@Test
	public void testAddingProjectionToAuthority() {

		try {
			ProjectionFactory.getProjection(ProjectionConstants.AUTHORITY_NONE,
					code);
			TestCase.fail("Missing projection did not fail");
		} catch (Exception e) {
			// pass
		}

		ProjectionRetriever
				.setProjection(
						ProjectionConstants.AUTHORITY_NONE,
						code,
						"+proj=tmerc +lat_0=0 +lon_0=121 +k=1 +x_0=500000 +y_0=0 +ellps=krass +units=m +no_defs");

		Projection projection = ProjectionFactory.getProjection(
				ProjectionConstants.AUTHORITY_NONE, code);
		TestCase.assertNotNull(projection);
	}

	@Test
	public void testAddingAuthorityProjections() {

		// Make sure 4 projections do not exist
		for (long i = code; i < code + 4; i++) {
			try {
				ProjectionFactory.getProjection(authority, i);
				TestCase.fail("Missing projection did not fail");
			} catch (Exception e) {
				// pass
			}
		}

		// Add 3 custom projections to the new authority
		Properties properties = new Properties();
		properties
				.setProperty(
						String.valueOf(code),
						"+proj=tmerc +lat_0=0 +lon_0=121 +k=1 +x_0=500000 +y_0=0 +ellps=krass +units=m +no_defs");
		properties.setProperty(String.valueOf(code + 1),
				"+proj=longlat +datum=WGS84 +no_defs");
		properties
				.setProperty(
						String.valueOf(code + 2),
						"+proj=merc +a=6378137 +b=6378137 +lat_ts=0.0 +lon_0=0.0 +x_0=0.0 +y_0=0 +k=1.0 +units=m +nadgrids=@null +wktext  +no_defs");
		ProjectionRetriever.setProjections(authority, properties);

		// Verify first 3 projections exist, last still does not
		for (long i = code; i < code + 4; i++) {

			if (i < code + 3) {
				Projection projection = ProjectionFactory.getProjection(
						authority, i);
				TestCase.assertNotNull(projection);
			} else {
				try {
					ProjectionFactory.getProjection(authority, i);
					TestCase.fail("Missing projection did not fail");
				} catch (Exception e) {
					// pass
				}
			}
		}

		// Clear authority code from retriever but not from factory cache
		ProjectionRetriever.clear(authority, code);
		Projection projection = ProjectionFactory
				.getProjection(authority, code);
		TestCase.assertNotNull(projection);

		// Clear authority code from factory cache and verify no longer exists
		ProjectionFactory.clear(authority, code);
		try {
			ProjectionFactory.getProjection(authority, code);
			TestCase.fail("Missing projection did not fail");
		} catch (Exception e) {
			// pass
		}

		// Set projection back into the retriever and verify factory creates it
		ProjectionRetriever.setProjection(authority, code,
				"+proj=longlat +datum=WGS84 +no_defs");
		projection = ProjectionFactory.getProjection(authority, code);
		TestCase.assertNotNull(projection);

	}
}
