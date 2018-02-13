package mil.nga.geopackage.geom;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import mil.nga.geopackage.GeoPackageConstants;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.extension.GeometryExtensions;
import mil.nga.wkb.geom.Geometry;
import mil.nga.wkb.geom.GeometryEnvelope;
import mil.nga.wkb.io.ByteReader;
import mil.nga.wkb.io.ByteWriter;
import mil.nga.wkb.io.WkbGeometryReader;
import mil.nga.wkb.io.WkbGeometryWriter;

/**
 * GeoPackage Geometry Data
 * 
 * @author osbornb
 * 
 */
public class GeoPackageGeometryData {

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
	private ByteOrder byteOrder = ByteOrder.BIG_ENDIAN;

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
	 * Constructor
	 * 
	 * @param srsId
	 */
	public GeoPackageGeometryData(long srsId) {
		// SRS ID in the database is a long (db INTEGER) but the wkb srs id is
		// only 4 bytes
		this.srsId = (int) srsId;
	}

	/**
	 * Constructor
	 * 
	 * @param bytes
	 */
	public GeoPackageGeometryData(byte[] bytes) {
		fromBytes(bytes);
	}

	/**
	 * Populate the geometry data from the bytes
	 * 
	 * @param bytes
	 */
	public void fromBytes(byte[] bytes) {
		this.bytes = bytes;

		ByteReader reader = new ByteReader(bytes);

		// Get 2 bytes as the magic number and validate
		String magic = null;
		try {
			magic = reader.readString(2);
		} catch (UnsupportedEncodingException e) {
			throw new GeoPackageException(
					"Unexpected GeoPackage Geometry magic number character encoding: Expected: "
							+ GeoPackageConstants.GEO_PACKAGE_GEOMETRY_MAGIC_NUMBER);
		}
		if (!magic
				.equals(GeoPackageConstants.GEO_PACKAGE_GEOMETRY_MAGIC_NUMBER)) {
			throw new GeoPackageException(
					"Unexpected GeoPackage Geometry magic number: "
							+ magic
							+ ", Expected: "
							+ GeoPackageConstants.GEO_PACKAGE_GEOMETRY_MAGIC_NUMBER);
		}

		// Get a byte as the version and validate, value of 0 = version 1
		byte version = reader.readByte();
		if (version != GeoPackageConstants.GEO_PACKAGE_GEOMETRY_VERSION_1) {
			throw new GeoPackageException(
					"Unexpected GeoPackage Geometry version: "
							+ version
							+ ", Expected: "
							+ GeoPackageConstants.GEO_PACKAGE_GEOMETRY_VERSION_1);
		}

		// Get a flags byte and then read the flag values
		byte flags = reader.readByte();
		int envelopeIndicator = readFlags(flags);
		reader.setByteOrder(byteOrder);

		// Read the 5th - 8th bytes as the srs id
		srsId = reader.readInt();

		// Read the envelope
		envelope = readEnvelope(envelopeIndicator, reader);

		// Save off where the WKB bytes start
		wkbGeometryIndex = reader.getNextByte();

		// Read the Well-Known Binary Geometry if not marked as empty
		if (!empty) {
			geometry = WkbGeometryReader.readGeometry(reader);
		}

	}

	/**
	 * Write the geometry to bytes
	 * 
	 * @return bytes
	 * @throws IOException
	 */
	public byte[] toBytes() throws IOException {

		ByteWriter writer = new ByteWriter();

		// Write GP as the 2 byte magic number
		writer.writeString(GeoPackageConstants.GEO_PACKAGE_GEOMETRY_MAGIC_NUMBER);

		// Write a byte as the version, value of 0 = version 1
		writer.writeByte(GeoPackageConstants.GEO_PACKAGE_GEOMETRY_VERSION_1);

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
			WkbGeometryWriter.writeGeometry(writer, geometry);
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
	 */
	private GeometryEnvelope readEnvelope(int envelopeIndicator,
			ByteReader reader) {

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
	 * Get the srs id
	 * 
	 * @return srs id
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
	 * Set the srs id
	 * 
	 * @param srsId
	 *            srs id
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
	 * the extended flag
	 * 
	 * @param geometry
	 *            geometry
	 */
	public void setGeometry(Geometry geometry) {
		this.geometry = geometry;
		empty = geometry == null;
		if (geometry != null) {
			extended = GeometryExtensions.isNonStandard(geometry
					.getGeometryType());
		}
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
		byte[] headerBytes = new byte[wkbGeometryIndex];
		System.arraycopy(bytes, 0, headerBytes, 0, wkbGeometryIndex);
		return headerBytes;
	}

	/**
	 * Get the GeoPackage header bytes already ordered in a Byte Buffer
	 * 
	 * @return byte buffer
	 */
	public ByteBuffer getHeaderByteBuffer() {
		return ByteBuffer.wrap(bytes, 0, wkbGeometryIndex).order(byteOrder);
	}

	/**
	 * Get the Well-Known Binary Geometry bytes
	 * 
	 * @return bytes
	 */
	public byte[] getWkbBytes() {
		int wkbByteCount = bytes.length - wkbGeometryIndex;
		byte[] wkbBytes = new byte[wkbByteCount];
		System.arraycopy(bytes, wkbGeometryIndex, wkbBytes, 0, wkbByteCount);
		return wkbBytes;
	}

	/**
	 * Get the Well-Known Binary Geometry bytes already ordered in a Byte Buffer
	 * 
	 * @return byte buffer
	 */
	public ByteBuffer getWkbByteBuffer() {
		return ByteBuffer.wrap(bytes, wkbGeometryIndex,
				bytes.length - wkbGeometryIndex).order(byteOrder);
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
	 * Get the envelope flag indicator
	 * 
	 * 1 for xy, 2 for xyz, 3 for xym, 4 for xyzm (null would be 0)
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

}
