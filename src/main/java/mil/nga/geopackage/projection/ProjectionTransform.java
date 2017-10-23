package mil.nga.geopackage.projection;

import java.util.ArrayList;
import java.util.List;

import mil.nga.geopackage.BoundingBox;
import mil.nga.wkb.geom.Geometry;
import mil.nga.wkb.geom.Point;

import org.osgeo.proj4j.CoordinateTransform;
import org.osgeo.proj4j.CoordinateTransformFactory;
import org.osgeo.proj4j.ProjCoordinate;

/**
 * Projection transform wrapper
 * 
 * @author osbornb
 */
public class ProjectionTransform {

	/**
	 * Coordinate transform factory
	 */
	private static CoordinateTransformFactory ctFactory = new CoordinateTransformFactory();

	/**
	 * From Projection
	 */
	private final Projection fromProjection;

	/**
	 * To Projection
	 */
	private final Projection toProjection;

	/**
	 * Coordinate transform
	 */
	private final CoordinateTransform transform;

	/**
	 * Constructor
	 * 
	 * @param fromProjection
	 * @param toProjection
	 */
	ProjectionTransform(Projection fromProjection, Projection toProjection) {
		this.fromProjection = fromProjection;
		this.toProjection = toProjection;
		this.transform = ctFactory.createTransform(fromProjection.getCrs(),
				toProjection.getCrs());
	}

	/**
	 * Transform the projected coordinate
	 * 
	 * @param from
	 *            from coordinate
	 * @return to coordinate
	 */
	public ProjCoordinate transform(ProjCoordinate from) {
		ProjCoordinate to = new ProjCoordinate();
		transform.transform(from, to);
		return to;
	}

	/**
	 * Transform the projected point
	 * 
	 * @param from
	 *            point
	 * @return projected point
	 */
	public Point transform(Point from) {

		GeometryProjectionTransform geometryTransform = new GeometryProjectionTransform(
				this);
		Point to = geometryTransform.transform(from);

		return to;
	}

	/**
	 * Transform a list of points
	 * 
	 * @param from
	 *            points to transform
	 * @return transformed points
	 * @since 1.4.0
	 */
	public List<Point> transform(List<Point> from) {

		List<Point> to = new ArrayList<>();

		GeometryProjectionTransform geometryTransform = new GeometryProjectionTransform(
				this);
		for (Point fromPoint : from) {
			Point toPoint = geometryTransform.transform(fromPoint);
			to.add(toPoint);
		}

		return to;
	}

	/**
	 * Transform the projected geometry
	 * 
	 * @param from
	 *            geometry
	 * @return projected geometry
	 * @since 1.1.3
	 */
	public Geometry transform(Geometry from) {

		GeometryProjectionTransform geometryTransform = new GeometryProjectionTransform(
				this);
		Geometry to = geometryTransform.transform(from);

		return to;
	}

	/**
	 * Transform the bounding box
	 * 
	 * @param boundingBox
	 *            bounding box
	 * @return bounding box
	 */
	public BoundingBox transform(BoundingBox boundingBox) {

		ProjCoordinate lowerLeft = new ProjCoordinate(
				boundingBox.getMinLongitude(), boundingBox.getMinLatitude());
		ProjCoordinate lowerRight = new ProjCoordinate(
				boundingBox.getMaxLongitude(), boundingBox.getMinLatitude());
		ProjCoordinate upperRight = new ProjCoordinate(
				boundingBox.getMaxLongitude(), boundingBox.getMaxLatitude());
		ProjCoordinate upperLeft = new ProjCoordinate(
				boundingBox.getMinLongitude(), boundingBox.getMaxLatitude());

		ProjCoordinate projectedLowerLeft = transform(lowerLeft);
		ProjCoordinate projectedLowerRight = transform(lowerRight);
		ProjCoordinate projectedUpperRight = transform(upperRight);
		ProjCoordinate projectedUpperLeft = transform(upperLeft);

		double minX = Math.min(projectedLowerLeft.x, projectedUpperLeft.x);
		double maxX = Math.max(projectedLowerRight.x, projectedUpperRight.x);
		double minY = Math.min(projectedLowerLeft.y, projectedLowerRight.y);
		double maxY = Math.max(projectedUpperLeft.y, projectedUpperRight.y);

		BoundingBox projectedBoundingBox = new BoundingBox(minX, maxX, minY,
				maxY);

		return projectedBoundingBox;
	}

	/**
	 * Transform a x and y location
	 * 
	 * @param x
	 *            x coordinate
	 * @param y
	 *            y coordinate
	 * @return transformed coordinates as [x, y]
	 */
	public double[] transform(double x, double y) {
		ProjCoordinate fromCoord = new ProjCoordinate(x, y);
		ProjCoordinate toCoord = transform(fromCoord);
		return new double[] { toCoord.x, toCoord.y };
	}

	/**
	 * Get the from projection in the transform
	 * 
	 * @return from projection
	 */
	public Projection getFromProjection() {
		return fromProjection;
	}

	/**
	 * Get the to projection in the transform
	 * 
	 * @return to projection
	 */
	public Projection getToProjection() {
		return toProjection;
	}

	/**
	 * Get the transform
	 * 
	 * @return transform
	 */
	public CoordinateTransform getTransform() {
		return transform;
	}

}
