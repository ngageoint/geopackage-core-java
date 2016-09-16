package mil.nga.tiff.io;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * Read through a byte array
 * 
 * @author osbornb
 */
public class ByteReader {

	/**
	 * Next byte index to read
	 */
	private int nextByte = 0;

	/**
	 * Bytes to read
	 */
	private final byte[] bytes;

	/**
	 * Byte order
	 */
	private ByteOrder byteOrder = null;

	/**
	 * Constructor
	 * 
	 * @param bytes
	 *            bytes
	 */
	public ByteReader(byte[] bytes) {
		this(bytes, ByteOrder.BIG_ENDIAN);
	}

	/**
	 * Constructor
	 * 
	 * @param bytes
	 *            bytes
	 * @param byteOrder
	 *            byte order
	 */
	public ByteReader(byte[] bytes, ByteOrder byteOrder) {
		this.bytes = bytes;
		this.byteOrder = byteOrder;
	}

	/**
	 * Get the next byte to be read
	 * 
	 * @return next byte to be read
	 */
	public int getNextByte() {
		return nextByte;
	}

	/**
	 * Set the next byte to be read
	 * 
	 * @param nextByte
	 *            next byte
	 */
	public void setNextByte(int nextByte) {
		this.nextByte = nextByte;
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
	 * Set the byte order
	 * 
	 * @param byteOrder
	 *            byte order
	 */
	public void setByteOrder(ByteOrder byteOrder) {
		this.byteOrder = byteOrder;
	}

	/**
	 * Check if there is at least one more byte left to read
	 * 
	 * @return true more bytes left to read
	 */
	public boolean hasByte() {
		return hasBytes(1);
	}

	/**
	 * Check if there are the provided number of bytes left to read
	 * 
	 * @param count
	 *            number of bytes
	 * @return true if has at least the number of bytes left
	 */
	public boolean hasBytes(int count) {
		return nextByte + count <= bytes.length;
	}

	/**
	 * Read a String from the provided number of bytes
	 * 
	 * @param num
	 *            number of bytes
	 * @return String
	 * @throws UnsupportedEncodingException
	 */
	public String readString(int num) throws UnsupportedEncodingException {
		verifyRemainingBytes(num);
		String value = null;
		if (num != 1 || bytes[nextByte] != 0) {
			value = new String(bytes, nextByte, num, StandardCharsets.US_ASCII);
		}
		nextByte += num;
		return value;
	}

	/**
	 * Read a byte
	 * 
	 * @return byte
	 */
	public byte readByte() {
		verifyRemainingBytes(1);
		byte value = bytes[nextByte];
		nextByte++;
		return value;
	}

	/**
	 * Read an unsigned byte
	 * 
	 * @return unsigned byte as short
	 */
	public short readUnsignedByte() {
		return ((short) (readByte() & 0xff));
	}

	/**
	 * Read a number of bytes
	 * 
	 * @param num
	 *            number of bytes
	 * @return bytes
	 */
	public byte[] readBytes(int num) {
		verifyRemainingBytes(num);
		byte[] readBytes = Arrays.copyOfRange(bytes, nextByte, nextByte + num);
		nextByte += num;
		return readBytes;
	}

	/**
	 * Read a short
	 * 
	 * @return short
	 */
	public short readShort() {
		verifyRemainingBytes(2);
		short value = ByteBuffer.wrap(bytes, nextByte, 2).order(byteOrder)
				.getShort();
		nextByte += 2;
		return value;
	}

	/**
	 * Read an unsigned short
	 * 
	 * @return unsigned short as int
	 */
	public int readUnsignedShort() {
		return (readShort() & 0xffff);
	}

	/**
	 * Read an integer
	 * 
	 * @return integer
	 */
	public int readInt() {
		verifyRemainingBytes(4);
		int value = ByteBuffer.wrap(bytes, nextByte, 4).order(byteOrder)
				.getInt();
		nextByte += 4;
		return value;
	}

	/**
	 * Read an unsigned int
	 * 
	 * @return unsigned int as long
	 */
	public long readUnsignedInt() {
		return ((long) readInt() & 0xffffffffL);
	}

	/**
	 * Read a float
	 * 
	 * @return float
	 */
	public double readFloat() {
		verifyRemainingBytes(4);
		float value = ByteBuffer.wrap(bytes, nextByte, 4).order(byteOrder)
				.getFloat();
		nextByte += 4;
		return value;
	}

	/**
	 * Read a double
	 * 
	 * @return double
	 */
	public double readDouble() {
		verifyRemainingBytes(8);
		double value = ByteBuffer.wrap(bytes, nextByte, 8).order(byteOrder)
				.getDouble();
		nextByte += 8;
		return value;
	}

	/**
	 * Verify with the remaining bytes that there are enough remaining to read
	 * the provided amount
	 * 
	 * @param bytesToRead
	 *            number of bytes to read
	 */
	private void verifyRemainingBytes(int bytesToRead) {
		if (nextByte + bytesToRead > bytes.length) {
			throw new IllegalStateException(
					"No more remaining bytes to read. Total Bytes: "
							+ bytes.length + ", Bytes already read: "
							+ nextByte + ", Attempted to read: " + bytesToRead);
		}
	}

}
