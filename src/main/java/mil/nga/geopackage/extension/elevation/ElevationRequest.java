package mil.nga.geopackage.extension.elevation;

import mil.nga.geopackage.BoundingBox;
import mil.nga.geopackage.tiles.TileBoundingBoxUtils;

public class ElevationRequest {

	private BoundingBox boundingBox;

	private boolean point;

	private BoundingBox projectedBoundingBox;

	public ElevationRequest(BoundingBox boundingBox) {
		this.boundingBox = boundingBox;
	}

	public ElevationRequest(double latitude, double longitude) {
		this(new BoundingBox(longitude, longitude, latitude, latitude));
		point = true;
	}

	public BoundingBox getBoundingBox() {
		return boundingBox;
	}

	public boolean isPoint() {
		return point;
	}

	public BoundingBox getProjectedBoundingBox() {
		return projectedBoundingBox;
	}

	public void setProjectedBoundingBox(BoundingBox projectedBoundingBox) {
		this.projectedBoundingBox = projectedBoundingBox;
	}

	public BoundingBox overlap(BoundingBox projected) {
		BoundingBox overlap = null;
		if (point) {
			if (projectedBoundingBox.getMinLatitude() >= projected
					.getMinLatitude()
					&& projectedBoundingBox.getMaxLatitude() <= projected
							.getMaxLatitude()
					&& projectedBoundingBox.getMinLongitude() >= projected
							.getMinLongitude()
					&& projectedBoundingBox.getMaxLongitude() <= projected
							.getMaxLongitude()) {
				overlap = projectedBoundingBox;
			}
		} else {
			overlap = TileBoundingBoxUtils.overlap(projectedBoundingBox,
					projected);
		}
		return overlap;
	}

}
