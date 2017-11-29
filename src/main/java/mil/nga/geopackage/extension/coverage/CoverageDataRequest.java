package mil.nga.geopackage.extension.coverage;

import mil.nga.geopackage.BoundingBox;
import mil.nga.geopackage.tiles.TileBoundingBoxUtils;

/**
 * Coverage Data request to retrieve coverage data values for a point or
 * bounding box
 * 
 * @author osbornb
 * @since 2.0.1
 */
public class CoverageDataRequest {

	/**
	 * Bounding box
	 */
	private BoundingBox boundingBox;

	/**
	 * Point flag, true when a single point request
	 */
	private boolean point;

	/**
	 * Bounding box projected to the coverage data projection
	 */
	private BoundingBox projectedBoundingBox;

	/**
	 * Constructor
	 * 
	 * @param boundingBox
	 *            bounding box
	 */
	public CoverageDataRequest(BoundingBox boundingBox) {
		this.boundingBox = boundingBox;
	}

	/**
	 * Constructor
	 * 
	 * @param latitude
	 *            latitude coordinate
	 * @param longitude
	 *            longitude coordinate
	 */
	public CoverageDataRequest(double latitude, double longitude) {
		this(new BoundingBox(longitude, latitude, longitude, latitude));
		point = true;
	}

	/**
	 * Get the bounding box
	 * 
	 * @return bounding box
	 */
	public BoundingBox getBoundingBox() {
		return boundingBox;
	}

	/**
	 * Is the request for a single point
	 * 
	 * @return true if a point request
	 */
	public boolean isPoint() {
		return point;
	}

	/**
	 * Get the projected bounding box
	 * 
	 * @return projected bounding box
	 */
	public BoundingBox getProjectedBoundingBox() {
		return projectedBoundingBox;
	}

	/**
	 * Set the projected bounding box
	 * 
	 * @param projectedBoundingBox
	 *            projected bounding box
	 */
	public void setProjectedBoundingBox(BoundingBox projectedBoundingBox) {
		this.projectedBoundingBox = projectedBoundingBox;
	}

	/**
	 * Get the bounding box overlap between the projected bounding box and the
	 * coverage data bounding box
	 * 
	 * @param projectedCoverage
	 *            projected coverage
	 * @return overlap bounding box
	 */
	public BoundingBox overlap(BoundingBox projectedCoverage) {
		BoundingBox overlap = null;
		if (point) {
			overlap = projectedBoundingBox;
		} else {
			overlap = TileBoundingBoxUtils.overlap(projectedBoundingBox,
					projectedCoverage);
		}
		return overlap;
	}

}
