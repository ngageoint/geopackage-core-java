package mil.nga.tiff.io.compression;

import java.nio.ByteOrder;

/**
 * Raw decoder with no compression
 * 
 * @author osbornb
 */
public class RawDecoder implements CompressionDecoder {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public byte[] decodeBlock(byte[] block, ByteOrder byteOrder) {
		return block;
	}

}
