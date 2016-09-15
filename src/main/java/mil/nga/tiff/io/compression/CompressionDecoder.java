package mil.nga.tiff.io.compression;

/**
 * Compression decoder interface
 * 
 * @author osbornb
 */
public interface CompressionDecoder {

	/**
	 * Decode the byte block
	 * 
	 * @param block
	 *            block of bytes
	 * @return decoded block of bytes
	 */
	public byte[] decodeBlock(byte[] block);

}
