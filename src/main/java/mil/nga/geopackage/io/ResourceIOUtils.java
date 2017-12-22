package mil.nga.geopackage.io;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import mil.nga.geopackage.GeoPackageException;

// TODO javadoc
public class ResourceIOUtils {

	public static List<String> parseSQLStatements(String path, String name) {
		return parseSQLStatements("/" + path + "/" + name);
	}

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
