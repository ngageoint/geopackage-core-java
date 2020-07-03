package mil.nga.geopackage.geom;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

import mil.nga.geopackage.GeoPackageConstants;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.extension.GeometryExtensions;
import mil.nga.geopackage.property.GeoPackageProperties;
import mil.nga.geopackage.property.PropertyConstants;
import mil.nga.sf.Geometry;
import mil.nga.sf.GeometryEnvelope;
import mil.nga.sf.proj.ProjectionTransform;
import mil.nga.sf.util.ByteReader;
import mil.nga.sf.util.ByteWriter;
import mil.nga.sf.util.GeometryEnvelopeBuilder;
import mil.nga.sf.util.filter.GeometryFilter;
import mil.nga.sf.util.filter.PointFiniteFilter;
import mil.nga.sf.wkb.GeometryReader;
import mil.nga.sf.wkb.GeometryWriter;

/**
 * GeoPackage Geometry Data
 * 
 * @author osbornb
 */
public class GeoPackageGeometryData {

	/**
	 * Point filter
	 */
	private static GeometryFilter geometryFilter = new PointFiniteFilter();

	/**
	 * Default SRS Id, Undefined Cartesian (-1)
	 */
	private static int defaultSrsId = GeoPackageProperties.getIntegerProperty(
			PropertyConstants.UNDEFINED_CARTESIAN, PropertyConstants.SRS_ID);

	/**
	 * Default byte order
	 */
	private static ByteOrder defaultByteOrder = ByteOrder.BIG_ENDIAN;

	/**
	 * Bytes
	 */
	private byte[] bytes;

	/**
	 * True if an extended geometry, false if standard
	 */
	private boolean extended = false;

	/**
	 * True if the geometry is empty
	 */
	private boolean empty = true;

	/**
	 * Byte ordering, big or little endian
	 */
	private ByteOrder byteOrder = defaultByteOrder;

	/**
	 * Spatial Reference System Id
	 */
	private int srsId;

	/**
	 * Envelope
	 */
	private GeometryEnvelope envelope;

	/**
	 * Well-Known Binary Geometry index of where the bytes start
	 */
	private int wkbGeometryIndex;

	/**
	 * Geometry
	 */
	private Geometry geometry;

	/**
	 * Get geometry filter
	 * 
	 * @return geometry filter
	 * @since 4.0.0
	 */
	public static GeometryFilter getGeometryFilter() {
		return geometryFilter;
	}

	/**
	 * Set the geometry filter
	 * 
	 * @param geometryFilter
	 *            geometry filter
	 * @since 4.0.0
	 */
	public static void setGeometryFilter(GeometryFilter geometryFilter) {
		GeoPackageGeometryData.geometryFilter = geometryFilter;
	}

	/**
	 * Get the default SRS id
	 * 
	 * @return SRS id
	 * @since 4.0.0
	 */
	public static int getDefaultSrsId() {
		return defaultSrsId;
	}

	/**
	 * Set the default SRS id
	 * 
	 * @param defaultSrsId
	 *            SRS id
	 * @since 4.0.0
	 */
	public static void setDefaultSrsId(int defaultSrsId) {
		GeoPackageGeometryData.defaultSrsId = defaultSrsId;
	}

	/**
	 * Get the default byte order
	 * 
	 * @return byte order
	 * @since 4.0.0
	 */
	public static ByteOrder getDefaultByteOrder() {
		return defaultByteOrder;
	}

	/**
	 * Set the default byte order
	 * 
	 * @param defaultByteOrder
	 *            byte order
	 * @since 4.0.0
	 */
	public static void setDefaultByteOrder(ByteOrder defaultByteOrder) {
		GeoPackageGeometryData.defaultByteOrder = defaultByteOrder;
	}

	/**
	 * Create geometry data, default SRS Id of {@link #getDefaultSrsId}
	 * 
	 * @return geometry data
	 * @since 4.0.0
	 */
	public static GeoPackageGeometryData create() {
		return new GeoPackageGeometryData();
	}

	/**
	 * Create geometry data, default SRS Id of {@link #getDefaultSrsId}
	 * 
	 * @param geometry
	 *            geometry
	 * @return geometry data
	 * @since 4.0.0
	 */
	public static GeoPackageGeometryData create(Geometry geometry) {
		return new GeoPackageGeometryData(geometry);
	}

	/**
	 * Create geometry data and build the envelope, default SRS Id of
	 * {@link #getDefaultSrsId}
	 * 
	 * @param geometry
	 *            geometry
	 * @return geometry data
	 * @since 4.0.0
	 */
	public static GeoPackageGeometryData createAndBuildEnvelope(
			Geometry geometry) {
		return new GeoPackageGeometryData(geometry, true);
	}

	/**
	 * Create geometry data
	 * 
	 * @param srsId
	 *            SRS id
	 * @return geometry data
	 * @since 4.0.0
	 */
	public static GeoPackageGeometryData create(long srsId) {
		return new GeoPackageGeometryData(srsId);
	}

	/**
	 * Create geometry data
	 * 
	 * @param srsId
	 *            SRS id
	 * @param geometry
	 *            geometry
	 * @return geometry data
	 * @since 4.0.0
	 */
	public static GeoPackageGeometryData create(long srsId, Geometry geometry) {
		return new GeoPackageGeometryData(srsId, geometry);
	}

	/**
	 * Create geometry data and build the envelope
	 * 
	 * @param srsId
	 *            SRS id
	 * @param geometry
	 *            geometry
	 * @return geometry data
	 * @since 4.0.0
	 */
	public static GeoPackageGeometryData createAndBuildEnvelope(long srsId,
			Geometry geometry) {
		return new GeoPackageGeometryData(srsId, geometry, true);
	}

	/**
	 * Create geometry data and write the GeoPackage geometry bytes, default SRS
	 * Id of {@link #getDefaultSrsId()}
	 * 
	 * @param geometry
	 *            geometry
	 * @return geometry data
	 * @throws IOException
	 *             upon failure to write bytes
	 * @since 4.0.0
	 */
	public static GeoPackageGeometryData createAndWrite(Geometry geometry)
			throws IOException {
		return writeBytes(create(geometry));
	}

	/**
	 * Create geometry data, build the envelope, and write the GeoPackage
	 * geometry bytes, default SRS Id of {@link #getDefaultSrsId()}
	 * 
	 * @param geometry
	 *            geometry
	 * @return geometry data
	 * @throws IOException
	 *             upon failure to write bytes
	 * @since 4.0.0
	 */
	public static GeoPackageGeometryData createBuildEnvelopeAndWrite(
			Geometry geometry) throws IOException {
		return writeBytes(createAndBuildEnvelope(geometry));
	}

	/**
	 * Create geometry data and write the GeoPackage geometry bytes
	 * 
	 * @param srsId
	 *            SRS id
	 * @param geometry
	 *            geometry
	 * @return geometry data
	 * @throws IOException
	 *             upon failure to write bytes
	 * @since 4.0.0
	 */
	public static GeoPackageGeometryData createAndWrite(long srsId,
			Geometry geometry) throws IOException {
		return writeBytes(create(srsId, geometry));
	}

	/**
	 * Create geometry data, build the envelope, and write the GeoPackage
	 * geometry bytes
	 * 
	 * @param srsId
	 *            SRS id
	 * @param geometry
	 *            geometry
	 * @return geometry data
	 * @throws IOException
	 *             upon failure to write bytes
	 * @since 4.0.0
	 */
	public static GeoPackageGeometryData createBuildEnvelopeAndWrite(long srsId,
			Geometry geometry) throws IOException {
		return writeBytes(createAndBuildEnvelope(srsId, geometry));
	}

	/**
	 * Create the geometry data from GeoPackage geometry bytes
	 * 
	 * @param bytes
	 *            GeoPackage geometry bytes
	 * @return geometry data
	 * @since 4.0.0
	 */
	public static GeoPackageGeometryData create(byte[] bytes) {
		return new GeoPackageGeometryData(bytes);
	}

	/**
	 * Create the geometry data, default SRS Id of {@link #getDefaultSrsId()}
	 * 
	 * @param geometry
	 *            geometry
	 * @param envelope
	 *            geometry envelope
	 * @return geometry data
	 * @since 4.0.0
	 */
	public static GeoPackageGeometryData create(Geometry geometry,
			GeometryEnvelope envelope) {
		return new GeoPackageGeometryData(geometry, envelope);
	}

	/**
	 * Create the geometry data
	 * 
	 * @param srsId
	 *            SRS id
	 * @param geometry
	 *            geometry
	 * @param envelope
	 *            geometry envelope
	 * @return geometry data
	 * @since 4.0.0
	 */
	public static GeoPackageGeometryData create(long srsId, Geometry geometry,
			GeometryEnvelope envelope) {
		return new GeoPackageGeometryData(srsId, geometry, envelope);
	}

	/**
	 * Copy the geometry data and create
	 * 
	 * @param geometryData
	 *            geometry data
	 * @return geometry data
	 * @since 4.0.0
	 */
	public static GeoPackageGeometryData create(
			GeoPackageGeometryData geometryData) {
		return new GeoPackageGeometryData(geometryData);
	}

	/**
	 * Create the geometry data from Well-Known Bytes, default SRS Id of
	 * {@link #getDefaultSrsId}
	 * 
	 * @param bytes
	 *            well-known bytes
	 * @return geometry data
	 * @throws IOException
	 *             upon failure to read bytes
	 * @since 4.0.0
	 */
	public static GeoPackageGeometryData createFromWkb(byte[] bytes)
			throws IOException {
		return createFromWkb(defaultSrsId, bytes);
	}

	/**
	 * Create the geometry data from Well-Known Bytes and build the envelope,
	 * default SRS Id of {@link #getDefaultSrsId}
	 * 
	 * @param bytes
	 *            well-known bytes
	 * @return geometry data
	 * @throws IOException
	 *             upon failure to read bytes
	 * @since 4.0.0
	 */
	public static GeoPackageGeometryData createFromWkbAndBuildEnvelope(
			byte[] bytes) throws IOException {
		return createFromWkbAndBuildEnvelope(defaultSrsId, bytes);
	}

	/**
	 * Create the geometry data from Well-Known Bytes
	 * 
	 * @param srsId
	 *            SRS id
	 * @param bytes
	 *            well-known bytes
	 * @return geometry data
	 * @throws IOException
	 *             upon failure to read bytes
	 * @since 4.0.0
	 */
	public static GeoPackageGeometryData createFromWkb(long srsId, byte[] bytes)
			throws IOException {
		return create(srsId, createGeometryFromWkb(bytes));
	}

	/**
	 * Create the geometry data from Well-Known Bytes and build the envelope
	 * 
	 * @param srsId
	 *            SRS id
	 * @param bytes
	 *            well-known bytes
	 * @return geometry data
	 * @throws IOException
	 *             upon failure to read bytes
	 * @since 4.0.0
	 */
	public static GeoPackageGeometryData createFromWkbAndBuildEnvelope(
			long srsId, byte[] bytes) throws IOException {
		return createAndBuildEnvelope(srsId, createGeometryFromWkb(bytes));
	}

	/**
	 * Create the geometry data from Well-Known Bytes and write the GeoPackage
	 * geometry bytes, default SRS Id of {@link #getDefaultSrsId}
	 * 
	 * @param bytes
	 *            well-known bytes
	 * @return geometry data
	 * @throws IOException
	 *             upon failure to read or write bytes
	 * @since 4.0.0
	 */
	public static GeoPackageGeometryData createFromWkbAndWrite(byte[] bytes)
			throws IOException {
		return writeBytes(createFromWkb(bytes));
	}

	/**
	 * Create the geometry data from Well-Known Bytes, build the envelope, and
	 * write the GeoPackage geometry bytes, default SRS Id of
	 * {@link #getDefaultSrsId}
	 * 
	 * @param bytes
	 *            well-known bytes
	 * @return geometry data
	 * @throws IOException
	 *             upon failure to read or write bytes
	 * @since 4.0.0
	 */
	public static GeoPackageGeometryData createFromWkbBuildEnvelopeAndWrite(
			byte[] bytes) throws IOException {
		return writeBytes(createFromWkbAndBuildEnvelope(bytes));
	}

	/**
	 * Create the geometry data from Well-Known Bytes and write the GeoPackage
	 * geometry bytes
	 * 
	 * @param srsId
	 *            SRS id
	 * @param bytes
	 *            well-known bytes
	 * @return geometry data
	 * @throws IOException
	 *             upon failure to read or write bytes
	 * @since 4.0.0
	 */
	public static GeoPackageGeometryData createFromWkbAndWrite(long srsId,
			byte[] bytes) throws IOException {
		return writeBytes(createFromWkb(srsId, bytes));
	}

	/**
	 * Create the geometry data from Well-Known Bytes, build the envelope, and
	 * write the GeoPackage geometry bytes
	 * 
	 * @param srsId
	 *            SRS id
	 * @param bytes
	 *            well-known bytes
	 * @return geometry data
	 * @throws IOException
	 *             upon failure to read or write bytes
	 * @since 4.0.0
	 */
	public static GeoPackageGeometryData createFromWkbBuildEnvelopeAndWrite(
			long srsId, byte[] bytes) throws IOException {
		return writeBytes(createFromWkbAndBuildEnvelope(srsId, bytes));
	}

	/**
	 * Create a geometry from Well-Known Bytes
	 * 
	 * @param bytes
	 *            well-known bytes
	 * @return geometry
	 * @throws IOException
	 *             upon failure to read bytes
	 * @since 4.0.0
	 */
	public static Geometry createGeometryFromWkb(byte[] bytes)
			throws IOException {
		return GeometryReader.readGeometry(bytes, geometryFilter);
	}

	/**
	 * Create the geometry data from Well-Known Text, default SRS Id of
	 * {@link #getDefaultSrsId}
	 * 
	 * @param text
	 *            well-known text
	 * @return geometry data
	 * @throws IOException
	 *             upon failure to read text
	 * @since 4.0.0
	 */
	public static GeoPackageGeometryData createFromWkt(String text)
			throws IOException {
		return createFromWkt(defaultSrsId, text);
	}

	/**
	 * Create the geometry data from Well-Known Text and build the envelope,
	 * default SRS Id of {@link #getDefaultSrsId}
	 * 
	 * @param text
	 *            well-known text
	 * @return geometry data
	 * @throws IOException
	 *             upon failure to read text
	 * @since 4.0.0
	 */
	public static GeoPackageGeometryData createFromWktAndBuildEnvelope(
			String text) throws IOException {
		return createFromWktAndBuildEnvelope(defaultSrsId, text);
	}

	/**
	 * Create the geometry data from Well-Known Text
	 * 
	 * @param srsId
	 *            SRS id
	 * @param text
	 *            well-known text
	 * @return geometry data
	 * @throws IOException
	 *             upon failure to read text
	 * @since 4.0.0
	 */
	public static GeoPackageGeometryData createFromWkt(long srsId, String text)
			throws IOException {
		return create(srsId, createGeometryFromWkt(text));
	}

	/**
	 * Create the geometry data from Well-Known Text and build the envelope
	 * 
	 * @param srsId
	 *            SRS id
	 * @param text
	 *            well-known text
	 * @return geometry data
	 * @throws IOException
	 *             upon failure to read text
	 * @since 4.0.0
	 */
	public static GeoPackageGeometryData createFromWktAndBuildEnvelope(
			long srsId, String text) throws IOException {
		return createAndBuildEnvelope(srsId, createGeometryFromWkt(text));
	}

	/**
	 * Create the geometry data from Well-Known Text and write the GeoPackage
	 * geometry bytes, default SRS Id of {@link #getDefaultSrsId}
	 * 
	 * @param text
	 *            well-known text
	 * @return geometry data
	 * @throws IOException
	 *             upon failure to read text or write bytes
	 * @since 4.0.0
	 */
	public static GeoPackageGeometryData createFromWktAndWrite(String text)
			throws IOException {
		return writeBytes(createFromWkt(text));
	}

	/**
	 * Create the geometry data from Well-Known Text, build the envelope, and
	 * write the GeoPackage geometry bytes, default SRS Id of
	 * {@link #getDefaultSrsId}
	 * 
	 * @param text
	 *            well-known text
	 * @return geometry data
	 * @throws IOException
	 *             upon failure to read text or write bytes
	 * @since 4.0.0
	 */
	public static GeoPackageGeometryData createFromWktBuildEnvelopeAndWrite(
			String text) throws IOException {
		return writeBytes(createFromWktAndBuildEnvelope(text));
	}

	/**
	 * Create the geometry data from Well-Known Text and write the GeoPackage
	 * geometry bytes
	 * 
	 * @param srsId
	 *            SRS id
	 * @param text
	 *            well-known text
	 * @return geometry data
	 * @throws IOException
	 *             upon failure to read text or write bytes
	 * @since 4.0.0
	 */
	public static GeoPackageGeometryData createFromWktAndWrite(long srsId,
			String text) throws IOException {
		return writeBytes(createFromWkt(srsId, text));
	}

	/**
	 * Create the geometry data from Well-Known Text, build the envelope, and
	 * write the GeoPackage geometry bytes
	 * 
	 * @param srsId
	 *            SRS id
	 * @param text
	 *            well-known text
	 * @return geometry data
	 * @throws IOException
	 *             upon failure to read text or write bytes
	 * @since 4.0.0
	 */
	public static GeoPackageGeometryData createFromWktBuildEnvelopeAndWrite(
			long srsId, String text) throws IOException {
		return writeBytes(createFromWktAndBuildEnvelope(srsId, text));
	}

	/**
	 * Create a geometry from Well-Known Text
	 * 
	 * @param text
	 *            well-known text
	 * @return geometry
	 * @throws IOException
	 *             upon failure to read text
	 * @since 4.0.0
	 */
	public static Geometry createGeometryFromWkt(String text)
			throws IOException {
		return mil.nga.sf.wkt.GeometryReader.readGeometry(text, geometryFilter);
	}

	/**
	 * GeoPackage geometry bytes from the geometry, default SRS Id of
	 * {@link #getDefaultSrsId()}
	 * 
	 * @param geometry
	 *            geometry
	 * @return GeoPackage geometry bytes
	 * @throws IOException
	 *             upon failure to write bytes
	 * @since 4.0.0
	 */
	public static byte[] bytes(Geometry geometry) throws IOException {
		return createAndWrite(geometry).getBytes();
	}

	/**
	 * GeoPackage geometry bytes from the geometry with built envelope, default
	 * SRS Id of {@link #getDefaultSrsId()}
	 * 
	 * @param geometry
	 *            geometry
	 * @return GeoPackage geometry bytes
	 * @throws IOException
	 *             upon failure to write bytes
	 * @since 4.0.0
	 */
	public static byte[] bytesAndBuildEnvelope(Geometry geometry)
			throws IOException {
		return createBuildEnvelopeAndWrite(geometry).getBytes();
	}

	/**
	 * GeoPackage geometry bytes from the geometry
	 * 
	 * @param srsId
	 *            SRS id
	 * @param geometry
	 *            geometry
	 * @return GeoPackage geometry bytes
	 * @throws IOException
	 *             upon failure to write bytes
	 * @since 4.0.0
	 */
	public static byte[] bytes(long srsId, Geometry geometry)
			throws IOException {
		return createAndWrite(srsId, geometry).getBytes();
	}

	/**
	 * GeoPackage geometry bytes from the geometry with built envelope
	 * 
	 * @param srsId
	 *            SRS id
	 * @param geometry
	 *            geometry
	 * @return GeoPackage geometry bytes
	 * @throws IOException
	 *             upon failure to write bytes
	 * @since 4.0.0
	 */
	public static byte[] bytesAndBuildEnvelope(long srsId, Geometry geometry)
			throws IOException {
		return createBuildEnvelopeAndWrite(srsId, geometry).getBytes();
	}

	/**
	 * GeoPackage geometry bytes from Well-Known bytes, default SRS Id of
	 * {@link #getDefaultSrsId}
	 * 
	 * @param bytes
	 *            well-known bytes
	 * @return GeoPackage geometry bytes
	 * @throws IOException
	 *             upon failure to read or write bytes
	 * @since 4.0.0
	 */
	public static byte[] bytesFromWkb(byte[] bytes) throws IOException {
		return createFromWkbAndWrite(bytes).getBytes();
	}

	/**
	 * GeoPackage geometry bytes from Well-Known bytes with built envelope,
	 * default SRS Id of {@link #getDefaultSrsId}
	 * 
	 * @param bytes
	 *            well-known bytes
	 * @return GeoPackage geometry bytes
	 * @throws IOException
	 *             upon failure to read or write bytes
	 * @since 4.0.0
	 */
	public static byte[] bytesFromWkbAndBuildEnvelope(byte[] bytes)
			throws IOException {
		return createFromWkbBuildEnvelopeAndWrite(bytes).getBytes();
	}

	/**
	 * GeoPackage geometry bytes from Well-Known bytes
	 * 
	 * @param srsId
	 *            SRS id
	 * @param bytes
	 *            well-known bytes
	 * @return GeoPackage geometry bytes
	 * @throws IOException
	 *             upon failure to read or write bytes
	 * @since 4.0.0
	 */
	public static byte[] bytesFromWkb(long srsId, byte[] bytes)
			throws IOException {
		return createFromWkbAndWrite(srsId, bytes).getBytes();
	}

	/**
	 * GeoPackage geometry bytes from Well-Known bytes with built envelope
	 * 
	 * @param srsId
	 *            SRS id
	 * @param bytes
	 *            well-known bytes
	 * @return GeoPackage geometry bytes
	 * @throws IOException
	 *             upon failure to read or write bytes
	 * @since 4.0.0
	 */
	public static byte[] bytesFromWkbAndBuildEnvelope(long srsId, byte[] bytes)
			throws IOException {
		return createFromWkbBuildEnvelopeAndWrite(srsId, bytes).getBytes();
	}

	/**
	 * GeoPackage geometry bytes from Well-Known text, default SRS Id of
	 * {@link #getDefaultSrsId}
	 * 
	 * @param text
	 *            well-known text
	 * @return GeoPackage geometry bytes
	 * @throws IOException
	 *             upon failure to read text or write bytes
	 * @since 4.0.0
	 */
	public static byte[] bytesFromWkt(String text) throws IOException {
		return createFromWktAndWrite(text).getBytes();
	}

	/**
	 * GeoPackage geometry bytes from Well-Known text with built envelope,
	 * default SRS Id of {@link #getDefaultSrsId}
	 * 
	 * @param text
	 *            well-known text
	 * @return GeoPackage geometry bytes
	 * @throws IOException
	 *             upon failure to read text or write bytes
	 * @since 4.0.0
	 */
	public static byte[] bytesFromWktAndBuildEnvelope(String text)
			throws IOException {
		return createFromWktBuildEnvelopeAndWrite(text).getBytes();
	}

	/**
	 * GeoPackage geometry bytes from Well-Known text
	 * 
	 * @param srsId
	 *            SRS id
	 * @param text
	 *            well-known text
	 * @return GeoPackage geometry bytes
	 * @throws IOException
	 *             upon failure to read text or write bytes
	 * @since 4.0.0
	 */
	public static byte[] bytesFromWkt(long srsId, String text)
			throws IOException {
		return createFromWktAndWrite(srsId, text).getBytes();
	}

	/**
	 * GeoPackage geometry bytes from Well-Known text with built envelope
	 * 
	 * @param srsId
	 *            SRS id
	 * @param text
	 *            well-known text
	 * @return GeoPackage geometry bytes
	 * @throws IOException
	 *             upon failure to read text or write bytes
	 * @since 4.0.0
	 */
	public static byte[] bytesFromWktAndBuildEnvelope(long srsId, String text)
			throws IOException {
		return createFromWktBuildEnvelopeAndWrite(srsId, text).getBytes();
	}

	/**
	 * Well-Known Bytes from the geometry data
	 * 
	 * @param geometryData
	 *            geometry data
	 * @return well-known bytes
	 * @throws IOException
	 *             upon failure to write bytes
	 * @since 4.0.0
	 */
	public static byte[] wkb(GeoPackageGeometryData geometryData)
			throws IOException {
		if (geometryData.getBytes() == null) {
			geometryData.toBytes();
		}
		return geometryData.getWkb();
	}

	/**
	 * Well-Known Bytes from the geometry
	 * 
	 * @param geometry
	 *            geometry
	 * @return well-known bytes
	 * @throws IOException
	 *             upon failure to write bytes
	 * @since 4.0.0
	 */
	public static byte[] wkb(Geometry geometry) throws IOException {
		return createAndWrite(geometry).getWkb();
	}

	/**
	 * Well-Known Bytes from GeoPackage geometry bytes
	 * 
	 * @param bytes
	 *            GeoPackage geometry bytes
	 * @return well-known bytes
	 * @throws IOException
	 *             upon failure to read or write bytes
	 * @since 4.0.0
	 */
	public static byte[] wkb(byte[] bytes) throws IOException {
		return create(bytes).getWkb();
	}

	/**
	 * Well-Known Bytes from Well-Known Text
	 * 
	 * @param text
	 *            well-known text
	 * @return well-known bytes
	 * @throws IOException
	 *             upon failure to read text or write bytes
	 * @since 4.0.0
	 */
	public static byte[] wkbFromWkt(String text) throws IOException {
		return createFromWktAndWrite(text).getWkb();
	}

	/**
	 * Well-Known Text from the geometry data
	 * 
	 * @param geometryData
	 *            geometry data
	 * @return well-known text
	 * @throws IOException
	 *             upon failure to write text
	 * @since 4.0.0
	 */
	public static String wkt(GeoPackageGeometryData geometryData)
			throws IOException {
		return geometryData.getWkt();
	}

	/**
	 * Well-Known Text from the geometry
	 * 
	 * @param geometry
	 *            geometry
	 * @return well-known text
	 * @throws IOException
	 *             upon failure to write text
	 * @since 4.0.0
	 */
	public static String wkt(Geometry geometry) throws IOException {
		return create(geometry).getWkt();
	}

	/**
	 * Well-Known Text from GeoPackage Geometry Bytes
	 * 
	 * @param bytes
	 *            GeoPackage geometry bytes
	 * @return well-known text
	 * @throws IOException
	 *             upon failure to write text
	 * @since 4.0.0
	 */
	public static String wkt(byte[] bytes) throws IOException {
		return create(bytes).getWkt();
	}

	/**
	 * Well-Known Text from Well-Known Bytes
	 * 
	 * @param bytes
	 *            well-known bytes
	 * @return well-known text
	 * @throws IOException
	 *             upon failure to write text
	 * @since 4.0.0
	 */
	public static String wktFromWkb(byte[] bytes) throws IOException {
		return createFromWkb(bytes).getWkt();
	}

	/**
	 * Default Constructor, default SRS Id of {@link #getDefaultSrsId}
	 * 
	 * @since 4.0.0
	 */
	public GeoPackageGeometryData() {
		this(defaultSrsId);
	}

	/**
	 * Constructor, default SRS Id of {@link #getDefaultSrsId}
	 * 
	 * @param geometry
	 *            geometry
	 * @since 4.0.0
	 */
	public GeoPackageGeometryData(Geometry geometry) {
		this(geometry, false);
	}

	/**
	 * Constructor
	 * 
	 * @param geometry
	 *            geometry
	 * @param buildEnvelope
	 *            true to build and set the envelope
	 * @since 4.0.0
	 */
	public GeoPackageGeometryData(Geometry geometry, boolean buildEnvelope) {
		this();
		setGeometry(geometry);
		if (buildEnvelope) {
			buildEnvelope();
		}
	}

	/**
	 * Constructor
	 * 
	 * @param srsId
	 *            SRS id
	 */
	public GeoPackageGeometryData(long srsId) {
		// SRS ID in the database is a long (db INTEGER) but the wkb srs id is
		// only 4 bytes
		this.srsId = (int) srsId;
	}

	/**
	 * Constructor
	 * 
	 * @param srsId
	 *            SRS id
	 * @param geometry
	 *            geometry
	 * @since 4.0.0
	 */
	public GeoPackageGeometryData(long srsId, Geometry geometry) {
		this(srsId, geometry, false);
	}

	/**
	 * Constructor
	 * 
	 * @param srsId
	 *            SRS id
	 * @param geometry
	 *            geometry
	 * @param buildEnvelope
	 *            true to build and set the envelope
	 * @since 4.0.0
	 */
	public GeoPackageGeometryData(long srsId, Geometry geometry,
			boolean buildEnvelope) {
		this(srsId);
		setGeometry(geometry);
		if (buildEnvelope) {
			buildEnvelope();
		}
	}

	/**
	 * Constructor, default SRS Id of {@link #getDefaultSrsId}
	 * 
	 * @param geometry
	 *            geometry
	 * @param envelope
	 *            geometry envelope
	 * @since 4.0.0
	 */
	public GeoPackageGeometryData(Geometry geometry,
			GeometryEnvelope envelope) {
		this();
		setGeometry(geometry);
		setEnvelope(envelope);
	}

	/**
	 * Constructor
	 * 
	 * @param srsId
	 *            SRS id
	 * @param geometry
	 *            geometry
	 * @param envelope
	 *            geometry envelope
	 * @since 4.0.0
	 */
	public GeoPackageGeometryData(long srsId, Geometry geometry,
			GeometryEnvelope envelope) {
		this(srsId);
		setGeometry(geometry);
		setEnvelope(envelope);
	}

	/**
	 * Copy Constructor
	 * 
	 * @param geometryData
	 *            geometry data
	 * @since 4.0.0
	 */
	public GeoPackageGeometryData(GeoPackageGeometryData geometryData) {
		setSrsId(geometryData.getSrsId());
		Geometry geometry = geometryData.getGeometry();
		if (geometry != null) {
			geometry = geometry.copy();
		}
		setGeometry(geometry);
		GeometryEnvelope envelope = geometryData.getEnvelope();
		if (envelope != null) {
			envelope = envelope.copy();
		}
		setEnvelope(envelope);
		byte[] bytes = geometryData.getBytes();
		if (bytes != null) {
			bytes = Arrays.copyOf(bytes, bytes.length);
		}
		this.bytes = bytes;
		this.wkbGeometryIndex = geometryData.wkbGeometryIndex;
		setByteOrder(geometryData.getByteOrder());
	}

	/**
	 * Constructor
	 * 
	 * @param bytes
	 *            geometry bytes
	 */
	public GeoPackageGeometryData(byte[] bytes) {
		fromBytes(bytes);
	}

	/**
	 * Populate the geometry data from the bytes
	 * 
	 * @param bytes
	 *            geometry bytes
	 */
	public void fromBytes(byte[] bytes) {
		this.bytes = bytes;

		ByteReader reader = new ByteReader(bytes);

		// Get 2 bytes as the magic number and validate
		String magic = null;
		try {
			magic = reader.readString(2);
		} catch (IOException e) {
			throw new GeoPackageException(
					"Unexpected GeoPackage Geometry magic number character encoding: Expected: "
							+ GeoPackageConstants.GEOMETRY_MAGIC_NUMBER,
					e);
		}
		if (!magic.equals(GeoPackageConstants.GEOMETRY_MAGIC_NUMBER)) {
			throw new GeoPackageException(
					"Unexpected GeoPackage Geometry magic number: " + magic
							+ ", Expected: "
							+ GeoPackageConstants.GEOMETRY_MAGIC_NUMBER);
		}

		try {

			// Get a byte as the version and validate, value of 0 = version 1
			byte version = reader.readByte();
			if (version != GeoPackageConstants.GEOMETRY_VERSION_1) {
				throw new GeoPackageException(
						"Unexpected GeoPackage Geometry version: " + version
								+ ", Expected: "
								+ GeoPackageConstants.GEOMETRY_VERSION_1);
			}

			// Get a flags byte and then read the flag values
			byte flags = reader.readByte();
			int envelopeIndicator = readFlags(flags);
			reader.setByteOrder(byteOrder);

			// Read the 5th - 8th bytes as the srs id
			srsId = reader.readInt();

			// Read the envelope
			envelope = readEnvelope(envelopeIndicator, reader);

		} catch (IOException e) {
			throw new GeoPackageException(
					"Failed to read the GeoPackage geometry header", e);
		}

		// Save off where the WKB bytes start
		wkbGeometryIndex = reader.getNextByte();

		// Read the Well-Known Binary Geometry if not marked as empty
		if (!empty) {
			try {
				geometry = GeometryReader.readGeometry(reader, geometryFilter);
			} catch (IOException e) {
				throw new GeoPackageException("Failed to read the WKB geometry",
						e);
			}
		}

		// Close the reader
		reader.close();
	}

	/**
	 * Write the geometry to bytes
	 * 
	 * @return bytes
	 * @throws IOException
	 *             upon failure to write bytes
	 */
	public byte[] toBytes() throws IOException {

		ByteWriter writer = new ByteWriter();

		// Write GP as the 2 byte magic number
		writer.writeString(GeoPackageConstants.GEOMETRY_MAGIC_NUMBER);

		// Write a byte as the version, value of 0 = version 1
		writer.writeByte(GeoPackageConstants.GEOMETRY_VERSION_1);

		// Build and write a flags byte
		byte flags = buildFlagsByte();
		writer.writeByte(flags);
		writer.setByteOrder(byteOrder);

		// Write the 4 byte srs id int
		writer.writeInt(srsId);

		// Write the envelope
		writeEnvelope(writer);

		// Save off where the WKB bytes start
		wkbGeometryIndex = writer.size();

		// Write the Well-Known Binary Geometry if not marked as empty
		if (!empty) {
			GeometryWriter.writeGeometry(writer, geometry);
		}

		// Get the bytes
		bytes = writer.getBytes();

		// Close the writer
		writer.close();

		return bytes;
	}

	/**
	 * Read the flags from the flag byte and return the envelope indicator
	 * 
	 * @param flags
	 *            flags byte
	 * @return envelope indicator
	 */
	private int readFlags(byte flags) {

		// Verify the reserved bits at 7 and 6 are 0
		int reserved7 = (flags >> 7) & 1;
		int reserved6 = (flags >> 6) & 1;
		if (reserved7 != 0 || reserved6 != 0) {
			throw new GeoPackageException(
					"Unexpected GeoPackage Geometry flags. Flag bit 7 and 6 should both be 0, 7="
							+ reserved7 + ", 6=" + reserved6);
		}

		// Get the binary type from bit 5, 0 for standard and 1 for extended
		int binaryType = (flags >> 5) & 1;
		extended = binaryType == 1;

		// Get the empty geometry flag from bit 4, 0 for non-empty and 1 for
		// empty
		int emptyValue = (flags >> 4) & 1;
		empty = emptyValue == 1;

		// Get the envelope contents indicator code (3-bit unsigned integer from
		// bits 3, 2, and 1)
		int envelopeIndicator = (flags >> 1) & 7;
		if (envelopeIndicator > 4) {
			throw new GeoPackageException(
					"Unexpected GeoPackage Geometry flags. Envelope contents indicator must be between 0 and 4. Actual: "
							+ envelopeIndicator);
		}

		// Get the byte order from bit 0, 0 for Big Endian and 1 for Little
		// Endian
		int byteOrderValue = flags & 1;
		byteOrder = byteOrderValue == 0 ? ByteOrder.BIG_ENDIAN
				: ByteOrder.LITTLE_ENDIAN;

		return envelopeIndicator;
	}

	/**
	 * Build the flags byte from the flag values
	 * 
	 * @return envelope indicator
	 */
	private byte buildFlagsByte() {

		byte flag = 0;

		// Add the binary type to bit 5, 0 for standard and 1 for extended
		int binaryType = extended ? 1 : 0;
		flag += (binaryType << 5);

		// Add the empty geometry flag to bit 4, 0 for non-empty and 1 for
		// empty
		int emptyValue = empty ? 1 : 0;
		flag += (emptyValue << 4);

		// Add the envelope contents indicator code (3-bit unsigned integer to
		// bits 3, 2, and 1)
		int envelopeIndicator = envelope == null ? 0 : getIndicator(envelope);
		flag += (envelopeIndicator << 1);

		// Add the byte order to bit 0, 0 for Big Endian and 1 for Little
		// Endian
		int byteOrderValue = (byteOrder == ByteOrder.BIG_ENDIAN) ? 0 : 1;
		flag += byteOrderValue;

		return flag;
	}

	/**
	 * Read the envelope based upon the indicator value
	 * 
	 * @param envelopeIndicator
	 *            envelope indicator
	 * @param reader
	 *            byte reader
	 * @return geometry envelope
	 * @throws IOException
	 *             upon error
	 */
	private GeometryEnvelope readEnvelope(int envelopeIndicator,
			ByteReader reader) throws IOException {

		GeometryEnvelope envelope = null;

		if (envelopeIndicator > 0) {

			// Read x and y values and create envelope
			double minX = reader.readDouble();
			double maxX = reader.readDouble();
			double minY = reader.readDouble();
			double maxY = reader.readDouble();

			boolean hasZ = false;
			Double minZ = null;
			Double maxZ = null;

			boolean hasM = false;
			Double minM = null;
			Double maxM = null;

			// Read z values
			if (envelopeIndicator == 2 || envelopeIndicator == 4) {
				hasZ = true;
				minZ = reader.readDouble();
				maxZ = reader.readDouble();
			}

			// Read m values
			if (envelopeIndicator == 3 || envelopeIndicator == 4) {
				hasM = true;
				minM = reader.readDouble();
				maxM = reader.readDouble();
			}

			envelope = new GeometryEnvelope(hasZ, hasM);

			envelope.setMinX(minX);
			envelope.setMaxX(maxX);
			envelope.setMinY(minY);
			envelope.setMaxY(maxY);

			if (hasZ) {
				envelope.setMinZ(minZ);
				envelope.setMaxZ(maxZ);
			}

			if (hasM) {
				envelope.setMinM(minM);
				envelope.setMaxM(maxM);
			}
		}

		return envelope;
	}

	/**
	 * Write the envelope bytes
	 * 
	 * @param writer
	 *            byte writer
	 * @throws IOException
	 */
	private void writeEnvelope(ByteWriter writer) throws IOException {

		if (envelope != null) {

			// Write x and y values
			writer.writeDouble(envelope.getMinX());
			writer.writeDouble(envelope.getMaxX());
			writer.writeDouble(envelope.getMinY());
			writer.writeDouble(envelope.getMaxY());

			// Write z values
			if (envelope.hasZ()) {
				writer.writeDouble(envelope.getMinZ());
				writer.writeDouble(envelope.getMaxZ());
			}

			// Write m values
			if (envelope.hasM()) {
				writer.writeDouble(envelope.getMinM());
				writer.writeDouble(envelope.getMaxM());
			}
		}
	}

	/**
	 * Is the geometry extended
	 * 
	 * @return true if extended
	 */
	public boolean isExtended() {
		return extended;
	}

	/**
	 * Is the geometry empty
	 * 
	 * @return true if empty
	 */
	public boolean isEmpty() {
		return empty;
	}

	/**
	 * Get the byte order
	 * 
	 * @return byte order
	 */
	public ByteOrder getByteOrder() {
		return byteOrder;
	}

	/**
	 * Get the SRS id
	 * 
	 * @return SRS id
	 */
	public int getSrsId() {
		return srsId;
	}

	/**
	 * Get the geometry envelope
	 * 
	 * @return geometry envelope
	 */
	public GeometryEnvelope getEnvelope() {
		return envelope;
	}

	/**
	 * Get the geometry
	 * 
	 * @return geometry
	 */
	public Geometry getGeometry() {
		return geometry;
	}

	/**
	 * Set the extended flag
	 * 
	 * @param extended
	 *            extended value
	 */
	public void setExtended(boolean extended) {
		this.extended = extended;
	}

	/**
	 * Set the empty flag
	 * 
	 * @param empty
	 *            empty value
	 */
	public void setEmpty(boolean empty) {
		this.empty = empty;
	}

	/**
	 * Set the byte order
	 * 
	 * @param byteOrder
	 *            byte order
	 */
	public void setByteOrder(ByteOrder byteOrder) {
		this.byteOrder = byteOrder;
	}

	/**
	 * Set the SRS id
	 * 
	 * @param srsId
	 *            SRS id
	 */
	public void setSrsId(int srsId) {
		this.srsId = srsId;
	}

	/**
	 * Set the geometry envelope
	 * 
	 * @param envelope
	 *            geometry envelope
	 */
	public void setEnvelope(GeometryEnvelope envelope) {
		this.envelope = envelope;
	}

	/**
	 * Set the geometry. Updates the empty flag and if the geometry is not null,
	 * the extended flag. Following invoking this method and upon setting the
	 * SRS id, call {@link #toBytes()} to convert the geometry to bytes.
	 * Alternatively call {@link #setGeometryToBytes(Geometry)} or
	 * {@link #setGeometryAndBuildEnvelopeToBytes(Geometry)} to perform both
	 * operations.
	 * 
	 * @param geometry
	 *            geometry
	 */
	public void setGeometry(Geometry geometry) {
		this.geometry = geometry;
		empty = geometry == null;
		if (geometry != null) {
			extended = GeometryExtensions
					.isNonStandard(geometry.getGeometryType());
		}
	}

	/**
	 * Set the geometry and write to bytes
	 * 
	 * @param geometry
	 *            geometry
	 * @return geometry bytes
	 * @throws IOException
	 *             upon failure to write bytes
	 * @since 4.0.0
	 */
	public byte[] setGeometryToBytes(Geometry geometry) throws IOException {
		return setGeometryToBytes(geometry, false);
	}

	/**
	 * Set the geometry, build the envelope, and write to bytes
	 * 
	 * @param geometry
	 *            geometry
	 * @return geometry bytes
	 * @throws IOException
	 *             upon failure to write bytes
	 * @since 4.0.0
	 */
	public byte[] setGeometryAndBuildEnvelopeToBytes(Geometry geometry)
			throws IOException {
		return setGeometryToBytes(geometry, true);
	}

	/**
	 * Set the geometry, optionally build the envelope, and write to bytes
	 * 
	 * @param geometry
	 *            geometry
	 * @param buildEnvelope
	 *            true to build and set the envelope
	 * @return geometry bytes
	 * @throws IOException
	 *             upon failure to write bytes
	 * @since 4.0.0
	 */
	private byte[] setGeometryToBytes(Geometry geometry, boolean buildEnvelope)
			throws IOException {
		setGeometry(geometry);
		if (buildEnvelope) {
			buildEnvelope();
		}
		return toBytes();
	}

	/**
	 * Set the geometry from Well-Known bytes
	 * 
	 * @param bytes
	 *            well-known bytes
	 * @throws IOException
	 *             upon failure to read bytes
	 * @since 4.0.0
	 */
	public void setGeometryFromWkb(byte[] bytes) throws IOException {
		setGeometry(createGeometryFromWkb(bytes));
	}

	/**
	 * Set the geometry from Well-Known text
	 * 
	 * @param text
	 *            well-known text
	 * @throws IOException
	 *             upon failure to read text
	 * @since 4.0.0
	 */
	public void setGeometryFromWkt(String text) throws IOException {
		setGeometry(createGeometryFromWkt(text));
	}

	/**
	 * Get the bytes of the entire GeoPackage geometry including GeoPackage
	 * header and WKB bytes
	 * 
	 * @return bytes
	 */
	public byte[] getBytes() {
		return bytes;
	}

	/**
	 * Get the GeoPackage header bytes
	 * 
	 * @return header bytes
	 */
	public byte[] getHeaderBytes() {
		byte[] headerBytes = null;
		if (bytes != null) {
			headerBytes = new byte[wkbGeometryIndex];
			System.arraycopy(bytes, 0, headerBytes, 0, wkbGeometryIndex);
		}
		return headerBytes;
	}

	/**
	 * Get the GeoPackage header bytes already ordered in a Byte Buffer
	 * 
	 * @return byte buffer
	 */
	public ByteBuffer getHeaderByteBuffer() {
		ByteBuffer buffer = null;
		if (bytes != null) {
			buffer = ByteBuffer.wrap(bytes, 0, wkbGeometryIndex)
					.order(byteOrder);
		}
		return buffer;
	}

	/**
	 * Get the Well-Known Binary Geometry bytes
	 * 
	 * @return bytes
	 * @since 4.0.0
	 */
	public byte[] getWkb() {
		byte[] wkbBytes = null;
		if (bytes != null) {
			int wkbByteCount = bytes.length - wkbGeometryIndex;
			wkbBytes = new byte[wkbByteCount];
			System.arraycopy(bytes, wkbGeometryIndex, wkbBytes, 0,
					wkbByteCount);
		}
		return wkbBytes;
	}

	/**
	 * Get the Well-Known Binary Geometry bytes already ordered in a Byte Buffer
	 * 
	 * @return byte buffer
	 * @since 4.0.0
	 */
	public ByteBuffer getWkbBuffer() {
		ByteBuffer buffer = null;
		if (bytes != null) {
			buffer = ByteBuffer.wrap(bytes, wkbGeometryIndex,
					bytes.length - wkbGeometryIndex).order(byteOrder);
		}
		return buffer;
	}

	/**
	 * Return the byte index where the Well-Known Binary bytes start
	 * 
	 * @return index
	 */
	public int getWkbGeometryIndex() {
		return wkbGeometryIndex;
	}

	/**
	 * Get a Well-Known text string from the geometry
	 * 
	 * @return well-known text string
	 */
	public String getWkt() {
		String wkt = null;
		if (geometry != null) {
			try {
				wkt = mil.nga.sf.wkt.GeometryWriter.writeGeometry(geometry);
			} catch (IOException e) {
				throw new GeoPackageException(
						"Failed to write the geometry WKT", e);
			}
		}
		return wkt;
	}

	/**
	 * Get the envelope if it exists or build, set, and retrieve it from the
	 * geometry
	 * 
	 * @return geometry envelope
	 * @since 3.1.0
	 */
	public GeometryEnvelope getOrBuildEnvelope() {
		GeometryEnvelope envelope = getEnvelope();
		if (envelope == null) {
			envelope = buildEnvelope();
		}
		return envelope;
	}

	/**
	 * Build, set, and retrieve the envelope from the geometry
	 * 
	 * @return geometry envelope
	 * @since 4.0.0
	 */
	public GeometryEnvelope buildEnvelope() {
		GeometryEnvelope envelope = null;
		Geometry geometry = getGeometry();
		if (geometry != null) {
			envelope = GeometryEnvelopeBuilder.buildEnvelope(geometry);
		}
		setEnvelope(envelope);
		return envelope;
	}

	/**
	 * Get the envelope flag indicator
	 * 
	 * 1 for xy, 2 for xyz, 3 for xym, 4 for xyzm (null would be 0)
	 * 
	 * @param envelope
	 *            geometry envelope
	 * 
	 * @return indicator
	 */
	public static int getIndicator(GeometryEnvelope envelope) {
		int indicator = 1;
		if (envelope.hasZ()) {
			indicator++;
		}
		if (envelope.hasM()) {
			indicator += 2;
		}
		return indicator;
	}

	/**
	 * Transform the geometry data using the provided projection transform
	 * 
	 * @param transform
	 *            projection transform
	 * @return transformed geometry data
	 * @since 4.0.0
	 */
	public GeoPackageGeometryData transform(ProjectionTransform transform) {
		GeoPackageGeometryData transformed = this;
		if (transform.isSameProjection()) {
			transformed = new GeoPackageGeometryData(transformed);
		} else {
			Geometry geometry = getGeometry();
			if (geometry != null) {
				geometry = transform.transform(geometry);
			}
			GeometryEnvelope envelope = getEnvelope();
			if (envelope != null) {
				envelope = transform.transform(envelope);
			}
			transformed = new GeoPackageGeometryData(getSrsId(), geometry,
					envelope);
		}
		return transformed;
	}

	/**
	 * Write the geometry data GeoPackage geometry bytes
	 * 
	 * @param geometryData
	 *            geometry data
	 * @return geometry data
	 * @throws IOException
	 *             upon failure to write bytes
	 */
	private static GeoPackageGeometryData writeBytes(
			GeoPackageGeometryData geometryData) throws IOException {
		geometryData.toBytes();
		return geometryData;
	}

}
