package mil.nga.tiff.io.compression;

import java.nio.ByteOrder;

import mil.nga.tiff.util.TiffException;

/**
 * LZW Compression Decoder
 * 
 * @author osbornb
 */
public class LZWDecoder implements CompressionDecoder {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public byte[] decodeBlock(byte[] block, ByteOrder byteOrder) {
		throw new TiffException("LZWDecoder is not yet implemented");
	}

}
