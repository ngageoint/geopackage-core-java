package mil.nga.tiff.io.compression;

import java.io.ByteArrayOutputStream;
import java.nio.ByteOrder;

import mil.nga.tiff.io.ByteReader;

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
	public byte[] decodeBlock(byte[] block, ByteOrder byteOrder) {

		ByteReader reader = new ByteReader(block, byteOrder);

		ByteArrayOutputStream decodedStream = new ByteArrayOutputStream();

		while (reader.hasByte()) {
			int header = reader.readByte();
			if (header != -128) {
				if (header < 0) {
					int next = reader.readUnsignedByte();
					header = -header;
					for (int i = 0; i <= header; i++) {
						decodedStream.write(next);
					}
				} else {
					for (int i = 0; i <= header; i++) {
						decodedStream.write(reader.readUnsignedByte());
					}
				}
			}
		}

		byte[] decoded = decodedStream.toByteArray();

		return decoded;
	}

}
