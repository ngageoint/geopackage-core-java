package mil.nga.geopackage;

import mil.nga.geopackage.projection.ProjectionConstants;
import mil.nga.wkb.geom.GeometryEnvelope;

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
				ProjectionConstants.WGS84_HALF_WORLD_LON_WIDTH,
				-ProjectionConstants.WGS84_HALF_WORLD_LAT_HEIGHT,
				ProjectionConstants.WGS84_HALF_WORLD_LAT_HEIGHT);
	}

	/**
	 * Constructor
	 * 
	 * @param minLongitude
	 * @param maxLongitude
	 * @param minLatitude
	 * @param maxLatitude
	 */
	public BoundingBox(double minLongitude, double maxLongitude,
			double minLatitude, double maxLatitude) {
		this.minLongitude = minLongitude;
		this.maxLongitude = maxLongitude;
		this.minLatitude = minLatitude;
		this.maxLatitude = maxLatitude;
	}

	/**
	 * Constructor
	 * 
	 * @param boundingBox
	 * @since 1.1.6
	 */
	public BoundingBox(BoundingBox boundingBox) {
		this(boundingBox.getMinLongitude(), boundingBox.getMaxLongitude(),
				boundingBox.getMinLatitude(), boundingBox.getMaxLatitude());
	}

	/**
	 * Constructor
	 * 
	 * @param envelope
	 *            geometry envelope
	 * @since 1.3.2
	 */
	public BoundingBox(GeometryEnvelope envelope) {
		this(envelope.getMinX(), envelope.getMaxX(), envelope.getMinY(),
				envelope.getMaxY());
	}

	public double getMinLongitude() {
		return minLongitude;
	}

	public void setMinLongitude(double minLongitude) {
		this.minLongitude = minLongitude;
	}

	public double getMaxLongitude() {
		return maxLongitude;
	}

	public void setMaxLongitude(double maxLongitude) {
		this.maxLongitude = maxLongitude;
	}

	public double getMinLatitude() {
		return minLatitude;
	}

	public void setMinLatitude(double minLatitude) {
		this.minLatitude = minLatitude;
	}

	public double getMaxLatitude() {
		return maxLatitude;
	}

	public void setMaxLatitude(double maxLatitude) {
		this.maxLatitude = maxLatitude;
	}

	/**
	 * Build a Geometry Envelope from the bounding box
	 * 
	 * @return geometry envelope
	 * @since 1.1.0
	 */
	public GeometryEnvelope buildEnvelope() {
		GeometryEnvelope envelope = new GeometryEnvelope();
		envelope.setMinX(minLongitude);
		envelope.setMaxX(maxLongitude);
		envelope.setMinY(minLatitude);
		envelope.setMaxY(maxLatitude);
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
	 * @since 1.3.2
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
			complementary.setMinLongitude(complementary.getMinLongitude()
					+ adjust);
			complementary.setMaxLongitude(complementary.getMaxLongitude()
					+ adjust);
		}

		return complementary;
	}

	/**
	 * If the bounding box spans the Anti-Meridian, attempt to get a
	 * complementary WGS84 bounding box
	 *
	 * @return complementary bounding box or nil if none
	 * @since 1.3.2
	 */
	public BoundingBox complementaryWgs84() {
		return complementary(ProjectionConstants.WGS84_HALF_WORLD_LON_WIDTH);
	}

	/**
	 * If the bounding box spans the Anti-Meridian, attempt to get a
	 * complementary Web Mercator bounding box
	 *
	 * @return complementary bounding box or nil if none
	 * @since 1.3.2
	 */
	public BoundingBox complementaryWebMercator() {
		return complementary(ProjectionConstants.WEB_MERCATOR_HALF_WORLD_WIDTH);
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
