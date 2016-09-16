package mil.nga.tiff.io.compression;

import java.nio.ByteOrder;

import mil.nga.tiff.util.TiffException;

/**
 * Deflate Compression Decoder
 * 
 * @author osbornb
 */
public class DeflateDecoder implements CompressionDecoder {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public byte[] decodeBlock(byte[] block, ByteOrder byteOrder) {
		throw new TiffException("DeflateDecoder is not yet implemented");
	}

}
