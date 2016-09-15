package mil.nga.tiff.io.compression;

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
	public byte[] decodeBlock(byte[] block) {
		return block;
	}

}
