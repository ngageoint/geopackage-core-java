package mil.nga.geopackage.io;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import mil.nga.geopackage.GeoPackageException;

/**
 * Resource file Input / Output utility methods
 * 
 * @author osbornb
 * @since 2.0.1
 */
public class ResourceIOUtils {

	/**
	 * Parse the SQL statements for the base resource path and sql file name
	 * 
	 * @param path
	 *            base resource path
	 * @param name
	 *            sql file name
	 * @return list of sql statements
	 */
	public static List<String> parseSQLStatements(String path, String name) {
		return parseSQLStatements("/" + path + "/" + name);
	}

	/**
	 * Parse the SQL statements for the resource name
	 * 
	 * @param resourceName
	 *            resource name
	 * @return list of sql statements
	 */
	public static List<String> parseSQLStatements(String resourceName) {
		InputStream stream = ResourceIOUtils.class
				.getResourceAsStream(resourceName);
		if (stream == null) {
			throw new GeoPackageException("Failed to find resource: "
					+ resourceName);
		}
		List<String> statements = parseSQLStatements(stream);
		return statements;
	}

	/**
	 * Parse the SQL statements for the input stream
	 * 
	 * @param stream
	 *            input stream
	 * @return list of sql statements
	 */
	public static List<String> parseSQLStatements(final InputStream stream) {

		List<String> statements = new ArrayList<>();

		// Use multiple newlines as the delimiter
		Scanner s = new Scanner(stream);
		try {
			s.useDelimiter(Pattern.compile("\\n\\s*\\n"));
			// Parse and add each statement
			while (s.hasNext()) {
				String statement = s.next().trim();
				statements.add(statement);
			}
		} finally {
			s.close();
		}

		return statements;
	}

}
