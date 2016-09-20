package mil.nga.tiff;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import mil.nga.tiff.io.ByteWriter;
import mil.nga.tiff.io.IOUtils;

/**
 * TIFF writer
 * 
 * @author osbornb
 */
public class TiffWriter {

	/**
	 * Write a TIFF to a file
	 * 
	 * @param file
	 *            file to create
	 * @param fileDirectories
	 *            file directories
	 * @throws IOException
	 *             upon failure to write
	 */
	public static void writeTiff(File file, FileDirectories fileDirectories)
			throws IOException {
		ByteWriter writer = new ByteWriter();
		writeTiff(file, writer, fileDirectories);
		writer.close();
	}

	/**
	 * Write a TIFF to a file
	 * 
	 * @param file
	 *            file to create
	 * @param writer
	 *            byte writer
	 * @param fileDirectories
	 *            file directories
	 * @throws IOException
	 *             upon failure to write
	 */
	public static void writeTiff(File file, ByteWriter writer,
			FileDirectories fileDirectories) throws IOException {
		byte[] bytes = writeTiffToBytes(writer, fileDirectories);
		ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
		IOUtils.copyStream(inputStream, file);
	}

	/**
	 * Write a TIFF to bytes
	 * 
	 * @param fileDirectories
	 *            file directories
	 * @return tiff bytes
	 * @throws IOException
	 *             upon failure to write
	 */
	public static byte[] writeTiffToBytes(FileDirectories fileDirectories)
			throws IOException {
		ByteWriter writer = new ByteWriter();
		byte[] bytes = writeTiffToBytes(writer, fileDirectories);
		writer.close();
		return bytes;
	}

	/**
	 * Write a TIFF to bytes
	 * 
	 * @param writer
	 *            byte writer
	 * @param fileDirectories
	 *            file directories
	 * @return tiff bytes
	 * @throws IOException
	 *             upon failure to write
	 */
	public static byte[] writeTiffToBytes(ByteWriter writer,
			FileDirectories fileDirectories) throws IOException {
		writeTiff(writer, fileDirectories);
		byte[] bytes = writer.getBytes();
		return bytes;
	}

	/**
	 * Write a TIFF to a byte writer
	 * 
	 * @param writer
	 *            byte writer
	 * @param fileDirectories
	 *            file directories
	 * @throws IOException
	 *             upon failure to write
	 */
	public static void writeTiff(ByteWriter writer,
			FileDirectories fileDirectories) throws IOException {
		// TODO
	}

}
