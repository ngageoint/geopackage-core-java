package mil.nga.geopackage;

import org.locationtech.proj4j.units.Units;

import mil.nga.geopackage.tiles.TileBoundingBoxUtils;
import mil.nga.sf.GeometryEnvelope;
import mil.nga.sf.proj.ProjectionConstants;
import mil.nga.sf.proj.ProjectionTransform;

/**
 * Bounding Box with longitude and latitude ranges in degrees
 * 
 * @author osbornb
 */
public class BoundingBox {

	/**
	 * Min longitude in degrees
	 */
	private double minLongitude;

	/**
	 * Max longitude in degrees
	 */
	private double maxLongitude;

	/**
	 * Min latitude in degrees
	 */
	private double minLatitude;

	/**
	 * Max latitude in degrees
	 */
	private double maxLatitude;

	/**
	 * Constructor
	 */
	public BoundingBox() {
		this(-ProjectionConstants.WGS84_HALF_WORLD_LON_WIDTH,
				-ProjectionConstants.WGS84_HALF_WORLD_LAT_HEIGHT,
				ProjectionConstants.WGS84_HALF_WORLD_LON_WIDTH,
				ProjectionConstants.WGS84_HALF_WORLD_LAT_HEIGHT);
	}

	/**
	 * Constructor
	 * 
	 * @param minLongitude
	 *            min longitude
	 * @param minLatitude
	 *            min latitude
	 * @param maxLongitude
	 *            max longitude
	 * @param maxLatitude
	 *            max latitude
	 * @since 2.0.0
	 */
	public BoundingBox(double minLongitude, double minLatitude,
			double maxLongitude, double maxLatitude) {
		this.minLongitude = minLongitude;
		this.minLatitude = minLatitude;
		this.maxLongitude = maxLongitude;
		this.maxLatitude = maxLatitude;
	}

	/**
	 * Constructor
	 * 
	 * @param boundingBox
	 *            bounding box
	 * @since 1.1.6
	 */
	public BoundingBox(BoundingBox boundingBox) {
		this(boundingBox.getMinLongitude(), boundingBox.getMinLatitude(),
				boundingBox.getMaxLongitude(), boundingBox.getMaxLatitude());
	}

	/**
	 * Constructor
	 * 
	 * @param envelope
	 *            geometry envelope
	 * @since 2.0.0
	 */
	public BoundingBox(GeometryEnvelope envelope) {
		this(envelope.getMinX(), envelope.getMinY(), envelope.getMaxX(),
				envelope.getMaxY());
	}

	/**
	 * Get the min longitude
	 * 
	 * @return min longitude
	 */
	public double getMinLongitude() {
		return minLongitude;
	}

	/**
	 * Set the min longitude
	 * 
	 * @param minLongitude
	 *            min longitude
	 */
	public void setMinLongitude(double minLongitude) {
		this.minLongitude = minLongitude;
	}

	/**
	 * Get the max longitude
	 * 
	 * @return max longitude
	 */
	public double getMaxLongitude() {
		return maxLongitude;
	}

	/**
	 * Set the max longitude
	 * 
	 * @param maxLongitude
	 *            max longitude
	 */
	public void setMaxLongitude(double maxLongitude) {
		this.maxLongitude = maxLongitude;
	}

	/**
	 * Get the min latitude
	 * 
	 * @return min latitude
	 */
	public double getMinLatitude() {
		return minLatitude;
	}

	/**
	 * Set the min latitude
	 * 
	 * @param minLatitude
	 *            min latitude
	 */
	public void setMinLatitude(double minLatitude) {
		this.minLatitude = minLatitude;
	}

	/**
	 * Get the max latitude
	 * 
	 * @return max latitude
	 */
	public double getMaxLatitude() {
		return maxLatitude;
	}

	/**
	 * Set the max latitude
	 * 
	 * @param maxLatitude
	 *            max latitude
	 */
	public void setMaxLatitude(double maxLatitude) {
		this.maxLatitude = maxLatitude;
	}

	/**
	 * Get the longitude range
	 * 
	 * @return longitude range
	 * @since 3.5.0
	 */
	public double getLongitudeRange() {
		return getMaxLongitude() - getMinLongitude();
	}

	/**
	 * Get the latitude range
	 * 
	 * @return latitude range
	 * @since 3.5.0
	 */
	public double getLatitudeRange() {
		return getMaxLatitude() - getMinLatitude();
	}

	/**
	 * Build a Geometry Envelope from the bounding box
	 * 
	 * @return geometry envelope
	 * @since 1.1.0
	 */
	public GeometryEnvelope buildEnvelope() {
		return buildEnvelope(this);
	}

	/**
	 * Build a Geometry Envelope from the bounding box
	 * 
	 * @param boundingBox
	 *            bounding box
	 * @return geometry envelope
	 * @since 3.2.0
	 */
	public static GeometryEnvelope buildEnvelope(BoundingBox boundingBox) {
		GeometryEnvelope envelope = new GeometryEnvelope();
		envelope.setMinX(boundingBox.getMinLongitude());
		envelope.setMaxX(boundingBox.getMaxLongitude());
		envelope.setMinY(boundingBox.getMinLatitude());
		envelope.setMaxY(boundingBox.getMaxLatitude());
		return envelope;
	}

	/**
	 * If the bounding box spans the Anti-Meridian, attempt to get a
	 * complementary bounding box using the max longitude of the unit projection
	 *
	 * @param maxProjectionLongitude
	 *            max longitude of the world for the current bounding box units
	 *
	 * @return complementary bounding box or nil if none
	 * @since 2.0.0
	 */
	public BoundingBox complementary(double maxProjectionLongitude) {

		BoundingBox complementary = null;

		Double adjust = null;

		if (this.maxLongitude > maxProjectionLongitude) {
			if (this.minLongitude >= -maxProjectionLongitude) {
				adjust = -2 * maxProjectionLongitude;
			}
		} else if (this.minLongitude < -maxProjectionLongitude) {
			if (this.maxLongitude <= maxProjectionLongitude) {
				adjust = 2 * maxProjectionLongitude;
			}
		}

		if (adjust != null) {
			complementary = new BoundingBox(this);
			complementary
					.setMinLongitude(complementary.getMinLongitude() + adjust);
			complementary
					.setMaxLongitude(complementary.getMaxLongitude() + adjust);
		}

		return complementary;
	}

	/**
	 * If the bounding box spans the Anti-Meridian, attempt to get a
	 * complementary WGS84 bounding box
	 *
	 * @return complementary bounding box or nil if none
	 * @since 2.0.0
	 */
	public BoundingBox complementaryWgs84() {
		return complementary(ProjectionConstants.WGS84_HALF_WORLD_LON_WIDTH);
	}

	/**
	 * If the bounding box spans the Anti-Meridian, attempt to get a
	 * complementary Web Mercator bounding box
	 *
	 * @return complementary bounding box or nil if none
	 * @since 2.0.0
	 */
	public BoundingBox complementaryWebMercator() {
		return complementary(ProjectionConstants.WEB_MERCATOR_HALF_WORLD_WIDTH);
	}

	/**
	 * Bound the bounding box longitudes within the min and max possible
	 * projection values. This may result in a max longitude numerically lower
	 * than the min longitude.
	 * 
	 * @param maxProjectionLongitude
	 *            max longitude of the world for the current bounding box units
	 * @return bounded bounding box
	 * @since 2.0.0
	 */
	public BoundingBox boundCoordinates(double maxProjectionLongitude) {

		BoundingBox bounded = new BoundingBox(this);

		double minLongitude = (getMinLongitude() + maxProjectionLongitude)
				% (2 * maxProjectionLongitude) - maxProjectionLongitude;
		double maxLongitude = (getMaxLongitude() + maxProjectionLongitude)
				% (2 * maxProjectionLongitude) - maxProjectionLongitude;

		bounded.setMinLongitude(minLongitude);
		bounded.setMaxLongitude(maxLongitude);

		return bounded;
	}

	/**
	 * Bound the bounding box coordinates within WGS84 range values
	 * 
	 * @return bounded bounding box
	 * @since 2.0.0
	 */
	public BoundingBox boundWgs84Coordinates() {
		return boundCoordinates(ProjectionConstants.WGS84_HALF_WORLD_LON_WIDTH);
	}

	/**
	 * Bound the bounding box coordinates within Web Mercator range values
	 * 
	 * @return bounded bounding box
	 * @since 2.0.0
	 */
	public BoundingBox boundWebMercatorCoordinates() {
		return boundCoordinates(
				ProjectionConstants.WEB_MERCATOR_HALF_WORLD_WIDTH);
	}

	/**
	 * Expand the bounding box max longitude above the max possible projection
	 * value if needed to create a bounding box where the max longitude is
	 * numerically larger than the min longitude.
	 * 
	 * @param maxProjectionLongitude
	 *            max longitude of the world for the current bounding box units
	 * @return expanded bounding box
	 * @since 2.0.0
	 */
	public BoundingBox expandCoordinates(double maxProjectionLongitude) {

		BoundingBox expanded = new BoundingBox(this);

		double minLongitude = getMinLongitude();
		double maxLongitude = getMaxLongitude();

		if (minLongitude > maxLongitude) {
			int worldWraps = 1 + (int) ((minLongitude - maxLongitude)
					/ (2 * maxProjectionLongitude));
			maxLongitude += (worldWraps * 2 * maxProjectionLongitude);
			expanded.setMaxLongitude(maxLongitude);
		}

		return expanded;
	}

	/**
	 * Expand the bounding box max longitude above the max WGS84 projection
	 * value if needed to create a bounding box where the max longitude is
	 * numerically larger than the min longitude.
	 * 
	 * @return expanded bounding box
	 * @since 2.0.0
	 */
	public BoundingBox expandWgs84Coordinates() {
		return expandCoordinates(
				ProjectionConstants.WGS84_HALF_WORLD_LON_WIDTH);
	}

	/**
	 * Expand the bounding box max longitude above the max Web Mercator
	 * projection value if needed to create a bounding box where the max
	 * longitude is numerically larger than the min longitude.
	 * 
	 * @return expanded bounding box
	 * @since 2.0.0
	 */
	public BoundingBox expandWebMercatorCoordinates() {
		return expandCoordinates(
				ProjectionConstants.WEB_MERCATOR_HALF_WORLD_WIDTH);
	}

	/**
	 * Transform the bounding box using the provided projection transform
	 * 
	 * @param transform
	 *            projection transform
	 * @return transformed bounding box
	 * @since 3.0.0
	 */
	public BoundingBox transform(ProjectionTransform transform) {
		BoundingBox transformed = this;
		if (transform.isSameProjection()) {
			transformed = new BoundingBox(transformed);
		} else {
			if (transform.getFromProjection().isUnit(Units.DEGREES)) {
				transformed = TileBoundingBoxUtils
						.boundDegreesBoundingBoxWithWebMercatorLimits(
								transformed);
			}
			GeometryEnvelope envelope = buildEnvelope(transformed);
			GeometryEnvelope transformedEnvelope = transform
					.transform(envelope);
			transformed = new BoundingBox(transformedEnvelope);
		}
		return transformed;
	}

	/**
	 * Determine if intersects with the provided bounding box
	 *
	 * @param boundingBox
	 *            bounding box
	 * @return true if intersects
	 * @since 3.1.0
	 */
	public boolean intersects(BoundingBox boundingBox) {
		return overlap(boundingBox) != null;
	}

	/**
	 * Determine if intersects with the provided bounding box
	 *
	 * @param boundingBox
	 *            bounding box
	 * @param allowEmpty
	 *            allow empty ranges when determining intersection
	 *
	 * @return true if intersects
	 * @since 3.1.0
	 */
	public boolean intersects(BoundingBox boundingBox, boolean allowEmpty) {
		return overlap(boundingBox, allowEmpty) != null;
	}

	/**
	 * Get the overlapping bounding box with the provided bounding box
	 *
	 * @param boundingBox
	 *            bounding box
	 * @return bounding box
	 * @since 3.1.0
	 */
	public BoundingBox overlap(BoundingBox boundingBox) {
		return overlap(boundingBox, false);
	}

	/**
	 * Get the overlapping bounding box with the provided bounding box
	 *
	 * @param boundingBox
	 *            bounding box
	 * @param allowEmpty
	 *            allow empty ranges when determining overlap
	 *
	 * @return bounding box
	 * @since 3.1.0
	 */
	public BoundingBox overlap(BoundingBox boundingBox, boolean allowEmpty) {

		double minLongitude = Math.max(getMinLongitude(),
				boundingBox.getMinLongitude());
		double maxLongitude = Math.min(getMaxLongitude(),
				boundingBox.getMaxLongitude());
		double minLatitude = Math.max(getMinLatitude(),
				boundingBox.getMinLatitude());
		double maxLatitude = Math.min(getMaxLatitude(),
				boundingBox.getMaxLatitude());

		BoundingBox overlap = null;

		if ((minLongitude < maxLongitude && minLatitude < maxLatitude)
				|| (allowEmpty && minLongitude <= maxLongitude
						&& minLatitude <= maxLatitude)) {
			overlap = new BoundingBox(minLongitude, minLatitude, maxLongitude,
					maxLatitude);
		}

		return overlap;
	}

	/**
	 * Get the union bounding box with the provided bounding box
	 *
	 * @param boundingBox
	 *            bounding box
	 * @return bounding box
	 * @since 3.1.0
	 */
	public BoundingBox union(BoundingBox boundingBox) {

		double minLongitude = Math.min(getMinLongitude(),
				boundingBox.getMinLongitude());
		double maxLongitude = Math.max(getMaxLongitude(),
				boundingBox.getMaxLongitude());
		double minLatitude = Math.min(getMinLatitude(),
				boundingBox.getMinLatitude());
		double maxLatitude = Math.max(getMaxLatitude(),
				boundingBox.getMaxLatitude());

		BoundingBox union = null;

		if (minLongitude < maxLongitude && minLatitude < maxLatitude) {
			union = new BoundingBox(minLongitude, minLatitude, maxLongitude,
					maxLatitude);
		}

		return union;
	}

	/**
	 * Determine if inclusively contains the provided bounding box
	 *
	 * @param boundingBox
	 *            bounding box
	 * @return true if contains
	 * @since 3.1.0
	 */
	public boolean contains(BoundingBox boundingBox) {
		return getMinLongitude() <= boundingBox.getMinLongitude()
				&& getMaxLongitude() >= boundingBox.getMaxLongitude()
				&& getMinLatitude() <= boundingBox.getMinLatitude()
				&& getMaxLatitude() >= boundingBox.getMaxLatitude();
	}

	/**
	 * Expand the bounding box to an equally sized width and height bounding box
	 * 
	 * @return new square expanded bounding box
	 * @since 3.5.0
	 */
	public BoundingBox squareExpand() {
		return squareExpand(0.0);
	}

	/**
	 * Expand the bounding box to an equally sized width and height bounding box
	 * with optional empty edge buffer
	 * 
	 * @param bufferPercentage
	 *            bounding box edge buffer percentage. A value of 0.1 adds a 10%
	 *            buffer on each side of the squared bounding box.
	 * @return new square expanded bounding box
	 * @since 3.5.0
	 */
	public BoundingBox squareExpand(double bufferPercentage) {

		BoundingBox boundingBox = new BoundingBox(this);

		if (boundingBox.isPoint() && bufferPercentage > 0.0) {

			double longitudeExpand = Math.ulp(boundingBox.getMinLongitude());
			boundingBox.setMinLongitude(
					boundingBox.getMinLongitude() - longitudeExpand);
			boundingBox.setMaxLongitude(
					boundingBox.getMaxLongitude() + longitudeExpand);

			double latitudeExpand = Math.ulp(boundingBox.getMinLatitude());
			boundingBox.setMinLatitude(
					boundingBox.getMinLatitude() - latitudeExpand);
			boundingBox.setMaxLatitude(
					boundingBox.getMaxLatitude() + latitudeExpand);

		}

		double lonRange = boundingBox.getLongitudeRange();
		double latRange = boundingBox.getLatitudeRange();

		if (lonRange < latRange) {
			double halfDiff = (latRange - lonRange) / 2.0;
			boundingBox
					.setMinLongitude(boundingBox.getMinLongitude() - halfDiff);
			boundingBox
					.setMaxLongitude(boundingBox.getMaxLongitude() + halfDiff);
		} else if (latRange < lonRange) {
			double halfDiff = (lonRange - latRange) / 2.0;
			boundingBox.setMinLatitude(boundingBox.getMinLatitude() - halfDiff);
			boundingBox.setMaxLatitude(boundingBox.getMaxLatitude() + halfDiff);
		}

		double range = Math.max(Math.max(lonRange, latRange), Double.MIN_VALUE);
		double buffer = ((range / (1.0 - (2.0 * bufferPercentage))) - range)
				/ 2.0;

		boundingBox.setMinLongitude(boundingBox.getMinLongitude() - buffer);
		boundingBox.setMinLatitude(boundingBox.getMinLatitude() - buffer);
		boundingBox.setMaxLongitude(boundingBox.getMaxLongitude() + buffer);
		boundingBox.setMaxLatitude(boundingBox.getMaxLatitude() + buffer);

		return boundingBox;
	}

	/**
	 * Determine if the bounding box is of a single point
	 * 
	 * @return true if a single point bounds
	 * @since 3.5.0
	 */
	public boolean isPoint() {
		return Double.compare(minLongitude, maxLongitude) == 0
				&& Double.compare(minLatitude, maxLatitude) == 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(maxLatitude);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(maxLongitude);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(minLatitude);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(minLongitude);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BoundingBox other = (BoundingBox) obj;
		if (Double.doubleToLongBits(maxLatitude) != Double
				.doubleToLongBits(other.maxLatitude))
			return false;
		if (Double.doubleToLongBits(maxLongitude) != Double
				.doubleToLongBits(other.maxLongitude))
			return false;
		if (Double.doubleToLongBits(minLatitude) != Double
				.doubleToLongBits(other.minLatitude))
			return false;
		if (Double.doubleToLongBits(minLongitude) != Double
				.doubleToLongBits(other.minLongitude))
			return false;
		return true;
	}

}
