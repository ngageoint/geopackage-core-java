package mil.nga.geopackage.projection;

import java.lang.reflect.Field;

import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.core.srs.SpatialReferenceSystem;

import org.osgeo.proj4j.CoordinateReferenceSystem;
import org.osgeo.proj4j.units.Unit;
import org.osgeo.proj4j.units.Units;

/**
 * Single Projection for an authority and code
 * 
 * @author osbornb
 */
public class Projection {

	/**
	 * Projection authority
	 */
	private final String authority;

	/**
	 * Coordinate code
	 */
	private final String code;

	/**
	 * Coordinate Reference System
	 */
	private final CoordinateReferenceSystem crs;

	/**
	 * Constructor
	 * 
	 * @param authority
	 *            coordinate authority
	 * @param code
	 *            coordinate code
	 * @param crs
	 *            crs
	 * @since 1.3.0
	 */
	public Projection(String authority, long code, CoordinateReferenceSystem crs) {
		this(authority, String.valueOf(code), crs);
	}

	/**
	 * Constructor
	 * 
	 * @param authority
	 *            coordinate authority
	 * @param code
	 *            coordinate code
	 * @param crs
	 *            crs
	 * @since 1.3.0
	 */
	public Projection(String authority, String code,
			CoordinateReferenceSystem crs) {
		if (authority == null || code == null || crs == null) {
			throw new IllegalArgumentException(
					"All projection arguments are required. authority: "
							+ authority + ", code: " + code + ", crs: " + crs);
		}
		this.authority = authority;
		this.code = code;
		this.crs = crs;
	}

	/**
	 * Get the coordinate authority
	 * 
	 * @return authority
	 * @since 1.3.0
	 */
	public String getAuthority() {
		return authority;
	}

	/**
	 * Get the coordinate code
	 * 
	 * @return code
	 * @since 1.3.0
	 */
	public String getCode() {
		return code;
	}

	/**
	 * Get the Coordinate Reference System
	 * 
	 * @return Coordinate Reference System
	 */
	public CoordinateReferenceSystem getCrs() {
		return crs;
	}

	/**
	 * Get the transformation from this Projection to the EPSG code. Each thread
	 * of execution should have it's own transformation.
	 * 
	 * @param epsg
	 *            epsg
	 * @return transform
	 */
	public ProjectionTransform getTransformation(long epsg) {
		return getTransformation(ProjectionConstants.AUTHORITY_EPSG, epsg);
	}

	/**
	 * Get the transformation from this Projection to the authority and
	 * coordinate code. Each thread of execution should have it's own
	 * transformation.
	 * 
	 * @param authority
	 *            coordinate authority
	 * @param code
	 *            coordinate code
	 * @return transform
	 * @since 1.3.0
	 */
	public ProjectionTransform getTransformation(String authority, long code) {
		Projection projectionTo = ProjectionFactory.getProjection(authority,
				code);
		return getTransformation(projectionTo);
	}

	/**
	 * Get the transformation from this Projection to the Spatial Reference
	 * System. Each thread of execution should have it's own transformation.
	 * 
	 * @param srs
	 *            spatial reference system
	 * @return projection transform
	 * @since 1.1.8
	 */
	public ProjectionTransform getTransformation(SpatialReferenceSystem srs) {
		Projection projectionTo = ProjectionFactory.getProjection(srs);
		return getTransformation(projectionTo);
	}

	/**
	 * Get the transformation from this Projection to the provided projection.
	 * Each thread of execution should have it's own transformation.
	 * 
	 * @param projection
	 *            projection
	 * @return transform
	 */
	public ProjectionTransform getTransformation(Projection projection) {
		return new ProjectionTransform(this, projection);
	}

	/**
	 * Convert the value to meters
	 * 
	 * @param value
	 *            value
	 * @return meters
	 */
	public double toMeters(double value) {
		return value / crs.getProjection().getFromMetres();
	}

	/**
	 * Get the units of this projection
	 * 
	 * @return the projection unit
	 * @since 1.2.0
	 */
	public Unit getUnit() {
		Unit unit = null;
		try {
			// The unit is currently not publicly available, use reflection
			Field f = org.osgeo.proj4j.proj.Projection.class
					.getDeclaredField("unit");
			f.setAccessible(true);
			unit = (Unit) f.get(crs.getProjection());
			if (unit == null) {
				unit = Units.METRES;
			}
		} catch (Exception e) {
			throw new GeoPackageException(
					"Failed to get projection unit for authority: " + authority
							+ ", code: " + code, e);
		}
		return unit;
	}

	/**
	 * Check if this projection is equal to the authority and code
	 * 
	 * @param authority
	 *            coordinate authority
	 * @param code
	 *            coordinate code
	 * @return true if equal
	 * @since 1.3.0
	 */
	public boolean equals(String authority, long code) {
		return equals(authority, String.valueOf(code));
	}

	/**
	 * Check if this projection is equal to the authority and code
	 * 
	 * @param authority
	 *            coordinate authority
	 * @param code
	 *            coordinate code
	 * @return true if equal
	 * @since 1.3.0
	 */
	public boolean equals(String authority, String code) {
		return this.authority.equals(authority) && this.code.equals(code);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.3.0
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + authority.hashCode();
		result = prime * result + code.hashCode();
		return result;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * Based upon {@link #getAuthority()} and {@link #getCode()}
	 * 
	 * @since 1.3.0
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Projection other = (Projection) obj;
		return equals(other.authority, other.code);
	}

}
