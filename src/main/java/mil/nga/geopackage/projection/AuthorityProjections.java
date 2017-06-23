package mil.nga.geopackage.projection;

import java.util.HashMap;
import java.util.Map;

/**
 * Collection of projections for a single coordinate authority
 * 
 * @author osbornb
 * @since 1.2.3
 */
public class AuthorityProjections {

	/**
	 * Coordinate authority
	 */
	private final String authority;

	/**
	 * Projections by code
	 */
	private Map<String, Projection> projections = new HashMap<>();

	/**
	 * Constructor
	 * 
	 * @param authority
	 *            coordinate authority
	 */
	public AuthorityProjections(String authority) {
		this.authority = authority;
	}

	/**
	 * Get the authority
	 * 
	 * @return authority
	 */
	public String getAuthority() {
		return authority;
	}

	/**
	 * Get the projection for the code
	 * 
	 * @param code
	 *            coordinate code
	 * @return projection
	 */
	public Projection getProjection(String code) {
		return projections.get(code);
	}

	/**
	 * Add the projection to the authority
	 * 
	 * @param projection
	 *            projection
	 */
	public void addProjection(Projection projection) {
		projections.put(projection.getCode(), projection);
	}

}