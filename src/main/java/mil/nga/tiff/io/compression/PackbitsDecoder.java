package mil.nga.tiff.io.compression;

import mil.nga.tiff.util.TiffException;

/**
 * Packbits Compression Decoder
 * 
 * @author osbornb
 */
public class PackbitsDecoder implements CompressionDecoder {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public byte[] decodeBlock(byte[] block) {
		throw new TiffException("PackbitsDecoder is not yet implemented");
	}

}
