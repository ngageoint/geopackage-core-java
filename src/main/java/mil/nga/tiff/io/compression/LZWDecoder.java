package mil.nga.tiff.io.compression;

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
	public byte[] decodeBlock(byte[] block) {
		throw new TiffException("LZWDecoder is not yet implemented");
	}

}
